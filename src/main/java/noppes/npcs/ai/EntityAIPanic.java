package noppes.npcs.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.constants.AiMutex;

public class EntityAIPanic extends EntityAIBase {
     private EntityCreature entityCreature;
     private float speed;
     private double randPosX;
     private double randPosY;
     private double randPosZ;

     public EntityAIPanic(EntityCreature par1EntityCreature, float par2) {
          this.entityCreature = par1EntityCreature;
          this.speed = par2;
          this.func_75248_a(AiMutex.PASSIVE);
     }

     public boolean func_75250_a() {
          if (this.entityCreature.func_70638_az() == null && !this.entityCreature.func_70027_ad()) {
               return false;
          } else {
               Vec3d var1 = RandomPositionGenerator.func_75463_a(this.entityCreature, 5, 4);
               if (var1 == null) {
                    return false;
               } else {
                    this.randPosX = var1.field_72450_a;
                    this.randPosY = var1.field_72448_b;
                    this.randPosZ = var1.field_72449_c;
                    return true;
               }
          }
     }

     public void func_75249_e() {
          this.entityCreature.func_70661_as().func_75492_a(this.randPosX, this.randPosY, this.randPosZ, (double)this.speed);
     }

     public boolean func_75253_b() {
          if (this.entityCreature.func_70638_az() == null) {
               return false;
          } else {
               return !this.entityCreature.func_70661_as().func_75500_f();
          }
     }
}
