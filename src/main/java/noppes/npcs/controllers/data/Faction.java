package noppes.npcs.controllers.data;

import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.entity.EntityNPCInterface;

public class Faction implements IFaction {
     public String name = "";
     public int color = Integer.parseInt("FF00", 16);
     public HashSet attackFactions;
     public int id = -1;
     public int neutralPoints = 500;
     public int friendlyPoints = 1500;
     public int defaultPoints = 1000;
     public boolean hideFaction = false;
     public boolean getsAttacked = false;

     public Faction() {
          this.attackFactions = new HashSet();
     }

     public Faction(int id, String name, int color, int defaultPoints) {
          this.name = name;
          this.color = color;
          this.defaultPoints = defaultPoints;
          this.id = id;
          this.attackFactions = new HashSet();
     }

     public static String formatName(String name) {
          name = name.toLowerCase().trim();
          return name.substring(0, 1).toUpperCase() + name.substring(1);
     }

     public void readNBT(NBTTagCompound compound) {
          this.name = compound.func_74779_i("Name");
          this.color = compound.func_74762_e("Color");
          this.id = compound.func_74762_e("Slot");
          this.neutralPoints = compound.func_74762_e("NeutralPoints");
          this.friendlyPoints = compound.func_74762_e("FriendlyPoints");
          this.defaultPoints = compound.func_74762_e("DefaultPoints");
          this.hideFaction = compound.func_74767_n("HideFaction");
          this.getsAttacked = compound.func_74767_n("GetsAttacked");
          this.attackFactions = NBTTags.getIntegerSet(compound.func_150295_c("AttackFactions", 10));
     }

     public NBTTagCompound writeNBT(NBTTagCompound compound) {
          compound.func_74768_a("Slot", this.id);
          compound.func_74778_a("Name", this.name);
          compound.func_74768_a("Color", this.color);
          compound.func_74768_a("NeutralPoints", this.neutralPoints);
          compound.func_74768_a("FriendlyPoints", this.friendlyPoints);
          compound.func_74768_a("DefaultPoints", this.defaultPoints);
          compound.func_74757_a("HideFaction", this.hideFaction);
          compound.func_74757_a("GetsAttacked", this.getsAttacked);
          compound.func_74782_a("AttackFactions", NBTTags.nbtIntegerCollection(this.attackFactions));
          return compound;
     }

     public boolean isFriendlyToPlayer(EntityPlayer player) {
          PlayerFactionData data = PlayerData.get(player).factionData;
          return data.getFactionPoints(player, this.id) >= this.friendlyPoints;
     }

     public boolean isAggressiveToPlayer(EntityPlayer player) {
          if (player.field_71075_bZ.field_75098_d) {
               return false;
          } else {
               PlayerFactionData data = PlayerData.get(player).factionData;
               return data.getFactionPoints(player, this.id) < this.neutralPoints;
          }
     }

     public boolean isNeutralToPlayer(EntityPlayer player) {
          PlayerFactionData data = PlayerData.get(player).factionData;
          int points = data.getFactionPoints(player, this.id);
          return points >= this.neutralPoints && points < this.friendlyPoints;
     }

     public boolean isAggressiveToNpc(EntityNPCInterface entity) {
          return this.attackFactions.contains(entity.faction.id);
     }

     public int getId() {
          return this.id;
     }

     public String getName() {
          return this.name;
     }

     public int getDefaultPoints() {
          return this.defaultPoints;
     }

     public int getColor() {
          return this.color;
     }

     public int playerStatus(IPlayer player) {
          PlayerFactionData data = PlayerData.get(player.getMCEntity()).factionData;
          int points = data.getFactionPoints(player.getMCEntity(), this.id);
          if (points >= this.friendlyPoints) {
               return 1;
          } else {
               return points < this.neutralPoints ? -1 : 0;
          }
     }

     public boolean hostileToNpc(ICustomNpc npc) {
          return this.attackFactions.contains(npc.getFaction().getId());
     }

     public void setDefaultPoints(int points) {
          this.defaultPoints = points;
     }

     public boolean hostileToFaction(int factionId) {
          return this.attackFactions.contains(factionId);
     }

     public int[] getHostileList() {
          int[] a = new int[this.attackFactions.size()];
          int i = 0;

          Integer val;
          for(Iterator var3 = this.attackFactions.iterator(); var3.hasNext(); a[i++] = val) {
               val = (Integer)var3.next();
          }

          return a;
     }

     public void addHostile(int id) {
          if (this.attackFactions.contains(id)) {
               throw new CustomNPCsException("Faction " + this.id + " is already hostile to " + id, new Object[0]);
          } else {
               this.attackFactions.add(id);
          }
     }

     public void removeHostile(int id) {
          this.attackFactions.remove(id);
     }

     public boolean hasHostile(int id) {
          return this.attackFactions.contains(id);
     }

     public boolean getIsHidden() {
          return this.hideFaction;
     }

     public void setIsHidden(boolean bo) {
          this.hideFaction = bo;
     }

     public boolean getAttackedByMobs() {
          return this.getsAttacked;
     }

     public void setAttackedByMobs(boolean bo) {
          this.getsAttacked = bo;
     }

     public void save() {
          FactionController.instance.saveFaction(this);
     }
}
