package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public abstract class AbstractTab extends GuiButton {
     ResourceLocation texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
     ItemStack renderStack;
     public int potionOffsetLast;
     protected RenderItem itemRender;

     public AbstractTab(int id, int posX, int posY, ItemStack renderStack) {
          super(id, posX, posY, 28, 32, "");
          this.renderStack = renderStack;
          this.itemRender = FMLClientHandler.instance().getClient().func_175599_af();
     }

     public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
          int newPotionOffset = TabRegistry.getPotionOffsetNEI();
          if (newPotionOffset != this.potionOffsetLast) {
               this.field_146128_h += newPotionOffset - this.potionOffsetLast;
               this.potionOffsetLast = newPotionOffset;
          }

          if (this.field_146125_m) {
               GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
               int yTexPos = this.field_146124_l ? 3 : 32;
               int ySize = this.field_146124_l ? 25 : 32;
               int xOffset = this.field_146127_k == 2 ? 0 : 1;
               int yPos = this.field_146129_i + (this.field_146124_l ? 3 : 0);
               mc.field_71446_o.func_110577_a(this.texture);
               this.func_73729_b(this.field_146128_h, yPos, xOffset * 28, yTexPos, 28, ySize);
               RenderHelper.func_74520_c();
               this.field_73735_i = 100.0F;
               this.itemRender.field_77023_b = 100.0F;
               GlStateManager.func_179145_e();
               GlStateManager.func_179091_B();
               this.itemRender.func_180450_b(this.renderStack, this.field_146128_h + 6, this.field_146129_i + 8);
               this.itemRender.func_180453_a(mc.field_71466_p, this.renderStack, this.field_146128_h + 6, this.field_146129_i + 8, (String)null);
               GlStateManager.func_179140_f();
               this.itemRender.field_77023_b = 0.0F;
               this.field_73735_i = 0.0F;
               RenderHelper.func_74518_a();
          }

     }

     public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
          boolean inWindow = this.field_146124_l && this.field_146125_m && mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
          if (inWindow) {
               this.onTabClicked();
          }

          return inWindow;
     }

     public abstract void onTabClicked();

     public abstract boolean shouldAddToList();
}
