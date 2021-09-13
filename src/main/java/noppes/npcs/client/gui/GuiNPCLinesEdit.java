package noppes.npcs.client.gui;

import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.Lines;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCLinesEdit extends GuiNPCInterface2 implements IGuiData, ISubGuiListener {
     private Lines lines;
     private int selectedId = -1;
     private GuiSoundSelection gui;

     public GuiNPCLinesEdit(EntityNPCInterface npc, Lines lines) {
          super(npc);
          this.lines = lines;
          Client.sendData(EnumPacketServer.MainmenuAdvancedGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();

          for(int i = 0; i < 8; ++i) {
               String text = "";
               String sound = "";
               if (this.lines.lines.containsKey(i)) {
                    Line line = (Line)this.lines.lines.get(i);
                    text = line.getText();
                    sound = line.getSound();
               }

               this.addTextField(new GuiNpcTextField(i, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 4 + i * 24, 200, 20, text));
               this.addTextField(new GuiNpcTextField(i + 8, this, this.field_146289_q, this.guiLeft + 208, this.guiTop + 4 + i * 24, 146, 20, sound));
               this.addButton(new GuiNpcButton(i, this.guiLeft + 358, this.guiTop + 4 + i * 24, 60, 20, "mco.template.button.select"));
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          this.selectedId = button.id + 8;
          this.setSubGui(new GuiSoundSelection(this.getTextField(this.selectedId).func_146179_b()));
     }

     public void setGuiData(NBTTagCompound compound) {
          this.npc.advanced.readToNBT(compound);
          this.func_73866_w_();
     }

     private void saveLines() {
          HashMap lines = new HashMap();

          for(int i = 0; i < 8; ++i) {
               GuiNpcTextField tf = this.getTextField(i);
               GuiNpcTextField tf2 = this.getTextField(i + 8);
               if (!tf.isEmpty() || !tf2.isEmpty()) {
                    Line line = new Line();
                    line.setText(tf.func_146179_b());
                    line.setSound(tf2.func_146179_b());
                    lines.put(i, line);
               }
          }

          this.lines.lines = lines;
     }

     public void save() {
          this.saveLines();
          Client.sendData(EnumPacketServer.MainmenuAdvancedSave, this.npc.advanced.writeToNBT(new NBTTagCompound()));
     }

     public void subGuiClosed(SubGuiInterface subgui) {
          GuiSoundSelection gss = (GuiSoundSelection)subgui;
          if (gss.selectedResource != null) {
               this.getTextField(this.selectedId).func_146180_a(gss.selectedResource.toString());
               this.saveLines();
               this.func_73866_w_();
          }

     }
}
