package noppes.npcs.client.gui.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiMenuTopIconButton extends GuiMenuTopButton {
     private static final ResourceLocation resource = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
     protected static RenderItem itemRender;
     private ItemStack item;

     public GuiMenuTopIconButton(int i, int x, int y, String s, ItemStack item) {
          super(i, x, y, s);
          this.width = 28;
          this.height = 28;
          this.item = item;
          itemRender = Minecraft.getMinecraft().getRenderItem();
     }

     public GuiMenuTopIconButton(int i, GuiButton parent, String s, ItemStack item) {
          super(i, parent, s);
          this.width = 28;
          this.height = 28;
          this.item = item;
     }

     public GuiMenuTopIconButton(int i, int x, int y, String s, IButtonListener listener, ItemStack item) {
          super(i, x, y, s);
          this.width = 28;
          this.height = 28;
          this.item = item;
          this.listener = listener;
     }

     public GuiMenuTopIconButton(int i, GuiButton parent, String s, IButtonListener listener, ItemStack item) {
          super(i, parent, s, listener);
          this.width = 28;
          this.height = 28;
          this.item = item;
     }

     public void drawButton(Minecraft minecraft, int i, int j, float partialTicks) {
          if (this.getVisible()) {
               if (this.item.getItem() == null) {
                    this.item = new ItemStack(Blocks.DIRT);
               }

               this.hover = i >= this.x && j >= this.y && i < this.x + this.getWidth() && j < this.y + this.height;
               Minecraft mc = Minecraft.getMinecraft();
               if (this.hover && !this.active) {
                    int x = i + mc.fontRenderer.getStringWidth(this.displayString);
                    GlStateManager.translate((float)x, (float)(this.y + 2), 0.0F);
                    this.drawHoveringText(Arrays.asList(this.displayString), 0, 0, mc.fontRenderer);
                    GlStateManager.translate((float)(-x), (float)(-(this.y + 2)), 0.0F);
               }

               mc.getTextureManager().bindTexture(resource);
               GlStateManager.pushMatrix();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.enableBlend();
               GlStateManager.disableLighting();
               this.drawTexturedModalRect(this.x, this.y + (this.active ? 2 : 0), 0, this.active ? 32 : 0, 28, 28);
               this.zLevel = 100.0F;
               itemRender.zLevel = 100.0F;
               GlStateManager.enableLighting();
               GlStateManager.enableRescaleNormal();
               RenderHelper.enableGUIStandardItemLighting();
               itemRender.renderItemAndEffectIntoGUI(this.item, this.x + 6, this.y + 10);
               itemRender.renderItemOverlays(mc.fontRenderer, this.item, this.x + 6, this.y + 10);
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableLighting();
               itemRender.zLevel = 0.0F;
               this.zLevel = 0.0F;
               GlStateManager.popMatrix();
          }
     }

     protected void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
          if (!p_146283_1_.isEmpty()) {
               GlStateManager.disableRescaleNormal();
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableLighting();
               GlStateManager.disableDepth();
               int k = 0;
               Iterator iterator = p_146283_1_.iterator();

               int k2;
               while(iterator.hasNext()) {
                    String s = (String)iterator.next();
                    k2 = font.getStringWidth(s);
                    if (k2 > k) {
                         k = k2;
                    }
               }

               int j2 = p_146283_2_ + 12;
               k2 = p_146283_3_ - 12;
               int i1 = 8;
               if (p_146283_1_.size() > 1) {
                    i1 += 2 + (p_146283_1_.size() - 1) * 10;
               }

               if (j2 + k > this.width) {
                    j2 -= 28 + k;
               }

               if (k2 + i1 + 6 > this.height) {
                    k2 = this.height - i1 - 6;
               }

               this.zLevel = 300.0F;
               itemRender.zLevel = 300.0F;
               int j1 = -267386864;
               this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
               this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
               this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
               this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
               this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
               int k1 = 1347420415;
               int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
               this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
               this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
               this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
               this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

               for(int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
                    String s1 = (String)p_146283_1_.get(i2);
                    font.drawStringWithShadow(s1, (float)j2, (float)k2, -1);
                    if (i2 == 0) {
                         k2 += 2;
                    }

                    k2 += 10;
               }

               this.zLevel = 0.0F;
               itemRender.zLevel = 0.0F;
               GlStateManager.enableLighting();
               GlStateManager.enableDepth();
               RenderHelper.enableStandardItemLighting();
               GlStateManager.enableRescaleNormal();
          }

     }
}
