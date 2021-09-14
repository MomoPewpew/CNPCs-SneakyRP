package noppes.npcs.ai;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAILook extends EntityAIBase {
	private final EntityNPCInterface npc;
	private int idle = 0;
	private double lookX;
	private double lookZ;
	boolean rotatebody;
	private boolean forced = false;
	private Entity forcedEntity = null;

	public EntityAILook(EntityNPCInterface npc) {
		this.npc = npc;
		this.setMutexBits(AiMutex.LOOK);
	}

	public boolean shouldExecute() {
		return !this.npc.isAttacking() && this.npc.getNavigator().noPath() && !this.npc.isPlayerSleeping()
				&& this.npc.isEntityAlive();
	}

	public void startExecuting() {
		this.rotatebody = this.npc.ais.getStandingType() == 0 || this.npc.ais.getStandingType() == 3;
	}

	public void rotate(Entity entity) {
		this.forced = true;
		this.forcedEntity = entity;
	}

	public void rotate(int degrees) {
		this.forced = true;
		this.npc.rotationYawHead = this.npc.rotationYaw = this.npc.renderYawOffset = (float) degrees;
	}

	public void resetTask() {
		this.rotatebody = false;
		this.forced = false;
		this.forcedEntity = null;
	}

	public void updateTask() {
		Entity lookat = null;
		if (this.forced && this.forcedEntity != null) {
			lookat = this.forcedEntity;
		} else if (this.npc.isInteracting()) {
			Iterator ita = this.npc.interactingEntities.iterator();
			double closestDistance = 12.0D;

			while (ita.hasNext()) {
				EntityLivingBase entity = (EntityLivingBase) ita.next();
				double distance = entity.getDistanceSq(this.npc);
				if (distance < closestDistance) {
					closestDistance = entity.getDistanceSq(this.npc);
					lookat = entity;
				} else if (distance > 12.0D) {
					ita.remove();
				}
			}
		} else if (this.npc.ais.getStandingType() == 2) {
			lookat = this.npc.world.getClosestPlayerToEntity(this.npc, 16.0D);
		}

		if (lookat != null) {
			this.npc.getLookHelper().setLookPositionWithEntity((Entity) lookat, 10.0F,
					(float) this.npc.getVerticalFaceSpeed());
		} else {
			if (this.rotatebody) {
				if (this.idle == 0 && this.npc.getRNG().nextFloat() < 0.004F) {
					double var1 = 6.283185307179586D * this.npc.getRNG().nextDouble();
					if (this.npc.ais.getStandingType() == 3) {
						var1 = 0.017453292519943295D * (double) this.npc.ais.orientation + 0.6283185307179586D
								+ 1.8849555921538759D * this.npc.getRNG().nextDouble();
					}

					this.lookX = Math.cos(var1);
					this.lookZ = Math.sin(var1);
					this.idle = 20 + this.npc.getRNG().nextInt(20);
				}

				if (this.idle > 0) {
					--this.idle;
					this.npc.getLookHelper().setLookPosition(this.npc.posX + this.lookX,
							this.npc.posY + (double) this.npc.getEyeHeight(), this.npc.posZ + this.lookZ, 10.0F,
							(float) this.npc.getVerticalFaceSpeed());
				}
			}

			if (this.npc.ais.getStandingType() == 1 && !this.forced) {
				this.npc.rotationYawHead = this.npc.rotationYaw = this.npc.renderYawOffset = (float) this.npc.ais.orientation;
			}

		}
	}
}
