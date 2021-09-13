package noppes.npcs;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.oredict.OreDictionary;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.QuestEvent;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.BankData;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerBankData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleDialog;
import noppes.npcs.roles.RoleFollower;

public class NoppesUtilPlayer {
     public static void changeFollowerState(EntityPlayerMP player, EntityNPCInterface npc) {
          if (npc.advanced.role == 2) {
               RoleFollower role = (RoleFollower)npc.roleInterface;
               EntityPlayer owner = role.owner;
               if (owner != null && owner.func_70005_c_().equals(player.func_70005_c_())) {
                    role.isFollowing = !role.isFollowing;
               }
          }
     }

     public static void hireFollower(EntityPlayerMP player, EntityNPCInterface npc) {
          if (npc.advanced.role == 2) {
               Container con = player.openContainer;
               if (con != null && con instanceof ContainerNPCFollowerHire) {
                    ContainerNPCFollowerHire container = (ContainerNPCFollowerHire)con;
                    RoleFollower role = (RoleFollower)npc.roleInterface;
                    followerBuy(role, container.currencyMatrix, player, npc);
               }
          }
     }

     public static void extendFollower(EntityPlayerMP player, EntityNPCInterface npc) {
          if (npc.advanced.role == 2) {
               Container con = player.openContainer;
               if (con != null && con instanceof ContainerNPCFollower) {
                    ContainerNPCFollower container = (ContainerNPCFollower)con;
                    RoleFollower role = (RoleFollower)npc.roleInterface;
                    followerBuy(role, container.currencyMatrix, player, npc);
               }
          }
     }

     public static void teleportPlayer(EntityPlayerMP player, double x, double y, double z, int dimension) {
          if (player.field_71093_bK != dimension) {
               int dim = player.field_71093_bK;
               MinecraftServer server = player.func_184102_h();
               WorldServer wor = server.getWorld(dimension);
               if (wor == null) {
                    player.func_145747_a(new TextComponentString("Broken transporter. Dimenion does not exist"));
                    return;
               }

               player.func_70012_b(x, y, z, player.field_70177_z, player.field_70125_A);
               server.func_184103_al().transferPlayerToDimension(player, dimension, new CustomTeleporter(wor));
               player.field_71135_a.func_147364_a(x, y, z, player.field_70177_z, player.field_70125_A);
               if (!wor.field_73010_i.contains(player)) {
                    wor.func_72838_d(player);
               }
          } else {
               player.field_71135_a.func_147364_a(x, y, z, player.field_70177_z, player.field_70125_A);
          }

          player.world.func_72866_a(player, false);
     }

     private static void followerBuy(RoleFollower role, IInventory currencyInv, EntityPlayerMP player, EntityNPCInterface npc) {
          ItemStack currency = currencyInv.func_70301_a(0);
          if (currency != null && !currency.func_190926_b()) {
               HashMap cd = new HashMap();

               int stackSize;
               int possibleDays;
               for(stackSize = 0; stackSize < role.inventory.items.size(); ++stackSize) {
                    ItemStack is = (ItemStack)role.inventory.items.get(stackSize);
                    if (!is.func_190926_b() && is.func_77973_b() == currency.func_77973_b() && (!is.func_77981_g() || is.func_77952_i() == currency.func_77952_i())) {
                         possibleDays = 1;
                         if (role.rates.containsKey(stackSize)) {
                              possibleDays = (Integer)role.rates.get(stackSize);
                         }

                         cd.put(is, possibleDays);
                    }
               }

               if (cd.size() != 0) {
                    stackSize = currency.func_190916_E();
                    int days = 0;
                    possibleDays = 0;
                    int possibleSize = stackSize;

                    while(true) {
                         Iterator var10 = cd.keySet().iterator();

                         while(var10.hasNext()) {
                              ItemStack item = (ItemStack)var10.next();
                              int rDays = (Integer)cd.get(item);
                              int rValue = item.func_190916_E();
                              if (rValue <= stackSize) {
                                   int newStackSize = stackSize % rValue;
                                   int size = stackSize - newStackSize;
                                   int posDays = size / rValue * rDays;
                                   if (possibleDays <= posDays) {
                                        possibleDays = posDays;
                                        possibleSize = newStackSize;
                                   }
                              }
                         }

                         if (stackSize == possibleSize) {
                              System.out.println(possibleSize);
                              RoleEvent.FollowerHireEvent event = new RoleEvent.FollowerHireEvent(player, npc.wrappedNPC, days);
                              if (EventHooks.onNPCRole(npc, event)) {
                                   return;
                              }

                              if (event.days == 0) {
                                   return;
                              }

                              if (stackSize <= 0) {
                                   currencyInv.func_70299_a(0, ItemStack.field_190927_a);
                              } else {
                                   currencyInv.func_70299_a(0, currency.splitStack(stackSize));
                              }

                              npc.say(player, new Line(NoppesStringUtils.formatText(role.dialogHire.replace("{days}", days + ""), player, npc)));
                              role.setOwner(player);
                              role.addDays(days);
                              return;
                         }

                         stackSize = possibleSize;
                         days += possibleDays;
                         possibleDays = 0;
                    }
               }
          }
     }

