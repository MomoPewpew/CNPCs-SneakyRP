package noppes.npcs.ai.target;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIClosestTarget extends EntityAITarget {
     private final Class targetClass;
     private final int targetChance;
     private final Sorter theNearestAttackableTargetSorter;
     private final Predicate field_82643_g;
     private EntityLivingBase targetEntity;
     private EntityNPCInterface npc;

     public EntityAIClosestTarget(EntityNPCInterface npc, Class par2Class, int par3, boolean par4, boolean par5, Predicate par6IEntitySelector) {
          super(npc, par4, par5);
          this.targetClass = par2Class;
          this.targetChance = par3;
          this.theNearestAttackableTargetSorter = new Sorter(npc);
          this.setMutexBits(1);
          this.field_82643_g = par6IEntitySelector;
          this.npc = npc;
     }

     public boolean shouldExecute() {
          if (this.targetChance > 0 && this.field_75299_d.getRNG().nextInt(this.targetChance) != 0) {
               return false;
          } else {
               double d0 = this.func_111175_f();
               List list = this.field_75299_d.world.func_175647_a(this.targetClass, this.field_75299_d.getEntityBoundingBox().expand(d0, (double)MathHelper.func_76143_f(d0 / 2.0D), d0), this.field_82643_g);
               Collections.sort(list, this.theNearestAttackableTargetSorter);
               if (list.isEmpty()) {
                    return false;
               } else {
                    this.targetEntity = (EntityLivingBase)list.get(0);
                    return true;
               }
          }
     }

     public void startExecuting() {
          this.field_75299_d.setAttackTarget(this.targetEntity);
          if (this.targetEntity instanceof EntityMob && ((EntityMob)this.targetEntity).getAttackTarget() == null) {
               ((EntityMob)this.targetEntity).setAttackTarget(this.field_75299_d);
          }

          super.startExecuting();
     }
}
