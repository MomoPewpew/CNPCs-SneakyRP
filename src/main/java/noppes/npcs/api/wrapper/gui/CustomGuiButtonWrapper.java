package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.gui.IButton;
import noppes.npcs.api.gui.ICustomGuiComponent;

public class CustomGuiButtonWrapper extends CustomGuiComponentWrapper implements IButton {
     int width;
     int height;
     String label;
     String texture;
     int textureX;
     int textureY;

     public CustomGuiButtonWrapper() {
          this.height = -1;
          this.textureY = -1;
     }

     public CustomGuiButtonWrapper(int id, String label, int x, int y) {
          this.height = -1;
          this.textureY = -1;
          this.setID(id);
          this.setLabel(label);
          this.setPos(x, y);
     }

     public CustomGuiButtonWrapper(int id, String label, int x, int y, int width, int height) {
          this(id, label, x, y);
          this.setSize(width, height);
     }

     public CustomGuiButtonWrapper(int id, String label, int x, int y, int width, int height, String texture) {
          this(id, label, x, y, width, height);
          this.setTexture(texture);
     }

     public CustomGuiButtonWrapper(int id, String label, int x, int y, int width, int height, String texture, int textureX, int textureY) {
          this(id, label, x, y, width, height, texture);
          this.setTextureOffset(textureX, textureY);
     }

     public int getWidth() {
          return this.width;
     }

     public int getHeight() {
          return this.height;
     }

     public IButton setSize(int width, int height) {
          this.width = width;
          this.height = height;
          return this;
     }

     public String getLabel() {
          return this.label;
     }

     public IButton setLabel(String label) {
          this.label = label;
          return this;
     }

     public String getTexture() {
          return this.texture;
     }

     public boolean hasTexture() {
          return this.texture != null;
     }

     public IButton setTexture(String texture) {
          this.texture = texture;
          return this;
     }

     public int getTextureX() {
          return this.textureX;
     }

     public int getTextureY() {
          return this.textureY;
     }

     public IButton setTextureOffset(int textureX, int textureY) {
          this.textureX = textureX;
          this.textureY = textureY;
          return this;
     }

     public int getID() {
          return this.id;
     }

     public ICustomGuiComponent setID(int id) {
          this.id = id;
          return this;
     }

     public int getPosX() {
          return this.posX;
     }

     public int getPosY() {
          return this.posY;
     }

     public int getType() {
          return 0;
     }

     public NBTTagCompound toNBT(NBTTagCompound nbt) {
          super.toNBT(nbt);
          if (this.width > 0 && this.height > 0) {
               nbt.func_74783_a("size", new int[]{this.width, this.height});
          }

          nbt.func_74778_a("label", this.label);
          if (this.hasTexture()) {
               nbt.func_74778_a("texture", this.texture);
          }

          if (this.textureX >= 0 && this.textureY >= 0) {
               nbt.func_74783_a("texPos", new int[]{this.textureX, this.textureY});
          }

          return nbt;
     }

     public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
          super.fromNBT(nbt);
          if (nbt.func_74764_b("size")) {
               this.setSize(nbt.func_74759_k("size")[0], nbt.func_74759_k("size")[1]);
          }

          this.setLabel(nbt.func_74779_i("label"));
          if (nbt.func_74764_b("texture")) {
               this.setTexture(nbt.func_74779_i("texture"));
          }

          if (nbt.func_74764_b("texPos")) {
               this.setTextureOffset(nbt.func_74759_k("texPos")[0], nbt.func_74759_k("texPos")[1]);
          }

          return this;
     }
}
