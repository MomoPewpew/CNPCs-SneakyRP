package noppes.npcs.api.wrapper;

import net.minecraft.entity.passive.EntityTameable;
import noppes.npcs.api.entity.IPixelmon;
import noppes.npcs.controllers.PixelmonHelper;

public class PixelmonWrapper extends AnimalWrapper implements IPixelmon {
     public PixelmonWrapper(EntityTameable entity) {
          super(entity);
     }

     public Object getPokemonData() {
          return PixelmonHelper.getPokemonData(this.entity);
     }

     public int getType() {
          return 8;
     }

     public boolean typeOf(int type) {
          return type == 8 ? true : super.typeOf(type);
     }
}
