package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.gui.ILabel;

public class CustomGuiLabelWrapper extends CustomGuiComponentWrapper implements ILabel {
     String label;
     int width;
     int height;
     int color;
     float scale;

     public CustomGuiLabelWrapper() {
          this.color = 16777215;
          this.scale = 1.0F;
     }

     public CustomGuiLabelWrapper(int id, String label, int x, int y, int width, int height) {
          this.color = 16777215;
          this.scale = 1.0F;
          this.setID(id);
          this.setText(label);
          this.setPos(x, y);
          this.setSize(width, height);
     }

     public CustomGuiLabelWrapper(int id, String label, int x, int y, int width, int height, int color) {
          this(id, label, x, y, width, height);
          this.setColor(color);
     }

     public String getText() {
          return this.label;
     }

     public ILabel setText(String label) {
          this.label = label;
          return this;
     }

     public int getWidth() {
          return this.width;
     }

     public int getHeight() {
          return this.height;
     }

     public ILabel setSize(int width, int height) {
          this.width = width;
          this.height = height;
          return this;
     }

     public int getColor() {
          return this.color;
     }

     public ILabel setColor(int color) {
          this.color = color;
          return this;
     }

     public float getScale() {
          return this.scale;
     }

     public ILabel setScale(float scale) {
          this.scale = scale;
          return this;
     }

     public int getType() {
          return 1;
     }

     public NBTTagCompound toNBT(NBTTagCompound nbt) {
          super.toNBT(nbt);
          nbt.func_74778_a("label", this.label);
          nbt.func_74783_a("size", new int[]{this.width, this.height});
          nbt.func_74768_a("color", this.color);
          nbt.func_74776_a("scale", this.scale);
          return nbt;
     }

     public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
          super.fromNBT(nbt);
          this.setText(nbt.func_74779_i("label"));
          this.setSize(nbt.func_74759_k("size")[0], nbt.func_74759_k("size")[1]);
          this.setColor(nbt.func_74762_e("color"));
          this.setScale(nbt.func_74760_g("scale"));
          return this;
     }
}
