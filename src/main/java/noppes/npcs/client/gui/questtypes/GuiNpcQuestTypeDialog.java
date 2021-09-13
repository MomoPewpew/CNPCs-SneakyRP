package noppes.npcs.client.gui.questtypes;

import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.select.GuiDialogSelection;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestDialog;

public class GuiNpcQuestTypeDialog extends SubGuiInterface implements GuiSelectionListener, IGuiData {
     private GuiScreen parent;
     private QuestDialog quest;
     private HashMap data = new HashMap();
     private int selectedSlot;

     public GuiNpcQuestTypeDialog(EntityNPCInterface npc, Quest q, GuiScreen parent) {
          this.npc = npc;
          this.parent = parent;
          this.title = "Quest Dialog Setup";
          this.quest = (QuestDialog)q.questInterface;
          this.setBackground("menubg.png");
          this.xSize = 256;
          this.ySize = 216;
          this.closeOnEsc = true;
          Client.sendData(EnumPacketServer.QuestDialogGetTitle, this.quest.dialogs.containsKey(0) ? this.quest.dialogs.get(0) : -1, this.quest.dialogs.containsKey(1) ? this.quest.dialogs.get(1) : -1, this.quest.dialogs.containsKey(2) ? this.quest.dialogs.get(2) : -1);
     }

     public void func_73866_w_() {
          super.func_73866_w_();

          for(int i = 0; i < 3; ++i) {
               String title = "dialog.selectoption";
               if (this.data.containsKey(i)) {
                    title = (String)this.data.get(i);
               }

               this.addButton(new GuiNpcButton(i + 9, this.guiLeft + 10, 55 + i * 22, 20, 20, "X"));
               this.addButton(new GuiNpcButton(i + 3, this.guiLeft + 34, 55 + i * 22, 210, 20, title));
          }

          this.addButton(new GuiNpcButton(0, this.guiLeft + 150, this.guiTop + 190, 98, 20, "gui.back"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.field_146127_k == 0) {
               this.close();
          }

          int slot;
          if (button.field_146127_k >= 3 && button.field_146127_k < 9) {
               this.selectedSlot = button.field_146127_k - 3;
               slot = -1;
               if (this.quest.dialogs.containsKey(this.selectedSlot)) {
                    slot = (Integer)this.quest.dialogs.get(this.selectedSlot);
               }

               this.setSubGui(new GuiDialogSelection(slot));
          }

          if (button.field_146127_k >= 9 && button.field_146127_k < 15) {
               slot = button.field_146127_k - 9;
               this.quest.dialogs.remove(slot);
               this.data.remove(slot);
               this.save();
               this.func_73866_w_();
          }

     }

     public void save() {
     }

     public void selected(int id, String name) {
          this.quest.dialogs.put(this.selectedSlot, id);
          this.data.put(this.selectedSlot, name);
     }

     public void setGuiData(NBTTagCompound compound) {
          this.data.clear();
          if (compound.func_74764_b("1")) {
               this.data.put(0, compound.func_74779_i("1"));
          }

          if (compound.func_74764_b("2")) {
               this.data.put(1, compound.func_74779_i("2"));
          }

          if (compound.func_74764_b("3")) {
               this.data.put(2, compound.func_74779_i("3"));
          }

          this.func_73866_w_();
     }
}
