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
          if (!player.world.field_72995_K) {
               this.container = NpcAPI.Instance().getIContainer((Container)this);
          }

          this.craftingMatrix = new InventoryBasic("crafting", false, rows * 9);

          int j;
          for(j = 0; j < 9; ++j) {
               this.func_75146_a(new Slot(player.inventory, j, j * 18 + 8, 89 + rows * 18));
          }

          int k;
          for(j = 0; j < 3; ++j) {
               for(k = 0; k < 9; ++k) {
                    this.func_75146_a(new Slot(player.inventory, k + j * 9 + 9, k * 18 + 8, 31 + rows * 18 + j * 18));
               }
          }

          for(j = 0; j < rows; ++j) {
               for(k = 0; k < 9; ++k) {
                    this.func_75146_a(new Slot(this.craftingMatrix, k + j * 9, 8 + k * 18, 18 + j * 18));
               }
          }

     }

     public boolean func_75145_c(EntityPlayer playerIn) {
          return true;
     }

     public ItemStack func_184996_a(int slotId, int dragType, ClickType clickType, EntityPlayer player) {
          if (clickType == ClickType.QUICK_MOVE) {
               return ItemStack.field_190927_a;
          } else if (slotId < 36) {
               return super.func_184996_a(slotId, dragType, clickType, player);
          } else if (clickType == ClickType.PICKUP && dragType == 0 && player instanceof EntityPlayerMP && this.container != null) {
               Slot slot = (Slot)this.field_75151_b.get(slotId);
               if (slot == null) {
                    return ItemStack.field_190927_a;
               } else {
                    PlayerData data = PlayerData.get(player);
                    IItemStack item = NpcAPI.Instance().getIItemStack(slot.func_75211_c());
                    IItemStack heldItem = NpcAPI.Instance().getIItemStack(player.inventory.func_70445_o());
                    CustomContainerEvent.SlotClickedEvent event = new CustomContainerEvent.SlotClickedEvent(data.scriptData.getPlayer(), this.container, slotId, item, heldItem);
                    EventHooks.onCustomChestClicked(event);
                    player.inventory.func_70437_b(event.heldItem == null ? ItemStack.field_190927_a : event.heldItem.getMCItemStack());
                    ((EntityPlayerMP)player).func_71113_k();
                    this.func_75141_a(slotId, event.slotItem == null ? ItemStack.field_190927_a : event.slotItem.getMCItemStack());
                    this.func_75142_b();
                    return ItemStack.field_190927_a;
               }
          } else {
               return ItemStack.field_190927_a;
          }
     }

     public boolean func_94530_a(ItemStack stack, Slot slotId) {
          return slotId.field_75224_c == this.player.inventory;
     }

     public void func_75134_a(EntityPlayer player) {
          super.func_75134_a(player);
          if (!player.world.field_72995_K) {
               PlayerData data = PlayerData.get(player);
               CustomContainerEvent.CloseEvent event = new CustomContainerEvent.CloseEvent(data.scriptData.getPlayer(), this.container);
               EventHooks.onCustomChestClosed(event);
          }

     }
}
