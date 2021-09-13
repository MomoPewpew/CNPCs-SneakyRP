package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCBankChest extends GuiContainerNPCInterface implements IGuiData {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/bankchest.png");
     private ContainerNPCBankInterface container;
     private int availableSlots = 0;
     private int maxSlots = 1;
     private int unlockedSlots = 1;
     private ItemStack currency;

     public GuiNPCBankChest(EntityNPCInterface npc, ContainerNPCBankInterface container) {
          super(npc, container);
          this.container = container;
          this.title = "";
          this.field_146291_p = false;
          this.field_147000_g = 235;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.availableSlots = 0;
          if (this.maxSlots > 1) {
               for(int i = 0; i < this.maxSlots; ++i) {
                    GuiNpcButton button = new GuiNpcButton(i, this.field_147003_i - 50, this.field_147009_r + 10 + i * 24, 50, 20, I18n.func_74838_a("gui.tab") + " " + (i + 1));
                    if (i > this.unlockedSlots) {
                         button.setEnabled(false);
                    }

                    this.addButton(button);
                    ++this.availableSlots;
               }

               if (this.availableSlots == 1) {
                    this.field_146292_n.clear();
               }
          }

          if (!this.container.isAvailable()) {
               this.addButton(new GuiNpcButton(8, this.field_147003_i + 48, this.field_147009_r + 48, 80, 20, I18n.func_74838_a("bank.unlock")));
          } else if (this.container.canBeUpgraded()) {
               this.addButton(new GuiNpcButton(9, this.field_147003_i + 48, this.field_147009_r + 48, 80, 20, I18n.func_74838_a("bank.upgrade")));
          }

          if (this.maxSlots > 1) {
               this.getButton(this.container.slot).visible = false;
               this.getButton(this.container.slot).setEnabled(false);
          }

     }

     public void func_146284_a(GuiButton guibutton) {
          super.func_146284_a(guibutton);
          int id = guibutton.id;
          if (id < 6) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.BankSlotOpen, id, this.container.bankid);
          }

          if (id == 8) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.BankUnlock);
          }

          if (id == 9) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.BankUpgrade);
          }

     }

     protected void func_146976_a(float f, int i, int j) {
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.resource);
          int l = (this.width - this.field_146999_f) / 2;
          int i1 = (this.height - this.field_147000_g) / 2;
          this.drawTexturedModalRect(l, i1, 0, 0, this.field_146999_f, 6);
          int ii;
          int y;
          if (!this.container.isAvailable()) {
               this.drawTexturedModalRect(l, i1 + 6, 0, 6, this.field_146999_f, 64);
               this.drawTexturedModalRect(l, i1 + 70, 0, 124, this.field_146999_f, 98);
               ii = this.field_147003_i + 30;
               y = this.field_147009_r + 8;
               this.field_146289_q.func_78276_b(I18n.func_74838_a("bank.unlockCosts") + ":", ii, y + 4, CustomNpcResourceListener.DefaultTextColor);
               this.drawItem(ii + 90, y, this.currency, i, j);
          } else if (this.container.isUpgraded()) {
               this.drawTexturedModalRect(l, i1 + 60, 0, 60, this.field_146999_f, 162);
               this.drawTexturedModalRect(l, i1 + 6, 0, 60, this.field_146999_f, 64);
          } else if (this.container.canBeUpgraded()) {
               this.drawTexturedModalRect(l, i1 + 6, 0, 6, this.field_146999_f, 216);
               ii = this.field_147003_i + 30;
               y = this.field_147009_r + 8;
               this.field_146289_q.func_78276_b(I18n.func_74838_a("bank.upgradeCosts") + ":", ii, y + 4, CustomNpcResourceListener.DefaultTextColor);
               this.drawItem(ii + 90, y, this.currency, i, j);
          } else {
               this.drawTexturedModalRect(l, i1 + 6, 0, 60, this.field_146999_f, 162);
          }

          if (this.maxSlots > 1) {
               for(ii = 0; ii < this.maxSlots && this.availableSlots != ii; ++ii) {
                    this.field_146289_q.func_78276_b("Tab " + (ii + 1), this.field_147003_i - 40, this.field_147009_r + 16 + ii * 24, 16777215);
               }
          }

          super.func_146976_a(f, i, j);
     }

     private void drawItem(int x, int y, ItemStack item, int mouseX, int mouseY) {
          if (!NoppesUtilServer.IsItemStackNull(item)) {
               GlStateManager.enableRescaleNormal();
               RenderHelper.enableGUIStandardItemLighting();
               this.field_146296_j.renderItemAndEffectIntoGUI(item, x, y);
               this.field_146296_j.func_175030_a(this.field_146289_q, item, x, y);
               RenderHelper.disableStandardItemLighting();
               GlStateManager.func_179101_C();
               if (this.func_146978_c(x - this.field_147003_i, y - this.field_147009_r, 16, 16, mouseX, mouseY)) {
                    this.func_146285_a(item, mouseX, mouseY);
               }

          }
     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          this.maxSlots = compound.func_74762_e("MaxSlots");
          this.unlockedSlots = compound.func_74762_e("UnlockedSlots");
          if (compound.func_74764_b("Currency")) {
               this.currency = new ItemStack(compound.func_74775_l("Currency"));
          } else {
               this.currency = ItemStack.field_190927_a;
          }

          if (this.container.currency != null) {
               this.container.currency.item = this.currency;
          }

          this.func_73866_w_();
     }
}
