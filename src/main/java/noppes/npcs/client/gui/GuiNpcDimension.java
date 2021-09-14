package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNpcDimension extends GuiNPCInterface implements IScrollData {
     private GuiCustomScroll scroll;
     private HashMap data = new HashMap();

     public GuiNpcDimension() {
          this.xSize = 256;
          this.setBackground("menubg.png");
     }

     public void initPacket() {
          Client.sendData(EnumPacketServer.DimensionsGet);
     }

     public void initGui() {
          super.initGui();
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setSize(165, 208);
          }

          this.scroll.guiLeft = this.guiLeft + 4;
          this.scroll.guiTop = this.guiTop + 4;
          this.addScroll(this.scroll);
          String title = I18n.translateToLocal("Dimensions");
          int x = (this.xSize - this.fontRenderer.getStringWidth(title)) / 2;
          this.addLabel(new GuiNpcLabel(0, title, this.guiLeft + x, this.guiTop - 8));
          this.addButton(new GuiNpcButton(4, this.guiLeft + 170, this.guiTop + 72, 82, 20, "remote.tp"));
     }

     public void confirmClicked(boolean flag, int i) {
          if (flag) {
               Client.sendData(EnumPacketServer.RemoteDelete, this.data.get(this.scroll.getSelected()));
          }

          NoppesUtil.openGUI(this.player, this);
     }

     protected void actionPerformed(GuiButton guibutton) {
          int id = guibutton.id;
          if (this.data.containsKey(this.scroll.getSelected())) {
               if (id == 4) {
                    Client.sendData(EnumPacketServer.DimensionTeleport, this.data.get(this.scroll.getSelected()));
                    this.close();
               }

          }
     }

     public void mouseClicked(int i, int j, int k) {
          super.mouseClicked(i, j, k);
          this.scroll.mouseClicked(i, j, k);
     }

     public void keyTyped(char c, int i) {
          if (i == 1 || this.isInventoryKey(i)) {
               this.close();
          }

     }

     public void save() {
     }

     public void setData(Vector list, HashMap data) {
          this.scroll.setList(list);
          this.data = data;
     }

     public void setSelected(String selected) {
          this.getButton(3).setDisplayText(selected);
     }
}
