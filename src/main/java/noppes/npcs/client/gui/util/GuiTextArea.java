package noppes.npcs.client.gui.util;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.config.TrueTypeFont;
import org.lwjgl.input.Mouse;

public class GuiTextArea extends Gui implements IGui, IKeyListener, IMouseListener {
     public int id;
     public int x;
     public int y;
     public int width;
     public int height;
     private int cursorCounter;
     private ITextChangeListener listener;
     private static TrueTypeFont font;
     public String text = null;
     private TextContainer container = null;
     public boolean active = false;
     public boolean enabled = true;
     public boolean visible = true;
     public boolean clicked = false;
     public boolean doubleClicked = false;
     public boolean clickScrolling = false;
     private int startSelection;
     private int endSelection;
     private int cursorPosition;
     private int scrolledLine = 0;
     private boolean enableCodeHighlighting = false;
     private static final char colorChar = '\uffff';
     public List undoList = new ArrayList();
     public List redoList = new ArrayList();
     public boolean undoing = false;
     private long lastClicked = 0L;

     public GuiTextArea(int id, int x, int y, int width, int height, String text) {
          this.id = id;
          this.x = x;
          this.y = y;
          this.width = width;
          this.height = height;
          this.undoing = true;
          this.setText(text);
          this.undoing = false;
          font.setSpecial('\uffff');
     }

