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
          this.inventoryContents = NonNullList.func_191197_a(i, ItemStack.field_190927_a);
     }

     public ItemStack func_70301_a(int i) {
          ItemStack toBuy = (ItemStack)this.inventoryContents.get(i);
          return NoppesUtilServer.IsItemStackNull(toBuy) ? ItemStack.field_190927_a : toBuy.func_77946_l();
     }

     public ItemStack func_70298_a(int i, int j) {
          ItemStack stack = (ItemStack)this.inventoryContents.get(i);
          return !NoppesUtilServer.IsItemStackNull(stack) ? stack.func_77946_l() : ItemStack.field_190927_a;
     }

     public void func_70299_a(int i, ItemStack itemstack) {
          if (!itemstack.func_190926_b()) {
               this.inventoryContents.set(i, itemstack.func_77946_l());
          }

          this.func_70296_d();
     }

     public int func_70302_i_() {
          return this.slotsCount;
     }

     public int func_70297_j_() {
          return 64;
     }

     public boolean func_70300_a(EntityPlayer entityplayer) {
          return true;
     }

     public ItemStack func_70304_b(int i) {
          return (ItemStack)this.inventoryContents.set(i, ItemStack.field_190927_a);
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

     public void func_70296_d() {
          this.con.func_75130_a(this);
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
          for(int slot = 0; slot < this.func_70302_i_(); ++slot) {
               ItemStack item = this.func_70301_a(slot);
               if (!NoppesUtilServer.IsItemStackNull(item) && !item.func_190926_b()) {
                    return false;
               }
          }

          return true;
     }
}
