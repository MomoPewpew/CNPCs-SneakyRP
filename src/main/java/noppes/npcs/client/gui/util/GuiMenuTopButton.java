package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class GuiMenuTopButton extends GuiNpcButton {
     public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/menutopbutton.png");
     protected int height;
     public boolean active;
     public boolean hover;
     public boolean rotated;
     public IButtonListener listener;

     public GuiMenuTopButton(int i, int j, int k, String s) {
          super(i, j, k, I18n.func_74838_a(s));
          this.hover = false;
          this.rotated = false;
          this.active = false;
          this.width = Minecraft.func_71410_x().fontRenderer.func_78256_a(this.field_146126_j) + 12;
          this.height = 20;
     }

     public GuiMenuTopButton(int i, GuiButton parent, String s) {
          this(i, parent.x + parent.width, parent.y, s);
     }

     public GuiMenuTopButton(int i, GuiButton parent, String s, IButtonListener listener) {
          this(i, parent, s);
          this.listener = listener;
     }

     public int func_146114_a(boolean flag) {
          byte byte0 = 1;
          if (this.active) {
               byte0 = 0;
          } else if (flag) {
               byte0 = 2;
          }

          return byte0;
     }

     public void func_191745_a(Minecraft minecraft, int i, int j, float partialTicks) {
          if (this.getVisible()) {
               GlStateManager.func_179094_E();
               minecraft.renderEngine.bindTexture(resource);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               int height = this.height - (this.active ? 0 : 2);
               this.hover = i >= this.x && j >= this.y && i < this.x + this.getWidth() && j < this.y + height;
               int k = this.func_146114_a(this.hover);
               this.drawTexturedModalRect(this.x, this.y, 0, k * 20, this.getWidth() / 2, height);
               this.drawTexturedModalRect(this.x + this.getWidth() / 2, this.y, 200 - this.getWidth() / 2, k * 20, this.getWidth() / 2, height);
               this.func_146119_b(minecraft, i, j);
               FontRenderer fontrenderer = minecraft.fontRenderer;
               if (this.rotated) {
                    GlStateManager.func_179114_b(90.0F, 1.0F, 0.0F, 0.0F);
               }

               if (this.active) {
                    this.func_73732_a(fontrenderer, this.field_146126_j, this.x + this.getWidth() / 2, this.y + (height - 8) / 2, 16777120);
               } else if (this.hover) {
                    this.func_73732_a(fontrenderer, this.field_146126_j, this.x + this.getWidth() / 2, this.y + (height - 8) / 2, 16777120);
               } else {
                    this.func_73732_a(fontrenderer, this.field_146126_j, this.x + this.getWidth() / 2, this.y + (height - 8) / 2, 14737632);
               }

               GlStateManager.func_179121_F();
          }
     }

     protected void func_146119_b(Minecraft minecraft, int i, int j) {
     }

     public void func_146118_a(int i, int j) {
     }

     public boolean func_146116_c(Minecraft minecraft, int i, int j) {
          boolean bo = !this.active && this.getVisible() && this.hover;
          if (bo && this.listener != null) {
               this.listener.actionPerformed(this);
               return false;
          } else {
               return bo;
          }
     }
}
