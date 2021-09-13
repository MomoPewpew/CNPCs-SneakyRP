package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class AniHug {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped base) {
          float f6 = MathHelper.func_76126_a(base.field_78095_p * 3.141593F);
          float f7 = MathHelper.func_76126_a((1.0F - (1.0F - base.field_78095_p) * (1.0F - base.field_78095_p)) * 3.141593F);
          base.field_178723_h.field_78808_h = 0.0F;
          base.field_178724_i.field_78808_h = 0.0F;
          base.field_178723_h.field_78796_g = -(0.1F - f6 * 0.6F);
          base.field_178724_i.field_78796_g = 0.1F;
          base.field_178723_h.field_78795_f = -1.570796F;
          base.field_178724_i.field_78795_f = -1.570796F;
          ModelRenderer var10000 = base.field_178723_h;
          var10000.field_78795_f -= f6 * 1.2F - f7 * 0.4F;
          var10000 = base.field_178723_h;
          var10000.field_78808_h += MathHelper.func_76134_b(par3 * 0.09F) * 0.05F + 0.05F;
          var10000 = base.field_178724_i;
          var10000.field_78808_h -= MathHelper.func_76134_b(par3 * 0.09F) * 0.05F + 0.05F;
          var10000 = base.field_178723_h;
          var10000.field_78795_f += MathHelper.func_76126_a(par3 * 0.067F) * 0.05F;
          var10000 = base.field_178724_i;
          var10000.field_78795_f -= MathHelper.func_76126_a(par3 * 0.067F) * 0.05F;
     }
}
