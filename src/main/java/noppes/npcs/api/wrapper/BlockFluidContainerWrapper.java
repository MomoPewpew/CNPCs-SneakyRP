package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import noppes.npcs.api.block.IBlockFluidContainer;

public class BlockFluidContainerWrapper extends BlockWrapper implements IBlockFluidContainer {
     private BlockFluidBase block;

     public BlockFluidContainerWrapper(World world, Block block, BlockPos pos) {
          super(world, block, pos);
          this.block = (BlockFluidBase)block;
     }

     public float getFluidPercentage() {
          return this.block.getFilledPercentage(this.world.getMCWorld(), this.pos);
     }

     public float getFuildDensity() {
          BlockFluidBase var10000 = this.block;
          return (float)BlockFluidBase.getDensity(this.world.getMCWorld(), this.pos);
     }

     public float getFuildTemperature() {
          BlockFluidBase var10000 = this.block;
          return (float)BlockFluidBase.getTemperature(this.world.getMCWorld(), this.pos);
     }

     public float getFluidValue() {
          return (float)this.block.getQuantaValue(this.world.getMCWorld(), this.pos);
     }

     public String getFluidName() {
          return this.block.getFluid().getName();
     }
}
