package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.util.ValueUtil;

public class QuestItem extends QuestInterface {
     public NpcMiscInventory items = new NpcMiscInventory(3);
     public boolean leaveItems = false;
     public boolean ignoreDamage = false;
     public boolean ignoreNBT = false;

     public void readEntityFromNBT(NBTTagCompound compound) {
          this.items.setFromNBT(compound.func_74775_l("Items"));
          this.leaveItems = compound.func_74767_n("LeaveItems");
          this.ignoreDamage = compound.func_74767_n("IgnoreDamage");
          this.ignoreNBT = compound.func_74767_n("IgnoreNBT");
     }

     public void writeEntityToNBT(NBTTagCompound compound) {
          compound.func_74782_a("Items", this.items.getToNBT());
          compound.func_74757_a("LeaveItems", this.leaveItems);
          compound.func_74757_a("IgnoreDamage", this.ignoreDamage);
          compound.func_74757_a("IgnoreNBT", this.ignoreNBT);
     }

     public boolean isCompleted(EntityPlayer player) {
          List questItems = NoppesUtilPlayer.countStacks(this.items, this.ignoreDamage, this.ignoreNBT);
          Iterator var3 = questItems.iterator();

          ItemStack reqItem;
          do {
               if (!var3.hasNext()) {
                    return true;
               }

               reqItem = (ItemStack)var3.next();
          } while(NoppesUtilPlayer.compareItems(player, reqItem, this.ignoreDamage, this.ignoreNBT));

          return false;
     }

     public Map getProgressSet(EntityPlayer player) {
          HashMap map = new HashMap();
          List questItems = NoppesUtilPlayer.countStacks(this.items, this.ignoreDamage, this.ignoreNBT);
          Iterator var4 = questItems.iterator();

          ItemStack item;
          while(var4.hasNext()) {
               item = (ItemStack)var4.next();
               if (!NoppesUtilServer.IsItemStackNull(item)) {
                    map.put(item, 0);
               }
          }

          for(int i = 0; i < player.inventory.func_70302_i_(); ++i) {
               item = player.inventory.func_70301_a(i);
               if (!NoppesUtilServer.IsItemStackNull(item)) {
                    Iterator var6 = map.entrySet().iterator();

                    while(var6.hasNext()) {
                         Entry questItem = (Entry)var6.next();
                         if (NoppesUtilPlayer.compareItems((ItemStack)questItem.getKey(), item, this.ignoreDamage, this.ignoreNBT)) {
                              map.put(questItem.getKey(), (Integer)questItem.getValue() + item.func_190916_E());
                         }
                    }
               }
          }

          return map;
     }

     public void handleComplete(EntityPlayer player) {
          if (!this.leaveItems) {
               Iterator var2 = this.items.items.iterator();

               while(true) {
                    ItemStack questitem;
                    do {
                         if (!var2.hasNext()) {
                              return;
                         }

                         questitem = (ItemStack)var2.next();
                    } while(questitem.func_190926_b());

                    int stacksize = questitem.func_190916_E();

                    for(int i = 0; i < player.inventory.func_70302_i_(); ++i) {
                         ItemStack item = player.inventory.func_70301_a(i);
                         if (!NoppesUtilServer.IsItemStackNull(item) && NoppesUtilPlayer.compareItems(item, questitem, this.ignoreDamage, this.ignoreNBT)) {
                              int size = item.func_190916_E();
                              if (stacksize - size >= 0) {
                                   player.inventory.func_70299_a(i, ItemStack.field_190927_a);
                                   item.splitStack(size);
                              } else {
                                   item.splitStack(stacksize);
                              }

                              stacksize -= size;
                              if (stacksize <= 0) {
                                   break;
                              }
                         }
                    }
               }
          }
     }

     public IQuestObjective[] getObjectives(EntityPlayer player) {
          List list = new ArrayList();
          List questItems = NoppesUtilPlayer.countStacks(this.items, this.ignoreDamage, this.ignoreNBT);
          Iterator var4 = questItems.iterator();

          while(var4.hasNext()) {
               ItemStack stack = (ItemStack)var4.next();
               if (!stack.func_190926_b()) {
                    list.add(new QuestItem.QuestItemObjective(player, stack));
               }
          }

          return (IQuestObjective[])list.toArray(new IQuestObjective[list.size()]);
     }

     class QuestItemObjective implements IQuestObjective {
          private final EntityPlayer player;
          private final ItemStack questItem;

          public QuestItemObjective(EntityPlayer player, ItemStack item) {
               this.player = player;
               this.questItem = item;
          }

          public int getProgress() {
               int count = 0;

               for(int i = 0; i < this.player.inventory.func_70302_i_(); ++i) {
                    ItemStack item = this.player.inventory.func_70301_a(i);
                    if (!NoppesUtilServer.IsItemStackNull(item) && NoppesUtilPlayer.compareItems(this.questItem, item, QuestItem.this.ignoreDamage, QuestItem.this.ignoreNBT)) {
                         count += item.func_190916_E();
                    }
               }

               return ValueUtil.CorrectInt(count, 0, this.questItem.func_190916_E());
          }

          public void setProgress(int progress) {
               throw new CustomNPCsException("Cant set the progress of ItemQuests", new Object[0]);
          }

          public int getMaxProgress() {
               return this.questItem.func_190916_E();
          }

          public boolean isCompleted() {
               return NoppesUtilPlayer.compareItems(this.player, this.questItem, QuestItem.this.ignoreDamage, QuestItem.this.ignoreNBT);
          }

          public String getText() {
               return this.questItem.func_82833_r() + ": " + this.getProgress() + "/" + this.getMaxProgress();
          }
     }
}
