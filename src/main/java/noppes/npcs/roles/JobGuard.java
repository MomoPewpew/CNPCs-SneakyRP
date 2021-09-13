package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityNPCInterface;

public class JobGuard extends JobInterface {
     public List targets = new ArrayList();

     public JobGuard(EntityNPCInterface npc) {
          super(npc);
     }

     public boolean isEntityApplicable(Entity entity) {
          if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityNPCInterface)) {
               return this.targets.contains("entity." + EntityList.func_75621_b(entity) + ".name");
          } else {
               return false;
          }
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.setTag("GuardTargets", NBTTags.nbtStringList(this.targets));
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.targets = NBTTags.getStringList(nbttagcompound.getTagList("GuardTargets", 10));
          Iterator var2;
          EntityEntry ent;
          Class cl;
          String name;
          if (nbttagcompound.getBoolean("GuardAttackAnimals")) {
               var2 = ForgeRegistries.ENTITIES.getValues().iterator();

               while(var2.hasNext()) {
                    ent = (EntityEntry)var2.next();
                    cl = ent.getEntityClass();
                    name = "entity." + ent.getName() + ".name";
                    if (EntityAnimal.class.isAssignableFrom(cl) && !this.targets.contains(name)) {
                         this.targets.add(name);
                    }
               }
          }

          if (nbttagcompound.getBoolean("GuardAttackMobs")) {
               var2 = ForgeRegistries.ENTITIES.getValues().iterator();

               while(var2.hasNext()) {
                    ent = (EntityEntry)var2.next();
                    cl = ent.getEntityClass();
                    name = "entity." + ent.getName() + ".name";
                    if (EntityMob.class.isAssignableFrom(cl) && !EntityCreeper.class.isAssignableFrom(cl) && !this.targets.contains(name)) {
                         this.targets.add(name);
                    }
               }
          }

          if (nbttagcompound.getBoolean("GuardAttackCreepers")) {
               var2 = ForgeRegistries.ENTITIES.getValues().iterator();

               while(var2.hasNext()) {
                    ent = (EntityEntry)var2.next();
                    cl = ent.getEntityClass();
                    name = "entity." + ent.getName() + ".name";
                    if (EntityCreeper.class.isAssignableFrom(cl) && !this.targets.contains(name)) {
                         this.targets.add(name);
                    }
               }
          }

     }
}
