package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileColorable extends TileNpcEntity {
     public int color = 14;
     public int rotation;

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          this.color = compound.getInteger("BannerColor");
          this.rotation = compound.getInteger("BannerRotation");
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("BannerColor", this.color);
          compound.setInteger("BannerRotation", this.rotation);
          return super.writeToNBT(compound);
     }

     public boolean canUpdate() {
          return false;
     }

     public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
          NBTTagCompound compound = pkt.func_148857_g();
          this.readFromNBT(compound);
     }

     public SPacketUpdateTileEntity func_189518_D_() {
          return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
     }

     public NBTTagCompound func_189517_E_() {
          NBTTagCompound compound = new NBTTagCompound();
          this.writeToNBT(compound);
          compound.removeTag("Items");
          compound.removeTag("ExtraData");
          return compound;
     }

     public AxisAlignedBB getRenderBoundingBox() {
          return new AxisAlignedBB((double)this.field_174879_c.getX(), (double)this.field_174879_c.getY(), (double)this.field_174879_c.getZ(), (double)(this.field_174879_c.getX() + 1), (double)(this.field_174879_c.getY() + 1), (double)(this.field_174879_c.getZ() + 1));
     }

     public int powerProvided() {
          return 0;
     }
}
