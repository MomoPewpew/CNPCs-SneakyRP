package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobItemGiver;

public class GuiNpcItemGiver extends GuiContainerNPCInterface2 {
     private JobItemGiver role;

     public GuiNpcItemGiver(EntityNPCInterface npc, ContainerNpcItemGiver container) {
          super(npc, container);
          this.field_147000_g = 200;
          this.role = (JobItemGiver)npc.jobInterface;
          this.setBackground("npcitemgiver.png");
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(new GuiNpcButton(0, this.field_147003_i + 6, this.field_147009_r + 6, 140, 20, new String[]{"Random Item", "All Items", "Give Not Owned Items", "Give When Doesnt Own Any", "Chained"}, this.role.givingMethod));
          this.addButton(new GuiNpcButton(1, this.field_147003_i + 6, this.field_147009_r + 29, 140, 20, new String[]{"Timer", "Give Only Once", "Daily"}, this.role.cooldownType));
          this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.field_147003_i + 55, this.field_147009_r + 54, 90, 20, this.role.cooldown + ""));
          this.getTextField(0).numbersOnly = true;
          this.addLabel(new GuiNpcLabel(0, "Cooldown:", this.field_147003_i + 6, this.field_147009_r + 59));
          this.addLabel(new GuiNpcLabel(1, "Items to give", this.field_147003_i + 46, this.field_147009_r + 79));
          this.getTextField(0).numbersOnly = true;
          int i = 0;

          for(Iterator var2 = this.role.lines.iterator(); var2.hasNext(); ++i) {
               String line = (String)var2.next();
               this.addTextField(new GuiNpcTextField(i + 1, this, this.field_146289_q, this.field_147003_i + 150, this.field_147009_r + 6 + i * 24, 236, 20, line));
          }

          while(i < 3) {
               this.addTextField(new GuiNpcTextField(i + 1, this, this.field_146289_q, this.field_147003_i + 150, this.field_147009_r + 6 + i * 24, 236, 20, ""));
               ++i;
          }

          this.getTextField(0).enabled = this.role.isOnTimer();
          this.getLabel(0).enabled = this.role.isOnTimer();
          this.addLabel(new GuiNpcLabel(4, "availability.options", this.field_147003_i + 180, this.field_147009_r + 101));
          this.addButton(new GuiNpcButton(4, this.field_147003_i + 280, this.field_147009_r + 96, 50, 20, "selectServer.edit"));
     }

     public void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.field_146127_k == 0) {
               this.role.givingMethod = button.getValue();
          }

          if (button.field_146127_k == 1) {
               this.role.cooldownType = button.getValue();
               this.getTextField(0).enabled = this.role.isOnTimer();
               this.getLabel(0).enabled = this.role.isOnTimer();
          }

          if (button.field_146127_k == 4) {
               this.setSubGui(new SubGuiNpcAvailability(this.role.availability));
          }

     }

     public void save() {
          List lines = new ArrayList();

          int cc;
          for(cc = 1; cc < 4; ++cc) {
               GuiNpcTextField tf = this.getTextField(cc);
               if (!tf.isEmpty()) {
                    lines.add(tf.func_146179_b());
               }
          }

          this.role.lines = lines;
          cc = 10;
          if (!this.getTextField(0).isEmpty() && this.getTextField(0).isInteger()) {
               cc = this.getTextField(0).getInteger();
          }

          this.role.cooldown = cc;
          Client.sendData(EnumPacketServer.JobSave, this.role.writeToNBT(new NBTTagCompound()));
     }
}
