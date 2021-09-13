package noppes.npcs.containers;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;

public class ContainerCustom extends ContainerNpcInterface {
     private InventoryNPC inventory;
     public final int columns;
     public final int rows;

     public ContainerCustom(EntityPlayer player, int columns, int rows) {
          super(player);
          this.columns = columns;
          this.rows = rows;
          this.inventory = new InventoryNPC("currency", columns * rows, this);

          int j;
          int k;
          for(j = 0; j < rows; ++j) {
               for(k = 0; k < columns; ++k) {
                    this.addSlotToContainer(new SlotApi(this.inventory, k + j * 9, 179 + k * 24, 138));
               }
          }

          for(j = 0; j < 3; ++j) {
               for(k = 0; k < 9; ++k) {
                    this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, 28 + k * 18, 175 + j * 18));
               }
          }

          for(j = 0; j < 9; ++j) {
               this.addSlotToContainer(new Slot(player.inventory, j, 28 + j * 18, 230));
          }

     }

     public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
          ItemStack itemstack = ItemStack.EMPTY;
          Slot slot = (Slot)this.inventorySlots.get(par2);
          if (slot != null && slot.getHasStack()) {
               ItemStack itemstack1 = slot.getStack();
               itemstack = itemstack1.copy();
               if (par2 < 4) {
                    if (!this.mergeItemStack(itemstack1, 4, this.inventorySlots.size(), true)) {
                         return ItemStack.EMPTY;
                    }
               } else if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
                    return null;
               }

               if (itemstack1.getCount() == 0) {
                    slot.putStack(ItemStack.EMPTY);
               } else {
                    slot.onSlotChanged();
               }
          }

          return itemstack;
     }

     public void onContainerClosed(EntityPlayer player) {
          super.onContainerClosed(player);
          if (!player.world.isRemote) {
               PlayerMailData data = PlayerData.get(player).mailData;

               PlayerMail var4;
               for(Iterator it = data.playermail.iterator(); it.hasNext(); var4 = (PlayerMail)it.next()) {
               }
          }

     }
}
