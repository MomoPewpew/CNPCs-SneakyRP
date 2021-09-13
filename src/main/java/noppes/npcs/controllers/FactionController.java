package noppes.npcs.controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.LogWriter;
import noppes.npcs.Server;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.data.Faction;

public class FactionController implements IFactionHandler {
     public HashMap factionsSync = new HashMap();
     public HashMap factions = new HashMap();
     public static FactionController instance = new FactionController();
     private int lastUsedID = 0;

     public FactionController() {
          instance = this;
          this.factions.put(0, new Faction(0, "Friendly", 56576, 2000));
          this.factions.put(1, new Faction(1, "Neutral", 15916288, 1000));
          this.factions.put(2, new Faction(2, "Aggressive", 14483456, 0));
     }

     public void load() {
          this.factions = new HashMap();
          this.lastUsedID = 0;

          try {
               File saveDir = CustomNpcs.getWorldSaveDirectory();
               if (saveDir != null) {
                    try {
                         File file = new File(saveDir, "factions.dat");
                         if (file.exists()) {
                              this.loadFactionsFile(file);
                              return;
                         }
                    } catch (Exception var9) {
                         try {
                              File file = new File(saveDir, "factions.dat_old");
                              if (file.exists()) {
                                   this.loadFactionsFile(file);
                                   return;
                              }
                         } catch (Exception var8) {
                         }

                         return;
                    }

                    return;
               }
          } finally {
               EventHooks.onGlobalFactionsLoaded(this);
               if (this.factions.isEmpty()) {
                    this.factions.put(0, new Faction(0, "Friendly", 56576, 2000));
                    this.factions.put(1, new Faction(1, "Neutral", 15916288, 1000));
                    this.factions.put(2, new Faction(2, "Aggressive", 14483456, 0));
               }

          }

     }

     private void loadFactionsFile(File file) throws IOException {
          DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
          this.loadFactions(var1);
          var1.close();
     }

     public void loadFactions(DataInputStream stream) throws IOException {
          HashMap factions = new HashMap();
          NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_74794_a(stream);
          this.lastUsedID = nbttagcompound1.func_74762_e("lastID");
          NBTTagList list = nbttagcompound1.func_150295_c("NPCFactions", 10);
          if (list != null) {
               for(int i = 0; i < list.func_74745_c(); ++i) {
                    NBTTagCompound nbttagcompound = list.func_150305_b(i);
                    Faction faction = new Faction();
                    faction.readNBT(nbttagcompound);
                    factions.put(faction.id, faction);
               }
          }

          this.factions = factions;
     }

     public NBTTagCompound getNBT() {
          NBTTagList list = new NBTTagList();
          Iterator var2 = this.factions.keySet().iterator();

          while(var2.hasNext()) {
               int slot = (Integer)var2.next();
               Faction faction = (Faction)this.factions.get(slot);
               NBTTagCompound nbtfactions = new NBTTagCompound();
               faction.writeNBT(nbtfactions);
               list.func_74742_a(nbtfactions);
          }

          NBTTagCompound nbttagcompound = new NBTTagCompound();
          nbttagcompound.func_74768_a("lastID", this.lastUsedID);
          nbttagcompound.func_74782_a("NPCFactions", list);
          return nbttagcompound;
     }

     public void saveFactions() {
          try {
               File saveDir = CustomNpcs.getWorldSaveDirectory();
               File file = new File(saveDir, "factions.dat_new");
               File file1 = new File(saveDir, "factions.dat_old");
               File file2 = new File(saveDir, "factions.dat");
               CompressedStreamTools.func_74799_a(this.getNBT(), new FileOutputStream(file));
               if (file1.exists()) {
                    file1.delete();
               }

               file2.renameTo(file1);
               if (file2.exists()) {
                    file2.delete();
               }

               file.renameTo(file2);
               if (file.exists()) {
                    file.delete();
               }
          } catch (Exception var5) {
               LogWriter.except(var5);
          }

     }

     public Faction getFaction(int faction) {
          return (Faction)this.factions.get(faction);
     }

     public void saveFaction(Faction faction) {
          if (faction.id < 0) {
               for(faction.id = this.getUnusedId(); this.hasName(faction.name); faction.name = faction.name + "_") {
               }
          } else {
               Faction existing = (Faction)this.factions.get(faction.id);
               if (existing != null && !existing.name.equals(faction.name)) {
                    while(this.hasName(faction.name)) {
                         faction.name = faction.name + "_";
                    }
               }
          }

          this.factions.remove(faction.id);
          this.factions.put(faction.id, faction);
          Server.sendToAll(CustomNpcs.Server, EnumPacketClient.SYNC_UPDATE, 1, faction.writeNBT(new NBTTagCompound()));
          this.saveFactions();
     }

     public int getUnusedId() {
          if (this.lastUsedID == 0) {
               Iterator var1 = this.factions.keySet().iterator();

               while(var1.hasNext()) {
                    int catid = (Integer)var1.next();
                    if (catid > this.lastUsedID) {
                         this.lastUsedID = catid;
                    }
               }
          }

          ++this.lastUsedID;
          return this.lastUsedID;
     }

     public IFaction delete(int id) {
          if (id >= 0 && this.factions.size() > 1) {
               Faction faction = (Faction)this.factions.remove(id);
               if (faction == null) {
                    return null;
               } else {
                    this.saveFactions();
                    faction.id = -1;
                    Server.sendToAll(CustomNpcs.Server, EnumPacketClient.SYNC_REMOVE, 1, id);
                    return faction;
               }
          } else {
               return null;
          }
     }

     public int getFirstFactionId() {
          return (Integer)this.factions.keySet().iterator().next();
     }

     public Faction getFirstFaction() {
          return (Faction)this.factions.values().iterator().next();
     }

     public boolean hasName(String newName) {
          if (newName.trim().isEmpty()) {
               return true;
          } else {
               Iterator var2 = this.factions.values().iterator();

               Faction faction;
               do {
                    if (!var2.hasNext()) {
                         return false;
                    }

                    faction = (Faction)var2.next();
               } while(!faction.name.equals(newName));

               return true;
          }
     }

     public Faction getFactionFromName(String factioname) {
          Iterator var2 = this.factions.entrySet().iterator();

          Entry entryfaction;
          do {
               if (!var2.hasNext()) {
                    return null;
               }

               entryfaction = (Entry)var2.next();
          } while(!((Faction)entryfaction.getValue()).name.equalsIgnoreCase(factioname));

          return (Faction)entryfaction.getValue();
     }

     public String[] getNames() {
          String[] names = new String[this.factions.size()];
          int i = 0;

          for(Iterator var3 = this.factions.values().iterator(); var3.hasNext(); ++i) {
               Faction faction = (Faction)var3.next();
               names[i] = faction.name.toLowerCase();
          }

          return names;
     }

     public List list() {
          return new ArrayList(this.factions.values());
     }

     public IFaction create(String name, int color) {
          Faction faction;
          for(faction = new Faction(); this.hasName(name); name = name + "_") {
          }

          faction.name = name;
          faction.color = color;
          this.saveFaction(faction);
          return faction;
     }

     public IFaction get(int id) {
          return (IFaction)this.factions.get(id);
     }
}
