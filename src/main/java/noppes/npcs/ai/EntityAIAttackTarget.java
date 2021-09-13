package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAttackTarget extends EntityAIBase {
     private World world;
     private EntityNPCInterface npc;
     private EntityLivingBase entityTarget;
     private int attackTick = 0;
     private Path entityPathEntity;
     private int field_75445_i;
     private boolean navOverride = false;

     public EntityAIAttackTarget(EntityNPCInterface par1EntityLiving) {
          this.npc = par1EntityLiving;
          this.world = par1EntityLiving.world;
          this.func_75248_a(this.navOverride ? AiMutex.PATHING : AiMutex.LOOK + AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          EntityLivingBase entitylivingbase = this.npc.func_70638_az();
          if (entitylivingbase != null && entitylivingbase.func_70089_S()) {
               int melee = this.npc.stats.ranged.getMeleeRange();
               if (this.npc.inventory.getProjectile() != null && (melee <= 0 || !this.npc.isInRange(entitylivingbase, (double)melee))) {
                    return false;
               } else {
                    this.entityTarget = entitylivingbase;
                    this.entityPathEntity = this.npc.func_70661_as().func_75494_a(entitylivingbase);
                    return this.entityPathEntity != null;
               }
          } else {
               return false;
          }
     }

     public boolean func_75253_b() {
          this.entityTarget = this.npc.func_70638_az();
          if (this.entityTarget == null) {
               this.entityTarget = this.npc.func_70643_av();
          }

          if (this.entityTarget != null && this.entityTarget.func_70089_S()) {
               if (!this.npc.isInRange(this.entityTarget, (double)this.npc.stats.aggroRange)) {
                    return false;
               } else {
                    int melee = this.npc.stats.ranged.getMeleeRange();
                    return melee > 0 && !this.npc.isInRange(this.entityTarget, (double)melee) ? false : this.npc.func_180485_d(new BlockPos(this.entityTarget));
               }
          } else {
               return false;
          }
     }

     public void func_75249_e() {
          if (!this.navOverride) {
               this.npc.func_70661_as().func_75484_a(this.entityPathEntity, 1.3D);
          }

          this.field_75445_i = 0;
     }

     public void func_75251_c() {
          this.entityPathEntity = null;
          this.entityTarget = null;
          this.npc.func_70661_as().func_75499_g();
     }

     public void func_75246_d() {
          this.npc.func_70671_ap().func_75651_a(this.entityTarget, 30.0F, 30.0F);
          if (!this.navOverride && --this.field_75445_i <= 0) {
               this.field_75445_i = 4 + this.npc.getRNG().nextInt(7);
               this.npc.func_70661_as().func_75497_a(this.entityTarget, 1.2999999523162842D);
          }

          this.attackTick = Math.max(this.attackTick - 1, 0);
          double y = this.entityTarget.field_70163_u;
          if (this.entityTarget.getEntityBoundingBox() != null) {
               y = this.entityTarget.getEntityBoundingBox().field_72338_b;
          }

          double distance = this.npc.getDistanceSq(this.entityTarget.field_70165_t, y, this.entityTarget.field_70161_v);
          double range = (double)((float)(this.npc.stats.melee.getRange() * this.npc.stats.melee.getRange()) + this.entityTarget.field_70130_N);
          double minRange = (double)(this.npc.field_70130_N * 2.0F * this.npc.field_70130_N * 2.0F + this.entityTarget.field_70130_N);
          if (minRange > range) {
               range = minRange;
          }

          if (distance <= range && (this.npc.canSee(this.entityTarget) || distance < minRange) && this.attackTick <= 0) {
               this.attackTick = this.npc.stats.melee.getDelay();
               this.npc.func_184609_a(EnumHand.MAIN_HAND);
               this.npc.func_70652_k(this.entityTarget);
          }

     }

     public void navOverride(boolean nav) {
          this.navOverride = nav;
          this.func_75248_a(this.navOverride ? AiMutex.PATHING : AiMutex.LOOK + AiMutex.PASSIVE);
     }
}
