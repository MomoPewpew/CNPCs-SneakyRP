package noppes.npcs.api.wrapper.gui;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import noppes.npcs.api.gui.ICustomGuiComponent;

public abstract class CustomGuiComponentWrapper implements ICustomGuiComponent {
     int id;
     int posX;
     int posY;
     String[] hoverText;

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

     public ICustomGuiComponent setPos(int x, int y) {
          this.posX = x;
          this.posY = y;
          return this;
     }

     public boolean hasHoverText() {
          return this.hoverText != null && this.hoverText.length > 0;
     }

     public String[] getHoverText() {
          return this.hoverText;
     }

     public ICustomGuiComponent setHoverText(String text) {
          this.hoverText = new String[]{text};
          return this;
     }

     public ICustomGuiComponent setHoverText(String[] text) {
          this.hoverText = text;
          return this;
     }

     public abstract int getType();

     public NBTTagCompound toNBT(NBTTagCompound nbt) {
          nbt.func_74768_a("id", this.id);
          nbt.func_74783_a("pos", new int[]{this.posX, this.posY});
          if (this.hoverText != null) {
               NBTTagList list = new NBTTagList();
               String[] var3 = this.hoverText;
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                    String s = var3[var5];
                    if (s != null && !s.isEmpty()) {
                         list.func_74742_a(new NBTTagString(s));
                    }
               }

               if (list.func_74745_c() > 0) {
                    nbt.func_74782_a("hover", list);
               }
          }

          nbt.func_74768_a("type", this.getType());
          return nbt;
     }

     public CustomGuiComponentWrapper fromNBT(NBTTagCompound nbt) {
          this.setID(nbt.func_74762_e("id"));
          this.setPos(nbt.func_74759_k("pos")[0], nbt.func_74759_k("pos")[1]);
          if (nbt.func_74764_b("hover")) {
               NBTTagList list = nbt.func_150295_c("hover", 8);
               String[] hoverText = new String[list.func_74745_c()];

               for(int i = 0; i < list.func_74745_c(); ++i) {
                    hoverText[i] = ((NBTTagString)list.func_179238_g(i)).func_150285_a_();
               }

               this.setHoverText(hoverText);
          }

          return this;
     }

     public static CustomGuiComponentWrapper createFromNBT(NBTTagCompound nbt) {
          switch(nbt.func_74762_e("type")) {
          case 0:
               return (new CustomGuiButtonWrapper()).fromNBT(nbt);
          case 1:
               return (new CustomGuiLabelWrapper()).fromNBT(nbt);
          case 2:
               return (new CustomGuiTexturedRectWrapper()).fromNBT(nbt);
          case 3:
               return (new CustomGuiTextFieldWrapper()).fromNBT(nbt);
          case 4:
               return (new CustomGuiScrollWrapper()).fromNBT(nbt);
          case 5:
               return (new CustomGuiItemSlotWrapper()).fromNBT(nbt);
          default:
               return null;
          }
     }
}
