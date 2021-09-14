package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.SubGuiNpcFactionPoints;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageFactions extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData, ISubGuiListener {
     private GuiCustomScroll scrollFactions;
     private HashMap data = new HashMap();
     private Faction faction = new Faction();
     private String selected = null;

     public GuiNPCManageFactions(EntityNPCInterface npc) {
          super(npc);
          Client.sendData(EnumPacketServer.FactionsGet);
     }

     public void initGui() {
          super.initGui();
          this.addButton(new GuiNpcButton(0, this.guiLeft + 368, this.guiTop + 8, 45, 20, "gui.add"));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 368, this.guiTop + 32, 45, 20, "gui.remove"));
          if (this.scrollFactions == null) {
               this.scrollFactions = new GuiCustomScroll(this, 0);
               this.scrollFactions.setSize(143, 208);
          }

          this.scrollFactions.guiLeft = this.guiLeft + 220;
          this.scrollFactions.guiTop = this.guiTop + 4;
          this.addScroll(this.scrollFactions);
          if (this.faction.id != -1) {
               this.addTextField(new GuiNpcTextField(0, this, this.guiLeft + 40, this.guiTop + 4, 136, 20, this.faction.name));
               this.getTextField(0).setMaxStringLength(20);
               this.addLabel(new GuiNpcLabel(0, "gui.name", this.guiLeft + 8, this.guiTop + 9));
               this.addLabel(new GuiNpcLabel(10, "ID", this.guiLeft + 178, this.guiTop + 4));
               this.addLabel(new GuiNpcLabel(11, this.faction.id + "", this.guiLeft + 178, this.guiTop + 14));

               String color;
               for(color = Integer.toHexString(this.faction.color); color.length() < 6; color = "0" + color) {
               }

               this.addButton(new GuiNpcButton(10, this.guiLeft + 40, this.guiTop + 26, 60, 20, color));
               this.addLabel(new GuiNpcLabel(1, "gui.color", this.guiLeft + 8, this.guiTop + 31));
               this.getButton(10).setTextColor(this.faction.color);
               this.addLabel(new GuiNpcLabel(2, "faction.points", this.guiLeft + 8, this.guiTop + 53));
               this.addButton(new GuiNpcButton(2, this.guiLeft + 100, this.guiTop + 48, 45, 20, "selectServer.edit"));
               this.addLabel(new GuiNpcLabel(3, "faction.hidden", this.guiLeft + 8, this.guiTop + 75));
               this.addButton(new GuiNpcButton(3, this.guiLeft + 100, this.guiTop + 70, 45, 20, new String[]{"gui.no", "gui.yes"}, this.faction.hideFaction ? 1 : 0));
               this.addLabel(new GuiNpcLabel(4, "faction.attacked", this.guiLeft + 8, this.guiTop + 97));
               this.addButton(new GuiNpcButton(4, this.guiLeft + 100, this.guiTop + 92, 45, 20, new String[]{"gui.no", "gui.yes"}, this.faction.getsAttacked ? 1 : 0));
               this.addLabel(new GuiNpcLabel(6, "faction.hostiles", this.guiLeft + 8, this.guiTop + 145));
               ArrayList hostileList = new ArrayList(this.scrollFactions.getList());
               hostileList.remove(this.faction.name);
               HashSet set = new HashSet();
               Iterator var4 = this.data.keySet().iterator();

               while(var4.hasNext()) {
                    String s = (String)var4.next();
                    if (!s.equals(this.faction.name) && this.faction.attackFactions.contains(this.data.get(s))) {
                         set.add(s);
                    }
               }

               GuiCustomScroll scrollHostileFactions = new GuiCustomScroll(this, 1, true);
               scrollHostileFactions.setSize(163, 58);
               scrollHostileFactions.guiLeft = this.guiLeft + 4;
               scrollHostileFactions.guiTop = this.guiTop + 154;
               scrollHostileFactions.setList(hostileList);
               scrollHostileFactions.setSelectedList(set);
               this.addScroll(scrollHostileFactions);
          }
     }

     protected void actionPerformed(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.id == 0) {
               this.save();

               String name;
               for(name = I18n.translateToLocal("gui.new"); this.data.containsKey(name); name = name + "_") {
               }

               Faction faction = new Faction(-1, name, 65280, 1000);
               NBTTagCompound compound = new NBTTagCompound();
               faction.writeNBT(compound);
               Client.sendData(EnumPacketServer.FactionSave, compound);
          }

          if (button.id == 1 && this.data.containsKey(this.scrollFactions.getSelected())) {
               Client.sendData(EnumPacketServer.FactionRemove, this.data.get(this.selected));
               this.scrollFactions.clear();
               this.faction = new Faction();
               this.initGui();
          }

          if (button.id == 2) {
               this.setSubGui(new SubGuiNpcFactionPoints(this.faction));
          }

          if (button.id == 3) {
               this.faction.hideFaction = button.getValue() == 1;
          }

          if (button.id == 4) {
               this.faction.getsAttacked = button.getValue() == 1;
          }

          if (button.id == 10) {
               this.setSubGui(new SubGuiColorSelector(this.faction.color));
          }

     }

     public void setGuiData(NBTTagCompound compound) {
          this.faction = new Faction();
          this.faction.readNBT(compound);
          this.setSelected(this.faction.name);
          this.initGui();
     }

     public void setData(Vector list, HashMap data) {
          String name = this.scrollFactions.getSelected();
          this.data = data;
          this.scrollFactions.setList(list);
          if (name != null) {
               this.scrollFactions.setSelected(name);
          }

     }

     public void setSelected(String selected) {
          this.selected = selected;
          this.scrollFactions.setSelected(selected);
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
          if (guiCustomScroll.id == 0) {
               this.save();
               this.selected = this.scrollFactions.getSelected();
               Client.sendData(EnumPacketServer.FactionGet, this.data.get(this.selected));
          } else if (guiCustomScroll.id == 1) {
               HashSet set = new HashSet();
               Iterator var6 = guiCustomScroll.getSelectedList().iterator();

               while(var6.hasNext()) {
                    String s = (String)var6.next();
                    if (this.data.containsKey(s)) {
                         set.add(this.data.get(s));
                    }
               }

               this.faction.attackFactions = set;
               this.save();
          }

     }

     public void save() {
          if (this.selected != null && this.data.containsKey(this.selected) && this.faction != null) {
               NBTTagCompound compound = new NBTTagCompound();
               this.faction.writeNBT(compound);
               Client.sendData(EnumPacketServer.FactionSave, compound);
          }

     }

     public void unFocused(GuiNpcTextField guiNpcTextField) {
          if (this.faction.id != -1) {
               if (guiNpcTextField.id == 0) {
                    String name = guiNpcTextField.getText();
                    if (!name.isEmpty() && !this.data.containsKey(name)) {
                         String old = this.faction.name;
                         this.data.remove(this.faction.name);
                         this.faction.name = name;
                         this.data.put(this.faction.name, this.faction.id);
                         this.selected = name;
                         this.scrollFactions.replace(old, this.faction.name);
                    }
               } else if (guiNpcTextField.id == 1) {
                    boolean var5 = false;

                    int color;
                    try {
                         color = Integer.parseInt(guiNpcTextField.getText(), 16);
                    } catch (NumberFormatException var4) {
                         color = 0;
                    }

                    this.faction.color = color;
                    guiNpcTextField.setTextColor(this.faction.color);
               }

          }
     }

     public void subGuiClosed(SubGuiInterface subgui) {
          if (subgui instanceof SubGuiColorSelector) {
               this.faction.color = ((SubGuiColorSelector)subgui).color;
               this.initGui();
          }

     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
