package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.NBTTags;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.BlockData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBuilder;
import noppes.npcs.schematics.SchematicWrapper;

public class TileBuilder extends TileEntity implements ITickable {
     private SchematicWrapper schematic = null;
     public int rotation = 0;
     public int yOffest = 0;
     public boolean enabled = false;
     public boolean started = false;
     public boolean finished = false;
     public Availability availability = new Availability();
     private Stack positions = new Stack();
     private Stack positionsSecond = new Stack();
     public static BlockPos DrawPos = null;
     public static boolean Compiled = false;
     private int ticks = 20;

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          if (compound.hasKey("SchematicName")) {
               this.schematic = SchematicController.Instance.load(compound.getString("SchematicName"));
          }

          Stack positions = new Stack();
          positions.addAll(NBTTags.getIntegerList(compound.getTagList("Positions", 10)));
          this.positions = positions;
          positions = new Stack();
          positions.addAll(NBTTags.getIntegerList(compound.getTagList("PositionsSecond", 10)));
          this.positionsSecond = positions;
          this.readPartNBT(compound);
     }

     public void readPartNBT(NBTTagCompound compound) {
          this.rotation = compound.getInteger("Rotation");
          this.yOffest = compound.getInteger("YOffset");
          this.enabled = compound.getBoolean("Enabled");
          this.started = compound.getBoolean("Started");
          this.finished = compound.getBoolean("Finished");
          this.availability.readFromNBT(compound.getCompoundTag("Availability"));
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          super.writeToNBT(compound);
          if (this.schematic != null) {
               compound.setString("SchematicName", this.schematic.schema.getName());
          }

          compound.setTag("Positions", NBTTags.nbtIntegerCollection(new ArrayList(this.positions)));
          compound.setTag("PositionsSecond", NBTTags.nbtIntegerCollection(new ArrayList(this.positionsSecond)));
          this.writePartNBT(compound);
          return compound;
     }

     public NBTTagCompound writePartNBT(NBTTagCompound compound) {
          compound.setInteger("Rotation", this.rotation);
          compound.setInteger("YOffset", this.yOffest);
          compound.setBoolean("Enabled", this.enabled);
          compound.setBoolean("Started", this.started);
          compound.setBoolean("Finished", this.finished);
          compound.setTag("Availability", this.availability.writeToNBT(new NBTTagCompound()));
          return compound;
     }

     @SideOnly(Side.CLIENT)
     public void setDrawSchematic(SchematicWrapper schematics) {
          this.schematic = schematics;
     }

     public void setSchematic(SchematicWrapper schematics) {
          this.schematic = schematics;
          if (schematics == null) {
               this.positions.clear();
               this.positionsSecond.clear();
          } else {
               Stack positions = new Stack();

               for(int y = 0; y < schematics.schema.getHeight(); ++y) {
                    int z;
                    int x;
                    for(z = 0; z < schematics.schema.getLength() / 2; ++z) {
                         for(x = 0; x < schematics.schema.getWidth() / 2; ++x) {
                              positions.add(0, this.xyzToIndex(x, y, z));
                         }
                    }

                    for(z = 0; z < schematics.schema.getLength() / 2; ++z) {
                         for(x = schematics.schema.getWidth() / 2; x < schematics.schema.getWidth(); ++x) {
                              positions.add(0, this.xyzToIndex(x, y, z));
                         }
                    }

                    for(z = schematics.schema.getLength() / 2; z < schematics.schema.getLength(); ++z) {
                         for(x = 0; x < schematics.schema.getWidth() / 2; ++x) {
                              positions.add(0, this.xyzToIndex(x, y, z));
                         }
                    }

                    for(z = schematics.schema.getLength() / 2; z < schematics.schema.getLength(); ++z) {
                         for(x = schematics.schema.getWidth() / 2; x < schematics.schema.getWidth(); ++x) {
                              positions.add(0, this.xyzToIndex(x, y, z));
                         }
                    }
               }

               this.positions = positions;
               this.positionsSecond.clear();
          }
     }

     public int xyzToIndex(int x, int y, int z) {
          return (y * this.schematic.schema.getLength() + z) * this.schematic.schema.getWidth() + x;
     }

     public SchematicWrapper getSchematic() {
          return this.schematic;
     }

     public boolean hasSchematic() {
          return this.schematic != null;
     }

     public void update() {
          if (!this.world.isRemote && this.hasSchematic() && !this.finished) {
               --this.ticks;
               if (this.ticks <= 0) {
                    this.ticks = 200;
                    if (this.positions.isEmpty() && this.positionsSecond.isEmpty()) {
                         this.finished = true;
                    } else {
                         if (!this.started) {
                              Iterator var1 = this.getPlayerList().iterator();

                              while(var1.hasNext()) {
                                   EntityPlayer player = (EntityPlayer)var1.next();
                                   if (this.availability.isAvailable(player)) {
                                        this.started = true;
                                        break;
                                   }
                              }

                              if (!this.started) {
                                   return;
                              }
                         }

                         List list = this.world.getEntitiesWithinAABB(EntityNPCInterface.class, (new AxisAlignedBB(this.getPos(), this.getPos())).grow(32.0D, 32.0D, 32.0D));
                         Iterator var6 = list.iterator();

                         while(var6.hasNext()) {
                              EntityNPCInterface npc = (EntityNPCInterface)var6.next();
                              if (npc.advanced.job == 10) {
                                   JobBuilder job = (JobBuilder)npc.jobInterface;
                                   if (job.build == null) {
                                        job.build = this;
                                   }
                              }
                         }

                    }
               }
          }
     }

     private List getPlayerList() {
          return this.world.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB((double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), (double)(this.pos.getX() + 1), (double)(this.pos.getY() + 1), (double)(this.pos.getZ() + 1))).grow(10.0D, 10.0D, 10.0D));
     }

     public Stack getBlock() {
          if (this.enabled && !this.finished && this.hasSchematic()) {
               boolean bo = this.positions.isEmpty();
               Stack list = new Stack();
               int size = this.schematic.schema.getWidth() * this.schematic.schema.getLength() / 4;
               if (size > 30) {
                    size = 30;
               }

               for(int i = 0; i < size; ++i) {
                    if (this.positions.isEmpty() && !bo || this.positionsSecond.isEmpty() && bo) {
                         return list;
                    }

                    int pos = bo ? (Integer)this.positionsSecond.pop() : (Integer)this.positions.pop();
                    if (pos < this.schematic.size) {
                         int x = pos % this.schematic.schema.getWidth();
                         int z = (pos - x) / this.schematic.schema.getWidth() % this.schematic.schema.getLength();
                         int y = ((pos - x) / this.schematic.schema.getWidth() - z) / this.schematic.schema.getLength();
                         IBlockState state = this.schematic.schema.getBlockState(x, y, z);
                         if (!state.isFullBlock() && !bo && state.getBlock() != Blocks.AIR) {
                              this.positionsSecond.add(0, pos);
                         } else {
                              BlockPos blockPos = this.getPos().add(1, this.yOffest, 1).add(this.schematic.rotatePos(x, y, z, this.rotation));
                              IBlockState original = this.world.getBlockState(blockPos);
                              if (Block.getStateId(state) != Block.getStateId(original)) {
                                   state = this.schematic.rotationState(state, this.rotation);
                                   NBTTagCompound tile = null;
                                   if (state.getBlock() instanceof ITileEntityProvider) {
                                        tile = this.schematic.getTileEntity(x, y, z, blockPos);
                                   }

                                   list.add(0, new BlockData(blockPos, state, tile));
                              }
                         }
                    }
               }

               return list;
          } else {
               return null;
          }
     }

     public static void SetDrawPos(BlockPos pos) {
          DrawPos = pos;
          Compiled = false;
     }
}
