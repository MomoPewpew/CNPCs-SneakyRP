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
     public static final PropertyInteger ROTATION = PropertyInteger.func_177719_a("rotation", 0, 3);

     public BlockCarpentryBench() {
          super(Material.field_151575_d);
          this.func_149672_a(SoundType.field_185848_a);
     }

     public boolean func_180639_a(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (!par1World.isRemote) {
               player.openGui(CustomNpcs.instance, EnumGuiType.PlayerAnvil.ordinal(), par1World, pos.getX(), pos.getY(), pos.getZ());
          }

          return true;
     }

     public boolean func_149662_c(IBlockState state) {
          return false;
     }

     public boolean func_149686_d(IBlockState state) {
          return false;
     }

     public int func_176201_c(IBlockState state) {
          return (Integer)state.getValue(ROTATION);
     }

     public IBlockState func_176203_a(int meta) {
          return this.getDefaultState().func_177226_a(ROTATION, meta % 4);
     }

     protected BlockStateContainer func_180661_e() {
          return new BlockStateContainer(this, new IProperty[]{ROTATION});
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int var6 = MathHelper.floor((double)(entity.field_70177_z / 90.0F) + 0.5D) & 3;
          world.func_180501_a(pos, state.func_177226_a(ROTATION, var6), 2);
     }

     public TileEntity func_149915_a(World var1, int var2) {
          return new TileBlockAnvil();
     }
}
