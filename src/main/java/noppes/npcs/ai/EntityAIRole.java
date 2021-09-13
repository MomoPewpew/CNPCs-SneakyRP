package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIRole extends EntityAIBase {
     private EntityNPCInterface npc;

     public EntityAIRole(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public boolean func_75250_a() {
          return !this.npc.isKilled() && this.npc.roleInterface != null ? this.npc.roleInterface.aiShouldExecute() : false;
     }

     public void func_75249_e() {
          this.npc.roleInterface.aiStartExecuting();
     }

     public boolean func_75253_b() {
          return !this.npc.isKilled() && this.npc.roleInterface != null ? this.npc.roleInterface.aiContinueExecute() : false;
     }

     public void func_75246_d() {
          if (this.npc.roleInterface != null) {
               this.npc.roleInterface.aiUpdateTask();
          }

     }
}
