package noppes.npcs.controllers;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NBTTags;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.items.ItemScripted;

public class SyncController {
     public static void syncPlayer(EntityPlayerMP player) {
          NBTTagList list = new NBTTagList();
          new NBTTagCompound();
          Iterator var3 = FactionController.instance.factions.values().iterator();

          NBTTagCompound compound;
          while(var3.hasNext()) {
               Faction faction = (Faction)var3.next();
               list.appendTag(faction.writeNBT(new NBTTagCompound()));
               if (list.tagCount() > 20) {
                    compound = new NBTTagCompound();
                    compound.setTag("Data", list);
                    Server.sendData(player, EnumPacketClient.SYNC_ADD, 1, compound);
                    list = new NBTTagList();
               }
          }

          compound = new NBTTagCompound();
          compound.setTag("Data", list);
          Server.sendData(player, EnumPacketClient.SYNC_END, 1, compound);
          var3 = QuestController.instance.categories.values().iterator();

          while(var3.hasNext()) {
               QuestCategory category = (QuestCategory)var3.next();
               Server.sendData(player, EnumPacketClient.SYNC_ADD, 3, category.writeNBT(new NBTTagCompound()));
          }

          Server.sendData(player, EnumPacketClient.SYNC_END, 3, new NBTTagCompound());
          var3 = DialogController.instance.categories.values().iterator();

          while(var3.hasNext()) {
               DialogCategory category = (DialogCategory)var3.next();
               Server.sendData(player, EnumPacketClient.SYNC_ADD, 5, category.writeNBT(new NBTTagCompound()));
          }

          Server.sendData(player, EnumPacketClient.SYNC_END, 5, new NBTTagCompound());
          list = new NBTTagList();
          var3 = RecipeController.instance.globalRecipes.values().iterator();

          RecipeCarpentry category;
          while(var3.hasNext()) {
               category = (RecipeCarpentry)var3.next();
               list.appendTag(category.writeNBT());
               if (list.tagCount() > 10) {
                    compound = new NBTTagCompound();
                    compound.setTag("Data", list);
                    Server.sendData(player, EnumPacketClient.SYNC_ADD, 6, compound);
                    list = new NBTTagList();
               }
          }

          compound = new NBTTagCompound();
          compound.setTag("Data", list);
          Server.sendData(player, EnumPacketClient.SYNC_END, 6, compound);
          list = new NBTTagList();
          var3 = RecipeController.instance.anvilRecipes.values().iterator();

          while(var3.hasNext()) {
               category = (RecipeCarpentry)var3.next();
               list.appendTag(category.writeNBT());
               if (list.tagCount() > 10) {
                    compound = new NBTTagCompound();
                    compound.setTag("Data", list);
                    Server.sendData(player, EnumPacketClient.SYNC_ADD, 7, compound);
                    list = new NBTTagList();
               }
          }

          compound = new NBTTagCompound();
          compound.setTag("Data", list);
          Server.sendData(player, EnumPacketClient.SYNC_END, 7, compound);
          PlayerData data = PlayerData.get(player);
          Server.sendData(player, EnumPacketClient.SYNC_END, 8, data.getNBT());
          syncScriptItems(player);
     }

     public static void syncAllDialogs(MinecraftServer server) {
          Iterator var1 = DialogController.instance.categories.values().iterator();

          while(var1.hasNext()) {
               DialogCategory category = (DialogCategory)var1.next();
               Server.sendToAll(server, EnumPacketClient.SYNC_ADD, 5, category.writeNBT(new NBTTagCompound()));
          }

          Server.sendToAll(server, EnumPacketClient.SYNC_END, 5, new NBTTagCompound());
     }

     public static void syncAllQuests(MinecraftServer server) {
          Iterator var1 = QuestController.instance.categories.values().iterator();

          while(var1.hasNext()) {
               QuestCategory category = (QuestCategory)var1.next();
               Server.sendToAll(server, EnumPacketClient.SYNC_ADD, 3, category.writeNBT(new NBTTagCompound()));
          }

          Server.sendToAll(server, EnumPacketClient.SYNC_END, 3, new NBTTagCompound());
     }

     public static void syncScriptItems(EntityPlayerMP player) {
          NBTTagCompound comp = new NBTTagCompound();
          comp.setTag("List", NBTTags.nbtIntegerStringMap(ItemScripted.Resources));
          Server.sendData(player, EnumPacketClient.SYNC_END, 9, comp);
     }

     public static void syncScriptItemsEverybody() {
          NBTTagCompound comp = new NBTTagCompound();
          comp.setTag("List", NBTTags.nbtIntegerStringMap(ItemScripted.Resources));
          Iterator var1 = CustomNpcs.Server.getPlayerList().getPlayers().iterator();

          while(var1.hasNext()) {
               EntityPlayerMP player = (EntityPlayerMP)var1.next();
               Server.sendData(player, EnumPacketClient.SYNC_END, 9, comp);
          }

     }

