package noppes.npcs.blocks.tiles;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.controllers.data.Availability;

public class TileRedstoneBlock extends TileNpcEntity implements ITickable {
     public int onRange = 12;
     public int offRange = 20;
     public int onRangeX = 12;
     public int onRangeY = 12;
     public int onRangeZ = 12;
     public int offRangeX = 20;
     public int offRangeY = 20;
     public int offRangeZ = 20;
     public boolean isDetailed = false;
     public Availability availability = new Availability();
     public boolean isActivated = false;
     private int ticks = 10;

     public void func_73660_a() {
          if (!this.field_145850_b.isRemote) {
               --this.ticks;
               if (this.ticks <= 0) {
                    this.ticks = this.onRange > 10 ? 20 : 10;
                    Block block = this.func_145838_q();
                    if (block != null && block instanceof BlockNpcRedstone) {
                         if (CustomNpcs.FreezeNPCs) {
                              if (this.isActivated) {
                                   this.setActive(block, false);
                              }

                         } else {
                              int x;
                              int y;
                              int z;
                              List list;
                              Iterator var6;
                              EntityPlayer player;
                              if (!this.isActivated) {
                                   x = this.isDetailed ? this.onRangeX : this.onRange;
                                   y = this.isDetailed ? this.onRangeY : this.onRange;
                                   z = this.isDetailed ? this.onRangeZ : this.onRange;
                                   list = this.getPlayerList(x, y, z);
                                   if (list.isEmpty()) {
                                        return;
                                   }

                                   var6 = list.iterator();

                                   while(var6.hasNext()) {
                                        player = (EntityPlayer)var6.next();
                                        if (this.availability.isAvailable(player)) {
                                             this.setActive(block, true);
                                             return;
                                        }
                                   }
                              } else {
                                   x = this.isDetailed ? this.offRangeX : this.offRange;
                                   y = this.isDetailed ? this.offRangeY : this.offRange;
                                   z = this.isDetailed ? this.offRangeZ : this.offRange;
                                   list = this.getPlayerList(x, y, z);
                                   var6 = list.iterator();

                                   while(var6.hasNext()) {
                                        player = (EntityPlayer)var6.next();
                                        if (this.availability.isAvailable(player)) {
                                             return;
                                        }
                                   }

                                   this.setActive(block, false);
                              }

                         }
                    }
               }
          }
     }

     public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
          return oldState.getBlock() != newState.getBlock();
     }

     private void setActive(Block block, boolean bo) {
          this.isActivated = bo;
          IBlockState state = block.getDefaultState().func_177226_a(BlockNpcRedstone.ACTIVE, this.isActivated);
          this.field_145850_b.func_180501_a(this.field_174879_c, state, 2);
          this.markDirty();
          this.field_145850_b.func_184138_a(this.field_174879_c, state, state, 3);
          block.func_176213_c(this.field_145850_b, this.field_174879_c, state);
     }

     private List getPlayerList(int x, int y, int z) {
          return this.field_145850_b.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB((double)this.field_174879_c.getX(), (double)this.field_174879_c.getY(), (double)this.field_174879_c.getZ(), (double)(this.field_174879_c.getX() + 1), (double)(this.field_174879_c.getY() + 1), (double)(this.field_174879_c.getZ() + 1))).expand((double)x, (double)y, (double)z));
     }

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          this.onRange = compound.getInteger("BlockOnRange");
          this.offRange = compound.getInteger("BlockOffRange");
          this.isDetailed = compound.getBoolean("BlockIsDetailed");
          if (compound.hasKey("BlockOnRangeX")) {
               this.isDetailed = true;
               this.onRangeX = compound.getInteger("BlockOnRangeX");
               this.onRangeY = compound.getInteger("BlockOnRangeY");
               this.onRangeZ = compound.getInteger("BlockOnRangeZ");
               this.offRangeX = compound.getInteger("BlockOffRangeX");
               this.offRangeY = compound.getInteger("BlockOffRangeY");
               this.offRangeZ = compound.getInteger("BlockOffRangeZ");
          }

          if (compound.hasKey("BlockActivated")) {
               this.isActivated = compound.getBoolean("BlockActivated");
          }

          this.availability.readFromNBT(compound);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("BlockOnRange", this.onRange);
          compound.setInteger("BlockOffRange", this.offRange);
          compound.setBoolean("BlockActivated", this.isActivated);
          compound.setBoolean("BlockIsDetailed", this.isDetailed);
          if (this.isDetailed) {
               compound.setInteger("BlockOnRangeX", this.onRangeX);
               compound.setInteger("BlockOnRangeY", this.onRangeY);
               compound.setInteger("BlockOnRangeZ", this.onRangeZ);
               compound.setInteger("BlockOffRangeX", this.offRangeX);
               compound.setInteger("BlockOffRangeY", this.offRangeY);
               compound.setInteger("BlockOffRangeZ", this.offRangeZ);
          }

          this.availability.writeToNBT(compound);
          return super.writeToNBT(compound);
     }
}
