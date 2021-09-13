package noppes.npcs.entity;

import com.google.common.base.Predicate;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.IChatMessages;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcDamageSource;
import noppes.npcs.Server;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.ai.CombatHandler;
import noppes.npcs.ai.EntityAIAmbushTarget;
import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.ai.EntityAIAttackTarget;
import noppes.npcs.ai.EntityAIAvoidTarget;
import noppes.npcs.ai.EntityAIBustDoor;
import noppes.npcs.ai.EntityAIDodgeShoot;
import noppes.npcs.ai.EntityAIFindShade;
import noppes.npcs.ai.EntityAIFollow;
import noppes.npcs.ai.EntityAIJob;
import noppes.npcs.ai.EntityAILook;
import noppes.npcs.ai.EntityAIMoveIndoors;
import noppes.npcs.ai.EntityAIMovingPath;
import noppes.npcs.ai.EntityAIOrbitTarget;
import noppes.npcs.ai.EntityAIPanic;
import noppes.npcs.ai.EntityAIPounceTarget;
import noppes.npcs.ai.EntityAIRangedAttack;
import noppes.npcs.ai.EntityAIReturn;
import noppes.npcs.ai.EntityAIRole;
import noppes.npcs.ai.EntityAISprintToTarget;
import noppes.npcs.ai.EntityAIStalkTarget;
import noppes.npcs.ai.EntityAITransform;
import noppes.npcs.ai.EntityAIWander;
import noppes.npcs.ai.EntityAIWatchClosest;
import noppes.npcs.ai.EntityAIWaterNav;
import noppes.npcs.ai.EntityAIWorldLines;
import noppes.npcs.ai.EntityAIZigZagTarget;
import noppes.npcs.ai.FlyingMoveHelper;
import noppes.npcs.ai.selector.NPCAttackSelector;
import noppes.npcs.ai.target.EntityAIClearTarget;
import noppes.npcs.ai.target.EntityAIClosestTarget;
import noppes.npcs.ai.target.EntityAIOwnerHurtByTarget;
import noppes.npcs.ai.target.EntityAIOwnerHurtTarget;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.data.DataTransform;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.data.DataAI;
import noppes.npcs.entity.data.DataAbilities;
import noppes.npcs.entity.data.DataAdvanced;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.entity.data.DataInventory;
import noppes.npcs.entity.data.DataScript;
import noppes.npcs.entity.data.DataStats;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.items.ItemSoulstoneFilled;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobFollower;
import noppes.npcs.roles.JobInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RoleInterface;
import noppes.npcs.util.GameProfileAlt;

public abstract class EntityNPCInterface extends EntityCreature implements IEntityAdditionalSpawnData, ICommandSender, IRangedAttackMob, IAnimals {
     public static final DataParameter Attacking;
     protected static final DataParameter Animation;
     private static final DataParameter RoleData;
     private static final DataParameter JobData;
     private static final DataParameter FactionData;
     private static final DataParameter Walking;
     private static final DataParameter Interacting;
     private static final DataParameter IsDead;
     public static final GameProfileAlt CommandProfile;
     public static final GameProfileAlt ChatEventProfile;
     public static final GameProfileAlt GenericProfile;
     public static FakePlayer ChatEventPlayer;
     public static FakePlayer CommandPlayer;
     public static FakePlayer GenericPlayer;
     public ICustomNpc wrappedNPC;
     public DataAbilities abilities;
     public DataDisplay display;
     public DataStats stats;
     public DataAI ais;
     public DataAdvanced advanced;
     public DataInventory inventory;
     public DataScript script;
     public DataTransform transform;
     public DataTimers timers;
     public CombatHandler combatHandler = new CombatHandler(this);
     public String linkedName = "";
     public long linkedLast = 0L;
     public LinkedNpcController.LinkedData linkedData;
     public float baseHeight = 1.8F;
     public float scaleX;
     public float scaleY;
     public float scaleZ;
     private boolean wasKilled = false;
     public RoleInterface roleInterface;
     public JobInterface jobInterface;
     public HashMap dialogs;
     public boolean hasDied = false;
     public long killedtime = 0L;
     public long totalTicksAlive = 0L;
     private int taskCount = 1;
     public int lastInteract = 0;
     public Faction faction;
     private EntityAIRangedAttack aiRange;
     private EntityAIBase aiAttackTarget;
     public EntityAILook lookAi;
     public EntityAIAnimation animateAi;
     public List interactingEntities = new ArrayList();
     public ResourceLocation textureLocation = null;
     public ResourceLocation textureGlowLocation = null;
     public ResourceLocation textureCloakLocation = null;
     public int currentAnimation = 0;
     public int animationStart = 0;
     public int npcVersion;
     public IChatMessages messages;
     public boolean updateClient;
     public boolean updateAI;
     public final BossInfoServer bossInfo;
     public double field_20066_r;
     public double field_20065_s;
     public double field_20064_t;
     public double field_20063_u;
     public double field_20062_v;
     public double field_20061_w;
     private double startYPos;

     public EntityNPCInterface(World world) {
          super(world);
          this.npcVersion = VersionCompatibility.ModRev;
          this.updateClient = false;
          this.updateAI = false;
          this.bossInfo = new BossInfoServer(this.func_145748_c_(), Color.PURPLE, Overlay.PROGRESS);
          this.startYPos = -1.0D;
          if (!this.isRemote()) {
               this.wrappedNPC = new NPCWrapper(this);
          }

          this.dialogs = new HashMap();
          if (!CustomNpcs.DefaultInteractLine.isEmpty()) {
               this.advanced.interactLines.lines.put(0, new Line(CustomNpcs.DefaultInteractLine));
          }

          this.field_70728_aV = 0;
          this.scaleX = this.scaleY = this.scaleZ = 0.9375F;
          this.faction = this.getFaction();
          this.setFaction(this.faction.id);
          this.func_70105_a(1.0F, 1.0F);
          this.updateAI = true;
          this.bossInfo.func_186758_d(false);
     }

     public boolean func_70648_aU() {
          return this.ais.movementType == 2;
     }

     public boolean func_96092_aw() {
          return this.ais.movementType != 2;
     }

