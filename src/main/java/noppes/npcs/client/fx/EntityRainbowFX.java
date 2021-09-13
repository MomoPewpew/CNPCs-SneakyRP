package noppes.npcs.client.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityRainbowFX extends Particle {
     public static float[][] colorTable = new float[][]{{1.0F, 0.0F, 0.0F}, {1.0F, 0.5F, 0.0F}, {1.0F, 1.0F, 0.0F}, {0.0F, 1.0F, 0.0F}, {0.0F, 0.0F, 1.0F}, {0.0F, 4375.0F, 0.0F, 1.0F}, {0.5625F, 0.0F, 1.0F}};
     float reddustParticleScale;

     public EntityRainbowFX(World world, double d, double d1, double d2, double f, double f1, double f2) {
          this(world, d, d1, d2, 1.0F, f, f1, f2);
     }

     public EntityRainbowFX(World world, double d, double d1, double d2, float f, double f1, double f2, double f3) {
          super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
          this.field_187129_i *= 0.10000000149011612D;
          this.field_187130_j *= 0.10000000149011612D;
          this.field_187131_k *= 0.10000000149011612D;
          if (f1 == 0.0D) {
               f1 = 1.0D;
          }

          int i = world.field_73012_v.nextInt(colorTable.length);
          this.field_70552_h = colorTable[i][0];
          this.field_70553_i = colorTable[i][1];
          this.field_70551_j = colorTable[i][2];
          this.field_70544_f *= 0.75F;
          this.field_70544_f *= f;
          this.reddustParticleScale = this.field_70544_f;
          this.field_70547_e = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
          this.field_70547_e = (int)((float)this.field_70547_e * f);
     }

     public void func_180434_a(BufferBuilder tessellator, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          float f6 = ((float)this.field_70546_d + f) / (float)this.field_70547_e * 32.0F;
          if (f6 < 0.0F) {
               f6 = 0.0F;
          } else if (f6 > 1.0F) {
               f6 = 1.0F;
          }

          this.field_70544_f = this.reddustParticleScale * f6;
          super.func_180434_a(tessellator, entity, f, f1, f2, f3, f4, f5);
     }

     public void func_189213_a() {
          this.field_187123_c = this.field_187126_f;
          this.field_187124_d = this.field_187127_g;
          this.field_187125_e = this.field_187128_h;
          if (this.field_70546_d++ >= this.field_70547_e) {
               this.func_187112_i();
          }

          this.func_70536_a(7 - this.field_70546_d * 8 / this.field_70547_e);
          this.func_187110_a(this.field_187129_i, this.field_187130_j, this.field_187131_k);
          if (this.field_187127_g == this.field_187124_d) {
               this.field_187129_i *= 1.1D;
               this.field_187131_k *= 1.1D;
          }

          this.field_187129_i *= 0.9599999785423279D;
          this.field_187130_j *= 0.9599999785423279D;
          this.field_187131_k *= 0.9599999785423279D;
          if (this.field_187132_l) {
               this.field_187129_i *= 0.699999988079071D;
               this.field_187131_k *= 0.699999988079071D;
          }

     }
}
