package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;

public class QuestKill extends QuestInterface {
     public TreeMap targets = new TreeMap();

     public void readEntityFromNBT(NBTTagCompound compound) {
          this.targets = new TreeMap(NBTTags.getStringIntegerMap(compound.getTagList("QuestDialogs", 10)));
     }

     public void writeEntityToNBT(NBTTagCompound compound) {
          compound.setTag("QuestDialogs", NBTTags.nbtStringIntegerMap(this.targets));
     }

     public boolean isCompleted(EntityPlayer player) {
          PlayerQuestData playerdata = PlayerData.get(player).questData;
          QuestData data = (QuestData)playerdata.activeQuests.get(this.questId);
          if (data == null) {
               return false;
          } else {
               HashMap killed = this.getKilled(data);
               if (killed.size() != this.targets.size()) {
                    return false;
               } else {
                    Iterator var5 = killed.keySet().iterator();

                    String entity;
                    do {
                         if (!var5.hasNext()) {
                              return true;
                         }

                         entity = (String)var5.next();
                    } while(this.targets.containsKey(entity) && (Integer)this.targets.get(entity) <= (Integer)killed.get(entity));

                    return false;
               }
          }
     }

     public void handleComplete(EntityPlayer player) {
     }

     public HashMap getKilled(QuestData data) {
          return NBTTags.getStringIntegerMap(data.extraData.getTagList("Killed", 10));
     }

     public void setKilled(QuestData data, HashMap killed) {
          data.extraData.setTag("Killed", NBTTags.nbtStringIntegerMap(killed));
     }

     public IQuestObjective[] getObjectives(EntityPlayer player) {
          List list = new ArrayList();
          Iterator var3 = this.targets.entrySet().iterator();

          while(var3.hasNext()) {
               Entry entry = (Entry)var3.next();
               list.add(new QuestKill.QuestKillObjective(player, (String)entry.getKey(), (Integer)entry.getValue()));
          }

          return (IQuestObjective[])list.toArray(new IQuestObjective[list.size()]);
     }

     class QuestKillObjective implements IQuestObjective {
          private final EntityPlayer player;
          private final String entity;
          private final int amount;

          public QuestKillObjective(EntityPlayer player, String entity, int amount) {
               this.player = player;
               this.entity = entity;
               this.amount = amount;
          }

          public int getProgress() {
               PlayerData data = PlayerData.get(this.player);
               PlayerQuestData playerdata = data.questData;
               QuestData questdata = (QuestData)playerdata.activeQuests.get(QuestKill.this.questId);
               HashMap killed = QuestKill.this.getKilled(questdata);
               return !killed.containsKey(this.entity) ? 0 : (Integer)killed.get(this.entity);
          }

          public void setProgress(int progress) {
               if (progress >= 0 && progress <= this.amount) {
                    PlayerData data = PlayerData.get(this.player);
                    PlayerQuestData playerdata = data.questData;
                    QuestData questdata = (QuestData)playerdata.activeQuests.get(QuestKill.this.questId);
                    HashMap killed = QuestKill.this.getKilled(questdata);
                    if (!killed.containsKey(this.entity) || (Integer)killed.get(this.entity) != progress) {
                         killed.put(this.entity, progress);
                         QuestKill.this.setKilled(questdata, killed);
                         data.questData.checkQuestCompletion(this.player, 2);
                         data.questData.checkQuestCompletion(this.player, 4);
                         data.updateClient = true;
                    }
               } else {
                    throw new CustomNPCsException("Progress has to be between 0 and " + this.amount, new Object[0]);
               }
          }

          public int getMaxProgress() {
               return this.amount;
          }

          public boolean isCompleted() {
               return this.getProgress() >= this.amount;
          }

          public String getText() {
               String name = "entity." + this.entity + ".name";
               String transName = I18n.func_74838_a(name);
               if (name.equals(transName)) {
                    transName = this.entity;
               }

               return transName + ": " + this.getProgress() + "/" + this.getMaxProgress();
          }
     }
}
