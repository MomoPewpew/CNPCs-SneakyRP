package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.ICompatibilty;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IAvailability;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IDialogCategory;
import noppes.npcs.api.handler.data.IDialogOption;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.QuestController;

public class Dialog implements ICompatibilty, IDialog {
     public int version;
     public int id;
     public String title;
     public String text;
     public int quest;
     public final DialogCategory category;
     public HashMap options;
     public Availability availability;
     public FactionOptions factionOptions;
     public String sound;
     public String command;
     public PlayerMail mail;
     public boolean hideNPC;
     public boolean showWheel;
     public boolean disableEsc;

     public Dialog(DialogCategory category) {
          this.version = VersionCompatibility.ModRev;
          this.id = -1;
          this.title = "";
          this.text = "";
          this.quest = -1;
          this.options = new HashMap();
          this.availability = new Availability();
          this.factionOptions = new FactionOptions();
          this.command = "";
          this.mail = new PlayerMail();
          this.hideNPC = false;
          this.showWheel = false;
          this.disableEsc = false;
          this.category = category;
     }

     public boolean hasDialogs(EntityPlayer player) {
          Iterator var2 = this.options.values().iterator();

          DialogOption option;
          do {
               if (!var2.hasNext()) {
                    return false;
               }

               option = (DialogOption)var2.next();
          } while(option == null || option.optionType != 1 || !option.hasDialog() || !option.isAvailable(player));

          return true;
     }

     public void readNBT(NBTTagCompound compound) {
          this.id = compound.func_74762_e("DialogId");
          this.readNBTPartial(compound);
     }

     public void readNBTPartial(NBTTagCompound compound) {
          this.version = compound.func_74762_e("ModRev");
          VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
          this.title = compound.func_74779_i("DialogTitle");
          this.text = compound.func_74779_i("DialogText");
          this.quest = compound.func_74762_e("DialogQuest");
          this.sound = compound.func_74779_i("DialogSound");
          this.command = compound.func_74779_i("DialogCommand");
          this.mail.readNBT(compound.func_74775_l("DialogMail"));
          this.hideNPC = compound.func_74767_n("DialogHideNPC");
          this.showWheel = compound.func_74767_n("DialogShowWheel");
          this.disableEsc = compound.func_74767_n("DialogDisableEsc");
          NBTTagList options = compound.func_150295_c("Options", 10);
          HashMap newoptions = new HashMap();

          for(int iii = 0; iii < options.func_74745_c(); ++iii) {
               NBTTagCompound option = options.func_150305_b(iii);
               int opslot = option.func_74762_e("OptionSlot");
               DialogOption dia = new DialogOption();
               dia.readNBT(option.func_74775_l("Option"));
               newoptions.put(opslot, dia);
               dia.slot = opslot;
          }

          this.options = newoptions;
          this.availability.readFromNBT(compound);
          this.factionOptions.readFromNBT(compound);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.func_74768_a("DialogId", this.id);
          return this.writeToNBTPartial(compound);
     }

     public NBTTagCompound writeToNBTPartial(NBTTagCompound compound) {
          compound.func_74778_a("DialogTitle", this.title);
          compound.func_74778_a("DialogText", this.text);
          compound.func_74768_a("DialogQuest", this.quest);
          compound.func_74778_a("DialogCommand", this.command);
          compound.func_74782_a("DialogMail", this.mail.writeNBT());
          compound.func_74757_a("DialogHideNPC", this.hideNPC);
          compound.func_74757_a("DialogShowWheel", this.showWheel);
          compound.func_74757_a("DialogDisableEsc", this.disableEsc);
          if (this.sound != null && !this.sound.isEmpty()) {
               compound.func_74778_a("DialogSound", this.sound);
          }

          NBTTagList options = new NBTTagList();
          Iterator var3 = this.options.keySet().iterator();

          while(var3.hasNext()) {
               int opslot = (Integer)var3.next();
               NBTTagCompound listcompound = new NBTTagCompound();
               listcompound.func_74768_a("OptionSlot", opslot);
               listcompound.func_74782_a("Option", ((DialogOption)this.options.get(opslot)).writeNBT());
               options.func_74742_a(listcompound);
          }

          compound.func_74782_a("Options", options);
          this.availability.writeToNBT(compound);
          this.factionOptions.writeToNBT(compound);
          compound.func_74768_a("ModRev", this.version);
          return compound;
     }

     public boolean hasQuest() {
          return this.getQuest() != null;
     }

     public Quest getQuest() {
          return QuestController.instance == null ? null : (Quest)QuestController.instance.quests.get(this.quest);
     }

     public boolean hasOtherOptions() {
          Iterator var1 = this.options.values().iterator();

          DialogOption option;
          do {
               if (!var1.hasNext()) {
                    return false;
               }

               option = (DialogOption)var1.next();
          } while(option == null || option.optionType == 2);

          return true;
     }

     public Dialog copy(EntityPlayer player) {
          Dialog dialog = new Dialog(this.category);
          dialog.id = this.id;
          dialog.text = this.text;
          dialog.title = this.title;
          dialog.quest = this.quest;
          dialog.sound = this.sound;
          dialog.mail = this.mail;
          dialog.command = this.command;
          dialog.hideNPC = this.hideNPC;
          dialog.showWheel = this.showWheel;
          dialog.disableEsc = this.disableEsc;
          Iterator var3 = this.options.keySet().iterator();

          while(true) {
               int slot;
               DialogOption option;
               do {
                    if (!var3.hasNext()) {
                         return dialog;
                    }

                    slot = (Integer)var3.next();
                    option = (DialogOption)this.options.get(slot);
               } while(option.optionType == 1 && (!option.hasDialog() || !option.isAvailable(player)));

               dialog.options.put(slot, option);
          }
     }

     public int getVersion() {
          return this.version;
     }

     public void setVersion(int version) {
          this.version = version;
     }

     public int getId() {
          return this.id;
     }

     public String getName() {
          return this.title;
     }

     public List getOptions() {
          return new ArrayList(this.options.values());
     }

     public IDialogOption getOption(int slot) {
          IDialogOption option = (IDialogOption)this.options.get(slot);
          if (option == null) {
               throw new CustomNPCsException("There is no DialogOption for slot: " + slot, new Object[0]);
          } else {
               return option;
          }
     }

     public IAvailability getAvailability() {
          return this.availability;
     }

     public IDialogCategory getCategory() {
          return this.category;
     }

     public void save() {
          DialogController.instance.saveDialog(this.category, this);
     }

     public void setName(String name) {
          this.title = name;
     }

     public String getText() {
          return this.text;
     }

     public void setText(String text) {
          this.text = text;
     }

     public void setQuest(IQuest quest) {
          if (quest == null) {
               this.quest = -1;
          } else {
               if (quest.getId() < 0) {
                    throw new CustomNPCsException("Quest id is lower than 0", new Object[0]);
               }

               this.quest = quest.getId();
          }

     }

     public String getCommand() {
          return this.command;
     }

     public void setCommand(String command) {
          this.command = command;
     }
}
