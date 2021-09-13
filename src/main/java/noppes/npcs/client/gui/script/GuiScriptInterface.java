package noppes.npcs.client.gui.script;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiTextArea;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextChangeListener;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;

public class GuiScriptInterface extends GuiNPCInterface implements IGuiData, ITextChangeListener {
     private int activeTab = 0;
     public IScriptHandler handler;
     public Map languages = new HashMap();

     public GuiScriptInterface() {
          this.drawDefaultBackground = true;
          this.closeOnEsc = true;
          this.xSize = 420;
          this.setBackground("menubg.png");
     }

     public void func_73866_w_() {
          this.xSize = (int)((double)this.width * 0.88D);
          this.ySize = (int)((double)this.xSize * 0.56D);
          if ((double)this.ySize > (double)this.height * 0.95D) {
               this.ySize = (int)((double)this.height * 0.95D);
               this.xSize = (int)((double)this.ySize / 0.56D);
          }

          this.bgScale = (float)this.xSize / 400.0F;
          super.func_73866_w_();
          this.guiTop += 10;
          int yoffset = (int)((double)this.ySize * 0.02D);
          GuiMenuTopButton top;
          this.addTopButton(top = new GuiMenuTopButton(0, this.guiLeft + 4, this.guiTop - 17, "gui.settings"));

          for(int i = 0; i < this.handler.getScripts().size(); ++i) {
               ScriptContainer var10000 = (ScriptContainer)this.handler.getScripts().get(i);
               this.addTopButton(top = new GuiMenuTopButton(i + 1, top, i + 1 + ""));
          }

          if (this.handler.getScripts().size() < 16) {
               this.addTopButton(new GuiMenuTopButton(12, top, "+"));
          }

          top = this.getTopButton(this.activeTab);
          if (top == null) {
               this.activeTab = 0;
               top = this.getTopButton(0);
          }

          top.active = true;
          if (this.activeTab > 0) {
               ScriptContainer container = (ScriptContainer)this.handler.getScripts().get(this.activeTab - 1);
               GuiTextArea ta = new GuiTextArea(2, this.guiLeft + 1 + yoffset, this.guiTop + yoffset, this.xSize - 108 - yoffset, (int)((double)this.ySize * 0.96D) - yoffset * 2, container == null ? "" : container.script);
               ta.enableCodeHighlighting();
               ta.setListener(this);
               this.add(ta);
               int left = this.guiLeft + this.xSize - 104;
               this.addButton(new GuiNpcButton(102, left, this.guiTop + yoffset, 60, 20, "gui.clear"));
               this.addButton(new GuiNpcButton(101, left + 61, this.guiTop + yoffset, 60, 20, "gui.paste"));
               this.addButton(new GuiNpcButton(100, left, this.guiTop + 21 + yoffset, 60, 20, "gui.copy"));
               this.addButton(new GuiNpcButton(105, left + 61, this.guiTop + 21 + yoffset, 60, 20, "gui.remove"));
               this.addButton(new GuiNpcButton(107, left, this.guiTop + 66 + yoffset, 80, 20, "script.loadscript"));
               GuiCustomScroll scroll = (new GuiCustomScroll(this, 0)).setUnselectable();
               scroll.setSize(100, (int)((double)this.ySize * 0.54D) - yoffset * 2);
               scroll.guiLeft = left;
               scroll.guiTop = this.guiTop + 88 + yoffset;
               if (container != null) {
                    scroll.setList(container.scripts);
               }

               this.addScroll(scroll);
          } else {
               GuiTextArea ta = new GuiTextArea(2, this.guiLeft + 4 + yoffset, this.guiTop + 6 + yoffset, this.xSize - 160 - yoffset, (int)((float)this.ySize * 0.92F) - yoffset * 2, this.getConsoleText());
               ta.enabled = false;
               this.add(ta);
               int left = this.guiLeft + this.xSize - 150;
               this.addButton(new GuiNpcButton(100, left, this.guiTop + 125, 60, 20, "gui.copy"));
               this.addButton(new GuiNpcButton(102, left, this.guiTop + 146, 60, 20, "gui.clear"));
               this.addLabel(new GuiNpcLabel(1, "script.language", left, this.guiTop + 15));
               this.addButton(new GuiNpcButton(103, left + 60, this.guiTop + 10, 80, 20, (String[])this.languages.keySet().toArray(new String[this.languages.keySet().size()]), this.getScriptIndex()));
               this.getButton(103).enabled = this.languages.size() > 0;
               this.addLabel(new GuiNpcLabel(2, "gui.enabled", left, this.guiTop + 36));
               this.addButton(new GuiNpcButton(104, left + 60, this.guiTop + 31, 50, 20, new String[]{"gui.no", "gui.yes"}, this.handler.getEnabled() ? 1 : 0));
               if (this.player.getServer() != null) {
                    this.addButton(new GuiNpcButton(106, left, this.guiTop + 55, 150, 20, "script.openfolder"));
               }

               this.addButton(new GuiNpcButton(109, left, this.guiTop + 78, 80, 20, "gui.website"));
               this.addButton(new GuiNpcButton(112, left + 81, this.guiTop + 78, 80, 20, "gui.forum"));
               this.addButton(new GuiNpcButton(110, left, this.guiTop + 99, 80, 20, "script.apidoc"));
               this.addButton(new GuiNpcButton(111, left + 81, this.guiTop + 99, 80, 20, "script.apisrc"));
          }

          this.xSize = 420;
          this.ySize = 256;
     }

