package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.item.IItemScripted;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.SyncController;
import noppes.npcs.items.ItemScripted;

public class ItemScriptedWrapper extends ItemStackWrapper implements IItemScripted, IScriptHandler {
     public List scripts = new ArrayList();
     public String scriptLanguage = "ECMAScript";
     public boolean enabled = false;
     public long lastInited = -1L;
     public boolean updateClient = false;
     public boolean durabilityShow = true;
     public double durabilityValue = 1.0D;
     public int durabilityColor = -1;
     public int itemColor = -1;
     public int stackSize = 64;
     public boolean loaded = false;

     public ItemScriptedWrapper(ItemStack item) {
          super(item);
     }

     public boolean hasTexture(int damage) {
          return ItemScripted.Resources.containsKey(damage);
     }

     public String getTexture(int damage) {
          return (String)ItemScripted.Resources.get(damage);
     }

     public void setTexture(int damage, String texture) {
          if (damage == 0) {
               throw new CustomNPCsException("Can't set texture for 0", new Object[0]);
          } else {
               String old = (String)ItemScripted.Resources.get(damage);
               if (old != texture && (old == null || texture == null || !old.equals(texture))) {
                    ItemScripted.Resources.put(damage, texture);
                    SyncController.syncScriptItemsEverybody();
               }
          }
     }

     public NBTTagCompound getScriptNBT(NBTTagCompound compound) {
          compound.func_74782_a("Scripts", NBTTags.NBTScript(this.scripts));
          compound.func_74778_a("ScriptLanguage", this.scriptLanguage);
          compound.func_74757_a("ScriptEnabled", this.enabled);
          return compound;
     }

     public NBTTagCompound getMCNbt() {
          NBTTagCompound compound = super.getMCNbt();
          this.getScriptNBT(compound);
          compound.func_74757_a("DurabilityShow", this.durabilityShow);
          compound.func_74780_a("DurabilityValue", this.durabilityValue);
          compound.func_74768_a("DurabilityColor", this.durabilityColor);
          compound.func_74768_a("ItemColor", this.itemColor);
          compound.func_74768_a("MaxStackSize", this.stackSize);
          return compound;
     }

     public void setScriptNBT(NBTTagCompound compound) {
          if (compound.func_74764_b("Scripts")) {
               this.scripts = NBTTags.GetScript(compound.func_150295_c("Scripts", 10), this);
               this.scriptLanguage = compound.func_74779_i("ScriptLanguage");
               this.enabled = compound.func_74767_n("ScriptEnabled");
          }
     }

     public void setMCNbt(NBTTagCompound compound) {
          super.setMCNbt(compound);
          this.setScriptNBT(compound);
          this.durabilityShow = compound.func_74767_n("DurabilityShow");
          this.durabilityValue = compound.func_74769_h("DurabilityValue");
          if (compound.func_74764_b("DurabilityColor")) {
               this.durabilityColor = compound.func_74762_e("DurabilityColor");
          }

          this.itemColor = compound.func_74762_e("ItemColor");
          this.stackSize = compound.func_74762_e("MaxStackSize");
     }

     public int getType() {
          return 6;
     }

     public void runScript(EnumScriptType type, Event event) {
          if (!this.loaded) {
               this.loadScriptData();
               this.loaded = true;
          }

          if (this.isEnabled()) {
               if (ScriptController.Instance.lastLoaded > this.lastInited) {
                    this.lastInited = ScriptController.Instance.lastLoaded;
                    if (type != EnumScriptType.INIT) {
                         EventHooks.onScriptItemInit(this);
                    }
               }

               Iterator var3 = this.scripts.iterator();

               while(var3.hasNext()) {
                    ScriptContainer script = (ScriptContainer)var3.next();
                    script.run(type, event);
               }

          }
     }

     private boolean isEnabled() {
          return this.enabled && ScriptController.HasStart;
     }

     public boolean isClient() {
          return false;
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
          return "ScriptedItem";
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

     public int getMaxStackSize() {
          return this.stackSize;
     }

     public void setMaxStackSize(int size) {
          if (size >= 1 && size <= 64) {
               this.stackSize = size;
          } else {
               throw new CustomNPCsException("Stacksize has to be between 1 and 64", new Object[0]);
          }
     }

     public double getDurabilityValue() {
          return this.durabilityValue;
     }

     public void setDurabilityValue(float value) {
          if ((double)value != this.durabilityValue) {
               this.updateClient = true;
          }

          this.durabilityValue = (double)value;
     }

     public boolean getDurabilityShow() {
          return this.durabilityShow;
     }

     public void setDurabilityShow(boolean bo) {
          if (bo != this.durabilityShow) {
               this.updateClient = true;
          }

          this.durabilityShow = bo;
     }

     public int getDurabilityColor() {
          return this.durabilityColor;
     }

     public void setDurabilityColor(int color) {
          if (color != this.durabilityColor) {
               this.updateClient = true;
          }

          this.durabilityColor = color;
     }

     public int getColor() {
          return this.itemColor;
     }

     public void setColor(int color) {
          if (color != this.itemColor) {
               this.updateClient = true;
          }

          this.itemColor = color;
     }

     public void saveScriptData() {
          NBTTagCompound c = this.item.func_77978_p();
          if (c == null) {
               this.item.func_77982_d(c = new NBTTagCompound());
          }

          c.func_74782_a("ScriptedData", this.getScriptNBT(new NBTTagCompound()));
     }

     public void loadScriptData() {
          NBTTagCompound c = this.item.func_77978_p();
          if (c != null) {
               this.setScriptNBT(c.func_74775_l("ScriptedData"));
          }
     }
}
