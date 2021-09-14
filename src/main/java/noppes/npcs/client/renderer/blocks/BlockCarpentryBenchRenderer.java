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
	private static final ResourceLocation TEXTURE = new ResourceLocation("customnpcs",
			"textures/models/carpentrybench.png");

	public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int rotation = 0;
		if (te != null && te.getPos() != BlockPos.ORIGIN) {
			rotation = te.getBlockMetadata() % 4;
		}

		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.4F, (float) z + 0.5F);
		GlStateManager.scale(0.95F, 0.95F, 0.95F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate((float) (90 * rotation), 0.0F, 1.0F, 0.0F);
		this.bindTexture(TEXTURE);
		this.model.render((Entity) null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}
}
