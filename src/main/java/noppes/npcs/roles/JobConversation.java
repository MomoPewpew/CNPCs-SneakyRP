package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;

public class JobConversation extends JobInterface {
     public Availability availability = new Availability();
     private ArrayList names = new ArrayList();
     private HashMap npcs = new HashMap();
     public HashMap lines = new HashMap();
     public int quest = -1;
     public String questTitle = "";
     public int generalDelay = 400;
     public int ticks = 100;
     public int range = 20;
     private JobConversation.ConversationLine nextLine;
     private boolean hasStarted = false;
     private int startedTicks = 20;
     public int mode = 0;

     public JobConversation(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.func_74782_a("ConversationAvailability", this.availability.writeToNBT(new NBTTagCompound()));
          compound.func_74768_a("ConversationQuest", this.quest);
          compound.func_74768_a("ConversationDelay", this.generalDelay);
          compound.func_74768_a("ConversationRange", this.range);
          compound.func_74768_a("ConversationMode", this.mode);
          NBTTagList nbttaglist = new NBTTagList();
          Iterator var3 = this.lines.keySet().iterator();

          while(var3.hasNext()) {
               int slot = (Integer)var3.next();
               JobConversation.ConversationLine line = (JobConversation.ConversationLine)this.lines.get(slot);
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74768_a("Slot", slot);
               line.writeEntityToNBT(nbttagcompound);
               nbttaglist.func_74742_a(nbttagcompound);
          }

          compound.func_74782_a("ConversationLines", nbttaglist);
          if (this.hasQuest()) {
               compound.func_74778_a("ConversationQuestTitle", this.getQuest().title);
          }

          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.names.clear();
          this.availability.readFromNBT(compound.func_74775_l("ConversationAvailability"));
          this.quest = compound.func_74762_e("ConversationQuest");
          this.generalDelay = compound.func_74762_e("ConversationDelay");
          this.questTitle = compound.func_74779_i("ConversationQuestTitle");
          this.range = compound.func_74762_e("ConversationRange");
          this.mode = compound.func_74762_e("ConversationMode");
          NBTTagList nbttaglist = compound.func_150295_c("ConversationLines", 10);
          HashMap map = new HashMap();

          for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
               NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
               JobConversation.ConversationLine line = new JobConversation.ConversationLine();
               line.readEntityFromNBT(nbttagcompound);
               if (!line.npc.isEmpty() && !this.names.contains(line.npc.toLowerCase())) {
                    this.names.add(line.npc.toLowerCase());
               }

               map.put(nbttagcompound.func_74762_e("Slot"), line);
          }