     protected void func_110147_ax() {
          super.func_110147_ax();
          this.abilities = new DataAbilities(this);
          this.display = new DataDisplay(this);
          this.stats = new DataStats(this);
          this.ais = new DataAI(this);
          this.advanced = new DataAdvanced(this);
          this.inventory = new DataInventory(this);
          this.transform = new DataTransform(this);
          this.script = new DataScript(this);
          this.timers = new DataTimers(this);
          this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e);
          this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_193334_e);
          this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.stats.maxHealth);
          this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a((double)CustomNpcs.NpcNavRange);
          this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a((double)this.getSpeed());
          this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a((double)this.stats.melee.getStrength());
          this.func_110148_a(SharedMonsterAttributes.field_193334_e).func_111128_a((double)(this.getSpeed() * 2.0F));
     }

     protected void func_70088_a() {
          super.func_70088_a();
          this.field_70180_af.func_187214_a(RoleData, String.valueOf(""));
          this.field_70180_af.func_187214_a(JobData, String.valueOf(""));
          this.field_70180_af.func_187214_a(FactionData, 0);
          this.field_70180_af.func_187214_a(Animation, 0);
          this.field_70180_af.func_187214_a(Walking, false);
          this.field_70180_af.func_187214_a(Interacting, false);
          this.field_70180_af.func_187214_a(IsDead, false);
          this.field_70180_af.func_187214_a(Attacking, false);
     }

     public boolean func_70089_S() {
          return super.func_70089_S() && !this.isKilled();
     }

     public void func_70071_h_() {
          super.func_70071_h_();
          if (this.field_70173_aa % 10 == 0) {
               this.startYPos = this.calculateStartYPos(this.ais.startPos()) + 1.0D;
               if (this.startYPos < 0.0D && !this.isRemote()) {
                    this.func_70106_y();
               }

               EventHooks.onNPCTick(this);
          }

          this.timers.update();
          if (this.world.field_72995_K && this.wasKilled != this.isKilled()) {
               this.field_70725_aQ = 0;
               this.updateHitbox();
          }

          this.wasKilled = this.isKilled();
          if (this.currentAnimation == 14) {
               this.field_70725_aQ = 19;
          }

     }

     public boolean func_70652_k(Entity par1Entity) {
          float f = (float)this.stats.melee.getStrength();
          if (this.stats.melee.getDelay() < 10) {
               par1Entity.field_70172_ad = 0;
          }

          if (par1Entity instanceof EntityLivingBase) {
               NpcEvent.MeleeAttackEvent event = new NpcEvent.MeleeAttackEvent(this.wrappedNPC, (EntityLivingBase)par1Entity, f);
               if (EventHooks.onNPCAttacksMelee(this, event)) {
                    return false;
               }

               f = event.damage;
          }

          boolean var4 = par1Entity.func_70097_a(new NpcDamageSource("mob", this), f);
          if (var4) {
               if (this.getOwner() instanceof EntityPlayer) {
                    EntityUtil.setRecentlyHit((EntityLivingBase)par1Entity);
               }

               if (this.stats.melee.getKnockback() > 0) {
                    par1Entity.func_70024_g((double)(-MathHelper.func_76126_a(this.field_70177_z * 3.1415927F / 180.0F) * (float)this.stats.melee.getKnockback() * 0.5F), 0.1D, (double)(MathHelper.func_76134_b(this.field_70177_z * 3.1415927F / 180.0F) * (float)this.stats.melee.getKnockback() * 0.5F));
                    this.field_70159_w *= 0.6D;
                    this.field_70179_y *= 0.6D;
               }

               if (this.advanced.role == 6) {
                    ((RoleCompanion)this.roleInterface).attackedEntity(par1Entity);
               }
          }

          if (this.stats.melee.getEffectType() != 0) {
               if (this.stats.melee.getEffectType() != 1) {
                    ((EntityLivingBase)par1Entity).func_70690_d(new PotionEffect(PotionEffectType.getMCType(this.stats.melee.getEffectType()), this.stats.melee.getEffectTime() * 20, this.stats.melee.getEffectStrength()));
               } else {
                    par1Entity.func_70015_d(this.stats.melee.getEffectTime());
               }
          }

          return var4;
     }

     public void func_70636_d() {
          if (!CustomNpcs.FreezeNPCs) {
               if (this.func_175446_cd()) {
                    super.func_70636_d();
               } else {
                    ++this.totalTicksAlive;
                    this.func_82168_bl();
                    if (this.field_70173_aa % 20 == 0) {
                         this.faction = this.getFaction();
                    }

                    if (!this.world.field_72995_K) {
                         if (!this.isKilled() && this.field_70173_aa % 20 == 0) {
                              this.advanced.scenes.update();
                              if (this.func_110143_aJ() < this.func_110138_aP()) {
                                   if (this.stats.healthRegen > 0 && !this.isAttacking()) {
                                        this.func_70691_i((float)this.stats.healthRegen);
                                   }

                                   if (this.stats.combatRegen > 0 && this.isAttacking()) {
                                        this.func_70691_i((float)this.stats.combatRegen);
                                   }
                              }

                              if (this.faction.getsAttacked && !this.isAttacking()) {
                                   List list = this.world.func_72872_a(EntityMob.class, this.func_174813_aQ().func_72314_b(16.0D, 16.0D, 16.0D));
                                   Iterator var2 = list.iterator();

                                   while(var2.hasNext()) {
                                        EntityMob mob = (EntityMob)var2.next();
                                        if (mob.func_70638_az() == null && this.canSee(mob)) {
                                             mob.func_70624_b(this);
                                        }
                                   }
                              }

                              if (this.linkedData != null && this.linkedData.time > this.linkedLast) {
                                   LinkedNpcController.Instance.loadNpcData(this);
                              }

                              if (this.updateClient) {
                                   this.updateClient();
                              }

                              if (this.updateAI) {
                                   this.updateTasks();
                                   this.updateAI = false;
                              }
                         }

                         if (this.func_110143_aJ() <= 0.0F && !this.isKilled()) {
                              this.func_70674_bp();
                              this.field_70180_af.func_187227_b(IsDead, true);
                              this.updateTasks();
                              this.updateHitbox();
                         }

                         if (this.display.getBossbar() == 2) {
                              this.bossInfo.func_186758_d(this.func_70638_az() != null);
                         }

                         this.field_70180_af.func_187227_b(Walking, !this.func_70661_as().func_75500_f());
                         this.field_70180_af.func_187227_b(Interacting, this.isInteracting());
                         this.combatHandler.update();
                         this.onCollide();
                    }

                    if (this.wasKilled != this.isKilled() && this.wasKilled) {
                         this.reset();
                    }

                    if (this.world.func_72935_r() && !this.world.field_72995_K && this.stats.burnInSun) {
                         float f = this.func_70013_c();
                         if (f > 0.5F && this.field_70146_Z.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.func_175710_j(new BlockPos(this))) {
                              this.func_70015_d(8);
                         }
                    }

                    super.func_70636_d();
                    if (this.world.field_72995_K) {
                         if (this.roleInterface != null) {
                              this.roleInterface.clientUpdate();
                         }

                         if (this.textureCloakLocation != null) {
                              this.cloakUpdate();
                         }

                         if (this.currentAnimation != (Integer)this.field_70180_af.func_187225_a(Animation)) {
                              this.currentAnimation = (Integer)this.field_70180_af.func_187225_a(Animation);
                              this.animationStart = this.field_70173_aa;
                              this.updateHitbox();
                         }

                         if (this.advanced.job == 1) {
                              ((JobBard)this.jobInterface).onLivingUpdate();
                         }
                    }

                    if (this.display.getBossbar() > 0) {
                         this.bossInfo.func_186735_a(this.func_110143_aJ() / this.func_110138_aP());
                    }

               }
          }
     }

     public void updateClient() {
          NBTTagCompound compound = this.writeSpawnData();
          compound.setInteger("EntityId", this.func_145782_y());
          Server.sendAssociatedData(this, EnumPacketClient.UPDATE_NPC, compound);
          this.updateClient = false;
     }

     protected void func_70665_d(DamageSource damageSrc, float damageAmount) {
          super.func_70665_d(damageSrc, damageAmount);
          this.combatHandler.damage(damageSrc, damageAmount);
     }

     public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
          if (this.world.field_72995_K) {
               return !this.isAttacking();
          } else if (hand != EnumHand.MAIN_HAND) {
               return true;
          } else {
               ItemStack stack = player.func_184586_b(hand);
               if (stack != null) {
                    Item item = stack.func_77973_b();
                    if (item == CustomItems.cloner || item == CustomItems.wand || item == CustomItems.mount || item == CustomItems.scripter) {
                         this.func_70624_b((EntityLivingBase)null);
                         this.func_70604_c((EntityLivingBase)null);
                         return true;
                    }

                    if (item == CustomItems.moving) {
                         this.func_70624_b((EntityLivingBase)null);
                         stack.func_77983_a("NPCID", new NBTTagInt(this.func_145782_y()));
                         player.func_145747_a(new TextComponentTranslation("Registered " + this.func_70005_c_() + " to your NPC Pather", new Object[0]));
                         return true;
                    }
               }

               if (EventHooks.onNPCInteract(this, player)) {
                    return false;
               } else if (this.getFaction().isAggressiveToPlayer(player)) {
                    return !this.isAttacking();
               } else {
                    this.addInteract(player);
                    Dialog dialog = this.getDialog(player);
                    QuestData data = PlayerData.get(player).questData.getQuestCompletion(player, this);
                    if (data != null) {
                         Server.sendData((EntityPlayerMP)player, EnumPacketClient.QUEST_COMPLETION, data.quest.id);
                    } else if (dialog != null) {
                         NoppesUtilServer.openDialog(player, this, dialog);
                    } else if (this.roleInterface != null) {
                         this.roleInterface.interact(player);
                    } else {
                         this.say(player, this.advanced.getInteractLine());
                    }

                    return true;
               }
          }
     }

     public void addInteract(EntityLivingBase entity) {
          if (this.ais.stopAndInteract && !this.isAttacking() && entity.func_70089_S() && !this.func_175446_cd()) {
               if (this.field_70173_aa - this.lastInteract < 180) {
                    this.interactingEntities.clear();
               }

               this.func_70661_as().func_75499_g();
               this.lastInteract = this.field_70173_aa;
               if (!this.interactingEntities.contains(entity)) {
                    this.interactingEntities.add(entity);
               }

          }
     }

     public boolean isInteracting() {
          if (this.field_70173_aa - this.lastInteract < 40 || this.isRemote() && (Boolean)this.field_70180_af.func_187225_a(Interacting)) {
               return true;
          } else {
               return this.ais.stopAndInteract && !this.interactingEntities.isEmpty() && this.field_70173_aa - this.lastInteract < 180;
          }
     }

     private Dialog getDialog(EntityPlayer player) {
          Iterator var2 = this.dialogs.values().iterator();

          while(var2.hasNext()) {
               DialogOption option = (DialogOption)var2.next();
               if (option != null && option.hasDialog()) {
                    Dialog dialog = option.getDialog();
                    if (dialog.availability.isAvailable(player)) {
                         return dialog;
                    }
               }
          }

          return null;
     }

     public boolean func_70097_a(DamageSource damagesource, float i) {
          if (!this.world.field_72995_K && !CustomNpcs.FreezeNPCs && !damagesource.field_76373_n.equals("inWall")) {
               if (damagesource.field_76373_n.equals("outOfWorld") && this.isKilled()) {
                    this.reset();
               }

               i = this.stats.resistances.applyResistance(damagesource, i);
               if ((float)this.field_70172_ad > (float)this.field_70771_an / 2.0F && i <= this.field_110153_bc) {
                    return false;
               } else {
                    Entity entity = NoppesUtilServer.GetDamageSourcee(damagesource);
                    EntityLivingBase attackingEntity = null;
                    if (entity instanceof EntityLivingBase) {
                         attackingEntity = (EntityLivingBase)entity;
                    }

                    if (attackingEntity != null && attackingEntity == this.getOwner()) {
                         return false;
                    } else {
                         if (attackingEntity instanceof EntityNPCInterface) {
                              EntityNPCInterface npc = (EntityNPCInterface)attackingEntity;
                              if (npc.faction.id == this.faction.id) {
                                   return false;
                              }

                              if (npc.getOwner() instanceof EntityPlayer) {
                                   this.field_70718_bc = 100;
                              }
                         } else if (attackingEntity instanceof EntityPlayer && this.faction.isFriendlyToPlayer((EntityPlayer)attackingEntity)) {
                              ForgeHooks.onLivingAttack(this, damagesource, i);
                              return false;
                         }

                         NpcEvent.DamagedEvent event = new NpcEvent.DamagedEvent(this.wrappedNPC, entity, i, damagesource);
                         if (EventHooks.onNPCDamaged(this, event)) {
                              ForgeHooks.onLivingAttack(this, damagesource, i);
                              return false;
                         } else {
                              i = event.damage;
                              if (this.isKilled()) {
                                   return false;
                              } else if (attackingEntity == null) {
                                   return super.func_70097_a(damagesource, i);
                              } else {
                                   boolean var6;
                                   try {
                                        if (!this.isAttacking()) {
                                             if (i > 0.0F) {
                                                  List inRange = this.world.func_72872_a(EntityNPCInterface.class, this.func_174813_aQ().func_72314_b(32.0D, 16.0D, 32.0D));
                                                  Iterator var7 = inRange.iterator();

                                                  while(true) {
                                                       if (!var7.hasNext()) {
                                                            this.func_70624_b(attackingEntity);
                                                            break;
                                                       }

                                                       EntityNPCInterface npc = (EntityNPCInterface)var7.next();
                                                       if (!npc.isKilled() && npc.advanced.defendFaction && npc.faction.id == this.faction.id && (npc.canSee(this) || npc.ais.directLOS || npc.canSee(attackingEntity))) {
                                                            npc.onAttack(attackingEntity);
                                                       }
                                                  }
                                             }

                                             var6 = super.func_70097_a(damagesource, i);
                                             return var6;
                                        }

                                        if (this.func_70638_az() != null && attackingEntity != null && this.func_70068_e(this.func_70638_az()) > this.func_70068_e(attackingEntity)) {
                                             this.func_70624_b(attackingEntity);
                                        }

                                        var6 = super.func_70097_a(damagesource, i);
                                   } finally {
                                        if (event.clearTarget) {
                                             this.func_70624_b((EntityLivingBase)null);
                                             this.func_70604_c((EntityLivingBase)null);
                                        }

                                   }

                                   return var6;
                              }
                         }
                    }
               }
          } else {
               return false;
          }
     }

     public void onAttack(EntityLivingBase entity) {
          if (entity != null && entity != this && !this.isAttacking() && this.ais.onAttack != 3 && entity != this.getOwner()) {
               super.func_70624_b(entity);
          }
     }

     public void func_70624_b(EntityLivingBase entity) {
          if ((!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).field_71075_bZ.field_75102_a) && (entity == null || entity != this.getOwner()) && this.func_70638_az() != entity) {
               if (entity != null) {
                    NpcEvent.TargetEvent event = new NpcEvent.TargetEvent(this.wrappedNPC, entity);
                    if (EventHooks.onNPCTarget(this, event)) {
                         return;
                    }

                    if (event.entity == null) {
                         entity = null;
                    } else {
                         entity = event.entity.getMCEntity();
                    }
               } else {
                    Iterator var4 = this.field_70715_bh.field_75782_a.iterator();

                    while(var4.hasNext()) {
                         EntityAITaskEntry en = (EntityAITaskEntry)var4.next();
                         if (en.field_188524_c) {
                              en.field_188524_c = false;
                              en.field_75733_a.func_75251_c();
                         }
                    }

                    if (EventHooks.onNPCTargetLost(this, this.func_70638_az())) {
                         return;
                    }
               }

               if (entity != null && entity != this && this.ais.onAttack != 3 && !this.isAttacking() && !this.isRemote()) {
                    Line line = this.advanced.getAttackLine();
                    if (line != null) {
                         this.saySurrounding(Line.formatTarget(line, entity));
                    }
               }

               super.func_70624_b(entity);
          }
     }

     public void func_82196_d(EntityLivingBase entity, float f) {
          ItemStack proj = ItemStackWrapper.MCItem(this.inventory.getProjectile());
          if (proj == null) {
               this.updateAI = true;
          } else {
               NpcEvent.RangedLaunchedEvent event = new NpcEvent.RangedLaunchedEvent(this.wrappedNPC, entity, (float)this.stats.ranged.getStrength());

               for(int i = 0; i < this.stats.ranged.getShotCount(); ++i) {
                    EntityProjectile projectile = this.shoot(entity, this.stats.ranged.getAccuracy(), proj, f == 1.0F);
                    projectile.damage = event.damage;
                    projectile.callback = (projectile1, pos, entity1) -> {
                         if (proj.func_77973_b() == CustomItems.soulstoneFull) {
                              Entity e = ItemSoulstoneFilled.Spawn((EntityPlayer)null, proj, this.world, pos);
                              if (e instanceof EntityLivingBase && entity1 instanceof EntityLivingBase) {
                                   if (e instanceof EntityLiving) {
                                        ((EntityLiving)e).func_70624_b((EntityLivingBase)entity1);
                                   } else {
                                        ((EntityLivingBase)e).func_70604_c((EntityLivingBase)entity1);
                                   }
                              }
                         }

                         projectile1.func_184185_a(this.stats.ranged.getSoundEvent(entity1 != null ? 1 : 2), 1.0F, 1.2F / (this.func_70681_au().nextFloat() * 0.2F + 0.9F));
                         return false;
                    };
                    this.func_184185_a(this.stats.ranged.getSoundEvent(0), 2.0F, 1.0F);
                    event.projectiles.add((IProjectile)NpcAPI.Instance().getIEntity(projectile));
               }

               EventHooks.onNPCRangedLaunched(this, event);
          }
     }

     public EntityProjectile shoot(EntityLivingBase entity, int accuracy, ItemStack proj, boolean indirect) {
          return this.shoot(entity.field_70165_t, entity.func_174813_aQ().field_72338_b + (double)(entity.height / 2.0F), entity.field_70161_v, accuracy, proj, indirect);
     }

     public EntityProjectile shoot(double x, double y, double z, int accuracy, ItemStack proj, boolean indirect) {
          EntityProjectile projectile = new EntityProjectile(this.world, this, proj.func_77946_l(), true);
          double varX = x - this.field_70165_t;
          double varY = y - (this.field_70163_u + (double)this.func_70047_e());
          double varZ = z - this.field_70161_v;
          float varF = projectile.hasGravity() ? MathHelper.func_76133_a(varX * varX + varZ * varZ) : 0.0F;
          float angle = projectile.getAngleForXYZ(varX, varY, varZ, (double)varF, indirect);
          float acc = 20.0F - (float)MathHelper.func_76141_d((float)accuracy / 5.0F);
          projectile.func_70186_c(varX, varY, varZ, angle, acc);
          this.world.func_72838_d(projectile);
          return projectile;
     }

     private void clearTasks(EntityAITasks tasks) {
          Iterator iterator = tasks.field_75782_a.iterator();
          List list = new ArrayList(tasks.field_75782_a);
          Iterator var4 = list.iterator();

          while(var4.hasNext()) {
               EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry)var4.next();
               tasks.func_85156_a(entityaitaskentry.field_75733_a);
          }

          tasks.field_75782_a.clear();
     }

     private void updateTasks() {
          if (this.world != null && !this.world.field_72995_K) {
               this.clearTasks(this.field_70714_bg);
               this.clearTasks(this.field_70715_bh);
               if (!this.isKilled()) {
                    Predicate attackEntitySelector = new NPCAttackSelector(this);
                    this.field_70715_bh.func_75776_a(0, new EntityAIClearTarget(this));
                    this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
                    this.field_70715_bh.func_75776_a(2, new EntityAIClosestTarget(this, EntityLivingBase.class, 4, this.ais.directLOS, false, attackEntitySelector));
                    this.field_70715_bh.func_75776_a(3, new EntityAIOwnerHurtByTarget(this));
                    this.field_70715_bh.func_75776_a(4, new EntityAIOwnerHurtTarget(this));
                    this.world.field_184152_t.func_72709_b(this);
                    if (this.ais.movementType == 1) {
                         this.field_70765_h = new FlyingMoveHelper(this);
                         this.field_70699_by = new PathNavigateFlying(this, this.world);
                    } else if (this.ais.movementType == 2) {
                         this.field_70765_h = new FlyingMoveHelper(this);
                         this.field_70699_by = new PathNavigateSwimmer(this, this.world);
                    } else {
                         this.field_70765_h = new EntityMoveHelper(this);
                         this.field_70699_by = new PathNavigateGround(this, this.world);
                         this.field_70714_bg.func_75776_a(0, new EntityAIWaterNav(this));
                    }

                    this.world.field_184152_t.func_72703_a(this);
                    this.taskCount = 1;
                    this.addRegularEntries();
                    this.doorInteractType();
                    this.seekShelter();
                    this.setResponse();
                    this.setMoveType();
               }
          }
     }

     private void setResponse() {
          this.aiAttackTarget = this.aiRange = null;
          if (this.ais.canSprint) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAISprintToTarget(this));
          }

          if (this.ais.onAttack == 1) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIPanic(this, 1.2F));
          } else if (this.ais.onAttack == 2) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIAvoidTarget(this));
          } else if (this.ais.onAttack == 0) {
               if (this.ais.canLeap) {
                    this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIPounceTarget(this));
               }

               if (this.inventory.getProjectile() == null) {
                    switch(this.ais.tacticalVariant) {
                    case 1:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIZigZagTarget(this, 1.3D));
                         break;
                    case 2:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIOrbitTarget(this, 1.3D, true));
                         break;
                    case 3:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIAvoidTarget(this));
                         break;
                    case 4:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIAmbushTarget(this, 1.2D));
                         break;
                    case 5:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIStalkTarget(this));
                    }
               } else {
                    switch(this.ais.tacticalVariant) {
                    case 1:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIDodgeShoot(this));
                         break;
                    case 2:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIOrbitTarget(this, 1.3D, false));
                         break;
                    case 3:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIAvoidTarget(this));
                         break;
                    case 4:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIAmbushTarget(this, 1.3D));
                         break;
                    case 5:
                         this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIStalkTarget(this));
                    }
               }

               this.field_70714_bg.func_75776_a(this.taskCount, this.aiAttackTarget = new EntityAIAttackTarget(this));
               ((EntityAIAttackTarget)this.aiAttackTarget).navOverride(this.ais.tacticalVariant == 6);
               if (this.inventory.getProjectile() != null) {
                    this.field_70714_bg.func_75776_a(this.taskCount++, this.aiRange = new EntityAIRangedAttack(this));
                    this.aiRange.navOverride(this.ais.tacticalVariant == 6);
               }
          } else if (this.ais.onAttack == 3) {
          }

     }

     public boolean canFly() {
          return false;
     }

     public void setMoveType() {
          if (this.ais.getMovingType() == 1) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIWander(this));
          }

          if (this.ais.getMovingType() == 2) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIMovingPath(this));
          }

     }

     public void doorInteractType() {
          if (!this.canFly()) {
               EntityAIBase aiDoor = null;
               if (this.ais.doorInteract == 1) {
                    this.field_70714_bg.func_75776_a(this.taskCount++, (EntityAIBase)(aiDoor = new EntityAIOpenDoor(this, true)));
               } else if (this.ais.doorInteract == 0) {
                    this.field_70714_bg.func_75776_a(this.taskCount++, (EntityAIBase)(aiDoor = new EntityAIBustDoor(this)));
               }

               if (this.func_70661_as() instanceof PathNavigateGround) {
                    ((PathNavigateGround)this.func_70661_as()).func_179688_b(aiDoor != null);
               }

          }
     }

     public void seekShelter() {
          if (this.ais.findShelter == 0) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIMoveIndoors(this));
          } else if (this.ais.findShelter == 1) {
               if (!this.canFly()) {
                    this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIRestrictSun(this));
               }

               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIFindShade(this));
          }

     }

     public void addRegularEntries() {
          this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIReturn(this));
          this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIFollow(this));
          if (this.ais.getStandingType() != 1 && this.ais.getStandingType() != 3) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIWatchClosest(this, EntityLivingBase.class, 5.0F));
          }

          this.field_70714_bg.func_75776_a(this.taskCount++, this.lookAi = new EntityAILook(this));
          this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIWorldLines(this));
          this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIJob(this));
          this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAIRole(this));
          this.field_70714_bg.func_75776_a(this.taskCount++, this.animateAi = new EntityAIAnimation(this));
          if (this.transform.isValid()) {
               this.field_70714_bg.func_75776_a(this.taskCount++, new EntityAITransform(this));
          }

     }

     public float getSpeed() {
          return (float)this.ais.getWalkingSpeed() / 20.0F;
     }

     public float func_180484_a(BlockPos pos) {
          if (this.ais.movementType == 2) {
               return this.world.func_180495_p(pos).func_185904_a() == Material.field_151586_h ? 10.0F : 0.0F;
          } else {
               float weight = this.world.func_175724_o(pos) - 0.5F;
               if (this.world.func_180495_p(pos).func_185914_p()) {
                    weight += 10.0F;
               }

               return weight;
          }
     }

     protected int func_70682_h(int par1) {
          return !this.stats.canDrown ? par1 : super.func_70682_h(par1);
     }

     public EnumCreatureAttribute func_70668_bt() {
          return this.stats == null ? null : this.stats.creatureType;
     }

     public int func_70627_aG() {
          return 160;
     }

     public void func_70642_aH() {
          if (this.func_70089_S()) {
               this.advanced.playSound(this.func_70638_az() != null ? 1 : 0, this.func_70599_aP(), this.func_70647_i());
          }
     }

     protected void func_184581_c(DamageSource source) {
          this.advanced.playSound(2, this.func_70599_aP(), this.func_70647_i());
     }

     public SoundEvent func_184615_bR() {
          return null;
     }

     protected float func_70647_i() {
          return this.advanced.disablePitch ? 1.0F : super.func_70647_i();
     }

     protected void func_180429_a(BlockPos pos, Block block) {
          if (this.advanced.getSound(4) != null) {
               this.advanced.playSound(4, 0.15F, 1.0F);
          } else {
               super.func_180429_a(pos, block);
          }

     }

     public EntityPlayerMP getFakeChatPlayer() {
          if (this.world.field_72995_K) {
               return null;
          } else {
               EntityUtil.Copy(this, ChatEventPlayer);
               ChatEventProfile.npc = this;
               ChatEventPlayer.refreshDisplayName();
               ChatEventPlayer.func_70029_a(this.world);
               ChatEventPlayer.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               return ChatEventPlayer;
          }
     }

     public void saySurrounding(Line line) {
          if (line != null) {
               if (line.getShowText() && !line.getText().isEmpty()) {
                    ServerChatEvent event = new ServerChatEvent(this.getFakeChatPlayer(), line.getText(), new TextComponentTranslation(line.getText().replace("%", "%%"), new Object[0]));
                    if (CustomNpcs.NpcSpeachTriggersChatEvent && (MinecraftForge.EVENT_BUS.post(event) || event.getComponent() == null)) {
                         return;
                    }

                    line.setText(event.getComponent().func_150260_c().replace("%%", "%"));
               }

               List inRange = this.world.func_72872_a(EntityPlayer.class, this.func_174813_aQ().func_72314_b(20.0D, 20.0D, 20.0D));
               Iterator var3 = inRange.iterator();

               while(var3.hasNext()) {
                    EntityPlayer player = (EntityPlayer)var3.next();
                    this.say(player, line);
               }

          }
     }

     public void say(EntityPlayer player, Line line) {
          if (line != null && this.canSee(player)) {
               if (!line.getSound().isEmpty()) {
                    BlockPos pos = this.func_180425_c();
                    Server.sendData((EntityPlayerMP)player, EnumPacketClient.PLAY_SOUND, line.getSound(), pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), this.func_70599_aP(), this.func_70647_i());
               }

               if (!line.getText().isEmpty()) {
                    Server.sendData((EntityPlayerMP)player, EnumPacketClient.CHATBUBBLE, this.func_145782_y(), line.getText(), line.getShowText());
               }

          }
     }

     public boolean func_94059_bO() {
          return true;
     }

     public void func_70024_g(double d, double d1, double d2) {
          if (this.isWalking() && !this.isKilled()) {
               super.func_70024_g(d, d1, d2);
          }

     }

     public void func_70037_a(NBTTagCompound compound) {
          super.func_70037_a(compound);
          this.npcVersion = compound.func_74762_e("ModRev");
          VersionCompatibility.CheckNpcCompatibility(this, compound);
          this.display.readToNBT(compound);
          this.stats.readToNBT(compound);
          this.ais.readToNBT(compound);
          this.script.readFromNBT(compound);
          this.timers.readFromNBT(compound);
          this.advanced.readToNBT(compound);
          if (this.advanced.role != 0 && this.roleInterface != null) {
               this.roleInterface.readFromNBT(compound);
          }

          if (this.advanced.job != 0 && this.jobInterface != null) {
               this.jobInterface.readFromNBT(compound);
          }

          this.inventory.readEntityFromNBT(compound);
          this.transform.readToNBT(compound);
          this.killedtime = compound.func_74763_f("KilledTime");
          this.totalTicksAlive = compound.func_74763_f("TotalTicksAlive");
          this.linkedName = compound.getString("LinkedNpcName");
          if (!this.isRemote()) {
               LinkedNpcController.Instance.loadNpcData(this);
          }

          this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a((double)CustomNpcs.NpcNavRange);
          this.updateAI = true;
     }

     public void func_70014_b(NBTTagCompound compound) {
          super.func_70014_b(compound);
          this.display.writeToNBT(compound);
          this.stats.writeToNBT(compound);
          this.ais.writeToNBT(compound);
          this.script.writeToNBT(compound);
          this.timers.writeToNBT(compound);
          this.advanced.writeToNBT(compound);
          if (this.advanced.role != 0 && this.roleInterface != null) {
               this.roleInterface.writeToNBT(compound);
          }

          if (this.advanced.job != 0 && this.jobInterface != null) {
               this.jobInterface.writeToNBT(compound);
          }

          this.inventory.writeEntityToNBT(compound);
          this.transform.writeToNBT(compound);
          compound.func_74772_a("KilledTime", this.killedtime);
          compound.func_74772_a("TotalTicksAlive", this.totalTicksAlive);
          compound.setInteger("ModRev", this.npcVersion);
          compound.setString("LinkedNpcName", this.linkedName);
     }

     public void updateHitbox() {
          if (this.currentAnimation != 2 && this.currentAnimation != 7 && this.field_70725_aQ <= 0) {
               if (this.func_184218_aH()) {
                    this.field_70130_N = 0.6F;
                    this.height = this.baseHeight * 0.77F;
               } else {
                    this.field_70130_N = 0.6F;
                    this.height = this.baseHeight;
               }
          } else {
               this.field_70130_N = 0.8F;
               this.height = 0.4F;
          }

          this.field_70130_N = this.field_70130_N / 5.0F * (float)this.display.getSize();
          this.height = this.height / 5.0F * (float)this.display.getSize();
          if (!this.display.getHasHitbox() || this.isKilled() && this.stats.hideKilledBody) {
               this.field_70130_N = 1.0E-5F;
          }

          double var10000 = (double)(this.field_70130_N / 2.0F);
          World var10001 = this.world;
          if (var10000 > World.MAX_ENTITY_RADIUS) {
               World var1 = this.world;
               World.MAX_ENTITY_RADIUS = (double)(this.field_70130_N / 2.0F);
          }

          this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
     }

     public void func_70609_aI() {
          if (this.stats.spawnCycle != 3 && this.stats.spawnCycle != 4) {
               ++this.field_70725_aQ;
               if (!this.world.field_72995_K) {
                    if (!this.hasDied) {
                         this.func_70106_y();
                    }

                    if (this.killedtime < System.currentTimeMillis() && (this.stats.spawnCycle == 0 || this.world.func_72935_r() && this.stats.spawnCycle == 1 || !this.world.func_72935_r() && this.stats.spawnCycle == 2)) {
                         this.reset();
                    }

               }
          } else {
               super.func_70609_aI();
          }
     }

     public void reset() {
          this.hasDied = false;
          this.field_70128_L = false;
          this.wasKilled = false;
          this.func_70031_b(false);
          this.func_70606_j(this.func_110138_aP());
          this.field_70180_af.func_187227_b(Animation, 0);
          this.field_70180_af.func_187227_b(Walking, false);
          this.field_70180_af.func_187227_b(IsDead, false);
          this.field_70180_af.func_187227_b(Interacting, false);
          this.interactingEntities.clear();
          this.combatHandler.reset();
          this.func_70624_b((EntityLivingBase)null);
          this.func_70604_c((EntityLivingBase)null);
          this.field_70725_aQ = 0;
          if (this.ais.returnToStart && !this.hasOwner() && !this.isRemote() && !this.func_184218_aH()) {
               this.func_70012_b((double)this.getStartXPos(), this.getStartYPos(), (double)this.getStartZPos(), this.field_70177_z, this.field_70125_A);
          }

          this.killedtime = 0L;
          this.func_70066_B();
          this.func_70674_bp();
          this.func_191986_a(0.0F, 0.0F, 0.0F);
          this.field_70140_Q = 0.0F;
          this.func_70661_as().func_75499_g();
          this.currentAnimation = 0;
          this.updateHitbox();
          this.updateAI = true;
          this.ais.movingPos = 0;
          if (this.getOwner() != null) {
               this.getOwner().func_130011_c((Entity)null);
          }

          this.bossInfo.func_186758_d(this.display.getBossbar() == 1);
          if (this.jobInterface != null) {
               this.jobInterface.reset();
          }

          EventHooks.onNPCInit(this);
     }

     public void onCollide() {
          if (this.func_70089_S() && this.field_70173_aa % 4 == 0 && !this.world.field_72995_K) {
               AxisAlignedBB axisalignedbb = null;
               if (this.func_184187_bx() != null && this.func_184187_bx().func_70089_S()) {
                    axisalignedbb = this.func_174813_aQ().func_111270_a(this.func_184187_bx().func_174813_aQ()).func_72314_b(1.0D, 0.0D, 1.0D);
               } else {
                    axisalignedbb = this.func_174813_aQ().func_72314_b(1.0D, 0.5D, 1.0D);
               }

               List list = this.world.func_72872_a(EntityLivingBase.class, axisalignedbb);
               if (list != null) {
                    for(int i = 0; i < list.size(); ++i) {
                         Entity entity = (Entity)list.get(i);
                         if (entity != this && entity.func_70089_S()) {
                              EventHooks.onNPCCollide(this, entity);
                         }
                    }

               }
          }
     }

     public void func_181015_d(BlockPos pos) {
     }

     public void cloakUpdate() {
          this.field_20066_r = this.field_20063_u;
          this.field_20065_s = this.field_20062_v;
          this.field_20064_t = this.field_20061_w;
          double d = this.field_70165_t - this.field_20063_u;
          double d1 = this.field_70163_u - this.field_20062_v;
          double d2 = this.field_70161_v - this.field_20061_w;
          double d3 = 10.0D;
          if (d > d3) {
               this.field_20066_r = this.field_20063_u = this.field_70165_t;
          }

          if (d2 > d3) {
               this.field_20064_t = this.field_20061_w = this.field_70161_v;
          }

          if (d1 > d3) {
               this.field_20065_s = this.field_20062_v = this.field_70163_u;
          }

          if (d < -d3) {
               this.field_20066_r = this.field_20063_u = this.field_70165_t;
          }

          if (d2 < -d3) {
               this.field_20064_t = this.field_20061_w = this.field_70161_v;
          }

          if (d1 < -d3) {
               this.field_20065_s = this.field_20062_v = this.field_70163_u;
          }

          this.field_20063_u += d * 0.25D;
          this.field_20061_w += d2 * 0.25D;
          this.field_20062_v += d1 * 0.25D;
     }

     protected boolean func_70692_ba() {
          return this.stats.spawnCycle == 4;
     }

     public ItemStack func_184614_ca() {
          IItemStack item = null;
          if (this.isAttacking()) {
               item = this.inventory.getRightHand();
          } else if (this.advanced.role == 6) {
               item = ((RoleCompanion)this.roleInterface).getHeldItem();
          } else if (this.jobInterface != null && this.jobInterface.overrideMainHand) {
               item = this.jobInterface.getMainhand();
          } else {
               item = this.inventory.getRightHand();
          }

          return ItemStackWrapper.MCItem(item);
     }

     public ItemStack func_184592_cb() {
          IItemStack item = null;
          if (this.isAttacking()) {
               item = this.inventory.getLeftHand();
          } else if (this.jobInterface != null && this.jobInterface.overrideOffHand) {
               item = this.jobInterface.getOffhand();
          } else {
               item = this.inventory.getLeftHand();
          }

          return ItemStackWrapper.MCItem(item);
     }

     public ItemStack func_184582_a(EntityEquipmentSlot slot) {
          if (slot == EntityEquipmentSlot.MAINHAND) {
               return this.func_184614_ca();
          } else {
               return slot == EntityEquipmentSlot.OFFHAND ? this.func_184592_cb() : ItemStackWrapper.MCItem(this.inventory.getArmor(3 - slot.func_188454_b()));
          }
     }

     public void func_184201_a(EntityEquipmentSlot slot, ItemStack item) {
          if (slot == EntityEquipmentSlot.MAINHAND) {
               this.inventory.weapons.put(0, NpcAPI.Instance().getIItemStack(item));
          } else if (slot == EntityEquipmentSlot.OFFHAND) {
               this.inventory.weapons.put(2, NpcAPI.Instance().getIItemStack(item));
          } else {
               this.inventory.armor.put(3 - slot.func_188454_b(), NpcAPI.Instance().getIItemStack(item));
          }

     }

     public Iterable func_184193_aE() {
          ArrayList list = new ArrayList();

          for(int i = 0; i < 4; ++i) {
               list.add(ItemStackWrapper.MCItem((IItemStack)this.inventory.armor.get(3 - i)));
          }

          return list;
     }

     public Iterable func_184214_aD() {
          ArrayList list = new ArrayList();
          list.add(ItemStackWrapper.MCItem((IItemStack)this.inventory.weapons.get(0)));
          list.add(ItemStackWrapper.MCItem((IItemStack)this.inventory.weapons.get(2)));
          return list;
     }

     protected void func_82160_b(boolean wasRecentlyHit, int lootingModifier) {
     }

     protected void func_70628_a(boolean wasRecentlyHit, int lootingModifier) {
     }

     public void func_70645_a(DamageSource damagesource) {
          this.func_70031_b(false);
          this.func_70661_as().func_75499_g();
          this.func_70066_B();
          this.func_70674_bp();
          if (!this.isRemote()) {
               this.advanced.playSound(3, this.func_70599_aP(), this.func_70647_i());
               Entity attackingEntity = NoppesUtilServer.GetDamageSourcee(damagesource);
               NpcEvent.DiedEvent event = new NpcEvent.DiedEvent(this.wrappedNPC, damagesource, attackingEntity);
               event.droppedItems = this.inventory.getItemsRNG();
               event.expDropped = this.inventory.getExpRNG();
               event.line = this.advanced.getKilledLine();
               EventHooks.onNPCDied(this, event);
               this.bossInfo.func_186758_d(false);
               this.inventory.dropStuff(event, attackingEntity, damagesource);
               if (event.line != null) {
                    this.saySurrounding(Line.formatTarget((Line)event.line, attackingEntity instanceof EntityLivingBase ? (EntityLivingBase)attackingEntity : null));
               }
          }

          super.func_70645_a(damagesource);
     }

     public void func_184178_b(EntityPlayerMP player) {
          super.func_184178_b(player);
          this.bossInfo.func_186760_a(player);
     }

     public void func_184203_c(EntityPlayerMP player) {
          super.func_184203_c(player);
          this.bossInfo.func_186761_b(player);
     }

     public void func_70106_y() {
          this.hasDied = true;
          this.func_184226_ay();
          this.func_184210_p();
          if (!this.world.field_72995_K && this.stats.spawnCycle != 3 && this.stats.spawnCycle != 4) {
               this.func_70606_j(-1.0F);
               this.func_70031_b(false);
               this.func_70661_as().func_75499_g();
               this.setCurrentAnimation(2);
               this.updateHitbox();
               if (this.killedtime <= 0L) {
                    this.killedtime = (long)(this.stats.respawnTime * 1000) + System.currentTimeMillis();
               }

               if (this.advanced.role != 0 && this.roleInterface != null) {
                    this.roleInterface.killed();
               }

               if (this.advanced.job != 0 && this.jobInterface != null) {
                    this.jobInterface.killed();
               }
          } else {
               this.delete();
          }

     }

     public void delete() {
          if (this.advanced.role != 0 && this.roleInterface != null) {
               this.roleInterface.delete();
          }

          if (this.advanced.job != 0 && this.jobInterface != null) {
               this.jobInterface.delete();
          }

          super.func_70106_y();
     }

     public float getStartXPos() {
          return (float)this.ais.startPos().func_177958_n() + this.ais.bodyOffsetX / 10.0F;
     }

     public float getStartZPos() {
          return (float)this.ais.startPos().func_177952_p() + this.ais.bodyOffsetZ / 10.0F;
     }

     public boolean isVeryNearAssignedPlace() {
          double xx = this.field_70165_t - (double)this.getStartXPos();
          double zz = this.field_70161_v - (double)this.getStartZPos();
          if (xx >= -0.2D && xx <= 0.2D) {
               return zz >= -0.2D && zz <= 0.2D;
          } else {
               return false;
          }
     }

     public double getStartYPos() {
          return this.startYPos < 0.0D ? this.calculateStartYPos(this.ais.startPos()) : this.startYPos;
     }

     private double calculateStartYPos(BlockPos pos) {
          BlockPos startPos = this.ais.startPos();

          while(true) {
               while(pos.func_177956_o() > 0) {
                    IBlockState state = this.world.func_180495_p(pos);
                    AxisAlignedBB bb = state.func_185900_c(this.world, pos).func_186670_a(pos);
                    if (bb != null) {
                         if (this.ais.movementType != 2 || startPos.func_177956_o() > pos.func_177956_o() || state.func_185904_a() != Material.field_151586_h) {
                              return bb.field_72337_e;
                         }

                         pos = pos.func_177977_b();
                    } else {
                         pos = pos.func_177977_b();
                    }
               }

               return 0.0D;
          }
     }

     private BlockPos calculateTopPos(BlockPos pos) {
          for(BlockPos check = pos; check.func_177956_o() > 0; check = check.func_177977_b()) {
               IBlockState state = this.world.func_180495_p(pos);
               AxisAlignedBB bb = state.func_185900_c(this.world, pos).func_186670_a(pos);
               if (bb != null) {
                    return check;
               }
          }

          return pos;
     }

     public boolean isInRange(Entity entity, double range) {
          return this.isInRange(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, range);
     }

     public boolean isInRange(double posX, double posY, double posZ, double range) {
          double y = Math.abs(this.field_70163_u - posY);
          if (posY >= 0.0D && y > range) {
               return false;
          } else {
               double x = Math.abs(this.field_70165_t - posX);
               double z = Math.abs(this.field_70161_v - posZ);
               return x <= range && z <= range;
          }
     }

     public void givePlayerItem(EntityPlayer player, ItemStack item) {
          if (!this.world.field_72995_K) {
               item = item.func_77946_l();
               float f = 0.7F;
               double d = (double)(this.world.field_73012_v.nextFloat() * f) + (double)(1.0F - f);
               double d1 = (double)(this.world.field_73012_v.nextFloat() * f) + (double)(1.0F - f);
               double d2 = (double)(this.world.field_73012_v.nextFloat() * f) + (double)(1.0F - f);
               EntityItem entityitem = new EntityItem(this.world, this.field_70165_t + d, this.field_70163_u + d1, this.field_70161_v + d2, item);
               entityitem.func_174867_a(2);
               this.world.func_72838_d(entityitem);
               int i = item.func_190916_E();
               if (player.inventory.func_70441_a(item)) {
                    this.world.func_184148_a((EntityPlayer)null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187638_cR, SoundCategory.PLAYERS, 0.2F, ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    player.func_71001_a(entityitem, i);
                    if (item.func_190916_E() <= 0) {
                         entityitem.func_70106_y();
                    }
               }

          }
     }

     public boolean func_70608_bn() {
          return this.currentAnimation == 2 && !this.isAttacking();
     }

     public boolean isWalking() {
          return this.ais.getMovingType() != 0 || this.isAttacking() || this.isFollower() || (Boolean)this.field_70180_af.func_187225_a(Walking);
     }

     public boolean func_70093_af() {
          return this.currentAnimation == 4;
     }

     public void func_70653_a(Entity par1Entity, float strength, double ratioX, double ratioZ) {
          super.func_70653_a(par1Entity, strength * (2.0F - this.stats.resistances.knockback), ratioX, ratioZ);
     }

     public Faction getFaction() {
          Faction fac = FactionController.instance.getFaction((Integer)this.field_70180_af.func_187225_a(FactionData));
          return fac == null ? FactionController.instance.getFaction(FactionController.instance.getFirstFactionId()) : fac;
     }

     public boolean isRemote() {
          return this.world == null || this.world.field_72995_K;
     }

     public void setFaction(int id) {
          if (id >= 0 && !this.isRemote()) {
               this.field_70180_af.func_187227_b(FactionData, id);
          }
     }

     public boolean func_70687_e(PotionEffect effect) {
          if (this.stats.potionImmune) {
               return false;
          } else {
               return this.func_70668_bt() == EnumCreatureAttribute.ARTHROPOD && effect.func_188419_a() == MobEffects.field_76436_u ? false : super.func_70687_e(effect);
          }
     }

     public boolean isAttacking() {
          return (Boolean)this.field_70180_af.func_187225_a(Attacking);
     }

     public boolean isKilled() {
          return this.field_70128_L || (Boolean)this.field_70180_af.func_187225_a(IsDead);
     }

     public void writeSpawnData(ByteBuf buffer) {
          try {
               Server.writeNBT(buffer, this.writeSpawnData());
          } catch (IOException var3) {
               var3.printStackTrace();
          }

     }

     public NBTTagCompound writeSpawnData() {
          NBTTagCompound compound = new NBTTagCompound();
          this.display.writeToNBT(compound);
          compound.setInteger("MaxHealth", this.stats.maxHealth);
          compound.setTag("Armor", NBTTags.nbtIItemStackMap(this.inventory.armor));
          compound.setTag("Weapons", NBTTags.nbtIItemStackMap(this.inventory.weapons));
          compound.setInteger("Speed", this.ais.getWalkingSpeed());
          compound.func_74757_a("DeadBody", this.stats.hideKilledBody);
          compound.setInteger("StandingState", this.ais.getStandingType());
          compound.setInteger("MovingState", this.ais.getMovingType());
          compound.setInteger("Orientation", this.ais.orientation);
          compound.func_74776_a("PositionXOffset", this.ais.bodyOffsetX);
          compound.func_74776_a("PositionYOffset", this.ais.bodyOffsetY);
          compound.func_74776_a("PositionZOffset", this.ais.bodyOffsetZ);
          compound.setInteger("Role", this.advanced.role);
          compound.setInteger("Job", this.advanced.job);
          NBTTagCompound bard;
          if (this.advanced.job == 1) {
               bard = new NBTTagCompound();
               this.jobInterface.writeToNBT(bard);
               compound.setTag("Bard", bard);
          }

          if (this.advanced.job == 9) {
               bard = new NBTTagCompound();
               this.jobInterface.writeToNBT(bard);
               compound.setTag("Puppet", bard);
          }

          if (this.advanced.role == 6) {
               bard = new NBTTagCompound();
               this.roleInterface.writeToNBT(bard);
               compound.setTag("Companion", bard);
          }

          if (this instanceof EntityCustomNpc) {
               compound.setTag("ModelData", ((EntityCustomNpc)this).modelData.writeToNBT());
          }

          return compound;
     }

     public void readSpawnData(ByteBuf buf) {
          try {
               this.readSpawnData(Server.readNBT(buf));
          } catch (IOException var3) {
          }

     }

     public void readSpawnData(NBTTagCompound compound) {
          this.stats.setMaxHealth(compound.func_74762_e("MaxHealth"));
          this.ais.setWalkingSpeed(compound.func_74762_e("Speed"));
          this.stats.hideKilledBody = compound.getBoolean("DeadBody");
          this.ais.setStandingType(compound.func_74762_e("StandingState"));
          this.ais.setMovingType(compound.func_74762_e("MovingState"));
          this.ais.orientation = compound.func_74762_e("Orientation");
          this.ais.bodyOffsetX = compound.func_74760_g("PositionXOffset");
          this.ais.bodyOffsetY = compound.func_74760_g("PositionYOffset");
          this.ais.bodyOffsetZ = compound.func_74760_g("PositionZOffset");
          this.inventory.armor = NBTTags.getIItemStackMap(compound.getTagList("Armor", 10));
          this.inventory.weapons = NBTTags.getIItemStackMap(compound.getTagList("Weapons", 10));
          this.advanced.setRole(compound.func_74762_e("Role"));
          this.advanced.setJob(compound.func_74762_e("Job"));
          NBTTagCompound puppet;
          if (this.advanced.job == 1) {
               puppet = compound.getCompoundTag("Bard");
               this.jobInterface.readFromNBT(puppet);
          }

          if (this.advanced.job == 9) {
               puppet = compound.getCompoundTag("Puppet");
               this.jobInterface.readFromNBT(puppet);
          }

          if (this.advanced.role == 6) {
               puppet = compound.getCompoundTag("Companion");
               this.roleInterface.readFromNBT(puppet);
          }

          if (this instanceof EntityCustomNpc) {
               ((EntityCustomNpc)this).modelData.readFromNBT(compound.getCompoundTag("ModelData"));
          }

          this.display.readToNBT(compound);
     }

     public Entity func_174793_f() {
          if (this.world.field_72995_K) {
               return this;
          } else {
               EntityUtil.Copy(this, CommandPlayer);
               CommandPlayer.func_70029_a(this.world);
               CommandPlayer.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
               return CommandPlayer;
          }
     }

     public String func_70005_c_() {
          return this.display.getName();
     }

     public BlockPos func_180425_c() {
          return new BlockPos(this.field_70165_t, this.field_70163_u, this.field_70161_v);
     }

     public Vec3d func_174791_d() {
          return new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v);
     }

     public boolean func_70686_a(Class par1Class) {
          return EntityBat.class != par1Class;
     }

     public void setImmuneToFire(boolean immuneToFire) {
          this.field_70178_ae = immuneToFire;
          this.stats.immuneToFire = immuneToFire;
     }

     public void func_180430_e(float distance, float modifier) {
          if (!this.stats.noFallDamage) {
               super.func_180430_e(distance, modifier);
          }

     }

     public void func_70110_aj() {
          if (!this.stats.ignoreCobweb) {
               super.func_70110_aj();
          }

     }

     public boolean func_70067_L() {
          return !this.isKilled() && this.display.getHasHitbox();
     }

     public boolean func_70104_M() {
          return super.func_70104_M() && this.display.getHasHitbox();
     }

     public EnumPushReaction func_184192_z() {
          return this.display.getHasHitbox() ? super.func_184192_z() : EnumPushReaction.IGNORE;
     }

     public EntityAIRangedAttack getRangedTask() {
          return this.aiRange;
     }

     public String getRoleData() {
          return (String)this.field_70180_af.func_187225_a(RoleData);
     }

     public void setRoleData(String s) {
          this.field_70180_af.func_187227_b(RoleData, s);
     }

     public String getJobData() {
          return (String)this.field_70180_af.func_187225_a(RoleData);
     }

     public void setJobData(String s) {
          this.field_70180_af.func_187227_b(RoleData, s);
     }

     public World func_130014_f_() {
          return this.world;
     }

     public boolean func_98034_c(EntityPlayer player) {
          return this.display.getVisible() == 1 && (player.func_184614_ca().func_190926_b() || player.func_184614_ca().func_77973_b() != CustomItems.wand);
     }

     public boolean func_82150_aj() {
          return this.display.getVisible() != 0;
     }

     public void func_145747_a(ITextComponent var1) {
     }

     public void setCurrentAnimation(int animation) {
          this.currentAnimation = animation;
          this.field_70180_af.func_187227_b(Animation, animation);
     }

     public boolean canSee(Entity entity) {
          return this.func_70635_at().func_75522_a(entity);
     }

     public boolean isFollower() {
          if (this.advanced.scenes.getOwner() != null) {
               return true;
          } else {
               return this.roleInterface != null && this.roleInterface.isFollowing() || this.jobInterface != null && this.jobInterface.isFollowing();
          }
     }

     public EntityLivingBase getOwner() {
          if (this.advanced.scenes.getOwner() != null) {
               return this.advanced.scenes.getOwner();
          } else if (this.advanced.role == 2 && this.roleInterface instanceof RoleFollower) {
               return ((RoleFollower)this.roleInterface).owner;
          } else if (this.advanced.role == 6 && this.roleInterface instanceof RoleCompanion) {
               return ((RoleCompanion)this.roleInterface).owner;
          } else {
               return this.advanced.job == 5 && this.jobInterface instanceof JobFollower ? ((JobFollower)this.jobInterface).following : null;
          }
     }

     public boolean hasOwner() {
          if (this.advanced.scenes.getOwner() != null) {
               return true;
          } else {
               return this.advanced.role == 2 && ((RoleFollower)this.roleInterface).hasOwner() || this.advanced.role == 6 && ((RoleCompanion)this.roleInterface).hasOwner() || this.advanced.job == 5 && ((JobFollower)this.jobInterface).hasOwner();
          }
     }

     public int followRange() {
          if (this.advanced.scenes.getOwner() != null) {
               return 4;
          } else if (this.advanced.role == 2 && this.roleInterface.isFollowing()) {
               return 6;
          } else if (this.advanced.role == 6 && this.roleInterface.isFollowing()) {
               return 4;
          } else {
               return this.advanced.job == 5 && this.jobInterface.isFollowing() ? 4 : 15;
          }
     }

     public void func_175449_a(BlockPos pos, int range) {
          super.func_175449_a(pos, range);
          this.ais.setStartPos(pos);
     }

     protected float func_70655_b(DamageSource source, float damage) {
          if (this.advanced.role == 6) {
               damage = ((RoleCompanion)this.roleInterface).applyArmorCalculations(source, damage);
          }

          return damage;
     }

     public boolean func_184191_r(Entity entity) {
          if (!this.isRemote()) {
               if (entity instanceof EntityPlayer && this.getFaction().isFriendlyToPlayer((EntityPlayer)entity)) {
                    return true;
               }

               if (entity == this.getOwner()) {
                    return true;
               }

               if (entity instanceof EntityNPCInterface && ((EntityNPCInterface)entity).faction.id == this.faction.id) {
                    return true;
               }
          }

          return super.func_184191_r(entity);
     }

     public void setDataWatcher(EntityDataManager dataManager) {
          this.field_70180_af = dataManager;
     }

     public void func_191986_a(float f1, float f2, float f3) {
          double d0 = this.field_70165_t;
          double d1 = this.field_70163_u;
          double d2 = this.field_70161_v;
          super.func_191986_a(f1, f2, f3);
          if (this.advanced.role == 6 && !this.isRemote()) {
               ((RoleCompanion)this.roleInterface).addMovementStat(this.field_70165_t - d0, this.field_70163_u - d1, this.field_70161_v - d2);
          }

     }

     public boolean func_184652_a(EntityPlayer player) {
          return false;
     }

     public boolean func_110167_bD() {
          return false;
     }

     public boolean nearPosition(BlockPos pos) {
          BlockPos npcpos = this.func_180425_c();
          float x = (float)(npcpos.func_177958_n() - pos.func_177958_n());
          float z = (float)(npcpos.func_177952_p() - pos.func_177952_p());
          float y = (float)(npcpos.func_177956_o() - pos.func_177956_o());
          float height = (float)(MathHelper.func_76123_f(this.height + 1.0F) * MathHelper.func_76123_f(this.height + 1.0F));
          return (double)(x * x + z * z) < 2.5D && (double)(y * y) < (double)height + 2.5D;
     }

     public void tpTo(EntityLivingBase owner) {
          if (owner != null) {
               EnumFacing facing = owner.func_174811_aO().func_176734_d();
               BlockPos pos = new BlockPos(owner.field_70165_t, owner.func_174813_aQ().field_72338_b, owner.field_70161_v);
               pos = pos.func_177982_a(facing.getFrontOffsetX(), 0, facing.getFrontOffsetZ());
               pos = this.calculateTopPos(pos);

               for(int i = -1; i < 2; ++i) {
                    for(int j = 0; j < 3; ++j) {
                         BlockPos check;
                         if (facing.getFrontOffsetX() == 0) {
                              check = pos.func_177982_a(i, 0, j * facing.getFrontOffsetZ());
                         } else {
                              check = pos.func_177982_a(j * facing.getFrontOffsetX(), 0, i);
                         }

                         check = this.calculateTopPos(check);
                         if (!this.world.func_180495_p(check).func_185913_b() && !this.world.func_180495_p(check.func_177984_a()).func_185913_b()) {
                              this.func_70012_b((double)((float)check.func_177958_n() + 0.5F), (double)check.func_177956_o(), (double)((float)check.func_177952_p() + 0.5F), this.field_70177_z, this.field_70125_A);
                              this.func_70661_as().func_75499_g();
                              break;
                         }
                    }
               }

          }
     }

     public int func_70641_bl() {
          return 8;
     }

     public void func_184724_a(boolean swingingArms) {
     }

     public boolean func_70601_bi() {
          return this.func_180484_a(new BlockPos(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v)) >= 0.0F && this.world.func_180495_p((new BlockPos(this)).func_177977_b()).func_189884_a(this);
     }

     public boolean shouldDismountInWater(Entity rider) {
          return false;
     }

     static {
          Attacking = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187198_h);
          Animation = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187192_b);
          RoleData = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187194_d);
          JobData = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187194_d);
          FactionData = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187192_b);
          Walking = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187198_h);
          Interacting = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187198_h);
          IsDead = EntityDataManager.func_187226_a(EntityNPCInterface.class, DataSerializers.field_187198_h);
          CommandProfile = new GameProfileAlt();
          ChatEventProfile = new GameProfileAlt();
          GenericProfile = new GameProfileAlt();
     }
}
