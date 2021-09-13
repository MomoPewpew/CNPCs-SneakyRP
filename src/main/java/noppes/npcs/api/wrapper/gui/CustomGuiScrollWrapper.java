package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import noppes.npcs.api.gui.IScroll;

public class CustomGuiScrollWrapper extends CustomGuiComponentWrapper implements IScroll {
     int width;
     int height;
     int defaultSelection = -1;
     String[] list;
     boolean multiSelect = false;

     public CustomGuiScrollWrapper() {
     }

     public CustomGuiScrollWrapper(int id, int x, int y, int width, int height, String[] list) {
          this.setID(id);
          this.setPos(x, y);
          this.setSize(width, height);
          this.setList(list);
     }

     public int getWidth() {
          return this.width;
     }

     public int getHeight() {
          return this.height;
     }

     public IScroll setSize(int width, int height) {
          this.width = width;
          this.height = height;
          return this;
     }

     public String[] getList() {
          return this.list;
     }

     public IScroll setList(String[] list) {
          this.list = list;
          return this;
     }

     public int getDefaultSelection() {
          return this.defaultSelection;
     }

     public IScroll setDefaultSelection(int defaultSelection) {
          this.defaultSelection = defaultSelection;
          return this;
     }

     public boolean isMultiSelect() {
          return this.multiSelect;
     }

     public IScroll setMultiSelect(boolean multiSelect) {
          this.multiSelect = multiSelect;
          return this;
     }

     public int getType() {
          return 4;
     }

     public NBTTagCompound toNBT(NBTTagCompound nbt) {
          super.toNBT(nbt);
          nbt.func_74783_a("size", new int[]{this.width, this.height});
          if (this.defaultSelection >= 0) {
               nbt.func_74768_a("default", this.defaultSelection);
          }

          NBTTagList list = new NBTTagList();
          String[] var3 = this.list;
          int var4 = var3.length;

          for(int var5 = 0; var5 < var4; ++var5) {
               String s = var3[var5];
               list.func_74742_a(new NBTTagString(s));
          }

          nbt.func_74782_a("list", list);
          nbt.func_74757_a("multiSelect", this.multiSelect);
          return nbt;
     }

     public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
          super.fromNBT(nbt);
          this.setSize(nbt.func_74759_k("size")[0], nbt.func_74759_k("size")[1]);
          if (nbt.func_74764_b("default")) {
               this.setDefaultSelection(nbt.func_74762_e("default"));
          }

          NBTTagList tagList = nbt.func_150295_c("list", 8);
          String[] list = new String[tagList.func_74745_c()];

          for(int i = 0; i < tagList.func_74745_c(); ++i) {
               list[i] = ((NBTTagString)tagList.func_179238_g(i)).func_150285_a_();
          }

          this.setList(list);
          this.setMultiSelect(nbt.func_74767_n("multiSelect"));
          return this;
     }
}
