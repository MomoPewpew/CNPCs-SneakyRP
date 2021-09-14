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
			NBTTagCompound compound = mainCompound.getCompoundTag("QuestData");
			NBTTagList list = compound.getTagList("CompletedQuests", 10);
			if (list != null) {
				HashMap finishedQuests = new HashMap();

				for (int i = 0; i < list.tagCount(); ++i) {
					NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
					finishedQuests.put(nbttagcompound.getInteger("Quest"), nbttagcompound.getLong("Date"));
				}

				this.finishedQuests = finishedQuests;
			}

			NBTTagList list2 = compound.getTagList("ActiveQuests", 10);
			if (list2 != null) {
				HashMap activeQuests = new HashMap();

				for (int i = 0; i < list2.tagCount(); ++i) {
					NBTTagCompound nbttagcompound = list2.getCompoundTagAt(i);
					int id = nbttagcompound.getInteger("Quest");
					Quest quest = (Quest) QuestController.instance.quests.get(id);
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

		while (var4.hasNext()) {
			int quest = (Integer) var4.next();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Quest", quest);
			nbttagcompound.setLong("Date", (Long) this.finishedQuests.get(quest));
			list.appendTag(nbttagcompound);
		}

		compound.setTag("CompletedQuests", list);
		NBTTagList list2 = new NBTTagList();
		Iterator var9 = this.activeQuests.keySet().iterator();

		while (var9.hasNext()) {
			int quest = (Integer) var9.next();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Quest", quest);
			((QuestData) this.activeQuests.get(quest)).writeEntityToNBT(nbttagcompound);
			list2.appendTag(nbttagcompound);
		}

		compound.setTag("ActiveQuests", list2);
		maincompound.setTag("QuestData", compound);
	}

	public QuestData getQuestCompletion(EntityPlayer player, EntityNPCInterface npc) {
		Iterator var3 = this.activeQuests.values().iterator();

		QuestData data;
		Quest quest;
		do {
			if (!var3.hasNext()) {
				return null;
			}

			data = (QuestData) var3.next();
			quest = data.quest;
		} while (quest == null || quest.completion != EnumQuestCompletion.Npc
				|| !quest.completerNpc.equals(npc.getName()) || !quest.questInterface.isCompleted(player));

		return data;
	}

	public boolean checkQuestCompletion(EntityPlayer player, int type) {
		boolean bo = false;
		Iterator var4 = this.activeQuests.values().iterator();

		while (true) {
			QuestData data;
			do {
				if (!var4.hasNext()) {
					return bo;
				}

				data = (QuestData) var4.next();
			} while (data.quest.type != type && type >= 0);

			QuestInterface inter = data.quest.questInterface;
			if (inter.isCompleted(player)) {
				if (!data.isCompleted) {
					if (!data.quest.complete(player, data)) {
						Server.sendData((EntityPlayerMP) player, EnumPacketClient.MESSAGE, "quest.completed",
								data.quest.title, 2);
						Server.sendData((EntityPlayerMP) player, EnumPacketClient.CHAT, "quest.completed", ": ",
								data.quest.title);
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
