package noppes.npcs.entity.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import noppes.npcs.api.entity.data.INPCRanged;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataRanged implements INPCRanged {
     private EntityNPCInterface npc;
     private int burstCount = 1;
     private int pDamage = 4;
     private int pSpeed = 10;
     private int pImpact = 0;
     private int pSize = 5;
     private int pArea = 0;
     private int pTrail = 0;
     private int minDelay = 20;
     private int maxDelay = 40;
     private int rangedRange = 15;
     private int fireRate = 5;
     private int shotCount = 1;
     private int accuracy = 60;
     private int meleeDistance = 0;
     private int canFireIndirect = 0;
     private boolean pRender3D = true;
     private boolean pSpin = false;
     private boolean pStick = false;
     private boolean pPhysics = true;
     private boolean pXlr8 = false;
     private boolean pGlows = false;
     private boolean aimWhileShooting = false;
     private int pEffect = 0;
     private int pDur = 5;
     private int pEffAmp = 0;
     private String fireSound = "minecraft:entity.arrow.shoot";
     private String hitSound = "minecraft:entity.arrow.hit";
     private String groundSound = "minecraft:block.stone.break";

     public DataRanged(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.pDamage = compound.func_74762_e("pDamage");
          this.pSpeed = compound.func_74762_e("pSpeed");
          this.burstCount = compound.func_74762_e("BurstCount");
          this.pImpact = compound.func_74762_e("pImpact");
          this.pSize = compound.func_74762_e("pSize");
          this.pArea = compound.func_74762_e("pArea");
          this.pTrail = compound.func_74762_e("pTrail");
          this.rangedRange = compound.func_74762_e("MaxFiringRange");
          this.fireRate = compound.func_74762_e("FireRate");
          this.minDelay = ValueUtil.CorrectInt(compound.func_74762_e("minDelay"), 1, 9999);
          this.maxDelay = ValueUtil.CorrectInt(compound.func_74762_e("maxDelay"), 1, 9999);
          this.shotCount = ValueUtil.CorrectInt(compound.func_74762_e("ShotCount"), 1, 10);
          this.accuracy = compound.func_74762_e("Accuracy");
          this.pRender3D = compound.getBoolean("pRender3D");
          this.pSpin = compound.getBoolean("pSpin");
          this.pStick = compound.getBoolean("pStick");
          this.pPhysics = compound.getBoolean("pPhysics");
          this.pXlr8 = compound.getBoolean("pXlr8");
          this.pGlows = compound.getBoolean("pGlows");
          this.aimWhileShooting = compound.getBoolean("AimWhileShooting");
          this.pEffect = compound.func_74762_e("pEffect");
          this.pDur = compound.func_74762_e("pDur");
          this.pEffAmp = compound.func_74762_e("pEffAmp");
          this.fireSound = compound.getString("FiringSound");
          this.hitSound = compound.getString("HitSound");
          this.groundSound = compound.getString("GroundSound");
          this.canFireIndirect = compound.func_74762_e("FireIndirect");
          this.meleeDistance = compound.func_74762_e("DistanceToMelee");
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("BurstCount", this.burstCount);
          compound.setInteger("pSpeed", this.pSpeed);
          compound.setInteger("pDamage", this.pDamage);
          compound.setInteger("pImpact", this.pImpact);
          compound.setInteger("pSize", this.pSize);
          compound.setInteger("pArea", this.pArea);
          compound.setInteger("pTrail", this.pTrail);
          compound.setInteger("MaxFiringRange", this.rangedRange);
          compound.setInteger("FireRate", this.fireRate);
          compound.setInteger("minDelay", this.minDelay);
          compound.setInteger("maxDelay", this.maxDelay);
          compound.setInteger("ShotCount", this.shotCount);
          compound.setInteger("Accuracy", this.accuracy);
          compound.func_74757_a("pRender3D", this.pRender3D);
          compound.func_74757_a("pSpin", this.pSpin);
          compound.func_74757_a("pStick", this.pStick);
          compound.func_74757_a("pPhysics", this.pPhysics);
          compound.func_74757_a("pXlr8", this.pXlr8);
          compound.func_74757_a("pGlows", this.pGlows);
          compound.func_74757_a("AimWhileShooting", this.aimWhileShooting);
          compound.setInteger("pEffect", this.pEffect);
          compound.setInteger("pDur", this.pDur);
          compound.setInteger("pEffAmp", this.pEffAmp);
          compound.setString("FiringSound", this.fireSound);
          compound.setString("HitSound", this.hitSound);
          compound.setString("GroundSound", this.groundSound);
          compound.setInteger("FireIndirect", this.canFireIndirect);
          compound.setInteger("DistanceToMelee", this.meleeDistance);
          return compound;
     }

     public int getStrength() {
          return this.pDamage;
     }

     public void setStrength(int strength) {
          this.pDamage = strength;
     }

     public int getSpeed() {
          return this.pSpeed;
     }

     public void setSpeed(int speed) {
          this.pSpeed = ValueUtil.CorrectInt(speed, 0, 100);
     }

     public int getKnockback() {
          return this.pImpact;
     }

     public void setKnockback(int punch) {
          this.pImpact = punch;
     }

     public int getSize() {
          return this.pSize;
     }

     public void setSize(int size) {
          this.pSize = size;
     }

     public boolean getRender3D() {
          return this.pRender3D;
     }

     public void setRender3D(boolean render3d) {
          this.pRender3D = render3d;
     }

     public boolean getSpins() {
          return this.pSpin;
     }

     public void setSpins(boolean spins) {
          this.pSpin = spins;
     }

     public boolean getSticks() {
          return this.pStick;
     }

     public void setSticks(boolean sticks) {
          this.pStick = sticks;
     }

     public boolean getHasGravity() {
          return this.pPhysics;
     }

     public void setHasGravity(boolean hasGravity) {
          this.pPhysics = hasGravity;
     }

     public boolean getAccelerate() {
          return this.pXlr8;
     }

     public void setAccelerate(boolean accelerate) {
          this.pXlr8 = accelerate;
     }

     public int getExplodeSize() {
          return this.pArea;
     }

     public void setExplodeSize(int size) {
          this.pArea = size;
     }

     public int getEffectType() {
          return this.pEffect;
     }

     public int getEffectTime() {
          return this.pDur;
     }

     public int getEffectStrength() {
          return this.pEffAmp;
     }

     public void setEffect(int type, int strength, int time) {
          this.pEffect = type;
          this.pDur = time;
          this.pEffAmp = strength;
     }

     public boolean getGlows() {
          return this.pGlows;
     }

     public void setGlows(boolean glows) {
          this.pGlows = glows;
     }

     public int getParticle() {
          return this.pTrail;
     }

     public void setParticle(int type) {
          this.pTrail = type;
     }

     public int getAccuracy() {
          return this.accuracy;
     }

     public void setAccuracy(int accuracy) {
          this.accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
     }

     public int getRange() {
          return this.rangedRange;
     }

     public void setRange(int range) {
          this.rangedRange = ValueUtil.CorrectInt(range, 1, 64);
     }

     public int getDelayMin() {
          return this.minDelay;
     }

     public int getDelayMax() {
          return this.maxDelay;
     }

     public int getDelayRNG() {
          int delay = this.minDelay;
          if (this.maxDelay - this.minDelay > 0) {
               delay += this.npc.world.field_73012_v.nextInt(this.maxDelay - this.minDelay);
          }

          return delay;
     }

     public void setDelay(int min, int max) {
          min = Math.min(min, max);
          this.minDelay = min;
          this.maxDelay = max;
     }

     public int getBurst() {
          return this.burstCount;
     }

     public void setBurst(int count) {
          this.burstCount = count;
     }

     public int getBurstDelay() {
          return this.fireRate;
     }

     public void setBurstDelay(int delay) {
          this.fireRate = delay;
     }

     public String getSound(int type) {
          String sound = null;
          if (type == 0) {
               sound = this.fireSound;
          }

          if (type == 1) {
               sound = this.hitSound;
          }

          if (type == 2) {
               sound = this.groundSound;
          }

          return sound != null && !sound.isEmpty() ? sound : null;
     }

     public SoundEvent getSoundEvent(int type) {
          String sound = this.getSound(type);
          if (sound == null) {
               return null;
          } else {
               ResourceLocation res = new ResourceLocation(sound);
               SoundEvent ev = (SoundEvent)SoundEvent.field_187505_a.getObject(res);
               return ev != null ? ev : new SoundEvent(res);
          }
     }

     public void setSound(int type, String sound) {
          if (sound == null) {
               sound = "";
          }

          if (type == 0) {
               this.fireSound = sound;
          }

          if (type == 1) {
               this.hitSound = sound;
          }

          if (type == 2) {
               this.groundSound = sound;
          }

          this.npc.updateClient = true;
     }

     public int getShotCount() {
          return this.shotCount;
     }

     public void setShotCount(int count) {
          this.shotCount = count;
     }

     public boolean getHasAimAnimation() {
          return this.aimWhileShooting;
     }

     public void setHasAimAnimation(boolean aim) {
          this.aimWhileShooting = aim;
     }

     public int getFireType() {
          return this.canFireIndirect;
     }

     public void setFireType(int type) {
          this.canFireIndirect = type;
     }

     public int getMeleeRange() {
          return this.meleeDistance;
     }

     public void setMeleeRange(int range) {
          this.meleeDistance = range;
          this.npc.updateAI = true;
     }
}
