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

     public boolean func_75145_c(EntityPlayer playerIn) {
          return true;
     }

     public void setGui(CustomGuiWrapper gui, EntityPlayer player) {
          this.customGui = gui;
          Iterator var3 = this.customGui.getSlots().iterator();

          while(var3.hasNext()) {
               IItemSlot slot = (IItemSlot)var3.next();
               if (slot.hasStack()) {
                    this.addSlot(slot.getPosX(), slot.getPosY(), slot.getStack().getMCItemStack(), player.field_70170_p.field_72995_K);
               } else {
                    this.addSlot(slot.getPosX(), slot.getPosY(), player.field_70170_p.field_72995_K);
               }
          }

          if (this.customGui.getShowPlayerInv()) {
               this.addPlayerInventory(player, this.customGui.getPlayerInvX(), this.customGui.getPlayerInvY());
          }

     }

     public ItemStack func_82846_b(EntityPlayer playerIn, int index) {
          ItemStack itemstack = ItemStack.field_190927_a;
          Slot slot = (Slot)this.field_75151_b.get(index);
          if (slot != null && slot.func_75216_d()) {
               ItemStack itemstack1 = slot.func_75211_c();
               itemstack = itemstack1.func_77946_l();
               if (index < this.guiInventory.func_70302_i_()) {
                    if (!this.func_75135_a(itemstack1, this.guiInventory.func_70302_i_(), this.field_75151_b.size(), true)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (!this.func_75135_a(itemstack1, 0, this.guiInventory.func_70302_i_(), false)) {
                    return ItemStack.field_190927_a;
               }

               if (itemstack1.func_190926_b()) {
                    slot.func_75215_d(ItemStack.field_190927_a);
               } else {
                    slot.func_75218_e();
               }
          }

          return itemstack;
     }

     void addSlot(int x, int y, boolean clientSide) {
          this.func_75146_a(new CustomGuiSlot(this.guiInventory, this.slotCount++, x, y, clientSide));
     }

     void addSlot(int x, int y, ItemStack itemStack, boolean clientSide) {
          int index = this.slotCount++;
          Slot s = new CustomGuiSlot(this.guiInventory, index, x, y, clientSide);
          this.func_75146_a(s);
          this.guiInventory.func_70299_a(index, itemStack);
     }

     void addPlayerInventory(EntityPlayer player, int x, int y) {
          int row;
          for(row = 0; row < 3; ++row) {
               for(int col = 0; col < 9; ++col) {
                    this.func_75146_a(new Slot(player.field_71071_by, col + row * 9 + 9, x + col * 18, y + row * 18));
               }
          }

          for(row = 0; row < 9; ++row) {
               this.func_75146_a(new Slot(player.field_71071_by, row, x + row * 18, y + 58));
          }

     }
}
