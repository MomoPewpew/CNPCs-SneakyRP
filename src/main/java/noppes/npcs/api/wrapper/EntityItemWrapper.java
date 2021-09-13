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
          return ((EntityItem)this.entity).func_145798_i();
     }

     public void setOwner(String name) {
          ((EntityItem)this.entity).func_145797_a(name);
     }

     public int getPickupDelay() {
          return ((EntityItem)this.entity).field_145804_b;
     }

     public void setPickupDelay(int delay) {
          ((EntityItem)this.entity).func_174867_a(delay);
     }

     public int getType() {
          return 6;
     }

     public long getAge() {
          return (long)((EntityItem)this.entity).field_70292_b;
     }

     public void setAge(long age) {
          age = Math.max(Math.min(age, 2147483647L), -2147483648L);
          ((EntityItem)this.entity).field_70292_b = (int)age;
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
          ((EntityItem)this.entity).func_92058_a(stack);
     }
}
