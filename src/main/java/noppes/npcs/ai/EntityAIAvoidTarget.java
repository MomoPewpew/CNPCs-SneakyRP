package noppes.npcs.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAvoidTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private Entity closestLivingEntity;
     private float distanceFromEntity;
     private float health;
     private Path entityPathEntity;
     private PathNavigate entityPathNavigate;
     private Class targetEntityClass;

     public EntityAIAvoidTarget(EntityNPCInterface par1EntityNPC) {
          this.npc = par1EntityNPC;
          this.distanceFromEntity = (float)this.npc.stats.aggroRange;
          this.health = this.npc.getHealth();
          this.entityPathNavigate = par1EntityNPC.getNavigator();
          this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean shouldExecute() {
          EntityLivingBase target = this.npc.getAttackTarget();
          if (target == null) {
               return false;
          } else {
               this.targetEntityClass = target.getClass();
               if (this.targetEntityClass == EntityPlayer.class) {
                    this.closestLivingEntity = this.npc.world.getClosestPlayerToEntity(this.npc, (double)this.distanceFromEntity);
                    if (this.closestLivingEntity == null) {
                         return false;
                    }
               } else {
                    List var1 = this.npc.world.getEntitiesWithinAABB(this.targetEntityClass, this.npc.getEntityBoundingBox().grow((double)this.distanceFromEntity, 3.0D, (double)this.distanceFromEntity));
                    if (var1.isEmpty()) {
                         return false;
                    }

                    this.closestLivingEntity = (Entity)var1.get(0);
               }

               if (!this.npc.getEntitySenses().canSee(this.closestLivingEntity) && this.npc.ais.directLOS) {
                    return false;
               } else {
                    Vec3d var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.npc, 16, 7, new Vec3d(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
                    boolean var3 = this.npc.inventory.getProjectile() == null;
                    boolean var4 = var3 ? this.health == this.npc.getHealth() : this.npc.getRangedTask() != null && !this.npc.getRangedTask().hasFired();
                    if (var2 == null) {
                         return false;
                    } else if (this.closestLivingEntity.getDistanceSq(var2.x, var2.y, var2.z) < this.closestLivingEntity.getDistanceSq(this.npc)) {
                         return false;
                    } else if (this.npc.ais.tacticalVariant == 3 && var4) {
                         return false;
                    } else {
                         this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(var2.x, var2.y, var2.z);
                         return this.entityPathEntity != null;
                    }
               }
          }
     }

     public boolean shouldContinueExecuting() {
          return !this.entityPathNavigate.noPath();
     }

     public void startExecuting() {
          this.entityPathNavigate.setPath(this.entityPathEntity, 1.0D);
     }

     public void resetTask() {
          this.closestLivingEntity = null;
          this.npc.setAttackTarget((EntityLivingBase)null);
     }

     public void updateTask() {
          if (this.npc.isInRange(this.closestLivingEntity, 7.0D)) {
               this.npc.getNavigator().setSpeed(1.2D);
          } else {
               this.npc.getNavigator().setSpeed(1.0D);
          }

          if (this.npc.ais.tacticalVariant == 3 && (!this.npc.isInRange(this.closestLivingEntity, (double)this.distanceFromEntity) || this.npc.isInRange(this.closestLivingEntity, (double)this.npc.ais.getTacticalRange()))) {
               this.health = this.npc.getHealth();
          }

     }
}
