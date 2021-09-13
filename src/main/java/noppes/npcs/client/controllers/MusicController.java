package noppes.npcs.client.controllers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.ISound.AttenuationType;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class MusicController {
     public static MusicController Instance;
     public PositionedSoundRecord playing;
     public ResourceLocation playingResource;
     public Entity playingEntity;

     public MusicController() {
          Instance = this;
     }

     public void stopMusic() {
          SoundHandler handler = Minecraft.getMinecraft().func_147118_V();
          if (this.playing != null) {
               handler.func_147683_b(this.playing);
          }

          handler.func_189520_a("", SoundCategory.MUSIC);
          handler.func_189520_a("", SoundCategory.AMBIENT);
          handler.func_189520_a("", SoundCategory.RECORDS);
          this.playingResource = null;
          this.playingEntity = null;
          this.playing = null;
     }

     public void playStreaming(String music, Entity entity) {
          if (!this.isPlaying(music)) {
               this.stopMusic();
               this.playingEntity = entity;
               this.playingResource = new ResourceLocation(music);
               SoundHandler handler = Minecraft.getMinecraft().func_147118_V();
               this.playing = new PositionedSoundRecord(this.playingResource, SoundCategory.RECORDS, 4.0F, 1.0F, false, 0, AttenuationType.LINEAR, (float)entity.field_70165_t, (float)entity.field_70163_u, (float)entity.field_70161_v);
               handler.func_147682_a(this.playing);
          }
     }

     public void playMusic(String music, Entity entity) {
          if (!this.isPlaying(music)) {
               this.stopMusic();
               this.playingResource = new ResourceLocation(music);
               this.playingEntity = entity;
               SoundHandler handler = Minecraft.getMinecraft().func_147118_V();
               this.playing = new PositionedSoundRecord(this.playingResource, SoundCategory.MUSIC, 1.0F, 1.0F, false, 0, AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
               handler.func_147682_a(this.playing);
          }
     }

     public boolean isPlaying(String music) {
          ResourceLocation resource = new ResourceLocation(music);
          return this.playingResource != null && this.playingResource.equals(resource) ? Minecraft.getMinecraft().func_147118_V().func_147692_c(this.playing) : false;
     }

     public void playSound(SoundCategory cat, String music, int x, int y, int z, float volumne, float pitch) {
          PositionedSoundRecord rec = new PositionedSoundRecord(new ResourceLocation(music), cat, volumne, pitch, false, 0, AttenuationType.LINEAR, (float)x + 0.5F, (float)y, (float)z + 0.5F);
          Minecraft.getMinecraft().func_147118_V().func_147682_a(rec);
     }
}
