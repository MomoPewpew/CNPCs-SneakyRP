package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.data.RecipeCarpentry;

@SideOnly(Side.CLIENT)
public class GuiRecipes extends GuiNPCInterface {
     private static final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/slot.png");
     private int page = 0;
     private boolean npcRecipes = true;
     private GuiNpcLabel label;
     private GuiNpcButton left;
     private GuiNpcButton right;
     private List recipes = new ArrayList();

     public GuiRecipes() {
          this.ySize = 182;
          this.xSize = 256;
          this.setBackground("recipes.png");
          this.closeOnEsc = true;
          this.recipes.addAll(RecipeController.instance.anvilRecipes.values());
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "Recipe List", this.guiLeft + 5, this.guiTop + 5));
          this.addLabel(this.label = new GuiNpcLabel(1, "", this.guiLeft + 5, this.guiTop + 168));
          this.addButton(this.left = new GuiButtonNextPage(1, this.guiLeft + 150, this.guiTop + 164, true));
          this.addButton(this.right = new GuiButtonNextPage(2, this.guiLeft + 80, this.guiTop + 164, false));
          this.updateButton();
     }

     private void updateButton() {
          this.right.visible = this.right.enabled = this.page > 0;
          this.left.visible = this.left.enabled = this.page + 1 < MathHelper.func_76123_f((float)this.recipes.size() / 4.0F);
     }

     protected void func_146284_a(GuiButton button) {
          if (button.enabled) {
               if (button == this.right) {
                    --this.page;
               }

               if (button == this.left) {
                    ++this.page;
               }

               this.updateButton();
          }
     }

     public void func_73863_a(int xMouse, int yMouse, float f) {
          super.func_73863_a(xMouse, yMouse, f);
          this.field_146297_k.renderEngine.bindTexture(resource);
          this.label.label = this.page + 1 + "/" + MathHelper.func_76123_f((float)this.recipes.size() / 4.0F);
          this.label.x = this.guiLeft + (256 - Minecraft.func_71410_x().fontRenderer.func_78256_a(this.label.label)) / 2;

          int i;
          int index;
          IRecipe irecipe;
          int x;
          int j;
          int k;
          ItemStack item;
          for(i = 0; i < 4; ++i) {
               index = i + this.page * 4;
               if (index >= this.recipes.size()) {
                    break;
               }

               irecipe = (IRecipe)this.recipes.get(index);
               if (!irecipe.func_77571_b().func_190926_b()) {
                    int x = this.guiLeft + 5 + i / 2 * 126;
                    x = this.guiTop + 15 + i % 2 * 76;
                    this.drawItem(irecipe.func_77571_b(), x + 98, x + 28, xMouse, yMouse);
                    if (irecipe instanceof RecipeCarpentry) {
                         RecipeCarpentry recipe = (RecipeCarpentry)irecipe;
                         x += (72 - recipe.field_77576_b * 18) / 2;
                         x += (72 - recipe.field_77577_c * 18) / 2;

                         for(j = 0; j < recipe.field_77576_b; ++j) {
                              for(k = 0; k < recipe.field_77577_c; ++k) {
                                   this.field_146297_k.renderEngine.bindTexture(resource);
                                   GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                   this.drawTexturedModalRect(x + j * 18, x + k * 18, 0, 0, 18, 18);
                                   item = recipe.getCraftingItem(j + k * recipe.field_77576_b);
                                   if (!item.func_190926_b()) {
                                        this.drawItem(item, x + j * 18 + 1, x + k * 18 + 1, xMouse, yMouse);
                                   }
                              }
                         }
                    }
               }
          }

          for(i = 0; i < 4; ++i) {
               index = i + this.page * 4;
               if (index >= this.recipes.size()) {
                    break;
               }

               irecipe = (IRecipe)this.recipes.get(index);
               if (irecipe instanceof RecipeCarpentry) {
                    RecipeCarpentry recipe = (RecipeCarpentry)irecipe;
                    if (!recipe.func_77571_b().func_190926_b()) {
                         x = this.guiLeft + 5 + i / 2 * 126;
                         int y = this.guiTop + 15 + i % 2 * 76;
                         this.drawOverlay(recipe.func_77571_b(), x + 98, y + 22, xMouse, yMouse);
                         x += (72 - recipe.field_77576_b * 18) / 2;
                         y += (72 - recipe.field_77577_c * 18) / 2;

                         for(j = 0; j < recipe.field_77576_b; ++j) {
                              for(k = 0; k < recipe.field_77577_c; ++k) {
                                   item = recipe.getCraftingItem(j + k * recipe.field_77576_b);
                                   if (!item.func_190926_b()) {
                                        this.drawOverlay(item, x + j * 18 + 1, y + k * 18 + 1, xMouse, yMouse);
                                   }
                              }
                         }
                    }
               }
          }

     }

     private void drawItem(ItemStack item, int x, int y, int xMouse, int yMouse) {
          GlStateManager.func_179094_E();
          GlStateManager.enableRescaleNormal();
          RenderHelper.enableGUIStandardItemLighting();
          this.field_146296_j.zLevel = 100.0F;
          this.field_146296_j.renderItemAndEffectIntoGUI(item, x, y);
          this.field_146296_j.func_175030_a(this.field_146289_q, item, x, y);
          this.field_146296_j.zLevel = 0.0F;
          RenderHelper.disableStandardItemLighting();
          GlStateManager.func_179101_C();
          GlStateManager.func_179121_F();
     }

     private void drawOverlay(ItemStack item, int x, int y, int xMouse, int yMouse) {
          if (this.func_146978_c(x - this.guiLeft, y - this.guiTop, 16, 16, xMouse, yMouse)) {
               this.func_146285_a(item, xMouse, yMouse);
          }

     }

     protected boolean func_146978_c(int p_146978_1_, int p_146978_2_, int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_) {
          int k1 = this.guiLeft;
          int l1 = this.guiTop;
          p_146978_5_ -= k1;
          p_146978_6_ -= l1;
          return p_146978_5_ >= p_146978_1_ - 1 && p_146978_5_ < p_146978_1_ + p_146978_3_ + 1 && p_146978_6_ >= p_146978_2_ - 1 && p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
     }

     public void save() {
     }
}
