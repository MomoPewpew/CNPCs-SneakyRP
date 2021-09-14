package noppes.npcs.api.wrapper;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.item.IItemArmor;

public class ItemArmorWrapper extends ItemStackWrapper implements IItemArmor {
	protected ItemArmor armor;

	protected ItemArmorWrapper(ItemStack item) {
		super(item);
		this.armor = (ItemArmor) item.getItem();
	}

	public int getType() {
		return 3;
	}

	public int getArmorSlot() {
		return this.armor.getEquipmentSlot().getSlotIndex();
	}

	public String getArmorMaterial() {
		return this.armor.getArmorMaterial().getName();
	}
}
