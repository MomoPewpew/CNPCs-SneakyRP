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
               GlStateManager.func_179094_E();
               this.model.field_78116_c.func_78794_c(0.0625F);
               GlStateManager.func_179152_a(par7, par7, -par7);
               GlStateManager.translate(0.0F, (float)((this.playerdata.eyes.type == 1 ? 1 : 2) - this.playerdata.eyes.eyePos), 0.0F);
               GlStateManager.enableRescaleNormal();
               GlStateManager.func_179103_j(7425);
               GlStateManager.func_179090_x();
               GlStateManager.func_179147_l();
               GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
               GlStateManager.func_179089_o();
               GlStateManager.func_179118_c();
               GlStateManager.func_179132_a(false);
               int i = this.npc.func_70070_b();
               int j = i % 65536;
               int k = i / 65536;
               OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)j, (float)k);
               Minecraft.getMinecraft().field_71460_t.func_191514_d(true);
               this.tessellator = Tessellator.func_178181_a().func_178180_c();
               this.tessellator.func_181668_a(7, DefaultVertexFormats.field_181706_f);
               this.drawLeft();
               this.drawRight();
               this.drawBrows();
               Tessellator.func_178181_a().func_78381_a();
               Minecraft.getMinecraft().field_71460_t.func_191514_d(false);
               GlStateManager.func_179132_a(true);
               GlStateManager.func_179084_k();
               GlStateManager.func_179103_j(7424);
               GlStateManager.func_179141_d();
               GlStateManager.func_179129_p();
               GlStateManager.disableRescaleNormal();
               GlStateManager.func_179121_F();
               GlStateManager.func_179098_w();
          }
     }

     private void drawLeft() {
          if (this.playerdata.eyes.pattern != 2) {
               this.drawRect(3.0D, -5.0D, 1.0D, -4.0D, 16185078, 4.01D);
               this.drawRect(2.0D, -5.0D, 1.0D, -4.0D, this.playerdata.eyes.color, 4.011D);
               if (this.playerdata.eyes.glint && this.npc.func_70089_S()) {
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
               if (this.playerdata.eyes.glint && this.npc.func_70089_S()) {
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
          if (this.playerdata.eyes.blinkStart > 0L && this.npc.func_70089_S() && this.npc.field_70725_aQ == 0) {
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
          this.tessellator.func_181662_b(x, y, z).func_181666_a(f1, f2, f3, 1.0F).func_181675_d();
          this.tessellator.func_181662_b(x, y2, z).func_181666_a(f1, f2, f3, 1.0F).func_181675_d();
          this.tessellator.func_181662_b(x2, y2, z).func_181666_a(f1, f2, f3, 1.0F).func_181675_d();
          this.tessellator.func_181662_b(x2, y, z).func_181666_a(f1, f2, f3, 1.0F).func_181675_d();
     }

     public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
     }
}
