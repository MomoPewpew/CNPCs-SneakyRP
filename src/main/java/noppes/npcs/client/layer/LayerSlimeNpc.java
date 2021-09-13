package noppes.npcs.client.layer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import noppes.npcs.client.model.ModelNpcSlime;

public class LayerSlimeNpc implements LayerRenderer {
     private final RenderLiving renderer;
     private final ModelBase slimeModel = new ModelNpcSlime(0);

     public LayerSlimeNpc(RenderLiving renderer) {
          this.renderer = renderer;
     }

     public boolean func_177142_b() {
          return true;
     }

     public void func_177141_a(EntityLivingBase living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
          if (!living.func_82150_aj()) {
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179108_z();
               GlStateManager.func_179147_l();
               GlStateManager.func_179112_b(770, 771);
               this.slimeModel.func_178686_a(this.renderer.func_177087_b());
               this.slimeModel.func_78088_a(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               GlStateManager.func_179084_k();
               GlStateManager.func_179133_A();
          }
     }
}
