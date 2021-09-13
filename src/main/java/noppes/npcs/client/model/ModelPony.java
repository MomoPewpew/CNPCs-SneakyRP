package noppes.npcs.client.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityNpcPony;

public class ModelPony extends ModelBase {
     private boolean rainboom;
     private float WingRotateAngleX;
     private float WingRotateAngleY;
     private float WingRotateAngleZ;
     private float TailRotateAngleY;
     public ModelRenderer Head;
     public ModelRenderer[] Headpiece;
     public ModelRenderer Helmet;
     public ModelRenderer Body;
     public ModelPlaneRenderer[] Bodypiece;
     public ModelRenderer RightArm;
     public ModelRenderer LeftArm;
     public ModelRenderer RightLeg;
     public ModelRenderer LeftLeg;
     public ModelRenderer unicornarm;
     public ModelPlaneRenderer[] Tail;
     public ModelRenderer[] LeftWing;
     public ModelRenderer[] RightWing;
     public ModelRenderer[] LeftWingExt;
     public ModelRenderer[] RightWingExt;
     public boolean isPegasus;
     public boolean isUnicorn;
     public boolean isFlying;
     public boolean isGlow;
     public boolean isSleeping;
     public boolean isSneak;
     public boolean aimedBow;
     public int heldItemRight;

     public ModelPony(float f) {
          this.init(f, 0.0F);
     }

