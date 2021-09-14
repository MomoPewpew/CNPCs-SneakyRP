package noppes.npcs.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.constants.AnimationType;
import noppes.npcs.api.constants.EntityType;
import noppes.npcs.api.constants.JobType;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.RoleType;
import noppes.npcs.api.constants.SideType;
import noppes.npcs.api.constants.TacticalType;
import noppes.npcs.api.wrapper.BlockPosWrapper;
import noppes.npcs.constants.EnumScriptType;

public class ScriptContainer {
     private static final String lock = "lock";
     public static ScriptContainer Current;
     private static String CurrentType;
     private static final HashMap Data = new HashMap();
     public String fullscript = "";
     public String script = "";
     public TreeMap console = new TreeMap();
     public boolean errored = false;
     public List scripts = new ArrayList();
     private HashSet unknownFunctions = new HashSet();
     public long lastCreated = 0L;
     private String currentScriptLanguage = null;
     private ScriptEngine engine = null;
     private IScriptHandler handler = null;
     private boolean init = false;
     private static Method luaCoerce;
     private static Method luaCall;

     private static void FillMap(Class c) {
          try {
               Data.put(c.getSimpleName(), c.newInstance());
          } catch (Exception var8) {
          }

          Field[] declaredFields = c.getDeclaredFields();
          Field[] var2 = declaredFields;
          int var3 = declaredFields.length;

          for(int var4 = 0; var4 < var3; ++var4) {
               Field field = var2[var4];

               try {
                    if (Modifier.isStatic(field.getModifiers()) && field.getType() == Integer.TYPE) {
                         Data.put(c.getSimpleName() + "_" + field.getName(), field.getInt((Object)null));
                    }
               } catch (Exception var7) {
               }
          }

     }

     public ScriptContainer(IScriptHandler handler) {
          this.handler = handler;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.script = compound.getString("Script");
          this.console = NBTTags.GetLongStringMap(compound.getTagList("Console", 10));
          this.scripts = NBTTags.getStringList(compound.getTagList("ScriptList", 10));
          this.lastCreated = 0L;
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setString("Script", this.script);
          compound.setTag("Console", NBTTags.NBTLongStringMap(this.console));
          compound.setTag("ScriptList", NBTTags.nbtStringList(this.scripts));
          return compound;
     }

     private String getFullCode() {
          if (!this.init) {
               this.fullscript = this.script;
               if (!this.fullscript.isEmpty()) {
                    this.fullscript = this.fullscript + "\n";
               }

               Iterator var1 = this.scripts.iterator();

               while(var1.hasNext()) {
                    String loc = (String)var1.next();
                    String code = (String)ScriptController.Instance.scripts.get(loc);
                    if (code != null && !code.isEmpty()) {
                         this.fullscript = this.fullscript + code + "\n";
                    }
               }

               this.unknownFunctions = new HashSet();
          }

          return this.fullscript;
     }

     public void run(EnumScriptType type, Event event) {
          this.run((String)type.function, (Object)event);
     }

     public void run(String type, Object event) {
          if (!this.errored && this.hasCode() && !this.unknownFunctions.contains(type) && CustomNpcs.EnableScripting) {
               this.setEngine(this.handler.getLanguage());
               if (this.engine != null) {
                    if (ScriptController.Instance.lastLoaded > this.lastCreated) {
                         this.lastCreated = ScriptController.Instance.lastLoaded;
                         this.init = false;
                    }

                    String var3 = "lock";
                    synchronized("lock") {
                         Current = this;
                         CurrentType = type;
                         StringWriter sw = new StringWriter();
                         PrintWriter pw = new PrintWriter(sw);
                         this.engine.getContext().setWriter(pw);
                         this.engine.getContext().setErrorWriter(pw);

                         try {
                              if (!this.init) {
                                   this.engine.eval(this.getFullCode());
                                   this.init = true;
                              }

                              if (this.engine.getFactory().getLanguageName().equals("lua")) {
                                   Object ob = this.engine.get(type);
                                   if (ob != null) {
                                        if (luaCoerce == null) {
                                             luaCoerce = Class.forName("org.luaj.vm2.lib.jse.CoerceJavaToLua").getMethod("coerce", Object.class);
                                             luaCall = ob.getClass().getMethod("call", Class.forName("org.luaj.vm2.LuaValue"));
                                        }

                                        luaCall.invoke(ob, luaCoerce.invoke((Object)null, event));
                                   } else {
                                        this.unknownFunctions.add(type);
                                   }
                              } else {
                                   ((Invocable)this.engine).invokeFunction(type, event);
                              }
                         } catch (NoSuchMethodException var13) {
                              this.unknownFunctions.add(type);
                         } catch (Throwable var14) {
                              this.errored = true;
                              var14.printStackTrace(pw);
                              NoppesUtilServer.NotifyOPs(this.handler.noticeString() + " script errored");
                         } finally {
                              this.appandConsole(sw.getBuffer().toString().trim());
                              pw.close();
                              Current = null;
                         }

                    }
               }
          }
     }

