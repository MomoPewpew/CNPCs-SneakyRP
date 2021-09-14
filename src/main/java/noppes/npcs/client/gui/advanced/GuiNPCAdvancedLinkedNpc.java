package noppes.npcs.client.gui.advanced;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCAdvancedLinkedNpc extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {
	private GuiCustomScroll scroll;
	private List data = new ArrayList();
	public static GuiScreen Instance;

	public GuiNPCAdvancedLinkedNpc(EntityNPCInterface npc) {
		super(npc);
		Instance = this;
	}

	public void initPacket() {
		Client.sendData(EnumPacketServer.LinkedGetAll);
	}

	public void initGui() {
		super.initGui();
		this.addButton(new GuiNpcButton(1, this.guiLeft + 358, this.guiTop + 38, 58, 20, "gui.clear"));
		if (this.scroll == null) {
			this.scroll = new GuiCustomScroll(this, 0);
			this.scroll.setSize(143, 208);
		}

		this.scroll.guiLeft = this.guiLeft + 137;
		this.scroll.guiTop = this.guiTop + 4;
		this.scroll.setSelected(this.npc.linkedName);
		this.scroll.setList(this.data);
		this.addScroll(this.scroll);
	}

	public void buttonEvent(GuiButton button) {
		if (button.id == 1) {
			Client.sendData(EnumPacketServer.LinkedSet, "");
		}

	}

	public void setData(Vector list, HashMap data) {
		this.data = new ArrayList(list);
		this.initGui();
	}

	public void setSelected(String selected) {
		this.scroll.setSelected(selected);
	}

	public void save() {
	}

	public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		Client.sendData(EnumPacketServer.LinkedSet, guiCustomScroll.getSelected());
	}

	public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
	}
}