     public void init(float strech, float f) {
          float f2 = 0.0F;
          float f3 = 0.0F;
          float f4 = 0.0F;
          this.Head = new ModelRenderer(this, 0, 0);
          this.Head.func_78790_a(-4.0F, -4.0F, -6.0F, 8, 8, 8, strech);
          this.Head.func_78793_a(f2, f3 + f, f4);
          this.Headpiece = new ModelRenderer[3];
          this.Headpiece[0] = new ModelRenderer(this, 12, 16);
          this.Headpiece[0].func_78790_a(-4.0F, -6.0F, -1.0F, 2, 2, 2, strech);
          this.Headpiece[0].func_78793_a(f2, f3 + f, f4);
          this.Headpiece[1] = new ModelRenderer(this, 12, 16);
          this.Headpiece[1].func_78790_a(2.0F, -6.0F, -1.0F, 2, 2, 2, strech);
          this.Headpiece[1].func_78793_a(f2, f3 + f, f4);
          this.Headpiece[2] = new ModelRenderer(this, 56, 0);
          this.Headpiece[2].func_78790_a(-0.5F, -10.0F, -4.0F, 1, 4, 1, strech);
          this.Headpiece[2].func_78793_a(f2, f3 + f, f4);
          this.Helmet = new ModelRenderer(this, 32, 0);
          this.Helmet.func_78790_a(-4.0F, -4.0F, -6.0F, 8, 8, 8, strech + 0.5F);
          this.Helmet.func_78793_a(f2, f3, f4);
          float f5 = 0.0F;
          float f6 = 0.0F;
          float f7 = 0.0F;
          this.Body = new ModelRenderer(this, 16, 16);
          this.Body.func_78790_a(-4.0F, 4.0F, -2.0F, 8, 8, 4, strech);
          this.Body.func_78793_a(f5, f6 + f, f7);
          this.Bodypiece = new ModelPlaneRenderer[13];
          this.Bodypiece[0] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[0].addSidePlane(-4.0F, 4.0F, 2.0F, 8, 8, strech);
          this.Bodypiece[0].func_78793_a(f5, f6 + f, f7);
          this.Bodypiece[1] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[1].addSidePlane(4.0F, 4.0F, 2.0F, 8, 8, strech);
          this.Bodypiece[1].func_78793_a(f5, f6 + f, f7);
          this.Bodypiece[2] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[2].addTopPlane(-4.0F, 4.0F, 2.0F, 8, 8, strech);
          this.Bodypiece[2].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[3] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[3].addTopPlane(-4.0F, 12.0F, 2.0F, 8, 8, strech);
          this.Bodypiece[3].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[4] = new ModelPlaneRenderer(this, 0, 20);
          this.Bodypiece[4].addSidePlane(-4.0F, 4.0F, 10.0F, 8, 4, strech);
          this.Bodypiece[4].func_78793_a(f5, f6 + f, f7);
          this.Bodypiece[5] = new ModelPlaneRenderer(this, 0, 20);
          this.Bodypiece[5].addSidePlane(4.0F, 4.0F, 10.0F, 8, 4, strech);
          this.Bodypiece[5].func_78793_a(f5, f6 + f, f7);
          this.Bodypiece[6] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[6].addTopPlane(-4.0F, 4.0F, 10.0F, 8, 4, strech);
          this.Bodypiece[6].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[7] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[7].addTopPlane(-4.0F, 12.0F, 10.0F, 8, 4, strech);
          this.Bodypiece[7].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[8] = new ModelPlaneRenderer(this, 24, 0);
          this.Bodypiece[8].addBackPlane(-4.0F, 4.0F, 14.0F, 8, 8, strech);
          this.Bodypiece[8].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[9] = new ModelPlaneRenderer(this, 32, 0);
          this.Bodypiece[9].addTopPlane(-1.0F, 10.0F, 8.0F, 2, 6, strech);
          this.Bodypiece[9].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[10] = new ModelPlaneRenderer(this, 32, 0);
          this.Bodypiece[10].addTopPlane(-1.0F, 12.0F, 8.0F, 2, 6, strech);
          this.Bodypiece[10].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[11] = new ModelPlaneRenderer(this, 32, 0);
          this.Bodypiece[11].field_78809_i = true;
          this.Bodypiece[11].addSidePlane(-1.0F, 10.0F, 8.0F, 2, 6, strech);
          this.Bodypiece[11].func_78793_a(f2, f3 + f, f4);
          this.Bodypiece[12] = new ModelPlaneRenderer(this, 32, 0);
          this.Bodypiece[12].addSidePlane(1.0F, 10.0F, 8.0F, 2, 6, strech);
          this.Bodypiece[12].func_78793_a(f2, f3 + f, f4);
          this.RightArm = new ModelRenderer(this, 40, 16);
          this.RightArm.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.RightArm.func_78793_a(-3.0F, 8.0F + f, 0.0F);
          this.LeftArm = new ModelRenderer(this, 40, 16);
          this.LeftArm.field_78809_i = true;
          this.LeftArm.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.LeftArm.func_78793_a(3.0F, 8.0F + f, 0.0F);
          this.RightLeg = new ModelRenderer(this, 40, 16);
          this.RightLeg.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.RightLeg.func_78793_a(-3.0F, 0.0F + f, 0.0F);
          this.LeftLeg = new ModelRenderer(this, 40, 16);
          this.LeftLeg.field_78809_i = true;
          this.LeftLeg.func_78790_a(-2.0F, 4.0F, -2.0F, 4, 12, 4, strech);
          this.LeftLeg.func_78793_a(3.0F, 0.0F + f, 0.0F);
          this.unicornarm = new ModelRenderer(this, 40, 16);
          this.unicornarm.func_78790_a(-3.0F, -2.0F, -2.0F, 4, 12, 4, strech);
          this.unicornarm.func_78793_a(-5.0F, 2.0F + f, 0.0F);
          float f8 = 0.0F;
          float f9 = 8.0F;
          float f10 = -14.0F;
          float f11 = 0.0F - f8;
          float f12 = 10.0F - f9;
          float f13 = 0.0F;
          this.Tail = new ModelPlaneRenderer[10];
          this.Tail[0] = new ModelPlaneRenderer(this, 32, 0);
          this.Tail[0].addTopPlane(-2.0F + f8, -7.0F + f9, 16.0F + f10, 4, 4, strech);
          this.Tail[0].func_78793_a(f11, f12 + f, f13);
          this.Tail[1] = new ModelPlaneRenderer(this, 32, 0);
          this.Tail[1].addTopPlane(-2.0F + f8, 9.0F + f9, 16.0F + f10, 4, 4, strech);
          this.Tail[1].func_78793_a(f11, f12 + f, f13);
          this.Tail[2] = new ModelPlaneRenderer(this, 32, 0);
          this.Tail[2].addBackPlane(-2.0F + f8, -7.0F + f9, 16.0F + f10, 4, 8, strech);
          this.Tail[2].func_78793_a(f11, f12 + f, f13);
          this.Tail[3] = new ModelPlaneRenderer(this, 32, 0);
          this.Tail[3].addBackPlane(-2.0F + f8, -7.0F + f9, 20.0F + f10, 4, 8, strech);
          this.Tail[3].func_78793_a(f11, f12 + f, f13);
          this.Tail[4] = new ModelPlaneRenderer(this, 32, 0);
          this.Tail[4].addBackPlane(-2.0F + f8, 1.0F + f9, 16.0F + f10, 4, 8, strech);
          this.Tail[4].func_78793_a(f11, f12 + f, f13);
          this.Tail[5] = new ModelPlaneRenderer(this, 32, 0);
          this.Tail[5].addBackPlane(-2.0F + f8, 1.0F + f9, 20.0F + f10, 4, 8, strech);
          this.Tail[5].func_78793_a(f11, f12 + f, f13);
          this.Tail[6] = new ModelPlaneRenderer(this, 36, 0);
          this.Tail[6].field_78809_i = true;
          this.Tail[6].addSidePlane(2.0F + f8, -7.0F + f9, 16.0F + f10, 8, 4, strech);
          this.Tail[6].func_78793_a(f11, f12 + f, f13);
          this.Tail[7] = new ModelPlaneRenderer(this, 36, 0);
          this.Tail[7].addSidePlane(-2.0F + f8, -7.0F + f9, 16.0F + f10, 8, 4, strech);
          this.Tail[7].func_78793_a(f11, f12 + f, f13);
          this.Tail[8] = new ModelPlaneRenderer(this, 36, 0);
          this.Tail[8].field_78809_i = true;
          this.Tail[8].addSidePlane(2.0F + f8, 1.0F + f9, 16.0F + f10, 8, 4, strech);
          this.Tail[8].func_78793_a(f11, f12 + f, f13);
          this.Tail[9] = new ModelPlaneRenderer(this, 36, 0);
          this.Tail[9].addSidePlane(-2.0F + f8, 1.0F + f9, 16.0F + f10, 8, 4, strech);
          this.Tail[9].func_78793_a(f11, f12 + f, f13);
          this.TailRotateAngleY = this.Tail[0].field_78796_g;
          this.TailRotateAngleY = this.Tail[0].field_78796_g;
          float f14 = 0.0F;
          float f15 = 0.0F;
          float f16 = 0.0F;
          this.LeftWing = new ModelRenderer[3];
          this.LeftWing[0] = new ModelRenderer(this, 56, 16);
          this.LeftWing[0].field_78809_i = true;
          this.LeftWing[0].func_78790_a(4.0F, 5.0F, 2.0F, 2, 6, 2, strech);
          this.LeftWing[0].func_78793_a(f14, f15 + f, f16);
          this.LeftWing[1] = new ModelRenderer(this, 56, 16);
          this.LeftWing[1].field_78809_i = true;
          this.LeftWing[1].func_78790_a(4.0F, 5.0F, 4.0F, 2, 8, 2, strech);
          this.LeftWing[1].func_78793_a(f14, f15 + f, f16);
          this.LeftWing[2] = new ModelRenderer(this, 56, 16);
          this.LeftWing[2].field_78809_i = true;
          this.LeftWing[2].func_78790_a(4.0F, 5.0F, 6.0F, 2, 6, 2, strech);
          this.LeftWing[2].func_78793_a(f14, f15 + f, f16);
          this.RightWing = new ModelRenderer[3];
          this.RightWing[0] = new ModelRenderer(this, 56, 16);
          this.RightWing[0].func_78790_a(-6.0F, 5.0F, 2.0F, 2, 6, 2, strech);
          this.RightWing[0].func_78793_a(f14, f15 + f, f16);
          this.RightWing[1] = new ModelRenderer(this, 56, 16);
          this.RightWing[1].func_78790_a(-6.0F, 5.0F, 4.0F, 2, 8, 2, strech);
          this.RightWing[1].func_78793_a(f14, f15 + f, f16);
          this.RightWing[2] = new ModelRenderer(this, 56, 16);
          this.RightWing[2].func_78790_a(-6.0F, 5.0F, 6.0F, 2, 6, 2, strech);
          this.RightWing[2].func_78793_a(f14, f15 + f, f16);
          float f17 = f2 + 4.5F;
          float f18 = f3 + 5.0F;
          float f19 = f4 + 6.0F;
          this.LeftWingExt = new ModelRenderer[7];
          this.LeftWingExt[0] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[0].field_78809_i = true;
          this.LeftWingExt[0].func_78790_a(0.0F, 0.0F, 0.0F, 1, 8, 2, strech + 0.1F);
          this.LeftWingExt[0].func_78793_a(f17, f18 + f, f19);
          this.LeftWingExt[1] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[1].field_78809_i = true;
          this.LeftWingExt[1].func_78790_a(0.0F, 8.0F, 0.0F, 1, 6, 2, strech + 0.1F);
          this.LeftWingExt[1].func_78793_a(f17, f18 + f, f19);
          this.LeftWingExt[2] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[2].field_78809_i = true;
          this.LeftWingExt[2].func_78790_a(0.0F, -1.2F, -0.2F, 1, 8, 2, strech - 0.2F);
          this.LeftWingExt[2].func_78793_a(f17, f18 + f, f19);
          this.LeftWingExt[3] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[3].field_78809_i = true;
          this.LeftWingExt[3].func_78790_a(0.0F, 1.8F, 1.3F, 1, 8, 2, strech - 0.1F);
          this.LeftWingExt[3].func_78793_a(f17, f18 + f, f19);
          this.LeftWingExt[4] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[4].field_78809_i = true;
          this.LeftWingExt[4].func_78790_a(0.0F, 5.0F, 2.0F, 1, 8, 2, strech);
          this.LeftWingExt[4].func_78793_a(f17, f18 + f, f19);
          this.LeftWingExt[5] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[5].field_78809_i = true;
          this.LeftWingExt[5].func_78790_a(0.0F, 0.0F, -0.2F, 1, 6, 2, strech + 0.3F);
          this.LeftWingExt[5].func_78793_a(f17, f18 + f, f19);
          this.LeftWingExt[6] = new ModelRenderer(this, 56, 19);
          this.LeftWingExt[6].field_78809_i = true;
          this.LeftWingExt[6].func_78790_a(0.0F, 0.0F, 0.2F, 1, 3, 2, strech + 0.2F);
          this.LeftWingExt[6].func_78793_a(f17, f18 + f, f19);
          float f20 = f2 - 4.5F;
          float f21 = f3 + 5.0F;
          float f22 = f4 + 6.0F;
          this.RightWingExt = new ModelRenderer[7];
          this.RightWingExt[0] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[0].field_78809_i = true;
          this.RightWingExt[0].func_78790_a(0.0F, 0.0F, 0.0F, 1, 8, 2, strech + 0.1F);
          this.RightWingExt[0].func_78793_a(f20, f21 + f, f22);
          this.RightWingExt[1] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[1].field_78809_i = true;
          this.RightWingExt[1].func_78790_a(0.0F, 8.0F, 0.0F, 1, 6, 2, strech + 0.1F);
          this.RightWingExt[1].func_78793_a(f20, f21 + f, f22);
          this.RightWingExt[2] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[2].field_78809_i = true;
          this.RightWingExt[2].func_78790_a(0.0F, -1.2F, -0.2F, 1, 8, 2, strech - 0.2F);
          this.RightWingExt[2].func_78793_a(f20, f21 + f, f22);
          this.RightWingExt[3] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[3].field_78809_i = true;
          this.RightWingExt[3].func_78790_a(0.0F, 1.8F, 1.3F, 1, 8, 2, strech - 0.1F);
          this.RightWingExt[3].func_78793_a(f20, f21 + f, f22);
          this.RightWingExt[4] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[4].field_78809_i = true;
          this.RightWingExt[4].func_78790_a(0.0F, 5.0F, 2.0F, 1, 8, 2, strech);
          this.RightWingExt[4].func_78793_a(f20, f21 + f, f22);
          this.RightWingExt[5] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[5].field_78809_i = true;
          this.RightWingExt[5].func_78790_a(0.0F, 0.0F, -0.2F, 1, 6, 2, strech + 0.3F);
          this.RightWingExt[5].func_78793_a(f20, f21 + f, f22);
          this.RightWingExt[6] = new ModelRenderer(this, 56, 19);
          this.RightWingExt[6].field_78809_i = true;
          this.RightWingExt[6].func_78790_a(0.0F, 0.0F, 0.2F, 1, 3, 2, strech + 0.2F);
          this.RightWingExt[6].func_78793_a(f20, f21 + f, f22);
          this.WingRotateAngleX = this.LeftWingExt[0].field_78795_f;
          this.WingRotateAngleY = this.LeftWingExt[0].field_78796_g;
          this.WingRotateAngleZ = this.LeftWingExt[0].field_78808_h;
     }

