package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWaterNav extends EntityAIBase {
     private EntityNPCInterface entity;

     public EntityAIWaterNav(EntityNPCInterface iNpc) {
          this.entity = iNpc;
          ((PathNavigateGround)iNpc.getNavigator()).setCanSwim(true);
     }

     public boolean shouldExecute() {
          if (!this.entity.isInWater() && !this.entity.isInLava()) {
               return false;
          } else {
               return this.entity.ais.canSwim ? true : this.entity.collidedHorizontally;
          }
     }

     public void updateTask() {
          if (this.entity.getRNG().nextFloat() < 0.8F) {
               this.entity.getJumpHelper().setJumping();
          }

     }
}
