package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestLocation;

public class GuiNpcQuestTypeLocation extends SubGuiInterface implements ITextfieldListener {
     private GuiScreen parent;
     private QuestLocation quest;

     public GuiNpcQuestTypeLocation(EntityNPCInterface npc, Quest q, GuiScreen parent) {
          this.npc = npc;
          this.parent = parent;
          this.title = "Quest Location Setup";
          this.quest = (QuestLocation)q.questInterface;
          this.setBackground("menubg.png");
          this.xSize = 256;
          this.ySize = 216;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "Fill in the name of your Location Quest Block", this.guiLeft + 4, this.guiTop + 50));
          this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 70, 180, 20, this.quest.location));
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 92, 180, 20, this.quest.location2));
          this.addTextField(new GuiNpcTextField(2, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 114, 180, 20, this.quest.location3));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 150, this.guiTop + 190, 98, 20, "gui.back"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          super.func_146284_a(guibutton);
          if (guibutton.id == 0) {
               this.close();
          }

     }

     public void save() {
     }

     public void unFocused(GuiNpcTextField textfield) {
          if (textfield.field_175208_g == 0) {
               this.quest.location = textfield.func_146179_b();
          }

          if (textfield.field_175208_g == 1) {
               this.quest.location2 = textfield.func_146179_b();
          }

          if (textfield.field_175208_g == 2) {
               this.quest.location3 = textfield.func_146179_b();
          }

     }
}