     public static void bankUpgrade(EntityPlayerMP player, EntityNPCInterface npc) {
          if (npc.advanced.role == 3) {
               Container con = player.openContainer;
               if (con != null && con instanceof ContainerNPCBankInterface) {
                    ContainerNPCBankInterface container = (ContainerNPCBankInterface)con;
                    Bank bank = BankController.getInstance().getBank(container.bankid);
                    ItemStack item = bank.upgradeInventory.func_70301_a(container.slot);
                    if (item != null && !item.func_190926_b()) {
                         int price = item.func_190916_E();
                         ItemStack currency = container.currencyMatrix.func_70301_a(0);
                         if (currency != null && !currency.func_190926_b() && price <= currency.func_190916_E()) {
                              if (currency.func_190916_E() - price == 0) {
                                   container.currencyMatrix.func_70299_a(0, ItemStack.field_190927_a);
                              } else {
                                   currency.splitStack(price);
                              }

                              player.func_71128_l();
                              PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
                              BankData bankData = data.getBank(bank.id);
                              bankData.upgradedSlots.put(container.slot, true);
                              RoleEvent.BankUpgradedEvent event = new RoleEvent.BankUpgradedEvent(player, npc.wrappedNPC, container.slot);
                              EventHooks.onNPCRole(npc, event);
                              bankData.openBankGui(player, npc, bank.id, container.slot);
                         }
                    }
               }
          }
     }

     public static void bankUnlock(EntityPlayerMP player, EntityNPCInterface npc) {
          if (npc.advanced.role == 3) {
               Container con = player.openContainer;
               if (con != null && con instanceof ContainerNPCBankInterface) {
                    ContainerNPCBankInterface container = (ContainerNPCBankInterface)con;
                    Bank bank = BankController.getInstance().getBank(container.bankid);
                    ItemStack item = bank.currencyInventory.func_70301_a(container.slot);
                    if (item != null && !item.func_190926_b()) {
                         int price = item.func_190916_E();
                         ItemStack currency = container.currencyMatrix.func_70301_a(0);
                         if (currency != null && !currency.func_190926_b() && price <= currency.func_190916_E()) {
                              if (currency.func_190916_E() - price == 0) {
                                   container.currencyMatrix.func_70299_a(0, ItemStack.field_190927_a);
                              } else {
                                   currency.splitStack(price);
                              }

                              player.func_71128_l();
                              PlayerBankData data = PlayerDataController.instance.getBankData(player, bank.id);
                              BankData bankData = data.getBank(bank.id);
                              if (bankData.unlockedSlots + 1 <= bank.maxSlots) {
                                   ++bankData.unlockedSlots;
                              }

                              RoleEvent.BankUnlockedEvent event = new RoleEvent.BankUnlockedEvent(player, npc.wrappedNPC, container.slot);
                              EventHooks.onNPCRole(npc, event);
                              bankData.openBankGui(player, npc, bank.id, container.slot);
                         }
                    }
               }
          }
     }

     public static void sendData(EnumPlayerPacket enu, Object... obs) {
          PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

          try {
               if (!Server.fillBuffer(buffer, enu, obs)) {
                    return;
               }

               CustomNpcs.ChannelPlayer.sendToServer(new FMLProxyPacket(buffer, "CustomNPCsPlayer"));
          } catch (IOException var4) {
               var4.printStackTrace();
          }

     }

