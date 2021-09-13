package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWaterNav extends EntityAIBase {
     private EntityNPCInterface entity;

     public EntityAIWaterNav(EntityNPCInterface iNpc) {
          this.entity = iNpc;
          ((PathNavigateGround)iNpc.getNavigator()).func_179693_d(true);
     }

     public boolean shouldExecute() {
          if (!this.entity.func_70090_H() && !this.entity.func_180799_ab()) {
               return false;
          } else {
               return this.entity.ais.canSwim ? true : this.entity.field_70123_F;
          }
     }

     public void updateTask() {
          if (this.entity.getRNG().nextFloat() < 0.8F) {
               this.entity.func_70683_ar().func_75660_a();
          }

     }
}
