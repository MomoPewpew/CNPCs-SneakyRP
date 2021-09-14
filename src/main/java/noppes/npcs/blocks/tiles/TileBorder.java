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

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          this.readExtraNBT(compound);
          if (this.getWorld() != null) {
               this.getWorld().setBlockState(this.getPos(), CustomItems.border.getDefaultState().withProperty(BlockBorder.ROTATION, this.rotation));
          }

     }

     public void readExtraNBT(NBTTagCompound compound) {
          this.availability.readFromNBT(compound.getCompoundTag("BorderAvailability"));
          this.rotation = compound.getInteger("BorderRotation");
          this.height = compound.getInteger("BorderHeight");
          this.message = compound.getString("BorderMessage");
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          this.writeExtraNBT(compound);
          return super.writeToNBT(compound);
     }

     public void writeExtraNBT(NBTTagCompound compound) {
          compound.setTag("BorderAvailability", this.availability.writeToNBT(new NBTTagCompound()));
          compound.setInteger("BorderRotation", this.rotation);
          compound.setInteger("BorderHeight", this.height);
          compound.setString("BorderMessage", this.message);
     }

     public void update() {
          if (!this.field_145850_b.isRemote) {
               AxisAlignedBB box = new AxisAlignedBB((double)this.field_174879_c.getX(), (double)this.field_174879_c.getY(), (double)this.field_174879_c.getZ(), (double)(this.field_174879_c.getX() + 1), (double)(this.field_174879_c.getY() + this.height + 1), (double)(this.field_174879_c.getZ() + 1));
               List list = this.field_145850_b.getEntitiesWithinAABB(Entity.class, box, this);
               Iterator var3 = list.iterator();

               while(true) {
                    while(var3.hasNext()) {
                         Entity entity = (Entity)var3.next();
                         if (entity instanceof EntityEnderPearl) {
                              EntityEnderPearl pearl = (EntityEnderPearl)entity;
                              if (pearl.getThrower() instanceof EntityPlayer && !this.availability.isAvailable((EntityPlayer)pearl.getThrower())) {
                                   entity.field_70128_L = true;
                              }
                         } else {
                              EntityPlayer player = (EntityPlayer)entity;
                              if (!this.availability.isAvailable(player)) {
                                   BlockPos pos2 = new BlockPos(this.field_174879_c);
                                   if (this.rotation == 2) {
                                        pos2 = pos2.south();
                                   } else if (this.rotation == 0) {
                                        pos2 = pos2.north();
                                   } else if (this.rotation == 1) {
                                        pos2 = pos2.east();
                                   } else if (this.rotation == 3) {
                                        pos2 = pos2.west();
                                   }

                                   while(!this.field_145850_b.isAirBlock(pos2)) {
                                        pos2 = pos2.up();
                                   }

                                   player.setPositionAndUpdate((double)pos2.getX() + 0.5D, (double)pos2.getY(), (double)pos2.getZ() + 0.5D);
                                   if (!this.message.isEmpty()) {
                                        player.sendStatusMessage(new TextComponentTranslation(this.message, new Object[0]), true);
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
          this.rotation = compound.getInteger("Rotation");
     }

     public SPacketUpdateTileEntity func_189518_D_() {
          return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
     }

     public NBTTagCompound func_189517_E_() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("x", this.field_174879_c.getX());
          compound.setInteger("y", this.field_174879_c.getY());
          compound.setInteger("z", this.field_174879_c.getZ());
          compound.setInteger("Rotation", this.rotation);
          return compound;
     }

     public boolean isEntityApplicable(Entity var1) {
          return var1 instanceof EntityPlayerMP || var1 instanceof EntityEnderPearl;
     }

     public boolean apply(Object ob) {
          return this.isEntityApplicable((Entity)ob);
     }
}
