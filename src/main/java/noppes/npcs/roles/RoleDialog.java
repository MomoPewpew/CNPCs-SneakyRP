package noppes.npcs.roles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.role.IRoleDialog;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleDialog extends RoleInterface implements IRoleDialog {
     public String dialog = "";
     public int questId = -1;
     public HashMap options = new HashMap();
     public HashMap optionsTexts = new HashMap();

     public RoleDialog(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.func_74768_a("RoleQuestId", this.questId);
          compound.func_74778_a("RoleDialog", this.dialog);
          compound.func_74782_a("RoleOptions", NBTTags.nbtIntegerStringMap(this.options));
          compound.func_74782_a("RoleOptionTexts", NBTTags.nbtIntegerStringMap(this.optionsTexts));
          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.questId = compound.func_74762_e("RoleQuestId");
          this.dialog = compound.func_74779_i("RoleDialog");
          this.options = NBTTags.getIntegerStringMap(compound.func_150295_c("RoleOptions", 10));
          this.optionsTexts = NBTTags.getIntegerStringMap(compound.func_150295_c("RoleOptionTexts", 10));
     }

     public void interact(EntityPlayer player) {
          if (this.dialog.isEmpty()) {
               this.npc.say(player, this.npc.advanced.getInteractLine());
          } else {
               Dialog d = new Dialog((DialogCategory)null);
               d.text = this.dialog;
               Iterator var3 = this.options.entrySet().iterator();

               label33:
               while(true) {
                    Entry entry;
                    do {
                         if (!var3.hasNext()) {
                              NoppesUtilServer.openDialog(player, this.npc, d);
                              break label33;
                         }

                         entry = (Entry)var3.next();
                    } while(((String)entry.getValue()).isEmpty());

                    DialogOption option = new DialogOption();
                    String text = (String)this.optionsTexts.get(entry.getKey());
                    if (text != null && !text.isEmpty()) {
                         option.optionType = 3;
                    } else {
                         option.optionType = 0;
                    }

                    option.title = (String)entry.getValue();
                    d.options.put(entry.getKey(), option);
               }
          }

          Quest quest = (Quest)QuestController.instance.quests.get(this.questId);
          if (quest != null) {
               PlayerQuestController.addActiveQuest(quest, player);
          }

     }

     public String getDialog() {
          return this.dialog;
     }

     public void setDialog(String text) {
          this.dialog = text;
     }

     public String getOption(int option) {
          return (String)this.options.get(option);
     }

     public void setOption(int option, String text) {
          if (option >= 1 && option <= 6) {
               this.options.put(option, text);
          } else {
               throw new CustomNPCsException("Wrong dialog option slot given: " + option, new Object[0]);
          }
     }

     public String getOptionDialog(int option) {
          return (String)this.optionsTexts.get(option);
     }

     public void setOptionDialog(int option, String text) {
          if (option >= 1 && option <= 6) {
               this.optionsTexts.put(option, text);
          } else {
               throw new CustomNPCsException("Wrong dialog option slot given: " + option, new Object[0]);
          }
     }
}
