package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCTransportCategoryEdit extends GuiNPCInterface {
     private GuiScreen parent;
     private String name;
     private int id;

     public GuiNPCTransportCategoryEdit(EntityNPCInterface npc, GuiScreen parent, String name, int id) {
          super(npc);
          this.parent = parent;
          this.name = name;
          this.id = id;
          this.title = "Npc Transport Category";
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.width / 2 - 40, 100, 140, 20, this.name));
          this.addLabel(new GuiNpcLabel(1, "Title:", this.width / 2 - 100 + 4, 105, 16777215));
          this.addButton(new GuiNpcButton(2, this.width / 2 - 100, 210, 98, 20, "gui.back"));
          this.addButton(new GuiNpcButton(3, this.width / 2 + 2, 210, 98, 20, "Save"));
     }

     public void func_73863_a(int i, int j, float f) {
          super.func_73863_a(i, j, f);
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 2) {
               NoppesUtil.openGUI(this.player, this.parent);
               Client.sendData(EnumPacketServer.TransportCategoriesGet);
          }

          if (id == 3) {
               this.save();
               NoppesUtil.openGUI(this.player, this.parent);
               Client.sendData(EnumPacketServer.TransportCategoriesGet);
          }

     }

     public void save() {
          String name = this.getTextField(1).func_146179_b();
          if (!name.trim().isEmpty()) {
               Client.sendData(EnumPacketServer.TransportCategorySave, name, this.id);
          }
     }
}
