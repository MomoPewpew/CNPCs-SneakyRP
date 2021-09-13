package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelFeatherTail extends ModelRenderer {
     ModelRenderer feather1;
     ModelRenderer feather2;
     ModelRenderer feather3;
     ModelRenderer feather4;
     ModelRenderer feather5;

     public ModelFeatherTail(ModelBiped base) {
          super(base);
          int x = 56;
          int y = 16;
          this.feather1 = new ModelRenderer(base, x, y);
          this.feather1.func_78789_a(-1.5F, 0.0F, 0.0F, 3, 8, 0);
          this.feather1.func_78793_a(1.0F, -0.5F, 2.0F);
          this.setRotation(this.feather1, 1.482807F, 0.2602503F, 0.1487144F);
          this.func_78792_a(this.feather1);
          this.feather2 = new ModelRenderer(base, x, y);
          this.feather2.func_78789_a(-1.5F, 0.0F, 0.0F, 3, 8, 0);
          this.feather2.func_78793_a(0.0F, -0.5F, 1.0F);
          this.setRotation(this.feather2, 1.200559F, 0.3717861F, 0.1858931F);
          this.func_78792_a(this.feather2);
          this.feather3 = new ModelRenderer(base, x, y);
          this.feather3.field_78809_i = true;
          this.feather3.func_78789_a(-1.5F, -0.5F, 0.0F, 3, 8, 0);
          this.feather3.func_78793_a(-1.0F, 0.0F, 2.0F);
          this.setRotation(this.feather3, 1.256389F, -0.4089647F, -0.4833219F);
          this.func_78792_a(this.feather3);
          this.feather4 = new ModelRenderer(base, x, y);
          this.feather4.func_78789_a(-1.5F, 0.0F, 0.0F, 3, 8, 0);
          this.feather4.func_78793_a(0.0F, -0.5F, 2.0F);
          this.setRotation(this.feather4, 1.786329F, 0.0F, 0.0F);
          this.func_78792_a(this.feather4);
          this.feather5 = new ModelRenderer(base, x, y);
          this.feather5.field_78809_i = true;
          this.feather5.func_78789_a(-1.5F, 0.0F, 0.0F, 3, 8, 0);
          this.feather5.func_78793_a(-1.0F, -0.5F, 2.0F);
          this.setRotation(this.feather5, 1.570073F, -0.2602503F, -0.2230717F);
          this.func_78792_a(this.feather5);
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
