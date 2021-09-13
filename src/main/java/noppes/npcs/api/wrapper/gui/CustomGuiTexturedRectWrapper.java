package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.gui.ITexturedRect;

public class CustomGuiTexturedRectWrapper extends CustomGuiComponentWrapper implements ITexturedRect {
     int width;
     int height;
     int textureX;
     int textureY;
     float scale;
     String texture;

     public CustomGuiTexturedRectWrapper() {
          this.textureY = -1;
          this.scale = 1.0F;
     }

     public CustomGuiTexturedRectWrapper(int id, String texture, int x, int y, int width, int height) {
          this.textureY = -1;
          this.scale = 1.0F;
          this.setID(id);
          this.setTexture(texture);
          this.setPos(x, y);
          this.setSize(width, height);
     }

     public CustomGuiTexturedRectWrapper(int id, String texture, int x, int y, int width, int height, int textureX, int textureY) {
          this(id, texture, x, y, width, height);
          this.setTextureOffset(textureX, textureY);
     }

     public String getTexture() {
          return this.texture;
     }

     public ITexturedRect setTexture(String texture) {
          this.texture = texture;
          return this;
     }

     public int getWidth() {
          return this.width;
     }

     public int getHeight() {
          return this.height;
     }

     public ITexturedRect setSize(int width, int height) {
          this.width = width;
          this.height = height;
          return this;
     }

     public float getScale() {
          return this.scale;
     }

     public ITexturedRect setScale(float scale) {
          this.scale = scale;
          return this;
     }

     public int getTextureX() {
          return this.textureX;
     }

     public int getTextureY() {
          return this.textureY;
     }

     public ITexturedRect setTextureOffset(int offsetX, int offsetY) {
          this.textureX = offsetX;
          this.textureY = offsetY;
          return this;
     }

     public int getType() {
          return 2;
     }

     public NBTTagCompound toNBT(NBTTagCompound nbt) {
          super.toNBT(nbt);
          nbt.func_74783_a("size", new int[]{this.width, this.height});
          nbt.func_74776_a("scale", this.scale);
          nbt.func_74778_a("texture", this.texture);
          if (this.textureX >= 0 && this.textureY >= 0) {
               nbt.func_74783_a("texPos", new int[]{this.textureX, this.textureY});
          }

          return nbt;
     }

     public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
          super.fromNBT(nbt);
          this.setSize(nbt.func_74759_k("size")[0], nbt.func_74759_k("size")[1]);
          this.setScale(nbt.func_74760_g("scale"));
          this.setTexture(nbt.func_74779_i("texture"));
          if (nbt.func_74764_b("texPos")) {
               this.setTextureOffset(nbt.func_74759_k("texPos")[0], nbt.func_74759_k("texPos")[1]);
          }

          return this;
     }
}
