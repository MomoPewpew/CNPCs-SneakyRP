package noppes.npcs.client;

import io.netty.buffer.ByteBuf;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Util;
import net.minecraft.util.Util.EnumOS;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.client.gui.player.GuiDialogInteract;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.Sys;

public class NoppesUtil {
     private static EntityNPCInterface lastNpc;
     private static HashMap data = new HashMap();

     public static void requestOpenGUI(EnumGuiType gui) {
          requestOpenGUI(gui, 0, 0, 0);
     }

     public static void requestOpenGUI(EnumGuiType gui, int i, int j, int k) {
          Client.sendData(EnumPacketServer.Gui, gui.ordinal(), i, j, k);
     }

     public static void spawnParticle(ByteBuf buffer) throws IOException {
          double posX = buffer.readDouble();
          double posY = buffer.readDouble();
          double posZ = buffer.readDouble();
          float height = buffer.readFloat();
          float width = buffer.readFloat();
          String particle = Server.readString(buffer);
          World world = Minecraft.getMinecraft().field_71441_e;
          Random rand = world.rand;
          if (particle.equals("heal")) {
               for(int k = 0; k < 6; ++k) {
                    world.func_175688_a(EnumParticleTypes.SPELL_INSTANT, posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.func_175688_a(EnumParticleTypes.SPELL, posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D, new int[0]);
               }
          }

     }

     public static EntityNPCInterface getLastNpc() {
          return lastNpc;
     }

     public static void setLastNpc(EntityNPCInterface npc) {
          lastNpc = npc;
     }

     public static void openGUI(EntityPlayer player, Object guiscreen) {
          CustomNpcs.proxy.openGui(player, guiscreen);
     }

     public static void openFolder(File dir) {
          String s = dir.getAbsolutePath();
          if (Util.func_110647_a() == EnumOS.OSX) {
               try {
                    Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                    return;
               } catch (IOException var7) {
               }
          } else if (Util.func_110647_a() == EnumOS.WINDOWS) {
               String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);

               try {
                    Runtime.getRuntime().exec(s1);
                    return;
               } catch (IOException var6) {
               }
          }

          boolean flag = false;

          try {
               Class oclass = Class.forName("java.awt.Desktop");
               Object object = oclass.getMethod("getDesktop").invoke((Object)null);
               oclass.getMethod("browse", URI.class).invoke(object, dir.toURI());
          } catch (Throwable var5) {
               flag = true;
          }

          if (flag) {
               Sys.openURL("file://" + s);
          }

     }

     public static void setScrollList(ByteBuf buffer) {
          GuiScreen gui = Minecraft.getMinecraft().field_71462_r;
          if (gui instanceof GuiNPCInterface && ((GuiNPCInterface)gui).hasSubGui()) {
               gui = ((GuiNPCInterface)gui).getSubGui();
          }

          if (gui != null && gui instanceof IScrollData) {
               Vector data = new Vector();

               try {
                    int size = buffer.readInt();

                    for(int i = 0; i < size; ++i) {
                         data.add(Server.readString(buffer));
                    }
               } catch (Exception var6) {
               }

               ((IScrollData)gui).setData(data, (HashMap)null);
          }
     }

     public static void addScrollData(ByteBuf buffer) {
          try {
               int size = buffer.readInt();

               for(int i = 0; i < size; ++i) {
                    int id = buffer.readInt();
                    String name = Server.readString(buffer);
                    data.put(name, id);
               }
          } catch (Exception var5) {
          }

     }

     public static void setScrollData(ByteBuf buffer) {
          GuiScreen gui = Minecraft.getMinecraft().field_71462_r;
          if (gui != null) {
               try {
                    int size = buffer.readInt();

                    for(int i = 0; i < size; ++i) {
                         int id = buffer.readInt();
                         String name = Server.readString(buffer);
                         data.put(name, id);
                    }
               } catch (Exception var6) {
               }

               if (gui instanceof GuiNPCInterface && ((GuiNPCInterface)gui).hasSubGui()) {
                    gui = ((GuiNPCInterface)gui).getSubGui();
               }

               if (gui instanceof GuiContainerNPCInterface && ((GuiContainerNPCInterface)gui).hasSubGui()) {
                    gui = ((GuiContainerNPCInterface)gui).getSubGui();
               }

               if (gui instanceof IScrollData) {
                    ((IScrollData)gui).setData(new Vector(data.keySet()), data);
               }

               data = new HashMap();
          }
     }

     public static void openDialog(Dialog dialog, EntityNPCInterface npc, EntityPlayer player) {
          GuiScreen gui = Minecraft.getMinecraft().field_71462_r;
          if (gui != null && gui instanceof GuiDialogInteract) {
               GuiDialogInteract dia = (GuiDialogInteract)gui;
               dia.appendDialog(dialog);
          } else {
               CustomNpcs.proxy.openGui((EntityPlayer)player, (Object)(new GuiDialogInteract(npc, dialog)));
          }

     }

     public static void clickSound() {
          Minecraft.getMinecraft().func_147118_V().func_147682_a(PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0F));
     }
}
