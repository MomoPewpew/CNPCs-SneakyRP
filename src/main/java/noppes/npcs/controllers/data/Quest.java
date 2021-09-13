package noppes.npcs.controllers.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ICompatibilty;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.Server;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.quests.QuestDialog;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.quests.QuestItem;
import noppes.npcs.quests.QuestKill;
import noppes.npcs.quests.QuestLocation;
import noppes.npcs.quests.QuestManual;

public class Quest implements ICompatibilty, IQuest {
     public int version;
     public int id;
     public int type;
     public EnumQuestRepeat repeat;
     public EnumQuestCompletion completion;
     public String title;
     public final QuestCategory category;
     public String logText;
     public String completeText;
     public String completerNpc;
     public int nextQuestid;
     public String nextQuestTitle;
     public PlayerMail mail;
     public String command;
     public QuestInterface questInterface;
     public int rewardExp;
     public NpcMiscInventory rewardItems;
     public boolean randomReward;
     public FactionOptions factionOptions;

     public Quest(QuestCategory category) {
          this.version = VersionCompatibility.ModRev;
          this.id = -1;
          this.type = 0;
          this.repeat = EnumQuestRepeat.NONE;
          this.completion = EnumQuestCompletion.Npc;
          this.title = "default";
          this.logText = "";
          this.completeText = "";
          this.completerNpc = "";
          this.nextQuestid = -1;
          this.nextQuestTitle = "";
          this.mail = new PlayerMail();
          this.command = "";
          this.questInterface = new QuestItem();
          this.rewardExp = 0;
          this.rewardItems = new NpcMiscInventory(9);
          this.randomReward = false;
          this.factionOptions = new FactionOptions();
          this.category = category;
     }

     public void readNBT(NBTTagCompound compound) {
          this.id = compound.func_74762_e("Id");
          this.readNBTPartial(compound);
     }

     public void readNBTPartial(NBTTagCompound compound) {
          this.version = compound.func_74762_e("ModRev");
          VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
          this.setType(compound.func_74762_e("Type"));
          this.title = compound.getString("Title");
          this.logText = compound.getString("Text");
          this.completeText = compound.getString("CompleteText");
          this.completerNpc = compound.getString("CompleterNpc");
          this.command = compound.getString("QuestCommand");
          this.nextQuestid = compound.func_74762_e("NextQuestId");
          this.nextQuestTitle = compound.getString("NextQuestTitle");
          if (this.hasNewQuest()) {
               this.nextQuestTitle = this.getNextQuest().title;
          } else {
               this.nextQuestTitle = "";
          }

          this.randomReward = compound.getBoolean("RandomReward");
          this.rewardExp = compound.func_74762_e("RewardExp");
          this.rewardItems.setFromNBT(compound.getCompoundTag("Rewards"));
          this.completion = EnumQuestCompletion.values()[compound.func_74762_e("QuestCompletion")];
          this.repeat = EnumQuestRepeat.values()[compound.func_74762_e("QuestRepeat")];
          this.questInterface.readEntityFromNBT(compound);
          this.factionOptions.readFromNBT(compound.getCompoundTag("QuestFactionPoints"));
          this.mail.readNBT(compound.getCompoundTag("QuestMail"));
     }

     public void setType(int questType) {
          this.type = questType;
          if (this.type == 0) {
               this.questInterface = new QuestItem();
          } else if (this.type == 1) {
               this.questInterface = new QuestDialog();
          } else if (this.type != 2 && this.type != 4) {
               if (this.type == 3) {
                    this.questInterface = new QuestLocation();
               } else if (this.type == 5) {
                    this.questInterface = new QuestManual();
               }
          } else {
               this.questInterface = new QuestKill();
          }

          if (this.questInterface != null) {
               this.questInterface.questId = this.id;
          }

     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("Id", this.id);
          return this.writeToNBTPartial(compound);
     }

     public NBTTagCompound writeToNBTPartial(NBTTagCompound compound) {
          compound.setInteger("ModRev", this.version);
          compound.setInteger("Type", this.type);
          compound.setString("Title", this.title);
          compound.setString("Text", this.logText);
          compound.setString("CompleteText", this.completeText);
          compound.setString("CompleterNpc", this.completerNpc);
          compound.setInteger("NextQuestId", this.nextQuestid);
          compound.setString("NextQuestTitle", this.nextQuestTitle);
          compound.setInteger("RewardExp", this.rewardExp);
          compound.setTag("Rewards", this.rewardItems.getToNBT());
          compound.setString("QuestCommand", this.command);
          compound.func_74757_a("RandomReward", this.randomReward);
          compound.setInteger("QuestCompletion", this.completion.ordinal());
          compound.setInteger("QuestRepeat", this.repeat.ordinal());
          this.questInterface.writeEntityToNBT(compound);
          compound.setTag("QuestFactionPoints", this.factionOptions.writeToNBT(new NBTTagCompound()));
          compound.setTag("QuestMail", this.mail.writeNBT());
          return compound;
     }

     public boolean hasNewQuest() {
          return this.getNextQuest() != null;
     }

     public Quest getNextQuest() {
          return QuestController.instance == null ? null : (Quest)QuestController.instance.quests.get(this.nextQuestid);
     }

     public boolean complete(EntityPlayer player, QuestData data) {
          if (this.completion == EnumQuestCompletion.Instant) {
               Server.sendData((EntityPlayerMP)player, EnumPacketClient.QUEST_COMPLETION, data.quest.id);
               return true;
          } else {
               return false;
          }
     }

     public Quest copy() {
          Quest quest = new Quest(this.category);
          quest.readNBT(this.writeToNBT(new NBTTagCompound()));
          return quest;
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

     public int getType() {
          return this.type;
     }

     public IQuestCategory getCategory() {
          return this.category;
     }

     public void save() {
          QuestController.instance.saveQuest(this.category, this);
     }

     public void setName(String name) {
          this.title = name;
     }

     public String getLogText() {
          return this.logText;
     }

     public void setLogText(String text) {
          this.logText = text;
     }

     public String getCompleteText() {
          return this.completeText;
     }

     public void setCompleteText(String text) {
          this.completeText = text;
     }

     public void setNextQuest(IQuest quest) {
          if (quest == null) {
               this.nextQuestid = -1;
               this.nextQuestTitle = "";
          } else {
               if (quest.getId() < 0) {
                    throw new CustomNPCsException("Quest id is lower than 0", new Object[0]);
               }

               this.nextQuestid = quest.getId();
               this.nextQuestTitle = quest.getName();
          }

     }

     public String getNpcName() {
          return this.completerNpc;
     }

     public void setNpcName(String name) {
          this.completerNpc = name;
     }

     public IQuestObjective[] getObjectives(IPlayer player) {
          if (!player.hasActiveQuest(this.id)) {
               throw new CustomNPCsException("Player doesnt have this quest active.", new Object[0]);
          } else {
               return this.questInterface.getObjectives(player.getMCEntity());
          }
     }

     public boolean getIsRepeatable() {
          return this.repeat != EnumQuestRepeat.NONE;
     }

     public IContainer getRewards() {
          return NpcAPI.Instance().getIContainer((IInventory)this.rewardItems);
     }
}
