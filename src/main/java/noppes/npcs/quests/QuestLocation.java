package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;

public class QuestLocation extends QuestInterface {
     public String location = "";
     public String location2 = "";
     public String location3 = "";

     public void readEntityFromNBT(NBTTagCompound compound) {
          this.location = compound.func_74779_i("QuestLocation");
          this.location2 = compound.func_74779_i("QuestLocation2");
          this.location3 = compound.func_74779_i("QuestLocation3");
     }

     public void writeEntityToNBT(NBTTagCompound compound) {
          compound.func_74778_a("QuestLocation", this.location);
          compound.func_74778_a("QuestLocation2", this.location2);
          compound.func_74778_a("QuestLocation3", this.location3);
     }

     public boolean isCompleted(EntityPlayer player) {
          PlayerQuestData playerdata = PlayerData.get(player).questData;
          QuestData data = (QuestData)playerdata.activeQuests.get(this.questId);
          return data == null ? false : this.getFound(data, 0);
     }

     public void handleComplete(EntityPlayer player) {
     }

     public boolean getFound(QuestData data, int i) {
          if (i == 1) {
               return data.extraData.func_74767_n("LocationFound");
          } else if (i == 2) {
               return data.extraData.func_74767_n("Location2Found");
          } else if (i == 3) {
               return data.extraData.func_74767_n("Location3Found");
          } else if (!this.location.isEmpty() && !data.extraData.func_74767_n("LocationFound")) {
               return false;
          } else if (!this.location2.isEmpty() && !data.extraData.func_74767_n("Location2Found")) {
               return false;
          } else {
               return this.location3.isEmpty() || data.extraData.func_74767_n("Location3Found");
          }
     }

     public boolean setFound(QuestData data, String location) {
          if (location.equalsIgnoreCase(this.location) && !data.extraData.func_74767_n("LocationFound")) {
               data.extraData.func_74757_a("LocationFound", true);
               return true;
          } else if (location.equalsIgnoreCase(this.location2) && !data.extraData.func_74767_n("LocationFound2")) {
               data.extraData.func_74757_a("Location2Found", true);
               return true;
          } else if (location.equalsIgnoreCase(this.location3) && !data.extraData.func_74767_n("LocationFound3")) {
               data.extraData.func_74757_a("Location3Found", true);
               return true;
          } else {
               return false;
          }
     }

     public IQuestObjective[] getObjectives(EntityPlayer player) {
          List list = new ArrayList();
          if (!this.location.isEmpty()) {
               list.add(new QuestLocation.QuestLocationObjective(player, this.location, "LocationFound"));
          }

          if (!this.location2.isEmpty()) {
               list.add(new QuestLocation.QuestLocationObjective(player, this.location2, "Location2Found"));
          }

          if (!this.location3.isEmpty()) {
               list.add(new QuestLocation.QuestLocationObjective(player, this.location3, "Location3Found"));
          }

          return (IQuestObjective[])list.toArray(new IQuestObjective[list.size()]);
     }

     class QuestLocationObjective implements IQuestObjective {
          private final EntityPlayer player;
          private final String location;
          private final String nbtName;

          public QuestLocationObjective(EntityPlayer player, String location, String nbtName) {
               this.player = player;
               this.location = location;
               this.nbtName = nbtName;
          }

          public int getProgress() {
               return this.isCompleted() ? 1 : 0;
          }

          public void setProgress(int progress) {
               if (progress >= 0 && progress <= 1) {
                    PlayerData data = PlayerData.get(this.player);
                    QuestData questData = (QuestData)data.questData.activeQuests.get(QuestLocation.this.questId);
                    boolean completed = questData.extraData.func_74767_n(this.nbtName);
                    if ((!completed || progress != 1) && (completed || progress != 0)) {
                         questData.extraData.func_74757_a(this.nbtName, progress == 1);
                         data.questData.checkQuestCompletion(this.player, 3);
                         data.updateClient = true;
                    }
               } else {
                    throw new CustomNPCsException("Progress has to be 0 or 1", new Object[0]);
               }
          }

          public int getMaxProgress() {
               return 1;
          }

          public boolean isCompleted() {
               PlayerData data = PlayerData.get(this.player);
               QuestData questData = (QuestData)data.questData.activeQuests.get(QuestLocation.this.questId);
               return questData.extraData.func_74767_n(this.nbtName);
          }

          public String getText() {
               String found = I18n.func_74838_a("quest.found");
               String notfound = I18n.func_74838_a("quest.notfound");
               return this.location + ": " + (this.isCompleted() ? found : notfound);
          }
     }
}
