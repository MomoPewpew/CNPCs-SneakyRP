package noppes.npcs.client.model.animation;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class AniCrawling {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
          model.field_78116_c.field_78808_h = -par4 / 57.295776F;
          model.field_78116_c.field_78796_g = 0.0F;
          model.field_78116_c.field_78795_f = -0.95993114F;
          model.field_178720_f.field_78795_f = model.field_78116_c.field_78795_f;
          model.field_178720_f.field_78796_g = model.field_78116_c.field_78796_g;
          model.field_178720_f.field_78808_h = model.field_78116_c.field_78808_h;
          if ((double)par2 > 0.25D) {
               par2 = 0.25F;
          }

          float movement = MathHelper.cos(par1 * 0.8F + 3.1415927F) * par2;
          model.field_178724_i.field_78795_f = 3.1415927F - movement * 0.25F;
          model.field_178724_i.field_78796_g = movement * -0.46F;
          model.field_178724_i.field_78808_h = movement * -0.2F;
          model.field_178724_i.field_78797_d = 2.0F - movement * 9.0F;
          model.field_178723_h.field_78795_f = 3.1415927F + movement * 0.25F;
          model.field_178723_h.field_78796_g = movement * -0.4F;
          model.field_178723_h.field_78808_h = movement * -0.2F;
          model.field_178723_h.field_78797_d = 2.0F + movement * 9.0F;
          model.field_78115_e.field_78796_g = movement * 0.1F;
          model.field_78115_e.field_78795_f = 0.0F;
          model.field_78115_e.field_78808_h = movement * 0.1F;
          model.field_178722_k.field_78795_f = movement * 0.1F;
          model.field_178722_k.field_78796_g = movement * 0.1F;
          model.field_178722_k.field_78808_h = -0.122173056F - movement * 0.25F;
          model.field_178722_k.field_78797_d = 10.4F + movement * 9.0F;
          model.field_178722_k.field_78798_e = movement * 0.6F;
          model.field_178721_j.field_78795_f = movement * -0.1F;
          model.field_178721_j.field_78796_g = movement * 0.1F;
          model.field_178721_j.field_78808_h = 0.122173056F - movement * 0.25F;
          model.field_178721_j.field_78797_d = 10.4F - movement * 9.0F;
          model.field_178721_j.field_78798_e = movement * -0.6F;
     }
}
