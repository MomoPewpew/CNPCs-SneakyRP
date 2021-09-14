package noppes.npcs.client.gui.custom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiScrollWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.custom.components.CustomGuiButton;
import noppes.npcs.client.gui.custom.components.CustomGuiLabel;
import noppes.npcs.client.gui.custom.components.CustomGuiScrollComponent;
import noppes.npcs.client.gui.custom.components.CustomGuiTextField;
import noppes.npcs.client.gui.custom.components.CustomGuiTexturedRect;
import noppes.npcs.client.gui.custom.interfaces.IClickListener;
import noppes.npcs.client.gui.custom.interfaces.IDataHolder;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.client.gui.custom.interfaces.IKeyListener;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerCustomGui;
import org.lwjgl.input.Mouse;

public class GuiCustom extends GuiContainer implements ICustomScrollListener, IGuiData {
	CustomGuiWrapper gui;
	int xSize;
	int ySize;
	public static int guiLeft;
	public static int guiTop;
	ResourceLocation background;
	public String[] hoverText;
	Map components = new HashMap();
	List clickListeners = new ArrayList();
	List keyListeners = new ArrayList();
	List dataHolders = new ArrayList();

	public GuiCustom(ContainerCustomGui container) {
		super(container);
	}

	public void initGui() {
		super.initGui();
		if (this.gui != null) {
			guiLeft = (this.width - this.xSize) / 2;
			guiTop = (this.height - this.ySize) / 2;
			this.components.clear();
			this.clickListeners.clear();
			this.keyListeners.clear();
			this.dataHolders.clear();
			Iterator var1 = this.gui.getComponents().iterator();

			while (var1.hasNext()) {
				ICustomGuiComponent c = (ICustomGuiComponent) var1.next();
				this.addComponent(c);
			}
		}

	}

	public void updateScreen() {
		super.updateScreen();
		Iterator var1 = this.dataHolders.iterator();

		while (var1.hasNext()) {
			IDataHolder component = (IDataHolder) var1.next();
			if (component instanceof GuiTextField) {
				((GuiTextField) component).updateCursorCounter();
			}
		}

	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.hoverText = null;
		this.drawDefaultBackground();
		if (this.background != null) {
			this.drawBackgroundTexture();
		}

		Iterator var4 = this.components.values().iterator();

		while (var4.hasNext()) {
			IGuiComponent component = (IGuiComponent) var4.next();
			component.onRender(this.mc, mouseX, mouseY, Mouse.getDWheel(), partialTicks);
		}

		if (this.hoverText != null) {
			this.drawHoveringText(Arrays.asList(this.hoverText), mouseX, mouseY);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
	}

	void drawBackgroundTexture() {
		this.mc.getTextureManager().bindTexture(this.background);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
	}

	private void addComponent(ICustomGuiComponent component) {
		CustomGuiComponentWrapper c = (CustomGuiComponentWrapper) component;
		switch (c.getType()) {
		case 0:
			CustomGuiButton button = CustomGuiButton.fromComponent((CustomGuiButtonWrapper) component);
			button.setParent(this);
			this.components.put(button.getID(), button);
			this.addClickListener(button);
			break;
		case 1:
			CustomGuiLabel lbl = CustomGuiLabel.fromComponent((CustomGuiLabelWrapper) component);
			lbl.setParent(this);
			this.components.put(lbl.getID(), lbl);
			break;
		case 2:
			CustomGuiTexturedRect rect = CustomGuiTexturedRect.fromComponent((CustomGuiTexturedRectWrapper) component);
			rect.setParent(this);
			this.components.put(rect.getID(), rect);
			break;
		case 3:
			CustomGuiTextField textField = CustomGuiTextField.fromComponent((CustomGuiTextFieldWrapper) component);
			textField.setParent(this);
			this.components.put(textField.id, textField);
			this.addDataHolder(textField);
			this.addClickListener(textField);
			this.addKeyListener(textField);
			break;
		case 4:
			CustomGuiScrollComponent scroll = new CustomGuiScrollComponent(this.mc, this, component.getID(),
					((CustomGuiScrollWrapper) component).isMultiSelect());
			scroll.fromComponent((CustomGuiScrollWrapper) component);
			scroll.setParent(this);
			this.components.put(scroll.getID(), scroll);
			this.addDataHolder(scroll);
			this.addClickListener(scroll);
		}

	}

	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		Client.sendData(EnumPacketServer.CustomGuiButton, this.updateGui().toNBT(), button.id);
	}

