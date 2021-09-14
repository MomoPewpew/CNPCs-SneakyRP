package noppes.npcs.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityNPCInterface;

public class ChunkController implements LoadingCallback {
     public static ChunkController instance;
     private HashMap tickets = new HashMap();

     public ChunkController() {
          instance = this;
     }

     public void clear() {
          this.tickets = new HashMap();
     }

     public Ticket getTicket(EntityNPCInterface npc) {
          Ticket ticket = (Ticket)this.tickets.get(npc);
          if (ticket != null) {
               return ticket;
          } else if (this.size() >= CustomNpcs.ChuckLoaders) {
               return null;
          } else {
               ticket = ForgeChunkManager.requestTicket(CustomNpcs.instance, npc.world, Type.ENTITY);
               if (ticket == null) {
                    return null;
               } else {
                    ticket.bindEntity(npc);
                    ticket.setChunkListDepth(6);
                    this.tickets.put(npc, ticket);
                    return null;
               }
          }
     }

     public void deleteNPC(EntityNPCInterface npc) {
          Ticket ticket = (Ticket)this.tickets.get(npc);
          if (ticket != null) {
               this.tickets.remove(npc);
               ForgeChunkManager.releaseTicket(ticket);
          }

     }

     public void ticketsLoaded(List tickets, World world) {
          Iterator var3 = tickets.iterator();

          while(var3.hasNext()) {
               Ticket ticket = (Ticket)var3.next();
               if (ticket.getEntity() instanceof EntityNPCInterface) {
                    EntityNPCInterface npc = (EntityNPCInterface)ticket.getEntity();
                    if (npc.advanced.job == 8 && !tickets.contains(npc)) {
                         this.tickets.put(npc, ticket);
                         double x = npc.field_70165_t / 16.0D;
                         double z = npc.field_70161_v / 16.0D;
                         ForgeChunkManager.forceChunk(ticket, new ChunkPos(MathHelper.floor(x), MathHelper.floor(z)));
                         ForgeChunkManager.forceChunk(ticket, new ChunkPos(MathHelper.ceil(x), MathHelper.ceil(z)));
                         ForgeChunkManager.forceChunk(ticket, new ChunkPos(MathHelper.floor(x), MathHelper.ceil(z)));
                         ForgeChunkManager.forceChunk(ticket, new ChunkPos(MathHelper.ceil(x), MathHelper.floor(z)));
                    }
               }
          }

     }

     public int size() {
          return this.tickets.size();
     }

     public void unload(int toRemove) {
          Iterator ite = this.tickets.keySet().iterator();

          for(int i = 0; ite.hasNext(); ++i) {
               if (i >= toRemove) {
                    return;
               }

               Entity entity = (Entity)ite.next();
               ForgeChunkManager.releaseTicket((Ticket)this.tickets.get(entity));
               ite.remove();
          }

     }
}
