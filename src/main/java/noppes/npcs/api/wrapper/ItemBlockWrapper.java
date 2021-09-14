package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.item.IItemBlock;

public class ItemBlockWrapper extends ItemStackWrapper implements IItemBlock {
	protected String blockName;

	protected ItemBlockWrapper(ItemStack item) {
		super(item);
		Block b = Block.getBlockFromItem(item.getItem());
		this.blockName = Block.REGISTRY.getNameForObject(b) + "";
	}

	public int getType() {
		return 2;
	}

	public String getBlockName() {
		return this.blockName;
	}
}
