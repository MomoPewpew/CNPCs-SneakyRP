package noppes.npcs.ai;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAILook extends EntityAIBase {
     private final EntityNPCInterface npc;
     private int idle = 0;
     private double lookX;
     private double lookZ;
     boolean rotatebody;
     private boolean forced = false;
     private Entity forcedEntity = null;

     public EntityAILook(EntityNPCInterface npc) {
          this.npc = npc;
          this.func_75248_a(AiMutex.LOOK);
     }

     public boolean func_75250_a() {
          return !this.npc.isAttacking() && this.npc.func_70661_as().func_75500_f() && !this.npc.func_70608_bn() && this.npc.func_70089_S();
     }

     public void func_75249_e() {
          this.rotatebody = this.npc.ais.getStandingType() == 0 || this.npc.ais.getStandingType() == 3;
     }

     public void rotate(Entity entity) {
          this.forced = true;
          this.forcedEntity = entity;
     }

     public void rotate(int degrees) {
          this.forced = true;
          this.npc.field_70759_as = this.npc.field_70177_z = this.npc.field_70761_aq = (float)degrees;
     }

     public void func_75251_c() {
          this.rotatebody = false;
          this.forced = false;
          this.forcedEntity = null;
     }

     public void func_75246_d() {
          Entity lookat = null;
          if (this.forced && this.forcedEntity != null) {
               lookat = this.forcedEntity;
          } else if (this.npc.isInteracting()) {
               Iterator ita = this.npc.interactingEntities.iterator();
               double closestDistance = 12.0D;

               while(ita.hasNext()) {
                    EntityLivingBase entity = (EntityLivingBase)ita.next();
                    double distance = entity.func_70068_e(this.npc);
                    if (distance < closestDistance) {
                         closestDistance = entity.func_70068_e(this.npc);
                         lookat = entity;
                    } else if (distance > 12.0D) {
                         ita.remove();
                    }
               }
          } else if (this.npc.ais.getStandingType() == 2) {
               lookat = this.npc.world.func_72890_a(this.npc, 16.0D);
          }

          if (lookat != null) {
               this.npc.func_70671_ap().func_75651_a((Entity)lookat, 10.0F, (float)this.npc.func_70646_bf());
          } else {
               if (this.rotatebody) {
                    if (this.idle == 0 && this.npc.func_70681_au().nextFloat() < 0.004F) {
                         double var1 = 6.283185307179586D * this.npc.func_70681_au().nextDouble();
                         if (this.npc.ais.getStandingType() == 3) {
                              var1 = 0.017453292519943295D * (double)this.npc.ais.orientation + 0.6283185307179586D + 1.8849555921538759D * this.npc.func_70681_au().nextDouble();
                         }

                         this.lookX = Math.cos(var1);
                         this.lookZ = Math.sin(var1);
                         this.idle = 20 + this.npc.func_70681_au().nextInt(20);
                    }

                    if (this.idle > 0) {
                         --this.idle;
                         this.npc.func_70671_ap().func_75650_a(this.npc.field_70165_t + this.lookX, this.npc.field_70163_u + (double)this.npc.func_70047_e(), this.npc.field_70161_v + this.lookZ, 10.0F, (float)this.npc.func_70646_bf());
                    }
               }

               if (this.npc.ais.getStandingType() == 1 && !this.forced) {
                    this.npc.field_70759_as = this.npc.field_70177_z = this.npc.field_70761_aq = (float)this.npc.ais.orientation;
               }

          }
     }
}
