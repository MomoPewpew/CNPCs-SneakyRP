package noppes.npcs.api.entity.data.role;

import noppes.npcs.api.entity.IEntityLivingBase;

public interface IJobSpawner {
     IEntityLivingBase spawnEntity(int var1);

     void removeAllSpawned();
}
