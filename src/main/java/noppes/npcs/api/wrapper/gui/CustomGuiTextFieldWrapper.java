package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.gui.ITextField;

public class CustomGuiTextFieldWrapper extends CustomGuiComponentWrapper implements ITextField {
     int width;
     int height;
     String defaultText;

     public CustomGuiTextFieldWrapper() {
     }

     public CustomGuiTextFieldWrapper(int id, int x, int y, int width, int height) {
          this.setID(id);
          this.setPos(x, y);
          this.setSize(width, height);
     }

     public int getWidth() {
          return this.width;
     }

     public int getHeight() {
          return this.height;
     }

     public ITextField setSize(int width, int height) {
          this.width = width;
          this.height = height;
          return this;
     }

     public String getText() {
          return this.defaultText;
     }

     public ITextField setText(String defaultText) {
          this.defaultText = defaultText;
          return this;
     }

     public int getType() {
          return 3;
     }

     public NBTTagCompound toNBT(NBTTagCompound nbt) {
          super.toNBT(nbt);
          nbt.func_74783_a("size", new int[]{this.width, this.height});
          if (this.defaultText != null && !this.defaultText.isEmpty()) {
               nbt.func_74778_a("default", this.defaultText);
          }

          return nbt;
     }

     public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
          super.fromNBT(nbt);
          this.setSize(nbt.func_74759_k("size")[0], nbt.func_74759_k("size")[1]);
          if (nbt.func_74764_b("default")) {
               this.setText(nbt.func_74779_i("default"));
          }

          return this;
     }
}
