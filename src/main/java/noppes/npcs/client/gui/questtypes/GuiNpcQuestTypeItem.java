package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestItem;

public class GuiNpcQuestTypeItem extends GuiContainerNPCInterface implements ITextfieldListener {
     private Quest quest;
     private static final ResourceLocation field_110422_t = new ResourceLocation("customnpcs", "textures/gui/followersetup.png");

     public GuiNpcQuestTypeItem(EntityNPCInterface npc, ContainerNpcQuestTypeItem container) {
          super(npc, container);
          this.quest = NoppesUtilServer.getEditingQuest(this.player);
          this.title = "";
          this.field_147000_g = 202;
          this.closeOnEsc = false;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "quest.takeitems", this.field_147003_i + 4, this.field_147009_r + 8));
          this.addButton(new GuiNpcButton(0, this.field_147003_i + 90, this.field_147009_r + 3, 60, 20, new String[]{"gui.yes", "gui.no"}, ((QuestItem)this.quest.questInterface).leaveItems ? 1 : 0));
          this.addLabel(new GuiNpcLabel(1, "gui.ignoreDamage", this.field_147003_i + 4, this.field_147009_r + 29));
          this.addButton(new GuiNpcButtonYesNo(1, this.field_147003_i + 90, this.field_147009_r + 24, 50, 20, ((QuestItem)this.quest.questInterface).ignoreDamage));
          this.addLabel(new GuiNpcLabel(2, "gui.ignoreNBT", this.field_147003_i + 62, this.field_147009_r + 51));
          this.addButton(new GuiNpcButtonYesNo(2, this.field_147003_i + 120, this.field_147009_r + 46, 50, 20, ((QuestItem)this.quest.questInterface).ignoreNBT));
          this.addButton(new GuiNpcButton(5, this.field_147003_i, this.field_147009_r + this.field_147000_g, 98, 20, "gui.back"));
     }

     public void func_146284_a(GuiButton guibutton) {
          if (guibutton.field_146127_k == 0) {
               ((QuestItem)this.quest.questInterface).leaveItems = ((GuiNpcButton)guibutton).getValue() == 1;
          }

          if (guibutton.field_146127_k == 1) {
               ((QuestItem)this.quest.questInterface).ignoreDamage = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.field_146127_k == 2) {
               ((QuestItem)this.quest.questInterface).ignoreNBT = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.field_146127_k == 5) {
               NoppesUtil.openGUI(this.player, GuiNPCManageQuest.Instance);
          }

     }

     protected void func_146976_a(float f, int i, int j) {
          this.func_146270_b(0);
          GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.field_71446_o.func_110577_a(field_110422_t);
          int l = (this.field_146294_l - this.field_146999_f) / 2;
          int i1 = (this.field_146295_m - this.field_147000_g) / 2;
          this.func_73729_b(l, i1, 0, 0, this.field_146999_f, this.field_147000_g);
          super.func_146976_a(f, i, j);
     }

     public void save() {
     }

     public void unFocused(GuiNpcTextField textfield) {
          this.quest.rewardExp = textfield.getInteger();
     }
}
