package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataAI;

public class SubGuiNpcWanderNode extends SubGuiInterface implements ITextfieldListener {
	private DataAI ai;
	private int index;

	public SubGuiNpcWanderNode(DataAI ai, int index) {
		this.ai = ai;
		this.index = index;
		this.setBackground("menubg.png");
		this.xSize = 256;
		this.ySize = 216;
		this.closeOnEsc = true;
	}

	public void initGui() {
		super.initGui();
		this.addTextField(new GuiNpcTextField(2, this, this.fontRenderer, this.guiLeft + 106, this.guiTop + 53, 50,
				18, this.ai.getMovingPath().get(this.index)[3] + ""));
		this.getTextField(2).numbersOnly = true;
		this.getTextField(2).setMinMaxDefault(0, Integer.MAX_VALUE, 0);

		this.addButton(new GuiNpcButton(66, this.guiLeft + 82, this.guiTop + 190, 98, 20, "gui.done"));
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 66) {
			this.close();
		}
	}

	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.getId() == 2) {
			this.ai.getMovingPath().get(this.index)[3] = Integer.valueOf(textfield.getText());
		}
	}
}
