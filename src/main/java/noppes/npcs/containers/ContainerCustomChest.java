package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.CustomContainerEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.data.PlayerData;

public class ContainerCustomChest extends ContainerNpcInterface {
     private InventoryBasic craftingMatrix;
     private IContainer container;
     public final int rows;

     public ContainerCustomChest(EntityPlayer player, int rows) {
          super(player);
          this.rows = rows;
          if (!player.world.isRemote) {
               this.container = NpcAPI.Instance().getIContainer((Container)this);
          }

          this.craftingMatrix = new InventoryBasic("crafting", false, rows * 9);

          int j;
          for(j = 0; j < 9; ++j) {
               this.addSlotToContainer(new Slot(player.inventory, j, j * 18 + 8, 89 + rows * 18));
          }

          int k;
          for(j = 0; j < 3; ++j) {
               for(k = 0; k < 9; ++k) {
                    this.addSlotToContainer(new Slot(player.inventory, k + j * 9 + 9, k * 18 + 8, 31 + rows * 18 + j * 18));
               }
          }

          for(j = 0; j < rows; ++j) {
               for(k = 0; k < 9; ++k) {
                    this.addSlotToContainer(new Slot(this.craftingMatrix, k + j * 9, 8 + k * 18, 18 + j * 18));
               }
          }

     }

     public boolean canInteractWith(EntityPlayer playerIn) {
          return true;
     }

     public ItemStack func_184996_a(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
          if (clickType == ClickType.QUICK_MOVE) {
               return ItemStack.EMPTY;
          } else if (slotId < 36) {
               return super.func_184996_a(slotId, dragType, clickType, player);
          } else if (clickType == ClickType.PICKUP && dragType == 0 && player instanceof EntityPlayerMP && this.container != null) {
               Slot slot = (Slot)this.inventorySlots.get(slotId);
               if (slot == null) {
                    return ItemStack.EMPTY;
               } else {
                    PlayerData data = PlayerData.get(player);
                    IItemStack item = NpcAPI.Instance().getIItemStack(slot.getStack());
                    IItemStack heldItem = NpcAPI.Instance().getIItemStack(player.inventory.func_70445_o());
                    CustomContainerEvent.SlotClickedEvent event = new CustomContainerEvent.SlotClickedEvent(data.scriptData.getPlayer(), this.container, slotId, item, heldItem);
                    EventHooks.onCustomChestClicked(event);
                    player.inventory.func_70437_b(event.heldItem == null ? ItemStack.EMPTY : event.heldItem.getMCItemStack());
                    ((EntityPlayerMP)player).func_71113_k();
                    this.putStackInSlot(slotId, event.slotItem == null ? ItemStack.EMPTY : event.slotItem.getMCItemStack());
                    this.detectAndSendChanges();
                    return ItemStack.EMPTY;
               }
          } else {
               return ItemStack.EMPTY;
          }
     }

     public boolean canMergeSlot(ItemStack stack, Slot slotId) {
          return slotId.field_75224_c == this.player.inventory;
     }

     public void onContainerClosed(EntityPlayer player) {
          super.onContainerClosed(player);
          if (!player.world.isRemote) {
               PlayerData data = PlayerData.get(player);
               CustomContainerEvent.CloseEvent event = new CustomContainerEvent.CloseEvent(data.scriptData.getPlayer(), this.container);
               EventHooks.onCustomChestClosed(event);
          }

     }
}
