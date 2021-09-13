package noppes.npcs.command;

import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.api.CommandNoppesBase;
import noppes.npcs.controllers.data.MarkData;

public class CmdMark extends CommandNoppesBase {
     public String func_71517_b() {
          return "mark";
     }

     public String getDescription() {
          return "Mark operations";
     }

     @CommandNoppesBase.SubCommand(
          desc = "Set mark (warning overrides existing marks)",
          usage = "<@e> <type> [color]"
     )
     public void set(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          List list = func_184890_c(server, sender, args[0]);
          int type = 0;

          try {
               type = Integer.parseInt(args[1]);
          } catch (Exception var11) {
          }

          int color = 16777215;
          if (args.length > 2) {
               try {
                    color = Integer.parseInt(args[2], 16);
               } catch (Exception var10) {
               }
          }

          Iterator var7 = list.iterator();

          while(var7.hasNext()) {
               Entity e = (Entity)var7.next();
               if (e instanceof EntityLivingBase) {
                    MarkData data = MarkData.get((EntityLivingBase)e);
                    data.marks.clear();
                    data.addMark(type, color);
               }
          }

     }

     @CommandNoppesBase.SubCommand(
          desc = "Clear mark",
          usage = "<@e>"
     )
     public void clear(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
          List list = func_184890_c(server, sender, args[0]);
          Iterator var5 = list.iterator();

          while(var5.hasNext()) {
               Entity e = (Entity)var5.next();
               if (e instanceof EntityLivingBase) {
                    MarkData data = MarkData.get((EntityLivingBase)e);
                    data.marks.clear();
                    data.syncClients();
               }
          }

     }
}
