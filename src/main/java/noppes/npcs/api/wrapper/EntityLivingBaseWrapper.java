package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.data.IMark;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.data.MarkData;

public class EntityLivingBaseWrapper extends EntityWrapper implements IEntityLivingBase {
	public EntityLivingBaseWrapper(EntityLivingBase entity) {
		super(entity);
	}

	public float getHealth() {
		return ((EntityLivingBase) this.entity).getHealth();
	}

	public void setHealth(float health) {
		((EntityLivingBase) this.entity).setHealth(health);
	}

	public float getMaxHealth() {
		return ((EntityLivingBase) this.entity).getMaxHealth();
	}

	public void setMaxHealth(float health) {
		if (health >= 0.0F) {
			((EntityLivingBase) this.entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH)
					.setBaseValue((double) health);
		}
	}

	public boolean isAttacking() {
		return ((EntityLivingBase) this.entity).getRevengeTarget() != null;
	}

	public void setAttackTarget(IEntityLivingBase living) {
		if (living == null) {
			((EntityLivingBase) this.entity).setRevengeTarget((EntityLivingBase) null);
		} else {
			((EntityLivingBase) this.entity).setRevengeTarget(living.getMCEntity());
		}

	}

	public IEntityLivingBase getAttackTarget() {
		return (IEntityLivingBase) NpcAPI.Instance().getIEntity(((EntityLivingBase) this.entity).getRevengeTarget());
	}

	public IEntityLivingBase getLastAttacked() {
		return (IEntityLivingBase) NpcAPI.Instance()
				.getIEntity(((EntityLivingBase) this.entity).getLastAttackedEntity());
	}

	public int getLastAttackedTime() {
		return ((EntityLivingBase) this.entity).getLastAttackedEntityTime();
	}

	public boolean canSeeEntity(IEntity entity) {
		return ((EntityLivingBase) this.entity).canEntityBeSeen(entity.getMCEntity());
	}

	public void swingMainhand() {
		((EntityLivingBase) this.entity).swingArm(EnumHand.MAIN_HAND);
	}

	public void swingOffhand() {
		((EntityLivingBase) this.entity).swingArm(EnumHand.OFF_HAND);
	}

	public void addPotionEffect(int effect, int duration, int strength, boolean hideParticles) {
		Potion p = Potion.getPotionById(effect);
		if (p != null) {
			if (strength < 0) {
				strength = 0;
			} else if (strength > 255) {
				strength = 255;
			}

			if (duration < 0) {
				duration = 0;
			} else if (duration > 1000000) {
				duration = 1000000;
			}

			if (!p.isInstant()) {
				duration *= 20;
			}

			if (duration == 0) {
				((EntityLivingBase) this.entity).removePotionEffect(p);
			} else {
				((EntityLivingBase) this.entity)
						.addPotionEffect(new PotionEffect(p, duration, strength, false, hideParticles));
			}

		}
	}

	public void clearPotionEffects() {
		((EntityLivingBase) this.entity).clearActivePotions();
	}

	public int getPotionEffect(int effect) {
		PotionEffect pf = ((EntityLivingBase) this.entity).getActivePotionEffect(Potion.getPotionById(effect));
		return pf == null ? -1 : pf.getAmplifier();
	}

	public IItemStack getMainhandItem() {
		return NpcAPI.Instance().getIItemStack(((EntityLivingBase) this.entity).getHeldItemMainhand());
	}

	public void setMainhandItem(IItemStack item) {
		((EntityLivingBase) this.entity).setHeldItem(EnumHand.MAIN_HAND,
				item == null ? ItemStack.EMPTY : item.getMCItemStack());
	}

	public IItemStack getOffhandItem() {
		return NpcAPI.Instance().getIItemStack(((EntityLivingBase) this.entity).getHeldItemOffhand());
	}

	public void setOffhandItem(IItemStack item) {
		((EntityLivingBase) this.entity).setHeldItem(EnumHand.OFF_HAND,
				item == null ? ItemStack.EMPTY : item.getMCItemStack());
	}

	public IItemStack getArmor(int slot) {
		if (slot >= 0 && slot <= 3) {
			return NpcAPI.Instance()
					.getIItemStack(((EntityLivingBase) this.entity).getItemStackFromSlot(this.getSlot(slot)));
		} else {
			throw new CustomNPCsException("Wrong slot id:" + slot, new Object[0]);
		}
	}

	public void setArmor(int slot, IItemStack item) {
		if (slot >= 0 && slot <= 3) {
			((EntityLivingBase) this.entity).setItemStackToSlot(this.getSlot(slot),
					item == null ? ItemStack.EMPTY : item.getMCItemStack());
		} else {
			throw new CustomNPCsException("Wrong slot id:" + slot, new Object[0]);
		}
	}

	private EntityEquipmentSlot getSlot(int slot) {
		if (slot == 3) {
			return EntityEquipmentSlot.HEAD;
		} else if (slot == 2) {
			return EntityEquipmentSlot.CHEST;
		} else if (slot == 1) {
			return EntityEquipmentSlot.LEGS;
		} else {
			return slot == 0 ? EntityEquipmentSlot.FEET : null;
		}
	}

	public float getRotation() {
		return ((EntityLivingBase) this.entity).renderYawOffset;
	}

	public void setRotation(float rotation) {
		((EntityLivingBase) this.entity).renderYawOffset = rotation;
	}

	public int getType() {
		return 5;
	}

	public boolean typeOf(int type) {
		return type == 5 ? true : super.typeOf(type);
	}

	public boolean isChild() {
		return ((EntityLivingBase) this.entity).isChild();
	}

	public IMark addMark(int type) {
		MarkData data = MarkData.get((EntityLivingBase) this.entity);
		return data.addMark(type);
	}

	public void removeMark(IMark mark) {
		MarkData data = MarkData.get((EntityLivingBase) this.entity);
		data.marks.remove(mark);
		data.syncClients();
	}

	public IMark[] getMarks() {
		MarkData data = MarkData.get((EntityLivingBase) this.entity);
		return (IMark[]) data.marks.toArray(new IMark[data.marks.size()]);
	}

	public float getMoveForward() {
		return ((EntityLivingBase) this.entity).moveForward;
	}

	public void setMoveForward(float move) {
		((EntityLivingBase) this.entity).moveForward = move;
	}

	public float getMoveStrafing() {
		return ((EntityLivingBase) this.entity).moveStrafing;
	}

	public void setMoveStrafing(float move) {
		((EntityLivingBase) this.entity).moveStrafing = move;
	}

	public float getMoveVertical() {
		return ((EntityLivingBase) this.entity).moveVertical;
	}

	public void setMoveVertical(float move) {
		((EntityLivingBase) this.entity).moveVertical = move;
	}

	@Override
	public EntityLivingBase getMCEntity() {
		// TODO Auto-generated method stub
		return null;
	}
}
