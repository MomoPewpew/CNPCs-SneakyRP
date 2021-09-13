package noppes.npcs.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelWrapper extends ModelBase {
     public ModelBase wrapped;
     public ResourceLocation texture;
     public ModelBase mainModelOld;

     public void func_78088_a(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
          Minecraft.getMinecraft().func_175598_ae().field_78724_e.bindTexture(this.texture);
          this.wrapped.func_78088_a(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
     }
}
