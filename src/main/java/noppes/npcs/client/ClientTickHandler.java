package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.player.GuiQuestLog;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumPlayerPacket;
import org.lwjgl.input.Keyboard;

public class ClientTickHandler {
     private World prevWorld;
     private boolean otherContainer = false;
     private int buttonPressed = -1;
     private long buttonTime = 0L;
     private final int[] ignoreKeys = new int[]{157, 29, 54, 42, 184, 56, 220, 219};

     @SubscribeEvent(
          priority = EventPriority.LOWEST
     )
     public void onClientTick(ClientTickEvent event) {
          if (event.phase != Phase.END) {
               Minecraft mc = Minecraft.func_71410_x();
               if (mc.player != null && mc.player.openContainer instanceof ContainerPlayer) {
                    if (this.otherContainer) {
                         NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion);
                         this.otherContainer = false;
                    }
               } else {
                    this.otherContainer = true;
               }

               ++CustomNpcs.ticks;
               ++RenderNPCInterface.LastTextureTick;
               if (this.prevWorld != mc.field_71441_e) {
                    this.prevWorld = mc.field_71441_e;
                    MusicController.Instance.stopMusic();
               }

          }
     }

     @SubscribeEvent
     public void onKey(KeyInputEvent event) {
          if (CustomNpcs.SceneButtonsEnabled) {
               if (ClientProxy.Scene1.func_151468_f()) {
                    Client.sendData(EnumPacketServer.SceneStart, 1);
               }

               if (ClientProxy.Scene2.func_151468_f()) {
                    Client.sendData(EnumPacketServer.SceneStart, 2);
               }

               if (ClientProxy.Scene3.func_151468_f()) {
                    Client.sendData(EnumPacketServer.SceneStart, 3);
               }

               if (ClientProxy.SceneReset.func_151468_f()) {
                    Client.sendData(EnumPacketServer.SceneReset);
               }
          }

          Minecraft mc = Minecraft.func_71410_x();
          if (ClientProxy.QuestLog.func_151468_f()) {
               if (mc.field_71462_r == null) {
                    NoppesUtil.openGUI(mc.player, new GuiQuestLog(mc.player));
               } else if (mc.field_71462_r instanceof GuiQuestLog) {
                    mc.func_71381_h();
               }
          }

          int key = Keyboard.getEventKey();
          long time = Keyboard.getEventNanoseconds();
          if (Keyboard.getEventKeyState()) {
               if (!this.isIgnoredKey(key)) {
                    this.buttonTime = time;
                    this.buttonPressed = key;
               }
          } else {
               if (key == this.buttonPressed && time - this.buttonTime < 500000000L && mc.field_71462_r == null) {
                    boolean isCtrlPressed = Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29);
                    boolean isShiftPressed = Keyboard.isKeyDown(54) || Keyboard.isKeyDown(42);
                    boolean isAltPressed = Keyboard.isKeyDown(184) || Keyboard.isKeyDown(56);
                    boolean isMetaPressed = Keyboard.isKeyDown(220) || Keyboard.isKeyDown(219);
                    NoppesUtilPlayer.sendData(EnumPlayerPacket.KeyPressed, key, isCtrlPressed, isShiftPressed, isAltPressed, isMetaPressed);
               }

               this.buttonPressed = -1;
               this.buttonTime = 0L;
          }

     }

     @SubscribeEvent
     public void invoke(LeftClickEmpty event) {
          if (event.getHand() == EnumHand.MAIN_HAND) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.LeftClick);
          }
     }

     private boolean isIgnoredKey(int key) {
          int[] var2 = this.ignoreKeys;
          int var3 = var2.length;

          for(int var4 = 0; var4 < var3; ++var4) {
               int i = var2[var4];
               if (i == key) {
                    return true;
               }
          }

          return false;
     }
}
