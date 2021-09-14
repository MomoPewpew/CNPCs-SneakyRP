package noppes.npcs.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;

public class EntityAIMoveIndoors extends EntityAIBase {
     private EntityCreature theCreature;
     private double shelterX;
     private double shelterY;
     private double shelterZ;
     private World world;

     public EntityAIMoveIndoors(EntityCreature par1EntityCreature) {
          this.theCreature = par1EntityCreature;
          this.world = par1EntityCreature.world;
          this.setMutexBits(AiMutex.PASSIVE);
     }

     public boolean shouldExecute() {
          if ((!this.theCreature.world.isDaytime() || this.theCreature.world.isRaining()) && !this.theCreature.world.provider.hasSkyLight()) {
               BlockPos pos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);
               if (!this.world.canSeeSky(pos) && this.world.getLight(pos) > 8) {
                    return false;
               } else {
                    Vec3d var1 = this.findPossibleShelter();
                    if (var1 == null) {
                         return false;
                    } else {
                         this.shelterX = var1.x;
                         this.shelterY = var1.y;
                         this.shelterZ = var1.z;
                         return true;
                    }
               }
          } else {
               return false;
          }
     }

     public boolean shouldContinueExecuting() {
          return !this.theCreature.getNavigator().noPath();
     }

     public void startExecuting() {
          this.theCreature.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, 1.0D);
     }

     private Vec3d findPossibleShelter() {
          Random random = this.theCreature.getRNG();
          BlockPos blockpos = new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ);

          for(int i = 0; i < 10; ++i) {
               BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
               if (!this.world.canSeeSky(blockpos1) && this.theCreature.getBlockPathWeight(blockpos1) < 0.0F) {
                    return new Vec3d((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
               }
          }

          return null;
     }
}
