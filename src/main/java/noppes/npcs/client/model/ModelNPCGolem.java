package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelNPCGolem extends ModelBipedAlt {
     private ModelRenderer bipedLowerBody;

     public ModelNPCGolem(float scale) {
          super(scale);
          this.init(0.0F, 0.0F);
     }

     public void init(float f, float f1) {
          short short1 = 128;
          short short2 = 128;
          float f2 = -7.0F;
          this.field_78116_c = (new ModelRenderer(this)).func_78787_b(short1, short2);
          this.field_78116_c.func_78793_a(0.0F, f2, -2.0F);
          this.field_78116_c.func_78784_a(0, 0).func_78790_a(-4.0F, -12.0F, -5.5F, 8, 10, 8, f);
          this.field_78116_c.func_78784_a(24, 0).func_78790_a(-1.0F, -5.0F, -7.5F, 2, 4, 2, f);
          this.field_178720_f = (new ModelRenderer(this)).func_78787_b(short1, short2);
          this.field_178720_f.func_78793_a(0.0F, f2, -2.0F);
          this.field_178720_f.func_78784_a(0, 85).func_78790_a(-4.0F, -12.0F, -5.5F, 8, 10, 8, f + 0.5F);
          this.field_78115_e = (new ModelRenderer(this)).func_78787_b(short1, short2);
          this.field_78115_e.func_78793_a(0.0F, 0.0F + f2, 0.0F);
          this.field_78115_e.func_78784_a(0, 40).func_78790_a(-9.0F, -2.0F, -6.0F, 18, 12, 11, f + 0.2F);
          this.field_78115_e.func_78784_a(0, 21).func_78790_a(-9.0F, -2.0F, -6.0F, 18, 8, 11, f);
          this.bipedLowerBody = (new ModelRenderer(this)).func_78787_b(short1, short2);
          this.bipedLowerBody.func_78793_a(0.0F, 0.0F + f2, 0.0F);
          this.bipedLowerBody.func_78784_a(0, 70).func_78790_a(-4.5F, 10.0F, -3.0F, 9, 5, 6, f + 0.5F);
          this.bipedLowerBody.func_78784_a(30, 70).func_78790_a(-4.5F, 6.0F, -3.0F, 9, 9, 6, f + 0.4F);
          this.field_178723_h = (new ModelRenderer(this)).func_78787_b(short1, short2);
          this.field_178723_h.func_78793_a(0.0F, f2, 0.0F);
          this.field_178723_h.func_78784_a(60, 21).func_78790_a(-13.0F, -2.5F, -3.0F, 4, 30, 6, f + 0.2F);
          this.field_178723_h.func_78784_a(80, 21).func_78790_a(-13.0F, -2.5F, -3.0F, 4, 20, 6, f);
          this.field_178723_h.func_78784_a(100, 21).func_78790_a(-13.0F, -2.5F, -3.0F, 4, 20, 6, f + 1.0F);
          this.field_178724_i = (new ModelRenderer(this)).func_78787_b(short1, short2);
          this.field_178724_i.func_78793_a(0.0F, f2, 0.0F);
          this.field_178724_i.func_78784_a(60, 58).func_78790_a(9.0F, -2.5F, -3.0F, 4, 30, 6, f + 0.2F);
          this.field_178724_i.func_78784_a(80, 58).func_78790_a(9.0F, -2.5F, -3.0F, 4, 20, 6, f);
          this.field_178724_i.func_78784_a(100, 58).func_78790_a(9.0F, -2.5F, -3.0F, 4, 20, 6, f + 1.0F);
          this.field_178722_k = (new ModelRenderer(this, 0, 22)).func_78787_b(short1, short2);
          this.field_178722_k.func_78793_a(-4.0F, 18.0F + f2, 0.0F);
          this.field_178722_k.func_78784_a(37, 0).func_78790_a(-3.5F, -3.0F, -3.0F, 6, 16, 5, f);
          this.field_178721_j = (new ModelRenderer(this, 0, 22)).func_78787_b(short1, short2);
          this.field_178721_j.field_78809_i = true;
          this.field_178721_j.func_78784_a(60, 0).func_78790_a(-3.5F, -3.0F, -3.0F, 6, 16, 5, f);
          this.field_178721_j.func_78793_a(5.0F, 18.0F + f2, 0.0F);
     }

     public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
          super.func_78088_a(par1Entity, par2, par3, par4, par5, par6, par7);
          this.bipedLowerBody.func_78785_a(par7);
     }

     public void func_78087_a(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          EntityNPCInterface npc = (EntityNPCInterface)entity;
          this.field_78093_q = npc.isRiding();
          if (this.field_78117_n && (npc.currentAnimation == 7 || npc.currentAnimation == 2)) {
               this.field_78117_n = false;
          }

          this.field_78116_c.field_78796_g = par4 / 57.295776F;
          this.field_78116_c.field_78795_f = par5 / 57.295776F;
          this.field_178720_f.field_78796_g = this.field_78116_c.field_78796_g;
          this.field_178720_f.field_78795_f = this.field_78116_c.field_78795_f;
          this.field_178722_k.field_78795_f = -1.5F * this.func_78172_a(par1, 13.0F) * par2;
          this.field_178721_j.field_78795_f = 1.5F * this.func_78172_a(par1, 13.0F) * par2;
          this.field_178722_k.field_78796_g = 0.0F;
          this.field_178721_j.field_78796_g = 0.0F;
          float f6 = MathHelper.sin(this.field_78095_p * 3.1415927F);
          float f7 = MathHelper.sin((16.0F - (1.0F - this.field_78095_p) * (1.0F - this.field_78095_p)) * 3.1415927F);
          ModelRenderer var10000;
          if ((double)this.field_78095_p > 0.0D) {
               this.field_178723_h.field_78808_h = 0.0F;
               this.field_178724_i.field_78808_h = 0.0F;
               this.field_178723_h.field_78796_g = -(0.1F - f6 * 0.6F);
               this.field_178724_i.field_78796_g = 0.1F - f6 * 0.6F;
               this.field_178723_h.field_78795_f = 0.0F;
               this.field_178724_i.field_78795_f = 0.0F;
               this.field_178723_h.field_78795_f = -1.5707964F;
               this.field_178724_i.field_78795_f = -1.5707964F;
               var10000 = this.field_178723_h;
               var10000.field_78795_f -= f6 * 1.2F - f7 * 0.4F;
               var10000 = this.field_178724_i;
               var10000.field_78795_f -= f6 * 1.2F - f7 * 0.4F;
          } else if (this.field_187076_m == ArmPose.BOW_AND_ARROW) {
               float f1 = 0.0F;
               float f3 = 0.0F;
               this.field_178723_h.field_78808_h = 0.0F;
               this.field_178723_h.field_78795_f = -1.5707964F + this.field_78116_c.field_78795_f;
               var10000 = this.field_178723_h;
               var10000.field_78795_f -= f1 * 1.2F - f3 * 0.4F;
               var10000 = this.field_178723_h;
               var10000.field_78808_h += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
               var10000 = this.field_178723_h;
               var10000.field_78795_f += MathHelper.sin(par3 * 0.067F) * 0.05F;
               this.field_178724_i.field_78795_f = (-0.2F - 1.5F * this.func_78172_a(par1, 13.0F)) * par2;
               this.field_78115_e.field_78796_g = -(0.1F - f1 * 0.6F) + this.field_78116_c.field_78796_g;
               this.field_178723_h.field_78796_g = -(0.1F - f1 * 0.6F) + this.field_78116_c.field_78796_g;
               this.field_178724_i.field_78796_g = 0.1F - f1 * 0.6F + this.field_78116_c.field_78796_g;
          } else {
               this.field_178723_h.field_78795_f = (-0.2F + 1.5F * this.func_78172_a(par1, 13.0F)) * par2;
               this.field_178724_i.field_78795_f = (-0.2F - 1.5F * this.func_78172_a(par1, 13.0F)) * par2;
               this.field_78115_e.field_78796_g = 0.0F;
               this.field_178723_h.field_78796_g = 0.0F;
               this.field_178724_i.field_78796_g = 0.0F;
               this.field_178723_h.field_78808_h = 0.0F;
               this.field_178724_i.field_78808_h = 0.0F;
          }

          if (this.field_78093_q) {
               var10000 = this.field_178723_h;
               var10000.field_78795_f += -0.62831855F;
               var10000 = this.field_178724_i;
               var10000.field_78795_f += -0.62831855F;
               this.field_178722_k.field_78795_f = -1.2566371F;
               this.field_178721_j.field_78795_f = -1.2566371F;
               this.field_178722_k.field_78796_g = 0.31415927F;
               this.field_178721_j.field_78796_g = -0.31415927F;
          }

     }

     private float func_78172_a(float par1, float par2) {
          return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
     }
}
