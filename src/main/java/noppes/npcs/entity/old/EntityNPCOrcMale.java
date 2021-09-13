package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCOrcMale extends EntityNPCInterface {
     public EntityNPCOrcMale(World world) {
          super(world);
          this.scaleY = 1.0F;
          this.scaleX = this.scaleZ = 1.2F;
          this.display.setSkinTexture("customnpcs:textures/entity/orcmale/StrandedOrc.png");
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
               data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.2F, 1.05F);
               data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.2F, 1.05F);
               data.getPartConfig(EnumParts.BODY).setScale(1.4F, 1.1F, 1.5F);
               data.getPartConfig(EnumParts.HEAD).setScale(1.2F, 1.1F);
               this.world.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
