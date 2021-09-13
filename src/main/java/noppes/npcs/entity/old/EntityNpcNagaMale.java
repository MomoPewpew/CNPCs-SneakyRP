package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcNagaMale extends EntityNPCInterface {
     public EntityNpcNagaMale(World world) {
          super(world);
          this.display.setSkinTexture("customnpcs:textures/entity/nagamale/Cobra.png");
     }

     public void func_70071_h_() {
          this.field_70128_L = true;
          this.func_94061_f(true);
          if (!this.world.isRemote) {
               NBTTagCompound compound = new NBTTagCompound();
               this.func_189511_e(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.world);
               npc.readFromNBT(compound);
               ModelData data = npc.modelData;
               ModelPartData legs = data.getOrCreatePart(EnumParts.LEGS);
               legs.playerTexture = true;
               legs.type = 1;
               this.world.spawnEntity(npc);
          }

          super.func_70071_h_();
     }
}