     public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
          EntityNPCInterface npc = (EntityNPCInterface)entity;
          this.field_78093_q = npc.func_184218_aH();
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

          this.Head.field_78796_g = f6;
          this.Head.field_78795_f = f7;
          this.Headpiece[0].field_78796_g = f6;
          this.Headpiece[0].field_78795_f = f7;
          this.Headpiece[1].field_78796_g = f6;
          this.Headpiece[1].field_78795_f = f7;
          this.Headpiece[2].field_78796_g = f6;
          this.Headpiece[2].field_78795_f = f7;
          this.Helmet.field_78796_g = f6;
          this.Helmet.field_78795_f = f7;
          this.Headpiece[2].field_78795_f = f7 + 0.5F;
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

               this.RightArm.field_78796_g = 0.2F;
               this.LeftArm.field_78796_g = -0.2F;
               this.RightLeg.field_78796_g = -0.2F;
               this.LeftLeg.field_78796_g = 0.2F;
          } else {
               f8 = MathHelper.func_76134_b(f * 0.6662F + 3.141593F) * 0.6F * f1;
               f9 = MathHelper.func_76134_b(f * 0.6662F) * 0.6F * f1;
               f10 = MathHelper.func_76134_b(f * 0.6662F) * 0.3F * f1;
               f11 = MathHelper.func_76134_b(f * 0.6662F + 3.141593F) * 0.3F * f1;
               this.RightArm.field_78796_g = 0.0F;
               this.unicornarm.field_78796_g = 0.0F;
               this.LeftArm.field_78796_g = 0.0F;
               this.RightLeg.field_78796_g = 0.0F;
               this.LeftLeg.field_78796_g = 0.0F;
          }

          if (this.isSleeping) {
               f8 = 4.712F;
               f9 = 4.712F;
               f10 = 1.571F;
               f11 = 1.571F;
          }

          this.RightArm.field_78795_f = f8;
          this.unicornarm.field_78795_f = 0.0F;
          this.LeftArm.field_78795_f = f9;
          this.RightLeg.field_78795_f = f10;
          this.LeftLeg.field_78795_f = f11;
          this.RightArm.field_78808_h = 0.0F;
          this.unicornarm.field_78808_h = 0.0F;
          this.LeftArm.field_78808_h = 0.0F;

          for(int i = 0; i < this.Tail.length; ++i) {
               if (this.rainboom) {
                    this.Tail[i].field_78808_h = 0.0F;
               } else {
                    this.Tail[i].field_78808_h = MathHelper.func_76134_b(f * 0.8F) * 0.2F * f1;
               }
          }

          if (this.heldItemRight != 0 && !this.rainboom && !this.isUnicorn) {
               this.RightArm.field_78795_f = this.RightArm.field_78795_f * 0.5F - 0.3141593F;
          }

          float f12 = 0.0F;
          if (f5 > -9990.0F && !this.isUnicorn) {
               f12 = MathHelper.func_76126_a(MathHelper.func_76129_c(f5) * 3.141593F * 2.0F) * 0.2F;
          }

          this.Body.field_78796_g = (float)((double)f12 * 0.2D);

          int i1;
          for(i1 = 0; i1 < this.Bodypiece.length; ++i1) {
               this.Bodypiece[i1].field_78796_g = (float)((double)f12 * 0.2D);
          }

          for(i1 = 0; i1 < this.LeftWing.length; ++i1) {
               this.LeftWing[i1].field_78796_g = (float)((double)f12 * 0.2D);
          }

          for(i1 = 0; i1 < this.RightWing.length; ++i1) {
               this.RightWing[i1].field_78796_g = (float)((double)f12 * 0.2D);
          }

          for(i1 = 0; i1 < this.Tail.length; ++i1) {
               this.Tail[i1].field_78796_g = f12;
          }

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
               this.RightArm.field_78798_e = f13 + 2.0F;
               this.LeftArm.field_78798_e = 0.0F - f13 + 2.0F;
          } else {
               this.RightArm.field_78798_e = f13 + 1.0F;
               this.LeftArm.field_78798_e = 0.0F - f13 + 1.0F;
          }

          this.RightArm.field_78800_c = 0.0F - f14 - 1.0F + f15;
          this.LeftArm.field_78800_c = f14 + 1.0F - f15;
          this.RightLeg.field_78800_c = 0.0F - f14 - 1.0F + f15;
          this.LeftLeg.field_78800_c = f14 + 1.0F - f15;
          ModelRenderer var10000 = this.RightArm;
          var10000.field_78796_g += this.Body.field_78796_g;
          var10000 = this.LeftArm;
          var10000.field_78796_g += this.Body.field_78796_g;
          var10000 = this.LeftArm;
          var10000.field_78795_f += this.Body.field_78796_g;
          this.RightArm.field_78797_d = 8.0F;
          this.LeftArm.field_78797_d = 8.0F;
          this.RightLeg.field_78797_d = 4.0F;
          this.LeftLeg.field_78797_d = 4.0F;
          float f20;
          float f26;
          float f30;
          float f35;
          if (f5 > -9990.0F) {
               f20 = 1.0F - f5;
               f20 *= f20 * f20;
               f20 = 1.0F - f20;
               f26 = MathHelper.func_76126_a(f20 * 3.141593F);
               f30 = MathHelper.func_76126_a(f5 * 3.141593F);
               f35 = f30 * -(this.Head.field_78795_f - 0.7F) * 0.75F;
               if (this.isUnicorn) {
                    var10000 = this.unicornarm;
                    var10000.field_78795_f = (float)((double)var10000.field_78795_f - ((double)f26 * 1.2D + (double)f35));
                    var10000 = this.unicornarm;
                    var10000.field_78796_g += this.Body.field_78796_g * 2.0F;
                    this.unicornarm.field_78808_h = f30 * -0.4F;
               } else {
                    var10000 = this.unicornarm;
                    var10000.field_78795_f = (float)((double)var10000.field_78795_f - ((double)f26 * 1.2D + (double)f35));
                    var10000 = this.unicornarm;
                    var10000.field_78796_g += this.Body.field_78796_g * 2.0F;
                    this.unicornarm.field_78808_h = f30 * -0.4F;
               }
          }

          float f39;
          int l4;
          float f44;
          int j5;
          float f48;
          float f50;
          float f52;
          float f55;
          float f57;
          float f58;
          float f61;
          int l3;
          float f41;
          float f46;
          if (this.isSneak && !this.isFlying) {
               f20 = 0.4F;
               f26 = 7.0F;
               f30 = -4.0F;
               this.Body.field_78795_f = f20;
               this.Body.field_78797_d = f26;
               this.Body.field_78798_e = f30;

               for(l3 = 0; l3 < this.Bodypiece.length; ++l3) {
                    this.Bodypiece[l3].field_78795_f = f20;
                    this.Bodypiece[l3].field_78797_d = f26;
                    this.Bodypiece[l3].field_78798_e = f30;
               }

               f35 = 3.5F;
               f39 = 6.0F;

               for(l4 = 0; l4 < this.LeftWingExt.length; ++l4) {
                    this.LeftWingExt[l4].field_78795_f = (float)((double)f20 + 2.3561947345733643D);
                    this.LeftWingExt[l4].field_78797_d = f26 + f35;
                    this.LeftWingExt[l4].field_78798_e = f30 + f39;
                    this.LeftWingExt[l4].field_78795_f = 2.5F;
                    this.LeftWingExt[l4].field_78808_h = -6.0F;
               }

               f41 = 4.5F;
               f44 = 6.0F;

               for(j5 = 0; j5 < this.LeftWingExt.length; ++j5) {
                    this.RightWingExt[j5].field_78795_f = (float)((double)f20 + 2.3561947345733643D);
                    this.RightWingExt[j5].field_78797_d = f26 + f41;
                    this.RightWingExt[j5].field_78798_e = f30 + f44;
                    this.RightWingExt[j5].field_78795_f = 2.5F;
                    this.RightWingExt[j5].field_78808_h = 6.0F;
               }

               var10000 = this.RightLeg;
               var10000.field_78795_f -= 0.0F;
               var10000 = this.LeftLeg;
               var10000.field_78795_f -= 0.0F;
               var10000 = this.RightArm;
               var10000.field_78795_f -= 0.4F;
               var10000 = this.unicornarm;
               var10000.field_78795_f += 0.4F;
               var10000 = this.LeftArm;
               var10000.field_78795_f -= 0.4F;
               this.RightLeg.field_78798_e = 10.0F;
               this.LeftLeg.field_78798_e = 10.0F;
               this.RightLeg.field_78797_d = 7.0F;
               this.LeftLeg.field_78797_d = 7.0F;
               if (this.isSleeping) {
                    f46 = 2.0F;
                    f48 = -1.0F;
                    f50 = 1.0F;
               } else {
                    f46 = 6.0F;
                    f48 = -2.0F;
                    f50 = 0.0F;
               }

               this.Head.field_78797_d = f46;
               this.Head.field_78798_e = f48;
               this.Head.field_78800_c = f50;
               this.Helmet.field_78797_d = f46;
               this.Helmet.field_78798_e = f48;
               this.Helmet.field_78800_c = f50;
               this.Headpiece[0].field_78797_d = f46;
               this.Headpiece[0].field_78798_e = f48;
               this.Headpiece[0].field_78800_c = f50;
               this.Headpiece[1].field_78797_d = f46;
               this.Headpiece[1].field_78798_e = f48;
               this.Headpiece[1].field_78800_c = f50;
               this.Headpiece[2].field_78797_d = f46;
               this.Headpiece[2].field_78798_e = f48;
               this.Headpiece[2].field_78800_c = f50;
               f52 = 0.0F;
               f55 = 8.0F;
               f57 = -14.0F;
               f58 = 0.0F - f52;
               f61 = 9.0F - f55;
               float f62 = -4.0F - f57;
               float f63 = 0.0F;

               for(int i6 = 0; i6 < this.Tail.length; ++i6) {
                    this.Tail[i6].field_78800_c = f58;
                    this.Tail[i6].field_78797_d = f61;
                    this.Tail[i6].field_78798_e = f62;
                    this.Tail[i6].field_78795_f = f63;
               }
          } else {
               f20 = 0.0F;
               f26 = 0.0F;
               f30 = 0.0F;
               this.Body.field_78795_f = f20;
               this.Body.field_78797_d = f26;
               this.Body.field_78798_e = f30;

               for(l3 = 0; l3 < this.Bodypiece.length; ++l3) {
                    this.Bodypiece[l3].field_78795_f = f20;
                    this.Bodypiece[l3].field_78797_d = f26;
                    this.Bodypiece[l3].field_78798_e = f30;
               }

               if (this.isPegasus) {
                    if (!this.isFlying) {
                         for(l3 = 0; l3 < this.LeftWing.length; ++l3) {
                              this.LeftWing[l3].field_78795_f = (float)((double)f20 + 1.5707964897155762D);
                              this.LeftWing[l3].field_78797_d = f26 + 13.0F;
                              this.LeftWing[l3].field_78798_e = f30 - 3.0F;
                         }

                         for(l3 = 0; l3 < this.RightWing.length; ++l3) {
                              this.RightWing[l3].field_78795_f = (float)((double)f20 + 1.5707964897155762D);
                              this.RightWing[l3].field_78797_d = f26 + 13.0F;
                              this.RightWing[l3].field_78798_e = f30 - 3.0F;
                         }
                    } else {
                         f35 = 5.5F;
                         f39 = 3.0F;

                         for(l4 = 0; l4 < this.LeftWingExt.length; ++l4) {
                              this.LeftWingExt[l4].field_78795_f = (float)((double)f20 + 1.5707964897155762D);
                              this.LeftWingExt[l4].field_78797_d = f26 + f35;
                              this.LeftWingExt[l4].field_78798_e = f30 + f39;
                         }

                         f41 = 6.5F;
                         f44 = 3.0F;

                         for(j5 = 0; j5 < this.RightWingExt.length; ++j5) {
                              this.RightWingExt[j5].field_78795_f = (float)((double)f20 + 1.5707964897155762D);
                              this.RightWingExt[j5].field_78797_d = f26 + f41;
                              this.RightWingExt[j5].field_78798_e = f30 + f44;
                         }
                    }
               }

               this.RightLeg.field_78798_e = 10.0F;
               this.LeftLeg.field_78798_e = 10.0F;
               this.RightLeg.field_78797_d = 8.0F;
               this.LeftLeg.field_78797_d = 8.0F;
               f35 = MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
               f39 = MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
               var10000 = this.unicornarm;
               var10000.field_78808_h += f35;
               var10000 = this.unicornarm;
               var10000.field_78795_f += f39;
               if (this.isPegasus && this.isFlying) {
                    this.WingRotateAngleY = MathHelper.func_76126_a(f2 * 0.067F * 8.0F) * 1.0F;
                    this.WingRotateAngleZ = MathHelper.func_76126_a(f2 * 0.067F * 8.0F) * 1.0F;

                    for(l4 = 0; l4 < this.LeftWingExt.length; ++l4) {
                         this.LeftWingExt[l4].field_78795_f = 2.5F;
                         this.LeftWingExt[l4].field_78808_h = -this.WingRotateAngleZ - 4.712F - 0.4F;
                    }

                    for(l4 = 0; l4 < this.RightWingExt.length; ++l4) {
                         this.RightWingExt[l4].field_78795_f = 2.5F;
                         this.RightWingExt[l4].field_78808_h = this.WingRotateAngleZ + 4.712F + 0.4F;
                    }
               }

               if (this.isSleeping) {
                    f41 = 2.0F;
                    f44 = 1.0F;
                    f46 = 1.0F;
               } else {
                    f41 = 0.0F;
                    f44 = 0.0F;
                    f46 = 0.0F;
               }

               this.Head.field_78797_d = f41;
               this.Head.field_78798_e = f44;
               this.Head.field_78800_c = f46;
               this.Helmet.field_78797_d = f41;
               this.Helmet.field_78798_e = f44;
               this.Helmet.field_78800_c = f46;
               this.Headpiece[0].field_78797_d = f41;
               this.Headpiece[0].field_78798_e = f44;
               this.Headpiece[0].field_78800_c = f46;
               this.Headpiece[1].field_78797_d = f41;
               this.Headpiece[1].field_78798_e = f44;
               this.Headpiece[1].field_78800_c = f46;
               this.Headpiece[2].field_78797_d = f41;
               this.Headpiece[2].field_78798_e = f44;
               this.Headpiece[2].field_78800_c = f46;
               f48 = 0.0F;
               f50 = 8.0F;
               f52 = -14.0F;
               f55 = 0.0F - f48;
               f57 = 9.0F - f50;
               f58 = 0.0F - f52;
               f61 = 0.5F * f1;

               int l5;
               for(l5 = 0; l5 < this.Tail.length; ++l5) {
                    this.Tail[l5].field_78800_c = f55;
                    this.Tail[l5].field_78797_d = f57;
                    this.Tail[l5].field_78798_e = f58;
                    if (this.rainboom) {
                         this.Tail[l5].field_78795_f = 1.571F + 0.1F * MathHelper.func_76126_a(f);
                    } else {
                         this.Tail[l5].field_78795_f = f61;
                    }
               }

               for(l5 = 0; l5 < this.Tail.length; ++l5) {
                    if (!this.rainboom) {
                         ModelPlaneRenderer var44 = this.Tail[l5];
                         var44.field_78795_f += f39;
                    }
               }
          }

          this.LeftWingExt[2].field_78795_f -= 0.85F;
          this.LeftWingExt[3].field_78795_f -= 0.75F;
          this.LeftWingExt[4].field_78795_f -= 0.5F;
          this.LeftWingExt[6].field_78795_f -= 0.85F;
          this.RightWingExt[2].field_78795_f -= 0.85F;
          this.RightWingExt[3].field_78795_f -= 0.75F;
          this.RightWingExt[4].field_78795_f -= 0.5F;
          this.RightWingExt[6].field_78795_f -= 0.85F;
          this.Bodypiece[9].field_78795_f += 0.5F;
          this.Bodypiece[10].field_78795_f += 0.5F;
          this.Bodypiece[11].field_78795_f += 0.5F;
          this.Bodypiece[12].field_78795_f += 0.5F;
          if (this.rainboom) {
               for(int j1 = 0; j1 < this.Tail.length; ++j1) {
                    this.Tail[j1].field_78797_d += 6.0F;
                    ++this.Tail[j1].field_78798_e;
               }
          }

          if (this.isSleeping) {
               this.RightArm.field_78798_e += 6.0F;
               this.LeftArm.field_78798_e += 6.0F;
               this.RightLeg.field_78798_e -= 8.0F;
               this.LeftLeg.field_78798_e -= 8.0F;
               this.RightArm.field_78797_d += 2.0F;
               this.LeftArm.field_78797_d += 2.0F;
               this.RightLeg.field_78797_d += 2.0F;
               this.LeftLeg.field_78797_d += 2.0F;
          }

          if (this.aimedBow) {
               if (this.isUnicorn) {
                    f20 = 0.0F;
                    f26 = 0.0F;
                    this.unicornarm.field_78808_h = 0.0F;
                    this.unicornarm.field_78796_g = -(0.1F - f20 * 0.6F) + this.Head.field_78796_g;
                    this.unicornarm.field_78795_f = 4.712F + this.Head.field_78795_f;
                    var10000 = this.unicornarm;
                    var10000.field_78795_f -= f20 * 1.2F - f26 * 0.4F;
                    var10000 = this.unicornarm;
                    var10000.field_78808_h += MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
                    var10000 = this.unicornarm;
                    var10000.field_78795_f += MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
               } else {
                    f20 = 0.0F;
                    f26 = 0.0F;
                    this.RightArm.field_78808_h = 0.0F;
                    this.RightArm.field_78796_g = -(0.1F - f20 * 0.6F) + this.Head.field_78796_g;
                    this.RightArm.field_78795_f = 4.712F + this.Head.field_78795_f;
                    var10000 = this.RightArm;
                    var10000.field_78795_f -= f20 * 1.2F - f26 * 0.4F;
                    var10000 = this.RightArm;
                    var10000.field_78808_h += MathHelper.func_76134_b(f2 * 0.09F) * 0.05F + 0.05F;
                    var10000 = this.RightArm;
                    var10000.field_78795_f += MathHelper.func_76126_a(f2 * 0.067F) * 0.05F;
                    ++this.RightArm.field_78798_e;
               }
          }

     }

     public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          EntityNpcPony pony = (EntityNpcPony)entity;
          if (pony.textureLocation != pony.checked && pony.textureLocation != null) {
               try {
                    IResource resource = Minecraft.getMinecraft().func_110442_L().func_110536_a(pony.textureLocation);
                    BufferedImage bufferedimage = ImageIO.read(resource.func_110527_b());
                    pony.isPegasus = false;
                    pony.isUnicorn = false;
                    Color color = new Color(bufferedimage.getRGB(0, 0), true);
                    Color color1 = new Color(249, 177, 49, 255);
                    Color color2 = new Color(136, 202, 240, 255);
                    Color color3 = new Color(209, 159, 228, 255);
                    Color color4 = new Color(254, 249, 252, 255);
                    if (color.equals(color1)) {
                    }

                    if (color.equals(color2)) {
                         pony.isPegasus = true;
                    }

                    if (color.equals(color3)) {
                         pony.isUnicorn = true;
                    }

                    if (color.equals(color4)) {
                         pony.isPegasus = true;
                         pony.isUnicorn = true;
                    }

                    pony.checked = pony.textureLocation;
               } catch (IOException var16) {
               }
          }

          this.isSleeping = pony.func_70608_bn();
          this.isUnicorn = pony.isUnicorn;
          this.isPegasus = pony.isPegasus;
          this.isSneak = pony.isSneaking();
          this.heldItemRight = pony.getHeldItemMainhand() == null ? 0 : 1;
          this.func_78087_a(f, f1, f2, f3, f4, f5, entity);
          GlStateManager.func_179094_E();
          if (this.isSleeping) {
               GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(0.0F, -0.5F, -0.9F);
          }

          float scale = f5;
          this.Head.func_78785_a(f5);
          this.Headpiece[0].func_78785_a(f5);
          this.Headpiece[1].func_78785_a(f5);
          if (this.isUnicorn) {
               this.Headpiece[2].func_78785_a(f5);
          }

          this.Helmet.func_78785_a(f5);
          this.Body.func_78785_a(f5);

          int l;
          for(l = 0; l < this.Bodypiece.length; ++l) {
               this.Bodypiece[l].func_78785_a(scale);
          }

          this.LeftArm.func_78785_a(scale);
          this.RightArm.func_78785_a(scale);
          this.LeftLeg.func_78785_a(scale);
          this.RightLeg.func_78785_a(scale);

          for(l = 0; l < this.Tail.length; ++l) {
               this.Tail[l].func_78785_a(scale);
          }

          if (this.isPegasus) {
               if (!this.isFlying && !this.isSneak) {
                    for(l = 0; l < this.LeftWing.length; ++l) {
                         this.LeftWing[l].func_78785_a(scale);
                    }

                    for(l = 0; l < this.RightWing.length; ++l) {
                         this.RightWing[l].func_78785_a(scale);
                    }
               } else {
                    for(l = 0; l < this.LeftWingExt.length; ++l) {
                         this.LeftWingExt[l].func_78785_a(scale);
                    }

                    for(l = 0; l < this.RightWingExt.length; ++l) {
                         this.RightWingExt[l].func_78785_a(scale);
                    }
               }
          }

          GlStateManager.func_179121_F();
     }
}
