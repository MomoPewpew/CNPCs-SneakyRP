package noppes.npcs.blocks.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;

public abstract class TileNpcContainer extends TileColorable implements IInventory {
     public final NonNullList inventoryContents;
     public String customName = "";
     public int playerUsing = 0;

     public TileNpcContainer() {
          this.inventoryContents = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.EMPTY);
     }

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          NBTTagList nbttaglist = compound.getTagList("Items", 10);
          if (compound.hasKey("CustomName", 8)) {
               this.customName = compound.getString("CustomName");
          }

          this.inventoryContents.clear();

          for(int i = 0; i < nbttaglist.tagCount(); ++i) {
               NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
               int j = nbttagcompound1.getByte("Slot") & 255;
               if (j >= 0 && j < this.inventoryContents.size()) {
                    this.inventoryContents.set(j, new ItemStack(nbttagcompound1));
               }
          }

     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          NBTTagList nbttaglist = new NBTTagList();

          for(int i = 0; i < this.inventoryContents.size(); ++i) {
               if (!((ItemStack)this.inventoryContents.get(i)).isEmpty()) {
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    tagCompound.setByte("Slot", (byte)i);
                    ((ItemStack)this.inventoryContents.get(i)).writeToNBT(tagCompound);
                    nbttaglist.appendTag(tagCompound);
               }
          }

          compound.setTag("Items", nbttaglist);
          if (this.func_145818_k_()) {
               compound.setString("CustomName", this.customName);
          }

          return super.func_189515_b(compound);
     }

     @Override
     public boolean receiveClientEvent(int id, int type) {
          if (id == 1) {
               this.playerUsing = type;
               return true;
          } else {
               return super.receiveClientEvent(id, type);
          }
     }

     public int getSizeInventory() {
          return 54;
     }

     public ItemStack getStackInSlot(int index) {
          return (ItemStack)this.inventoryContents.get(index);
     }

     public ItemStack decrStackSize(int index, int count) {
          ItemStack itemstack = ItemStackHelper.func_188382_a(this.inventoryContents, index, count);
          if (!itemstack.isEmpty()) {
               this.markDirty();
          }

          return itemstack;
     }

     public ItemStack removeStackFromSlot(int index) {
          return (ItemStack)this.inventoryContents.set(index, ItemStack.EMPTY);
     }

     public void setInventorySlotContents(int index, ItemStack stack) {
          this.inventoryContents.set(index, stack);
          if (stack.getCount() > this.getInventoryStackLimit()) {
               stack.func_190920_e(this.getInventoryStackLimit());
          }

          this.markDirty();
     }

     public ITextComponent func_145748_c_() {
          return new TextComponentString(this.func_145818_k_() ? this.customName : this.func_70005_c_());
     }

     public abstract String func_70005_c_();

     public boolean func_145818_k_() {
          return !this.customName.isEmpty();
     }

     public int getInventoryStackLimit() {
          return 64;
     }

     public boolean isUseableByPlayer(EntityPlayer player) {
          return (player.field_70128_L || this.field_145850_b.func_175625_s(this.field_174879_c) == this) && player.getDistanceSq((double)this.field_174879_c.getX() + 0.5D, (double)this.field_174879_c.getY() + 0.5D, (double)this.field_174879_c.getZ() + 0.5D) <= 64.0D;
     }

     public void func_174889_b(EntityPlayer player) {
          ++this.playerUsing;
     }

     public void func_174886_c(EntityPlayer player) {
          --this.playerUsing;
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

     public boolean func_94041_b(int var1, ItemStack var2) {
          return true;
     }

     public void dropItems(World world, BlockPos pos) {
          for(int i1 = 0; i1 < this.getSizeInventory(); ++i1) {
               ItemStack itemstack = this.getStackInSlot(i1);
               if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityitem;
                    for(float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.getCount() > 0; world.func_72838_d(entityitem)) {
                         int j1 = world.rand.nextInt(21) + 10;
                         if (j1 > itemstack.getCount()) {
                              j1 = itemstack.getCount();
                         }

                         itemstack.func_190920_e(itemstack.getCount() - j1);
                         entityitem = new EntityItem(world, (double)((float)pos.getX() + f), (double)((float)pos.getY() + f1), (double)((float)pos.getZ() + f2), new ItemStack(itemstack.func_77973_b(), j1, itemstack.func_77952_i()));
                         float f3 = 0.05F;
                         entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                         entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                         entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
                         if (itemstack.hasTagCompound()) {
                              entityitem.getItem().setTagCompound(itemstack.getTagCompound().copy());
                         }
                    }
               }
          }

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
