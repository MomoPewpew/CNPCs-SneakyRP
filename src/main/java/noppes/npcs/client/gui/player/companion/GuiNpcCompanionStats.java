package noppes.npcs.client.gui.player.companion;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiMenuTopIconButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class GuiNpcCompanionStats extends GuiNPCInterface implements IGuiData {
     private RoleCompanion role;
     private boolean isEating = false;

     public GuiNpcCompanionStats(EntityNPCInterface npc) {
          super(npc);
          this.role = (RoleCompanion)npc.roleInterface;
          this.closeOnEsc = true;
          this.setBackground("companion.png");
          this.xSize = 171;
          this.ySize = 166;
          NoppesUtilPlayer.sendData(EnumPlayerPacket.RoleGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int y = this.guiTop + 10;
          this.addLabel(new GuiNpcLabel(0, NoppesStringUtils.translate("gui.name", ": ", this.npc.display.getName()), this.guiLeft + 4, y));
          String var10004 = NoppesStringUtils.translate("companion.owner", ": ", this.role.ownerName);
          int var10005 = this.guiLeft + 4;
          y += 12;
          this.addLabel(new GuiNpcLabel(1, var10004, var10005, y));
          var10004 = NoppesStringUtils.translate("companion.age", ": ", this.role.ticksActive / 18000L + " (", this.role.stage.name, ")");
          var10005 = this.guiLeft + 4;
          y += 12;
          this.addLabel(new GuiNpcLabel(2, var10004, var10005, y));
          var10004 = NoppesStringUtils.translate("companion.strength", ": ", this.npc.stats.melee.getStrength());
          var10005 = this.guiLeft + 4;
          y += 12;
          this.addLabel(new GuiNpcLabel(3, var10004, var10005, y));
          var10004 = NoppesStringUtils.translate("companion.level", ": ", this.role.getTotalLevel());
          var10005 = this.guiLeft + 4;
          y += 12;
          this.addLabel(new GuiNpcLabel(4, var10004, var10005, y));
          var10004 = NoppesStringUtils.translate("job.name", ": ", "gui.none");
          var10005 = this.guiLeft + 4;
          y += 12;
          this.addLabel(new GuiNpcLabel(5, var10004, var10005, y));
          addTopMenu(this.role, this, 1);
     }

     public static void addTopMenu(RoleCompanion role, GuiScreen screen, int active) {
          GuiMenuTopIconButton button;
          if (screen instanceof GuiNPCInterface) {
               GuiNPCInterface gui = (GuiNPCInterface)screen;
               gui.addTopButton(button = new GuiMenuTopIconButton(1, gui.guiLeft + 4, gui.guiTop - 27, "menu.stats", new ItemStack(Items.field_151122_aG)));
               gui.addTopButton(button = new GuiMenuTopIconButton(2, button, "companion.talent", new ItemStack(Items.field_151156_bN)));
               if (role.hasInv()) {
                    gui.addTopButton(button = new GuiMenuTopIconButton(3, button, "inv.inventory", new ItemStack(Blocks.field_150486_ae)));
               }

               if (role.job != EnumCompanionJobs.NONE) {
                    gui.addTopButton(new GuiMenuTopIconButton(4, button, "job.name", new ItemStack(Items.field_151172_bF)));
               }

               gui.getTopButton(active).active = true;
          }

          if (screen instanceof GuiContainerNPCInterface) {
               GuiContainerNPCInterface gui = (GuiContainerNPCInterface)screen;
               gui.addTopButton(button = new GuiMenuTopIconButton(1, gui.field_147003_i + 4, gui.field_147009_r - 27, "menu.stats", new ItemStack(Items.field_151122_aG)));
               gui.addTopButton(button = new GuiMenuTopIconButton(2, button, "companion.talent", new ItemStack(Items.field_151156_bN)));
               if (role.hasInv()) {
                    gui.addTopButton(button = new GuiMenuTopIconButton(3, button, "inv.inventory", new ItemStack(Blocks.field_150486_ae)));
               }

               if (role.job != EnumCompanionJobs.NONE) {
                    gui.addTopButton(new GuiMenuTopIconButton(4, button, "job.name", new ItemStack(Items.field_151172_bF)));
               }

               gui.getTopButton(active).active = true;
          }

     }

     public void func_146284_a(GuiButton guibutton) {
          super.func_146284_a(guibutton);
          int id = guibutton.id;
          if (id == 2) {
               CustomNpcs.proxy.openGui(this.npc, EnumGuiType.CompanionTalent);
          }

          if (id == 3) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.CompanionOpenInv);
          }

     }

     public void func_73863_a(int i, int j, float f) {
          super.func_73863_a(i, j, f);
          if (this.isEating && !this.role.isEating()) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.RoleGet);
          }

          this.isEating = this.role.isEating();
          super.drawNpc(34, 150);
          int y = this.drawHealth(this.guiTop + 88);
     }

     private int drawHealth(int y) {
          this.field_146297_k.func_110434_K().bindTexture(field_110324_m);
          int max = this.role.getTotalArmorValue();
          int k;
          if (this.role.talents.containsKey(EnumCompanionTalent.ARMOR) || max > 0) {
               for(k = 0; k < 10; ++k) {
                    int x = this.guiLeft + 66 + k * 10;
                    if (k * 2 + 1 < max) {
                         this.drawTexturedModalRect(x, y, 34, 9, 9, 9);
                    }

                    if (k * 2 + 1 == max) {
                         this.drawTexturedModalRect(x, y, 25, 9, 9, 9);
                    }

                    if (k * 2 + 1 > max) {
                         this.drawTexturedModalRect(x, y, 16, 9, 9, 9);
                    }
               }

               y += 10;
          }

          max = MathHelper.func_76123_f(this.npc.func_110138_aP());
          k = (int)this.npc.func_110143_aJ();
          float scale = 1.0F;
          if (max > 40) {
               scale = (float)max / 40.0F;
               k = (int)((float)k / scale);
               max = 40;
          }

          int i;
          int x;
          for(i = 0; i < max; ++i) {
               x = this.guiLeft + 66 + i % 20 * 5;
               int offset = i / 20 * 10;
               this.drawTexturedModalRect(x, y + offset, 52 + i % 2 * 5, 9, i % 2 == 1 ? 4 : 5, 9);
               if (k > i) {
                    this.drawTexturedModalRect(x, y + offset, 52 + i % 2 * 5, 0, i % 2 == 1 ? 4 : 5, 9);
               }
          }

          k = this.role.foodstats.getFoodLevel();
          y += 10;
          if (max > 20) {
               y += 10;
          }

          for(i = 0; i < 20; ++i) {
               x = this.guiLeft + 66 + i % 20 * 5;
               this.drawTexturedModalRect(x, y, 16 + i % 2 * 5, 27, i % 2 == 1 ? 4 : 5, 9);
               if (k > i) {
                    this.drawTexturedModalRect(x, y, 52 + i % 2 * 5, 27, i % 2 == 1 ? 4 : 5, 9);
               }
          }

          return y;
     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          this.role.readFromNBT(compound);
     }
}
