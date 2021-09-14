package noppes.npcs.api.wrapper;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.IPos;

public class BlockPosWrapper implements IPos {
     private BlockPos blockPos;

     public BlockPosWrapper(BlockPos pos) {
          this.blockPos = pos;
     }

     public int getX() {
          return this.blockPos.getX();
     }

     public int getY() {
          return this.blockPos.getY();
     }

     public int getZ() {
          return this.blockPos.getZ();
     }

     public IPos up() {
          return new BlockPosWrapper(this.blockPos.up());
     }

     public IPos up(int n) {
          return new BlockPosWrapper(this.blockPos.up(n));
     }

     public IPos down() {
          return new BlockPosWrapper(this.blockPos.down());
     }

     public IPos down(int n) {
          return new BlockPosWrapper(this.blockPos.down(n));
     }

     public IPos north() {
          return new BlockPosWrapper(this.blockPos.north());
     }

     public IPos north(int n) {
          return new BlockPosWrapper(this.blockPos.north(n));
     }

     public IPos east() {
          return new BlockPosWrapper(this.blockPos.north());
     }

     public IPos east(int n) {
          return new BlockPosWrapper(this.blockPos.north(n));
     }

     public IPos south() {
          return new BlockPosWrapper(this.blockPos.north());
     }

     public IPos south(int n) {
          return new BlockPosWrapper(this.blockPos.north(n));
     }

     public IPos west() {
          return new BlockPosWrapper(this.blockPos.north());
     }

     public IPos west(int n) {
          return new BlockPosWrapper(this.blockPos.north(n));
     }

     public IPos add(int x, int y, int z) {
          return new BlockPosWrapper(this.blockPos.add(x, y, z));
     }

     public IPos add(IPos pos) {
          return new BlockPosWrapper(this.blockPos.add(pos.getMCBlockPos()));
     }

     public IPos subtract(int x, int y, int z) {
          return new BlockPosWrapper(this.blockPos.add(-x, -y, -z));
     }

     public IPos subtract(IPos pos) {
          return new BlockPosWrapper(this.blockPos.add(-pos.getX(), -pos.getY(), -pos.getZ()));
     }

     public IPos offset(int direction) {
          return new BlockPosWrapper(this.blockPos.offset(EnumFacing.field_82609_l[direction]));
     }

     public IPos offset(int direction, int n) {
          return new BlockPosWrapper(this.blockPos.offset(EnumFacing.field_82609_l[direction], n));
     }

     public BlockPos getMCBlockPos() {
          return this.blockPos;
     }

     public double[] normalize() {
          double d = Math.sqrt((double)(this.blockPos.getX() * this.blockPos.getX() + this.blockPos.getY() * this.blockPos.getY() + this.blockPos.getZ() * this.blockPos.getZ()));
          return new double[]{(double)this.getX() / d, (double)this.getY() / d, (double)this.getZ() / d};
     }

     public double distanceTo(IPos pos) {
          double d0 = (double)(this.getX() - pos.getX());
          double d1 = (double)(this.getY() - pos.getY());
          double d2 = (double)(this.getZ() - pos.getZ());
          return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
     }
}
