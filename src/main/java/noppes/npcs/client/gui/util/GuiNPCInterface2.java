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

     public void initGui() {
          super.initGui();
          this.menu.initGui(this.guiLeft, this.guiTop, this.xSize);
     }

     public void mouseClicked(int i, int j, int k) {
          if (!this.hasSubGui()) {
               this.menu.mouseClicked(i, j, k);
          }

          super.mouseClicked(i, j, k);
     }

     public abstract void save();

     public void drawScreen(int i, int j, float f) {
          if (this.drawDefaultBackground) {
               this.drawDefaultBackground();
          }

          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.mc.renderEngine.bindTexture(this.background);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 200, 220);
          this.drawTexturedModalRect(this.guiLeft + this.xSize - 230, this.guiTop, 26, 0, 230, 220);
          int x = i;
          int y = j;
          if (this.hasSubGui()) {
               y = 0;
               x = 0;
          }

          this.menu.drawElements(this.getFontRenderer(), x, y, this.mc, f);
          boolean bo = this.drawDefaultBackground;
          this.drawDefaultBackground = false;
          super.drawScreen(i, j, f);
          this.drawDefaultBackground = bo;
     }
}
