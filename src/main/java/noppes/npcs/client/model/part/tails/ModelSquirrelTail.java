package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSquirrelTail extends ModelRenderer {
     private ModelBiped base;

     public ModelSquirrelTail(ModelBiped base) {
          super(base);
          this.base = base;
          ModelRenderer Shape1 = new ModelRenderer(base, 0, 0);
          Shape1.func_78789_a(-1.0F, -1.0F, -1.0F, 2, 2, 3);
          Shape1.func_78793_a(0.0F, -1.0F, 3.0F);
          this.setRotation(Shape1, 0.0F, 0.0F, 0.0F);
          this.func_78792_a(Shape1);
          ModelRenderer Shape2 = new ModelRenderer(base, 0, 9);
          Shape2.func_78789_a(-2.0F, -5.0F, -1.0F, 4, 5, 3);
          Shape2.func_78793_a(0.0F, 0.0F, 1.0F);
          this.setRotation(Shape2, -0.37F, 0.0F, 0.0F);
          Shape1.func_78792_a(Shape2);
          ModelRenderer Shape3 = new ModelRenderer(base, 0, 18);
          Shape3.func_78789_a(-2.466667F, -6.0F, -1.0F, 5, 7, 3);
          Shape3.func_78793_a(0.0F, -5.0F, 0.0F);
          this.setRotation(Shape3, 0.3F, 0.0F, 0.0F);
          Shape2.func_78792_a(Shape3);
          ModelRenderer Shape4 = new ModelRenderer(base, 25, 0);
          Shape4.func_78789_a(-3.0F, -0.6F, -1.0F, 6, 5, 3);
          Shape4.func_78793_a(0.0F, -5.0F, 1.0F);
          this.setRotation(Shape4, 2.5F, 0.0F, 0.0F);
          Shape3.func_78792_a(Shape4);
          ModelRenderer Shape5 = new ModelRenderer(base, 25, 10);
          Shape5.func_78789_a(-3.0F, -2.0F, -1.0F, 6, 3, 5);
          Shape5.func_78793_a(0.0F, 3.5F, 0.0F);
          this.setRotation(Shape5, -2.5F, 0.0F, 0.0F);
          Shape4.func_78792_a(Shape5);
     }

     public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
