package noppes.npcs.controllers.data;

import java.util.Set;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ICompatibilty;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IAvailability;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.constants.EnumAvailabilityFaction;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.constants.EnumAvailabilityScoreboard;
import noppes.npcs.constants.EnumDayTime;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerQuestController;

public class Availability implements ICompatibilty, IAvailability {
     public int version;
     public EnumAvailabilityDialog dialogAvailable;
     public EnumAvailabilityDialog dialog2Available;
     public EnumAvailabilityDialog dialog3Available;
     public EnumAvailabilityDialog dialog4Available;
     public int dialogId;
     public int dialog2Id;
     public int dialog3Id;
     public int dialog4Id;
     public EnumAvailabilityQuest questAvailable;
     public EnumAvailabilityQuest quest2Available;
     public EnumAvailabilityQuest quest3Available;
     public EnumAvailabilityQuest quest4Available;
     public int questId;
     public int quest2Id;
     public int quest3Id;
     public int quest4Id;
     public EnumDayTime daytime;
     public int factionId;
     public int faction2Id;
     public EnumAvailabilityFactionType factionAvailable;
     public EnumAvailabilityFactionType faction2Available;
     public EnumAvailabilityFaction factionStance;
     public EnumAvailabilityFaction faction2Stance;
     public EnumAvailabilityScoreboard scoreboardType;
     public EnumAvailabilityScoreboard scoreboard2Type;
     public String scoreboardObjective;
     public String scoreboard2Objective;
     public int scoreboardValue;
     public int scoreboard2Value;
     public int minPlayerLevel;

     public Availability() {
          this.version = VersionCompatibility.ModRev;
          this.dialogAvailable = EnumAvailabilityDialog.Always;
          this.dialog2Available = EnumAvailabilityDialog.Always;
          this.dialog3Available = EnumAvailabilityDialog.Always;
          this.dialog4Available = EnumAvailabilityDialog.Always;
          this.dialogId = -1;
          this.dialog2Id = -1;
          this.dialog3Id = -1;
          this.dialog4Id = -1;
          this.questAvailable = EnumAvailabilityQuest.Always;
          this.quest2Available = EnumAvailabilityQuest.Always;
          this.quest3Available = EnumAvailabilityQuest.Always;
          this.quest4Available = EnumAvailabilityQuest.Always;
          this.questId = -1;
          this.quest2Id = -1;
          this.quest3Id = -1;
          this.quest4Id = -1;
          this.daytime = EnumDayTime.Always;
          this.factionId = -1;
          this.faction2Id = -1;
          this.factionAvailable = EnumAvailabilityFactionType.Always;
          this.faction2Available = EnumAvailabilityFactionType.Always;
          this.factionStance = EnumAvailabilityFaction.Friendly;
          this.faction2Stance = EnumAvailabilityFaction.Friendly;
          this.scoreboardType = EnumAvailabilityScoreboard.EQUAL;
          this.scoreboard2Type = EnumAvailabilityScoreboard.EQUAL;
          this.scoreboardObjective = "";
          this.scoreboard2Objective = "";
          this.scoreboardValue = 1;
          this.scoreboard2Value = 1;
          this.minPlayerLevel = 0;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.version = compound.getInteger("ModRev");
          VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
          this.dialogAvailable = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog")];
          this.dialog2Available = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog2")];
          this.dialog3Available = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog3")];
          this.dialog4Available = EnumAvailabilityDialog.values()[compound.getInteger("AvailabilityDialog4")];
          this.dialogId = compound.getInteger("AvailabilityDialogId");
          this.dialog2Id = compound.getInteger("AvailabilityDialog2Id");
          this.dialog3Id = compound.getInteger("AvailabilityDialog3Id");
          this.dialog4Id = compound.getInteger("AvailabilityDialog4Id");
          this.questAvailable = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest")];
          this.quest2Available = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest2")];
          this.quest3Available = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest3")];
          this.quest4Available = EnumAvailabilityQuest.values()[compound.getInteger("AvailabilityQuest4")];
          this.questId = compound.getInteger("AvailabilityQuestId");
          this.quest2Id = compound.getInteger("AvailabilityQuest2Id");
          this.quest3Id = compound.getInteger("AvailabilityQuest3Id");
          this.quest4Id = compound.getInteger("AvailabilityQuest4Id");
          this.setFactionAvailability(compound.getInteger("AvailabilityFaction"));
          this.setFactionAvailabilityStance(compound.getInteger("AvailabilityFactionStance"));
          this.setFaction2Availability(compound.getInteger("AvailabilityFaction2"));
          this.setFaction2AvailabilityStance(compound.getInteger("AvailabilityFaction2Stance"));
          this.factionId = compound.getInteger("AvailabilityFactionId");
          this.faction2Id = compound.getInteger("AvailabilityFaction2Id");
          this.scoreboardObjective = compound.getString("AvailabilityScoreboardObjective");
          this.scoreboard2Objective = compound.getString("AvailabilityScoreboard2Objective");
          this.initScore(this.scoreboardObjective);
          this.initScore(this.scoreboard2Objective);
          this.scoreboardType = EnumAvailabilityScoreboard.values()[compound.getInteger("AvailabilityScoreboardType")];
          this.scoreboard2Type = EnumAvailabilityScoreboard.values()[compound.getInteger("AvailabilityScoreboard2Type")];
          this.scoreboardValue = compound.getInteger("AvailabilityScoreboardValue");
          this.scoreboard2Value = compound.getInteger("AvailabilityScoreboard2Value");
          this.daytime = EnumDayTime.values()[compound.getInteger("AvailabilityDayTime")];
          this.minPlayerLevel = compound.getInteger("AvailabilityMinPlayerLevel");
     }

