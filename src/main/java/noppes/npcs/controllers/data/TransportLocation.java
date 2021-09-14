package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.entity.data.role.IRoleTransporter;

public class TransportLocation implements IRoleTransporter.ITransportLocation {
	public int id = -1;
	public String name = "default name";
	public BlockPos pos;
	public int type = 0;
	public int dimension = 0;
	public TransportCategory category;

	public void readNBT(NBTTagCompound compound) {
		if (compound != null) {
			this.id = compound.getInteger("Id");
			this.pos = new BlockPos(compound.getDouble("PosX"), compound.getDouble("PosY"), compound.getDouble("PosZ"));
			this.type = compound.getInteger("Type");
			this.dimension = compound.getInteger("Dimension");
			this.name = compound.getString("Name");
		}
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Id", this.id);
		compound.setDouble("PosX", (double) this.pos.getX());
		compound.setDouble("PosY", (double) this.pos.getY());
		compound.setDouble("PosZ", (double) this.pos.getZ());
		compound.setInteger("Type", this.type);
		compound.setInteger("Dimension", this.dimension);
		compound.setString("Name", this.name);
		return compound;
	}

	public int getId() {
		return this.id;
	}

	public int getDimension() {
		return this.dimension;
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

	public String getName() {
		return this.name;
	}

	public int getType() {
		return this.type;
	}

	public boolean isDefault() {
		return this.type == 1;
	}
}
