package noppes.npcs.client.gui.custom.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;

public class CustomGuiTexturedRect extends Gui implements IGuiComponent {
     GuiCustom parent;
     ResourceLocation texture;
     int id;
     int x;
     int y;
     int width;
     int height;
     int textureX;
     int textureY;
     float scale;
     String[] hoverText;

     public CustomGuiTexturedRect(int id, String texture, int x, int y, int width, int height) {
          this(id, texture, x, y, width, height, 0, 0);
     }

     public CustomGuiTexturedRect(int id, String texture, int x, int y, int width, int height, int textureX, int textureY) {
          this.scale = 1.0F;
          this.id = id;
          this.texture = new ResourceLocation(texture);
          this.x = GuiCustom.guiLeft + x;
          this.y = GuiCustom.guiTop + y;
          this.width = width;
          this.height = height;
          this.textureX = textureX;
          this.textureY = textureY;
     }

     public void setParent(GuiCustom parent) {
          this.parent = parent;
     }

     public int getID() {
          return this.id;
     }

     public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
          boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
          GlStateManager.func_179094_E();
          GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
          mc.func_110434_K().func_110577_a(this.texture);
          Tessellator tessellator = Tessellator.func_178181_a();
          BufferBuilder bufferbuilder = tessellator.func_178180_c();
          bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
          bufferbuilder.func_181662_b((double)this.x, (double)((float)this.y + (float)this.height * this.scale), (double)this.id).func_187315_a((double)((float)(this.textureX + 0) * 0.00390625F), (double)((float)(this.textureY + this.height) * 0.00390625F)).func_181675_d();
          bufferbuilder.func_181662_b((double)((float)this.x + (float)this.width * this.scale), (double)((float)this.y + (float)this.height * this.scale), (double)this.id).func_187315_a((double)((float)(this.textureX + this.width) * 0.00390625F), (double)((float)(this.textureY + this.height) * 0.00390625F)).func_181675_d();
          bufferbuilder.func_181662_b((double)((float)this.x + (float)this.width * this.scale), (double)this.y, (double)this.id).func_187315_a((double)((float)(this.textureX + this.width) * 0.00390625F), (double)((float)(this.textureY + 0) * 0.00390625F)).func_181675_d();
          bufferbuilder.func_181662_b((double)this.x, (double)this.y, (double)this.id).func_187315_a((double)((float)(this.textureX + 0) * 0.00390625F), (double)((float)(this.textureY + 0) * 0.00390625F)).func_181675_d();
          tessellator.func_78381_a();
          if (hovered && this.hoverText != null && this.hoverText.length > 0) {
               this.parent.hoverText = this.hoverText;
          }

          GlStateManager.func_179121_F();
     }

     public ICustomGuiComponent toComponent() {
          CustomGuiTexturedRectWrapper component = new CustomGuiTexturedRectWrapper(this.id, this.texture.toString(), this.x, this.y, this.width, this.height, this.textureX, this.textureY);
          component.setHoverText(this.hoverText);
          component.setScale(this.scale);
          return component;
     }

     public static CustomGuiTexturedRect fromComponent(CustomGuiTexturedRectWrapper component) {
          CustomGuiTexturedRect rect;
          if (component.getTextureX() >= 0 && component.getTextureY() >= 0) {
               rect = new CustomGuiTexturedRect(component.getID(), component.getTexture(), component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), component.getTextureX(), component.getTextureY());
          } else {
               rect = new CustomGuiTexturedRect(component.getID(), component.getTexture(), component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight());
          }

          rect.scale = component.getScale();
          if (component.hasHoverText()) {
               rect.hoverText = component.getHoverText();
          }

          return rect;
     }
}
