package noppes.npcs.config;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.util.LRUHashMap;

public class TrueTypeFont {
     private static final int MaxWidth = 512;
     private static final List allFonts = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
     private List usedFonts = new ArrayList();
     private LinkedHashMap textcache = new LRUHashMap(100);
     private Map glyphcache = new HashMap();
     private List textures = new ArrayList();
     private Font font;
     private int lineHeight = 1;
     private Graphics2D globalG = (Graphics2D)(new BufferedImage(1, 1, 2)).getGraphics();
     public float scale = 1.0F;
     private int specialChar = 167;

     public TrueTypeFont(Font font, float scale) {
          this.font = font;
          this.scale = scale;
          this.globalG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          this.lineHeight = this.globalG.getFontMetrics(font).getHeight();
     }

     public TrueTypeFont(ResourceLocation resource, int fontSize, float scale) throws IOException, FontFormatException {
          InputStream stream = Minecraft.getMinecraft().func_110442_L().func_110536_a(resource).func_110527_b();
          GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
          Font font = Font.createFont(0, stream);
          ge.registerFont(font);
          this.font = font.deriveFont(0, (float)fontSize);
          this.scale = scale;
          this.globalG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          this.lineHeight = this.globalG.getFontMetrics(font).getHeight();
     }

     public void setSpecial(char c) {
          this.specialChar = c;
     }

     public void draw(String text, float x, float y, int color) {
          TrueTypeFont.GlyphCache cache = this.getOrCreateCache(text);
          float r = (float)(color >> 16 & 255) / 255.0F;
          float g = (float)(color >> 8 & 255) / 255.0F;
          float b = (float)(color & 255) / 255.0F;
          GlStateManager.color(r, g, b, 1.0F);
          GlStateManager.func_179147_l();
          GlStateManager.func_179094_E();
          GlStateManager.func_179109_b(x, y, 0.0F);
          GlStateManager.func_179152_a(this.scale, this.scale, 1.0F);
          float i = 0.0F;
          Iterator var10 = cache.glyphs.iterator();

          while(var10.hasNext()) {
               TrueTypeFont.Glyph gl = (TrueTypeFont.Glyph)var10.next();
               if (gl.type != TrueTypeFont.GlyphType.NORMAL) {
                    if (gl.type == TrueTypeFont.GlyphType.RESET) {
                         GlStateManager.color(r, g, b, 1.0F);
                    } else if (gl.type == TrueTypeFont.GlyphType.COLOR) {
                         GlStateManager.color((float)(gl.color >> 16 & 255) / 255.0F, (float)(gl.color >> 8 & 255) / 255.0F, (float)(gl.color & 255) / 255.0F, 1.0F);
                    }
               } else {
                    GlStateManager.func_179144_i(gl.texture);
                    this.drawTexturedModalRect(i, 0.0F, (float)gl.x * this.textureScale(), (float)gl.y * this.textureScale(), (float)gl.width * this.textureScale(), (float)gl.height * this.textureScale());
                    i += (float)gl.width * this.textureScale();
               }
          }

          GlStateManager.func_179084_k();
          GlStateManager.func_179121_F();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
     }

     private TrueTypeFont.GlyphCache getOrCreateCache(String text) {
          TrueTypeFont.GlyphCache cache = (TrueTypeFont.GlyphCache)this.textcache.get(text);
          if (cache != null) {
               return cache;
          } else {
               cache = new TrueTypeFont.GlyphCache();

               for(int i = 0; i < text.length(); ++i) {
                    char c = text.charAt(i);
                    if (c == this.specialChar && i + 1 < text.length()) {
                         char next = text.toLowerCase(Locale.ENGLISH).charAt(i + 1);
                         int index = "0123456789abcdefklmnor".indexOf(next);
                         if (index >= 0) {
                              TrueTypeFont.Glyph g = new TrueTypeFont.Glyph();
                              if (index < 16) {
                                   g.type = TrueTypeFont.GlyphType.COLOR;
                                   g.color = Minecraft.getMinecraft().fontRenderer.func_175064_b(next);
                              } else if (index == 16) {
                                   g.type = TrueTypeFont.GlyphType.RANDOM;
                              } else if (index == 17) {
                                   g.type = TrueTypeFont.GlyphType.BOLD;
                              } else if (index == 18) {
                                   g.type = TrueTypeFont.GlyphType.STRIKETHROUGH;
                              } else if (index == 19) {
                                   g.type = TrueTypeFont.GlyphType.UNDERLINE;
                              } else if (index == 20) {
                                   g.type = TrueTypeFont.GlyphType.ITALIC;
                              } else {
                                   g.type = TrueTypeFont.GlyphType.RESET;
                              }

                              cache.glyphs.add(g);
                              ++i;
                              continue;
                         }
                    }

                    TrueTypeFont.Glyph g = this.getOrCreateGlyph(c);
                    cache.glyphs.add(g);
                    cache.width += g.width;
                    cache.height = Math.max(cache.height, g.height);
               }

               this.textcache.put(text, cache);
               return cache;
          }
     }