     public static void clientSync(int synctype, NBTTagCompound compound, boolean syncEnd) {
          if (synctype == 1) {
               NBTTagList list = compound.getTagList("Data", 10);

               for(int i = 0; i < list.tagCount(); ++i) {
                    Faction faction = new Faction();
                    faction.readNBT(list.getCompoundTagAt(i));
                    FactionController.instance.factionsSync.put(faction.id, faction);
               }

               if (syncEnd) {
                    FactionController.instance.factions = FactionController.instance.factionsSync;
                    FactionController.instance.factionsSync = new HashMap();
               }
          } else {
               Iterator var6;
               HashMap dialogs;
               Iterator var11;
               if (synctype == 3) {
                    if (!compound.func_82582_d()) {
                         QuestCategory category = new QuestCategory();
                         category.readNBT(compound);
                         QuestController.instance.categoriesSync.put(category.id, category);
                    }

                    if (syncEnd) {
                         dialogs = new HashMap();
                         var11 = QuestController.instance.categoriesSync.values().iterator();

                         while(var11.hasNext()) {
                              QuestCategory category = (QuestCategory)var11.next();
                              var6 = category.quests.values().iterator();

                              while(var6.hasNext()) {
                                   Quest quest = (Quest)var6.next();
                                   dialogs.put(quest.id, quest);
                              }
                         }

                         QuestController.instance.categories = QuestController.instance.categoriesSync;
                         QuestController.instance.quests = dialogs;
                         QuestController.instance.categoriesSync = new HashMap();
                    }
               } else if (synctype == 5) {
                    if (!compound.func_82582_d()) {
                         DialogCategory category = new DialogCategory();
                         category.readNBT(compound);
                         DialogController.instance.categoriesSync.put(category.id, category);
                    }

                    if (syncEnd) {
                         dialogs = new HashMap();
                         var11 = DialogController.instance.categoriesSync.values().iterator();

                         while(var11.hasNext()) {
                              DialogCategory category = (DialogCategory)var11.next();
                              var6 = category.dialogs.values().iterator();

                              while(var6.hasNext()) {
                                   Dialog dialog = (Dialog)var6.next();
                                   dialogs.put(dialog.id, dialog);
                              }
                         }

                         DialogController.instance.categories = DialogController.instance.categoriesSync;
                         DialogController.instance.dialogs = dialogs;
                         DialogController.instance.categoriesSync = new HashMap();
                    }
               }
          }

     }

     public static void clientSyncUpdate(int synctype, NBTTagCompound compound, ByteBuf buffer) {
          if (synctype == 1) {
               Faction faction = new Faction();
               faction.readNBT(compound);
               FactionController.instance.factions.put(faction.id, faction);
          } else {
               DialogCategory category;
               if (synctype == 4) {
                    category = (DialogCategory)DialogController.instance.categories.get(buffer.readInt());
                    Dialog dialog = new Dialog(category);
                    dialog.readNBT(compound);
                    DialogController.instance.dialogs.put(dialog.id, dialog);
                    category.dialogs.put(dialog.id, dialog);
               } else if (synctype == 5) {
                    category = new DialogCategory();
                    category.readNBT(compound);
                    DialogController.instance.categories.put(category.id, category);
               } else {
                    QuestCategory category;
                    if (synctype == 2) {
                         category = (QuestCategory)QuestController.instance.categories.get(buffer.readInt());
                         Quest quest = new Quest(category);
                         quest.readNBT(compound);
                         QuestController.instance.quests.put(quest.id, quest);
                         category.quests.put(quest.id, quest);
                    } else if (synctype == 3) {
                         category = new QuestCategory();
                         category.readNBT(compound);
                         QuestController.instance.categories.put(category.id, category);
                    }
               }
          }

     }

     public static void clientSyncRemove(int synctype, int id) {
          if (synctype == 1) {
               FactionController.instance.factions.remove(id);
          } else if (synctype == 4) {
               Dialog dialog = (Dialog)DialogController.instance.dialogs.remove(id);
               if (dialog != null) {
                    dialog.category.dialogs.remove(id);
               }
          } else if (synctype == 5) {
               DialogCategory category = (DialogCategory)DialogController.instance.categories.remove(id);
               if (category != null) {
                    DialogController.instance.dialogs.keySet().removeAll(category.dialogs.keySet());
               }
          } else if (synctype == 2) {
               Quest quest = (Quest)QuestController.instance.quests.remove(id);
               if (quest != null) {
                    quest.category.quests.remove(id);
               }
          } else if (synctype == 3) {
               QuestCategory category = (QuestCategory)QuestController.instance.categories.remove(id);
               if (category != null) {
                    QuestController.instance.quests.keySet().removeAll(category.quests.keySet());
               }
          }

     }
}
