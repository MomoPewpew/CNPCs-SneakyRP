package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.util.NaturalOrderComparator;
import org.lwjgl.input.Mouse;

public class GuiCustomScroll extends GuiScreen {
     public static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/misc.png");
     private List list;
     public int id;
     public int guiLeft;
     public int guiTop;
     private int xSize;
     private int ySize;
     public int selected;
     private HashSet selectedList;
     private int hover;
     private int listHeight;
     private int scrollY;
     private int maxScrollY;
     private int scrollHeight;
     private boolean isScrolling;
     private boolean multipleSelection;
     private ICustomScrollListener listener;
     private boolean isSorted;
     public boolean visible;
     private boolean selectable;
     private int lastClickedItem;
     private long lastClickedTime;

     public GuiCustomScroll(GuiScreen parent, int id) {
          this.guiLeft = 0;
          this.guiTop = 0;
          this.multipleSelection = false;
          this.isSorted = true;
          this.visible = true;
          this.selectable = true;
          this.lastClickedTime = 0L;
          this.width = 176;
          this.height = 166;
          this.xSize = 176;
          this.ySize = 159;
          this.selected = -1;
          this.hover = -1;
          this.selectedList = new HashSet();
          this.listHeight = 0;
          this.scrollY = 0;
          this.scrollHeight = 0;
          this.isScrolling = false;
          if (parent instanceof ICustomScrollListener) {
               this.listener = (ICustomScrollListener)parent;
          }

          this.list = new ArrayList();
          this.id = id;
     }

     public GuiCustomScroll(GuiScreen parent, int id, boolean multipleSelection) {
          this(parent, id);
          this.multipleSelection = multipleSelection;
     }

     public void setSize(int x, int y) {
          this.ySize = y;
          this.xSize = x;
          this.listHeight = 14 * this.list.size();
          if (this.listHeight > 0) {
               this.scrollHeight = (int)((double)(this.ySize - 8) / (double)this.listHeight * (double)(this.ySize - 8));
          } else {
               this.scrollHeight = Integer.MAX_VALUE;
          }

          this.maxScrollY = this.listHeight - (this.ySize - 8) - 1;
     }

     public int getXSize() {
          return this.xSize;
     }

     public int getYSize() {
          return this.ySize;
     }

     public void drawScreen(int i, int j, float f, int mouseScrolled) {
          if (this.visible) {
               this.drawGradientRect(this.guiLeft, this.guiTop, this.xSize + this.guiLeft, this.ySize + this.guiTop, -1072689136, -804253680);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.mc.renderEngine.bindTexture(resource);
               if (this.scrollHeight < this.ySize - 8) {
                    this.drawScrollBar();
               }

               GlStateManager.pushMatrix();
               GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
               GlStateManager.popMatrix();
               GlStateManager.pushMatrix();
               GlStateManager.translate((float)this.guiLeft, (float)this.guiTop, 0.0F);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               if (this.selectable) {
                    this.hover = this.getMouseOver(i, j);
               }

               this.drawItems();
               GlStateManager.popMatrix();
               if (this.scrollHeight < this.ySize - 8) {
                    i -= this.guiLeft;
                    j -= this.guiTop;
                    if (Mouse.isButtonDown(0)) {
                         if (i >= this.xSize - 11 && i < this.xSize - 6 && j >= 4 && j < this.ySize) {
                              this.isScrolling = true;
                         }
                    } else {
                         this.isScrolling = false;
                    }

                    if (this.isScrolling) {
                         this.scrollY = (j - 8) * this.listHeight / (this.ySize - 8) - this.scrollHeight;
                         if (this.scrollY < 0) {
                              this.scrollY = 0;
                         }

                         if (this.scrollY > this.maxScrollY) {
                              this.scrollY = this.maxScrollY;
                         }
                    }

                    if (mouseScrolled != 0) {
                         this.scrollY += mouseScrolled > 0 ? -14 : 14;
                         if (this.scrollY > this.maxScrollY) {
                              this.scrollY = this.maxScrollY;
                         }

                         if (this.scrollY < 0) {
                              this.scrollY = 0;
                         }
                    }
               }

          }
     }

     public boolean mouseInOption(int i, int j, int k) {
          int l = 4;
          int i1 = 14 * k + 4 - this.scrollY;
          return i >= l - 1 && i < l + this.xSize - 11 && j >= i1 - 1 && j < i1 + 8;
     }

     protected void drawItems() {
          for(int i = 0; i < this.list.size(); ++i) {
               int j = 4;
               int k = 14 * i + 4 - this.scrollY;
               if (k >= 4 && k + 12 < this.ySize) {
                    int xOffset = this.scrollHeight < this.ySize - 8 ? 0 : 10;
                    String displayString = I18n.translateToLocal((String)this.list.get(i));
                    String text = "";
                    float maxWidth = (float)(this.xSize + xOffset - 8) * 0.8F;
                    if ((float)this.fontRenderer.getStringWidth(displayString) > maxWidth) {
                         for(int h = 0; h < displayString.length(); ++h) {
                              char c = displayString.charAt(h);
                              text = text + c;
                              if ((float)this.fontRenderer.getStringWidth(text) > maxWidth) {
                                   break;
                              }
                         }

                         if (displayString.length() > text.length()) {
                              text = text + "...";
                         }
                    } else {
                         text = displayString;
                    }

                    if (this.multipleSelection && this.selectedList.contains(text) || !this.multipleSelection && this.selected == i) {
                         this.drawVerticalLine(j - 2, k - 4, k + 10, -1);
                         this.drawVerticalLine(j + this.xSize - 18 + xOffset, k - 4, k + 10, -1);
                         this.drawHorizontalLine(j - 2, j + this.xSize - 18 + xOffset, k - 3, -1);
                         this.drawHorizontalLine(j - 2, j + this.xSize - 18 + xOffset, k + 10, -1);
                         this.fontRenderer.drawString(text, j, k, 16777215);
                    } else if (i == this.hover) {
                         this.fontRenderer.drawString(text, j, k, 65280);
                    } else {
                         this.fontRenderer.drawString(text, j, k, 16777215);
                    }
               }
          }

     }

