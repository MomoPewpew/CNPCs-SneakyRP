package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.entity.data.INPCAdvanced;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class NPCWrapper extends EntityLivingWrapper implements ICustomNpc {
     public NPCWrapper(EntityNPCInterface npc) {
          super(npc);
     }

     public void setMaxHealth(float health) {
          if ((int)health != ((EntityNPCInterface)this.entity).stats.maxHealth) {
               super.setMaxHealth(health);
               ((EntityNPCInterface)this.entity).stats.maxHealth = (int)health;
               ((EntityNPCInterface)this.entity).updateClient = true;
          }
     }

     public INPCDisplay getDisplay() {
          return ((EntityNPCInterface)this.entity).display;
     }

     public INPCInventory getInventory() {
          return ((EntityNPCInterface)this.entity).inventory;
     }

     public INPCAi getAi() {
          return ((EntityNPCInterface)this.entity).ais;
     }

     public INPCAdvanced getAdvanced() {
          return ((EntityNPCInterface)this.entity).advanced;
     }

     public INPCStats getStats() {
          return ((EntityNPCInterface)this.entity).stats;
     }

     public IFaction getFaction() {
          return ((EntityNPCInterface)this.entity).faction;
     }

     public ITimers getTimers() {
          return ((EntityNPCInterface)this.entity).timers;
     }

     public void setFaction(int id) {
          Faction faction = FactionController.instance.getFaction(id);
          if (faction == null) {
               throw new CustomNPCsException("Unknown faction id: " + id, new Object[0]);
          } else {
               ((EntityNPCInterface)this.entity).setFaction(id);
          }
     }

     public INPCRole getRole() {
          return ((EntityNPCInterface)this.entity).roleInterface;
     }

     public INPCJob getJob() {
          return ((EntityNPCInterface)this.entity).jobInterface;
     }

     public int getHomeX() {
          return ((EntityNPCInterface)this.entity).ais.startPos().func_177958_n();
     }

     public int getHomeY() {
          return ((EntityNPCInterface)this.entity).ais.startPos().func_177956_o();
     }

     public int getHomeZ() {
          return ((EntityNPCInterface)this.entity).ais.startPos().func_177952_p();
     }

     public void setHome(int x, int y, int z) {
          ((EntityNPCInterface)this.entity).ais.setStartPos(new BlockPos(x, y, z));
     }

     public int getOffsetX() {
          return (int)((EntityNPCInterface)this.entity).ais.bodyOffsetX;
     }

     public int getOffsetY() {
          return (int)((EntityNPCInterface)this.entity).ais.bodyOffsetY;
     }

     public int getOffsetZ() {
          return (int)((EntityNPCInterface)this.entity).ais.bodyOffsetZ;
     }

     public void setOffset(int x, int y, int z) {
          ((EntityNPCInterface)this.entity).ais.bodyOffsetX = ValueUtil.correctFloat((float)x, 0.0F, 9.0F);
          ((EntityNPCInterface)this.entity).ais.bodyOffsetY = ValueUtil.correctFloat((float)y, 0.0F, 9.0F);
          ((EntityNPCInterface)this.entity).ais.bodyOffsetZ = ValueUtil.correctFloat((float)z, 0.0F, 9.0F);
          ((EntityNPCInterface)this.entity).updateClient = true;
     }

     public void say(String message) {
          ((EntityNPCInterface)this.entity).saySurrounding(new Line(message));
     }

     public void sayTo(IPlayer player, String message) {
          ((EntityNPCInterface)this.entity).say(player.getMCEntity(), new Line(message));
     }

     public void reset() {
          ((EntityNPCInterface)this.entity).reset();
     }

     public long getAge() {
          return ((EntityNPCInterface)this.entity).totalTicksAlive;
     }

     public IProjectile shootItem(IEntityLivingBase target, IItemStack item, int accuracy) {
          if (item == null) {
               throw new CustomNPCsException("No item was given", new Object[0]);
          } else if (target == null) {
               throw new CustomNPCsException("No target was given", new Object[0]);
          } else {
               accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
               return (IProjectile)NpcAPI.Instance().getIEntity(((EntityNPCInterface)this.entity).shoot(target.getMCEntity(), accuracy, item.getMCItemStack(), false));
          }
     }

     public IProjectile shootItem(double x, double y, double z, IItemStack item, int accuracy) {
          if (item == null) {
               throw new CustomNPCsException("No item was given", new Object[0]);
          } else {
               accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
               return (IProjectile)NpcAPI.Instance().getIEntity(((EntityNPCInterface)this.entity).shoot(x, y, z, accuracy, item.getMCItemStack(), false));
          }
     }

     public void giveItem(IPlayer player, IItemStack item) {
          ((EntityNPCInterface)this.entity).givePlayerItem(player.getMCEntity(), item.getMCItemStack());
     }

     public String executeCommand(String command) {
          if (!((EntityNPCInterface)this.entity).func_184102_h().func_82356_Z()) {
               throw new CustomNPCsException("Command blocks need to be enabled to executeCommands", new Object[0]);
          } else {
               return NoppesUtilServer.runCommand(this.entity, ((EntityNPCInterface)this.entity).func_70005_c_(), command, (EntityPlayer)null);
          }
     }

     public int getType() {
          return 2;
     }

     public String getName() {
          return ((EntityNPCInterface)this.entity).display.getName();
     }

     public void setName(String name) {
          ((EntityNPCInterface)this.entity).display.setName(name);
     }

     public void setRotation(float rotation) {
          super.setRotation(rotation);
          int r = (int)rotation;
          if (((EntityNPCInterface)this.entity).ais.orientation != r) {
               ((EntityNPCInterface)this.entity).ais.orientation = r;
               ((EntityNPCInterface)this.entity).updateClient = true;
          }

     }

     public boolean typeOf(int type) {
          return type == 2 ? true : super.typeOf(type);
     }

     public void setDialog(int slot, IDialog dialog) {
          if (slot >= 0 && slot <= 11) {
               if (dialog == null) {
                    ((EntityNPCInterface)this.entity).dialogs.remove(slot);
               } else {
                    DialogOption option = new DialogOption();
                    option.dialogId = dialog.getId();
                    option.title = dialog.getName();
                    ((EntityNPCInterface)this.entity).dialogs.put(slot, option);
               }

          } else {
               throw new CustomNPCsException("Slot needs to be between 0 and 11", new Object[0]);
          }
     }

     public IDialog getDialog(int slot) {
          if (slot >= 0 && slot <= 11) {
               DialogOption option = (DialogOption)((EntityNPCInterface)this.entity).dialogs.get(slot);
               return option != null && option.hasDialog() ? option.getDialog() : null;
          } else {
               throw new CustomNPCsException("Slot needs to be between 0 and 11", new Object[0]);
          }
     }

     public void updateClient() {
          ((EntityNPCInterface)this.entity).updateClient();
     }

     public IEntityLivingBase getOwner() {
          EntityLivingBase owner = ((EntityNPCInterface)this.entity).getOwner();
          return owner != null ? (IEntityLivingBase)NpcAPI.Instance().getIEntity(owner) : null;
     }
}
