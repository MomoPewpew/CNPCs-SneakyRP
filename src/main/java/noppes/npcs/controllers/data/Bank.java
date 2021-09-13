package noppes.npcs.controllers.data;

import java.util.HashMap;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.NpcMiscInventory;

public class Bank {
     public int id = -1;
     public String name = "";
     public HashMap slotTypes = new HashMap();
     public int startSlots = 1;
     public int maxSlots = 6;
     public NpcMiscInventory currencyInventory = new NpcMiscInventory(6);
     public NpcMiscInventory upgradeInventory = new NpcMiscInventory(6);

     public Bank() {
          for(int i = 0; i < 6; ++i) {
               this.slotTypes.put(i, 0);
          }

     }

     public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.func_74768_a("BankID", this.id);
          nbttagcompound.func_74782_a("BankCurrency", this.currencyInventory.getToNBT());
          nbttagcompound.func_74782_a("BankUpgrade", this.upgradeInventory.getToNBT());
          nbttagcompound.func_74778_a("Username", this.name);
          nbttagcompound.func_74768_a("MaxSlots", this.maxSlots);
          nbttagcompound.func_74768_a("StartSlots", this.startSlots);
          nbttagcompound.func_74782_a("BankTypes", NBTTags.nbtIntegerIntegerMap(this.slotTypes));
     }

     public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
          this.id = nbttagcompound.func_74762_e("BankID");
          this.name = nbttagcompound.func_74779_i("Username");
          this.startSlots = nbttagcompound.func_74762_e("StartSlots");
          this.maxSlots = nbttagcompound.func_74762_e("MaxSlots");
          this.slotTypes = NBTTags.getIntegerIntegerMap(nbttagcompound.func_150295_c("BankTypes", 10));
          this.currencyInventory.setFromNBT(nbttagcompound.func_74775_l("BankCurrency"));
          this.upgradeInventory.setFromNBT(nbttagcompound.func_74775_l("BankUpgrade"));
     }

     public boolean isUpgraded(int slot) {
          return this.slotTypes.get(slot) != null && (Integer)this.slotTypes.get(slot) == 2;
     }

     public boolean canBeUpgraded(int slot) {
          if (this.upgradeInventory.func_70301_a(slot) != null && !this.upgradeInventory.func_70301_a(slot).func_190926_b()) {
               return this.slotTypes.get(slot) == null || (Integer)this.slotTypes.get(slot) == 0;
          } else {
               return false;
          }
     }

     public int getMaxSlots() {
          for(int i = 0; i < this.maxSlots; ++i) {
               if ((this.currencyInventory.func_70301_a(i) == null || this.currencyInventory.func_70301_a(i).func_190926_b()) && i > this.startSlots - 1) {
                    return i;
               }
          }

          return this.maxSlots;
     }
}
