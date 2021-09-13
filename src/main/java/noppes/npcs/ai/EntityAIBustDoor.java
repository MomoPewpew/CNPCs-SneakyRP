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

     public boolean shouldExecute() {
          return !super.shouldExecute() ? false : !BlockDoor.isOpen(this.field_75356_a.world, this.field_179507_b);
     }

     public void startExecuting() {
          super.startExecuting();
          this.breakingTime = 0;
     }

     public boolean shouldContinueExecuting() {
          double var1 = this.field_75356_a.getDistanceSq(this.field_179507_b);
          return this.breakingTime <= 240 && !BlockDoor.isOpen(this.field_75356_a.world, this.field_179507_b) && var1 < 4.0D;
     }

     public void resetTask() {
          super.resetTask();
          this.field_75356_a.world.sendBlockBreakProgress(this.field_75356_a.getEntityId(), this.field_179507_b, -1);
     }

     public void updateTask() {
          super.updateTask();
          if (this.field_75356_a.getRNG().nextInt(20) == 0) {
               this.field_75356_a.world.playEvent((EntityPlayer)null, 1010, this.field_179507_b, 0);
               this.field_75356_a.swingArm(EnumHand.MAIN_HAND);
          }

          ++this.breakingTime;
          int var1 = (int)((float)this.breakingTime / 240.0F * 10.0F);
          if (var1 != this.field_75358_j) {
               this.field_75356_a.world.sendBlockBreakProgress(this.field_75356_a.getEntityId(), this.field_179507_b, var1);
               this.field_75358_j = var1;
          }

          if (this.breakingTime == 240) {
               this.field_75356_a.world.setBlockToAir(this.field_179507_b);
               this.field_75356_a.world.playEvent((EntityPlayer)null, 1012, this.field_179507_b, 0);
               this.field_75356_a.world.playEvent((EntityPlayer)null, 2001, this.field_179507_b, Block.getIdFromBlock(this.field_151504_e));
          }

     }
}
