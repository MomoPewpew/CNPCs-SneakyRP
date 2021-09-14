package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.util.ValueUtil;

public class DataScenes {
     private EntityNPCInterface npc;
     public List scenes = new ArrayList();
     public static Map StartedScenes = new HashMap();
     public static List ScenesToRun = new ArrayList();
     private EntityLivingBase owner = null;
     private String ownerScene = null;

     public DataScenes(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          NBTTagList list = new NBTTagList();
          Iterator var3 = this.scenes.iterator();

          while(var3.hasNext()) {
               DataScenes.SceneContainer scene = (DataScenes.SceneContainer)var3.next();
               list.appendTag(scene.writeToNBT(new NBTTagCompound()));
          }

          compound.setTag("Scenes", list);
          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          NBTTagList list = compound.getTagList("Scenes", 10);
          List scenes = new ArrayList();

          for(int i = 0; i < list.tagCount(); ++i) {
               DataScenes.SceneContainer scene = new DataScenes.SceneContainer();
               scene.readFromNBT(list.getCompoundTagAt(i));
               scenes.add(scene);
          }

          this.scenes = scenes;
     }

     public EntityLivingBase getOwner() {
          return this.owner;
     }

     public static void Toggle(ICommandSender sender, String id) {
          DataScenes.SceneState state = (DataScenes.SceneState)StartedScenes.get(id.toLowerCase());
          if (state != null && !state.paused) {
               state.paused = true;
               NoppesUtilServer.NotifyOPs("Paused scene %s at %s", id, state.ticks);
          } else {
               Start(sender, id);
          }

     }

     public static void Start(ICommandSender sender, String id) {
          DataScenes.SceneState state = (DataScenes.SceneState)StartedScenes.get(id.toLowerCase());
          if (state == null) {
               NoppesUtilServer.NotifyOPs("Started scene %s", id);
               StartedScenes.put(id.toLowerCase(), new DataScenes.SceneState());
          } else if (state.paused) {
               state.paused = false;
               NoppesUtilServer.NotifyOPs("Started scene %s from %s", id, state.ticks);
          }

     }

     public static void Pause(ICommandSender sender, String id) {
          if (id == null) {
               DataScenes.SceneState state;
               for(Iterator var2 = StartedScenes.values().iterator(); var2.hasNext(); state.paused = true) {
                    state = (DataScenes.SceneState)var2.next();
               }

               NoppesUtilServer.NotifyOPs("Paused all scenes");
          } else {
               DataScenes.SceneState state = (DataScenes.SceneState)StartedScenes.get(id.toLowerCase());
               state.paused = true;
               NoppesUtilServer.NotifyOPs("Paused scene %s at %s", id, state.ticks);
          }

     }

     public static void Reset(ICommandSender sender, String id) {
          if (id == null) {
               if (StartedScenes.isEmpty()) {
                    return;
               }

               StartedScenes = new HashMap();
               NoppesUtilServer.NotifyOPs("Reset all scene");
          } else if (StartedScenes.remove(id.toLowerCase()) == null) {
               sender.sendMessage(new TextComponentTranslation("Unknown scene %s ", new Object[]{id}));
          } else {
               NoppesUtilServer.NotifyOPs("Reset scene %s", id);
          }

     }

     public void update() {
          Iterator var1 = this.scenes.iterator();

          while(var1.hasNext()) {
               DataScenes.SceneContainer scene = (DataScenes.SceneContainer)var1.next();
               if (scene.validState()) {
                    ScenesToRun.add(scene);
               }
          }

          if (this.owner != null && !StartedScenes.containsKey(this.ownerScene.toLowerCase())) {
               this.owner = null;
               this.ownerScene = null;
          }

     }

     public void addScene(String name) {
          if (!name.isEmpty()) {
               DataScenes.SceneContainer scene = new DataScenes.SceneContainer();
               scene.name = name;
               this.scenes.add(scene);
          }
     }

     public static enum SceneType {
          ANIMATE,
          MOVE,
          FACTION,
          COMMAND,
          EQUIP,
          THROW,
          ATTACK,
          FOLLOW,
          SAY,
          ROTATE,
          STATS;
     }

