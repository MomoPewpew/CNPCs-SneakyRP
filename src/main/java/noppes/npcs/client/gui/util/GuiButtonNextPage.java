package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonNextPage extends GuiNpcButton {
     private final boolean field_146151_o;
     private static final String __OBFID = "CL_00000745";
     private static final ResourceLocation field_110405_a = new ResourceLocation("textures/gui/book.png");

     public GuiButtonNextPage(int par1, int par2, int par3, boolean par4) {
          super(par1, par2, par3, 23, 13, "");
          this.field_146151_o = par4;
     }

     public void func_191745_a(Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_, float partialTicks) {
          if (this.visible) {
               boolean flag = p_146112_2_ >= this.x && p_146112_3_ >= this.y && p_146112_2_ < this.x + this.width && p_146112_3_ < this.y + this.height;
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               p_146112_1_.func_110434_K().bindTexture(field_110405_a);
               int k = 0;
               int l = 192;
               if (flag) {
                    k += 23;
               }

               if (!this.field_146151_o) {
                    l += 13;
               }

               this.drawTexturedModalRect(this.x, this.y, k, l, 23, 13);
          }

     }
}
