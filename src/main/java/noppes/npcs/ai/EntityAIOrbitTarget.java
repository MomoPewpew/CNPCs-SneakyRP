package noppes.npcs.ai;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOrbitTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase targetEntity;
     private double movePosX;
     private double movePosY;
     private double movePosZ;
     private double speed;
     private float distance;
     private int delay = 0;
     private float angle = 0.0F;
     private int direction = 1;
     private float targetDistance;
     private boolean decay;
     private boolean canNavigate = true;
     private float decayRate = 1.0F;
     private int tick = 0;

     public EntityAIOrbitTarget(EntityNPCInterface par1EntityCreature, double par2, boolean par5) {
          this.npc = par1EntityCreature;
          this.speed = par2;
          this.decay = par5;
          this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean shouldExecute() {
          if (--this.delay > 0) {
               return false;
          } else {
               this.delay = 10;
               this.targetEntity = this.npc.getAttackTarget();
               if (this.targetEntity == null) {
                    return false;
               } else {
                    if (this.decay) {
                         this.distance = (float)this.npc.ais.getTacticalRange();
                    } else {
                         this.distance = (float)this.npc.stats.ranged.getRange();
                    }

                    return !this.npc.isInRange(this.targetEntity, (double)(this.distance / 2.0F)) && (this.npc.inventory.getProjectile() != null || this.npc.isInRange(this.targetEntity, (double)this.distance));
               }
          }
     }

     public boolean shouldContinueExecuting() {
          return this.targetEntity.isEntityAlive() && !this.npc.isInRange(this.targetEntity, (double)(this.distance / 2.0F)) && this.npc.isInRange(this.targetEntity, (double)this.distance * 1.5D) && !this.npc.isInWater() && this.canNavigate;
     }

     public void resetTask() {
          this.npc.getNavigator().clearPath();
          this.delay = 60;
          if (this.npc.getRangedTask() != null) {
               this.npc.getRangedTask().navOverride(false);
          }

     }

     public void startExecuting() {
          this.canNavigate = true;
          Random random = this.npc.getRNG();
          this.direction = random.nextInt(10) > 5 ? 1 : -1;
          this.decayRate = random.nextFloat() + this.distance / 16.0F;
          this.targetDistance = this.npc.getDistance(this.targetEntity);
          double d0 = this.npc.field_70165_t - this.targetEntity.field_70165_t;
          double d1 = this.npc.field_70161_v - this.targetEntity.field_70161_v;
          this.angle = (float)(Math.atan2(d1, d0) * 180.0D / 3.141592653589793D);
          if (this.npc.getRangedTask() != null) {
               this.npc.getRangedTask().navOverride(true);
          }

     }

     public void updateTask() {
          this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
          if (this.npc.getNavigator().noPath() && this.tick >= 0 && this.npc.field_70122_E && !this.npc.isInWater()) {
               double d0 = (double)this.targetDistance * (double)MathHelper.cos(this.angle / 180.0F * 3.1415927F);
               double d1 = (double)this.targetDistance * (double)MathHelper.sin(this.angle / 180.0F * 3.1415927F);
               this.movePosX = this.targetEntity.field_70165_t + d0;
               this.movePosY = this.targetEntity.getEntityBoundingBox().field_72337_e;
               this.movePosZ = this.targetEntity.field_70161_v + d1;
               this.npc.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
               this.angle += 15.0F * (float)this.direction;
               this.tick = MathHelper.ceil(this.npc.getDistance(this.movePosX, this.movePosY, this.movePosZ) / (double)(this.npc.getSpeed() / 20.0F));
               if (this.decay) {
                    this.targetDistance -= this.decayRate;
               }
          }

          if (this.tick >= 0) {
               --this.tick;
          }

     }
}
