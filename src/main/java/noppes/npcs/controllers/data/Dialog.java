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
          this.id = compound.getInteger("DialogId");
          this.readNBTPartial(compound);
     }

     public void readNBTPartial(NBTTagCompound compound) {
          this.version = compound.getInteger("ModRev");
          VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
          this.title = compound.getString("DialogTitle");
          this.text = compound.getString("DialogText");
          this.quest = compound.getInteger("DialogQuest");
          this.sound = compound.getString("DialogSound");
          this.command = compound.getString("DialogCommand");
          this.mail.readNBT(compound.getCompoundTag("DialogMail"));
          this.hideNPC = compound.getBoolean("DialogHideNPC");
          this.showWheel = compound.getBoolean("DialogShowWheel");
          this.disableEsc = compound.getBoolean("DialogDisableEsc");
          NBTTagList options = compound.getTagList("Options", 10);
          HashMap newoptions = new HashMap();

          for(int iii = 0; iii < options.tagCount(); ++iii) {
               NBTTagCompound option = options.getCompoundTagAt(iii);
               int opslot = option.getInteger("OptionSlot");
               DialogOption dia = new DialogOption();
               dia.readNBT(option.getCompoundTag("Option"));
               newoptions.put(opslot, dia);
               dia.slot = opslot;
          }

          this.options = newoptions;
          this.availability.readFromNBT(compound);
          this.factionOptions.readFromNBT(compound);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("DialogId", this.id);
          return this.writeToNBTPartial(compound);
     }

     public NBTTagCompound writeToNBTPartial(NBTTagCompound compound) {
          compound.setString("DialogTitle", this.title);
          compound.setString("DialogText", this.text);
          compound.setInteger("DialogQuest", this.quest);
          compound.setString("DialogCommand", this.command);
          compound.setTag("DialogMail", this.mail.writeNBT());
          compound.setBoolean("DialogHideNPC", this.hideNPC);
          compound.setBoolean("DialogShowWheel", this.showWheel);
          compound.setBoolean("DialogDisableEsc", this.disableEsc);
          if (this.sound != null && !this.sound.isEmpty()) {
               compound.setString("DialogSound", this.sound);
          }

          NBTTagList options = new NBTTagList();
          Iterator var3 = this.options.keySet().iterator();

          while(var3.hasNext()) {
               int opslot = (Integer)var3.next();
               NBTTagCompound listcompound = new NBTTagCompound();
               listcompound.setInteger("OptionSlot", opslot);
               listcompound.setTag("Option", ((DialogOption)this.options.get(opslot)).writeNBT());
               options.appendTag(listcompound);
          }

          compound.setTag("Options", options);
          this.availability.writeToNBT(compound);
          this.factionOptions.writeToNBT(compound);
          compound.setInteger("ModRev", this.version);
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