     public static void dialogSelected(int diaId, int optionId, EntityPlayerMP player, EntityNPCInterface npc) {
          PlayerData data = PlayerData.get(player);
          if (data.dialogId == diaId) {
               if (data.dialogId < 0 && npc.advanced.role == 7) {
                    String text = (String)((RoleDialog)npc.roleInterface).optionsTexts.get(optionId);
                    if (text != null && !text.isEmpty()) {
                         Dialog d = new Dialog((DialogCategory)null);
                         d.text = text;
                         NoppesUtilServer.openDialog(player, npc, d);
                    }

               } else {
                    Dialog dialog = (Dialog)DialogController.instance.dialogs.get(data.dialogId);
                    if (dialog != null) {
                         if (!dialog.hasDialogs(player) && !dialog.hasOtherOptions()) {
                              closeDialog(player, npc, true);
                         } else {
                              DialogOption option = (DialogOption)dialog.options.get(optionId);
                              if (option != null && !EventHooks.onNPCDialogOption(npc, player, dialog, option) && (option.optionType != 1 || option.isAvailable(player) && option.hasDialog()) && option.optionType != 2 && option.optionType != 0) {
                                   if (option.optionType == 3) {
                                        closeDialog(player, npc, true);
                                        if (npc.roleInterface != null) {
                                             if (npc.advanced.role == 6) {
                                                  ((RoleCompanion)npc.roleInterface).interact(player, true);
                                             } else {
                                                  npc.roleInterface.interact(player);
                                             }
                                        }
                                   } else if (option.optionType == 1) {
                                        closeDialog(player, npc, false);
                                        NoppesUtilServer.openDialog(player, npc, option.getDialog());
                                   } else if (option.optionType == 4) {
                                        closeDialog(player, npc, true);
                                        NoppesUtilServer.runCommand(npc, npc.func_70005_c_(), option.command, player);
                                   } else {
                                        closeDialog(player, npc, true);
                                   }

                              } else {
                                   closeDialog(player, npc, true);
                              }
                         }
                    }
               }
          }
     }

     public static void closeDialog(EntityPlayerMP player, EntityNPCInterface npc, boolean notifyClient) {
          PlayerData data = PlayerData.get(player);
          Dialog dialog = (Dialog)DialogController.instance.dialogs.get(data.dialogId);
          EventHooks.onNPCDialogClose(npc, player, dialog);
          if (notifyClient) {
               Server.sendData(player, EnumPacketClient.GUI_CLOSE, -1, new NBTTagCompound());
          }

          data.dialogId = -1;
     }

     public static void questCompletion(EntityPlayerMP player, int questId) {
          PlayerData data = PlayerData.get(player);
          PlayerQuestData playerdata = data.questData;
          QuestData questdata = (QuestData)playerdata.activeQuests.get(questId);
          if (questdata != null) {
               Quest quest = questdata.quest;
               if (quest.questInterface.isCompleted(player)) {
                    QuestEvent.QuestTurnedInEvent event = new QuestEvent.QuestTurnedInEvent(data.scriptData.getPlayer(), quest);
                    event.expReward = quest.rewardExp;
                    List list = new ArrayList();
                    Iterator var8 = quest.rewardItems.items.iterator();

                    while(var8.hasNext()) {
                         ItemStack item = (ItemStack)var8.next();
                         if (!item.func_190926_b()) {
                              list.add(NpcAPI.Instance().getIItemStack(item));
                         }
                    }

                    if (!quest.randomReward) {
                         event.itemRewards = (IItemStack[])list.toArray(new IItemStack[list.size()]);
                    } else if (!list.isEmpty()) {
                         event.itemRewards = new IItemStack[]{(IItemStack)list.get(player.func_70681_au().nextInt(list.size()))};
                    }

                    EventHooks.onQuestTurnedIn(data.scriptData, event);
                    IItemStack[] var12 = event.itemRewards;
                    int var14 = var12.length;

                    for(int var10 = 0; var10 < var14; ++var10) {
                         IItemStack item = var12[var10];
                         if (item != null) {
                              NoppesUtilServer.GivePlayerItem(player, player, item.getMCItemStack());
                         }
                    }

                    quest.questInterface.handleComplete(player);
                    if (event.expReward > 0) {
                         NoppesUtilServer.playSound(player, SoundEvents.field_187604_bf, 0.1F, 0.5F * ((player.world.field_73012_v.nextFloat() - player.world.field_73012_v.nextFloat()) * 0.7F + 1.8F));
                         player.func_71023_q(event.expReward);
                    }

                    quest.factionOptions.addPoints(player);
                    if (quest.mail.isValid()) {
                         PlayerDataController.instance.addPlayerMessage(player.func_184102_h(), player.func_70005_c_(), quest.mail);
                    }

                    if (!quest.command.isEmpty()) {
                         FakePlayer cplayer = EntityNPCInterface.CommandPlayer;
                         cplayer.func_70029_a(player.world);
                         cplayer.func_70107_b(player.field_70165_t, player.field_70163_u, player.field_70161_v);
                         NoppesUtilServer.runCommand(cplayer, "QuestCompletion", quest.command, player);
                    }

                    PlayerQuestController.setQuestFinished(quest, player);
                    if (quest.hasNewQuest()) {
                         PlayerQuestController.addActiveQuest(quest.getNextQuest(), player);
                    }

               }
          }
     }

