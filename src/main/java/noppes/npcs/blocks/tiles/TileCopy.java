package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileCopy extends TileEntity {
     public short length = 10;
     public short width = 10;
     public short height = 10;
     public String name = "";

     public void func_145839_a(NBTTagCompound compound) {
          super.func_145839_a(compound);
          this.length = compound.func_74765_d("Length");
          this.width = compound.func_74765_d("Width");
          this.height = compound.func_74765_d("Height");
          this.name = compound.func_74779_i("Name");
     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          compound.func_74777_a("Length", this.length);
          compound.func_74777_a("Width", this.width);
          compound.func_74777_a("Height", this.height);
          compound.func_74778_a("Name", this.name);
          return super.func_189515_b(compound);
     }

     public void handleUpdateTag(NBTTagCompound compound) {
          this.length = compound.func_74765_d("Length");
          this.width = compound.func_74765_d("Width");
          this.height = compound.func_74765_d("Height");
     }

     public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
          this.handleUpdateTag(pkt.func_148857_g());
     }

     public SPacketUpdateTileEntity func_189518_D_() {
          return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
     }

     public NBTTagCompound func_189517_E_() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74768_a("x", this.field_174879_c.func_177958_n());
          compound.func_74768_a("y", this.field_174879_c.func_177956_o());
          compound.func_74768_a("z", this.field_174879_c.func_177952_p());
          compound.func_74777_a("Length", this.length);
          compound.func_74777_a("Width", this.width);
          compound.func_74777_a("Height", this.height);
          return compound;
     }

     public AxisAlignedBB getRenderBoundingBox() {
          return new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + this.width + 1), (double)(this.field_174879_c.func_177956_o() + this.height + 1), (double)(this.field_174879_c.func_177952_p() + this.length + 1));
     }
}
