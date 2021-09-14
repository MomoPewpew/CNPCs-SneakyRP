package noppes.npcs.client.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class LayerEyes extends LayerInterface {
     private BufferBuilder tessellator;

     public LayerEyes(RenderLiving render) {
          super(render);
     }

     public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
          if (this.playerdata.eyes.isEnabled()) {
               GlStateManager.pushMatrix();
               this.model.bipedHead.postRender(0.0625F);
               GlStateManager.scale(par7, par7, -par7);
               GlStateManager.translate(0.0F, (float)((this.playerdata.eyes.type == 1 ? 1 : 2) - this.playerdata.eyes.eyePos), 0.0F);
               GlStateManager.enableRescaleNormal();
               GlStateManager.shadeModel(7425);
               GlStateManager.disableTexture2D();
               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
               GlStateManager.enableCull();
               GlStateManager.disableAlpha();
               GlStateManager.depthMask(false);
               int i = this.npc.getBrightnessForRender();
               int j = i % 65536;
               int k = i / 65536;
               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
               Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
               this.tessellator = Tessellator.getInstance().getBuffer();
               this.tessellator.begin(7, DefaultVertexFormats.POSITION_COLOR);
               this.drawLeft();
               this.drawRight();
               this.drawBrows();
               Tessellator.getInstance().draw();
               Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
               GlStateManager.depthMask(true);
               GlStateManager.disableBlend();
               GlStateManager.shadeModel(7424);
               GlStateManager.enableAlpha();
               GlStateManager.disableCull();
               GlStateManager.disableRescaleNormal();
               GlStateManager.popMatrix();
               GlStateManager.enableTexture2D();
          }
     }

     private void drawLeft() {
          if (this.playerdata.eyes.pattern != 2) {
               this.drawRect(3.0D, -5.0D, 1.0D, -4.0D, 16185078, 4.01D);
               this.drawRect(2.0D, -5.0D, 1.0D, -4.0D, this.playerdata.eyes.color, 4.011D);
               if (this.playerdata.eyes.glint && this.npc.isEntityAlive()) {
                    this.drawRect(1.5D, -4.9D, 1.9D, -4.5D, -1, 4.012D);
               }

               if (this.playerdata.eyes.type == 1) {
                    this.drawRect(3.0D, -4.0D, 1.0D, -3.0D, 16777215, 4.01D);
                    this.drawRect(2.0D, -4.0D, 1.0D, -3.0D, this.playerdata.eyes.color, 4.011D);
               }

          }
     }

     private void drawRight() {
          if (this.playerdata.eyes.pattern != 1) {
               this.drawRect(-3.0D, -5.0D, -1.0D, -4.0D, 16185078, 4.01D);
               this.drawRect(-2.0D, -5.0D, -1.0D, -4.0D, this.playerdata.eyes.color, 4.011D);
               if (this.playerdata.eyes.glint && this.npc.isEntityAlive()) {
                    this.drawRect(-1.5D, -4.9D, -1.1D, -4.5D, -1, 4.012D);
               }

               if (this.playerdata.eyes.type == 1) {
                    this.drawRect(-3.0D, -4.0D, -1.0D, -3.0D, 16777215, 4.01D);
                    this.drawRect(-2.0D, -4.0D, -1.0D, -3.0D, this.playerdata.eyes.color, 4.011D);
               }

          }
     }

     private void drawBrows() {
          float offsetY = 0.0F;
          float f;
          if (this.playerdata.eyes.blinkStart > 0L && this.npc.isEntityAlive() && this.npc.deathTime == 0) {
               f = (float)(System.currentTimeMillis() - this.playerdata.eyes.blinkStart) / 150.0F;
               if (f > 1.0F) {
                    f = 2.0F - f;
               }

               if (f < 0.0F) {
                    this.playerdata.eyes.blinkStart = 0L;
                    f = 0.0F;
               }

               offsetY = (float)(this.playerdata.eyes.type == 1 ? 2 : 1) * f;
               this.drawRect(-3.0D, -5.0D, -1.0D, (double)(-5.0F + offsetY), this.playerdata.eyes.skinColor, 4.013D);
               this.drawRect(3.0D, -5.0D, 1.0D, (double)(-5.0F + offsetY), this.playerdata.eyes.skinColor, 4.013D);
          }

          if (this.playerdata.eyes.browThickness > 0) {
               f = (float)this.playerdata.eyes.browThickness / 10.0F;
               this.drawRect(-3.0D, (double)(-5.0F + offsetY), -1.0D, (double)(-5.0F - f + offsetY), this.playerdata.eyes.browColor, 4.014D);
               this.drawRect(1.0D, (double)(-5.0F + offsetY), 3.0D, (double)(-5.0F - f + offsetY), this.playerdata.eyes.browColor, 4.014D);
          }

     }

     public void drawRect(double x, double y, double x2, double y2, int color, double z) {
          double j1;
          if (x < x2) {
               j1 = x;
               x = x2;
               x2 = j1;
          }

          if (y < y2) {
               j1 = y;
               y = y2;
               y2 = j1;
          }

          float f1 = (float)(color >> 16 & 255) / 255.0F;
          float f2 = (float)(color >> 8 & 255) / 255.0F;
          float f3 = (float)(color & 255) / 255.0F;
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.tessellator.pos(x, y, z).color(f1, f2, f3, 1.0F).endVertex();
          this.tessellator.pos(x, y2, z).color(f1, f2, f3, 1.0F).endVertex();
          this.tessellator.pos(x2, y2, z).color(f1, f2, f3, 1.0F).endVertex();
          this.tessellator.pos(x2, y, z).color(f1, f2, f3, 1.0F).endVertex();
     }

     public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
     }
}
