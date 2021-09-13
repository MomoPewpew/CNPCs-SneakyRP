package noppes.npcs.api.wrapper;

import net.minecraft.entity.projectile.EntityThrowable;
import noppes.npcs.api.entity.IThrowable;

public class ThrowableWrapper extends EntityWrapper implements IThrowable {
     public ThrowableWrapper(EntityThrowable entity) {
          super(entity);
     }

     public int getType() {
          return 11;
     }

     public boolean typeOf(int type) {
          return type == 11 ? true : super.typeOf(type);
     }
}
