package micdoodle8.mods.galacticraft.api.client.tabs;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.client.gui.player.GuiFaction;
import noppes.npcs.util.CustomNPCsScheduler;

public class InventoryTabFactions extends AbstractTab {
     public InventoryTabFactions() {
          super(0, 0, 0, new ItemStack(Items.BANNER, 1, 1));
          this.displayString = NoppesStringUtils.translate("menu.factions");
     }

     public void onTabClicked() {
          CustomNPCsScheduler.runTack(() -> {
               Minecraft mc = Minecraft.getMinecraft();
               mc.displayGuiScreen(new GuiFaction());
          });
     }

     public boolean shouldAddToList() {
          return true;
     }

     public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
          if (this.enabled && this.visible) {
               Minecraft mc = Minecraft.getMinecraft();
               boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
               if (hovered) {
                    int x = mouseX + mc.fontRenderer.getStringWidth(this.displayString);
                    GlStateManager.translate((float)x, (float)(this.y + 2), 0.0F);
                    this.drawHoveringText(Arrays.asList(this.displayString), 0, 0, mc.fontRenderer);
                    GlStateManager.translate((float)(-x), (float)(-(this.y + 2)), 0.0F);
               }

               super.drawButton(minecraft, mouseX, mouseY, partialTicks);
          } else {
               super.drawButton(minecraft, mouseX, mouseY, partialTicks);
          }
     }

     protected void drawHoveringText(List list, int x, int y, FontRenderer font) {
          if (!list.isEmpty()) {
               GlStateManager.disableRescaleNormal();
               RenderHelper.disableStandardItemLighting();
               GlStateManager.disableLighting();
               GlStateManager.disableDepth();
               int k = 0;
               Iterator iterator = list.iterator();

               int k2;
               while(iterator.hasNext()) {
                    String s = (String)iterator.next();
                    k2 = font.getStringWidth(s);
                    if (k2 > k) {
                         k = k2;
                    }
               }

               int j2 = x + 12;
               k2 = y - 12;
               int i1 = 8;
               if (list.size() > 1) {
                    i1 += 2 + (list.size() - 1) * 10;
               }

               if (j2 + k > this.width) {
                    j2 -= 28 + k;
               }

               if (k2 + i1 + 6 > this.height) {
                    k2 = this.height - i1 - 6;
               }

               this.zLevel = 300.0F;
               this.itemRender.zLevel = 300.0F;
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

               for(int i2 = 0; i2 < list.size(); ++i2) {
                    String s1 = (String)list.get(i2);
                    font.drawStringWithShadow(s1, (float)j2, (float)k2, -1);
                    if (i2 == 0) {
                         k2 += 2;
                    }

                    k2 += 10;
               }

               this.zLevel = 0.0F;
               this.itemRender.zLevel = 0.0F;
               GlStateManager.enableLighting();
               GlStateManager.enableDepth();
               RenderHelper.enableStandardItemLighting();
               GlStateManager.enableRescaleNormal();
          }
     }
}
