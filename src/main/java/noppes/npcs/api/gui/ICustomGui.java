package noppes.npcs.api.gui;

import java.util.List;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.item.IItemStack;

public interface ICustomGui {
	int getID();

	int getWidth();

	int getHeight();

	List getComponents();

	List getSlots();

	void setSize(int var1, int var2);

	void setDoesPauseGame(boolean var1);

	void setBackgroundTexture(String var1);

	IButton addButton(int var1, String var2, int var3, int var4);

	IButton addButton(int var1, String var2, int var3, int var4, int var5, int var6);

	IButton addTexturedButton(int var1, String var2, int var3, int var4, int var5, int var6, String var7);

	IButton addTexturedButton(int var1, String var2, int var3, int var4, int var5, int var6, String var7, int var8,
			int var9);

	ILabel addLabel(int var1, String var2, int var3, int var4, int var5, int var6);

	ILabel addLabel(int var1, String var2, int var3, int var4, int var5, int var6, int var7);

	ITextField addTextField(int var1, int var2, int var3, int var4, int var5);

	ITexturedRect addTexturedRect(int var1, String var2, int var3, int var4, int var5, int var6);

	ITexturedRect addTexturedRect(int var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8);

	IScroll addScroll(int var1, int var2, int var3, int var4, int var5, String[] var6);

	IItemSlot addItemSlot(int var1, int var2);

	IItemSlot addItemSlot(int var1, int var2, IItemStack var3);

	void showPlayerInventory(int var1, int var2);

	ICustomGuiComponent getComponent(int var1);

	void removeComponent(int var1);

	void updateComponent(ICustomGuiComponent var1);

	void update(IPlayer var1);
}
