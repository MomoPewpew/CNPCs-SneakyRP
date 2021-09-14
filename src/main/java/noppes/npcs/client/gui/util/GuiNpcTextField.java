package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiNpcTextField extends GuiTextField {
     public boolean enabled;
     public boolean inMenu;
     public boolean numbersOnly;
     private ITextfieldListener listener;
     public int min;
     public int max;
     public int def;
     private static GuiNpcTextField activeTextfield = null;
     public boolean canEdit;
     private final int[] allowedSpecialChars;

     public GuiNpcTextField(int id, GuiScreen parent, FontRenderer fontRenderer, int i, int j, int k, int l, String s) {
          super(id, fontRenderer, i, j, k, l);
          this.enabled = true;
          this.inMenu = true;
          this.numbersOnly = false;
          this.min = 0;
          this.max = Integer.MAX_VALUE;
          this.def = 0;
          this.canEdit = true;
          this.allowedSpecialChars = new int[]{14, 211, 203, 205};
          this.setMaxStringLength(500);
          this.setText(s == null ? "" : s);
          if (parent instanceof ITextfieldListener) {
               this.listener = (ITextfieldListener)parent;
          }

     }

     public static boolean isActive() {
          return activeTextfield != null;
     }

     public GuiNpcTextField(int id, GuiScreen parent, int i, int j, int k, int l, String s) {
          this(id, parent, Minecraft.getMinecraft().fontRenderer, i, j, k, l, s);
     }

     private boolean charAllowed(char c, int i) {
          if (!this.numbersOnly || Character.isDigit(c) || c == '-' && this.getText().length() == 0) {
               return true;
          } else {
               int[] var3 = this.allowedSpecialChars;
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                    int j = var3[var5];
                    if (j == i) {
                         return true;
                    }
               }

               return false;
          }
     }

     public boolean textboxKeyTyped(char c, int i) {
          return this.charAllowed(c, i) && this.canEdit ? super.textboxKeyTyped(c, i) : false;
     }

     public boolean isEmpty() {
          return this.getText().trim().length() == 0;
     }

     public int getInteger() {
          return Integer.parseInt(this.getText());
     }

     public boolean isInteger() {
          try {
               Integer.parseInt(this.getText());
               return true;
          } catch (NumberFormatException var2) {
               return false;
          }
     }

     public boolean mouseClicked(int i, int j, int k) {
          if (!this.canEdit) {
               return false;
          } else {
               boolean wasFocused = this.isFocused();
               boolean clicked = super.mouseClicked(i, j, k);
               if (wasFocused != this.isFocused() && wasFocused) {
                    this.unFocused();
               }

               if (this.isFocused()) {
                    activeTextfield = this;
               }

               return clicked;
          }
     }

     public void unFocused() {
          if (this.numbersOnly) {
               if (!this.isEmpty() && this.isInteger()) {
                    if (this.getInteger() < this.min) {
                         this.setText(this.min + "");
                    } else if (this.getInteger() > this.max) {
                         this.setText(this.max + "");
                    }
               } else {
                    this.setText(this.def + "");
               }
          }

          if (this.listener != null) {
               this.listener.unFocused(this);
          }

          if (this == activeTextfield) {
               activeTextfield = null;
          }

     }

     public void drawTextBox() {
          if (this.enabled) {
               super.drawTextBox();
          }

     }

     public void setMinMaxDefault(int i, int j, int k) {
          this.min = i;
          this.max = j;
          this.def = k;
     }

     public static void unfocus() {
          GuiNpcTextField prev = activeTextfield;
          activeTextfield = null;
          if (prev != null) {
               prev.unFocused();
          }

     }

     public void drawTextBox(int mousX, int mousY) {
          this.drawTextBox();
     }

     public GuiNpcTextField setNumbersOnly() {
          this.numbersOnly = true;
          return this;
     }
}
