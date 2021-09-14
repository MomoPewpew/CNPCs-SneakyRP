package noppes.npcs.client.gui.advanced;

import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.select.GuiDialogSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCDialogNpcOptions extends GuiNPCInterface2 implements GuiSelectionListener, IGuiData {
	private GuiScreen parent;
	private HashMap data = new HashMap();
	private int selectedSlot;

	public GuiNPCDialogNpcOptions(EntityNPCInterface npc, GuiScreen parent) {
		super(npc);
		this.parent = parent;
		this.drawDefaultBackground = true;
		Client.sendData(EnumPacketServer.DialogNpcGet);
	}

	public void initGui() {
		super.initGui();

		for (int i = 0; i < 12; ++i) {
			int offset = i >= 6 ? 200 : 0;
			this.addButton(
					new GuiNpcButton(i + 20, this.guiLeft + 20 + offset, this.guiTop + 13 + i % 6 * 22, 20, 20, "X"));
			this.addLabel(new GuiNpcLabel(i, "" + i, this.guiLeft + 6 + offset, this.guiTop + 18 + i % 6 * 22));
			String title = "dialog.selectoption";
			if (this.data.containsKey(i)) {
				title = ((DialogOption) this.data.get(i)).title;
			}

			this.addButton(
					new GuiNpcButton(i, this.guiLeft + 44 + offset, this.guiTop + 13 + i % 6 * 22, 140, 20, title));
		}

	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
	}

	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		int slot;
		if (id >= 0 && id < 20) {
			this.selectedSlot = id;
			slot = -1;
			if (this.data.containsKey(id)) {
				slot = ((DialogOption) this.data.get(id)).dialogId;
			}

			this.setSubGui(new GuiDialogSelection(slot));
		}

		if (id >= 20 && id < 40) {
			slot = id - 20;
			this.data.remove(slot);
			Client.sendData(EnumPacketServer.DialogNpcRemove, slot);
			this.initGui();
		}

	}

	public void save() {
	}

	public void selected(int id, String name) {
		Client.sendData(EnumPacketServer.DialogNpcSet, this.selectedSlot, id);
	}

	public void setGuiData(NBTTagCompound compound) {
		int pos = compound.getInteger("Position");
		DialogOption dialog = new DialogOption();
		dialog.readNBT(compound);
		this.data.put(pos, dialog);
		this.initGui();
	}
}
