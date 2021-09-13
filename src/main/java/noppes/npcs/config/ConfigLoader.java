package noppes.npcs.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import noppes.npcs.LogWriter;

public class ConfigLoader {
     private boolean updateFile = false;
     private File dir;
     private String fileName;
     private Class configClass;
     private LinkedList configFields;

     public ConfigLoader(Class clss, File dir, String fileName) {
          if (!dir.exists()) {
               dir.mkdir();
          }

          this.dir = dir;
          this.configClass = clss;
          this.configFields = new LinkedList();
          this.fileName = fileName + ".cfg";
          Field[] fields = this.configClass.getDeclaredFields();
          Field[] var5 = fields;
          int var6 = fields.length;

          for(int var7 = 0; var7 < var6; ++var7) {
               Field field = var5[var7];
               if (field.isAnnotationPresent(ConfigProp.class)) {
                    this.configFields.add(field);
               }
          }

     }

     public void loadConfig() {
          try {
               File configFile = new File(this.dir, this.fileName);
               HashMap types = new HashMap();
               Iterator var3 = this.configFields.iterator();

               while(var3.hasNext()) {
                    Field field = (Field)var3.next();
                    ConfigProp prop = (ConfigProp)field.getAnnotation(ConfigProp.class);
                    types.put(!prop.name().isEmpty() ? prop.name() : field.getName(), field);
               }

               if (configFile.exists()) {
                    HashMap properties = this.parseConfig(configFile, types);
                    Iterator var10 = properties.keySet().iterator();

                    String type;
                    while(var10.hasNext()) {
                         type = (String)var10.next();
                         Field field = (Field)types.get(type);
                         Object obj = properties.get(type);
                         if (!obj.equals(field.get((Object)null))) {
                              field.set((Object)null, obj);
                         }
                    }

                    var10 = types.keySet().iterator();

                    while(var10.hasNext()) {
                         type = (String)var10.next();
                         if (!properties.containsKey(type)) {
                              this.updateFile = true;
                         }
                    }
               } else {
                    this.updateFile = true;
               }
          } catch (Exception var8) {
               this.updateFile = true;
               LogWriter.except(var8);
          }

          if (this.updateFile) {
               this.updateConfig();
          }

          this.updateFile = false;
     }

     private HashMap parseConfig(File file, HashMap types) throws Exception {
          HashMap config = new HashMap();
          BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

          while(true) {
               while(true) {
                    String strLine;
                    do {
                         do {
                              if ((strLine = reader.readLine()) == null) {
                                   reader.close();
                                   return config;
                              }
                         } while(strLine.startsWith("#"));
                    } while(strLine.length() == 0);

                    int index = strLine.indexOf("=");
                    if (index > 0 && index != strLine.length()) {
                         String name = strLine.substring(0, index);
                         String prop = strLine.substring(index + 1);
                         if (!types.containsKey(name)) {
                              this.updateFile = true;
                         } else {
                              Object obj = null;
                              Class class2 = ((Field)types.get(name)).getType();
                              if (class2.isAssignableFrom(String.class)) {
                                   obj = prop;
                              } else if (class2.isAssignableFrom(Integer.TYPE)) {
                                   obj = Integer.parseInt(prop);
                              } else if (class2.isAssignableFrom(Short.TYPE)) {
                                   obj = Short.parseShort(prop);
                              } else if (class2.isAssignableFrom(Byte.TYPE)) {
                                   obj = Byte.parseByte(prop);
                              } else if (class2.isAssignableFrom(Boolean.TYPE)) {
                                   obj = Boolean.parseBoolean(prop);
                              } else if (class2.isAssignableFrom(Float.TYPE)) {
                                   obj = Float.parseFloat(prop);
                              } else if (class2.isAssignableFrom(Double.TYPE)) {
                                   obj = Double.parseDouble(prop);
                              }

                              if (obj != null) {
                                   config.put(name, obj);
                              }
                         }
                    } else {
                         this.updateFile = true;
                    }
               }
          }
     }

     public void updateConfig() {
          File file = new File(this.dir, this.fileName);

          try {
               if (!file.exists()) {
                    file.createNewFile();
               }

               BufferedWriter out = new BufferedWriter(new FileWriter(file));
               Iterator var3 = this.configFields.iterator();

               while(var3.hasNext()) {
                    Field field = (Field)var3.next();
                    ConfigProp prop = (ConfigProp)field.getAnnotation(ConfigProp.class);
                    if (prop.info().length() != 0) {
                         out.write("#" + prop.info() + System.getProperty("line.separator"));
                    }

                    String name = !prop.name().isEmpty() ? prop.name() : field.getName();

                    try {
                         out.write(name + "=" + field.get((Object)null).toString() + System.getProperty("line.separator"));
                         out.write(System.getProperty("line.separator"));
                    } catch (IllegalArgumentException var8) {
                         var8.printStackTrace();
                    } catch (IllegalAccessException var9) {
                         var9.printStackTrace();
                    }
               }

               out.close();
          } catch (IOException var10) {
               var10.printStackTrace();
          }

     }
}
