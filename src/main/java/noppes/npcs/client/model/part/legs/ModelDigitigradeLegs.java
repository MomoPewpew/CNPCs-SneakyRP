package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelDigitigradeLegs extends ModelRenderer {
     private ModelRenderer rightleg;
     private ModelRenderer rightleg2;
     private ModelRenderer rightleglow;
     private ModelRenderer rightfoot;
     private ModelRenderer leftleg;
     private ModelRenderer leftleg2;
     private ModelRenderer leftleglow;
     private ModelRenderer leftfoot;
     private ModelBiped base;

     public ModelDigitigradeLegs(ModelBiped base) {
          super(base);
          this.base = base;
          this.rightleg = new ModelRenderer(base, 0, 16);
          this.rightleg.func_78789_a(-2.0F, 0.0F, -2.0F, 4, 6, 4);
          this.rightleg.func_78793_a(-2.1F, 11.0F, 0.0F);
          this.setRotation(this.rightleg, -0.3F, 0.0F, 0.0F);
          this.func_78792_a(this.rightleg);
          this.rightleg2 = new ModelRenderer(base, 0, 20);
          this.rightleg2.func_78789_a(-1.5F, -1.0F, -2.0F, 3, 7, 3);
          this.rightleg2.func_78793_a(0.0F, 4.1F, 0.0F);
          this.setRotation(this.rightleg2, 1.1F, 0.0F, 0.0F);
          this.rightleg.func_78792_a(this.rightleg2);
          this.rightleglow = new ModelRenderer(base, 0, 24);
          this.rightleglow.func_78789_a(-1.5F, 0.0F, -1.0F, 3, 5, 2);
          this.rightleglow.func_78793_a(0.0F, 5.0F, 0.0F);
          this.setRotation(this.rightleglow, -1.35F, 0.0F, 0.0F);
          this.rightleg2.func_78792_a(this.rightleglow);
          this.rightfoot = new ModelRenderer(base, 1, 26);
          this.rightfoot.func_78789_a(-1.5F, 0.0F, -5.0F, 3, 2, 4);
          this.rightfoot.func_78793_a(0.0F, 3.7F, 1.2F);
          this.setRotation(this.rightfoot, 0.55F, 0.0F, 0.0F);
          this.rightleglow.func_78792_a(this.rightfoot);
          this.leftleg = new ModelRenderer(base, 0, 16);
          this.leftleg.field_78809_i = true;
          this.leftleg.func_78789_a(-2.0F, 0.0F, -2.0F, 4, 6, 4);
          this.leftleg.func_78793_a(2.1F, 11.0F, 0.0F);
          this.setRotation(this.leftleg, -0.3F, 0.0F, 0.0F);
          this.func_78792_a(this.leftleg);
          this.leftleg2 = new ModelRenderer(base, 0, 20);
          this.leftleg2.field_78809_i = true;
          this.leftleg2.func_78789_a(-1.5F, -1.0F, -2.0F, 3, 7, 3);
          this.leftleg2.func_78793_a(0.0F, 4.1F, 0.0F);
          this.setRotation(this.leftleg2, 1.1F, 0.0F, 0.0F);
          this.leftleg.func_78792_a(this.leftleg2);
          this.leftleglow = new ModelRenderer(base, 0, 24);
          this.leftleglow.field_78809_i = true;
          this.leftleglow.func_78789_a(-1.5F, 0.0F, -1.0F, 3, 5, 2);
          this.leftleglow.func_78793_a(0.0F, 5.0F, 0.0F);
          this.setRotation(this.leftleglow, -1.35F, 0.0F, 0.0F);
          this.leftleg2.func_78792_a(this.leftleglow);
          this.leftfoot = new ModelRenderer(base, 1, 26);
          this.leftfoot.field_78809_i = true;
          this.leftfoot.func_78789_a(-1.5F, 0.0F, -5.0F, 3, 2, 4);
          this.leftfoot.func_78793_a(0.0F, 3.7F, 1.2F);
          this.setRotation(this.leftfoot, 0.55F, 0.0F, 0.0F);
          this.leftleglow.func_78792_a(this.leftfoot);
     }

     public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          this.rightleg.field_78795_f = this.base.field_178721_j.field_78795_f - 0.3F;
          this.leftleg.field_78795_f = this.base.field_178722_k.field_78795_f - 0.3F;
          this.rightleg.field_78797_d = this.base.field_178721_j.field_78797_d;
          this.leftleg.field_78797_d = this.base.field_178722_k.field_78797_d;
          this.rightleg.field_78798_e = this.base.field_178721_j.field_78798_e;
          this.leftleg.field_78798_e = this.base.field_178722_k.field_78798_e;
          if (!this.base.field_78117_n) {
               --this.leftleg.field_78797_d;
               --this.rightleg.field_78797_d;
          }

     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
