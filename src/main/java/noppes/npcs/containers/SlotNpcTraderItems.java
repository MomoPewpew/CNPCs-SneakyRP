package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;

class SlotNpcTraderItems extends Slot {
     public SlotNpcTraderItems(IInventory iinventory, int i, int j, int k) {
          super(iinventory, i, j, k);
     }

     public ItemStack func_190901_a(EntityPlayer player, ItemStack itemstack) {
          if (!NoppesUtilServer.IsItemStackNull(itemstack) && !NoppesUtilServer.IsItemStackNull(this.func_75211_c())) {
               if (itemstack.func_77973_b() != this.func_75211_c().func_77973_b()) {
                    return itemstack;
               } else {
                    itemstack.func_190918_g(1);
                    return itemstack;
               }
          } else {
               return itemstack;
          }
     }

     public int func_75219_a() {
          return 64;
     }

     public boolean func_75214_a(ItemStack itemstack) {
          return false;
     }
}
