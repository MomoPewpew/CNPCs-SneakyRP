package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.NoppesStringUtils;
import org.lwjgl.input.Mouse;

public class GuiNpcSlider extends GuiButton {
     private ISliderListener listener;
     public int field_146127_k;
     public float sliderValue;
     public boolean dragging;

     public GuiNpcSlider(GuiScreen parent, int id, int xPos, int yPos, String displayString, float sliderValue) {
          super(id, xPos, yPos, 150, 20, NoppesStringUtils.translate(displayString));
          this.sliderValue = 1.0F;
          this.field_146127_k = id;
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
          this.field_146120_f = width;
          this.field_146121_g = height;
          if (this.listener != null) {
               this.listener.mouseDragged(this);
          }

     }

     public void func_146119_b(Minecraft mc, int par2, int par3) {
          if (this.field_146125_m) {
               mc.func_110434_K().func_110577_a(field_146122_a);
               if (this.dragging) {
                    this.sliderValue = (float)(par2 - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
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

               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               this.func_73729_b(this.field_146128_h + (int)(this.sliderValue * (float)(this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
               this.func_73729_b(this.field_146128_h + (int)(this.sliderValue * (float)(this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
          }
     }

     public String getDisplayString() {
          return this.field_146126_j;
     }

     public void setString(String str) {
          this.field_146126_j = NoppesStringUtils.translate(str);
     }

     public boolean func_146116_c(Minecraft par1Minecraft, int par2, int par3) {
          if (this.field_146124_l && this.field_146125_m && par2 >= this.field_146128_h && par3 >= this.field_146129_i && par2 < this.field_146128_h + this.field_146120_f && par3 < this.field_146129_i + this.field_146121_g) {
               this.sliderValue = (float)(par2 - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
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
