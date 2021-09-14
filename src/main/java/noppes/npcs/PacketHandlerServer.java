package noppes.npcs;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.ForgeScriptData;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataScenes;
import noppes.npcs.roles.JobSpawner;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.roles.RoleTransporter;
import noppes.npcs.schematics.SchematicWrapper;
import noppes.npcs.util.IPermission;

public class PacketHandlerServer {
     @SubscribeEvent
     public void onServerPacket(ServerCustomPacketEvent event) {
          EntityPlayerMP player = ((NetHandlerPlayServer)event.getHandler()).player;
          if (CustomNpcs.OpsOnly && !NoppesUtilServer.isOp(player)) {
               this.warn(player, "tried to use custom npcs without being an op");
          } else {
               ByteBuf buffer = event.getPacket().payload();
               player.getServer().addScheduledTask(() -> {
                    EnumPacketServer type = null;

                    try {
                         type = EnumPacketServer.values()[buffer.readInt()];
                         LogWriter.debug("Received: " + type);
                         ItemStack item = player.inventory.getCurrentItem();
                         EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
                         if ((!type.needsNpc || npc != null) && (!type.hasPermission() || CustomNpcsPermissions.hasPermission(player, type.permission))) {
                              if (!type.isExempt() && !this.allowItem(item, type)) {
                                   this.warn(player, "tried to use custom npcs without a tool in hand, possibly a hacker");
                              } else {
                                   this.handlePacket(type, buffer, player, npc);
                              }
                         }
                    } catch (Exception var9) {
                         LogWriter.error("Error with EnumPacketServer." + type, var9);
                    } finally {
                         buffer.release();
                    }

               });
          }
     }

     private boolean allowItem(ItemStack stack, EnumPacketServer type) {
          if (stack != null && stack.getItem() != null) {
               Item item = stack.getItem();
               IPermission permission = null;
               if (item instanceof IPermission) {
                    permission = (IPermission)item;
               } else if (item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof IPermission) {
                    permission = (IPermission)((ItemBlock)item).getBlock();
               }

               return permission != null && permission.isAllowed(type);
          } else {
               return false;
          }
     }

