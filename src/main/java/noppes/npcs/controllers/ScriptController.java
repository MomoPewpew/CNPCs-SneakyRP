package noppes.npcs.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.api.wrapper.WorldWrapper;
import noppes.npcs.controllers.data.ForgeScriptData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.util.NBTJsonUtil;

public class ScriptController {
     public static ScriptController Instance;
     public static boolean HasStart = false;
     private ScriptEngineManager manager;
     public Map languages = new HashMap();
     public Map factories = new HashMap();
     public Map scripts = new HashMap();
     public PlayerScriptData playerScripts = new PlayerScriptData((EntityPlayer)null);
     public ForgeScriptData forgeScripts = new ForgeScriptData();
     public long lastLoaded = 0L;
     public long lastPlayerUpdate = 0L;
     public File dir;
     public NBTTagCompound compound = new NBTTagCompound();
     private boolean loaded = false;
     public boolean shouldSave = false;

     public ScriptController() {
          this.loaded = false;
          Instance = this;
          if (!CustomNpcs.NashorArguments.isEmpty()) {
               System.setProperty("nashorn.args", CustomNpcs.NashorArguments);
          }

          this.manager = new ScriptEngineManager();

          Class c;
          ScriptEngineFactory fac;
          try {
               if (this.manager.getEngineByName("ecmascript") == null) {
                    Launch.classLoader.addClassLoaderExclusion("jdk.nashorn.");
                    Launch.classLoader.addClassLoaderExclusion("jdk.internal.dynalink");
                    c = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");
                    fac = (ScriptEngineFactory)c.newInstance();
                    fac.getScriptEngine();
                    this.manager.registerEngineName("ecmascript", fac);
                    this.manager.registerEngineExtension("js", fac);
                    this.manager.registerEngineMimeType("application/ecmascript", fac);
                    this.languages.put(fac.getLanguageName(), ".js");
                    this.factories.put(fac.getLanguageName().toLowerCase(), fac);
               }
          } catch (Throwable var5) {
          }

          try {
               c = Class.forName("org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory");
               fac = (ScriptEngineFactory)c.newInstance();
               fac.getScriptEngine();
               this.manager.registerEngineName("kotlin", fac);
               this.manager.registerEngineExtension("ktl", fac);
               this.manager.registerEngineMimeType("application/kotlin", fac);
               this.languages.put(fac.getLanguageName(), ".ktl");
               this.factories.put(fac.getLanguageName().toLowerCase(), fac);
          } catch (Throwable var4) {
          }

          LogWriter.info("Script Engines Available:");
          Iterator var7 = this.manager.getEngineFactories().iterator();

          while(var7.hasNext()) {
               fac = (ScriptEngineFactory)var7.next();

               try {
                    if (!fac.getExtensions().isEmpty() && (fac.getScriptEngine() instanceof Invocable || fac.getLanguageName().equals("lua"))) {
                         String ext = "." + ((String)fac.getExtensions().get(0)).toLowerCase();
                         LogWriter.info(fac.getLanguageName() + ": " + ext);
                         this.languages.put(fac.getLanguageName(), ext);
                         this.factories.put(fac.getLanguageName().toLowerCase(), fac);
                    }
               } catch (Throwable var6) {
               }
          }

     }

     public void loadCategories() {
          this.dir = new File(CustomNpcs.getWorldSaveDirectory(), "scripts");
          if (!this.dir.exists()) {
               this.dir.mkdirs();
          }

          if (!this.worldDataFile().exists()) {
               this.shouldSave = true;
          }

          WorldWrapper.tempData.clear();
          this.scripts.clear();
          Iterator var1 = this.languages.keySet().iterator();

          while(var1.hasNext()) {
               String language = (String)var1.next();
               String ext = (String)this.languages.get(language);
               File scriptDir = new File(this.dir, language.toLowerCase());
               if (!scriptDir.exists()) {
                    scriptDir.mkdir();
               } else {
                    this.loadDir(scriptDir, "", ext);
               }
          }

          this.lastLoaded = System.currentTimeMillis();
     }

     private void loadDir(File dir, String name, String ext) {
          File[] var4 = dir.listFiles();
          int var5 = var4.length;

          for(int var6 = 0; var6 < var5; ++var6) {
               File file = var4[var6];
               String filename = name + file.getName().toLowerCase();
               if (file.isDirectory()) {
                    this.loadDir(file, filename + "/", ext);
               } else if (filename.endsWith(ext)) {
                    try {
                         this.scripts.put(filename, this.readFile(file));
                    } catch (IOException var10) {
                         var10.printStackTrace();
                    }
               }
          }

     }

