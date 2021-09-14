package noppes.npcs.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.constants.AiMutex;

public class EntityAIPanic extends EntityAIBase {
	private EntityCreature entityCreature;
	private float speed;
	private double randPosX;
	private double randPosY;
	private double randPosZ;

	public EntityAIPanic(EntityCreature par1EntityCreature, float par2) {
		this.entityCreature = par1EntityCreature;
		this.speed = par2;
		this.setMutexBits(AiMutex.PASSIVE);
	}

	public boolean shouldExecute() {
		if (this.entityCreature.getAttackTarget() == null && !this.entityCreature.isBurning()) {
			return false;
		} else {
			Vec3d var1 = RandomPositionGenerator.findRandomTarget(this.entityCreature, 5, 4);
			if (var1 == null) {
				return false;
			} else {
				this.randPosX = var1.x;
				this.randPosY = var1.y;
				this.randPosZ = var1.z;
				return true;
			}
		}
	}

	public void startExecuting() {
		this.entityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ,
				(double) this.speed);
	}

	public boolean shouldContinueExecuting() {
		if (this.entityCreature.getAttackTarget() == null) {
			return false;
		} else {
			return !this.entityCreature.getNavigator().noPath();
		}
	}
}
