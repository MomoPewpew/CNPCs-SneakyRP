package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.controllers.data.Bank;

public class ContainerManageBanks extends Container {
     public Bank bank = new Bank();

     public ContainerManageBanks(EntityPlayer player) {
          int j1;
          byte y;
          int y;
          for(j1 = 0; j1 < 6; ++j1) {
               int x = 36;
               y = 38;
               y = y + j1 * 22;
               this.func_75146_a(new Slot(this.bank.currencyInventory, j1, x, y));
          }

          for(j1 = 0; j1 < 6; ++j1) {
               int x = 142;
               y = 38;
               y = y + j1 * 22;
               this.func_75146_a(new Slot(this.bank.upgradeInventory, j1, x, y));
          }

          for(j1 = 0; j1 < 9; ++j1) {
               this.func_75146_a(new Slot(player.inventory, j1, 8 + j1 * 18, 171));
          }

     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int i) {
          return ItemStack.field_190927_a;
     }

     public boolean func_75145_c(EntityPlayer entityplayer) {
          return true;
     }

     public void setBank(Bank bank2) {
          for(int i = 0; i < 6; ++i) {
               this.bank.currencyInventory.func_70299_a(i, bank2.currencyInventory.func_70301_a(i));
               this.bank.upgradeInventory.func_70299_a(i, bank2.upgradeInventory.func_70301_a(i));
          }

     }
}
