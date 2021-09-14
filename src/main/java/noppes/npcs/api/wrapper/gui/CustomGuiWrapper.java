package noppes.npcs.api.wrapper.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.gui.IButton;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.gui.ILabel;
import noppes.npcs.api.gui.IScroll;
import noppes.npcs.api.gui.ITextField;
import noppes.npcs.api.gui.ITexturedRect;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.controllers.ScriptContainer;

public class CustomGuiWrapper implements ICustomGui {
	int id;
	int width;
	int height;
	int playerInvX;
	int playerInvY;
	boolean pauseGame;
	boolean showPlayerInv;
	String backgroundTexture = "";
	ScriptContainer scriptHandler;
	List components = new ArrayList();
	List slots = new ArrayList();

	public CustomGuiWrapper() {
	}

	public CustomGuiWrapper(int id, int width, int height, boolean pauseGame) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.pauseGame = pauseGame;
		this.scriptHandler = ScriptContainer.Current;
	}

	public int getID() {
		return this.id;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public List getComponents() {
		return this.components;
	}

	public List getSlots() {
		return this.slots;
	}

	public ScriptContainer getScriptHandler() {
		return this.scriptHandler;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setDoesPauseGame(boolean pauseGame) {
		this.pauseGame = pauseGame;
	}

	public boolean getDoesPauseGame() {
		return this.pauseGame;
	}

	public void setBackgroundTexture(String resourceLocation) {
		this.backgroundTexture = resourceLocation;
	}

	public String getBackgroundTexture() {
		return this.backgroundTexture;
	}

	public IButton addButton(int id, String label, int x, int y) {
		CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y);
		this.components.add(component);
		return (IButton) this.components.get(this.components.size() - 1);
	}

	public IButton addButton(int id, String label, int x, int y, int width, int height) {
		CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y, width, height);
		this.components.add(component);
		return (IButton) this.components.get(this.components.size() - 1);
	}

	public IButton addTexturedButton(int id, String label, int x, int y, int width, int height, String texture) {
		CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y, width, height, texture);
		this.components.add(component);
		return (IButton) this.components.get(this.components.size() - 1);
	}

	public IButton addTexturedButton(int id, String label, int x, int y, int width, int height, String texture,
			int textureX, int textureY) {
		CustomGuiButtonWrapper component = new CustomGuiButtonWrapper(id, label, x, y, width, height, texture, textureX,
				textureY);
		this.components.add(component);
		return (IButton) this.components.get(this.components.size() - 1);
	}

	public ILabel addLabel(int id, String label, int x, int y, int width, int height) {
		CustomGuiLabelWrapper component = new CustomGuiLabelWrapper(id, label, x, y, width, height);
		this.components.add(component);
		return (ILabel) this.components.get(this.components.size() - 1);
	}

	public ILabel addLabel(int id, String label, int x, int y, int width, int height, int color) {
		CustomGuiLabelWrapper component = new CustomGuiLabelWrapper(id, label, x, y, width, height, color);
		this.components.add(component);
		return (ILabel) this.components.get(this.components.size() - 1);
	}

	public ITextField addTextField(int id, int x, int y, int width, int height) {
		CustomGuiTextFieldWrapper component = new CustomGuiTextFieldWrapper(id, x, y, width, height);
		this.components.add(component);
		return (ITextField) this.components.get(this.components.size() - 1);
	}

	public ITexturedRect addTexturedRect(int id, String texture, int x, int y, int width, int height) {
		CustomGuiTexturedRectWrapper component = new CustomGuiTexturedRectWrapper(id, texture, x, y, width, height);
		this.components.add(component);
		return (ITexturedRect) this.components.get(this.components.size() - 1);
	}

	public ITexturedRect addTexturedRect(int id, String texture, int x, int y, int width, int height, int textureX,
			int textureY) {
		CustomGuiTexturedRectWrapper component = new CustomGuiTexturedRectWrapper(id, texture, x, y, width, height,
				textureX, textureY);
		this.components.add(component);
		return (ITexturedRect) this.components.get(this.components.size() - 1);
	}

	public IItemSlot addItemSlot(int x, int y) {
		CustomGuiItemSlotWrapper slot = new CustomGuiItemSlotWrapper(x, y);
		this.slots.add(slot);
		return (IItemSlot) this.slots.get(this.slots.size() - 1);
	}

	public IItemSlot addItemSlot(int x, int y, IItemStack stack) {
		CustomGuiItemSlotWrapper slot = new CustomGuiItemSlotWrapper(x, y, stack);
		this.slots.add(slot);
		return (IItemSlot) this.slots.get(this.slots.size() - 1);
	}

	public IScroll addScroll(int id, int x, int y, int width, int height, String[] list) {
		CustomGuiScrollWrapper component = new CustomGuiScrollWrapper(id, x, y, width, height, list);
		this.components.add(component);
		return (IScroll) this.components.get(this.components.size() - 1);
	}

	public void showPlayerInventory(int x, int y) {
		this.showPlayerInv = true;
		this.playerInvX = x;
		this.playerInvY = y;
	}

	public ICustomGuiComponent getComponent(int componentID) {
		Iterator var2 = this.components.iterator();

		ICustomGuiComponent component;
		do {
			if (!var2.hasNext()) {
				return null;
			}

			component = (ICustomGuiComponent) var2.next();
		} while (component.getID() != componentID);

		return component;
	}

	public void removeComponent(int componentID) {
		for (int i = 0; i < this.components.size(); ++i) {
			if (((ICustomGuiComponent) this.components.get(i)).getID() == componentID) {
				this.components.remove(i);
				return;
			}
		}

	}

	public void updateComponent(ICustomGuiComponent component) {
		for (int i = 0; i < this.components.size(); ++i) {
			ICustomGuiComponent c = (ICustomGuiComponent) this.components.get(i);
			if (c.getID() == component.getID()) {
				this.components.set(i, component);
				return;
			}
		}

	}

	public void update(IPlayer player) {
		CustomGuiController.updateGui((PlayerWrapper) player, this);
	}

	public boolean getShowPlayerInv() {
		return this.showPlayerInv;
	}

	public int getPlayerInvX() {
		return this.playerInvX;
	}

	public int getPlayerInvY() {
		return this.playerInvY;
	}

	public ICustomGui fromNBT(NBTTagCompound tag) {
		this.id = tag.getInteger("id");
		this.width = tag.getIntArray("size")[0];
		this.height = tag.getIntArray("size")[1];
		this.pauseGame = tag.getBoolean("pause");
		this.backgroundTexture = tag.getString("bgTexture");
		List components = new ArrayList();
		NBTTagList list = tag.getTagList("components", 10);
		Iterator var4 = list.iterator();

		while (var4.hasNext()) {
			NBTBase b = (NBTBase) var4.next();
			CustomGuiComponentWrapper component = CustomGuiComponentWrapper.createFromNBT((NBTTagCompound) b);
			components.add(component);
		}

		this.components = components;
		List slots = new ArrayList();
		list = tag.getTagList("slots", 10);
		Iterator var9 = list.iterator();

		while (var9.hasNext()) {
			NBTBase b = (NBTBase) var9.next();
			CustomGuiItemSlotWrapper component = (CustomGuiItemSlotWrapper) CustomGuiComponentWrapper
					.createFromNBT((NBTTagCompound) b);
			slots.add(component);
		}

		this.slots = slots;
		this.showPlayerInv = tag.getBoolean("showPlayerInv");
		if (this.showPlayerInv) {
			this.playerInvX = tag.getIntArray("pInvPos")[0];
			this.playerInvY = tag.getIntArray("pInvPos")[1];
		}

		return this;
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("id", this.id);
		tag.setIntArray("size", new int[] { this.width, this.height });
		tag.setBoolean("pause", this.pauseGame);
		tag.setString("bgTexture", this.backgroundTexture);
		NBTTagList list = new NBTTagList();
		Iterator var3 = this.components.iterator();

		ICustomGuiComponent c;
		while (var3.hasNext()) {
			c = (ICustomGuiComponent) var3.next();
			list.appendTag(((CustomGuiComponentWrapper) c).toNBT(new NBTTagCompound()));
		}

		tag.setTag("components", list);
		list = new NBTTagList();
		var3 = this.slots.iterator();

		while (var3.hasNext()) {
			c = (ICustomGuiComponent) var3.next();
			list.appendTag(((CustomGuiComponentWrapper) c).toNBT(new NBTTagCompound()));
		}

		tag.setTag("slots", list);
		tag.setBoolean("showPlayerInv", this.showPlayerInv);
		if (this.showPlayerInv) {
			tag.setIntArray("pInvPos", new int[] { this.playerInvX, this.playerInvY });
		}

		return tag;
	}
}
