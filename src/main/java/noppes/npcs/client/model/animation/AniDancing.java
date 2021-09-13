package noppes.npcs.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class AniDancing {
     public static void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, ModelBiped model) {
          float dancing = (float)entity.field_70173_aa / 4.0F;
          float dancing2 = (float)(entity.field_70173_aa + 1) / 4.0F;
          dancing += (dancing2 - dancing) * Minecraft.getMinecraft().func_184121_ak();
          float x = (float)Math.sin((double)dancing);
          float y = (float)Math.abs(Math.cos((double)dancing));
          model.field_178720_f.field_78800_c = model.field_78116_c.field_78800_c = x * 0.75F;
          model.field_178720_f.field_78797_d = model.field_78116_c.field_78797_d = y * 1.25F - 0.02F;
          model.field_178720_f.field_78798_e = model.field_78116_c.field_78798_e = -y * 0.75F;
          ModelRenderer var10000 = model.field_178724_i;
          var10000.field_78800_c += x * 0.25F;
          var10000 = model.field_178724_i;
          var10000.field_78797_d += y * 1.25F;
          var10000 = model.field_178723_h;
          var10000.field_78800_c += x * 0.25F;
          var10000 = model.field_178723_h;
          var10000.field_78797_d += y * 1.25F;
          model.field_78115_e.field_78800_c = x * 0.25F;
     }
}
