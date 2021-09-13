package noppes.npcs.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;
import org.apache.commons.lang3.ArrayUtils;

public class CmdNPC extends CommandNoppesBase {
     public EntityNPCInterface selectedNpc;

     public String func_71517_b() {
          return "npc";
     }

     public String getDescription() {
          return "NPC operation";
     }

     public String getUsage() {
          return "<name> <command>";
     }

     public boolean runSubCommands() {
          return false;
     }

     public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          String npcname = args[0].replace("%", " ");
          String command = args[1];
          args = (String[])Arrays.copyOfRange(args, 2, args.length);
          if (command.equalsIgnoreCase("create")) {
               args = (String[])ArrayUtils.add(args, 0, npcname);
               this.executeSub(server, sender, command, args);
          } else {
               List list = this.getEntities(EntityNPCInterface.class, sender.func_130014_f_(), sender.func_180425_c(), 80);
               Iterator var7 = list.iterator();

               while(true) {
                    EntityNPCInterface npc;
                    String name;
                    do {
                         do {
                              if (!var7.hasNext()) {
                                   if (this.selectedNpc == null) {
                                        throw new CommandException("Npc '%s' was not found", new Object[]{npcname});
                                   }

                                   this.executeSub(server, sender, command, args);
                                   this.selectedNpc = null;
                                   return;
                              }

                              npc = (EntityNPCInterface)var7.next();
                              name = npc.display.getName().replace(" ", "_");
                         } while(!name.equalsIgnoreCase(npcname));
                    } while(this.selectedNpc != null && this.selectedNpc.func_174818_b(sender.func_180425_c()) <= npc.func_174818_b(sender.func_180425_c()));

                    this.selectedNpc = npc;
               }
          }
     }

     @CommandNoppesBase.SubCommand(
          desc = "Set Home (respawn place)",
          usage = "[x] [y] [z]",
          permission = 2
     )
     public void home(MinecraftServer server, ICommandSender sender, String[] args) {
          BlockPos pos = sender.func_180425_c();
          if (args.length == 3) {
               try {
                    pos = CommandBase.func_175757_a(sender, args, 0, false);
               } catch (NumberInvalidException var6) {
               }
          }

          this.selectedNpc.ais.setStartPos(pos);
     }

     @CommandNoppesBase.SubCommand(
          desc = "Set NPC visibility",
          usage = "[true/false/semi]",
          permission = 2
     )
     public void visible(MinecraftServer server, ICommandSender sender, String[] args) {
          if (args.length >= 1) {
               boolean bo = args[0].equalsIgnoreCase("true");
               boolean semi = args[0].equalsIgnoreCase("semi");
               int current = this.selectedNpc.display.getVisible();
               if (semi) {
                    this.selectedNpc.display.setVisible(2);
               } else if (bo) {
                    this.selectedNpc.display.setVisible(0);
               } else {
                    this.selectedNpc.display.setVisible(1);
               }

          }
     }

     @CommandNoppesBase.SubCommand(
          desc = "Delete an NPC"
     )
     public void delete(MinecraftServer server, ICommandSender sender, String[] args) {
          this.selectedNpc.delete();
     }

     @CommandNoppesBase.SubCommand(
          desc = "Sets the owner of an follower/companion",
          usage = "[player]",
          permission = 2
     )
     public void owner(MinecraftServer server, ICommandSender sender, String[] args) {
          if (args.length < 1) {
               EntityPlayer player = null;
               if (this.selectedNpc.roleInterface instanceof RoleFollower) {
                    player = ((RoleFollower)this.selectedNpc.roleInterface).owner;
               }

               if (this.selectedNpc.roleInterface instanceof RoleCompanion) {
                    player = ((RoleCompanion)this.selectedNpc.roleInterface).owner;
               }

               if (player == null) {
                    this.sendMessage(sender, "No owner", new Object[0]);
               } else {
                    this.sendMessage(sender, "Owner is: " + player.func_70005_c_(), new Object[0]);
               }
          } else {
               EntityPlayerMP player = null;

               try {
                    player = CommandBase.func_184888_a(server, sender, args[0]);
               } catch (PlayerNotFoundException var6) {
               } catch (CommandException var7) {
               }

               if (this.selectedNpc.roleInterface instanceof RoleFollower) {
                    ((RoleFollower)this.selectedNpc.roleInterface).setOwner(player);
               }

               if (this.selectedNpc.roleInterface instanceof RoleCompanion) {
                    ((RoleCompanion)this.selectedNpc.roleInterface).setOwner(player);
               }
          }

     }

     @CommandNoppesBase.SubCommand(
          desc = "Set NPC name",
          usage = "[name]",
          permission = 2
     )
     public void name(MinecraftServer server, ICommandSender sender, String[] args) {
          if (args.length >= 1) {
               String name = args[0];

               for(int i = 1; i < args.length; ++i) {
                    name = name + " " + args[i];
               }

               if (!this.selectedNpc.display.getName().equals(name)) {
                    this.selectedNpc.display.setName(name);
                    this.selectedNpc.updateClient = true;
               }

          }
     }

     @CommandNoppesBase.SubCommand(
          desc = "Resets the npc",
          usage = "[name]",
          permission = 2
     )
     public void reset(MinecraftServer server, ICommandSender sender, String[] args) {
          this.selectedNpc.reset();
     }

     @CommandNoppesBase.SubCommand(
          desc = "Creates an NPC",
          usage = "[name]"
     )
     public void create(MinecraftServer server, ICommandSender sender, String[] args) {
          World pw = sender.func_130014_f_();
          EntityCustomNpc npc = new EntityCustomNpc(pw);
          if (args.length > 0) {
               npc.display.setName(args[0]);
          }

          BlockPos pos = sender.func_180425_c();
          npc.func_70080_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), 0.0F, 0.0F);
          npc.ais.setStartPos(pos);
          pw.func_72838_d(npc);
          npc.func_70606_j(npc.func_110138_aP());
     }

     public List func_184883_a(MinecraftServer server, ICommandSender par1, String[] args, BlockPos pos) {
          if (args.length == 2) {
               return CommandBase.func_71530_a(args, new String[]{"create", "home", "visible", "delete", "owner", "name"});
          } else {
               return args.length == 3 && args[1].equalsIgnoreCase("owner") ? CommandBase.func_71530_a(args, server.func_71213_z()) : null;
          }
     }

     public int func_82362_a() {
          return 4;
     }

     public List getEntities(Class cls, World world, BlockPos pos, int range) {
          return world.func_72872_a(cls, (new AxisAlignedBB(pos, pos.func_177982_a(1, 1, 1))).func_72314_b((double)range, (double)range, (double)range));
     }
}
