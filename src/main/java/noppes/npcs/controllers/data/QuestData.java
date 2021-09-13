package noppes.npcs.controllers.data;

import net.minecraft.nbt.NBTTagCompound;

public class QuestData {
     public Quest quest;
     public boolean isCompleted;
     public NBTTagCompound extraData = new NBTTagCompound();

     public QuestData(Quest quest) {
          this.quest = quest;
     }

     public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.func_74757_a("QuestCompleted", this.isCompleted);
          nbttagcompound.setTag("ExtraData", this.extraData);
     }

     public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
          this.isCompleted = nbttagcompound.getBoolean("QuestCompleted");
          this.extraData = nbttagcompound.getCompoundTag("ExtraData");
     }
}
