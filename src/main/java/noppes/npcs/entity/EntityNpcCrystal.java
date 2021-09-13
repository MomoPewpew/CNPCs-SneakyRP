package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcCrystal extends EntityNPCInterface {
     public EntityNpcCrystal(World world) {
          super(world);
          this.scaleX = 0.7F;
          this.scaleY = 0.7F;
          this.scaleZ = 0.7F;
          this.display.setSkinTexture("customnpcs:textures/entity/crystal/EnderCrystal.png");
     }

     public void func_70071_h_() {
          this.field_70128_L = true;
          this.func_94061_f(true);
          if (!this.field_70170_p.field_72995_K) {
               NBTTagCompound compound = new NBTTagCompound();
               this.func_189511_e(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.field_70170_p);
               npc.func_70020_e(compound);
               ModelData data = npc.modelData;
               data.setEntityClass(EntityNpcCrystal.class);
               this.field_70170_p.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
