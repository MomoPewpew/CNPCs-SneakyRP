package noppes.npcs.schematics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class SchematicWrapper {
	public static final int buildSize = 10000;
	private BlockPos offset;
	private BlockPos start;
	public ISchematic schema;
	public int buildPos;
	public int size;
	public int rotation;
	private World world;
	public boolean isBuilding;
	public boolean firstLayer;
	private Map[] tileEntities;

	public SchematicWrapper(ISchematic schematic) {
		this.offset = BlockPos.ORIGIN;
		this.start = BlockPos.ORIGIN;
		this.rotation = 0;
		this.isBuilding = false;
		this.firstLayer = true;
		this.schema = schematic;
		this.size = schematic.getWidth() * schematic.getHeight() * schematic.getLength();
		this.tileEntities = new Map[schematic.getHeight()];

		for (int i = 0; i < schematic.getTileEntitySize(); ++i) {
			NBTTagCompound teTag = schematic.getTileEntity(i);
			int x = teTag.getInteger("x");
			int y = teTag.getInteger("y");
			int z = teTag.getInteger("z");
			Map map = this.tileEntities[y];
			if (map == null) {
				this.tileEntities[y] = (Map) (map = new HashMap());
			}

			((Map) map).put(new ChunkPos(x, z), teTag);
		}

	}

	public void load(Schematic s) {
	}

	public void init(BlockPos pos, World world, int rotation) {
		this.start = pos;
		this.world = world;
		this.rotation = rotation;
	}

	public void offset(int x, int y, int z) {
		this.offset = new BlockPos(x, y, z);
	}

	public void build() {
		if (this.world != null && this.isBuilding) {
			long endPos = (long) (this.buildPos + 10000);
			if (endPos > (long) this.size) {
				endPos = (long) this.size;
			}

			for (; (long) this.buildPos < endPos; ++this.buildPos) {
				int x = this.buildPos % this.schema.getWidth();
				int z = (this.buildPos - x) / this.schema.getWidth() % this.schema.getLength();
				int y = ((this.buildPos - x) / this.schema.getWidth() - z) / this.schema.getLength();
				if (this.firstLayer) {
					this.place(x, y, z, 1);
				} else {
					this.place(x, y, z, 2);
				}
			}

			if (this.buildPos >= this.size) {
				if (this.firstLayer) {
					this.firstLayer = false;
					this.buildPos = 0;
				} else {
					this.isBuilding = false;
				}
			}

		}
	}

	public void place(int x, int y, int z, int flag) {
		IBlockState state = this.schema.getBlockState(x, y, z);
		if (state != null && (flag != 1 || state.isFullBlock() || state.getBlock() == Blocks.AIR)
				&& (flag != 2 || !state.isFullBlock() && state.getBlock() != Blocks.AIR)) {
			int rotation = this.rotation / 90;
			BlockPos pos = this.start.add(this.rotatePos(x, y, z, rotation));
			state = this.rotationState(state, rotation);
			this.world.setBlockState(pos, state, 2);
			if (state.getBlock() instanceof ITileEntityProvider) {
				TileEntity tile = this.world.getTileEntity(pos);
				if (tile != null) {
					NBTTagCompound comp = this.getTileEntity(x, y, z, pos);
					if (comp != null) {
						tile.readFromNBT(comp);
					}
				}
			}

		}
	}

	public IBlockState rotationState(IBlockState state, int rotation) {
		if (rotation == 0) {
			return state;
		} else {
			Set set = state.getProperties().keySet();
			Iterator var4 = set.iterator();

			while (var4.hasNext()) {
				IProperty prop = (IProperty) var4.next();
				if (prop instanceof PropertyDirection) {
					EnumFacing direction = (EnumFacing) state.getValue(prop);
					if (direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
						for (int i = 0; i < rotation; ++i) {
							direction = direction.rotateY();
						}

						return state.withProperty(prop, direction);
					}
				}
			}

			return state;
		}
	}

	public NBTTagCompound getTileEntity(int x, int y, int z, BlockPos pos) {
		if (y < this.tileEntities.length && this.tileEntities[y] != null) {
			NBTTagCompound compound = (NBTTagCompound) this.tileEntities[y].get(new ChunkPos(x, z));
			if (compound == null) {
				return null;
			} else {
				compound = compound.copy();
				compound.setInteger("x", pos.getX());
				compound.setInteger("y", pos.getY());
				compound.setInteger("z", pos.getZ());
				return compound;
			}
		} else {
			return null;
		}
	}

	public NBTTagCompound getNBTSmall() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setShort("Width", this.schema.getWidth());
		compound.setShort("Height", this.schema.getHeight());
		compound.setShort("Length", this.schema.getLength());
		compound.setString("SchematicName", this.schema.getName());
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.size && i < 25000; ++i) {
			IBlockState state = this.schema.getBlockState(i);
			if (state.getBlock() != Blocks.AIR && state.getBlock() != Blocks.STRUCTURE_VOID) {
				list.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), this.schema.getBlockState(i)));
			} else {
				list.appendTag(new NBTTagCompound());
			}
		}

		compound.setTag("Data", list);
		return compound;
	}

	public BlockPos rotatePos(int x, int y, int z, int rotation) {
		if (rotation == 1) {
			return new BlockPos(this.schema.getLength() - z - 1, y, x);
		} else if (rotation == 2) {
			return new BlockPos(this.schema.getWidth() - x - 1, y, this.schema.getLength() - z - 1);
		} else {
			return rotation == 3 ? new BlockPos(z, y, this.schema.getWidth() - x - 1) : new BlockPos(x, y, z);
		}
	}

	public int getPercentage() {
		double l = (double) (this.buildPos + (this.firstLayer ? 0 : this.size));
		return (int) (l / (double) this.size * 50.0D);
	}
}
