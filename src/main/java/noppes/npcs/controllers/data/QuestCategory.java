package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;

public class QuestCategory implements IQuestCategory {
     public HashMap quests = new HashMap();
     public int id = -1;
     public String title = "";

     public void readNBT(NBTTagCompound nbttagcompound) {
          this.id = nbttagcompound.func_74762_e("Slot");
          this.title = nbttagcompound.getString("Title");
          NBTTagList dialogsList = nbttagcompound.getTagList("Dialogs", 10);
          if (dialogsList != null) {
               for(int ii = 0; ii < dialogsList.tagCount(); ++ii) {
                    NBTTagCompound nbttagcompound2 = dialogsList.getCompoundTagAt(ii);
                    Quest quest = new Quest(this);
                    quest.readNBT(nbttagcompound2);
                    this.quests.put(quest.id, quest);
               }
          }

     }

     public NBTTagCompound writeNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.setInteger("Slot", this.id);
          nbttagcompound.setString("Title", this.title);
          NBTTagList dialogs = new NBTTagList();
          Iterator var3 = this.quests.keySet().iterator();

          while(var3.hasNext()) {
               int dialogId = (Integer)var3.next();
               Quest quest = (Quest)this.quests.get(dialogId);
               dialogs.appendTag(quest.writeToNBT(new NBTTagCompound()));
          }

          nbttagcompound.setTag("Dialogs", dialogs);
          return nbttagcompound;
     }

     public List quests() {
          return new ArrayList(this.quests.values());
     }

     public String getName() {
          return this.title;
     }

     public IQuest create() {
          return new Quest(this);
     }
}
