package noppes.npcs.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.constants.EnumGuiType;

public class BlockCarpentryBench extends BlockInterface {
     public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

     public BlockCarpentryBench() {
          super(Material.field_151575_d);
          this.setSoundType(SoundType.field_185848_a);
     }

     public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (!par1World.isRemote) {
               player.openGui(CustomNpcs.instance, EnumGuiType.PlayerAnvil.ordinal(), par1World, pos.getX(), pos.getY(), pos.getZ());
          }

          return true;
     }

     public boolean isOpaqueCube(IBlockState state) {
          return false;
     }

     public boolean isFullCube(IBlockState state) {
          return false;
     }

     public int getMetaFromState(IBlockState state) {
          return (Integer)state.getValue(ROTATION);
     }

     public IBlockState getStateFromMeta(int meta) {
          return this.getDefaultState().withProperty(ROTATION, meta % 4);
     }

     protected BlockStateContainer createBlockState() {
          return new BlockStateContainer(this, new IProperty[]{ROTATION});
     }

     public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int var6 = MathHelper.floor((double)(entity.field_70177_z / 90.0F) + 0.5D) & 3;
          world.setBlockState(pos, state.withProperty(ROTATION, var6), 2);
     }

     public TileEntity createNewTileEntity(World var1, int var2) {
          return new TileBlockAnvil();
     }
}
