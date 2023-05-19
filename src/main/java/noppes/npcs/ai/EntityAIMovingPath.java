package noppes.npcs.ai;

import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIMovingPath extends EntityAIBase {
	private EntityNPCInterface npc;
	private int[] pos;
	private int retries = 0;
	private long wanderEndTime = 0;
	private long lastWander = 0;

	public EntityAIMovingPath(EntityNPCInterface iNpc) {
		this.npc = iNpc;
		this.setMutexBits(AiMutex.PASSIVE);
	}

	public boolean shouldExecute() {
		if (!this.npc.isAttacking() && !this.npc.isInteracting()
				&& (this.npc.getRNG().nextInt(40) == 0 || !this.npc.ais.movingPause)
				&& this.npc.getNavigator().noPath()) {
			List<int[]> list = this.npc.ais.getMovingPath();
			if (list.size() < 2) {
				return false;
			} else if (this.wanderEndTime < System.currentTimeMillis()) {
				this.npc.ais.incrementMovingPath();
				this.pos = this.npc.ais.getCurrentMovingPath();
				this.retries = 0;
				return true;
			} else {
				this.wander();
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean shouldContinueExecuting() {
		if (!this.npc.isAttacking() && !this.npc.isInteracting()) {
			if (this.npc.getNavigator().noPath()) {
				this.npc.getNavigator().clearPath();
				if (this.npc.getDistanceSq((double) this.pos[0], (double) this.pos[1], (double) this.pos[2]) < 3.0D) {
					if (this.npc.ais.isCurrentPathWanderNode() && this.wanderEndTime < System.currentTimeMillis()) {
						this.wanderEndTime = System.currentTimeMillis() + (this.npc.ais.getCurrentPathWanderDuration() * 1000);
					}
					return false;
				} else if (this.wanderEndTime > System.currentTimeMillis()) {
					return false;
				} else if (this.retries++ < 3) {
					this.startExecuting();
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			this.npc.ais.decreaseMovingPath();
			return false;
		}
	}

	public void startExecuting() {
		this.npc.getNavigator().tryMoveToXYZ((double) this.pos[0] + 0.5D, (double) this.pos[1],
				(double) this.pos[2] + 0.5D, 1.0D);
	}

	public void wander() {
		if (System.currentTimeMillis() < (this.lastWander + 10000)) return;

		Vec3d vec = getVec();
		if (vec == null) {
			return;
		}
		this.lastWander = System.currentTimeMillis();
		this.npc.getNavigator().tryMoveToXYZ(vec.x, vec.y, vec.z, 1.0D);
	}

	private Vec3d getVec() {
		BlockPos start = new BlockPos((double) this.pos[0] + 0.5D, (double) this.pos[1],
				(double) this.pos[2] + 0.5D);
		int distance = (int) MathHelper.sqrt(this.npc.getDistanceSq(start));
		int range = 10 - distance;
		if (range > CustomNpcs.NpcNavRange) {
			range = CustomNpcs.NpcNavRange;
		}

		if (range < 3) {
			range = 10;
			if (range > CustomNpcs.NpcNavRange) {
				range = CustomNpcs.NpcNavRange;
			}

			Vec3d pos2 = new Vec3d((this.npc.posX + (double) start.getX()) / 2.0D,
					(this.npc.posY + (double) start.getY()) / 2.0D,
					(this.npc.posZ + (double) start.getZ()) / 2.0D);
			return RandomPositionGenerator.findRandomTargetBlockTowards(this.npc, distance / 2,
					distance / 2 > 7 ? 7 : distance / 2, pos2);
		} else {
			return RandomPositionGenerator.findRandomTarget(this.npc, range / 2, range / 2 > 7 ? 7 : range / 2);
		}
	}
}
