package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelMermaidLegs extends ModelRenderer {
     private ModelRenderer top;
     private ModelRenderer middle;
     private ModelRenderer bottom;
     private ModelRenderer fin1;
     private ModelRenderer fin2;

     public ModelMermaidLegs(ModelBase base) {
          super(base);
          this.field_78801_a = 64.0F;
          this.field_78799_b = 32.0F;
          this.top = new ModelRenderer(base, 0, 16);
          this.top.func_78789_a(-2.0F, -2.5F, -2.0F, 8, 9, 4);
          this.top.func_78793_a(-2.0F, 14.0F, 1.0F);
          this.setRotation(this.top, 0.26F, 0.0F, 0.0F);
          this.middle = new ModelRenderer(base, 28, 0);
          this.middle.func_78789_a(0.0F, 0.0F, 0.0F, 7, 6, 4);
          this.middle.func_78793_a(-1.5F, 6.5F, -1.0F);
          this.setRotation(this.middle, 0.86F, 0.0F, 0.0F);
          this.top.func_78792_a(this.middle);
          this.bottom = new ModelRenderer(base, 24, 16);
          this.bottom.func_78789_a(0.0F, 0.0F, 0.0F, 6, 7, 3);
          this.bottom.func_78793_a(0.5F, 6.0F, 0.5F);
          this.setRotation(this.bottom, 0.15F, 0.0F, 0.0F);
          this.middle.func_78792_a(this.bottom);
          this.fin1 = new ModelRenderer(base, 0, 0);
          this.fin1.func_78789_a(0.0F, 0.0F, 0.0F, 5, 9, 1);
          this.fin1.func_78793_a(0.0F, 4.5F, 1.0F);
          this.setRotation(this.fin1, 0.05F, 0.0F, 0.5911399F);
          this.bottom.func_78792_a(this.fin1);
          this.fin2 = new ModelRenderer(base, 0, 0);
          this.fin2.field_78809_i = true;
          this.fin2.func_78789_a(-5.0F, 0.0F, 0.0F, 5, 9, 1);
          this.fin2.func_78793_a(6.0F, 4.5F, 1.0F);
          this.setRotation(this.fin2, 0.05F, 0.0F, -0.591143F);
          this.bottom.func_78792_a(this.fin2);
     }

     public void func_78785_a(float f5) {
          if (!this.field_78807_k && this.field_78806_j) {
               this.top.func_78785_a(f5);
          }
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }

     public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          float ani = MathHelper.func_76126_a(par1 * 0.6662F);
          if ((double)ani > 0.2D) {
               ani /= 3.0F;
          }

          this.top.field_78795_f = 0.26F - ani * 0.2F * par2;
          this.middle.field_78795_f = 0.86F - ani * 0.24F * par2;
          this.bottom.field_78795_f = 0.15F - ani * 0.28F * par2;
          this.fin2.field_78795_f = this.fin1.field_78795_f = 0.05F - ani * 0.35F * par2;
     }
}
