package noppes.npcs.api.constants;

import net.minecraft.util.EnumParticleTypes;

public class ParticleType {
     public static final int NONE = 0;
     public static final int SMOKE = 1;
     public static final int PORTAL = 2;
     public static final int REDSTONE = 3;
     public static final int LIGHTNING = 4;
     public static final int LARGE_SMOKE = 5;
     public static final int MAGIC = 6;
     public static final int ENCHANT = 7;
     public static final int CRIT = 8;

     public static EnumParticleTypes getMCType(int type) {
          if (type == 1) {
               return EnumParticleTypes.SMOKE_NORMAL;
          } else if (type == 2) {
               return EnumParticleTypes.PORTAL;
          } else if (type == 3) {
               return EnumParticleTypes.REDSTONE;
          } else if (type == 4) {
               return EnumParticleTypes.CRIT_MAGIC;
          } else if (type == 5) {
               return EnumParticleTypes.SMOKE_LARGE;
          } else if (type == 6) {
               return EnumParticleTypes.SPELL_WITCH;
          } else if (type == 7) {
               return EnumParticleTypes.ENCHANTMENT_TABLE;
          } else {
               return type == 8 ? EnumParticleTypes.CRIT : null;
          }
     }
}
