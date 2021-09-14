package noppes.npcs.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.entity.data.IData;

public interface IBlock {
	int getX();

	int getY();

	int getZ();

	IPos getPos();

	int getMetadata();

	void setMetadata(int var1);

	String getName();

	void remove();

	boolean isRemoved();

	boolean isAir();

	IBlock setBlock(String var1);

	IBlock setBlock(IBlock var1);

	boolean hasTileEntity();

	boolean isContainer();

	IContainer getContainer();

	IData getTempdata();

	IData getStoreddata();

	IWorld getWorld();

	INbt getTileEntityNBT();

	void setTileEntityNBT(INbt var1);

	TileEntity getMCTileEntity();

	Block getMCBlock();

	void blockEvent(int var1, int var2);

	String getDisplayName();

	IBlockState getMCBlockState();

	void interact(int var1);
}