     public void drawScreen(int xMouse, int yMouse) {
          if (this.visible) {
               func_73734_a(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
               func_73734_a(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
               this.container.visibleLines = this.height / this.container.lineHeight;
               int startBracket;
               if (this.clicked) {
                    this.clicked = Mouse.isButtonDown(0);
                    startBracket = this.getSelectionPos(xMouse, yMouse);
                    if (startBracket != this.cursorPosition) {
                         if (this.doubleClicked) {
                              this.startSelection = this.endSelection = this.cursorPosition;
                              this.doubleClicked = false;
                         }

                         this.setCursor(startBracket, true);
                    }
               } else if (this.doubleClicked) {
                    this.doubleClicked = false;
               }

               if (this.clickScrolling) {
                    this.clickScrolling = Mouse.isButtonDown(0);
                    startBracket = this.container.linesCount - this.container.visibleLines;
                    this.scrolledLine = Math.min(Math.max((int)(1.0F * (float)startBracket * (float)(yMouse - this.y) / (float)this.height), 0), startBracket);
               }

               startBracket = 0;
               int endBracket = 0;
               if (this.endSelection - this.startSelection == 1 || this.startSelection == this.endSelection && this.startSelection < this.text.length()) {
                    char c = this.text.charAt(this.startSelection);
                    int found = 0;
                    if (c == '{') {
                         found = this.findClosingBracket(this.text.substring(this.startSelection), '{', '}');
                    } else if (c == '[') {
                         found = this.findClosingBracket(this.text.substring(this.startSelection), '[', ']');
                    } else if (c == '(') {
                         found = this.findClosingBracket(this.text.substring(this.startSelection), '(', ')');
                    } else if (c == '}') {
                         found = this.findOpeningBracket(this.text.substring(0, this.startSelection + 1), '{', '}');
                    } else if (c == ']') {
                         found = this.findOpeningBracket(this.text.substring(0, this.startSelection + 1), '[', ']');
                    } else if (c == ')') {
                         found = this.findOpeningBracket(this.text.substring(0, this.startSelection + 1), '(', ')');
                    }

                    if (found != 0) {
                         startBracket = this.startSelection;
                         endBracket = this.startSelection + found;
                    }
               }

               List list = new ArrayList(this.container.lines);
               String wordHightLight = null;
               if (this.startSelection != this.endSelection) {
                    Matcher m = this.container.regexWord.matcher(this.text);

                    while(m.find()) {
                         if (m.start() == this.startSelection && m.end() == this.endSelection) {
                              wordHightLight = this.text.substring(this.startSelection, this.endSelection);
                         }
                    }
               }

               int i;
               for(i = 0; i < list.size(); ++i) {
                    TextContainer.LineData data = (TextContainer.LineData)list.get(i);
                    String line = data.text;
                    int w = line.length();
                    int yPos;
                    int posX;
                    int e;
                    if (startBracket != endBracket) {
                         if (startBracket >= data.start && startBracket < data.end) {
                              yPos = font.width(line.substring(0, startBracket - data.start));
                              posX = font.width(line.substring(0, startBracket - data.start + 1)) + 1;
                              e = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                              func_73734_a(this.x + 1 + yPos, e, this.x + 1 + posX, e + this.container.lineHeight + 1, -1728001024);
                         }

                         if (endBracket >= data.start && endBracket < data.end) {
                              yPos = font.width(line.substring(0, endBracket - data.start));
                              posX = font.width(line.substring(0, endBracket - data.start + 1)) + 1;
                              e = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                              func_73734_a(this.x + 1 + yPos, e, this.x + 1 + posX, e + this.container.lineHeight + 1, -1728001024);
                         }
                    }

                    if (i >= this.scrolledLine && i < this.scrolledLine + this.container.visibleLines) {
                         if (wordHightLight != null) {
                              Matcher m = this.container.regexWord.matcher(line);

                              while(m.find()) {
                                   if (line.substring(m.start(), m.end()).equals(wordHightLight)) {
                                        posX = font.width(line.substring(0, m.start()));
                                        e = font.width(line.substring(0, m.end())) + 1;
                                        int posY = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                                        func_73734_a(this.x + 1 + posX, posY, this.x + 1 + e, posY + this.container.lineHeight + 1, -1728033792);
                                   }
                              }
                         }

                         if (this.startSelection != this.endSelection && this.endSelection > data.start && this.startSelection <= data.end && this.startSelection < data.end) {
                              yPos = font.width(line.substring(0, Math.max(this.startSelection - data.start, 0)));
                              posX = font.width(line.substring(0, Math.min(this.endSelection - data.start, w))) + 1;
                              e = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                              func_73734_a(this.x + 1 + yPos, e, this.x + 1 + posX, e + this.container.lineHeight + 1, -1728052993);
                         }

                         yPos = this.y + (i - this.scrolledLine) * this.container.lineHeight + 1;
                         font.draw(data.getFormattedString(), (float)(this.x + 1), (float)yPos, -2039584);
                         if (this.active && this.isEnabled() && this.cursorCounter / 6 % 2 == 0 && this.cursorPosition >= data.start && this.cursorPosition < data.end) {
                              posX = this.x + font.width(line.substring(0, this.cursorPosition - data.start));
                              func_73734_a(posX + 1, yPos, posX + 2, yPos + 1 + this.container.lineHeight, -3092272);
                         }
                    }
               }

               if (this.hasVerticalScrollbar()) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(GuiCustomScroll.resource);
                    i = Math.max((int)(1.0F * (float)this.container.visibleLines / (float)this.container.linesCount * (float)this.height), 2);
                    int posX = this.x + this.width - 6;
                    int posY = (int)((float)this.y + 1.0F * (float)this.scrolledLine / (float)this.container.linesCount * (float)(this.height - 4)) + 1;
                    func_73734_a(posX, posY, posX + 5, posY + i, -2039584);
               }

          }
     }

     private int findClosingBracket(String str, char s, char e) {
          int found = 0;
          char[] chars = str.toCharArray();

          for(int i = 0; i < chars.length; ++i) {
               char c = chars[i];
               if (c == s) {
                    ++found;
               } else if (c == e) {
                    --found;
                    if (found == 0) {
                         return i;
                    }
               }
          }

          return 0;
     }

     private int findOpeningBracket(String str, char s, char e) {
          int found = 0;
          char[] chars = str.toCharArray();

          for(int i = chars.length - 1; i >= 0; --i) {
               char c = chars[i];
               if (c == e) {
                    ++found;
               } else if (c == s) {
                    --found;
                    if (found == 0) {
                         return i - chars.length + 1;
                    }
               }
          }

          return 0;
     }

     private int getSelectionPos(int xMouse, int yMouse) {
          xMouse -= this.x + 1;
          yMouse -= this.y + 1;
          List list = new ArrayList(this.container.lines);

          for(int i = 0; i < list.size(); ++i) {
               TextContainer.LineData data = (TextContainer.LineData)list.get(i);
               if (i >= this.scrolledLine && i < this.scrolledLine + this.container.visibleLines) {
                    int yPos = (i - this.scrolledLine) * this.container.lineHeight;
                    if (yMouse >= yPos && yMouse < yPos + this.container.lineHeight) {
                         int lineWidth = 0;
                         char[] chars = data.text.toCharArray();

                         for(int j = 1; j <= chars.length; ++j) {
                              int w = font.width(data.text.substring(0, j));
                              if (xMouse < lineWidth + (w - lineWidth) / 2) {
                                   return data.start + j - 1;
                              }

                              lineWidth = w;
                         }

                         return data.end - 1;
                    }
               }
          }

          return this.container.text.length();
     }

     public int getID() {
          return this.id;
     }

     public void keyTyped(char c, int i) {
          if (this.active) {
               if (GuiScreen.func_175278_g(i)) {
                    this.startSelection = this.cursorPosition = 0;
                    this.endSelection = this.text.length();
               } else if (this.isEnabled()) {
                    String original = this.text;
                    int j;
                    Matcher m;
                    if (i == 203) {
                         j = 1;
                         if (GuiScreen.func_146271_m()) {
                              m = this.container.regexWord.matcher(this.text.substring(0, this.cursorPosition));

                              while(m.find()) {
                                   if (m.start() != m.end()) {
                                        j = this.cursorPosition - m.start();
                                   }
                              }
                         }

                         this.setCursor(this.cursorPosition - j, GuiScreen.func_146272_n());
                    } else if (i != 205) {
                         if (i == 200) {
                              this.setCursor(this.cursorUp(), GuiScreen.func_146272_n());
                         } else if (i == 208) {
                              this.setCursor(this.cursorDown(), GuiScreen.func_146272_n());
                         } else {
                              String s;
                              if (i == 211) {
                                   s = this.getSelectionAfterText();
                                   if (!s.isEmpty() && this.startSelection == this.endSelection) {
                                        s = s.substring(1);
                                   }

                                   this.setText(this.getSelectionBeforeText() + s);
                                   this.endSelection = this.cursorPosition = this.startSelection;
                              } else if (i == 14) {
                                   s = this.getSelectionBeforeText();
                                   if (this.startSelection > 0 && this.startSelection == this.endSelection) {
                                        s = s.substring(0, s.length() - 1);
                                        --this.startSelection;
                                   }

                                   this.setText(s + this.getSelectionAfterText());
                                   this.endSelection = this.cursorPosition = this.startSelection;
                              } else if (GuiScreen.func_175277_d(i)) {
                                   if (this.startSelection != this.endSelection) {
                                        NoppesStringUtils.setClipboardContents(this.text.substring(this.startSelection, this.endSelection));
                                        s = this.getSelectionBeforeText();
                                        this.setText(s + this.getSelectionAfterText());
                                        this.endSelection = this.startSelection = this.cursorPosition = s.length();
                                   }

                              } else if (GuiScreen.func_175280_f(i)) {
                                   if (this.startSelection != this.endSelection) {
                                        NoppesStringUtils.setClipboardContents(this.text.substring(this.startSelection, this.endSelection));
                                   }

                              } else if (GuiScreen.func_175279_e(i)) {
                                   this.addText(NoppesStringUtils.getClipboardContents());
                              } else {
                                   GuiTextArea.UndoData data;
                                   if (i == 44 && GuiScreen.func_146271_m()) {
                                        if (!this.undoList.isEmpty()) {
                                             this.undoing = true;
                                             this.redoList.add(new GuiTextArea.UndoData(this.text, this.cursorPosition));
                                             data = (GuiTextArea.UndoData)this.undoList.remove(this.undoList.size() - 1);
                                             this.setText(data.text);
                                             this.endSelection = this.startSelection = this.cursorPosition = data.cursorPosition;
                                             this.undoing = false;
                                        }
                                   } else if (i == 21 && GuiScreen.func_146271_m()) {
                                        if (!this.redoList.isEmpty()) {
                                             this.undoing = true;
                                             this.undoList.add(new GuiTextArea.UndoData(this.text, this.cursorPosition));
                                             data = (GuiTextArea.UndoData)this.redoList.remove(this.redoList.size() - 1);
                                             this.setText(data.text);
                                             this.endSelection = this.startSelection = this.cursorPosition = data.cursorPosition;
                                             this.undoing = false;
                                        }
                                   } else {
                                        if (i == 15) {
                                             this.addText("    ");
                                        }

                                        if (i == 28) {
                                             this.addText(Character.toString('\n') + this.getIndentCurrentLine());
                                        }

                                        if (ChatAllowedCharacters.func_71566_a(c)) {
                                             this.addText(Character.toString(c));
                                        }

                                   }
                              }
                         }
                    } else {
                         j = 1;
                         if (GuiScreen.func_146271_m()) {
                              m = this.container.regexWord.matcher(this.text.substring(this.cursorPosition));
                              if (m.find() && m.start() > 0 || m.find()) {
                                   j = m.start();
                              }
                         }

                         this.setCursor(this.cursorPosition + j, GuiScreen.func_146272_n());
                    }
               }
          }
     }

     private String getIndentCurrentLine() {
          Iterator var1 = this.container.lines.iterator();

          TextContainer.LineData data;
          do {
               if (!var1.hasNext()) {
                    return "";
               }

               data = (TextContainer.LineData)var1.next();
          } while(this.cursorPosition <= data.start || this.cursorPosition > data.end);

          int i;
          for(i = 0; i < data.text.length() && data.text.charAt(i) == ' '; ++i) {
          }

          return data.text.substring(0, i);
     }

     private void setCursor(int i, boolean select) {
          i = Math.min(Math.max(i, 0), this.text.length());
          if (i != this.cursorPosition) {
               if (!select) {
                    this.endSelection = this.startSelection = this.cursorPosition = i;
               } else {
                    int diff = this.cursorPosition - i;
                    if (this.cursorPosition == this.startSelection) {
                         this.startSelection -= diff;
                    } else if (this.cursorPosition == this.endSelection) {
                         this.endSelection -= diff;
                    }

                    if (this.startSelection > this.endSelection) {
                         int j = this.endSelection;
                         this.endSelection = this.startSelection;
                         this.startSelection = j;
                    }

                    this.cursorPosition = i;
               }
          }
     }

     private void addText(String s) {
          this.setText(this.getSelectionBeforeText() + s + this.getSelectionAfterText());
          this.endSelection = this.startSelection = this.cursorPosition = this.startSelection + s.length();
     }

     private int cursorUp() {
          for(int i = 0; i < this.container.lines.size(); ++i) {
               TextContainer.LineData data = (TextContainer.LineData)this.container.lines.get(i);
               if (this.cursorPosition >= data.start && this.cursorPosition < data.end) {
                    if (i == 0) {
                         return 0;
                    }

                    int var10000 = this.cursorPosition - data.start;
                    return this.getSelectionPos(this.x + 1 + font.width(data.text.substring(0, this.cursorPosition - data.start)), this.y + 1 + (i - 1 - this.scrolledLine) * this.container.lineHeight);
               }
          }

          return 0;
     }

     private int cursorDown() {
          for(int i = 0; i < this.container.lines.size(); ++i) {
               TextContainer.LineData data = (TextContainer.LineData)this.container.lines.get(i);
               if (this.cursorPosition >= data.start && this.cursorPosition < data.end) {
                    int var10000 = this.cursorPosition - data.start;
                    return this.getSelectionPos(this.x + 1 + font.width(data.text.substring(0, this.cursorPosition - data.start)), this.y + 1 + (i + 1 - this.scrolledLine) * this.container.lineHeight);
               }
          }

          return this.text.length();
     }

     public String getSelectionBeforeText() {
          return this.startSelection == 0 ? "" : this.text.substring(0, this.startSelection);
     }

     public String getSelectionAfterText() {
          return this.text.substring(this.endSelection);
     }

     public boolean mouseClicked(int xMouse, int yMouse, int mouseButton) {
          this.active = xMouse >= this.x && xMouse < this.x + this.width && yMouse >= this.y && yMouse < this.y + this.height;
          if (this.active) {
               this.startSelection = this.endSelection = this.cursorPosition = this.getSelectionPos(xMouse, yMouse);
               this.clicked = mouseButton == 0;
               this.doubleClicked = false;
               long time = System.currentTimeMillis();
               if (this.clicked && this.container.linesCount * this.container.lineHeight > this.height && xMouse > this.x + this.width - 8) {
                    this.clicked = false;
                    this.clickScrolling = true;
               } else if (time - this.lastClicked < 500L) {
                    this.doubleClicked = true;
                    Matcher m = this.container.regexWord.matcher(this.text);

                    while(m.find()) {
                         if (this.cursorPosition > m.start() && this.cursorPosition < m.end()) {
                              this.startSelection = m.start();
                              this.endSelection = m.end();
                              break;
                         }
                    }
               }

               this.lastClicked = time;
          }

          return this.active;
     }

     public void updateScreen() {
          ++this.cursorCounter;
          int k2 = Mouse.getDWheel();
          if (k2 != 0) {
               this.scrolledLine += k2 > 0 ? -1 : 1;
               this.scrolledLine = Math.max(Math.min(this.scrolledLine, this.container.linesCount - this.height / this.container.lineHeight), 0);
          }

     }

     public void setText(String text) {
          text = text.replace("\r", "");
          if (this.text == null || !this.text.equals(text)) {
               if (this.listener != null) {
                    this.listener.textUpdate(text);
               }

               if (!this.undoing) {
                    this.undoList.add(new GuiTextArea.UndoData(this.text, this.cursorPosition));
                    this.redoList.clear();
               }

               this.text = text;
               this.container = new TextContainer(text);
               this.container.init(font, this.width, this.height);
               if (this.enableCodeHighlighting) {
                    this.container.formatCodeText();
               }

               if (this.scrolledLine > this.container.linesCount - this.container.visibleLines) {
                    this.scrolledLine = Math.max(0, this.container.linesCount - this.container.visibleLines);
               }

          }
     }

     public String getText() {
          return this.text;
     }

     public boolean isEnabled() {
          return this.enabled && this.visible;
     }

     public boolean hasVerticalScrollbar() {
          return this.container.visibleLines < this.container.linesCount;
     }

     public void enableCodeHighlighting() {
          this.enableCodeHighlighting = true;
          this.container.formatCodeText();
     }

     public void setListener(ITextChangeListener listener) {
          this.listener = listener;
     }

     public boolean isActive() {
          return this.active;
     }

     static {
          font = new TrueTypeFont(new Font("Arial Unicode MS", 0, CustomNpcs.FontSize), 1.0F);
     }

     class UndoData {
          public String text;
          public int cursorPosition;

          public UndoData(String text, int cursorPosition) {
               this.text = text;
               this.cursorPosition = cursorPosition;
          }
     }
}
