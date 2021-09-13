package noppes.npcs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class Resistances {
     public float knockback = 1.0F;
     public float arrow = 1.0F;
     public float melee = 1.0F;
     public float explosion = 1.0F;

     public NBTTagCompound writeToNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setFloat("Knockback", this.knockback);
          compound.setFloat("Arrow", this.arrow);
          compound.setFloat("Melee", this.melee);
          compound.setFloat("Explosion", this.explosion);
          return compound;
     }

     public void readToNBT(NBTTagCompound compound) {
          this.knockback = compound.getFloat("Knockback");
          this.arrow = compound.getFloat("Arrow");
          this.melee = compound.getFloat("Melee");
          this.explosion = compound.getFloat("Explosion");
     }

     public float applyResistance(DamageSource source, float damage) {
          if (!source.field_76373_n.equals("arrow") && !source.field_76373_n.equals("thrown") && !source.func_76352_a()) {
               if (!source.field_76373_n.equals("player") && !source.field_76373_n.equals("mob")) {
                    if (source.field_76373_n.equals("explosion") || source.field_76373_n.equals("explosion.player")) {
                         damage *= 2.0F - this.explosion;
                    }
               } else {
                    damage *= 2.0F - this.melee;
               }
          } else {
               damage *= 2.0F - this.arrow;
          }

          return damage;
     }
}
