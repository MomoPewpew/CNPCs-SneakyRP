package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCDwarfMale extends EntityNPCInterface {
     public EntityNPCDwarfMale(World world) {
          super(world);
          this.scaleX = this.scaleZ = 0.85F;
          this.scaleY = 0.6875F;
          this.display.setSkinTexture("customnpcs:textures/entity/dwarfmale/Simon.png");
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
               data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1F, 0.7F, 0.9F);
               data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9F, 0.7F);
               data.getPartConfig(EnumParts.BODY).setScale(1.2F, 0.7F, 1.5F);
               data.getPartConfig(EnumParts.HEAD).setScale(0.85F, 0.85F);
               this.field_70170_p.func_72838_d(npc);
          }

          super.func_70071_h_();
     }
}
