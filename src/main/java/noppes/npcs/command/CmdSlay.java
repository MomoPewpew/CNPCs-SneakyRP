package noppes.npcs.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.EntityNPCInterface;

public class CmdSlay extends CommandNoppesBase {
     public Map SlayMap = new LinkedHashMap();

     public CmdSlay() {
          this.SlayMap.clear();
          this.SlayMap.put("all", EntityLivingBase.class);
          this.SlayMap.put("mobs", EntityMob.class);
          this.SlayMap.put("animals", EntityAnimal.class);
          this.SlayMap.put("items", EntityItem.class);
          this.SlayMap.put("xporbs", EntityXPOrb.class);
          this.SlayMap.put("npcs", EntityNPCInterface.class);
          Iterator var1 = ForgeRegistries.ENTITIES.getValues().iterator();

          while(var1.hasNext()) {
               EntityEntry ent = (EntityEntry)var1.next();
               String name = ent.getName();
               Class cls = ent.getEntityClass();
               if (!EntityNPCInterface.class.isAssignableFrom(cls) && EntityLivingBase.class.isAssignableFrom(cls)) {
                    this.SlayMap.put(name.toLowerCase(), cls);
               }
          }

          this.SlayMap.remove("monster");
          this.SlayMap.remove("mob");
     }

     public String getName() {
          return "slay";
     }

     public String getDescription() {
          return "Kills given entity within range. Also has all, mobs, animal options. Can have multiple types";
     }

     public String getUsage() {
          return "<type>.. [range]";
     }

     public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          ArrayList toDelete = new ArrayList();
          boolean deleteNPCs = false;
          String[] var6 = args;
          int range = args.length;

          for(int var8 = 0; var8 < range; ++var8) {
               String delete = var6[var8];
               delete = delete.toLowerCase();
               Class cls = (Class)this.SlayMap.get(delete);
               if (cls != null) {
                    toDelete.add(cls);
               }

               if (delete.equals("mobs")) {
                    toDelete.add(EntityGhast.class);
                    toDelete.add(EntityDragon.class);
               }

               if (delete.equals("npcs")) {
                    deleteNPCs = true;
               }
          }

          int count = 0;
          range = 120;

          try {
               range = Integer.parseInt(args[args.length - 1]);
          } catch (NumberFormatException var12) {
          }

          AxisAlignedBB box = (new AxisAlignedBB(sender.getPosition(), sender.getPosition().add(1, 1, 1))).expand((double)range, (double)range, (double)range);
          List list = sender.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, box);
          Iterator var16 = list.iterator();

          while(true) {
               Entity entity;
               do {
                    do {
                         do {
                              if (!var16.hasNext()) {
                                   if (toDelete.contains(EntityXPOrb.class)) {
                                        list = sender.getEntityWorld().getEntitiesWithinAABB(EntityXPOrb.class, box);

                                        for(var16 = list.iterator(); var16.hasNext(); ++count) {
                                             entity = (Entity)var16.next();
                                             entity.field_70128_L = true;
                                        }
                                   }

                                   if (toDelete.contains(EntityItem.class)) {
                                        list = sender.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, box);

                                        for(var16 = list.iterator(); var16.hasNext(); ++count) {
                                             entity = (Entity)var16.next();
                                             entity.field_70128_L = true;
                                        }
                                   }

                                   sender.sendMessage(new TextComponentTranslation(count + " entities deleted", new Object[0]));
                                   return;
                              }

                              entity = (Entity)var16.next();
                         } while(entity instanceof EntityPlayer);
                    } while(entity instanceof EntityTameable && ((EntityTameable)entity).func_70909_n());
               } while(entity instanceof EntityNPCInterface && !deleteNPCs);

               if (this.delete(entity, toDelete)) {
                    ++count;
               }
          }
     }

     private boolean delete(Entity entity, ArrayList toDelete) {
          Iterator var3 = toDelete.iterator();

          Class delete;
          do {
               do {
                    if (!var3.hasNext()) {
                         return false;
                    }

                    delete = (Class)var3.next();
               } while(delete == EntityAnimal.class && entity instanceof EntityHorse);
          } while(!delete.isAssignableFrom(entity.getClass()));

          entity.field_70128_L = true;
          return true;
     }

     public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
          return CommandBase.func_71530_a(args, (String[])this.SlayMap.keySet().toArray(new String[this.SlayMap.size()]));
     }
}
