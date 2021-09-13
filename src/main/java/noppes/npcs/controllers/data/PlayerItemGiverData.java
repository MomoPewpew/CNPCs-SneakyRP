package noppes.npcs.controllers.data;

import java.util.HashMap;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.roles.JobItemGiver;

public class PlayerItemGiverData {
     private HashMap itemgivers = new HashMap();
     private HashMap chained = new HashMap();

     public void loadNBTData(NBTTagCompound compound) {
          this.chained = NBTTags.getIntegerIntegerMap(compound.getTagList("ItemGiverChained", 10));
          this.itemgivers = NBTTags.getIntegerLongMap(compound.getTagList("ItemGiversList", 10));
     }

     public void saveNBTData(NBTTagCompound compound) {
          compound.setTag("ItemGiverChained", NBTTags.nbtIntegerIntegerMap(this.chained));
          compound.setTag("ItemGiversList", NBTTags.nbtIntegerLongMap(this.itemgivers));
     }

     public boolean hasInteractedBefore(JobItemGiver jobItemGiver) {
          return this.itemgivers.containsKey(jobItemGiver.itemGiverId);
     }

     public long getTime(JobItemGiver jobItemGiver) {
          return (Long)this.itemgivers.get(jobItemGiver.itemGiverId);
     }

     public void setTime(JobItemGiver jobItemGiver, long day) {
          this.itemgivers.put(jobItemGiver.itemGiverId, day);
     }

     public int getItemIndex(JobItemGiver jobItemGiver) {
          return this.chained.containsKey(jobItemGiver.itemGiverId) ? (Integer)this.chained.get(jobItemGiver.itemGiverId) : 0;
     }

     public void setItemIndex(JobItemGiver jobItemGiver, int i) {
          this.chained.put(jobItemGiver.itemGiverId, i);
     }
}
