package noppes.npcs.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.ability.AbstractAbility;
import noppes.npcs.entity.EntityNPCInterface;

public class CombatHandler {
     private Map aggressors = new HashMap();
     private EntityNPCInterface npc;
     private long startTime = 0L;
     private int combatResetTimer = 0;

     public CombatHandler(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public void update() {
          if (this.npc.isKilled()) {
               if (this.npc.isAttacking()) {
                    this.reset();
               }

          } else {
               if (this.npc.func_70638_az() != null && !this.npc.isAttacking()) {
                    this.start();
               }

               if (!this.shouldCombatContinue()) {
                    if (this.combatResetTimer++ > 40) {
                         this.reset();
                    }

               } else {
                    this.combatResetTimer = 0;
               }
          }
     }

     private boolean shouldCombatContinue() {
          return this.npc.func_70638_az() == null ? false : this.isValidTarget(this.npc.func_70638_az());
     }

     public void damage(DamageSource source, float damageAmount) {
          this.combatResetTimer = 0;
          Entity e = NoppesUtilServer.GetDamageSourcee(source);
          if (e instanceof EntityLivingBase) {
               EntityLivingBase el = (EntityLivingBase)e;
               Float f = (Float)this.aggressors.get(el);
               if (f == null) {
                    f = 0.0F;
               }

               this.aggressors.put(el, f + damageAmount);
          }

     }

     public void start() {
          this.combatResetTimer = 0;
          this.startTime = this.npc.field_70170_p.func_72912_H().func_82573_f();
          this.npc.func_184212_Q().func_187227_b(EntityNPCInterface.Attacking, true);
          Iterator var1 = this.npc.abilities.abilities.iterator();

          while(var1.hasNext()) {
               AbstractAbility ab = (AbstractAbility)var1.next();
               ab.startCombat();
          }

     }

     public void reset() {
          this.combatResetTimer = 0;
          this.aggressors.clear();
          this.npc.func_184212_Q().func_187227_b(EntityNPCInterface.Attacking, false);
     }

     public boolean checkTarget() {
          if (!this.aggressors.isEmpty() && this.npc.field_70173_aa % 10 == 0) {
               EntityLivingBase target = this.npc.func_70638_az();
               Float current = 0.0F;
               if (this.isValidTarget(target)) {
                    current = (Float)this.aggressors.get(target);
                    if (current == null) {
                         current = 0.0F;
                    }
               } else {
                    target = null;
               }

               Iterator var3 = this.aggressors.entrySet().iterator();

               while(var3.hasNext()) {
                    Entry entry = (Entry)var3.next();
                    if ((Float)entry.getValue() > current && this.isValidTarget((EntityLivingBase)entry.getKey())) {
                         current = (Float)entry.getValue();
                         target = (EntityLivingBase)entry.getKey();
                    }
               }

               return target == null;
          } else {
               return false;
          }
     }

     public boolean isValidTarget(EntityLivingBase target) {
          if (target != null && target.func_70089_S()) {
               return target instanceof EntityPlayer && ((EntityPlayer)target).field_71075_bZ.field_75102_a ? false : this.npc.isInRange(target, (double)this.npc.stats.aggroRange);
          } else {
               return false;
          }
     }
}
