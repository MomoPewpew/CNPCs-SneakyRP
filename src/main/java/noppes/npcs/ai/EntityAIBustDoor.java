package noppes.npcs.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class EntityAIBustDoor extends EntityAIDoorInteract {
     private int breakingTime;
     private int field_75358_j = -1;

     public EntityAIBustDoor(EntityLiving par1EntityLiving) {
          super(par1EntityLiving);
     }

     public boolean func_75250_a() {
          return !super.func_75250_a() ? false : !BlockDoor.func_176514_f(this.field_75356_a.field_70170_p, this.field_179507_b);
     }

     public void func_75249_e() {
          super.func_75249_e();
          this.breakingTime = 0;
     }

     public boolean func_75253_b() {
          double var1 = this.field_75356_a.func_174818_b(this.field_179507_b);
          return this.breakingTime <= 240 && !BlockDoor.func_176514_f(this.field_75356_a.field_70170_p, this.field_179507_b) && var1 < 4.0D;
     }

     public void func_75251_c() {
          super.func_75251_c();
          this.field_75356_a.field_70170_p.func_175715_c(this.field_75356_a.func_145782_y(), this.field_179507_b, -1);
     }

     public void func_75246_d() {
          super.func_75246_d();
          if (this.field_75356_a.func_70681_au().nextInt(20) == 0) {
               this.field_75356_a.field_70170_p.func_180498_a((EntityPlayer)null, 1010, this.field_179507_b, 0);
               this.field_75356_a.func_184609_a(EnumHand.MAIN_HAND);
          }

          ++this.breakingTime;
          int var1 = (int)((float)this.breakingTime / 240.0F * 10.0F);
          if (var1 != this.field_75358_j) {
               this.field_75356_a.field_70170_p.func_175715_c(this.field_75356_a.func_145782_y(), this.field_179507_b, var1);
               this.field_75358_j = var1;
          }

          if (this.breakingTime == 240) {
               this.field_75356_a.field_70170_p.func_175698_g(this.field_179507_b);
               this.field_75356_a.field_70170_p.func_180498_a((EntityPlayer)null, 1012, this.field_179507_b, 0);
               this.field_75356_a.field_70170_p.func_180498_a((EntityPlayer)null, 2001, this.field_179507_b, Block.func_149682_b(this.field_151504_e));
          }

     }
}
