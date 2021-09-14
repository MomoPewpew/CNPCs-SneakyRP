package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcMovement;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataAI;

public class GuiNpcAI extends GuiNPCInterface2 implements ITextfieldListener, IGuiData {
	private String[] tacts = new String[] { "aitactics.rush", "aitactics.stagger", "aitactics.orbit",
			"aitactics.hitandrun", "aitactics.ambush", "aitactics.stalk", "gui.none" };
	private DataAI ai;

	public GuiNpcAI(EntityNPCInterface npc) {
		super(npc, 6);
		this.ai = npc.ais;
		Client.sendData(EnumPacketServer.MainmenuAIGet);
	}

	public void initGui() {
		super.initGui();
		this.addLabel(new GuiNpcLabel(0, "ai.enemyresponse", this.guiLeft + 5, this.guiTop + 17));
		this.addButton(new GuiNpcButton(0, this.guiLeft + 86, this.guiTop + 10, 60, 20,
				new String[] { "gui.retaliate", "gui.panic", "gui.retreat", "gui.nothing" }, this.npc.ais.onAttack));
		this.addLabel(new GuiNpcLabel(1, "ai.door", this.guiLeft + 5, this.guiTop + 40));
		this.addButton(new GuiNpcButton(1, this.guiLeft + 86, this.guiTop + 35, 60, 20,
				new String[] { "gui.break", "gui.open", "gui.disabled" }, this.npc.ais.doorInteract));
		this.addLabel(new GuiNpcLabel(12, "ai.swim", this.guiLeft + 5, this.guiTop + 65));
		this.addButton(new GuiNpcButton(7, this.guiLeft + 86, this.guiTop + 60, 60, 20,
				new String[] { "gui.no", "gui.yes" }, this.npc.ais.canSwim ? 1 : 0));
		this.addLabel(new GuiNpcLabel(13, "ai.shelter", this.guiLeft + 5, this.guiTop + 90));
		this.addButton(new GuiNpcButton(9, this.guiLeft + 86, this.guiTop + 85, 60, 20,
				new String[] { "gui.darkness", "gui.sunlight", "gui.disabled" }, this.npc.ais.findShelter));
		this.addLabel(new GuiNpcLabel(14, "ai.clearlos", this.guiLeft + 5, this.guiTop + 115));
		this.addButton(new GuiNpcButton(10, this.guiLeft + 86, this.guiTop + 110, 60, 20,
				new String[] { "gui.no", "gui.yes" }, this.npc.ais.directLOS ? 1 : 0));
		this.addButton(
				new GuiNpcButtonYesNo(23, this.guiLeft + 86, this.guiTop + 135, 60, 20, this.ai.attackInvisible));
		this.addLabel(new GuiNpcLabel(23, "stats.attackInvisible", this.guiLeft + 5, this.guiTop + 140));
		this.addLabel(new GuiNpcLabel(10, "ai.avoidwater", this.guiLeft + 150, this.guiTop + 17));
		this.addButton(new GuiNpcButton(5, this.guiLeft + 230, this.guiTop + 10, 60, 20,
				new String[] { "gui.no", "gui.yes" }, this.ai.avoidsWater ? 1 : 0));
		this.addLabel(new GuiNpcLabel(11, "ai.return", this.guiLeft + 150, this.guiTop + 40));
		this.addButton(new GuiNpcButton(6, this.guiLeft + 230, this.guiTop + 35, 60, 20,
				new String[] { "gui.no", "gui.yes" }, this.npc.ais.returnToStart ? 1 : 0));
		this.addLabel(new GuiNpcLabel(17, "ai.leapattarget", this.guiLeft + 150, this.guiTop + 65));
		this.addButton(new GuiNpcButton(15, this.guiLeft + 230, this.guiTop + 60, 60, 20,
				new String[] { "gui.no", "gui.yes" }, this.npc.ais.canLeap ? 1 : 0));
		this.addLabel(new GuiNpcLabel(19, "ai.tacticalvariant", this.guiLeft + 150, this.guiTop + 140));
		this.addButton(new GuiNpcButton(17, this.guiLeft + 230, this.guiTop + 135, 60, 20, this.tacts,
				this.ai.tacticalVariant));
		if (this.ai.tacticalVariant != 0 && this.ai.tacticalVariant != 6) {
			String label = "";
			switch (this.ai.tacticalVariant) {
			case 2:
				label = "gui.orbitdistance";
				break;
			case 3:
				label = "gui.fightifthisclose";
				break;
			case 4:
				label = "gui.ambushdistance";
				break;
			case 5:
				label = "gui.ambushdistance";
				break;
			default:
				label = "gui.engagedistance";
			}

			this.addLabel(new GuiNpcLabel(21, label, this.guiLeft + 300, this.guiTop + 140));
			this.addTextField(new GuiNpcTextField(3, this, this.fontRenderer, this.guiLeft + 380, this.guiTop + 135, 30,
					20, this.ai.getTacticalRange() + ""));
			this.getTextField(3).numbersOnly = true;
			this.getTextField(3).setMinMaxDefault(1, this.npc.stats.aggroRange, 5);
		}

		this.getButton(17).setEnabled(this.ai.onAttack == 0);
		this.getButton(15).setEnabled(this.ai.onAttack == 0);
		this.getButton(10).setEnabled(this.ai.tacticalVariant != 5 || this.ai.tacticalVariant != 6);
		this.addLabel(new GuiNpcLabel(2, "ai.movement", this.guiLeft + 4, this.guiTop + 165));
		this.addButton(new GuiNpcButton(2, this.guiLeft + 86, this.guiTop + 160, 60, 20, "selectServer.edit"));
	}

	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 3) {
			this.ai.setTacticalRange(textfield.getInteger());
		}

	}

	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 0) {
			this.ai.onAttack = button.getValue();
			this.initGui();
		} else if (button.id == 1) {
			this.ai.doorInteract = button.getValue();
		} else if (button.id == 2) {
			this.setSubGui(new SubGuiNpcMovement(this.ai));
		} else if (button.id == 5) {
			this.npc.ais.setAvoidsWater(button.getValue() == 1);
		} else if (button.id == 6) {
			this.ai.returnToStart = button.getValue() == 1;
		} else if (button.id == 7) {
			this.ai.canSwim = button.getValue() == 1;
		} else if (button.id == 9) {
			this.ai.findShelter = button.getValue();
		} else if (button.id == 10) {
			this.ai.directLOS = button.getValue() == 1;
		} else if (button.id == 15) {
			this.ai.canLeap = button.getValue() == 1;
		} else if (button.id == 17) {
			this.ai.tacticalVariant = button.getValue();
			this.ai.directLOS = button.getValue() == 5 ? false : this.ai.directLOS;
			this.initGui();
		} else if (button.id == 23) {
			this.ai.attackInvisible = ((GuiNpcButtonYesNo) button).getBoolean();
		}

	}

	public void save() {
		Client.sendData(EnumPacketServer.MainmenuAISave, this.ai.writeToNBT(new NBTTagCompound()));
	}

	public void setGuiData(NBTTagCompound compound) {
		this.ai.readToNBT(compound);
		this.initGui();
	}
}
