package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.LogWriter;
import noppes.npcs.client.model.ModelWrapper;

public class NPCRendererHelper {
     private static final ModelWrapper wrapper = new ModelWrapper();

     public static String getTexture(RenderLivingBase render, Entity entity) {
          ResourceLocation location = render.getEntityTexture(entity);
          return location != null ? location.toString() : TextureMap.field_174945_f.toString();
     }

     public static void preRenderCallback(EntityLivingBase entity, float f, RenderLivingBase render) {
          render.preRenderCallback(entity, f);
     }

     public static void renderModel(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, RenderLivingBase render, ModelBase main, ResourceLocation resource) {
          wrapper.mainModelOld = render.field_77045_g;
          if (!(main instanceof ModelWrapper)) {
               wrapper.wrapped = main;
               wrapper.texture = resource;
               render.field_77045_g = wrapper;
          }

          try {
               render.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
          } catch (Exception var11) {
               LogWriter.except(var11);
          }

          render.field_77045_g = wrapper.mainModelOld;
     }

     public static float handleRotationFloat(EntityLivingBase entity, float par2, RenderLivingBase renderEntity) {
          return renderEntity.handleRotationFloat(entity, par2);
     }

     public static void drawLayers(EntityLivingBase entity, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_, RenderLivingBase renderEntity) {
          renderEntity.renderLayers(entity, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
     }
}
