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
import net.minecraft.pathfinding.PathWorldListener;
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
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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

public abstract class EntityNPCInterface extends EntityCreature
		implements IEntityAdditionalSpawnData, ICommandSender, IRangedAttackMob, IAnimals {
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
		this.bossInfo = new BossInfoServer(this.getDisplayName(), Color.PURPLE, Overlay.PROGRESS);
		this.startYPos = -1.0D;
		if (!this.isRemote()) {
			this.wrappedNPC = new NPCWrapper(this);
		}

		this.dialogs = new HashMap();
		if (!CustomNpcs.DefaultInteractLine.isEmpty()) {
			this.advanced.interactLines.lines.put(0, new Line(CustomNpcs.DefaultInteractLine));
		}

		this.experienceValue = 0;
		this.scaleX = this.scaleY = this.scaleZ = 0.9375F;
		this.faction = this.getFaction();
		this.setFaction(this.faction.id);
		this.setSize(1.0F, 1.0F);
		this.updateAI = true;
		this.bossInfo.setVisible(false);
	}

	public boolean canBreatheUnderwater() {
		return this.ais.movementType == 2;
	}

	public boolean isPushedByWater() {
		return this.ais.movementType != 2;
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.abilities = new DataAbilities(this);
		this.display = new DataDisplay(this);
		this.stats = new DataStats(this);
		this.ais = new DataAI(this);
		this.advanced = new DataAdvanced(this);
		this.inventory = new DataInventory(this);
		this.transform = new DataTransform(this);
		this.script = new DataScript(this);
		this.timers = new DataTimers(this);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double) this.stats.maxHealth);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue((double) CustomNpcs.NpcNavRange);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) this.getSpeed());
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
				.setBaseValue((double) this.stats.melee.getStrength());
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue((double) (this.getSpeed() * 2.0F));
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(RoleData, String.valueOf(""));
		this.dataManager.register(JobData, String.valueOf(""));
		this.dataManager.register(FactionData, 0);
		this.dataManager.register(Animation, 0);
		this.dataManager.register(Walking, false);
		this.dataManager.register(Interacting, false);
		this.dataManager.register(IsDead, false);
		this.dataManager.register(Attacking, false);
	}

	public boolean isEntityAlive() {
		return super.isEntityAlive() && !this.isKilled();
	}

	public void onUpdate() {
		super.onUpdate();
		if (this.ticksExisted % 10 == 0) {
			this.startYPos = this.calculateStartYPos(this.ais.startPos()) + 1.0D;
			if (this.startYPos < 0.0D && !this.isRemote()) {
				this.setDead();
			}

			EventHooks.onNPCTick(this);
		}

		this.timers.update();
		if (this.world.isRemote && this.wasKilled != this.isKilled()) {
			this.deathTime = 0;
			this.updateHitbox();
		}

		this.wasKilled = this.isKilled();
		if (this.currentAnimation == 14) {
			this.deathTime = 19;
		}

	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		float f = (float) this.stats.melee.getStrength();
		if (this.stats.melee.getDelay() < 10) {
			par1Entity.hurtResistantTime = 0;
		}

		if (par1Entity instanceof EntityLivingBase) {
			NpcEvent.MeleeAttackEvent event = new NpcEvent.MeleeAttackEvent(this.wrappedNPC,
					(EntityLivingBase) par1Entity, f);
			if (EventHooks.onNPCAttacksMelee(this, event)) {
				return false;
			}

			f = event.damage;
		}

		boolean var4 = par1Entity.attackEntityFrom(new NpcDamageSource("mob", this), f);
		if (var4) {
			if (this.getOwner() instanceof EntityPlayer) {
				EntityUtil.setRecentlyHit((EntityLivingBase) par1Entity);
			}

			if (this.stats.melee.getKnockback() > 0) {
				par1Entity.addVelocity(
						(double) (-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F)
								* (float) this.stats.melee.getKnockback() * 0.5F),
						0.1D, (double) (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F)
								* (float) this.stats.melee.getKnockback() * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			if (this.advanced.role == 6) {
				((RoleCompanion) this.roleInterface).attackedEntity(par1Entity);
			}
		}

		if (this.stats.melee.getEffectType() != 0) {
			if (this.stats.melee.getEffectType() != 1) {
				((EntityLivingBase) par1Entity)
						.addPotionEffect(new PotionEffect(PotionEffectType.getMCType(this.stats.melee.getEffectType()),
								this.stats.melee.getEffectTime() * 20, this.stats.melee.getEffectStrength()));
			} else {
				par1Entity.setFire(this.stats.melee.getEffectTime());
			}
		}

		return var4;
	}

	public void onLivingUpdate() {
		if (!CustomNpcs.FreezeNPCs) {
			if (this.isAIDisabled()) {
				super.onLivingUpdate();
			} else {
				++this.totalTicksAlive;
				this.updateArmSwingProgress();
				if (this.ticksExisted % 20 == 0) {
					this.faction = this.getFaction();
				}

				if (!this.world.isRemote) {
					if (!this.isKilled() && this.ticksExisted % 20 == 0) {
						this.advanced.scenes.update();
						if (this.getHealth() < this.getMaxHealth()) {
							if (this.stats.healthRegen > 0 && !this.isAttacking()) {
								this.heal((float) this.stats.healthRegen);
							}

							if (this.stats.combatRegen > 0 && this.isAttacking()) {
								this.heal((float) this.stats.combatRegen);
							}
						}

						if (this.faction.getsAttacked && !this.isAttacking()) {
							List list = this.world.getEntitiesWithinAABB(EntityMob.class,
									this.getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D));
							Iterator var2 = list.iterator();

							while (var2.hasNext()) {
								EntityMob mob = (EntityMob) var2.next();
								if (mob.getAttackTarget() == null && this.canSee(mob)) {
									mob.setAttackTarget(this);
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

					if (this.getHealth() <= 0.0F && !this.isKilled()) {
						this.clearActivePotions();
						this.dataManager.set(IsDead, true);
						this.updateTasks();
						this.updateHitbox();
					}

					if (this.display.getBossbar() == 2) {
						this.bossInfo.setVisible(this.getAttackTarget() != null);
					}

					this.dataManager.set(Walking, !this.getNavigator().noPath());
					this.dataManager.set(Interacting, this.isInteracting());
					this.combatHandler.update();
					this.onCollide();
				}

				if (this.wasKilled != this.isKilled() && this.wasKilled) {
					this.reset();
				}

				if (this.world.isDaytime() && !this.world.isRemote && this.stats.burnInSun) {
					float f = this.getBrightness();
					if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F
							&& this.world.canBlockSeeSky(new BlockPos(this))) {
						this.setFire(8);
					}
				}

				super.onLivingUpdate();
				if (this.world.isRemote) {
					if (this.roleInterface != null) {
						this.roleInterface.clientUpdate();
					}

					if (this.textureCloakLocation != null) {
						this.cloakUpdate();
					}

					if (this.currentAnimation != (Integer) this.dataManager.get(Animation)) {
						this.currentAnimation = (Integer) this.dataManager.get(Animation);
						this.animationStart = this.ticksExisted;
						this.updateHitbox();
					}

					if (this.advanced.job == 1) {
						((JobBard) this.jobInterface).onLivingUpdate();
					}
				}

				if (this.display.getBossbar() > 0) {
					this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
				}

			}
		}
	}

	public void updateClient() {
		NBTTagCompound compound = this.writeSpawnData();
		compound.setInteger("EntityId", this.getEntityId());
		Server.sendAssociatedData(this, EnumPacketClient.UPDATE_NPC, compound);
		this.updateClient = false;
	}

	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);
		this.combatHandler.damage(damageSrc, damageAmount);
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (this.world.isRemote) {
			return !this.isAttacking();
		} else if (hand != EnumHand.MAIN_HAND) {
			return true;
		} else {
			ItemStack stack = player.getHeldItem(hand);
			if (stack != null) {
				Item item = stack.getItem();
				if (item == CustomItems.cloner || item == CustomItems.wand || item == CustomItems.mount
						|| item == CustomItems.scripter) {
					this.setAttackTarget((EntityLivingBase) null);
					this.setRevengeTarget((EntityLivingBase) null);
					return true;
				}

				if (item == CustomItems.moving) {
					this.setAttackTarget((EntityLivingBase) null);
					stack.setTagInfo("NPCID", new NBTTagInt(this.getEntityId()));
					player.sendMessage(new TextComponentTranslation(
							"Registered " + this.getName() + " to your NPC Pather", new Object[0]));
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
					Server.sendData((EntityPlayerMP) player, EnumPacketClient.QUEST_COMPLETION, data.quest.id);
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
		if (this.ais.stopAndInteract && !this.isAttacking() && entity.isEntityAlive() && !this.isAIDisabled()) {
			if (this.ticksExisted - this.lastInteract < 180) {
				this.interactingEntities.clear();
			}

			this.getNavigator().clearPath();
			this.lastInteract = this.ticksExisted;
			if (!this.interactingEntities.contains(entity)) {
				this.interactingEntities.add(entity);
			}

		}
	}

	public boolean isInteracting() {
		if (this.ticksExisted - this.lastInteract < 40
				|| this.isRemote() && (Boolean) this.dataManager.get(Interacting)) {
			return true;
		} else {
			return this.ais.stopAndInteract && !this.interactingEntities.isEmpty()
					&& this.ticksExisted - this.lastInteract < 180;
		}
	}

	private Dialog getDialog(EntityPlayer player) {
		Iterator var2 = this.dialogs.values().iterator();

		while (var2.hasNext()) {
			DialogOption option = (DialogOption) var2.next();
			if (option != null && option.hasDialog()) {
				Dialog dialog = option.getDialog();
				if (dialog.availability.isAvailable(player)) {
					return dialog;
				}
			}
		}

		return null;
	}

	public boolean attackEntityFrom(DamageSource damagesource, float i) {
		if (!this.world.isRemote && !CustomNpcs.FreezeNPCs && !damagesource.damageType.equals("inWall")) {
			if (damagesource.damageType.equals("outOfWorld") && this.isKilled()) {
				this.reset();
			}

			i = this.stats.resistances.applyResistance(damagesource, i);
			if ((float) this.hurtResistantTime > (float) this.maxHurtResistantTime / 2.0F && i <= this.lastDamage) {
				return false;
			} else {
				Entity entity = NoppesUtilServer.GetDamageSourcee(damagesource);
				EntityLivingBase attackingEntity = null;
				if (entity instanceof EntityLivingBase) {
					attackingEntity = (EntityLivingBase) entity;
				}

				if (attackingEntity != null && attackingEntity == this.getOwner()) {
					return false;
				} else {
					if (attackingEntity instanceof EntityNPCInterface) {
						EntityNPCInterface npc = (EntityNPCInterface) attackingEntity;
						if (npc.faction.id == this.faction.id) {
							return false;
						}

						if (npc.getOwner() instanceof EntityPlayer) {
							this.recentlyHit = 100;
						}
					} else if (attackingEntity instanceof EntityPlayer
							&& this.faction.isFriendlyToPlayer((EntityPlayer) attackingEntity)) {
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
							return super.attackEntityFrom(damagesource, i);
						} else {
							boolean var13;
							try {
								if (this.isAttacking()) {
									if (this.getAttackTarget() != null && attackingEntity != null
											&& this.getDistanceSq(this.getAttackTarget()) > this
													.getDistanceSq(attackingEntity)) {
										this.setAttackTarget(attackingEntity);
									}

									var13 = super.attackEntityFrom(damagesource, i);
									return var13;
								}

								if (i > 0.0F) {
									List inRange = this.world.getEntitiesWithinAABB(EntityNPCInterface.class,
											this.getEntityBoundingBox().grow(32.0D, 16.0D, 32.0D));
									Iterator var7 = inRange.iterator();

									while (true) {
										if (!var7.hasNext()) {
											this.setAttackTarget(attackingEntity);
											break;
										}

										EntityNPCInterface npc = (EntityNPCInterface) var7.next();
										if (!npc.isKilled() && npc.advanced.defendFaction
												&& npc.faction.id == this.faction.id && (npc.canSee(this)
														|| npc.ais.directLOS || npc.canSee(attackingEntity))) {
											npc.onAttack(attackingEntity);
										}
									}
								}

								var13 = super.attackEntityFrom(damagesource, i);
							} finally {
								if (event.clearTarget) {
									this.setAttackTarget((EntityLivingBase) null);
									this.setRevengeTarget((EntityLivingBase) null);
								}

							}

							return var13;
						}
					}
				}
			}
		} else {
			return false;
		}
	}

	public void onAttack(EntityLivingBase entity) {
		if (entity != null && entity != this && !this.isAttacking() && this.ais.onAttack != 3
				&& entity != this.getOwner()) {
			super.setAttackTarget(entity);
		}
	}

	public void setAttackTarget(EntityLivingBase entity) {
		if ((!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).capabilities.disableDamage)
				&& (entity == null || entity != this.getOwner()) && this.getAttackTarget() != entity) {
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
				Iterator var4 = this.targetTasks.taskEntries.iterator();

				while (var4.hasNext()) {
					EntityAITaskEntry en = (EntityAITaskEntry) var4.next();
					if (en.using) {
						en.using = false;
						en.action.resetTask();
					}
				}

				if (EventHooks.onNPCTargetLost(this, this.getAttackTarget())) {
					return;
				}
			}

			if (entity != null && entity != this && this.ais.onAttack != 3 && !this.isAttacking() && !this.isRemote()) {
				Line line = this.advanced.getAttackLine();
				if (line != null) {
					this.saySurrounding(Line.formatTarget(line, entity));
				}
			}

			super.setAttackTarget(entity);
		}
	}

	public void attackEntityWithRangedAttack(EntityLivingBase entity, float f) {
		ItemStack proj = ItemStackWrapper.MCItem(this.inventory.getProjectile());
		if (proj == null) {
			this.updateAI = true;
		} else {
			NpcEvent.RangedLaunchedEvent event = new NpcEvent.RangedLaunchedEvent(this.wrappedNPC, entity,
					(float) this.stats.ranged.getStrength());

			for (int i = 0; i < this.stats.ranged.getShotCount(); ++i) {
				EntityProjectile projectile = this.shoot(entity, this.stats.ranged.getAccuracy(), proj, f == 1.0F);
				projectile.damage = event.damage;
				projectile.callback = (projectile1, pos, entity1) -> {
					if (proj.getItem() == CustomItems.soulstoneFull) {
						Entity e = ItemSoulstoneFilled.Spawn((EntityPlayer) null, proj, this.world, pos);
						if (e instanceof EntityLivingBase && entity1 instanceof EntityLivingBase) {
							if (e instanceof EntityLiving) {
								((EntityLiving) e).setAttackTarget((EntityLivingBase) entity1);
							} else {
								((EntityLivingBase) e).setRevengeTarget((EntityLivingBase) entity1);
							}
						}
					}

					projectile1.playSound(this.stats.ranged.getSoundEvent(entity1 != null ? 1 : 2), 1.0F,
							1.2F / (this.getRNG().nextFloat() * 0.2F + 0.9F));
					return false;
				};
				this.playSound(this.stats.ranged.getSoundEvent(0), 2.0F, 1.0F);
				event.projectiles.add((IProjectile) NpcAPI.Instance().getIEntity(projectile));
			}

			EventHooks.onNPCRangedLaunched(this, event);
		}
	}

	public EntityProjectile shoot(EntityLivingBase entity, int accuracy, ItemStack proj, boolean indirect) {
		return this.shoot(entity.posX, entity.getEntityBoundingBox().minY + (double) (entity.height / 2.0F),
				entity.posZ, accuracy, proj, indirect);
	}

	public EntityProjectile shoot(double x, double y, double z, int accuracy, ItemStack proj, boolean indirect) {
		EntityProjectile projectile = new EntityProjectile(this.world, this, proj.copy(), true);
		double varX = x - this.posX;
		double varY = y - (this.posY + (double) this.getEyeHeight());
		double varZ = z - this.posZ;
		float varF = projectile.hasGravity() ? MathHelper.sqrt(varX * varX + varZ * varZ) : 0.0F;
		float angle = projectile.getAngleForXYZ(varX, varY, varZ, (double) varF, indirect);
		float acc = 20.0F - (float) MathHelper.floor((float) accuracy / 5.0F);
		projectile.shoot(varX, varY, varZ, angle, acc);
		this.world.spawnEntity(projectile);
		return projectile;
	}

	private void clearTasks(EntityAITasks tasks) {
		Iterator iterator = tasks.taskEntries.iterator();
		List list = new ArrayList(tasks.taskEntries);
		Iterator var4 = list.iterator();

		while (var4.hasNext()) {
			EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry) var4.next();
			tasks.removeTask(entityaitaskentry.action);
		}

		tasks.taskEntries.clear();
	}

	private void updateTasks() {
		if (this.world != null && !this.world.isRemote) {
			this.clearTasks(this.tasks);
			this.clearTasks(this.targetTasks);
			if (!this.isKilled()) {
				Predicate attackEntitySelector = new NPCAttackSelector(this);
				this.targetTasks.addTask(0, new EntityAIClearTarget(this));
				this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
				this.targetTasks.addTask(2, new EntityAIClosestTarget(this, EntityLivingBase.class, 4,
						this.ais.directLOS, false, attackEntitySelector));
				this.targetTasks.addTask(3, new EntityAIOwnerHurtByTarget(this));
				this.targetTasks.addTask(4, new EntityAIOwnerHurtTarget(this));

				PathWorldListener listener = (PathWorldListener)ObfuscationReflectionHelper.getPrivateValue(World.class, this.world, "pathListener");
				listener.onEntityRemoved(this);
				// this.world.pathListener.onEntityRemoved(this);

				if (this.ais.movementType == 1) {
					this.moveHelper = new FlyingMoveHelper(this);
					this.navigator = new PathNavigateFlying(this, this.world);
				} else if (this.ais.movementType == 2) {
					this.moveHelper = new FlyingMoveHelper(this);
					this.navigator = new PathNavigateSwimmer(this, this.world);
				} else {
					this.moveHelper = new EntityMoveHelper(this);
					this.navigator = new PathNavigateGround(this, this.world);
					this.tasks.addTask(0, new EntityAIWaterNav(this));
				}

				listener.onEntityAdded(this);
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
			this.tasks.addTask(this.taskCount++, new EntityAISprintToTarget(this));
		}

		if (this.ais.onAttack == 1) {
			this.tasks.addTask(this.taskCount++, new EntityAIPanic(this, 1.2F));
		} else if (this.ais.onAttack == 2) {
			this.tasks.addTask(this.taskCount++, new EntityAIAvoidTarget(this));
		} else if (this.ais.onAttack == 0) {
			if (this.ais.canLeap) {
				this.tasks.addTask(this.taskCount++, new EntityAIPounceTarget(this));
			}

			if (this.inventory.getProjectile() == null) {
				switch (this.ais.tacticalVariant) {
				case 1:
					this.tasks.addTask(this.taskCount++, new EntityAIZigZagTarget(this, 1.3D));
					break;
				case 2:
					this.tasks.addTask(this.taskCount++, new EntityAIOrbitTarget(this, 1.3D, true));
					break;
				case 3:
					this.tasks.addTask(this.taskCount++, new EntityAIAvoidTarget(this));
					break;
				case 4:
					this.tasks.addTask(this.taskCount++, new EntityAIAmbushTarget(this, 1.2D));
					break;
				case 5:
					this.tasks.addTask(this.taskCount++, new EntityAIStalkTarget(this));
				}
			} else {
				switch (this.ais.tacticalVariant) {
				case 1:
					this.tasks.addTask(this.taskCount++, new EntityAIDodgeShoot(this));
					break;
				case 2:
					this.tasks.addTask(this.taskCount++, new EntityAIOrbitTarget(this, 1.3D, false));
					break;
				case 3:
					this.tasks.addTask(this.taskCount++, new EntityAIAvoidTarget(this));
					break;
				case 4:
					this.tasks.addTask(this.taskCount++, new EntityAIAmbushTarget(this, 1.3D));
					break;
				case 5:
					this.tasks.addTask(this.taskCount++, new EntityAIStalkTarget(this));
				}
			}

			this.tasks.addTask(this.taskCount, this.aiAttackTarget = new EntityAIAttackTarget(this));
			((EntityAIAttackTarget) this.aiAttackTarget).navOverride(this.ais.tacticalVariant == 6);
			if (this.inventory.getProjectile() != null) {
				this.tasks.addTask(this.taskCount++, this.aiRange = new EntityAIRangedAttack(this));
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
			this.tasks.addTask(this.taskCount++, new EntityAIWander(this));
		}

		if (this.ais.getMovingType() == 2) {
			this.tasks.addTask(this.taskCount++, new EntityAIMovingPath(this));
		}

	}

	public void doorInteractType() {
		if (!this.canFly()) {
			EntityAIBase aiDoor = null;
			if (this.ais.doorInteract == 1) {
				this.tasks.addTask(this.taskCount++, (EntityAIBase) (aiDoor = new EntityAIOpenDoor(this, true)));
			} else if (this.ais.doorInteract == 0) {
				this.tasks.addTask(this.taskCount++, (EntityAIBase) (aiDoor = new EntityAIBustDoor(this)));
			}

			if (this.getNavigator() instanceof PathNavigateGround) {
				((PathNavigateGround) this.getNavigator()).setBreakDoors(aiDoor != null);
			}

		}
	}

	public void seekShelter() {
		if (this.ais.findShelter == 0) {
			this.tasks.addTask(this.taskCount++, new EntityAIMoveIndoors(this));
		} else if (this.ais.findShelter == 1) {
			if (!this.canFly()) {
				this.tasks.addTask(this.taskCount++, new EntityAIRestrictSun(this));
			}

			this.tasks.addTask(this.taskCount++, new EntityAIFindShade(this));
		}

	}

	public void addRegularEntries() {
		this.tasks.addTask(this.taskCount++, new EntityAIReturn(this));
		this.tasks.addTask(this.taskCount++, new EntityAIFollow(this));
		if (this.ais.getStandingType() != 1 && this.ais.getStandingType() != 3) {
			this.tasks.addTask(this.taskCount++, new EntityAIWatchClosest(this, EntityLivingBase.class, 5.0F));
		}

		this.tasks.addTask(this.taskCount++, this.lookAi = new EntityAILook(this));
		this.tasks.addTask(this.taskCount++, new EntityAIWorldLines(this));
		this.tasks.addTask(this.taskCount++, new EntityAIJob(this));
		this.tasks.addTask(this.taskCount++, new EntityAIRole(this));
		this.tasks.addTask(this.taskCount++, this.animateAi = new EntityAIAnimation(this));
		if (this.transform.isValid()) {
			this.tasks.addTask(this.taskCount++, new EntityAITransform(this));
		}

	}

	public float getSpeed() {
		return (float) this.ais.getWalkingSpeed() / 20.0F;
	}

	public float getBlockPathWeight(BlockPos pos) {
		if (this.ais.movementType == 2) {
			return this.world.getBlockState(pos).getMaterial() == Material.WATER ? 10.0F : 0.0F;
		} else {
			float weight = this.world.getLightBrightness(pos) - 0.5F;
			if (this.world.getBlockState(pos).isOpaqueCube()) {
				weight += 10.0F;
			}

			return weight;
		}
	}

	protected int decreaseAirSupply(int par1) {
		return !this.stats.canDrown ? par1 : super.decreaseAirSupply(par1);
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return this.stats == null ? null : this.stats.creatureType;
	}

	public int getTalkInterval() {
		return 160;
	}

	public void playLivingSound() {
		if (this.isEntityAlive()) {
			this.advanced.playSound(this.getAttackTarget() != null ? 1 : 0, this.getSoundVolume(),
					this.getSoundPitch());
		}
	}

	protected void playHurtSound(DamageSource source) {
		this.advanced.playSound(2, this.getSoundVolume(), this.getSoundPitch());
	}

	public SoundEvent getDeathSound() {
		return null;
	}

	protected float getSoundPitch() {
		return this.advanced.disablePitch ? 1.0F : super.getSoundPitch();
	}

	protected void playStepSound(BlockPos pos, Block block) {
		if (this.advanced.getSound(4) != null) {
			this.advanced.playSound(4, 0.15F, 1.0F);
		} else {
			super.playStepSound(pos, block);
		}

	}

	public EntityPlayerMP getFakeChatPlayer() {
		if (this.world.isRemote) {
			return null;
		} else {
			EntityUtil.Copy(this, ChatEventPlayer);
			ChatEventProfile.npc = this;
			ChatEventPlayer.refreshDisplayName();
			ChatEventPlayer.setWorld(this.world);
			ChatEventPlayer.setPosition(this.posX, this.posY, this.posZ);
			return ChatEventPlayer;
		}
	}

	public void saySurrounding(Line line) {
		if (line != null) {
			if (line.getShowText() && !line.getText().isEmpty()) {
				ServerChatEvent event = new ServerChatEvent(this.getFakeChatPlayer(), line.getText(),
						new TextComponentTranslation(line.getText().replace("%", "%%"), new Object[0]));
				if (CustomNpcs.NpcSpeachTriggersChatEvent
						&& (MinecraftForge.EVENT_BUS.post(event) || event.getComponent() == null)) {
					return;
				}

				line.setText(event.getComponent().getUnformattedText().replace("%%", "%"));
			}

			List inRange = this.world.getEntitiesWithinAABB(EntityPlayer.class,
					this.getEntityBoundingBox().grow(20.0D, 20.0D, 20.0D));
			Iterator var3 = inRange.iterator();

			while (var3.hasNext()) {
				EntityPlayer player = (EntityPlayer) var3.next();
				this.say(player, line);
			}

		}
	}

	public void say(EntityPlayer player, Line line) {
		if (line != null && this.canSee(player)) {
			if (!line.getSound().isEmpty()) {
				BlockPos pos = this.getPosition();
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.PLAY_SOUND, line.getSound(), pos.getX(),
						pos.getY(), pos.getZ(), this.getSoundVolume(), this.getSoundPitch());
			}

			if (!line.getText().isEmpty()) {
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.CHATBUBBLE, this.getEntityId(),
						line.getText(), line.getShowText());
			}

		}
	}

	public boolean getAlwaysRenderNameTagForRender() {
		return true;
	}

	public void addVelocity(double d, double d1, double d2) {
		if (this.isWalking() && !this.isKilled()) {
			super.addVelocity(d, d1, d2);
		}

	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.npcVersion = compound.getInteger("ModRev");
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
		this.killedtime = compound.getLong("KilledTime");
		this.totalTicksAlive = compound.getLong("TotalTicksAlive");
		this.linkedName = compound.getString("LinkedNpcName");
		if (!this.isRemote()) {
			LinkedNpcController.Instance.loadNpcData(this);
		}

		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue((double) CustomNpcs.NpcNavRange);
		this.updateAI = true;
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
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
		compound.setLong("KilledTime", this.killedtime);
		compound.setLong("TotalTicksAlive", this.totalTicksAlive);
		compound.setInteger("ModRev", this.npcVersion);
		compound.setString("LinkedNpcName", this.linkedName);
	}

	public void updateHitbox() {
		if (this.currentAnimation != 2 && this.currentAnimation != 7 && this.deathTime <= 0) {
			if (this.isRiding()) {
				this.width = 0.6F;
				this.height = this.baseHeight * 0.77F;
			} else {
				this.width = 0.6F;
				this.height = this.baseHeight;
			}
		} else {
			this.width = 0.8F;
			this.height = 0.4F;
		}

		this.width = this.width / 5.0F * (float) this.display.getSize();
		this.height = this.height / 5.0F * (float) this.display.getSize();
		if (!this.display.getHasHitbox() || this.isKilled() && this.stats.hideKilledBody) {
			this.width = 1.0E-5F;
		}

		double var10000 = (double) (this.width / 2.0F);
		World var10001 = this.world;
		if (var10000 > World.MAX_ENTITY_RADIUS) {
			World var1 = this.world;
			World.MAX_ENTITY_RADIUS = (double) (this.width / 2.0F);
		}

		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public void onDeathUpdate() {
		if (this.stats.spawnCycle != 3 && this.stats.spawnCycle != 4) {
			++this.deathTime;
			if (!this.world.isRemote) {
				if (!this.hasDied) {
					this.setDead();
				}

				if (this.killedtime < System.currentTimeMillis()
						&& (this.stats.spawnCycle == 0 || this.world.isDaytime() && this.stats.spawnCycle == 1
								|| !this.world.isDaytime() && this.stats.spawnCycle == 2)) {
					this.reset();
				}

			}
		} else {
			super.onDeathUpdate();
		}
	}

	public void reset() {
		this.hasDied = false;
		this.isDead = false;
		this.wasKilled = false;
		this.setSprinting(false);
		this.setHealth(this.getMaxHealth());
		this.dataManager.set(Animation, 0);
		this.dataManager.set(Walking, false);
		this.dataManager.set(IsDead, false);
		this.dataManager.set(Interacting, false);
		this.interactingEntities.clear();
		this.combatHandler.reset();
		this.setAttackTarget((EntityLivingBase) null);
		this.setRevengeTarget((EntityLivingBase) null);
		this.deathTime = 0;
		if (this.ais.returnToStart && !this.hasOwner() && !this.isRemote() && !this.isRiding()) {
			this.setLocationAndAngles((double) this.getStartXPos(), this.getStartYPos(), (double) this.getStartZPos(),
					this.rotationYaw, this.rotationPitch);
		}

		this.killedtime = 0L;
		this.extinguish();
		this.clearActivePotions();
		this.travel(0.0F, 0.0F, 0.0F);
		this.distanceWalkedModified = 0.0F;
		this.getNavigator().clearPath();
		this.currentAnimation = 0;
		this.updateHitbox();
		this.updateAI = true;
		this.ais.movingPos = 0;
		if (this.getOwner() != null) {
			this.getOwner().setLastAttackedEntity((Entity) null);
		}

		this.bossInfo.setVisible(this.display.getBossbar() == 1);
		if (this.jobInterface != null) {
			this.jobInterface.reset();
		}

		EventHooks.onNPCInit(this);
	}

	public void onCollide() {
		if (this.isEntityAlive() && this.ticksExisted % 4 == 0 && !this.world.isRemote) {
			AxisAlignedBB axisalignedbb = null;
			if (this.getRidingEntity() != null && this.getRidingEntity().isEntityAlive()) {
				axisalignedbb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox())
						.grow(1.0D, 0.0D, 1.0D);
			} else {
				axisalignedbb = this.getEntityBoundingBox().grow(1.0D, 0.5D, 1.0D);
			}

			List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
			if (list != null) {
				for (int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity) list.get(i);
					if (entity != this && entity.isEntityAlive()) {
						EventHooks.onNPCCollide(this, entity);
					}
				}

			}
		}
	}

	public void setPortal(BlockPos pos) {
	}

	public void cloakUpdate() {
		this.field_20066_r = this.field_20063_u;
		this.field_20065_s = this.field_20062_v;
		this.field_20064_t = this.field_20061_w;
		double d = this.posX - this.field_20063_u;
		double d1 = this.posY - this.field_20062_v;
		double d2 = this.posZ - this.field_20061_w;
		double d3 = 10.0D;
		if (d > d3) {
			this.field_20066_r = this.field_20063_u = this.posX;
		}

		if (d2 > d3) {
			this.field_20064_t = this.field_20061_w = this.posZ;
		}

		if (d1 > d3) {
			this.field_20065_s = this.field_20062_v = this.posY;
		}

		if (d < -d3) {
			this.field_20066_r = this.field_20063_u = this.posX;
		}

		if (d2 < -d3) {
			this.field_20064_t = this.field_20061_w = this.posZ;
		}

		if (d1 < -d3) {
			this.field_20065_s = this.field_20062_v = this.posY;
		}

		this.field_20063_u += d * 0.25D;
		this.field_20061_w += d2 * 0.25D;
		this.field_20062_v += d1 * 0.25D;
	}

	protected boolean canDespawn() {
		return this.stats.spawnCycle == 4;
	}

	public ItemStack getHeldItemMainhand() {
		IItemStack item = null;
		if (this.isAttacking()) {
			item = this.inventory.getRightHand();
		} else if (this.advanced.role == 6) {
			item = ((RoleCompanion) this.roleInterface).getHeldItem();
		} else if (this.jobInterface != null && this.jobInterface.overrideMainHand) {
			item = this.jobInterface.getMainhand();
		} else {
			item = this.inventory.getRightHand();
		}

		return ItemStackWrapper.MCItem(item);
	}

	public ItemStack getHeldItemOffhand() {
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

	public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
		if (slot == EntityEquipmentSlot.MAINHAND) {
			return this.getHeldItemMainhand();
		} else {
			return slot == EntityEquipmentSlot.OFFHAND ? this.getHeldItemOffhand()
					: ItemStackWrapper.MCItem(this.inventory.getArmor(3 - slot.getIndex()));
		}
	}

	public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack item) {
		if (slot == EntityEquipmentSlot.MAINHAND) {
			this.inventory.weapons.put(0, NpcAPI.Instance().getIItemStack(item));
		} else if (slot == EntityEquipmentSlot.OFFHAND) {
			this.inventory.weapons.put(2, NpcAPI.Instance().getIItemStack(item));
		} else {
			this.inventory.armor.put(3 - slot.getIndex(), NpcAPI.Instance().getIItemStack(item));
		}

	}

	public Iterable getArmorInventoryList() {
		ArrayList list = new ArrayList();

		for (int i = 0; i < 4; ++i) {
			list.add(ItemStackWrapper.MCItem((IItemStack) this.inventory.armor.get(3 - i)));
		}

		return list;
	}

	public Iterable getHeldEquipment() {
		ArrayList list = new ArrayList();
		list.add(ItemStackWrapper.MCItem((IItemStack) this.inventory.weapons.get(0)));
		list.add(ItemStackWrapper.MCItem((IItemStack) this.inventory.weapons.get(2)));
		return list;
	}

	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
	}

	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
	}

	public void onDeath(DamageSource damagesource) {
		this.setSprinting(false);
		this.getNavigator().clearPath();
		this.extinguish();
		this.clearActivePotions();
		if (!this.isRemote()) {
			this.advanced.playSound(3, this.getSoundVolume(), this.getSoundPitch());
			Entity attackingEntity = NoppesUtilServer.GetDamageSourcee(damagesource);
			NpcEvent.DiedEvent event = new NpcEvent.DiedEvent(this.wrappedNPC, damagesource, attackingEntity);
			event.droppedItems = this.inventory.getItemsRNG();
			event.expDropped = this.inventory.getExpRNG();
			event.line = this.advanced.getKilledLine();
			EventHooks.onNPCDied(this, event);
			this.bossInfo.setVisible(false);
			this.inventory.dropStuff(event, attackingEntity, damagesource);
			if (event.line != null) {
				this.saySurrounding(Line.formatTarget((Line) event.line,
						attackingEntity instanceof EntityLivingBase ? (EntityLivingBase) attackingEntity : null));
			}
		}

		super.onDeath(damagesource);
	}

	public void addTrackingPlayer(EntityPlayerMP player) {
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	public void removeTrackingPlayer(EntityPlayerMP player) {
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	public void setDead() {
		this.hasDied = true;
		this.removePassengers();
		this.dismountRidingEntity();
		if (!this.world.isRemote && this.stats.spawnCycle != 3 && this.stats.spawnCycle != 4) {
			this.setHealth(-1.0F);
			this.setSprinting(false);
			this.getNavigator().clearPath();
			this.setCurrentAnimation(2);
			this.updateHitbox();
			if (this.killedtime <= 0L) {
				this.killedtime = (long) (this.stats.respawnTime * 1000) + System.currentTimeMillis();
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

		super.setDead();
	}

	public float getStartXPos() {
		return (float) this.ais.startPos().getX() + this.ais.bodyOffsetX / 10.0F;
	}

	public float getStartZPos() {
		return (float) this.ais.startPos().getZ() + this.ais.bodyOffsetZ / 10.0F;
	}

	public boolean isVeryNearAssignedPlace() {
		double xx = this.posX - (double) this.getStartXPos();
		double zz = this.posZ - (double) this.getStartZPos();
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

		while (true) {
			while (pos.getY() > 0) {
				IBlockState state = this.world.getBlockState(pos);
				AxisAlignedBB bb = state.getBoundingBox(this.world, pos).offset(pos);
				if (bb != null) {
					if (this.ais.movementType != 2 || startPos.getY() > pos.getY()
							|| state.getMaterial() != Material.WATER) {
						return bb.maxY;
					}

					pos = pos.down();
				} else {
					pos = pos.down();
				}
			}

			return 0.0D;
		}
	}

	private BlockPos calculateTopPos(BlockPos pos) {
		for (BlockPos check = pos; check.getY() > 0; check = check.down()) {
			IBlockState state = this.world.getBlockState(pos);
			AxisAlignedBB bb = state.getBoundingBox(this.world, pos).offset(pos);
			if (bb != null) {
				return check;
			}
		}

		return pos;
	}

	public boolean isInRange(Entity entity, double range) {
		return this.isInRange(entity.posX, entity.posY, entity.posZ, range);
	}

	public boolean isInRange(double posX, double posY, double posZ, double range) {
		double y = Math.abs(this.posY - posY);
		if (posY >= 0.0D && y > range) {
			return false;
		} else {
			double x = Math.abs(this.posX - posX);
			double z = Math.abs(this.posZ - posZ);
			return x <= range && z <= range;
		}
	}

	public void givePlayerItem(EntityPlayer player, ItemStack item) {
		if (!this.world.isRemote) {
			item = item.copy();
			float f = 0.7F;
			double d = (double) (this.world.rand.nextFloat() * f) + (double) (1.0F - f);
			double d1 = (double) (this.world.rand.nextFloat() * f) + (double) (1.0F - f);
			double d2 = (double) (this.world.rand.nextFloat() * f) + (double) (1.0F - f);
			EntityItem entityitem = new EntityItem(this.world, this.posX + d, this.posY + d1, this.posZ + d2, item);
			entityitem.setPickupDelay(2);
			this.world.spawnEntity(entityitem);
			int i = item.getCount();
			if (player.inventory.addItemStackToInventory(item)) {
				this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
						SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
						((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				player.onItemPickup(entityitem, i);
				if (item.getCount() <= 0) {
					entityitem.setDead();
				}
			}

		}
	}

	public boolean isPlayerSleeping() {
		return this.currentAnimation == 2 && !this.isAttacking();
	}

	public boolean isWalking() {
		return this.ais.getMovingType() != 0 || this.isAttacking() || this.isFollower()
				|| (Boolean) this.dataManager.get(Walking);
	}

	public boolean isSneaking() {
		return this.currentAnimation == 4;
	}

	public void knockBack(Entity par1Entity, float strength, double ratioX, double ratioZ) {
		super.knockBack(par1Entity, strength * (2.0F - this.stats.resistances.knockback), ratioX, ratioZ);
	}

	public Faction getFaction() {
		Faction fac = FactionController.instance.getFaction((Integer) this.dataManager.get(FactionData));
		return fac == null ? FactionController.instance.getFaction(FactionController.instance.getFirstFactionId())
				: fac;
	}

	public boolean isRemote() {
		return this.world == null || this.world.isRemote;
	}

	public void setFaction(int id) {
		if (id >= 0 && !this.isRemote()) {
			this.dataManager.set(FactionData, id);
		}
	}

	public boolean isPotionApplicable(PotionEffect effect) {
		if (this.stats.potionImmune) {
			return false;
		} else {
			return this.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD
					&& effect.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(effect);
		}
	}

	public boolean isAttacking() {
		return (Boolean) this.dataManager.get(Attacking);
	}

	public boolean isKilled() {
		return this.isDead || (Boolean) this.dataManager.get(IsDead);
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
		compound.setBoolean("DeadBody", this.stats.hideKilledBody);
		compound.setInteger("StandingState", this.ais.getStandingType());
		compound.setInteger("MovingState", this.ais.getMovingType());
		compound.setInteger("Orientation", this.ais.orientation);
		compound.setFloat("PositionXOffset", this.ais.bodyOffsetX);
		compound.setFloat("PositionYOffset", this.ais.bodyOffsetY);
		compound.setFloat("PositionZOffset", this.ais.bodyOffsetZ);
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
			compound.setTag("ModelData", ((EntityCustomNpc) this).modelData.writeToNBT());
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
		this.stats.setMaxHealth(compound.getInteger("MaxHealth"));
		this.ais.setWalkingSpeed(compound.getInteger("Speed"));
		this.stats.hideKilledBody = compound.getBoolean("DeadBody");
		this.ais.setStandingType(compound.getInteger("StandingState"));
		this.ais.setMovingType(compound.getInteger("MovingState"));
		this.ais.orientation = compound.getInteger("Orientation");
		this.ais.bodyOffsetX = compound.getFloat("PositionXOffset");
		this.ais.bodyOffsetY = compound.getFloat("PositionYOffset");
		this.ais.bodyOffsetZ = compound.getFloat("PositionZOffset");
		this.inventory.armor = NBTTags.getIItemStackMap(compound.getTagList("Armor", 10));
		this.inventory.weapons = NBTTags.getIItemStackMap(compound.getTagList("Weapons", 10));
		this.advanced.setRole(compound.getInteger("Role"));
		this.advanced.setJob(compound.getInteger("Job"));
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
			((EntityCustomNpc) this).modelData.readFromNBT(compound.getCompoundTag("ModelData"));
		}

		this.display.readToNBT(compound);
	}

	public Entity getCommandSenderEntity() {
		if (this.world.isRemote) {
			return this;
		} else {
			EntityUtil.Copy(this, CommandPlayer);
			CommandPlayer.setWorld(this.world);
			CommandPlayer.setPosition(this.posX, this.posY, this.posZ);
			return CommandPlayer;
		}
	}

	public String getName() {
		return this.display.getName();
	}

	public BlockPos getPosition() {
		return new BlockPos(this.posX, this.posY, this.posZ);
	}

	public Vec3d getPositionVector() {
		return new Vec3d(this.posX, this.posY, this.posZ);
	}

	public boolean canAttackClass(Class par1Class) {
		return EntityBat.class != par1Class;
	}

	public void setImmuneToFire(boolean immuneToFire) {
		this.isImmuneToFire = immuneToFire;
		this.stats.immuneToFire = immuneToFire;
	}

	public void fall(float distance, float modifier) {
		if (!this.stats.noFallDamage) {
			super.fall(distance, modifier);
		}

	}

	public void setInWeb() {
		if (!this.stats.ignoreCobweb) {
			super.setInWeb();
		}

	}

	public boolean canBeCollidedWith() {
		return !this.isKilled() && this.display.getHasHitbox();
	}

	public boolean canBePushed() {
		return super.canBePushed() && this.display.getHasHitbox();
	}

	public EnumPushReaction getPushReaction() {
		return this.display.getHasHitbox() ? super.getPushReaction() : EnumPushReaction.IGNORE;
	}

	public EntityAIRangedAttack getRangedTask() {
		return this.aiRange;
	}

	public String getRoleData() {
		return (String) this.dataManager.get(RoleData);
	}

	public void setRoleData(String s) {
		this.dataManager.set(RoleData, s);
	}

	public String getJobData() {
		return (String) this.dataManager.get(RoleData);
	}

	public void setJobData(String s) {
		this.dataManager.set(RoleData, s);
	}

	public World getEntityWorld() {
		return this.world;
	}

	public boolean isInvisibleToPlayer(EntityPlayer player) {
		return this.display.getVisible() == 1 && (player.getHeldItemMainhand().isEmpty()
				|| player.getHeldItemMainhand().getItem() != CustomItems.wand);
	}

	public boolean isInvisible() {
		return this.display.getVisible() != 0;
	}

	public void sendMessage(ITextComponent var1) {
	}

	public void setCurrentAnimation(int animation) {
		this.currentAnimation = animation;
		this.dataManager.set(Animation, animation);
	}

	public boolean canSee(Entity entity) {
		return this.getEntitySenses().canSee(entity);
	}

	public boolean isFollower() {
		if (this.advanced.scenes.getOwner() != null) {
			return true;
		} else {
			return this.roleInterface != null && this.roleInterface.isFollowing()
					|| this.jobInterface != null && this.jobInterface.isFollowing();
		}
	}

	public EntityLivingBase getOwner() {
		if (this.advanced.scenes.getOwner() != null) {
			return this.advanced.scenes.getOwner();
		} else if (this.advanced.role == 2 && this.roleInterface instanceof RoleFollower) {
			return ((RoleFollower) this.roleInterface).owner;
		} else if (this.advanced.role == 6 && this.roleInterface instanceof RoleCompanion) {
			return ((RoleCompanion) this.roleInterface).owner;
		} else {
			return this.advanced.job == 5 && this.jobInterface instanceof JobFollower
					? ((JobFollower) this.jobInterface).following
					: null;
		}
	}

	public boolean hasOwner() {
		if (this.advanced.scenes.getOwner() != null) {
			return true;
		} else {
			return this.advanced.role == 2 && ((RoleFollower) this.roleInterface).hasOwner()
					|| this.advanced.role == 6 && ((RoleCompanion) this.roleInterface).hasOwner()
					|| this.advanced.job == 5 && ((JobFollower) this.jobInterface).hasOwner();
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

	public void setHomePosAndDistance(BlockPos pos, int range) {
		super.setHomePosAndDistance(pos, range);
		this.ais.setStartPos(pos);
	}

	protected float applyArmorCalculations(DamageSource source, float damage) {
		if (this.advanced.role == 6) {
			damage = ((RoleCompanion) this.roleInterface).applyArmorCalculations(source, damage);
		}

		return damage;
	}

	public boolean isOnSameTeam(Entity entity) {
		if (!this.isRemote()) {
			if (entity instanceof EntityPlayer && this.getFaction().isFriendlyToPlayer((EntityPlayer) entity)) {
				return true;
			}

			if (entity == this.getOwner()) {
				return true;
			}

			if (entity instanceof EntityNPCInterface && ((EntityNPCInterface) entity).faction.id == this.faction.id) {
				return true;
			}
		}

		return super.isOnSameTeam(entity);
	}

	public void setDataWatcher(EntityDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void travel(float f1, float f2, float f3) {
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		super.travel(f1, f2, f3);
		if (this.advanced.role == 6 && !this.isRemote()) {
			((RoleCompanion) this.roleInterface).addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
		}

	}

	public boolean canBeLeashedTo(EntityPlayer player) {
		return false;
	}

	public boolean getLeashed() {
		return false;
	}

	public boolean nearPosition(BlockPos pos) {
		BlockPos npcpos = this.getPosition();
		float x = (float) (npcpos.getX() - pos.getX());
		float z = (float) (npcpos.getZ() - pos.getZ());
		float y = (float) (npcpos.getY() - pos.getY());
		float height = (float) (MathHelper.ceil(this.height + 1.0F) * MathHelper.ceil(this.height + 1.0F));
		return (double) (x * x + z * z) < 2.5D && (double) (y * y) < (double) height + 2.5D;
	}

	public void tpTo(EntityLivingBase owner) {
		if (owner != null) {
			EnumFacing facing = owner.getHorizontalFacing().getOpposite();
			BlockPos pos = new BlockPos(owner.posX, owner.getEntityBoundingBox().minY, owner.posZ);
			pos = pos.add(facing.getFrontOffsetX(), 0, facing.getFrontOffsetZ());
			pos = this.calculateTopPos(pos);

			for (int i = -1; i < 2; ++i) {
				for (int j = 0; j < 3; ++j) {
					BlockPos check;
					if (facing.getFrontOffsetX() == 0) {
						check = pos.add(i, 0, j * facing.getFrontOffsetZ());
					} else {
						check = pos.add(j * facing.getFrontOffsetX(), 0, i);
					}

					check = this.calculateTopPos(check);
					if (!this.world.getBlockState(check).isFullBlock()
							&& !this.world.getBlockState(check.up()).isFullBlock()) {
						this.setLocationAndAngles((double) ((float) check.getX() + 0.5F), (double) check.getY(),
								(double) ((float) check.getZ() + 0.5F), this.rotationYaw, this.rotationPitch);
						this.getNavigator().clearPath();
						break;
					}
				}
			}

		}
	}

	public int getMaxSpawnedInChunk() {
		return 8;
	}

	public void setSwingingArms(boolean swingingArms) {
	}

	public boolean getCanSpawnHere() {
		return this.getBlockPathWeight(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ)) >= 0.0F
				&& this.world.getBlockState((new BlockPos(this)).down()).canEntitySpawn(this);
	}

	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

	static {
		Attacking = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.BOOLEAN);
		Animation = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.VARINT);
		RoleData = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.STRING);
		JobData = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.STRING);
		FactionData = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.VARINT);
		Walking = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.BOOLEAN);
		Interacting = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.BOOLEAN);
		IsDead = EntityDataManager.createKey(EntityNPCInterface.class, DataSerializers.BOOLEAN);
		CommandProfile = new GameProfileAlt();
		ChatEventProfile = new GameProfileAlt();
		GenericProfile = new GameProfileAlt();
	}
}