     private String getConsoleText() {
          Map map = this.handler.getConsoleText();
          StringBuilder builder = new StringBuilder();
          Iterator var3 = map.entrySet().iterator();

          while(var3.hasNext()) {
               Entry entry = (Entry)var3.next();
               builder.insert(0, new Date((Long)entry.getKey()) + (String)entry.getValue() + "\n");
          }

          return builder.toString();
     }

     private int getScriptIndex() {
          int i = 0;

          for(Iterator var2 = this.languages.keySet().iterator(); var2.hasNext(); ++i) {
               String language = (String)var2.next();
               if (language.equalsIgnoreCase(this.handler.getLanguage())) {
                    return i;
               }
          }

          return 0;
     }

     public void func_73878_a(boolean flag, int i) {
          if (flag) {
               if (i == 0) {
                    this.openLink("http://www.kodevelopment.nl/minecraft/customnpcs/scripting");
               }

               if (i == 1) {
                    this.openLink("http://www.kodevelopment.nl/customnpcs/api/");
               }

               if (i == 2) {
                    this.openLink("http://www.kodevelopment.nl/minecraft/customnpcs/scripting");
               }

               if (i == 3) {
                    this.openLink("http://www.minecraftforge.net/forum/index.php/board,122.0.html");
               }

               if (i == 10) {
                    this.handler.getScripts().remove(this.activeTab - 1);
                    this.activeTab = 0;
               }
          }

          this.displayGuiScreen(this);
     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.id >= 0 && guibutton.id < 12) {
               this.setScript();
               this.activeTab = guibutton.id;
               this.func_73866_w_();
          }

          if (guibutton.id == 12) {
               this.handler.getScripts().add(new ScriptContainer(this.handler));
               this.activeTab = this.handler.getScripts().size();
               this.func_73866_w_();
          }

          if (guibutton.id == 109) {
               this.displayGuiScreen(new GuiConfirmOpenLink(this, "http://www.kodevelopment.nl/minecraft/customnpcs/scripting", 0, true));
          }

          if (guibutton.id == 110) {
               this.displayGuiScreen(new GuiConfirmOpenLink(this, "http://www.kodevelopment.nl/customnpcs/api/", 1, true));
          }

          if (guibutton.id == 111) {
               this.displayGuiScreen(new GuiConfirmOpenLink(this, "https://github.com/Noppes/CustomNPCsAPI", 2, true));
          }

          if (guibutton.id == 112) {
               this.displayGuiScreen(new GuiConfirmOpenLink(this, "http://www.minecraftforge.net/forum/index.php/board,122.0.html", 3, true));
          }

          if (guibutton.id == 100) {
               NoppesStringUtils.setClipboardContents(((GuiTextArea)this.get(2)).getText());
          }

          if (guibutton.id == 101) {
               ((GuiTextArea)this.get(2)).setText(NoppesStringUtils.getClipboardContents());
          }

          ScriptContainer container;
          if (guibutton.id == 102) {
               if (this.activeTab > 0) {
                    container = (ScriptContainer)this.handler.getScripts().get(this.activeTab - 1);
                    container.script = "";
               } else {
                    this.handler.clearConsole();
               }

               this.func_73866_w_();
          }

          if (guibutton.id == 103) {
               this.handler.setLanguage(((GuiNpcButton)guibutton).field_146126_j);
          }

          if (guibutton.id == 104) {
               this.handler.setEnabled(((GuiNpcButton)guibutton).getValue() == 1);
          }

          if (guibutton.id == 105) {
               GuiYesNo guiyesno = new GuiYesNo(this, "", I18n.translateToLocal("gui.deleteMessage"), 10);
               this.displayGuiScreen(guiyesno);
          }

          if (guibutton.id == 106) {
               NoppesUtil.openFolder(ScriptController.Instance.dir);
          }

          if (guibutton.id == 107) {
               container = (ScriptContainer)this.handler.getScripts().get(this.activeTab - 1);
               if (container == null) {
                    this.handler.getScripts().add(container = new ScriptContainer(this.handler));
               }

               this.setSubGui(new GuiScriptList((List)this.languages.get(this.handler.getLanguage()), container));
          }

          if (guibutton.id == 108) {
               container = (ScriptContainer)this.handler.getScripts().get(this.activeTab - 1);
               if (container != null) {
                    this.setScript();
               }
          }

     }

     private void setScript() {
          if (this.activeTab > 0) {
               ScriptContainer container = (ScriptContainer)this.handler.getScripts().get(this.activeTab - 1);
               if (container == null) {
                    this.handler.getScripts().add(container = new ScriptContainer(this.handler));
               }

               String text = ((GuiTextArea)this.get(2)).getText();
               text = text.replace("\r\n", "\n");
               text = text.replace("\r", "\n");
               container.script = text;
          }

     }

     public void setGuiData(NBTTagCompound compound) {
          NBTTagList data = compound.getTagList("Languages", 10);
          Map languages = new HashMap();

          for(int i = 0; i < data.tagCount(); ++i) {
               NBTTagCompound comp = data.getCompoundTagAt(i);
               List scripts = new ArrayList();
               NBTTagList list = comp.getTagList("Scripts", 8);

               for(int j = 0; j < list.tagCount(); ++j) {
                    scripts.add(list.func_150307_f(j));
               }

               languages.put(comp.getString("Language"), scripts);
          }

          this.languages = languages;
          this.func_73866_w_();
     }

     public void save() {
          this.setScript();
     }

     public void textUpdate(String text) {
          ScriptContainer container = (ScriptContainer)this.handler.getScripts().get(this.activeTab - 1);
          if (container != null) {
               container.script = text;
          }

     }
}
