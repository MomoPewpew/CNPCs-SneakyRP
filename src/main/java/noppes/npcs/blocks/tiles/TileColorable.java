package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileColorable extends TileNpcEntity {
     public int color = 14;
     public int rotation;

     public void func_145839_a(NBTTagCompound compound) {
          super.func_145839_a(compound);
          this.color = compound.func_74762_e("BannerColor");
          this.rotation = compound.func_74762_e("BannerRotation");
     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          compound.setInteger("BannerColor", this.color);
          compound.setInteger("BannerRotation", this.rotation);
          return super.func_189515_b(compound);
     }

     public boolean canUpdate() {
          return false;
     }

     public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
          NBTTagCompound compound = pkt.func_148857_g();
          this.func_145839_a(compound);
     }

     public SPacketUpdateTileEntity func_189518_D_() {
          return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
     }

     public NBTTagCompound func_189517_E_() {
          NBTTagCompound compound = new NBTTagCompound();
          this.func_189515_b(compound);
          compound.func_82580_o("Items");
          compound.func_82580_o("ExtraData");
          return compound;
     }

     public AxisAlignedBB getRenderBoundingBox() {
          return new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + 1), (double)(this.field_174879_c.func_177952_p() + 1));
     }

     public int powerProvided() {
          return 0;
     }
}
