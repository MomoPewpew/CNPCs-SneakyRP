package noppes.npcs.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.entity.EntityProjectile;

@SideOnly(Side.CLIENT)
public class RenderProjectile extends Render {
     public boolean renderWithColor = true;
     private static final ResourceLocation field_110780_a = new ResourceLocation("textures/entity/arrow.png");
     private static final ResourceLocation field_110798_h = new ResourceLocation("textures/misc/enchanted_item_glint.png");

     public RenderProjectile() {
          super(Minecraft.func_71410_x().func_175598_ae());
     }

     public void doRenderProjectile(EntityProjectile projectile, double x, double y, double z, float entityYaw, float partialTicks) {
          Minecraft mc = Minecraft.func_71410_x();
          GlStateManager.func_179094_E();
          GlStateManager.func_179109_b((float)x, (float)y, (float)z);
          GlStateManager.func_179091_B();
          float scale = (float)projectile.getSize() / 10.0F;
          ItemStack item = projectile.getItemDisplay();
          GlStateManager.func_179152_a(scale, scale, scale);
          if (projectile.isArrow()) {
               this.func_180548_c(projectile);
               GlStateManager.func_179114_b(projectile.field_70126_B + (projectile.field_70177_z - projectile.field_70126_B) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179114_b(projectile.field_70127_C + (projectile.field_70125_A - projectile.field_70127_C) * partialTicks, 0.0F, 0.0F, 1.0F);
               Tessellator tessellator = Tessellator.func_178181_a();
               BufferBuilder BufferBuilder = tessellator.func_178180_c();
               int i = 0;
               float f = 0.0F;
               float f1 = 0.5F;
               float f2 = (float)(0 + i * 10) / 32.0F;
               float f3 = (float)(5 + i * 10) / 32.0F;
               float f4 = 0.0F;
               float f5 = 0.15625F;
               float f6 = (float)(5 + i * 10) / 32.0F;
               float f7 = (float)(10 + i * 10) / 32.0F;
               float f8 = 0.05625F;
               GlStateManager.func_179091_B();
               float f9 = (float)projectile.arrowShake - partialTicks;
               if (f9 > 0.0F) {
                    float f10 = -MathHelper.func_76126_a(f9 * 3.0F) * f9;
                    GlStateManager.func_179114_b(f10, 0.0F, 0.0F, 1.0F);
               }

               GlStateManager.func_179114_b(45.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.func_179152_a(f8, f8, f8);
               GlStateManager.func_179109_b(-4.0F, 0.0F, 0.0F);
               if (this.field_188301_f) {
                    GlStateManager.func_179142_g();
                    GlStateManager.func_187431_e(this.func_188298_c(projectile));
               }

               GlStateManager.func_187432_a(f8, 0.0F, 0.0F);
               BufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
               BufferBuilder.func_181662_b(-7.0D, -2.0D, -2.0D).func_187315_a((double)f4, (double)f6).func_181675_d();
               BufferBuilder.func_181662_b(-7.0D, -2.0D, 2.0D).func_187315_a((double)f5, (double)f6).func_181675_d();
               BufferBuilder.func_181662_b(-7.0D, 2.0D, 2.0D).func_187315_a((double)f5, (double)f7).func_181675_d();
               BufferBuilder.func_181662_b(-7.0D, 2.0D, -2.0D).func_187315_a((double)f4, (double)f7).func_181675_d();
               tessellator.func_78381_a();
               GlStateManager.func_187432_a(-f8, 0.0F, 0.0F);
               BufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
               BufferBuilder.func_181662_b(-7.0D, 2.0D, -2.0D).func_187315_a((double)f4, (double)f6).func_181675_d();
               BufferBuilder.func_181662_b(-7.0D, 2.0D, 2.0D).func_187315_a((double)f5, (double)f6).func_181675_d();
               BufferBuilder.func_181662_b(-7.0D, -2.0D, 2.0D).func_187315_a((double)f5, (double)f7).func_181675_d();
               BufferBuilder.func_181662_b(-7.0D, -2.0D, -2.0D).func_187315_a((double)f4, (double)f7).func_181675_d();
               tessellator.func_78381_a();

               for(int j = 0; j < 4; ++j) {
                    GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.func_187432_a(0.0F, 0.0F, f8);
                    BufferBuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
                    BufferBuilder.func_181662_b(-8.0D, -2.0D, 0.0D).func_187315_a((double)f, (double)f2).func_181675_d();
                    BufferBuilder.func_181662_b(8.0D, -2.0D, 0.0D).func_187315_a((double)f1, (double)f2).func_181675_d();
                    BufferBuilder.func_181662_b(8.0D, 2.0D, 0.0D).func_187315_a((double)f1, (double)f3).func_181675_d();
                    BufferBuilder.func_181662_b(-8.0D, 2.0D, 0.0D).func_187315_a((double)f, (double)f3).func_181675_d();
                    tessellator.func_78381_a();
               }

               if (this.field_188301_f) {
                    GlStateManager.func_187417_n();
                    GlStateManager.func_179119_h();
               }
          } else if (projectile.is3D()) {
               GlStateManager.func_179114_b(projectile.field_70126_B + (projectile.field_70177_z - projectile.field_70126_B) * partialTicks - 180.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179114_b(projectile.field_70127_C + (projectile.field_70125_A - projectile.field_70127_C) * partialTicks, 1.0F, 0.0F, 0.0F);
               GlStateManager.func_179137_b(0.0D, -0.125D, 0.25D);
               if (item.func_77973_b() instanceof ItemBlock && Block.func_149634_a(item.func_77973_b()).func_176223_P().func_185901_i() == EnumBlockRenderType.ENTITYBLOCK_ANIMATED) {
                    GlStateManager.func_179109_b(0.0F, 0.1875F, -0.3125F);
                    GlStateManager.func_179114_b(20.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.func_179114_b(45.0F, 0.0F, 1.0F, 0.0F);
                    float f8 = 0.375F;
                    GlStateManager.func_179152_a(-f8, -f8, f8);
               }

               mc.func_175599_af().func_181564_a(item, TransformType.THIRD_PERSON_RIGHT_HAND);
          } else {
               GlStateManager.func_179091_B();
               GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
               GlStateManager.func_179114_b(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179114_b(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
               this.func_110776_a(TextureMap.field_110575_b);
               mc.func_175599_af().func_181564_a(item, TransformType.NONE);
               GlStateManager.func_179101_C();
          }

          if (projectile.is3D() && projectile.glows()) {
               GlStateManager.func_179140_f();
          }

          GlStateManager.func_179101_C();
          GlStateManager.func_179121_F();
          GlStateManager.func_179145_e();
     }

     public void func_76986_a(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
          this.doRenderProjectile((EntityProjectile)par1Entity, par2, par4, par6, par8, par9);
     }

     protected ResourceLocation func_110779_a(EntityProjectile projectile) {
          return projectile.isArrow() ? field_110780_a : TextureMap.field_110575_b;
     }

     protected ResourceLocation func_110775_a(Entity par1Entity) {
          return this.func_110779_a((EntityProjectile)par1Entity);
     }
}
