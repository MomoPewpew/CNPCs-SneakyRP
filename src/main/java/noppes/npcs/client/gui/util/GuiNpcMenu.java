package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.input.Keyboard;

public class GuiNpcMenu implements GuiYesNoCallback {
     private GuiScreen parent;
     private GuiMenuTopButton[] topButtons = new GuiMenuTopButton[0];
     private int activeMenu;
     private EntityNPCInterface npc;

     public GuiNpcMenu(GuiScreen parent, int activeMenu, EntityNPCInterface npc) {
          this.parent = parent;
          this.activeMenu = activeMenu;
          this.npc = npc;
     }

     public void initGui(int guiLeft, int guiTop, int width) {
          Keyboard.enableRepeatEvents(true);
          GuiMenuTopButton display = new GuiMenuTopButton(1, guiLeft + 4, guiTop - 17, "menu.display");
          GuiMenuTopButton stats = new GuiMenuTopButton(2, display.x + display.getWidth(), guiTop - 17, "menu.stats");
          GuiMenuTopButton ai = new GuiMenuTopButton(6, stats.x + stats.getWidth(), guiTop - 17, "menu.ai");
          GuiMenuTopButton inv = new GuiMenuTopButton(3, ai.x + ai.getWidth(), guiTop - 17, "menu.inventory");
          GuiMenuTopButton advanced = new GuiMenuTopButton(4, inv.x + inv.getWidth(), guiTop - 17, "menu.advanced");
          GuiMenuTopButton global = new GuiMenuTopButton(5, advanced.x + advanced.getWidth(), guiTop - 17, "menu.global");
          GuiMenuTopButton close = new GuiMenuTopButton(0, guiLeft + width - 22, guiTop - 17, "X");
          GuiMenuTopButton delete = new GuiMenuTopButton(66, guiLeft + width - 72, guiTop - 17, "selectWorld.deleteButton");
          delete.x = close.x - delete.getWidth();
          this.topButtons = new GuiMenuTopButton[]{display, stats, ai, inv, advanced, global, close, delete};
          GuiMenuTopButton[] var12 = this.topButtons;
          int var13 = var12.length;

          for(int var14 = 0; var14 < var13; ++var14) {
               GuiMenuTopButton button = var12[var14];
               button.active = button.id == this.activeMenu;
          }

     }

     private void topButtonPressed(GuiMenuTopButton button) {
          if (!button.displayString.equals(this.activeMenu)) {
               Minecraft mc = Minecraft.getMinecraft();
               NoppesUtil.clickSound();
               int id = button.id;
               if (id == 0) {
                    this.close();
               } else if (id == 66) {
                    GuiYesNo guiyesno = new GuiYesNo(this, "", I18n.translateToLocal("gui.deleteMessage"), 0);
                    mc.displayGuiScreen(guiyesno);
               } else {
                    this.save();
                    if (id == 1) {
                         CustomNpcs.proxy.openGui(this.npc, EnumGuiType.MainMenuDisplay);
                    } else if (id == 2) {
                         CustomNpcs.proxy.openGui(this.npc, EnumGuiType.MainMenuStats);
                    } else if (id == 3) {
                         NoppesUtil.requestOpenGUI(EnumGuiType.MainMenuInv);
                    } else if (id == 4) {
                         CustomNpcs.proxy.openGui(this.npc, EnumGuiType.MainMenuAdvanced);
                    } else if (id == 5) {
                         CustomNpcs.proxy.openGui(this.npc, EnumGuiType.MainMenuGlobal);
                    } else if (id == 6) {
                         CustomNpcs.proxy.openGui(this.npc, EnumGuiType.MainMenuAI);
                    }

                    this.activeMenu = id;
               }
          }
     }

     private void save() {
          GuiNpcTextField.unfocus();
          if (this.parent instanceof GuiContainerNPCInterface2) {
               ((GuiContainerNPCInterface2)this.parent).save();
          }

          if (this.parent instanceof GuiNPCInterface2) {
               ((GuiNPCInterface2)this.parent).save();
          }

     }

     private void close() {
          if (this.parent instanceof GuiContainerNPCInterface2) {
               ((GuiContainerNPCInterface2)this.parent).close();
          }

          if (this.parent instanceof GuiNPCInterface2) {
               ((GuiNPCInterface2)this.parent).close();
          }

          if (this.npc != null) {
               this.npc.reset();
               Client.sendData(EnumPacketServer.NpcMenuClose);
          }

     }

     public void mouseClicked(int i, int j, int k) {
          if (k == 0) {
               Minecraft mc = Minecraft.getMinecraft();
               GuiMenuTopButton[] var5 = this.topButtons;
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                    GuiMenuTopButton button = var5[var7];
                    if (button.mousePressed(mc, i, j)) {
                         this.topButtonPressed(button);
                    }
               }
          }

     }

     public void drawElements(FontRenderer fontRenderer, int i, int j, Minecraft mc, float f) {
          GuiMenuTopButton[] var6 = this.topButtons;
          int var7 = var6.length;

          for(int var8 = 0; var8 < var7; ++var8) {
               GuiMenuTopButton button = var6[var8];
               button.drawButton(mc, i, j, f);
          }

     }

     public void confirmClicked(boolean flag, int i) {
          Minecraft mc = Minecraft.getMinecraft();
          if (flag) {
               Client.sendData(EnumPacketServer.Delete);
               mc.displayGuiScreen((GuiScreen)null);
               mc.setIngameFocus();
          } else {
               NoppesUtil.openGUI(mc.player, this.parent);
          }

     }
}
