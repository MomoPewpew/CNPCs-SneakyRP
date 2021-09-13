package noppes.npcs.roles.companion;

import net.minecraft.nbt.NBTTagCompound;

public class CompanionFarmer extends CompanionJobInterface {
     public boolean isStanding = false;

     public NBTTagCompound getNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74757_a("CompanionFarmerStanding", this.isStanding);
          return compound;
     }

     public void setNBT(NBTTagCompound compound) {
          this.isStanding = compound.getBoolean("CompanionFarmerStanding");
     }

     public boolean isSelfSufficient() {
          return this.isStanding;
     }

     public void onUpdate() {
     }
}
