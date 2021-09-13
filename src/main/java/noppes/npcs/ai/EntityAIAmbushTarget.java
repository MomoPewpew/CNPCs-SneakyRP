package noppes.npcs.ai;

import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIAmbushTarget extends EntityAIBase {
     private EntityNPCInterface npc;
     private EntityLivingBase targetEntity;
     private double shelterX;
     private double shelterY;
     private double shelterZ;
     private double movementSpeed;
     private double distance;
     private int delay = 0;
     private World world;
     private int tick;

     public EntityAIAmbushTarget(EntityNPCInterface par1EntityCreature, double par2) {
          this.npc = par1EntityCreature;
          this.movementSpeed = par2;
          this.world = par1EntityCreature.world;
          this.func_75248_a(AiMutex.PASSIVE + AiMutex.LOOK);
     }

     public boolean func_75250_a() {
          this.targetEntity = this.npc.func_70638_az();
          this.distance = (double)this.npc.ais.getTacticalRange();
          if (this.targetEntity != null && !this.npc.isInRange(this.targetEntity, this.distance) && this.npc.canSee(this.targetEntity) && this.delay-- <= 0) {
               Vec3d vec3 = this.findHidingSpot();
               if (vec3 == null) {
                    this.delay = 10;
                    return false;
               } else {
                    this.shelterX = vec3.field_72450_a;
                    this.shelterY = vec3.field_72448_b;
                    this.shelterZ = vec3.field_72449_c;
                    return true;
               }
          } else {
               return false;
          }
     }

     public boolean func_75253_b() {
          boolean shouldHide = !this.npc.isInRange(this.targetEntity, this.distance);
          boolean isSeen = this.npc.canSee(this.targetEntity);
          return !this.npc.func_70661_as().func_75500_f() && shouldHide || !isSeen && (shouldHide || this.npc.ais.directLOS);
     }

     public void func_75249_e() {
          this.npc.func_70661_as().func_75492_a(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
     }

     public void func_75251_c() {
          this.npc.func_70661_as().func_75499_g();
          if (this.npc.func_70638_az() == null && this.targetEntity != null) {
               this.npc.func_70624_b(this.targetEntity);
          }

          if (!this.npc.isInRange(this.targetEntity, this.distance)) {
               this.delay = 60;
          }

     }

     public void func_75246_d() {
          this.npc.func_70671_ap().func_75651_a(this.targetEntity, 30.0F, 30.0F);
     }

     private Vec3d findHidingSpot() {
          Random random = this.npc.func_70681_au();
          Vec3d idealPos = null;

          for(int i = 1; i <= 8; ++i) {
               for(int y = -2; y <= 2; ++y) {
                    double k = (double)MathHelper.func_76128_c(this.npc.getEntityBoundingBox().field_72338_b + (double)y);

                    for(int x = -i; x <= i; ++x) {
                         double j = (double)MathHelper.func_76128_c(this.npc.field_70165_t + (double)x) + 0.5D;

                         for(int z = -i; z <= i; ++z) {
                              double l = (double)MathHelper.func_76128_c(this.npc.field_70161_v + (double)z) + 0.5D;
                              if (this.isOpaque((int)j, (int)k, (int)l) && !this.isOpaque((int)j, (int)k + 1, (int)l) && this.isOpaque((int)j, (int)k + 2, (int)l)) {
                                   Vec3d vec1 = new Vec3d(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u + (double)this.targetEntity.func_70047_e(), this.targetEntity.field_70161_v);
                                   Vec3d vec2 = new Vec3d(j, k + (double)this.npc.func_70047_e(), l);
                                   RayTraceResult movingobjectposition = this.world.func_72933_a(vec1, vec2);
                                   if (movingobjectposition != null && this.shelterX != j && this.shelterY != k && this.shelterZ != l) {
                                        idealPos = new Vec3d(j, k, l);
                                   }
                              }
                         }
                    }
               }

               if (idealPos != null) {
                    return idealPos;
               }
          }

          this.delay = 60;
          return null;
     }

     private boolean isOpaque(int x, int y, int z) {
          return this.world.getBlockState(new BlockPos(x, y, z)).func_185914_p();
     }
}