     private void handlePacket(EnumPacketServer type, ByteBuf buffer, EntityPlayerMP player, EntityNPCInterface npc) throws Exception {
          if (type == EnumPacketServer.Delete) {
               npc.delete();
               NoppesUtilServer.deleteNpc(npc, player);
          } else if (type == EnumPacketServer.SceneStart) {
               if (CustomNpcs.SceneButtonsEnabled) {
                    DataScenes.Toggle(player, buffer.readInt() + "btn");
               }
          } else if (type == EnumPacketServer.SceneReset) {
               if (CustomNpcs.SceneButtonsEnabled) {
                    DataScenes.Reset(player, (String)null);
               }
          } else {
               Iterator var43;
               LinkedNpcController.LinkedData data;
               ArrayList list;
               if (type == EnumPacketServer.LinkedAdd) {
                    LinkedNpcController.Instance.addData(Server.readString(buffer));
                    list = new ArrayList();
                    var43 = LinkedNpcController.Instance.list.iterator();

                    while(var43.hasNext()) {
                         data = (LinkedNpcController.LinkedData)var43.next();
                         list.add(data.name);
                    }

                    Server.sendData(player, EnumPacketClient.SCROLL_LIST, list);
               } else if (type == EnumPacketServer.LinkedRemove) {
                    LinkedNpcController.Instance.removeData(Server.readString(buffer));
                    list = new ArrayList();
                    var43 = LinkedNpcController.Instance.list.iterator();

                    while(var43.hasNext()) {
                         data = (LinkedNpcController.LinkedData)var43.next();
                         list.add(data.name);
                    }

                    Server.sendData(player, EnumPacketClient.SCROLL_LIST, list);
               } else if (type == EnumPacketServer.LinkedGetAll) {
                    list = new ArrayList();
                    var43 = LinkedNpcController.Instance.list.iterator();

                    while(var43.hasNext()) {
                         data = (LinkedNpcController.LinkedData)var43.next();
                         list.add(data.name);
                    }

                    Server.sendData(player, EnumPacketClient.SCROLL_LIST, list);
                    if (npc != null) {
                         Server.sendData(player, EnumPacketClient.SCROLL_SELECTED, npc.linkedName);
                    }
               } else if (type == EnumPacketServer.LinkedSet) {
                    npc.linkedName = Server.readString(buffer);
                    LinkedNpcController.Instance.loadNpcData(npc);
               } else if (type == EnumPacketServer.NpcMenuClose) {
                    npc.reset();
                    if (npc.linkedData != null) {
                         LinkedNpcController.Instance.saveNpcData(npc);
                    }

                    NoppesUtilServer.setEditingNpc(player, (EntityNPCInterface)null);
               } else if (type == EnumPacketServer.BanksGet) {
                    NoppesUtilServer.sendBankDataAll(player);
               } else {
                    Bank bank;
                    if (type == EnumPacketServer.BankGet) {
                         bank = BankController.getInstance().getBank(buffer.readInt());
                         NoppesUtilServer.sendBank(player, bank);
                    } else if (type == EnumPacketServer.BankSave) {
                         bank = new Bank();
                         bank.readEntityFromNBT(Server.readNBT(buffer));
                         BankController.getInstance().saveBank(bank);
                         NoppesUtilServer.sendBankDataAll(player);
                         NoppesUtilServer.sendBank(player, bank);
                    } else if (type == EnumPacketServer.BankRemove) {
                         BankController.getInstance().removeBank(buffer.readInt());
                         NoppesUtilServer.sendBankDataAll(player);
                         NoppesUtilServer.sendBank(player, new Bank());
                    } else {
                         Entity entity;
                         if (type == EnumPacketServer.RemoteMainMenu) {
                              entity = player.world.getEntityByID(buffer.readInt());
                              if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                   return;
                              }

                              NoppesUtilServer.sendOpenGui(player, EnumGuiType.MainMenuDisplay, (EntityNPCInterface)entity);
                         } else if (type == EnumPacketServer.RemoteDelete) {
                              entity = player.world.getEntityByID(buffer.readInt());
                              if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                   return;
                              }

                              npc = (EntityNPCInterface)entity;
                              npc.delete();
                              NoppesUtilServer.deleteNpc(npc, player);
                              NoppesUtilServer.sendNearbyNpcs(player);
                         } else if (type == EnumPacketServer.RemoteNpcsGet) {
                              NoppesUtilServer.sendNearbyNpcs(player);
                              Server.sendData(player, EnumPacketClient.SCROLL_SELECTED, CustomNpcs.FreezeNPCs ? "Unfreeze Npcs" : "Freeze Npcs");
                         } else if (type == EnumPacketServer.RemoteFreeze) {
                              CustomNpcs.FreezeNPCs = !CustomNpcs.FreezeNPCs;
                              Server.sendData(player, EnumPacketClient.SCROLL_SELECTED, CustomNpcs.FreezeNPCs ? "Unfreeze Npcs" : "Freeze Npcs");
                         } else if (type == EnumPacketServer.RemoteReset) {
                              entity = player.world.getEntityByID(buffer.readInt());
                              if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                   return;
                              }

                              npc = (EntityNPCInterface)entity;
                              npc.reset();
                         } else if (type == EnumPacketServer.RemoteTpToNpc) {
                              entity = player.world.getEntityByID(buffer.readInt());
                              if (entity == null || !(entity instanceof EntityNPCInterface)) {
                                   return;
                              }

                              npc = (EntityNPCInterface)entity;
                              player.connection.setPlayerLocation(npc.posX, npc.posY, npc.posZ, 0.0F, 0.0F);
                         } else {
                              int z;
                              int t;
                              int y;
                              if (type == EnumPacketServer.Gui) {
                                   EnumGuiType gui = EnumGuiType.values()[buffer.readInt()];
                                   t = buffer.readInt();
                                   y = buffer.readInt();
                                   z = buffer.readInt();
                                   NoppesUtilServer.sendOpenGui(player, gui, npc, t, y, z);
                              } else if (type == EnumPacketServer.RecipesGet) {
                                   NoppesUtilServer.sendRecipeData(player, buffer.readInt());
                              } else {
                                   RecipeCarpentry recipe;
                                   if (type == EnumPacketServer.RecipeGet) {
                                        recipe = RecipeController.instance.getRecipe(buffer.readInt());
                                        NoppesUtilServer.setRecipeGui(player, recipe);
                                   } else if (type == EnumPacketServer.RecipeRemove) {
                                        recipe = RecipeController.instance.delete(buffer.readInt());
                                        NoppesUtilServer.sendRecipeData(player, recipe.isGlobal ? 3 : 4);
                                        NoppesUtilServer.setRecipeGui(player, new RecipeCarpentry(""));
                                   } else if (type == EnumPacketServer.RecipeSave) {
                                        recipe = RecipeCarpentry.read(Server.readNBT(buffer));
                                        RecipeController.instance.saveRecipe(recipe);
                                        NoppesUtilServer.sendRecipeData(player, recipe.isGlobal ? 3 : 4);
                                        NoppesUtilServer.setRecipeGui(player, recipe);
                                   } else if (type == EnumPacketServer.NaturalSpawnGetAll) {
                                        NoppesUtilServer.sendScrollData(player, SpawnController.instance.getScroll());
                                   } else {
                                        SpawnData data;
                                        if (type == EnumPacketServer.NaturalSpawnGet) {
                                             data = SpawnController.instance.getSpawnData(buffer.readInt());
                                             if (data != null) {
                                                  Server.sendData(player, EnumPacketClient.GUI_DATA, data.writeNBT(new NBTTagCompound()));
                                             }
                                        } else if (type == EnumPacketServer.NaturalSpawnSave) {
                                             data = new SpawnData();
                                             data.readNBT(Server.readNBT(buffer));
                                             SpawnController.instance.saveSpawnData(data);
                                             NoppesUtilServer.sendScrollData(player, SpawnController.instance.getScroll());
                                        } else if (type == EnumPacketServer.NaturalSpawnRemove) {
                                             SpawnController.instance.removeSpawnData(buffer.readInt());
                                             NoppesUtilServer.sendScrollData(player, SpawnController.instance.getScroll());
                                        } else {
                                             DialogCategory category;
                                             if (type == EnumPacketServer.DialogCategorySave) {
                                                  category = new DialogCategory();
                                                  category.readNBT(Server.readNBT(buffer));
                                                  DialogController.instance.saveCategory(category);
                                                  Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                             } else if (type == EnumPacketServer.DialogCategoryRemove) {
                                                  DialogController.instance.removeCategory(buffer.readInt());
                                                  Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                             } else {
                                                  Dialog quest2;
                                                  if (type == EnumPacketServer.DialogSave) {
                                                       category = (DialogCategory)DialogController.instance.categories.get(buffer.readInt());
                                                       if (category == null) {
                                                            return;
                                                       }

                                                       quest2 = new Dialog(category);
                                                       quest2.readNBT(Server.readNBT(buffer));
                                                       DialogController.instance.saveDialog(category, quest2);
                                                       Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                                  } else {
                                                       Dialog quest;
                                                       if (type == EnumPacketServer.DialogRemove) {
                                                            quest = (Dialog)DialogController.instance.dialogs.get(buffer.readInt());
                                                            if (quest != null && quest.category != null) {
                                                                 DialogController.instance.removeDialog(quest);
                                                                 Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                                            }
                                                       } else {
                                                            Quest quest;
                                                            if (type == EnumPacketServer.QuestOpenGui) {
                                                                 quest = new Quest((QuestCategory)null);
                                                                 t = buffer.readInt();
                                                                 quest.readNBT(Server.readNBT(buffer));
                                                                 NoppesUtilServer.setEditingQuest(player, quest);
                                                                 player.openGui(CustomNpcs.instance, t, player.world, 0, 0, 0);
                                                            } else if (type == EnumPacketServer.DialogNpcGet) {
                                                                 NoppesUtilServer.sendNpcDialogs(player);
                                                            } else {
                                                                 int entityId;
                                                                 NBTTagCompound compound;
                                                                 if (type == EnumPacketServer.DialogNpcSet) {
                                                                      entityId = buffer.readInt();
                                                                      t = buffer.readInt();
                                                                      DialogOption option = NoppesUtilServer.setNpcDialog(entityId, t, player);
                                                                      if (option != null && option.hasDialog()) {
                                                                           compound = option.writeNBT();
                                                                           compound.setInteger("Position", entityId);
                                                                           Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                      }
                                                                 } else if (type == EnumPacketServer.DialogNpcRemove) {
                                                                      npc.dialogs.remove(buffer.readInt());
                                                                 } else {
                                                                      QuestCategory category;
                                                                      if (type == EnumPacketServer.QuestCategorySave) {
                                                                           category = new QuestCategory();
                                                                           category.readNBT(Server.readNBT(buffer));
                                                                           QuestController.instance.saveCategory(category);
                                                                           Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                                                      } else if (type == EnumPacketServer.QuestCategoryRemove) {
                                                                           QuestController.instance.removeCategory(buffer.readInt());
                                                                           Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                                                      } else if (type == EnumPacketServer.QuestSave) {
                                                                           category = (QuestCategory)QuestController.instance.categories.get(buffer.readInt());
                                                                           if (category == null) {
                                                                                return;
                                                                           }

                                                                           Quest quest = new Quest(category);
                                                                           quest.readNBT(Server.readNBT(buffer));
                                                                           QuestController.instance.saveQuest(category, quest);
                                                                           Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                                                      } else if (type == EnumPacketServer.QuestDialogGetTitle) {
                                                                           quest = (Dialog)DialogController.instance.dialogs.get(buffer.readInt());
                                                                           quest2 = (Dialog)DialogController.instance.dialogs.get(buffer.readInt());
                                                                           Dialog quest3 = (Dialog)DialogController.instance.dialogs.get(buffer.readInt());
                                                                           compound = new NBTTagCompound();
                                                                           if (quest != null) {
                                                                                compound.setString("1", quest.title);
                                                                           }

                                                                           if (quest2 != null) {
                                                                                compound.setString("2", quest2.title);
                                                                           }

                                                                           if (quest3 != null) {
                                                                                compound.setString("3", quest3.title);
                                                                           }

                                                                           Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                      } else if (type == EnumPacketServer.QuestRemove) {
                                                                           quest = (Quest)QuestController.instance.quests.get(buffer.readInt());
                                                                           if (quest != null) {
                                                                                QuestController.instance.removeQuest(quest);
                                                                                Server.sendData(player, EnumPacketClient.GUI_UPDATE);
                                                                           }
                                                                      } else if (type == EnumPacketServer.TransportCategoriesGet) {
                                                                           NoppesUtilServer.sendTransportCategoryData(player);
                                                                      } else if (type == EnumPacketServer.TransportCategorySave) {
                                                                           TransportController.getInstance().saveCategory(Server.readString(buffer), buffer.readInt());
                                                                      } else if (type == EnumPacketServer.TransportCategoryRemove) {
                                                                           TransportController.getInstance().removeCategory(buffer.readInt());
                                                                           NoppesUtilServer.sendTransportCategoryData(player);
                                                                      } else {
                                                                           TransportLocation location;
                                                                           if (type == EnumPacketServer.TransportRemove) {
                                                                                entityId = buffer.readInt();
                                                                                location = TransportController.getInstance().removeLocation(entityId);
                                                                                if (location != null) {
                                                                                     NoppesUtilServer.sendTransportData(player, location.category.id);
                                                                                }
                                                                           } else if (type == EnumPacketServer.TransportsGet) {
                                                                                NoppesUtilServer.sendTransportData(player, buffer.readInt());
                                                                           } else if (type == EnumPacketServer.TransportSave) {
                                                                                entityId = buffer.readInt();
                                                                                location = TransportController.getInstance().saveLocation(entityId, Server.readNBT(buffer), player, npc);
                                                                                if (location != null) {
                                                                                     if (npc.advanced.role != 4) {
                                                                                          return;
                                                                                     }

                                                                                     RoleTransporter role = (RoleTransporter)npc.roleInterface;
                                                                                     role.setTransport(location);
                                                                                }
                                                                           } else if (type == EnumPacketServer.TransportGetLocation) {
                                                                                if (npc.advanced.role != 4) {
                                                                                     return;
                                                                                }

                                                                                RoleTransporter role = (RoleTransporter)npc.roleInterface;
                                                                                if (role.hasTransport()) {
                                                                                     Server.sendData(player, EnumPacketClient.GUI_DATA, role.getLocation().writeNBT());
                                                                                     Server.sendData(player, EnumPacketClient.SCROLL_SELECTED, role.getLocation().category.title);
                                                                                }
                                                                           } else if (type == EnumPacketServer.FactionSet) {
                                                                                npc.setFaction(buffer.readInt());
                                                                           } else {
                                                                                NBTTagCompound compound;
                                                                                if (type == EnumPacketServer.FactionSave) {
                                                                                     Faction faction = new Faction();
                                                                                     faction.readNBT(Server.readNBT(buffer));
                                                                                     FactionController.instance.saveFaction(faction);
                                                                                     NoppesUtilServer.sendFactionDataAll(player);
                                                                                     compound = new NBTTagCompound();
                                                                                     faction.writeNBT(compound);
                                                                                     Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                } else {
                                                                                     NBTTagCompound compound;
                                                                                     if (type == EnumPacketServer.FactionRemove) {
                                                                                          FactionController.instance.delete(buffer.readInt());
                                                                                          NoppesUtilServer.sendFactionDataAll(player);
                                                                                          compound = new NBTTagCompound();
                                                                                          (new Faction()).writeNBT(compound);
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                     } else if (type == EnumPacketServer.PlayerDataGet) {
                                                                                          entityId = buffer.readInt();
                                                                                          if (EnumPlayerData.values().length <= entityId) {
                                                                                               return;
                                                                                          }

                                                                                          String name = null;
                                                                                          EnumPlayerData datatype = EnumPlayerData.values()[entityId];
                                                                                          if (datatype != EnumPlayerData.Players) {
                                                                                               name = Server.readString(buffer);
                                                                                          }

                                                                                          NoppesUtilServer.sendPlayerData(datatype, player, name);
                                                                                     } else if (type == EnumPacketServer.PlayerDataRemove) {
                                                                                          NoppesUtilServer.removePlayerData(buffer, player);
                                                                                     } else if (type == EnumPacketServer.MainmenuDisplayGet) {
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, npc.display.writeToNBT(new NBTTagCompound()));
                                                                                     } else if (type == EnumPacketServer.MainmenuDisplaySave) {
                                                                                          npc.display.readToNBT(Server.readNBT(buffer));
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.MainmenuStatsGet) {
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, npc.stats.writeToNBT(new NBTTagCompound()));
                                                                                     } else if (type == EnumPacketServer.MainmenuStatsSave) {
                                                                                          npc.stats.readToNBT(Server.readNBT(buffer));
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.MainmenuInvGet) {
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, npc.inventory.writeEntityToNBT(new NBTTagCompound()));
                                                                                     } else if (type == EnumPacketServer.MainmenuInvSave) {
                                                                                          npc.inventory.readEntityFromNBT(Server.readNBT(buffer));
                                                                                          npc.updateAI = true;
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.MainmenuAIGet) {
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, npc.ais.writeToNBT(new NBTTagCompound()));
                                                                                     } else if (type == EnumPacketServer.MainmenuAISave) {
                                                                                          npc.ais.readToNBT(Server.readNBT(buffer));
                                                                                          npc.setHealth(npc.getMaxHealth());
                                                                                          npc.updateAI = true;
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.MainmenuAdvancedGet) {
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, npc.advanced.writeToNBT(new NBTTagCompound()));
                                                                                     } else if (type == EnumPacketServer.MainmenuAdvancedSave) {
                                                                                          npc.advanced.readToNBT(Server.readNBT(buffer));
                                                                                          npc.updateAI = true;
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.MainmenuAdvancedMarkData) {
                                                                                          MarkData data = MarkData.get(npc);
                                                                                          data.setNBT(Server.readNBT(buffer));
                                                                                          data.syncClients();
                                                                                     } else if (type == EnumPacketServer.JobSave) {
                                                                                          compound = npc.jobInterface.writeToNBT(new NBTTagCompound());
                                                                                          compound = Server.readNBT(buffer);
                                                                                          Set names = compound.getKeySet();
                                                                                          Iterator var26 = names.iterator();

                                                                                          while(var26.hasNext()) {
                                                                                               String name = (String)var26.next();
                                                                                               compound.setTag(name, compound.getTag(name));
                                                                                          }

                                                                                          npc.jobInterface.readFromNBT(compound);
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.JobGet) {
                                                                                          if (npc.jobInterface == null) {
                                                                                               return;
                                                                                          }

                                                                                          compound = new NBTTagCompound();
                                                                                          compound.setBoolean("JobData", true);
                                                                                          npc.jobInterface.writeToNBT(compound);
                                                                                          if (npc.advanced.job == 6) {
                                                                                               ((JobSpawner)npc.jobInterface).cleanCompound(compound);
                                                                                          }

                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                          if (npc.advanced.job == 6) {
                                                                                               Server.sendData(player, EnumPacketClient.GUI_DATA, ((JobSpawner)npc.jobInterface).getTitles());
                                                                                          }
                                                                                     } else if (type == EnumPacketServer.JobSpawnerAdd) {
                                                                                          if (npc.advanced.job != 6) {
                                                                                               return;
                                                                                          }

                                                                                          JobSpawner job = (JobSpawner)npc.jobInterface;
                                                                                          if (buffer.readBoolean()) {
                                                                                               compound = ServerCloneController.Instance.getCloneData((ICommandSender)null, Server.readString(buffer), buffer.readInt());
                                                                                               job.setJobCompound(buffer.readInt(), compound);
                                                                                          } else {
                                                                                               job.setJobCompound(buffer.readInt(), Server.readNBT(buffer));
                                                                                          }

                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, job.getTitles());
                                                                                     } else if (type == EnumPacketServer.RoleCompanionUpdate) {
                                                                                          if (npc.advanced.role != 6) {
                                                                                               return;
                                                                                          }

                                                                                          ((RoleCompanion)npc.roleInterface).matureTo(EnumCompanionStage.values()[buffer.readInt()]);
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.JobSpawnerRemove) {
                                                                                          if (npc.advanced.job != 6) {
                                                                                               return;
                                                                                          }
                                                                                     } else if (type == EnumPacketServer.RoleSave) {
                                                                                          npc.roleInterface.readFromNBT(Server.readNBT(buffer));
                                                                                          npc.updateClient = true;
                                                                                     } else if (type == EnumPacketServer.RoleGet) {
                                                                                          if (npc.roleInterface == null) {
                                                                                               return;
                                                                                          }

                                                                                          compound = new NBTTagCompound();
                                                                                          compound.setBoolean("RoleData", true);
                                                                                          Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(compound));
                                                                                     } else if (type == EnumPacketServer.MerchantUpdate) {
                                                                                          entity = player.world.getEntityByID(buffer.readInt());
                                                                                          if (entity == null || !(entity instanceof EntityVillager)) {
                                                                                               return;
                                                                                          }

                                                                                          MerchantRecipeList list = MerchantRecipeList.readFromBuf(new PacketBuffer(buffer));
                                                                                          ((EntityVillager)entity).setRecipes(list);
                                                                                     } else if (type == EnumPacketServer.ModelDataSave) {
                                                                                          if (npc instanceof EntityCustomNpc) {
                                                                                               ((EntityCustomNpc)npc).modelData.readFromNBT(Server.readNBT(buffer));
                                                                                          }
                                                                                     } else if (type == EnumPacketServer.MailOpenSetup) {
                                                                                          PlayerMail mail = new PlayerMail();
                                                                                          mail.readNBT(Server.readNBT(buffer));
                                                                                          ContainerMail.staticmail = mail;
                                                                                          player.openGui(CustomNpcs.instance, EnumGuiType.PlayerMailman.ordinal(), player.world, 1, 0, 0);
                                                                                     } else {
                                                                                          boolean bo;
                                                                                          if (type == EnumPacketServer.TransformSave) {
                                                                                               bo = npc.transform.isValid();
                                                                                               npc.transform.readOptions(Server.readNBT(buffer));
                                                                                               if (bo != npc.transform.isValid()) {
                                                                                                    npc.updateAI = true;
                                                                                               }
                                                                                          } else if (type == EnumPacketServer.TransformGet) {
                                                                                               Server.sendData(player, EnumPacketClient.GUI_DATA, npc.transform.writeOptions(new NBTTagCompound()));
                                                                                          } else if (type == EnumPacketServer.TransformLoad) {
                                                                                               if (npc.transform.isValid()) {
                                                                                                    npc.transform.transform(buffer.readBoolean());
                                                                                               }
                                                                                          } else {
                                                                                               String name;
                                                                                               if (type == EnumPacketServer.TraderMarketSave) {
                                                                                                    name = Server.readString(buffer);
                                                                                                    boolean bo = buffer.readBoolean();
                                                                                                    if (npc.roleInterface instanceof RoleTrader) {
                                                                                                         if (bo) {
                                                                                                              RoleTrader.setMarket(npc, name);
                                                                                                         } else {
                                                                                                              RoleTrader.save((RoleTrader)npc.roleInterface, name);
                                                                                                         }
                                                                                                    }
                                                                                               } else if (type == EnumPacketServer.MovingPathGet) {
                                                                                                    Server.sendData(player, EnumPacketClient.GUI_DATA, npc.ais.writeToNBT(new NBTTagCompound()));
                                                                                               } else if (type == EnumPacketServer.MovingPathSave) {
                                                                                                    npc.ais.setMovingPath(NBTTags.getIntegerArraySet(Server.readNBT(buffer).getTagList("MovingPathNew", 10)));
                                                                                               } else if (type == EnumPacketServer.SpawnRider) {
                                                                                                    entity = EntityList.createEntityFromNBT(Server.readNBT(buffer), player.world);
                                                                                                    player.world.spawnEntity(entity);
                                                                                                    entity.startRiding(ServerEventsHandler.mounted, true);
                                                                                               } else if (type == EnumPacketServer.PlayerRider) {
                                                                                                    player.startRiding(ServerEventsHandler.mounted, true);
                                                                                               } else if (type == EnumPacketServer.SpawnMob) {
                                                                                                    bo = buffer.readBoolean();
                                                                                                    t = buffer.readInt();
                                                                                                    y = buffer.readInt();
                                                                                                    z = buffer.readInt();
                                                                                                    NBTTagCompound compound;
                                                                                                    if (bo) {
                                                                                                         compound = ServerCloneController.Instance.getCloneData(player, Server.readString(buffer), buffer.readInt());
                                                                                                    } else {
                                                                                                         compound = Server.readNBT(buffer);
                                                                                                    }

                                                                                                    if (compound == null) {
                                                                                                         return;
                                                                                                    }

                                                                                                    Entity entity = NoppesUtilServer.spawnClone(compound, (double)t + 0.5D, (double)(y + 1), (double)z + 0.5D, player.world);
                                                                                                    if (entity == null) {
                                                                                                         player.sendMessage(new TextComponentString("Failed to create an entity out of your clone"));
                                                                                                         return;
                                                                                                    }
                                                                                               } else {
                                                                                                    NBTTagCompound compound;
                                                                                                    if (type == EnumPacketServer.MobSpawner) {
                                                                                                         bo = buffer.readBoolean();
                                                                                                         BlockPos pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                         if (bo) {
                                                                                                              compound = ServerCloneController.Instance.getCloneData(player, Server.readString(buffer), buffer.readInt());
                                                                                                         } else {
                                                                                                              compound = Server.readNBT(buffer);
                                                                                                         }

                                                                                                         if (compound != null) {
                                                                                                              NoppesUtilServer.createMobSpawner(pos, compound, player);
                                                                                                         }
                                                                                                    } else if (type == EnumPacketServer.ClonePreSave) {
                                                                                                         bo = ServerCloneController.Instance.getCloneData((ICommandSender)null, Server.readString(buffer), buffer.readInt()) != null;
                                                                                                         compound = new NBTTagCompound();
                                                                                                         compound.setBoolean("NameExists", bo);
                                                                                                         Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                    } else if (type == EnumPacketServer.CloneSave) {
                                                                                                         PlayerData data = PlayerData.get(player);
                                                                                                         if (data.cloned == null) {
                                                                                                              return;
                                                                                                         }

                                                                                                         ServerCloneController.Instance.addClone(data.cloned, Server.readString(buffer), buffer.readInt());
                                                                                                    } else if (type == EnumPacketServer.CloneRemove) {
                                                                                                         entityId = buffer.readInt();
                                                                                                         ServerCloneController.Instance.removeClone(Server.readString(buffer), entityId);
                                                                                                         NBTTagList list = new NBTTagList();
                                                                                                         Iterator var42 = ServerCloneController.Instance.getClones(entityId).iterator();

                                                                                                         while(var42.hasNext()) {
                                                                                                              String name = (String)var42.next();
                                                                                                              list.appendTag(new NBTTagString(name));
                                                                                                         }

                                                                                                         compound = new NBTTagCompound();
                                                                                                         compound.setTag("List", list);
                                                                                                         Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                    } else {
                                                                                                         String name;
                                                                                                         if (type == EnumPacketServer.CloneList) {
                                                                                                              NBTTagList list = new NBTTagList();
                                                                                                              var43 = ServerCloneController.Instance.getClones(buffer.readInt()).iterator();

                                                                                                              while(var43.hasNext()) {
                                                                                                                   name = (String)var43.next();
                                                                                                                   list.appendTag(new NBTTagString(name));
                                                                                                              }

                                                                                                              compound = new NBTTagCompound();
                                                                                                              compound.setTag("List", list);
                                                                                                              Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                         } else if (type == EnumPacketServer.ScriptDataSave) {
                                                                                                              npc.script.readFromNBT(Server.readNBT(buffer));
                                                                                                              npc.updateAI = true;
                                                                                                              npc.script.lastInited = -1L;
                                                                                                         } else if (type == EnumPacketServer.ScriptDataGet) {
                                                                                                              compound = npc.script.writeToNBT(new NBTTagCompound());
                                                                                                              compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
                                                                                                              Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                         } else if (type == EnumPacketServer.DimensionsGet) {
                                                                                                              HashMap map = new HashMap();
                                                                                                              Integer[] var41 = DimensionManager.getStaticDimensionIDs();
                                                                                                              y = var41.length;

                                                                                                              for(z = 0; z < y; ++z) {
                                                                                                                   int id = var41[z];
                                                                                                                   WorldProvider provider = DimensionManager.createProviderFor(id);
                                                                                                                   map.put(provider.getDimensionType().getName(), id);
                                                                                                              }

                                                                                                              NoppesUtilServer.sendScrollData(player, map);
                                                                                                         } else if (type == EnumPacketServer.DimensionTeleport) {
                                                                                                              entityId = buffer.readInt();
                                                                                                              WorldServer world = player.getServer().getWorld(entityId);
                                                                                                              BlockPos coords = world.getSpawnCoordinate();
                                                                                                              if (coords == null) {
                                                                                                                   coords = world.getSpawnPoint();
                                                                                                                   if (!world.isAirBlock(coords)) {
                                                                                                                        coords = world.getTopSolidOrLiquidBlock(coords);
                                                                                                                   } else {
                                                                                                                        while(world.isAirBlock(coords) && coords.getY() > 0) {
                                                                                                                             coords = coords.down();
                                                                                                                        }

                                                                                                                        if (coords.getY() == 0) {
                                                                                                                             coords = world.getTopSolidOrLiquidBlock(coords);
                                                                                                                        }
                                                                                                                   }
                                                                                                              }

                                                                                                              NoppesUtilPlayer.teleportPlayer(player, (double)coords.getX(), (double)coords.getY(), (double)coords.getZ(), entityId);
                                                                                                         } else {
                                                                                                              TileEntity tile;
                                                                                                              if (type == EnumPacketServer.ScriptBlockDataGet) {
                                                                                                                   tile = player.world.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
                                                                                                                   if (!(tile instanceof TileScripted)) {
                                                                                                                        return;
                                                                                                                   }

                                                                                                                   compound = ((TileScripted)tile).getNBT(new NBTTagCompound());
                                                                                                                   compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
                                                                                                                   Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                              } else if (type == EnumPacketServer.ScriptItemDataGet) {
                                                                                                                   ItemScriptedWrapper iw = (ItemScriptedWrapper)NpcAPI.Instance().getIItemStack(player.getHeldItemMainhand());
                                                                                                                   compound = iw.getMCNbt();
                                                                                                                   compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
                                                                                                                   Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                              } else if (type == EnumPacketServer.ScriptItemDataSave) {
                                                                                                                   if (!player.isCreative()) {
                                                                                                                        return;
                                                                                                                   }

                                                                                                                   compound = Server.readNBT(buffer);
                                                                                                                   ItemStack item = player.getHeldItemMainhand();
                                                                                                                   ItemScriptedWrapper wrapper = (ItemScriptedWrapper)NpcAPI.Instance().getIItemStack(player.getHeldItemMainhand());
                                                                                                                   wrapper.setMCNbt(compound);
                                                                                                                   wrapper.lastInited = -1L;
                                                                                                                   wrapper.saveScriptData();
                                                                                                                   wrapper.updateClient = true;
                                                                                                                   player.sendContainerToPlayer(player.inventoryContainer);
                                                                                                              } else if (type == EnumPacketServer.ScriptForgeGet) {
                                                                                                                   ForgeScriptData data = ScriptController.Instance.forgeScripts;
                                                                                                                   compound = data.writeToNBT(new NBTTagCompound());
                                                                                                                   compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
                                                                                                                   Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                              } else if (type == EnumPacketServer.ScriptForgeSave) {
                                                                                                                   ScriptController.Instance.setForgeScripts(Server.readNBT(buffer));
                                                                                                              } else if (type == EnumPacketServer.ScriptPlayerGet) {
                                                                                                                   compound = ScriptController.Instance.playerScripts.writeToNBT(new NBTTagCompound());
                                                                                                                   compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
                                                                                                                   Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                              } else if (type == EnumPacketServer.ScriptPlayerSave) {
                                                                                                                   ScriptController.Instance.setPlayerScripts(Server.readNBT(buffer));
                                                                                                              } else if (type == EnumPacketServer.FactionsGet) {
                                                                                                                   NoppesUtilServer.sendFactionDataAll(player);
                                                                                                              } else if (type == EnumPacketServer.FactionGet) {
                                                                                                                   compound = new NBTTagCompound();
                                                                                                                   Faction faction = FactionController.instance.getFaction(buffer.readInt());
                                                                                                                   faction.writeNBT(compound);
                                                                                                                   Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                              } else if (type == EnumPacketServer.SaveTileEntity) {
                                                                                                                   NoppesUtilServer.saveTileEntity(player, Server.readNBT(buffer));
                                                                                                              } else {
                                                                                                                   BlockPos pos;
                                                                                                                   if (type == EnumPacketServer.GetTileEntity) {
                                                                                                                        pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                                        TileEntity tile = player.world.getTileEntity(pos);
                                                                                                                        compound = new NBTTagCompound();
                                                                                                                        tile.writeToNBT(compound);
                                                                                                                        Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                                   } else if (type == EnumPacketServer.ScriptBlockDataSave) {
                                                                                                                        tile = player.world.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
                                                                                                                        if (!(tile instanceof TileScripted)) {
                                                                                                                             return;
                                                                                                                        }

                                                                                                                        TileScripted script = (TileScripted)tile;
                                                                                                                        script.setNBT(Server.readNBT(buffer));
                                                                                                                        script.lastInited = -1L;
                                                                                                                   } else if (type == EnumPacketServer.ScriptDoorDataSave) {
                                                                                                                        tile = player.world.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
                                                                                                                        if (!(tile instanceof TileScriptedDoor)) {
                                                                                                                             return;
                                                                                                                        }

                                                                                                                        TileScriptedDoor script = (TileScriptedDoor)tile;
                                                                                                                        script.setNBT(Server.readNBT(buffer));
                                                                                                                        script.lastInited = -1L;
                                                                                                                   } else if (type == EnumPacketServer.ScriptDoorDataGet) {
                                                                                                                        tile = player.world.getTileEntity(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
                                                                                                                        if (!(tile instanceof TileScriptedDoor)) {
                                                                                                                             return;
                                                                                                                        }

                                                                                                                        compound = ((TileScriptedDoor)tile).getNBT(new NBTTagCompound());
                                                                                                                        compound.setTag("Languages", ScriptController.Instance.nbtLanguages());
                                                                                                                        Server.sendData(player, EnumPacketClient.GUI_DATA, compound);
                                                                                                                   } else {
                                                                                                                        TileBuilder tile;
                                                                                                                        if (type == EnumPacketServer.SchematicsTile) {
                                                                                                                             pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                                             tile = (TileBuilder)player.world.getTileEntity(pos);
                                                                                                                             if (tile == null) {
                                                                                                                                  return;
                                                                                                                             }

                                                                                                                             Server.sendData(player, EnumPacketClient.GUI_DATA, tile.writePartNBT(new NBTTagCompound()));
                                                                                                                             Server.sendData(player, EnumPacketClient.SCROLL_LIST, SchematicController.Instance.list());
                                                                                                                             if (tile.hasSchematic()) {
                                                                                                                                  Server.sendData(player, EnumPacketClient.GUI_DATA, tile.getSchematic().getNBTSmall());
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.SchematicsSet) {
                                                                                                                             pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                                             tile = (TileBuilder)player.world.getTileEntity(pos);
                                                                                                                             name = Server.readString(buffer);
                                                                                                                             tile.setSchematic(SchematicController.Instance.load(name));
                                                                                                                             if (tile.hasSchematic()) {
                                                                                                                                  Server.sendData(player, EnumPacketClient.GUI_DATA, tile.getSchematic().getNBTSmall());
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.SchematicsBuild) {
                                                                                                                             pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                                             tile = (TileBuilder)player.world.getTileEntity(pos);
                                                                                                                             SchematicWrapper schem = tile.getSchematic();
                                                                                                                             schem.init(pos.add(1, tile.yOffest, 1), player.world, tile.rotation * 90);
                                                                                                                             SchematicController.Instance.build(tile.getSchematic(), player);
                                                                                                                             player.world.setBlockToAir(pos);
                                                                                                                        } else if (type == EnumPacketServer.SchematicsTileSave) {
                                                                                                                             pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                                             tile = (TileBuilder)player.world.getTileEntity(pos);
                                                                                                                             if (tile != null) {
                                                                                                                                  tile.readPartNBT(Server.readNBT(buffer));
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.SchematicStore) {
                                                                                                                             name = Server.readString(buffer);
                                                                                                                             t = buffer.readInt();
                                                                                                                             TileCopy tile = (TileCopy)NoppesUtilServer.saveTileEntity(player, Server.readNBT(buffer));
                                                                                                                             if (tile == null || name.isEmpty()) {
                                                                                                                                  return;
                                                                                                                             }

                                                                                                                             SchematicController.Instance.save(player, name, t, tile.getPos(), tile.height, tile.width, tile.length);
                                                                                                                        } else if (type == EnumPacketServer.NbtBookSaveBlock) {
                                                                                                                             pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                                                                                                                             compound = Server.readNBT(buffer);
                                                                                                                             TileEntity tile = player.world.getTileEntity(pos);
                                                                                                                             if (tile != null) {
                                                                                                                                  tile.readFromNBT(compound);
                                                                                                                                  tile.markDirty();
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.NbtBookSaveEntity) {
                                                                                                                             entityId = buffer.readInt();
                                                                                                                             compound = Server.readNBT(buffer);
                                                                                                                             Entity entity = player.world.getEntityByID(entityId);
                                                                                                                             if (entity != null) {
                                                                                                                                  entity.readFromNBT(compound);
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.CustomGuiClose) {
                                                                                                                             EventHooks.onCustomGuiClose((PlayerWrapper)NpcAPI.Instance().getIEntity(player), (new CustomGuiWrapper()).fromNBT(Server.readNBT(buffer)));
                                                                                                                        } else if (type == EnumPacketServer.CustomGuiButton) {
                                                                                                                             if (player.openContainer instanceof ContainerCustomGui) {
                                                                                                                                  ((ContainerCustomGui)player.openContainer).customGui.fromNBT(Server.readNBT(buffer));
                                                                                                                                  EventHooks.onCustomGuiButton((PlayerWrapper)NpcAPI.Instance().getIEntity(player), ((ContainerCustomGui)player.openContainer).customGui, buffer.readInt());
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.CustomGuiSlotChange) {
                                                                                                                             if (player.openContainer instanceof ContainerCustomGui) {
                                                                                                                                  ((ContainerCustomGui)player.openContainer).customGui.fromNBT(Server.readNBT(buffer));
                                                                                                                                  EventHooks.onCustomGuiSlot((PlayerWrapper)NpcAPI.Instance().getIEntity(player), ((ContainerCustomGui)player.openContainer).customGui, buffer.readInt());
                                                                                                                             }
                                                                                                                        } else if (type == EnumPacketServer.CustomGuiScrollClick && player.openContainer instanceof ContainerCustomGui) {
                                                                                                                             ((ContainerCustomGui)player.openContainer).customGui.fromNBT(Server.readNBT(buffer));
                                                                                                                             EventHooks.onCustomGuiScrollClick((PlayerWrapper)NpcAPI.Instance().getIEntity(player), ((ContainerCustomGui)player.openContainer).customGui, buffer.readInt(), buffer.readInt(), CustomGuiController.readScrollSelection(buffer), buffer.readBoolean());
                                                                                                                        }
                                                                                                                   }
                                                                                                              }
                                                                                                         }
                                                                                                    }
                                                                                               }
                                                                                          }
                                                                                     }
                                                                                }
                                                                           }
                                                                      }
                                                                 }
                                                            }
                                                       }
                                                  }
                                             }
                                        }
                                   }
                              }
                         }
                    }
               }
          }

     }

     private void warn(EntityPlayer player, String warning) {
          player.getServer().logWarning(player.getName() + ": " + warning);
     }
}
