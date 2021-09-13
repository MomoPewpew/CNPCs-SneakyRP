package noppes.npcs.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileDoor;

public abstract class BlockNpcDoorInterface extends BlockDoor implements ITileEntityProvider {
     public BlockNpcDoorInterface() {
          super(Material.field_151575_d);
          this.field_149758_A = true;
     }

     public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
          super.func_180663_b(worldIn, pos, state);
          worldIn.func_175713_t(pos);
     }

     public TileEntity func_149915_a(World worldIn, int meta) {
          return new TileDoor();
     }

     public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
          return new ItemStack(CustomItems.scriptedDoorTool, 1, this.func_180651_a(state));
     }

     public boolean hasTileEntity(IBlockState state) {
          return true;
     }

     public Item func_180660_a(IBlockState state, Random rand, int fortune) {
          return null;
     }

     public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
          IBlockState iblockstate1;
          if (state.func_177229_b(field_176523_O) == EnumDoorHalf.LOWER) {
               iblockstate1 = worldIn.func_180495_p(pos.func_177984_a());
               if (iblockstate1.func_177230_c() == this) {
                    state = state.func_177226_a(field_176521_M, iblockstate1.func_177229_b(field_176521_M)).func_177226_a(field_176522_N, iblockstate1.func_177229_b(field_176522_N));
               }
          } else {
               iblockstate1 = worldIn.func_180495_p(pos.func_177977_b());
               if (iblockstate1.func_177230_c() == this) {
                    state = state.func_177226_a(field_176520_a, iblockstate1.func_177229_b(field_176520_a)).func_177226_a(field_176519_b, iblockstate1.func_177229_b(field_176519_b));
               }
          }

          return state;
     }

     public Block setUnlocalizedName(String name) {
          this.setRegistryName(name);
          return super.setUnlocalizedName(name);
     }
}
