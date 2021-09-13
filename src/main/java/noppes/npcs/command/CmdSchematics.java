package noppes.npcs.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.schematics.SchematicWrapper;

public class CmdSchematics extends CommandNoppesBase {
     public String func_71517_b() {
          return "schema";
     }

     public String getDescription() {
          return "Schematic operation";
     }

     @CommandNoppesBase.SubCommand(
          desc = "Build the schematic",
          usage = "<name> [rotation] [[world:]x,y,z]]",
          permission = 4
     )
     public void build(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          String name = args[0];
          SchematicWrapper schem = SchematicController.Instance.load(name);
          if (schem == null) {
               throw new CommandException("Unknown schematic: " + name, new Object[0]);
          } else {
               this.sendMessage(sender, "width: " + schem.schema.getWidth() + ", length: " + schem.schema.getLength() + ", height: " + schem.schema.getHeight(), new Object[0]);
               BlockPos pos = sender.func_180425_c();
               World world = sender.func_130014_f_();
               int rotation = 0;
               if (args.length > 1) {
                    try {
                         rotation = Integer.parseInt(args[1]);
                    } catch (NumberFormatException var13) {
                    }
               }

               if (args.length > 2) {
                    String location = args[2];
                    String[] par;
                    if (location.contains(":")) {
                         par = location.split(":");
                         location = par[1];
                         world = this.getWorld(server, par[0]);
                         if (world == null) {
                              throw new CommandException("'%s' is an unknown world", new Object[]{par[0]});
                         }
                    }

                    if (location.contains(",")) {
                         par = location.split(",");
                         if (par.length != 3) {
                              throw new CommandException("Location should be x,y,z", new Object[0]);
                         }

                         try {
                              pos = CommandBase.func_175757_a(sender, par, 0, false);
                         } catch (NumberInvalidException var12) {
                              throw new CommandException("Location should be in numbers", new Object[0]);
                         }
                    }
               }

               if (pos.func_177958_n() == 0 && pos.func_177956_o() == 0 && pos.func_177952_p() == 0) {
                    throw new CommandException("Location needed", new Object[0]);
               } else {
                    schem.init(pos, world, rotation);
                    SchematicController.Instance.build(schem, sender);
               }
          }
     }

     @CommandNoppesBase.SubCommand(
          desc = "Stops the current build",
          permission = 4
     )
     public void stop(MinecraftServer server, ICommandSender sender, String[] args) {
          SchematicController.Instance.stop(sender);
     }

     @CommandNoppesBase.SubCommand(
          desc = "Gives info about the current build",
          permission = 4
     )
     public void info(MinecraftServer server, ICommandSender sender, String[] args) {
          SchematicController.Instance.info(sender);
     }

     @CommandNoppesBase.SubCommand(
          desc = "Lists available schematics",
          permission = 4
     )
     public void list(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          List list = SchematicController.Instance.list();
          if (list.isEmpty()) {
               throw new CommandException("No available schematics", new Object[0]);
          } else {
               String s = "";

               String file;
               for(Iterator var6 = list.iterator(); var6.hasNext(); s = s + file + ", ") {
                    file = (String)var6.next();
               }

               this.sendMessage(sender, s, new Object[0]);
          }
     }

     public List func_184883_a(MinecraftServer server, ICommandSender par1, String[] args, BlockPos pos) {
          if (args[0].equalsIgnoreCase("build") && args.length == 2) {
               List list = SchematicController.Instance.list();
               return CommandBase.func_71530_a(args, (String[])list.toArray(new String[list.size()]));
          } else {
               return null;
          }
     }

     public World getWorld(MinecraftServer server, String t) {
          WorldServer[] ws = server.field_71305_c;
          WorldServer[] var4 = ws;
          int var5 = ws.length;

          for(int var6 = 0; var6 < var5; ++var6) {
               WorldServer w = var4[var6];
               if (w != null && (w.field_73011_w.getDimension() + "").equalsIgnoreCase(t)) {
                    return w;
               }
          }

          return null;
     }
}
