package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.entity.EntityNPCInterface;

public class JobChunkLoader extends JobInterface {
     private List chunks = new ArrayList();
     private int ticks = 20;
     private long playerLastSeen = 0L;

     public JobChunkLoader(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setLong("ChunkPlayerLastSeen", this.playerLastSeen);
          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.playerLastSeen = compound.getLong("ChunkPlayerLastSeen");
     }

     public boolean aiShouldExecute() {
          --this.ticks;
          if (this.ticks > 0) {
               return false;
          } else {
               this.ticks = 20;
               List players = this.npc.world.getEntitiesWithinAABB(EntityPlayer.class, this.npc.getEntityBoundingBox().expand(48.0D, 48.0D, 48.0D));
               if (!players.isEmpty()) {
                    this.playerLastSeen = System.currentTimeMillis();
               }

               if (System.currentTimeMillis() > this.playerLastSeen + 600000L) {
                    ChunkController.instance.deleteNPC(this.npc);
                    this.chunks.clear();
                    return false;
               } else {
                    Ticket ticket = ChunkController.instance.getTicket(this.npc);
                    if (ticket == null) {
                         return false;
                    } else {
                         double x = this.npc.field_70165_t / 16.0D;
                         double z = this.npc.field_70161_v / 16.0D;
                         List list = new ArrayList();
                         list.add(new ChunkPos(MathHelper.func_76128_c(x), MathHelper.func_76128_c(z)));
                         list.add(new ChunkPos(MathHelper.func_76143_f(x), MathHelper.func_76143_f(z)));
                         list.add(new ChunkPos(MathHelper.func_76128_c(x), MathHelper.func_76143_f(z)));
                         list.add(new ChunkPos(MathHelper.func_76143_f(x), MathHelper.func_76128_c(z)));
                         Iterator var8 = list.iterator();

                         ChunkPos chunk;
                         while(var8.hasNext()) {
                              chunk = (ChunkPos)var8.next();
                              if (!this.chunks.contains(chunk)) {
                                   ForgeChunkManager.forceChunk(ticket, chunk);
                              } else {
                                   this.chunks.remove(chunk);
                              }
                         }

                         var8 = this.chunks.iterator();

                         while(var8.hasNext()) {
                              chunk = (ChunkPos)var8.next();
                              ForgeChunkManager.unforceChunk(ticket, chunk);
                         }

                         this.chunks = list;
                         return false;
                    }
               }
          }
     }

     public boolean aiContinueExecute() {
          return false;
     }

     public void reset() {
          ChunkController.instance.deleteNPC(this.npc);
          this.chunks.clear();
          this.playerLastSeen = 0L;
     }

     public void delete() {
     }
}
