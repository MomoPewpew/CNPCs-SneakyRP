package noppes.npcs.ai;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAmbushTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase targetEntity;
     private double shelterX;
     private double shelterY;
     private double shelterZ;
     private double movementSpeed;
     private double distance;
     private int delay = 0;
     private World world;
     private int tick;

     public EntityAIAmbushTarget(EntityNPCInterface par1EntityCreature, double par2) {
          this.npc = par1EntityCreature;
          this.movementSpeed = par2;
          this.world = par1EntityCreature.world;
          this.setMutexBits(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean shouldExecute() {
          this.targetEntity = this.npc.getAttackTarget();
          this.distance = (double)this.npc.ais.getTacticalRange();
          if (this.targetEntity != null && !this.npc.isInRange(this.targetEntity, this.distance) && this.npc.canSee(this.targetEntity) && this.delay-- <= 0) {
               Vec3d vec3 = this.findHidingSpot();
               if (vec3 == null) {
                    this.delay = 10;
                    return false;
               } else {
                    this.shelterX = vec3.x;
                    this.shelterY = vec3.y;
                    this.shelterZ = vec3.z;
                    return true;
               }
          } else {
               return false;
          }
     }

     public boolean shouldContinueExecuting() {
          boolean shouldHide = !this.npc.isInRange(this.targetEntity, this.distance);
          boolean isSeen = this.npc.canSee(this.targetEntity);
          return !this.npc.getNavigator().noPath() && shouldHide || !isSeen && (shouldHide || this.npc.ais.directLOS);
     }

     public void startExecuting() {
          this.npc.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
     }

     public void resetTask() {
          this.npc.getNavigator().clearPath();
          if (this.npc.getAttackTarget() == null && this.targetEntity != null) {
               this.npc.setAttackTarget(this.targetEntity);
          }

          if (!this.npc.isInRange(this.targetEntity, this.distance)) {
               this.delay = 60;
          }

     }

     public void updateTask() {
          this.npc.getLookHelper().setLookPositionWithEntity(this.targetEntity, 30.0F, 30.0F);
     }

     private Vec3d findHidingSpot() {
          Random random = this.npc.getRNG();
          Vec3d idealPos = null;

          for(int i = 1; i <= 8; ++i) {
               for(int y = -2; y <= 2; ++y) {
                    double k = (double)MathHelper.floor(this.npc.getEntityBoundingBox().minY + (double)y);

                    for(int x = -i; x <= i; ++x) {
                         double j = (double)MathHelper.floor(this.npc.posX + (double)x) + 0.5D;

                         for(int z = -i; z <= i; ++z) {
                              double l = (double)MathHelper.floor(this.npc.posZ + (double)z) + 0.5D;
                              if (this.isOpaque((int)j, (int)k, (int)l) && !this.isOpaque((int)j, (int)k + 1, (int)l) && this.isOpaque((int)j, (int)k + 2, (int)l)) {
                                   Vec3d vec1 = new Vec3d(this.targetEntity.posX, this.targetEntity.posY + (double)this.targetEntity.getEyeHeight(), this.targetEntity.posZ);
                                   Vec3d vec2 = new Vec3d(j, k + (double)this.npc.getEyeHeight(), l);
                                   RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, vec2);
                                   if (movingobjectposition != null && this.shelterX != j && this.shelterY != k && this.shelterZ != l) {
                                        idealPos = new Vec3d(j, k, l);
                                   }
                              }
                         }
                    }
               }

               if (idealPos != null) {
                    return idealPos;
               }
          }

          this.delay = 60;
          return null;
     }

     private boolean isOpaque(int x, int y, int z) {
          return this.world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube();
     }
}
