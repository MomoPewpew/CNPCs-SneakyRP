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

	public void render(TileEntity te, double x, double y, double z, float partialTicks, int blockDamage, float alpha) {
		TileScripted tile = (TileScripted) te;
		GlStateManager.pushMatrix();
		GlStateManager.disableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.translate(x + 0.5D, y, z + 0.5D);
		if (this.overrideModel()) {
			GlStateManager.translate(0.0D, 0.5D, 0.0D);
			this.renderItem(new ItemStack(CustomItems.scripted));
		} else {
			GlStateManager.rotate((float) tile.rotationY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate((float) tile.rotationX, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate((float) tile.rotationZ, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(tile.scaleX, tile.scaleY, tile.scaleZ);
			Block b = tile.blockModel;
			if (b != null && b != Blocks.AIR) {
				if (b == CustomItems.scripted) {
					GlStateManager.translate(0.0D, 0.5D, 0.0D);
					this.renderItem(tile.itemModel);
				} else {
					IBlockState state = b.getStateFromMeta(tile.itemModel.getItemDamage());
					this.renderBlock(tile, b, state);
					if (b.hasTileEntity(state) && !tile.renderTileErrored) {
						try {
							if (tile.renderTile == null) {
								TileEntity entity = b.createTileEntity(this.getWorld(), state);
								entity.setPos(tile.getPos());
								entity.setWorld(this.getWorld());
								ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity,
										tile.itemModel.getItemDamage(), 5);
								ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, entity, b, 6);
								tile.renderTile = entity;
								if (entity instanceof ITickable) {
									tile.renderTileUpdate = (ITickable) entity;
								}
							}

							TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.instance
									.getRenderer(tile.renderTile);
							if (renderer != null) {
								renderer.render(tile.renderTile, -0.5D, 0.0D, -0.5D, partialTicks, blockDamage, alpha);
							} else {
								tile.renderTileErrored = true;
							}
						} catch (Exception var15) {
							tile.renderTileErrored = true;
						}
					}
				}
			} else {
				GlStateManager.translate(0.0D, 0.5D, 0.0D);
				this.renderItem(tile.itemModel);
			}
		}

		GlStateManager.popMatrix();
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
			text1.textBlock = new TextBlockClient(text1.text, 336, true,
					new Object[] { Minecraft.getMinecraft().player });
			text1.textHasChanged = false;
		}

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
		GlStateManager.rotate((float) text1.rotationY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float) text1.rotationX, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((float) text1.rotationZ, 0.0F, 0.0F, 1.0F);
		GlStateManager.scale(text1.scale, text1.scale, 1.0F);
		GlStateManager.translate(text1.offsetX, text1.offsetY, text1.offsetZ);
		float f1 = 0.6666667F;
		float f3 = 0.0133F * f1;
		GlStateManager.translate(0.0F, 0.5F, 0.01F);
		GlStateManager.scale(f3, -f3, f3);
		GlStateManager.glNormal3f(0.0F, 0.0F, -1.0F * f3);
		GlStateManager.depthMask(false);
		FontRenderer fontrenderer = this.getFontRenderer();
		float lineOffset = 0.0F;
		if (text1.textBlock.lines.size() < 14) {
			lineOffset = (14.0F - (float) text1.textBlock.lines.size()) / 2.0F;
		}

		for (int i = 0; i < text1.textBlock.lines.size(); ++i) {
			String text = ((ITextComponent) text1.textBlock.lines.get(i)).getFormattedText();
			fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2,
					(int) ((double) (lineOffset + (float) i) * ((double) fontrenderer.FONT_HEIGHT - 0.3D)), 0);
		}

		GlStateManager.depthMask(true);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private void renderItem(ItemStack item) {
		Minecraft.getMinecraft().getRenderItem().renderItem(item, TransformType.NONE);
	}

	private void renderBlock(TileScripted tile, Block b, IBlockState state) {
		GlStateManager.pushMatrix();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		GlStateManager.translate(-0.5F, 0.0F, 0.5F);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, 1.0F);
		if (b.getTickRandomly() && random.nextInt(12) == 1) {
			b.randomDisplayTick(state, tile.getWorld(), tile.getPos(), random);
		}

		GlStateManager.popMatrix();
	}

	private boolean overrideModel() {
		ItemStack held = Minecraft.getMinecraft().player.getHeldItemMainhand();
		if (held == null) {
			return false;
		} else {
			return held.getItem() == CustomItems.wand || held.getItem() == CustomItems.scripter;
		}
	}
}
