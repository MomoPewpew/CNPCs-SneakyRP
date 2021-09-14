package noppes.npcs.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockNpcRedstone extends BlockInterface implements IPermission {
     public static final PropertyBool ACTIVE = PropertyBool.func_177716_a("active");

     public BlockNpcRedstone() {
          super(Material.field_151576_e);
     }

     public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (par1World.isRemote) {
               return false;
          } else {
               ItemStack currentItem = player.inventory.getCurrentItem();
               if (currentItem != null && currentItem.getItem() == CustomItems.wand && CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.RedstoneBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
                    return true;
               } else {
                    return false;
               }
          }
     }

     public void func_176213_c(World par1World, BlockPos pos, IBlockState state) {
          par1World.func_175685_c(pos, this, false);
          par1World.func_175685_c(pos.down(), this, false);
          par1World.func_175685_c(pos.up(), this, false);
          par1World.func_175685_c(pos.func_177976_e(), this, false);
          par1World.func_175685_c(pos.func_177974_f(), this, false);
          par1World.func_175685_c(pos.func_177968_d(), this, false);
          par1World.func_175685_c(pos.north(), this, false);
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entityliving, ItemStack item) {
          if (entityliving instanceof EntityPlayer && !world.isRemote) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entityliving, EnumGuiType.RedstoneBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
          }

     }

     public void func_176206_d(World par1World, BlockPos pos, IBlockState state) {
          this.func_176213_c(par1World, pos, state);
     }

     public int func_180656_a(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
          return this.isActivated(state);
     }

     public int func_176211_b(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
          return this.isActivated(state);
     }

     public boolean func_149744_f(IBlockState state) {
          return true;
     }

     public int getMetaFromState(IBlockState state) {
          return (Boolean)state.getValue(ACTIVE) ? 1 : 0;
     }

     public IBlockState getStateFromMeta(int meta) {
          return this.getDefaultState().withProperty(ACTIVE, false);
     }

     protected BlockStateContainer func_180661_e() {
          return new BlockStateContainer(this, new IProperty[]{ACTIVE});
     }

     public int isActivated(IBlockState state) {
          return (Boolean)state.getValue(ACTIVE) ? 15 : 0;
     }

     public TileEntity func_149915_a(World var1, int var2) {
          return new TileRedstoneBlock();
     }

     public EnumBlockRenderType func_149645_b(IBlockState state) {
          return EnumBlockRenderType.MODEL;
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SaveTileEntity;
     }
}
