package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.schematics.Schematic;

public class BlockCopyRenderer extends BlockRendererInterface {
     private static final ItemStack item;
     public static Schematic schematic;
     public static BlockPos pos;

     public void func_192841_a(TileEntity var1, double x, double y, double z, float var8, int blockDamage, float alpha) {
          TileCopy tile = (TileCopy)var1;
          GlStateManager.func_179094_E();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          RenderHelper.enableStandardItemLighting();
          GlStateManager.func_179084_k();
          GlStateManager.func_179137_b(x, y, z);
          this.drawSelectionBox(new BlockPos(tile.width, tile.height, tile.length));
          GlStateManager.translate(0.5F, 0.5F, 0.5F);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          GlStateManager.func_179114_b(180.0F, 0.0F, 1.0F, 0.0F);
          Minecraft.getMinecraft().getRenderItem().func_181564_a(item, TransformType.NONE);
          GlStateManager.func_179121_F();
     }

     public void drawSelectionBox(BlockPos pos) {
          GlStateManager.func_179090_x();
          GlStateManager.disableLighting();
          GlStateManager.func_179129_p();
          GlStateManager.func_179084_k();
          AxisAlignedBB bb = new AxisAlignedBB(BlockPos.field_177992_a, pos);
          GlStateManager.translate(0.001F, 0.001F, 0.001F);
          RenderGlobal.func_189697_a(bb, 1.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.func_179098_w();
          GlStateManager.enableLighting();
          GlStateManager.func_179089_o();
          GlStateManager.func_179084_k();
     }

     static {
          item = new ItemStack(CustomItems.copy);
          schematic = null;
          pos = null;
     }
}
