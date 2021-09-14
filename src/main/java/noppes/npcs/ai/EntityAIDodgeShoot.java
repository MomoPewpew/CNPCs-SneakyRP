package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIDodgeShoot extends EntityAIBase {
	private EntityNPCInterface entity;
	private double x;
	private double y;
	private double zPosition;

	public EntityAIDodgeShoot(EntityNPCInterface iNpc) {
		this.entity = iNpc;
		this.setMutexBits(AiMutex.PASSIVE);
	}

	public boolean shouldExecute() {
		EntityLivingBase var1 = this.entity.getAttackTarget();
		if (var1 != null && var1.isEntityAlive()) {
			if (this.entity.inventory.getProjectile() == null) {
				return false;
			} else if (this.entity.getRangedTask() == null) {
				return false;
			} else {
				Vec3d vec = this.entity.getRangedTask().hasFired()
						? RandomPositionGenerator.findRandomTarget(this.entity, 4, 1)
						: null;
				if (vec == null) {
					return false;
				} else {
					this.x = vec.x;
					this.y = vec.y;
					this.zPosition = vec.z;
					return true;
				}
			}
		} else {
			return false;
		}
	}

	public boolean shouldContinueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.zPosition, 1.2D);
	}

	public void updateTask() {
		if (this.entity.getAttackTarget() != null) {
			this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 30.0F, 30.0F);
		}

	}
}
