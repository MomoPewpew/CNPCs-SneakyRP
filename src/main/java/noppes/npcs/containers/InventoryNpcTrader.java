package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.NoppesUtilServer;

public class InventoryNpcTrader implements IInventory {
     private String inventoryTitle;
     private int slotsCount;
     public final NonNullList inventoryContents;
     private ContainerNPCTrader con;

     public InventoryNpcTrader(String s, int i, ContainerNPCTrader con) {
          this.con = con;
          this.inventoryTitle = s;
          this.slotsCount = i;
          this.inventoryContents = NonNullList.func_191197_a(i, ItemStack.EMPTY);
     }

     public ItemStack getStackInSlot(int i) {
          ItemStack toBuy = (ItemStack)this.inventoryContents.get(i);
          return NoppesUtilServer.IsItemStackNull(toBuy) ? ItemStack.EMPTY : toBuy.copy();
     }

     public ItemStack decrStackSize(int i, int j) {
          ItemStack stack = (ItemStack)this.inventoryContents.get(i);
          return !NoppesUtilServer.IsItemStackNull(stack) ? stack.copy() : ItemStack.EMPTY;
     }

     public void setInventorySlotContents(int i, ItemStack itemstack) {
          if (!itemstack.isEmpty()) {
               this.inventoryContents.set(i, itemstack.copy());
          }

          this.markDirty();
     }

     public int getSizeInventory() {
          return this.slotsCount;
     }

     public int getInventoryStackLimit() {
          return 64;
     }

     public boolean isUseableByPlayer(EntityPlayer entityplayer) {
          return true;
     }

     public ItemStack removeStackFromSlot(int i) {
          return (ItemStack)this.inventoryContents.set(i, ItemStack.EMPTY);
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