     public static boolean compareItems(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
          if (!NoppesUtilServer.IsItemStackNull(item) && !NoppesUtilServer.IsItemStackNull(item2)) {
               boolean oreMatched = false;
               OreDictionary.itemMatches(item, item2, false);
               int[] ids = OreDictionary.getOreIDs(item);
               if (ids.length > 0) {
                    int[] var6 = ids;
                    int var7 = ids.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                         int id = var6[var8];
                         boolean match1 = false;
                         boolean match2 = false;
                         Iterator var12 = OreDictionary.getOres(OreDictionary.getOreName(id)).iterator();

                         while(var12.hasNext()) {
                              ItemStack is = (ItemStack)var12.next();
                              if (compareItemDetails(item, is, ignoreDamage, ignoreNBT)) {
                                   match1 = true;
                              }

                              if (compareItemDetails(item2, is, ignoreDamage, ignoreNBT)) {
                                   match2 = true;
                              }
                         }

                         if (match1 && match2) {
                              return true;
                         }
                    }
               }

               return compareItemDetails(item, item2, ignoreDamage, ignoreNBT);
          } else {
               return false;
          }
     }

     private static boolean compareItemDetails(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
          if (item.func_77973_b() != item2.func_77973_b()) {
               return false;
          } else if (!ignoreDamage && item.func_77952_i() != -1 && item.func_77952_i() != item2.func_77952_i()) {
               return false;
          } else if (!ignoreNBT && item.func_77978_p() != null && (item2.func_77978_p() == null || !item.func_77978_p().equals(item2.func_77978_p()))) {
               return false;
          } else {
               return ignoreNBT || item2.func_77978_p() == null || item.func_77978_p() != null;
          }
     }

     public static boolean compareItems(EntityPlayer player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
          int size = 0;

          for(int i = 0; i < player.inventory.func_70302_i_(); ++i) {
               ItemStack is = player.inventory.func_70301_a(i);
               if (!NoppesUtilServer.IsItemStackNull(is) && compareItems(item, is, ignoreDamage, ignoreNBT)) {
                    size += is.func_190916_E();
               }
          }

          return size >= item.func_190916_E();
     }

     public static void consumeItem(EntityPlayer player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
          if (!NoppesUtilServer.IsItemStackNull(item)) {
               int size = item.func_190916_E();

               for(int i = 0; i < player.inventory.func_70302_i_(); ++i) {
                    ItemStack is = player.inventory.func_70301_a(i);
                    if (!NoppesUtilServer.IsItemStackNull(is) && compareItems(item, is, ignoreDamage, ignoreNBT)) {
                         if (size < is.func_190916_E()) {
                              is.splitStack(size);
                              break;
                         }

                         size -= is.func_190916_E();
                         player.inventory.func_70299_a(i, ItemStack.field_190927_a);
                    }
               }

          }
     }

     public static List countStacks(IInventory inv, boolean ignoreDamage, boolean ignoreNBT) {
          List list = new ArrayList();

          for(int i = 0; i < inv.func_70302_i_(); ++i) {
               ItemStack item = inv.func_70301_a(i);
               if (!NoppesUtilServer.IsItemStackNull(item)) {
                    boolean found = false;
                    Iterator var7 = list.iterator();

                    while(var7.hasNext()) {
                         ItemStack is = (ItemStack)var7.next();
                         if (compareItems(item, is, ignoreDamage, ignoreNBT)) {
                              is.func_190920_e(is.func_190916_E() + item.func_190916_E());
                              found = true;
                              break;
                         }
                    }

                    if (!found) {
                         list.add(item.func_77946_l());
                    }
               }
          }

          return list;
     }
}
