package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import noppes.npcs.Server;
import noppes.npcs.api.entity.data.IMark;
import noppes.npcs.api.handler.data.IAvailability;
import noppes.npcs.constants.EnumPacketClient;

public class MarkData implements ICapabilityProvider {
     @CapabilityInject(MarkData.class)
     public static Capability MARKDATA_CAPABILITY = null;
     private static final String NBTKEY = "cnpcmarkdata";
     private static final ResourceLocation CAPKEY = new ResourceLocation("customnpcs", "markdata");
     private EntityLivingBase entity;
     public List marks = new ArrayList();

     public void setNBT(NBTTagCompound compound) {
          List marks = new ArrayList();
          NBTTagList list = compound.func_150295_c("marks", 10);

          for(int i = 0; i < list.func_74745_c(); ++i) {
               NBTTagCompound c = list.func_150305_b(i);
               MarkData.Mark m = new MarkData.Mark();
               m.type = c.func_74762_e("type");
               m.color = c.func_74762_e("color");
               m.availability.readFromNBT(c.func_74775_l("availability"));
               marks.add(m);
          }

          this.marks = marks;
     }

     public NBTTagCompound getNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          NBTTagList list = new NBTTagList();
          Iterator var3 = this.marks.iterator();

          while(var3.hasNext()) {
               MarkData.Mark m = (MarkData.Mark)var3.next();
               NBTTagCompound c = new NBTTagCompound();
               c.func_74768_a("type", m.type);
               c.func_74768_a("color", m.color);
               c.func_74782_a("availability", m.availability.writeToNBT(new NBTTagCompound()));
               list.func_74742_a(c);
          }

          compound.func_74782_a("marks", list);
          return compound;
     }

     public boolean hasCapability(Capability capability, EnumFacing facing) {
          return capability == MARKDATA_CAPABILITY;
     }

     public Object getCapability(Capability capability, EnumFacing facing) {
          return this.hasCapability(capability, facing) ? this : null;
     }

     public static void register(AttachCapabilitiesEvent event) {
          event.addCapability(CAPKEY, new MarkData());
     }

     public void save() {
          this.entity.getEntityData().func_74782_a("cnpcmarkdata", this.getNBT());
     }

     public IMark addMark(int type) {
          MarkData.Mark m = new MarkData.Mark();
          m.type = type;
          this.marks.add(m);
          if (!this.entity.field_70170_p.field_72995_K) {
               this.syncClients();
          }

          return m;
     }

     public IMark addMark(int type, int color) {
          MarkData.Mark m = new MarkData.Mark();
          m.type = type;
          m.color = color;
          this.marks.add(m);
          if (!this.entity.field_70170_p.field_72995_K) {
               this.syncClients();
          }

          return m;
     }

     public static MarkData get(EntityLivingBase entity) {
          MarkData data = (MarkData)entity.getCapability(MARKDATA_CAPABILITY, (EnumFacing)null);
          if (data.entity == null) {
               data.entity = entity;
               data.setNBT(entity.getEntityData().func_74775_l("cnpcmarkdata"));
          }

          return data;
     }

     public void syncClients() {
          Server.sendToAll(this.entity.func_184102_h(), EnumPacketClient.MARK_DATA, this.entity.func_145782_y(), this.getNBT());
     }

     public class Mark implements IMark {
          public int type = 0;
          public Availability availability = new Availability();
          public int color = 16772433;

          public IAvailability getAvailability() {
               return this.availability;
          }

          public int getColor() {
               return this.color;
          }

          public void setColor(int color) {
               this.color = color;
          }

          public int getType() {
               return this.type;
          }

          public void setType(int type) {
               this.type = type;
          }

          public void update() {
               MarkData.this.syncClients();
          }
     }
}
