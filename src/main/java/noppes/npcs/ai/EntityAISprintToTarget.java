package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAISprintToTarget extends EntityAIBase {
     private EntityNPCInterface npc;

     public EntityAISprintToTarget(EntityNPCInterface par1EntityLiving) {
          this.npc = par1EntityLiving;
          this.setMutexBits(AiMutex.PASSIVE);
     }

     public boolean shouldExecute() {
          EntityLivingBase runTarget = this.npc.getAttackTarget();
          if (runTarget != null && !this.npc.getNavigator().noPath()) {
               switch(this.npc.ais.onAttack) {
               case 0:
                    return !this.npc.isInRange(runTarget, 8.0D) ? this.npc.field_70122_E : false;
               case 2:
                    return this.npc.isInRange(runTarget, 7.0D) ? this.npc.field_70122_E : false;
               default:
                    return false;
               }
          } else {
               return false;
          }
     }

     public boolean shouldContinueExecuting() {
          return this.npc.isEntityAlive() && this.npc.field_70122_E && this.npc.field_70737_aN <= 0 && this.npc.motionX != 0.0D && this.npc.motionZ != 0.0D;
     }

     public void startExecuting() {
          this.npc.func_70031_b(true);
     }

     public void resetTask() {
          this.npc.func_70031_b(false);
     }
}
