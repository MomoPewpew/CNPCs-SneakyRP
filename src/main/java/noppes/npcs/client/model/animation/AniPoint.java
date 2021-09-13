package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class AniPoint {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped base) {
          base.field_178723_h.field_78795_f = -1.570796F;
          base.field_178723_h.field_78796_g = par4 / 57.295776F;
          base.field_178723_h.field_78808_h = 0.0F;
     }
}
