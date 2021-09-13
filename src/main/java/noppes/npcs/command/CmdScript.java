package noppes.npcs.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import noppes.npcs.EventHooks;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.WorldEvent;
import noppes.npcs.controllers.ScriptController;

public class CmdScript extends CommandNoppesBase {
     @CommandNoppesBase.SubCommand(
          desc = "Reload scripts and saved data from disks script folder."
     )
     public Boolean reload(MinecraftServer server, ICommandSender sender, String[] args) {
          ScriptController.Instance.loadCategories();
          if (ScriptController.Instance.loadPlayerScripts()) {
               sender.func_145747_a(new TextComponentString("Reload player scripts succesfully"));
          } else {
               sender.func_145747_a(new TextComponentString("Failed reloading player scripts"));
          }

          if (ScriptController.Instance.loadForgeScripts()) {
               sender.func_145747_a(new TextComponentString("Reload forge scripts succesfully"));
          } else {
               sender.func_145747_a(new TextComponentString("Failed reloading forge scripts"));
          }

          if (ScriptController.Instance.loadStoredData()) {
               sender.func_145747_a(new TextComponentString("Reload stored data succesfully"));
          } else {
               sender.func_145747_a(new TextComponentString("Failed reloading stored data"));
          }

          return true;
     }

     @CommandNoppesBase.SubCommand(
          desc = "Runs scriptCommand in the players scripts",
          usage = "[args]"
     )
     public Boolean run(MinecraftServer server, ICommandSender sender, String[] args) {
          IWorld world = NpcAPI.Instance().getIWorld((WorldServer)sender.func_130014_f_());
          BlockPos bpos = sender.func_180425_c();
          IPos pos = NpcAPI.Instance().getIPos((double)bpos.func_177958_n(), (double)bpos.func_177956_o(), (double)bpos.func_177952_p());
          WorldEvent.ScriptCommandEvent event = new WorldEvent.ScriptCommandEvent(world, pos, args);
          EventHooks.onWorldScriptEvent(event);
          return true;
     }

     public String func_71517_b() {
          return "script";
     }

     public String getDescription() {
          return "Commands for scripts";
     }
}
