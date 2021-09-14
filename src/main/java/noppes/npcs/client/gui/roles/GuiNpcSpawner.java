package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.GuiNpcMobSpawnerSelector;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobSpawner;

public class GuiNpcSpawner extends GuiNPCInterface2 implements ITextfieldListener, IGuiData {
	private JobSpawner job;
	private int slot = -1;
	public String title1 = "gui.selectnpc";
	public String title2 = "gui.selectnpc";
	public String title3 = "gui.selectnpc";
	public String title4 = "gui.selectnpc";
	public String title5 = "gui.selectnpc";
	public String title6 = "gui.selectnpc";

	public GuiNpcSpawner(EntityNPCInterface npc) {
		super(npc);
		this.job = (JobSpawner) npc.jobInterface;
	}

	public void initGui() {
		super.initGui();
		int y = this.guiTop + 6;
		this.addButton(new GuiNpcButton(20, this.guiLeft + 25, y, 20, 20, "X"));
		this.addLabel(new GuiNpcLabel(0, "1:", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(0, this.guiLeft + 50, y, this.title1));
		y += 23;
		this.addButton(new GuiNpcButton(21, this.guiLeft + 25, y, 20, 20, "X"));
		this.addLabel(new GuiNpcLabel(1, "2:", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(1, this.guiLeft + 50, y, this.title2));
		y += 23;
		this.addButton(new GuiNpcButton(22, this.guiLeft + 25, y, 20, 20, "X"));
		this.addLabel(new GuiNpcLabel(2, "3:", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(2, this.guiLeft + 50, y, this.title3));
		y += 23;
		this.addButton(new GuiNpcButton(23, this.guiLeft + 25, y, 20, 20, "X"));
		this.addLabel(new GuiNpcLabel(3, "4:", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(3, this.guiLeft + 50, y, this.title4));
		y += 23;
		this.addButton(new GuiNpcButton(24, this.guiLeft + 25, y, 20, 20, "X"));
		this.addLabel(new GuiNpcLabel(4, "5:", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(4, this.guiLeft + 50, y, this.title5));
		y += 23;
		this.addButton(new GuiNpcButton(25, this.guiLeft + 25, y, 20, 20, "X"));
		this.addLabel(new GuiNpcLabel(5, "6:", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(5, this.guiLeft + 50, y, this.title6));
		y += 23;
		this.addLabel(new GuiNpcLabel(6, "spawner.diesafter", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(26, this.guiLeft + 115, y, 40, 20, new String[] { "gui.yes", "gui.no" },
				this.job.doesntDie ? 1 : 0));
		this.addLabel(new GuiNpcLabel(11, "spawner.despawn", this.guiLeft + 170, y + 5));
		this.addButton(new GuiNpcButton(11, this.guiLeft + 335, y, 40, 20, new String[] { "gui.no", "gui.yes" },
				this.job.despawnOnTargetLost ? 1 : 0));
		y += 23;
		this.addLabel(new GuiNpcLabel(7, I18n.translateToLocal("spawner.posoffset") + " X:", this.guiLeft + 4, y + 5));
		this.addTextField(
				new GuiNpcTextField(7, this, this.fontRenderer, this.guiLeft + 99, y, 24, 20, this.job.xOffset + ""));
		this.getTextField(7).numbersOnly = true;
		this.getTextField(7).setMinMaxDefault(-9, 9, 0);
		this.addLabel(new GuiNpcLabel(8, "Y:", this.guiLeft + 125, y + 5));
		this.addTextField(
				new GuiNpcTextField(8, this, this.fontRenderer, this.guiLeft + 135, y, 24, 20, this.job.yOffset + ""));
		this.getTextField(8).numbersOnly = true;
		this.getTextField(8).setMinMaxDefault(-9, 9, 0);
		this.addLabel(new GuiNpcLabel(9, "Z:", this.guiLeft + 161, y + 5));
		this.addTextField(
				new GuiNpcTextField(9, this, this.fontRenderer, this.guiLeft + 171, y, 24, 20, this.job.zOffset + ""));
		this.getTextField(9).numbersOnly = true;
		this.getTextField(9).setMinMaxDefault(-9, 9, 0);
		y += 23;
		this.addLabel(new GuiNpcLabel(10, "spawner.type", this.guiLeft + 4, y + 5));
		this.addButton(new GuiNpcButton(10, this.guiLeft + 80, y, 100, 20,
				new String[] { "spawner.one", "spawner.all", "spawner.random" }, this.job.spawnType));
	}

	public void elementClicked() {
	}

	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id >= 0 && button.id < 6) {
			this.slot = button.id + 1;
			this.setSubGui(new GuiNpcMobSpawnerSelector());
		}

		if (button.id >= 20 && button.id < 26) {
			this.job.setJobCompound(button.id - 19, (NBTTagCompound) null);
			Client.sendData(EnumPacketServer.JobSpawnerRemove, button.id - 19);
		}

		if (button.id == 26) {
			this.job.doesntDie = button.getValue() == 1;
		}

		if (button.id == 10) {
			this.job.spawnType = button.getValue();
		}

		if (button.id == 11) {
			this.job.despawnOnTargetLost = button.getValue() == 1;
		}

	}

	public void closeSubGui(SubGuiInterface gui) {
		super.closeSubGui(gui);
		GuiNpcMobSpawnerSelector selector = (GuiNpcMobSpawnerSelector) gui;
		if (selector.isServer) {
			String selected = selector.getSelected();
			if (selected != null) {
				Client.sendData(EnumPacketServer.JobSpawnerAdd, selector.isServer, selected, selector.activeTab,
						this.slot);
			}
		} else {
			NBTTagCompound compound = selector.getCompound();
			if (compound != null) {
				this.job.setJobCompound(this.slot, compound);
				Client.sendData(EnumPacketServer.JobSpawnerAdd, selector.isServer, this.slot, compound);
			}
		}

		this.initGui();
	}

	public void save() {
		NBTTagCompound compound = this.job.writeToNBT(new NBTTagCompound());
		this.job.cleanCompound(compound);
		Client.sendData(EnumPacketServer.JobSave, compound);
	}

	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.getId() == 7) {
			this.job.xOffset = textfield.getInteger();
		}

		if (textfield.getId() == 8) {
			this.job.yOffset = textfield.getInteger();
		}

		if (textfield.getId() == 9) {
			this.job.zOffset = textfield.getInteger();
		}

	}

	public void setGuiData(NBTTagCompound compound) {
		this.title1 = compound.getString("Title1");
		this.title2 = compound.getString("Title2");
		this.title3 = compound.getString("Title3");
		this.title4 = compound.getString("Title4");
		this.title5 = compound.getString("Title5");
		this.title6 = compound.getString("Title6");
		this.initGui();
	}
}
