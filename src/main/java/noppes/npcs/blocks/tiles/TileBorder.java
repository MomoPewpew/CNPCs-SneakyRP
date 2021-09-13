package noppes.npcs.blocks.tiles;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.controllers.data.Availability;

public class TileBorder extends TileNpcEntity implements Predicate, ITickable {
     public Availability availability = new Availability();
     public AxisAlignedBB boundingbox;
     public int rotation = 0;
     public int height = 10;
     public String message = "availability.areaNotAvailble";

     public void func_145839_a(NBTTagCompound compound) {
          super.func_145839_a(compound);
          this.readExtraNBT(compound);
          if (this.func_145831_w() != null) {
               this.func_145831_w().func_175656_a(this.func_174877_v(), CustomItems.border.func_176223_P().func_177226_a(BlockBorder.ROTATION, this.rotation));
          }

     }

     public void readExtraNBT(NBTTagCompound compound) {
          this.availability.readFromNBT(compound.func_74775_l("BorderAvailability"));
          this.rotation = compound.func_74762_e("BorderRotation");
          this.height = compound.func_74762_e("BorderHeight");
          this.message = compound.func_74779_i("BorderMessage");
     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          this.writeExtraNBT(compound);
          return super.func_189515_b(compound);
     }

     public void writeExtraNBT(NBTTagCompound compound) {
          compound.func_74782_a("BorderAvailability", this.availability.writeToNBT(new NBTTagCompound()));
          compound.func_74768_a("BorderRotation", this.rotation);
          compound.func_74768_a("BorderHeight", this.height);
          compound.func_74778_a("BorderMessage", this.message);
     }

     public void func_73660_a() {
          if (!this.field_145850_b.field_72995_K) {
               AxisAlignedBB box = new AxisAlignedBB((double)this.field_174879_c.func_177958_n(), (double)this.field_174879_c.func_177956_o(), (double)this.field_174879_c.func_177952_p(), (double)(this.field_174879_c.func_177958_n() + 1), (double)(this.field_174879_c.func_177956_o() + this.height + 1), (double)(this.field_174879_c.func_177952_p() + 1));
               List list = this.field_145850_b.func_175647_a(Entity.class, box, this);
               Iterator var3 = list.iterator();

               while(true) {
                    while(var3.hasNext()) {
                         Entity entity = (Entity)var3.next();
                         if (entity instanceof EntityEnderPearl) {
                              EntityEnderPearl pearl = (EntityEnderPearl)entity;
                              if (pearl.func_85052_h() instanceof EntityPlayer && !this.availability.isAvailable((EntityPlayer)pearl.func_85052_h())) {
                                   entity.field_70128_L = true;
                              }
                         } else {
                              EntityPlayer player = (EntityPlayer)entity;
                              if (!this.availability.isAvailable(player)) {
                                   BlockPos pos2 = new BlockPos(this.field_174879_c);
                                   if (this.rotation == 2) {
                                        pos2 = pos2.func_177968_d();
                                   } else if (this.rotation == 0) {
                                        pos2 = pos2.func_177978_c();
                                   } else if (this.rotation == 1) {
                                        pos2 = pos2.func_177974_f();
                                   } else if (this.rotation == 3) {
                                        pos2 = pos2.func_177976_e();
                                   }

                                   while(!this.field_145850_b.func_175623_d(pos2)) {
                                        pos2 = pos2.func_177984_a();
                                   }

                                   player.func_70634_a((double)pos2.func_177958_n() + 0.5D, (double)pos2.func_177956_o(), (double)pos2.func_177952_p() + 0.5D);
                                   if (!this.message.isEmpty()) {
                                        player.func_146105_b(new TextComponentTranslation(this.message, new Object[0]), true);
                                   }
                              }
                         }
                    }

                    return;
               }
          }
     }

     public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
          this.handleUpdateTag(pkt.func_148857_g());
     }

     public void handleUpdateTag(NBTTagCompound compound) {
          this.rotation = compound.func_74762_e("Rotation");
     }

     public SPacketUpdateTileEntity func_189518_D_() {
          return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
     }

     public NBTTagCompound func_189517_E_() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74768_a("x", this.field_174879_c.func_177958_n());
          compound.func_74768_a("y", this.field_174879_c.func_177956_o());
          compound.func_74768_a("z", this.field_174879_c.func_177952_p());
          compound.func_74768_a("Rotation", this.rotation);
          return compound;
     }

     public boolean isEntityApplicable(Entity var1) {
          return var1 instanceof EntityPlayerMP || var1 instanceof EntityEnderPearl;
     }

     public boolean apply(Object ob) {
          return this.isEntityApplicable((Entity)ob);
     }
}
