package noppes.npcs.client.model.blocks;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMailboxWow extends ModelBase {
     ModelRenderer Shape4;
     ModelRenderer Shape1;
     ModelRenderer Shape2;
     ModelRenderer Shape3;

     public ModelMailboxWow() {
          this.field_78090_t = 128;
          this.field_78089_u = 64;
          this.Shape4 = new ModelRenderer(this, 59, 0);
          this.Shape4.func_78789_a(0.0F, 0.0F, 0.0F, 8, 6, 0);
          this.Shape4.func_78793_a(-4.0F, -4.0F, 0.0F);
          this.Shape1 = new ModelRenderer(this, 0, 39);
          this.Shape1.func_78789_a(0.0F, 0.0F, 0.0F, 8, 5, 8);
          this.Shape1.func_78793_a(-4.0F, 19.0F, -4.0F);
          this.Shape2 = new ModelRenderer(this, 0, 21);
          this.Shape2.func_78789_a(0.0F, 0.0F, 0.0F, 6, 9, 6);
          this.Shape2.func_78793_a(-3.0F, 10.0F, -3.0F);
          this.Shape3 = new ModelRenderer(this, 0, 0);
          this.Shape3.func_78789_a(0.0F, 0.0F, 0.0F, 12, 8, 12);
          this.Shape3.func_78793_a(-6.0F, 2.0F, -6.0F);
     }

     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          super.func_78088_a(entity, f, f1, f2, f3, f4, f5);
          this.Shape4.func_78785_a(f5);
          this.Shape1.func_78785_a(f5);
          this.Shape2.func_78785_a(f5);
          this.Shape3.func_78785_a(f5);
     }
}
