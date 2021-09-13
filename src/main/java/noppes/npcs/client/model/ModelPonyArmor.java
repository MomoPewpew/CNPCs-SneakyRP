package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelPonyArmor extends ModelBase {
     private boolean rainboom;
     public ModelRenderer head;
     public ModelRenderer Body;
     public ModelRenderer BodyBack;
     public ModelRenderer rightarm;
     public ModelRenderer LeftArm;
     public ModelRenderer RightLeg;
     public ModelRenderer LeftLeg;
     public ModelRenderer rightarm2;
     public ModelRenderer LeftArm2;
     public ModelRenderer RightLeg2;
     public ModelRenderer LeftLeg2;
     public boolean isPegasus = false;
     public boolean isUnicorn = false;
     public boolean isSleeping = false;
     public boolean isFlying = false;
     public boolean isGlow = false;
     public boolean isSneak = false;
     public boolean aimedBow;
     public int heldItemRight;

     public ModelPonyArmor(float f) {
          this.init(f, 0.0F);
     }

     public void init(float strech, float f) {
          float f2 = 0.0F;
          float f3 = 0.0F;
          float f4 = 0.0F;
          this.head = new ModelRenderer(this, 0, 0);
          this.head.func_78790_a(-4.0F, -4.0F, -6.0F, 8, 8, 8, strech);
          this.head.func_78793_a(f2, f3, f4);
          float f5 = 0.0F;
          float f6 = 0.0F;
          float f7 = 0.0F;
          this.Body = new ModelRenderer(this, 16, 16);
          this.Body.func_78790_a(-4.0F, 4.0F, -2.0F, 8, 8, 4, strech);
          this.Body.func_78793_a(f5, f6 + f, f7);
          this.BodyBack = new ModelRenderer(this, 0, 0);
          this.BodyBack.func_78790_a(-4.0F, 4.0F, 6.0F, 8, 8, 8, strech);
          this.BodyBack.func_78793_a(f5, f6 + f, f7);
          this.rightarm = new ModelRenderer(this, 0, 16);
          this.rightarm.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.rightarm.func_78793_a(-3.0F, 8.0F + f, 0.0F);
          this.LeftArm = new ModelRenderer(this, 0, 16);
          this.LeftArm.field_78809_i = true;
          this.LeftArm.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.LeftArm.func_78793_a(3.0F, 8.0F + f, 0.0F);
          this.RightLeg = new ModelRenderer(this, 0, 16);
          this.RightLeg.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.RightLeg.func_78793_a(-3.0F, 0.0F + f, 0.0F);
          this.LeftLeg = new ModelRenderer(this, 0, 16);
          this.LeftLeg.field_78809_i = true;
          this.LeftLeg.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.LeftLeg.func_78793_a(3.0F, 0.0F + f, 0.0F);
          this.rightarm2 = new ModelRenderer(this, 0, 16);
          this.rightarm2.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech * 0.5F);
          this.rightarm2.func_78793_a(-3.0F, 8.0F + f, 0.0F);
          this.LeftArm2 = new ModelRenderer(this, 0, 16);
          this.LeftArm2.field_78809_i = true;
          this.LeftArm2.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech * 0.5F);
          this.LeftArm2.func_78793_a(3.0F, 8.0F + f, 0.0F);
          this.RightLeg2 = new ModelRenderer(this, 0, 16);
          this.RightLeg2.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech * 0.5F);
          this.RightLeg2.func_78793_a(-3.0F, 0.0F + f, 0.0F);
          this.LeftLeg2 = new ModelRenderer(this, 0, 16);
          this.LeftLeg2.field_78809_i = true;
          this.LeftLeg2.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech * 0.5F);
          this.LeftLeg2.func_78793_a(3.0F, 0.0F + f, 0.0F);
     }

     public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
          EntityNPCInterface npc = (EntityNPCInterface)entity;
          if (!this.field_78093_q) {
               this.field_78093_q = npc.currentAnimation == 1;
          }

          if (this.isSneak && (npc.currentAnimation == 7 || npc.currentAnimation == 2)) {
               this.isSneak = false;
          }

          this.rainboom = false;
          float f6;
          float f7;
          if (this.isSleeping) {
               f6 = 1.4F;
               f7 = 0.1F;
          } else {
               f6 = f3 / 57.29578F;
               f7 = f4 / 57.29578F;
          }

          this.head.field_78796_g = f6;
          this.head.field_78795_f = f7;
          float f8;
          float f9;
          float f10;
          float f11;
          if (this.isFlying && this.isPegasus) {
               if (f1 < 0.9999F) {
                    this.rainboom = false;
                    f8 = MathHelper.func_76126_a(0.0F - f1 * 0.5F);
                    f9 = MathHelper.func_76126_a(0.0F - f1 * 0.5F);
                    f10 = MathHelper.func_76126_a(f1 * 0.5F);
                    f11 = MathHelper.func_76126_a(f1 * 0.5F);
               } else {
                    this.rainboom = true;
                    f8 = 4.712F;
                    f9 = 4.712F;
                    f10 = 1.571F;
                    f11 = 1.571F;
               }

               this.rightarm.field_78796_g = 0.2F;
               this.LeftArm.field_78796_g = -0.2F;
               this.RightLeg.field_78796_g = -0.2F;
               this.LeftLeg.field_78796_g = 0.2F;
               this.rightarm2.field_78796_g = 0.2F;
               this.LeftArm2.field_78796_g = -0.2F;
               this.RightLeg2.field_78796_g = -0.2F;
               this.LeftLeg2.field_78796_g = 0.2F;
          } else {
               f8 = MathHelper.func_76134_b(f * 0.6662F + 3.141593F) * 0.6F * f1;
               f9 = MathHelper.func_76134_b(f * 0.6662F) * 0.6F * f1;
               f10 = MathHelper.func_76134_b(f * 0.6662F) * 0.3F * f1;
               f11 = MathHelper.func_76134_b(f * 0.6662F + 3.141593F) * 0.3F * f1;
               this.rightarm.field_78796_g = 0.0F;
               this.LeftArm.field_78796_g = 0.0F;
               this.RightLeg.field_78796_g = 0.0F;
               this.LeftLeg.field_78796_g = 0.0F;
               this.rightarm2.field_78796_g = 0.0F;
               this.LeftArm2.field_78796_g = 0.0F;
               this.RightLeg2.field_78796_g = 0.0F;
               this.LeftLeg2.field_78796_g = 0.0F;
          }

          if (this.isSleeping) {
               f8 = 4.712F;
               f9 = 4.712F;
               f10 = 1.571F;
               f11 = 1.571F;
          }

          this.rightarm.field_78795_f = f8;
          this.LeftArm.field_78795_f = f9;
          this.RightLeg.field_78795_f = f10;
          this.LeftLeg.field_78795_f = f11;
          this.rightarm.field_78808_h = 0.0F;
          this.LeftArm.field_78808_h = 0.0F;
          this.rightarm2.field_78795_f = f8;
          this.LeftArm2.field_78795_f = f9;
          this.RightLeg2.field_78795_f = f10;
          this.LeftLeg2.field_78795_f = f11;
          this.rightarm2.field_78808_h = 0.0F;
          this.LeftArm2.field_78808_h = 0.0F;
          if (this.heldItemRight != 0 && !this.rainboom && !this.isUnicorn) {
               this.rightarm.field_78795_f = this.rightarm.field_78795_f * 0.5F - 0.3141593F;
               this.rightarm2.field_78795_f = this.rightarm2.field_78795_f * 0.5F - 0.3141593F;
          }

          float f12 = 0.0F;
          if (f5 > -9990.0F && !this.isUnicorn) {
               f12 = MathHelper.func_76126_a(MathHelper.func_76129_c(f5) * 3.141593F * 2.0F) * 0.2F;
          }

          this.Body.field_78796_g = (float)((double)f12 * 0.2D);
          this.BodyBack.field_78796_g = (float)((double)f12 * 0.2D);
          float f13 = MathHelper.func_76126_a(this.Body.field_78796_g) * 5.0F;
          float f14 = MathHelper.func_76134_b(this.Body.field_78796_g) * 5.0F;
          float f15 = 4.0F;
          if (this.isSneak && !this.isFlying) {
               f15 = 0.0F;
          }

          if (this.isSleeping) {
               f15 = 2.6F;
          }

          if (this.rainboom) {
               this.rightarm.field_78798_e = f13 + 2.0F;
               this.rightarm2.field_78798_e = f13 + 2.0F;
               this.LeftArm.field_78798_e = 0.0F - f13 + 2.0F;
               this.LeftArm2.field_78798_e = 0.0F - f13 + 2.0F;
          } else {
               this.rightarm.field_78798_e = f13 + 1.0F;
               this.rightarm2.field_78798_e = f13 + 1.0F;
               this.LeftArm.field_78798_e = 0.0F - f13 + 1.0F;
               this.LeftArm2.field_78798_e = 0.0F - f13 + 1.0F;
          }

          this.rightarm.field_78800_c = 0.0F - f14 - 1.0F + f15;
          this.rightarm2.field_78800_c = 0.0F - f14 - 1.0F + f15;
          this.LeftArm.field_78800_c = f14 + 1.0F - f15;
          this.LeftArm2.field_78800_c = f14 + 1.0F - f15;
          this.RightLeg.field_78800_c = 0.0F - f14 - 1.0F + f15;
          this.RightLeg2.field_78800_c = 0.0F - f14 - 1.0F + f15;
          this.LeftLeg.field_78800_c = f14 + 1.0F - f15;
          this.LeftLeg2.field_78800_c = f14 + 1.0F - f15;
          ModelRenderer var10000 = this.rightarm;
          var10000.field_78796_g += this.Body.field_78796_g;
          var10000 = this.rightarm2;
          var10000.field_78796_g += this.Body.field_78796_g;
          var10000 = this.LeftArm;
          var10000.field_78796_g += this.Body.field_78796_g;
          var10000 = this.LeftArm2;
          var10000.field_78796_g += this.Body.field_78796_g;
          var10000 = this.LeftArm;
          var10000.field_78795_f += this.Body.field_78796_g;
          var10000 = this.LeftArm2;
          var10000.field_78795_f += this.Body.field_78796_g;
          this.rightarm.field_78797_d = 8.0F;
          this.LeftArm.field_78797_d = 8.0F;
          this.RightLeg.field_78797_d = 4.0F;
          this.LeftLeg.field_78797_d = 4.0F;
          this.rightarm2.field_78797_d = 8.0F;
          this.LeftArm2.field_78797_d = 8.0F;
          this.RightLeg2.field_78797_d = 4.0F;
          this.LeftLeg2.field_78797_d = 4.0F;
          float f20;
          float f25;
          float f28;
          float f31;
          if (f5 > -9990.0F && !this.isUnicorn) {
               f20 = 1.0F - f5;
               f20 *= f20 * f20;
               f20 = 1.0F - f20;
               f25 = MathHelper.func_76126_a(f20 * 3.141593F);
               f28 = MathHelper.func_76126_a(f5 * 3.141593F);
               f31 = f28 * -(this.head.field_78795_f - 0.7F) * 0.75F;
          }

          float f33;
          float f35;
          if (this.isSneak && !this.isFlying) {
               f20 = 0.4F;
               f25 = 7.0F;
               f28 = -4.0F;
               this.Body.field_78795_f = f20;
               this.Body.field_78797_d = f25;
               this.Body.field_78798_e = f28;
               this.BodyBack.field_78795_f = f20;
               this.BodyBack.field_78797_d = f25;
               this.BodyBack.field_78798_e = f28;
               var10000 = this.RightLeg;
               var10000.field_78795_f -= 0.0F;
               var10000 = this.LeftLeg;
               var10000.field_78795_f -= 0.0F;
               var10000 = this.rightarm;
               var10000.field_78795_f -= 0.4F;
               var10000 = this.LeftArm;
               var10000.field_78795_f -= 0.4F;
               this.RightLeg.field_78798_e = 10.0F;
               this.LeftLeg.field_78798_e = 10.0F;
               this.RightLeg.field_78797_d = 7.0F;
               this.LeftLeg.field_78797_d = 7.0F;
               var10000 = this.RightLeg2;
               var10000.field_78795_f -= 0.0F;
               var10000 = this.LeftLeg2;
               var10000.field_78795_f -= 0.0F;
               var10000 = this.rightarm2;
               var10000.field_78795_f -= 0.4F;
               var10000 = this.LeftArm2;
               var10000.field_78795_f -= 0.4F;
               this.RightLeg2.field_78798_e = 10.0F;
               this.LeftLeg2.field_78798_e = 10.0F;
               this.RightLeg2.field_78797_d = 7.0F;
               this.LeftLeg2.field_78797_d = 7.0F;
               if (this.isSleeping) {
                    f31 = 2.0F;
                    f33 = -1.0F;
                    f35 = 1.0F;
               } else {
                    f31 = 6.0F;
                    f33 = -2.0F;
                    f35 = 0.0F;
               }

               this.head.field_78797_d = f31;
               this.head.field_78798_e = f33;
               this.head.field_78800_c = f35;
          } else {
               f20 = 0.0F;
               f25 = 0.0F;
               f28 = 0.0F;
               this.Body.field_78795_f = f20;
               this.Body.field_78797_d = f25;
               this.Body.field_78798_e = f28;
               this.BodyBack.field_78795_f = f20;
               this.BodyBack.field_78797_d = f25;
               this.BodyBack.field_78798_e = f28;
               this.RightLeg.field_78798_e = 10.0F;
               this.LeftLeg.field_78798_e = 10.0F;
               this.RightLeg.field_78797_d = 8.0F;
               this.LeftLeg.field_78797_d = 8.0F;
               this.RightLeg2.field_78798_e = 10.0F;
               this.LeftLeg2.field_78798_e = 10.0F;
               this.RightLeg2.field_78797_d = 8.0F;
               this.LeftLeg2.field_78797_d = 8.0F;
               f31 = MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
               f33 = MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
               f35 = 0.0F;
               float f37 = 0.0F;
               this.head.field_78797_d = f35;
               this.head.field_78798_e = f37;
          }

          if (this.isSleeping) {
               this.rightarm.field_78798_e += 6.0F;
               this.LeftArm.field_78798_e += 6.0F;
               this.RightLeg.field_78798_e -= 8.0F;
               this.LeftLeg.field_78798_e -= 8.0F;
               this.rightarm.field_78797_d += 2.0F;
               this.LeftArm.field_78797_d += 2.0F;
               this.RightLeg.field_78797_d += 2.0F;
               this.LeftLeg.field_78797_d += 2.0F;
               this.rightarm2.field_78798_e += 6.0F;
               this.LeftArm2.field_78798_e += 6.0F;
               this.RightLeg2.field_78798_e -= 8.0F;
               this.LeftLeg2.field_78798_e -= 8.0F;
               this.rightarm2.field_78797_d += 2.0F;
               this.LeftArm2.field_78797_d += 2.0F;
               this.RightLeg2.field_78797_d += 2.0F;
               this.LeftLeg2.field_78797_d += 2.0F;
          }

          if (this.aimedBow && !this.isUnicorn) {
               f20 = 0.0F;
               f25 = 0.0F;
               this.rightarm.field_78808_h = 0.0F;
               this.rightarm.field_78796_g = -(0.1F - f20 * 0.6F) + this.head.field_78796_g;
               this.rightarm.field_78795_f = 4.712F + this.head.field_78795_f;
               var10000 = this.rightarm;
               var10000.field_78795_f -= f20 * 1.2F - f25 * 0.4F;
               var10000 = this.rightarm;
               var10000.field_78808_h += MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
               var10000 = this.rightarm;
               var10000.field_78795_f += MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
               this.rightarm2.field_78808_h = 0.0F;
               this.rightarm2.field_78796_g = -(0.1F - f20 * 0.6F) + this.head.field_78796_g;
               this.rightarm2.field_78795_f = 4.712F + this.head.field_78795_f;
               var10000 = this.rightarm2;
               var10000.field_78795_f -= f20 * 1.2F - f25 * 0.4F;
               var10000 = this.rightarm2;
               var10000.field_78808_h += MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
               var10000 = this.rightarm2;
               var10000.field_78795_f += MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
               ++this.rightarm.field_78798_e;
               ++this.rightarm2.field_78798_e;
          }

     }

     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
          this.head.func_78785_a(f5);
          this.Body.func_78785_a(f5);
          this.BodyBack.func_78785_a(f5);
          this.LeftArm.func_78785_a(f5);
          this.rightarm.func_78785_a(f5);
          this.LeftLeg.func_78785_a(f5);
          this.RightLeg.func_78785_a(f5);
          this.LeftArm2.func_78785_a(f5);
          this.rightarm2.func_78785_a(f5);
          this.LeftLeg2.func_78785_a(f5);
          this.RightLeg2.func_78785_a(f5);
     }
}
