package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.client.ClientProxy;
import org.lwjgl.input.Mouse;

public class GuiNpcTextArea extends GuiNpcTextField {
     public boolean inMenu = true;
     public boolean numbersOnly = false;
     private int posX;
     private int posY;
     private int width;
     private int height;
     private int cursorCounter;
     private ClientProxy.FontContainer font;
     private int cursorPosition = 0;
     private int listHeight;
     private float scrolledY = 0.0F;
     private int startClick = -1;
     private boolean clickVerticalBar = false;
     private boolean wrapLine = true;

     public GuiNpcTextArea(int id, GuiScreen guiscreen, int i, int j, int k, int l, String s) {
          super(id, guiscreen, i, j, k, l, s);
          this.posX = i;
          this.posY = j;
          this.width = k;
          this.listHeight = this.height = l;
          this.font = ClientProxy.Font.copy();
          this.font.useCustomFont = true;
          this.setMaxStringLength(Integer.MAX_VALUE);
          this.setText(s.replace("\r\n", "\n"));
     }

     public void updateCursorCounter() {
          ++this.cursorCounter;
     }

     public boolean textboxKeyTyped(char c, int i) {
          if (this.isFocused() && this.canEdit) {
               String originalText = this.getText();
               this.setText(originalText);
               if (c == '\r' || c == '\n') {
                    this.setText(originalText.substring(0, this.cursorPosition) + c + originalText.substring(this.cursorPosition));
               }

               this.setCursorPositionZero();
               this.moveCursorBy(this.cursorPosition);
               boolean bo = super.textboxKeyTyped(c, i);
               String newText = this.getText();
               if (i != 211) {
                    this.cursorPosition += newText.length() - originalText.length();
               }

               if (i == 203 && this.cursorPosition > 0) {
                    --this.cursorPosition;
               }

               if (i == 205 && this.cursorPosition < newText.length()) {
                    ++this.cursorPosition;
               }

               return bo;
          } else {
               return false;
          }
     }

     public boolean mouseClicked(int i, int j, int k) {
          boolean wasFocused = this.isFocused();
          super.mouseClicked(i, j, k);
          if (this.hoverVerticalScrollBar(i, j)) {
               this.clickVerticalBar = true;
               this.startClick = -1;
               return false;
          } else if (k == 0 && this.canEdit) {
               int x = i - this.posX;
               int y = (j - this.posY - 4) / this.font.height(this.getText()) + this.getStartLineY();
               this.cursorPosition = 0;
               List lines = this.getLines();
               int charCount = 0;
               int lineCount = 0;
               int maxSize = this.width - (this.isScrolling() ? 14 : 4);

               for(int g = 0; g < lines.size(); ++g) {
                    String wholeLine = (String)lines.get(g);
                    String line = "";
                    char[] var14 = wholeLine.toCharArray();
                    int var15 = var14.length;

                    for(int var16 = 0; var16 < var15; ++var16) {
                         char c = var14[var16];
                         this.cursorPosition = charCount;
                         if (this.font.width(line + c) > maxSize && this.wrapLine) {
                              ++lineCount;
                              line = "";
                              if (y < lineCount) {
                                   break;
                              }
                         }

                         if (lineCount == y && x <= this.font.width(line + c)) {
                              return true;
                         }

                         ++charCount;
                         line = line + c;
                    }

                    this.cursorPosition = charCount;
                    ++lineCount;
                    ++charCount;
                    if (y < lineCount) {
                         break;
                    }
               }

               if (y >= lineCount) {
                    this.cursorPosition = this.getText().length();
               }

               return true;
          } else {
               return false;
          }
     }

     private List getLines() {
          List list = new ArrayList();
          String line = "";
          char[] var3 = this.getText().toCharArray();
          int var4 = var3.length;

          for(int var5 = 0; var5 < var4; ++var5) {
               char c = var3[var5];
               if (c != '\r' && c != '\n') {
                    line = line + c;
               } else {
                    list.add(line);
                    line = "";
               }
          }

          list.add(line);
          return list;
     }

     private int getStartLineY() {
          if (!this.isScrolling()) {
               this.scrolledY = 0.0F;
          }

          return MathHelper.ceil(this.scrolledY * (float)this.listHeight / (float)this.font.height(this.getText()));
     }

