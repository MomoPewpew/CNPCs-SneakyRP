package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelTailFin extends ModelRenderer {
     public ModelTailFin(ModelBiped base) {
          super(base);
          ModelRenderer Shape1 = new ModelRenderer(base, 0, 0);
          Shape1.func_78789_a(-2.0F, -2.0F, -2.0F, 3, 3, 8);
          Shape1.func_78793_a(0.5F, 0.0F, 1.0F);
          this.setRotation(Shape1, -0.669215F, 0.0F, 0.0F);
          this.func_78792_a(Shape1);
          ModelRenderer Shape2 = new ModelRenderer(base, 2, 2);
          Shape2.func_78789_a(-1.0F, -1.0F, 1.0F, 3, 2, 6);
          Shape2.func_78793_a(-0.5F, 3.0F, 4.5F);
          this.setRotation(Shape2, -0.2602503F, 0.0F, 0.0F);
          this.func_78792_a(Shape2);
          ModelRenderer Shape3 = new ModelRenderer(base, 0, 11);
          Shape3.func_78789_a(-1.0F, -1.0F, -1.0F, 3, 1, 6);
          Shape3.func_78793_a(0.5F, 5.0F, 12.0F);
          this.setRotation(Shape3, 0.0F, 1.07818F, 0.0F);
          this.func_78792_a(Shape3);
          ModelRenderer Shape4 = new ModelRenderer(base, 0, 11);
          Shape4.field_78809_i = true;
          Shape4.func_78789_a(-2.0F, 0.0F, -1.0F, 3, 1, 6);
          Shape4.func_78793_a(-0.5F, 4.0F, 12.0F);
          this.setRotation(Shape4, 0.0F, -1.003822F, 0.0F);
          this.func_78792_a(Shape4);
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
