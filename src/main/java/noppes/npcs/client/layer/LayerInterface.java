package noppes.npcs.client.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.entity.EntityCustomNpc;

public abstract class LayerInterface implements LayerRenderer {
     protected RenderLiving render;
     protected EntityCustomNpc npc;
     protected ModelData playerdata;
     public ModelBiped model;

     public LayerInterface(RenderLiving render) {
          this.render = render;
          this.model = (ModelBiped)render.func_177087_b();
     }

     public void setColor(ModelPartData data, EntityLivingBase entity) {
     }

     public void preRender(ModelPartData data) {
          if (data.playerTexture) {
               ClientProxy.bindTexture(this.npc.textureLocation);
          } else {
               ClientProxy.bindTexture(data.getResource());
          }

          if (this.npc.field_70737_aN <= 0 && this.npc.field_70725_aQ <= 0) {
               int color = data.color;
               if (this.npc.display.getTint() != 16777215) {
                    if (data.color != 16777215) {
                         color = this.blend(data.color, this.npc.display.getTint(), 0.5F);
                    } else {
                         color = this.npc.display.getTint();
                    }
               }

               float red = (float)(color >> 16 & 255) / 255.0F;
               float green = (float)(color >> 8 & 255) / 255.0F;
               float blue = (float)(color & 255) / 255.0F;
               GlStateManager.color(red, green, blue, this.npc.func_82150_aj() ? 0.15F : 0.99F);
          }
     }

     private int blend(int color1, int color2, float ratio) {
          if (ratio >= 1.0F) {
               return color2;
          } else if (ratio <= 0.0F) {
               return color1;
          } else {
               int aR = (color1 & 16711680) >> 16;
               int aG = (color1 & '\uff00') >> 8;
               int aB = color1 & 255;
               int bR = (color2 & 16711680) >> 16;
               int bG = (color2 & '\uff00') >> 8;
               int bB = color2 & 255;
               int R = (int)((float)aR + (float)(bR - aR) * ratio);
               int G = (int)((float)aG + (float)(bG - aG) * ratio);
               int B = (int)((float)aB + (float)(bB - aB) * ratio);
               return R << 16 | G << 8 | B;
          }
     }

     public void func_177141_a(EntityLivingBase entity, float par2, float par3, float par8, float par4, float par5, float par6, float par7) {
          this.npc = (EntityCustomNpc)entity;
          if (!this.npc.func_98034_c(Minecraft.getMinecraft().player)) {
               this.playerdata = this.npc.modelData;
               this.model = (ModelBiped)this.render.func_177087_b();
               this.rotate(par2, par3, par4, par5, par6, par7);
               GlStateManager.func_179094_E();
               if (entity.func_82150_aj()) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                    GlStateManager.func_179132_a(false);
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179112_b(770, 771);
                    GlStateManager.func_179092_a(516, 0.003921569F);
               }

               if (this.npc.field_70737_aN > 0 || this.npc.field_70725_aQ > 0) {
                    GlStateManager.color(1.0F, 0.0F, 0.0F, 0.3F);
               }

               if (this.npc.func_70093_af()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
               }

               GlStateManager.enableRescaleNormal();
               this.render(par2, par3, par4, par5, par6, par7);
               GlStateManager.disableRescaleNormal();
               if (entity.func_82150_aj()) {
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179092_a(516, 0.1F);
                    GlStateManager.func_179132_a(true);
               }

               GlStateManager.func_179121_F();
          }
     }

     public void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }

     public boolean func_177142_b() {
          return false;
     }

     public abstract void render(float var1, float var2, float var3, float var4, float var5, float var6);

     public abstract void rotate(float var1, float var2, float var3, float var4, float var5, float var6);
}
