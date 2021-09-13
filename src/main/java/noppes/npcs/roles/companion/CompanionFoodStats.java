package noppes.npcs.roles.companion;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.entity.EntityNPCInterface;

public class CompanionFoodStats {
     private int foodLevel = 20;
     private float foodSaturationLevel = 5.0F;
     private float foodExhaustionLevel;
     private int foodTimer;
     private int prevFoodLevel = 20;

     private void addStats(int p_75122_1_, float p_75122_2_) {
          this.foodLevel = Math.min(p_75122_1_ + this.foodLevel, 20);
          this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float)p_75122_1_ * p_75122_2_ * 2.0F, (float)this.foodLevel);
     }

     public void onFoodEaten(ItemFood food, ItemStack itemstack) {
          this.addStats(food.func_150905_g(itemstack), food.func_150906_h(itemstack));
     }

     public void onUpdate(EntityNPCInterface npc) {
          EnumDifficulty enumdifficulty = npc.world.func_175659_aa();
          this.prevFoodLevel = this.foodLevel;
          if (this.foodExhaustionLevel > 4.0F) {
               this.foodExhaustionLevel -= 4.0F;
               if (this.foodSaturationLevel > 0.0F) {
                    this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
               } else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
                    this.foodLevel = Math.max(this.foodLevel - 1, 0);
               }
          }

          if (npc.world.func_82736_K().func_82766_b("naturalRegeneration") && this.foodLevel >= 18 && npc.func_110143_aJ() > 0.0F && npc.func_110143_aJ() < npc.func_110138_aP()) {
               ++this.foodTimer;
               if (this.foodTimer >= 80) {
                    npc.func_70691_i(1.0F);
                    this.addExhaustion(3.0F);
                    this.foodTimer = 0;
               }
          } else if (this.foodLevel <= 0) {
               ++this.foodTimer;
               if (this.foodTimer >= 80) {
                    if (npc.func_110143_aJ() > 10.0F || enumdifficulty == EnumDifficulty.HARD || npc.func_110143_aJ() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL) {
                         npc.func_70097_a(DamageSource.field_76366_f, 1.0F);
                    }

                    this.foodTimer = 0;
               }
          } else {
               this.foodTimer = 0;
          }

     }

     public void readNBT(NBTTagCompound p_75112_1_) {
          if (p_75112_1_.func_150297_b("foodLevel", 99)) {
               this.foodLevel = p_75112_1_.func_74762_e("foodLevel");
               this.foodTimer = p_75112_1_.func_74762_e("foodTickTimer");
               this.foodSaturationLevel = p_75112_1_.func_74760_g("foodSaturationLevel");
               this.foodExhaustionLevel = p_75112_1_.func_74760_g("foodExhaustionLevel");
          }

     }

     public void writeNBT(NBTTagCompound p_75117_1_) {
          p_75117_1_.setInteger("foodLevel", this.foodLevel);
          p_75117_1_.setInteger("foodTickTimer", this.foodTimer);
          p_75117_1_.func_74776_a("foodSaturationLevel", this.foodSaturationLevel);
          p_75117_1_.func_74776_a("foodExhaustionLevel", this.foodExhaustionLevel);
     }

     public int getFoodLevel() {
          return this.foodLevel;
     }

     @SideOnly(Side.CLIENT)
     public int getPrevFoodLevel() {
          return this.prevFoodLevel;
     }

     public boolean needFood() {
          return this.foodLevel < 20;
     }

     public void addExhaustion(float p_75113_1_) {
          this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + p_75113_1_, 40.0F);
     }

     public float getSaturationLevel() {
          return this.foodSaturationLevel;
     }

     @SideOnly(Side.CLIENT)
     public void setFoodLevel(int p_75114_1_) {
          this.foodLevel = p_75114_1_;
     }

     @SideOnly(Side.CLIENT)
     public void setFoodSaturationLevel(float p_75119_1_) {
          this.foodSaturationLevel = p_75119_1_;
     }
}
