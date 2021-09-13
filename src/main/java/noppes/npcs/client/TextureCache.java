package noppes.npcs.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.LogWriter;

public class TextureCache extends SimpleTexture {
     private BufferedImage bufferedImage;
     private boolean textureUploaded;

     public TextureCache(ResourceLocation location) {
          super(location);
     }

     public int func_110552_b() {
          this.checkTextureUploaded();
          return super.func_110552_b();
     }

     private void checkTextureUploaded() {
          if (!this.textureUploaded && this.bufferedImage != null) {
               if (this.field_110568_b != null && this.field_110553_a != -1) {
                    TextureUtil.func_147942_a(this.field_110553_a);
                    this.field_110553_a = -1;
               }

               TextureUtil.func_110987_a(super.func_110552_b(), this.bufferedImage);
               this.textureUploaded = true;
          }

     }

     public void setImage(ResourceLocation location) {
          try {
               IResourceManager manager = Minecraft.func_71410_x().func_110442_L();
               BufferedImage bufferedimage = ImageIO.read(manager.func_110536_a(location).func_110527_b());
               int i = bufferedimage.getWidth();
               int j = bufferedimage.getHeight();
               this.bufferedImage = new BufferedImage(i * 4, j * 2, 1);
               Graphics g = this.bufferedImage.getGraphics();
               g.drawImage(bufferedimage, 0, 0, (ImageObserver)null);
               g.drawImage(bufferedimage, i, 0, (ImageObserver)null);
               g.drawImage(bufferedimage, i * 2, 0, (ImageObserver)null);
               g.drawImage(bufferedimage, i * 3, 0, (ImageObserver)null);
               g.drawImage(bufferedimage, 0, i, (ImageObserver)null);
               g.drawImage(bufferedimage, i, j, (ImageObserver)null);
               g.drawImage(bufferedimage, i * 2, j, (ImageObserver)null);
               g.drawImage(bufferedimage, i * 3, j, (ImageObserver)null);
               this.textureUploaded = false;
          } catch (Exception var7) {
               LogWriter.error("Failed caching texture: " + location, var7);
          }

     }

     public void func_110551_a(IResourceManager resourceManager) throws IOException {
     }
}
