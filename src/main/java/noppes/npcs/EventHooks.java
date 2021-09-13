package noppes.npcs;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityItem;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.event.BlockEvent;
import noppes.npcs.api.event.CustomContainerEvent;
import noppes.npcs.api.event.CustomGuiEvent;
import noppes.npcs.api.event.DialogEvent;
import noppes.npcs.api.event.ForgeEvent;
import noppes.npcs.api.event.HandlerEvent;
import noppes.npcs.api.event.ItemEvent;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.event.ProjectileEvent;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.wrapper.BlockPosWrapper;
import noppes.npcs.api.wrapper.ContainerCustomChestWrapper;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.ForgeScriptData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityProjectile;
import org.apache.commons.lang3.StringUtils;

public class EventHooks {
     public static boolean onNPCAttacksMelee(EntityNPCInterface npc, NpcEvent.MeleeAttackEvent event) {
          if (npc.script.isClient()) {
               return false;
          } else {
               npc.script.runScript(EnumScriptType.ATTACK_MELEE, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCRangedLaunched(EntityNPCInterface npc, NpcEvent.RangedLaunchedEvent event) {
          if (!npc.script.isClient()) {
               npc.script.runScript(EnumScriptType.RANGED_LAUNCHED, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCTarget(EntityNPCInterface npc, NpcEvent.TargetEvent event) {
          if (npc.script.isClient()) {
               return false;
          } else {
               npc.script.runScript(EnumScriptType.TARGET, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCTargetLost(EntityNPCInterface npc, EntityLivingBase prevtarget) {
          if (npc.script.isClient()) {
               return false;
          } else {
               NpcEvent.TargetLostEvent event = new NpcEvent.TargetLostEvent(npc.wrappedNPC, prevtarget);
               npc.script.runScript(EnumScriptType.TARGET_LOST, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCInteract(EntityNPCInterface npc, EntityPlayer player) {
          if (npc.script.isClient()) {
               return false;
          } else {
               NpcEvent.InteractEvent event = new NpcEvent.InteractEvent(npc.wrappedNPC, player);
               event.setCanceled(npc.isAttacking() || npc.isKilled() || npc.faction.isAggressiveToPlayer(player));
               npc.script.runScript(EnumScriptType.INTERACT, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCDamaged(EntityNPCInterface npc, NpcEvent.DamagedEvent event) {
          if (npc.script.isClient()) {
               return false;
          } else {
               event.setCanceled(npc.isKilled());
               npc.script.runScript(EnumScriptType.DAMAGED, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCInit(EntityNPCInterface npc) {
          if (!npc.script.isClient()) {
               NpcEvent.InitEvent event = new NpcEvent.InitEvent(npc.wrappedNPC);
               npc.script.runScript(EnumScriptType.INIT, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCCollide(EntityNPCInterface npc, Entity entity) {
          if (!npc.script.isClient()) {
               NpcEvent.CollideEvent event = new NpcEvent.CollideEvent(npc.wrappedNPC, entity);
               npc.script.runScript(EnumScriptType.COLLIDE, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCTick(EntityNPCInterface npc) {
          if (!npc.script.isClient()) {
               NpcEvent.UpdateEvent event = new NpcEvent.UpdateEvent(npc.wrappedNPC);
               npc.script.runScript(EnumScriptType.TICK, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCDied(EntityNPCInterface npc, NpcEvent.DiedEvent event) {
          if (!npc.script.isClient()) {
               npc.script.runScript(EnumScriptType.DIED, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCDialogOption(EntityNPCInterface npc, EntityPlayerMP player, Dialog dialog, DialogOption option) {
          if (npc.script.isClient()) {
               return false;
          } else {
               DialogEvent.OptionEvent event = new DialogEvent.OptionEvent(npc.wrappedNPC, player, dialog, option);
               if (!(npc instanceof EntityDialogNpc)) {
                    npc.script.runScript(EnumScriptType.DIALOG_OPTION, event);
               }

               PlayerData.get(player).scriptData.runScript(EnumScriptType.DIALOG_OPTION, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCDialog(EntityNPCInterface npc, EntityPlayer player, Dialog dialog) {
          if (npc.script.isClient()) {
               return false;
          } else {
               DialogEvent.OpenEvent event = new DialogEvent.OpenEvent(npc.wrappedNPC, player, dialog);
               if (!(npc instanceof EntityDialogNpc)) {
                    npc.script.runScript(EnumScriptType.DIALOG, event);
               }

               PlayerData.get(player).scriptData.runScript(EnumScriptType.DIALOG, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCDialogClose(EntityNPCInterface npc, EntityPlayerMP player, Dialog dialog) {
          if (!npc.script.isClient()) {
               DialogEvent.CloseEvent event = new DialogEvent.CloseEvent(npc.wrappedNPC, player, dialog);
               if (!(npc instanceof EntityDialogNpc)) {
                    npc.script.runScript(EnumScriptType.DIALOG_CLOSE, event);
               }

               PlayerData.get(player).scriptData.runScript(EnumScriptType.DIALOG_CLOSE, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCKills(EntityNPCInterface npc, EntityLivingBase entityLiving) {
          if (!npc.script.isClient()) {
               NpcEvent.KilledEntityEvent event = new NpcEvent.KilledEntityEvent(npc.wrappedNPC, entityLiving);
               npc.script.runScript(EnumScriptType.KILL, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onNPCRole(EntityNPCInterface npc, RoleEvent event) {
          if (npc.script.isClient()) {
               return false;
          } else {
               npc.script.runScript(EnumScriptType.ROLE, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onNPCTimer(EntityNPCInterface npc, int id) {
          NpcEvent.TimerEvent event = new NpcEvent.TimerEvent(npc.wrappedNPC, id);
          npc.script.runScript(EnumScriptType.TIMER, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onScriptBlockInteract(IScriptBlockHandler handler, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
          if (handler.isClient()) {
               return false;
          } else {
               BlockEvent.InteractEvent event = new BlockEvent.InteractEvent(handler.getBlock(), player, side, hitX, hitY, hitZ);
               handler.runScript(EnumScriptType.INTERACT, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockCollide(IScriptBlockHandler handler, Entity entityIn) {
          if (!handler.isClient()) {
               BlockEvent.CollidedEvent event = new BlockEvent.CollidedEvent(handler.getBlock(), entityIn);
               handler.runScript(EnumScriptType.COLLIDE, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockRainFill(IScriptBlockHandler handler) {
          if (!handler.isClient()) {
               BlockEvent.RainFillEvent event = new BlockEvent.RainFillEvent(handler.getBlock());
               handler.runScript(EnumScriptType.RAIN_FILLED, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static float onScriptBlockFallenUpon(IScriptBlockHandler handler, Entity entity, float distance) {
          if (handler.isClient()) {
               return distance;
          } else {
               BlockEvent.EntityFallenUponEvent event = new BlockEvent.EntityFallenUponEvent(handler.getBlock(), entity, distance);
               handler.runScript(EnumScriptType.FALLEN_UPON, event);
               return WrapperNpcAPI.EVENT_BUS.post(event) ? 0.0F : event.distanceFallen;
          }
     }

     public static void onScriptBlockClicked(IScriptBlockHandler handler, EntityPlayer player) {
          if (!handler.isClient()) {
               BlockEvent.ClickedEvent event = new BlockEvent.ClickedEvent(handler.getBlock(), player);
               handler.runScript(EnumScriptType.CLICKED, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockBreak(IScriptBlockHandler handler) {
          if (!handler.isClient()) {
               BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(handler.getBlock());
               handler.runScript(EnumScriptType.BROKEN, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onScriptBlockHarvest(IScriptBlockHandler handler, EntityPlayer player) {
          if (handler.isClient()) {
               return false;
          } else {
               BlockEvent.HarvestedEvent event = new BlockEvent.HarvestedEvent(handler.getBlock(), player);
               handler.runScript(EnumScriptType.HARVESTED, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onScriptBlockExploded(IScriptBlockHandler handler) {
          if (handler.isClient()) {
               return false;
          } else {
               BlockEvent.ExplodedEvent event = new BlockEvent.ExplodedEvent(handler.getBlock());
               handler.runScript(EnumScriptType.EXPLODED, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockNeighborChanged(IScriptBlockHandler handler, BlockPos changedPos) {
          if (!handler.isClient()) {
               BlockEvent.NeighborChangedEvent event = new BlockEvent.NeighborChangedEvent(handler.getBlock(), new BlockPosWrapper(changedPos));
               handler.runScript(EnumScriptType.NEIGHBOR_CHANGED, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockRedstonePower(IScriptBlockHandler handler, int prevPower, int power) {
          if (!handler.isClient()) {
               BlockEvent.RedstoneEvent event = new BlockEvent.RedstoneEvent(handler.getBlock(), prevPower, power);
               handler.runScript(EnumScriptType.REDSTONE, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockInit(IScriptBlockHandler handler) {
          if (!handler.isClient()) {
               BlockEvent.InitEvent event = new BlockEvent.InitEvent(handler.getBlock());
               handler.runScript(EnumScriptType.INIT, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockUpdate(IScriptBlockHandler handler) {
          if (!handler.isClient()) {
               BlockEvent.UpdateEvent event = new BlockEvent.UpdateEvent(handler.getBlock());
               handler.runScript(EnumScriptType.TICK, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onScriptBlockDoorToggle(IScriptBlockHandler handler) {
          if (handler.isClient()) {
               return false;
          } else {
               BlockEvent.DoorToggleEvent event = new BlockEvent.DoorToggleEvent(handler.getBlock());
               handler.runScript(EnumScriptType.DOOR_TOGGLE, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptBlockTimer(IScriptBlockHandler handler, int id) {
          BlockEvent.TimerEvent event = new BlockEvent.TimerEvent(handler.getBlock(), id);
          handler.runScript(EnumScriptType.TIMER, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onGlobalRecipesLoaded(IRecipeHandler handler) {
          HandlerEvent.RecipesLoadedEvent event = new HandlerEvent.RecipesLoadedEvent(handler);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onGlobalFactionsLoaded(IFactionHandler handler) {
          HandlerEvent.FactionsLoadedEvent event = new HandlerEvent.FactionsLoadedEvent(handler);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerInit(PlayerScriptData handler) {
          PlayerEvent.InitEvent event = new PlayerEvent.InitEvent(handler.getPlayer());
          handler.runScript(EnumScriptType.INIT, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerTick(PlayerScriptData handler) {
          PlayerEvent.UpdateEvent event = new PlayerEvent.UpdateEvent(handler.getPlayer());
          handler.runScript(EnumScriptType.TICK, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerInteract(PlayerScriptData handler, PlayerEvent.InteractEvent event) {
          handler.runScript(EnumScriptType.INTERACT, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerAttack(PlayerScriptData handler, PlayerEvent.AttackEvent event) {
          handler.runScript(EnumScriptType.ATTACK, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerBreak(PlayerScriptData handler, PlayerEvent.BreakEvent event) {
          handler.runScript(EnumScriptType.BROKEN, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerToss(PlayerScriptData handler, EntityItem entityItem) {
          PlayerEvent.TossEvent event = new PlayerEvent.TossEvent(handler.getPlayer(), NpcAPI.Instance().getIItemStack(entityItem.func_92059_d()));
          handler.runScript(EnumScriptType.TOSS, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerLevelUp(PlayerScriptData handler, int change) {
          PlayerEvent.LevelUpEvent event = new PlayerEvent.LevelUpEvent(handler.getPlayer(), change);
          handler.runScript(EnumScriptType.LEVEL_UP, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerPickUp(PlayerScriptData handler, EntityItem entityItem) {
          PlayerEvent.PickUpEvent event = new PlayerEvent.PickUpEvent(handler.getPlayer(), NpcAPI.Instance().getIItemStack(entityItem.func_92059_d()));
          handler.runScript(EnumScriptType.PICKUP, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerContainerOpen(PlayerScriptData handler, Container container) {
          PlayerEvent.ContainerOpen event = new PlayerEvent.ContainerOpen(handler.getPlayer(), NpcAPI.Instance().getIContainer(container));
          handler.runScript(EnumScriptType.CONTAINER_OPEN, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerContainerClose(PlayerScriptData handler, Container container) {
          PlayerEvent.ContainerClosed event = new PlayerEvent.ContainerClosed(handler.getPlayer(), NpcAPI.Instance().getIContainer(container));
          handler.runScript(EnumScriptType.CONTAINER_CLOSED, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerDeath(PlayerScriptData handler, DamageSource source, Entity entity) {
          PlayerEvent.DiedEvent event = new PlayerEvent.DiedEvent(handler.getPlayer(), source, entity);
          handler.runScript(EnumScriptType.DIED, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerKills(PlayerScriptData handler, EntityLivingBase entityLiving) {
          PlayerEvent.KilledEntityEvent event = new PlayerEvent.KilledEntityEvent(handler.getPlayer(), entityLiving);
          handler.runScript(EnumScriptType.KILL, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerTimer(PlayerData data, int id) {
          PlayerScriptData handler = data.scriptData;
          PlayerEvent.TimerEvent event = new PlayerEvent.TimerEvent(handler.getPlayer(), id);
          handler.runScript(EnumScriptType.TIMER, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerDamaged(PlayerScriptData handler, PlayerEvent.DamagedEvent event) {
          handler.runScript(EnumScriptType.DAMAGED, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerLogin(PlayerScriptData handler) {
          PlayerEvent.LoginEvent event = new PlayerEvent.LoginEvent(handler.getPlayer());
          handler.runScript(EnumScriptType.LOGIN, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerLogout(PlayerScriptData handler) {
          PlayerEvent.LogoutEvent event = new PlayerEvent.LogoutEvent(handler.getPlayer());
          handler.runScript(EnumScriptType.LOGOUT, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onPlayerChat(PlayerScriptData handler, PlayerEvent.ChatEvent event) {
          handler.runScript(EnumScriptType.CHAT, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerRanged(PlayerScriptData handler, PlayerEvent.RangedLaunchedEvent event) {
          handler.runScript(EnumScriptType.RANGED_LAUNCHED, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onPlayerDamagedEntity(PlayerScriptData handler, PlayerEvent.DamagedEntityEvent event) {
          handler.runScript(EnumScriptType.DAMAGED_ENTITY, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void OnPlayerFactionChange(PlayerScriptData handler, PlayerEvent.FactionUpdateEvent event) {
          if (!handler.isClient()) {
               handler.runScript(EnumScriptType.FACTION_UPDATE, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onPlayerKeyPressed(EntityPlayerMP player, int button, boolean isCtrlPressed, boolean isShiftPressed, boolean isAltPressed, boolean isMetaPressed) {
          PlayerScriptData handler = PlayerData.get(player).scriptData;
          Event event = new PlayerEvent.KeyPressedEvent(handler.getPlayer(), button, isCtrlPressed, isAltPressed, isShiftPressed, isMetaPressed);
          handler.runScript(EnumScriptType.KEY_PRESSED, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onForgeEntityEvent(EntityEvent event) {
          if (ScriptController.Instance.forgeScripts.isEnabled()) {
               IEntity e = NpcAPI.Instance().getIEntity(event.getEntity());
               onForgeEvent(new ForgeEvent.EntityEvent(event, e), event);
          }
     }

     public static void onForgeWorldEvent(WorldEvent event) {
          if (ScriptController.Instance.forgeScripts.isEnabled()) {
               IWorld e = NpcAPI.Instance().getIWorld((WorldServer)event.getWorld());
               onForgeEvent(new ForgeEvent.WorldEvent(event, e), event);
          }
     }

     public static void onForgeInit(ForgeScriptData handler) {
          ForgeEvent.InitEvent event = new ForgeEvent.InitEvent();
          handler.runScript((EnumScriptType)EnumScriptType.INIT, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onForgeEvent(ForgeEvent ev, Event event) {
          ForgeScriptData data = ScriptController.Instance.forgeScripts;
          if (data.isEnabled()) {
               String eventName = event.getClass().getName();
               int i = eventName.lastIndexOf(".");
               eventName = StringUtils.uncapitalize(eventName.substring(i + 1).replace("$", ""));
               if (event.isCancelable()) {
                    ev.setCanceled(event.isCanceled());
               }

               data.runScript(eventName, event);
               WrapperNpcAPI.EVENT_BUS.post(ev);
               if (event.isCancelable()) {
                    event.setCanceled(ev.isCanceled());
               }

          }
     }

     public static boolean onQuestStarted(PlayerScriptData handler, Quest quest) {
          if (handler.isClient()) {
               return false;
          } else {
               QuestEvent.QuestStartEvent event = new QuestEvent.QuestStartEvent(handler.getPlayer(), quest);
               handler.runScript(EnumScriptType.QUEST_START, event);
               return WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onQuestFinished(PlayerScriptData handler, Quest quest) {
          if (!handler.isClient()) {
               QuestEvent.QuestCompletedEvent event = new QuestEvent.QuestCompletedEvent(handler.getPlayer(), quest);
               handler.runScript(EnumScriptType.QUEST_COMPLETED, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onQuestTurnedIn(PlayerScriptData handler, QuestEvent.QuestTurnedInEvent event) {
          if (!handler.isClient()) {
               handler.runScript(EnumScriptType.QUEST_TURNIN, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptItemInit(ItemScriptedWrapper handler) {
          if (!handler.isClient()) {
               ItemEvent.InitEvent event = new ItemEvent.InitEvent(handler);
               handler.runScript(EnumScriptType.INIT, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onScriptItemUpdate(ItemScriptedWrapper handler, EntityPlayer player) {
          if (!handler.isClient()) {
               ItemEvent.UpdateEvent event = new ItemEvent.UpdateEvent(handler, PlayerData.get(player).scriptData.getPlayer());
               handler.runScript(EnumScriptType.TICK, event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static boolean onScriptItemTossed(ItemScriptedWrapper handler, EntityPlayer player, EntityItem entity) {
          ItemEvent.TossedEvent event = new ItemEvent.TossedEvent(handler, PlayerData.get(player).scriptData.getPlayer(), (IEntityItem)NpcAPI.Instance().getIEntity(entity));
          handler.runScript(EnumScriptType.TOSSED, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onScriptItemPickedUp(ItemScriptedWrapper handler, EntityPlayer player, EntityItem entity) {
          ItemEvent.PickedUpEvent event = new ItemEvent.PickedUpEvent(handler, PlayerData.get(player).scriptData.getPlayer(), (IEntityItem)NpcAPI.Instance().getIEntity(entity));
          handler.runScript(EnumScriptType.PICKEDUP, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onScriptItemSpawn(ItemScriptedWrapper handler, EntityItem entity) {
          ItemEvent.SpawnEvent event = new ItemEvent.SpawnEvent(handler, (IEntityItem)NpcAPI.Instance().getIEntity(entity));
          handler.runScript(EnumScriptType.SPAWN, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onScriptItemInteract(ItemScriptedWrapper handler, ItemEvent.InteractEvent event) {
          handler.runScript(EnumScriptType.INTERACT, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static boolean onScriptItemAttack(ItemScriptedWrapper handler, ItemEvent.AttackEvent event) {
          handler.runScript(EnumScriptType.ATTACK, event);
          return WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onProjectileTick(EntityProjectile projectile) {
          ProjectileEvent.UpdateEvent event = new ProjectileEvent.UpdateEvent((IProjectile)NpcAPI.Instance().getIEntity(projectile));
          Iterator var2 = projectile.scripts.iterator();

          while(var2.hasNext()) {
               ScriptContainer script = (ScriptContainer)var2.next();
               if (script.isValid()) {
                    script.run((EnumScriptType)EnumScriptType.PROJECTILE_TICK, (Event)event);
               }
          }

          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onProjectileImpact(EntityProjectile projectile, ProjectileEvent.ImpactEvent event) {
          Iterator var2 = projectile.scripts.iterator();

          while(var2.hasNext()) {
               ScriptContainer script = (ScriptContainer)var2.next();
               if (script.isValid()) {
                    script.run((EnumScriptType)EnumScriptType.PROJECTILE_IMPACT, (Event)event);
               }
          }

          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onCustomChestClosed(CustomContainerEvent.CloseEvent event) {
          ContainerCustomChestWrapper container = (ContainerCustomChestWrapper)event.container;
          if (container.script.isValid()) {
               container.script.run((EnumScriptType)EnumScriptType.CUSTOM_CHEST_CLOSED, (Event)event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onCustomChestClicked(CustomContainerEvent.SlotClickedEvent event) {
          ContainerCustomChestWrapper container = (ContainerCustomChestWrapper)event.container;
          if (container.script.isValid()) {
               container.script.run((EnumScriptType)EnumScriptType.CUSTOM_CHEST_CLICKED, (Event)event);
               WrapperNpcAPI.EVENT_BUS.post(event);
          }
     }

     public static void onWorldScriptEvent(noppes.npcs.api.event.WorldEvent.ScriptCommandEvent event) {
          ScriptController.Instance.playerScripts.runScript(EnumScriptType.SCRIPT_COMMAND, event);
          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onCustomGuiButton(PlayerWrapper player, ICustomGui gui, int buttonId) {
          CustomGuiEvent.ButtonEvent event = new CustomGuiEvent.ButtonEvent(player, gui, buttonId);
          CustomGuiController.onButton(event);
     }

     public static void onCustomGuiSlot(PlayerWrapper player, ICustomGui gui, int slotId) {
          CustomGuiEvent.SlotEvent event = new CustomGuiEvent.SlotEvent(player, gui, slotId, player.getOpenContainer().getSlot(slotId));
          CustomGuiController.onSlotChange(event);
     }

     public static void onCustomGuiScrollClick(PlayerWrapper player, ICustomGui gui, int scrollId, int scrollIndex, String[] selection, boolean doubleClick) {
          CustomGuiEvent.ScrollEvent event = new CustomGuiEvent.ScrollEvent(player, gui, scrollId, scrollIndex, selection, doubleClick);
          CustomGuiController.onScrollClick(event);
     }

     public static void onCustomGuiClose(PlayerWrapper player, ICustomGui gui) {
          CustomGuiEvent.CloseEvent event = new CustomGuiEvent.CloseEvent(player, gui);
          CustomGuiController.onClose(event);
     }
}
