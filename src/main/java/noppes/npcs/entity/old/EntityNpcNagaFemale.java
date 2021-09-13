package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcNagaFemale extends EntityNPCInterface {
     public EntityNpcNagaFemale(World world) {
          super(world);
          this.scaleX = this.scaleY = this.scaleZ = 0.9075F;
          this.display.setSkinTexture("customnpcs:textures/entity/nagafemale/Claire.png");
     }

     public void func_70071_h_() {
          this.field_70128_L = true;
          this.func_94061_f(true);
          if (!this.world.field_72995_K) {
               NBTTagCompound compound = new NBTTagCompound();
               this.func_189511_e(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.world);
               npc.func_70020_e(compound);
               ModelData data = npc.modelData;
               data.getOrCreatePart(EnumParts.BREASTS).type = 2;
               data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92F, 0.92F);
               data.getPartConfig(EnumParts.HEAD).setScale(0.95F, 0.95F);
               data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.92F);
               data.getPartConfig(EnumParts.BODY).setScale(0.92F, 0.92F);
               ModelPartData legs = data.getOrCreatePart(EnumParts.LEGS);
               legs.playerTexture = true;
               legs.type = 1;
               this.world.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
