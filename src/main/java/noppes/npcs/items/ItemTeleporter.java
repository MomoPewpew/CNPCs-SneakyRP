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
          this.field_77777_bU = 1;
          this.setCreativeTab(CustomItems.tab);
     }

     public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
          ItemStack itemstack = player.func_184586_b(hand);
          if (!world.isRemote) {
               return new ActionResult(EnumActionResult.SUCCESS, itemstack);
          } else {
               CustomNpcs.proxy.openGui((EntityNPCInterface)null, EnumGuiType.NpcDimensions);
               return new ActionResult(EnumActionResult.SUCCESS, itemstack);
          }
     }

     public boolean onEntitySwing(EntityLivingBase par3EntityPlayer, ItemStack stack) {
          if (par3EntityPlayer.world.isRemote) {
               return false;
          } else {
               float f = 1.0F;
               float f1 = par3EntityPlayer.field_70127_C + (par3EntityPlayer.field_70125_A - par3EntityPlayer.field_70127_C) * f;
               float f2 = par3EntityPlayer.field_70126_B + (par3EntityPlayer.field_70177_z - par3EntityPlayer.field_70126_B) * f;
               double d0 = par3EntityPlayer.field_70169_q + (par3EntityPlayer.field_70165_t - par3EntityPlayer.field_70169_q) * (double)f;
               double d1 = par3EntityPlayer.field_70167_r + (par3EntityPlayer.field_70163_u - par3EntityPlayer.field_70167_r) * (double)f + 1.62D;
               double d2 = par3EntityPlayer.field_70166_s + (par3EntityPlayer.field_70161_v - par3EntityPlayer.field_70166_s) * (double)f;
               Vec3d vec3 = new Vec3d(d0, d1, d2);
               float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - 3.1415927F);
               float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - 3.1415927F);
               float f5 = -MathHelper.func_76134_b(-f1 * 0.017453292F);
               float f6 = MathHelper.func_76126_a(-f1 * 0.017453292F);
               float f7 = f4 * f5;
               float f8 = f3 * f5;
               double d3 = 80.0D;
               Vec3d vec31 = vec3.func_72441_c((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
               RayTraceResult movingobjectposition = par3EntityPlayer.world.func_72901_a(vec3, vec31, true);
               if (movingobjectposition == null) {
                    return false;
               } else {
                    Vec3d vec32 = par3EntityPlayer.func_70676_i(f);
                    boolean flag = false;
                    float f9 = 1.0F;
                    List list = par3EntityPlayer.world.func_72839_b(par3EntityPlayer, par3EntityPlayer.getEntityBoundingBox().expand(vec32.field_72450_a * d3, vec32.field_72448_b * d3, vec32.field_72449_c * d3).expand((double)f9, (double)f9, (double)f9));

                    for(int i = 0; i < list.size(); ++i) {
                         Entity entity = (Entity)list.get(i);
                         if (entity.func_70067_L()) {
                              float f10 = entity.func_70111_Y();
                              AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double)f10, (double)f10, (double)f10);
                              if (axisalignedbb.func_72318_a(vec3)) {
                                   flag = true;
                              }
                         }
                    }

                    if (flag) {
                         return false;
                    } else {
                         if (movingobjectposition.field_72313_a == Type.BLOCK) {
                              BlockPos pos;
                              for(pos = movingobjectposition.func_178782_a(); par3EntityPlayer.world.getBlockState(pos).getBlock() != Blocks.field_150350_a; pos = pos.func_177984_a()) {
                              }

                              par3EntityPlayer.func_70634_a((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 1.0F), (double)((float)pos.getZ() + 0.5F));
                         }

                         return true;
                    }
               }
          }
     }

     public Item setUnlocalizedName(String name) {
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return super.setUnlocalizedName(name);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.DimensionsGet || e == EnumPacketServer.DimensionTeleport;
     }
}
