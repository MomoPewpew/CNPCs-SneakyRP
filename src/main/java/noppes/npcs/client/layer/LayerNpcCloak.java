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
			if (this.npc.display.getCapeTexture() == null || this.npc.display.getCapeTexture().isEmpty()
					|| !(this.model instanceof ModelPlayerAlt)) {
				return;
			}

			this.npc.textureCloakLocation = new ResourceLocation(this.npc.display.getCapeTexture());
		}

		GlStateManager.color(0.0F, 0.0F, 0.0F);
		this.render.bindTexture(this.npc.textureCloakLocation);
		GlStateManager.pushMatrix();
		ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.BODY);
		if (this.npc.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		GlStateManager.translate(config.transX, config.transY, config.transZ);
		GlStateManager.translate(0.0F, 0.0F, 0.125F);
		double d = this.npc.field_20066_r + (this.npc.field_20063_u - this.npc.field_20066_r) * (double) par7
				- (this.npc.prevPosX + (this.npc.posX - this.npc.prevPosX) * (double) par7);
		double d1 = this.npc.field_20065_s + (this.npc.field_20062_v - this.npc.field_20065_s) * (double) par7
				- (this.npc.prevPosY + (this.npc.posY - this.npc.prevPosY) * (double) par7);
		double d2 = this.npc.field_20064_t + (this.npc.field_20061_w - this.npc.field_20064_t) * (double) par7
				- (this.npc.prevPosZ + (this.npc.posZ - this.npc.prevPosZ) * (double) par7);
		float f11 = this.npc.prevRenderYawOffset + (this.npc.renderYawOffset - this.npc.prevRenderYawOffset) * par7;
		double d3 = (double) MathHelper.sin(f11 * 3.141593F / 180.0F);
		double d4 = (double) (-MathHelper.cos(f11 * 3.141593F / 180.0F));
		float f14 = (float) (d * d3 + d2 * d4) * 100.0F;
		float f15 = (float) (d * d4 - d2 * d3) * 100.0F;
		if (f14 < 0.0F) {
			f14 = 0.0F;
		}

		float var10000 = this.npc.prevRotationYaw + (this.npc.rotationYaw - this.npc.prevRotationYaw) * par7;
		float f13 = 5.0F;
		if (this.npc.isSneaking()) {
			f13 += 25.0F;
		}

		GlStateManager.rotate(6.0F + f14 / 2.0F + f13, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f15 / 2.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-f15 / 2.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		((ModelPlayerAlt) this.model).renderCape(0.0625F);
		GlStateManager.popMatrix();
	}

	public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
	}
}
