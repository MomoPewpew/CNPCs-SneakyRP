package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;

class SlotNpcTraderItems extends Slot {
	public SlotNpcTraderItems(IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	public ItemStack onTake(EntityPlayer player, ItemStack itemstack) {
		if (!NoppesUtilServer.IsItemStackNull(itemstack) && !NoppesUtilServer.IsItemStackNull(this.getStack())) {
			if (itemstack.getItem() != this.getStack().getItem()) {
				return itemstack;
			} else {
				itemstack.shrink(1);
				return itemstack;
			}
		} else {
			return itemstack;
		}
	}

	public int getSlotStackLimit() {
		return 64;
	}

	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}
}
