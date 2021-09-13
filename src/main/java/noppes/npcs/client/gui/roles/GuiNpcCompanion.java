package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionTalents;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class GuiNpcCompanion extends GuiNPCInterface2 implements ITextfieldListener, ISliderListener {
     private RoleCompanion role;
     private List talents = new ArrayList();

     public GuiNpcCompanion(EntityNPCInterface npc) {
          super(npc);
          this.role = (RoleCompanion)npc.roleInterface;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.talents = new ArrayList();
          int y = this.guiTop + 4;
          this.addButton(new GuiNpcButton(0, this.guiLeft + 70, y, 90, 20, new String[]{EnumCompanionStage.BABY.name, EnumCompanionStage.CHILD.name, EnumCompanionStage.TEEN.name, EnumCompanionStage.ADULT.name, EnumCompanionStage.FULLGROWN.name}, this.role.stage.ordinal()));
          this.addLabel(new GuiNpcLabel(0, "companion.stage", this.guiLeft + 4, y + 5));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 162, y, 90, 20, "gui.update"));
          int var10004 = this.guiLeft + 70;
          y += 22;
          this.addButton(new GuiNpcButton(2, var10004, y, 90, 20, new String[]{"gui.no", "gui.yes"}, this.role.canAge ? 1 : 0));
          this.addLabel(new GuiNpcLabel(2, "companion.age", this.guiLeft + 4, y + 5));
          if (this.role.canAge) {
               this.addTextField(new GuiNpcTextField(2, this, this.guiLeft + 162, y, 140, 20, this.role.ticksActive + ""));
               this.getTextField(2).numbersOnly = true;
               this.getTextField(2).setMinMaxDefault(0, Integer.MAX_VALUE, 0);
          }

          int var10005 = this.guiLeft + 4;
          y += 26;
          this.talents.add(new GuiNpcCompanionTalents.GuiTalent(this.role, EnumCompanionTalent.INVENTORY, var10005, y));
          this.addSlider(new GuiNpcSlider(this, 10, this.guiLeft + 30, y + 2, 100, 20, (float)this.role.getExp(EnumCompanionTalent.INVENTORY) / 5000.0F));
          var10005 = this.guiLeft + 4;
          y += 26;
          this.talents.add(new GuiNpcCompanionTalents.GuiTalent(this.role, EnumCompanionTalent.ARMOR, var10005, y));
          this.addSlider(new GuiNpcSlider(this, 11, this.guiLeft + 30, y + 2, 100, 20, (float)this.role.getExp(EnumCompanionTalent.ARMOR) / 5000.0F));
          var10005 = this.guiLeft + 4;
          y += 26;
          this.talents.add(new GuiNpcCompanionTalents.GuiTalent(this.role, EnumCompanionTalent.SWORD, var10005, y));
          this.addSlider(new GuiNpcSlider(this, 12, this.guiLeft + 30, y + 2, 100, 20, (float)this.role.getExp(EnumCompanionTalent.SWORD) / 5000.0F));
          Iterator var2 = this.talents.iterator();

          while(var2.hasNext()) {
               GuiNpcCompanionTalents.GuiTalent gui = (GuiNpcCompanionTalents.GuiTalent)var2.next();
               gui.func_146280_a(this.field_146297_k, this.field_146294_l, this.field_146295_m);
          }

     }

     public void buttonEvent(GuiButton guibutton) {
          GuiNpcButton button;
          if (guibutton.field_146127_k == 0) {
               button = (GuiNpcButton)guibutton;
               this.role.matureTo(EnumCompanionStage.values()[button.getValue()]);
               if (this.role.canAge) {
                    this.role.ticksActive = (long)this.role.stage.matureAge;
               }

               this.func_73866_w_();
          }

          if (guibutton.field_146127_k == 1) {
               Client.sendData(EnumPacketServer.RoleCompanionUpdate, this.role.stage.ordinal());
          }

          if (guibutton.field_146127_k == 2) {
               button = (GuiNpcButton)guibutton;
               this.role.canAge = button.getValue() == 1;
               this.func_73866_w_();
          }

     }

     public void unFocused(GuiNpcTextField textfield) {
          if (textfield.field_175208_g == 2) {
               this.role.ticksActive = (long)textfield.getInteger();
          }

     }

     public void func_73863_a(int i, int j, float f) {
          super.func_73863_a(i, j, f);
          Iterator var4 = (new ArrayList(this.talents)).iterator();

          while(var4.hasNext()) {
               GuiNpcCompanionTalents.GuiTalent talent = (GuiNpcCompanionTalents.GuiTalent)var4.next();
               talent.func_73863_a(i, j, f);
          }

     }

     public void elementClicked() {
     }

     public void save() {
          Client.sendData(EnumPacketServer.RoleSave, this.role.writeToNBT(new NBTTagCompound()));
     }

     public void mouseDragged(GuiNpcSlider slider) {
          if (slider.sliderValue <= 0.0F) {
               slider.setString("gui.disabled");
               this.role.talents.remove(EnumCompanionTalent.values()[slider.field_146127_k - 10]);
          } else {
               slider.field_146126_j = (int)(slider.sliderValue * 50.0F) * 100 + " exp";
               this.role.setExp(EnumCompanionTalent.values()[slider.field_146127_k - 10], (int)(slider.sliderValue * 50.0F) * 100);
          }

     }

     public void mousePressed(GuiNpcSlider slider) {
     }

     public void mouseReleased(GuiNpcSlider slider) {
     }
}
