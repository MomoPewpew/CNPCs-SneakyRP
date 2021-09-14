package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AniDancing {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
          float dancing = (float)entity.ticksExisted / 4.0F;
          float dancing2 = (float)(entity.ticksExisted + 1) / 4.0F;
          dancing += (dancing2 - dancing) * Minecraft.getMinecraft().getRenderPartialTicks();
          float x = (float)Math.sin((double)dancing);
          float y = (float)Math.abs(Math.cos((double)dancing));
          model.bipedHeadwear.rotationPointX = model.bipedHead.rotationPointX = x * 0.75F;
          model.bipedHeadwear.rotationPointY = model.bipedHead.rotationPointY = y * 1.25F - 0.02F;
          model.bipedHeadwear.rotationPointZ = model.bipedHead.rotationPointZ = -y * 0.75F;
          ModelRenderer var10000 = model.bipedLeftArm;
          var10000.rotationPointX += x * 0.25F;
          var10000 = model.bipedLeftArm;
          var10000.rotationPointY += y * 1.25F;
          var10000 = model.bipedRightArm;
          var10000.rotationPointX += x * 0.25F;
          var10000 = model.bipedRightArm;
          var10000.rotationPointY += y * 1.25F;
          model.bipedBody.rotationPointX = x * 0.25F;
     }
}
