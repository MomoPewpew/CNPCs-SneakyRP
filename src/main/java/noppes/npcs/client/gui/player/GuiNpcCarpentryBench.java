package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcCarpentryBench extends GuiContainerNPCInterface {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/carpentry.png");
     private ContainerCarpentryBench container;
     private GuiNpcButton button;

     public GuiNpcCarpentryBench(ContainerCarpentryBench container) {
          super((EntityNPCInterface)null, container);
          this.container = container;
          this.title = "";
          this.allowUserInput = false;
          this.closeOnEsc = true;
          this.ySize = 180;
     }

     public void initGui() {
          super.initGui();
          this.addButton(this.button = new GuiNpcButton(0, this.guiLeft + 158, this.guiTop + 4, 12, 20, "..."));
     }

     public void buttonEvent(GuiButton guibutton) {
          this.displayGuiScreen(new GuiRecipes());
     }

     protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
          this.button.enabled = RecipeController.instance != null && !RecipeController.instance.anvilRecipes.isEmpty();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.mc.renderEngine.bindTexture(this.resource);
          int l = (this.width - this.xSize) / 2;
          int i1 = (this.height - this.ySize) / 2;
          String title = I18n.translateToLocal("tile.npccarpentybench.name");
          this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);
          super.drawGuiContainerBackgroundLayer(f, i, j);
          this.fontRenderer.drawString(title, this.guiLeft + 4, this.guiTop + 4, CustomNpcResourceListener.DefaultTextColor);
          this.fontRenderer.drawString(I18n.translateToLocal("container.inventory"), this.guiLeft + 4, this.guiTop + 87, CustomNpcResourceListener.DefaultTextColor);
     }

     public void save() {
     }
}
