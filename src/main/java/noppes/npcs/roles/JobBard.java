package noppes.npcs.roles;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.data.role.IJobBard;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.entity.EntityNPCInterface;

public class JobBard extends JobInterface implements IJobBard {
     public int minRange = 2;
     public int maxRange = 64;
     public boolean isStreamer = true;
     public boolean hasOffRange = true;
     public String song = "";
     private long ticks = 0L;

     public JobBard(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.setString("BardSong", this.song);
          nbttagcompound.setInteger("BardMinRange", this.minRange);
          nbttagcompound.setInteger("BardMaxRange", this.maxRange);
          nbttagcompound.setBoolean("BardStreamer", this.isStreamer);
          nbttagcompound.setBoolean("BardHasOff", this.hasOffRange);
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.song = nbttagcompound.getString("BardSong");
          this.minRange = nbttagcompound.getInteger("BardMinRange");
          this.maxRange = nbttagcompound.getInteger("BardMaxRange");
          this.isStreamer = nbttagcompound.getBoolean("BardStreamer");
          this.hasOffRange = nbttagcompound.getBoolean("BardHasOff");
     }

     public void onLivingUpdate() {
          if (this.npc.isRemote() && !this.song.isEmpty()) {
               List list;
               if (!MusicController.Instance.isPlaying(this.song)) {
                    list = this.npc.world.getEntitiesWithinAABB(EntityPlayer.class, this.npc.getEntityBoundingBox().expand((double)this.minRange, (double)(this.minRange / 2), (double)this.minRange));
                    if (!list.contains(CustomNpcs.proxy.getPlayer())) {
                         return;
                    }

                    if (this.isStreamer) {
                         MusicController.Instance.playStreaming(this.song, this.npc);
                    } else {
                         MusicController.Instance.playMusic(this.song, this.npc);
                    }
               } else if (MusicController.Instance.playingEntity != this.npc) {
                    EntityPlayer player = CustomNpcs.proxy.getPlayer();
                    if (this.npc.getDistanceSq(player) < MusicController.Instance.playingEntity.getDistanceSq(player)) {
                         MusicController.Instance.playingEntity = this.npc;
                    }
               } else if (this.hasOffRange) {
                    list = this.npc.world.getEntitiesWithinAABB(EntityPlayer.class, this.npc.getEntityBoundingBox().expand((double)this.maxRange, (double)(this.maxRange / 2), (double)this.maxRange));
                    if (!list.contains(CustomNpcs.proxy.getPlayer())) {
                         MusicController.Instance.stopMusic();
                    }
               }

               if (MusicController.Instance.isPlaying(this.song)) {
                    Minecraft.getMinecraft().func_181535_r().field_147676_d = 12000;
               }

          }
     }

     public void killed() {
          this.delete();
     }

     public void delete() {
          if (this.npc.world.isRemote && this.hasOffRange && MusicController.Instance.isPlaying(this.song)) {
               MusicController.Instance.stopMusic();
          }

     }

     public String getSong() {
          return this.song;
     }

     public void setSong(String song) {
          this.song = song;
          this.npc.updateClient = true;
     }
}
