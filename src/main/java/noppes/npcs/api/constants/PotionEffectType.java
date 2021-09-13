package noppes.npcs.api.constants;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;

public final class PotionEffectType {
     public static final int NONE = 0;
     public static final int FIRE = 1;
     public static final int POISON = 2;
     public static final int HUNGER = 3;
     public static final int WEAKNESS = 4;
     public static final int SLOWNESS = 5;
     public static final int NAUSEA = 6;
     public static final int BLINDNESS = 7;
     public static final int WITHER = 8;

     public static Potion getMCType(int effect) {
          switch(effect) {
          case 2:
               return MobEffects.field_76436_u;
          case 3:
               return MobEffects.field_76438_s;
          case 4:
               return MobEffects.field_76437_t;
          case 5:
               return MobEffects.field_76421_d;
          case 6:
               return MobEffects.field_76431_k;
          case 7:
               return MobEffects.field_76440_q;
          case 8:
               return MobEffects.field_82731_v;
          default:
               return null;
          }
     }
}
