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
			return (String[]) BlockWrapper.this.storage.tempData.keySet()
					.toArray(new String[BlockWrapper.this.storage.tempData.size()]);
		}
	};
	private final IData storeddata = new IData() {
		public void put(String key, Object value) {
			NBTTagCompound compound = this.getNBT();
			if (compound != null) {
				if (value instanceof Number) {
					compound.setDouble(key, ((Number) value).doubleValue());
				} else if (value instanceof String) {
					compound.setString(key, (String) value);
				}

			}
		}

		public Object get(String key) {
			NBTTagCompound compound = this.getNBT();
			if (compound == null) {
				return null;
			} else if (!compound.hasKey(key)) {
				return null;
			} else {
				NBTBase base = compound.getTag(key);
				return base instanceof NBTPrimitive ? ((NBTPrimitive) base).getDouble()
						: ((NBTTagString) base).getString();
			}
		}

		public void remove(String key) {
			NBTTagCompound compound = this.getNBT();
			if (compound != null) {
				compound.removeTag(key);
			}
		}

		public boolean has(String key) {
			NBTTagCompound compound = this.getNBT();
			return compound == null ? false : compound.hasKey(key);
		}

		public void clear() {
			if (BlockWrapper.this.tile != null) {
				BlockWrapper.this.tile.getTileData().setTag("CustomNPCsData", new NBTTagCompound());
			}
		}

		private NBTTagCompound getNBT() {
			if (BlockWrapper.this.tile == null) {
				return null;
			} else {
				NBTTagCompound compound = BlockWrapper.this.tile.getTileData().getCompoundTag("CustomNPCsData");
				if (compound.isEmpty() && !BlockWrapper.this.tile.getTileData().hasKey("CustomNPCsData")) {
					BlockWrapper.this.tile.getTileData().setTag("CustomNPCsData", compound);
				}

				return compound;
			}
		}

		public String[] getKeys() {
			NBTTagCompound compound = this.getNBT();
			return compound == null ? new String[0]
					: (String[]) compound.getKeySet().toArray(new String[compound.getKeySet().size()]);
		}
	};

	protected BlockWrapper(World world, Block block, BlockPos pos) {
		this.world = NpcAPI.Instance().getIWorld((WorldServer) world);
		this.block = block;
		this.pos = pos;
		this.bPos = new BlockPosWrapper(pos);
		this.setTile(world.getTileEntity(pos));
	}

	public int getX() {
		return this.pos.getX();
	}

	public int getY() {
		return this.pos.getY();
	}

	public int getZ() {
		return this.pos.getZ();
	}

	public IPos getPos() {
		return this.bPos;
	}

	public int getMetadata() {
		return this.block.getMetaFromState(this.world.getMCWorld().getBlockState(this.pos));
	}

	public void setMetadata(int i) {
		this.world.getMCWorld().setBlockState(this.pos, this.block.getStateFromMeta(i), 3);
	}

	public void remove() {
		this.world.getMCWorld().setBlockToAir(this.pos);
	}

	public boolean isRemoved() {
		IBlockState state = this.world.getMCWorld().getBlockState(this.pos);
		if (state == null) {
			return true;
		} else {
			return state.getBlock() != this.block;
		}
	}

	public boolean isAir() {
		return this.block.isAir(this.world.getMCWorld().getBlockState(this.pos), this.world.getMCWorld(), this.pos);
	}

	public BlockWrapper setBlock(String name) {
		Block block = (Block) Block.REGISTRY.getObject(new ResourceLocation(name));
		if (block == null) {
			return this;
		} else {
			this.world.getMCWorld().setBlockState(this.pos, block.getDefaultState());
			return new BlockWrapper(this.world.getMCWorld(), block, this.pos);
		}
	}

	public BlockWrapper setBlock(IBlock block) {
		this.world.getMCWorld().setBlockState(this.pos, block.getMCBlock().getDefaultState());
		return new BlockWrapper(this.world.getMCWorld(), block.getMCBlock(), this.pos);
	}

	public boolean isContainer() {
		if (this.tile != null && this.tile instanceof IInventory) {
			return ((IInventory) this.tile).getSizeInventory() > 0;
		} else {
			return false;
		}
	}

	public IContainer getContainer() {
		if (!this.isContainer()) {
			throw new CustomNPCsException("This block is not a container", new Object[0]);
		} else {
			return NpcAPI.Instance().getIContainer((IInventory) this.tile);
		}
	}

	public IData getTempdata() {
		return this.tempdata;
	}

	public IData getStoreddata() {
		return this.storeddata;
	}

	public String getName() {
		return Block.REGISTRY.getNameForObject(this.block) + "";
	}

	public String getDisplayName() {
		return this.tile == null ? this.getName() : this.tile.getDisplayName().getUnformattedText();
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
		Block block = state.getBlock();
		String key = state.toString() + pos.toString();
		BlockWrapper b = (BlockWrapper) blockCache.get(key);
		if (b != null) {
			b.setTile(world.getTileEntity(pos));
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
			return (IBlock) b;
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
			this.storage = (TileNpcEntity) tile;
		}

	}

	public INbt getTileEntityNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		this.tile.writeToNBT(compound);
		return NpcAPI.Instance().getINbt(compound);
	}

	public void setTileEntityNBT(INbt nbt) {
		this.tile.readFromNBT(nbt.getMCNBT());
		this.tile.markDirty();
		IBlockState state = this.world.getMCWorld().getBlockState(this.pos);
		this.world.getMCWorld().notifyBlockUpdate(this.pos, state, state, 3);
	}

	public TileEntity getMCTileEntity() {
		return this.tile;
	}

	public IBlockState getMCBlockState() {
		return this.world.getMCWorld().getBlockState(this.pos);
	}

	public void blockEvent(int type, int data) {
		this.world.getMCWorld().addBlockEvent(this.pos, this.getMCBlock(), type, data);
	}

	public void interact(int side) {
		EntityPlayer player = EntityNPCInterface.GenericPlayer;
		World w = this.world.getMCWorld();
		player.setWorld(w);
		player.setPosition((double) this.pos.getX(), (double) this.pos.getY(), (double) this.pos.getZ());
		this.block.onBlockActivated(w, this.pos, w.getBlockState(this.pos), EntityNPCInterface.CommandPlayer,
				EnumHand.MAIN_HAND, EnumFacing.byIndex(side), 0.0F, 0.0F, 0.0F);
	}
}
