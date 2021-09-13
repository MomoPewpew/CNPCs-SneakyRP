package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

public class VersionChecker extends Thread {
     public void run() {
          String name = "§2CustomNpcs§f";
          String link = "§9§nClick here";
          String text = name + " installed. For more info " + link;

          EntityPlayerSP player;
          try {
               player = Minecraft.func_71410_x().field_71439_g;
          } catch (NoSuchMethodError var7) {
               return;
          }

          while((player = Minecraft.func_71410_x().field_71439_g) == null) {
               try {
                    Thread.sleep(2000L);
               } catch (InterruptedException var6) {
                    var6.printStackTrace();
               }
          }

          TextComponentTranslation message = new TextComponentTranslation(text, new Object[0]);
          message.func_150256_b().func_150241_a(new ClickEvent(Action.OPEN_URL, "http://www.kodevelopment.nl/minecraft/customnpcs/"));
          player.func_145747_a(message);
     }
}
