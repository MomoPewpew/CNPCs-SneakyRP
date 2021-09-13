package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIPounceTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase leapTarget;
     private float leapSpeed = 1.3F;

     public EntityAIPounceTarget(EntityNPCInterface leapingEntity) {
          this.npc = leapingEntity;
          this.setMutexBits(4);
     }

     public boolean shouldExecute() {
          if (!this.npc.field_70122_E) {
               return false;
          } else {
               this.leapTarget = this.npc.getAttackTarget();
               if (this.leapTarget != null && this.npc.getEntitySenses().canSee(this.leapTarget)) {
                    return !this.npc.isInRange(this.leapTarget, 4.0D) && this.npc.isInRange(this.leapTarget, 8.0D) ? this.npc.getRNG().nextInt(5) == 0 : false;
               } else {
                    return false;
               }
          }
     }

     public boolean shouldContinueExecuting() {
          return !this.npc.field_70122_E;
     }

     public void startExecuting() {
          double varX = this.leapTarget.field_70165_t - this.npc.field_70165_t;
          double varY = this.leapTarget.getEntityBoundingBox().field_72338_b - this.npc.getEntityBoundingBox().field_72338_b;
          double varZ = this.leapTarget.field_70161_v - this.npc.field_70161_v;
          float varF = MathHelper.func_76133_a(varX * varX + varZ * varZ);
          float angle = this.getAngleForXYZ(varX, varY, varZ, (double)varF);
          float yaw = (float)(Math.atan2(varX, varZ) * 180.0D / 3.141592653589793D);
          this.npc.motionX = (double)(MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(angle / 180.0F * 3.1415927F));
          this.npc.motionZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(angle / 180.0F * 3.1415927F));
          this.npc.motionY = (double)MathHelper.func_76126_a((angle + 1.0F) / 180.0F * 3.1415927F);
          EntityNPCInterface var10000 = this.npc;
          var10000.motionX *= (double)this.leapSpeed;
          var10000 = this.npc;
          var10000.motionZ *= (double)this.leapSpeed;
          var10000 = this.npc;
          var10000.motionY *= (double)this.leapSpeed;
     }

     public float getAngleForXYZ(double varX, double varY, double varZ, double horiDist) {
          float g = 0.1F;
          float var1 = this.leapSpeed * this.leapSpeed;
          double var2 = (double)g * horiDist;
          double var3 = (double)g * horiDist * horiDist + 2.0D * varY * (double)var1;
          double var4 = (double)(var1 * var1) - (double)g * var3;
          if (var4 < 0.0D) {
               return 90.0F;
          } else {
               float var6 = var1 - MathHelper.func_76133_a(var4);
               float var7 = (float)(Math.atan2((double)var6, var2) * 180.0D / 3.141592653589793D);
               return var7;
          }
     }
}
