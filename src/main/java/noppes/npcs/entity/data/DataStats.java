package noppes.npcs.entity.data;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.Resistances;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCMelee;
import noppes.npcs.api.entity.data.INPCRanged;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataStats implements INPCStats {
     public int aggroRange = 16;
     public int maxHealth = 20;
     public int respawnTime = 20;
     public int spawnCycle = 0;
     public boolean hideKilledBody = false;
     public Resistances resistances = new Resistances();
     public boolean immuneToFire = false;
     public boolean potionImmune = false;
     public boolean canDrown = true;
     public boolean burnInSun = false;
     public boolean noFallDamage = false;
     public boolean ignoreCobweb = false;
     public int healthRegen = 1;
     public int combatRegen = 0;
     public EnumCreatureAttribute creatureType;
     public DataMelee melee;
     public DataRanged ranged;
     private EntityNPCInterface npc;

     public DataStats(EntityNPCInterface npc) {
          this.creatureType = EnumCreatureAttribute.UNDEFINED;
          this.npc = npc;
          this.melee = new DataMelee(npc);
          this.ranged = new DataRanged(npc);
     }

     public void readToNBT(NBTTagCompound compound) {
          this.resistances.readToNBT(compound.getCompoundTag("Resistances"));
          this.setMaxHealth(compound.func_74762_e("MaxHealth"));
          this.hideKilledBody = compound.getBoolean("HideBodyWhenKilled");
          this.aggroRange = compound.func_74762_e("AggroRange");
          this.respawnTime = compound.func_74762_e("RespawnTime");
          this.spawnCycle = compound.func_74762_e("SpawnCycle");
          this.creatureType = EnumCreatureAttribute.values()[compound.func_74762_e("CreatureType")];
          this.healthRegen = compound.func_74762_e("HealthRegen");
          this.combatRegen = compound.func_74762_e("CombatRegen");
          this.immuneToFire = compound.getBoolean("ImmuneToFire");
          this.potionImmune = compound.getBoolean("PotionImmune");
          this.canDrown = compound.getBoolean("CanDrown");
          this.burnInSun = compound.getBoolean("BurnInSun");
          this.noFallDamage = compound.getBoolean("NoFallDamage");
          this.npc.setImmuneToFire(this.immuneToFire);
          this.ignoreCobweb = compound.getBoolean("IgnoreCobweb");
          this.melee.readFromNBT(compound);
          this.ranged.readFromNBT(compound);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setTag("Resistances", this.resistances.writeToNBT());
          compound.setInteger("MaxHealth", this.maxHealth);
          compound.setInteger("AggroRange", this.aggroRange);
          compound.func_74757_a("HideBodyWhenKilled", this.hideKilledBody);
          compound.setInteger("RespawnTime", this.respawnTime);
          compound.setInteger("SpawnCycle", this.spawnCycle);
          compound.setInteger("CreatureType", this.creatureType.ordinal());
          compound.setInteger("HealthRegen", this.healthRegen);
          compound.setInteger("CombatRegen", this.combatRegen);
          compound.func_74757_a("ImmuneToFire", this.immuneToFire);
          compound.func_74757_a("PotionImmune", this.potionImmune);
          compound.func_74757_a("CanDrown", this.canDrown);
          compound.func_74757_a("BurnInSun", this.burnInSun);
          compound.func_74757_a("NoFallDamage", this.noFallDamage);
          compound.func_74757_a("IgnoreCobweb", this.ignoreCobweb);
          this.melee.writeToNBT(compound);
          this.ranged.writeToNBT(compound);
          return compound;
     }

     public void setMaxHealth(int maxHealth) {
          if (maxHealth != this.maxHealth) {
               this.maxHealth = maxHealth;
               this.npc.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)maxHealth);
               this.npc.updateClient = true;
          }
     }

     public int getMaxHealth() {
          return this.maxHealth;
     }

     public float getResistance(int type) {
          if (type == 0) {
               return this.resistances.melee;
          } else if (type == 1) {
               return this.resistances.arrow;
          } else if (type == 2) {
               return this.resistances.explosion;
          } else {
               return type == 3 ? this.resistances.knockback : 1.0F;
          }
     }

     public void setResistance(int type, float value) {
          value = ValueUtil.correctFloat(value, 0.0F, 2.0F);
          if (type == 0) {
               this.resistances.melee = value;
          } else if (type == 1) {
               this.resistances.arrow = value;
          } else if (type == 2) {
               this.resistances.explosion = value;
          } else if (type == 3) {
               this.resistances.knockback = value;
          }

     }

     public int getCombatRegen() {
          return this.combatRegen;
     }

     public void setCombatRegen(int regen) {
          this.combatRegen = regen;
     }

     public int getHealthRegen() {
          return this.healthRegen;
     }

     public void setHealthRegen(int regen) {
          this.healthRegen = regen;
     }

     public INPCMelee getMelee() {
          return this.melee;
     }

     public INPCRanged getRanged() {
          return this.ranged;
     }

     public boolean getImmune(int type) {
          if (type == 0) {
               return this.potionImmune;
          } else if (type == 1) {
               return !this.noFallDamage;
          } else if (type == 2) {
               return this.burnInSun;
          } else if (type == 3) {
               return this.immuneToFire;
          } else if (type == 4) {
               return !this.canDrown;
          } else if (type == 5) {
               return this.ignoreCobweb;
          } else {
               throw new CustomNPCsException("Unknown immune type: " + type, new Object[0]);
          }
     }

     public void setImmune(int type, boolean bo) {
          if (type == 0) {
               this.potionImmune = bo;
          } else if (type == 1) {
               this.noFallDamage = !bo;
          } else if (type == 2) {
               this.burnInSun = bo;
          } else if (type == 3) {
               this.npc.setImmuneToFire(bo);
          } else if (type == 4) {
               this.canDrown = !bo;
          } else {
               if (type != 5) {
                    throw new CustomNPCsException("Unknown immune type: " + type, new Object[0]);
               }

               this.ignoreCobweb = bo;
          }

     }

     public int getCreatureType() {
          return this.creatureType.ordinal();
     }

     public void setCreatureType(int type) {
          this.creatureType = EnumCreatureAttribute.values()[type];
     }

     public int getRespawnType() {
          return this.spawnCycle;
     }

     public void setRespawnType(int type) {
          this.spawnCycle = type;
     }

     public int getRespawnTime() {
          return this.respawnTime;
     }

     public void setRespawnTime(int seconds) {
          this.respawnTime = seconds;
     }

     public boolean getHideDeadBody() {
          return this.hideKilledBody;
     }

     public void setHideDeadBody(boolean hide) {
          this.hideKilledBody = hide;
          this.npc.updateClient = true;
     }

     public int getAggroRange() {
          return this.aggroRange;
     }

     public void setAggroRange(int range) {
          this.aggroRange = range;
     }
}
