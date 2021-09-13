package noppes.npcs.client.gui.player;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class GuiNPCTrader extends GuiContainerNPCInterface {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/trader.png");
     private final ResourceLocation slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");
     private RoleTrader role;
     private ContainerNPCTrader container;

     public GuiNPCTrader(EntityNPCInterface npc, ContainerNPCTrader container) {
          super(npc, container);
          this.container = container;
          this.role = (RoleTrader)npc.roleInterface;
          this.closeOnEsc = true;
          this.field_147000_g = 224;
          this.field_146999_f = 223;
          this.title = "role.trader";
     }

     protected void func_146976_a(float f, int i, int j) {
          this.func_146270_b(0);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.resource);
          this.drawTexturedModalRect(this.field_147003_i, this.field_147009_r, 0, 0, this.field_146999_f, this.field_147000_g);
          GlStateManager.enableRescaleNormal();
          this.field_146297_k.renderEngine.bindTexture(this.slot);

          for(int slot = 0; slot < 18; ++slot) {
               int x = this.field_147003_i + slot % 3 * 72 + 10;
               int y = this.field_147009_r + slot / 3 * 21 + 6;
               ItemStack item = (ItemStack)this.role.inventoryCurrency.items.get(slot);
               ItemStack item2 = (ItemStack)this.role.inventoryCurrency.items.get(slot + 18);
               if (NoppesUtilServer.IsItemStackNull(item)) {
                    item = item2;
                    item2 = ItemStack.EMPTY;
               }

               if (NoppesUtilPlayer.compareItems(item, item2, false, false)) {
                    item = item.copy();
                    item.func_190920_e(item.getCount() + item2.getCount());
                    item2 = ItemStack.EMPTY;
               }

               ItemStack sold = (ItemStack)this.role.inventorySold.items.get(slot);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.field_146297_k.renderEngine.bindTexture(this.slot);
               this.drawTexturedModalRect(x + 42, y, 0, 0, 18, 18);
               if (!NoppesUtilServer.IsItemStackNull(item) && !NoppesUtilServer.IsItemStackNull(sold)) {
                    RenderHelper.enableGUIStandardItemLighting();
                    if (!NoppesUtilServer.IsItemStackNull(item2)) {
                         this.field_146296_j.renderItemAndEffectIntoGUI(item2, x, y + 1);
                         this.field_146296_j.func_175030_a(this.field_146289_q, item2, x, y + 1);
                    }

                    this.field_146296_j.renderItemAndEffectIntoGUI(item, x + 18, y + 1);
                    this.field_146296_j.func_175030_a(this.field_146289_q, item, x + 18, y + 1);
                    RenderHelper.disableStandardItemLighting();
                    this.field_146289_q.func_78276_b("=", x + 36, y + 5, CustomNpcResourceListener.DefaultTextColor);
               }
          }

          GlStateManager.func_179101_C();
          super.func_146976_a(f, i, j);
     }

     protected void func_146979_b(int par1, int par2) {
          for(int slot = 0; slot < 18; ++slot) {
               int x = slot % 3 * 72 + 10;
               int y = slot / 3 * 21 + 6;
               ItemStack item = (ItemStack)this.role.inventoryCurrency.items.get(slot);
               ItemStack item2 = (ItemStack)this.role.inventoryCurrency.items.get(slot + 18);
               if (NoppesUtilServer.IsItemStackNull(item)) {
                    item = item2;
                    item2 = ItemStack.EMPTY;
               }

               if (NoppesUtilPlayer.compareItems(item, item2, this.role.ignoreDamage, this.role.ignoreNBT)) {
                    item = item.copy();
                    item.func_190920_e(item.getCount() + item2.getCount());
                    item2 = ItemStack.EMPTY;
               }

               ItemStack sold = (ItemStack)this.role.inventorySold.items.get(slot);
               if (!NoppesUtilServer.IsItemStackNull(sold)) {
                    if (this.func_146978_c(x + 43, y + 1, 16, 16, par1, par2)) {
                         String title;
                         if (!this.container.canBuy(item, item2, this.player)) {
                              GlStateManager.func_179109_b(0.0F, 0.0F, 300.0F);
                              if (!item.isEmpty() && !NoppesUtilPlayer.compareItems((EntityPlayer)this.player, item, this.role.ignoreDamage, this.role.ignoreNBT)) {
                                   this.func_73733_a(x + 17, y, x + 35, y + 18, 1886851088, 1886851088);
                              }

                              if (!item2.isEmpty() && !NoppesUtilPlayer.compareItems((EntityPlayer)this.player, item2, this.role.ignoreDamage, this.role.ignoreNBT)) {
                                   this.func_73733_a(x - 1, y, x + 17, y + 18, 1886851088, 1886851088);
                              }

                              title = I18n.func_74838_a("trader.insufficient");
                              this.field_146289_q.func_78276_b(title, (this.field_146999_f - this.field_146289_q.func_78256_a(title)) / 2, 131, 14483456);
                              GlStateManager.func_179109_b(0.0F, 0.0F, -300.0F);
                         } else {
                              title = I18n.func_74838_a("trader.sufficient");
                              this.field_146289_q.func_78276_b(title, (this.field_146999_f - this.field_146289_q.func_78256_a(title)) / 2, 131, 56576);
                         }
                    }

                    if (this.func_146978_c(x, y, 16, 16, par1, par2) && !NoppesUtilServer.IsItemStackNull(item2)) {
                         this.func_146285_a(item2, par1 - this.field_147003_i, par2 - this.field_147009_r);
                    }

                    if (this.func_146978_c(x + 18, y, 16, 16, par1, par2)) {
                         this.func_146285_a(item, par1 - this.field_147003_i, par2 - this.field_147009_r);
                    }
               }
          }

     }

     public void save() {
     }
}
