package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumAvailabilityScoreboard;
import noppes.npcs.controllers.data.Availability;

public class SubGuiNpcAvailabilityScoreboard extends SubGuiInterface implements ITextfieldListener {
     private Availability availabitily;
     private boolean selectFaction = false;
     private int slot = 0;

     public SubGuiNpcAvailabilityScoreboard(Availability availabitily) {
          this.availabitily = availabitily;
          this.setBackground("menubg.png");
          this.xSize = 316;
          this.ySize = 216;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(1, "availability.available", this.guiLeft, this.guiTop + 4));
          this.getLabel(1).center(this.xSize);
          int y = this.guiTop + 12;
          this.addTextField(new GuiNpcTextField(10, this, this.guiLeft + 4, y, 140, 20, this.availabitily.scoreboardObjective));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 148, y, 90, 20, new String[]{"availability.smaller", "availability.equals", "availability.bigger"}, this.availabitily.scoreboardType.ordinal()));
          this.addTextField(new GuiNpcTextField(20, this, this.guiLeft + 244, y, 60, 20, this.availabitily.scoreboardValue + ""));
          this.getTextField(20).numbersOnly = true;
          y += 23;
          this.addTextField(new GuiNpcTextField(11, this, this.guiLeft + 4, y, 140, 20, this.availabitily.scoreboard2Objective));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 148, y, 90, 20, new String[]{"availability.smaller", "availability.equals", "availability.bigger"}, this.availabitily.scoreboard2Type.ordinal()));
          this.addTextField(new GuiNpcTextField(21, this, this.guiLeft + 244, y, 60, 20, this.availabitily.scoreboard2Value + ""));
          this.getTextField(21).numbersOnly = true;
          this.addButton(new GuiNpcButton(66, this.guiLeft + 82, this.guiTop + 192, 98, 20, "gui.done"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (guibutton.id == 0) {
               this.availabitily.scoreboardType = EnumAvailabilityScoreboard.values()[button.getValue()];
          }

          if (guibutton.id == 1) {
               this.availabitily.scoreboard2Type = EnumAvailabilityScoreboard.values()[button.getValue()];
          }

          if (guibutton.id == 66) {
               this.close();
          }

     }

     public void unFocused(GuiNpcTextField textfield) {
          if (textfield.field_175208_g == 10) {
               this.availabitily.scoreboardObjective = textfield.field_146216_j;
          }

          if (textfield.field_175208_g == 11) {
               this.availabitily.scoreboard2Objective = textfield.field_146216_j;
          }

          if (textfield.field_175208_g == 20) {
               this.availabitily.scoreboardValue = NoppesStringUtils.parseInt(textfield.field_146216_j, 0);
          }

          if (textfield.field_175208_g == 21) {
               this.availabitily.scoreboard2Value = NoppesStringUtils.parseInt(textfield.field_146216_j, 0);
          }

     }
}
