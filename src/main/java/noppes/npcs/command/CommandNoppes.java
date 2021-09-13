package noppes.npcs.command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.api.CustomNPCsException;

public class CommandNoppes extends CommandBase {
     public Map map = new HashMap();
     public CmdHelp help = new CmdHelp(this);

     public CommandNoppes() {
          this.registerCommand(this.help);
          this.registerCommand(new CmdScript());
          this.registerCommand(new CmdScene());
          this.registerCommand(new CmdSlay());
          this.registerCommand(new CmdQuest());
          this.registerCommand(new CmdDialog());
          this.registerCommand(new CmdSchematics());
          this.registerCommand(new CmdFaction());
          this.registerCommand(new CmdNPC());
          this.registerCommand(new CmdClone());
          this.registerCommand(new CmdConfig());
          this.registerCommand(new CmdMark());
     }

     public void registerCommand(CommandNoppesBase command) {
          String name = command.func_71517_b().toLowerCase();
          if (this.map.containsKey(name)) {
               throw new CustomNPCsException("Already a subcommand with the name: " + name, new Object[0]);
          } else {
               this.map.put(name, command);
          }
     }

     public String func_71517_b() {
          return "noppes";
     }

     public String func_71518_a(ICommandSender sender) {
          return "Use as /noppes subcommand";
     }

     public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          if (args.length == 0) {
               this.help.func_184881_a(server, sender, args);
          } else {
               CommandNoppesBase command = this.getCommand(args);
               if (command == null) {
                    throw new CommandException("Unknown command " + args[0], new Object[0]);
               } else {
                    args = (String[])Arrays.copyOfRange(args, 1, args.length);
                    if (!command.subcommands.isEmpty() && command.runSubCommands()) {
                         if (args.length == 0) {
                              this.help.func_184881_a(server, sender, new String[]{command.func_71517_b()});
                         } else {
                              command.executeSub(server, sender, args[0], (String[])Arrays.copyOfRange(args, 1, args.length));
                         }
                    } else if (!sender.canUseCommand(command.func_82362_a(), "commands.noppes." + command.func_71517_b().toLowerCase())) {
                         throw new CommandException("You are not allowed to use this command", new Object[0]);
                    } else {
                         command.canRun(server, sender, command.getUsage(), args);
                         command.func_184881_a(server, sender, args);
                    }
               }
          }
     }

     public List func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
          if (args.length == 1) {
               return CommandBase.func_175762_a(args, this.map.keySet());
          } else {
               CommandNoppesBase command = this.getCommand(args);
               if (command == null) {
                    return null;
               } else if (args.length == 2 && command.runSubCommands()) {
                    return CommandBase.func_175762_a(args, command.subcommands.keySet());
               } else {
                    String[] useArgs = command.getUsage().split(" ");
                    if (command.runSubCommands()) {
                         Method m = (Method)command.subcommands.get(args[1].toLowerCase());
                         if (m != null) {
                              useArgs = ((CommandNoppesBase.SubCommand)m.getAnnotation(CommandNoppesBase.SubCommand.class)).usage().split(" ");
                         }
                    }

                    if (args.length <= useArgs.length + 2) {
                         String usage = useArgs[args.length - 3];
                         if (usage.equals("<player>") || usage.equals("[player]")) {
                              return CommandBase.func_71530_a(args, server.func_71213_z());
                         }
                    }

                    return command.func_184883_a(server, sender, (String[])Arrays.copyOfRange(args, 1, args.length), pos);
               }
          }
     }

     public CommandNoppesBase getCommand(String[] args) {
          return args.length == 0 ? null : (CommandNoppesBase)this.map.get(args[0].toLowerCase());
     }

     public int func_82362_a() {
          return 2;
     }
}
