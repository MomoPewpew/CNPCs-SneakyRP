package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcSlime extends EntityNPCInterface {
     public EntityNpcSlime(World world) {
          super(world);
          this.scaleX = 2.0F;
          this.scaleY = 2.0F;
          this.scaleZ = 2.0F;
          this.display.setSkinTexture("customnpcs:textures/entity/slime/Slime.png");
          this.width = 0.8F;
          this.height = 0.8F;
     }

     public void updateHitbox() {
          this.width = 0.8F;
          this.height = 0.8F;
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
               data.setEntityClass(EntityNpcSlime.class);
               this.world.spawnEntity(npc);
          }

          super.onUpdate();
     }
}
