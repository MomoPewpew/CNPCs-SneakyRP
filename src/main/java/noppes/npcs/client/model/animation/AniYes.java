package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;

public class AniYes {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
          float ticks = (float)(entity.field_70173_aa - ((EntityNPCInterface)entity).animationStart) / 8.0F;
          float ticks2 = (float)(entity.field_70173_aa + 1 - ((EntityNPCInterface)entity).animationStart) / 8.0F;
          ticks += (ticks2 - ticks) * Minecraft.func_71410_x().func_184121_ak();
          ticks %= 2.0F;
          float ani = ticks - 0.5F;
          if (ticks > 1.0F) {
               ani = 1.5F - ticks;
          }

          model.field_78116_c.field_78795_f = ani;
     }
}
