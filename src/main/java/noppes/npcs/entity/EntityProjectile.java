package noppes.npcs.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.event.ProjectileEvent;
import noppes.npcs.entity.data.DataRanged;

public class EntityProjectile extends EntityThrowable {
     private static final DataParameter Gravity;
     private static final DataParameter Arrow;
     private static final DataParameter Is3d;
     private static final DataParameter Glows;
     private static final DataParameter Rotating;
     private static final DataParameter Sticks;
     private static final DataParameter ItemStackThrown;
     private static final DataParameter Velocity;
     private static final DataParameter Size;
     private static final DataParameter Particle;
     private BlockPos tilePos;
     private Block inTile;
     protected boolean field_174854_a;
     private int inData;
     public int field_70191_b;
     public int arrowShake;
     public boolean canBePickedUp;
     public boolean destroyedOnEntityHit;
     private EntityLivingBase thrower;
     private EntityNPCInterface npc;
     private String throwerName;
     private int ticksInGround;
     public int field_70195_i;
     private double accelerationX;
     private double accelerationY;
     private double accelerationZ;
     public float damage;
     public int punch;
     public boolean accelerate;
     public boolean explosiveDamage;
     public int explosiveRadius;
     public int effect;
     public int duration;
     public int amplify;
     public int accuracy;
     public EntityProjectile.IProjectileCallback callback;
     public List scripts;

     public EntityProjectile(World par1World) {
          super(par1World);
          this.tilePos = BlockPos.field_177992_a;
          this.field_174854_a = false;
          this.inData = 0;
          this.field_70191_b = 0;
          this.arrowShake = 0;
          this.canBePickedUp = false;
          this.destroyedOnEntityHit = true;
          this.throwerName = null;
          this.field_70195_i = 0;
          this.damage = 5.0F;
          this.punch = 0;
          this.accelerate = false;
          this.explosiveDamage = true;
          this.explosiveRadius = 0;
          this.effect = 0;
          this.duration = 5;
          this.amplify = 0;
          this.accuracy = 60;
          this.scripts = new ArrayList();
          this.func_70105_a(0.25F, 0.25F);
     }

     protected void func_70088_a() {
          this.field_70180_af.func_187214_a(ItemStackThrown, ItemStack.field_190927_a);
          this.field_70180_af.func_187214_a(Velocity, 10);
          this.field_70180_af.func_187214_a(Size, 10);
          this.field_70180_af.func_187214_a(Particle, 0);
          this.field_70180_af.func_187214_a(Gravity, false);
          this.field_70180_af.func_187214_a(Glows, false);
          this.field_70180_af.func_187214_a(Arrow, false);
          this.field_70180_af.func_187214_a(Is3d, false);
          this.field_70180_af.func_187214_a(Rotating, false);
          this.field_70180_af.func_187214_a(Sticks, false);
     }

     @SideOnly(Side.CLIENT)
     public boolean func_70112_a(double par1) {
          double d1 = this.func_174813_aQ().func_72320_b() * 4.0D;
          d1 *= 64.0D;
          return par1 < d1 * d1;
     }

     public EntityProjectile(World par1World, EntityLivingBase par2EntityLiving, ItemStack item, boolean isNPC) {
          super(par1World);
          this.tilePos = BlockPos.field_177992_a;
          this.field_174854_a = false;
          this.inData = 0;
          this.field_70191_b = 0;
          this.arrowShake = 0;
          this.canBePickedUp = false;
          this.destroyedOnEntityHit = true;
          this.throwerName = null;
          this.field_70195_i = 0;
          this.damage = 5.0F;
          this.punch = 0;
          this.accelerate = false;
          this.explosiveDamage = true;
          this.explosiveRadius = 0;
          this.effect = 0;
          this.duration = 5;
          this.amplify = 0;
          this.accuracy = 60;
          this.scripts = new ArrayList();
          this.thrower = par2EntityLiving;
          if (this.thrower != null) {
               this.throwerName = this.thrower.func_110124_au().toString();
          }

          this.setThrownItem(item);
          this.field_70180_af.func_187227_b(Arrow, this.getItem() == Items.field_151032_g);
          this.func_70105_a((float)this.getSize() / 10.0F, (float)this.getSize() / 10.0F);
          this.func_70012_b(par2EntityLiving.field_70165_t, par2EntityLiving.field_70163_u + (double)par2EntityLiving.func_70047_e(), par2EntityLiving.field_70161_v, par2EntityLiving.field_70177_z, par2EntityLiving.field_70125_A);
          this.field_70165_t -= (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * 0.1F);
          this.field_70163_u -= 0.10000000149011612D;
          this.field_70161_v -= (double)(MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * 0.1F);
          this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
          if (isNPC) {
               this.npc = (EntityNPCInterface)this.thrower;
               this.getStatProperties(this.npc.stats.ranged);
          }

     }

