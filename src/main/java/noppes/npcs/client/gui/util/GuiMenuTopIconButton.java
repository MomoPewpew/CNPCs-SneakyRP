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

     public void func_191745_a(Minecraft minecraft, int i, int j, float partialTicks) {
          if (this.getVisible()) {
               if (this.item.func_77973_b() == null) {
                    this.item = new ItemStack(Blocks.field_150346_d);
               }

               this.hover = i >= this.x && j >= this.y && i < this.x + this.getWidth() && j < this.y + this.height;
               Minecraft mc = Minecraft.getMinecraft();
               if (this.hover && !this.active) {
                    int x = i + mc.fontRenderer.func_78256_a(this.field_146126_j);
                    GlStateManager.func_179109_b((float)x, (float)(this.y + 2), 0.0F);
                    this.drawHoveringText(Arrays.asList(this.field_146126_j), 0, 0, mc.fontRenderer);
                    GlStateManager.func_179109_b((float)(-x), (float)(-(this.y + 2)), 0.0F);
               }

               mc.func_110434_K().bindTexture(resource);
               GlStateManager.func_179094_E();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.func_179147_l();
               GlStateManager.disableLighting();
               this.drawTexturedModalRect(this.x, this.y + (this.active ? 2 : 0), 0, this.active ? 32 : 0, 28, 28);
               this.zLevel = 100.0F;
               itemRender.zLevel = 100.0F;
               GlStateManager.enableLighting();
               GlStateManager.enableRescaleNormal();
               RenderHelper.enableGUIStandardItemLighting();
               itemRender.renderItemAndEffectIntoGUI(this.item, this.x + 6, this.y + 10);
               itemRender.func_175030_a(mc.fontRenderer, this.item, this.x + 6, this.y + 10);
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableLighting();
               itemRender.zLevel = 0.0F;
               this.zLevel = 0.0F;
               GlStateManager.func_179121_F();
          }
     }

     protected void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font) {
          if (!p_146283_1_.isEmpty()) {
               GlStateManager.func_179101_C();
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableLighting();
               GlStateManager.func_179097_i();
               int k = 0;
               Iterator iterator = p_146283_1_.iterator();

               int k2;
               while(iterator.hasNext()) {
                    String s = (String)iterator.next();
                    k2 = font.func_78256_a(s);
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
               this.func_73733_a(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
               this.func_73733_a(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
               this.func_73733_a(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
               this.func_73733_a(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
               this.func_73733_a(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
               int k1 = 1347420415;
               int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
               this.func_73733_a(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
               this.func_73733_a(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
               this.func_73733_a(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
               this.func_73733_a(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

               for(int i2 = 0; i2 < p_146283_1_.size(); ++i2) {
                    String s1 = (String)p_146283_1_.get(i2);
                    font.func_175063_a(s1, (float)j2, (float)k2, -1);
                    if (i2 == 0) {
                         k2 += 2;
                    }

                    k2 += 10;
               }

               this.zLevel = 0.0F;
               itemRender.zLevel = 0.0F;
               GlStateManager.enableLighting();
               GlStateManager.func_179126_j();
               RenderHelper.func_74519_b();
               GlStateManager.enableRescaleNormal();
          }

     }
}
