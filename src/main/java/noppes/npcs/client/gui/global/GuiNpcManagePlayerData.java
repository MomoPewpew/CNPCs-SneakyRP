package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcManagePlayerData extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {
	private GuiCustomScroll scroll;
	private String selectedPlayer = null;
	private String selected = null;
	private HashMap data = new HashMap();
	private EnumPlayerData selection;
	private String search;

	public GuiNpcManagePlayerData(EntityNPCInterface npc, GuiNPCInterface2 parent) {
		super(npc);
		this.selection = EnumPlayerData.Players;
		this.search = "";
		Client.sendData(EnumPacketServer.PlayerDataGet, this.selection);
	}

	public void initGui() {
		super.initGui();
		this.scroll = new GuiCustomScroll(this, 0);
		this.scroll.setSize(190, 175);
		this.scroll.guiLeft = this.guiLeft + 4;
		this.scroll.guiTop = this.guiTop + 16;
		this.addScroll(this.scroll);
		this.selected = null;
		this.addLabel(new GuiNpcLabel(0, "All Players", this.guiLeft + 10, this.guiTop + 6));
		this.addButton(new GuiNpcButton(0, this.guiLeft + 200, this.guiTop + 10, 98, 20, "selectWorld.deleteButton"));
		this.addButton(new GuiNpcButton(1, this.guiLeft + 200, this.guiTop + 32, 98, 20, "playerdata.players"));
		this.addButton(new GuiNpcButton(2, this.guiLeft + 200, this.guiTop + 54, 98, 20, "quest.quest"));
		this.addButton(new GuiNpcButton(3, this.guiLeft + 200, this.guiTop + 76, 98, 20, "dialog.dialog"));
		this.addButton(new GuiNpcButton(4, this.guiLeft + 200, this.guiTop + 98, 98, 20, "global.transport"));
		this.addButton(new GuiNpcButton(5, this.guiLeft + 200, this.guiTop + 120, 98, 20, "role.bank"));
		this.addButton(new GuiNpcButton(6, this.guiLeft + 200, this.guiTop + 142, 98, 20, "menu.factions"));
		this.addTextField(new GuiNpcTextField(0, this, this.fontRenderer, this.guiLeft + 4, this.guiTop + 193, 190, 20,
				this.search));
		this.getTextField(0).enabled = this.selection == EnumPlayerData.Players;
		this.initButtons();
	}

	public void initButtons() {
		this.getButton(1).setEnabled(this.selection != EnumPlayerData.Players);
		this.getButton(2).setEnabled(this.selection != EnumPlayerData.Quest);
		this.getButton(3).setEnabled(this.selection != EnumPlayerData.Dialog);
		this.getButton(4).setEnabled(this.selection != EnumPlayerData.Transport);
		this.getButton(5).setEnabled(this.selection != EnumPlayerData.Bank);
		this.getButton(6).setEnabled(this.selection != EnumPlayerData.Factions);
		if (this.selection == EnumPlayerData.Players) {
			this.getLabel(0).label = "All Players";
		} else {
			this.getLabel(0).label = "Selected player: " + this.selectedPlayer;
		}

	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		this.scroll.drawScreen(i, j, f);
	}

	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		if (k == 0 && this.scroll != null) {
			this.scroll.mouseClicked(i, j, k);
		}

	}

	public void keyTyped(char c, int i) {
		super.keyTyped(c, i);
		if (this.selection == EnumPlayerData.Players) {
			if (!this.search.equals(this.getTextField(0).getText())) {
				this.search = this.getTextField(0).getText().toLowerCase();
				this.scroll.setList(this.getSearchList());
			}
		}
	}

	private List getSearchList() {
		if (!this.search.isEmpty() && this.selection == EnumPlayerData.Players) {
			List list = new ArrayList();
			Iterator var2 = this.data.keySet().iterator();

			while (var2.hasNext()) {
				String name = (String) var2.next();
				if (name.toLowerCase().contains(this.search)) {
					list.add(name);
				}
			}

			return list;
		} else {
			return new ArrayList(this.data.keySet());
		}
	}

	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 0) {
			if (this.selected != null) {
				if (this.selection == EnumPlayerData.Players) {
					Client.sendData(EnumPacketServer.PlayerDataRemove, this.selection, this.selectedPlayer,
							this.selected);
				} else {
					Client.sendData(EnumPacketServer.PlayerDataRemove, this.selection, this.selectedPlayer,
							this.data.get(this.selected));
				}

				this.data.clear();
			}

			this.selected = null;
		}

		if (id >= 1 && id <= 6) {
			if (this.selectedPlayer == null && id != 1) {
				return;
			}

			this.selection = EnumPlayerData.values()[id - 1];
			this.initButtons();
			this.scroll.clear();
			this.data.clear();
			Client.sendData(EnumPacketServer.PlayerDataGet, this.selection, this.selectedPlayer);
			this.selected = null;
		}

	}

	public void save() {
	}

	public void setData(Vector list, HashMap data) {
		this.data.putAll(data);
		this.scroll.setList(this.getSearchList());
		if (this.selection == EnumPlayerData.Players && this.selectedPlayer != null) {
			this.scroll.setSelected(this.selectedPlayer);
			this.selected = this.selectedPlayer;
		}

	}

	public void setSelected(String selected) {
	}

	public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		this.selected = guiCustomScroll.getSelected();
		if (this.selection == EnumPlayerData.Players) {
			this.selectedPlayer = this.selected;
		}

	}

	public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
	}
}
