package noppes.npcs.entity.data;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.Server;
import noppes.npcs.api.entity.data.INPCAdvanced;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.FactionOptions;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.Lines;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobBuilder;
import noppes.npcs.roles.JobChunkLoader;
import noppes.npcs.roles.JobConversation;
import noppes.npcs.roles.JobFarmer;
import noppes.npcs.roles.JobFollower;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.roles.JobHealer;
import noppes.npcs.roles.JobItemGiver;
import noppes.npcs.roles.JobPuppet;
import noppes.npcs.roles.JobSpawner;
import noppes.npcs.roles.RoleBank;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleDialog;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RolePostman;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.roles.RoleTransporter;
import noppes.npcs.util.ValueUtil;

public class DataAdvanced implements INPCAdvanced {
     public Lines interactLines = new Lines();
     public Lines worldLines = new Lines();
     public Lines attackLines = new Lines();
     public Lines killedLines = new Lines();
     public Lines killLines = new Lines();
     public Lines npcInteractLines = new Lines();
     public boolean orderedLines = false;
     private String idleSound = "";
     private String angrySound = "";
     private String hurtSound = "minecraft:entity.player.hurt";
     private String deathSound = "minecraft:entity.player.hurt";
     private String stepSound = "";
     private EntityNPCInterface npc;
     public FactionOptions factions = new FactionOptions();
     public int role = 0;
     public int job = 0;
     public boolean attackOtherFactions = false;
     public boolean defendFaction = false;
     public boolean disablePitch = false;
     public DataScenes scenes;