     public void appandConsole(String message) {
          if (message != null && !message.isEmpty()) {
               long time = System.currentTimeMillis();
               if (this.console.containsKey(time)) {
                    message = (String)this.console.get(time) + "\n" + message;
               }

               this.console.put(time, message);

               while(this.console.size() > 40) {
                    this.console.remove(this.console.firstKey());
               }

          }
     }

     public boolean hasCode() {
          return !this.getFullCode().isEmpty();
     }

     public void setEngine(String scriptLanguage) {
          if (this.currentScriptLanguage == null || !this.currentScriptLanguage.equals(scriptLanguage)) {
               this.engine = ScriptController.Instance.getEngineByName(scriptLanguage);
               if (this.engine == null) {
                    this.errored = true;
               } else {
                    Iterator var2 = Data.entrySet().iterator();

                    while(var2.hasNext()) {
                         Entry entry = (Entry)var2.next();
                         this.engine.put((String)entry.getKey(), entry.getValue());
                    }

                    this.engine.put("dump", new ScriptContainer.Dump());
                    this.engine.put("log", new ScriptContainer.Log());
                    this.currentScriptLanguage = scriptLanguage;
                    this.init = false;
               }
          }
     }

     public boolean isValid() {
          return this.init && !this.errored;
     }

     static {
          FillMap(AnimationType.class);
          FillMap(EntityType.class);
          FillMap(RoleType.class);
          FillMap(JobType.class);
          FillMap(SideType.class);
          FillMap(TacticalType.class);
          FillMap(PotionType.class);
          FillMap(ParticleType.class);
          Data.put("PosZero", new BlockPosWrapper(BlockPos.ORIGIN));
     }

     private class Log implements Function {
          private Log() {
          }

          public Void apply(Object o) {
               ScriptContainer.this.appandConsole(o + "");
               LogWriter.info(o + "");
               return null;
          }

          // $FF: synthetic method
          Log(Object x1) {
               this();
          }
     }

     private class Dump implements Function {
          private Dump() {
          }

          public String apply(Object o) {
               if (o == null) {
                    return "null";
               } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append(o + ":" + NoppesStringUtils.newLine());
                    Field[] var3 = o.getClass().getFields();
                    int var4 = var3.length;

                    int var5;
                    for(var5 = 0; var5 < var4; ++var5) {
                         Field field = var3[var5];

                         try {
                              builder.append(field.getName() + " - " + field.getType().getSimpleName() + ", ");
                         } catch (IllegalArgumentException var12) {
                         }
                    }

                    Method[] var14 = o.getClass().getMethods();
                    var4 = var14.length;

                    for(var5 = 0; var5 < var4; ++var5) {
                         Method method = var14[var5];

                         try {
                              String s = method.getName() + "(";
                              Class[] var8 = method.getParameterTypes();
                              int var9 = var8.length;

                              for(int var10 = 0; var10 < var9; ++var10) {
                                   Class c = var8[var10];
                                   s = s + c.getSimpleName() + ", ";
                              }

                              if (s.endsWith(", ")) {
                                   s = s.substring(0, s.length() - 2);
                              }

                              builder.append(s + "), ");
                         } catch (IllegalArgumentException var13) {
                         }
                    }

                    return builder.toString();
               }
          }

          // $FF: synthetic method
          Dump(Object x1) {
               this();
          }
     }
}
