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
          super(Minecraft.func_71410_x(), parent.field_146294_l, parent.field_146295_m, 32, parent.field_146295_m - 64, size);
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

     protected int func_148127_b() {
          return this.list.size();
     }

     protected void func_148144_a(int i, boolean flag, int j, int k) {
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

     protected boolean func_148131_a(int i) {
          if (!this.multiSelect) {
               return this.selected == null ? false : this.selected.equals(this.list.get(i));
          } else {
               return this.selectedList.contains(this.list.get(i));
          }
     }

     protected int func_148138_e() {
          return this.list.size() * this.size;
     }

     protected void func_148123_a() {
          this.parent.func_146276_q_();
     }

     protected void func_192637_a(int i, int j, int k, int l, int var6, int var7, float partialTick) {
          String s = (String)this.list.get(i);
          this.parent.func_73731_b(this.parent.getFontRenderer(), s, j + 50, k + 3, 16777215);
     }

     public void clear() {
          this.list.clear();
     }
}