     public DataAdvanced(EntityNPCInterface npc) {
          this.npc = npc;
          this.scenes = new DataScenes(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.func_74782_a("NpcLines", this.worldLines.writeToNBT());
          compound.func_74782_a("NpcKilledLines", this.killedLines.writeToNBT());
          compound.func_74782_a("NpcInteractLines", this.interactLines.writeToNBT());
          compound.func_74782_a("NpcAttackLines", this.attackLines.writeToNBT());
          compound.func_74782_a("NpcKillLines", this.killLines.writeToNBT());
          compound.func_74782_a("NpcInteractNPCLines", this.npcInteractLines.writeToNBT());
          compound.func_74757_a("OrderedLines", this.orderedLines);
          compound.func_74778_a("NpcIdleSound", this.idleSound);
          compound.func_74778_a("NpcAngrySound", this.angrySound);
          compound.func_74778_a("NpcHurtSound", this.hurtSound);
          compound.func_74778_a("NpcDeathSound", this.deathSound);
          compound.func_74778_a("NpcStepSound", this.stepSound);
          compound.func_74768_a("FactionID", this.npc.getFaction().id);
          compound.func_74757_a("AttackOtherFactions", this.attackOtherFactions);
          compound.func_74757_a("DefendFaction", this.defendFaction);
          compound.func_74757_a("DisablePitch", this.disablePitch);
          compound.func_74768_a("Role", this.role);
          compound.func_74768_a("NpcJob", this.job);
          compound.func_74782_a("FactionPoints", this.factions.writeToNBT(new NBTTagCompound()));
          compound.func_74782_a("NPCDialogOptions", this.nbtDialogs(this.npc.dialogs));
          compound.func_74782_a("NpcScenes", this.scenes.writeToNBT(new NBTTagCompound()));
          return compound;
     }

     public void readToNBT(NBTTagCompound compound) {
          this.interactLines.readNBT(compound.func_74775_l("NpcInteractLines"));
          this.worldLines.readNBT(compound.func_74775_l("NpcLines"));
          this.attackLines.readNBT(compound.func_74775_l("NpcAttackLines"));
          this.killedLines.readNBT(compound.func_74775_l("NpcKilledLines"));
          this.killLines.readNBT(compound.func_74775_l("NpcKillLines"));
          this.npcInteractLines.readNBT(compound.func_74775_l("NpcInteractNPCLines"));
          this.orderedLines = compound.func_74767_n("OrderedLines");
          this.idleSound = compound.func_74779_i("NpcIdleSound");
          this.angrySound = compound.func_74779_i("NpcAngrySound");
          this.hurtSound = compound.func_74779_i("NpcHurtSound");
          this.deathSound = compound.func_74779_i("NpcDeathSound");
          this.stepSound = compound.func_74779_i("NpcStepSound");
          this.npc.setFaction(compound.func_74762_e("FactionID"));
          this.npc.faction = this.npc.getFaction();
          this.attackOtherFactions = compound.func_74767_n("AttackOtherFactions");
          this.defendFaction = compound.func_74767_n("DefendFaction");
          this.disablePitch = compound.func_74767_n("DisablePitch");
          this.setRole(compound.func_74762_e("Role"));
          this.setJob(compound.func_74762_e("NpcJob"));
          this.factions.readFromNBT(compound.func_74775_l("FactionPoints"));
          this.npc.dialogs = this.getDialogs(compound.func_150295_c("NPCDialogOptions", 10));
          this.scenes.readFromNBT(compound.func_74775_l("NpcScenes"));
     }

     private HashMap getDialogs(NBTTagList tagList) {
          HashMap map = new HashMap();

          for(int i = 0; i < tagList.func_74745_c(); ++i) {
               NBTTagCompound nbttagcompound = tagList.func_150305_b(i);
               int slot = nbttagcompound.func_74762_e("DialogSlot");
               DialogOption option = new DialogOption();
               option.readNBT(nbttagcompound.func_74775_l("NPCDialog"));
               option.optionType = 1;
               map.put(slot, option);
          }

          return map;
     }

     private NBTTagList nbtDialogs(HashMap dialogs2) {
          NBTTagList nbttaglist = new NBTTagList();
          Iterator var3 = dialogs2.keySet().iterator();

          while(var3.hasNext()) {
               int slot = (Integer)var3.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74768_a("DialogSlot", slot);
               nbttagcompound.func_74782_a("NPCDialog", ((DialogOption)dialogs2.get(slot)).writeNBT());
               nbttaglist.func_74742_a(nbttagcompound);
          }

          return nbttaglist;
     }

     private Lines getLines(int type) {
          if (type == 0) {
               return this.interactLines;
          } else if (type == 1) {
               return this.attackLines;
          } else if (type == 2) {
               return this.worldLines;
          } else if (type == 3) {
               return this.killedLines;
          } else if (type == 4) {
               return this.killLines;
          } else {
               return type == 5 ? this.npcInteractLines : null;
          }
     }

     public void setLine(int type, int slot, String text, String sound) {
          slot = ValueUtil.CorrectInt(slot, 0, 7);
          Lines lines = this.getLines(type);
          if (text != null && !text.isEmpty()) {
               Line line = (Line)lines.lines.get(slot);
               if (line == null) {
                    lines.lines.put(slot, line = new Line());
               }

               line.setText(text);
               line.setSound(sound);
          } else {
               lines.lines.remove(slot);
          }

     }

     public String getLine(int type, int slot) {
          Line line = (Line)this.getLines(type).lines.get(slot);
          return line == null ? null : line.getText();
     }

     public int getLineCount(int type) {
          return this.getLines(type).lines.size();
     }

     public String getSound(int type) {
          String sound = null;
          if (type == 0) {
               sound = this.idleSound;
          } else if (type == 1) {
               sound = this.angrySound;
          } else if (type == 2) {
               sound = this.hurtSound;
          } else if (type == 3) {
               sound = this.deathSound;
          } else if (type == 4) {
               sound = this.stepSound;
          }

          return sound != null && sound.isEmpty() ? null : sound;
     }

     public void playSound(int type, float volume, float pitch) {
          String sound = this.getSound(type);
          if (sound != null) {
               BlockPos pos = this.npc.func_180425_c();
               Server.sendRangedData(this.npc, 16, EnumPacketClient.PLAY_SOUND, sound, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), volume, pitch);
          }
     }

     public void setSound(int type, String sound) {
          if (sound == null) {
               sound = "";
          }

          if (type == 0) {
               this.idleSound = sound;
          } else if (type == 1) {
               this.angrySound = sound;
          } else if (type == 2) {
               this.hurtSound = sound;
          } else if (type == 3) {
               this.deathSound = sound;
          } else if (type == 4) {
               this.stepSound = sound;
          }

     }

