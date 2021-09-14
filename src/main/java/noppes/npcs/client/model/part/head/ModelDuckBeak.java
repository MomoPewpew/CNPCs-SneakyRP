package noppes.npcs.client.model.part.head;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelDuckBeak extends ModelRenderer {
	ModelRenderer Top3;
	ModelRenderer Top2;
	ModelRenderer Bottom;
	ModelRenderer Left;
	ModelRenderer Right;
	ModelRenderer Middle;
	ModelRenderer Top;

	public ModelDuckBeak(ModelBiped base) {
		super(base);
		this.Top3 = new ModelRenderer(base, 14, 0);
		this.Top3.addBox(0.0F, 0.0F, 0.0F, 2, 1, 3);
		this.Top3.setRotationPoint(-1.0F, -2.0F, -5.0F);
		this.setRotation(this.Top3, 0.3346075F, 0.0F, 0.0F);
		this.addChild(this.Top3);
		this.Top2 = new ModelRenderer(base, 0, 0);
		this.Top2.addBox(0.0F, 0.0F, -0.4F, 4, 1, 3);
		this.Top2.setRotationPoint(-2.0F, -3.0F, -2.0F);
		this.setRotation(this.Top2, 0.3346075F, 0.0F, 0.0F);
		this.addChild(this.Top2);
		this.Bottom = new ModelRenderer(base, 24, 0);
		this.Bottom.addBox(0.0F, 0.0F, 0.0F, 2, 1, 5);
		this.Bottom.setRotationPoint(-1.0F, -1.0F, -5.0F);
		this.addChild(this.Bottom);
		this.Left = new ModelRenderer(base, 0, 4);
		this.Left.mirror = true;
		this.Left.addBox(0.0F, 0.0F, 0.0F, 1, 3, 2);
		this.Left.setRotationPoint(0.98F, -3.0F, -2.0F);
		this.addChild(this.Left);
		this.Right = new ModelRenderer(base, 0, 4);
		this.Right.addBox(0.0F, 0.0F, 0.0F, 1, 3, 2);
		this.Right.setRotationPoint(-1.98F, -3.0F, -2.0F);
		this.addChild(this.Right);
		this.Middle = new ModelRenderer(base, 3, 0);
		this.Middle.addBox(0.0F, 0.0F, 0.0F, 2, 1, 3);
		this.Middle.setRotationPoint(-1.0F, -2.0F, -5.0F);
		this.addChild(this.Middle);
		this.Top = new ModelRenderer(base, 6, 4);
		this.Top.addBox(0.0F, 0.0F, 0.0F, 2, 2, 1);
		this.Top.setRotationPoint(-1.0F, -4.4F, -1.0F);
		this.addChild(this.Top);
	}

	public void render(float f) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.0F, -1.0F * f);
		GlStateManager.scale(0.82F, 0.82F, 0.7F);
		super.render(f);
		GlStateManager.popMatrix();
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