     private TrueTypeFont.Glyph getOrCreateGlyph(char c) {
          TrueTypeFont.Glyph g = (TrueTypeFont.Glyph)this.glyphcache.get(c);
          if (g != null) {
               return g;
          } else {
               TrueTypeFont.TextureCache cache = this.getCurrentTexture();
               Font font = this.getFontForChar(c);
               FontMetrics metrics = this.globalG.getFontMetrics(font);
               g = new TrueTypeFont.Glyph();
               g.width = Math.max(metrics.charWidth(c), 1);
               g.height = Math.max(metrics.getHeight(), 1);
               if (cache.x + g.width >= 512) {
                    cache.x = 0;
                    cache.y += this.lineHeight + 1;
                    if (cache.y >= 512) {
                         cache.full = true;
                         cache = this.getCurrentTexture();
                    }
               }

               g.x = cache.x;
               g.y = cache.y;
               cache.x += g.width + 3;
               this.lineHeight = Math.max(this.lineHeight, g.height);
               cache.g.setFont(font);
               cache.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
               cache.g.drawString(c + "", g.x, g.y + metrics.getAscent());
               g.texture = cache.textureId;
               TextureUtil.func_110987_a(cache.textureId, cache.bufferedImage);
               this.glyphcache.put(c, g);
               return g;
          }
     }

     private TrueTypeFont.TextureCache getCurrentTexture() {
          TrueTypeFont.TextureCache cache = null;
          Iterator var2 = this.textures.iterator();

          while(var2.hasNext()) {
               TrueTypeFont.TextureCache t = (TrueTypeFont.TextureCache)var2.next();
               if (!t.full) {
                    cache = t;
                    break;
               }
          }

          if (cache == null) {
               this.textures.add(cache = new TrueTypeFont.TextureCache());
          }

          return cache;
     }

     public void drawCentered(String text, float x, float y, int color) {
          this.draw(text, x - (float)this.width(text) / 2.0F, y, color);
     }

     private Font getFontForChar(char c) {
          if (this.font.canDisplay(c)) {
               return this.font;
          } else {
               Iterator var2 = this.usedFonts.iterator();

               Font f;
               do {
                    if (!var2.hasNext()) {
                         Font fa = new Font("Arial Unicode MS", 0, this.font.getSize());
                         if (fa.canDisplay(c)) {
                              return fa;
                         }

                         Iterator var6 = allFonts.iterator();

                         Font f;
                         do {
                              if (!var6.hasNext()) {
                                   return this.font;
                              }

                              f = (Font)var6.next();
                         } while(!f.canDisplay(c));

                         this.usedFonts.add(f = f.deriveFont(0, (float)this.font.getSize()));
                         return f;
                    }

                    f = (Font)var2.next();
               } while(!f.canDisplay(c));

               return f;
          }
     }

     public void drawTexturedModalRect(float x, float y, float textureX, float textureY, float width, float height) {
          float f = 0.00390625F;
          float f1 = 0.00390625F;
          int zLevel = 0;
          BufferBuilder tessellator = Tessellator.func_178181_a().func_178180_c();
          tessellator.func_181668_a(7, DefaultVertexFormats.field_181707_g);
          tessellator.func_78914_f();
          tessellator.func_181662_b((double)x, (double)(y + height), (double)zLevel).func_187315_a((double)(textureX * f), (double)((textureY + height) * f1)).func_181675_d();
          tessellator.func_181662_b((double)(x + width), (double)(y + height), (double)zLevel).func_187315_a((double)((textureX + width) * f), (double)((textureY + height) * f1)).func_181675_d();
          tessellator.func_181662_b((double)(x + width), (double)y, (double)zLevel).func_187315_a((double)((textureX + width) * f), (double)(textureY * f1)).func_181675_d();
          tessellator.func_181662_b((double)x, (double)y, (double)zLevel).func_187315_a((double)(textureX * f), (double)(textureY * f1)).func_181675_d();
          Tessellator.func_178181_a().func_78381_a();
     }

     public int width(String text) {
          TrueTypeFont.GlyphCache cache = this.getOrCreateCache(text);
          return (int)((float)cache.width * this.scale * this.textureScale());
     }

     public int height(String text) {
          if (text != null && !text.trim().isEmpty()) {
               TrueTypeFont.GlyphCache cache = this.getOrCreateCache(text);
               return Math.max(1, (int)((float)cache.height * this.scale * this.textureScale()));
          } else {
               return (int)((float)this.lineHeight * this.scale * this.textureScale());
          }
     }

     private float textureScale() {
          return 0.5F;
     }

     public void dispose() {
          Iterator var1 = this.textures.iterator();

          while(var1.hasNext()) {
               TrueTypeFont.TextureCache cache = (TrueTypeFont.TextureCache)var1.next();
               GlStateManager.func_179150_h(cache.textureId);
          }

          this.textcache.clear();
     }

     public String getFontName() {
          return this.font.getFontName();
     }

     class GlyphCache {
          public int width;
          public int height;
          List glyphs = new ArrayList();
     }

     class Glyph {
          TrueTypeFont.GlyphType type;
          int color;
          int x;
          int y;
          int height;
          int width;
          int texture;

          Glyph() {
               this.type = TrueTypeFont.GlyphType.NORMAL;
               this.color = -1;
          }
     }

     class TextureCache {
          int x;
          int y;
          int textureId = GlStateManager.func_179146_y();
          BufferedImage bufferedImage = new BufferedImage(512, 512, 2);
          Graphics2D g;
          boolean full;

          TextureCache() {
               this.g = (Graphics2D)this.bufferedImage.getGraphics();
          }
     }

     static enum GlyphType {
          NORMAL,
          COLOR,
          RANDOM,
          BOLD,
          STRIKETHROUGH,
          UNDERLINE,
          ITALIC,
          RESET,
          OTHER;
     }
}
