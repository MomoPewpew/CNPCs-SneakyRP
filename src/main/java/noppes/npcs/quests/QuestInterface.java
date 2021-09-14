package noppes.npcs.quests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.handler.data.IQuestObjective;

public abstract class QuestInterface {
	public int questId;

	public abstract void writeEntityToNBT(NBTTagCompound var1);

	public abstract void readEntityFromNBT(NBTTagCompound var1);

	public abstract boolean isCompleted(EntityPlayer var1);

	public abstract void handleComplete(EntityPlayer var1);

	public abstract IQuestObjective[] getObjectives(EntityPlayer var1);
}
