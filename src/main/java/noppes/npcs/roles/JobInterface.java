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
               return ItemStack.EMPTY;
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

               Item item = Item.getByNameOrId(s);
               return item == null ? ItemStack.EMPTY : new ItemStack(item, 1, damage);
          }
     }

     public String itemToString(ItemStack item) {
          return item != null && !item.isEmpty() ? Item.REGISTRY.getNameForObject(item.getItem()) + " - " + item.getItemDamage() : "";
     }

     public int getType() {
          return this.npc.advanced.job;
     }
}
