package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;

public class AniBow {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
          float ticks = (float)(entity.field_70173_aa - ((EntityNPCInterface)entity).animationStart) / 10.0F;
          if (ticks > 1.0F) {
               ticks = 1.0F;
          }

          float ticks2 = (float)(entity.field_70173_aa + 1 - ((EntityNPCInterface)entity).animationStart) / 10.0F;
          if (ticks2 > 1.0F) {
               ticks2 = 1.0F;
          }

          ticks += (ticks2 - ticks) * Minecraft.getMinecraft().func_184121_ak();
          model.field_78115_e.field_78795_f = ticks;
          model.field_78116_c.field_78795_f = ticks;
          model.field_178724_i.field_78795_f = ticks;
          model.field_178723_h.field_78795_f = ticks;
          model.field_78115_e.field_78798_e = -ticks * 10.0F;
          model.field_78115_e.field_78797_d = ticks * 6.0F;
          model.field_78116_c.field_78798_e = -ticks * 10.0F;
          model.field_78116_c.field_78797_d = ticks * 6.0F;
          model.field_178724_i.field_78798_e = -ticks * 10.0F;
          ModelRenderer var10000 = model.field_178724_i;
          var10000.field_78797_d += ticks * 6.0F;
          model.field_178723_h.field_78798_e = -ticks * 10.0F;
          var10000 = model.field_178723_h;
          var10000.field_78797_d += ticks * 6.0F;
     }
}
