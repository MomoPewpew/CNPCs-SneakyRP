package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiTextArea;
import noppes.npcs.client.gui.util.ITextChangeListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class SubGuiNpcTextArea extends SubGuiInterface implements ITextChangeListener {
     public String text;
     public String originalText;
     private GuiTextArea textarea;
     private boolean highlighting;

     public SubGuiNpcTextArea(String text) {
          this.highlighting = false;
          this.text = text;
          this.originalText = text;
          this.setBackground("bgfilled.png");
          this.xSize = 256;
          this.ySize = 256;
          this.closeOnEsc = true;
     }

     public SubGuiNpcTextArea(String originalText, String text) {
          this(text);
          this.originalText = originalText;
     }

     public void initGui() {
          this.xSize = (int)((double)this.width * 0.88D);
          this.ySize = (int)((double)this.xSize * 0.56D);
          if ((double)this.ySize > (double)this.height * 0.95D) {
               this.ySize = (int)((double)this.height * 0.95D);
               this.xSize = (int)((double)this.ySize / 0.56D);
          }

          this.bgScale = (float)this.xSize / 440.0F;
          super.initGui();
          if (this.textarea != null) {
               this.text = this.textarea.getText();
          }

          int yoffset = (int)((double)this.ySize * 0.02D);
          this.textarea = new GuiTextArea(2, this.guiLeft + 1 + yoffset, this.guiTop + yoffset, this.xSize - 100 - yoffset, this.ySize - yoffset * 2, this.text);
          this.textarea.setListener(this);
          if (this.highlighting) {
               this.textarea.enableCodeHighlighting();
          }

          this.add(this.textarea);
          this.buttonList.add(new GuiNpcButton(102, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 20, 56, 20, "gui.clear"));
          this.buttonList.add(new GuiNpcButton(101, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 43, 56, 20, "gui.paste"));
          this.buttonList.add(new GuiNpcButton(100, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 66, 56, 20, "gui.copy"));
          this.buttonList.add(new GuiNpcButton(103, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 89, 56, 20, "remote.reset"));
          this.buttonList.add(new GuiNpcButton(0, this.guiLeft + this.xSize - 90 - yoffset, this.guiTop + 160, 56, 20, "gui.close"));
          this.xSize = 420;
          this.ySize = 256;
     }

     public SubGuiNpcTextArea enableHighlighting() {
          this.highlighting = true;
          return this;
     }

     public void buttonEvent(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 100) {
               NoppesStringUtils.setClipboardContents(this.textarea.getText());
          }

          if (id == 101) {
               this.textarea.setText(NoppesStringUtils.getClipboardContents());
          }

          if (id == 102) {
               this.textarea.setText("");
          }

          if (id == 103) {
               this.textarea.setText(this.originalText);
          }

          if (id == 0) {
               this.close();
          }

     }

     public void textUpdate(String text) {
          this.text = text;
     }
}
