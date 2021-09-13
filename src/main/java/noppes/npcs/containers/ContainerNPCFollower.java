package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class ContainerNPCFollower extends ContainerNpcInterface {
     public InventoryNPC currencyMatrix;
     public RoleFollower role;

     public ContainerNPCFollower(EntityNPCInterface npc, EntityPlayer player) {
          super(player);
          this.role = (RoleFollower)npc.roleInterface;
          this.currencyMatrix = new InventoryNPC("currency", 1, this);
          this.func_75146_a(new SlotNpcMercenaryCurrency(this.role, this.currencyMatrix, 0, 26, 9));

          for(int j1 = 0; j1 < 9; ++j1) {
               this.func_75146_a(new Slot(player.field_71071_by, j1, 8 + j1 * 18, 142));
          }

     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int i) {
          return ItemStack.field_190927_a;
     }

     public void func_75134_a(EntityPlayer entityplayer) {
          super.func_75134_a(entityplayer);
          if (!entityplayer.field_70170_p.field_72995_K) {
               ItemStack itemstack = this.currencyMatrix.func_70304_b(0);
               if (!NoppesUtilServer.IsItemStackNull(itemstack) && !entityplayer.field_70170_p.field_72995_K) {
                    entityplayer.func_70099_a(itemstack, 0.0F);
               }
          }

     }
}
