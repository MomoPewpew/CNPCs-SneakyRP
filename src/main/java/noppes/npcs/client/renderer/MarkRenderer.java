package noppes.npcs.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.Model2DRenderer;
import noppes.npcs.controllers.data.MarkData;
import org.lwjgl.opengl.GL11;

public class MarkRenderer {
     public static ResourceLocation markExclamation = new ResourceLocation("customnpcs", "textures/marks/exclamation.png");
     public static ResourceLocation markQuestion = new ResourceLocation("customnpcs", "textures/marks/question.png");
     public static ResourceLocation markPointer = new ResourceLocation("customnpcs", "textures/marks/pointer.png");
     public static ResourceLocation markCross = new ResourceLocation("customnpcs", "textures/marks/cross.png");
     public static ResourceLocation markSkull = new ResourceLocation("customnpcs", "textures/marks/skull.png");
     public static ResourceLocation markStar = new ResourceLocation("customnpcs", "textures/marks/star.png");
     public static int displayList = -1;

     public static void render(EntityLivingBase entity, double x, double y, double z, MarkData.Mark mark) {
          Minecraft mc = Minecraft.getMinecraft();
          GlStateManager.pushMatrix();
          int color = mark.color;
          float red = (float)(color >> 16 & 255) / 255.0F;
          float blue = (float)(color >> 8 & 255) / 255.0F;
          float green = (float)(color & 255) / 255.0F;
          GlStateManager.color(red, blue, green);
          GlStateManager.translate(x, y + (double)entity.height + 0.6D, z);
          GlStateManager.rotate(-entity.rotationYawHead, 0.0F, 1.0F, 0.0F);
          if (mark.type == 2) {
               Minecraft.getMinecraft().getTextureManager().bindTexture(markExclamation);
          } else if (mark.type == 1) {
               Minecraft.getMinecraft().getTextureManager().bindTexture(markQuestion);
          } else if (mark.type == 3) {
               Minecraft.getMinecraft().getTextureManager().bindTexture(markPointer);
          } else if (mark.type == 5) {
               Minecraft.getMinecraft().getTextureManager().bindTexture(markCross);
          } else if (mark.type == 4) {
               Minecraft.getMinecraft().getTextureManager().bindTexture(markSkull);
          } else if (mark.type == 6) {
               Minecraft.getMinecraft().getTextureManager().bindTexture(markStar);
          }

          if (displayList >= 0) {
               GlStateManager.callList(displayList);
          } else {
               displayList = GLAllocation.generateDisplayLists(1);
               GL11.glNewList(displayList, 4864);
               GlStateManager.translate(-0.5D, 0.0D, 0.0D);
               Model2DRenderer.renderItemIn2D(Tessellator.getInstance().getBuffer(), 0.0F, 0.0F, 1.0F, 1.0F, 32, 32, 0.0625F);
               GL11.glEndList();
          }

          GlStateManager.popMatrix();
     }
}
