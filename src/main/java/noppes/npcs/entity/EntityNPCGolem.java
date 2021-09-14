package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNPCGolem extends EntityNPCInterface {
     public EntityNPCGolem(World world) {
          super(world);
          this.display.setSkinTexture("customnpcs:textures/entity/golem/Iron Golem.png");
          this.field_70130_N = 1.4F;
          this.height = 2.5F;
     }

     public void updateHitbox() {
          this.currentAnimation = (Integer)this.field_70180_af.func_187225_a(Animation);
          if (this.currentAnimation == 2) {
               this.field_70130_N = this.height = 0.5F;
          } else if (this.currentAnimation == 1) {
               this.field_70130_N = 1.4F;
               this.height = 2.0F;
          } else {
               this.field_70130_N = 1.4F;
               this.height = 2.5F;
          }

     }

     public void func_70071_h_() {
          this.field_70128_L = true;
          this.func_94061_f(true);
          if (!this.world.isRemote) {
               NBTTagCompound compound = new NBTTagCompound();
               this.writeToNBT(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.world);
               npc.readFromNBT(compound);
               ModelData data = npc.modelData;
               data.setEntityClass(EntityNPCGolem.class);
               this.world.spawnEntity(npc);
          }

          super.func_70071_h_();
     }
}
