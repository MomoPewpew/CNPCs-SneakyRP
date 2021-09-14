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

	public void render(TileEntity var1, double x, double y, double z, float var8, int blockDamage, float alpha) {
		TileCopy tile = (TileCopy) var1;
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.disableBlend();
		GlStateManager.translate(x, y, z);
		this.drawSelectionBox(new BlockPos(tile.width, tile.height, tile.length));
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.NONE);
		GlStateManager.popMatrix();
	}

	public void drawSelectionBox(BlockPos pos) {
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		AxisAlignedBB bb = new AxisAlignedBB(BlockPos.ORIGIN, pos);
		GlStateManager.translate(0.001F, 0.001F, 0.001F);
		RenderGlobal.drawSelectionBoundingBox(bb, 1.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
	}

	static {
		item = new ItemStack(CustomItems.copy);
		schematic = null;
		pos = null;
	}
}