     public void setThrownItem(ItemStack item) {
          this.field_70180_af.func_187227_b(ItemStackThrown, item);
     }

     public int getSize() {
          return (Integer)this.field_70180_af.func_187225_a(Size);
     }

     public void func_70186_c(double par1, double par3, double par5, float par7, float par8) {
          float f2 = MathHelper.func_76133_a(par1 * par1 + par3 * par3 + par5 * par5);
          float f3 = MathHelper.func_76133_a(par1 * par1 + par5 * par5);
          float yaw = (float)(Math.atan2(par1, par5) * 180.0D / 3.141592653589793D);
          float pitch = this.hasGravity() ? par7 : (float)(Math.atan2(par3, (double)f3) * 180.0D / 3.141592653589793D);
          this.field_70126_B = this.field_70177_z = yaw;
          this.field_70127_C = this.field_70125_A = pitch;
          this.field_70159_w = (double)(MathHelper.func_76126_a(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
          this.field_70179_y = (double)(MathHelper.func_76134_b(yaw / 180.0F * 3.1415927F) * MathHelper.func_76134_b(pitch / 180.0F * 3.1415927F));
          this.field_70181_x = (double)MathHelper.func_76126_a((pitch + 1.0F) / 180.0F * 3.1415927F);
          this.field_70159_w += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double)par8;
          this.field_70179_y += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double)par8;
          this.field_70181_x += this.field_70146_Z.nextGaussian() * 0.007499999832361937D * (double)par8;
          this.field_70159_w *= (double)this.getSpeed();
          this.field_70179_y *= (double)this.getSpeed();
          this.field_70181_x *= (double)this.getSpeed();
          this.accelerationX = par1 / (double)f2 * 0.1D;
          this.accelerationY = par3 / (double)f2 * 0.1D;
          this.accelerationZ = par5 / (double)f2 * 0.1D;
          this.ticksInGround = 0;
     }

     public float getAngleForXYZ(double varX, double varY, double varZ, double horiDist, boolean arc) {
          float g = this.func_70185_h();
          float var1 = this.getSpeed() * this.getSpeed();
          double var2 = (double)g * horiDist;
          double var3 = (double)g * horiDist * horiDist + 2.0D * varY * (double)var1;
          double var4 = (double)(var1 * var1) - (double)g * var3;
          if (var4 < 0.0D) {
               return 30.0F;
          } else {
               float var6 = arc ? var1 + MathHelper.func_76133_a(var4) : var1 - MathHelper.func_76133_a(var4);
               float var7 = (float)(Math.atan2((double)var6, var2) * 180.0D / 3.141592653589793D);
               return var7;
          }
     }

