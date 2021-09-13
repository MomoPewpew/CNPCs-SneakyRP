package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleBank;

public class GuiNpcBankSetup extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {
     private GuiCustomScroll scroll;
     private HashMap data = new HashMap();
     private RoleBank role;

     public GuiNpcBankSetup(EntityNPCInterface npc) {
          super(npc);
          this.role = (RoleBank)npc.roleInterface;
     }

     public void initPacket() {
          Client.sendData(EnumPacketServer.BanksGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
          }

          this.scroll.setSize(200, 152);
          this.scroll.guiLeft = this.guiLeft + 85;
          this.scroll.guiTop = this.guiTop + 20;
          this.addScroll(this.scroll);
     }

     protected void func_146284_a(GuiButton guibutton) {
     }

     public void setData(Vector list, HashMap data) {
          String name = null;
          Bank bank = this.role.getBank();
          if (bank != null) {
               name = bank.name;
          }

          this.data = data;
          this.scroll.setList(list);
          if (name != null) {
               this.setSelected(name);
          }

     }

     public void func_73864_a(int i, int j, int k) {
          super.func_73864_a(i, j, k);
          if (k == 0 && this.scroll != null) {
               this.scroll.func_73864_a(i, j, k);
          }

     }

     public void setSelected(String selected) {
          this.scroll.setSelected(selected);
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
          if (guiCustomScroll.id == 0) {
               this.role.bankId = (Integer)this.data.get(this.scroll.getSelected());
               this.save();
          }

     }

     public void save() {
          Client.sendData(EnumPacketServer.RoleSave, this.role.writeToNBT(new NBTTagCompound()));
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
