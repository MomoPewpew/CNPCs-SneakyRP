package noppes.npcs.client.gui.advanced;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCMarks extends GuiNPCInterface2 implements ISubGuiListener {
     private final String[] marks = new String[]{"gui.none", "mark.question", "mark.exclamation", "mark.pointer", "mark.skull", "mark.cross", "mark.star"};
     private MarkData data;
     private MarkData.Mark selectedMark;

     public GuiNPCMarks(EntityNPCInterface npc) {
          super(npc);
          this.data = MarkData.get(npc);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int y = this.guiTop + 14;

          for(int i = 0; i < this.data.marks.size(); ++i) {
               MarkData.Mark mark = (MarkData.Mark)this.data.marks.get(i);
               this.addButton(new GuiButtonBiDirectional(1 + i * 10, this.guiLeft + 6, y, 120, 20, this.marks, mark.type));

               String color;
               for(color = Integer.toHexString(mark.color); color.length() < 6; color = "0" + color) {
               }

               this.addButton(new GuiNpcButton(2 + i * 10, this.guiLeft + 128, y, 60, 20, color));
               this.getButton(2 + i * 10).setTextColor(mark.color);
               this.addButton(new GuiNpcButton(3 + i * 10, this.guiLeft + 190, y, 120, 20, "availability.options"));
               this.addButton(new GuiNpcButton(4 + i * 10, this.guiLeft + 312, y, 40, 20, "X"));
               y += 22;
          }

          if (this.data.marks.size() < 9) {
               this.addButton(new GuiNpcButton(101, this.guiLeft + 6, y + 2, 60, 20, "gui.add"));
          }

     }

     public void buttonEvent(GuiButton button) {
          if (button.id < 90) {
               this.selectedMark = (MarkData.Mark)this.data.marks.get(button.id / 10);
               if (button.id % 10 == 1) {
                    this.selectedMark.type = ((GuiNpcButton)button).getValue();
               }

               if (button.id % 10 == 2) {
                    this.setSubGui(new SubGuiColorSelector(this.selectedMark.color));
               }

               if (button.id % 10 == 3) {
                    this.setSubGui(new SubGuiNpcAvailability(this.selectedMark.availability));
               }

               if (button.id % 10 == 4) {
                    this.data.marks.remove(this.selectedMark);
                    this.func_73866_w_();
               }
          }

          if (button.id == 101) {
               this.data.addMark(0);
               this.func_73866_w_();
          }

     }

     public void subGuiClosed(SubGuiInterface subgui) {
          if (subgui instanceof SubGuiColorSelector) {
               this.selectedMark.color = ((SubGuiColorSelector)subgui).color;
               this.func_73866_w_();
          }

     }

     public void save() {
          Client.sendData(EnumPacketServer.MainmenuAdvancedMarkData, this.data.getNBT());
     }
}
