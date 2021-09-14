package noppes.npcs.api.wrapper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntityItem;
import noppes.npcs.api.item.IItemStack;

public class EntityItemWrapper extends EntityWrapper implements IEntityItem {
     public EntityItemWrapper(EntityItem entity) {
          super(entity);
     }

     public String getOwner() {
          return ((EntityItem)this.entity).getOwner();
     }

     public void setOwner(String name) {
          ((EntityItem)this.entity).setOwner(name);
     }

     public int getPickupDelay() {
          return ((EntityItem)this.entity).pickupDelay;
     }

     public void setPickupDelay(int delay) {
          ((EntityItem)this.entity).setPickupDelay(delay);
     }

     public int getType() {
          return 6;
     }

     public long getAge() {
          return (long)((EntityItem)this.entity).age;
     }

     public void setAge(long age) {
          age = Math.max(Math.min(age, 2147483647L), -2147483648L);
          ((EntityItem)this.entity).age = (int)age;
     }

     public int getLifeSpawn() {
          return ((EntityItem)this.entity).lifespan;
     }

     public void setLifeSpawn(int age) {
          ((EntityItem)this.entity).lifespan = age;
     }

     public IItemStack getItem() {
          return NpcAPI.Instance().getIItemStack(((EntityItem)this.entity).getItem());
     }

     public void setItem(IItemStack item) {
          ItemStack stack = item == null ? ItemStack.EMPTY : item.getMCItemStack();
          ((EntityItem)this.entity).setItem(stack);
     }
}
