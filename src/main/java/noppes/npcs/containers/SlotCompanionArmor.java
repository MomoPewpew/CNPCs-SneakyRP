package noppes.npcs.containers;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.roles.RoleCompanion;

class SlotCompanionArmor extends Slot {
	final EntityEquipmentSlot armorType;
	final RoleCompanion role;

	public SlotCompanionArmor(RoleCompanion role, IInventory iinventory, int id, int x, int y,
			EntityEquipmentSlot type) {
		super(iinventory, id, x, y);
		this.armorType = type;
		this.role = role;
	}

	public int getSlotStackLimit() {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public String getSlotTexture() {
		return ItemArmor.EMPTY_SLOT_NAMES[this.armorType.getIndex()];
	}

	public boolean isItemValid(ItemStack itemstack) {
		if (itemstack.getItem() instanceof ItemArmor && this.role.canWearArmor(itemstack)) {
			return ((ItemArmor) itemstack.getItem()).armorType == this.armorType;
		} else if (itemstack.getItem() instanceof ItemBlock) {
			return this.armorType == EntityEquipmentSlot.HEAD;
		} else {
			return false;
		}
	}
}
