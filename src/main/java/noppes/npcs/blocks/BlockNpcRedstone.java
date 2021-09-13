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

     public boolean func_180639_a(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (par1World.field_72995_K) {
               return false;
          } else {
               ItemStack currentItem = player.field_71071_by.func_70448_g();
               if (currentItem != null && currentItem.func_77973_b() == CustomItems.wand && CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.RedstoneBlock, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
                    return true;
               } else {
                    return false;
               }
          }
     }

     public void func_176213_c(World par1World, BlockPos pos, IBlockState state) {
          par1World.func_175685_c(pos, this, false);
          par1World.func_175685_c(pos.func_177977_b(), this, false);
          par1World.func_175685_c(pos.func_177984_a(), this, false);
          par1World.func_175685_c(pos.func_177976_e(), this, false);
          par1World.func_175685_c(pos.func_177974_f(), this, false);
          par1World.func_175685_c(pos.func_177968_d(), this, false);
          par1World.func_175685_c(pos.func_177978_c(), this, false);
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entityliving, ItemStack item) {
          if (entityliving instanceof EntityPlayer && !world.field_72995_K) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entityliving, EnumGuiType.RedstoneBlock, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
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

     public int func_176201_c(IBlockState state) {
          return (Boolean)state.func_177229_b(ACTIVE) ? 1 : 0;
     }

     public IBlockState func_176203_a(int meta) {
          return this.func_176223_P().func_177226_a(ACTIVE, false);
     }

     protected BlockStateContainer func_180661_e() {
          return new BlockStateContainer(this, new IProperty[]{ACTIVE});
     }

     public int isActivated(IBlockState state) {
          return (Boolean)state.func_177229_b(ACTIVE) ? 15 : 0;
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
