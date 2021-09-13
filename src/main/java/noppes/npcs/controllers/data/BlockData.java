package noppes.npcs.controllers.data;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class BlockData {
     public BlockPos pos;
     public IBlockState state;
     public NBTTagCompound tile;
     private ItemStack stack;

     public BlockData(BlockPos pos, IBlockState state, NBTTagCompound tile) {
          this.pos = pos;
          this.state = state;
          this.tile = tile;
     }

     public NBTTagCompound getNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74768_a("BuildX", this.pos.func_177958_n());
          compound.func_74768_a("BuildY", this.pos.func_177956_o());
          compound.func_74768_a("BuildZ", this.pos.func_177952_p());
          compound.func_74778_a("Block", ((ResourceLocation)Block.field_149771_c.func_177774_c(this.state.func_177230_c())).toString());
          compound.func_74768_a("Meta", this.state.func_177230_c().func_176201_c(this.state));
          if (this.tile != null) {
               compound.func_74782_a("Tile", this.tile);
          }

          return compound;
     }

     public static BlockData getData(NBTTagCompound compound) {
          BlockPos pos = new BlockPos(compound.func_74762_e("BuildX"), compound.func_74762_e("BuildY"), compound.func_74762_e("BuildZ"));
          Block b = Block.func_149684_b(compound.func_74779_i("Block"));
          if (b == null) {
               return null;
          } else {
               IBlockState state = b.func_176203_a(compound.func_74762_e("Meta"));
               NBTTagCompound tile = null;
               if (compound.func_74764_b("Tile")) {
                    tile = compound.func_74775_l("Tile");
               }

               return new BlockData(pos, state, tile);
          }
     }

     public ItemStack getStack() {
          if (this.stack == null) {
               this.stack = new ItemStack(this.state.func_177230_c(), 1, this.state.func_177230_c().func_180651_a(this.state));
          }

          return this.stack;
     }
}
