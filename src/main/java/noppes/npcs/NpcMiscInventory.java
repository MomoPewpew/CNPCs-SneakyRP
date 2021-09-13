package noppes.npcs;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class NpcMiscInventory implements IInventory {
     public final NonNullList items;
     public int stackLimit = 64;
     private int size;

     public NpcMiscInventory(int size) {
          this.size = size;
          this.items = NonNullList.func_191197_a(size, ItemStack.EMPTY);
     }

     public NBTTagCompound getToNBT() {
          NBTTagCompound nbttagcompound = new NBTTagCompound();
          nbttagcompound.setTag("NpcMiscInv", NBTTags.nbtItemStackList(this.items));
          return nbttagcompound;
     }

     public void setFromNBT(NBTTagCompound nbttagcompound) {
          NBTTags.getItemStackList(nbttagcompound.getTagList("NpcMiscInv", 10), this.items);
     }

     public int getSizeInventory() {
          return this.size;
     }

     public ItemStack getStackInSlot(int index) {
          return (ItemStack)this.items.get(index);
     }

     public ItemStack decrStackSize(int index, int count) {
          return ItemStackHelper.func_188382_a(this.items, index, count);
     }

     public boolean decrStackSize(ItemStack eating, int decrease) {
          for(int slot = 0; slot < this.items.size(); ++slot) {
               ItemStack item = (ItemStack)this.items.get(slot);
               if (!item.isEmpty() && eating == item && item.getCount() >= decrease) {
                    item.splitStack(decrease);
                    if (item.getCount() <= 0) {
                         this.items.set(slot, ItemStack.EMPTY);
                    }

                    return true;
               }
          }

          return false;
     }

     public ItemStack removeStackFromSlot(int var1) {
          return (ItemStack)this.items.set(var1, ItemStack.EMPTY);
     }

     public void setInventorySlotContents(int var1, ItemStack var2) {
          if (var1 < this.getSizeInventory()) {
               this.items.set(var1, var2);
          }
     }

     public int getInventoryStackLimit() {
          return this.stackLimit;
     }

     public boolean isUseableByPlayer(EntityPlayer var1) {
          return true;
     }

     public boolean func_94041_b(int i, ItemStack itemstack) {
          return true;
     }

     public void markDirty() {
     }

     public boolean addItemStack(ItemStack item) {
          boolean merged = false;

          ItemStack mergable;
          int slot;
          while(!(mergable = this.getMergableItem(item)).isEmpty() && mergable.getCount() > 0) {
               slot = mergable.func_77976_d() - mergable.getCount();
               if (slot > item.getCount()) {
                    mergable.func_190920_e(mergable.func_77976_d());
                    item.func_190920_e(item.getCount() - slot);
                    merged = true;
               } else {
                    mergable.func_190920_e(mergable.getCount() + item.getCount());
                    item.func_190920_e(0);
               }
          }

          if (item.getCount() <= 0) {
               return true;
          } else {
               slot = this.firstFreeSlot();
               if (slot >= 0) {
                    this.items.set(slot, item.copy());
                    item.func_190920_e(0);
                    return true;
               } else {
                    return merged;
               }
          }
     }

     public ItemStack getMergableItem(ItemStack item) {
          Iterator var2 = this.items.iterator();

          ItemStack is;
          do {
               if (!var2.hasNext()) {
                    return ItemStack.EMPTY;
               }

               is = (ItemStack)var2.next();
          } while(!NoppesUtilPlayer.compareItems(item, is, false, false) || is.getCount() >= is.func_77976_d());

          return is;
     }

     public int firstFreeSlot() {
          for(int i = 0; i < this.getSizeInventory(); ++i) {
               if (((ItemStack)this.items.get(i)).isEmpty()) {
                    return i;
               }
          }

          return -1;
     }

     public void setSize(int i) {
          this.size = i;
     }

     public String func_70005_c_() {
          return "Npc Misc Inventory";
     }

     public boolean func_145818_k_() {
          return true;
     }

     public ITextComponent func_145748_c_() {
          return null;
     }

     public void func_174889_b(EntityPlayer player) {
     }

     public void func_174886_c(EntityPlayer player) {
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
