package noppes.npcs.api.wrapper;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidBase;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.blocks.tiles.TileNpcEntity;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.LRUHashMap;

public class BlockWrapper implements IBlock {
     private static final Map blockCache = new LRUHashMap(400);
     protected final IWorld world;
     protected final Block block;
     protected final BlockPos pos;
     protected final BlockPosWrapper bPos;
     protected TileEntity tile;
     protected TileNpcEntity storage;
     private final IData tempdata = new IData() {
          public void remove(String key) {
               if (BlockWrapper.this.storage != null) {
                    BlockWrapper.this.storage.tempData.remove(key);
               }
          }

          public void put(String key, Object value) {
               if (BlockWrapper.this.storage != null) {
                    BlockWrapper.this.storage.tempData.put(key, value);
               }
          }

          public boolean has(String key) {
               return BlockWrapper.this.storage == null ? false : BlockWrapper.this.storage.tempData.containsKey(key);
          }

          public Object get(String key) {
               return BlockWrapper.this.storage == null ? null : BlockWrapper.this.storage.tempData.get(key);
          }

          public void clear() {
               if (BlockWrapper.this.storage != null) {
                    BlockWrapper.this.storage.tempData.clear();
               }
          }

          public String[] getKeys() {
               return (String[])BlockWrapper.this.storage.tempData.keySet().toArray(new String[BlockWrapper.this.storage.tempData.size()]);
          }
     };
     private final IData storeddata = new IData() {
          public void put(String key, Object value) {
               NBTTagCompound compound = this.getNBT();
               if (compound != null) {
                    if (value instanceof Number) {
                         compound.func_74780_a(key, ((Number)value).doubleValue());
                    } else if (value instanceof String) {
                         compound.func_74778_a(key, (String)value);
                    }

               }
          }

          public Object get(String key) {
               NBTTagCompound compound = this.getNBT();
               if (compound == null) {
                    return null;
               } else if (!compound.func_74764_b(key)) {
                    return null;
               } else {
                    NBTBase base = compound.func_74781_a(key);
                    return base instanceof NBTPrimitive ? ((NBTPrimitive)base).func_150286_g() : ((NBTTagString)base).func_150285_a_();
               }
          }

          public void remove(String key) {
               NBTTagCompound compound = this.getNBT();
               if (compound != null) {
                    compound.func_82580_o(key);
               }
          }

          public boolean has(String key) {
               NBTTagCompound compound = this.getNBT();
               return compound == null ? false : compound.func_74764_b(key);
          }

          public void clear() {
               if (BlockWrapper.this.tile != null) {
                    BlockWrapper.this.tile.getTileData().func_74782_a("CustomNPCsData", new NBTTagCompound());
               }
          }

          private NBTTagCompound getNBT() {
               if (BlockWrapper.this.tile == null) {
                    return null;
               } else {
                    NBTTagCompound compound = BlockWrapper.this.tile.getTileData().func_74775_l("CustomNPCsData");
                    if (compound.func_82582_d() && !BlockWrapper.this.tile.getTileData().func_74764_b("CustomNPCsData")) {
                         BlockWrapper.this.tile.getTileData().func_74782_a("CustomNPCsData", compound);
                    }

                    return compound;
               }
          }

          public String[] getKeys() {
               NBTTagCompound compound = this.getNBT();
               return compound == null ? new String[0] : (String[])compound.func_150296_c().toArray(new String[compound.func_150296_c().size()]);
          }
     };

     protected BlockWrapper(World world, Block block, BlockPos pos) {
          this.world = NpcAPI.Instance().getIWorld((WorldServer)world);
          this.block = block;
          this.pos = pos;
          this.bPos = new BlockPosWrapper(pos);
          this.setTile(world.func_175625_s(pos));
     }

     public int getX() {
          return this.pos.func_177958_n();
     }

     public int getY() {
          return this.pos.func_177956_o();
     }

     public int getZ() {
          return this.pos.func_177952_p();
     }

     public IPos getPos() {
          return this.bPos;
     }

     public int getMetadata() {
          return this.block.func_176201_c(this.world.getMCWorld().func_180495_p(this.pos));
     }

     public void setMetadata(int i) {
          this.world.getMCWorld().func_180501_a(this.pos, this.block.func_176203_a(i), 3);
     }

     public void remove() {
          this.world.getMCWorld().func_175698_g(this.pos);
     }

