package noppes.npcs.ai;

import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIMovingPath extends EntityAIBase {
     private EntityNPCInterface npc;
     private int[] pos;
     private int retries = 0;

     public EntityAIMovingPath(EntityNPCInterface iNpc) {
          this.npc = iNpc;
          this.func_75248_a(AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          if (!this.npc.isAttacking() && !this.npc.isInteracting() && (this.npc.getRNG().nextInt(40) == 0 || !this.npc.ais.movingPause) && this.npc.func_70661_as().func_75500_f()) {
               List list = this.npc.ais.getMovingPath();
               if (list.size() < 2) {
                    return false;
               } else {
                    this.npc.ais.incrementMovingPath();
                    this.pos = this.npc.ais.getCurrentMovingPath();
                    this.retries = 0;
                    return true;
               }
          } else {
               return false;
          }
     }

     public boolean func_75253_b() {
          if (!this.npc.isAttacking() && !this.npc.isInteracting()) {
               if (this.npc.func_70661_as().func_75500_f()) {
                    this.npc.func_70661_as().func_75499_g();
                    if (this.npc.getDistanceSq((double)this.pos[0], (double)this.pos[1], (double)this.pos[2]) < 3.0D) {
                         return false;
                    } else if (this.retries++ < 3) {
                         this.func_75249_e();
                         return true;
                    } else {
                         return false;
                    }
               } else {
                    return true;
               }
          } else {
               this.npc.ais.decreaseMovingPath();
               return false;
          }
     }

     public void func_75249_e() {
          this.npc.func_70661_as().func_75492_a((double)this.pos[0] + 0.5D, (double)this.pos[1], (double)this.pos[2] + 0.5D, 1.0D);
     }
}
