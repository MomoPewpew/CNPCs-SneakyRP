package noppes.npcs;

import net.minecraft.item.ItemStack;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.wrapper.ItemStackWrapper;

public class ItemStackEmptyWrapper extends ItemStackWrapper {
	public ItemStackEmptyWrapper() {
		super(ItemStack.EMPTY);
	}

	public IData getTempdata() {
		return null;
	}

	public IData getStoreddata() {
		return null;
	}
}
