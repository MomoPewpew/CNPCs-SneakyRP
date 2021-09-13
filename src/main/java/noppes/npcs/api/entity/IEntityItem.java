package noppes.npcs.api.entity;

import noppes.npcs.api.item.IItemStack;

public interface IEntityItem extends IEntity {
     String getOwner();

     void setOwner(String var1);

     int getPickupDelay();

     void setPickupDelay(int var1);

     long getAge();

     void setAge(long var1);

     int getLifeSpawn();

     void setLifeSpawn(int var1);

     IItemStack getItem();

     void setItem(IItemStack var1);
}
