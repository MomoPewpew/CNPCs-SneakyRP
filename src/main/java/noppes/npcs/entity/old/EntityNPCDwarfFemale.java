package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCDwarfFemale extends EntityNPCInterface {
     public EntityNPCDwarfFemale(World world) {
          super(world);
          this.scaleX = this.scaleZ = 0.75F;
          this.scaleY = 0.6275F;
          this.display.setSkinTexture("customnpcs:textures/entity/dwarffemale/Simone.png");
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
               data.getOrCreatePart(EnumParts.BREASTS).type = 2;
               data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.9F, 0.65F);
               data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9F, 0.65F);
               data.getPartConfig(EnumParts.BODY).setScale(1.0F, 0.65F, 1.1F);
               data.getPartConfig(EnumParts.HEAD).setScale(0.85F, 0.85F);
               this.world.spawnEntity(npc);
          }

          super.func_70071_h_();
     }
}
