package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.Client;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class GuiNpcTraderSetup extends GuiContainerNPCInterface2 implements ITextfieldListener {
     private final ResourceLocation slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");
     private RoleTrader role;

     public GuiNpcTraderSetup(EntityNPCInterface npc, ContainerNPCTraderSetup container) {
          super(npc, container);
          this.field_147000_g = 220;
          this.menuYOffset = 10;
          this.role = container.role;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.field_146292_n.clear();
          this.setBackground("tradersetup.png");
          this.addLabel(new GuiNpcLabel(0, "role.marketname", this.field_147003_i + 214, this.field_147009_r + 150));
          this.addTextField(new GuiNpcTextField(0, this, this.field_147003_i + 214, this.field_147009_r + 160, 180, 20, this.role.marketName));
          this.addLabel(new GuiNpcLabel(1, "gui.ignoreDamage", this.field_147003_i + 260, this.field_147009_r + 29));
          this.addButton(new GuiNpcButtonYesNo(1, this.field_147003_i + 340, this.field_147009_r + 24, this.role.ignoreDamage));
          this.addLabel(new GuiNpcLabel(2, "gui.ignoreNBT", this.field_147003_i + 260, this.field_147009_r + 51));
          this.addButton(new GuiNpcButtonYesNo(2, this.field_147003_i + 340, this.field_147009_r + 46, this.role.ignoreNBT));
     }

     public void func_73863_a(int i, int j, float f) {
          this.field_147009_r += 10;
          super.func_73863_a(i, j, f);
          this.field_147009_r -= 10;
     }

     public void func_146284_a(GuiButton guibutton) {
          if (guibutton.id == 1) {
               this.role.ignoreDamage = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.id == 2) {
               this.role.ignoreNBT = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

     }

     protected void func_146976_a(float f, int i, int j) {
          super.func_146976_a(f, i, j);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

          for(int slot = 0; slot < 18; ++slot) {
               int x = this.field_147003_i + slot % 3 * 94 + 7;
               int y = this.field_147009_r + slot / 3 * 22 + 4;
               this.field_146297_k.renderEngine.bindTexture(this.slot);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(x - 1, y, 0, 0, 18, 18);
               this.drawTexturedModalRect(x + 17, y, 0, 0, 18, 18);
               this.field_146289_q.func_78276_b("=", x + 36, y + 5, CustomNpcResourceListener.DefaultTextColor);
               this.field_146297_k.renderEngine.bindTexture(this.slot);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(x + 42, y, 0, 0, 18, 18);
          }

     }

     public void save() {
          Client.sendData(EnumPacketServer.TraderMarketSave, this.role.marketName, false);
          Client.sendData(EnumPacketServer.RoleSave, this.role.writeToNBT(new NBTTagCompound()));
     }

     public void unFocused(GuiNpcTextField guiNpcTextField) {
          String name = guiNpcTextField.func_146179_b();
          if (!name.equalsIgnoreCase(this.role.marketName)) {
               this.role.marketName = name;
               Client.sendData(EnumPacketServer.TraderMarketSave, this.role.marketName, true);
          }

     }
}
