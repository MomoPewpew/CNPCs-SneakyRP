package noppes.npcs.api.entity;

import net.minecraft.entity.EntityLiving;
import noppes.npcs.api.IPos;

public interface IEntityLiving extends IEntityLivingBase {
     boolean isNavigating();

     void clearNavigation();

     void navigateTo(double var1, double var3, double var5, double var7);

     void jump();

     EntityLiving getMCEntity();

     IPos getNavigationPath();
}
