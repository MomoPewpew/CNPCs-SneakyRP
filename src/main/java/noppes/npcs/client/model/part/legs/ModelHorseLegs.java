package noppes.npcs.client.model.part.legs;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.ModelData;

public class ModelHorseLegs extends ModelRenderer {
     private ModelRenderer backLeftLeg;
     private ModelRenderer backLeftShin;
     private ModelRenderer backLeftHoof;
     private ModelRenderer backRightLeg;
     private ModelRenderer backRightShin;
     private ModelRenderer backRightHoof;
     private ModelRenderer frontLeftLeg;
     private ModelRenderer frontLeftShin;
     private ModelRenderer frontLeftHoof;
     private ModelRenderer frontRightLeg;
     private ModelRenderer frontRightShin;
     private ModelRenderer frontRightHoof;
     private ModelBiped base;

     public ModelHorseLegs(ModelBiped model) {
          super(model);
          this.base = model;
          float var1 = 0.0F;
          byte var2 = true;
          int zOffset = 10;
          float yOffset = 7.0F;
          ModelRenderer body = new ModelRenderer(model, 0, 34);
          body.func_78787_b(128, 128);
          body.func_78789_a(-5.0F, -8.0F, -19.0F, 10, 10, 24);
          body.func_78793_a(0.0F, 11.0F + yOffset, 9.0F + (float)zOffset);
          this.func_78792_a(body);
          this.backLeftLeg = new ModelRenderer(model, 78, 29);
          this.backLeftLeg.func_78787_b(128, 128);
          this.backLeftLeg.func_78789_a(-2.0F, -2.0F, -2.5F, 4, 9, 5);
          this.backLeftLeg.func_78793_a(4.0F, 9.0F + yOffset, 11.0F + (float)zOffset);
          this.func_78792_a(this.backLeftLeg);
          this.backLeftShin = new ModelRenderer(model, 78, 43);
          this.backLeftShin.func_78787_b(128, 128);
          this.backLeftShin.func_78789_a(-1.5F, 0.0F, -1.5F, 3, 5, 3);
          this.backLeftShin.func_78793_a(0.0F, 7.0F, 0.0F);
          this.backLeftLeg.func_78792_a(this.backLeftShin);
          this.backLeftHoof = new ModelRenderer(model, 78, 51);
          this.backLeftHoof.func_78787_b(128, 128);
          this.backLeftHoof.func_78789_a(-2.0F, 5.0F, -2.0F, 4, 3, 4);
          this.backLeftHoof.func_78793_a(0.0F, 7.0F, 0.0F);
          this.backLeftLeg.func_78792_a(this.backLeftHoof);
          this.backRightLeg = new ModelRenderer(model, 96, 29);
          this.backRightLeg.func_78787_b(128, 128);
          this.backRightLeg.func_78789_a(-2.0F, -2.0F, -2.5F, 4, 9, 5);
          this.backRightLeg.func_78793_a(-4.0F, 9.0F + yOffset, 11.0F + (float)zOffset);
          this.func_78792_a(this.backRightLeg);
          this.backRightShin = new ModelRenderer(model, 96, 43);
          this.backRightShin.func_78787_b(128, 128);
          this.backRightShin.func_78789_a(-1.5F, 0.0F, -1.5F, 3, 5, 3);
          this.backRightShin.func_78793_a(0.0F, 7.0F, 0.0F);
          this.backRightLeg.func_78792_a(this.backRightShin);
          this.backRightHoof = new ModelRenderer(model, 96, 51);
          this.backRightHoof.func_78787_b(128, 128);
          this.backRightHoof.func_78789_a(-2.0F, 5.0F, -2.0F, 4, 3, 4);
          this.backRightHoof.func_78793_a(0.0F, 7.0F, 0.0F);
          this.backRightLeg.func_78792_a(this.backRightHoof);
          this.frontLeftLeg = new ModelRenderer(model, 44, 29);
          this.frontLeftLeg.func_78787_b(128, 128);
          this.frontLeftLeg.func_78789_a(-1.4F, -1.0F, -2.1F, 3, 8, 4);
          this.frontLeftLeg.func_78793_a(4.0F, 9.0F + yOffset, -8.0F + (float)zOffset);
          this.func_78792_a(this.frontLeftLeg);
          this.frontLeftShin = new ModelRenderer(model, 44, 41);
          this.frontLeftShin.func_78787_b(128, 128);
          this.frontLeftShin.func_78789_a(-1.4F, 0.0F, -1.6F, 3, 5, 3);
          this.frontLeftShin.func_78793_a(0.0F, 7.0F, 0.0F);
          this.frontLeftLeg.func_78792_a(this.frontLeftShin);
          this.frontLeftHoof = new ModelRenderer(model, 44, 51);
          this.frontLeftHoof.func_78787_b(128, 128);
          this.frontLeftHoof.func_78789_a(-1.9F, 5.0F, -2.1F, 4, 3, 4);
          this.frontLeftHoof.func_78793_a(0.0F, 7.0F, 0.0F);
          this.frontLeftLeg.func_78792_a(this.frontLeftHoof);
          this.frontRightLeg = new ModelRenderer(model, 60, 29);
          this.frontRightLeg.func_78787_b(128, 128);
          this.frontRightLeg.func_78789_a(-1.6F, -1.0F, -2.1F, 3, 8, 4);
          this.frontRightLeg.func_78793_a(-4.0F, 9.0F + yOffset, -8.0F + (float)zOffset);
          this.func_78792_a(this.frontRightLeg);
          this.frontRightShin = new ModelRenderer(model, 60, 41);
          this.frontRightShin.func_78787_b(128, 128);
          this.frontRightShin.func_78789_a(-1.6F, 0.0F, -1.6F, 3, 5, 3);
          this.frontRightShin.func_78793_a(0.0F, 7.0F, 0.0F);
          this.frontRightLeg.func_78792_a(this.frontRightShin);
          this.frontRightHoof = new ModelRenderer(model, 60, 51);
          this.frontRightHoof.func_78787_b(128, 128);
          this.frontRightHoof.func_78789_a(-2.1F, 5.0F, -2.1F, 4, 3, 4);
          this.frontRightHoof.func_78793_a(0.0F, 7.0F, 0.0F);
          this.frontRightLeg.func_78792_a(this.frontRightHoof);
     }

     public void setRotationAngles(ModelData data, float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          this.frontLeftLeg.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F) * 0.4F * par2;
          this.frontRightLeg.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F + 3.1415927F) * 0.4F * par2;
          this.backLeftLeg.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F + 3.1415927F) * 0.4F * par2;
          this.backRightLeg.field_78795_f = MathHelper.func_76134_b(par1 * 0.6662F) * 0.4F * par2;
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
