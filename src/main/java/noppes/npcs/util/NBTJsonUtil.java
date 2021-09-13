package noppes.npcs.util;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagLongArray;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.io.Charsets;

public class NBTJsonUtil {
     public static String Convert(NBTTagCompound compound) {
          List list = new ArrayList();
          NBTJsonUtil.JsonLine line = ReadTag("", compound, list);
          line.removeComma();
          return ConvertList(list);
     }

     public static NBTTagCompound Convert(String json) throws NBTJsonUtil.JsonException {
          json = json.trim();
          NBTJsonUtil.JsonFile file = new NBTJsonUtil.JsonFile(json);
          if (json.startsWith("{") && json.endsWith("}")) {
               NBTTagCompound compound = new NBTTagCompound();
               FillCompound(compound, file);
               return compound;
          } else {
               throw new NBTJsonUtil.JsonException("Not properly incapsulated between { }", file);
          }
     }

     public static void FillCompound(NBTTagCompound compound, NBTJsonUtil.JsonFile json) throws NBTJsonUtil.JsonException {
          if (json.startsWith("{") || json.startsWith(",")) {
               json.cut(1);
          }

          if (!json.startsWith("}")) {
               int index = json.keyIndex();
               if (index < 1) {
                    throw new NBTJsonUtil.JsonException("Expected key after ,", json);
               } else {
                    String key = json.substring(0, index);
                    json.cut(index + 1);
                    NBTBase base = ReadValue(json);
                    if (base == null) {
                         base = new NBTTagString();
                    }

                    if (key.startsWith("\"")) {
                         key = key.substring(1);
                    }

                    if (key.endsWith("\"")) {
                         key = key.substring(0, key.length() - 1);
                    }

                    compound.setTag(key, (NBTBase)base);
                    if (json.startsWith(",")) {
                         FillCompound(compound, json);
                    }

               }
          }
     }

     public static NBTBase ReadValue(NBTJsonUtil.JsonFile json) throws NBTJsonUtil.JsonException {
          if (json.startsWith("{")) {
               NBTTagCompound compound = new NBTTagCompound();
               FillCompound(compound, json);
               if (!json.startsWith("}")) {
                    throw new NBTJsonUtil.JsonException("Expected }", json);
               } else {
                    json.cut(1);
                    return compound;
               }
          } else if (json.startsWith("[")) {
               json.cut(1);
               NBTTagList list = new NBTTagList();
               if (json.startsWith("B;") || json.startsWith("I;") || json.startsWith("L;")) {
                    json.cut(2);
               }

               for(NBTBase value = ReadValue(json); value != null; value = ReadValue(json)) {
                    list.appendTag(value);
                    if (!json.startsWith(",")) {
                         break;
                    }

                    json.cut(1);
               }

               if (!json.startsWith("]")) {
                    throw new NBTJsonUtil.JsonException("Expected ]", json);
               } else {
                    json.cut(1);
                    int i;
                    if (list.func_150303_d() == 3) {
                         int[] arr = new int[list.tagCount()];

                         for(i = 0; list.tagCount() > 0; ++i) {
                              arr[i] = ((NBTTagInt)list.removeTag(0)).getInt();
                         }

                         return new NBTTagIntArray(arr);
                    } else if (list.func_150303_d() == 1) {
                         byte[] arr = new byte[list.tagCount()];

                         for(i = 0; list.tagCount() > 0; ++i) {
                              arr[i] = ((NBTTagByte)list.removeTag(0)).func_150290_f();
                         }

                         return new NBTTagByteArray(arr);
                    } else if (list.func_150303_d() != 4) {
                         return list;
                    } else {
                         long[] arr = new long[list.tagCount()];

                         for(i = 0; list.tagCount() > 0; ++i) {
                              arr[i] = (long)((NBTTagLong)list.removeTag(0)).func_150290_f();
                         }

                         return new NBTTagLongArray(arr);
                    }
               }
          } else {
               String s;
               if (json.startsWith("\"")) {
                    json.cut(1);
                    s = "";

                    String cut;
                    for(boolean ignore = false; !json.startsWith("\"") || ignore; s = s + cut) {
                         cut = json.cutDirty(1);
                         ignore = cut.equals("\\");
                    }

                    json.cut(1);
                    return new NBTTagString(s.replace("\\\\", "\\").replace("\\\"", "\""));
               } else {
                    for(s = ""; !json.startsWith(",", "]", "}"); s = s + json.cut(1)) {
                    }

                    s = s.trim().toLowerCase();
                    if (s.isEmpty()) {
                         return null;
                    } else {
                         try {
                              if (s.endsWith("d")) {
                                   return new NBTTagDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
                              } else if (s.endsWith("f")) {
                                   return new NBTTagFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
                              } else if (s.endsWith("b")) {
                                   return new NBTTagByte(Byte.parseByte(s.substring(0, s.length() - 1)));
                              } else if (s.endsWith("s")) {
                                   return new NBTTagShort(Short.parseShort(s.substring(0, s.length() - 1)));
                              } else if (s.endsWith("l")) {
                                   return new NBTTagLong(Long.parseLong(s.substring(0, s.length() - 1)));
                              } else {
                                   return (NBTBase)(s.contains(".") ? new NBTTagDouble(Double.parseDouble(s)) : new NBTTagInt(Integer.parseInt(s)));
                              }
                         } catch (NumberFormatException var5) {
                              throw new NBTJsonUtil.JsonException("Unable to convert: " + s + " to a number", json);
                         }
                    }
               }
          }
     }

     private static List getListData(NBTTagList list) {
          return (List)ObfuscationReflectionHelper.getPrivateValue(NBTTagList.class, list, 1);
     }

