package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class ContainerNPCTrader extends ContainerNpcInterface {
     public RoleTrader role;
     private EntityNPCInterface npc;

     public ContainerNPCTrader(EntityNPCInterface npc, EntityPlayer player) {
          super(player);
          this.npc = npc;
          this.role = (RoleTrader)npc.roleInterface;

          int j1;
          int l1;
          for(j1 = 0; j1 < 18; ++j1) {
               int x = 53;
               l1 = x + j1 % 3 * 72;
               int y = 7;
               int y = y + j1 / 3 * 21;
               ItemStack item = (ItemStack)this.role.inventoryCurrency.items.get(j1);
               ItemStack item2 = (ItemStack)this.role.inventoryCurrency.items.get(j1 + 18);
               if (item == null) {
                    item2 = null;
               }

               this.func_75146_a(new Slot(this.role.inventorySold, j1, l1, y));
          }

          for(j1 = 0; j1 < 3; ++j1) {
               for(l1 = 0; l1 < 9; ++l1) {
                    this.func_75146_a(new Slot(player.inventory, l1 + j1 * 9 + 9, 32 + l1 * 18, 140 + j1 * 18));
               }
          }

          for(j1 = 0; j1 < 9; ++j1) {
               this.func_75146_a(new Slot(player.inventory, j1, 32 + j1 * 18, 198));
          }

     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int i) {
          return ItemStack.field_190927_a;
     }

     public ItemStack func_184996_a(int i, int j, ClickType par3, EntityPlayer entityplayer) {
          if (par3 != ClickType.PICKUP) {
               return ItemStack.field_190927_a;
          } else if (i >= 0 && i < 18) {
               if (j == 1) {
                    return ItemStack.field_190927_a;
               } else {
                    Slot slot = (Slot)this.field_75151_b.get(i);
                    if (slot != null && slot.func_75211_c() != null) {
                         ItemStack item = slot.func_75211_c();
                         if (!this.canGivePlayer(item, entityplayer)) {
                              return ItemStack.field_190927_a;
                         } else {
                              ItemStack currency = this.role.inventoryCurrency.func_70301_a(i);
                              ItemStack currency2 = this.role.inventoryCurrency.func_70301_a(i + 18);
                              if (!this.canBuy(currency, currency2, entityplayer)) {
                                   RoleEvent.TradeFailedEvent event = new RoleEvent.TradeFailedEvent(entityplayer, this.npc.wrappedNPC, item, currency, currency2);
                                   EventHooks.onNPCRole(this.npc, event);
                                   return event.receiving == null ? ItemStack.field_190927_a : event.receiving.getMCItemStack();
                              } else {
                                   RoleEvent.TraderEvent event = new RoleEvent.TraderEvent(entityplayer, this.npc.wrappedNPC, item, currency, currency2);
                                   if (EventHooks.onNPCRole(this.npc, event)) {
                                        return ItemStack.field_190927_a;
                                   } else {
                                        if (event.currency1 != null) {
                                             currency = event.currency1.getMCItemStack();
                                        }

                                        if (event.currency2 != null) {
                                             currency2 = event.currency2.getMCItemStack();
                                        }

                                        if (!this.canBuy(currency, currency2, entityplayer)) {
                                             return ItemStack.field_190927_a;
                                        } else {
                                             NoppesUtilPlayer.consumeItem(entityplayer, currency, this.role.ignoreDamage, this.role.ignoreNBT);
                                             NoppesUtilPlayer.consumeItem(entityplayer, currency2, this.role.ignoreDamage, this.role.ignoreNBT);
                                             ItemStack soldItem = ItemStack.field_190927_a;
                                             if (event.sold != null) {
                                                  soldItem = event.sold.getMCItemStack();
                                                  this.givePlayer(soldItem.func_77946_l(), entityplayer);
                                             }

                                             return soldItem;
                                        }
                                   }
                              }
                         }
                    } else {
                         return ItemStack.field_190927_a;
                    }
               }
          } else {
               return super.func_184996_a(i, j, par3, entityplayer);
          }
     }

     public boolean canBuy(ItemStack currency, ItemStack currency2, EntityPlayer player) {
          if (NoppesUtilServer.IsItemStackNull(currency) && NoppesUtilServer.IsItemStackNull(currency2)) {
               return true;
          } else {
               if (NoppesUtilServer.IsItemStackNull(currency)) {
                    currency = currency2;
                    currency2 = ItemStack.field_190927_a;
               }

               if (NoppesUtilPlayer.compareItems(currency, currency2, this.role.ignoreDamage, this.role.ignoreNBT)) {
                    currency = currency.func_77946_l();
                    currency.func_190917_f(currency2.func_190916_E());
                    currency2 = ItemStack.field_190927_a;
               }

               if (NoppesUtilServer.IsItemStackNull(currency2)) {
                    return NoppesUtilPlayer.compareItems(player, currency, this.role.ignoreDamage, this.role.ignoreNBT);
               } else {
                    return NoppesUtilPlayer.compareItems(player, currency, this.role.ignoreDamage, this.role.ignoreNBT) && NoppesUtilPlayer.compareItems(player, currency2, this.role.ignoreDamage, this.role.ignoreNBT);
               }
          }
     }

     private boolean canGivePlayer(ItemStack item, EntityPlayer entityplayer) {
          ItemStack itemstack3 = entityplayer.inventory.func_70445_o();
          if (NoppesUtilServer.IsItemStackNull(itemstack3)) {
               return true;
          } else {
               if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
                    int k1 = item.func_190916_E();
                    if (k1 > 0 && k1 + itemstack3.func_190916_E() <= itemstack3.func_77976_d()) {
                         return true;
                    }
               }

               return false;
          }
     }

     private void givePlayer(ItemStack item, EntityPlayer entityplayer) {
          ItemStack itemstack3 = entityplayer.inventory.func_70445_o();
          if (NoppesUtilServer.IsItemStackNull(itemstack3)) {
               entityplayer.inventory.func_70437_b(item);
          } else if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
               int k1 = item.func_190916_E();
               if (k1 > 0 && k1 + itemstack3.func_190916_E() <= itemstack3.func_77976_d()) {
                    itemstack3.func_190917_f(k1);
               }
          }

     }
}
