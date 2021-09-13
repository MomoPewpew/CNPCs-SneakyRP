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
          this.field_146291_p = false;
          this.closeOnEsc = true;
          this.field_147000_g = 180;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(this.button = new GuiNpcButton(0, this.field_147003_i + 158, this.field_147009_r + 4, 12, 20, "..."));
     }

     public void buttonEvent(GuiButton guibutton) {
          this.displayGuiScreen(new GuiRecipes());
     }

     protected void func_146976_a(float f, int i, int j) {
          this.button.enabled = RecipeController.instance != null && !RecipeController.instance.anvilRecipes.isEmpty();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.resource);
          int l = (this.width - this.field_146999_f) / 2;
          int i1 = (this.height - this.field_147000_g) / 2;
          String title = I18n.func_74838_a("tile.npccarpentybench.name");
          this.drawTexturedModalRect(l, i1, 0, 0, this.field_146999_f, this.field_147000_g);
          super.func_146976_a(f, i, j);
          this.field_146289_q.func_78276_b(title, this.field_147003_i + 4, this.field_147009_r + 4, CustomNpcResourceListener.DefaultTextColor);
          this.field_146289_q.func_78276_b(I18n.func_74838_a("container.inventory"), this.field_147003_i + 4, this.field_147009_r + 87, CustomNpcResourceListener.DefaultTextColor);
     }

     public void save() {
     }
}
