package noppes.npcs.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWatchClosest extends EntityAIBase {
     private EntityNPCInterface npc;
     protected Entity closestEntity;
     private float field_75333_c;
     private int lookTime;
     private float field_75331_e;
     private Class watchedClass;

     public EntityAIWatchClosest(EntityNPCInterface par1EntityLiving, Class par2Class, float par3) {
          this.npc = par1EntityLiving;
          this.watchedClass = par2Class;
          this.field_75333_c = par3;
          this.field_75331_e = 0.002F;
          this.setMutexBits(AiMutex.LOOK);
     }

     public boolean shouldExecute() {
          if (this.npc.getRNG().nextFloat() < this.field_75331_e && !this.npc.isInteracting()) {
               if (this.npc.getAttackTarget() != null) {
                    this.closestEntity = this.npc.getAttackTarget();
               }

               if (this.watchedClass == EntityPlayer.class) {
                    this.closestEntity = this.npc.world.getClosestPlayerToEntity(this.npc, (double)this.field_75333_c);
               } else {
                    this.closestEntity = this.npc.world.func_72857_a(this.watchedClass, this.npc.getEntityBoundingBox().expand((double)this.field_75333_c, 3.0D, (double)this.field_75333_c), this.npc);
                    if (this.closestEntity != null) {
                         return this.npc.canSee(this.closestEntity);
                    }
               }

               return this.closestEntity != null;
          } else {
               return false;
          }
     }

     public boolean shouldContinueExecuting() {
          if (!this.npc.isInteracting() && !this.npc.isAttacking() && this.closestEntity.isEntityAlive() && this.npc.isEntityAlive()) {
               return !this.npc.isInRange(this.closestEntity, (double)this.field_75333_c) ? false : this.lookTime > 0;
          } else {
               return false;
          }
     }

     public void startExecuting() {
          this.lookTime = 60 + this.npc.getRNG().nextInt(60);
     }

     public void resetTask() {
          this.closestEntity = null;
     }

     public void updateTask() {
          this.npc.getLookHelper().func_75650_a(this.closestEntity.field_70165_t, this.closestEntity.field_70163_u + (double)this.closestEntity.getEyeHeight(), this.closestEntity.field_70161_v, 10.0F, (float)this.npc.getVerticalFaceSpeed());
          --this.lookTime;
     }
}
