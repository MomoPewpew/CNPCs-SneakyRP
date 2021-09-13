package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.data.Quest;

public class ContainerNpcQuestReward extends Container {
     public ContainerNpcQuestReward(EntityPlayer player) {
          Quest quest = NoppesUtilServer.getEditingQuest(player);

          int j1;
          int l1;
          for(j1 = 0; j1 < 3; ++j1) {
               for(l1 = 0; l1 < 3; ++l1) {
                    this.func_75146_a(new Slot(quest.rewardItems, l1 + j1 * 3, 105 + l1 * 18, 17 + j1 * 18));
               }
          }

          for(j1 = 0; j1 < 3; ++j1) {
               for(l1 = 0; l1 < 9; ++l1) {
                    this.func_75146_a(new Slot(player.field_71071_by, l1 + j1 * 9 + 9, 8 + l1 * 18, 84 + j1 * 18));
               }
          }

          for(j1 = 0; j1 < 9; ++j1) {
               this.func_75146_a(new Slot(player.field_71071_by, j1, 8 + j1 * 18, 142));
          }

     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int i) {
          return ItemStack.field_190927_a;
     }

     public boolean func_75145_c(EntityPlayer entityplayer) {
          return true;
     }
}
