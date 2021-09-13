package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIJob extends EntityAIBase {
     private EntityNPCInterface npc;

     public EntityAIJob(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public boolean func_75250_a() {
          return !this.npc.isKilled() && this.npc.jobInterface != null ? this.npc.jobInterface.aiShouldExecute() : false;
     }

     public void func_75249_e() {
          this.npc.jobInterface.aiStartExecuting();
     }

     public boolean func_75253_b() {
          return !this.npc.isKilled() && this.npc.jobInterface != null ? this.npc.jobInterface.aiContinueExecute() : false;
     }

     public void func_75246_d() {
          if (this.npc.jobInterface != null) {
               this.npc.jobInterface.aiUpdateTask();
          }

     }

     public void func_75251_c() {
          if (this.npc.jobInterface != null) {
               this.npc.jobInterface.resetTask();
          }

     }

     public int func_75247_h() {
          return this.npc.jobInterface == null ? super.func_75247_h() : this.npc.jobInterface.getMutexBits();
     }
}
