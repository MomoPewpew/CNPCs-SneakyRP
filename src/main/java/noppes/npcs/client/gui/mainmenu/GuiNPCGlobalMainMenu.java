package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNpcManagePlayerData;
import noppes.npcs.client.gui.global.GuiNpcNaturalSpawns;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCGlobalMainMenu extends GuiNPCInterface2 {
     public GuiNPCGlobalMainMenu(EntityNPCInterface npc) {
          super(npc, 5);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int y = this.guiTop + 10;
          this.addButton(new GuiNpcButton(2, this.guiLeft + 85, y, "global.banks"));
          int var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(3, var10004, y, "menu.factions"));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(4, var10004, y, "dialog.dialogs"));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(11, var10004, y, "quest.quests"));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(12, var10004, y, "global.transport"));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(13, var10004, y, "global.playerdata"));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(14, var10004, y, NoppesStringUtils.translate("global.recipes", "(Broken)")));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(15, var10004, y, NoppesStringUtils.translate("global.naturalspawn", "(WIP)")));
          var10004 = this.guiLeft + 85;
          y += 22;
          this.addButton(new GuiNpcButton(16, var10004, y, "global.linked"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 11) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageQuests);
          }

          if (id == 2) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageBanks);
          }

          if (id == 3) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageFactions);
          }

          if (id == 4) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageDialogs);
          }

          if (id == 12) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageTransport);
          }

          if (id == 13) {
               NoppesUtil.openGUI(this.player, new GuiNpcManagePlayerData(this.npc, this));
          }

          if (id == 14) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, 4, 0, 0);
          }

          if (id == 15) {
               NoppesUtil.openGUI(this.player, new GuiNpcNaturalSpawns(this.npc));
          }

          if (id == 16) {
               NoppesUtil.requestOpenGUI(EnumGuiType.ManageLinked);
          }

     }

     public void save() {
     }
}
