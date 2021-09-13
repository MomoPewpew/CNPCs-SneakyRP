package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class GuiNpcFollowerSetup extends GuiContainerNPCInterface2 {
     private RoleFollower role;
     private static final ResourceLocation field_110422_t = new ResourceLocation("textures/gui/followersetup.png");

     public GuiNpcFollowerSetup(EntityNPCInterface npc, ContainerNPCFollowerSetup container) {
          super(npc, container);
          this.field_147000_g = 200;
          this.role = (RoleFollower)npc.roleInterface;
          this.setBackground("followersetup.png");
     }

     public void func_73866_w_() {
          super.func_73866_w_();

          int i;
          int day;
          for(i = 0; i < 3; ++i) {
               int x = this.field_147003_i + 66;
               day = this.field_147009_r + 37;
               day += i * 25;
               GuiNpcTextField tf = new GuiNpcTextField(i, this, this.field_146289_q, x, day, 24, 20, "1");
               tf.numbersOnly = true;
               tf.setMinMaxDefault(1, Integer.MAX_VALUE, 1);
               this.addTextField(tf);
          }

          i = 0;

          for(Iterator var5 = this.role.rates.values().iterator(); var5.hasNext(); ++i) {
               day = (Integer)var5.next();
               this.getTextField(i).func_146180_a(day + "");
          }

          this.addTextField(new GuiNpcTextField(3, this, this.field_146289_q, this.field_147003_i + 100, this.field_147009_r + 6, 286, 20, this.role.dialogHire));
          this.addTextField(new GuiNpcTextField(4, this, this.field_146289_q, this.field_147003_i + 100, this.field_147009_r + 30, 286, 20, this.role.dialogFarewell));
          this.addLabel(new GuiNpcLabel(7, "follower.infiniteDays", this.field_147003_i + 180, this.field_147009_r + 80));
          this.addButton(new GuiNpcButtonYesNo(7, this.field_147003_i + 260, this.field_147009_r + 75, this.role.infiniteDays));
          this.addLabel(new GuiNpcLabel(8, "follower.guiDisabled", this.field_147003_i + 180, this.field_147009_r + 104));
          this.addButton(new GuiNpcButtonYesNo(8, this.field_147003_i + 260, this.field_147009_r + 99, this.role.disableGui));
          this.addLabel(new GuiNpcLabel(9, "follower.allowSoulstone", this.field_147003_i + 180, this.field_147009_r + 128));
          this.addButton(new GuiNpcButtonYesNo(9, this.field_147003_i + 260, this.field_147009_r + 123, !this.role.refuseSoulStone));
          this.addButton(new GuiNpcButton(10, this.field_147003_i + 195, this.field_147009_r + 147, 100, 20, "remote.reset"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.field_146127_k == 7) {
               this.role.infiniteDays = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.field_146127_k == 8) {
               this.role.disableGui = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.field_146127_k == 9) {
               this.role.refuseSoulStone = !((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.field_146127_k == 10) {
               this.role.killed();
          }

     }

     protected void func_146979_b(int par1, int par2) {
     }

     public void save() {
          HashMap map = new HashMap();

          for(int i = 0; i < this.role.inventory.func_70302_i_(); ++i) {
               ItemStack item = this.role.inventory.func_70301_a(i);
               if (item != null && !item.func_190926_b()) {
                    int days = 1;
                    if (!this.getTextField(i).isEmpty() && this.getTextField(i).isInteger()) {
                         days = this.getTextField(i).getInteger();
                    }

                    if (days <= 0) {
                         days = 1;
                    }

                    map.put(i, days);
               }
          }

          this.role.rates = map;
          this.role.dialogHire = this.getTextField(3).func_146179_b();
          this.role.dialogFarewell = this.getTextField(4).func_146179_b();
          Client.sendData(EnumPacketServer.RoleSave, this.role.writeToNBT(new NBTTagCompound()));
     }
}
