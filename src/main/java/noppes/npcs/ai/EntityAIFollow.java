package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIFollow extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase owner;
     public int updateTick = 0;

     public EntityAIFollow(EntityNPCInterface npc) {
          this.npc = npc;
          this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean shouldExecute() {
          if (!this.canExcute()) {
               return false;
          } else {
               return !this.npc.isInRange(this.owner, (double)this.npc.followRange());
          }
     }

     public boolean canExcute() {
          return this.npc.isEntityAlive() && this.npc.isFollower() && !this.npc.isAttacking() && (this.owner = this.npc.getOwner()) != null && this.npc.ais.animationType != 1;
     }

     public void startExecuting() {
          this.updateTick = 10;
     }

     public boolean shouldContinueExecuting() {
          return !this.npc.getNavigator().noPath() && !this.npc.isInRange(this.owner, 2.0D) && this.canExcute();
     }

     public void resetTask() {
          this.owner = null;
          this.npc.getNavigator().clearPath();
     }

     public void updateTask() {
          ++this.updateTick;
          if (this.updateTick >= 10) {
               this.updateTick = 0;
               this.npc.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float)this.npc.getVerticalFaceSpeed());
               double distance = this.npc.getDistanceSq(this.owner);
               double speed = 1.0D + distance / 150.0D;
               if (speed > 3.0D) {
                    speed = 3.0D;
               }

               if (this.owner.isSprinting()) {
                    speed += 0.5D;
               }

               if (!this.npc.getNavigator().tryMoveToEntityLiving(this.owner, speed) && !this.npc.isInRange(this.owner, 16.0D)) {
                    this.npc.tpTo(this.owner);
               }
          }
     }
}
