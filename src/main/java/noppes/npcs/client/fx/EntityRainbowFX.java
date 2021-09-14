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
          this.motionX *= 0.10000000149011612D;
          this.motionY *= 0.10000000149011612D;
          this.motionZ *= 0.10000000149011612D;
          if (f1 == 0.0D) {
               f1 = 1.0D;
          }

          int i = world.rand.nextInt(colorTable.length);
          this.particleRed = colorTable[i][0];
          this.particleGreen = colorTable[i][1];
          this.particleBlue = colorTable[i][2];
          this.particleScale *= 0.75F;
          this.particleScale *= f;
          this.reddustParticleScale = this.particleScale;
          this.particleMaxAge = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
          this.particleMaxAge = (int)((float)this.particleMaxAge * f);
     }

     public void renderParticle(BufferBuilder tessellator, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
          float f6 = ((float)this.particleAge + f) / (float)this.particleMaxAge * 32.0F;
          if (f6 < 0.0F) {
               f6 = 0.0F;
          } else if (f6 > 1.0F) {
               f6 = 1.0F;
          }

          this.particleScale = this.reddustParticleScale * f6;
          super.renderParticle(tessellator, entity, f, f1, f2, f3, f4, f5);
     }

     public void onUpdate() {
          this.prevPosX = this.posX;
          this.prevPosY = this.posY;
          this.prevPosZ = this.posZ;
          if (this.particleAge++ >= this.particleMaxAge) {
               this.setExpired();
          }

          this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
          this.move(this.motionX, this.motionY, this.motionZ);
          if (this.posY == this.prevPosY) {
               this.motionX *= 1.1D;
               this.motionZ *= 1.1D;
          }

          this.motionX *= 0.9599999785423279D;
          this.motionY *= 0.9599999785423279D;
          this.motionZ *= 0.9599999785423279D;
          if (this.onGround) {
               this.motionX *= 0.699999988079071D;
               this.motionZ *= 0.699999988079071D;
          }

     }
}
