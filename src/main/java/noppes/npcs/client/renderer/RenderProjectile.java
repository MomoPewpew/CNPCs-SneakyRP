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
     private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

     public RenderProjectile() {
          super(Minecraft.getMinecraft().getRenderManager());
     }

     public void doRenderProjectile(EntityProjectile projectile, double x, double y, double z, float entityYaw, float partialTicks) {
          Minecraft mc = Minecraft.getMinecraft();
          GlStateManager.pushMatrix();
          GlStateManager.translate((float)x, (float)y, (float)z);
          GlStateManager.enableRescaleNormal();
          float scale = (float)projectile.getSize() / 10.0F;
          ItemStack item = projectile.getItemDisplay();
          GlStateManager.scale(scale, scale, scale);
          if (projectile.isArrow()) {
               this.bindEntityTexture(projectile);
               GlStateManager.rotate(projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.rotate(projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
               Tessellator tessellator = Tessellator.getInstance();
               BufferBuilder BufferBuilder = tessellator.getBuffer();
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
               GlStateManager.enableRescaleNormal();
               float f9 = (float)projectile.arrowShake - partialTicks;
               if (f9 > 0.0F) {
                    float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
                    GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
               }

               GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.scale(f8, f8, f8);
               GlStateManager.translate(-4.0F, 0.0F, 0.0F);
               if (this.renderOutlines) {
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(this.getTeamColor(projectile));
               }

               GlStateManager.glNormal3f(f8, 0.0F, 0.0F);
               BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
               BufferBuilder.pos(-7.0D, -2.0D, -2.0D).tex((double)f4, (double)f6).endVertex();
               BufferBuilder.pos(-7.0D, -2.0D, 2.0D).tex((double)f5, (double)f6).endVertex();
               BufferBuilder.pos(-7.0D, 2.0D, 2.0D).tex((double)f5, (double)f7).endVertex();
               BufferBuilder.pos(-7.0D, 2.0D, -2.0D).tex((double)f4, (double)f7).endVertex();
               tessellator.draw();
               GlStateManager.glNormal3f(-f8, 0.0F, 0.0F);
               BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
               BufferBuilder.pos(-7.0D, 2.0D, -2.0D).tex((double)f4, (double)f6).endVertex();
               BufferBuilder.pos(-7.0D, 2.0D, 2.0D).tex((double)f5, (double)f6).endVertex();
               BufferBuilder.pos(-7.0D, -2.0D, 2.0D).tex((double)f5, (double)f7).endVertex();
               BufferBuilder.pos(-7.0D, -2.0D, -2.0D).tex((double)f4, (double)f7).endVertex();
               tessellator.draw();

               for(int j = 0; j < 4; ++j) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.glNormal3f(0.0F, 0.0F, f8);
                    BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    BufferBuilder.pos(-8.0D, -2.0D, 0.0D).tex((double)f, (double)f2).endVertex();
                    BufferBuilder.pos(8.0D, -2.0D, 0.0D).tex((double)f1, (double)f2).endVertex();
                    BufferBuilder.pos(8.0D, 2.0D, 0.0D).tex((double)f1, (double)f3).endVertex();
                    BufferBuilder.pos(-8.0D, 2.0D, 0.0D).tex((double)f, (double)f3).endVertex();
                    tessellator.draw();
               }

               if (this.renderOutlines) {
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
               }
          } else if (projectile.is3D()) {
               GlStateManager.rotate(projectile.prevRotationYaw + (projectile.rotationYaw - projectile.prevRotationYaw) * partialTicks - 180.0F, 0.0F, 1.0F, 0.0F);
               GlStateManager.rotate(projectile.prevRotationPitch + (projectile.rotationPitch - projectile.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
               GlStateManager.translate(0.0D, -0.125D, 0.25D);
               if (item.getItem() instanceof ItemBlock && Block.getBlockFromItem(item.getItem()).getDefaultState().getRenderType() == EnumBlockRenderType.ENTITYBLOCK_ANIMATED) {
                    GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                    GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                    float f8 = 0.375F;
                    GlStateManager.scale(-f8, -f8, f8);
               }

               mc.getRenderItem().renderItem(item, TransformType.THIRD_PERSON_RIGHT_HAND);
          } else {
               GlStateManager.enableRescaleNormal();
               GlStateManager.scale(0.5F, 0.5F, 0.5F);
               GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
               GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
               this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
               mc.getRenderItem().renderItem(item, TransformType.NONE);
               GlStateManager.disableRescaleNormal();
          }

          if (projectile.is3D() && projectile.glows()) {
               GlStateManager.disableLighting();
          }

          GlStateManager.disableRescaleNormal();
          GlStateManager.popMatrix();
          GlStateManager.enableLighting();
     }

     public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
          this.doRenderProjectile((EntityProjectile)par1Entity, par2, par4, par6, par8, par9);
     }

     protected ResourceLocation func_110779_a(EntityProjectile projectile) {
          return projectile.isArrow() ? field_110780_a : TextureMap.LOCATION_BLOCKS_TEXTURE;
     }

     protected ResourceLocation getEntityTexture(Entity par1Entity) {
          return this.func_110779_a((EntityProjectile)par1Entity);
     }
}
