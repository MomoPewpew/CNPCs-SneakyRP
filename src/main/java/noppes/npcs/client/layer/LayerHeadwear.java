package noppes.npcs.client.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.model.part.head.ModelHeadwear;
import noppes.npcs.entity.EntityCustomNpc;

public class LayerHeadwear extends LayerInterface implements LayerPreRender {
	private ModelHeadwear headwear;

	public LayerHeadwear(RenderLiving render) {
		super(render);
		this.headwear = new ModelHeadwear(this.model);
	}

	public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
		if (CustomNpcs.HeadWearType == 1) {
			if (this.npc.hurtTime <= 0 && this.npc.deathTime <= 0) {
				int color = this.npc.display.getTint();
				float red = (float) (color >> 16 & 255) / 255.0F;
				float green = (float) (color >> 8 & 255) / 255.0F;
				float blue = (float) (color & 255) / 255.0F;
				GlStateManager.color(red, green, blue, 1.0F);
			}

			ClientProxy.bindTexture(this.npc.textureLocation);
			this.model.bipedHead.postRender(par7);
			this.headwear.render(par7);
		}
	}

	public void rotate(float par2, float par3, float par4, float par5, float par6, float par7) {
	}

	public void preRender(EntityCustomNpc player) {
		this.model.bipedHeadwear.isHidden = CustomNpcs.HeadWearType == 1;
		this.headwear.config = null;
	}
}
