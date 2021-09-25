package noppes.npcs.client.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.entity.EntityNpcDragon;

public class RenderNpcDragon extends RenderNPCInterface<EntityNpcDragon> {
	public RenderNpcDragon(ModelBase model, float f) {
		super(model, f);
	}

	protected void preRenderCallback(EntityNpcDragon npc, float f) {
		GlStateManager.translate(0.0F, 0.0F, 0.120000005F * (float) npc.display.getSize());
		super.preRenderCallback(npc, f);
	}
}
