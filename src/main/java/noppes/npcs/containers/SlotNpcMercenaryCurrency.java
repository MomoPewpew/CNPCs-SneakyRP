package noppes.npcs.containers;

import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.roles.RoleFollower;

class SlotNpcMercenaryCurrency extends Slot {
     RoleFollower role;

     public SlotNpcMercenaryCurrency(RoleFollower role, IInventory inv, int i, int j, int k) {
          super(inv, i, j, k);
          this.role = role;
     }

     public int func_75219_a() {
          return 64;
     }

     public boolean func_75214_a(ItemStack itemstack) {
          Item item = itemstack.func_77973_b();
          Iterator var3 = this.role.inventory.items.iterator();

          ItemStack is;
          do {
               do {
                    if (!var3.hasNext()) {
                         return false;
                    }

                    is = (ItemStack)var3.next();
               } while(item != is.func_77973_b());
          } while(itemstack.func_77981_g() && itemstack.func_77952_i() != is.func_77952_i());

          return true;
     }
}
