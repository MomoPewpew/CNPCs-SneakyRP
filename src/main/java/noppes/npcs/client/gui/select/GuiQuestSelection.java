package noppes.npcs.client.gui.select;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;

public class GuiQuestSelection extends SubGuiInterface implements ICustomScrollListener {
     private HashMap categoryData = new HashMap();
     private HashMap questData = new HashMap();
     private GuiCustomScroll scrollCategories;
     private GuiCustomScroll scrollQuests;
     private QuestCategory selectedCategory;
     public Quest selectedQuest;
     private GuiSelectionListener listener;

     public GuiQuestSelection(int quest) {
          this.drawDefaultBackground = false;
          this.title = "";
          this.setBackground("menubg.png");
          this.xSize = 366;
          this.ySize = 226;
          this.selectedQuest = (Quest)QuestController.instance.quests.get(quest);
          if (this.selectedQuest != null) {
               this.selectedCategory = this.selectedQuest.category;
          }

     }

     public void initGui() {
          super.initGui();
          if (this.parent instanceof GuiSelectionListener) {
               this.listener = (GuiSelectionListener)this.parent;
          }

          this.addLabel(new GuiNpcLabel(0, "gui.categories", this.guiLeft + 8, this.guiTop + 4));
          this.addLabel(new GuiNpcLabel(1, "quest.quests", this.guiLeft + 175, this.guiTop + 4));
          this.addButton(new GuiNpcButton(2, this.guiLeft + this.xSize - 26, this.guiTop + 4, 20, 20, "X"));
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
          if (this.selectedCategory != null) {
               this.scrollCategories.setSelected(this.selectedCategory.title);
          }

          this.scrollCategories.guiLeft = this.guiLeft + 4;
          this.scrollCategories.guiTop = this.guiTop + 14;
          this.addScroll(this.scrollCategories);
          if (this.scrollQuests == null) {
               this.scrollQuests = new GuiCustomScroll(this, 1);
               this.scrollQuests.setSize(170, 200);
          }

          this.scrollQuests.setList(Lists.newArrayList(questData.keySet()));
          if (this.selectedQuest != null) {
               this.scrollQuests.setSelected(this.selectedQuest.title);
          }

          this.scrollQuests.guiLeft = this.guiLeft + 175;
          this.scrollQuests.guiTop = this.guiTop + 14;
          this.addScroll(this.scrollQuests);
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

          this.initGui();
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
          if (this.selectedQuest != null) {
               if (this.listener != null) {
                    this.listener.selected(this.selectedQuest.id, this.selectedQuest.title);
               }

               this.close();
          }
     }

     protected void actionPerformed(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 2) {
               if (this.selectedQuest != null) {
                    this.scrollDoubleClicked((String)null, (GuiCustomScroll)null);
               } else {
                    this.close();
               }
          }

     }
}
