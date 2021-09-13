package noppes.npcs.client.renderer.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.BlockNpcDoorInterface;
import noppes.npcs.blocks.tiles.TileDoor;

public class BlockDoorRenderer extends BlockRendererInterface {
     private static Random random = new Random();

     public void func_192841_a(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
          TileDoor tile = (TileDoor)te;
          IBlockState original = CustomItems.scriptedDoor.func_176203_a(tile.func_145832_p());
          BlockPos lowerPos = tile.func_174877_v();
          if (original.func_177229_b(BlockDoor.field_176523_O) == EnumDoorHalf.UPPER) {
               lowerPos = tile.func_174877_v().func_177977_b();
          }

          BlockPos upperPos = lowerPos.func_177984_a();
          TileDoor lowerTile = (TileDoor)this.func_178459_a().func_175625_s(lowerPos);
          TileDoor upperTile = (TileDoor)this.func_178459_a().func_175625_s(upperPos);
          if (lowerTile != null && upperTile != null) {
               IBlockState lowerState = CustomItems.scriptedDoor.func_176203_a(lowerTile.func_145832_p());
               IBlockState upperState = CustomItems.scriptedDoor.func_176203_a(upperTile.func_145832_p());
               int meta = BlockNpcDoorInterface.func_176515_e(this.func_178459_a(), tile.func_174877_v());
               Block b = lowerTile.blockModel;
               if (this.overrideModel()) {
                    b = CustomItems.scriptedDoor;
               }

               IBlockState state = b.func_176203_a(meta);
               state = state.func_177226_a(BlockDoor.field_176523_O, original.func_177229_b(BlockDoor.field_176523_O));
               state = state.func_177226_a(BlockDoor.field_176520_a, lowerState.func_177229_b(BlockDoor.field_176520_a));
               state = state.func_177226_a(BlockDoor.field_176519_b, lowerState.func_177229_b(BlockDoor.field_176519_b));
               state = state.func_177226_a(BlockDoor.field_176521_M, upperState.func_177229_b(BlockDoor.field_176521_M));
               state = state.func_177226_a(BlockDoor.field_176522_N, upperState.func_177229_b(BlockDoor.field_176522_N));
               GlStateManager.func_179094_E();
               RenderHelper.func_74519_b();
               GlStateManager.func_179141_d();
               GlStateManager.func_179084_k();
               GlStateManager.func_179137_b(x + 0.5D, y, z + 0.5D);
               GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
               this.renderBlock(tile, b, state);
               GlStateManager.func_179118_c();
               GlStateManager.func_179121_F();
          }
     }

     private void renderBlock(TileDoor tile, Block b, IBlockState state) {
          this.func_147499_a(TextureMap.field_110575_b);
          GlStateManager.func_179109_b(-0.5F, 0.0F, 0.5F);
          BlockRendererDispatcher dispatcher = Minecraft.func_71410_x().func_175602_ab();
          IBakedModel ibakedmodel = dispatcher.func_175023_a().func_178125_b(state);
          if (ibakedmodel == null) {
               dispatcher.func_175016_a(state, 1.0F);
          } else {
               dispatcher.func_175019_b().func_178266_a(ibakedmodel, state, 1.0F, true);
          }

     }

     private boolean overrideModel() {
          ItemStack held = Minecraft.func_71410_x().field_71439_g.func_184614_ca();
          if (held == null) {
               return false;
          } else {
               return held.func_77973_b() == CustomItems.wand || held.func_77973_b() == CustomItems.scripter || held.func_77973_b() == CustomItems.scriptedDoorTool;
          }
     }
}
