package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import noppes.npcs.NoppesUtilServer;

public class SlotNpcCrafting extends SlotCrafting {
     private final InventoryCrafting craftMatrix;

     public SlotNpcCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventory, int slotIndex, int x, int y) {
          super(player, craftingInventory, inventory, slotIndex, x, y);
          this.craftMatrix = craftingInventory;
     }

     public ItemStack onTake(EntityPlayer player, ItemStack itemStack) {
          FMLCommonHandler.instance().firePlayerCraftingEvent(player, itemStack, this.craftMatrix);
          this.func_75208_c(itemStack);

          for(int i = 0; i < this.craftMatrix.getSizeInventory(); ++i) {
               ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
               if (!NoppesUtilServer.IsItemStackNull(itemstack1)) {
                    this.craftMatrix.decrStackSize(i, 1);
                    if (itemstack1.getItem().hasContainerItem(itemstack1)) {
                         ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);
                         if ((NoppesUtilServer.IsItemStackNull(itemstack2) || !itemstack2.func_77984_f() || itemstack2.getItemDamage() <= itemstack2.getMaxDamage()) && !player.inventory.addItemStackToInventory(itemstack2)) {
                              if (NoppesUtilServer.IsItemStackNull(this.craftMatrix.getStackInSlot(i))) {
                                   this.craftMatrix.setInventorySlotContents(i, itemstack2);
                              } else {
                                   player.dropItem(itemstack2, false);
                              }
                         }
                    }
               }
          }

          return itemStack;
     }
}