          this.lines = map;
          this.ticks = this.generalDelay;
     }

     public boolean hasQuest() {
          return this.getQuest() != null;
     }

     public Quest getQuest() {
          return this.npc.isRemote() ? null : (Quest)QuestController.instance.quests.get(this.quest);
     }

     public void aiUpdateTask() {
          --this.ticks;
          if (this.ticks <= 0 && this.nextLine != null) {
               this.say(this.nextLine);
               boolean seenNext = false;
               JobConversation.ConversationLine compare = this.nextLine;
               this.nextLine = null;
               Iterator var3 = this.lines.values().iterator();

               while(var3.hasNext()) {
                    JobConversation.ConversationLine line = (JobConversation.ConversationLine)var3.next();
                    if (!line.isEmpty()) {
                         if (seenNext) {
                              this.nextLine = line;
                              break;
                         }

                         if (line == compare) {
                              seenNext = true;
                         }
                    }
               }

               if (this.nextLine != null) {
                    this.ticks = this.nextLine.delay;
               } else if (this.hasQuest()) {
                    List inRange = this.npc.field_70170_p.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b((double)this.range, (double)this.range, (double)this.range));
                    Iterator var7 = inRange.iterator();

                    while(var7.hasNext()) {
                         EntityPlayer player = (EntityPlayer)var7.next();
                         if (this.availability.isAvailable(player)) {
                              PlayerQuestController.addActiveQuest(this.getQuest(), player);
                         }
                    }
               }

          }
     }

     public boolean aiShouldExecute() {
          if (!this.lines.isEmpty() && !this.npc.isKilled() && !this.npc.isAttacking() && this.shouldRun()) {
               if (!this.hasStarted && this.mode == 1) {
                    if (this.startedTicks-- > 0) {
                         return false;
                    }

                    this.startedTicks = 10;
                    if (this.npc.field_70170_p.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b((double)this.range, (double)this.range, (double)this.range)).isEmpty()) {
                         return false;
                    }
               }

               Iterator var1 = this.lines.values().iterator();

               while(var1.hasNext()) {
                    JobConversation.ConversationLine line = (JobConversation.ConversationLine)var1.next();
                    if (line != null && !line.isEmpty()) {
                         this.nextLine = line;
                         break;
                    }
               }

               return this.nextLine != null;
          } else {
               return false;
          }
     }

     private boolean shouldRun() {
          --this.ticks;
          if (this.ticks > 0) {
               return false;
          } else {
               this.npcs.clear();
               List list = this.npc.field_70170_p.func_72872_a(EntityNPCInterface.class, this.npc.func_174813_aQ().func_72314_b(10.0D, 10.0D, 10.0D));
               Iterator var2 = list.iterator();

               while(var2.hasNext()) {
                    EntityNPCInterface npc = (EntityNPCInterface)var2.next();
                    if (!npc.isKilled() && !npc.isAttacking() && this.names.contains(npc.func_70005_c_().toLowerCase())) {
                         this.npcs.put(npc.func_70005_c_().toLowerCase(), npc);
                    }
               }

               boolean bo = this.names.size() == this.npcs.size();
               if (!bo) {
                    this.ticks = 20;
               }

               return bo;
          }
     }

     public boolean aiContinueExecute() {
          Iterator var1 = this.npcs.values().iterator();

          EntityNPCInterface npc;
          do {
               if (!var1.hasNext()) {
                    return this.nextLine != null;
               }

               npc = (EntityNPCInterface)var1.next();
          } while(!npc.isKilled() && !npc.isAttacking());

          return false;
     }

     public void resetTask() {
          this.nextLine = null;
          this.ticks = this.generalDelay;
          this.hasStarted = false;
     }

     public void aiStartExecuting() {
          this.startedTicks = 20;
          this.hasStarted = true;
     }

     private void say(JobConversation.ConversationLine line) {
          List inRange = this.npc.field_70170_p.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b((double)this.range, (double)this.range, (double)this.range));
          EntityNPCInterface npc = (EntityNPCInterface)this.npcs.get(line.npc.toLowerCase());
          if (npc != null) {
               Iterator var4 = inRange.iterator();

               while(var4.hasNext()) {
                    EntityPlayer player = (EntityPlayer)var4.next();
                    if (this.availability.isAvailable(player)) {
                         npc.say(player, line);
                    }
               }

          }
     }

     public void reset() {
          this.hasStarted = false;
          this.resetTask();
          this.ticks = 60;
     }

     public void killed() {
          this.reset();
     }

     public JobConversation.ConversationLine getLine(int slot) {
          if (this.lines.containsKey(slot)) {
               return (JobConversation.ConversationLine)this.lines.get(slot);
          } else {
               JobConversation.ConversationLine line = new JobConversation.ConversationLine();
               this.lines.put(slot, line);
               return line;
          }
     }

     public class ConversationLine extends Line {
          public String npc = "";
          public int delay = 40;

          public void writeEntityToNBT(NBTTagCompound compound) {
               compound.func_74778_a("Line", this.text);
               compound.func_74778_a("Npc", this.npc);
               compound.func_74778_a("Sound", this.sound);
               compound.func_74768_a("Delay", this.delay);
          }

          public void readEntityFromNBT(NBTTagCompound compound) {
               this.text = compound.func_74779_i("Line");
               this.npc = compound.func_74779_i("Npc");
               this.sound = compound.func_74779_i("Sound");
               this.delay = compound.func_74762_e("Delay");
          }

          public boolean isEmpty() {
               return this.npc.isEmpty() || this.text.isEmpty();
          }
     }
}
