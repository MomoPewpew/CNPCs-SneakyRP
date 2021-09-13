package noppes.npcs.ai.target;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
     EntityNPCInterface npc;
     EntityLivingBase theOwnerAttacker;
     private int timer;

     public EntityAIOwnerHurtByTarget(EntityNPCInterface npc) {
          super(npc, false);
          this.npc = npc;
          this.func_75248_a(AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          if (this.npc.isFollower() && this.npc.roleInterface != null && this.npc.roleInterface.defendOwner()) {
               EntityLivingBase entitylivingbase = this.npc.getOwner();
               if (entitylivingbase == null) {
                    return false;
               } else {
                    this.theOwnerAttacker = entitylivingbase.func_70643_av();
                    int i = entitylivingbase.func_142015_aE();
                    return i != this.timer && this.func_75296_a(this.theOwnerAttacker, false);
               }
          } else {
               return false;
          }
     }

     public void func_75249_e() {
          this.field_75299_d.func_70624_b(this.theOwnerAttacker);
          EntityLivingBase entitylivingbase = this.npc.getOwner();
          if (entitylivingbase != null) {
               this.timer = entitylivingbase.func_142015_aE();
          }

          super.func_75249_e();
     }
}
