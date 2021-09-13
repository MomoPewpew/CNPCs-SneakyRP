package noppes.npcs.containers;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.client.gui.custom.components.CustomGuiSlot;

public class ContainerCustomGui extends Container {
     public CustomGuiWrapper customGui;
     public IInventory guiInventory;
     int slotCount = 0;

     public ContainerCustomGui(IInventory inventory) {
          this.guiInventory = inventory;
     }

     public boolean canInteractWith(EntityPlayer playerIn) {
          return true;
     }

     public void setGui(CustomGuiWrapper gui, EntityPlayer player) {
          this.customGui = gui;
          Iterator var3 = this.customGui.getSlots().iterator();

          while(var3.hasNext()) {
               IItemSlot slot = (IItemSlot)var3.next();
               if (slot.hasStack()) {
                    this.addSlot(slot.getPosX(), slot.getPosY(), slot.getStack().getMCItemStack(), player.world.isRemote);
               } else {
                    this.addSlot(slot.getPosX(), slot.getPosY(), player.world.isRemote);
               }
          }

          if (this.customGui.getShowPlayerInv()) {
               this.addPlayerInventory(player, this.customGui.getPlayerInvX(), this.customGui.getPlayerInvY());
          }

     }

     public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
          ItemStack itemstack = ItemStack.EMPTY;
          Slot slot = (Slot)this.inventorySlots.get(index);
          if (slot != null && slot.getHasStack()) {
               ItemStack itemstack1 = slot.getStack();
               itemstack = itemstack1.copy();
               if (index < this.guiInventory.getSizeInventory()) {
                    if (!this.mergeItemStack(itemstack1, this.guiInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                         return ItemStack.EMPTY;
                    }
               } else if (!this.mergeItemStack(itemstack1, 0, this.guiInventory.getSizeInventory(), false)) {
                    return ItemStack.EMPTY;
               }

               if (itemstack1.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
               } else {
                    slot.onSlotChanged();
               }
          }

          return itemstack;
     }

     void addSlot(int x, int y, boolean clientSide) {
          this.addSlotToContainer(new CustomGuiSlot(this.guiInventory, this.slotCount++, x, y, clientSide));
     }

     void addSlot(int x, int y, ItemStack itemStack, boolean clientSide) {
          int index = this.slotCount++;
          Slot s = new CustomGuiSlot(this.guiInventory, index, x, y, clientSide);
          this.addSlotToContainer(s);
          this.guiInventory.setInventorySlotContents(index, itemStack);
     }

     void addPlayerInventory(EntityPlayer player, int x, int y) {
          int row;
          for(row = 0; row < 3; ++row) {
               for(int col = 0; col < 9; ++col) {
                    this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, x + col * 18, y + row * 18));
               }
          }

          for(row = 0; row < 9; ++row) {
               this.addSlotToContainer(new Slot(player.inventory, row, x + row * 18, y + 58));
          }

     }
}
