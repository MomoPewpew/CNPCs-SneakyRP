package noppes.npcs.entity.data;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.EntityNPCInterface;

public class DataScript implements IScriptHandler {
     private List scripts = new ArrayList();
     private String scriptLanguage = "ECMAScript";
     private EntityNPCInterface npc;
     private boolean enabled = false;
     public long lastInited = -1L;

     public DataScript(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.scripts = NBTTags.GetScript(compound.func_150295_c("Scripts", 10), this);
          this.scriptLanguage = compound.func_74779_i("ScriptLanguage");
          this.enabled = compound.func_74767_n("ScriptEnabled");
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.func_74782_a("Scripts", NBTTags.NBTScript(this.scripts));
          compound.func_74778_a("ScriptLanguage", this.scriptLanguage);
          compound.func_74757_a("ScriptEnabled", this.enabled);
          return compound;
     }

     public void runScript(EnumScriptType type, Event event) {
          if (this.isEnabled()) {
               if (ScriptController.Instance.lastLoaded > this.lastInited) {
                    this.lastInited = ScriptController.Instance.lastLoaded;
                    if (type != EnumScriptType.INIT) {
                         EventHooks.onNPCInit(this.npc);
                    }
               }

               Iterator var3 = this.scripts.iterator();

               while(var3.hasNext()) {
                    ScriptContainer script = (ScriptContainer)var3.next();
                    script.run(type, event);
               }

          }
     }

     public boolean isEnabled() {
          return this.enabled && ScriptController.HasStart && !this.npc.world.field_72995_K;
     }

     public boolean isClient() {
          return this.npc.isRemote();
     }

     public boolean getEnabled() {
          return this.enabled;
     }

     public void setEnabled(boolean bo) {
          this.enabled = bo;
     }

     public String getLanguage() {
          return this.scriptLanguage;
     }

     public void setLanguage(String lang) {
          this.scriptLanguage = lang;
     }

     public List getScripts() {
          return this.scripts;
     }

     public String noticeString() {
          BlockPos pos = this.npc.func_180425_c();
          return MoreObjects.toStringHelper(this.npc).add("x", pos.func_177958_n()).add("y", pos.func_177956_o()).add("z", pos.func_177952_p()).toString();
     }

     public Map getConsoleText() {
          Map map = new TreeMap();
          int tab = 0;
          Iterator var3 = this.getScripts().iterator();

          while(var3.hasNext()) {
               ScriptContainer script = (ScriptContainer)var3.next();
               ++tab;
               Iterator var5 = script.console.entrySet().iterator();

               while(var5.hasNext()) {
                    Entry entry = (Entry)var5.next();
                    map.put(entry.getKey(), " tab " + tab + ":\n" + (String)entry.getValue());
               }
          }

          return map;
     }

     public void clearConsole() {
          Iterator var1 = this.getScripts().iterator();

          while(var1.hasNext()) {
               ScriptContainer script = (ScriptContainer)var1.next();
               script.console.clear();
          }

     }
}
