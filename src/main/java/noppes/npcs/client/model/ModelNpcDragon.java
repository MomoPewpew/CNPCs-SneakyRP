package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.entity.EntityNpcDragon;

public class ModelNpcDragon extends ModelBase {
     private ModelRenderer head;
     private ModelRenderer neck;
     private ModelRenderer jaw;
     private ModelRenderer body;
     private ModelRenderer rearLeg;
     private ModelRenderer frontLeg;
     private ModelRenderer rearLegTip;
     private ModelRenderer frontLegTip;
     private ModelRenderer rearFoot;
     private ModelRenderer frontFoot;
     private ModelRenderer wing;
     private ModelRenderer wingTip;
     private float field_40317_s;

     public ModelNpcDragon(float scale) {
          this.field_78090_t = 256;
          this.field_78089_u = 256;
          this.func_78085_a("body.body", 0, 0);
          this.func_78085_a("wing.skin", -56, 88);
          this.func_78085_a("wingtip.skin", -56, 144);
          this.func_78085_a("rearleg.main", 0, 0);
          this.func_78085_a("rearfoot.main", 112, 0);
          this.func_78085_a("rearlegtip.main", 196, 0);
          this.func_78085_a("head.upperhead", 112, 30);
          this.func_78085_a("wing.bone", 112, 88);
          this.func_78085_a("head.upperlip", 176, 44);
          this.func_78085_a("jaw.jaw", 176, 65);
          this.func_78085_a("frontleg.main", 112, 104);
          this.func_78085_a("wingtip.bone", 112, 136);
          this.func_78085_a("frontfoot.main", 144, 104);
          this.func_78085_a("neck.box", 192, 104);
          this.func_78085_a("frontlegtip.main", 226, 138);
          this.func_78085_a("body.scale", 220, 53);
          this.func_78085_a("head.scale", 0, 0);
          this.func_78085_a("neck.scale", 48, 0);
          this.func_78085_a("head.nostril", 112, 0);
          float f = -16.0F;
          this.head = new ModelRenderer(this, "head");
          this.head.func_78786_a("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16);
          this.head.func_78786_a("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16);
          this.head.field_78809_i = true;
          this.head.func_78786_a("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6);
          this.head.func_78786_a("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4);
          this.head.field_78809_i = false;
          this.head.func_78786_a("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6);
          this.head.func_78786_a("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4);
          this.jaw = new ModelRenderer(this, "jaw");
          this.jaw.func_78793_a(0.0F, 4.0F, -8.0F);
          this.jaw.func_78786_a("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
          this.head.func_78792_a(this.jaw);
          this.neck = new ModelRenderer(this, "neck");
          this.neck.func_78786_a("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
          this.neck.func_78786_a("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
          this.body = new ModelRenderer(this, "body");
          this.body.func_78793_a(0.0F, 4.0F, 8.0F);
          this.body.func_78786_a("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
          this.body.func_78786_a("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
          this.body.func_78786_a("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
          this.body.func_78786_a("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
          this.wing = new ModelRenderer(this, "wing");
          this.wing.func_78793_a(-12.0F, 5.0F, 2.0F);
          this.wing.func_78786_a("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
          this.wing.func_78786_a("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
          this.wingTip = new ModelRenderer(this, "wingtip");
          this.wingTip.func_78793_a(-56.0F, 0.0F, 0.0F);
          this.wingTip.func_78786_a("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
          this.wingTip.func_78786_a("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
          this.wing.func_78792_a(this.wingTip);
          this.frontLeg = new ModelRenderer(this, "frontleg");
          this.frontLeg.func_78793_a(-12.0F, 20.0F, 2.0F);
          this.frontLeg.func_78786_a("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
          this.frontLegTip = new ModelRenderer(this, "frontlegtip");
          this.frontLegTip.func_78793_a(0.0F, 20.0F, -1.0F);
          this.frontLegTip.func_78786_a("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
          this.frontLeg.func_78792_a(this.frontLegTip);
          this.frontFoot = new ModelRenderer(this, "frontfoot");
          this.frontFoot.func_78793_a(0.0F, 23.0F, 0.0F);
          this.frontFoot.func_78786_a("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
          this.frontLegTip.func_78792_a(this.frontFoot);
          this.rearLeg = new ModelRenderer(this, "rearleg");
          this.rearLeg.func_78793_a(-16.0F, 16.0F, 42.0F);
          this.rearLeg.func_78786_a("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
          this.rearLegTip = new ModelRenderer(this, "rearlegtip");
          this.rearLegTip.func_78793_a(0.0F, 32.0F, -4.0F);
          this.rearLegTip.func_78786_a("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
          this.rearLeg.func_78792_a(this.rearLegTip);
          this.rearFoot = new ModelRenderer(this, "rearfoot");
          this.rearFoot.func_78793_a(0.0F, 31.0F, 4.0F);
          this.rearFoot.func_78786_a("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
          this.rearLegTip.func_78792_a(this.rearFoot);
     }

     public void func_78086_a(EntityLivingBase entityliving, float f, float f1, float f2) {
          this.field_40317_s = f2;
     }

     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          EntityNpcDragon entitydragon = (EntityNpcDragon)entity;
          GlStateManager.func_179094_E();
          float f6 = entitydragon.field_40173_aw + (entitydragon.field_40172_ax - entitydragon.field_40173_aw) * this.field_40317_s;
          this.jaw.field_78795_f = (float)(Math.sin((double)(f6 * 3.1415927F * 2.0F)) + 1.0D) * 0.2F;
          float f7 = (float)(Math.sin((double)(f6 * 3.1415927F * 2.0F - 1.0F)) + 1.0D);
          f7 = (f7 * f7 * 1.0F + f7 * 2.0F) * 0.05F;
          GlStateManager.translate(0.0F, f7 - 2.0F, -3.0F);
          GlStateManager.func_179114_b(f7 * 2.0F, 1.0F, 0.0F, 0.0F);
          float f8 = -30.0F;
          float f9 = 22.0F;
          float f10 = 0.0F;
          float f11 = 1.5F;
          double[] ad = entitydragon.func_40160_a(6, this.field_40317_s);
          float f12 = this.func_40307_a(entitydragon.func_40160_a(5, this.field_40317_s)[0] - entitydragon.func_40160_a(10, this.field_40317_s)[0]);
          float f13 = this.func_40307_a(entitydragon.func_40160_a(5, this.field_40317_s)[0] + (double)(f12 / 2.0F));
          f8 += 2.0F;
          float f14 = 0.0F;
          float f15 = f6 * 3.141593F * 2.0F;
          f8 = 20.0F;
          f9 = -12.0F;

          for(int i = 0; i < 5; ++i) {
               double[] ad3 = entitydragon.func_40160_a(5 - i, this.field_40317_s);
               f14 = (float)Math.cos((double)((float)i * 0.45F + f15)) * 0.15F;
               this.neck.field_78796_g = this.func_40307_a(ad3[0] - ad[0]) * 3.1415927F / 180.0F * f11;
               this.neck.field_78795_f = f14 + (float)(ad3[1] - ad[1]) * 3.1415927F / 180.0F * f11 * 5.0F;
               this.neck.field_78808_h = -this.func_40307_a(ad3[0] - (double)f13) * 3.1415927F / 180.0F * f11;
               this.neck.field_78797_d = f8;
               this.neck.field_78798_e = f9;
               this.neck.field_78800_c = f10;
               f8 = (float)((double)f8 + Math.sin((double)this.neck.field_78795_f) * 10.0D);
               f9 = (float)((double)f9 - Math.cos((double)this.neck.field_78796_g) * Math.cos((double)this.neck.field_78795_f) * 10.0D);
               f10 = (float)((double)f10 - Math.sin((double)this.neck.field_78796_g) * Math.cos((double)this.neck.field_78795_f) * 10.0D);
               this.neck.func_78785_a(f5);
          }

          this.head.field_78797_d = f8;
          this.head.field_78798_e = f9;
          this.head.field_78800_c = f10;
          double[] ad1 = entitydragon.func_40160_a(0, this.field_40317_s);
          this.head.field_78796_g = this.func_40307_a(ad1[0] - ad[0]) * 3.1415927F / 180.0F * 1.0F;
          this.head.field_78808_h = -this.func_40307_a(ad1[0] - (double)f13) * 3.1415927F / 180.0F * 1.0F;
          this.head.func_78785_a(f5);
          GlStateManager.func_179094_E();
          GlStateManager.translate(0.0F, 1.0F, 0.0F);
          if (entitydragon.field_70122_E) {
               GlStateManager.func_179114_b(-f12 * f11 * 0.3F, 0.0F, 0.0F, 1.0F);
          } else {
               GlStateManager.func_179114_b(-f12 * f11 * 1.0F, 0.0F, 0.0F, 1.0F);
          }

          GlStateManager.translate(0.0F, -1.18F, 0.0F);
          this.body.field_78808_h = 0.0F;
          this.body.func_78785_a(f5);
          int k;
          if (entitydragon.field_70122_E) {
               for(k = 0; k < 2; ++k) {
                    GlStateManager.func_179089_o();
                    this.wing.field_78795_f = 0.25F;
                    this.wing.field_78796_g = 0.95F;
                    this.wing.field_78808_h = -0.5F;
                    this.wingTip.field_78808_h = -0.4F;
                    this.frontLeg.field_78795_f = MathHelper.func_76134_b((float)((double)(f * 0.6662F) + (k == 0 ? 0.0D : 3.141592653589793D))) * 0.6F * f1 + 0.45F + f7 * 0.5F;
                    this.frontLegTip.field_78795_f = -1.3F - f7 * 1.2F;
                    this.frontFoot.field_78795_f = 0.85F + f7 * 0.5F;
                    this.frontLeg.func_78785_a(f5);
                    this.rearLeg.field_78795_f = MathHelper.func_76134_b((float)((double)(f * 0.6662F) + (k == 0 ? 3.141592653589793D : 0.0D))) * 0.6F * f1 + 0.75F + f7 * 0.5F;
                    this.rearLegTip.field_78795_f = -1.6F - f7 * 0.8F;
                    this.rearLegTip.field_78797_d = 20.0F;
                    this.rearLegTip.field_78798_e = 2.0F;
                    this.rearFoot.field_78795_f = 0.85F + f7 * 0.2F;
                    this.rearLeg.func_78785_a(f5);
                    this.wing.func_78785_a(f5);
                    GlStateManager.func_179152_a(-1.0F, 1.0F, 1.0F);
                    if (k == 0) {
                         GlStateManager.func_187407_a(CullFace.FRONT);
                    }
               }
          } else {
               for(k = 0; k < 2; ++k) {
                    GlStateManager.func_179089_o();
                    float f16 = f6 * 3.1415927F * 2.0F;
                    this.wing.field_78795_f = 0.125F - (float)Math.cos((double)f16) * 0.2F;
                    this.wing.field_78796_g = 0.25F;
                    this.wing.field_78808_h = (float)(Math.sin((double)f16) + 0.125D) * 0.8F;
                    this.wingTip.field_78808_h = -((float)(Math.sin((double)(f16 + 2.0F)) + 0.5D)) * 0.75F;
                    this.rearLegTip.field_78797_d = 32.0F;
                    this.rearLegTip.field_78798_e = -2.0F;
                    this.rearLeg.field_78795_f = 1.0F + f7 * 0.1F;
                    this.rearLegTip.field_78795_f = 0.5F + f7 * 0.1F;
                    this.rearFoot.field_78795_f = 0.75F + f7 * 0.1F;
                    this.frontLeg.field_78795_f = 1.3F + f7 * 0.1F;
                    this.frontLegTip.field_78795_f = -0.5F - f7 * 0.1F;
                    this.frontFoot.field_78795_f = 0.75F + f7 * 0.1F;
                    this.wing.func_78785_a(f5);
                    this.frontLeg.func_78785_a(f5);
                    this.rearLeg.func_78785_a(f5);
                    GlStateManager.func_179152_a(-1.0F, 1.0F, 1.0F);
                    if (k == 0) {
                         GlStateManager.func_187407_a(CullFace.FRONT);
                    }
               }
          }

          GlStateManager.func_179121_F();
          GlStateManager.func_187407_a(CullFace.BACK);
          GlStateManager.func_179129_p();
          f14 = -((float)Math.sin((double)(f6 * 3.141593F * 2.0F))) * 0.0F;
          f15 = f6 * 3.1415927F * 2.0F;
          f8 = 10.0F;
          f9 = 60.0F;
          f10 = 0.0F;
          ad = entitydragon.func_40160_a(11, this.field_40317_s);

          for(k = 0; k < 12; ++k) {
               double[] ad2 = entitydragon.func_40160_a(12 + k, this.field_40317_s);
               f14 = (float)((double)f14 + Math.sin((double)((float)k * 0.45F + f15)) * 0.05000000074505806D);
               this.neck.field_78796_g = (this.func_40307_a(ad2[0] - ad[0]) * f11 + 180.0F) * 3.1415927F / 180.0F;
               this.neck.field_78795_f = f14 + (float)(ad2[1] - ad[1]) * 3.1415927F / 180.0F * f11 * 5.0F;
               this.neck.field_78808_h = this.func_40307_a(ad2[0] - (double)f13) * 3.1415927F / 180.0F * f11;
               this.neck.field_78797_d = f8;
               this.neck.field_78798_e = f9;
               this.neck.field_78800_c = f10;
               f8 = (float)((double)f8 + Math.sin((double)this.neck.field_78795_f) * 10.0D);
               f9 = (float)((double)f9 - Math.cos((double)this.neck.field_78796_g) * Math.cos((double)this.neck.field_78795_f) * 10.0D);
               f10 = (float)((double)f10 - Math.sin((double)this.neck.field_78796_g) * Math.cos((double)this.neck.field_78795_f) * 10.0D);
               this.neck.func_78785_a(f5);
          }

          GlStateManager.func_179121_F();
     }

     private float func_40307_a(double d) {
          while(d >= 180.0D) {
               d -= 360.0D;
          }

          while(d < -180.0D) {
               d += 360.0D;
          }

          return (float)d;
     }
}
