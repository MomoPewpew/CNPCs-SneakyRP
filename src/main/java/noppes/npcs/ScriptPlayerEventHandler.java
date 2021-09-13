package noppes.npcs;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Close;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.relauncher.Side;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.event.ForgeEvent;
import noppes.npcs.api.event.ItemEvent;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemNbtBook;
import noppes.npcs.items.ItemScripted;

public class ScriptPlayerEventHandler {
     @SubscribeEvent
     public void onServerTick(PlayerTickEvent event) {
          if (event.side == Side.SERVER && event.phase == Phase.START) {
               EntityPlayer player = event.player;
               PlayerData data = PlayerData.get(player);
               if (player.field_70173_aa % 10 == 0) {
                    EventHooks.onPlayerTick(data.scriptData);

                    for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                         ItemStack item = player.inventory.getStackInSlot(i);
                         if (!item.isEmpty() && item.getItem() == CustomItems.scripted_item) {
                              ItemScriptedWrapper isw = (ItemScriptedWrapper)NpcAPI.Instance().getIItemStack(item);
                              EventHooks.onScriptItemUpdate(isw, player);
                              if (isw.updateClient) {
                                   isw.updateClient = false;
                                   Server.sendData((EntityPlayerMP)player, EnumPacketClient.UPDATE_ITEM, i, isw.getMCNbt());
                              }
                         }
                    }
               }

               if (data.playerLevel != player.field_71068_ca) {
                    EventHooks.onPlayerLevelUp(data.scriptData, data.playerLevel - player.field_71068_ca);
                    data.playerLevel = player.field_71068_ca;
               }

               data.timers.update();
          }
     }

     @SubscribeEvent
     public void invoke(LeftClickBlock event) {
          if (!event.getEntityPlayer().world.isRemote && event.getHand() == EnumHand.MAIN_HAND && event.getWorld() instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
               PlayerEvent.AttackEvent ev = new PlayerEvent.AttackEvent(handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getWorld(), event.getPos()));
               event.setCanceled(EventHooks.onPlayerAttack(handler, ev));
               if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                    ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                    ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent(isw, handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getWorld(), event.getPos()));
                    eve.setCanceled(event.isCanceled());
                    event.setCanceled(EventHooks.onScriptItemAttack(isw, eve));
               }

          }
     }

     @SubscribeEvent
     public void invoke(RightClickBlock event) {
          if (!event.getEntityPlayer().world.isRemote && event.getHand() == EnumHand.MAIN_HAND && event.getWorld() instanceof WorldServer) {
               if (event.getItemStack().getItem() == CustomItems.nbt_book) {
                    ((ItemNbtBook)event.getItemStack().getItem()).blockEvent(event);
                    event.setCanceled(true);
               } else {
                    PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
                    handler.hadInteract = true;
                    PlayerEvent.InteractEvent ev = new PlayerEvent.InteractEvent(handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getWorld(), event.getPos()));
                    event.setCanceled(EventHooks.onPlayerInteract(handler, ev));
                    if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                         ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                         ItemEvent.InteractEvent eve = new ItemEvent.InteractEvent(isw, handler.getPlayer(), 2, NpcAPI.Instance().getIBlock(event.getWorld(), event.getPos()));
                         event.setCanceled(EventHooks.onScriptItemInteract(isw, eve));
                    }

               }
          }
     }

     @SubscribeEvent
     public void invoke(EntityInteract event) {
          if (!event.getEntityPlayer().world.isRemote && event.getHand() == EnumHand.MAIN_HAND && event.getWorld() instanceof WorldServer) {
               if (event.getItemStack().getItem() == CustomItems.nbt_book) {
                    ((ItemNbtBook)event.getItemStack().getItem()).entityEvent(event);
                    event.setCanceled(true);
               } else {
                    PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
                    PlayerEvent.InteractEvent ev = new PlayerEvent.InteractEvent(handler.getPlayer(), 1, NpcAPI.Instance().getIEntity(event.getTarget()));
                    event.setCanceled(EventHooks.onPlayerInteract(handler, ev));
                    if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                         ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                         ItemEvent.InteractEvent eve = new ItemEvent.InteractEvent(isw, handler.getPlayer(), 1, NpcAPI.Instance().getIEntity(event.getTarget()));
                         event.setCanceled(EventHooks.onScriptItemInteract(isw, eve));
                    }

               }
          }
     }

     @SubscribeEvent
     public void invoke(RightClickItem event) {
          if (!event.getEntityPlayer().world.isRemote && event.getHand() == EnumHand.MAIN_HAND && event.getWorld() instanceof WorldServer) {
               if (event.getEntityPlayer().func_184812_l_() && event.getEntityPlayer().func_70093_af() && event.getItemStack().getItem() == CustomItems.scripted_item) {
                    NoppesUtilServer.sendOpenGui(event.getEntityPlayer(), EnumGuiType.ScriptItem, (EntityNPCInterface)null);
               } else {
                    PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
                    if (handler.hadInteract) {
                         handler.hadInteract = false;
                    } else {
                         PlayerEvent.InteractEvent ev = new PlayerEvent.InteractEvent(handler.getPlayer(), 0, (Object)null);
                         event.setCanceled(EventHooks.onPlayerInteract(handler, ev));
                         if (event.getItemStack().getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                              ItemScriptedWrapper isw = ItemScripted.GetWrapper(event.getItemStack());
                              ItemEvent.InteractEvent eve = new ItemEvent.InteractEvent(isw, handler.getPlayer(), 0, (Object)null);
                              event.setCanceled(EventHooks.onScriptItemInteract(isw, eve));
                         }

                    }
               }
          }
     }

     @SubscribeEvent
     public void invoke(ArrowLooseEvent event) {
          if (!event.getEntityPlayer().world.isRemote && event.getWorld() instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
               PlayerEvent.RangedLaunchedEvent ev = new PlayerEvent.RangedLaunchedEvent(handler.getPlayer());
               event.setCanceled(EventHooks.onPlayerRanged(handler, ev));
          }
     }

     @SubscribeEvent
     public void invoke(BreakEvent event) {
          if (!event.getPlayer().world.isRemote && event.getWorld() instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getPlayer()).scriptData;
               PlayerEvent.BreakEvent ev = new PlayerEvent.BreakEvent(handler.getPlayer(), NpcAPI.Instance().getIBlock(event.getWorld(), event.getPos()), event.getExpToDrop());
               event.setCanceled(EventHooks.onPlayerBreak(handler, ev));
               event.setExpToDrop(ev.exp);
          }
     }

     @SubscribeEvent
     public void invoke(ItemTossEvent event) {
          if (event.getPlayer().world instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getPlayer()).scriptData;
               event.setCanceled(EventHooks.onPlayerToss(handler, event.getEntityItem()));
          }
     }

     @SubscribeEvent
     public void invoke(EntityItemPickupEvent event) {
          if (event.getEntityPlayer().world instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
               event.setCanceled(EventHooks.onPlayerPickUp(handler, event.getItem()));
          }
     }

     @SubscribeEvent
     public void invoke(Open event) {
          if (event.getEntityPlayer().world instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
               EventHooks.onPlayerContainerOpen(handler, event.getContainer());
          }
     }

     @SubscribeEvent
     public void invoke(Close event) {
          if (event.getEntityPlayer().world instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.getEntityPlayer()).scriptData;
               EventHooks.onPlayerContainerClose(handler, event.getContainer());
          }
     }

     @SubscribeEvent
     public void invoke(LivingDeathEvent event) {
          if (event.getEntityLiving().world instanceof WorldServer) {
               Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
               PlayerScriptData handler;
               if (event.getEntityLiving() instanceof EntityPlayer) {
                    handler = PlayerData.get((EntityPlayer)event.getEntityLiving()).scriptData;
                    EventHooks.onPlayerDeath(handler, event.getSource(), source);
               }

               if (source instanceof EntityPlayer) {
                    handler = PlayerData.get((EntityPlayer)source).scriptData;
                    EventHooks.onPlayerKills(handler, event.getEntityLiving());
               }

          }
     }

     @SubscribeEvent
     public void invoke(LivingHurtEvent event) {
          if (event.getEntityLiving().world instanceof WorldServer) {
               Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
               PlayerScriptData handler;
               if (event.getEntityLiving() instanceof EntityPlayer) {
                    handler = PlayerData.get((EntityPlayer)event.getEntityLiving()).scriptData;
                    PlayerEvent.DamagedEvent pevent = new PlayerEvent.DamagedEvent(handler.getPlayer(), source, event.getAmount(), event.getSource());
                    event.setCanceled(EventHooks.onPlayerDamaged(handler, pevent));
                    event.setAmount(pevent.damage);
               }

               if (source instanceof EntityPlayer) {
                    handler = PlayerData.get((EntityPlayer)source).scriptData;
                    PlayerEvent.DamagedEntityEvent pevent = new PlayerEvent.DamagedEntityEvent(handler.getPlayer(), event.getEntityLiving(), event.getAmount(), event.getSource());
                    event.setCanceled(EventHooks.onPlayerDamagedEntity(handler, pevent));
                    event.setAmount(pevent.damage);
               }

          }
     }

     @SubscribeEvent
     public void invoke(LivingAttackEvent event) {
          if (event.getEntityLiving().world instanceof WorldServer) {
               Entity source = NoppesUtilServer.GetDamageSourcee(event.getSource());
               if (source instanceof EntityPlayer) {
                    PlayerScriptData handler = PlayerData.get((EntityPlayer)source).scriptData;
                    ItemStack item = ((EntityPlayer)source).func_184614_ca();
                    IEntity target = NpcAPI.Instance().getIEntity(event.getEntityLiving());
                    PlayerEvent.AttackEvent ev = new PlayerEvent.AttackEvent(handler.getPlayer(), 1, target);
                    event.setCanceled(EventHooks.onPlayerAttack(handler, ev));
                    if (item.getItem() == CustomItems.scripted_item && !event.isCanceled()) {
                         ItemScriptedWrapper isw = ItemScripted.GetWrapper(item);
                         ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent(isw, handler.getPlayer(), 1, target);
                         eve.setCanceled(event.isCanceled());
                         event.setCanceled(EventHooks.onScriptItemAttack(isw, eve));
                    }
               }

          }
     }

     @SubscribeEvent
     public void invoke(PlayerLoggedInEvent event) {
          if (event.player.world instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.player).scriptData;
               EventHooks.onPlayerLogin(handler);
          }
     }

     @SubscribeEvent
     public void invoke(PlayerLoggedOutEvent event) {
          if (event.player.world instanceof WorldServer) {
               PlayerScriptData handler = PlayerData.get(event.player).scriptData;
               EventHooks.onPlayerLogout(handler);
          }
     }

     @SubscribeEvent(
          priority = EventPriority.HIGHEST
     )
     public void invoke(ServerChatEvent event) {
          if (event.getPlayer().world instanceof WorldServer && event.getPlayer() != EntityNPCInterface.ChatEventPlayer) {
               PlayerScriptData handler = PlayerData.get(event.getPlayer()).scriptData;
               String message = event.getMessage();
               PlayerEvent.ChatEvent ev = new PlayerEvent.ChatEvent(handler.getPlayer(), event.getMessage());
               EventHooks.onPlayerChat(handler, ev);
               event.setCanceled(ev.isCanceled());
               if (!message.equals(ev.message)) {
                    TextComponentTranslation chat = new TextComponentTranslation("", new Object[0]);
                    chat.appendSibling(ForgeHooks.newChatWithLinks(ev.message));
                    event.setComponent(chat);
               }

          }
     }

     public ScriptPlayerEventHandler registerForgeEvents() {
          ScriptPlayerEventHandler.ForgeEventHandler handler = new ScriptPlayerEventHandler.ForgeEventHandler();

          try {
               Method m = handler.getClass().getMethod("forgeEntity", Event.class);
               Method register = MinecraftForge.EVENT_BUS.getClass().getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
               register.setAccessible(true);
               List list = new ArrayList(ClassPath.from(this.getClass().getClassLoader()).getTopLevelClassesRecursive("net.minecraftforge.event"));
               list.addAll(ClassPath.from(this.getClass().getClassLoader()).getTopLevelClassesRecursive("net.minecraftforge.fml.common"));
               Iterator var5 = list.iterator();

               while(true) {
                    ClassInfo info;
                    String name;
                    do {
                         if (!var5.hasNext()) {
                              if (PixelmonHelper.Enabled) {
                                   try {
                                        Field f = ClassLoader.class.getDeclaredField("classes");
                                        f.setAccessible(true);
                                        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                                        List classes = new ArrayList((Vector)f.get(classLoader));
                                        Iterator var17 = classes.iterator();

                                        while(var17.hasNext()) {
                                             Class c = (Class)var17.next();
                                             if (c.getName().startsWith("com.pixelmonmod.pixelmon.api.events") && Event.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers()) && Modifier.isPublic(c.getModifiers())) {
                                                  register.invoke(PixelmonHelper.EVENT_BUS, c, handler, m, Loader.instance().activeModContainer());
                                             }
                                        }
                                   } catch (Exception var12) {
                                        var12.printStackTrace();
                                   }

                                   return this;
                              }

                              return this;
                         }

                         info = (ClassInfo)var5.next();
                         name = info.getName();
                    } while(name.startsWith("net.minecraftforge.event.terraingen"));

                    Class infoClass = info.load();
                    List classes = new ArrayList(Arrays.asList(infoClass.getDeclaredClasses()));
                    if (classes.isEmpty()) {
                         classes.add(infoClass);
                    }

                    Iterator var10 = classes.iterator();

                    while(var10.hasNext()) {
                         Class c = (Class)var10.next();
                         if (!GenericEvent.class.isAssignableFrom(c) && !EntityConstructing.class.isAssignableFrom(c) && !PotentialSpawns.class.isAssignableFrom(c) && !RenderTickEvent.class.isAssignableFrom(c) && !ClientTickEvent.class.isAssignableFrom(c) && !GetCollisionBoxesEvent.class.isAssignableFrom(c) && !ClientCustomPacketEvent.class.isAssignableFrom(c) && !ItemTooltipEvent.class.isAssignableFrom(c) && Event.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers()) && Modifier.isPublic(c.getModifiers())) {
                              register.invoke(MinecraftForge.EVENT_BUS, c, handler, m, Loader.instance().activeModContainer());
                         }
                    }
               }
          } catch (Exception var13) {
               var13.printStackTrace();
               return this;
          }
     }

     public class ForgeEventHandler {
          @SubscribeEvent
          public void forgeEntity(Event event) {
               if (CustomNpcs.Server != null && ScriptController.Instance.forgeScripts.isEnabled()) {
                    if (event instanceof EntityEvent) {
                         EntityEvent evx = (EntityEvent)event;
                         if (evx.getEntity() != null && evx.getEntity().world instanceof WorldServer) {
                              EventHooks.onForgeEntityEvent(evx);
                         }
                    } else if (event instanceof WorldEvent) {
                         WorldEvent ev = (WorldEvent)event;
                         if (ev.getWorld() instanceof WorldServer) {
                              EventHooks.onForgeWorldEvent(ev);
                         }
                    } else if (!(event instanceof TickEvent) || ((TickEvent)event).side != Side.CLIENT) {
                         if (event instanceof net.minecraftforge.fml.common.gameevent.PlayerEvent) {
                              net.minecraftforge.fml.common.gameevent.PlayerEvent evxx = (net.minecraftforge.fml.common.gameevent.PlayerEvent)event;
                              if (!(evxx.player.world instanceof WorldServer)) {
                                   return;
                              }
                         }

                         EventHooks.onForgeEvent(new ForgeEvent(event), event);
                    }
               }
          }
     }
}
