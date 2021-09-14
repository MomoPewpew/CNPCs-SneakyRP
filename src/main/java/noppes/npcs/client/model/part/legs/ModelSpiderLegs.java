package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelSpiderLegs extends ModelRenderer {
     private ModelRenderer spiderLeg1;
     private ModelRenderer spiderLeg2;
     private ModelRenderer spiderLeg3;
     private ModelRenderer spiderLeg4;
     private ModelRenderer spiderLeg5;
     private ModelRenderer spiderLeg6;
     private ModelRenderer spiderLeg7;
     private ModelRenderer spiderLeg8;
     private ModelRenderer spiderBody;
     private ModelRenderer spiderNeck;
     private ModelBiped base;

     public ModelSpiderLegs(ModelBiped base) {
          super(base);
          this.base = base;
          float var1 = 0.0F;
          byte var2 = 15;
          this.spiderNeck = new ModelRenderer(base, 0, 0);
          this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, var1);
          this.spiderNeck.setRotationPoint(0.0F, (float)var2, 2.0F);
          this.addChild(this.spiderNeck);
          this.spiderBody = new ModelRenderer(base, 0, 12);
          this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, var1);
          this.spiderBody.setRotationPoint(0.0F, (float)var2, 11.0F);
          this.addChild(this.spiderBody);
          this.spiderLeg1 = new ModelRenderer(base, 18, 0);
          this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg1.setRotationPoint(-4.0F, (float)var2, 4.0F);
          this.addChild(this.spiderLeg1);
          this.spiderLeg2 = new ModelRenderer(base, 18, 0);
          this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg2.setRotationPoint(4.0F, (float)var2, 4.0F);
          this.addChild(this.spiderLeg2);
          this.spiderLeg3 = new ModelRenderer(base, 18, 0);
          this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg3.setRotationPoint(-4.0F, (float)var2, 3.0F);
          this.addChild(this.spiderLeg3);
          this.spiderLeg4 = new ModelRenderer(base, 18, 0);
          this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg4.setRotationPoint(4.0F, (float)var2, 3.0F);
          this.addChild(this.spiderLeg4);
          this.spiderLeg5 = new ModelRenderer(base, 18, 0);
          this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg5.setRotationPoint(-4.0F, (float)var2, 2.0F);
          this.addChild(this.spiderLeg5);
          this.spiderLeg6 = new ModelRenderer(base, 18, 0);
          this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg6.setRotationPoint(4.0F, (float)var2, 2.0F);
          this.addChild(this.spiderLeg6);
          this.spiderLeg7 = new ModelRenderer(base, 18, 0);
          this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg7.setRotationPoint(-4.0F, (float)var2, 1.0F);
          this.addChild(this.spiderLeg7);
          this.spiderLeg8 = new ModelRenderer(base, 18, 0);
          this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
          this.spiderLeg8.setRotationPoint(4.0F, (float)var2, 1.0F);
          this.addChild(this.spiderLeg8);
     }

     public void setRotationAngles(ModelData data, float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          this.rotateAngleX = 0.0F;
          this.rotationPointY = 0.0F;
          this.rotationPointZ = 0.0F;
          this.spiderBody.rotationPointY = 15.0F;
          this.spiderBody.rotationPointZ = 11.0F;
          this.spiderNeck.rotateAngleX = 0.0F;
          float var8 = 0.7853982F;
          this.spiderLeg1.rotateAngleZ = -var8;
          this.spiderLeg2.rotateAngleZ = var8;
          this.spiderLeg3.rotateAngleZ = -var8 * 0.74F;
          this.spiderLeg4.rotateAngleZ = var8 * 0.74F;
          this.spiderLeg5.rotateAngleZ = -var8 * 0.74F;
          this.spiderLeg6.rotateAngleZ = var8 * 0.74F;
          this.spiderLeg7.rotateAngleZ = -var8;
          this.spiderLeg8.rotateAngleZ = var8;
          float var9 = -0.0F;
          float var10 = 0.3926991F;
          this.spiderLeg1.rotateAngleY = var10 * 2.0F + var9;
          this.spiderLeg2.rotateAngleY = -var10 * 2.0F - var9;
          this.spiderLeg3.rotateAngleY = var10 * 1.0F + var9;
          this.spiderLeg4.rotateAngleY = -var10 * 1.0F - var9;
          this.spiderLeg5.rotateAngleY = -var10 * 1.0F + var9;
          this.spiderLeg6.rotateAngleY = var10 * 1.0F - var9;
          this.spiderLeg7.rotateAngleY = -var10 * 2.0F + var9;
          this.spiderLeg8.rotateAngleY = var10 * 2.0F - var9;
          float var11 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * par2;
          float var12 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * par2;
          float var13 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * par2;
          float var14 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 4.712389F) * 0.4F) * par2;
          float var15 = Math.abs(MathHelper.sin(par1 * 0.6662F + 0.0F) * 0.4F) * par2;
          float var16 = Math.abs(MathHelper.sin(par1 * 0.6662F + 3.1415927F) * 0.4F) * par2;
          float var17 = Math.abs(MathHelper.sin(par1 * 0.6662F + 1.5707964F) * 0.4F) * par2;
          float var18 = Math.abs(MathHelper.sin(par1 * 0.6662F + 4.712389F) * 0.4F) * par2;
          ModelRenderer var10000 = this.spiderLeg1;
          var10000.rotateAngleY += var11;
          var10000 = this.spiderLeg2;
          var10000.rotateAngleY += -var11;
          var10000 = this.spiderLeg3;
          var10000.rotateAngleY += var12;
          var10000 = this.spiderLeg4;
          var10000.rotateAngleY += -var12;
          var10000 = this.spiderLeg5;
          var10000.rotateAngleY += var13;
          var10000 = this.spiderLeg6;
          var10000.rotateAngleY += -var13;
          var10000 = this.spiderLeg7;
          var10000.rotateAngleY += var14;
          var10000 = this.spiderLeg8;
          var10000.rotateAngleY += -var14;
          var10000 = this.spiderLeg1;
          var10000.rotateAngleZ += var15;
          var10000 = this.spiderLeg2;
          var10000.rotateAngleZ += -var15;
          var10000 = this.spiderLeg3;
          var10000.rotateAngleZ += var16;
          var10000 = this.spiderLeg4;
          var10000.rotateAngleZ += -var16;
          var10000 = this.spiderLeg5;
          var10000.rotateAngleZ += var17;
          var10000 = this.spiderLeg6;
          var10000.rotateAngleZ += -var17;
          var10000 = this.spiderLeg7;
          var10000.rotateAngleZ += var18;
          var10000 = this.spiderLeg8;
          var10000.rotateAngleZ += -var18;
          if (this.base.isSneak) {
               this.rotationPointZ = 5.0F;
               this.rotationPointY = -1.0F;
               this.spiderBody.rotationPointY = 16.0F;
               this.spiderBody.rotationPointZ = 10.0F;
               this.spiderNeck.rotateAngleX = -0.3926991F;
          }

          if (((EntityNPCInterface)entity).isPlayerSleeping() || ((EntityNPCInterface)entity).currentAnimation == 7) {
               this.rotationPointY = 12.0F * data.getPartConfig(EnumParts.LEG_LEFT).scaleY;
               this.rotationPointZ = 15.0F * data.getPartConfig(EnumParts.LEG_LEFT).scaleY;
               this.rotateAngleX = -1.5707964F;
          }

     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.rotateAngleX = x;
          model.rotateAngleY = y;
          model.rotateAngleZ = z;
     }
}