     public Line getInteractLine() {
          return this.interactLines.getLine(!this.orderedLines);
     }

     public Line getAttackLine() {
          return this.attackLines.getLine(!this.orderedLines);
     }

     public Line getKilledLine() {
          return this.killedLines.getLine(!this.orderedLines);
     }

     public Line getKillLine() {
          return this.killLines.getLine(!this.orderedLines);
     }

     public Line getWorldLine() {
          return this.worldLines.getLine(!this.orderedLines);
     }

     public Line getNPCInteractLine() {
          return this.npcInteractLines.getLine(!this.orderedLines);
     }

     public void setRole(int i) {
          if (8 <= i) {
               i -= 2;
          }

          this.role = i % 8;
          if (this.role == 0) {
               this.npc.roleInterface = null;
          } else if (this.role == 3 && !(this.npc.roleInterface instanceof RoleBank)) {
               this.npc.roleInterface = new RoleBank(this.npc);
          } else if (this.role == 2 && !(this.npc.roleInterface instanceof RoleFollower)) {
               this.npc.roleInterface = new RoleFollower(this.npc);
          } else if (this.role == 5 && !(this.npc.roleInterface instanceof RolePostman)) {
               this.npc.roleInterface = new RolePostman(this.npc);
          } else if (this.role == 1 && !(this.npc.roleInterface instanceof RoleTrader)) {
               this.npc.roleInterface = new RoleTrader(this.npc);
          } else if (this.role == 4 && !(this.npc.roleInterface instanceof RoleTransporter)) {
               this.npc.roleInterface = new RoleTransporter(this.npc);
          } else if (this.role == 6 && !(this.npc.roleInterface instanceof RoleCompanion)) {
               this.npc.roleInterface = new RoleCompanion(this.npc);
          } else if (this.role == 7 && !(this.npc.roleInterface instanceof RoleDialog)) {
               this.npc.roleInterface = new RoleDialog(this.npc);
          }

     }

     public void setJob(int i) {
          if (this.npc.jobInterface != null && !this.npc.field_70170_p.field_72995_K) {
               this.npc.jobInterface.reset();
          }

          this.job = i % 12;
          if (this.job == 0) {
               this.npc.jobInterface = null;
          } else if (this.job == 1 && !(this.npc.jobInterface instanceof JobBard)) {
               this.npc.jobInterface = new JobBard(this.npc);
          } else if (this.job == 2 && !(this.npc.jobInterface instanceof JobHealer)) {
               this.npc.jobInterface = new JobHealer(this.npc);
          } else if (this.job == 3 && !(this.npc.jobInterface instanceof JobGuard)) {
               this.npc.jobInterface = new JobGuard(this.npc);
          } else if (this.job == 4 && !(this.npc.jobInterface instanceof JobItemGiver)) {
               this.npc.jobInterface = new JobItemGiver(this.npc);
          } else if (this.job == 5 && !(this.npc.jobInterface instanceof JobFollower)) {
               this.npc.jobInterface = new JobFollower(this.npc);
          } else if (this.job == 6 && !(this.npc.jobInterface instanceof JobSpawner)) {
               this.npc.jobInterface = new JobSpawner(this.npc);
          } else if (this.job == 7 && !(this.npc.jobInterface instanceof JobConversation)) {
               this.npc.jobInterface = new JobConversation(this.npc);
          } else if (this.job == 8 && !(this.npc.jobInterface instanceof JobChunkLoader)) {
               this.npc.jobInterface = new JobChunkLoader(this.npc);
          } else if (this.job == 9 && !(this.npc.jobInterface instanceof JobPuppet)) {
               this.npc.jobInterface = new JobPuppet(this.npc);
          } else if (this.job == 10 && !(this.npc.jobInterface instanceof JobBuilder)) {
               this.npc.jobInterface = new JobBuilder(this.npc);
          } else if (this.job == 11 && !(this.npc.jobInterface instanceof JobFarmer)) {
               this.npc.jobInterface = new JobFarmer(this.npc);
          }

     }

     public boolean hasWorldLines() {
          return !this.worldLines.isEmpty();
     }
}
