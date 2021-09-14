package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNPCGolem extends EntityNPCInterface {
     public EntityNPCGolem(World world) {
          super(world);
          this.display.setSkinTexture("customnpcs:textures/entity/golem/Iron Golem.png");
          this.width = 1.4F;
          this.height = 2.5F;
     }

     public void updateHitbox() {
          this.currentAnimation = (Integer)this.dataManager.get(Animation);
          if (this.currentAnimation == 2) {
               this.width = this.height = 0.5F;
          } else if (this.currentAnimation == 1) {
               this.width = 1.4F;
               this.height = 2.0F;
          } else {
               this.width = 1.4F;
               this.height = 2.5F;
          }

     }

     public void onUpdate() {
          this.isDead = true;
          this.setNoAI(true);
          if (!this.world.isRemote) {
               NBTTagCompound compound = new NBTTagCompound();
               this.writeToNBT(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.world);
               npc.readFromNBT(compound);
               ModelData data = npc.modelData;
               data.setEntityClass(EntityNPCGolem.class);
               this.world.spawnEntity(npc);
          }

          super.onUpdate();
     }
}
