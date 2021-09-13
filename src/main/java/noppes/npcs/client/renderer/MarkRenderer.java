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
          Minecraft mc = Minecraft.func_71410_x();
          GlStateManager.func_179094_E();
          int color = mark.color;
          float red = (float)(color >> 16 & 255) / 255.0F;
          float blue = (float)(color >> 8 & 255) / 255.0F;
          float green = (float)(color & 255) / 255.0F;
          GlStateManager.func_179124_c(red, blue, green);
          GlStateManager.func_179137_b(x, y + (double)entity.field_70131_O + 0.6D, z);
          GlStateManager.func_179114_b(-entity.field_70759_as, 0.0F, 1.0F, 0.0F);
          if (mark.type == 2) {
               Minecraft.func_71410_x().func_110434_K().bindTexture(markExclamation);
          } else if (mark.type == 1) {
               Minecraft.func_71410_x().func_110434_K().bindTexture(markQuestion);
          } else if (mark.type == 3) {
               Minecraft.func_71410_x().func_110434_K().bindTexture(markPointer);
          } else if (mark.type == 5) {
               Minecraft.func_71410_x().func_110434_K().bindTexture(markCross);
          } else if (mark.type == 4) {
               Minecraft.func_71410_x().func_110434_K().bindTexture(markSkull);
          } else if (mark.type == 6) {
               Minecraft.func_71410_x().func_110434_K().bindTexture(markStar);
          }

          if (displayList >= 0) {
               GlStateManager.func_179148_o(displayList);
          } else {
               displayList = GLAllocation.func_74526_a(1);
               GL11.glNewList(displayList, 4864);
               GlStateManager.func_179137_b(-0.5D, 0.0D, 0.0D);
               Model2DRenderer.renderItemIn2D(Tessellator.func_178181_a().func_178180_c(), 0.0F, 0.0F, 1.0F, 1.0F, 32, 32, 0.0625F);
               GL11.glEndList();
          }

          GlStateManager.func_179121_F();
     }
}
