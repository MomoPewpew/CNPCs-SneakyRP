package noppes.npcs.controllers.data;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EventHooks;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestInterface;

public class PlayerQuestData {
     public HashMap activeQuests = new HashMap();
     public HashMap finishedQuests = new HashMap();

     public void loadNBTData(NBTTagCompound mainCompound) {
          if (mainCompound != null) {
               NBTTagCompound compound = mainCompound.func_74775_l("QuestData");
               NBTTagList list = compound.func_150295_c("CompletedQuests", 10);
               if (list != null) {
                    HashMap finishedQuests = new HashMap();

                    for(int i = 0; i < list.func_74745_c(); ++i) {
                         NBTTagCompound nbttagcompound = list.func_150305_b(i);
                         finishedQuests.put(nbttagcompound.func_74762_e("Quest"), nbttagcompound.func_74763_f("Date"));
                    }

                    this.finishedQuests = finishedQuests;
               }

               NBTTagList list2 = compound.func_150295_c("ActiveQuests", 10);
               if (list2 != null) {
                    HashMap activeQuests = new HashMap();

                    for(int i = 0; i < list2.func_74745_c(); ++i) {
                         NBTTagCompound nbttagcompound = list2.func_150305_b(i);
                         int id = nbttagcompound.func_74762_e("Quest");
                         Quest quest = (Quest)QuestController.instance.quests.get(id);
                         if (quest != null) {
                              QuestData data = new QuestData(quest);
                              data.readEntityFromNBT(nbttagcompound);
                              activeQuests.put(id, data);
                         }
                    }

                    this.activeQuests = activeQuests;
               }

          }
     }

     public void saveNBTData(NBTTagCompound maincompound) {
          NBTTagCompound compound = new NBTTagCompound();
          NBTTagList list = new NBTTagList();
          Iterator var4 = this.finishedQuests.keySet().iterator();

          while(var4.hasNext()) {
               int quest = (Integer)var4.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74768_a("Quest", quest);
               nbttagcompound.func_74772_a("Date", (Long)this.finishedQuests.get(quest));
               list.func_74742_a(nbttagcompound);
          }

          compound.func_74782_a("CompletedQuests", list);
          NBTTagList list2 = new NBTTagList();
          Iterator var9 = this.activeQuests.keySet().iterator();

          while(var9.hasNext()) {
               int quest = (Integer)var9.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74768_a("Quest", quest);
               ((QuestData)this.activeQuests.get(quest)).writeEntityToNBT(nbttagcompound);
               list2.func_74742_a(nbttagcompound);
          }

          compound.func_74782_a("ActiveQuests", list2);
          maincompound.func_74782_a("QuestData", compound);
     }

     public QuestData getQuestCompletion(EntityPlayer player, EntityNPCInterface npc) {
          Iterator var3 = this.activeQuests.values().iterator();

          QuestData data;
          Quest quest;
          do {
               if (!var3.hasNext()) {
                    return null;
               }

               data = (QuestData)var3.next();
               quest = data.quest;
          } while(quest == null || quest.completion != EnumQuestCompletion.Npc || !quest.completerNpc.equals(npc.func_70005_c_()) || !quest.questInterface.isCompleted(player));

          return data;
     }

     public boolean checkQuestCompletion(EntityPlayer player, int type) {
          boolean bo = false;
          Iterator var4 = this.activeQuests.values().iterator();

          while(true) {
               QuestData data;
               do {
                    if (!var4.hasNext()) {
                         return bo;
                    }

                    data = (QuestData)var4.next();
               } while(data.quest.type != type && type >= 0);

               QuestInterface inter = data.quest.questInterface;
               if (inter.isCompleted(player)) {
                    if (!data.isCompleted) {
                         if (!data.quest.complete(player, data)) {
                              Server.sendData((EntityPlayerMP)player, EnumPacketClient.MESSAGE, "quest.completed", data.quest.title, 2);
                              Server.sendData((EntityPlayerMP)player, EnumPacketClient.CHAT, "quest.completed", ": ", data.quest.title);
                         }

                         data.isCompleted = true;
                         bo = true;
                         EventHooks.onQuestFinished(PlayerData.get(player).scriptData, data.quest);
                    }
               } else {
                    data.isCompleted = false;
               }
          }
     }
}
