package noppes.npcs.client;

import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.ModelData;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.PacketHandlerServer;
import noppes.npcs.Server;
import noppes.npcs.ServerEventsHandler;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.GuiAchievement;
import noppes.npcs.client.gui.GuiNpcMobSpawnerAdd;
import noppes.npcs.client.gui.player.GuiCustomChest;
import noppes.npcs.client.gui.player.GuiQuestCompletion;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.IGuiClose;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IGuiError;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.SyncController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemScripted;

public class PacketHandlerClient extends PacketHandlerServer {
     @SubscribeEvent
     public void onPacketData(ClientCustomPacketEvent event) {
          EntityPlayer player = Minecraft.getMinecraft().player;
          if (player != null) {
               ByteBuf buffer = event.getPacket().payload();
               Minecraft.getMinecraft().addScheduledTask(() -> {
                    EnumPacketClient type = null;

                    try {
                         type = EnumPacketClient.values()[buffer.readInt()];
                         LogWriter.debug("Received: " + type);
                         this.client(buffer, player, type);
                    } catch (Exception var8) {
                         LogWriter.error("Error with EnumPacketClient." + type, var8);
                    } finally {
                         buffer.release();
                    }

               });
          }
     }

     private void client(ByteBuf buffer, EntityPlayer player, EnumPacketClient type) throws Exception {
          Entity entity;
          if (type == EnumPacketClient.CHATBUBBLE) {
               entity = Minecraft.getMinecraft().world.getEntityByID(buffer.readInt());
               if (entity == null || !(entity instanceof EntityNPCInterface)) {
                    return;
               }

               EntityNPCInterface npc = (EntityNPCInterface)entity;
               if (npc.messages == null) {
                    npc.messages = new RenderChatMessages();
               }

               String text = NoppesStringUtils.formatText(Server.readString(buffer), player, npc);
               npc.messages.addMessage(text, npc);
               if (buffer.readBoolean()) {
                    player.sendMessage(new TextComponentTranslation(npc.getName() + ": " + text, new Object[0]));
               }
          } else {
               String font;
               if (type == EnumPacketClient.CHAT) {
                    String message;
                    for(message = ""; (font = Server.readString(buffer)) != null && !font.isEmpty(); message = message + I18n.translateToLocal(font)) {
                    }

                    player.sendMessage(new TextComponentTranslation(message, new Object[0]));
               } else if (type == EnumPacketClient.EYE_BLINK) {
                    entity = Minecraft.getMinecraft().world.getEntityByID(buffer.readInt());
                    if (entity == null || !(entity instanceof EntityNPCInterface)) {
                         return;
                    }

                    ModelData data = ((EntityCustomNpc)entity).modelData;
                    data.eyes.blinkStart = System.currentTimeMillis();
               } else {
                    int size;
                    if (type == EnumPacketClient.MESSAGE) {
                         TextComponentTranslation title = new TextComponentTranslation(Server.readString(buffer), new Object[0]);
                         TextComponentTranslation message = new TextComponentTranslation(Server.readString(buffer), new Object[0]);
                         size = buffer.readInt();
                         Minecraft.getMinecraft().getToastGui().add(new GuiAchievement(title, message, size));
                    } else {
                         int config;
                         NBTTagCompound compound;
                         if (type == EnumPacketClient.UPDATE_ITEM) {
                              config = buffer.readInt();
                              compound = Server.readNBT(buffer);
                              ItemStack stack = player.inventory.getStackInSlot(config);
                              if (!stack.isEmpty()) {
                                   ((ItemStackWrapper)NpcAPI.Instance().getIItemStack(stack)).setMCNbt(compound);
                              }
                         } else if (type != EnumPacketClient.SYNC_ADD && type != EnumPacketClient.SYNC_END) {
                              if (type == EnumPacketClient.SYNC_UPDATE) {
                                   config = buffer.readInt();
                                   compound = Server.readNBT(buffer);
                                   SyncController.clientSyncUpdate(config, compound, buffer);
                              } else {
                                   GuiScreen gui;
                                   if (type == EnumPacketClient.CHEST_NAME) {
                                        gui = Minecraft.getMinecraft().currentScreen;
                                        if (gui instanceof GuiCustomChest) {
                                             ((GuiCustomChest)gui).title = I18n.translateToLocal(Server.readString(buffer));
                                        }
                                   } else {
                                        int i;
                                        if (type == EnumPacketClient.SYNC_REMOVE) {
                                             config = buffer.readInt();
                                             i = buffer.readInt();
                                             SyncController.clientSyncRemove(config, i);
                                        } else if (type == EnumPacketClient.MARK_DATA) {
                                             entity = Minecraft.getMinecraft().world.getEntityByID(buffer.readInt());
                                             if (entity == null || !(entity instanceof EntityLivingBase)) {
                                                  return;
                                             }

                                             MarkData data = MarkData.get((EntityLivingBase)entity);
                                             data.setNBT(Server.readNBT(buffer));
                                        } else {
                                             Dialog dialog;
                                             if (type == EnumPacketClient.DIALOG) {
                                                  entity = Minecraft.getMinecraft().world.getEntityByID(buffer.readInt());
                                                  if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                                       return;
                                                  }

                                                  dialog = (Dialog)DialogController.instance.dialogs.get(buffer.readInt());
                                                  NoppesUtil.openDialog(dialog, (EntityNPCInterface)entity, player);
                                             } else if (type == EnumPacketClient.DIALOG_DUMMY) {
                                                  EntityDialogNpc npc = new EntityDialogNpc(player.world);
                                                  npc.display.setName(Server.readString(buffer));
                                                  EntityUtil.Copy(player, npc);
                                                  dialog = new Dialog((DialogCategory)null);
                                                  dialog.readNBT(Server.readNBT(buffer));
                                                  NoppesUtil.openDialog(dialog, npc, player);
                                             } else if (type == EnumPacketClient.QUEST_COMPLETION) {
                                                  config = buffer.readInt();
                                                  IQuest quest = QuestController.instance.get(config);
                                                  if (!quest.getCompleteText().isEmpty()) {
                                                       NoppesUtil.openGUI(player, new GuiQuestCompletion(quest));
                                                  } else {
                                                       NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, config);
                                                  }
                                             } else if (type == EnumPacketClient.EDIT_NPC) {
                                                  entity = Minecraft.getMinecraft().world.getEntityByID(buffer.readInt());
                                                  if (entity != null && entity instanceof EntityNPCInterface) {
                                                       NoppesUtil.setLastNpc((EntityNPCInterface)entity);
                                                  } else {
                                                       NoppesUtil.setLastNpc((EntityNPCInterface)null);
                                                  }
                                             } else if (type == EnumPacketClient.PLAY_MUSIC) {
                                                  MusicController.Instance.playMusic(Server.readString(buffer), player);
                                             } else if (type == EnumPacketClient.PLAY_SOUND) {
                                                  MusicController.Instance.playSound(SoundCategory.VOICE, Server.readString(buffer), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readFloat(), buffer.readFloat());
                                             } else {
                                                  Entity entity;
                                                  NBTTagCompound compound;
                                                  if (type == EnumPacketClient.UPDATE_NPC) {
                                                       compound = Server.readNBT(buffer);
                                                       entity = Minecraft.getMinecraft().world.getEntityByID(compound.getInteger("EntityId"));
                                                       if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                                            return;
                                                       }

                                                       ((EntityNPCInterface)entity).readSpawnData(compound);
                                                  } else if (type == EnumPacketClient.ROLE) {
                                                       compound = Server.readNBT(buffer);
                                                       entity = Minecraft.getMinecraft().world.getEntityByID(compound.getInteger("EntityId"));
                                                       if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                                            return;
                                                       }

                                                       ((EntityNPCInterface)entity).advanced.setRole(compound.getInteger("Role"));
                                                       ((EntityNPCInterface)entity).roleInterface.readFromNBT(compound);
                                                       NoppesUtil.setLastNpc((EntityNPCInterface)entity);
                                                  } else if (type == EnumPacketClient.GUI) {
                                                       EnumGuiType gui = EnumGuiType.values()[buffer.readInt()];
                                                       CustomNpcs.proxy.openGui(NoppesUtil.getLastNpc(), gui, buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                  } else if (type == EnumPacketClient.PARTICLE) {
                                                       NoppesUtil.spawnParticle(buffer);
                                                  } else if (type == EnumPacketClient.DELETE_NPC) {
                                                       entity = Minecraft.getMinecraft().world.getEntityByID(buffer.readInt());
                                                       if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                                            return;
                                                       }

                                                       ((EntityNPCInterface)entity).delete();
                                                  } else if (type == EnumPacketClient.SCROLL_LIST) {
                                                       NoppesUtil.setScrollList(buffer);
                                                  } else if (type == EnumPacketClient.SCROLL_DATA) {
                                                       NoppesUtil.setScrollData(buffer);
                                                  } else if (type == EnumPacketClient.SCROLL_DATA_PART) {
                                                       NoppesUtil.addScrollData(buffer);
                                                  } else if (type == EnumPacketClient.SCROLL_SELECTED) {
                                                       gui = Minecraft.getMinecraft().currentScreen;
                                                       if (gui == null || !(gui instanceof IScrollData)) {
                                                            return;
                                                       }

                                                       font = Server.readString(buffer);
                                                       ((IScrollData)gui).setSelected(font);
                                                  } else if (type == EnumPacketClient.CLONE) {
                                                       compound = Server.readNBT(buffer);
                                                       NoppesUtil.openGUI(player, new GuiNpcMobSpawnerAdd(compound));
                                                  } else if (type == EnumPacketClient.GUI_DATA) {
                                                       GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                                                       if (gui == null) {
                                                            return;
                                                       }

                                                       if (gui instanceof GuiNPCInterface && ((GuiNPCInterface)gui).hasSubGui()) {
                                                            gui = ((GuiNPCInterface)gui).getSubGui();
                                                       } else if (gui instanceof GuiContainerNPCInterface && ((GuiContainerNPCInterface)gui).hasSubGui()) {
                                                            gui = ((GuiContainerNPCInterface)gui).getSubGui();
                                                       }

                                                       if (gui instanceof IGuiData) {
                                                            ((IGuiData)gui).setGuiData(Server.readNBT(buffer));
                                                       }
                                                  } else if (type == EnumPacketClient.GUI_UPDATE) {
                                                       gui = Minecraft.getMinecraft().currentScreen;
                                                       if (gui == null) {
                                                            return;
                                                       }

                                                       gui.initGui();
                                                  } else {
                                                       NBTTagCompound compound;
                                                       if (type == EnumPacketClient.GUI_ERROR) {
                                                            gui = Minecraft.getMinecraft().currentScreen;
                                                            if (gui == null || !(gui instanceof IGuiError)) {
                                                                 return;
                                                            }

                                                            i = buffer.readInt();
                                                            compound = Server.readNBT(buffer);
                                                            ((IGuiError)gui).setError(i, compound);
                                                       } else if (type == EnumPacketClient.GUI_CLOSE) {
                                                            gui = Minecraft.getMinecraft().currentScreen;
                                                            if (gui == null) {
                                                                 return;
                                                            }

                                                            if (gui instanceof IGuiClose) {
                                                                 i = buffer.readInt();
                                                                 compound = Server.readNBT(buffer);
                                                                 ((IGuiClose)gui).setClose(i, compound);
                                                            }

                                                            Minecraft mc = Minecraft.getMinecraft();
                                                            mc.displayGuiScreen((GuiScreen)null);
                                                            mc.setIngameFocus();
                                                       } else if (type == EnumPacketClient.VILLAGER_LIST) {
                                                            MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(new PacketBuffer(buffer));
                                                            ServerEventsHandler.Merchant.setRecipes(merchantrecipelist);
                                                       } else if (type == EnumPacketClient.CONFIG) {
                                                            config = buffer.readInt();
                                                            if (config == 0) {
                                                                 font = Server.readString(buffer);
                                                                 size = buffer.readInt();
                                                                 Runnable run = () -> {
                                                                      if (!font.isEmpty()) {
                                                                           CustomNpcs.FontType = font;
                                                                           CustomNpcs.FontSize = size;
                                                                           ClientProxy.Font.clear();
                                                                           ClientProxy.Font = new ClientProxy.FontContainer(CustomNpcs.FontType, CustomNpcs.FontSize);
                                                                           CustomNpcs.Config.updateConfig();
                                                                           player.sendMessage(new TextComponentTranslation("Font set to %s", new Object[]{ClientProxy.Font.getName()}));
                                                                      } else {
                                                                           player.sendMessage(new TextComponentTranslation("Current font is %s", new Object[]{ClientProxy.Font.getName()}));
                                                                      }

                                                                 };
                                                                 Minecraft.getMinecraft().addScheduledTask(run);
                                                            }
                                                       }
                                                  }
                                             }
                                        }
                                   }
                              }
                         } else {
                              config = buffer.readInt();
                              compound = Server.readNBT(buffer);
                              SyncController.clientSync(config, compound, type == EnumPacketClient.SYNC_END);
                              if (config == 8) {
                                   ClientProxy.playerData.setNBT(compound);
                              } else if (config == 9) {
                                   if (player.getServer() == null) {
                                        ItemScripted.Resources = NBTTags.getIntegerStringMap(compound.getTagList("List", 10));
                                   }

                                   Iterator var6 = ItemScripted.Resources.entrySet().iterator();

                                   while(var6.hasNext()) {
                                        Entry entry = (Entry)var6.next();
                                        ModelResourceLocation mrl = new ModelResourceLocation((String)entry.getValue(), "inventory");
                                        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(CustomItems.scripted_item, (Integer)entry.getKey(), mrl);
                                        ModelLoader.setCustomModelResourceLocation(CustomItems.scripted_item, (Integer)entry.getKey(), mrl);
                                   }
                              }
                         }
                    }
               }
          }

     }
}