     public boolean isRemoved() {
          IBlockState state = this.world.getMCWorld().func_180495_p(this.pos);
          if (state == null) {
               return true;
          } else {
               return state.func_177230_c() != this.block;
          }
     }

     public boolean isAir() {
          return this.block.isAir(this.world.getMCWorld().func_180495_p(this.pos), this.world.getMCWorld(), this.pos);
     }

     public BlockWrapper setBlock(String name) {
          Block block = (Block)Block.field_149771_c.func_82594_a(new ResourceLocation(name));
          if (block == null) {
               return this;
          } else {
               this.world.getMCWorld().func_175656_a(this.pos, block.func_176223_P());
               return new BlockWrapper(this.world.getMCWorld(), block, this.pos);
          }
     }

     public BlockWrapper setBlock(IBlock block) {
          this.world.getMCWorld().func_175656_a(this.pos, block.getMCBlock().func_176223_P());
          return new BlockWrapper(this.world.getMCWorld(), block.getMCBlock(), this.pos);
     }

     public boolean isContainer() {
          if (this.tile != null && this.tile instanceof IInventory) {
               return ((IInventory)this.tile).func_70302_i_() > 0;
          } else {
               return false;
          }
     }

     public IContainer getContainer() {
          if (!this.isContainer()) {
               throw new CustomNPCsException("This block is not a container", new Object[0]);
          } else {
               return NpcAPI.Instance().getIContainer((IInventory)this.tile);
          }
     }

     public IData getTempdata() {
          return this.tempdata;
     }

     public IData getStoreddata() {
          return this.storeddata;
     }

     public String getName() {
          return Block.field_149771_c.func_177774_c(this.block) + "";
     }

     public String getDisplayName() {
          return this.tile == null ? this.getName() : this.tile.func_145748_c_().func_150260_c();
     }

     public IWorld getWorld() {
          return this.world;
     }

     public Block getMCBlock() {
          return this.block;
     }

     /** @deprecated */
     @Deprecated
     public static IBlock createNew(World world, BlockPos pos, IBlockState state) {
          Block block = state.func_177230_c();
          String key = state.toString() + pos.toString();
          BlockWrapper b = (BlockWrapper)blockCache.get(key);
          if (b != null) {
               b.setTile(world.func_175625_s(pos));
               return b;
          } else {
               Object b;
               if (block instanceof BlockScripted) {
                    b = new BlockScriptedWrapper(world, block, pos);
               } else if (block instanceof BlockScriptedDoor) {
                    b = new BlockScriptedDoorWrapper(world, block, pos);
               } else if (block instanceof BlockFluidBase) {
                    b = new BlockFluidContainerWrapper(world, block, pos);
               } else {
                    b = new BlockWrapper(world, block, pos);
               }

               blockCache.put(key, b);
               return (IBlock)b;
          }
     }

     public static void clearCache() {
          blockCache.clear();
     }

     public boolean hasTileEntity() {
          return this.tile != null;
     }

     protected void setTile(TileEntity tile) {
          this.tile = tile;
          if (tile instanceof TileNpcEntity) {
               this.storage = (TileNpcEntity)tile;
          }

     }

     public INbt getTileEntityNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          this.tile.func_189515_b(compound);
          return NpcAPI.Instance().getINbt(compound);
     }

     public void setTileEntityNBT(INbt nbt) {
          this.tile.func_145839_a(nbt.getMCNBT());
          this.tile.func_70296_d();
          IBlockState state = this.world.getMCWorld().func_180495_p(this.pos);
          this.world.getMCWorld().func_184138_a(this.pos, state, state, 3);
     }

     public TileEntity getMCTileEntity() {
          return this.tile;
     }

     public IBlockState getMCBlockState() {
          return this.world.getMCWorld().func_180495_p(this.pos);
     }

     public void blockEvent(int type, int data) {
          this.world.getMCWorld().func_175641_c(this.pos, this.getMCBlock(), type, data);
     }

     public void interact(int side) {
          EntityPlayer player = EntityNPCInterface.GenericPlayer;
          World w = this.world.getMCWorld();
          player.func_70029_a(w);
          player.func_70107_b((double)this.pos.func_177958_n(), (double)this.pos.func_177956_o(), (double)this.pos.func_177952_p());
          this.block.func_180639_a(w, this.pos, w.func_180495_p(this.pos), EntityNPCInterface.CommandPlayer, EnumHand.MAIN_HAND, EnumFacing.func_82600_a(side), 0.0F, 0.0F, 0.0F);
     }
}