     public boolean loadStoredData() {
          this.compound = new NBTTagCompound();
          File file = this.worldDataFile();

          try {
               if (!file.exists()) {
                    return false;
               } else {
                    this.compound = NBTJsonUtil.LoadFile(file);
                    this.shouldSave = false;
                    return true;
               }
          } catch (Exception var3) {
               LogWriter.error("Error loading: " + file.getAbsolutePath(), var3);
               return false;
          }
     }

     private File worldDataFile() {
          return new File(this.dir, "world_data.json");
     }

     private File playerScriptsFile() {
          return new File(this.dir, "player_scripts.json");
     }

     private File forgeScriptsFile() {
          return new File(this.dir, "forge_scripts.json");
     }

     public boolean loadPlayerScripts() {
          this.playerScripts.clear();
          File file = this.playerScriptsFile();

          try {
               if (!file.exists()) {
                    return false;
               } else {
                    this.playerScripts.readFromNBT(NBTJsonUtil.LoadFile(file));
                    return true;
               }
          } catch (Exception var3) {
               LogWriter.error("Error loading: " + file.getAbsolutePath(), var3);
               return false;
          }
     }

     public void setPlayerScripts(NBTTagCompound compound) {
          this.playerScripts.readFromNBT(compound);
          File file = this.playerScriptsFile();

          try {
               NBTJsonUtil.SaveFile(file, compound);
               this.lastPlayerUpdate = System.currentTimeMillis();
          } catch (IOException var4) {
               var4.printStackTrace();
          } catch (NBTJsonUtil.JsonException var5) {
               var5.printStackTrace();
          }

     }

     public boolean loadForgeScripts() {
          this.forgeScripts.clear();
          File file = this.forgeScriptsFile();

          try {
               if (!file.exists()) {
                    return false;
               } else {
                    this.forgeScripts.readFromNBT(NBTJsonUtil.LoadFile(file));
                    return true;
               }
          } catch (Exception var3) {
               LogWriter.error("Error loading: " + file.getAbsolutePath(), var3);
               return false;
          }
     }

     public void setForgeScripts(NBTTagCompound compound) {
          this.forgeScripts.readFromNBT(compound);
          File file = this.forgeScriptsFile();

          try {
               NBTJsonUtil.SaveFile(file, compound);
               this.forgeScripts.lastInited = -1L;
          } catch (IOException var4) {
               var4.printStackTrace();
          } catch (NBTJsonUtil.JsonException var5) {
               var5.printStackTrace();
          }

     }

     private String readFile(File file) throws IOException {
          BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

          try {
               StringBuilder sb = new StringBuilder();

               for(String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                    sb.append("\n");
               }

               String var5 = sb.toString();
               return var5;
          } finally {
               br.close();
          }
     }

     public ScriptEngine getEngineByName(String language) {
          ScriptEngineFactory fac = (ScriptEngineFactory)this.factories.get(language.toLowerCase());
          return fac == null ? null : fac.getScriptEngine();
     }

     public NBTTagList nbtLanguages() {
          NBTTagList list = new NBTTagList();
          Iterator var2 = this.languages.keySet().iterator();

          while(var2.hasNext()) {
               String language = (String)var2.next();
               NBTTagCompound compound = new NBTTagCompound();
               NBTTagList scripts = new NBTTagList();
               Iterator var6 = this.getScripts(language).iterator();

               while(var6.hasNext()) {
                    String script = (String)var6.next();
                    scripts.appendTag(new NBTTagString(script));
               }

               compound.setTag("Scripts", scripts);
               compound.setString("Language", language);
               list.appendTag(compound);
          }

          return list;
     }

     private List getScripts(String language) {
          List list = new ArrayList();
          String ext = (String)this.languages.get(language);
          if (ext == null) {
               return list;
          } else {
               Iterator var4 = this.scripts.keySet().iterator();

               while(var4.hasNext()) {
                    String script = (String)var4.next();
                    if (script.endsWith(ext)) {
                         list.add(script);
                    }
               }

               return list;
          }
     }

     @SubscribeEvent
     public void saveWorld(Save event) {
          if (this.shouldSave && !event.getWorld().field_72995_K && event.getWorld() == event.getWorld().func_73046_m().field_71305_c[0]) {
               try {
                    NBTJsonUtil.SaveFile(this.worldDataFile(), this.compound.func_74737_b());
               } catch (Exception var3) {
                    LogWriter.except(var3);
               }

               this.shouldSave = false;
          }
     }
}
