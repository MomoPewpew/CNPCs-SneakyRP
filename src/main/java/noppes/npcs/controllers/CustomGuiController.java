package noppes.npcs.controllers;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.CustomNpcs;
import noppes.npcs.Server;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.CustomGuiEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.containers.ContainerCustomGui;

public class CustomGuiController {
     public static void openGui(PlayerWrapper player, CustomGuiWrapper gui) {
          ((EntityPlayerMP)player.getMCEntity()).openGui(CustomNpcs.instance, EnumGuiType.CustomGui.ordinal(), player.getWorld().getMCWorld(), gui.getSlots().size(), 0, 0);
          ((ContainerCustomGui)((EntityPlayerMP)player.getMCEntity()).openContainer).setGui(gui, (EntityPlayer)player.getMCEntity());
          Server.sendDataDelayed((EntityPlayerMP)player.getMCEntity(), EnumPacketClient.GUI_DATA, 100, gui.toNBT());
     }

     public static boolean updateGui(PlayerWrapper player, CustomGuiWrapper gui) {
          if (((EntityPlayerMP)player.getMCEntity()).openContainer instanceof ContainerCustomGui) {
               Server.sendData((EntityPlayerMP)player.getMCEntity(), EnumPacketClient.GUI_DATA, gui.toNBT());
               return true;
          } else {
               return false;
          }
     }

     static boolean checkGui(CustomGuiEvent event) {
          EntityPlayer player = event.player.getMCEntity();
          if (!(player.openContainer instanceof ContainerCustomGui)) {
               return false;
          } else {
               return ((ContainerCustomGui)player.openContainer).customGui.getID() == event.gui.getID();
          }
     }

     public static IItemStack[] getSlotContents(EntityPlayer player) {
          IItemStack[] slotContents = new IItemStack[0];
          if (player.openContainer instanceof ContainerCustomGui) {
               ContainerCustomGui container = (ContainerCustomGui)player.openContainer;
               slotContents = new IItemStack[container.guiInventory.getSizeInventory()];

               for(int i = 0; i < container.guiInventory.getSizeInventory(); ++i) {
                    slotContents[i] = NpcAPI.Instance().getIItemStack(container.guiInventory.getStackInSlot(i));
               }
          }

          return slotContents;
     }

     public static void onButton(CustomGuiEvent.ButtonEvent event) {
          EntityPlayer player = event.player.getMCEntity();
          if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
               getOpenGui(player).getScriptHandler().run((EnumScriptType)EnumScriptType.CUSTOM_GUI_BUTTON, (Event)event);
          }

          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onSlotChange(CustomGuiEvent.SlotEvent event) {
          EntityPlayer player = event.player.getMCEntity();
          if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
               getOpenGui(player).getScriptHandler().run((EnumScriptType)EnumScriptType.CUSTOM_GUI_SLOT, (Event)event);
          }

          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onScrollClick(CustomGuiEvent.ScrollEvent event) {
          EntityPlayer player = event.player.getMCEntity();
          if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
               getOpenGui(player).getScriptHandler().run((EnumScriptType)EnumScriptType.CUSTOM_GUI_SCROLL, (Event)event);
          }

          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static void onClose(CustomGuiEvent.CloseEvent event) {
          EntityPlayer player = event.player.getMCEntity();
          if (checkGui(event) && getOpenGui(player).getScriptHandler() != null) {
               getOpenGui(player).getScriptHandler().run((EnumScriptType)EnumScriptType.CUSTOM_GUI_CLOSED, (Event)event);
          }

          WrapperNpcAPI.EVENT_BUS.post(event);
     }

     public static CustomGuiWrapper getOpenGui(EntityPlayer player) {
          return player.openContainer instanceof ContainerCustomGui ? ((ContainerCustomGui)player.openContainer).customGui : null;
     }

     public static String[] readScrollSelection(ByteBuf buffer) {
          try {
               NBTTagList list = Server.readNBT(buffer).getTagList("selection", 8);
               String[] selection = new String[list.tagCount()];

               for(int i = 0; i < list.tagCount(); ++i) {
                    selection[i] = ((NBTTagString)list.func_179238_g(i)).func_150285_a_();
               }

               return selection;
          } catch (IOException var4) {
               var4.printStackTrace();
               return null;
          }
     }
}
