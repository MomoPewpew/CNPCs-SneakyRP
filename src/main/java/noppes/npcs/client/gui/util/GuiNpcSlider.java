package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.NoppesStringUtils;
import org.lwjgl.input.Mouse;

public class GuiNpcSlider extends GuiButton {
     private ISliderListener listener;
     public int id;
     public float sliderValue;
     public boolean dragging;

     public GuiNpcSlider(GuiScreen parent, int id, int xPos, int yPos, String displayString, float sliderValue) {
          super(id, xPos, yPos, 150, 20, NoppesStringUtils.translate(displayString));
          this.sliderValue = 1.0F;
          this.id = id;
          this.sliderValue = sliderValue;
          if (parent instanceof ISliderListener) {
               this.listener = (ISliderListener)parent;
          }

     }

     public GuiNpcSlider(GuiScreen parent, int id, int xPos, int yPos, float sliderValue) {
          this(parent, id, xPos, yPos, "", sliderValue);
          if (this.listener != null) {
               this.listener.mouseDragged(this);
          }

     }

     public GuiNpcSlider(GuiScreen parent, int id, int xPos, int yPos, int width, int height, float sliderValue) {
          this(parent, id, xPos, yPos, "", sliderValue);
          this.width = width;
          this.height = height;
          if (this.listener != null) {
               this.listener.mouseDragged(this);
          }

     }

     public void func_146119_b(Minecraft mc, int par2, int par3) {
          if (this.visible) {
               mc.func_110434_K().bindTexture(field_146122_a);
               if (this.dragging) {
                    this.sliderValue = (float)(par2 - (this.x + 4)) / (float)(this.width - 8);
                    if (this.sliderValue < 0.0F) {
                         this.sliderValue = 0.0F;
                    }

                    if (this.sliderValue > 1.0F) {
                         this.sliderValue = 1.0F;
                    }

                    if (this.listener != null) {
                         this.listener.mouseDragged(this);
                    }

                    if (!Mouse.isButtonDown(0)) {
                         this.func_146118_a(0, 0);
                    }
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)), this.y, 0, 66, 4, 20);
               this.drawTexturedModalRect(this.x + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.y, 196, 66, 4, 20);
          }
     }

     public String getDisplayString() {
          return this.field_146126_j;
     }

     public void setString(String str) {
          this.field_146126_j = NoppesStringUtils.translate(str);
     }

     public boolean func_146116_c(Minecraft par1Minecraft, int par2, int par3) {
          if (this.enabled && this.visible && par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height) {
               this.sliderValue = (float)(par2 - (this.x + 4)) / (float)(this.width - 8);
               if (this.sliderValue < 0.0F) {
                    this.sliderValue = 0.0F;
               }

               if (this.sliderValue > 1.0F) {
                    this.sliderValue = 1.0F;
               }

               if (this.listener != null) {
                    this.listener.mousePressed(this);
               }

               this.dragging = true;
               return true;
          } else {
               return false;
          }
     }

     public void func_146118_a(int par1, int par2) {
          this.dragging = false;
          if (this.listener != null) {
               this.listener.mouseReleased(this);
          }

     }

     public int func_146114_a(boolean par1) {
          return 0;
     }
}
