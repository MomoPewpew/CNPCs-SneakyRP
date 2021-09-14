package noppes.npcs.client.gui.custom.components;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.renderer.GlStateManager;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;

public class CustomGuiLabel extends GuiLabel implements IGuiComponent {
	GuiCustom parent;
	String fullLabel;
	int colour;
	String[] hoverText;
	float scale;

	public CustomGuiLabel(String label, int id, int x, int y, int width, int height) {
		this(label, id, x, y, width, height, 16777215);
	}

	public CustomGuiLabel(String label, int id, int x, int y, int width, int height, int colour) {
		super(Minecraft.getMinecraft().fontRenderer, id, (GuiCustom.guiLeft + x), (GuiCustom.guiTop + y), width, height, colour);
		this.scale = 1.0F;
		this.fullLabel = label;
		this.colour = colour;
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		fontRenderer.getStringWidth(label);
		Iterator var10 = fontRenderer.listFormattedStringToWidth(label, width).iterator();

		while (var10.hasNext()) {
			String s = (String) var10.next();
			this.addLine(s);
		}

	}

	public void setParent(GuiCustom parent) {
		this.parent = parent;
	}

	public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.0F, (float) this.id);
		boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
				&& mouseY < this.y + this.height;
		this.drawLabel(mc, mouseX, mouseY);
		if (hovered && this.hoverText != null && this.hoverText.length > 0) {
			this.parent.hoverText = this.hoverText;
		}

		GlStateManager.popMatrix();
	}

	public int getID() {
		return this.id;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public static CustomGuiLabel fromComponent(CustomGuiLabelWrapper component) {
		CustomGuiLabel lbl = new CustomGuiLabel(component.getText(), component.getID(), component.getPosX(),
				component.getPosY(), component.getWidth(), component.getHeight(), component.getColor());
		lbl.setScale(component.getScale());
		if (component.hasHoverText()) {
			lbl.hoverText = component.getHoverText();
		}

		return lbl;
	}

	public ICustomGuiComponent toComponent() {
		CustomGuiLabelWrapper component = new CustomGuiLabelWrapper(this.id, this.fullLabel, this.x, this.y, this.width,
				this.height, this.colour);
		component.setHoverText(this.hoverText);
		return component;
	}
}
