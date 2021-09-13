package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerBankData;

public class ContainerNPCBankInterface extends ContainerNpcInterface {
     public InventoryNPC currencyMatrix;
     private EntityPlayer player;
     public SlotNpcBankCurrency currency;
     public int slot = 0;
     public int bankid;
     private PlayerBankData data;

     public ContainerNPCBankInterface(EntityPlayer player, int slot, int bankid) {
          super(player);
          this.bankid = bankid;
          this.slot = slot;
          this.player = player;
          this.currencyMatrix = new InventoryNPC("currency", 1, this);
          if (!this.isAvailable() || this.canBeUpgraded()) {
               this.currency = new SlotNpcBankCurrency(this, this.currencyMatrix, 0, 80, 29);
               this.func_75146_a(this.currency);
          }

          NpcMiscInventory items = new NpcMiscInventory(54);
          if (!player.field_70170_p.field_72995_K) {
               this.data = PlayerDataController.instance.getBankData(player, bankid);
               items = (NpcMiscInventory)this.data.getBankOrDefault(bankid).itemSlots.get(slot);
          }

          int xOffset = this.xOffset();

          int l;
          int j1;
          for(l = 0; l < this.getRowNumber(); ++l) {
               for(j1 = 0; j1 < 9; ++j1) {
                    int id = j1 + l * 9;
                    this.func_75146_a(new Slot(items, id, 8 + j1 * 18, 17 + xOffset + l * 18));
               }
          }

          if (this.isUpgraded()) {
               xOffset += 54;
          }

          for(l = 0; l < 3; ++l) {
               for(j1 = 0; j1 < 9; ++j1) {
                    this.func_75146_a(new Slot(player.field_71071_by, j1 + l * 9 + 9, 8 + j1 * 18, 86 + xOffset + l * 18));
               }
          }

          for(l = 0; l < 9; ++l) {
               this.func_75146_a(new Slot(player.field_71071_by, l, 8 + l * 18, 144 + xOffset));
          }

     }

     public synchronized void setCurrency(ItemStack item) {
          this.currency.item = item;
     }

     public int getRowNumber() {
          return 0;
     }

     public int xOffset() {
          return 0;
     }

     public void func_75130_a(IInventory inv) {
     }

     public boolean isAvailable() {
          return false;
     }

     public boolean isUpgraded() {
          return false;
     }

     public boolean canBeUpgraded() {
          return false;
     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int i) {
          return ItemStack.field_190927_a;
     }

     public void func_75134_a(EntityPlayer entityplayer) {
          super.func_75134_a(entityplayer);
          if (!entityplayer.field_70170_p.field_72995_K) {
               ItemStack var3 = this.currencyMatrix.func_70301_a(0);
               this.currencyMatrix.func_70299_a(0, ItemStack.field_190927_a);
               if (!NoppesUtilServer.IsItemStackNull(var3)) {
                    entityplayer.func_71019_a(var3, false);
               }
          }

     }
}
