package noppes.npcs.ability;

import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.constants.EnumAbilityType;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class AbstractAbility implements IAbility {
     private long cooldown = 0L;
     private int cooldownTime = 10;
     private int startCooldownTime = 10;
     protected EntityNPCInterface npc;
     public float maxHP = 1.0F;
     public float minHP = 0.0F;

     public AbstractAbility(EntityNPCInterface npc) {
          this.npc = npc;
     }

     private boolean onCooldown() {
          return System.currentTimeMillis() < this.cooldown;
     }

     public int getRNG() {
          return 0;
     }

     public boolean canRun(EntityLivingBase target) {
          if (this.onCooldown()) {
               return false;
          } else {
               float f = this.npc.func_110143_aJ() / this.npc.getMaxHealth();
               if (f >= this.minHP && f <= this.maxHP) {
                    return this.getRNG() > 1 && this.npc.getRNG().nextInt(this.getRNG()) != 0 ? false : this.npc.func_70685_l(target);
               } else {
                    return false;
               }
          }
     }

     public void endAbility() {
          this.cooldown = System.currentTimeMillis() + (long)(this.cooldownTime * 1000);
     }

     public abstract boolean isType(EnumAbilityType var1);

     public void startCombat() {
          this.cooldown = System.currentTimeMillis() + (long)(this.startCooldownTime * 1000);
     }
}
