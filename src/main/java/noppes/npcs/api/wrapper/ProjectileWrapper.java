package noppes.npcs.api.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityProjectile;

public class ProjectileWrapper extends ThrowableWrapper implements IProjectile {
     public ProjectileWrapper(EntityProjectile entity) {
          super(entity);
     }

     public IItemStack getItem() {
          return NpcAPI.Instance().getIItemStack(((EntityProjectile)this.entity).getItemDisplay());
     }

     public void setItem(IItemStack item) {
          if (item == null) {
               ((EntityProjectile)this.entity).setThrownItem(ItemStack.field_190927_a);
          } else {
               ((EntityProjectile)this.entity).setThrownItem(item.getMCItemStack());
          }

     }

     public boolean getHasGravity() {
          return ((EntityProjectile)this.entity).hasGravity();
     }

     public void setHasGravity(boolean bo) {
          ((EntityProjectile)this.entity).setHasGravity(bo);
     }

     public int getAccuracy() {
          return ((EntityProjectile)this.entity).accuracy;
     }

     public void setAccuracy(int accuracy) {
          ((EntityProjectile)this.entity).accuracy = accuracy;
     }

     public void setHeading(IEntity entity) {
          this.setHeading(entity.getX(), entity.getMCEntity().func_174813_aQ().field_72338_b + (double)(entity.getHeight() / 2.0F), entity.getZ());
     }

     public void setHeading(double x, double y, double z) {
          x -= ((EntityProjectile)this.entity).field_70165_t;
          y -= ((EntityProjectile)this.entity).field_70163_u;
          z -= ((EntityProjectile)this.entity).field_70161_v;
          float varF = ((EntityProjectile)this.entity).hasGravity() ? MathHelper.func_76133_a(x * x + z * z) : 0.0F;
          float angle = ((EntityProjectile)this.entity).getAngleForXYZ(x, y, z, (double)varF, false);
          float acc = 20.0F - (float)MathHelper.func_76141_d((float)((EntityProjectile)this.entity).accuracy / 5.0F);
          ((EntityProjectile)this.entity).func_70186_c(x, y, z, angle, acc);
     }

     public void setHeading(float yaw, float pitch) {
          ((EntityProjectile)this.entity).field_70126_B = ((EntityProjectile)this.entity).field_70177_z = yaw;
          ((EntityProjectile)this.entity).field_70127_C = ((EntityProjectile)this.entity).field_70125_A = pitch;
          double varX = (double)(-MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
          double varZ = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
          double varY = (double)(-MathHelper.func_76126_a(pitch / 180.0F * 3.1415927F));
          float acc = 20.0F - (float)MathHelper.func_76141_d((float)((EntityProjectile)this.entity).accuracy / 5.0F);
          ((EntityProjectile)this.entity).func_70186_c(varX, varY, varZ, -pitch, acc);
     }

     public int getType() {
          return 7;
     }

     public boolean typeOf(int type) {
          return type == 7 ? true : super.typeOf(type);
     }

     public void enableEvents() {
          if (ScriptContainer.Current == null) {
               throw new CustomNPCsException("Can only be called during scripts", new Object[0]);
          } else {
               if (!((EntityProjectile)this.entity).scripts.contains(ScriptContainer.Current)) {
                    ((EntityProjectile)this.entity).scripts.add(ScriptContainer.Current);
               }

          }
     }
}