     public static class SceneEvent implements Comparable {
          public int ticks = 0;
          public DataScenes.SceneType type;
          public String param = "";

          public String toString() {
               return this.ticks + " " + this.type.name() + " " + this.param;
          }

          public static DataScenes.SceneEvent parse(String str) {
               DataScenes.SceneEvent event = new DataScenes.SceneEvent();
               int i = str.indexOf(" ");
               if (i <= 0) {
                    return null;
               } else {
                    try {
                         event.ticks = Integer.parseInt(str.substring(0, i));
                         str = str.substring(i + 1);
                    } catch (NumberFormatException var8) {
                         return null;
                    }

                    i = str.indexOf(" ");
                    if (i <= 0) {
                         return null;
                    } else {
                         String name = str.substring(0, i);
                         DataScenes.SceneType[] var4 = DataScenes.SceneType.values();
                         int var5 = var4.length;

                         for(int var6 = 0; var6 < var5; ++var6) {
                              DataScenes.SceneType type = var4[var6];
                              if (name.equalsIgnoreCase(type.name())) {
                                   event.type = type;
                              }
                         }

                         if (event.type == null) {
                              return null;
                         } else {
                              event.param = str.substring(i + 1);
                              return event;
                         }
                    }
               }
          }

          public int compareTo(DataScenes.SceneEvent o) {
               return this.ticks - o.ticks;
          }
     }

     public class SceneContainer {
          public int btn = 0;
          public String name = "";
          public String lines = "";
          public boolean enabled = false;
          public int ticks = -1;
          private DataScenes.SceneState state = null;
          private List events = new ArrayList();

          public NBTTagCompound writeToNBT(NBTTagCompound compound) {
               compound.setBoolean("Enabled", this.enabled);
               compound.setString("Name", this.name);
               compound.setString("Lines", this.lines);
               compound.setInteger("Button", this.btn);
               compound.setInteger("Ticks", this.ticks);
               return compound;
          }

          public boolean validState() {
               if (!this.enabled) {
                    return false;
               } else {
                    if (this.state != null) {
                         if (DataScenes.StartedScenes.containsValue(this.state)) {
                              return !this.state.paused;
                         }

                         this.state = null;
                    }

                    this.state = (DataScenes.SceneState)DataScenes.StartedScenes.get(this.name.toLowerCase());
                    if (this.state == null) {
                         this.state = (DataScenes.SceneState)DataScenes.StartedScenes.get(this.btn + "btn");
                    }

                    if (this.state != null) {
                         return !this.state.paused;
                    } else {
                         return false;
                    }
               }
          }

          public void readFromNBT(NBTTagCompound compound) {
               this.enabled = compound.getBoolean("Enabled");
               this.name = compound.getString("Name");
               this.lines = compound.getString("Lines");
               this.btn = compound.getInteger("Button");
               this.ticks = compound.getInteger("Ticks");
               ArrayList events = new ArrayList();
               String[] var3 = this.lines.split("\r\n|\r|\n");
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                    String line = var3[var5];
                    DataScenes.SceneEvent event = DataScenes.SceneEvent.parse(line);
                    if (event != null) {
                         events.add(event);
                    }
               }

               Collections.sort(events);
               this.events = events;
          }

          public void update() {
               if (this.enabled && !this.events.isEmpty() && this.state != null) {
                    Iterator var1 = this.events.iterator();

                    while(var1.hasNext()) {
                         DataScenes.SceneEvent event = (DataScenes.SceneEvent)var1.next();
                         if (event.ticks > this.state.ticks) {
                              break;
                         }

                         if (event.ticks == this.state.ticks) {
                              try {
                                   this.handle(event);
                              } catch (Exception var4) {
                              }
                         }
                    }

                    this.ticks = this.state.ticks;
               }
          }

