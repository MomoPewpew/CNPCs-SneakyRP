package noppes.npcs.client.gui.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.entity.EntityNPCInterface;

public abstract class GuiNPCInterface2 extends GuiNPCInterface {
     private ResourceLocation background;
     private GuiNpcMenu menu;

     public GuiNPCInterface2(EntityNPCInterface npc) {
          this(npc, -1);
     }

     public GuiNPCInterface2(EntityNPCInterface npc, int activeMenu) {
          super(npc);
          this.background = new ResourceLocation("customnpcs:textures/gui/menubg.png");
          this.xSize = 420;
          this.ySize = 200;
          this.menu = new GuiNpcMenu(this, activeMenu, npc);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.menu.initGui(this.guiLeft, this.guiTop, this.xSize);
     }

     public void func_73864_a(int i, int j, int k) {
          if (!this.hasSubGui()) {
               this.menu.mouseClicked(i, j, k);
          }

          super.func_73864_a(i, j, k);
     }

     public abstract void save();

     public void func_73863_a(int i, int j, float f) {
          if (this.drawDefaultBackground) {
               this.func_146276_q_();
          }

          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.background);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 200, 220);
          this.drawTexturedModalRect(this.guiLeft + this.xSize - 230, this.guiTop, 26, 0, 230, 220);
          int x = i;
          int y = j;
          if (this.hasSubGui()) {
               y = 0;
               x = 0;
          }

          this.menu.drawElements(this.getFontRenderer(), x, y, this.field_146297_k, f);
          boolean bo = this.drawDefaultBackground;
          this.drawDefaultBackground = false;
          super.func_73863_a(i, j, f);
          this.drawDefaultBackground = bo;
     }
}
