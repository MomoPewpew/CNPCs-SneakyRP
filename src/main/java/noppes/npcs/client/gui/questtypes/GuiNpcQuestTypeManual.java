package noppes.npcs.client.gui.questtypes;

import java.util.Iterator;
import java.util.TreeMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestManual;

public class GuiNpcQuestTypeManual extends SubGuiInterface implements ITextfieldListener {
     private GuiScreen parent;
     private QuestManual quest;
     private GuiNpcTextField lastSelected;

     public GuiNpcQuestTypeManual(EntityNPCInterface npc, Quest q, GuiScreen parent) {
          this.npc = npc;
          this.parent = parent;
          this.title = "Quest Manual Setup";
          this.quest = (QuestManual)q.questInterface;
          this.setBackground("menubg.png");
          this.xSize = 356;
          this.ySize = 216;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int i = 0;
          this.addLabel(new GuiNpcLabel(0, "You can fill in npc or player names too", this.guiLeft + 4, this.guiTop + 50));

          for(Iterator var2 = this.quest.manuals.keySet().iterator(); var2.hasNext(); ++i) {
               String name = (String)var2.next();
               this.addTextField(new GuiNpcTextField(i, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 70 + i * 22, 180, 20, name));
               this.addTextField(new GuiNpcTextField(i + 3, this, this.field_146289_q, this.guiLeft + 186, this.guiTop + 70 + i * 22, 24, 20, this.quest.manuals.get(name) + ""));
               this.getTextField(i + 3).numbersOnly = true;
               this.getTextField(i + 3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
          }

          while(i < 3) {
               this.addTextField(new GuiNpcTextField(i, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 70 + i * 22, 180, 20, ""));
               this.addTextField(new GuiNpcTextField(i + 3, this, this.field_146289_q, this.guiLeft + 186, this.guiTop + 70 + i * 22, 24, 20, "1"));
               this.getTextField(i + 3).numbersOnly = true;
               this.getTextField(i + 3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
               ++i;
          }

          this.addButton(new GuiNpcButton(0, this.guiLeft + 4, this.guiTop + 140, 98, 20, "gui.back"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          super.func_146284_a(guibutton);
          if (guibutton.field_146127_k == 0) {
               this.close();
          }

     }

     public void save() {
     }

     public void unFocused(GuiNpcTextField guiNpcTextField) {
          if (guiNpcTextField.field_175208_g < 3) {
               this.lastSelected = guiNpcTextField;
          }

          this.saveTargets();
     }

     private void saveTargets() {
          TreeMap map = new TreeMap();

          for(int i = 0; i < 3; ++i) {
               String name = this.getTextField(i).func_146179_b();
               if (!name.isEmpty()) {
                    map.put(name, this.getTextField(i + 3).getInteger());
               }
          }

          this.quest.manuals = map;
     }
}