          private void handle(DataScenes.SceneEvent event) throws Exception {
               String[] args;
               if (event.type == DataScenes.SceneType.MOVE) {
                    args = event.param.split(" ");

                    while(args.length > 1) {
                         boolean move = false;
                         if (args[0].startsWith("to")) {
                              move = true;
                         } else if (!args[0].startsWith("tp")) {
                              break;
                         }

                         BlockPos pos = null;
                         if (args[0].startsWith("@")) {
                              EntityLivingBase entitylivingbase = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc.getServer(), DataScenes.this.npc, args[0], EntityLivingBase.class);
                              if (entitylivingbase != null) {
                                   pos = entitylivingbase.getPosition();
                              }

                              args = (String[])Arrays.copyOfRange(args, 2, args.length);
                         } else {
                              if (args.length < 4) {
                                   return;
                              }

                              pos = CommandBase.parseBlockPos(DataScenes.this.npc, args, 1, false);
                              args = (String[])Arrays.copyOfRange(args, 4, args.length);
                         }

                         if (pos != null) {
                              DataScenes.this.npc.ais.setStartPos(pos);
                              DataScenes.this.npc.getNavigator().clearPath();
                              if (move) {
                                   Path pathentity = DataScenes.this.npc.getNavigator().getResourcePathToPos(pos);
                                   DataScenes.this.npc.getNavigator().setPath(pathentity, 1.0D);
                              } else if (!DataScenes.this.npc.isInRange((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, 2.0D)) {
                                   DataScenes.this.npc.setPosition((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
                              }
                         }
                    }
               } else if (event.type == DataScenes.SceneType.SAY) {
                    DataScenes.this.npc.saySurrounding(new Line(event.param));
               } else {
                    EntityLivingBase entity;
                    if (event.type == DataScenes.SceneType.ROTATE) {
                         DataScenes.this.npc.lookAi.resetTask();
                         if (event.param.startsWith("@")) {
                              entity = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc.getServer(), DataScenes.this.npc, event.param, EntityLivingBase.class);
                              DataScenes.this.npc.lookAi.rotate(DataScenes.this.npc.world.getClosestPlayerToEntity(entity, 30.0D));
                         } else {
                              DataScenes.this.npc.lookAi.rotate(Integer.parseInt(event.param));
                         }
                    } else if (event.type == DataScenes.SceneType.EQUIP) {
                         args = event.param.split(" ");
                         if (args.length < 2) {
                              return;
                         }

                         IItemStack itemstack = null;
                         if (!args[1].equalsIgnoreCase("none")) {
                              Item item = CommandBase.getItemByText(DataScenes.this.npc, args[1]);
                              int i = args.length >= 3 ? CommandBase.parseInt(args[2], 1, 64) : 1;
                              int j = args.length >= 4 ? CommandBase.parseInt(args[3]) : 0;
                              itemstack = NpcAPI.Instance().getIItemStack(new ItemStack(item, i, j));
                         }

                         if (args[0].equalsIgnoreCase("main")) {
                              DataScenes.this.npc.inventory.weapons.put(0, itemstack);
                         } else if (args[0].equalsIgnoreCase("off")) {
                              DataScenes.this.npc.inventory.weapons.put(2, itemstack);
                         } else if (args[0].equalsIgnoreCase("proj")) {
                              DataScenes.this.npc.inventory.weapons.put(1, itemstack);
                         } else if (args[0].equalsIgnoreCase("head")) {
                              DataScenes.this.npc.inventory.armor.put(0, itemstack);
                         } else if (args[0].equalsIgnoreCase("body")) {
                              DataScenes.this.npc.inventory.armor.put(1, itemstack);
                         } else if (args[0].equalsIgnoreCase("legs")) {
                              DataScenes.this.npc.inventory.armor.put(2, itemstack);
                         } else if (args[0].equalsIgnoreCase("boots")) {
                              DataScenes.this.npc.inventory.armor.put(3, itemstack);
                         }
                    } else if (event.type == DataScenes.SceneType.ATTACK) {
                         if (event.param.equals("none")) {
                              DataScenes.this.npc.setAttackTarget((EntityLivingBase)null);
                         } else {
                              entity = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc.getServer(), DataScenes.this.npc, event.param, EntityLivingBase.class);
                              if (entity != null) {
                                   DataScenes.this.npc.setAttackTarget(entity);
                              }
                         }
                    } else if (event.type == DataScenes.SceneType.THROW) {
                         args = event.param.split(" ");
                         EntityLivingBase entityx = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc.getServer(), DataScenes.this.npc, args[0], EntityLivingBase.class);
                         if (entityx == null) {
                              return;
                         }

                         float damage = Float.parseFloat(args[1]);
                         if (damage <= 0.0F) {
                              damage = 0.01F;
                         }

                         ItemStack stack = ItemStackWrapper.MCItem(DataScenes.this.npc.inventory.getProjectile());
                         if (args.length > 2) {
                              Item itemx = CommandBase.getItemByText(DataScenes.this.npc, args[2]);
                              stack = new ItemStack(itemx, 1, 0);
                         }

                         EntityProjectile projectile = DataScenes.this.npc.shoot(entityx, 100, stack, false);
                         projectile.damage = damage;
                    } else if (event.type == DataScenes.SceneType.ANIMATE) {
                         DataScenes.this.npc.animateAi.temp = 0;
                         if (event.param.equalsIgnoreCase("sleep")) {
                              DataScenes.this.npc.animateAi.temp = 2;
                         } else if (event.param.equalsIgnoreCase("sneak")) {
                              DataScenes.this.npc.ais.animationType = 4;
                         } else if (event.param.equalsIgnoreCase("normal")) {
                              DataScenes.this.npc.ais.animationType = 0;
                         } else if (event.param.equalsIgnoreCase("sit")) {
                              DataScenes.this.npc.animateAi.temp = 1;
                         } else if (event.param.equalsIgnoreCase("crawl")) {
                              DataScenes.this.npc.ais.animationType = 7;
                         } else if (event.param.equalsIgnoreCase("bow")) {
                              DataScenes.this.npc.animateAi.temp = 11;
                         } else if (event.param.equalsIgnoreCase("yes")) {
                              DataScenes.this.npc.animateAi.temp = 13;
                         } else if (event.param.equalsIgnoreCase("no")) {
                              DataScenes.this.npc.animateAi.temp = 12;
                         }
                    } else if (event.type == DataScenes.SceneType.COMMAND) {
                         NoppesUtilServer.runCommand(DataScenes.this.npc, DataScenes.this.npc.getName(), event.param, (EntityPlayer)null);
                    } else if (event.type == DataScenes.SceneType.STATS) {
                         int ix = event.param.indexOf(" ");
                         if (ix <= 0) {
                              return;
                         }

                         String type = event.param.substring(0, ix).toLowerCase();
                         String value = event.param.substring(ix).trim();

                         try {
                              if (type.equals("walking_speed")) {
                                   DataScenes.this.npc.ais.setWalkingSpeed(ValueUtil.CorrectInt(Integer.parseInt(value), 0, 10));
                              } else if (type.equals("size")) {
                                   DataScenes.this.npc.display.setSize(ValueUtil.CorrectInt(Integer.parseInt(value), 1, 30));
                              } else {
                                   NoppesUtilServer.NotifyOPs("Unknown scene stat: " + type);
                              }
                         } catch (NumberFormatException var7) {
                              NoppesUtilServer.NotifyOPs("Unknown scene stat " + type + " value: " + value);
                         }
                    } else if (event.type == DataScenes.SceneType.FACTION) {
                         DataScenes.this.npc.setFaction(Integer.parseInt(event.param));
                    } else if (event.type == DataScenes.SceneType.FOLLOW) {
                         if (event.param.equalsIgnoreCase("none")) {
                              DataScenes.this.owner = null;
                              DataScenes.this.ownerScene = null;
                         } else {
                              entity = (EntityLivingBase)CommandBase.getEntity(DataScenes.this.npc.getServer(), DataScenes.this.npc, event.param, EntityLivingBase.class);
                              if (entity == null) {
                                   return;
                              }

                              DataScenes.this.owner = entity;
                              DataScenes.this.ownerScene = this.name;
                         }
                    }
               }

          }
     }

     public static class SceneState {
          public boolean paused = false;
          public int ticks = -1;
     }
}
