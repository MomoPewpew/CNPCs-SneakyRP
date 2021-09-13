package noppes.npcs.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWatchClosest extends EntityAIBase {
     private EntityNPCInterface npc;
     protected Entity closestEntity;
     private float field_75333_c;
     private int lookTime;
     private float field_75331_e;
     private Class watchedClass;

     public EntityAIWatchClosest(EntityNPCInterface par1EntityLiving, Class par2Class, float par3) {
          this.npc = par1EntityLiving;
          this.watchedClass = par2Class;
          this.field_75333_c = par3;
          this.field_75331_e = 0.002F;
          this.func_75248_a(AiMutex.LOOK);
     }

     public boolean func_75250_a() {
          if (this.npc.func_70681_au().nextFloat() < this.field_75331_e && !this.npc.isInteracting()) {
               if (this.npc.func_70638_az() != null) {
                    this.closestEntity = this.npc.func_70638_az();
               }

               if (this.watchedClass == EntityPlayer.class) {
                    this.closestEntity = this.npc.world.func_72890_a(this.npc, (double)this.field_75333_c);
               } else {
                    this.closestEntity = this.npc.world.func_72857_a(this.watchedClass, this.npc.func_174813_aQ().func_72314_b((double)this.field_75333_c, 3.0D, (double)this.field_75333_c), this.npc);
                    if (this.closestEntity != null) {
                         return this.npc.canSee(this.closestEntity);
                    }
               }

               return this.closestEntity != null;
          } else {
               return false;
          }
     }

     public boolean func_75253_b() {
          if (!this.npc.isInteracting() && !this.npc.isAttacking() && this.closestEntity.func_70089_S() && this.npc.func_70089_S()) {
               return !this.npc.isInRange(this.closestEntity, (double)this.field_75333_c) ? false : this.lookTime > 0;
          } else {
               return false;
          }
     }

     public void func_75249_e() {
          this.lookTime = 60 + this.npc.func_70681_au().nextInt(60);
     }

     public void func_75251_c() {
          this.closestEntity = null;
     }

     public void func_75246_d() {
          this.npc.func_70671_ap().func_75650_a(this.closestEntity.field_70165_t, this.closestEntity.field_70163_u + (double)this.closestEntity.func_70047_e(), this.closestEntity.field_70161_v, 10.0F, (float)this.npc.func_70646_bf());
          --this.lookTime;
     }
}
