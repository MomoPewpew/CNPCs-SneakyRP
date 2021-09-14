package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcRemoteEditor extends GuiNPCInterface implements IScrollData, GuiYesNoCallback {
     private GuiCustomScroll scroll;
     private HashMap data = new HashMap();

     public GuiNpcRemoteEditor() {
          this.xSize = 256;
          this.setBackground("menubg.png");
     }

     public void initPacket() {
          Client.sendData(EnumPacketServer.RemoteNpcsGet);
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
          String title = I18n.translateToLocal("remote.title");
          int x = (this.xSize - this.fontRenderer.getStringWidth(title)) / 2;
          this.addLabel(new GuiNpcLabel(0, title, this.guiLeft + x, this.guiTop - 8));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 170, this.guiTop + 6, 82, 20, "selectServer.edit"));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 170, this.guiTop + 28, 82, 20, "selectWorld.deleteButton"));
          this.addButton(new GuiNpcButton(2, this.guiLeft + 170, this.guiTop + 50, 82, 20, "remote.reset"));
          this.addButton(new GuiNpcButton(4, this.guiLeft + 170, this.guiTop + 72, 82, 20, "remote.tp"));
          this.addButton(new GuiNpcButton(5, this.guiLeft + 170, this.guiTop + 110, 82, 20, "remote.resetall"));
          this.addButton(new GuiNpcButton(3, this.guiLeft + 170, this.guiTop + 132, 82, 20, "remote.freeze"));
     }

     public void confirmClicked(boolean flag, int i) {
          if (flag) {
               Client.sendData(EnumPacketServer.RemoteDelete, this.data.get(this.scroll.getSelected()));
          }

          NoppesUtil.openGUI(this.player, this);
     }

     protected void actionPerformed(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 3) {
               Client.sendData(EnumPacketServer.RemoteFreeze);
          }

          if (id == 5) {
               Iterator var3 = this.data.values().iterator();

               while(var3.hasNext()) {
                    int ids = (Integer)var3.next();
                    Client.sendData(EnumPacketServer.RemoteReset, ids);
                    Entity entity = this.player.world.getEntityByID(ids);
                    if (entity != null && entity instanceof EntityNPCInterface) {
                         ((EntityNPCInterface)entity).reset();
                    }
               }
          }

          if (this.data.containsKey(this.scroll.getSelected())) {
               if (id == 0) {
                    Client.sendData(EnumPacketServer.RemoteMainMenu, this.data.get(this.scroll.getSelected()));
               }

               if (id == 1) {
                    GuiYesNo guiyesno = new GuiYesNo(this, "", I18n.translateToLocal("gui.deleteMessage"), 0);
                    this.displayGuiScreen(guiyesno);
               }

               if (id == 2) {
                    Client.sendData(EnumPacketServer.RemoteReset, this.data.get(this.scroll.getSelected()));
                    Entity entity = this.player.world.getEntityByID((Integer)this.data.get(this.scroll.getSelected()));
                    if (entity != null && entity instanceof EntityNPCInterface) {
                         ((EntityNPCInterface)entity).reset();
                    }
               }

               if (id == 4) {
                    Client.sendData(EnumPacketServer.RemoteTpToNpc, this.data.get(this.scroll.getSelected()));
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
