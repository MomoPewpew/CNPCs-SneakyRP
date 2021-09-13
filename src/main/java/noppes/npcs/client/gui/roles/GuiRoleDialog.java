package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleDialog;

public class GuiRoleDialog extends GuiNPCInterface2 implements ISubGuiListener {
     private RoleDialog role;
     private int slot = 0;

     public GuiRoleDialog(EntityNPCInterface npc) {
          super(npc);
          this.role = (RoleDialog)npc.roleInterface;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "dialog.starttext", this.guiLeft + 4, this.guiTop + 10));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 60, this.guiTop + 5, 50, 20, "selectServer.edit"));
          this.addLabel(new GuiNpcLabel(100, "dialog.options", this.guiLeft + 4, this.guiTop + 34));

          for(int i = 1; i <= 6; ++i) {
               int y = this.guiTop + 24 + i * 23;
               this.addLabel(new GuiNpcLabel(i, i + ":", this.guiLeft + 4, y + 5));
               String text = (String)this.role.options.get(i);
               if (text == null) {
                    text = "";
               }

               this.addTextField(new GuiNpcTextField(i, this, this.guiLeft + 16, y, 280, 20, text));
               this.addButton(new GuiNpcButton(i, this.guiLeft + 310, y, 50, 20, "selectServer.edit"));
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.id <= 6) {
               this.save();
               this.slot = guibutton.id;
               String text = this.role.dialog;
               if (this.slot >= 1) {
                    text = (String)this.role.optionsTexts.get(this.slot);
               }

               if (text == null) {
                    text = "";
               }

               this.setSubGui(new SubGuiNpcTextArea(text));
          }

     }

     public void save() {
          HashMap map = new HashMap();

          for(int i = 1; i <= 6; ++i) {
               String text = this.getTextField(i).func_146179_b();
               if (!text.isEmpty()) {
                    map.put(i, text);
               }
          }

          this.role.options = map;
          Client.sendData(EnumPacketServer.RoleSave, this.role.writeToNBT(new NBTTagCompound()));
     }

     public void subGuiClosed(SubGuiInterface subgui) {
          if (subgui instanceof SubGuiNpcTextArea) {
               SubGuiNpcTextArea text = (SubGuiNpcTextArea)subgui;
               if (this.slot == 0) {
                    this.role.dialog = text.text;
               } else if (text.text.isEmpty()) {
                    this.role.optionsTexts.remove(this.slot);
               } else {
                    this.role.optionsTexts.put(this.slot, text.text);
               }
          }

     }
}
