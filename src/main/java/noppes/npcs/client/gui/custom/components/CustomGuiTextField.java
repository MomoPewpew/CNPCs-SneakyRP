package noppes.npcs.client.gui.custom.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IClickListener;
import noppes.npcs.client.gui.custom.interfaces.IDataHolder;
import noppes.npcs.client.gui.custom.interfaces.IKeyListener;

public class CustomGuiTextField extends GuiTextField implements IDataHolder, IClickListener, IKeyListener {
     GuiCustom parent;
     String[] hoverText;

     public CustomGuiTextField(int id, int x, int y, int width, int height) {
          int var10003 = GuiCustom.guiLeft + x;
          int var10004 = GuiCustom.guiTop + y;
          super(id, Minecraft.func_71410_x().fontRenderer, var10003, var10004, width, height);
          this.func_146203_f(500);
     }

     public void keyTyped(char typedChar, int keyCode) {
          this.func_146201_a(typedChar, keyCode);
     }

     public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
          GlStateManager.func_179094_E();
          GlStateManager.func_179109_b(0.0F, 0.0F, (float)this.field_175208_g);
          boolean hovered = mouseX >= this.field_146209_f && mouseY >= this.field_146210_g && mouseX < this.field_146209_f + this.field_146218_h && mouseY < this.field_146210_g + this.field_146219_i;
          this.func_146194_f();
          if (hovered && this.hoverText != null && this.hoverText.length > 0) {
               this.parent.hoverText = this.hoverText;
          }

          GlStateManager.func_179121_F();
     }

     public void setParent(GuiCustom parent) {
          this.parent = parent;
     }

     public int getID() {
          return this.field_175208_g;
     }

     public NBTTagCompound toNBT() {
          NBTTagCompound tag = new NBTTagCompound();
          tag.func_74768_a("id", this.field_175208_g);
          tag.func_74778_a("text", this.field_146216_j);
          return tag;
     }

     public ICustomGuiComponent toComponent() {
          CustomGuiTextFieldWrapper component = new CustomGuiTextFieldWrapper(this.field_175208_g, this.field_146209_f - GuiCustom.guiLeft, this.field_146210_g - GuiCustom.guiTop, this.field_146218_h, this.field_146219_i);
          component.setText(this.func_146179_b());
          component.setHoverText(this.hoverText);
          return component;
     }

     public static CustomGuiTextField fromComponent(CustomGuiTextFieldWrapper component) {
          CustomGuiTextField txt = new CustomGuiTextField(component.getID(), component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight());
          if (component.hasHoverText()) {
               txt.hoverText = component.getHoverText();
          }

          if (component.getText() != null && !component.getText().isEmpty()) {
               txt.field_146216_j = component.getText();
          }

          return txt;
     }

     public boolean mouseClicked(GuiCustom gui, int mouseX, int mouseY, int mouseButton) {
          return this.func_146192_a(mouseX, mouseY, mouseButton);
     }
}
