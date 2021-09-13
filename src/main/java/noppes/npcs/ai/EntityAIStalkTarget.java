package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIStalkTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase targetEntity;
     private Vec3d movePosition;
     private boolean overRide;
     private World world;
     private int delay;
     private int tick = 0;

     public EntityAIStalkTarget(EntityNPCInterface par1EntityCreature) {
          this.npc = par1EntityCreature;
          this.world = par1EntityCreature.world;
          this.overRide = false;
          this.delay = 0;
          this.func_75248_a(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean func_75250_a() {
          this.targetEntity = this.npc.func_70638_az();
          if (this.targetEntity != null && this.tick-- <= 0) {
               return !this.npc.isInRange(this.targetEntity, (double)this.npc.ais.getTacticalRange());
          } else {
               return false;
          }
     }

     public void func_75251_c() {
          this.npc.func_70661_as().func_75499_g();
          if (this.npc.func_70638_az() == null && this.targetEntity != null) {
               this.npc.func_70624_b(this.targetEntity);
          }

          if (this.npc.getRangedTask() != null) {
               this.npc.getRangedTask().navOverride(false);
          }

     }

     public void func_75249_e() {
          if (this.npc.getRangedTask() != null) {
               this.npc.getRangedTask().navOverride(true);
          }

     }

     public void func_75246_d() {
          this.npc.func_70671_ap().func_75651_a(this.targetEntity, 30.0F, 30.0F);
          if (this.npc.func_70661_as().func_75500_f() || this.overRide) {
               if (this.isLookingAway()) {
                    this.movePosition = this.stalkTarget();
                    if (this.movePosition != null) {
                         this.npc.func_70661_as().func_75492_a(this.movePosition.field_72450_a, this.movePosition.field_72448_b, this.movePosition.field_72449_c, 1.0D);
                         this.overRide = false;
                    } else {
                         this.tick = 100;
                    }
               } else if (this.npc.canSee(this.targetEntity)) {
                    this.movePosition = this.hideFromTarget();
                    if (this.movePosition != null) {
                         this.npc.func_70661_as().func_75492_a(this.movePosition.field_72450_a, this.movePosition.field_72448_b, this.movePosition.field_72449_c, 1.33D);
                         this.overRide = false;
                    } else {
                         this.tick = 100;
                    }
               }
          }

          if (this.delay > 0) {
               --this.delay;
          }

          if (!this.isLookingAway() && this.npc.canSee(this.targetEntity) && this.delay == 0) {
               this.overRide = true;
               this.delay = 60;
          }

     }

     private Vec3d hideFromTarget() {
          for(int i = 1; i <= 8; ++i) {
               Vec3d vec = this.findSecludedXYZ(i, false);
               if (vec != null) {
                    return vec;
               }
          }

          return null;
     }

     private Vec3d stalkTarget() {
          for(int i = 8; i >= 1; --i) {
               Vec3d vec = this.findSecludedXYZ(i, true);
               if (vec != null) {
                    return vec;
               }
          }

          return null;
     }

     private Vec3d findSecludedXYZ(int radius, boolean nearest) {
          Vec3d idealPos = null;
          double dist = this.targetEntity.func_70068_e(this.npc);
          double u = 0.0D;
          double v = 0.0D;
          double w = 0.0D;
          if (this.movePosition != null) {
               u = this.movePosition.field_72450_a;
               v = this.movePosition.field_72448_b;
               w = this.movePosition.field_72449_c;
          }

          for(int y = -2; y <= 2; ++y) {
               double k = (double)MathHelper.floor(this.npc.getEntityBoundingBox().field_72338_b + (double)y);

               for(int x = -radius; x <= radius; ++x) {
                    double j = (double)MathHelper.floor(this.npc.field_70165_t + (double)x) + 0.5D;

                    for(int z = -radius; z <= radius; ++z) {
                         double l = (double)MathHelper.floor(this.npc.field_70161_v + (double)z) + 0.5D;
                         BlockPos pos = new BlockPos(j, k, l);
                         if (this.isOpaque(pos) && !this.isOpaque(pos.up()) && !this.isOpaque(pos.up(2))) {
                              Vec3d vec1 = new Vec3d(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u + (double)this.targetEntity.func_70047_e(), this.targetEntity.field_70161_v);
                              Vec3d vec2 = new Vec3d(j, k + (double)this.npc.func_70047_e(), l);
                              RayTraceResult movingobjectposition = this.world.func_72933_a(vec1, vec2);
                              if (movingobjectposition != null) {
                                   boolean weight = nearest ? this.targetEntity.getDistanceSq(j, k, l) <= dist : true;
                                   if (weight && (j != u || k != v || l != w)) {
                                        idealPos = new Vec3d(j, k, l);
                                        if (nearest) {
                                             dist = this.targetEntity.getDistanceSq(j, k, l);
                                        }
                                   }
                              }
                         }
                    }
               }
          }

          return idealPos;
     }

     private boolean isOpaque(BlockPos pos) {
          return this.world.getBlockState(pos).func_185914_p();
     }

     private boolean isLookingAway() {
          Vec3d vec3 = this.targetEntity.func_70676_i(1.0F).func_72432_b();
          Vec3d vec31 = new Vec3d(this.npc.field_70165_t - this.targetEntity.field_70165_t, this.npc.getEntityBoundingBox().field_72338_b + (double)(this.npc.height / 2.0F) - (this.targetEntity.field_70163_u + (double)this.targetEntity.func_70047_e()), this.npc.field_70161_v - this.targetEntity.field_70161_v);
          double d0 = vec31.func_72433_c();
          vec31 = vec31.func_72432_b();
          double d1 = vec3.func_72430_b(vec31);
          return d1 < 0.6D;
     }
}