	public void buttonClick(CustomGuiButton button) {
		Client.sendData(EnumPacketServer.CustomGuiButton, this.updateGui().toNBT(), button.id);
	}

	public void slotChange(Slot slot) {
		Client.sendData(EnumPacketServer.CustomGuiSlotChange, this.updateGui().toNBT(), slot.slotNumber);
	}

	public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		Client.sendData(EnumPacketServer.CustomGuiScrollClick, this.updateGui().toNBT(), scroll.id, scroll.selected,
				this.getScrollSelection((CustomGuiScrollComponent) scroll), false);
	}

	public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
		Client.sendData(EnumPacketServer.CustomGuiScrollClick, this.updateGui().toNBT(), scroll.id, scroll.selected,
				this.getScrollSelection((CustomGuiScrollComponent) scroll), true);
	}

	public void onGuiClosed() {
		if (this.gui != null) {
			Client.sendData(EnumPacketServer.CustomGuiClose, this.updateGui().toNBT());
		}

		super.onGuiClosed();
	}

	CustomGuiWrapper updateGui() {
		Iterator var1 = this.dataHolders.iterator();

		while (var1.hasNext()) {
			IDataHolder component = (IDataHolder) var1.next();
			this.gui.updateComponent(component.toComponent());
		}

		return this.gui;
	}

	NBTTagCompound getScrollSelection(CustomGuiScrollComponent scroll) {
		NBTTagList list = new NBTTagList();
		if (scroll.multiSelect) {
			Iterator var3 = scroll.getSelectedList().iterator();

			while (var3.hasNext()) {
				String s = (String) var3.next();
				list.appendTag(new NBTTagString(s));
			}
		} else {
			list.appendTag(new NBTTagString(scroll.getSelected()));
		}

		NBTTagCompound selection = new NBTTagCompound();
		selection.setTag("selection", list);
		return selection;
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		Iterator var3 = this.keyListeners.iterator();

		while (var3.hasNext()) {
			IKeyListener listener = (IKeyListener) var3.next();
			listener.keyTyped(typedChar, keyCode);
		}

		if (!this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Iterator var4 = this.clickListeners.iterator();

		while (var4.hasNext()) {
			IClickListener listener = (IClickListener) var4.next();
			listener.mouseClicked(this, mouseX, mouseY, mouseButton);
		}

	}

	public boolean doesGuiPauseGame() {
		return this.gui != null ? this.gui.getDoesPauseGame() : true;
	}

	public void addDataHolder(IDataHolder component) {
		this.dataHolders.add(component);
	}

	public void addKeyListener(IKeyListener component) {
		this.keyListeners.add(component);
	}

	public void addClickListener(IClickListener component) {
		this.clickListeners.add(component);
	}

	public void setGuiData(NBTTagCompound compound) {
		Minecraft mc = Minecraft.getMinecraft();
		CustomGuiWrapper gui = (CustomGuiWrapper) (new CustomGuiWrapper()).fromNBT(compound);
		((ContainerCustomGui) this.inventorySlots).setGui(gui, mc.player);
		this.gui = gui;
		this.xSize = gui.getWidth();
		this.ySize = gui.getHeight();
		if (!gui.getBackgroundTexture().isEmpty()) {
			this.background = new ResourceLocation(gui.getBackgroundTexture());
		}

		this.initGui();
	}
}
