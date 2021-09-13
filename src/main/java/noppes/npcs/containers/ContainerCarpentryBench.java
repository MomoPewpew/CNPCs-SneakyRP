package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.data.RecipeCarpentry;

public class ContainerCarpentryBench extends Container {
     public InventoryCrafting craftMatrix = new InventoryCrafting(this, 4, 4);
     public IInventory craftResult = new InventoryCraftResult();
     private EntityPlayer player;
     private World worldObj;
     private BlockPos pos;

     public ContainerCarpentryBench(InventoryPlayer par1InventoryPlayer, World par2World, BlockPos pos) {
          this.worldObj = par2World;
          this.pos = pos;
          this.player = par1InventoryPlayer.field_70458_d;
          this.func_75146_a(new SlotNpcCrafting(par1InventoryPlayer.field_70458_d, this.craftMatrix, this.craftResult, 0, 133, 41));

          int var6;
          int var7;
          for(var6 = 0; var6 < 4; ++var6) {
               for(var7 = 0; var7 < 4; ++var7) {
                    this.func_75146_a(new Slot(this.craftMatrix, var7 + var6 * 4, 17 + var7 * 18, 14 + var6 * 18));
               }
          }

          for(var6 = 0; var6 < 3; ++var6) {
               for(var7 = 0; var7 < 9; ++var7) {
                    this.func_75146_a(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 98 + var6 * 18));
               }
          }

          for(var6 = 0; var6 < 9; ++var6) {
               this.func_75146_a(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 156));
          }

          this.func_75130_a(this.craftMatrix);
     }

     public void func_75130_a(IInventory par1IInventory) {
          if (!this.worldObj.field_72995_K) {
               RecipeCarpentry recipe = RecipeController.instance.findMatchingRecipe(this.craftMatrix);
               ItemStack item = ItemStack.field_190927_a;
               if (recipe != null && recipe.availability.isAvailable(this.player)) {
                    item = recipe.func_77572_b(this.craftMatrix);
               }

               this.craftResult.func_70299_a(0, item);
               EntityPlayerMP plmp = (EntityPlayerMP)this.player;
               plmp.field_71135_a.func_147359_a(new SPacketSetSlot(this.windowId, 0, item));
          }

     }

     public void func_75134_a(EntityPlayer par1EntityPlayer) {
          super.func_75134_a(par1EntityPlayer);
          if (!this.worldObj.field_72995_K) {
               for(int var2 = 0; var2 < 16; ++var2) {
                    ItemStack var3 = this.craftMatrix.func_70304_b(var2);
                    if (var3 != null) {
                         par1EntityPlayer.func_71019_a(var3, false);
                    }
               }
          }

     }

     public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
          return this.worldObj.func_180495_p(this.pos).func_177230_c() == CustomItems.carpentyBench && par1EntityPlayer.func_70092_e((double)this.pos.func_177958_n() + 0.5D, (double)this.pos.func_177956_o() + 0.5D, (double)this.pos.func_177952_p() + 0.5D) <= 64.0D;
     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par1) {
          ItemStack var2 = ItemStack.field_190927_a;
          Slot var3 = (Slot)this.field_75151_b.get(par1);
          if (var3 != null && var3.func_75216_d()) {
               ItemStack var4 = var3.func_75211_c();
               var2 = var4.func_77946_l();
               if (par1 == 0) {
                    if (!this.func_75135_a(var4, 17, 53, true)) {
                         return ItemStack.field_190927_a;
                    }

                    var3.func_75220_a(var4, var2);
               } else if (par1 >= 17 && par1 < 44) {
                    if (!this.func_75135_a(var4, 44, 53, false)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (par1 >= 44 && par1 < 53) {
                    if (!this.func_75135_a(var4, 17, 44, false)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (!this.func_75135_a(var4, 17, 53, false)) {
                    return ItemStack.field_190927_a;
               }

               if (var4.func_190916_E() == 0) {
                    var3.func_75215_d(ItemStack.field_190927_a);
               } else {
                    var3.func_75218_e();
               }

               if (var4.func_190916_E() == var2.func_190916_E()) {
                    return ItemStack.field_190927_a;
               }

               var3.func_190901_a(par1EntityPlayer, var4);
          }

          return var2;
     }

     public boolean func_94530_a(ItemStack stack, Slot slotIn) {
          return slotIn.field_75224_c != this.craftResult && super.func_94530_a(stack, slotIn);
     }
}
