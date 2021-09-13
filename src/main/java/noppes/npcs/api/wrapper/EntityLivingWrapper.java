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
          ((EntityLiving)this.entity).func_70661_as().func_75499_g();
          ((EntityLiving)this.entity).func_70661_as().func_75492_a(x, y, z, speed * 0.7D);
     }

     public void clearNavigation() {
          ((EntityLiving)this.entity).func_70661_as().func_75499_g();
     }

     public IPos getNavigationPath() {
          if (!this.isNavigating()) {
               return null;
          } else {
               PathPoint point = ((EntityLiving)this.entity).func_70661_as().func_75505_d().func_75870_c();
               return point == null ? null : new BlockPosWrapper(new BlockPos(point.field_75839_a, point.field_75837_b, point.field_75838_c));
          }
     }

     public boolean isNavigating() {
          return !((EntityLiving)this.entity).func_70661_as().func_75500_f();
     }

     public boolean isAttacking() {
          return super.isAttacking() || ((EntityLiving)this.entity).func_70638_az() != null;
     }

     public void setAttackTarget(IEntityLivingBase living) {
          if (living == null) {
               ((EntityLiving)this.entity).func_70624_b((EntityLivingBase)null);
          } else {
               ((EntityLiving)this.entity).func_70624_b(living.getMCEntity());
          }

          super.setAttackTarget(living);
     }

     public IEntityLivingBase getAttackTarget() {
          IEntityLivingBase base = (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLiving)this.entity).func_70638_az());
          return base != null ? base : super.getAttackTarget();
     }

     public boolean canSeeEntity(IEntity entity) {
          return ((EntityLiving)this.entity).func_70635_at().func_75522_a(entity.getMCEntity());
     }

     public void jump() {
          ((EntityLiving)this.entity).func_70683_ar().func_75660_a();
     }
}
