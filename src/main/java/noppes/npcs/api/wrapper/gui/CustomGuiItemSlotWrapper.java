package noppes.npcs.api.wrapper.gui;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.item.IItemStack;

public class CustomGuiItemSlotWrapper extends CustomGuiComponentWrapper implements IItemSlot {
	IItemStack stack;

	public CustomGuiItemSlotWrapper() {
	}

	public CustomGuiItemSlotWrapper(int x, int y) {
		this.setPos(x, y);
	}

	public CustomGuiItemSlotWrapper(int x, int y, IItemStack stack) {
		this(x, y);
		this.setStack(stack);
	}

	public boolean hasStack() {
		return this.stack != null && !this.stack.isEmpty();
	}

	public IItemStack getStack() {
		return this.stack;
	}

	public IItemSlot setStack(IItemStack itemStack) {
		this.stack = itemStack;
		return this;
	}

	public Slot getMCSlot() {
		return null;
	}

	public int getType() {
		return 5;
	}

	public NBTTagCompound toNBT(NBTTagCompound nbt) {
		super.toNBT(nbt);
		if (this.hasStack()) {
			nbt.setTag("stack", this.stack.getItemNbt().getMCNBT());
		}

		return nbt;
	}

	public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
		super.fromNBT(nbt);
		if (nbt.hasKey("stack")) {
			this.setStack(NpcAPI.Instance().getIItemStack(new ItemStack(nbt.getCompoundTag("stack"))));
		}

		return this;
	}
}
