package noppes.npcs.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.selector.NPCInteractSelector;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWander extends EntityAIBase {
	private EntityNPCInterface entity;
	public final NPCInteractSelector selector;
	private double x;
	private double y;
	private double zPosition;
	private EntityNPCInterface nearbyNPC;

	public EntityAIWander(EntityNPCInterface npc) {
		this.entity = npc;
		this.setMutexBits(AiMutex.PASSIVE);
		this.selector = new NPCInteractSelector(npc);
	}

	public boolean shouldExecute() {
		if (this.entity.getIdleTime() >= 100 || !this.entity.getNavigator().noPath() || this.entity.isInteracting()
				|| this.entity.isRiding() || this.entity.ais.movingPause && this.entity.getRNG().nextInt(80) != 0) {
			return false;
		} else {
			if (this.entity.ais.npcInteracting
					&& this.entity.getRNG().nextInt(this.entity.ais.movingPause ? 6 : 16) == 1) {
				this.nearbyNPC = this.getNearbyNPC();
			}

			if (this.nearbyNPC != null) {
				this.x = (double) MathHelper.floor(this.nearbyNPC.posX);
				this.y = (double) MathHelper.floor(this.nearbyNPC.posY);
				this.zPosition = (double) MathHelper.floor(this.nearbyNPC.posZ);
				this.nearbyNPC.addInteract(this.entity);
			} else {
				Vec3d vec = this.getVec();
				if (vec == null) {
					return false;
				}

				this.x = vec.x;
				this.y = vec.y;
				if (this.entity.ais.movementType == 1) {
					this.y = this.entity.getStartYPos()
							+ (double) this.entity.getRNG().nextFloat() * 0.75D * (double) this.entity.ais.walkingRange;
				}

				this.zPosition = vec.z;
			}

			return true;
		}
	}

	public void updateTask() {
		if (this.nearbyNPC != null) {
			this.nearbyNPC.getNavigator().clearPath();
		}

	}

	private EntityNPCInterface getNearbyNPC() {
		List list = this.entity.world.getEntitiesInAABBexcluding(this.entity,
				this.entity.getEntityBoundingBox().grow((double) this.entity.ais.walkingRange,
						this.entity.ais.walkingRange > 7 ? 7.0D : (double) this.entity.ais.walkingRange,
						(double) this.entity.ais.walkingRange),
				this.selector);
		Iterator ita = list.iterator();

		while (true) {
			EntityNPCInterface npc;
			do {
				if (!ita.hasNext()) {
					if (list.isEmpty()) {
						return null;
					}

					return (EntityNPCInterface) list.get(this.entity.getRNG().nextInt(list.size()));
				}

				npc = (EntityNPCInterface) ita.next();
			} while (npc.ais.stopAndInteract && !npc.isAttacking() && npc.isEntityAlive()
					&& !this.entity.faction.isAggressiveToNpc(npc));

			ita.remove();
		}
	}

	private Vec3d getVec() {
		if (this.entity.ais.walkingRange > 0) {
			BlockPos start = new BlockPos((double) this.entity.getStartXPos(), this.entity.getStartYPos(),
					(double) this.entity.getStartZPos());
			int distance = (int) MathHelper.sqrt(this.entity.getDistanceSq(start));
			int range = this.entity.ais.walkingRange - distance;
			if (range > CustomNpcs.NpcNavRange) {
				range = CustomNpcs.NpcNavRange;
			}

			if (range < 3) {
				range = this.entity.ais.walkingRange;
				if (range > CustomNpcs.NpcNavRange) {
					range = CustomNpcs.NpcNavRange;
				}

				Vec3d pos2 = new Vec3d((this.entity.posX + (double) start.getX()) / 2.0D,
						(this.entity.posY + (double) start.getY()) / 2.0D,
						(this.entity.posZ + (double) start.getZ()) / 2.0D);
				return RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, distance / 2,
						distance / 2 > 7 ? 7 : distance / 2, pos2);
			} else {
				return RandomPositionGenerator.findRandomTarget(this.entity, range / 2, range / 2 > 7 ? 7 : range / 2);
			}
		} else {
			return RandomPositionGenerator.findRandomTarget(this.entity, CustomNpcs.NpcNavRange, 7);
		}
	}

	public boolean shouldContinueExecuting() {
		if (this.nearbyNPC != null && (!this.selector.apply(this.nearbyNPC)
				|| this.entity.isInRange(this.nearbyNPC, (double) this.entity.width))) {
			return false;
		} else {
			return !this.entity.getNavigator().noPath() && this.entity.isEntityAlive() && !this.entity.isInteracting();
		}
	}

	public void startExecuting() {
		this.entity.getNavigator().tryMoveToXYZ(this.x, this.y, this.zPosition, 1.0D);
	}

	public void resetTask() {
		if (this.nearbyNPC != null && this.entity.isInRange(this.nearbyNPC, 3.5D)) {
			EntityNPCInterface talk = this.entity;
			if (this.entity.getRNG().nextBoolean()) {
				talk = this.nearbyNPC;
			}

			Line line = talk.advanced.getNPCInteractLine();
			if (line == null) {
				line = new Line(".........");
			}

			line.setShowText(false);
			talk.saySurrounding(line);
			this.entity.addInteract(this.nearbyNPC);
			this.nearbyNPC.addInteract(this.entity);
		}

		this.nearbyNPC = null;
	}
}
