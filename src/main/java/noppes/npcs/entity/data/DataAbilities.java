package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ability.AbstractAbility;
import noppes.npcs.constants.EnumAbilityType;
import noppes.npcs.entity.EntityNPCInterface;

public class DataAbilities {
     public List abilities = new ArrayList();
     public EntityNPCInterface npc;

     public DataAbilities(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          return compound;
     }

     public void readToNBT(NBTTagCompound compound) {
     }

     public AbstractAbility getAbility(EnumAbilityType type) {
          EntityLivingBase target = this.npc.getAttackTarget();
          Iterator var3 = this.abilities.iterator();

          AbstractAbility ability;
          do {
               if (!var3.hasNext()) {
                    return null;
               }

               ability = (AbstractAbility)var3.next();
          } while(!ability.isType(type) || !ability.canRun(target));

          return ability;
     }
}