     private void initScore(String objective) {
          if (!objective.isEmpty()) {
               if (CustomNpcs.Server != null) {
                    WorldServer[] var2 = CustomNpcs.Server.field_71305_c;
                    int var3 = var2.length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                         WorldServer world = var2[var4];
                         ServerScoreboard board = (ServerScoreboard)world.field_96442_D;
                         ScoreObjective so = board.func_96518_b(objective);
                         if (so != null) {
                              Set addedObjectives = (Set)ObfuscationReflectionHelper.getPrivateValue(ServerScoreboard.class, board, 1);
                              if (!addedObjectives.contains(so)) {
                                   board.func_96549_e(so);
                              }
                         }
                    }

               }
          }
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("ModRev", this.version);
          compound.setInteger("AvailabilityDialog", this.dialogAvailable.ordinal());
          compound.setInteger("AvailabilityDialog2", this.dialog2Available.ordinal());
          compound.setInteger("AvailabilityDialog3", this.dialog3Available.ordinal());
          compound.setInteger("AvailabilityDialog4", this.dialog4Available.ordinal());
          compound.setInteger("AvailabilityDialogId", this.dialogId);
          compound.setInteger("AvailabilityDialog2Id", this.dialog2Id);
          compound.setInteger("AvailabilityDialog3Id", this.dialog3Id);
          compound.setInteger("AvailabilityDialog4Id", this.dialog4Id);
          compound.setInteger("AvailabilityQuest", this.questAvailable.ordinal());
          compound.setInteger("AvailabilityQuest2", this.quest2Available.ordinal());
          compound.setInteger("AvailabilityQuest3", this.quest3Available.ordinal());
          compound.setInteger("AvailabilityQuest4", this.quest4Available.ordinal());
          compound.setInteger("AvailabilityQuestId", this.questId);
          compound.setInteger("AvailabilityQuest2Id", this.quest2Id);
          compound.setInteger("AvailabilityQuest3Id", this.quest3Id);
          compound.setInteger("AvailabilityQuest4Id", this.quest4Id);
          compound.setInteger("AvailabilityFaction", this.factionAvailable.ordinal());
          compound.setInteger("AvailabilityFaction2", this.faction2Available.ordinal());
          compound.setInteger("AvailabilityFactionStance", this.factionStance.ordinal());
          compound.setInteger("AvailabilityFaction2Stance", this.faction2Stance.ordinal());
          compound.setInteger("AvailabilityFactionId", this.factionId);
          compound.setInteger("AvailabilityFaction2Id", this.faction2Id);
          compound.setString("AvailabilityScoreboardObjective", this.scoreboardObjective);
          compound.setString("AvailabilityScoreboard2Objective", this.scoreboard2Objective);
          compound.setInteger("AvailabilityScoreboardType", this.scoreboardType.ordinal());
          compound.setInteger("AvailabilityScoreboard2Type", this.scoreboard2Type.ordinal());
          compound.setInteger("AvailabilityScoreboardValue", this.scoreboardValue);
          compound.setInteger("AvailabilityScoreboard2Value", this.scoreboard2Value);
          compound.setInteger("AvailabilityDayTime", this.daytime.ordinal());
          compound.setInteger("AvailabilityMinPlayerLevel", this.minPlayerLevel);
          return compound;
     }

     public void setFactionAvailability(int value) {
          this.factionAvailable = EnumAvailabilityFactionType.values()[value];
     }

     public void setFaction2Availability(int value) {
          this.faction2Available = EnumAvailabilityFactionType.values()[value];
     }

     public void setFactionAvailabilityStance(int integer) {
          this.factionStance = EnumAvailabilityFaction.values()[integer];
     }

     public void setFaction2AvailabilityStance(int integer) {
          this.faction2Stance = EnumAvailabilityFaction.values()[integer];
     }

     public boolean isAvailable(EntityPlayer player) {
          long time;
          if (this.daytime == EnumDayTime.Day) {
               time = player.world.func_72820_D() % 24000L;
               if (time > 12000L) {
                    return false;
               }
          }

          if (this.daytime == EnumDayTime.Night) {
               time = player.world.func_72820_D() % 24000L;
               if (time < 12000L) {
                    return false;
               }
          }

          if (!this.dialogAvailable(this.dialogId, this.dialogAvailable, player)) {
               return false;
          } else if (!this.dialogAvailable(this.dialog2Id, this.dialog2Available, player)) {
               return false;
          } else if (!this.dialogAvailable(this.dialog3Id, this.dialog3Available, player)) {
               return false;
          } else if (!this.dialogAvailable(this.dialog4Id, this.dialog4Available, player)) {
               return false;
          } else if (!this.questAvailable(this.questId, this.questAvailable, player)) {
               return false;
          } else if (!this.questAvailable(this.quest2Id, this.quest2Available, player)) {
               return false;
          } else if (!this.questAvailable(this.quest3Id, this.quest3Available, player)) {
               return false;
          } else if (!this.questAvailable(this.quest4Id, this.quest4Available, player)) {
               return false;
          } else if (!this.factionAvailable(this.factionId, this.factionStance, this.factionAvailable, player)) {
               return false;
          } else if (!this.factionAvailable(this.faction2Id, this.faction2Stance, this.faction2Available, player)) {
               return false;
          } else if (!this.scoreboardAvailable(player, this.scoreboardObjective, this.scoreboardType, this.scoreboardValue)) {
               return false;
          } else if (!this.scoreboardAvailable(player, this.scoreboard2Objective, this.scoreboard2Type, this.scoreboard2Value)) {
               return false;
          } else {
               return player.field_71068_ca >= this.minPlayerLevel;
          }
     }

     private boolean scoreboardAvailable(EntityPlayer player, String objective, EnumAvailabilityScoreboard type, int value) {
          if (objective.isEmpty()) {
               return true;
          } else {
               ScoreObjective sbObjective = player.func_96123_co().func_96518_b(objective);
               if (sbObjective == null) {
                    return false;
               } else if (!player.func_96123_co().func_178819_b(player.func_70005_c_(), sbObjective)) {
                    return false;
               } else {
                    int i = player.func_96123_co().func_96529_a(player.func_70005_c_(), sbObjective).func_96652_c();
                    if (type == EnumAvailabilityScoreboard.EQUAL) {
                         return i == value;
                    } else if (type == EnumAvailabilityScoreboard.BIGGER) {
                         return i > value;
                    } else {
                         return i < value;
                    }
               }
          }
     }

     private boolean factionAvailable(int id, EnumAvailabilityFaction stance, EnumAvailabilityFactionType available, EntityPlayer player) {
          if (available == EnumAvailabilityFactionType.Always) {
               return true;
          } else {
               Faction faction = FactionController.instance.getFaction(id);
               if (faction == null) {
                    return true;
               } else {
                    PlayerFactionData data = PlayerData.get(player).factionData;
                    int points = data.getFactionPoints(player, id);
                    EnumAvailabilityFaction current = EnumAvailabilityFaction.Neutral;
                    if (points < faction.neutralPoints) {
                         current = EnumAvailabilityFaction.Hostile;
                    }

                    if (points >= faction.friendlyPoints) {
                         current = EnumAvailabilityFaction.Friendly;
                    }

                    if (available == EnumAvailabilityFactionType.Is && stance == current) {
                         return true;
                    } else {
                         return available == EnumAvailabilityFactionType.IsNot && stance != current;
                    }
               }
          }
     }

     public boolean dialogAvailable(int id, EnumAvailabilityDialog en, EntityPlayer player) {
          if (en == EnumAvailabilityDialog.Always) {
               return true;
          } else {
               boolean hasRead = PlayerData.get(player).dialogData.dialogsRead.contains(id);
               if (hasRead && en == EnumAvailabilityDialog.After) {
                    return true;
               } else {
                    return !hasRead && en == EnumAvailabilityDialog.Before;
               }
          }
     }

     public boolean questAvailable(int id, EnumAvailabilityQuest en, EntityPlayer player) {
          if (en == EnumAvailabilityQuest.Always) {
               return true;
          } else if (en == EnumAvailabilityQuest.After && PlayerQuestController.isQuestFinished(player, id)) {
               return true;
          } else if (en == EnumAvailabilityQuest.Before && !PlayerQuestController.isQuestFinished(player, id)) {
               return true;
          } else if (en == EnumAvailabilityQuest.Active && PlayerQuestController.isQuestActive(player, id)) {
               return true;
          } else if (en == EnumAvailabilityQuest.NotActive && !PlayerQuestController.isQuestActive(player, id)) {
               return true;
          } else if (en == EnumAvailabilityQuest.Completed && PlayerQuestController.isQuestCompleted(player, id)) {
               return true;
          } else {
               return en == EnumAvailabilityQuest.CanStart && PlayerQuestController.canQuestBeAccepted(player, id);
          }
     }

     public int getVersion() {
          return this.version;
     }

     public void setVersion(int version) {
          this.version = version;
     }

     public boolean isAvailable(IPlayer player) {
          return this.isAvailable((EntityPlayer)player.getMCEntity());
     }

     public int getDaytime() {
          return this.daytime.ordinal();
     }

     public void setDaytime(int type) {
          this.daytime = EnumDayTime.values()[MathHelper.func_76125_a(type, 0, 2)];
     }

     public int getMinPlayerLevel() {
          return this.minPlayerLevel;
     }

     public void setMinPlayerLevel(int level) {
          this.minPlayerLevel = level;
     }

     public int getDialog(int i) {
          if (i < 0 && i > 3) {
               throw new CustomNPCsException(i + " isnt between 0 and 3", new Object[0]);
          } else if (i == 0) {
               return this.dialogId;
          } else if (i == 1) {
               return this.dialog2Id;
          } else {
               return i == 2 ? this.dialog3Id : this.dialog4Id;
          }
     }

     public void setDialog(int i, int id, int type) {
          if (i < 0 && i > 3) {
               throw new CustomNPCsException(i + " isnt between 0 and 3", new Object[0]);
          } else {
               EnumAvailabilityDialog e = EnumAvailabilityDialog.values()[MathHelper.func_76125_a(type, 0, 2)];
               if (i == 0) {
                    this.dialogId = id;
                    this.dialogAvailable = e;
               } else if (i == 1) {
                    this.dialog2Id = id;
                    this.dialog2Available = e;
               } else if (i == 2) {
                    this.dialog3Id = id;
                    this.dialog3Available = e;
               } else if (i == 3) {
                    this.dialog4Id = id;
                    this.dialog4Available = e;
               }

          }
     }

     public void removeDialog(int i) {
          if (i < 0 && i > 3) {
               throw new CustomNPCsException(i + " isnt between 0 and 3", new Object[0]);
          } else {
               if (i == 0) {
                    this.dialogId = -1;
                    this.dialogAvailable = EnumAvailabilityDialog.Always;
               } else if (i == 1) {
                    this.dialog2Id = -1;
                    this.dialog2Available = EnumAvailabilityDialog.Always;
               } else if (i == 2) {
                    this.dialog3Id = -1;
                    this.dialog3Available = EnumAvailabilityDialog.Always;
               } else if (i == 3) {
                    this.dialog4Id = -1;
                    this.dialog4Available = EnumAvailabilityDialog.Always;
               }

          }
     }

     public int getQuest(int i) {
          if (i < 0 && i > 3) {
               throw new CustomNPCsException(i + " isnt between 0 and 3", new Object[0]);
          } else if (i == 0) {
               return this.questId;
          } else if (i == 1) {
               return this.quest2Id;
          } else {
               return i == 2 ? this.quest3Id : this.quest4Id;
          }
     }

     public void setQuest(int i, int id, int type) {
          if (i < 0 && i > 3) {
               throw new CustomNPCsException(i + " isnt between 0 and 3", new Object[0]);
          } else {
               EnumAvailabilityQuest e = EnumAvailabilityQuest.values()[MathHelper.func_76125_a(type, 0, 5)];
               if (i == 0) {
                    this.questId = id;
                    this.questAvailable = e;
               } else if (i == 1) {
                    this.quest2Id = id;
                    this.quest2Available = e;
               } else if (i == 2) {
                    this.quest3Id = id;
                    this.quest3Available = e;
               } else if (i == 3) {
                    this.quest4Id = id;
                    this.quest4Available = e;
               }

          }
     }

     public void removeQuest(int i) {
          if (i < 0 && i > 3) {
               throw new CustomNPCsException(i + " isnt between 0 and 3", new Object[0]);
          } else {
               if (i == 0) {
                    this.questId = -1;
                    this.questAvailable = EnumAvailabilityQuest.Always;
               } else if (i == 1) {
                    this.quest2Id = -1;
                    this.quest2Available = EnumAvailabilityQuest.Always;
               } else if (i == 2) {
                    this.quest3Id = -1;
                    this.quest3Available = EnumAvailabilityQuest.Always;
               } else if (i == 3) {
                    this.quest4Id = -1;
                    this.quest4Available = EnumAvailabilityQuest.Always;
               }

          }
     }

     public void setFaction(int i, int id, int type, int stance) {
          if (i < 0 && i > 1) {
               throw new CustomNPCsException(i + " isnt between 0 and 1", new Object[0]);
          } else {
               EnumAvailabilityFactionType e = EnumAvailabilityFactionType.values()[MathHelper.func_76125_a(type, 0, 2)];
               EnumAvailabilityFaction ee = EnumAvailabilityFaction.values()[MathHelper.func_76125_a(stance, 0, 2)];
               if (i == 0) {
                    this.factionId = id;
                    this.factionAvailable = e;
                    this.factionStance = ee;
               } else if (i == 1) {
                    this.faction2Id = id;
                    this.faction2Available = e;
                    this.faction2Stance = ee;
               }

          }
     }

     public void setScoreboard(int i, String objective, int type, int value) {
          if (i < 0 && i > 1) {
               throw new CustomNPCsException(i + " isnt between 0 and 1", new Object[0]);
          } else {
               if (objective == null) {
                    objective = "";
               }

               EnumAvailabilityScoreboard e = EnumAvailabilityScoreboard.values()[MathHelper.func_76125_a(type, 0, 2)];
               if (i == 0) {
                    this.scoreboardObjective = objective;
                    this.scoreboardType = e;
                    this.scoreboardValue = value;
               } else if (i == 1) {
                    this.scoreboard2Objective = objective;
                    this.scoreboard2Type = e;
                    this.scoreboard2Value = value;
               }

          }
     }

     public void removeFaction(int i) {
          if (i < 0 && i > 1) {
               throw new CustomNPCsException(i + " isnt between 0 and 1", new Object[0]);
          } else {
               if (i == 0) {
                    this.factionId = -1;
                    this.factionAvailable = EnumAvailabilityFactionType.Always;
                    this.factionStance = EnumAvailabilityFaction.Friendly;
               } else if (i == 1) {
                    this.faction2Id = -1;
                    this.faction2Available = EnumAvailabilityFactionType.Always;
                    this.faction2Stance = EnumAvailabilityFaction.Friendly;
               }

          }
     }
}
