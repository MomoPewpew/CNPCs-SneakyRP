package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class AniWaving {
	public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6,
			Entity entity, ModelBiped base) {
		float f = MathHelper.sin((float) entity.ticksExisted * 0.27F);
		float f2 = MathHelper.sin((float) (entity.ticksExisted + 1) * 0.27F);
		f += (f2 - f) * Minecraft.getMinecraft().getRenderPartialTicks();
		base.bipedRightArm.rotateAngleX = -0.1F;
		base.bipedRightArm.rotateAngleY = 0.0F;
		base.bipedRightArm.rotateAngleZ = (float) (2.141592653589793D - (double) (f * 0.5F));
	}
}
