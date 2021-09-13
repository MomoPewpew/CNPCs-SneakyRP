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
          this.inventoryContents = NonNullList.func_191197_a(i, ItemStack.EMPTY);
     }

     public ItemStack getStackInSlot(int i) {
          return (ItemStack)this.inventoryContents.get(i);
     }

     public ItemStack decrStackSize(int index, int count) {
          return ItemStackHelper.func_188382_a(this.inventoryContents, index, count);
     }

     public void setInventorySlotContents(int index, ItemStack stack) {
          this.inventoryContents.set(index, stack);
          if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
               stack.func_190920_e(this.getInventoryStackLimit());
          }

     }

     public int getSizeInventory() {
          return this.slotsCount;
     }

     public int getInventoryStackLimit() {
          return 64;
     }

     public boolean isUseableByPlayer(EntityPlayer entityplayer) {
          return false;
     }

     public ItemStack removeStackFromSlot(int i) {
          return ItemStackHelper.func_188383_a(this.inventoryContents, i);
     }

     public boolean func_94041_b(int i, ItemStack itemstack) {
          return true;
     }

     public ITextComponent func_145748_c_() {
          return new TextComponentString(this.inventoryTitle);
     }

     public boolean func_145818_k_() {
          return true;
     }

     public void markDirty() {
          this.con.onCraftMatrixChanged(this);
     }

     public void func_174889_b(EntityPlayer player) {
     }

     public void func_174886_c(EntityPlayer player) {
     }

     public String func_70005_c_() {
          return null;
     }

     public int func_174887_a_(int id) {
          return 0;
     }

     public void func_174885_b(int id, int value) {
     }

     public int func_174890_g() {
          return 0;
     }

     public void func_174888_l() {
     }

     public boolean func_191420_l() {
          for(int slot = 0; slot < this.getSizeInventory(); ++slot) {
               ItemStack item = this.getStackInSlot(slot);
               if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                    return false;
               }
          }

          return true;
     }
}
