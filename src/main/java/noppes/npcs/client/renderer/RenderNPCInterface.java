package noppes.npcs.client.renderer;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.io.File;
import java.security.MessageDigest;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ImageDownloadAlt;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;

public class RenderNPCInterface extends RenderLiving {
     public static int LastTextureTick;

     public RenderNPCInterface(ModelBase model, float f) {
          super(Minecraft.getMinecraft().func_175598_ae(), model, f);
     }

     public void renderName(EntityNPCInterface npc, double d, double d1, double d2) {
          if (npc != null && this.func_177070_b(npc) && this.field_76990_c.field_78734_h != null) {
               double d0 = npc.func_70068_e(this.field_76990_c.field_78734_h);
               if (d0 <= 512.0D) {
                    float scale;
                    if (npc.messages != null) {
                         scale = npc.baseHeight / 5.0F * (float)npc.display.getSize();
                         float offset = npc.height * (1.2F + (!npc.display.showName() ? 0.0F : (npc.display.getTitle().isEmpty() ? 0.15F : 0.25F)));
                         npc.messages.renderMessages(d, d1 + (double)offset, d2, 0.666667F * scale, npc.isInRange(this.field_76990_c.field_78734_h, 4.0D));
                    }

                    scale = npc.baseHeight / 5.0F * (float)npc.display.getSize();
                    if (npc.display.showName()) {
                         this.renderLivingLabel(npc, (float)d, (float)d1 + npc.height - 0.06F * scale, (float)d2, 64, npc.getName(), npc.display.getTitle());
                    }

               }
          }
     }

     public void func_76979_b(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
          EntityNPCInterface npc = (EntityNPCInterface)par1Entity;
          this.field_76989_e = npc.field_70130_N;
          if (!npc.isKilled()) {
               super.func_76979_b(par1Entity, par2, par4, par6, par8, par9);
          }

     }

