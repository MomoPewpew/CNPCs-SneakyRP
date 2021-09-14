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

	public void fall(float distance, float damageMultiplier) {
		if (!this.canFly()) {
			super.fall(distance, damageMultiplier);
		}

	}

	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
		if (!this.canFly()) {
			super.updateFallState(y, onGroundIn, state, pos);
		}

	}

	public void travel(float par1, float par2, float par3) {
		if (!this.canFly()) {
			super.travel(par1, par2, par3);
		} else {
			if (!this.isInWater() && this.ais.movementType == 2) {
				this.motionY = -0.15D;
			}

			if (this.isInWater() && this.ais.movementType == 1) {
				this.moveRelative(par1, par2, par3, 0.02F);
				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				this.motionX *= 0.800000011920929D;
				this.motionY *= 0.800000011920929D;
				this.motionZ *= 0.800000011920929D;
			} else if (this.isInLava()) {
				this.moveRelative(par1, par2, par3, 0.02F);
				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			} else {
				float f2 = 0.91F;
				if (this.onGround) {
					f2 = this.world
							.getBlockState(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ))
							.getBlock().slipperiness * 0.91F;
				}

				float f3 = 0.16277136F / (f2 * f2 * f2);
				this.moveRelative(par1, par2, par3, this.onGround ? 0.1F * f3 : 0.02F);
				f2 = 0.91F;
				if (this.onGround) {
					f2 = this.world
							.getBlockState(new BlockPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ))
							.getBlock().slipperiness * 0.91F;
				}

				this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				this.motionX *= (double) f2;
				this.motionY *= (double) f2;
				this.motionZ *= (double) f2;
			}

			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f4 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
			if (f4 > 1.0F) {
				f4 = 1.0F;
			}

			this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		}
	}

	public boolean isOnLadder() {
		return false;
	}
}
