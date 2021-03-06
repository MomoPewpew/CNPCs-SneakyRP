package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class JobHealer extends JobInterface {
	private int healTicks = 0;
	public int range = 8;
	public byte type = 2;
	public int speed = 20;
	public HashMap effects = new HashMap();
	private List affected = new ArrayList();

	public JobHealer(EntityNPCInterface npc) {
		super(npc);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("HealerRange", this.range);
		nbttagcompound.setByte("HealerType", this.type);
		nbttagcompound.setTag("BeaconEffects", NBTTags.nbtIntegerIntegerMap(this.effects));
		nbttagcompound.setInteger("HealerSpeed", this.speed);
		return nbttagcompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.range = nbttagcompound.getInteger("HealerRange");
		this.type = nbttagcompound.getByte("HealerType");
		this.effects = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("BeaconEffects", 10));
		this.speed = ValueUtil.CorrectInt(nbttagcompound.getInteger("HealerSpeed"), 10, Integer.MAX_VALUE);
	}

	public boolean aiShouldExecute() {
		++this.healTicks;
		if (this.healTicks < this.speed) {
			return false;
		} else {
			this.healTicks = 0;
			this.affected = this.npc.world.getEntitiesWithinAABB(EntityLivingBase.class, this.npc.getEntityBoundingBox()
					.grow((double) this.range, (double) this.range / 2.0D, (double) this.range));
			return !this.affected.isEmpty();
		}
	}

	public boolean aiContinueExecute() {
		return false;
	}

	public void aiStartExecuting() {
		Iterator var1 = this.affected.iterator();

		while (true) {
			EntityLivingBase entity;
			boolean isEnemy;
			do {
				do {
					do {
						if (!var1.hasNext()) {
							this.affected.clear();
							return;
						}

						entity = (EntityLivingBase) var1.next();
						isEnemy = false;
						if (entity instanceof EntityPlayer) {
							isEnemy = this.npc.faction.isAggressiveToPlayer((EntityPlayer) entity);
						} else if (entity instanceof EntityNPCInterface) {
							isEnemy = this.npc.faction.isAggressiveToNpc((EntityNPCInterface) entity);
						} else {
							isEnemy = entity instanceof EntityMob;
						}
					} while (entity == this.npc);
				} while (this.type == 0 && isEnemy);
			} while (this.type == 1 && !isEnemy);

			Iterator var4 = this.effects.keySet().iterator();

			while (var4.hasNext()) {
				Integer potionEffect = (Integer) var4.next();
				Potion p = Potion.getPotionById(potionEffect);
				if (p != null) {
					entity.addPotionEffect(new PotionEffect(p, 100, (Integer) this.effects.get(potionEffect)));
				}
			}
		}
	}
}
