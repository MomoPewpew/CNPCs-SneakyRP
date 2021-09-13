package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.ability.AbstractAbility;
import noppes.npcs.ability.IAbilityUpdate;
import noppes.npcs.constants.EnumAbilityType;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAbilities extends EntityAIBase {
     private EntityNPCInterface npc;
     private IAbilityUpdate ability;

     public EntityAIAbilities(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public boolean func_75250_a() {
          if (!this.npc.isAttacking()) {
               return false;
          } else {
               this.ability = (IAbilityUpdate)this.npc.abilities.getAbility(EnumAbilityType.UPDATE);
               return this.ability != null;
          }
     }

     public boolean func_75253_b() {
          return this.npc.isAttacking() && this.ability.isActive();
     }

     public void func_75246_d() {
          this.ability.update();
     }

     public void func_75251_c() {
          ((AbstractAbility)this.ability).endAbility();
          this.ability = null;
     }
}
