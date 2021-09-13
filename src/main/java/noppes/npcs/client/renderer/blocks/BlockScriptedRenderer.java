package noppes.npcs.client.renderer.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.client.TextBlockClient;

public class BlockScriptedRenderer extends BlockRendererInterface {
     private static Random random = new Random();

     public void func_192841_a(TileEntity te, double x, double y, double z, float partialTicks, int blockDamage, float alpha) {
          TileScripted tile = (TileScripted)te;
          GlStateManager.func_179094_E();
          GlStateManager.func_179084_k();
          RenderHelper.func_74519_b();
          GlStateManager.func_179137_b(x + 0.5D, y, z + 0.5D);
          if (this.overrideModel()) {
               GlStateManager.func_179137_b(0.0D, 0.5D, 0.0D);
               this.renderItem(new ItemStack(CustomItems.scripted));
          } else {
               GlStateManager.func_179114_b((float)tile.rotationY, 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179114_b((float)tile.rotationX, 1.0F, 0.0F, 0.0F);
               GlStateManager.func_179114_b((float)tile.rotationZ, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179152_a(tile.scaleX, tile.scaleY, tile.scaleZ);
               Block b = tile.blockModel;
               if (b != null && b != Blocks.field_150350_a) {
                    if (b == CustomItems.scripted) {
                         GlStateManager.func_179137_b(0.0D, 0.5D, 0.0D);
                         this.renderItem(tile.itemModel);
                    } else {
                         IBlockState state = b.func_176203_a(tile.itemModel.func_77952_i());
                         this.renderBlock(tile, b, state);
                         if (b.hasTileEntity(state) && !tile.renderTileErrored) {
                              try {
                                   if (tile.renderTile == null) {
                                        TileEntity entity = b.createTileEntity(this.func_178459_a(), state);
                                        entity.func_174878_a(tile.func_174877_v());
                                        entity.func_145834_a(this.func_178459_a());
                                        ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity, tile.itemModel.func_77952_i(), 5);
                                        ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity, b, 6);
                                        tile.renderTile = entity;
                                        if (entity instanceof ITickable) {
                                             tile.renderTileUpdate = (ITickable)entity;
                                        }
                                   }

                                   TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.field_147556_a.func_147547_b(tile.renderTile);
                                   if (renderer != null) {
                                        renderer.func_192841_a(tile.renderTile, -0.5D, 0.0D, -0.5D, partialTicks, blockDamage, alpha);
                                   } else {
                                        tile.renderTileErrored = true;
                                   }
                              } catch (Exception var15) {
                                   tile.renderTileErrored = true;
                              }
                         }
                    }
               } else {
                    GlStateManager.func_179137_b(0.0D, 0.5D, 0.0D);
                    this.renderItem(tile.itemModel);
               }
          }

          GlStateManager.func_179121_F();
          if (!tile.text1.text.isEmpty()) {
               this.drawText(tile.text1, x, y, z);
          }

          if (!tile.text2.text.isEmpty()) {
               this.drawText(tile.text2, x, y, z);
          }

          if (!tile.text3.text.isEmpty()) {
               this.drawText(tile.text3, x, y, z);
          }

          if (!tile.text4.text.isEmpty()) {
               this.drawText(tile.text4, x, y, z);
          }

          if (!tile.text5.text.isEmpty()) {
               this.drawText(tile.text5, x, y, z);
          }

          if (!tile.text6.text.isEmpty()) {
               this.drawText(tile.text6, x, y, z);
          }

     }

     private void drawText(TileScripted.TextPlane text1, double x, double y, double z) {
          if (text1.textBlock == null || text1.textHasChanged) {
               text1.textBlock = new TextBlockClient(text1.text, 336, true, new Object[]{Minecraft.func_71410_x().player});
               text1.textHasChanged = false;
          }

          GlStateManager.func_179084_k();
          GlStateManager.enableLighting();
          GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
          GlStateManager.func_179094_E();
          GlStateManager.func_179137_b(x + 0.5D, y + 0.5D, z + 0.5D);
          GlStateManager.func_179114_b((float)text1.rotationY, 0.0F, 1.0F, 0.0F);
          GlStateManager.func_179114_b((float)text1.rotationX, 1.0F, 0.0F, 0.0F);
          GlStateManager.func_179114_b((float)text1.rotationZ, 0.0F, 0.0F, 1.0F);
          GlStateManager.func_179152_a(text1.scale, text1.scale, 1.0F);
          GlStateManager.func_179109_b(text1.offsetX, text1.offsetY, text1.offsetZ);
          float f1 = 0.6666667F;
          float f3 = 0.0133F * f1;
          GlStateManager.func_179109_b(0.0F, 0.5F, 0.01F);
          GlStateManager.func_179152_a(f3, -f3, f3);
          GlStateManager.func_187432_a(0.0F, 0.0F, -1.0F * f3);
          GlStateManager.func_179132_a(false);
          FontRenderer fontrenderer = this.func_147498_b();
          float lineOffset = 0.0F;
          if (text1.textBlock.lines.size() < 14) {
               lineOffset = (14.0F - (float)text1.textBlock.lines.size()) / 2.0F;
          }

          for(int i = 0; i < text1.textBlock.lines.size(); ++i) {
               String text = ((ITextComponent)text1.textBlock.lines.get(i)).func_150254_d();
               fontrenderer.func_78276_b(text, -fontrenderer.func_78256_a(text) / 2, (int)((double)(lineOffset + (float)i) * ((double)fontrenderer.field_78288_b - 0.3D)), 0);
          }

          GlStateManager.func_179132_a(true);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.func_179121_F();
     }

     private void renderItem(ItemStack item) {
          Minecraft.func_71410_x().getRenderItem().func_181564_a(item, TransformType.NONE);
     }

     private void renderBlock(TileScripted tile, Block b, IBlockState state) {
          GlStateManager.func_179094_E();
          this.func_147499_a(TextureMap.field_110575_b);
          GlStateManager.func_187401_a(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
          GlStateManager.func_179147_l();
          GlStateManager.func_179129_p();
          GlStateManager.func_179109_b(-0.5F, 0.0F, 0.5F);
          Minecraft.func_71410_x().func_175602_ab().func_175016_a(state, 1.0F);
          if (b.func_149653_t() && random.nextInt(12) == 1) {
               b.func_180655_c(state, tile.func_145831_w(), tile.func_174877_v(), random);
          }

          GlStateManager.func_179121_F();
     }

     private boolean overrideModel() {
          ItemStack held = Minecraft.func_71410_x().player.func_184614_ca();
          if (held == null) {
               return false;
          } else {
               return held.func_77973_b() == CustomItems.wand || held.func_77973_b() == CustomItems.scripter;
          }
     }
}
