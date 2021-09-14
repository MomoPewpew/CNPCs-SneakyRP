package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class AniHug {
	public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity entity, ModelBiped base) {
		float f6 = MathHelper.sin(base.swingProgress * 3.141593F);
		float f7 = MathHelper.sin((1.0F - (1.0F - base.swingProgress) * (1.0F - base.swingProgress)) * 3.141593F);
		base.bipedRightArm.rotateAngleZ = 0.0F;
		base.bipedLeftArm.rotateAngleZ = 0.0F;
		base.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
		base.bipedLeftArm.rotateAngleY = 0.1F;
		base.bipedRightArm.rotateAngleX = -1.570796F;
		base.bipedLeftArm.rotateAngleX = -1.570796F;
		ModelRenderer var10000 = base.bipedRightArm;
		var10000.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		var10000 = base.bipedRightArm;
		var10000.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		var10000 = base.bipedLeftArm;
		var10000.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
		var10000 = base.bipedRightArm;
		var10000.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
		var10000 = base.bipedLeftArm;
		var10000.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
	}
}
