package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.advanced.GuiNPCAdvancedLinkedNpc;
import noppes.npcs.client.gui.advanced.GuiNPCDialogNpcOptions;
import noppes.npcs.client.gui.advanced.GuiNPCFactionSetup;
import noppes.npcs.client.gui.advanced.GuiNPCLinesMenu;
import noppes.npcs.client.gui.advanced.GuiNPCMarks;
import noppes.npcs.client.gui.advanced.GuiNPCNightSetup;
import noppes.npcs.client.gui.advanced.GuiNPCScenes;
import noppes.npcs.client.gui.advanced.GuiNPCSoundsMenu;
import noppes.npcs.client.gui.roles.GuiJobFarmer;
import noppes.npcs.client.gui.roles.GuiNpcBard;
import noppes.npcs.client.gui.roles.GuiNpcCompanion;
import noppes.npcs.client.gui.roles.GuiNpcConversation;
import noppes.npcs.client.gui.roles.GuiNpcFollowerJob;
import noppes.npcs.client.gui.roles.GuiNpcGuard;
import noppes.npcs.client.gui.roles.GuiNpcHealer;
import noppes.npcs.client.gui.roles.GuiNpcPuppet;
import noppes.npcs.client.gui.roles.GuiNpcSpawner;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;
import noppes.npcs.client.gui.roles.GuiRoleDialog;
import noppes.npcs.client.gui.util.GuiButtonBiDirectional;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcAdvanced extends GuiNPCInterface2 implements IGuiData {
     private boolean hasChanges = false;

     public GuiNpcAdvanced(EntityNPCInterface npc) {
          super(npc, 4);
          Client.sendData(EnumPacketServer.MainmenuAdvancedGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int y = this.guiTop + 8;
          this.addButton(new GuiNpcButton(3, this.guiLeft + 85 + 160, y, 52, 20, "selectServer.edit"));
          this.addButton(new GuiButtonBiDirectional(8, this.guiLeft + 85, y, 155, 20, new String[]{"role.none", "role.trader", "role.follower", "role.bank", "role.transporter", "role.mailman", NoppesStringUtils.translate("role.companion", "(WIP)"), "dialog.dialog"}, this.npc.advanced.role));
          this.getButton(3).setEnabled(this.npc.advanced.role != 0 && this.npc.advanced.role != 5);
          int var10004 = this.guiLeft + 85 + 160;
          y += 22;
          this.addButton(new GuiNpcButton(4, var10004, y, 52, 20, "selectServer.edit"));
          this.addButton(new GuiButtonBiDirectional(5, this.guiLeft + 85, y, 155, 20, new String[]{"job.none", "job.bard", "job.healer", "job.guard", "job.itemgiver", "role.follower", "job.spawner", "job.conversation", "job.chunkloader", "job.puppet", "job.builder", "job.farmer"}, this.npc.advanced.job));
          this.getButton(4).setEnabled(this.npc.advanced.job != 0 && this.npc.advanced.job != 8 && this.npc.advanced.job != 10);
          var10004 = this.guiLeft + 15;
          y += 22;
          this.addButton(new GuiNpcButton(7, var10004, y, 190, 20, "advanced.lines"));
          this.addButton(new GuiNpcButton(9, this.guiLeft + 208, y, 190, 20, "menu.factions"));
          var10004 = this.guiLeft + 15;
          y += 22;
          this.addButton(new GuiNpcButton(10, var10004, y, 190, 20, "dialog.dialogs"));
          this.addButton(new GuiNpcButton(11, this.guiLeft + 208, y, 190, 20, "advanced.sounds"));
          var10004 = this.guiLeft + 15;
          y += 22;
          this.addButton(new GuiNpcButton(12, var10004, y, 190, 20, "advanced.night"));
          this.addButton(new GuiNpcButton(13, this.guiLeft + 208, y, 190, 20, "global.linked"));
          var10004 = this.guiLeft + 15;
          y += 22;
          this.addButton(new GuiNpcButton(14, var10004, y, 190, 20, "advanced.scenes"));
          this.addButton(new GuiNpcButton(15, this.guiLeft + 208, y, 190, 20, "advanced.marks"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.field_146127_k == 3) {
               this.save();
               Client.sendData(EnumPacketServer.RoleGet);
          }

          if (button.field_146127_k == 8) {
               this.hasChanges = true;
               this.npc.advanced.setRole(button.getValue());
               this.getButton(3).setEnabled(this.npc.advanced.role != 0 && this.npc.advanced.role != 5);
          }

          if (button.field_146127_k == 4) {
               this.save();
               Client.sendData(EnumPacketServer.JobGet);
          }

          if (button.field_146127_k == 5) {
               this.hasChanges = true;
               this.npc.advanced.setJob(button.getValue());
               this.getButton(4).setEnabled(this.npc.advanced.job != 0 && this.npc.advanced.job != 8 && this.npc.advanced.job != 10);
          }

          if (button.field_146127_k == 9) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCFactionSetup(this.npc));
          }

          if (button.field_146127_k == 10) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCDialogNpcOptions(this.npc, this));
          }

          if (button.field_146127_k == 11) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCSoundsMenu(this.npc));
          }

          if (button.field_146127_k == 7) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCLinesMenu(this.npc));
          }

          if (button.field_146127_k == 12) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCNightSetup(this.npc));
          }

          if (button.field_146127_k == 13) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCAdvancedLinkedNpc(this.npc));
          }

          if (button.field_146127_k == 14) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCScenes(this.npc));
          }

          if (button.field_146127_k == 15) {
               this.save();
               NoppesUtil.openGUI(this.player, new GuiNPCMarks(this.npc));
          }

     }

     public void setGuiData(NBTTagCompound compound) {
          if (compound.func_74764_b("RoleData")) {
               if (this.npc.roleInterface != null) {
                    this.npc.roleInterface.readFromNBT(compound);
               }

               if (this.npc.advanced.role == 1) {
                    NoppesUtil.requestOpenGUI(EnumGuiType.SetupTrader);
               } else if (this.npc.advanced.role == 2) {
                    NoppesUtil.requestOpenGUI(EnumGuiType.SetupFollower);
               } else if (this.npc.advanced.role == 3) {
                    NoppesUtil.requestOpenGUI(EnumGuiType.SetupBank);
               } else if (this.npc.advanced.role == 4) {
                    this.displayGuiScreen(new GuiNpcTransporter(this.npc));
               } else if (this.npc.advanced.role == 6) {
                    this.displayGuiScreen(new GuiNpcCompanion(this.npc));
               } else if (this.npc.advanced.role == 7) {
                    NoppesUtil.openGUI(this.player, new GuiRoleDialog(this.npc));
               }
          } else if (compound.func_74764_b("JobData")) {
               if (this.npc.jobInterface != null) {
                    this.npc.jobInterface.readFromNBT(compound);
               }

               if (this.npc.advanced.job == 1) {
                    NoppesUtil.openGUI(this.player, new GuiNpcBard(this.npc));
               } else if (this.npc.advanced.job == 2) {
                    NoppesUtil.openGUI(this.player, new GuiNpcHealer(this.npc));
               } else if (this.npc.advanced.job == 3) {
                    NoppesUtil.openGUI(this.player, new GuiNpcGuard(this.npc));
               } else if (this.npc.advanced.job == 4) {
                    NoppesUtil.requestOpenGUI(EnumGuiType.SetupItemGiver);
               } else if (this.npc.advanced.job == 5) {
                    NoppesUtil.openGUI(this.player, new GuiNpcFollowerJob(this.npc));
               } else if (this.npc.advanced.job == 6) {
                    NoppesUtil.openGUI(this.player, new GuiNpcSpawner(this.npc));
               } else if (this.npc.advanced.job == 7) {
                    NoppesUtil.openGUI(this.player, new GuiNpcConversation(this.npc));
               } else if (this.npc.advanced.job == 9) {
                    NoppesUtil.openGUI(this.player, new GuiNpcPuppet(this, (EntityCustomNpc)this.npc));
               } else if (this.npc.advanced.job == 11) {
                    NoppesUtil.openGUI(this.player, new GuiJobFarmer(this.npc));
               }
          } else {
               this.npc.advanced.readToNBT(compound);
               this.func_73866_w_();
          }

     }

     public void save() {
          if (this.hasChanges) {
               Client.sendData(EnumPacketServer.MainmenuAdvancedSave, this.npc.advanced.writeToNBT(new NBTTagCompound()));
               this.hasChanges = false;
          }

     }
}
