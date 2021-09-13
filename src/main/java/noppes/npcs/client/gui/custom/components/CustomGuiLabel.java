package noppes.npcs.client.gui.custom.components;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;

public class CustomGuiLabel extends GuiLabel implements IGuiComponent {
     GuiCustom parent;
     String fullLabel;
     int colour;
     String[] hoverText;
     float scale;

     public CustomGuiLabel(String label, int id, int x, int y, int width, int height) {
          this(label, id, x, y, width, height, 16777215);
     }

     public CustomGuiLabel(String label, int id, int x, int y, int width, int height, int colour) {
          int var10003 = GuiCustom.guiLeft + x;
          int var10004 = GuiCustom.guiTop + y;
          super(Minecraft.func_71410_x().fontRenderer, id, var10003, var10004, width, height, colour);
          this.scale = 1.0F;
          this.fullLabel = label;
          this.colour = colour;
          FontRenderer fontRenderer = Minecraft.func_71410_x().fontRenderer;
          fontRenderer.func_78256_a(label);
          Iterator var10 = fontRenderer.func_78271_c(label, width).iterator();

          while(var10.hasNext()) {
               String s = (String)var10.next();
               this.func_175202_a(s);
          }

     }

     public void setParent(GuiCustom parent) {
          this.parent = parent;
     }

     public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
          GlStateManager.func_179094_E();
          GlStateManager.func_179109_b(0.0F, 0.0F, (float)this.field_175204_i);
          boolean hovered = mouseX >= this.field_146162_g && mouseY >= this.field_146174_h && mouseX < this.field_146162_g + this.field_146167_a && mouseY < this.field_146174_h + this.field_146161_f;
          this.func_146159_a(mc, mouseX, mouseY);
          if (hovered && this.hoverText != null && this.hoverText.length > 0) {
               this.parent.hoverText = this.hoverText;
          }

          GlStateManager.func_179121_F();
     }

     public int getID() {
          return this.field_175204_i;
     }

     public void setScale(float scale) {
          this.scale = scale;
     }

     public static CustomGuiLabel fromComponent(CustomGuiLabelWrapper component) {
          CustomGuiLabel lbl = new CustomGuiLabel(component.getText(), component.getID(), component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), component.getColor());
          lbl.setScale(component.getScale());
          if (component.hasHoverText()) {
               lbl.hoverText = component.getHoverText();
          }

          return lbl;
     }

     public ICustomGuiComponent toComponent() {
          CustomGuiLabelWrapper component = new CustomGuiLabelWrapper(this.field_175204_i, this.fullLabel, this.field_146162_g, this.field_146174_h, this.field_146167_a, this.field_146161_f, this.colour);
          component.setHoverText(this.hoverText);
          return component;
     }
}
