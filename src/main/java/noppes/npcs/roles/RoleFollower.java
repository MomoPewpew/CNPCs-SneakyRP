package noppes.npcs.roles;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.role.IRoleFollower;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleFollower extends RoleInterface implements IRoleFollower {
     private String ownerUUID;
     public boolean isFollowing = true;
     public HashMap rates = new HashMap();
     public NpcMiscInventory inventory = new NpcMiscInventory(3);
     public String dialogHire = I18n.func_74838_a("follower.hireText") + " {days} " + I18n.func_74838_a("follower.days");
     public String dialogFarewell = I18n.func_74838_a("follower.farewellText") + " {player}";
     public int daysHired;
     public long hiredTime;
     public boolean disableGui = false;
     public boolean infiniteDays = false;
     public boolean refuseSoulStone = false;
     public EntityPlayer owner = null;

     public RoleFollower(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.setInteger("MercenaryDaysHired", this.daysHired);
          nbttagcompound.setLong("MercenaryHiredTime", this.hiredTime);
          nbttagcompound.setString("MercenaryDialogHired", this.dialogHire);
          nbttagcompound.setString("MercenaryDialogFarewell", this.dialogFarewell);
          if (this.hasOwner()) {
               nbttagcompound.setString("MercenaryOwner", this.ownerUUID);
          }

          nbttagcompound.setTag("MercenaryDayRates", NBTTags.nbtIntegerIntegerMap(this.rates));
          nbttagcompound.setTag("MercenaryInv", this.inventory.getToNBT());
          nbttagcompound.setBoolean("MercenaryIsFollowing", this.isFollowing);
          nbttagcompound.setBoolean("MercenaryDisableGui", this.disableGui);
          nbttagcompound.setBoolean("MercenaryInfiniteDays", this.infiniteDays);
          nbttagcompound.setBoolean("MercenaryRefuseSoulstone", this.refuseSoulStone);
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.ownerUUID = nbttagcompound.getString("MercenaryOwner");
          this.daysHired = nbttagcompound.getInteger("MercenaryDaysHired");
          this.hiredTime = nbttagcompound.getLong("MercenaryHiredTime");
          this.dialogHire = nbttagcompound.getString("MercenaryDialogHired");
          this.dialogFarewell = nbttagcompound.getString("MercenaryDialogFarewell");
          this.rates = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("MercenaryDayRates", 10));
          this.inventory.setFromNBT(nbttagcompound.getCompoundTag("MercenaryInv"));
          this.isFollowing = nbttagcompound.getBoolean("MercenaryIsFollowing");
          this.disableGui = nbttagcompound.getBoolean("MercenaryDisableGui");
          this.infiniteDays = nbttagcompound.getBoolean("MercenaryInfiniteDays");
          this.refuseSoulStone = nbttagcompound.getBoolean("MercenaryRefuseSoulstone");
     }

     public boolean aiShouldExecute() {
          this.owner = this.getOwner();
          if (!this.infiniteDays && this.owner != null && this.getDays() <= 0) {
               RoleEvent.FollowerFinishedEvent event = new RoleEvent.FollowerFinishedEvent(this.owner, this.npc.wrappedNPC);
               EventHooks.onNPCRole(this.npc, event);
               this.owner.func_145747_a(new TextComponentTranslation(NoppesStringUtils.formatText(this.dialogFarewell, this.owner, this.npc), new Object[0]));
               this.killed();
          }

          return false;
     }

     public EntityPlayer getOwner() {
          if (this.ownerUUID != null && !this.ownerUUID.isEmpty()) {
               try {
                    UUID uuid = UUID.fromString(this.ownerUUID);
                    if (uuid != null) {
                         return this.npc.world.func_152378_a(uuid);
                    }
               } catch (IllegalArgumentException var2) {
               }

               return this.npc.world.func_72924_a(this.ownerUUID);
          } else {
               return null;
          }
     }

     public boolean hasOwner() {
          if (!this.infiniteDays && this.daysHired <= 0) {
               return false;
          } else {
               return this.ownerUUID != null && !this.ownerUUID.isEmpty();
          }
     }

     public void killed() {
          this.ownerUUID = null;
          this.daysHired = 0;
          this.hiredTime = 0L;
          this.isFollowing = true;
     }

     public void reset() {
          this.killed();
     }

     public void interact(EntityPlayer player) {
          if (this.ownerUUID != null && !this.ownerUUID.isEmpty()) {
               if (player == this.owner && !this.disableGui) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollower, this.npc);
               }
          } else {
               this.npc.say(player, this.npc.advanced.getInteractLine());
               NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollowerHire, this.npc);
          }

     }

     public boolean defendOwner() {
          return this.isFollowing() && this.npc.advanced.job == 3;
     }

     public void delete() {
     }

     public boolean isFollowing() {
          return this.owner != null && this.isFollowing && this.getDays() > 0;
     }

     public void setOwner(EntityPlayer player) {
          UUID id = player.func_110124_au();
          if (this.ownerUUID == null || id == null || !this.ownerUUID.equals(id.toString())) {
               this.killed();
          }

          this.ownerUUID = id.toString();
     }

     public int getDays() {
          if (this.infiniteDays) {
               return 100;
          } else if (this.daysHired <= 0) {
               return 0;
          } else {
               int days = (int)((this.npc.world.func_82737_E() - this.hiredTime) / 24000L);
               return this.daysHired - days;
          }
     }

     public void addDays(int days) {
          this.daysHired = days + this.getDays();
          this.hiredTime = this.npc.world.func_82737_E();
     }

     public boolean getInfinite() {
          return this.infiniteDays;
     }

     public void setInfinite(boolean infinite) {
          this.infiniteDays = infinite;
     }

     public boolean getGuiDisabled() {
          return this.disableGui;
     }

     public void setGuiDisabled(boolean disabled) {
          this.disableGui = disabled;
     }

     public boolean getRefuseSoulstone() {
          return this.refuseSoulStone;
     }

     public void setRefuseSoulstone(boolean refuse) {
          this.refuseSoulStone = refuse;
     }

     public IPlayer getFollowing() {
          EntityPlayer owner = this.getOwner();
          return owner != null ? (IPlayer)NpcAPI.Instance().getIEntity(owner) : null;
     }

     public void setFollowing(IPlayer player) {
          if (player == null) {
               this.setOwner((EntityPlayer)null);
          } else {
               this.setOwner(player.getMCEntity());
          }

     }
}
