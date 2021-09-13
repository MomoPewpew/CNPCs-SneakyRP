package noppes.npcs.containers;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.NoppesUtilServer;

public class ContainerMerchantAdd extends ContainerNpcInterface {
     private IMerchant theMerchant;
     private InventoryBasic merchantInventory;
     private final World world;

     public ContainerMerchantAdd(EntityPlayer player, IMerchant par2IMerchant, World par3World) {
          super(player);
          this.theMerchant = par2IMerchant;
          this.world = par3World;
          this.merchantInventory = new InventoryBasic("", false, 3);
          this.func_75146_a(new Slot(this.merchantInventory, 0, 36, 53));
          this.func_75146_a(new Slot(this.merchantInventory, 1, 62, 53));
          this.func_75146_a(new Slot(this.merchantInventory, 2, 120, 53));

          int i;
          for(i = 0; i < 3; ++i) {
               for(int j = 0; j < 9; ++j) {
                    this.func_75146_a(new Slot(player.field_71071_by, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
               }
          }

          for(i = 0; i < 9; ++i) {
               this.func_75146_a(new Slot(player.field_71071_by, i, 8 + i * 18, 142));
          }

     }

     public void func_75142_b() {
          super.func_75142_b();
     }

     public void func_75130_a(IInventory par1IInventory) {
          super.func_75130_a(par1IInventory);
     }

     public void setCurrentRecipeIndex(int par1) {
     }

     @SideOnly(Side.CLIENT)
     public void func_75137_b(int par1, int par2) {
     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
          ItemStack itemstack = ItemStack.field_190927_a;
          Slot slot = (Slot)this.field_75151_b.get(par2);
          if (slot != null && slot.func_75216_d()) {
               ItemStack itemstack1 = slot.func_75211_c();
               itemstack = itemstack1.func_77946_l();
               if (par2 != 0 && par2 != 1 && par2 != 2) {
                    if (par2 >= 3 && par2 < 30) {
                         if (!this.func_75135_a(itemstack1, 30, 39, false)) {
                              return ItemStack.field_190927_a;
                         }
                    } else if (par2 >= 30 && par2 < 39 && !this.func_75135_a(itemstack1, 3, 30, false)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (!this.func_75135_a(itemstack1, 3, 39, false)) {
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

     public void func_75134_a(EntityPlayer par1EntityPlayer) {
          super.func_75134_a(par1EntityPlayer);
          this.theMerchant.func_70932_a_((EntityPlayer)null);
          super.func_75134_a(par1EntityPlayer);
          if (!this.world.field_72995_K) {
               ItemStack itemstack = this.merchantInventory.func_70304_b(0);
               if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                    par1EntityPlayer.func_71019_a(itemstack, false);
               }

               itemstack = this.merchantInventory.func_70304_b(1);
               if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                    par1EntityPlayer.func_71019_a(itemstack, false);
               }
          }

     }
}
