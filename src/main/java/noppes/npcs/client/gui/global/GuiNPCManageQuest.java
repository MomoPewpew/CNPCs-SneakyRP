package noppes.npcs.client.gui.global;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiEditText;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCManageQuest extends GuiNPCInterface2 implements ISubGuiListener, ICustomScrollListener, GuiYesNoCallback {
     private HashMap categoryData = new HashMap();
     private HashMap questData = new HashMap();
     private GuiCustomScroll scrollCategories;
     private GuiCustomScroll scrollQuests;
     public static GuiScreen Instance;
     private QuestCategory selectedCategory;
     private Quest selectedQuest;

     public GuiNPCManageQuest(EntityNPCInterface npc) {
          super(npc);
          Instance = this;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "gui.categories", this.guiLeft + 8, this.guiTop + 4));
          this.addLabel(new GuiNpcLabel(1, "quest.quests", this.guiLeft + 175, this.guiTop + 4));
          this.addLabel(new GuiNpcLabel(3, "quest.quests", this.guiLeft + 356, this.guiTop + 8));
          this.addButton(new GuiNpcButton(13, this.guiLeft + 356, this.guiTop + 18, 58, 20, "selectServer.edit", this.selectedQuest != null));
          this.addButton(new GuiNpcButton(12, this.guiLeft + 356, this.guiTop + 41, 58, 20, "gui.remove", this.selectedQuest != null));
          this.addButton(new GuiNpcButton(11, this.guiLeft + 356, this.guiTop + 64, 58, 20, "gui.add", this.selectedCategory != null));
          this.addLabel(new GuiNpcLabel(2, "gui.categories", this.guiLeft + 356, this.guiTop + 110));
          this.addButton(new GuiNpcButton(3, this.guiLeft + 356, this.guiTop + 120, 58, 20, "selectServer.edit", this.selectedCategory != null));
          this.addButton(new GuiNpcButton(2, this.guiLeft + 356, this.guiTop + 143, 58, 20, "gui.remove", this.selectedCategory != null));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 356, this.guiTop + 166, 58, 20, "gui.add"));
          HashMap categoryData = new HashMap();
          HashMap questData = new HashMap();
          Iterator var3 = QuestController.instance.categories.values().iterator();

          while(var3.hasNext()) {
               QuestCategory category = (QuestCategory)var3.next();
               categoryData.put(category.title, category);
          }

          this.categoryData = categoryData;
          if (this.selectedCategory != null) {
               var3 = this.selectedCategory.quests.values().iterator();

               while(var3.hasNext()) {
                    Quest quest = (Quest)var3.next();
                    questData.put(quest.title, quest);
               }
          }

          this.questData = questData;
          if (this.scrollCategories == null) {
               this.scrollCategories = new GuiCustomScroll(this, 0);
               this.scrollCategories.setSize(170, 200);
          }

          this.scrollCategories.setList(Lists.newArrayList(categoryData.keySet()));
          this.scrollCategories.guiLeft = this.guiLeft + 4;
          this.scrollCategories.guiTop = this.guiTop + 14;
          this.addScroll(this.scrollCategories);
          if (this.scrollQuests == null) {
               this.scrollQuests = new GuiCustomScroll(this, 1);
               this.scrollQuests.setSize(170, 200);
          }

          this.scrollQuests.setList(Lists.newArrayList(questData.keySet()));
          this.scrollQuests.guiLeft = this.guiLeft + 175;
          this.scrollQuests.guiTop = this.guiTop + 14;
          this.addScroll(this.scrollQuests);
     }

     public void buttonEvent(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.id == 1) {
               this.setSubGui(new SubGuiEditText(1, I18n.func_74838_a("gui.new")));
          }

          GuiYesNo guiyesno;
          if (button.id == 2) {
               guiyesno = new GuiYesNo(this, this.selectedCategory.title, I18n.func_74838_a("gui.deleteMessage"), 2);
               this.displayGuiScreen(guiyesno);
          }

          if (button.id == 3) {
               this.setSubGui(new SubGuiEditText(3, this.selectedCategory.title));
          }

          if (button.id == 11) {
               this.setSubGui(new SubGuiEditText(11, I18n.func_74838_a("gui.new")));
          }

          if (button.id == 12) {
               guiyesno = new GuiYesNo(this, this.selectedQuest.title, I18n.func_74838_a("gui.deleteMessage"), 12);
               this.displayGuiScreen(guiyesno);
          }

          if (button.id == 13) {
               this.setSubGui(new GuiQuestEdit(this.selectedQuest));
          }

     }

     public void subGuiClosed(SubGuiInterface subgui) {
          if (!(subgui instanceof SubGuiEditText) || !((SubGuiEditText)subgui).cancelled) {
               if (subgui.id == 1) {
                    QuestCategory category = new QuestCategory();

                    for(category.title = ((SubGuiEditText)subgui).text; QuestController.instance.containsCategoryName(category); category.title = category.title + "_") {
                    }

                    Client.sendData(EnumPacketServer.QuestCategorySave, category.writeNBT(new NBTTagCompound()));
               }

               if (subgui.id == 3) {
                    StringBuilder var10000;
                    QuestCategory var10002;
                    for(this.selectedCategory.title = ((SubGuiEditText)subgui).text; QuestController.instance.containsCategoryName(this.selectedCategory); var10002.title = var10000.append(var10002.title).append("_").toString()) {
                         var10000 = new StringBuilder();
                         var10002 = this.selectedCategory;
                    }

                    Client.sendData(EnumPacketServer.QuestCategorySave, this.selectedCategory.writeNBT(new NBTTagCompound()));
               }

               if (subgui.id == 11) {
                    Quest quest = new Quest(this.selectedCategory);

                    for(quest.title = ((SubGuiEditText)subgui).text; QuestController.instance.containsQuestName(this.selectedCategory, quest); quest.title = quest.title + "_") {
                    }

                    Client.sendData(EnumPacketServer.QuestSave, this.selectedCategory.id, quest.writeToNBT(new NBTTagCompound()));
               }

               if (subgui instanceof GuiQuestEdit) {
                    this.func_73866_w_();
               }

          }
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
          if (guiCustomScroll.id == 0) {
               this.selectedCategory = (QuestCategory)this.categoryData.get(this.scrollCategories.getSelected());
               this.selectedQuest = null;
               this.scrollQuests.selected = -1;
          }

          if (guiCustomScroll.id == 1) {
               this.selectedQuest = (Quest)this.questData.get(this.scrollQuests.getSelected());
          }

          this.func_73866_w_();
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
          if (this.selectedQuest != null && scroll.id == 1) {
               this.setSubGui(new GuiQuestEdit(this.selectedQuest));
          }

     }

     public void close() {
          super.close();
     }

     public void save() {
          GuiNpcTextField.unfocus();
     }

     public void func_73878_a(boolean result, int id) {
          NoppesUtil.openGUI(this.player, this);
          if (result) {
               if (id == 2) {
                    Client.sendData(EnumPacketServer.QuestCategoryRemove, this.selectedCategory.id);
               }

               if (id == 12) {
                    Client.sendData(EnumPacketServer.QuestRemove, this.selectedQuest.id);
               }

          }
     }
}
