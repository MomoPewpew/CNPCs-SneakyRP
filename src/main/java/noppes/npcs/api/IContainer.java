package noppes.npcs.api;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import noppes.npcs.api.item.IItemStack;

public interface IContainer {
     int getSize();

     IItemStack getSlot(int var1);

     void setSlot(int var1, IItemStack var2);

     IInventory getMCInventory();

     Container getMCContainer();

     int count(IItemStack var1, boolean var2, boolean var3);

     IItemStack[] getItems();
}
