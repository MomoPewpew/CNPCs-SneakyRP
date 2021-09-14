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
          super(npc.world, par2, par4, par6, par8, par10, par12);
          this.npc = npc;
          this.particleNumber = npc.getRNG().nextInt(2);
          this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
          this.particleRed = (float)(data.color >> 16 & 255) / 255.0F;
          this.particleGreen = (float)(data.color >> 8 & 255) / 255.0F;
          this.particleBlue = (float)(data.color & 255) / 255.0F;
          if (npc.getRNG().nextInt(3) == 1) {
               this.move = false;
               this.startX = (float)npc.posX;
               this.startY = (float)npc.posY;
               this.startZ = (float)npc.posZ;
          }

          if (data.playerTexture) {
               this.location = npc.textureLocation;
          } else {
               this.location = data.getResource();
          }

     }

     public void renderParticle(BufferBuilder renderer, Entity entity, float partialTicks, float par3, float par4, float par5, float par6, float par7) {
          if (this.move) {
               this.startX = (float)(this.npc.prevPosX + (this.npc.posX - this.npc.prevPosX) * (double)partialTicks);
               this.startY = (float)(this.npc.prevPosY + (this.npc.posY - this.npc.prevPosY) * (double)partialTicks);
               this.startZ = (float)(this.npc.prevPosZ + (this.npc.posZ - this.npc.prevPosZ) * (double)partialTicks);
          }

          Tessellator tessellator = Tessellator.getInstance();
          tessellator.draw();
          float scale = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge;
          scale = 1.0F - scale;
          scale *= scale;
          scale = 1.0F - scale;
          this.particleScale = this.portalParticleScale * scale;
          ClientProxy.bindTexture(this.location);
          float f = 0.875F;
          float f1 = f + 0.125F;
          float f2 = 0.75F - (float)this.particleNumber * 0.25F;
          float f3 = f2 + 0.25F;
          float f4 = 0.1F * this.particleScale;
          float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX + (double)this.startX);
          float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY + (double)this.startY);
          float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ + (double)this.startZ);
          int i = this.getBrightnessForRender(partialTicks);
          int j = i >> 16 & '\uffff';
          int k = i & '\uffff';
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          renderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
          renderer.pos((double)(f5 - par3 * f4 - par6 * f4), (double)(f6 - par4 * f4), (double)(f7 - par5 * f4 - par7 * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
          renderer.pos((double)(f5 - par3 * f4 + par6 * f4), (double)(f6 + par4 * f4), (double)(f7 - par5 * f4 + par7 * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
          renderer.pos((double)(f5 + par3 * f4 + par6 * f4), (double)(f6 + par4 * f4), (double)(f7 + par5 * f4 + par7 * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
          renderer.pos((double)(f5 + par3 * f4 - par6 * f4), (double)(f6 - par4 * f4), (double)(f7 + par5 * f4 - par7 * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
          tessellator.draw();
          ClientProxy.bindTexture(resource);
          renderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
     }

     public int getFXLayer() {
          return 0;
     }
}
