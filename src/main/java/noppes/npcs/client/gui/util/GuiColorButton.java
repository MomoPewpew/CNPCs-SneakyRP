package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;

public class GuiColorButton extends GuiNpcButton {
     public int color;

     public GuiColorButton(int id, int x, int y, int color) {
          super(id, x, y, 50, 20, "");
          this.color = color;
     }

     public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
          if (this.field_146125_m) {
               func_73734_a(this.field_146128_h, this.field_146129_i, this.field_146128_h + 50, this.field_146129_i + 20, -16777216 + this.color);
          }
     }
}
