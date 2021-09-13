package noppes.npcs.roles;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class JobInterface implements INPCJob {
     public EntityNPCInterface npc;
     public boolean overrideMainHand = false;
     public boolean overrideOffHand = false;

     public JobInterface(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public abstract NBTTagCompound writeToNBT(NBTTagCompound var1);

     public abstract void readFromNBT(NBTTagCompound var1);

     public void killed() {
     }

     public void delete() {
     }

     public boolean aiShouldExecute() {
          return false;
     }

     public boolean aiContinueExecute() {
          return this.aiShouldExecute();
     }

     public void aiStartExecuting() {
     }

     public void aiUpdateTask() {
     }

     public void reset() {
     }

     public void resetTask() {
     }

     public IItemStack getMainhand() {
          return null;
     }

     public IItemStack getOffhand() {
          return null;
     }

     public boolean isFollowing() {
          return false;
     }

     public int getMutexBits() {
          return 0;
     }

     public ItemStack stringToItem(String s) {
          if (s.isEmpty()) {
               return ItemStack.field_190927_a;
          } else {
               int damage = 0;
               if (s.contains(" - ")) {
                    String[] split = s.split(" - ");
                    if (split.length == 2) {
                         try {
                              damage = Integer.parseInt(split[1]);
                         } catch (NumberFormatException var5) {
                         }

                         s = split[0];
                    }
               }

               Item item = Item.func_111206_d(s);
               return item == null ? ItemStack.field_190927_a : new ItemStack(item, 1, damage);
          }
     }

     public String itemToString(ItemStack item) {
          return item != null && !item.func_190926_b() ? Item.field_150901_e.func_177774_c(item.func_77973_b()) + " - " + item.func_77952_i() : "";
     }

     public int getType() {
          return this.npc.advanced.job;
     }
}
