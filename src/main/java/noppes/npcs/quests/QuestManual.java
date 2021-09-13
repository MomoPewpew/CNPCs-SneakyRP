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

public class QuestManual extends QuestInterface {
     public TreeMap manuals = new TreeMap();

     public void readEntityFromNBT(NBTTagCompound compound) {
          this.manuals = new TreeMap(NBTTags.getStringIntegerMap(compound.func_150295_c("QuestManual", 10)));
     }

     public void writeEntityToNBT(NBTTagCompound compound) {
          compound.func_74782_a("QuestManual", NBTTags.nbtStringIntegerMap(this.manuals));
     }

     public boolean isCompleted(EntityPlayer player) {
          PlayerQuestData playerdata = PlayerData.get(player).questData;
          QuestData data = (QuestData)playerdata.activeQuests.get(this.questId);
          if (data == null) {
               return false;
          } else {
               HashMap manual = this.getManual(data);
               if (manual.size() != this.manuals.size()) {
                    return false;
               } else {
                    Iterator var5 = manual.keySet().iterator();

                    String entity;
                    do {
                         if (!var5.hasNext()) {
                              return true;
                         }

                         entity = (String)var5.next();
                    } while(this.manuals.containsKey(entity) && (Integer)this.manuals.get(entity) <= (Integer)manual.get(entity));

                    return false;
               }
          }
     }

     public void handleComplete(EntityPlayer player) {
     }

     public HashMap getManual(QuestData data) {
          return NBTTags.getStringIntegerMap(data.extraData.func_150295_c("Manual", 10));
     }

     public void setManual(QuestData data, HashMap manual) {
          data.extraData.func_74782_a("Manual", NBTTags.nbtStringIntegerMap(manual));
     }

     public IQuestObjective[] getObjectives(EntityPlayer player) {
          List list = new ArrayList();
          Iterator var3 = this.manuals.entrySet().iterator();

          while(var3.hasNext()) {
               Entry entry = (Entry)var3.next();
               list.add(new QuestManual.QuestManualObjective(player, (String)entry.getKey(), (Integer)entry.getValue()));
          }

          return (IQuestObjective[])list.toArray(new IQuestObjective[list.size()]);
     }

     class QuestManualObjective implements IQuestObjective {
          private final EntityPlayer player;
          private final String entity;
          private final int amount;

          public QuestManualObjective(EntityPlayer player, String entity, int amount) {
               this.player = player;
               this.entity = entity;
               this.amount = amount;
          }

          public int getProgress() {
               PlayerData data = PlayerData.get(this.player);
               PlayerQuestData playerdata = data.questData;
               QuestData questdata = (QuestData)playerdata.activeQuests.get(QuestManual.this.questId);
               HashMap manual = QuestManual.this.getManual(questdata);
               return !manual.containsKey(this.entity) ? 0 : (Integer)manual.get(this.entity);
          }

          public void setProgress(int progress) {
               if (progress >= 0 && progress <= this.amount) {
                    PlayerData data = PlayerData.get(this.player);
                    PlayerQuestData playerdata = data.questData;
                    QuestData questdata = (QuestData)playerdata.activeQuests.get(QuestManual.this.questId);
                    HashMap manual = QuestManual.this.getManual(questdata);
                    if (!manual.containsKey(this.entity) || (Integer)manual.get(this.entity) != progress) {
                         manual.put(this.entity, progress);
                         QuestManual.this.setManual(questdata, manual);
                         data.questData.checkQuestCompletion(this.player, 5);
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
               return I18n.func_74838_a(this.entity) + ": " + this.getProgress() + "/" + this.getMaxProgress();
          }
     }
}
