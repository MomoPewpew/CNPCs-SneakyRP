package noppes.npcs.client.fx;

import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.entity.EntityCustomNpc;

public class EntityEnderFX extends ParticlePortal {
     private float portalParticleScale;
     private int particleNumber;
     private EntityCustomNpc npc;
     private static final ResourceLocation resource = new ResourceLocation("textures/particle/particles.png");
     private final ResourceLocation location;
     private boolean move = true;
     private float startX = 0.0F;
     private float startY = 0.0F;
     private float startZ = 0.0F;

     public EntityEnderFX(EntityCustomNpc npc, double par2, double par4, double par6, double par8, double par10, double par12, ModelPartData data) {
          super(npc.field_70170_p, par2, par4, par6, par8, par10, par12);
          this.npc = npc;
          this.particleNumber = npc.func_70681_au().nextInt(2);
          this.portalParticleScale = this.field_70544_f = this.field_187136_p.nextFloat() * 0.2F + 0.5F;
          this.field_70552_h = (float)(data.color >> 16 & 255) / 255.0F;
          this.field_70553_i = (float)(data.color >> 8 & 255) / 255.0F;
          this.field_70551_j = (float)(data.color & 255) / 255.0F;
          if (npc.func_70681_au().nextInt(3) == 1) {
               this.move = false;
               this.startX = (float)npc.field_70165_t;
               this.startY = (float)npc.field_70163_u;
               this.startZ = (float)npc.field_70161_v;
          }

          if (data.playerTexture) {
               this.location = npc.textureLocation;
          } else {
               this.location = data.getResource();
          }

     }

     public void func_180434_a(BufferBuilder renderer, Entity entity, float partialTicks, float par3, float par4, float par5, float par6, float par7) {
          if (this.move) {
               this.startX = (float)(this.npc.field_70169_q + (this.npc.field_70165_t - this.npc.field_70169_q) * (double)partialTicks);
               this.startY = (float)(this.npc.field_70167_r + (this.npc.field_70163_u - this.npc.field_70167_r) * (double)partialTicks);
               this.startZ = (float)(this.npc.field_70166_s + (this.npc.field_70161_v - this.npc.field_70166_s) * (double)partialTicks);
          }

          Tessellator tessellator = Tessellator.func_178181_a();
          tessellator.func_78381_a();
          float scale = ((float)this.field_70546_d + partialTicks) / (float)this.field_70547_e;
          scale = 1.0F - scale;
          scale *= scale;
          scale = 1.0F - scale;
          this.field_70544_f = this.portalParticleScale * scale;
          ClientProxy.bindTexture(this.location);
          float f = 0.875F;
          float f1 = f + 0.125F;
          float f2 = 0.75F - (float)this.particleNumber * 0.25F;
          float f3 = f2 + 0.25F;
          float f4 = 0.1F * this.field_70544_f;
          float f5 = (float)(this.field_187123_c + (this.field_187126_f - this.field_187123_c) * (double)partialTicks - field_70556_an + (double)this.startX);
          float f6 = (float)(this.field_187124_d + (this.field_187127_g - this.field_187124_d) * (double)partialTicks - field_70554_ao + (double)this.startY);
          float f7 = (float)(this.field_187125_e + (this.field_187128_h - this.field_187125_e) * (double)partialTicks - field_70555_ap + (double)this.startZ);
          int i = this.func_189214_a(partialTicks);
          int j = i >> 16 & '\uffff';
          int k = i & '\uffff';
          GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
          renderer.func_181668_a(7, DefaultVertexFormats.field_181704_d);
          renderer.func_181662_b((double)(f5 - par3 * f4 - par6 * f4), (double)(f6 - par4 * f4), (double)(f7 - par5 * f4 - par7 * f4)).func_187315_a((double)f1, (double)f3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
          renderer.func_181662_b((double)(f5 - par3 * f4 + par6 * f4), (double)(f6 + par4 * f4), (double)(f7 - par5 * f4 + par7 * f4)).func_187315_a((double)f1, (double)f2).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
          renderer.func_181662_b((double)(f5 + par3 * f4 + par6 * f4), (double)(f6 + par4 * f4), (double)(f7 + par5 * f4 + par7 * f4)).func_187315_a((double)f, (double)f2).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
          renderer.func_181662_b((double)(f5 + par3 * f4 - par6 * f4), (double)(f6 - par4 * f4), (double)(f7 + par5 * f4 - par7 * f4)).func_187315_a((double)f, (double)f3).func_181666_a(this.field_70552_h, this.field_70553_i, this.field_70551_j, 1.0F).func_187314_a(j, k).func_181675_d();
          tessellator.func_78381_a();
          ClientProxy.bindTexture(resource);
          renderer.func_181668_a(7, DefaultVertexFormats.field_181704_d);
     }

     public int func_70537_b() {
          return 0;
     }
}
