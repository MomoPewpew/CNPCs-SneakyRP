package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIReturn extends EntityAIBase {
     public static final int MaxTotalTicks = 600;
     private final EntityNPCInterface npc;
     private int stuckTicks = 0;
     private int totalTicks = 0;
     private double endPosX;
     private double endPosY;
     private double endPosZ;
     private boolean wasAttacked = false;
     private double[] preAttackPos;
     private int stuckCount = 0;

     public EntityAIReturn(EntityNPCInterface npc) {
          this.npc = npc;
          this.func_75248_a(AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          if (!this.npc.hasOwner() && !this.npc.func_184218_aH() && this.npc.ais.shouldReturnHome() && !this.npc.isKilled() && this.npc.func_70661_as().func_75500_f() && !this.npc.isInteracting()) {
               BlockPos pos;
               if (this.npc.ais.findShelter == 0 && (!this.npc.world.func_72935_r() || this.npc.world.func_72896_J()) && !this.npc.world.field_73011_w.func_191066_m()) {
                    pos = new BlockPos((double)this.npc.getStartXPos(), this.npc.getStartYPos(), (double)this.npc.getStartZPos());
                    if (this.npc.world.func_175678_i(pos) || this.npc.world.getLight(pos) <= 8) {
                         return false;
                    }
               } else if (this.npc.ais.findShelter == 1 && this.npc.world.func_72935_r()) {
                    pos = new BlockPos((double)this.npc.getStartXPos(), this.npc.getStartYPos(), (double)this.npc.getStartZPos());
                    if (this.npc.world.func_175678_i(pos)) {
                         return false;
                    }
               }

               if (this.npc.isAttacking()) {
                    if (!this.wasAttacked) {
                         this.wasAttacked = true;
                         this.preAttackPos = new double[]{this.npc.field_70165_t, this.npc.field_70163_u, this.npc.field_70161_v};
                    }

                    return false;
               } else if (!this.npc.isAttacking() && this.wasAttacked) {
                    return true;
               } else if (this.npc.ais.getMovingType() == 2 && this.npc.ais.getDistanceSqToPathPoint() < (double)(CustomNpcs.NpcNavRange * CustomNpcs.NpcNavRange)) {
                    return false;
               } else if (this.npc.ais.getMovingType() == 1) {
                    double x = this.npc.field_70165_t - (double)this.npc.getStartXPos();
                    double z = this.npc.field_70165_t - (double)this.npc.getStartZPos();
                    return !this.npc.isInRange((double)this.npc.getStartXPos(), -1.0D, (double)this.npc.getStartZPos(), (double)this.npc.ais.walkingRange);
               } else if (this.npc.ais.getMovingType() == 0) {
                    return !this.npc.isVeryNearAssignedPlace();
               } else {
                    return false;
               }
          } else {
               return false;
          }
     }

     public boolean func_75253_b() {
          if (!this.npc.isFollower() && !this.npc.isKilled() && !this.npc.isAttacking() && !this.npc.isVeryNearAssignedPlace() && !this.npc.isInteracting() && !this.npc.func_184218_aH()) {
               if (this.npc.func_70661_as().func_75500_f() && this.wasAttacked && !this.isTooFar()) {
                    return false;
               } else {
                    return this.totalTicks <= 600;
               }
          } else {
               return false;
          }
     }

     public void func_75246_d() {
          ++this.totalTicks;
          if (this.totalTicks > 600) {
               this.npc.func_70107_b(this.endPosX, this.endPosY, this.endPosZ);
               this.npc.func_70661_as().func_75499_g();
          } else {
               if (this.stuckTicks > 0) {
                    --this.stuckTicks;
               } else if (this.npc.func_70661_as().func_75500_f()) {
                    ++this.stuckCount;
                    this.stuckTicks = 10;
                    if ((this.totalTicks <= 30 || !this.wasAttacked || !this.isTooFar()) && this.stuckCount <= 5) {
                         this.navigate(this.stuckCount % 2 == 1);
                    } else {
                         this.npc.func_70107_b(this.endPosX, this.endPosY, this.endPosZ);
                         this.npc.func_70661_as().func_75499_g();
                    }
               } else {
                    this.stuckCount = 0;
               }

          }
     }

     private boolean isTooFar() {
          int allowedDistance = this.npc.stats.aggroRange * 2;
          if (this.npc.ais.getMovingType() == 1) {
               allowedDistance += this.npc.ais.walkingRange;
          }

          double x = this.npc.field_70165_t - this.endPosX;
          double z = this.npc.field_70165_t - this.endPosZ;
          return x * x + z * z > (double)(allowedDistance * allowedDistance);
     }

     public void func_75249_e() {
          this.stuckTicks = 0;
          this.totalTicks = 0;
          this.stuckCount = 0;
          this.navigate(false);
     }

     private void navigate(boolean towards) {
          if (!this.wasAttacked) {
               this.endPosX = (double)this.npc.getStartXPos();
               this.endPosY = this.npc.getStartYPos();
               this.endPosZ = (double)this.npc.getStartZPos();
          } else {
               this.endPosX = this.preAttackPos[0];
               this.endPosY = this.preAttackPos[1];
               this.endPosZ = this.preAttackPos[2];
          }

          double posX = this.endPosX;
          double posY = this.endPosY;
          double posZ = this.endPosZ;
          double range = this.npc.func_70011_f(posX, posY, posZ);
          if (range > (double)CustomNpcs.NpcNavRange || towards) {
               int distance = (int)range;
               if (distance > CustomNpcs.NpcNavRange) {
                    distance = CustomNpcs.NpcNavRange / 2;
               } else {
                    distance /= 2;
               }

               if (distance > 2) {
                    Vec3d start = new Vec3d(posX, posY, posZ);
                    Vec3d pos = RandomPositionGenerator.func_75464_a(this.npc, distance, distance / 2 > 7 ? 7 : distance / 2, start);
                    if (pos != null) {
                         posX = pos.field_72450_a;
                         posY = pos.field_72448_b;
                         posZ = pos.field_72449_c;
                    }
               }
          }

          this.npc.func_70661_as().func_75499_g();
          this.npc.func_70661_as().func_75492_a(posX, posY, posZ, 1.0D);
     }

     public void func_75251_c() {
          this.wasAttacked = false;
          this.npc.func_70661_as().func_75499_g();
     }
}
