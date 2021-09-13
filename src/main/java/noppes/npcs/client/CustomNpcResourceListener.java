package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.CustomNpcs;

public class CustomNpcResourceListener implements IResourceManagerReloadListener {
     public static int DefaultTextColor = 4210752;

     public void func_110549_a(IResourceManager var1) {
          if (var1 instanceof SimpleReloadableResourceManager) {
               this.createTextureCache();
               SimpleReloadableResourceManager simplemanager = (SimpleReloadableResourceManager)var1;
               FolderResourcePack pack = new FolderResourcePack(CustomNpcs.Dir);
               simplemanager.func_110545_a(pack);

               try {
                    DefaultTextColor = Integer.parseInt(I18n.translateToLocal("customnpcs.defaultTextColor"), 16);
               } catch (NumberFormatException var5) {
                    DefaultTextColor = 4210752;
               }
          }

     }

     private void createTextureCache() {
          this.enlargeTexture("planks_oak");
          this.enlargeTexture("planks_big_oak");
          this.enlargeTexture("planks_birch");
          this.enlargeTexture("planks_jungle");
          this.enlargeTexture("planks_spruce");
          this.enlargeTexture("planks_acacia");
          this.enlargeTexture("iron_block");
          this.enlargeTexture("diamond_block");
          this.enlargeTexture("stone");
          this.enlargeTexture("gold_block");
          this.enlargeTexture("wool_colored_white");
     }

     private void enlargeTexture(String texture) {
          TextureManager manager = Minecraft.getMinecraft().func_110434_K();
          if (manager != null) {
               ResourceLocation location = new ResourceLocation("customnpcs:textures/cache/" + texture + ".png");
               ITextureObject ob = manager.func_110581_b(location);
               if (ob == null || !(ob instanceof TextureCache)) {
                    ob = new TextureCache(location);
                    manager.func_110579_a(location, (ITextureObject)ob);
               }

               ((TextureCache)ob).setImage(new ResourceLocation("textures/blocks/" + texture + ".png"));
          }
     }
}
