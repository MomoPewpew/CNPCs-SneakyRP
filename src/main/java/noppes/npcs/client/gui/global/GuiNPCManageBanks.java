package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageBanks extends GuiContainerNPCInterface2 implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData {
     private GuiCustomScroll scroll;
     private HashMap data = new HashMap();
     private ContainerManageBanks container;
     private Bank bank = new Bank();
     private String selected = null;

     public GuiNPCManageBanks(EntityNPCInterface npc, ContainerManageBanks container) {
          super(npc, container);
          this.container = container;
          this.drawDefaultBackground = false;
          this.setBackground("npcbanksetup.png");
          this.field_147000_g = 200;
     }

     public void initPacket() {
          Client.sendData(EnumPacketServer.BanksGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(new GuiNpcButton(6, this.field_147003_i + 340, this.field_147009_r + 10, 45, 20, "gui.add"));
          this.addButton(new GuiNpcButton(7, this.field_147003_i + 340, this.field_147009_r + 32, 45, 20, "gui.remove"));
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
          }

          this.scroll.setSize(160, 180);
          this.scroll.guiLeft = this.field_147003_i + 174;
          this.scroll.guiTop = this.field_147009_r + 8;
          this.addScroll(this.scroll);

          for(int i = 0; i < 6; ++i) {
               int x = this.field_147003_i + 6;
               int y = this.field_147009_r + 36 + i * 22;
               this.addButton(new GuiNpcButton(i, x + 50, y, 80, 20, new String[]{"bank.canUpgrade", "bank.cantUpgrade", "bank.upgraded"}, 0));
               this.getButton(i).setEnabled(false);
          }

          this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.field_147003_i + 8, this.field_147009_r + 8, 160, 16, ""));
          this.getTextField(0).func_146203_f(20);
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.field_147003_i + 10, this.field_147009_r + 80, 16, 16, ""));
          this.getTextField(1).numbersOnly = true;
          this.getTextField(1).func_146203_f(1);
          this.addTextField(new GuiNpcTextField(2, this, this.field_146289_q, this.field_147003_i + 10, this.field_147009_r + 110, 16, 16, ""));
          this.getTextField(2).numbersOnly = true;
          this.getTextField(2).func_146203_f(1);
     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.id == 6) {
               this.save();
               this.scroll.clear();

               String name;
               for(name = "New"; this.data.containsKey(name); name = name + "_") {
               }

               Bank bank = new Bank();
               bank.name = name;
               NBTTagCompound compound = new NBTTagCompound();
               bank.writeEntityToNBT(compound);
               Client.sendData(EnumPacketServer.BankSave, compound);
          } else if (button.id == 7) {
               if (this.data.containsKey(this.scroll.getSelected())) {
                    Client.sendData(EnumPacketServer.BankRemove, this.data.get(this.selected));
               }
          } else if (button.id >= 0 && button.id < 6) {
               this.bank.slotTypes.put(button.id, button.getValue());
          }

     }

     protected void func_146979_b(int par1, int par2) {
          this.field_146289_q.func_78276_b(I18n.func_74838_a("bank.tabCost"), 23, 28, CustomNpcResourceListener.DefaultTextColor);
          this.field_146289_q.func_78276_b(I18n.func_74838_a("bank.upgCost"), 123, 28, CustomNpcResourceListener.DefaultTextColor);
          this.field_146289_q.func_78276_b(I18n.func_74838_a("gui.start"), 6, 70, CustomNpcResourceListener.DefaultTextColor);
          this.field_146289_q.func_78276_b(I18n.func_74838_a("gui.max"), 9, 100, CustomNpcResourceListener.DefaultTextColor);
     }

     public void setGuiData(NBTTagCompound compound) {
          Bank bank = new Bank();
          bank.readEntityFromNBT(compound);
          this.bank = bank;
          int i;
          if (bank.id == -1) {
               this.getTextField(0).func_146180_a("");
               this.getTextField(1).func_146180_a("");
               this.getTextField(2).func_146180_a("");

               for(i = 0; i < 6; ++i) {
                    this.getButton(i).setDisplay(0);
                    this.getButton(i).setEnabled(false);
               }
          } else {
               this.getTextField(0).func_146180_a(bank.name);
               this.getTextField(1).func_146180_a(Integer.toString(bank.startSlots));
               this.getTextField(2).func_146180_a(Integer.toString(bank.maxSlots));

               for(i = 0; i < 6; ++i) {
                    int type = 0;
                    if (bank.slotTypes.containsKey(i)) {
                         type = (Integer)bank.slotTypes.get(i);
                    }

                    this.getButton(i).setDisplay(type);
                    this.getButton(i).setEnabled(true);
               }
          }

          this.setSelected(bank.name);
     }

     public void setData(Vector list, HashMap data) {
          String name = this.scroll.getSelected();
          this.data = data;
          this.scroll.setList(list);
          if (name != null) {
               this.scroll.setSelected(name);
          }

     }

     public void setSelected(String selected) {
          this.selected = selected;
          this.scroll.setSelected(selected);
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
          if (guiCustomScroll.id == 0) {
               this.save();
               this.selected = this.scroll.getSelected();
               Client.sendData(EnumPacketServer.BankGet, this.data.get(this.selected));
          }

     }

     public void save() {
          if (this.selected != null && this.data.containsKey(this.selected) && this.bank != null) {
               NBTTagCompound compound = new NBTTagCompound();
               this.bank.currencyInventory = this.container.bank.currencyInventory;
               this.bank.upgradeInventory = this.container.bank.upgradeInventory;
               this.bank.writeEntityToNBT(compound);
               Client.sendData(EnumPacketServer.BankSave, compound);
          }

     }

     public void unFocused(GuiNpcTextField guiNpcTextField) {
          if (this.bank.id != -1) {
               if (guiNpcTextField.field_175208_g == 0) {
                    String name = guiNpcTextField.func_146179_b();
                    if (!name.isEmpty() && !this.data.containsKey(name)) {
                         String old = this.bank.name;
                         this.data.remove(this.bank.name);
                         this.bank.name = name;
                         this.data.put(this.bank.name, this.bank.id);
                         this.selected = name;
                         this.scroll.replace(old, this.bank.name);
                    }
               } else if (guiNpcTextField.field_175208_g == 1 || guiNpcTextField.field_175208_g == 2) {
                    int num = 1;
                    if (!guiNpcTextField.isEmpty()) {
                         num = guiNpcTextField.getInteger();
                    }

                    if (num > 6) {
                         num = 6;
                    }

                    if (num < 0) {
                         num = 0;
                    }

                    if (guiNpcTextField.field_175208_g == 1) {
                         this.bank.startSlots = num;
                    } else if (guiNpcTextField.field_175208_g == 2) {
                         this.bank.maxSlots = num;
                    }

                    if (this.bank.startSlots > this.bank.maxSlots) {
                         this.bank.maxSlots = this.bank.startSlots;
                    }

                    guiNpcTextField.func_146180_a(Integer.toString(num));
               }
          }

     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
