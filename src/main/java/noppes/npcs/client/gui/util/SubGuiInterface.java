package noppes.npcs.client.gui.util;

import net.minecraft.client.gui.GuiScreen;

public class SubGuiInterface extends GuiNPCInterface {
	public GuiScreen parent;
	public int id;

	public void save() {
	}

	public void close() {
		this.save();
		if (this.parent instanceof ISubGuiListener) {
			((ISubGuiListener) this.parent).subGuiClosed(this);
		}

		if (this.parent instanceof GuiNPCInterface) {
			((GuiNPCInterface) this.parent).closeSubGui(this);
		} else if (this.parent instanceof GuiContainerNPCInterface) {
			((GuiContainerNPCInterface) this.parent).closeSubGui(this);
		} else {
			super.close();
		}

	}

	public GuiScreen getParent() {
		return this.parent instanceof SubGuiInterface ? ((SubGuiInterface) this.parent).getParent() : this.parent;
	}
}
