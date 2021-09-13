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
          this.items = NonNullList.func_191197_a(size, ItemStack.field_190927_a);
     }

     public NBTTagCompound getToNBT() {
          NBTTagCompound nbttagcompound = new NBTTagCompound();
          nbttagcompound.setTag("NpcMiscInv", NBTTags.nbtItemStackList(this.items));
          return nbttagcompound;
     }

     public void setFromNBT(NBTTagCompound nbttagcompound) {
          NBTTags.getItemStackList(nbttagcompound.getTagList("NpcMiscInv", 10), this.items);
     }

     public int func_70302_i_() {
          return this.size;
     }

     public ItemStack func_70301_a(int index) {
          return (ItemStack)this.items.get(index);
     }

     public ItemStack func_70298_a(int index, int count) {
          return ItemStackHelper.func_188382_a(this.items, index, count);
     }

     public boolean decrStackSize(ItemStack eating, int decrease) {
          for(int slot = 0; slot < this.items.size(); ++slot) {
               ItemStack item = (ItemStack)this.items.get(slot);
               if (!item.func_190926_b() && eating == item && item.func_190916_E() >= decrease) {
                    item.splitStack(decrease);
                    if (item.func_190916_E() <= 0) {
                         this.items.set(slot, ItemStack.field_190927_a);
                    }

                    return true;
               }
          }

          return false;
     }

     public ItemStack func_70304_b(int var1) {
          return (ItemStack)this.items.set(var1, ItemStack.field_190927_a);
     }

     public void func_70299_a(int var1, ItemStack var2) {
          if (var1 < this.func_70302_i_()) {
               this.items.set(var1, var2);
          }
     }

     public int func_70297_j_() {
          return this.stackLimit;
     }

     public boolean func_70300_a(EntityPlayer var1) {
          return true;
     }

     public boolean func_94041_b(int i, ItemStack itemstack) {
          return true;
     }

     public void func_70296_d() {
     }

     public boolean addItemStack(ItemStack item) {
          boolean merged = false;

          ItemStack mergable;
          int slot;
          while(!(mergable = this.getMergableItem(item)).func_190926_b() && mergable.func_190916_E() > 0) {
               slot = mergable.func_77976_d() - mergable.func_190916_E();
               if (slot > item.func_190916_E()) {
                    mergable.func_190920_e(mergable.func_77976_d());
                    item.func_190920_e(item.func_190916_E() - slot);
                    merged = true;
               } else {
                    mergable.func_190920_e(mergable.func_190916_E() + item.func_190916_E());
                    item.func_190920_e(0);
               }
          }

          if (item.func_190916_E() <= 0) {
               return true;
          } else {
               slot = this.firstFreeSlot();
               if (slot >= 0) {
                    this.items.set(slot, item.func_77946_l());
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
                    return ItemStack.field_190927_a;
               }

               is = (ItemStack)var2.next();
          } while(!NoppesUtilPlayer.compareItems(item, is, false, false) || is.func_190916_E() >= is.func_77976_d());

          return is;
     }

     public int firstFreeSlot() {
          for(int i = 0; i < this.func_70302_i_(); ++i) {
               if (((ItemStack)this.items.get(i)).func_190926_b()) {
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
          for(int slot = 0; slot < this.func_70302_i_(); ++slot) {
               ItemStack item = this.func_70301_a(slot);
               if (!NoppesUtilServer.IsItemStackNull(item) && !item.func_190926_b()) {
                    return false;
               }
          }

          return true;
     }
}
