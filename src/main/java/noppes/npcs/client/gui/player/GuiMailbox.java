package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;

public class GuiMailbox extends GuiNPCInterface implements IGuiData, ICustomScrollListener, GuiYesNoCallback {
     private GuiCustomScroll scroll;
     private PlayerMailData data;
     private PlayerMail selected;

     public GuiMailbox() {
          this.xSize = 256;
          this.setBackground("menubg.png");
          NoppesUtilPlayer.sendData(EnumPlayerPacket.MailGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setSize(165, 186);
          }

          this.scroll.guiLeft = this.guiLeft + 4;
          this.scroll.guiTop = this.guiTop + 4;
          this.addScroll(this.scroll);
          String title = I18n.func_74838_a("mailbox.name");
          int x = (this.xSize - this.field_146289_q.func_78256_a(title)) / 2;
          this.addLabel(new GuiNpcLabel(0, title, this.guiLeft + x, this.guiTop - 8));
          if (this.selected != null) {
               this.addLabel(new GuiNpcLabel(3, I18n.func_74838_a("mailbox.sender") + ":", this.guiLeft + 170, this.guiTop + 6));
               this.addLabel(new GuiNpcLabel(1, this.selected.sender, this.guiLeft + 174, this.guiTop + 18));
               this.addLabel(new GuiNpcLabel(2, I18n.func_74837_a("mailbox.timesend", new Object[]{this.getTimePast()}), this.guiLeft + 174, this.guiTop + 30));
          }

          this.addButton(new GuiNpcButton(0, this.guiLeft + 4, this.guiTop + 192, 82, 20, "mailbox.read"));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 88, this.guiTop + 192, 82, 20, "selectWorld.deleteButton"));
          this.getButton(1).setEnabled(this.selected != null);
     }

     private String getTimePast() {
          int minutes;
          if (this.selected.timePast > 86400000L) {
               minutes = (int)(this.selected.timePast / 86400000L);
               return minutes == 1 ? minutes + " " + I18n.func_74838_a("mailbox.day") : minutes + " " + I18n.func_74838_a("mailbox.days");
          } else if (this.selected.timePast > 3600000L) {
               minutes = (int)(this.selected.timePast / 3600000L);
               return minutes == 1 ? minutes + " " + I18n.func_74838_a("mailbox.hour") : minutes + " " + I18n.func_74838_a("mailbox.hours");
          } else {
               minutes = (int)(this.selected.timePast / 60000L);
               return minutes == 1 ? minutes + " " + I18n.func_74838_a("mailbox.minutes") : minutes + " " + I18n.func_74838_a("mailbox.minutes");
          }
     }

     public void func_73878_a(boolean flag, int i) {
          if (flag && this.selected != null) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.MailDelete, this.selected.time, this.selected.sender);
               this.selected = null;
          }

          NoppesUtil.openGUI(this.player, this);
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.field_146127_k;
          if (this.scroll.selected >= 0) {
               if (id == 0) {
                    GuiMailmanWrite.parent = this;
                    GuiMailmanWrite.mail = this.selected;
                    NoppesUtilPlayer.sendData(EnumPlayerPacket.MailboxOpenMail, this.selected.time, this.selected.sender);
                    this.selected = null;
                    this.scroll.selected = -1;
               }

               if (id == 1) {
                    GuiYesNo guiyesno = new GuiYesNo(this, "", I18n.func_74838_a("gui.deleteMessage"), 0);
                    this.displayGuiScreen(guiyesno);
               }

          }
     }

     public void func_73864_a(int i, int j, int k) {
          super.func_73864_a(i, j, k);
          this.scroll.func_73864_a(i, j, k);
     }

     public void func_73869_a(char c, int i) {
          if (i == 1 || this.isInventoryKey(i)) {
               this.close();
          }

     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          PlayerMailData data = new PlayerMailData();
          data.loadNBTData(compound);
          List list = new ArrayList();
          Collections.sort(data.playermail, (o1, o2) -> {
               if (o1.time == o2.time) {
                    return 0;
               } else {
                    return o1.time > o2.time ? -1 : 1;
               }
          });
          Iterator var4 = data.playermail.iterator();

          while(var4.hasNext()) {
               PlayerMail mail = (PlayerMail)var4.next();
               list.add(mail.subject);
          }

          this.data = data;
          this.scroll.clear();
          this.selected = null;
          this.scroll.setUnsortedList(list);
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
          this.selected = (PlayerMail)this.data.playermail.get(guiCustomScroll.selected);
          this.func_73866_w_();
          if (this.selected != null && !this.selected.beenRead) {
               this.selected.beenRead = true;
               NoppesUtilPlayer.sendData(EnumPlayerPacket.MailRead, this.selected.time, this.selected.sender);
          }

     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
