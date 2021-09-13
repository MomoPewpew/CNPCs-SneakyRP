package noppes.npcs.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAvoidTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private Entity closestLivingEntity;
     private float distanceFromEntity;
     private float health;
     private Path entityPathEntity;
     private PathNavigate entityPathNavigate;
     private Class targetEntityClass;

     public EntityAIAvoidTarget(EntityNPCInterface par1EntityNPC) {
          this.npc = par1EntityNPC;
          this.distanceFromEntity = (float)this.npc.stats.aggroRange;
          this.health = this.npc.func_110143_aJ();
          this.entityPathNavigate = par1EntityNPC.func_70661_as();
          this.func_75248_a(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean func_75250_a() {
          EntityLivingBase target = this.npc.func_70638_az();
          if (target == null) {
               return false;
          } else {
               this.targetEntityClass = target.getClass();
               if (this.targetEntityClass == EntityPlayer.class) {
                    this.closestLivingEntity = this.npc.world.func_72890_a(this.npc, (double)this.distanceFromEntity);
                    if (this.closestLivingEntity == null) {
                         return false;
                    }
               } else {
                    List var1 = this.npc.world.func_72872_a(this.targetEntityClass, this.npc.func_174813_aQ().func_72314_b((double)this.distanceFromEntity, 3.0D, (double)this.distanceFromEntity));
                    if (var1.isEmpty()) {
                         return false;
                    }

                    this.closestLivingEntity = (Entity)var1.get(0);
               }

               if (!this.npc.func_70635_at().func_75522_a(this.closestLivingEntity) && this.npc.ais.directLOS) {
                    return false;
               } else {
                    Vec3d var2 = RandomPositionGenerator.func_75461_b(this.npc, 16, 7, new Vec3d(this.closestLivingEntity.field_70165_t, this.closestLivingEntity.field_70163_u, this.closestLivingEntity.field_70161_v));
                    boolean var3 = this.npc.inventory.getProjectile() == null;
                    boolean var4 = var3 ? this.health == this.npc.func_110143_aJ() : this.npc.getRangedTask() != null && !this.npc.getRangedTask().hasFired();
                    if (var2 == null) {
                         return false;
                    } else if (this.closestLivingEntity.func_70092_e(var2.field_72450_a, var2.field_72448_b, var2.field_72449_c) < this.closestLivingEntity.func_70068_e(this.npc)) {
                         return false;
                    } else if (this.npc.ais.tacticalVariant == 3 && var4) {
                         return false;
                    } else {
                         this.entityPathEntity = this.entityPathNavigate.func_75488_a(var2.field_72450_a, var2.field_72448_b, var2.field_72449_c);
                         return this.entityPathEntity != null;
                    }
               }
          }
     }

     public boolean func_75253_b() {
          return !this.entityPathNavigate.func_75500_f();
     }

     public void func_75249_e() {
          this.entityPathNavigate.func_75484_a(this.entityPathEntity, 1.0D);
     }

     public void func_75251_c() {
          this.closestLivingEntity = null;
          this.npc.func_70624_b((EntityLivingBase)null);
     }

     public void func_75246_d() {
          if (this.npc.isInRange(this.closestLivingEntity, 7.0D)) {
               this.npc.func_70661_as().func_75489_a(1.2D);
          } else {
               this.npc.func_70661_as().func_75489_a(1.0D);
          }

          if (this.npc.ais.tacticalVariant == 3 && (!this.npc.isInRange(this.closestLivingEntity, (double)this.distanceFromEntity) || this.npc.isInRange(this.closestLivingEntity, (double)this.npc.ais.getTacticalRange()))) {
               this.health = this.npc.func_110143_aJ();
          }

     }
}
