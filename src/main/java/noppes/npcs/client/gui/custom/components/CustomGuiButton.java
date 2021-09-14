package noppes.npcs.client.gui.custom.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IClickListener;

public class CustomGuiButton extends GuiButton implements IClickListener {
	GuiCustom parent;
	ResourceLocation texture;
	public int textureX;
	public int textureY;
	boolean hovered;
	String label;
	int colour;
	String[] hoverText;

	public CustomGuiButton(int id, String buttonText, int x, int y) {
		super(id, GuiCustom.guiLeft + x, GuiCustom.guiTop + y, buttonText);
		this.colour = 16777215;
	}

	public CustomGuiButton(int id, String buttonText, int x, int y, int width, int height) {
		super(id, GuiCustom.guiLeft + x, GuiCustom.guiTop + y, width, height, buttonText);
		this.colour = 16777215;
	}

	public CustomGuiButton(int buttonId, String buttonText, int x, int y, int width, int height, String texture) {
		this(buttonId, buttonText, x, y, width, height, texture, 0, 0);
	}

	public CustomGuiButton(int buttonId, String buttonText, int x, int y, int width, int height, String texture,
			int textureX, int textureY) {
		this(buttonId, buttonText, x, y, width, height);
		this.textureX = textureX;
		this.textureY = textureY;
		this.label = buttonText;
		this.texture = new ResourceLocation(texture);
	}

	public void setParent(GuiCustom parent) {
		this.parent = parent;
	}

	public static CustomGuiButton fromComponent(CustomGuiButtonWrapper component) {
		CustomGuiButton btn;
		if (component.hasTexture()) {
			if (component.getTextureX() >= 0 && component.getTextureY() >= 0) {
				btn = new CustomGuiButton(component.getID(), component.getLabel(), component.getPosX(),
						component.getPosY(), component.getWidth(), component.getHeight(), component.getTexture(),
						component.getTextureX(), component.getTextureY());
			} else {
				btn = new CustomGuiButton(component.getID(), component.getLabel(), component.getPosX(),
						component.getPosY(), component.getWidth(), component.getHeight(), component.getTexture());
			}
		} else if (component.getWidth() >= 0 && component.getHeight() >= 0) {
			btn = new CustomGuiButton(component.getID(), component.getLabel(), component.getPosX(), component.getPosY(),
					component.getWidth(), component.getHeight());
		} else {
			btn = new CustomGuiButton(component.getID(), component.getLabel(), component.getPosX(),
					component.getPosY());
		}

		if (component.hasHoverText()) {
			btn.hoverText = component.getHoverText();
		}

		return btn;
	}

	public int getID() {
		return this.id;
	}

	public void onRender(Minecraft mc, int mouseX, int mouseY, int mouseWheel, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.0F, (float) this.id);
		FontRenderer fontRenderer = mc.fontRenderer;
		int i;
		if (this.texture == null) {
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			i = this.getHoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
					SourceFactor.ONE, DestFactor.ZERO);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20,
					this.width / 2, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
			int j = 14737632;
			if (this.packedFGColour != 0) {
				j = this.packedFGColour;
			} else if (!this.enabled) {
				j = 10526880;
			} else if (this.hovered) {
				j = 16777120;
			}

			GlStateManager.translate(0.0D, 0.0D, 0.1D);
			this.drawCenteredString(fontRenderer, this.displayString, this.x + this.width / 2,
					this.y + (this.height - 8) / 2, j);
		} else {
			mc.getTextureManager().bindTexture(this.texture);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			i = this.hoverState(this.hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
					SourceFactor.ONE, DestFactor.ZERO);
			GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, this.textureX, this.textureY + i * this.height, this.width,
					this.height);
			this.drawCenteredString(fontRenderer, this.label, this.x + this.width / 2, this.y + (this.height - 8) / 2,
					this.colour);
			if (this.hovered && this.hoverText != null && this.hoverText.length > 0) {
				this.parent.hoverText = this.hoverText;
			}
		}

		GlStateManager.popMatrix();
	}

	public ICustomGuiComponent toComponent() {
		CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(this.id, this.label, this.x, this.y, this.width,
				this.height, this.texture.toString(), this.textureX, this.textureY);
		component.setHoverText(this.hoverText);
		return component;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	protected int hoverState(boolean mouseOver) {
		int i = 0;
		if (mouseOver) {
			i = 1;
		}

		return i;
	}

	public boolean mouseClicked(GuiCustom gui, int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
			Minecraft.getMinecraft().getSoundHandler()
					.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			gui.buttonClick(this);
			return true;
		} else {
			return false;
		}
	}
}