     public String getSelected() {
          return this.selected != -1 && this.selected < this.list.size() ? (String)this.list.get(this.selected) : null;
     }

     private int getMouseOver(int i, int j) {
          i -= this.guiLeft;
          j -= this.guiTop;
          if (i >= 4 && i < this.xSize - 4 && j >= 4 && j < this.ySize) {
               for(int j1 = 0; j1 < this.list.size(); ++j1) {
                    if (this.mouseInOption(i, j, j1)) {
                         return j1;
                    }
               }
          }

          return -1;
     }

     public void mouseClicked(int i, int j, int k) {
          if (k == 0 && this.hover >= 0) {
               if (this.multipleSelection) {
                    if (this.selectedList.contains(this.list.get(this.hover))) {
                         this.selectedList.remove(this.list.get(this.hover));
                    } else {
                         this.selectedList.add(this.list.get(this.hover));
                    }
               } else {
                    if (this.hover >= 0) {
                         this.selected = this.hover;
                    }

                    this.hover = -1;
               }

               if (this.listener != null) {
                    long time = System.currentTimeMillis();
                    this.listener.scrollClicked(i, j, k, this);
                    if (this.selected >= 0 && this.selected == this.lastClickedItem && time - this.lastClickedTime < 500L) {
                         this.listener.scrollDoubleClicked((String)this.list.get(this.selected), this);
                    }

                    this.lastClickedTime = time;
                    this.lastClickedItem = this.selected;
               }

          }
     }

     private void drawScrollBar() {
          int i = this.guiLeft + this.xSize - 9;
          int j = this.guiTop + (int)((double)this.scrollY / (double)this.listHeight * (double)(this.ySize - 8)) + 4;
          this.drawTexturedModalRect(i, j, this.xSize, 9, 5, 1);

          int k;
          for(k = j + 1; k < j + this.scrollHeight - 1; ++k) {
               this.drawTexturedModalRect(i, k, this.xSize, 10, 5, 1);
          }

          this.drawTexturedModalRect(i, k, this.xSize, 11, 5, 1);
     }

     public boolean hasSelected() {
          return this.selected >= 0;
     }

     public void setList(List list) {
          if (!this.isSameList(list)) {
               this.isSorted = true;
               this.scrollY = 0;
               Collections.sort(list, new NaturalOrderComparator());
               this.list = list;
               this.setSize(this.xSize, this.ySize);
          }
     }

     public void setUnsortedList(List list) {
          if (!this.isSameList(list)) {
               this.isSorted = false;
               this.scrollY = 0;
               this.list = list;
               this.setSize(this.xSize, this.ySize);
          }
     }

     private boolean isSameList(List list) {
          if (this.list.size() != list.size()) {
               return false;
          } else {
               Iterator var2 = this.list.iterator();

               String s;
               do {
                    if (!var2.hasNext()) {
                         return true;
                    }

                    s = (String)var2.next();
               } while(list.contains(s));

               return false;
          }
     }

     public void replace(String old, String name) {
          String select = this.getSelected();
          this.list.remove(old);
          this.list.add(name);
          if (this.isSorted) {
               Collections.sort(this.list, new NaturalOrderComparator());
          }

          if (old.equals(select)) {
               select = name;
          }

          this.selected = this.list.indexOf(select);
          this.setSize(this.xSize, this.ySize);
     }

     public void setSelected(String name) {
          this.selected = this.list.indexOf(name);
     }

     public void clear() {
          this.list = new ArrayList();
          this.selected = -1;
          this.scrollY = 0;
          this.setSize(this.xSize, this.ySize);
     }

     public List getList() {
          return this.list;
     }

     public HashSet getSelectedList() {
          return this.selectedList;
     }

     public void setSelectedList(HashSet selectedList) {
          this.selectedList = selectedList;
     }

     public GuiCustomScroll setUnselectable() {
          this.selectable = false;
          return this;
     }

     public void scrollTo(String name) {
          int i = this.list.indexOf(name);
          if (i >= 0 && this.scrollHeight < this.ySize - 8) {
               int pos = (int)(1.0F * (float)i / (float)this.list.size() * (float)this.listHeight);
               if (pos > this.maxScrollY) {
                    pos = this.maxScrollY;
               }

               this.scrollY = pos;
          }
     }

     public boolean isMouseOver(int x, int y) {
          return x >= this.guiLeft && x <= this.guiLeft + this.xSize && y >= this.guiTop && y <= this.guiTop + this.ySize;
     }
}
