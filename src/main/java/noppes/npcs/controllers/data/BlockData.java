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
          compound.setInteger("BuildX", this.pos.getX());
          compound.setInteger("BuildY", this.pos.getY());
          compound.setInteger("BuildZ", this.pos.getZ());
          compound.setString("Block", ((ResourceLocation)Block.REGISTRY.func_177774_c(this.state.getBlock())).toString());
          compound.setInteger("Meta", this.state.getBlock().func_176201_c(this.state));
          if (this.tile != null) {
               compound.setTag("Tile", this.tile);
          }

          return compound;
     }

     public static BlockData getData(NBTTagCompound compound) {
          BlockPos pos = new BlockPos(compound.getInteger("BuildX"), compound.getInteger("BuildY"), compound.getInteger("BuildZ"));
          Block b = Block.func_149684_b(compound.getString("Block"));
          if (b == null) {
               return null;
          } else {
               IBlockState state = b.func_176203_a(compound.getInteger("Meta"));
               NBTTagCompound tile = null;
               if (compound.hasKey("Tile")) {
                    tile = compound.getCompoundTag("Tile");
               }

               return new BlockData(pos, state, tile);
          }
     }

     public ItemStack getStack() {
          if (this.stack == null) {
               this.stack = new ItemStack(this.state.getBlock(), 1, this.state.getBlock().func_180651_a(this.state));
          }

          return this.stack;
     }
}