     public void shoot(float speed) {
          double varX = (double)(-MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.field_70125_A / 180.0F * 3.1415927F));
          double varZ = (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.field_70125_A / 180.0F * 3.1415927F));
          double varY = (double)(-MathHelper.func_76126_a(this.field_70125_A / 180.0F * 3.1415927F));
          this.func_70186_c(varX, varY, varZ, -this.field_70125_A, speed);
     }

     @SideOnly(Side.CLIENT)
     public void func_180426_a(double par1, double par3, double par5, float par7, float par8, int par9, boolean bo) {
          if (!this.world.field_72995_K || !this.field_174854_a) {
               this.func_70107_b(par1, par3, par5);
               this.func_70101_b(par7, par8);
          }
     }

     public void func_70071_h_() {
          super.func_70030_z();
          if (++this.field_70173_aa % 10 == 0) {
               EventHooks.onProjectileTick(this);
          }

          if (this.effect == 1 && !this.field_174854_a) {
               this.func_70015_d(1);
          }

          IBlockState state = this.world.func_180495_p(this.tilePos);
          Block block = state.func_177230_c();
          if ((this.isArrow() || this.sticksToWalls()) && this.tilePos != BlockPos.field_177992_a) {
               AxisAlignedBB axisalignedbb = state.func_185890_d(this.world, this.tilePos);
               if (axisalignedbb != null && axisalignedbb.func_72318_a(new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v))) {
                    this.field_174854_a = true;
               }
          }

          if (this.arrowShake > 0) {
               --this.arrowShake;
          }

          if (this.field_174854_a) {
               int j = block.func_176201_c(state);
               if (block == this.inTile && j == this.inData) {
                    ++this.ticksInGround;
                    if (this.ticksInGround == 1200) {
                         this.func_70106_y();
                    }
               } else {
                    this.field_174854_a = false;
                    this.field_70159_w *= (double)(this.field_70146_Z.nextFloat() * 0.2F);
                    this.field_70181_x *= (double)(this.field_70146_Z.nextFloat() * 0.2F);
                    this.field_70179_y *= (double)(this.field_70146_Z.nextFloat() * 0.2F);
                    this.ticksInGround = 0;
                    this.field_70195_i = 0;
               }
          } else {
               ++this.field_70195_i;
               if (this.field_70195_i == 1200) {
                    this.func_70106_y();
               }

               Vec3d vec3 = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               Vec3d vec31 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
               RayTraceResult movingobjectposition = this.world.func_147447_a(vec3, vec31, false, true, false);
               vec3 = new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               vec31 = new Vec3d(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
               if (movingobjectposition != null) {
                    vec31 = new Vec3d(movingobjectposition.field_72307_f.field_72450_a, movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);
               }

               if (!this.world.field_72995_K) {
                    Entity entity = null;
                    List list = this.world.func_72839_b(this, this.func_174813_aQ().func_72314_b(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_72314_b(1.0D, 1.0D, 1.0D));
                    double d0 = 0.0D;
                    EntityLivingBase entityliving = this.func_85052_h();
                    int k = 0;

                    while(true) {
                         if (k >= list.size()) {
                              if (entity != null) {
                                   movingobjectposition = new RayTraceResult(entity);
                              }

                              if (movingobjectposition == null || movingobjectposition.field_72308_g == null) {
                                   break;
                              }

                              if (this.npc != null && movingobjectposition.field_72308_g instanceof EntityLivingBase && this.npc.func_184191_r((EntityLivingBase)movingobjectposition.field_72308_g)) {
                                   movingobjectposition = null;
                                   break;
                              }

                              if (!(movingobjectposition.field_72308_g instanceof EntityPlayer)) {
                                   break;
                              }

                              EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.field_72308_g;
                              if (entityplayer.field_71075_bZ.field_75102_a || this.thrower instanceof EntityPlayer && !((EntityPlayer)this.thrower).func_96122_a(entityplayer)) {
                                   movingobjectposition = null;
                              }
                              break;
                         }

                         Entity entity1 = (Entity)list.get(k);
                         if (entity1.func_70067_L() && (!entity1.func_70028_i(this.thrower) || this.field_70195_i >= 25)) {
                              float f = 0.3F;
                              AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_72314_b((double)f, (double)f, (double)f);
                              RayTraceResult movingobjectposition1 = axisalignedbb.func_72327_a(vec3, vec31);
                              if (movingobjectposition1 != null) {
                                   double d1 = vec3.func_72438_d(movingobjectposition1.field_72307_f);
                                   if (d1 < d0 || d0 == 0.0D) {
                                        entity = entity1;
                                        d0 = d1;
                                   }
                              }
                         }

                         ++k;
                    }
               }

               if (movingobjectposition != null) {
                    if (movingobjectposition.field_72313_a == Type.BLOCK && this.world.func_180495_p(movingobjectposition.func_178782_a()).func_177230_c() == Blocks.field_150427_aO) {
                         this.func_181015_d(movingobjectposition.func_178782_a());
                    } else {
                         this.field_70180_af.func_187227_b(Rotating, false);
                         this.func_70184_a(movingobjectposition);
                    }
               }

               this.field_70165_t += this.field_70159_w;
               this.field_70163_u += this.field_70181_x;
               this.field_70161_v += this.field_70179_y;
               float f1 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
               this.field_70177_z = (float)(Math.atan2(this.field_70159_w, this.field_70179_y) * 180.0D / 3.141592653589793D);

               for(this.field_70125_A = (float)(Math.atan2(this.field_70181_x, (double)f1) * 180.0D / 3.141592653589793D); this.field_70125_A - this.field_70127_C < -180.0F; this.field_70127_C -= 360.0F) {
               }

               while(this.field_70125_A - this.field_70127_C >= 180.0F) {
                    this.field_70127_C += 360.0F;
               }

               while(this.field_70177_z - this.field_70126_B < -180.0F) {
                    this.field_70126_B -= 360.0F;
               }

               while(this.field_70177_z - this.field_70126_B >= 180.0F) {
                    this.field_70126_B += 360.0F;
               }

               this.field_70125_A = this.field_70127_C + (this.field_70125_A - this.field_70127_C);
               this.field_70177_z = this.field_70126_B + (this.field_70177_z - this.field_70126_B);
               if (this.isRotating()) {
                    int spin = this.isBlock() ? 10 : 20;
                    this.field_70125_A -= (float)(this.field_70195_i % 15 * spin) * this.getSpeed();
               }

               float f2 = this.getMotionFactor();
               float f3 = this.func_70185_h();
               if (this.func_70090_H()) {
                    if (this.world.field_72995_K) {
                         for(int k = 0; k < 4; ++k) {
                              float f4 = 0.25F;
                              this.world.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t - this.field_70159_w * (double)f4, this.field_70163_u - this.field_70181_x * (double)f4, this.field_70161_v - this.field_70179_y * (double)f4, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
                         }
                    }

                    f2 = 0.8F;
               }

               this.field_70159_w *= (double)f2;
               this.field_70181_x *= (double)f2;
               this.field_70179_y *= (double)f2;
               if (this.hasGravity()) {
                    this.field_70181_x -= (double)f3;
               }

               if (this.accelerate) {
                    this.field_70159_w += this.accelerationX;
                    this.field_70181_x += this.accelerationY;
                    this.field_70179_y += this.accelerationZ;
               }

               if (this.world.field_72995_K && (Integer)this.field_70180_af.func_187225_a(Particle) > 0) {
                    this.world.func_175688_a(ParticleType.getMCType((Integer)this.field_70180_af.func_187225_a(Particle)), this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
               }

               this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               this.func_145775_I();
          }

     }

     public boolean isBlock() {
          ItemStack item = this.getItemDisplay();
          return item.func_190926_b() ? false : item.func_77973_b() instanceof ItemBlock;
     }

     private Item getItem() {
          ItemStack item = this.getItemDisplay();
          return item.func_190926_b() ? Items.field_190931_a : item.func_77973_b();
     }

     protected float getMotionFactor() {
          return this.accelerate ? 0.95F : 1.0F;
     }

     protected void func_70184_a(RayTraceResult movingobjectposition) {
          if (!this.world.field_72995_K) {
               BlockPos pos = null;
               ProjectileEvent.ImpactEvent event;
               if (movingobjectposition.field_72308_g != null) {
                    pos = movingobjectposition.field_72308_g.func_180425_c();
                    event = new ProjectileEvent.ImpactEvent((IProjectile)NpcAPI.Instance().getIEntity(this), 0, movingobjectposition.field_72308_g);
               } else {
                    pos = movingobjectposition.func_178782_a();
                    event = new ProjectileEvent.ImpactEvent((IProjectile)NpcAPI.Instance().getIEntity(this), 1, NpcAPI.Instance().getIBlock(this.world, pos));
               }

               if (pos == BlockPos.field_177992_a) {
                    pos = new BlockPos(movingobjectposition.field_72307_f);
               }

               if (this.callback != null && this.callback.onImpact(this, pos, movingobjectposition.field_72308_g)) {
                    return;
               }

               EventHooks.onProjectileImpact(this, event);
          }

          float f3;
          if (movingobjectposition.field_72308_g != null) {
               float damage = this.damage;
               if (damage == 0.0F) {
                    damage = 0.001F;
               }

               if (movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76356_a(this, this.func_85052_h()), damage)) {
                    if (movingobjectposition.field_72308_g instanceof EntityLivingBase && (this.isArrow() || this.sticksToWalls())) {
                         EntityLivingBase entityliving = (EntityLivingBase)movingobjectposition.field_72308_g;
                         if (!this.world.field_72995_K) {
                              entityliving.func_85034_r(entityliving.func_85035_bI() + 1);
                         }

                         if (this.destroyedOnEntityHit && !(movingobjectposition.field_72308_g instanceof EntityEnderman)) {
                              this.func_70106_y();
                         }
                    }

                    if (this.isBlock()) {
                         this.world.func_180498_a((EntityPlayer)null, 2001, movingobjectposition.field_72308_g.func_180425_c(), Item.func_150891_b(this.getItem()));
                    } else if (!this.isArrow() && !this.sticksToWalls()) {
                         int[] intArr = new int[]{Item.func_150891_b(this.getItem())};
                         if (this.getItem().func_77614_k()) {
                              intArr = new int[]{Item.func_150891_b(this.getItem()), this.getItemDisplay().func_77960_j()};
                         }

                         for(int i = 0; i < 8; ++i) {
                              this.world.func_175688_a(EnumParticleTypes.ITEM_CRACK, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70146_Z.nextGaussian() * 0.15D, this.field_70146_Z.nextGaussian() * 0.2D, this.field_70146_Z.nextGaussian() * 0.15D, intArr);
                         }
                    }

                    if (this.punch > 0) {
                         f3 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
                         if (f3 > 0.0F) {
                              movingobjectposition.field_72308_g.func_70024_g(this.field_70159_w * (double)this.punch * 0.6000000238418579D / (double)f3, 0.1D, this.field_70179_y * (double)this.punch * 0.6000000238418579D / (double)f3);
                         }
                    }

                    if (this.effect != 0 && movingobjectposition.field_72308_g instanceof EntityLivingBase) {
                         if (this.effect != 1) {
                              Potion p = PotionEffectType.getMCType(this.effect);
                              ((EntityLivingBase)movingobjectposition.field_72308_g).func_70690_d(new PotionEffect(p, this.duration * 20, this.amplify));
                         } else {
                              movingobjectposition.field_72308_g.func_70015_d(this.duration);
                         }
                    }
               } else if (this.hasGravity() && (this.isArrow() || this.sticksToWalls())) {
                    this.field_70159_w *= -0.10000000149011612D;
                    this.field_70181_x *= -0.10000000149011612D;
                    this.field_70179_y *= -0.10000000149011612D;
                    this.field_70177_z += 180.0F;
                    this.field_70126_B += 180.0F;
                    this.field_70195_i = 0;
               }
          } else if (!this.isArrow() && !this.sticksToWalls()) {
               if (this.isBlock()) {
                    this.world.func_180498_a((EntityPlayer)null, 2001, this.func_180425_c(), Item.func_150891_b(this.getItem()));
               } else {
                    int[] intArr = new int[]{Item.func_150891_b(this.getItem())};
                    if (this.getItem().func_77614_k()) {
                         intArr = new int[]{Item.func_150891_b(this.getItem()), this.getItemDisplay().func_77960_j()};
                    }

                    for(int i = 0; i < 8; ++i) {
                         this.world.func_175688_a(EnumParticleTypes.ITEM_CRACK, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70146_Z.nextGaussian() * 0.15D, this.field_70146_Z.nextGaussian() * 0.2D, this.field_70146_Z.nextGaussian() * 0.15D, intArr);
                    }
               }
          } else {
               this.tilePos = movingobjectposition.func_178782_a();
               IBlockState state = this.world.func_180495_p(this.tilePos);
               this.inTile = state.func_177230_c();
               this.inData = this.inTile.func_176201_c(state);
               this.field_70159_w = (double)((float)(movingobjectposition.field_72307_f.field_72450_a - this.field_70165_t));
               this.field_70181_x = (double)((float)(movingobjectposition.field_72307_f.field_72448_b - this.field_70163_u));
               this.field_70179_y = (double)((float)(movingobjectposition.field_72307_f.field_72449_c - this.field_70161_v));
               f3 = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
               this.field_70165_t -= this.field_70159_w / (double)f3 * 0.05000000074505806D;
               this.field_70163_u -= this.field_70181_x / (double)f3 * 0.05000000074505806D;
               this.field_70161_v -= this.field_70179_y / (double)f3 * 0.05000000074505806D;
               this.field_174854_a = true;
               this.arrowShake = 7;
               if (!this.hasGravity()) {
                    this.field_70180_af.func_187227_b(Gravity, true);
               }

               if (this.inTile != null) {
                    this.inTile.func_180634_a(this.world, this.tilePos, state, this);
               }
          }

          if (this.explosiveRadius > 0) {
               boolean terraindamage = this.world.func_82736_K().func_82766_b("mobGriefing") && this.explosiveDamage;
               this.world.func_72885_a((Entity)(this.func_85052_h() == null ? this : this.func_85052_h()), this.field_70165_t, this.field_70163_u, this.field_70161_v, (float)this.explosiveRadius, this.effect == 1, terraindamage);
               if (this.effect != 0) {
                    AxisAlignedBB axisalignedbb = this.func_174813_aQ().func_72314_b((double)(this.explosiveRadius * 2), (double)(this.explosiveRadius * 2), (double)(this.explosiveRadius * 2));
                    List list1 = this.world.func_72872_a(EntityLivingBase.class, axisalignedbb);
                    Iterator var5 = list1.iterator();

                    while(var5.hasNext()) {
                         EntityLivingBase entity = (EntityLivingBase)var5.next();
                         if (this.effect != 1) {
                              Potion p = PotionEffectType.getMCType(this.effect);
                              if (p != null) {
                                   entity.func_70690_d(new PotionEffect(p, this.duration * 20, this.amplify));
                              }
                         } else {
                              entity.func_70015_d(this.duration);
                         }
                    }

                    this.world.func_180498_a((EntityPlayer)null, 2002, this.func_180425_c(), this.getPotionColor(this.effect));
               }

               this.func_70106_y();
          }

          if (!this.world.field_72995_K && !this.isArrow() && !this.sticksToWalls()) {
               this.func_70106_y();
          }

     }

     private void blockParticles() {
     }

     public void func_70014_b(NBTTagCompound par1NBTTagCompound) {
          par1NBTTagCompound.func_74777_a("xTile", (short)this.tilePos.func_177958_n());
          par1NBTTagCompound.func_74777_a("yTile", (short)this.tilePos.func_177956_o());
          par1NBTTagCompound.func_74777_a("zTile", (short)this.tilePos.func_177952_p());
          par1NBTTagCompound.func_74774_a("inTile", (byte)Block.func_149682_b(this.inTile));
          par1NBTTagCompound.func_74774_a("inData", (byte)this.inData);
          par1NBTTagCompound.func_74774_a("shake", (byte)this.field_70191_b);
          par1NBTTagCompound.func_74757_a("inGround", this.field_174854_a);
          par1NBTTagCompound.func_74757_a("isArrow", this.isArrow());
          par1NBTTagCompound.setTag("direction", this.func_70087_a(new double[]{this.field_70159_w, this.field_70181_x, this.field_70179_y}));
          par1NBTTagCompound.func_74757_a("canBePickedUp", this.canBePickedUp);
          if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
               this.throwerName = this.thrower.func_110124_au().toString();
          }

          par1NBTTagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
          par1NBTTagCompound.setTag("Item", this.getItemDisplay().func_77955_b(new NBTTagCompound()));
          par1NBTTagCompound.func_74776_a("damagev2", this.damage);
          par1NBTTagCompound.setInteger("punch", this.punch);
          par1NBTTagCompound.setInteger("size", (Integer)this.field_70180_af.func_187225_a(Size));
          par1NBTTagCompound.setInteger("velocity", (Integer)this.field_70180_af.func_187225_a(Velocity));
          par1NBTTagCompound.setInteger("explosiveRadius", this.explosiveRadius);
          par1NBTTagCompound.setInteger("effectDuration", this.duration);
          par1NBTTagCompound.func_74757_a("gravity", this.hasGravity());
          par1NBTTagCompound.func_74757_a("accelerate", this.accelerate);
          par1NBTTagCompound.func_74757_a("glows", (Boolean)this.field_70180_af.func_187225_a(Glows));
          par1NBTTagCompound.setInteger("PotionEffect", this.effect);
          par1NBTTagCompound.setInteger("trailenum", (Integer)this.field_70180_af.func_187225_a(Particle));
          par1NBTTagCompound.func_74757_a("Render3D", (Boolean)this.field_70180_af.func_187225_a(Is3d));
          par1NBTTagCompound.func_74757_a("Spins", (Boolean)this.field_70180_af.func_187225_a(Rotating));
          par1NBTTagCompound.func_74757_a("Sticks", (Boolean)this.field_70180_af.func_187225_a(Sticks));
          par1NBTTagCompound.setInteger("accuracy", this.accuracy);
     }

     public void func_70037_a(NBTTagCompound compound) {
          this.tilePos = new BlockPos(compound.func_74765_d("xTile"), compound.func_74765_d("yTile"), compound.func_74765_d("zTile"));
          this.inTile = Block.func_149729_e(compound.func_74771_c("inTile") & 255);
          this.inData = compound.func_74771_c("inData") & 255;
          this.field_70191_b = compound.func_74771_c("shake") & 255;
          this.field_174854_a = compound.func_74771_c("inGround") == 1;
          this.field_70180_af.func_187227_b(Arrow, compound.getBoolean("isArrow"));
          this.throwerName = compound.getString("ownerName");
          this.canBePickedUp = compound.getBoolean("canBePickedUp");
          this.damage = compound.func_74760_g("damagev2");
          this.punch = compound.func_74762_e("punch");
          this.explosiveRadius = compound.func_74762_e("explosiveRadius");
          this.duration = compound.func_74762_e("effectDuration");
          this.accelerate = compound.getBoolean("accelerate");
          this.effect = compound.func_74762_e("PotionEffect");
          this.accuracy = compound.func_74762_e("accuracy");
          this.field_70180_af.func_187227_b(Particle, compound.func_74762_e("trailenum"));
          this.field_70180_af.func_187227_b(Size, compound.func_74762_e("size"));
          this.field_70180_af.func_187227_b(Glows, compound.getBoolean("glows"));
          this.field_70180_af.func_187227_b(Velocity, compound.func_74762_e("velocity"));
          this.field_70180_af.func_187227_b(Gravity, compound.getBoolean("gravity"));
          this.field_70180_af.func_187227_b(Is3d, compound.getBoolean("Render3D"));
          this.field_70180_af.func_187227_b(Rotating, compound.getBoolean("Spins"));
          this.field_70180_af.func_187227_b(Sticks, compound.getBoolean("Sticks"));
          if (this.throwerName != null && this.throwerName.length() == 0) {
               this.throwerName = null;
          }

          if (compound.hasKey("direction")) {
               NBTTagList nbttaglist = compound.getTagList("direction", 6);
               this.field_70159_w = nbttaglist.func_150309_d(0);
               this.field_70181_x = nbttaglist.func_150309_d(1);
               this.field_70179_y = nbttaglist.func_150309_d(2);
          }

          NBTTagCompound var2 = compound.getCompoundTag("Item");
          ItemStack item = new ItemStack(var2);
          if (item.func_190926_b()) {
               this.func_70106_y();
          } else {
               this.field_70180_af.func_187227_b(ItemStackThrown, item);
          }

     }

     public EntityLivingBase func_85052_h() {
          if (this.throwerName != null && !this.throwerName.isEmpty()) {
               try {
                    UUID uuid = UUID.fromString(this.throwerName);
                    if (this.thrower == null && uuid != null) {
                         this.thrower = this.world.func_152378_a(uuid);
                    }
               } catch (IllegalArgumentException var2) {
               }

               return this.thrower;
          } else {
               return null;
          }
     }

     private int getPotionColor(int p) {
          switch(p) {
          case 2:
               return 32660;
          case 3:
               return 32660;
          case 4:
               return 32696;
          case 5:
               return 32698;
          case 6:
               return 32732;
          case 7:
               return 15;
          case 8:
               return 32732;
          default:
               return 0;
          }
     }

     public void getStatProperties(DataRanged stats) {
          this.damage = (float)stats.getStrength();
          this.punch = stats.getKnockback();
          this.accelerate = stats.getAccelerate();
          this.explosiveRadius = stats.getExplodeSize();
          this.effect = stats.getEffectType();
          this.duration = stats.getEffectTime();
          this.amplify = stats.getEffectStrength();
          this.setParticleEffect(stats.getParticle());
          this.field_70180_af.func_187227_b(Size, stats.getSize());
          this.field_70180_af.func_187227_b(Glows, stats.getGlows());
          this.setSpeed(stats.getSpeed());
          this.setHasGravity(stats.getHasGravity());
          this.setIs3D(stats.getRender3D());
          this.setRotating(stats.getSpins());
          this.setStickInWall(stats.getSticks());
     }

     public void setParticleEffect(int type) {
          this.field_70180_af.func_187227_b(Particle, type);
     }

     public void setHasGravity(boolean bo) {
          this.field_70180_af.func_187227_b(Gravity, bo);
     }

     public void setIs3D(boolean bo) {
          this.field_70180_af.func_187227_b(Is3d, bo);
     }

     public void setStickInWall(boolean bo) {
          this.field_70180_af.func_187227_b(Sticks, bo);
     }

     public ItemStack getItemDisplay() {
          return (ItemStack)this.field_70180_af.func_187225_a(ItemStackThrown);
     }

     public float func_70013_c() {
          return (Boolean)this.field_70180_af.func_187225_a(Glows) ? 1.0F : super.func_70013_c();
     }

     @SideOnly(Side.CLIENT)
     public int func_70070_b() {
          return (Boolean)this.field_70180_af.func_187225_a(Glows) ? 15728880 : super.func_70070_b();
     }

     public boolean hasGravity() {
          return (Boolean)this.field_70180_af.func_187225_a(Gravity);
     }

     public void setSpeed(int speed) {
          this.field_70180_af.func_187227_b(Velocity, speed);
     }

     public float getSpeed() {
          return (float)(Integer)this.field_70180_af.func_187225_a(Velocity) / 10.0F;
     }

     public boolean isArrow() {
          return (Boolean)this.field_70180_af.func_187225_a(Arrow);
     }

     public void setRotating(boolean bo) {
          this.field_70180_af.func_187227_b(Rotating, bo);
     }

     public boolean isRotating() {
          return (Boolean)this.field_70180_af.func_187225_a(Rotating);
     }

     public boolean glows() {
          return (Boolean)this.field_70180_af.func_187225_a(Glows);
     }

     public boolean is3D() {
          return (Boolean)this.field_70180_af.func_187225_a(Is3d) || this.isBlock();
     }

     public boolean sticksToWalls() {
          return this.is3D() && (Boolean)this.field_70180_af.func_187225_a(Sticks);
     }

     public void func_70100_b_(EntityPlayer par1EntityPlayer) {
          if (!this.world.field_72995_K && this.canBePickedUp && this.field_174854_a && this.arrowShake <= 0) {
               if (par1EntityPlayer.inventory.func_70441_a(this.getItemDisplay())) {
                    this.field_174854_a = false;
                    this.func_184185_a(SoundEvents.field_187638_cR, 0.2F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    par1EntityPlayer.func_71001_a(this, 1);
                    this.func_70106_y();
               }

          }
     }

     protected boolean func_70041_e_() {
          return false;
     }

     public ITextComponent func_145748_c_() {
          return (ITextComponent)(!this.getItemDisplay().func_190926_b() ? new TextComponentTranslation(this.getItemDisplay().func_82833_r(), new Object[0]) : super.func_145748_c_());
     }

     static {
          Gravity = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187198_h);
          Arrow = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187198_h);
          Is3d = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187198_h);
          Glows = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187198_h);
          Rotating = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187198_h);
          Sticks = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187198_h);
          ItemStackThrown = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187196_f);
          Velocity = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187192_b);
          Size = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187192_b);
          Particle = EntityDataManager.func_187226_a(EntityProjectile.class, DataSerializers.field_187192_b);
     }

     public interface IProjectileCallback {
          boolean onImpact(EntityProjectile var1, BlockPos var2, Entity var3);
     }
}
