package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.client.model.ModelPlaneRenderer;

public class ModelNagaLegs extends ModelRenderer {
     private ModelRenderer nagaPart1;
     private ModelRenderer nagaPart2;
     private ModelRenderer nagaPart3;
     private ModelRenderer nagaPart4;
     private ModelRenderer nagaPart5;
     public boolean isRiding = false;
     public boolean isSneaking = false;
     public boolean isSleeping = false;
     public boolean isCrawling = false;

     public ModelNagaLegs(ModelBase base) {
          super(base);
          this.nagaPart1 = new ModelRenderer(base, 0, 0);
          ModelRenderer legPart = new ModelRenderer(base, 0, 16);
          legPart.func_78789_a(0.0F, -2.0F, -2.0F, 4, 4, 4);
          legPart.func_78793_a(-4.0F, 0.0F, 0.0F);
          this.nagaPart1.func_78792_a(legPart);
          legPart = new ModelRenderer(base, 0, 16);
          legPart.field_78809_i = true;
          legPart.func_78789_a(0.0F, -2.0F, -2.0F, 4, 4, 4);
          this.nagaPart1.func_78792_a(legPart);
          this.nagaPart2 = new ModelRenderer(base, 0, 0);
          this.nagaPart2.field_78805_m = this.nagaPart1.field_78805_m;
          this.nagaPart3 = new ModelRenderer(base, 0, 0);
          ModelPlaneRenderer plane = new ModelPlaneRenderer(base, 4, 24);
          plane.addBackPlane(0.0F, -2.0F, 0.0F, 4, 4);
          plane.func_78793_a(-4.0F, 0.0F, 0.0F);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 4, 24);
          plane.field_78809_i = true;
          plane.addBackPlane(0.0F, -2.0F, 0.0F, 4, 4);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 8, 24);
          plane.addBackPlane(0.0F, -2.0F, 6.0F, 4, 4);
          plane.func_78793_a(-4.0F, 0.0F, 0.0F);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 8, 24);
          plane.field_78809_i = true;
          plane.addBackPlane(0.0F, -2.0F, 6.0F, 4, 4);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 4, 26);
          plane.addTopPlane(0.0F, -2.0F, -6.0F, 4, 6);
          plane.func_78793_a(-4.0F, 0.0F, 0.0F);
          plane.field_78795_f = 3.1415927F;
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 4, 26);
          plane.field_78809_i = true;
          plane.addTopPlane(0.0F, -2.0F, -6.0F, 4, 6);
          plane.field_78795_f = 3.1415927F;
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 8, 26);
          plane.addTopPlane(0.0F, -2.0F, 0.0F, 4, 6);
          plane.func_78793_a(-4.0F, 0.0F, 0.0F);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 8, 26);
          plane.field_78809_i = true;
          plane.addTopPlane(0.0F, -2.0F, 0.0F, 4, 6);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 0, 26);
          plane.field_78795_f = 1.5707964F;
          plane.addSidePlane(0.0F, 0.0F, -2.0F, 6, 4);
          plane.func_78793_a(-4.0F, 0.0F, 0.0F);
          this.nagaPart3.func_78792_a(plane);
          plane = new ModelPlaneRenderer(base, 0, 26);
          plane.field_78795_f = 1.5707964F;
          plane.addSidePlane(4.0F, 0.0F, -2.0F, 6, 4);
          this.nagaPart3.func_78792_a(plane);
          this.nagaPart4 = new ModelRenderer(base, 0, 0);
          this.nagaPart4.field_78805_m = this.nagaPart3.field_78805_m;
          this.nagaPart5 = new ModelRenderer(base, 0, 0);
          legPart = new ModelRenderer(base, 56, 20);
          legPart.func_78789_a(0.0F, 0.0F, -2.0F, 2, 5, 2);
          legPart.func_78793_a(-2.0F, 0.0F, 0.0F);
          legPart.field_78795_f = 1.5707964F;
          this.nagaPart5.func_78792_a(legPart);
          legPart = new ModelRenderer(base, 56, 20);
          legPart.field_78809_i = true;
          legPart.func_78789_a(0.0F, 0.0F, -2.0F, 2, 5, 2);
          legPart.field_78795_f = 1.5707964F;
          this.nagaPart5.func_78792_a(legPart);
          this.func_78792_a(this.nagaPart1);
          this.func_78792_a(this.nagaPart2);
          this.func_78792_a(this.nagaPart3);
          this.func_78792_a(this.nagaPart4);
          this.func_78792_a(this.nagaPart5);
          this.nagaPart1.func_78793_a(0.0F, 14.0F, 0.0F);
          this.nagaPart2.func_78793_a(0.0F, 18.0F, 0.6F);
          this.nagaPart3.func_78793_a(0.0F, 22.0F, -0.3F);
          this.nagaPart4.func_78793_a(0.0F, 22.0F, 5.0F);
          this.nagaPart5.func_78793_a(0.0F, 22.0F, 10.0F);
     }

     public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          this.nagaPart1.field_78796_g = MathHelper.func_76134_b(par1 * 0.6662F) * 0.26F * par2;
          this.nagaPart2.field_78796_g = MathHelper.func_76134_b(par1 * 0.6662F) * 0.5F * par2;
          this.nagaPart3.field_78796_g = MathHelper.func_76134_b(par1 * 0.6662F) * 0.26F * par2;
          this.nagaPart4.field_78796_g = -MathHelper.func_76134_b(par1 * 0.6662F) * 0.16F * par2;
          this.nagaPart5.field_78796_g = -MathHelper.func_76134_b(par1 * 0.6662F) * 0.3F * par2;
          this.nagaPart1.func_78793_a(0.0F, 14.0F, 0.0F);
          this.nagaPart2.func_78793_a(0.0F, 18.0F, 0.6F);
          this.nagaPart3.func_78793_a(0.0F, 22.0F, -0.3F);
          this.nagaPart4.func_78793_a(0.0F, 22.0F, 5.0F);
          this.nagaPart5.func_78793_a(0.0F, 22.0F, 10.0F);
          this.nagaPart1.field_78795_f = 0.0F;
          this.nagaPart2.field_78795_f = 0.0F;
          this.nagaPart3.field_78795_f = 0.0F;
          this.nagaPart4.field_78795_f = 0.0F;
          this.nagaPart5.field_78795_f = 0.0F;
          ModelRenderer var10000;
          if (this.isSleeping || this.isCrawling) {
               this.nagaPart3.field_78795_f = -1.5707964F;
               this.nagaPart4.field_78795_f = -1.5707964F;
               this.nagaPart5.field_78795_f = -1.5707964F;
               var10000 = this.nagaPart3;
               var10000.field_78797_d -= 2.0F;
               this.nagaPart3.field_78798_e = 0.9F;
               var10000 = this.nagaPart4;
               var10000.field_78797_d += 4.0F;
               this.nagaPart4.field_78798_e = 0.9F;
               var10000 = this.nagaPart5;
               var10000.field_78797_d += 7.0F;
               this.nagaPart5.field_78798_e = 2.9F;
          }

          if (this.isRiding) {
               --this.nagaPart1.field_78797_d;
               this.nagaPart1.field_78795_f = -0.19634955F;
               this.nagaPart1.field_78798_e = -1.0F;
               var10000 = this.nagaPart2;
               var10000.field_78797_d -= 4.0F;
               this.nagaPart2.field_78798_e = -1.0F;
               var10000 = this.nagaPart3;
               var10000.field_78797_d -= 9.0F;
               --this.nagaPart3.field_78798_e;
               var10000 = this.nagaPart4;
               var10000.field_78797_d -= 13.0F;
               --this.nagaPart4.field_78798_e;
               var10000 = this.nagaPart5;
               var10000.field_78797_d -= 9.0F;
               --this.nagaPart5.field_78798_e;
               if (this.isSneaking) {
                    var10000 = this.nagaPart1;
                    var10000.field_78798_e += 5.0F;
                    var10000 = this.nagaPart3;
                    var10000.field_78798_e += 5.0F;
                    var10000 = this.nagaPart4;
                    var10000.field_78798_e += 5.0F;
                    var10000 = this.nagaPart5;
                    var10000.field_78798_e += 4.0F;
                    --this.nagaPart1.field_78797_d;
                    --this.nagaPart2.field_78797_d;
                    --this.nagaPart3.field_78797_d;
                    --this.nagaPart4.field_78797_d;
                    --this.nagaPart5.field_78797_d;
               }
          } else if (this.isSneaking) {
               --this.nagaPart1.field_78797_d;
               --this.nagaPart2.field_78797_d;
               --this.nagaPart3.field_78797_d;
               --this.nagaPart4.field_78797_d;
               --this.nagaPart5.field_78797_d;
               this.nagaPart1.field_78798_e = 5.0F;
               this.nagaPart2.field_78798_e = 3.0F;
          }

     }

     public void func_78785_a(float par7) {
          if (!this.field_78807_k && this.field_78806_j) {
               this.nagaPart1.func_78785_a(par7);
               this.nagaPart3.func_78785_a(par7);
               if (!this.isRiding) {
                    this.nagaPart2.func_78785_a(par7);
               }

               GlStateManager.func_179094_E();
               GlStateManager.func_179152_a(0.74F, 0.7F, 0.85F);
               GlStateManager.translate(this.nagaPart3.field_78796_g, 0.66F, 0.06F);
               this.nagaPart4.func_78785_a(par7);
               GlStateManager.func_179121_F();
               GlStateManager.func_179094_E();
               GlStateManager.translate(this.nagaPart3.field_78796_g + this.nagaPart4.field_78796_g, 0.0F, 0.0F);
               this.nagaPart5.func_78785_a(par7);
               GlStateManager.func_179121_F();
          }
     }
}
