package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.client.model.blocks.ModelCarpentryBench;

public class BlockCarpentryBenchRenderer extends TileEntitySpecialRenderer {
     private final ModelCarpentryBench model = new ModelCarpentryBench();
     private static final ResourceLocation TEXTURE = new ResourceLocation("customnpcs", "textures/models/carpentrybench.png");

     public void func_192841_a(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
          int rotation = 0;
          if (te != null && te.func_174877_v() != BlockPos.field_177992_a) {
               rotation = te.func_145832_p() % 4;
          }

          GlStateManager.func_179094_E();
          GlStateManager.func_179084_k();
          GlStateManager.func_179145_e();
          GlStateManager.func_179109_b((float)x + 0.5F, (float)y + 1.4F, (float)z + 0.5F);
          GlStateManager.func_179152_a(0.95F, 0.95F, 0.95F);
          GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.func_179114_b((float)(90 * rotation), 0.0F, 1.0F, 0.0F);
          this.func_147499_a(TEXTURE);
          this.model.func_78088_a((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
          GlStateManager.func_179121_F();
     }
}
