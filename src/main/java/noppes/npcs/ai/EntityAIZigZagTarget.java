package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIZigZagTarget extends EntityAIBase {
	private EntityNPCInterface npc;
	private EntityLivingBase targetEntity;
	private Path pathentity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private int entityPosX;
	private int entityPosY;
	private int entityPosZ;
	private double speed;
	private int ticks;

	public EntityAIZigZagTarget(EntityNPCInterface par1EntityCreature, double par2) {
		this.npc = par1EntityCreature;
		this.speed = par2;
		this.ticks = 0;
		this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
	}

	public boolean shouldExecute() {
		this.targetEntity = this.npc.getAttackTarget();
		if (this.targetEntity != null && this.targetEntity.isEntityAlive()) {
			return !this.npc.isInRange(this.targetEntity, (double) this.npc.ais.getTacticalRange());
		} else {
			return false;
		}
	}

	public void resetTask() {
		this.npc.getNavigator().clearPath();
		this.ticks = 0;
	}

	public void updateTask() {
		this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
		if (this.ticks-- <= 0) {
			Path pathentity = this.npc.getNavigator().getResourcePathToEntityLiving(this.targetEntity);
			if (pathentity != null && pathentity.getCurrentPathLength() >= this.npc.ais.getTacticalRange()) {
				PathPoint pathpoint = pathentity.getResourcePathPointFromIndex(
						MathHelper.floor((double) this.npc.ais.getTacticalRange() / 2.0D));
				this.entityPosX = pathpoint.x;
				this.entityPosY = pathpoint.y;
				this.entityPosZ = pathpoint.z;
				Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.npc,
						this.npc.ais.getTacticalRange(), 3,
						new Vec3d((double) this.entityPosX, (double) this.entityPosY, (double) this.entityPosZ));
				if (vec3 != null) {
					if (this.targetEntity.getDistanceSq(vec3.x, vec3.y, vec3.z) < this.targetEntity.getDistanceSq(
							(double) this.entityPosX, (double) this.entityPosY, (double) this.entityPosZ)) {
						this.movePosX = vec3.x;
						this.movePosY = vec3.y;
						this.movePosZ = vec3.z;
					}
				} else {
					this.movePosX = (double) pathpoint.x;
					this.movePosY = (double) pathpoint.y;
					this.movePosZ = (double) pathpoint.z;
				}

				this.npc.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
			} else {
				this.ticks = 10;
			}
		}

	}
}
