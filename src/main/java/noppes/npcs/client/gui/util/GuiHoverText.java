package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiHoverText extends GuiScreen {
     private int x;
     private int y;
     public int id;
     protected static final ResourceLocation buttonTextures = new ResourceLocation("customnpcs:textures/gui/info.png");
     private String text;

     public GuiHoverText(int id, String text, int x, int y) {
          this.text = text;
          this.id = id;
          this.x = x;
          this.y = y;
     }

     public void func_73863_a(int par1, int par2, float par3) {
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.func_110434_K().bindTexture(buttonTextures);
          this.drawTexturedModalRect(this.x, this.y, 0, 0, 12, 12);
          if (this.inArea(this.x, this.y, 12, 12, par1, par2)) {
               List lines = new ArrayList();
               lines.add(this.text);
               this.drawHoveringText(lines, this.x + 8, this.y + 6, this.field_146289_q);
               GlStateManager.disableLighting();
          }

     }

     public boolean inArea(int x, int y, int width, int height, int mouseX, int mouseY) {
          return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
     }
}
