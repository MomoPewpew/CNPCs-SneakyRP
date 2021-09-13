package noppes.npcs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityChairMount extends Entity {
     public EntityChairMount(World world) {
          super(world);
          this.func_70105_a(0.0F, 0.0F);
     }

     public double func_70042_X() {
          return 0.5D;
     }

     protected void func_70088_a() {
     }

     public void func_70030_z() {
          super.func_70030_z();
          if (this.world != null && !this.world.isRemote && this.func_184188_bt().isEmpty()) {
               this.field_70128_L = true;
          }

     }

     public boolean func_180431_b(DamageSource source) {
          return true;
     }

     public boolean func_82150_aj() {
          return true;
     }

     public void func_70091_d(MoverType type, double x, double y, double z) {
     }

     protected void readEntityFromNBT(NBTTagCompound tagCompound) {
     }

     protected void writeEntityToNBT(NBTTagCompound tagCompound) {
     }

     public boolean func_70067_L() {
          return false;
     }

     public boolean func_70104_M() {
          return false;
     }

     public void func_180430_e(float distance, float damageMultiplier) {
     }

     @SideOnly(Side.CLIENT)
     public void func_180426_a(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_, boolean bo) {
          this.func_70107_b(p_70056_1_, p_70056_3_, p_70056_5_);
          this.func_70101_b(p_70056_7_, p_70056_8_);
     }
}
