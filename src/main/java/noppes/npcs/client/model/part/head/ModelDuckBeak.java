package noppes.npcs.client.model.part.head;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelDuckBeak extends ModelRenderer {
     ModelRenderer Top3;
     ModelRenderer Top2;
     ModelRenderer Bottom;
     ModelRenderer Left;
     ModelRenderer Right;
     ModelRenderer Middle;
     ModelRenderer Top;

     public ModelDuckBeak(ModelBiped base) {
          super(base);
          this.Top3 = new ModelRenderer(base, 14, 0);
          this.Top3.func_78789_a(0.0F, 0.0F, 0.0F, 2, 1, 3);
          this.Top3.func_78793_a(-1.0F, -2.0F, -5.0F);
          this.setRotation(this.Top3, 0.3346075F, 0.0F, 0.0F);
          this.func_78792_a(this.Top3);
          this.Top2 = new ModelRenderer(base, 0, 0);
          this.Top2.func_78789_a(0.0F, 0.0F, -0.4F, 4, 1, 3);
          this.Top2.func_78793_a(-2.0F, -3.0F, -2.0F);
          this.setRotation(this.Top2, 0.3346075F, 0.0F, 0.0F);
          this.func_78792_a(this.Top2);
          this.Bottom = new ModelRenderer(base, 24, 0);
          this.Bottom.func_78789_a(0.0F, 0.0F, 0.0F, 2, 1, 5);
          this.Bottom.func_78793_a(-1.0F, -1.0F, -5.0F);
          this.func_78792_a(this.Bottom);
          this.Left = new ModelRenderer(base, 0, 4);
          this.Left.field_78809_i = true;
          this.Left.func_78789_a(0.0F, 0.0F, 0.0F, 1, 3, 2);
          this.Left.func_78793_a(0.98F, -3.0F, -2.0F);
          this.func_78792_a(this.Left);
          this.Right = new ModelRenderer(base, 0, 4);
          this.Right.func_78789_a(0.0F, 0.0F, 0.0F, 1, 3, 2);
          this.Right.func_78793_a(-1.98F, -3.0F, -2.0F);
          this.func_78792_a(this.Right);
          this.Middle = new ModelRenderer(base, 3, 0);
          this.Middle.func_78789_a(0.0F, 0.0F, 0.0F, 2, 1, 3);
          this.Middle.func_78793_a(-1.0F, -2.0F, -5.0F);
          this.func_78792_a(this.Middle);
          this.Top = new ModelRenderer(base, 6, 4);
          this.Top.func_78789_a(0.0F, 0.0F, 0.0F, 2, 2, 1);
          this.Top.func_78793_a(-1.0F, -4.4F, -1.0F);
          this.func_78792_a(this.Top);
     }

     public void func_78785_a(float f) {
          GlStateManager.func_179094_E();
          GlStateManager.func_179109_b(0.0F, 0.0F, -1.0F * f);
          GlStateManager.func_179152_a(0.82F, 0.82F, 0.7F);
          super.func_78785_a(f);
          GlStateManager.func_179121_F();
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
