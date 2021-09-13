package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.Server;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.util.NBTJsonUtil;

public class QuestController implements IQuestHandler {
     public HashMap categoriesSync = new HashMap();
     public HashMap categories = new HashMap();
     public HashMap quests = new HashMap();
     public static QuestController instance = new QuestController();
     private int lastUsedCatID = 0;
     private int lastUsedQuestID = 0;

     public QuestController() {
          instance = this;
     }

     public void load() {
          this.categories.clear();
          this.quests.clear();
          this.lastUsedCatID = 0;
          this.lastUsedQuestID = 0;

          File file;
          try {
               file = new File(CustomNpcs.getWorldSaveDirectory(), "quests.dat");
               if (file.exists()) {
                    this.loadCategoriesOld(file);
                    file.delete();
                    file = new File(CustomNpcs.getWorldSaveDirectory(), "quests.dat_old");
                    if (file.exists()) {
                         file.delete();
                    }

                    return;
               }
          } catch (Exception var10) {
          }

          file = this.getDir();
          if (!file.exists()) {
               file.mkdir();
          } else {
               File[] var2 = file.listFiles();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if (file.isDirectory()) {
                         QuestCategory category = this.loadCategoryDir(file);
                         Iterator ite = category.quests.keySet().iterator();

                         while(ite.hasNext()) {
                              int id = (Integer)ite.next();
                              if (id > this.lastUsedQuestID) {
                                   this.lastUsedQuestID = id;
                              }

                              Quest quest = (Quest)category.quests.get(id);
                              if (this.quests.containsKey(id)) {
                                   LogWriter.error("Duplicate id " + quest.id + " from category " + category.title);
                                   ite.remove();
                              } else {
                                   this.quests.put(id, quest);
                              }
                         }

                         ++this.lastUsedCatID;
                         category.id = this.lastUsedCatID;
                         this.categories.put(category.id, category);
                    }
               }
          }

     }

     private QuestCategory loadCategoryDir(File dir) {
          QuestCategory category = new QuestCategory();
          category.title = dir.getName();
          File[] var3 = dir.listFiles();
          int var4 = var3.length;

          for(int var5 = 0; var5 < var4; ++var5) {
               File file = var3[var5];
               if (file.isFile() && file.getName().endsWith(".json")) {
                    try {
                         Quest quest = new Quest(category);
                         quest.id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 5));
                         quest.readNBTPartial(NBTJsonUtil.LoadFile(file));
                         category.quests.put(quest.id, quest);
                    } catch (Exception var8) {
                         LogWriter.error("Error loading: " + file.getAbsolutePath(), var8);
                    }
               }
          }

          return category;
     }

     private void loadCategoriesOld(File file) throws Exception {
          NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_74796_a(new FileInputStream(file));
          this.lastUsedCatID = nbttagcompound1.func_74762_e("lastID");
          this.lastUsedQuestID = nbttagcompound1.func_74762_e("lastQuestID");
          NBTTagList list = nbttagcompound1.func_150295_c("Data", 10);
          if (list != null) {
               for(int i = 0; i < list.func_74745_c(); ++i) {
                    QuestCategory category = new QuestCategory();
                    category.readNBT(list.func_150305_b(i));
                    this.categories.put(category.id, category);
                    this.saveCategory(category);
                    Iterator ita = category.quests.entrySet().iterator();

                    while(ita.hasNext()) {
                         Entry entry = (Entry)ita.next();
                         Quest quest = (Quest)entry.getValue();
                         quest.id = (Integer)entry.getKey();
                         if (this.quests.containsKey(quest.id)) {
                              ita.remove();
                         } else {
                              this.saveQuest(category, quest);
                         }
                    }
               }
          }

     }

     public void removeCategory(int category) {
          QuestCategory cat = (QuestCategory)this.categories.get(category);
          if (cat != null) {
               File dir = new File(this.getDir(), cat.title);
               if (dir.delete()) {
                    Iterator var4 = cat.quests.keySet().iterator();

                    while(var4.hasNext()) {
                         int dia = (Integer)var4.next();
                         this.quests.remove(dia);
                    }

                    this.categories.remove(category);
                    Server.sendToAll(CustomNpcs.Server, EnumPacketClient.SYNC_REMOVE, 3, category);
               }
          }
     }

     public void saveCategory(QuestCategory category) {
          category.title = NoppesStringUtils.cleanFileName(category.title);
          if (this.categories.containsKey(category.id)) {
               QuestCategory currentCategory = (QuestCategory)this.categories.get(category.id);
               if (!currentCategory.title.equals(category.title)) {
                    while(this.containsCategoryName(category)) {
                         category.title = category.title + "_";
                    }

                    File newdir = new File(this.getDir(), category.title);
                    File olddir = new File(this.getDir(), currentCategory.title);
                    if (newdir.exists()) {
                         return;
                    }

                    if (!olddir.renameTo(newdir)) {
                         return;
                    }
               }

               category.quests = currentCategory.quests;
          } else {
               if (category.id < 0) {
                    ++this.lastUsedCatID;
                    category.id = this.lastUsedCatID;
               }

               while(this.containsCategoryName(category)) {
                    category.title = category.title + "_";
               }

               File dir = new File(this.getDir(), category.title);
               if (!dir.exists()) {
                    dir.mkdirs();
               }
          }

          this.categories.put(category.id, category);
          Server.sendToAll(CustomNpcs.Server, EnumPacketClient.SYNC_UPDATE, 3, category.writeNBT(new NBTTagCompound()));
     }

     public boolean containsCategoryName(QuestCategory category) {
          Iterator var2 = this.categories.values().iterator();

          QuestCategory cat;
          do {
               if (!var2.hasNext()) {
                    return false;
               }

               cat = (QuestCategory)var2.next();
          } while(cat.id == category.id || !cat.title.equalsIgnoreCase(category.title));

          return true;
     }

     public boolean containsQuestName(QuestCategory category, Quest quest) {
          Iterator var3 = category.quests.values().iterator();

          Quest q;
          do {
               if (!var3.hasNext()) {
                    return false;
               }

               q = (Quest)var3.next();
          } while(q.id == quest.id || !q.title.equalsIgnoreCase(quest.title));

          return true;
     }

     public void saveQuest(QuestCategory category, Quest quest) {
          if (category != null) {
               while(this.containsQuestName(quest.category, quest)) {
                    quest.title = quest.title + "_";
               }

               if (quest.id < 0) {
                    ++this.lastUsedQuestID;
                    quest.id = this.lastUsedQuestID;
               }

               this.quests.put(quest.id, quest);
               category.quests.put(quest.id, quest);
               File dir = new File(this.getDir(), category.title);
               if (!dir.exists()) {
                    dir.mkdirs();
               }

               File file = new File(dir, quest.id + ".json_new");
               File file2 = new File(dir, quest.id + ".json");

               try {
                    NBTJsonUtil.SaveFile(file, quest.writeToNBTPartial(new NBTTagCompound()));
                    if (file2.exists()) {
                         file2.delete();
                    }

                    file.renameTo(file2);
                    Server.sendToAll(CustomNpcs.Server, EnumPacketClient.SYNC_UPDATE, 2, quest.writeToNBT(new NBTTagCompound()), category.id);
               } catch (Exception var7) {
                    var7.printStackTrace();
               }

          }
     }

     public void removeQuest(Quest quest) {
          File file = new File(new File(this.getDir(), quest.category.title), quest.id + ".json");
          if (file.delete()) {
               this.quests.remove(quest.id);
               quest.category.quests.remove(quest.id);
               Server.sendToAll(CustomNpcs.Server, EnumPacketClient.SYNC_REMOVE, 2, quest.id);
          }
     }

     private File getDir() {
          return new File(CustomNpcs.getWorldSaveDirectory(), "quests");
     }

     public List categories() {
          return new ArrayList(this.categories.values());
     }

     public IQuest get(int id) {
          return (IQuest)this.quests.get(id);
     }
}
