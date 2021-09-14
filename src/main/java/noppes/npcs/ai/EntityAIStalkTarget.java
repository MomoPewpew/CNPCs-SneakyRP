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
          this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean shouldExecute() {
          this.targetEntity = this.npc.getAttackTarget();
          if (this.targetEntity != null && this.tick-- <= 0) {
               return !this.npc.isInRange(this.targetEntity, (double)this.npc.ais.getTacticalRange());
          } else {
               return false;
          }
     }

     public void resetTask() {
          this.npc.getNavigator().clearPath();
          if (this.npc.getAttackTarget() == null && this.targetEntity != null) {
               this.npc.setAttackTarget(this.targetEntity);
          }

          if (this.npc.getRangedTask() != null) {
               this.npc.getRangedTask().navOverride(false);
          }

     }

     public void startExecuting() {
          if (this.npc.getRangedTask() != null) {
               this.npc.getRangedTask().navOverride(true);
          }

     }

     public void updateTask() {
          this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
          if (this.npc.getNavigator().noPath() || this.overRide) {
               if (this.isLookingAway()) {
                    this.movePosition = this.stalkTarget();
                    if (this.movePosition != null) {
                         this.npc.getNavigator().tryMoveToXYZ(this.movePosition.x, this.movePosition.y, this.movePosition.z, 1.0D);
                         this.overRide = false;
                    } else {
                         this.tick = 100;
                    }
               } else if (this.npc.canSee(this.targetEntity)) {
                    this.movePosition = this.hideFromTarget();
                    if (this.movePosition != null) {
                         this.npc.getNavigator().tryMoveToXYZ(this.movePosition.x, this.movePosition.y, this.movePosition.z, 1.33D);
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
          double dist = this.targetEntity.getDistanceSq(this.npc);
          double u = 0.0D;
          double v = 0.0D;
          double w = 0.0D;
          if (this.movePosition != null) {
               u = this.movePosition.x;
               v = this.movePosition.y;
               w = this.movePosition.z;
          }

          for(int y = -2; y <= 2; ++y) {
               double k = (double)MathHelper.floor(this.npc.getEntityBoundingBox().field_72338_b + (double)y);

               for(int x = -radius; x <= radius; ++x) {
                    double j = (double)MathHelper.floor(this.npc.field_70165_t + (double)x) + 0.5D;

                    for(int z = -radius; z <= radius; ++z) {
                         double l = (double)MathHelper.floor(this.npc.field_70161_v + (double)z) + 0.5D;
                         BlockPos pos = new BlockPos(j, k, l);
                         if (this.isOpaque(pos) && !this.isOpaque(pos.up()) && !this.isOpaque(pos.up(2))) {
                              Vec3d vec1 = new Vec3d(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u + (double)this.targetEntity.getEyeHeight(), this.targetEntity.field_70161_v);
                              Vec3d vec2 = new Vec3d(j, k + (double)this.npc.getEyeHeight(), l);
                              RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, vec2);
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
          return this.world.getBlockState(pos).isOpaqueCube();
     }

     private boolean isLookingAway() {
          Vec3d vec3 = this.targetEntity.getLook(1.0F).normalize();
          Vec3d vec31 = new Vec3d(this.npc.field_70165_t - this.targetEntity.field_70165_t, this.npc.getEntityBoundingBox().field_72338_b + (double)(this.npc.height / 2.0F) - (this.targetEntity.field_70163_u + (double)this.targetEntity.getEyeHeight()), this.npc.field_70161_v - this.targetEntity.field_70161_v);
          double d0 = vec31.lengthVector();
          vec31 = vec31.normalize();
          double d1 = vec3.dotProduct(vec31);
          return d1 < 0.6D;
     }
}
