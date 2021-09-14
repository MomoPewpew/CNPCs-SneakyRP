package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.IPos;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.IEntityLivingBase;

public class EntityLivingWrapper extends EntityLivingBaseWrapper implements IEntityLiving {
     public EntityLivingWrapper(EntityLiving entity) {
          super(entity);
     }

     public void navigateTo(double x, double y, double z, double speed) {
          ((EntityLiving)this.entity).getNavigator().clearPath();
          ((EntityLiving)this.entity).getNavigator().tryMoveToXYZ(x, y, z, speed * 0.7D);
     }

     public void clearNavigation() {
          ((EntityLiving)this.entity).getNavigator().clearPath();
     }

     public IPos getNavigationPath() {
          if (!this.isNavigating()) {
               return null;
          } else {
               PathPoint point = ((EntityLiving)this.entity).getNavigator().getPath().getFinalPathPoint();
               return point == null ? null : new BlockPosWrapper(new BlockPos(point.field_75839_a, point.field_75837_b, point.field_75838_c));
          }
     }

     public boolean isNavigating() {
          return !((EntityLiving)this.entity).getNavigator().noPath();
     }

     public boolean isAttacking() {
          return super.isAttacking() || ((EntityLiving)this.entity).getAttackTarget() != null;
     }

     public void setAttackTarget(IEntityLivingBase living) {
          if (living == null) {
               ((EntityLiving)this.entity).setAttackTarget((EntityLivingBase)null);
          } else {
               ((EntityLiving)this.entity).setAttackTarget(living.getMCEntity());
          }

          super.setAttackTarget(living);
     }

     public IEntityLivingBase getAttackTarget() {
          IEntityLivingBase base = (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLiving)this.entity).getAttackTarget());
          return base != null ? base : super.getAttackTarget();
     }

     public boolean canSeeEntity(IEntity entity) {
          return ((EntityLiving)this.entity).getEntitySenses().canSee(entity.getMCEntity());
     }

     public void jump() {
          ((EntityLiving)this.entity).getJumpHelper().setJumping();
     }
}
