package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelClassicPlayer extends ModelPlayerAlt {
     public ModelClassicPlayer(float scale) {
          super(scale, false);
     }

     public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
          super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
          float j = 2.0F;
          if (entity.isSprinting()) {
               j = 1.0F;
          }

          ModelRenderer var10000 = this.bipedRightArm;
          var10000.rotateAngleX += MathHelper.cos(par1 * 0.6662F + 3.1415927F) * j * par2;
          var10000 = this.bipedLeftArm;
          var10000.rotateAngleX += MathHelper.cos(par1 * 0.6662F) * j * par2;
          var10000 = this.bipedLeftArm;
          var10000.rotateAngleZ += (MathHelper.cos(par1 * 0.2812F) - 1.0F) * par2;
          var10000 = this.bipedRightArm;
          var10000.rotateAngleZ += (MathHelper.cos(par1 * 0.2312F) + 1.0F) * par2;
          this.bipedLeftArmwear.rotateAngleX = this.bipedLeftArm.rotateAngleX;
          this.bipedLeftArmwear.rotateAngleY = this.bipedLeftArm.rotateAngleY;
          this.bipedLeftArmwear.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
          this.bipedRightArmwear.rotateAngleX = this.bipedRightArm.rotateAngleX;
          this.bipedRightArmwear.rotateAngleY = this.bipedRightArm.rotateAngleY;
          this.bipedRightArmwear.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
     }
}
