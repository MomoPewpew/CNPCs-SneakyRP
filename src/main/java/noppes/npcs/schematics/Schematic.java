package noppes.npcs.schematics;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;

public class Schematic implements ISchematic {
     public String name;
     public short width;
     public short height;
     public short length;
     private NBTTagList entityList;
     public NBTTagList tileList;
     public short[] blockArray;
     public byte[] blockDataArray;

     public Schematic(String name) {
          this.name = name;
     }

     public void load(NBTTagCompound compound) {
          this.width = compound.func_74765_d("Width");
          this.height = compound.func_74765_d("Height");
          this.length = compound.func_74765_d("Length");
          byte[] addId = compound.func_74764_b("AddBlocks") ? compound.func_74770_j("AddBlocks") : new byte[0];
          this.setBlockBytes(compound.func_74770_j("Blocks"), addId);
          this.blockDataArray = compound.func_74770_j("Data");
          this.entityList = compound.func_150295_c("Entities", 10);
          this.tileList = compound.func_150295_c("TileEntities", 10);
     }

     public NBTTagCompound getNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74777_a("Width", this.width);
          compound.func_74777_a("Height", this.height);
          compound.func_74777_a("Length", this.length);
          byte[][] arr = this.getBlockBytes();
          compound.func_74773_a("Blocks", arr[0]);
          if (arr.length > 1) {
               compound.func_74773_a("AddBlocks", arr[1]);
          }

          compound.func_74773_a("Data", this.blockDataArray);
          compound.func_74782_a("TileEntities", this.tileList);
          return compound;
     }

     public void setBlockBytes(byte[] blockId, byte[] addId) {
          this.blockArray = new short[blockId.length];

          for(int index = 0; index < blockId.length; ++index) {
               short id = (short)(blockId[index] & 255);
               if (index >> 1 < addId.length) {
                    if ((index & 1) == 0) {
                         id += (short)((addId[index >> 1] & 15) << 8);
                    } else {
                         id += (short)((addId[index >> 1] & 240) << 4);
                    }
               }

               this.blockArray[index] = id;
          }

     }

     public byte[][] getBlockBytes() {
          byte[] blocks = new byte[this.blockArray.length];
          byte[] addBlocks = null;

          for(int i = 0; i < blocks.length; ++i) {
               short id = this.blockArray[i];
               if (id > 255) {
                    if (addBlocks == null) {
                         addBlocks = new byte[(blocks.length >> 1) + 1];
                    }

                    if ((i & 1) == 0) {
                         addBlocks[i >> 1] = (byte)(addBlocks[i >> 1] & 240 | id >> 8 & 15);
                    } else {
                         addBlocks[i >> 1] = (byte)(addBlocks[i >> 1] & 15 | (id >> 8 & 15) << 4);
                    }
               }

               blocks[i] = (byte)id;
          }

          if (addBlocks == null) {
               return new byte[][]{blocks};
          } else {
               return new byte[][]{blocks, addBlocks};
          }
     }

     public int xyzToIndex(int x, int y, int z) {
          return (y * this.length + z) * this.width + x;
     }

     public IBlockState getBlockState(int x, int y, int z) {
          int i = this.xyzToIndex(x, y, z);
          Block b = Block.func_149729_e(this.blockArray[i]);
          return b == null ? Blocks.field_150350_a.func_176223_P() : b.func_176203_a(this.blockDataArray[i]);
     }

     public IBlockState getBlockState(int i) {
          Block b = Block.func_149729_e(this.blockArray[i]);
          return b == null ? Blocks.field_150350_a.func_176223_P() : b.func_176203_a(this.blockDataArray[i]);
     }

     public short getWidth() {
          return this.width;
     }

     public short getHeight() {
          return this.height;
     }

     public short getLength() {
          return this.length;
     }

     public int getTileEntitySize() {
          return this.entityList == null ? 0 : this.entityList.func_74745_c();
     }

     public NBTTagCompound getTileEntity(int i) {
          return this.entityList.func_150305_b(i);
     }

     public String getName() {
          return this.name;
     }

     public static Schematic Create(World world, String name, BlockPos pos, short height, short width, short length) {
          Schematic schema = new Schematic(name);
          schema.height = height;
          schema.width = width;
          schema.length = length;
          int size = height * width * length;
          schema.blockArray = new short[size];
          schema.blockDataArray = new byte[size];
          NoppesUtilServer.NotifyOPs("Creating schematic at: " + pos + " might lag slightly");
          schema.tileList = new NBTTagList();

          for(int i = 0; i < size; ++i) {
               int x = i % width;
               int z = (i - x) / width % length;
               int y = ((i - x) / width - z) / length;
               IBlockState state = world.func_180495_p(pos.func_177982_a(x, y, z));
               if (state.func_177230_c() != Blocks.field_150350_a && state.func_177230_c() != CustomItems.copy) {
                    schema.blockArray[i] = (short)Block.field_149771_c.func_148757_b(state.func_177230_c());
                    schema.blockDataArray[i] = (byte)state.func_177230_c().func_176201_c(state);
                    if (state.func_177230_c() instanceof ITileEntityProvider) {
                         TileEntity tile = world.func_175625_s(pos.func_177982_a(x, y, z));
                         NBTTagCompound compound = new NBTTagCompound();
                         tile.func_189515_b(compound);
                         compound.func_74768_a("x", x);
                         compound.func_74768_a("y", y);
                         compound.func_74768_a("z", z);
                         schema.tileList.func_74742_a(compound);
                    }
               }
          }

          return schema;
     }
}
