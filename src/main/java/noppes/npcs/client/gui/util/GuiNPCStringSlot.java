package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import noppes.npcs.util.NaturalOrderComparator;

public class GuiNPCStringSlot extends GuiSlot {
     private List list;
     public String selected;
     public HashSet selectedList = new HashSet();
     private boolean multiSelect;
     private GuiNPCInterface parent;
     public int size;

     public GuiNPCStringSlot(Collection list, GuiNPCInterface parent, boolean multiSelect, int size) {
          super(Minecraft.getMinecraft(), parent.width, parent.height, 32, parent.height - 64, size);
          this.parent = parent;
          this.list = new ArrayList(list);
          Collections.sort(this.list, new NaturalOrderComparator());
          this.multiSelect = multiSelect;
          this.size = size;
     }

     public void setList(List list) {
          Collections.sort(list, new NaturalOrderComparator());
          this.list = list;
          this.selected = "";
     }

     protected int getSize() {
          return this.list.size();
     }

     protected void elementClicked(int i, boolean flag, int j, int k) {
          if (this.selected != null && this.selected.equals(this.list.get(i)) && flag) {
               this.parent.doubleClicked();
          }

          this.selected = (String)this.list.get(i);
          if (this.selectedList.contains(this.selected)) {
               this.selectedList.remove(this.selected);
          } else {
               this.selectedList.add(this.selected);
          }

          this.parent.elementClicked();
     }

     protected boolean isSelected(int i) {
          if (!this.multiSelect) {
               return this.selected == null ? false : this.selected.equals(this.list.get(i));
          } else {
               return this.selectedList.contains(this.list.get(i));
          }
     }

     protected int getContentHeight() {
          return this.list.size() * this.size;
     }

     protected void drawBackground() {
          this.parent.drawDefaultBackground();
     }

     protected void drawSlot(int i, int j, int k, int l, int var6, int var7, float partialTick) {
          String s = (String)this.list.get(i);
          this.parent.drawString(this.parent.getFontRenderer(), s, j + 50, k + 3, 16777215);
     }

     public void clear() {
          this.list.clear();
     }
}
