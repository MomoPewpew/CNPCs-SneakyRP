package noppes.npcs.api.wrapper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntityItem;
import noppes.npcs.api.item.IItemStack;

public class EntityItemWrapper extends EntityWrapper implements IEntityItem {
	public EntityItemWrapper(EntityItem entity) {
		super(entity);
	}

	public String getOwner() {
		return ((EntityItem) this.entity).getOwner();
	}

	public void setOwner(String name) {
		((EntityItem) this.entity).setOwner(name);
	}

	public int getPickupDelay() {
		return (int)ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, (EntityItem)this.entity, "field_145804_b");
	}

	public void setPickupDelay(int delay) {
		((EntityItem) this.entity).setPickupDelay(delay);
	}

	public int getType() {
		return 6;
	}

	public long getAge() {
		return (long) ((EntityItem) this.entity).getAge();
	}

	public void setAge(long age) {
		age = Math.max(Math.min(age, 2147483647L), -2147483648L);
		ObfuscationReflectionHelper.setPrivateValue(EntityItem.class, (EntityItem)this.entity, (int)age, "field_70292_b");
		// ((EntityItem) this.entity).age = (int) age;
	}

	public int getLifeSpawn() {
		return ((EntityItem) this.entity).lifespan;
	}

	public void setLifeSpawn(int age) {
		((EntityItem) this.entity).lifespan = age;
	}

	public IItemStack getItem() {
		return NpcAPI.Instance().getIItemStack(((EntityItem) this.entity).getItem());
	}

	public void setItem(IItemStack item) {
		ItemStack stack = item == null ? ItemStack.EMPTY : item.getMCItemStack();
		((EntityItem) this.entity).setItem(stack);
	}
}
