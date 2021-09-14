package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataRanged;
import noppes.npcs.entity.data.DataStats;

public class SubGuiNpcRangeProperties extends SubGuiInterface implements ITextfieldListener, ISubGuiListener {
	private DataRanged ranged;
	private DataStats stats;
	private GuiSoundSelection gui;
	private GuiNpcTextField soundSelected = null;

	public SubGuiNpcRangeProperties(DataStats stats) {
		this.ranged = stats.ranged;
		this.stats = stats;
		this.setBackground("menubg.png");
		this.xSize = 256;
		this.ySize = 216;
		this.closeOnEsc = true;
	}

	public void initGui() {
		super.initGui();
		int y = this.guiTop + 4;
		this.addTextField(new GuiNpcTextField(1, this, this.fontRenderer, this.guiLeft + 80, y, 50, 18,
				this.ranged.getAccuracy() + ""));
		this.addLabel(new GuiNpcLabel(1, "stats.accuracy", this.guiLeft + 5, y + 5));
		this.getTextField(1).numbersOnly = true;
		this.getTextField(1).setMinMaxDefault(0, 100, 90);
		this.addTextField(new GuiNpcTextField(8, this, this.fontRenderer, this.guiLeft + 200, y, 50, 18,
				this.ranged.getShotCount() + ""));
		this.addLabel(new GuiNpcLabel(8, "stats.shotcount", this.guiLeft + 135, y + 5));
		this.getTextField(8).numbersOnly = true;
		this.getTextField(8).setMinMaxDefault(1, 10, 1);
		int var10006 = this.guiLeft + 80;
		y += 22;
		this.addTextField(
				new GuiNpcTextField(2, this, this.fontRenderer, var10006, y, 50, 18, this.ranged.getRange() + ""));
		this.addLabel(new GuiNpcLabel(2, "gui.range", this.guiLeft + 5, y + 5));
		this.getTextField(2).numbersOnly = true;
		this.getTextField(2).setMinMaxDefault(1, 64, 2);
		this.addTextField(new GuiNpcTextField(9, this, this.fontRenderer, this.guiLeft + 200, y, 30, 20,
				this.ranged.getMeleeRange() + ""));
		this.addLabel(new GuiNpcLabel(16, "stats.meleerange", this.guiLeft + 135, y + 5));
		this.getTextField(9).numbersOnly = true;
		this.getTextField(9).setMinMaxDefault(0, this.stats.aggroRange, 5);
		var10006 = this.guiLeft + 80;
		y += 22;
		this.addTextField(
				new GuiNpcTextField(3, this, this.fontRenderer, var10006, y, 50, 18, this.ranged.getDelayMin() + ""));
		this.addLabel(new GuiNpcLabel(3, "stats.mindelay", this.guiLeft + 5, y + 5));
		this.getTextField(3).numbersOnly = true;
		this.getTextField(3).setMinMaxDefault(1, 9999, 20);
		this.addTextField(new GuiNpcTextField(4, this, this.fontRenderer, this.guiLeft + 200, y, 50, 18,
				this.ranged.getDelayMax() + ""));
		this.addLabel(new GuiNpcLabel(4, "stats.maxdelay", this.guiLeft + 135, y + 5));
		this.getTextField(4).numbersOnly = true;
		this.getTextField(4).setMinMaxDefault(1, 9999, 20);
		var10006 = this.guiLeft + 80;
		y += 22;
		this.addTextField(
				new GuiNpcTextField(6, this, this.fontRenderer, var10006, y, 50, 18, this.ranged.getBurst() + ""));
		this.addLabel(new GuiNpcLabel(6, "stats.burstcount", this.guiLeft + 5, y + 5));
		this.getTextField(6).numbersOnly = true;
		this.getTextField(6).setMinMaxDefault(1, 100, 20);
		this.addTextField(new GuiNpcTextField(5, this, this.fontRenderer, this.guiLeft + 200, y, 50, 18,
				this.ranged.getBurstDelay() + ""));
		this.addLabel(new GuiNpcLabel(5, "stats.burstspeed", this.guiLeft + 135, y + 5));
		this.getTextField(5).numbersOnly = true;
		this.getTextField(5).setMinMaxDefault(0, 30, 0);
		var10006 = this.guiLeft + 80;
		y += 22;
		this.addTextField(
				new GuiNpcTextField(7, this, this.fontRenderer, var10006, y, 100, 20, this.ranged.getSound(0)));
		this.addLabel(new GuiNpcLabel(7, "stats.firesound", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(7, this.guiLeft + 187, y, 60, 20, "mco.template.button.select"));
		var10006 = this.guiLeft + 80;
		y += 22;
		this.addTextField(
				new GuiNpcTextField(11, this, this.fontRenderer, var10006, y, 100, 20, this.ranged.getSound(1)));
		this.addLabel(new GuiNpcLabel(11, "stats.hitsound", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(11, this.guiLeft + 187, y, 60, 20, "mco.template.button.select"));
		var10006 = this.guiLeft + 80;
		y += 22;
		this.addTextField(
				new GuiNpcTextField(10, this, this.fontRenderer, var10006, y, 100, 20, this.ranged.getSound(2)));
		this.addLabel(new GuiNpcLabel(10, "stats.groundsound", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(10, this.guiLeft + 187, y, 60, 20, "mco.template.button.select"));
		int var10004 = this.guiLeft + 100;
		y += 22;
		this.addButton(new GuiNpcButtonYesNo(9, var10004, y, this.ranged.getHasAimAnimation()));
		this.addLabel(new GuiNpcLabel(9, "stats.aimWhileShooting", this.guiLeft + 5, y + 5));
		var10004 = this.guiLeft + 100;
		y += 22;
		this.addButton(new GuiNpcButton(13, var10004, y, 80, 20,
				new String[] { "gui.no", "gui.whendistant", "gui.whenhidden" }, this.ranged.getFireType()));
		this.addLabel(new GuiNpcLabel(13, "stats.indirect", this.guiLeft + 5, y + 5));
		this.addButton(new GuiNpcButton(66, this.guiLeft + 190, this.guiTop + 190, 60, 20, "gui.done"));
	}

	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.getId() == 1) {
			this.ranged.setAccuracy(textfield.getInteger());
		} else if (textfield.getId() == 2) {
			this.ranged.setRange(textfield.getInteger());
		} else if (textfield.getId() == 3) {
			this.ranged.setDelay(textfield.getInteger(), this.ranged.getDelayMax());
			this.initGui();
		} else if (textfield.getId() == 4) {
			this.ranged.setDelay(this.ranged.getDelayMin(), textfield.getInteger());
			this.initGui();
		} else if (textfield.getId() == 5) {
			this.ranged.setBurstDelay(textfield.getInteger());
		} else if (textfield.getId() == 6) {
			this.ranged.setBurst(textfield.getInteger());
		} else if (textfield.getId() == 7) {
			this.ranged.setSound(0, textfield.getText());
		} else if (textfield.getId() == 8) {
			this.ranged.setShotCount(textfield.getInteger());
		} else if (textfield.getId() == 9) {
			this.ranged.setMeleeRange(textfield.getInteger());
		} else if (textfield.getId() == 10) {
			this.ranged.setSound(2, textfield.getText());
		} else if (textfield.getId() == 11) {
			this.ranged.setSound(1, textfield.getText());
		}

	}

	protected void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 7) {
			this.soundSelected = this.getTextField(7);
			this.setSubGui(new GuiSoundSelection(this.soundSelected.getText()));
		}

		if (id == 11) {
			this.soundSelected = this.getTextField(11);
			this.setSubGui(new GuiSoundSelection(this.soundSelected.getText()));
		}

		if (id == 10) {
			this.soundSelected = this.getTextField(10);
			this.setSubGui(new GuiSoundSelection(this.soundSelected.getText()));
		} else if (id == 66) {
			this.close();
		} else if (id == 9) {
			this.ranged.setHasAimAnimation(((GuiNpcButtonYesNo) guibutton).getBoolean());
		} else if (id == 13) {
			this.ranged.setFireType(((GuiNpcButton) guibutton).getValue());
		}

	}

	public void subGuiClosed(SubGuiInterface subgui) {
		GuiSoundSelection gss = (GuiSoundSelection) subgui;
		if (gss.selectedResource != null) {
			this.soundSelected.setText(gss.selectedResource.toString());
			this.unFocused(this.soundSelected);
		}

	}
}