     protected void renderLivingLabel(EntityNPCInterface npc, float d, float d1, float d2, int i, String name, String title) {
          FontRenderer fontrenderer = this.func_76983_a();
          float f1 = npc.baseHeight / 5.0F * (float)npc.display.getSize();
          float f2 = 0.01666667F * f1;
          GlStateManager.func_179094_E();
          GlStateManager.translate(d, d1, d2);
          GL11.glNormal3f(0.0F, 1.0F, 0.0F);
          GlStateManager.func_179114_b(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
          GlStateManager.func_179114_b(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
          float height = f1 / 6.5F * 2.0F;
          int color = npc.getFaction().color;
          GlStateManager.disableLighting();
          GlStateManager.func_179132_a(false);
          GlStateManager.translate(0.0F, height, 0.0F);
          GlStateManager.func_179147_l();
          GlStateManager.func_187428_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
          if (!title.isEmpty()) {
               title = "<" + title + ">";
               float f3 = 0.01666667F * f1 * 0.6F;
               GlStateManager.translate(0.0F, -f1 / 6.5F * 0.4F, 0.0F);
               GlStateManager.func_179152_a(-f3, -f3, f3);
               fontrenderer.func_78276_b(title, -fontrenderer.getStringWidth(title) / 2, 0, color);
               GlStateManager.func_179152_a(1.0F / -f3, 1.0F / -f3, 1.0F / f3);
               GlStateManager.translate(0.0F, f1 / 6.5F * 0.85F, 0.0F);
          }

          GlStateManager.func_179152_a(-f2, -f2, f2);
          if (npc.isInRange(this.field_76990_c.field_78734_h, 4.0D)) {
               GlStateManager.disableDepth();
               fontrenderer.func_78276_b(name, -fontrenderer.getStringWidth(name) / 2, 0, color + 1426063360);
               GlStateManager.enableDepth();
          }

          GlStateManager.func_179132_a(true);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          fontrenderer.func_78276_b(name, -fontrenderer.getStringWidth(name) / 2, 0, color);
          GlStateManager.enableLighting();
          GlStateManager.func_179084_k();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.func_179121_F();
     }

     protected void renderColor(EntityNPCInterface npc) {
          if (npc.field_70737_aN <= 0 && npc.field_70725_aQ <= 0) {
               float red = (float)(npc.display.getTint() >> 16 & 255) / 255.0F;
               float green = (float)(npc.display.getTint() >> 8 & 255) / 255.0F;
               float blue = (float)(npc.display.getTint() & 255) / 255.0F;
               GlStateManager.color(red, green, blue, 1.0F);
          }

     }

     private void renderLiving(EntityNPCInterface npc, double d, double d1, double d2, float xoffset, float yoffset, float zoffset) {
     }

     protected void applyRotations(EntityNPCInterface npc, float f, float f1, float f2) {
          if (npc.func_70089_S() && npc.func_70608_bn()) {
               GlStateManager.func_179114_b((float)npc.ais.orientation, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179114_b(this.func_77037_a(npc), 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179114_b(270.0F, 0.0F, 1.0F, 0.0F);
          } else if (npc.func_70089_S() && npc.currentAnimation == 7) {
               GlStateManager.func_179114_b(270.0F - f1, 0.0F, 1.0F, 0.0F);
               float scale = (float)((EntityCustomNpc)npc).display.getSize() / 5.0F;
               GlStateManager.translate(-scale + ((EntityCustomNpc)npc).modelData.getLegsY() * scale, 0.14F, 0.0F);
               GlStateManager.func_179114_b(270.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179114_b(270.0F, 0.0F, 1.0F, 0.0F);
          } else {
               super.func_77043_a(npc, f, f1, f2);
          }

     }

     protected void preRenderCallback(EntityNPCInterface npc, float f) {
          this.renderColor(npc);
          int size = npc.display.getSize();
          GlStateManager.func_179152_a(npc.scaleX / 5.0F * (float)size, npc.scaleY / 5.0F * (float)size, npc.scaleZ / 5.0F * (float)size);
     }

     public void doRender(EntityNPCInterface npc, double d, double d1, double d2, float f, float f1) {
          if (!npc.isKilled() || !npc.stats.hideKilledBody || npc.field_70725_aQ <= 20) {
               if ((npc.display.getBossbar() == 1 || npc.display.getBossbar() == 2 && npc.isAttacking()) && !npc.isKilled() && npc.field_70725_aQ <= 20 && npc.canSee(Minecraft.getMinecraft().player)) {
               }

               if (npc.ais.getStandingType() == 3 && !npc.isWalking() && !npc.isInteracting()) {
                    npc.field_70760_ar = npc.field_70761_aq = (float)npc.ais.orientation;
               }

               super.func_76986_a(npc, d, d1, d2, f, f1);
          }
     }

     protected void renderModel(EntityNPCInterface npc, float par2, float par3, float par4, float par5, float par6, float par7) {
          super.renderModel(npc, par2, par3, par4, par5, par6, par7);
          if (!npc.display.getOverlayTexture().isEmpty()) {
               GlStateManager.func_179143_c(515);
               if (npc.textureGlowLocation == null) {
                    npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
               }

               this.func_110776_a(npc.textureGlowLocation);
               float f1 = 1.0F;
               GlStateManager.func_179147_l();
               GlStateManager.func_179112_b(1, 1);
               GlStateManager.disableLighting();
               if (npc.func_82150_aj()) {
                    GlStateManager.func_179132_a(false);
               } else {
                    GlStateManager.func_179132_a(true);
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179094_E();
               GlStateManager.func_179152_a(1.001F, 1.001F, 1.001F);
               this.field_77045_g.func_78088_a(npc, par2, par3, par4, par5, par6, par7);
               GlStateManager.func_179121_F();
               GlStateManager.enableLighting();
               GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
               GlStateManager.func_179143_c(515);
               GlStateManager.func_179084_k();
          }

     }

     protected float handleRotationFloat(EntityNPCInterface npc, float par2) {
          return !npc.isKilled() && npc.display.getHasLivingAnimation() ? super.handleRotationFloat(npc, par2) : 0.0F;
     }

     protected void renderLivingAt(EntityNPCInterface npc, double d, double d1, double d2) {
          this.field_76989_e = (float)npc.display.getSize() / 10.0F;
          float xOffset = 0.0F;
          float yOffset = npc.currentAnimation == 0 ? npc.ais.bodyOffsetY / 10.0F - 0.5F : 0.0F;
          float zOffset = 0.0F;
          if (npc.func_70089_S()) {
               if (npc.func_70608_bn()) {
                    xOffset = (float)(-Math.cos(Math.toRadians((double)(180 - npc.ais.orientation))));
                    zOffset = (float)(-Math.sin(Math.toRadians((double)npc.ais.orientation)));
                    yOffset += 0.14F;
               } else if (npc.currentAnimation == 1 || npc.func_184218_aH()) {
                    yOffset -= 0.5F - ((EntityCustomNpc)npc).modelData.getLegsY() * 0.8F;
               }
          }

          xOffset = xOffset / 5.0F * (float)npc.display.getSize();
          yOffset = yOffset / 5.0F * (float)npc.display.getSize();
          zOffset = zOffset / 5.0F * (float)npc.display.getSize();
          super.func_77039_a(npc, d + (double)xOffset, d1 + (double)yOffset, d2 + (double)zOffset);
     }

     public ResourceLocation getEntityTexture(EntityNPCInterface npc) {
          if (npc.textureLocation == null) {
               if (npc.display.skinType == 0) {
                    npc.textureLocation = new ResourceLocation(npc.display.getSkinTexture());
               } else {
                    if (LastTextureTick < 5) {
                         return DefaultPlayerSkin.func_177335_a();
                    }

                    if (npc.display.skinType == 1 && npc.display.playerProfile != null) {
                         Minecraft minecraft = Minecraft.getMinecraft();
                         Map map = minecraft.func_152342_ad().func_152788_a(npc.display.playerProfile);
                         if (map.containsKey(Type.SKIN)) {
                              npc.textureLocation = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
                         }
                    } else if (npc.display.skinType == 2) {
                         try {
                              MessageDigest digest = MessageDigest.getInstance("MD5");
                              byte[] hash = digest.digest(npc.display.getSkinUrl().getBytes("UTF-8"));
                              StringBuilder sb = new StringBuilder(2 * hash.length);
                              byte[] var5 = hash;
                              int var6 = hash.length;

                              for(int var7 = 0; var7 < var6; ++var7) {
                                   byte b = var5[var7];
                                   sb.append(String.format("%02x", b & 255));
                              }

                              npc.textureLocation = new ResourceLocation("skins/" + sb.toString());
                              this.loadSkin((File)null, npc.textureLocation, npc.display.getSkinUrl());
                         } catch (Exception var9) {
                         }
                    }
               }
          }

          return npc.textureLocation == null ? DefaultPlayerSkin.func_177335_a() : npc.textureLocation;
     }

     private void loadSkin(File file, ResourceLocation resource, String par1Str) {
          TextureManager texturemanager = Minecraft.getMinecraft().func_110434_K();
          if (texturemanager.func_110581_b(resource) == null) {
               ITextureObject object = new ImageDownloadAlt(file, par1Str, DefaultPlayerSkin.func_177335_a(), new ImageBufferDownloadAlt());
               texturemanager.func_110579_a(resource, object);
          }
     }
}
