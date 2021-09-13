package noppes.npcs.roles;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.entity.data.role.IRoleTransporter;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerTransportData;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleTransporter extends RoleInterface implements IRoleTransporter {
     public int transportId = -1;
     public String name;
     private int ticks = 10;

     public RoleTransporter(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.func_74768_a("TransporterId", this.transportId);
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.transportId = nbttagcompound.func_74762_e("TransporterId");
          TransportLocation loc = this.getLocation();
          if (loc != null) {
               this.name = loc.name;
          }

     }

     public boolean aiShouldExecute() {
          --this.ticks;
          if (this.ticks > 0) {
               return false;
          } else {
               this.ticks = 10;
               if (!this.hasTransport()) {
                    return false;
               } else {
                    TransportLocation loc = this.getLocation();
                    if (loc.type != 0) {
                         return false;
                    } else {
                         List inRange = this.npc.field_70170_p.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b(6.0D, 6.0D, 6.0D));
                         Iterator var3 = inRange.iterator();

                         while(var3.hasNext()) {
                              EntityPlayer player = (EntityPlayer)var3.next();
                              if (this.npc.canSee(player)) {
                                   this.unlock(player, loc);
                              }
                         }

                         return false;
                    }
               }
          }
     }

     public void interact(EntityPlayer player) {
          if (this.hasTransport()) {
               TransportLocation loc = this.getLocation();
               if (loc.type == 2) {
                    this.unlock(player, loc);
               }

               NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTransporter, this.npc);
          }

     }

     public void transport(EntityPlayerMP player, String location) {
          TransportLocation loc = TransportController.getInstance().getTransport(location);
          PlayerTransportData playerdata = PlayerData.get(player).transportData;
          if (loc != null && (loc.isDefault() || playerdata.transports.contains(loc.id))) {
               RoleEvent.TransporterUseEvent event = new RoleEvent.TransporterUseEvent(player, this.npc.wrappedNPC, loc);
               if (!EventHooks.onNPCRole(this.npc, event)) {
                    NoppesUtilPlayer.teleportPlayer(player, (double)loc.pos.func_177958_n(), (double)loc.pos.func_177956_o(), (double)loc.pos.func_177952_p(), loc.dimension);
               }
          }
     }

     private void unlock(EntityPlayer player, TransportLocation loc) {
          PlayerTransportData data = PlayerData.get(player).transportData;
          if (!data.transports.contains(this.transportId)) {
               RoleEvent.TransporterUnlockedEvent event = new RoleEvent.TransporterUnlockedEvent(player, this.npc.wrappedNPC);
               if (!EventHooks.onNPCRole(this.npc, event)) {
                    data.transports.add(this.transportId);
                    player.func_145747_a(new TextComponentTranslation("transporter.unlock", new Object[]{loc.name}));
               }
          }
     }

     public TransportLocation getLocation() {
          return this.npc.isRemote() ? null : TransportController.getInstance().getTransport(this.transportId);
     }

     public boolean hasTransport() {
          TransportLocation loc = this.getLocation();
          return loc != null && loc.id == this.transportId;
     }

     public void setTransport(TransportLocation location) {
          this.transportId = location.id;
          this.name = location.name;
     }
}
