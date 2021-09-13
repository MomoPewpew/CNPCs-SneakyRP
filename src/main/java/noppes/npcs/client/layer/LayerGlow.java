package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

@SideOnly(Side.CLIENT)
public class LayerGlow implements LayerRenderer {
     private final RenderCustomNpc renderer;

     public LayerGlow(RenderCustomNpc npcRenderer) {
          this.renderer = npcRenderer;
     }

     public void doRenderLayer(EntityNPCInterface npc, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
          if (!npc.display.getOverlayTexture().isEmpty()) {
               if (npc.textureGlowLocation == null) {
                    npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
               }

               this.renderer.func_110776_a(npc.textureGlowLocation);
               GlStateManager.func_179147_l();
               GlStateManager.func_179112_b(1, 1);
               GlStateManager.func_179140_f();
               GlStateManager.func_179143_c(514);
               char c0 = '\uf0f0';
               int i = c0 % 65536;
               int j = c0 / 65536;
               OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)i / 1.0F, (float)j / 1.0F);
               GlStateManager.func_179145_e();
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               this.renderer.func_177087_b().func_78088_a(npc, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               this.renderer.func_177105_a(npc);
               GlStateManager.func_179084_k();
               GlStateManager.func_179141_d();
               GlStateManager.func_179143_c(515);
          }
     }

     public boolean func_177142_b() {
          return true;
     }
}
