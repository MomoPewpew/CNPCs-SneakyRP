package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

public class GuiNpcFollowerHire extends GuiContainerNPCInterface {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/followerhire.png");
     private EntityNPCInterface npc;
     private ContainerNPCFollowerHire container;
     private RoleFollower role;

     public GuiNpcFollowerHire(EntityNPCInterface npc, ContainerNPCFollowerHire container) {
          super(npc, container);
          this.container = container;
          this.npc = npc;
          this.role = (RoleFollower)npc.roleInterface;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(new GuiNpcButton(5, this.field_147003_i + 26, this.field_147009_r + 60, 50, 20, I18n.func_74838_a("follower.hire")));
     }

     public void func_146284_a(GuiButton guibutton) {
          super.func_146284_a(guibutton);
          if (guibutton.field_146127_k == 5) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerHire);
               this.close();
          }

     }

     protected void func_146979_b(int par1, int par2) {
     }

     protected void func_146976_a(float f, int i, int j) {
          GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.field_71446_o.func_110577_a(this.resource);
          int l = (this.field_146294_l - this.field_146999_f) / 2;
          int i1 = (this.field_146295_m - this.field_147000_g) / 2;
          this.func_73729_b(l, i1, 0, 0, this.field_146999_f, this.field_147000_g);
          int index = 0;

          for(int slot = 0; slot < this.role.inventory.items.size(); ++slot) {
               ItemStack itemstack = (ItemStack)this.role.inventory.items.get(slot);
               if (!NoppesUtilServer.IsItemStackNull(itemstack)) {
                    int days = 1;
                    if (this.role.rates.containsKey(slot)) {
                         days = (Integer)this.role.rates.get(slot);
                    }

                    int yOffset = index * 26;
                    int x = this.field_147003_i + 78;
                    int y = this.field_147009_r + yOffset + 10;
                    GlStateManager.func_179091_B();
                    RenderHelper.func_74520_c();
                    this.field_146296_j.func_180450_b(itemstack, x + 11, y);
                    this.field_146296_j.func_175030_a(this.field_146289_q, itemstack, x + 11, y);
                    RenderHelper.func_74518_a();
                    GlStateManager.func_179101_C();
                    String daysS = days + " " + (days == 1 ? I18n.func_74838_a("follower.day") : I18n.func_74838_a("follower.days"));
                    this.field_146289_q.func_78276_b(" = " + daysS, x + 27, y + 4, CustomNpcResourceListener.DefaultTextColor);
                    if (this.func_146978_c(x - this.field_147003_i + 11, y - this.field_147009_r, 16, 16, this.mouseX, this.mouseY)) {
                         this.func_146285_a(itemstack, this.mouseX, this.mouseY);
                    }

                    ++index;
               }
          }

     }

     public void save() {
     }
}
