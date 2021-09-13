package noppes.npcs.schematics;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public interface ISchematic {
     short getWidth();

     short getHeight();

     short getLength();

     int getTileEntitySize();

     NBTTagCompound getTileEntity(int var1);

     String getName();

     IBlockState getBlockState(int var1, int var2, int var3);

     IBlockState getBlockState(int var1);

     NBTTagCompound getNBT();
}
