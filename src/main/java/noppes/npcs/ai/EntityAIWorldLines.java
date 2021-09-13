package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWorldLines extends EntityAIBase {
     private EntityNPCInterface npc;
     private int cooldown = 100;

     public EntityAIWorldLines(EntityNPCInterface npc) {
          this.npc = npc;
          this.func_75248_a(AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          if (this.cooldown > 0) {
               --this.cooldown;
          }

          return !this.npc.isAttacking() && !this.npc.isKilled() && this.npc.advanced.hasWorldLines() && this.npc.getRNG().nextInt(1800) == 1;
     }

     public void func_75249_e() {
          this.cooldown = 100;
          this.npc.saySurrounding(this.npc.advanced.getWorldLine());
     }
}
