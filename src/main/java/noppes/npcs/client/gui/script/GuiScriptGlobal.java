package noppes.npcs.client.gui.script;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;

public class GuiScriptGlobal extends GuiNPCInterface {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/smallbg.png");

     public GuiScriptGlobal() {
          this.xSize = 176;
          this.ySize = 222;
          this.drawDefaultBackground = false;
          this.title = "";
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(new GuiNpcButton(0, this.guiLeft + 38, this.guiTop + 20, 100, 20, "Players"));
          this.addButton(new GuiNpcButton(1, this.guiLeft + 38, this.guiTop + 50, 100, 20, "Forge"));
     }

     public void func_73863_a(int i, int j, float f) {
          this.func_146276_q_();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.resource);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
          super.func_73863_a(i, j, f);
     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.id == 0) {
               this.displayGuiScreen(new GuiScriptPlayers());
          }

          if (guibutton.id == 1) {
               this.displayGuiScreen(new GuiScriptForge());
          }

     }

     public void func_73869_a(char c, int i) {
          if (i == 1 || this.isInventoryKey(i)) {
               this.close();
          }

     }

     public void save() {
     }
}
