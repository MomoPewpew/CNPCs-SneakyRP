package noppes.npcs.client.renderer;

import noppes.npcs.client.model.ModelNpcCrystal;
import noppes.npcs.entity.EntityNpcCrystal;

public class RenderNpcCrystal extends RenderNPCInterface<EntityNpcCrystal> {
	ModelNpcCrystal mainmodel;

	public RenderNpcCrystal(ModelNpcCrystal model) {
		super(model, 0.0F);
		this.mainmodel = model;
	}
}
