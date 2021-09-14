package noppes.npcs.ai.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIClearTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase target;

     public EntityAIClearTarget(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public boolean shouldExecute() {
          this.target = this.npc.getAttackTarget();
          if (this.target == null) {
               return false;
          } else {
               return this.npc.getOwner() != null && !this.npc.isInRange(this.npc.getOwner(), (double)(this.npc.stats.aggroRange * 2)) ? true : this.npc.combatHandler.checkTarget();
          }
     }

     public void startExecuting() {
          this.npc.setAttackTarget((EntityLivingBase)null);
          if (this.target == this.npc.getRevengeTarget()) {
               this.npc.setRevengeTarget((EntityLivingBase)null);
          }

          super.startExecuting();
     }

     public void resetTask() {
          this.npc.getNavigator().clearPath();
     }
}
