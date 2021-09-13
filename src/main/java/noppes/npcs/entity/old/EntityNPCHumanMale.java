package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCHumanMale extends EntityNPCInterface {
     public EntityNPCHumanMale(World world) {
          super(world);
     }

     public void func_70071_h_() {
          this.field_70128_L = true;
          this.func_94061_f(true);
          if (!this.field_70170_p.field_72995_K) {
               NBTTagCompound compound = new NBTTagCompound();
               this.func_189511_e(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.field_70170_p);
               npc.func_70020_e(compound);
               this.field_70170_p.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
