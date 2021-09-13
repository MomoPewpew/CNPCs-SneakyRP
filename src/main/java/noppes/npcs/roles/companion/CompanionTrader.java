package noppes.npcs.roles.companion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;

public class CompanionTrader extends CompanionJobInterface {
     public NBTTagCompound getNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          return compound;
     }

     public void setNBT(NBTTagCompound compound) {
     }

     public void interact(EntityPlayer player) {
          NoppesUtilServer.sendOpenGui(player, EnumGuiType.CompanionTrader, this.npc);
     }
}
