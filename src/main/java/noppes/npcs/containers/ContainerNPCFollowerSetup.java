package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class ContainerNPCFollowerSetup extends Container {
     private RoleFollower role;

     public ContainerNPCFollowerSetup(EntityNPCInterface npc, EntityPlayer player) {
          this.role = (RoleFollower)npc.roleInterface;

          int j1;
          for(j1 = 0; j1 < 3; ++j1) {
               this.func_75146_a(new Slot(this.role.inventory, j1, 44, 39 + j1 * 25));
          }

          for(j1 = 0; j1 < 3; ++j1) {
               for(int l1 = 0; l1 < 9; ++l1) {
                    this.func_75146_a(new Slot(player.inventory, l1 + j1 * 9 + 9, 8 + l1 * 18, 113 + j1 * 18));
               }
          }

          for(j1 = 0; j1 < 9; ++j1) {
               this.func_75146_a(new Slot(player.inventory, j1, 8 + j1 * 18, 171));
          }

     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int i) {
          ItemStack itemstack = ItemStack.field_190927_a;
          Slot slot = (Slot)this.field_75151_b.get(i);
          if (slot != null && slot.func_75216_d()) {
               ItemStack itemstack1 = slot.func_75211_c();
               itemstack = itemstack1.func_77946_l();
               if (i >= 0 && i < 3) {
                    if (!this.func_75135_a(itemstack1, 3, 38, true)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (i >= 3 && i < 30) {
                    if (!this.func_75135_a(itemstack1, 30, 38, false)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (i >= 30 && i < 38) {
                    if (!this.func_75135_a(itemstack1, 3, 29, false)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (!this.func_75135_a(itemstack1, 3, 38, false)) {
                    return ItemStack.field_190927_a;
               }

               if (itemstack1.func_190916_E() == 0) {
                    slot.func_75215_d(ItemStack.field_190927_a);
               } else {
                    slot.func_75218_e();
               }

               if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
                    return ItemStack.field_190927_a;
               }

               slot.func_190901_a(par1EntityPlayer, itemstack1);
          }

          return itemstack;
     }

     public boolean func_75145_c(EntityPlayer entityplayer) {
          return true;
     }
}
