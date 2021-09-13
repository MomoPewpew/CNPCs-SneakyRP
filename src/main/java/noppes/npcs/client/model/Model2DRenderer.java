package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class Model2DRenderer extends ModelRenderer {
     private boolean isCompiled;
     private int displayList;
     private float x1;
     private float x2;
     private float y1;
     private float y2;
     private int width;
     private int height;
     private float rotationOffsetX;
     private float rotationOffsetY;
     private float rotationOffsetZ;
     private float scaleX;
     private float scaleY;
     private float thickness;

     public Model2DRenderer(ModelBase modelBase, float x, float y, int width, int height, int textureWidth, int textureHeight) {
          super(modelBase);
          this.scaleX = 1.0F;
          this.scaleY = 1.0F;
          this.thickness = 1.0F;
          this.width = width;
          this.height = height;
          this.field_78801_a = (float)textureWidth;
          this.field_78799_b = (float)textureHeight;
          this.x1 = x / (float)textureWidth;
          this.y1 = y / (float)textureHeight;
          this.x2 = (x + (float)width) / (float)textureWidth;
          this.y2 = (y + (float)height) / (float)textureHeight;
     }

     public Model2DRenderer(ModelBase modelBase, float x, float y, int width, int height) {
          this(modelBase, x, y, width, height, modelBase.field_78090_t, modelBase.field_78089_u);
     }

     public void func_78785_a(float scale) {
          if (this.field_78806_j && !this.field_78807_k) {
               if (!this.isCompiled) {
                    this.compile(scale);
               }

               GlStateManager.func_179094_E();
               this.func_78794_c(scale);
               GlStateManager.func_179148_o(this.displayList);
               GlStateManager.func_179121_F();
          }
     }

     public void setRotationOffset(float x, float y, float z) {
          this.rotationOffsetX = x;
          this.rotationOffsetY = y;
          this.rotationOffsetZ = z;
     }

     public void setScale(float scale) {
          this.scaleX = scale;
          this.scaleY = scale;
     }

     public void setScale(float x, float y) {
          this.scaleX = x;
          this.scaleY = y;
     }

     public void setThickness(float thickness) {
          this.thickness = thickness;
     }

     @SideOnly(Side.CLIENT)
     private void compile(float scale) {
          this.displayList = GLAllocation.func_74526_a(1);
          GlStateManager.func_187423_f(this.displayList, 4864);
          GlStateManager.translate(this.rotationOffsetX * scale, this.rotationOffsetY * scale, this.rotationOffsetZ * scale);
          GlStateManager.func_179152_a(this.scaleX * (float)this.width / (float)this.height, this.scaleY, this.thickness);
          GlStateManager.func_179114_b(180.0F, 1.0F, 0.0F, 0.0F);
          if (this.field_78809_i) {
               GlStateManager.translate(0.0F, 0.0F, -1.0F * scale);
               GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
          }

          renderItemIn2D(Tessellator.func_178181_a().func_178180_c(), this.x1, this.y1, this.x2, this.y2, this.width, this.height, scale);
          GL11.glEndList();
          this.isCompiled = true;
     }

     public static void renderItemIn2D(BufferBuilder worldrenderer, float p_78439_1_, float p_78439_2_, float p_78439_3_, float p_78439_4_, int p_78439_5_, int p_78439_6_, float p_78439_7_) {
          Tessellator tessellator = Tessellator.func_178181_a();
          worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181710_j);
          worldrenderer.func_181662_b(0.0D, 0.0D, 0.0D).func_187315_a((double)p_78439_1_, (double)p_78439_4_).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
          worldrenderer.func_181662_b(1.0D, 0.0D, 0.0D).func_187315_a((double)p_78439_3_, (double)p_78439_4_).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
          worldrenderer.func_181662_b(1.0D, 1.0D, 0.0D).func_187315_a((double)p_78439_3_, (double)p_78439_2_).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
          worldrenderer.func_181662_b(0.0D, 1.0D, 0.0D).func_187315_a((double)p_78439_1_, (double)p_78439_2_).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
          worldrenderer.func_181662_b(0.0D, 1.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_1_, (double)p_78439_2_).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
          worldrenderer.func_181662_b(1.0D, 1.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_3_, (double)p_78439_2_).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
          worldrenderer.func_181662_b(1.0D, 0.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_3_, (double)p_78439_4_).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
          worldrenderer.func_181662_b(0.0D, 0.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_1_, (double)p_78439_4_).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
          float f5 = 0.5F * (p_78439_1_ - p_78439_3_) / (float)p_78439_5_;
          float f6 = 0.5F * (p_78439_4_ - p_78439_2_) / (float)p_78439_6_;

          int k;
          float f7;
          float f8;
          for(k = 0; k < p_78439_5_; ++k) {
               f7 = (float)k / (float)p_78439_5_;
               f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
               worldrenderer.func_181662_b((double)f7, 0.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)f8, (double)p_78439_4_).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b((double)f7, 0.0D, 0.0D).func_187315_a((double)f8, (double)p_78439_4_).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b((double)f7, 1.0D, 0.0D).func_187315_a((double)f8, (double)p_78439_2_).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b((double)f7, 1.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)f8, (double)p_78439_2_).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
          }

          float f9;
          for(k = 0; k < p_78439_5_; ++k) {
               f7 = (float)k / (float)p_78439_5_;
               f8 = p_78439_1_ + (p_78439_3_ - p_78439_1_) * f7 - f5;
               f9 = f7 + 1.0F / (float)p_78439_5_;
               worldrenderer.func_181662_b((double)f9, 1.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)f8, (double)p_78439_2_).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b((double)f9, 1.0D, 0.0D).func_187315_a((double)f8, (double)p_78439_2_).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b((double)f9, 0.0D, 0.0D).func_187315_a((double)f8, (double)p_78439_4_).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b((double)f9, 0.0D, (double)(0.0F - p_78439_7_)).func_187315_a((double)f8, (double)p_78439_4_).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
          }

          for(k = 0; k < p_78439_6_; ++k) {
               f7 = (float)k / (float)p_78439_6_;
               f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
               f9 = f7 + 1.0F / (float)p_78439_6_;
               worldrenderer.func_181662_b(0.0D, (double)f9, 0.0D).func_187315_a((double)p_78439_1_, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b(1.0D, (double)f9, 0.0D).func_187315_a((double)p_78439_3_, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b(1.0D, (double)f9, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_3_, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b(0.0D, (double)f9, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_1_, (double)f8).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
          }

          for(k = 0; k < p_78439_6_; ++k) {
               f7 = (float)k / (float)p_78439_6_;
               f8 = p_78439_4_ + (p_78439_2_ - p_78439_4_) * f7 - f6;
               worldrenderer.func_181662_b(1.0D, (double)f7, 0.0D).func_187315_a((double)p_78439_3_, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b(0.0D, (double)f7, 0.0D).func_187315_a((double)p_78439_1_, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b(0.0D, (double)f7, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_1_, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
               worldrenderer.func_181662_b(1.0D, (double)f7, (double)(0.0F - p_78439_7_)).func_187315_a((double)p_78439_3_, (double)f8).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
          }

          tessellator.func_78381_a();
     }
}
