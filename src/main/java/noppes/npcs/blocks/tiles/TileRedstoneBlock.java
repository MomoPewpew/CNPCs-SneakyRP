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
          if (!this.field_145850_b.field_72995_K) {
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
          return oldState.func_177230_c() != newState.func_177230_c();
     }

     private void setActive(Block block, boolean bo) {
          this.isActivated = bo;
          IBlockState state = block.func_176223_P().func_177226_a(BlockNpcRedstone.ACTIVE, this.isActivated);
          this.field_145850_b.func_180501_a(this.field_174879_c, state, 2);
          this.func_70296_d();
          this.field_145850_b.func_184138_a(this.field_174879_c, state, state, 3);
          block.func_176213_c(this.field_145850_b, this.field_174879_c, state);
     }

     private List getPlayerList(int x, int y, int z) {
          return this.field_145850_b.func_72872_a(EntityPlayer.class, (new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + 1), (double)(this.field_174879_c.func_177952_p() + 1))).func_72314_b((double)x, (double)y, (double)z));
     }

     public void func_145839_a(NBTTagCompound compound) {
          super.func_145839_a(compound);
          this.onRange = compound.func_74762_e("BlockOnRange");
          this.offRange = compound.func_74762_e("BlockOffRange");
          this.isDetailed = compound.func_74767_n("BlockIsDetailed");
          if (compound.func_74764_b("BlockOnRangeX")) {
               this.isDetailed = true;
               this.onRangeX = compound.func_74762_e("BlockOnRangeX");
               this.onRangeY = compound.func_74762_e("BlockOnRangeY");
               this.onRangeZ = compound.func_74762_e("BlockOnRangeZ");
               this.offRangeX = compound.func_74762_e("BlockOffRangeX");
               this.offRangeY = compound.func_74762_e("BlockOffRangeY");
               this.offRangeZ = compound.func_74762_e("BlockOffRangeZ");
          }

          if (compound.func_74764_b("BlockActivated")) {
               this.isActivated = compound.func_74767_n("BlockActivated");
          }

          this.availability.readFromNBT(compound);
     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          compound.func_74768_a("BlockOnRange", this.onRange);
          compound.func_74768_a("BlockOffRange", this.offRange);
          compound.func_74757_a("BlockActivated", this.isActivated);
          compound.func_74757_a("BlockIsDetailed", this.isDetailed);
          if (this.isDetailed) {
               compound.func_74768_a("BlockOnRangeX", this.onRangeX);
               compound.func_74768_a("BlockOnRangeY", this.onRangeY);
               compound.func_74768_a("BlockOnRangeZ", this.onRangeZ);
               compound.func_74768_a("BlockOffRangeX", this.offRangeX);
               compound.func_74768_a("BlockOffRangeY", this.offRangeY);
               compound.func_74768_a("BlockOffRangeZ", this.offRangeZ);
          }

          this.availability.writeToNBT(compound);
          return super.func_189515_b(compound);
     }
}
