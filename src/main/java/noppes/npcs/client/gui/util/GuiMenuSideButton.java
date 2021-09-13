package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMenuSideButton extends GuiNpcButton {
     public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/menusidebutton.png");
     public boolean active;

     public GuiMenuSideButton(int i, int j, int k, String s) {
          this(i, j, k, 200, 20, s);
     }

     public GuiMenuSideButton(int i, int j, int k, int l, int i1, String s) {
          super(i, j, k, l, i1, s);
          this.active = false;
     }

     public int func_146114_a(boolean flag) {
          return this.active ? 0 : 1;
     }

     public void func_191745_a(Minecraft minecraft, int i, int j, float partialTicks) {
          if (this.field_146125_m) {
               FontRenderer fontrenderer = minecraft.field_71466_p;
               minecraft.field_71446_o.func_110577_a(resource);
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               int width = this.field_146120_f + (this.active ? 2 : 0);
               this.field_146123_n = i >= this.field_146128_h && j >= this.field_146129_i && i < this.field_146128_h + width && j < this.field_146129_i + this.field_146121_g;
               int k = this.func_146114_a(this.field_146123_n);
               this.func_73729_b(this.field_146128_h, this.field_146129_i, 0, k * 22, width, this.field_146121_g);
               this.func_146119_b(minecraft, i, j);
               String text = "";
               float maxWidth = (float)width * 0.75F;
               if ((float)fontrenderer.func_78256_a(this.field_146126_j) > maxWidth) {
                    for(int h = 0; h < this.field_146126_j.length(); ++h) {
                         char c = this.field_146126_j.charAt(h);
                         if ((float)fontrenderer.func_78256_a(text + c) > maxWidth) {
                              break;
                         }

                         text = text + c;
                    }

                    text = text + "...";
               } else {
                    text = this.field_146126_j;
               }

               if (this.active) {
                    this.func_73732_a(fontrenderer, text, this.field_146128_h + width / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, 16777120);
               } else if (this.field_146123_n) {
                    this.func_73732_a(fontrenderer, text, this.field_146128_h + width / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, 16777120);
               } else {
                    this.func_73732_a(fontrenderer, text, this.field_146128_h + width / 2, this.field_146129_i + (this.field_146121_g - 8) / 2, 14737632);
               }

          }
     }

     protected void func_146119_b(Minecraft minecraft, int i, int j) {
     }

     public void func_146118_a(int i, int j) {
     }

     public boolean func_146116_c(Minecraft minecraft, int i, int j) {
          return !this.active && this.field_146125_m && this.field_146123_n;
     }
}
