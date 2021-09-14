package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataDisplay;

public class SubGuiNpcName extends SubGuiInterface implements ITextfieldListener {
	private DataDisplay display;

	public SubGuiNpcName(DataDisplay display) {
		this.display = display;
		this.setBackground("menubg.png");
		this.xSize = 256;
		this.ySize = 216;
		this.closeOnEsc = true;
	}

	public void initGui() {
		super.initGui();
		int y = this.guiTop + 4;
		this.addButton(new GuiNpcButton(66, this.guiLeft + this.xSize - 24, y, 20, 20, "X"));
		int var10006 = this.guiLeft + 4;
		y += 50;
		this.addTextField(
				new GuiNpcTextField(0, this, this.fontRenderer, var10006, y, 226, 20, this.display.getName()));
		int var10004 = this.guiLeft + 4;
		y += 22;
		this.addButton(new GuiButtonBiDirectional(1, var10004, y, 200, 20,
				new String[] { "markov.roman.name", "markov.japanese.name", "markov.slavic.name", "markov.welsh.name",
						"markov.sami.name", "markov.oldNorse.name", "markov.ancientGreek.name", "markov.aztec.name",
						"markov.classicCNPCs.name", "markov.spanish.name" },
				this.display.getMarkovGeneratorId()));
		var10004 = this.guiLeft + 64;
		y += 22;
		this.addButton(new GuiButtonBiDirectional(2, var10004, y, 120, 20,
				new String[] { "markov.gender.either", "markov.gender.male", "markov.gender.female" },
				this.display.getMarkovGender()));
		this.addLabel(new GuiNpcLabel(2, "markov.gender.name", this.guiLeft + 5, y + 5));
		var10004 = this.guiLeft + 4;
		y += 42;
		this.addButton(new GuiNpcButton(3, var10004, y, 70, 20, "markov.generate"));
	}

	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id == 0) {
			if (!textfield.isEmpty()) {
				this.display.setName(textfield.getText());
			} else {
				textfield.setText(this.display.getName());
			}
		}

	}

	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (button.id == 1) {
			this.display.setMarkovGeneratorId(button.getValue());
		}

		if (button.id == 2) {
			this.display.setMarkovGender(button.getValue());
		}

		if (button.id == 3) {
			String name = this.display.getRandomName();
			this.display.setName(name);
			this.getTextField(0).setText(name);
		}

		if (button.id == 66) {
			this.close();
		}

	}
}
