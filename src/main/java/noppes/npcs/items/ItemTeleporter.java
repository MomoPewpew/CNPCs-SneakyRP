package noppes.npcs.items;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class ItemTeleporter extends Item implements IPermission {
	public ItemTeleporter() {
		this.maxStackSize = 1;
		this.setCreativeTab(CustomItems.tab);
	}

	public ActionResult onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (!world.isRemote) {
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		} else {
			CustomNpcs.proxy.openGui((EntityNPCInterface) null, EnumGuiType.NpcDimensions);
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		}
	}

	public boolean onEntitySwing(EntityLivingBase par3EntityPlayer, ItemStack stack) {
		if (par3EntityPlayer.world.isRemote) {
			return false;
		} else {
			float f = 1.0F;
			float f1 = par3EntityPlayer.prevRotationPitch
					+ (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f;
			float f2 = par3EntityPlayer.prevRotationYaw
					+ (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f;
			double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double) f;
			double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double) f
					+ 1.62D;
			double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double) f;
			Vec3d vec3 = new Vec3d(d0, d1, d2);
			float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
			float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
			float f5 = -MathHelper.cos(-f1 * 0.017453292F);
			float f6 = MathHelper.sin(-f1 * 0.017453292F);
			float f7 = f4 * f5;
			float f8 = f3 * f5;
			double d3 = 80.0D;
			Vec3d vec31 = vec3.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
			RayTraceResult movingobjectposition = par3EntityPlayer.world.rayTraceBlocks(vec3, vec31, true);
			if (movingobjectposition == null) {
				return false;
			} else {
				Vec3d vec32 = par3EntityPlayer.getLook(f);
				boolean flag = false;
				float f9 = 1.0F;
				List list = par3EntityPlayer.world.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer,
						par3EntityPlayer.getEntityBoundingBox().grow(vec32.x * d3, vec32.y * d3, vec32.z * d3)
								.grow((double) f9, (double) f9, (double) f9));

				for (int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity) list.get(i);
					if (entity.canBeCollidedWith()) {
						float f10 = entity.getCollisionBorderSize();
						AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow((double) f10, (double) f10,
								(double) f10);
						if (axisalignedbb.contains(vec3)) {
							flag = true;
						}
					}
				}

				if (flag) {
					return false;
				} else {
					if (movingobjectposition.typeOfHit == Type.BLOCK) {
						BlockPos pos;
						for (pos = movingobjectposition.getBlockPos(); par3EntityPlayer.world.getBlockState(pos)
								.getBlock() != Blocks.AIR; pos = pos.up()) {
						}

						par3EntityPlayer.setPositionAndUpdate((double) ((float) pos.getX() + 0.5F),
								(double) ((float) pos.getY() + 1.0F), (double) ((float) pos.getZ() + 0.5F));
					}

					return true;
				}
			}
		}
	}

	public Item setTranslationKey(String name) {
		this.setRegistryName(new ResourceLocation("customnpcs", name));
		return super.setTranslationKey(name);
	}

	public boolean isAllowed(EnumPacketServer e) {
		return e == EnumPacketServer.DimensionsGet || e == EnumPacketServer.DimensionTeleport;
	}
}
