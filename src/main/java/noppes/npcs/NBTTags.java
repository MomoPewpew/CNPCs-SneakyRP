package noppes.npcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;

public class NBTTags {
     public static void getItemStackList(NBTTagList tagList, NonNullList items) {
          items.clear();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);

               try {
                    items.set(nbttagcompound.func_74771_c("Slot") & 255, new ItemStack(nbttagcompound));
               } catch (ClassCastException var5) {
                    items.set(nbttagcompound.func_74762_e("Slot"), new ItemStack(nbttagcompound));
               }
          }

     }

     public static Map getIItemStackMap(NBTTagList tagList) {
          Map list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               ItemStack item = new ItemStack(nbttagcompound);
               if (!item.func_190926_b()) {
                    try {
                         list.put(nbttagcompound.func_74771_c("Slot") & 255, NpcAPI.Instance().getIItemStack(item));
                    } catch (ClassCastException var6) {
                         list.put(nbttagcompound.func_74762_e("Slot"), NpcAPI.Instance().getIItemStack(item));
                    }
               }
          }

          return list;
     }

     public static ItemStack[] getItemStackArray(NBTTagList tagList) {
          ItemStack[] list = new ItemStack[tagList.tagCount()];

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list[nbttagcompound.func_74771_c("Slot") & 255] = new ItemStack(nbttagcompound);
          }

          return list;
     }

     public static NonNullList getIngredientList(NBTTagList tagList) {
          NonNullList list = NonNullList.func_191196_a();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.add(nbttagcompound.func_74771_c("Slot") & 255, Ingredient.func_193369_a(new ItemStack[]{new ItemStack(nbttagcompound)}));
          }

          return list;
     }

     public static ArrayList getIntegerArraySet(NBTTagList tagList) {
          ArrayList set = new ArrayList();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound compound = tagList.getCompoundTagAt(i);
               set.add(compound.func_74759_k("Array"));
          }

          return set;
     }

     public static HashMap getBooleanList(NBTTagList tagList) {
          HashMap list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.func_74762_e("Slot"), nbttagcompound.getBoolean("Boolean"));
          }

          return list;
     }

     public static HashMap getIntegerIntegerMap(NBTTagList tagList) {
          HashMap list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.func_74762_e("Slot"), nbttagcompound.func_74762_e("Integer"));
          }

          return list;
     }

     public static HashMap getIntegerLongMap(NBTTagList tagList) {
          HashMap list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.func_74762_e("Slot"), nbttagcompound.func_74763_f("Long"));
          }

          return list;
     }

     public static HashSet getIntegerSet(NBTTagList tagList) {
          HashSet list = new HashSet();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.add(nbttagcompound.func_74762_e("Integer"));
          }

          return list;
     }

     public static List getIntegerList(NBTTagList tagList) {
          List list = new ArrayList();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.add(nbttagcompound.func_74762_e("Integer"));
          }

          return list;
     }

     public static HashMap getStringStringMap(NBTTagList tagList) {
          HashMap list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.getString("Slot"), nbttagcompound.getString("Value"));
          }

          return list;
     }

     public static HashMap getIntegerStringMap(NBTTagList tagList) {
          HashMap list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.func_74762_e("Slot"), nbttagcompound.getString("Value"));
          }

          return list;
     }

     public static HashMap getStringIntegerMap(NBTTagList tagList) {
          HashMap list = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.getString("Slot"), nbttagcompound.func_74762_e("Value"));
          }

          return list;
     }

     public static HashMap getVectorMap(NBTTagList tagList) {
          HashMap map = new HashMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               Vector values = new Vector();
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               NBTTagList list = nbttagcompound.getTagList("Values", 10);

               for(int j = 0; j < list.tagCount(); ++j) {
                    NBTTagCompound value = list.getCompoundTagAt(j);
                    values.add(value.getString("Value"));
               }

               map.put(nbttagcompound.getString("Key"), values);
          }

          return map;
     }

     public static List getStringList(NBTTagList tagList) {
          List list = new ArrayList();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               String line = nbttagcompound.getString("Line");
               list.add(line);
          }

          return list;
     }

     public static String[] getStringArray(NBTTagList tagList, int size) {
          String[] arr = new String[size];

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               String line = nbttagcompound.getString("Value");
               int slot = nbttagcompound.func_74762_e("Slot");
               arr[slot] = line;
          }

          return arr;
     }

     public static NBTTagList nbtIntegerArraySet(List set) {
          NBTTagList nbttaglist = new NBTTagList();
          if (set == null) {
               return nbttaglist;
          } else {
               Iterator var2 = set.iterator();

               while(var2.hasNext()) {
                    int[] arr = (int[])var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setIntArray("Array", arr);
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtItemStackList(NonNullList inventory) {
          NBTTagList nbttaglist = new NBTTagList();

          for(int slot = 0; slot < inventory.size(); ++slot) {
               ItemStack item = (ItemStack)inventory.get(slot);
               if (!item.func_190926_b()) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.func_74774_a("Slot", (byte)slot);
                    item.func_77955_b(nbttagcompound);
                    nbttaglist.appendTag(nbttagcompound);
               }
          }

          return nbttaglist;
     }

     public static NBTTagList nbtIItemStackMap(Map inventory) {
          NBTTagList nbttaglist = new NBTTagList();
          if (inventory == null) {
               return nbttaglist;
          } else {
               Iterator var2 = inventory.keySet().iterator();

               while(var2.hasNext()) {
                    int slot = (Integer)var2.next();
                    IItemStack item = (IItemStack)inventory.get(slot);
                    if (item != null) {
                         NBTTagCompound nbttagcompound = new NBTTagCompound();
                         nbttagcompound.func_74774_a("Slot", (byte)slot);
                         item.getMCItemStack().func_77955_b(nbttagcompound);
                         nbttaglist.appendTag(nbttagcompound);
                    }
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtItemStackArray(ItemStack[] inventory) {
          NBTTagList nbttaglist = new NBTTagList();
          if (inventory == null) {
               return nbttaglist;
          } else {
               for(int slot = 0; slot < inventory.length; ++slot) {
                    ItemStack item = inventory[slot];
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.func_74774_a("Slot", (byte)slot);
                    if (item != null) {
                         item.func_77955_b(nbttagcompound);
                    }

                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtIngredientList(NonNullList inventory) {
          NBTTagList nbttaglist = new NBTTagList();
          if (inventory == null) {
               return nbttaglist;
          } else {
               for(int slot = 0; slot < inventory.size(); ++slot) {
                    Ingredient ingredient = (Ingredient)inventory.get(slot);
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.func_74774_a("Slot", (byte)slot);
                    if (ingredient != null && ingredient.func_193365_a().length > 0) {
                         ingredient.func_193365_a()[0].func_77955_b(nbttagcompound);
                    }

                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtBooleanList(HashMap updatedSlots) {
          NBTTagList nbttaglist = new NBTTagList();
          if (updatedSlots == null) {
               return nbttaglist;
          } else {
               HashMap inventory2 = updatedSlots;
               Iterator var3 = updatedSlots.keySet().iterator();

               while(var3.hasNext()) {
                    Integer slot = (Integer)var3.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setInteger("Slot", slot);
                    nbttagcompound.func_74757_a("Boolean", (Boolean)inventory2.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtIntegerIntegerMap(Map lines) {
          NBTTagList nbttaglist = new NBTTagList();
          if (lines == null) {
               return nbttaglist;
          } else {
               Iterator var2 = lines.keySet().iterator();

               while(var2.hasNext()) {
                    int slot = (Integer)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setInteger("Slot", slot);
                    nbttagcompound.setInteger("Integer", (Integer)lines.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtIntegerLongMap(HashMap lines) {
          NBTTagList nbttaglist = new NBTTagList();
          if (lines == null) {
               return nbttaglist;
          } else {
               Iterator var2 = lines.keySet().iterator();

               while(var2.hasNext()) {
                    int slot = (Integer)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setInteger("Slot", slot);
                    nbttagcompound.func_74772_a("Long", (Long)lines.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtIntegerCollection(Collection set) {
          NBTTagList nbttaglist = new NBTTagList();
          if (set == null) {
               return nbttaglist;
          } else {
               Iterator var2 = set.iterator();

               while(var2.hasNext()) {
                    int slot = (Integer)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setInteger("Integer", slot);
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtVectorMap(HashMap map) {
          NBTTagList list = new NBTTagList();
          if (map == null) {
               return list;
          } else {
               Iterator var2 = map.keySet().iterator();

               while(var2.hasNext()) {
                    String key = (String)var2.next();
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setString("Key", key);
                    NBTTagList values = new NBTTagList();
                    Iterator var6 = ((Vector)map.get(key)).iterator();

                    while(var6.hasNext()) {
                         String value = (String)var6.next();
                         NBTTagCompound comp = new NBTTagCompound();
                         comp.setString("Value", value);
                         values.appendTag(comp);
                    }

                    compound.setTag("Values", values);
                    list.appendTag(compound);
               }

               return list;
          }
     }

     public static NBTTagList nbtStringStringMap(HashMap map) {
          NBTTagList nbttaglist = new NBTTagList();
          if (map == null) {
               return nbttaglist;
          } else {
               Iterator var2 = map.keySet().iterator();

               while(var2.hasNext()) {
                    String slot = (String)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setString("Slot", slot);
                    nbttagcompound.setString("Value", (String)map.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtStringIntegerMap(Map map) {
          NBTTagList nbttaglist = new NBTTagList();
          if (map == null) {
               return nbttaglist;
          } else {
               Iterator var2 = map.keySet().iterator();

               while(var2.hasNext()) {
                    String slot = (String)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setString("Slot", slot);
                    nbttagcompound.setInteger("Value", (Integer)map.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTBase nbtIntegerStringMap(Map map) {
          NBTTagList nbttaglist = new NBTTagList();
          if (map == null) {
               return nbttaglist;
          } else {
               Iterator var2 = map.keySet().iterator();

               while(var2.hasNext()) {
                    int slot = (Integer)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setInteger("Slot", slot);
                    nbttagcompound.setString("Value", (String)map.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtStringArray(String[] list) {
          NBTTagList nbttaglist = new NBTTagList();
          if (list == null) {
               return nbttaglist;
          } else {
               for(int i = 0; i < list.length; ++i) {
                    if (list[i] != null) {
                         NBTTagCompound nbttagcompound = new NBTTagCompound();
                         nbttagcompound.setString("Value", list[i]);
                         nbttagcompound.setInteger("Slot", i);
                         nbttaglist.appendTag(nbttagcompound);
                    }
               }

               return nbttaglist;
          }
     }

     public static NBTTagList nbtStringList(List list) {
          NBTTagList nbttaglist = new NBTTagList();
          Iterator var2 = list.iterator();

          while(var2.hasNext()) {
               String s = (String)var2.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.setString("Line", s);
               nbttaglist.appendTag(nbttagcompound);
          }

          return nbttaglist;
     }

     public static NBTTagList nbtDoubleList(double... par1ArrayOfDouble) {
          NBTTagList nbttaglist = new NBTTagList();
          double[] adouble = par1ArrayOfDouble;
          int i = par1ArrayOfDouble.length;

          for(int j = 0; j < i; ++j) {
               double d1 = adouble[j];
               nbttaglist.appendTag(new NBTTagDouble(d1));
          }

          return nbttaglist;
     }

     public static NBTTagCompound NBTMerge(NBTTagCompound data, NBTTagCompound merge) {
          NBTTagCompound compound = data.func_74737_b();
          Set names = merge.getKeySet();

          String name;
          Object base;
          for(Iterator var4 = names.iterator(); var4.hasNext(); compound.setTag(name, (NBTBase)base)) {
               name = (String)var4.next();
               base = merge.getTag(name);
               if (((NBTBase)base).func_74732_a() == 10) {
                    base = NBTMerge(compound.getCompoundTag(name), (NBTTagCompound)base);
               }
          }

          return compound;
     }

     public static List GetScript(NBTTagList list, IScriptHandler handler) {
          List scripts = new ArrayList();

          for(int i = 0; i < list.tagCount(); ++i) {
               NBTTagCompound compoundd = list.getCompoundTagAt(i);
               ScriptContainer script = new ScriptContainer(handler);
               script.readFromNBT(compoundd);
               scripts.add(script);
          }

          return scripts;
     }

     public static NBTTagList NBTScript(List scripts) {
          NBTTagList list = new NBTTagList();
          Iterator var2 = scripts.iterator();

          while(var2.hasNext()) {
               ScriptContainer script = (ScriptContainer)var2.next();
               NBTTagCompound compound = new NBTTagCompound();
               script.writeToNBT(compound);
               list.appendTag(compound);
          }

          return list;
     }

     public static TreeMap GetLongStringMap(NBTTagList tagList) {
          TreeMap list = new TreeMap();

          for(int i = 0; i < tagList.tagCount(); ++i) {
               NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
               list.put(nbttagcompound.func_74763_f("Long"), nbttagcompound.getString("String"));
          }

          return list;
     }

     public static NBTTagList NBTLongStringMap(Map map) {
          NBTTagList nbttaglist = new NBTTagList();
          if (map == null) {
               return nbttaglist;
          } else {
               Iterator var2 = map.keySet().iterator();

               while(var2.hasNext()) {
                    long slot = (Long)var2.next();
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.func_74772_a("Long", slot);
                    nbttagcompound.setString("String", (String)map.get(slot));
                    nbttaglist.appendTag(nbttagcompound);
               }

               return nbttaglist;
          }
     }
}
