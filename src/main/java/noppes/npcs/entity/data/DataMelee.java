package noppes.npcs.entity.data;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.data.INPCMelee;
import noppes.npcs.entity.EntityNPCInterface;

public class DataMelee implements INPCMelee {
     private EntityNPCInterface npc;
     private int attackStrength = 5;
     private int attackSpeed = 20;
     private int attackRange = 2;
     private int knockback = 0;
     private int potionType = 0;
     private int potionDuration = 5;
     private int potionAmp = 0;

     public DataMelee(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.attackSpeed = compound.getInteger("AttackSpeed");
          this.setStrength(compound.getInteger("AttackStrenght"));
          this.attackRange = compound.getInteger("AttackRange");
          this.knockback = compound.getInteger("KnockBack");
          this.potionType = compound.getInteger("PotionEffect");
          this.potionDuration = compound.getInteger("PotionDuration");
          this.potionAmp = compound.getInteger("PotionAmp");
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("AttackStrenght", this.attackStrength);
          compound.setInteger("AttackSpeed", this.attackSpeed);
          compound.setInteger("AttackRange", this.attackRange);
          compound.setInteger("KnockBack", this.knockback);
          compound.setInteger("PotionEffect", this.potionType);
          compound.setInteger("PotionDuration", this.potionDuration);
          compound.setInteger("PotionAmp", this.potionAmp);
          return compound;
     }

     public int getStrength() {
          return this.attackStrength;
     }

     public void setStrength(int strength) {
          this.attackStrength = strength;
          this.npc.getEntityAttribute(SharedMonsterAttributes.field_111264_e).setBaseValue((double)this.attackStrength);
     }

     public int getDelay() {
          return this.attackSpeed;
     }

     public void setDelay(int speed) {
          this.attackSpeed = speed;
     }

     public int getRange() {
          return this.attackRange;
     }

     public void setRange(int range) {
          this.attackRange = range;
     }

     public int getKnockback() {
          return this.knockback;
     }

     public void setKnockback(int knockback) {
          this.knockback = knockback;
     }

     public int getEffectType() {
          return this.potionType;
     }

     public int getEffectTime() {
          return this.potionDuration;
     }

     public int getEffectStrength() {
          return this.potionAmp;
     }

     public void setEffect(int type, int strength, int time) {
          this.potionType = type;
          this.potionDuration = time;
          this.potionAmp = strength;
     }
}
