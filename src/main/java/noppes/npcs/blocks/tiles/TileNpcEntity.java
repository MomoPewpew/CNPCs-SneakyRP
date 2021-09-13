package noppes.npcs.blocks.tiles;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileNpcEntity extends TileEntity {
     public Map tempData = new HashMap();

     public void func_145839_a(NBTTagCompound compound) {
          super.func_145839_a(compound);
          NBTTagCompound extraData = compound.func_74775_l("ExtraData");
          if (extraData.func_186856_d() > 0) {
               this.getTileData().func_74782_a("CustomNPCsData", extraData);
          }

     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          return super.func_189515_b(compound);
     }
}
