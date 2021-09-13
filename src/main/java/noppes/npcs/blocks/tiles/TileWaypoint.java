package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.quests.QuestLocation;

public class TileWaypoint extends TileNpcEntity implements ITickable {
     public String name = "";
     private int ticks = 10;
     private List recentlyChecked = new ArrayList();
     private List toCheck;
     public int range = 10;

     public void func_73660_a() {
          if (!this.field_145850_b.isRemote && !this.name.isEmpty()) {
               --this.ticks;
               if (this.ticks <= 0) {
                    this.ticks = 10;
                    this.toCheck = this.getPlayerList(this.range, this.range, this.range);
                    this.toCheck.removeAll(this.recentlyChecked);
                    List listMax = this.getPlayerList(this.range + 10, this.range + 10, this.range + 10);
                    this.recentlyChecked.retainAll(listMax);
                    this.recentlyChecked.addAll(this.toCheck);
                    if (!this.toCheck.isEmpty()) {
                         Iterator var2 = this.toCheck.iterator();

                         while(var2.hasNext()) {
                              EntityPlayer player = (EntityPlayer)var2.next();
                              PlayerData pdata = PlayerData.get(player);
                              PlayerQuestData playerdata = pdata.questData;
                              Iterator var6 = playerdata.activeQuests.values().iterator();

                              while(var6.hasNext()) {
                                   QuestData data = (QuestData)var6.next();
                                   if (data.quest.type == 3) {
                                        QuestLocation quest = (QuestLocation)data.quest.questInterface;
                                        if (quest.setFound(data, this.name)) {
                                             player.sendMessage(new TextComponentTranslation(this.name + " " + I18n.translateToLocal("quest.found"), new Object[0]));
                                             playerdata.checkQuestCompletion(player, 3);
                                             pdata.updateClient = true;
                                        }
                                   }
                              }
                         }

                    }
               }
          }
     }

     private List getPlayerList(int x, int y, int z) {
          return this.field_145850_b.getEntitiesWithinAABB(EntityPlayer.class, (new AxisAlignedBB(this.field_174879_c, this.field_174879_c.add(1, 1, 1))).expand((double)x, (double)y, (double)z));
     }

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          this.name = compound.getString("LocationName");
          this.range = compound.getInteger("LocationRange");
          if (this.range < 2) {
               this.range = 2;
          }

     }

     public NBTTagCompound func_189515_b(NBTTagCompound compound) {
          if (!this.name.isEmpty()) {
               compound.setString("LocationName", this.name);
          }

          compound.setInteger("LocationRange", this.range);
          return super.func_189515_b(compound);
     }
}
