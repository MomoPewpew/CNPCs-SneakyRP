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

     public boolean func_75250_a() {
          this.target = this.npc.func_70638_az();
          if (this.target == null) {
               return false;
          } else {
               return this.npc.getOwner() != null && !this.npc.isInRange(this.npc.getOwner(), (double)(this.npc.stats.aggroRange * 2)) ? true : this.npc.combatHandler.checkTarget();
          }
     }

     public void func_75249_e() {
          this.npc.func_70624_b((EntityLivingBase)null);
          if (this.target == this.npc.func_70643_av()) {
               this.npc.func_70604_c((EntityLivingBase)null);
          }

          super.func_75249_e();
     }

     public void func_75251_c() {
          this.npc.func_70661_as().func_75499_g();
     }
}
