package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.NoppesUtilServer;

public class InventoryNPC implements IInventory {
     private String inventoryTitle;
     private int slotsCount;
     public final NonNullList inventoryContents;
     private Container con;

     public InventoryNPC(String s, int i, Container con) {
          this.con = con;
          this.inventoryTitle = s;
          this.slotsCount = i;
          this.inventoryContents = NonNullList.withSize(i, ItemStack.EMPTY);
     }

     public ItemStack getStackInSlot(int i) {
          return (ItemStack)this.inventoryContents.get(i);
     }

     public ItemStack decrStackSize(int index, int count) {
          return ItemStackHelper.getAndSplit(this.inventoryContents, index, count);
     }

     public void setInventorySlotContents(int index, ItemStack stack) {
          this.inventoryContents.set(index, stack);
          if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
               stack.setCount(this.getInventoryStackLimit());
          }

     }

     public int getSizeInventory() {
          return this.slotsCount;
     }

     public int getInventoryStackLimit() {
          return 64;
     }

     public boolean isUsableByPlayer(EntityPlayer entityplayer) {
          return false;
     }

     public ItemStack removeStackFromSlot(int i) {
          return ItemStackHelper.getAndRemove(this.inventoryContents, i);
     }

     public boolean isItemValidForSlot(int i, ItemStack itemstack) {
          return true;
     }

     public ITextComponent getDisplayName() {
          return new TextComponentString(this.inventoryTitle);
     }

     public boolean hasCustomName() {
          return true;
     }

     public void markDirty() {
          this.con.onCraftMatrixChanged(this);
     }

     public void openInventory(EntityPlayer player) {
     }

     public void closeInventory(EntityPlayer player) {
     }

     public String getName() {
          return null;
     }

     public int getField(int id) {
          return 0;
     }

     public void setField(int id, int value) {
     }

     public int getFieldCount() {
          return 0;
     }

     public void clear() {
     }

     public boolean isEmpty() {
          for(int slot = 0; slot < this.getSizeInventory(); ++slot) {
               ItemStack item = this.getStackInSlot(slot);
               if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                    return false;
               }
          }

          return true;
     }
}
