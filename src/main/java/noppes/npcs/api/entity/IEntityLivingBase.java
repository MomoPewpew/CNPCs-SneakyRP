package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.api.entity.data.IMark;
import noppes.npcs.api.item.IItemStack;

public interface IEntityLivingBase extends IEntity {
	float getHealth();

	void setHealth(float var1);

	float getMaxHealth();

	void setMaxHealth(float var1);

	boolean isAttacking();

	void setAttackTarget(IEntityLivingBase var1);

	IEntityLivingBase getAttackTarget();

	IEntityLivingBase getLastAttacked();

	int getLastAttackedTime();

	boolean canSeeEntity(IEntity var1);

	void swingMainhand();

	void swingOffhand();

	IItemStack getMainhandItem();

	void setMainhandItem(IItemStack var1);

	IItemStack getOffhandItem();

	void setOffhandItem(IItemStack var1);

	IItemStack getArmor(int var1);

	void setArmor(int var1, IItemStack var2);

	void addPotionEffect(int var1, int var2, int var3, boolean var4);

	void clearPotionEffects();

	int getPotionEffect(int var1);

	IMark addMark(int var1);

	void removeMark(IMark var1);

	IMark[] getMarks();

	boolean isChild();

	EntityLivingBase getMCEntity();

	float getMoveForward();

	void setMoveForward(float var1);

	float getMoveStrafing();

	void setMoveStrafing(float var1);

	float getMoveVertical();

	void setMoveVertical(float var1);
}
