package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerItemGiverData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobItemGiver extends JobInterface {
     public int cooldownType = 0;
     public int givingMethod = 0;
     public int cooldown = 10;
     public NpcMiscInventory inventory = new NpcMiscInventory(9);
     public int itemGiverId = 0;
     public List lines = new ArrayList();
     private int ticks = 10;
     private List recentlyChecked = new ArrayList();
     private List toCheck;
     public Availability availability = new Availability();

     public JobItemGiver(EntityNPCInterface npc) {
          super(npc);
          this.lines.add("Have these items {player}");
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.func_74768_a("igCooldownType", this.cooldownType);
          nbttagcompound.func_74768_a("igGivingMethod", this.givingMethod);
          nbttagcompound.func_74768_a("igCooldown", this.cooldown);
          nbttagcompound.func_74768_a("ItemGiverId", this.itemGiverId);
          nbttagcompound.func_74782_a("igLines", NBTTags.nbtStringList(this.lines));
          nbttagcompound.func_74782_a("igJobInventory", this.inventory.getToNBT());
          nbttagcompound.func_74782_a("igAvailability", this.availability.writeToNBT(new NBTTagCompound()));
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.itemGiverId = nbttagcompound.func_74762_e("ItemGiverId");
          this.cooldownType = nbttagcompound.func_74762_e("igCooldownType");
          this.givingMethod = nbttagcompound.func_74762_e("igGivingMethod");
          this.cooldown = nbttagcompound.func_74762_e("igCooldown");
          this.lines = NBTTags.getStringList(nbttagcompound.func_150295_c("igLines", 10));
          this.inventory.setFromNBT(nbttagcompound.func_74775_l("igJobInventory"));
          if (this.itemGiverId == 0 && GlobalDataController.instance != null) {
               this.itemGiverId = GlobalDataController.instance.incrementItemGiverId();
          }

          this.availability.readFromNBT(nbttagcompound.func_74775_l("igAvailability"));
     }

     public NBTTagList newHashMapNBTList(HashMap lines) {
          NBTTagList nbttaglist = new NBTTagList();
          Iterator var4 = lines.keySet().iterator();

          while(var4.hasNext()) {
               String s = (String)var4.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74778_a("Line", s);
               nbttagcompound.func_74772_a("Time", (Long)lines.get(s));
               nbttaglist.func_74742_a(nbttagcompound);
          }

          return nbttaglist;
     }

     public HashMap getNBTLines(NBTTagList tagList) {
          HashMap map = new HashMap();

          for(int i = 0; i < tagList.func_74745_c(); ++i) {
               NBTTagCompound nbttagcompound = tagList.func_150305_b(i);
               String line = nbttagcompound.func_74779_i("Line");
               long time = nbttagcompound.func_74763_f("Time");
               map.put(line, time);
          }

          return map;
     }

     private boolean giveItems(EntityPlayer player) {
          PlayerItemGiverData data = PlayerData.get(player).itemgiverData;
          if (!this.canPlayerInteract(data)) {
               return false;
          } else {
               Vector items = new Vector();
               Vector toGive = new Vector();
               Iterator var5 = this.inventory.items.iterator();

               ItemStack is;
               while(var5.hasNext()) {
                    is = (ItemStack)var5.next();
                    if (!is.func_190926_b()) {
                         items.add(is.func_77946_l());
                    }
               }

               if (items.isEmpty()) {
                    return false;
               } else {
                    if (this.isAllGiver()) {
                         toGive = items;
                    } else if (this.isRemainingGiver()) {
                         var5 = items.iterator();

                         while(var5.hasNext()) {
                              is = (ItemStack)var5.next();
                              if (!this.playerHasItem(player, is.func_77973_b())) {
                                   toGive.add(is);
                              }
                         }
                    } else if (this.isRandomGiver()) {
                         toGive.add(((ItemStack)items.get(this.npc.field_70170_p.field_73012_v.nextInt(items.size()))).func_77946_l());
                    } else if (this.isGiverWhenNotOwnedAny()) {
                         boolean ownsItems = false;
                         Iterator var10 = items.iterator();

                         while(var10.hasNext()) {
                              ItemStack is = (ItemStack)var10.next();
                              if (this.playerHasItem(player, is.func_77973_b())) {
                                   ownsItems = true;
                                   break;
                              }
                         }

                         if (ownsItems) {
                              return false;
                         }

                         toGive = items;
                    } else if (this.isChainedGiver()) {
                         int itemIndex = data.getItemIndex(this);
                         int i = 0;

                         for(Iterator var13 = this.inventory.items.iterator(); var13.hasNext(); ++i) {
                              ItemStack item = (ItemStack)var13.next();
                              if (i == itemIndex) {
                                   toGive.add(item);
                                   break;
                              }
                         }
                    }

                    if (toGive.isEmpty()) {
                         return false;
                    } else if (this.givePlayerItems(player, toGive)) {
                         if (!this.lines.isEmpty()) {
                              this.npc.say(player, new Line((String)this.lines.get(this.npc.func_70681_au().nextInt(this.lines.size()))));
                         }

                         if (this.isDaily()) {
                              data.setTime(this, (long)this.getDay());
                         } else {
                              data.setTime(this, System.currentTimeMillis());
                         }

                         if (this.isChainedGiver()) {
                              data.setItemIndex(this, (data.getItemIndex(this) + 1) % this.inventory.items.size());
                         }

                         return true;
                    } else {
                         return false;
                    }
               }
          }
     }

     private int getDay() {
          return (int)(this.npc.field_70170_p.func_82737_E() / 24000L);
     }

     private boolean canPlayerInteract(PlayerItemGiverData data) {
          if (this.inventory.items.isEmpty()) {
               return false;
          } else if (this.isOnTimer()) {
               if (!data.hasInteractedBefore(this)) {
                    return true;
               } else {
                    return data.getTime(this) + (long)(this.cooldown * 1000) < System.currentTimeMillis();
               }
          } else if (this.isGiveOnce()) {
               return !data.hasInteractedBefore(this);
          } else if (this.isDaily()) {
               if (!data.hasInteractedBefore(this)) {
                    return true;
               } else {
                    return (long)this.getDay() > data.getTime(this);
               }
          } else {
               return false;
          }
     }

     private boolean givePlayerItems(EntityPlayer player, Vector toGive) {
          if (toGive.isEmpty()) {
               return false;
          } else if (this.freeInventorySlots(player) < toGive.size()) {
               return false;
          } else {
               Iterator var3 = toGive.iterator();

               while(var3.hasNext()) {
                    ItemStack is = (ItemStack)var3.next();
                    this.npc.givePlayerItem(player, is);
               }

               return true;
          }
     }

     private boolean playerHasItem(EntityPlayer player, Item item) {
          Iterator var3 = player.field_71071_by.field_70462_a.iterator();

          ItemStack is;
          do {
               if (!var3.hasNext()) {
                    var3 = player.field_71071_by.field_70460_b.iterator();

                    do {
                         if (!var3.hasNext()) {
                              return false;
                         }

                         is = (ItemStack)var3.next();
                    } while(is.func_190926_b() || is.func_77973_b() != item);

                    return true;
               }

               is = (ItemStack)var3.next();
          } while(is.func_190926_b() || is.func_77973_b() != item);

          return true;
     }

     private int freeInventorySlots(EntityPlayer player) {
          int i = 0;
          Iterator var3 = player.field_71071_by.field_70462_a.iterator();

          while(var3.hasNext()) {
               ItemStack is = (ItemStack)var3.next();
               if (NoppesUtilServer.IsItemStackNull(is)) {
                    ++i;
               }
          }

          return i;
     }

     private boolean isRandomGiver() {
          return this.givingMethod == 0;
     }

     private boolean isAllGiver() {
          return this.givingMethod == 1;
     }

     private boolean isRemainingGiver() {
          return this.givingMethod == 2;
     }

     private boolean isGiverWhenNotOwnedAny() {
          return this.givingMethod == 3;
     }

     private boolean isChainedGiver() {
          return this.givingMethod == 4;
     }

     public boolean isOnTimer() {
          return this.cooldownType == 0;
     }

     private boolean isGiveOnce() {
          return this.cooldownType == 1;
     }

     private boolean isDaily() {
          return this.cooldownType == 2;
     }

     public boolean aiShouldExecute() {
          if (this.npc.isAttacking()) {
               return false;
          } else {
               --this.ticks;
               if (this.ticks > 0) {
                    return false;
               } else {
                    this.ticks = 10;
                    this.toCheck = this.npc.field_70170_p.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b(3.0D, 3.0D, 3.0D));
                    this.toCheck.removeAll(this.recentlyChecked);
                    List listMax = this.npc.field_70170_p.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b(10.0D, 10.0D, 10.0D));
                    this.recentlyChecked.retainAll(listMax);
                    this.recentlyChecked.addAll(this.toCheck);
                    return this.toCheck.size() > 0;
               }
          }
     }

     public boolean aiContinueExecute() {
          return false;
     }

     public void aiStartExecuting() {
          Iterator var1 = this.toCheck.iterator();

          while(var1.hasNext()) {
               EntityPlayer player = (EntityPlayer)var1.next();
               if (this.npc.canSee(player) && this.availability.isAvailable(player)) {
                    this.recentlyChecked.add(player);
                    this.interact(player);
               }
          }

     }

     public void killed() {
     }

     private boolean interact(EntityPlayer player) {
          if (!this.giveItems(player)) {
               this.npc.say(player, this.npc.advanced.getInteractLine());
          }

          return true;
     }

     public void delete() {
     }
}
