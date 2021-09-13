package noppes.npcs.client.model.part.horns;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelAntennasFront extends ModelRenderer {
     public ModelAntennasFront(ModelBiped base) {
          super(base);
          ModelRenderer rightantenna1 = new ModelRenderer(base, 60, 27);
          rightantenna1.func_78789_a(0.0F, 0.0F, 0.0F, 1, 4, 1);
          rightantenna1.func_78793_a(2.0F, -9.4F, -2.0F);
          this.setRotation(rightantenna1, 0.4014257F, 0.0698132F, 0.0698132F);
          this.func_78792_a(rightantenna1);
          ModelRenderer leftantenna1 = new ModelRenderer(base, 56, 27);
          leftantenna1.field_78809_i = true;
          leftantenna1.func_78789_a(-1.0F, 0.0F, 0.0F, 1, 4, 1);
          leftantenna1.func_78793_a(-2.0F, -9.4F, -2.0F);
          this.setRotation(leftantenna1, 0.4014257F, -0.0698132F, -0.0698132F);
          this.func_78792_a(leftantenna1);
          ModelRenderer rightantenna2 = new ModelRenderer(base, 60, 27);
          rightantenna2.func_78789_a(0.0F, 0.0F, 0.0F, 1, 4, 1);
          rightantenna2.func_78793_a(3.0F, -10.2F, -5.3F);
          this.setRotation(rightantenna2, 1.22173F, -0.2094395F, 0.0698132F);
          this.func_78792_a(rightantenna2);
          ModelRenderer leftantenna2 = new ModelRenderer(base, 56, 27);
          leftantenna2.field_78809_i = true;
          leftantenna2.func_78789_a(-1.0F, 0.0F, 0.0F, 1, 4, 1);
          leftantenna2.func_78793_a(-3.0F, -10.2F, -5.3F);
          this.setRotation(leftantenna2, 1.22173F, 0.2094395F, -0.0698132F);
          this.func_78792_a(leftantenna2);
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
