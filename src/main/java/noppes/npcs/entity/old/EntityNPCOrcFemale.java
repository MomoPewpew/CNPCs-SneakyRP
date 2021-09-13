package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCOrcFemale extends EntityNPCInterface {
     public EntityNPCOrcFemale(World world) {
          super(world);
          this.scaleX = this.scaleY = this.scaleZ = 0.9375F;
          this.display.setSkinTexture("customnpcs:textures/entity/orcfemale/StrandedFemaleOrc.png");
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
               data.getOrCreatePart(EnumParts.BREASTS).type = 2;
               data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1F, 1.0F);
               data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.1F, 1.0F);
               data.getPartConfig(EnumParts.BODY).setScale(1.1F, 1.0F, 1.25F);
               this.field_70170_p.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
