package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMailboxUS extends ModelBase {
     ModelRenderer Shape1;
     ModelRenderer Shape2;
     ModelRenderer Shape3;
     ModelRenderer Shape4;
     ModelRenderer Shape5;
     ModelRenderer Shape6;
     ModelRenderer Shape7;
     ModelRenderer Shape8;
     ModelRenderer Shape9;
     ModelRenderer Shape10;
     ModelRenderer Shape11;
     ModelRenderer Shape12;
     ModelRenderer Shape13;

     public ModelMailboxUS() {
          this.field_78090_t = 64;
          this.field_78089_u = 128;
          this.Shape1 = new ModelRenderer(this, 0, 48);
          this.Shape1.func_78789_a(0.0F, 0.0F, 0.0F, 16, 14, 16);
          this.Shape1.func_78793_a(-8.0F, 8.0F, -8.0F);
          this.Shape2 = new ModelRenderer(this, 0, 79);
          this.Shape2.func_78789_a(0.0F, 0.0F, 0.0F, 1, 2, 1);
          this.Shape2.func_78793_a(-8.0F, 22.0F, -8.0F);
          this.Shape3 = new ModelRenderer(this, 5, 79);
          this.Shape3.func_78789_a(0.0F, 0.0F, 0.0F, 1, 2, 1);
          this.Shape3.func_78793_a(-8.0F, 22.0F, 7.0F);
          this.Shape4 = new ModelRenderer(this, 10, 79);
          this.Shape4.func_78789_a(0.0F, 0.0F, 0.0F, 1, 2, 1);
          this.Shape4.func_78793_a(7.0F, 22.0F, -8.0F);
          this.Shape5 = new ModelRenderer(this, 15, 79);
          this.Shape5.func_78789_a(0.0F, 0.0F, 0.0F, 1, 2, 1);
          this.Shape5.func_78793_a(7.0F, 22.0F, 7.0F);
          this.Shape6 = new ModelRenderer(this, 0, 14);
          this.Shape6.func_78789_a(0.0F, 0.0F, 0.0F, 16, 3, 7);
          this.Shape6.func_78793_a(-8.0F, 5.0F, 0.0F);
          this.Shape7 = new ModelRenderer(this, 0, 6);
          this.Shape7.func_78789_a(0.0F, 0.0F, 0.0F, 16, 2, 6);
          this.Shape7.func_78793_a(-8.0F, 3.0F, 0.0F);
          this.Shape8 = new ModelRenderer(this, 0, 0);
          this.Shape8.func_78789_a(0.0F, 0.0F, 0.0F, 16, 1, 5);
          this.Shape8.func_78793_a(-8.0F, 2.0F, 0.0F);
          this.Shape9 = new ModelRenderer(this, 0, 37);
          this.Shape9.func_78789_a(0.0F, 0.0F, 0.0F, 1, 3, 7);
          this.Shape9.func_78793_a(-8.0F, 5.0F, -7.0F);
          this.Shape10 = new ModelRenderer(this, 16, 37);
          this.Shape10.func_78789_a(0.0F, 0.0F, 0.0F, 1, 3, 7);
          this.Shape10.func_78793_a(7.0F, 5.0F, -7.0F);
          this.Shape11 = new ModelRenderer(this, 0, 29);
          this.Shape11.func_78789_a(0.0F, 0.0F, 0.0F, 1, 2, 6);
          this.Shape11.func_78793_a(-8.0F, 3.0F, -6.0F);
          this.Shape12 = new ModelRenderer(this, 14, 29);
          this.Shape12.func_78789_a(0.0F, 0.0F, 0.0F, 1, 2, 6);
          this.Shape12.func_78793_a(7.0F, 3.0F, -6.0F);
          this.Shape13 = new ModelRenderer(this, 0, 25);
          this.Shape13.func_78789_a(0.0F, 0.0F, 0.0F, 16, 1, 3);
          this.Shape13.func_78793_a(-8.0F, 2.0F, -3.0F);
     }

     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
          this.Shape1.func_78785_a(f5);
          this.Shape2.func_78785_a(f5);
          this.Shape3.func_78785_a(f5);
          this.Shape4.func_78785_a(f5);
          this.Shape5.func_78785_a(f5);
          this.Shape6.func_78785_a(f5);
          this.Shape7.func_78785_a(f5);
          this.Shape8.func_78785_a(f5);
          this.Shape9.func_78785_a(f5);
          this.Shape10.func_78785_a(f5);
          this.Shape11.func_78785_a(f5);
          this.Shape12.func_78785_a(f5);
          this.Shape13.func_78785_a(f5);
     }
}