     private static NBTJsonUtil.JsonLine ReadTag(String name, NBTBase base, List list) {
          if (!name.isEmpty()) {
               name = "\"" + name + "\": ";
          }

          NBTJsonUtil.JsonLine line;
          if (base.func_74732_a() == 9) {
               list.add(new NBTJsonUtil.JsonLine(name + "["));
               NBTTagList tags = (NBTTagList)base;
               line = null;
               List data = getListData(tags);

               NBTBase b;
               for(Iterator var6 = data.iterator(); var6.hasNext(); line = ReadTag("", b, list)) {
                    b = (NBTBase)var6.next();
               }

               if (line != null) {
                    line.removeComma();
               }

               list.add(new NBTJsonUtil.JsonLine("]"));
          } else if (base.func_74732_a() == 10) {
               list.add(new NBTJsonUtil.JsonLine(name + "{"));
               NBTTagCompound compound = (NBTTagCompound)base;
               line = null;

               Object key;
               for(Iterator var10 = compound.getKeySet().iterator(); var10.hasNext(); line = ReadTag(key.toString(), compound.getTag(key.toString()), list)) {
                    key = var10.next();
               }

               if (line != null) {
                    line.removeComma();
               }

               list.add(new NBTJsonUtil.JsonLine("}"));
          } else if (base.func_74732_a() == 11) {
               list.add(new NBTJsonUtil.JsonLine(name + base.toString().replaceFirst(",]", "]")));
          } else {
               list.add(new NBTJsonUtil.JsonLine(name + base));
          }

          NBTJsonUtil.JsonLine line = (NBTJsonUtil.JsonLine)list.get(list.size() - 1);
          line.line = line.line + ",";
          return line;
     }

     private static String ConvertList(List list) {
          String json = "";
          int tab = 0;
          Iterator var3 = list.iterator();

          while(var3.hasNext()) {
               NBTJsonUtil.JsonLine tag = (NBTJsonUtil.JsonLine)var3.next();
               if (tag.reduceTab()) {
                    --tab;
               }

               for(int i = 0; i < tab; ++i) {
                    json = json + "    ";
               }

               json = json + tag + "\n";
               if (tag.increaseTab()) {
                    ++tab;
               }
          }

          return json;
     }

     public static NBTTagCompound LoadFile(File file) throws IOException, NBTJsonUtil.JsonException {
          return Convert(Files.toString(file, Charsets.UTF_8));
     }

     public static void SaveFile(File file, NBTTagCompound compound) throws IOException, NBTJsonUtil.JsonException {
          String json = Convert(compound);
          OutputStreamWriter writer = null;

          try {
               writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
               writer.write(json);
          } finally {
               if (writer != null) {
                    writer.close();
               }

          }

     }

     public static void main(String[] args) {
          NBTTagCompound comp = new NBTTagCompound();
          NBTTagCompound comp2 = new NBTTagCompound();
          comp2.func_74773_a("test", new byte[]{0, 0, 1, 1, 0});
          comp.setTag("comp", comp2);
          System.out.println(Convert(comp));
     }

     public static class JsonException extends Exception {
          public JsonException(String message, NBTJsonUtil.JsonFile json) {
               super(message + ": " + json.getCurrentPos());
          }
     }

     static class JsonFile {
          private String original;
          private String text;

          public JsonFile(String text) {
               this.text = text;
               this.original = text;
          }

          public int keyIndex() {
               boolean hasQuote = false;

               for(int i = 0; i < this.text.length(); ++i) {
                    char c = this.text.charAt(i);
                    if (i == 0 && c == '"') {
                         hasQuote = true;
                    } else if (hasQuote && c == '"') {
                         hasQuote = false;
                    }

                    if (!hasQuote && c == ':') {
                         return i;
                    }
               }

               return -1;
          }

          public String cutDirty(int i) {
               String s = this.text.substring(0, i);
               this.text = this.text.substring(i);
               return s;
          }

          public String cut(int i) {
               String s = this.text.substring(0, i);
               this.text = this.text.substring(i).trim();
               return s;
          }

          public String substring(int beginIndex, int endIndex) {
               return this.text.substring(beginIndex, endIndex);
          }

          public int indexOf(String s) {
               return this.text.indexOf(s);
          }

          public String getCurrentPos() {
               int lengthOr = this.original.length();
               int lengthCur = this.text.length();
               int currentPos = lengthOr - lengthCur;
               String done = this.original.substring(0, currentPos);
               String[] lines = done.split("\r\n|\r|\n");
               int pos = 0;
               String line = "";
               if (lines.length > 0) {
                    pos = lines[lines.length - 1].length();
                    line = this.original.split("\r\n|\r|\n")[lines.length - 1].trim();
               }

               return "Line: " + lines.length + ", Pos: " + pos + ", Text: " + line;
          }

          public boolean startsWith(String... ss) {
               String[] var2 = ss;
               int var3 = ss.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                    String s = var2[var4];
                    if (this.text.startsWith(s)) {
                         return true;
                    }
               }

               return false;
          }

          public boolean endsWith(String s) {
               return this.text.endsWith(s);
          }
     }

     static class JsonLine {
          private String line;

          public JsonLine(String line) {
               this.line = line;
          }

          public void removeComma() {
               if (this.line.endsWith(",")) {
                    this.line = this.line.substring(0, this.line.length() - 1);
               }

          }

          public boolean reduceTab() {
               int length = this.line.length();
               return length == 1 && (this.line.endsWith("}") || this.line.endsWith("]")) || length == 2 && (this.line.endsWith("},") || this.line.endsWith("],"));
          }

          public boolean increaseTab() {
               return this.line.endsWith("{") || this.line.endsWith("[");
          }

          public String toString() {
               return this.line;
          }
     }
}
