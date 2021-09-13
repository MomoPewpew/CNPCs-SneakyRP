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
               this.id = compound.func_74762_e("Id");
               this.pos = new BlockPos(compound.func_74769_h("PosX"), compound.func_74769_h("PosY"), compound.func_74769_h("PosZ"));
               this.type = compound.func_74762_e("Type");
               this.dimension = compound.func_74762_e("Dimension");
               this.name = compound.getString("Name");
          }
     }

     public NBTTagCompound writeNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("Id", this.id);
          compound.func_74780_a("PosX", (double)this.pos.func_177958_n());
          compound.func_74780_a("PosY", (double)this.pos.func_177956_o());
          compound.func_74780_a("PosZ", (double)this.pos.func_177952_p());
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
          return this.pos.func_177958_n();
     }

     public int getY() {
          return this.pos.func_177956_o();
     }

     public int getZ() {
          return this.pos.func_177952_p();
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
