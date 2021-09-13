package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.client.model.ModelPlayerAlt;
import noppes.npcs.constants.EnumParts;

public class LayerNpcCloak extends LayerInterface {
     public LayerNpcCloak(RenderLiving render) {
          super(render);
     }

     public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
          if (this.npc.textureCloakLocation == null) {
               if (this.npc.display.getCapeTexture() == null || this.npc.display.getCapeTexture().isEmpty() || !(this.model instanceof ModelPlayerAlt)) {
                    return;
               }

               this.npc.textureCloakLocation = new ResourceLocation(this.npc.display.getCapeTexture());
          }

          GlStateManager.func_179124_c(0.0F, 0.0F, 0.0F);
          this.render.func_110776_a(this.npc.textureCloakLocation);
          GlStateManager.func_179094_E();
          ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.BODY);
          if (this.npc.isSneaking()) {
               GlStateManager.translate(0.0F, 0.2F, 0.0F);
          }

          GlStateManager.translate(config.transX, config.transY, config.transZ);
          GlStateManager.translate(0.0F, 0.0F, 0.125F);
          double d = this.npc.field_20066_r + (this.npc.field_20063_u - this.npc.field_20066_r) * (double)par7 - (this.npc.field_70169_q + (this.npc.field_70165_t - this.npc.field_70169_q) * (double)par7);
          double d1 = this.npc.field_20065_s + (this.npc.field_20062_v - this.npc.field_20065_s) * (double)par7 - (this.npc.field_70167_r + (this.npc.field_70163_u - this.npc.field_70167_r) * (double)par7);
          double d2 = this.npc.field_20064_t + (this.npc.field_20061_w - this.npc.field_20064_t) * (double)par7 - (this.npc.field_70166_s + (this.npc.field_70161_v - this.npc.field_70166_s) * (double)par7);
          float f11 = this.npc.field_70760_ar + (this.npc.field_70761_aq - this.npc.field_70760_ar) * par7;
          double d3 = (double)MathHelper.func_76126_a(f11 * 3.141593F / 180.0F);
          double d4 = (double)(-MathHelper.func_76134_b(f11 * 3.141593F / 180.0F));
          float f14 = (float)(d * d3 + d2 * d4) * 100.0F;
          float f15 = (float)(d * d4 - d2 * d3) * 100.0F;
          if (f14 < 0.0F) {
               f14 = 0.0F;
          }

          float var10000 = this.npc.field_70126_B + (this.npc.field_70177_z - this.npc.field_70126_B) * par7;
          float f13 = 5.0F;
          if (this.npc.isSneaking()) {
               f13 += 25.0F;
          }

          GlStateManager.func_179114_b(6.0F + f14 / 2.0F + f13, 1.0F, 0.0F, 0.0F);
          GlStateManager.func_179114_b(f15 / 2.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.func_179114_b(-f15 / 2.0F, 0.0F, 1.0F, 0.0F);
          GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
          ((ModelPlayerAlt)this.model).func_178728_c(0.0625F);
          GlStateManager.func_179121_F();
     }

     public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
     }
}
