package noppes.npcs.blocks.tiles;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileNpcEntity extends TileEntity {
     public Map tempData = new HashMap();

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          NBTTagCompound extraData = compound.getCompoundTag("ExtraData");
          if (extraData.func_186856_d() > 0) {
               this.getTileData().setTag("CustomNPCsData", extraData);
          }

     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          return super.func_189515_b(compound);
     }
}
