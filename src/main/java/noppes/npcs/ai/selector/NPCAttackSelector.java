package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.companion.CompanionGuard;

public class NPCAttackSelector implements Predicate {
	private EntityNPCInterface npc;

	public NPCAttackSelector(EntityNPCInterface npc) {
		this.npc = npc;
	}

	public boolean isEntityApplicable(EntityLivingBase entity) {
		if (entity.isEntityAlive() && entity != this.npc
				&& this.npc.isInRange(entity, (double) this.npc.stats.aggroRange) && entity.getHealth() >= 1.0F) {
			if (this.npc.ais.directLOS && !this.npc.getEntitySenses().canSee(entity)) {
				return false;
			} else if (!this.npc.ais.attackInvisible && entity.isPotionActive(MobEffects.INVISIBILITY)
					&& !this.npc.isInRange(entity, 3.0D)) {
				return false;
			} else {
				if (!this.npc.isFollower() && this.npc.ais.shouldReturnHome()) {
					int allowedDistance = this.npc.stats.aggroRange * 2;
					if (this.npc.ais.getMovingType() == 1) {
						allowedDistance += this.npc.ais.walkingRange;
					}

					double distance = entity.getDistanceSq((double) this.npc.getStartXPos(), this.npc.getStartYPos(),
							(double) this.npc.getStartZPos());
					if (this.npc.ais.getMovingType() == 2) {
						int[] arr = this.npc.ais.getCurrentMovingPath();
						distance = entity.getDistanceSq((double) arr[0], (double) arr[1], (double) arr[2]);
					}

					if (distance > (double) (allowedDistance * allowedDistance)) {
						return false;
					}
				}

				if (this.npc.advanced.job == 3 && ((JobGuard) this.npc.jobInterface).isEntityApplicable(entity)) {
					return true;
				} else {
					if (this.npc.advanced.role == 6) {
						RoleCompanion role = (RoleCompanion) this.npc.roleInterface;
						if (role.job == EnumCompanionJobs.GUARD
								&& ((CompanionGuard) role.jobInterface).isEntityApplicable(entity)) {
							return true;
						}
					}

					if (entity instanceof EntityPlayerMP) {
						EntityPlayerMP player = (EntityPlayerMP) entity;
						return this.npc.faction.isAggressiveToPlayer(player) && !player.capabilities.disableDamage;
					} else {
						if (entity instanceof EntityNPCInterface) {
							if (((EntityNPCInterface) entity).isKilled()) {
								return false;
							}

							if (this.npc.advanced.attackOtherFactions) {
								return this.npc.faction.isAggressiveToNpc((EntityNPCInterface) entity);
							}
						}

						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	public boolean apply(EntityLivingBase ob) {
		return this.isEntityApplicable(ob);
	}

	@Override
	public boolean apply(Object input) {
		// TODO Auto-generated method stub
		return false;
	}
}