     public void drawTextBox(int mouseX, int mouseY) {
          drawRect(this.posX - 1, this.posY - 1, this.posX + this.width + 1, this.posY + this.height + 1, -6250336);
          drawRect(this.posX, this.posY, this.posX + this.width, this.posY + this.height, -16777216);
          int color = 14737632;
          boolean flag = this.isFocused() && this.cursorCounter / 6 % 2 == 0;
          int startLine = this.getStartLineY();
          int maxLine = this.height / this.font.height(this.getText()) + startLine;
          List lines = this.getLines();
          int charCount = 0;
          int lineCount = 0;
          int maxSize = this.width - (this.isScrolling() ? 14 : 4);

          int k2;
          for(k2 = 0; k2 < lines.size(); ++k2) {
               String wholeLine = (String)lines.get(k2);
               String line = "";
               char[] var14 = wholeLine.toCharArray();
               int yy = var14.length;

               for(int var16 = 0; var16 < yy; ++var16) {
                    char c = var14[var16];
                    if (this.font.width(line + c) > maxSize && this.wrapLine) {
                         if (lineCount >= startLine && lineCount < maxLine) {
                              this.drawString((FontRenderer)null, line, this.posX + 4, this.posY + 4 + (lineCount - startLine) * this.font.height(line), color);
                         }

                         line = "";
                         ++lineCount;
                    }

                    if (flag && charCount == this.cursorPosition && lineCount >= startLine && lineCount < maxLine && this.canEdit) {
                         int xx = this.posX + this.font.width(line) + 4;
                         int yy = this.posY + (lineCount - startLine) * this.font.height(line) + 4;
                         if (this.getText().length() == this.cursorPosition) {
                              this.font.drawString("_", xx, yy, color);
                         } else {
                              this.drawCursorVertical(xx, yy, xx + 1, yy + this.font.height(line));
                         }
                    }

                    ++charCount;
                    line = line + c;
               }

               if (lineCount >= startLine && lineCount < maxLine) {
                    this.drawString((FontRenderer)null, line, this.posX + 4, this.posY + 4 + (lineCount - startLine) * this.font.height(line), color);
                    if (flag && charCount == this.cursorPosition && this.canEdit) {
                         int xx = this.posX + this.font.width(line) + 4;
                         yy = this.posY + (lineCount - startLine) * this.font.height(line) + 4;
                         if (this.getText().length() == this.cursorPosition) {
                              this.font.drawString("_", xx, yy, color);
                         } else {
                              this.drawCursorVertical(xx, yy, xx + 1, yy + this.font.height(line));
                         }
                    }
               }

               ++lineCount;
               ++charCount;
          }

          k2 = Mouse.getDWheel();
          if (k2 != 0 && this.isFocused()) {
               this.addScrollY(k2 < 0 ? -10 : 10);
          }

          if (Mouse.isButtonDown(0)) {
               if (this.clickVerticalBar) {
                    if (this.startClick >= 0) {
                         this.addScrollY(this.startClick - (mouseY - this.posY));
                    }

                    if (this.hoverVerticalScrollBar(mouseX, mouseY)) {
                         this.startClick = mouseY - this.posY;
                    }

                    this.startClick = mouseY - this.posY;
               }
          } else {
               this.clickVerticalBar = false;
          }

          this.listHeight = lineCount * this.font.height(this.getText());
          this.drawVerticalScrollBar();
     }

     public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.font.drawString(text, x, y, color);
     }

     private boolean isScrolling() {
          return this.listHeight > this.height - 4;
     }

     private void addScrollY(int scrolled) {
          this.scrolledY -= 1.0F * (float)scrolled / (float)this.height;
          if (this.scrolledY < 0.0F) {
               this.scrolledY = 0.0F;
          }

          float max = 1.0F - 1.0F * (float)(this.height + 2) / (float)this.listHeight;
          if (this.scrolledY > max) {
               this.scrolledY = max;
          }

     }

     private boolean hoverVerticalScrollBar(int x, int y) {
          if (this.listHeight <= this.height - 4) {
               return false;
          } else {
               return this.posY < y && this.posY + this.height > y && x < this.posX + this.width && x > this.posX + (this.width - 8);
          }
     }

     private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
          int i1;
          if (p_146188_1_ < p_146188_3_) {
               i1 = p_146188_1_;
               p_146188_1_ = p_146188_3_;
               p_146188_3_ = i1;
          }

          if (p_146188_2_ < p_146188_4_) {
               i1 = p_146188_2_;
               p_146188_2_ = p_146188_4_;
               p_146188_4_ = i1;
          }

          if (p_146188_3_ > this.posX + this.width) {
               p_146188_3_ = this.posX + this.width;
          }

          if (p_146188_1_ > this.posX + this.width) {
               p_146188_1_ = this.posX + this.width;
          }

          BufferBuilder tessellator = Tessellator.getInstance().getBuffer();
          GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
          GlStateManager.disableTexture2D();
          GlStateManager.enableColorLogic();
          GlStateManager.colorLogicOp(5387);
          tessellator.begin(7, DefaultVertexFormats.POSITION);
          tessellator.pos((double)p_146188_1_, (double)p_146188_4_, 0.0D).endVertex();
          tessellator.pos((double)p_146188_3_, (double)p_146188_4_, 0.0D).endVertex();
          tessellator.pos((double)p_146188_3_, (double)p_146188_2_, 0.0D).endVertex();
          tessellator.pos((double)p_146188_1_, (double)p_146188_2_, 0.0D).endVertex();
          Tessellator.getInstance().draw();
          GlStateManager.disableColorLogic();
          GlStateManager.enableTexture2D();
     }

     private int getVerticalBarSize() {
          return (int)(1.0F * (float)this.height / (float)this.listHeight * (float)(this.height - 4));
     }

     private void drawVerticalScrollBar() {
          if (this.listHeight > this.height - 4) {
               Minecraft.getMinecraft().renderEngine.bindTexture(GuiCustomScroll.resource);
               int x = this.posX + this.width - 6;
               int y = (int)((float)this.posY + this.scrolledY * (float)this.height) + 2;
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               int sbSize = this.getVerticalBarSize();
               this.drawTexturedModalRect(x, y, this.width, 9, 5, 1);

               for(int k = 0; k < sbSize; ++k) {
                    this.drawTexturedModalRect(x, y + k, this.width, 10, 5, 1);
               }

               this.drawTexturedModalRect(x, y, this.width, 11, 5, 1);
          }
     }
}
