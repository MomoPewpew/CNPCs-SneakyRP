package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;

public class AniNo {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
          float ticks = (float)(entity.ticksExisted - ((EntityNPCInterface)entity).animationStart) / 8.0F;
          float ticks2 = (float)(entity.ticksExisted + 1 - ((EntityNPCInterface)entity).animationStart) / 8.0F;
          ticks += (ticks2 - ticks) * Minecraft.getMinecraft().getRenderPartialTicks();
          ticks %= 2.0F;
          float ani = ticks - 0.5F;
          if (ticks > 1.0F) {
               ani = 1.5F - ticks;
          }

          model.bipedHead.rotateAngleY = ani;
     }
}
