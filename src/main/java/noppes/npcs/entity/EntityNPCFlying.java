package noppes.npcs.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityNPCFlying extends EntityNPCInterface {
     public EntityNPCFlying(World world) {
          super(world);
     }

     public boolean canFly() {
          return this.ais.movementType > 0;
     }

     public void func_180430_e(float distance, float damageMultiplier) {
          if (!this.canFly()) {
               super.func_180430_e(distance, damageMultiplier);
          }

     }

     protected void func_184231_a(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
          if (!this.canFly()) {
               super.func_184231_a(y, onGroundIn, state, pos);
          }

     }

     public void func_191986_a(float par1, float par2, float par3) {
          if (!this.canFly()) {
               super.func_191986_a(par1, par2, par3);
          } else {
               if (!this.func_70090_H() && this.ais.movementType == 2) {
                    this.motionY = -0.15D;
               }

               if (this.func_70090_H() && this.ais.movementType == 1) {
                    this.func_191958_b(par1, par2, par3, 0.02F);
                    this.func_70091_d(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.800000011920929D;
                    this.motionY *= 0.800000011920929D;
                    this.motionZ *= 0.800000011920929D;
               } else if (this.func_180799_ab()) {
                    this.func_191958_b(par1, par2, par3, 0.02F);
                    this.func_70091_d(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
               } else {
                    float f2 = 0.91F;
                    if (this.field_70122_E) {
                         f2 = this.world.getBlockState(new BlockPos(this.field_70165_t, this.getEntityBoundingBox().field_72338_b - 1.0D, this.field_70161_v)).getBlock().field_149765_K * 0.91F;
                    }

                    float f3 = 0.16277136F / (f2 * f2 * f2);
                    this.func_191958_b(par1, par2, par3, this.field_70122_E ? 0.1F * f3 : 0.02F);
                    f2 = 0.91F;
                    if (this.field_70122_E) {
                         f2 = this.world.getBlockState(new BlockPos(this.field_70165_t, this.getEntityBoundingBox().field_72338_b - 1.0D, this.field_70161_v)).getBlock().field_149765_K * 0.91F;
                    }

                    this.func_70091_d(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= (double)f2;
                    this.motionY *= (double)f2;
                    this.motionZ *= (double)f2;
               }

               this.field_184618_aE = this.field_70721_aZ;
               double d1 = this.field_70165_t - this.field_70169_q;
               double d0 = this.field_70161_v - this.field_70166_s;
               float f4 = MathHelper.func_76133_a(d1 * d1 + d0 * d0) * 4.0F;
               if (f4 > 1.0F) {
                    f4 = 1.0F;
               }

               this.field_70721_aZ += (f4 - this.field_70721_aZ) * 0.4F;
               this.field_184619_aG += this.field_70721_aZ;
          }
     }

     public boolean func_70617_f_() {
          return false;
     }
}
