package noppes.npcs.api.wrapper;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;

public class ContainerWrapper implements IContainer {
	private IInventory inventory;
	private Container container;

	public ContainerWrapper(IInventory inventory) {
		this.inventory = inventory;
	}

	public ContainerWrapper(Container container) {
		this.container = container;
	}

	public int getSize() {
		return this.inventory != null ? this.inventory.getSizeInventory() : this.container.inventorySlots.size();
	}

	public IItemStack getSlot(int slot) {
		if (slot >= 0 && slot < this.getSize()) {
			return this.inventory != null ? NpcAPI.Instance().getIItemStack(this.inventory.getStackInSlot(slot))
					: NpcAPI.Instance().getIItemStack(this.container.getSlot(slot).getStack());
		} else {
			throw new CustomNPCsException("Slot is out of range " + slot, new Object[0]);
		}
	}

	public void setSlot(int slot, IItemStack item) {
		if (slot >= 0 && slot < this.getSize()) {
			ItemStack itemstack = item == null ? ItemStack.EMPTY : item.getMCItemStack();
			if (this.inventory != null) {
				this.inventory.setInventorySlotContents(slot, itemstack);
			} else {
				this.container.putStackInSlot(slot, itemstack);
				this.container.detectAndSendChanges();
			}

		} else {
			throw new CustomNPCsException("Slot is out of range " + slot, new Object[0]);
		}
	}

	public int count(IItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
		int count = 0;

		for (int i = 0; i < this.getSize(); ++i) {
			IItemStack toCompare = this.getSlot(i);
			if (NoppesUtilPlayer.compareItems(item.getMCItemStack(), toCompare.getMCItemStack(), ignoreDamage,
					ignoreNBT)) {
				count += toCompare.getStackSize();
			}
		}

		return count;
	}

	public IInventory getMCInventory() {
		return this.inventory;
	}

	public Container getMCContainer() {
		return this.container;
	}

	public IItemStack[] getItems() {
		IItemStack[] items = new IItemStack[this.getSize()];

		for (int i = 0; i < this.getSize(); ++i) {
			items[i] = this.getSlot(i);
		}

		return items;
	}
}
