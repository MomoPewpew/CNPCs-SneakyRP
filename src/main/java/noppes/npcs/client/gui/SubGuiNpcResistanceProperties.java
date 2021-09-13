package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.Resistances;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class SubGuiNpcResistanceProperties extends SubGuiInterface implements ISliderListener {
     private Resistances resistances;

     public SubGuiNpcResistanceProperties(Resistances resistances) {
          this.resistances = resistances;
          this.setBackground("menubg.png");
          this.xSize = 256;
          this.ySize = 216;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "enchantment.knockback", this.guiLeft + 4, this.guiTop + 15));
          this.addSlider(new GuiNpcSlider(this, 0, this.guiLeft + 94, this.guiTop + 10, (int)(this.resistances.knockback * 100.0F - 100.0F) + "%", this.resistances.knockback / 2.0F));
          this.addLabel(new GuiNpcLabel(1, "item.arrow.name", this.guiLeft + 4, this.guiTop + 37));
          this.addSlider(new GuiNpcSlider(this, 1, this.guiLeft + 94, this.guiTop + 32, (int)(this.resistances.arrow * 100.0F - 100.0F) + "%", this.resistances.arrow / 2.0F));
          this.addLabel(new GuiNpcLabel(2, "stats.melee", this.guiLeft + 4, this.guiTop + 59));
          this.addSlider(new GuiNpcSlider(this, 2, this.guiLeft + 94, this.guiTop + 54, (int)(this.resistances.melee * 100.0F - 100.0F) + "%", this.resistances.melee / 2.0F));
          this.addLabel(new GuiNpcLabel(3, "stats.explosion", this.guiLeft + 4, this.guiTop + 81));
          this.addSlider(new GuiNpcSlider(this, 3, this.guiLeft + 94, this.guiTop + 76, (int)(this.resistances.explosion * 100.0F - 100.0F) + "%", this.resistances.explosion / 2.0F));
          this.addButton(new GuiNpcButton(66, this.guiLeft + 190, this.guiTop + 190, 60, 20, "gui.done"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.field_146127_k;
          if (id == 66) {
               this.close();
          }

     }

     public void mouseDragged(GuiNpcSlider slider) {
          slider.field_146126_j = (int)(slider.sliderValue * 200.0F - 100.0F) + "%";
     }

     public void mousePressed(GuiNpcSlider slider) {
     }

     public void mouseReleased(GuiNpcSlider slider) {
          if (slider.field_146127_k == 0) {
               this.resistances.knockback = slider.sliderValue * 2.0F;
          }

          if (slider.field_146127_k == 1) {
               this.resistances.arrow = slider.sliderValue * 2.0F;
          }

          if (slider.field_146127_k == 2) {
               this.resistances.melee = slider.sliderValue * 2.0F;
          }

          if (slider.field_146127_k == 3) {
               this.resistances.explosion = slider.sliderValue * 2.0F;
          }

     }
}
