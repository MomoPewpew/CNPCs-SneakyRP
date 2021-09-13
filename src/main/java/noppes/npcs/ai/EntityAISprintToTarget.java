package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAISprintToTarget extends EntityAIBase {
     private EntityNPCInterface npc;

     public EntityAISprintToTarget(EntityNPCInterface par1EntityLiving) {
          this.npc = par1EntityLiving;
          this.func_75248_a(AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          EntityLivingBase runTarget = this.npc.func_70638_az();
          if (runTarget != null && !this.npc.func_70661_as().func_75500_f()) {
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

     public boolean func_75253_b() {
          return this.npc.func_70089_S() && this.npc.field_70122_E && this.npc.field_70737_aN <= 0 && this.npc.motionX != 0.0D && this.npc.motionZ != 0.0D;
     }

     public void func_75249_e() {
          this.npc.func_70031_b(true);
     }

     public void func_75251_c() {
          this.npc.func_70031_b(false);
     }
}
