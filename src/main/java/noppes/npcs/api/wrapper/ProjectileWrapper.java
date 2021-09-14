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
               ((EntityProjectile)this.entity).setThrownItem(ItemStack.EMPTY);
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
          this.setHeading(entity.getX(), entity.getMCEntity().getEntityBoundingBox().minY + (double)(entity.getHeight() / 2.0F), entity.getZ());
     }

     public void setHeading(double x, double y, double z) {
          x -= ((EntityProjectile)this.entity).posX;
          y -= ((EntityProjectile)this.entity).posY;
          z -= ((EntityProjectile)this.entity).posZ;
          float varF = ((EntityProjectile)this.entity).hasGravity() ? MathHelper.sqrt(x * x + z * z) : 0.0F;
          float angle = ((EntityProjectile)this.entity).getAngleForXYZ(x, y, z, (double)varF, false);
          float acc = 20.0F - (float)MathHelper.floor((float)((EntityProjectile)this.entity).accuracy / 5.0F);
          ((EntityProjectile)this.entity).shoot(x, y, z, angle, acc);
     }

     public void setHeading(float yaw, float pitch) {
          ((EntityProjectile)this.entity).prevRotationYaw = ((EntityProjectile)this.entity).rotationYaw = yaw;
          ((EntityProjectile)this.entity).prevRotationPitch = ((EntityProjectile)this.entity).rotationPitch = pitch;
          double varX = (double)(-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F));
          double varZ = (double)(MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F));
          double varY = (double)(-MathHelper.sin(pitch / 180.0F * 3.1415927F));
          float acc = 20.0F - (float)MathHelper.floor((float)((EntityProjectile)this.entity).accuracy / 5.0F);
          ((EntityProjectile)this.entity).shoot(varX, varY, varZ, -pitch, acc);
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
