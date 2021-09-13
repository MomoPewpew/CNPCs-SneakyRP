package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabQuests;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.util.NaturalOrderComparator;

public class GuiQuestLog extends GuiNPCInterface implements ITopButtonListener, ICustomScrollListener {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/standardbg.png");
     public HashMap activeQuests = new HashMap();
     private HashMap categoryQuests = new HashMap();
     public Quest selectedQuest = null;
     public String selectedCategory = "";
     private EntityPlayer player;
     private GuiCustomScroll scroll;
     private HashMap sideButtons = new HashMap();
     private boolean noQuests = false;
     private final int maxLines = 10;
     private int currentPage = 0;
     private int maxPages = 1;
     TextBlockClient textblock = null;
     private Minecraft mc = Minecraft.getMinecraft();

     public GuiQuestLog(EntityPlayer player) {
          this.player = player;
          this.xSize = 280;
          this.ySize = 180;
          this.drawDefaultBackground = false;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          Iterator var1 = PlayerQuestController.getActiveQuests(this.player).iterator();

          while(var1.hasNext()) {
               Quest quest = (Quest)var1.next();
               String category = quest.category.title;
               if (!this.activeQuests.containsKey(category)) {
                    this.activeQuests.put(category, new ArrayList());
               }

               List list = (List)this.activeQuests.get(category);
               list.add(quest);
          }

          this.sideButtons.clear();
          this.guiTop += 10;
          TabRegistry.updateTabValues(this.guiLeft, this.guiTop, InventoryTabQuests.class);
          TabRegistry.addTabsToList(this.field_146292_n);
          this.noQuests = false;
          if (this.activeQuests.isEmpty()) {
               this.noQuests = true;
          } else {
               List categories = new ArrayList();
               categories.addAll(this.activeQuests.keySet());
               Collections.sort(categories, new NaturalOrderComparator());
               int i = 0;

               for(Iterator var8 = categories.iterator(); var8.hasNext(); ++i) {
                    String category = (String)var8.next();
                    if (this.selectedCategory.isEmpty()) {
                         this.selectedCategory = category;
                    }

                    this.sideButtons.put(i, new GuiMenuSideButton(i, this.guiLeft - 69, this.guiTop + 2 + i * 21, 70, 22, category));
               }

               ((GuiMenuSideButton)this.sideButtons.get(categories.indexOf(this.selectedCategory))).active = true;
               if (this.scroll == null) {
                    this.scroll = new GuiCustomScroll(this, 0);
               }

               HashMap categoryQuests = new HashMap();
               Iterator var11 = ((List)this.activeQuests.get(this.selectedCategory)).iterator();

               while(var11.hasNext()) {
                    Quest q = (Quest)var11.next();
                    categoryQuests.put(q.title, q);
               }

               this.categoryQuests = categoryQuests;
               this.scroll.setList(new ArrayList(categoryQuests.keySet()));
               this.scroll.setSize(134, 174);
               this.scroll.guiLeft = this.guiLeft + 5;
               this.scroll.guiTop = this.guiTop + 15;
               this.addScroll(this.scroll);
               this.addButton(new GuiButtonNextPage(1, this.guiLeft + 286, this.guiTop + 114, true));
               this.addButton(new GuiButtonNextPage(2, this.guiLeft + 144, this.guiTop + 114, false));
               this.getButton(1).visible = this.selectedQuest != null && this.currentPage < this.maxPages - 1;
               this.getButton(2).visible = this.selectedQuest != null && this.currentPage > 0;
          }
     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton instanceof GuiButtonNextPage) {
               if (guibutton.id == 1) {
                    ++this.currentPage;
                    this.func_73866_w_();
               }

               if (guibutton.id == 2) {
                    --this.currentPage;
                    this.func_73866_w_();
               }

          }
     }

     public void func_73863_a(int i, int j, float f) {
          if (this.scroll != null) {
               this.scroll.visible = !this.noQuests;
          }

          this.func_146276_q_();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.mc.renderEngine.bindTexture(this.resource);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 252, 195);
          this.drawTexturedModalRect(this.guiLeft + 252, this.guiTop, 188, 0, 67, 195);
          super.func_73863_a(i, j, f);
          if (this.noQuests) {
               this.mc.fontRenderer.func_78276_b(I18n.func_74838_a("quest.noquests"), this.guiLeft + 84, this.guiTop + 80, CustomNpcResourceListener.DefaultTextColor);
          } else {
               GuiMenuSideButton[] var4 = (GuiMenuSideButton[])this.sideButtons.values().toArray(new GuiMenuSideButton[this.sideButtons.size()]);
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                    GuiMenuSideButton button = var4[var6];
                    button.func_191745_a(this.mc, i, j, f);
               }

               this.mc.fontRenderer.func_78276_b(this.selectedCategory, this.guiLeft + 5, this.guiTop + 5, CustomNpcResourceListener.DefaultTextColor);
               if (this.selectedQuest != null) {
                    this.drawProgress();
                    this.drawQuestText();
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179109_b((float)(this.guiLeft + 148), (float)this.guiTop, 0.0F);
                    GlStateManager.func_179152_a(1.24F, 1.24F, 1.24F);
                    String title = I18n.func_74838_a(this.selectedQuest.title);
                    this.field_146289_q.func_78276_b(title, (130 - this.field_146289_q.func_78256_a(title)) / 2, 4, CustomNpcResourceListener.DefaultTextColor);
                    GlStateManager.func_179121_F();
                    this.func_73730_a(this.guiLeft + 142, this.guiLeft + 312, this.guiTop + 17, -16777216 + CustomNpcResourceListener.DefaultTextColor);
               }
          }
     }

     private void drawQuestText() {
          if (this.textblock != null) {
               int yoffset = this.guiTop + 5;

               for(int i = 0; i < 10; ++i) {
                    int index = i + this.currentPage * 10;
                    if (index < this.textblock.lines.size()) {
                         String text = ((ITextComponent)this.textblock.lines.get(index)).func_150254_d();
                         this.field_146289_q.func_78276_b(text, this.guiLeft + 142, this.guiTop + 20 + i * this.field_146289_q.field_78288_b, CustomNpcResourceListener.DefaultTextColor);
                    }
               }

          }
     }

     private void drawProgress() {
          String title = I18n.func_74838_a("quest.objectives") + ":";
          this.mc.fontRenderer.func_78276_b(title, this.guiLeft + 142, this.guiTop + 130, CustomNpcResourceListener.DefaultTextColor);
          this.func_73730_a(this.guiLeft + 142, this.guiLeft + 312, this.guiTop + 140, -16777216 + CustomNpcResourceListener.DefaultTextColor);
          int yoffset = this.guiTop + 144;
          IQuestObjective[] var3 = this.selectedQuest.questInterface.getObjectives(this.player);
          int var4 = var3.length;

          for(int var5 = 0; var5 < var4; ++var5) {
               IQuestObjective objective = var3[var5];
               this.mc.fontRenderer.func_78276_b("- " + objective.getText(), this.guiLeft + 142, yoffset, CustomNpcResourceListener.DefaultTextColor);
               yoffset += 10;
          }

          this.func_73730_a(this.guiLeft + 142, this.guiLeft + 312, this.guiTop + 178, -16777216 + CustomNpcResourceListener.DefaultTextColor);
          String complete = this.selectedQuest.getNpcName();
          if (complete != null && !complete.isEmpty()) {
               this.mc.fontRenderer.func_78276_b(I18n.func_74837_a("quest.completewith", new Object[]{complete}), this.guiLeft + 142, this.guiTop + 182, CustomNpcResourceListener.DefaultTextColor);
          }

     }

     public void func_73864_a(int i, int j, int k) {
          super.func_73864_a(i, j, k);
          if (k == 0) {
               if (this.scroll != null) {
                    this.scroll.func_73864_a(i, j, k);
               }

               Iterator var4 = (new ArrayList(this.sideButtons.values())).iterator();

               while(var4.hasNext()) {
                    GuiMenuSideButton button = (GuiMenuSideButton)var4.next();
                    if (button.func_146116_c(this.mc, i, j)) {
                         this.sideButtonPressed(button);
                    }
               }
          }

     }

     private void sideButtonPressed(GuiMenuSideButton button) {
          if (!button.active) {
               NoppesUtil.clickSound();
               this.selectedCategory = button.field_146126_j;
               this.selectedQuest = null;
               this.func_73866_w_();
          }
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
          if (scroll.hasSelected()) {
               this.selectedQuest = (Quest)this.categoryQuests.get(scroll.getSelected());
               this.textblock = new TextBlockClient(this.selectedQuest.getLogText(), 172, true, new Object[]{this.player});
               if (this.textblock.lines.size() > 10) {
                    this.maxPages = MathHelper.func_76123_f(1.0F * (float)this.textblock.lines.size() / 10.0F);
               }

               this.currentPage = 0;
               this.func_73866_w_();
          }
     }

     public void func_73869_a(char c, int i) {
          if (i == 1 || i == this.mc.field_71474_y.field_151445_Q.func_151463_i()) {
               this.mc.displayGuiScreen((GuiScreen)null);
               this.mc.func_71381_h();
          }

     }

     public boolean func_73868_f() {
          return false;
     }

     public void save() {
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
