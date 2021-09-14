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

	public boolean shouldCombineTextures() {
		return true;
	}

	public void doRenderLayer(EntityLivingBase living, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!living.isInvisible()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableNormalize();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			this.slimeModel.setModelAttributes(this.renderer.getMainModel());
			this.slimeModel.render(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.disableBlend();
			GlStateManager.disableNormalize();
		}
	}
}
