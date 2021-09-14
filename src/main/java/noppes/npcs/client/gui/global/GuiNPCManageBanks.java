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

public class GuiNPCManageBanks extends GuiContainerNPCInterface2
		implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData {
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
		this.ySize = 200;
	}

	public void initPacket() {
		Client.sendData(EnumPacketServer.BanksGet);
	}

	public void initGui() {
		super.initGui();
		this.addButton(new GuiNpcButton(6, this.guiLeft + 340, this.guiTop + 10, 45, 20, "gui.add"));
		this.addButton(new GuiNpcButton(7, this.guiLeft + 340, this.guiTop + 32, 45, 20, "gui.remove"));
		if (this.scroll == null) {
			this.scroll = new GuiCustomScroll(this, 0);
		}

		this.scroll.setSize(160, 180);
		this.scroll.guiLeft = this.guiLeft + 174;
		this.scroll.guiTop = this.guiTop + 8;
		this.addScroll(this.scroll);

		for (int i = 0; i < 6; ++i) {
			int x = this.guiLeft + 6;
			int y = this.guiTop + 36 + i * 22;
			this.addButton(new GuiNpcButton(i, x + 50, y, 80, 20,
					new String[] { "bank.canUpgrade", "bank.cantUpgrade", "bank.upgraded" }, 0));
			this.getButton(i).setEnabled(false);
		}

		this.addTextField(
				new GuiNpcTextField(0, this, this.fontRenderer, this.guiLeft + 8, this.guiTop + 8, 160, 16, ""));
		this.getTextField(0).setMaxStringLength(20);
		this.addTextField(
				new GuiNpcTextField(1, this, this.fontRenderer, this.guiLeft + 10, this.guiTop + 80, 16, 16, ""));
		this.getTextField(1).numbersOnly = true;
		this.getTextField(1).setMaxStringLength(1);
		this.addTextField(
				new GuiNpcTextField(2, this, this.fontRenderer, this.guiLeft + 10, this.guiTop + 110, 16, 16, ""));
		this.getTextField(2).numbersOnly = true;
		this.getTextField(2).setMaxStringLength(1);
	}

	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 6) {
			this.save();
			this.scroll.clear();

			String name;
			for (name = "New"; this.data.containsKey(name); name = name + "_") {
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

	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(I18n.translateToLocal("bank.tabCost"), 23, 28,
				CustomNpcResourceListener.DefaultTextColor);
		this.fontRenderer.drawString(I18n.translateToLocal("bank.upgCost"), 123, 28,
				CustomNpcResourceListener.DefaultTextColor);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.start"), 6, 70,
				CustomNpcResourceListener.DefaultTextColor);
		this.fontRenderer.drawString(I18n.translateToLocal("gui.max"), 9, 100,
				CustomNpcResourceListener.DefaultTextColor);
	}

	public void setGuiData(NBTTagCompound compound) {
		Bank bank = new Bank();
		bank.readEntityFromNBT(compound);
		this.bank = bank;
		int i;
		if (bank.id == -1) {
			this.getTextField(0).setText("");
			this.getTextField(1).setText("");
			this.getTextField(2).setText("");

			for (i = 0; i < 6; ++i) {
				this.getButton(i).setDisplay(0);
				this.getButton(i).setEnabled(false);
			}
		} else {
			this.getTextField(0).setText(bank.name);
			this.getTextField(1).setText(Integer.toString(bank.startSlots));
			this.getTextField(2).setText(Integer.toString(bank.maxSlots));

			for (i = 0; i < 6; ++i) {
				int type = 0;
				if (bank.slotTypes.containsKey(i)) {
					type = (Integer) bank.slotTypes.get(i);
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
			if (guiNpcTextField.getId() == 0) {
				String name = guiNpcTextField.getText();
				if (!name.isEmpty() && !this.data.containsKey(name)) {
					String old = this.bank.name;
					this.data.remove(this.bank.name);
					this.bank.name = name;
					this.data.put(this.bank.name, this.bank.id);
					this.selected = name;
					this.scroll.replace(old, this.bank.name);
				}
			} else if (guiNpcTextField.getId() == 1 || guiNpcTextField.getId() == 2) {
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

				if (guiNpcTextField.getId() == 1) {
					this.bank.startSlots = num;
				} else if (guiNpcTextField.getId() == 2) {
					this.bank.maxSlots = num;
				}

				if (this.bank.startSlots > this.bank.maxSlots) {
					this.bank.maxSlots = this.bank.startSlots;
				}

				guiNpcTextField.setText(Integer.toString(num));
			}
		}

	}

	public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
	}
}
