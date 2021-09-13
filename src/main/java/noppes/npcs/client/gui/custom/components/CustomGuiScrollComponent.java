package noppes.npcs.client.gui.custom.components;

import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IClickListener;
import noppes.npcs.client.gui.custom.interfaces.IDataHolder;
import noppes.npcs.client.gui.util.GuiCustomScroll;

public class CustomGuiScrollComponent extends GuiCustomScroll implements IDataHolder, IClickListener {
     GuiCustom parent;
     String[] hoverText;
     public boolean multiSelect = false;

     public CustomGuiScrollComponent(Minecraft mc, GuiScreen parent, int id) {
          super(parent, id);
          this.field_146297_k = mc;
          this.field_146289_q = mc.fontRenderer;
     }

     public CustomGuiScrollComponent(Minecraft mc, GuiScreen parent, int id, boolean multiSelect) {
          super(parent, id, multiSelect);
          this.field_146297_k = mc;
          this.field_146289_q = mc.fontRenderer;
          this.multiSelect = multiSelect;
     }

     public void setParent(GuiCustom parent) {
          this.parent = parent;
     }

     public int getID() {
          return this.id;
     }

     public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
          GlStateManager.func_179094_E();
          GlStateManager.translate(0.0F, 0.0F, (float)this.id);
          boolean hovered = mouseX >= this.guiLeft && mouseY >= this.guiTop && mouseX < this.guiLeft + this.getXSize() && mouseY < this.guiTop + this.getYSize();
          super.drawScreen(mouseX, mouseY, partialTicks, mouseWheel);
          if (hovered && this.hoverText != null && this.hoverText.length > 0) {
               this.parent.hoverText = this.hoverText;
          }

          GlStateManager.func_179121_F();
     }

     public boolean mouseClicked(GuiCustom gui, int mouseX, int mouseY, int mouseButton) {
          super.func_73864_a(mouseX, mouseY, mouseButton);
          return this.isMouseOver(mouseX, mouseY);
     }

     public void fromComponent(CustomGuiScrollWrapper component) {
          this.guiLeft = GuiCustom.guiLeft + component.getPosX();
          this.guiTop = GuiCustom.guiTop + component.getPosY();
          this.setSize(component.getWidth(), component.getHeight());
          this.setUnsortedList(Arrays.asList(component.getList()));
          if (component.getDefaultSelection() >= 0) {
               int defaultSelect = component.getDefaultSelection();
               if (defaultSelect < this.getList().size()) {
                    this.selected = defaultSelect;
               }
          }

          if (component.hasHoverText()) {
               this.hoverText = component.getHoverText();
          }

     }

     public ICustomGuiComponent toComponent() {
          CustomGuiScrollWrapper component = new CustomGuiScrollWrapper(this.id, this.guiLeft - GuiCustom.guiLeft, this.guiTop - GuiCustom.guiTop, this.width, this.height, (String[])this.getList().toArray(new String[0]));
          component.setHoverText(this.hoverText);
          return component;
     }

     public NBTTagCompound toNBT() {
          NBTTagCompound nbt = new NBTTagCompound();
          nbt.setInteger("id", this.id);
          if (!this.getSelectedList().isEmpty()) {
               NBTTagList tagList = new NBTTagList();
               Iterator var3 = this.getSelectedList().iterator();

               while(var3.hasNext()) {
                    String s = (String)var3.next();
                    tagList.appendTag(new NBTTagString(s));
               }

               nbt.setTag("selectedList", tagList);
          } else if (this.getSelected() != null && !this.getSelected().isEmpty()) {
               nbt.setString("selected", this.getSelected());
          } else {
               nbt.setString("selected", "Null");
          }

          return nbt;
     }
}
