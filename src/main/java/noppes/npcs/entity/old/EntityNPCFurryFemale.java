package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCFurryFemale extends EntityNPCInterface {
     public EntityNPCFurryFemale(World world) {
          super(world);
          this.scaleX = this.scaleY = this.scaleZ = 0.9075F;
          this.display.setSkinTexture("customnpcs:textures/entity/furryfemale/WolfBlack.png");
     }

     public void func_70071_h_() {
          this.field_70128_L = true;
          this.func_94061_f(true);
          if (!this.world.isRemote) {
               NBTTagCompound compound = new NBTTagCompound();
               this.func_189511_e(compound);
               EntityCustomNpc npc = new EntityCustomNpc(this.world);
               npc.func_70020_e(compound);
               ModelData data = npc.modelData;
               ModelPartData ears = data.getOrCreatePart(EnumParts.EARS);
               ears.type = 0;
               ears.color = 6182997;
               ModelPartData snout = data.getOrCreatePart(EnumParts.SNOUT);
               snout.type = 2;
               snout.color = 6182997;
               ModelPartData tail = data.getOrCreatePart(EnumParts.TAIL);
               tail.type = 0;
               tail.color = 6182997;
               data.getOrCreatePart(EnumParts.BREASTS).type = 2;
               data.getPartConfig(EnumParts.HEAD).setScale(0.95F, 0.95F);
               data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92F, 0.92F);
               data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.92F);
               data.getPartConfig(EnumParts.BODY).setScale(0.92F, 0.92F);
               this.world.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
