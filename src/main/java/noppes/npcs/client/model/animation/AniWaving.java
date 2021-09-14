package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class AniWaving {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped base) {
          float f = MathHelper.sin((float)entity.field_70173_aa * 0.27F);
          float f2 = MathHelper.sin((float)(entity.field_70173_aa + 1) * 0.27F);
          f += (f2 - f) * Minecraft.getMinecraft().func_184121_ak();
          base.field_178723_h.field_78795_f = -0.1F;
          base.field_178723_h.field_78796_g = 0.0F;
          base.field_178723_h.field_78808_h = (float)(2.141592653589793D - (double)(f * 0.5F));
     }
}
