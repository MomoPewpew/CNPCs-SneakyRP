package noppes.npcs;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSourceIndirect;

public class NpcDamageSourceInderect extends EntityDamageSourceIndirect {
     public NpcDamageSourceInderect(String par1Str, Entity par2Entity, Entity par3Entity) {
          super(par1Str, par2Entity, par3Entity);
     }

     public boolean func_76350_n() {
          return false;
     }
}
