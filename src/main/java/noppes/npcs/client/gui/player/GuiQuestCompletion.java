package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.TextBlockClient;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;

public class GuiQuestCompletion extends GuiNPCInterface implements ITopButtonListener {
     private IQuest quest;
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/smallbg.png");

     public GuiQuestCompletion(IQuest quest) {
          this.xSize = 176;
          this.ySize = 222;
          this.quest = quest;
          this.drawDefaultBackground = false;
          this.title = "";
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          String questTitle = I18n.translateToLocal(this.quest.getName());
          int left = (this.xSize - this.field_146289_q.getStringWidth(questTitle)) / 2;
          this.addLabel(new GuiNpcLabel(0, questTitle, this.guiLeft + left, this.guiTop + 4));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 38, this.guiTop + this.ySize - 24, 100, 20, I18n.translateToLocal("quest.complete")));
     }

     public void func_73863_a(int i, int j, float f) {
          this.func_146276_q_();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.resource);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
          this.func_73730_a(this.guiLeft + 4, this.guiLeft + 170, this.guiTop + 13, -16777216 + CustomNpcResourceListener.DefaultTextColor);
          this.drawQuestText();
          super.func_73863_a(i, j, f);
     }

     private void drawQuestText() {
          int xoffset = this.guiLeft + 4;
          TextBlockClient block = new TextBlockClient(this.quest.getCompleteText(), 172, true, new Object[]{this.player});
          int yoffset = this.guiTop + 20;

          for(int i = 0; i < block.lines.size(); ++i) {
               String text = ((ITextComponent)block.lines.get(i)).func_150254_d();
               this.field_146289_q.func_78276_b(text, this.guiLeft + 4, this.guiTop + 16 + i * this.field_146289_q.field_78288_b, CustomNpcResourceListener.DefaultTextColor);
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.id == 0) {
               NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, this.quest.getId());
               this.close();
          }

     }

     public void func_73869_a(char c, int i) {
          if (i == 1 || this.isInventoryKey(i)) {
               this.close();
          }

     }

     public void save() {
          NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, this.quest.getId());
     }
}
