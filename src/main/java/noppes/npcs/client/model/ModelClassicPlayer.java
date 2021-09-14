package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelClassicPlayer extends ModelPlayerAlt {
     public ModelClassicPlayer(float scale) {
          super(scale, false);
     }

     public void func_78087_a(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          super.func_78087_a(par1, par2, par3, par4, par5, par6, entity);
          float j = 2.0F;
          if (entity.isSprinting()) {
               j = 1.0F;
          }

          ModelRenderer var10000 = this.field_178723_h;
          var10000.field_78795_f += MathHelper.cos(par1 * 0.6662F + 3.1415927F) * j * par2;
          var10000 = this.field_178724_i;
          var10000.field_78795_f += MathHelper.cos(par1 * 0.6662F) * j * par2;
          var10000 = this.field_178724_i;
          var10000.field_78808_h += (MathHelper.cos(par1 * 0.2812F) - 1.0F) * par2;
          var10000 = this.field_178723_h;
          var10000.field_78808_h += (MathHelper.cos(par1 * 0.2312F) + 1.0F) * par2;
          this.field_178734_a.field_78795_f = this.field_178724_i.field_78795_f;
          this.field_178734_a.field_78796_g = this.field_178724_i.field_78796_g;
          this.field_178734_a.field_78808_h = this.field_178724_i.field_78808_h;
          this.field_178732_b.field_78795_f = this.field_178723_h.field_78795_f;
          this.field_178732_b.field_78796_g = this.field_178723_h.field_78796_g;
          this.field_178732_b.field_78808_h = this.field_178723_h.field_78808_h;
     }
}
