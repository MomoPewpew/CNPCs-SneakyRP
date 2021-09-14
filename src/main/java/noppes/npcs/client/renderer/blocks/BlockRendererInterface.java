package noppes.npcs.client.renderer.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public abstract class BlockRendererInterface extends TileEntitySpecialRenderer {
	protected static final ResourceLocation Stone = new ResourceLocation("customnpcs", "textures/cache/stone.png");
	protected static final ResourceLocation Iron = new ResourceLocation("customnpcs", "textures/cache/iron_block.png");
	protected static final ResourceLocation Gold = new ResourceLocation("customnpcs", "textures/cache/gold_block.png");
	protected static final ResourceLocation Diamond = new ResourceLocation("customnpcs",
			"textures/cache/diamond_block.png");
	protected static final ResourceLocation PlanksOak = new ResourceLocation("customnpcs",
			"textures/cache/planks_oak.png");
	protected static final ResourceLocation PlanksBigOak = new ResourceLocation("customnpcs",
			"textures/cache/planks_big_oak.png");
	protected static final ResourceLocation PlanksSpruce = new ResourceLocation("customnpcs",
			"textures/cache/planks_spruce.png");
	protected static final ResourceLocation PlanksBirch = new ResourceLocation("customnpcs",
			"textures/cache/planks_birch.png");
	protected static final ResourceLocation PlanksAcacia = new ResourceLocation("customnpcs",
			"textures/cache/planks_acacia.png");
	protected static final ResourceLocation PlanksJungle = new ResourceLocation("customnpcs",
			"textures/cache/planks_jungle.png");
	protected static final ResourceLocation Steel = new ResourceLocation("customnpcs", "textures/models/Steel.png");
	public static float[][] colorTable = new float[][] { { 1.0F, 1.0F, 1.0F }, { 0.95F, 0.7F, 0.2F },
			{ 0.9F, 0.5F, 0.85F }, { 0.6F, 0.7F, 0.95F }, { 0.9F, 0.9F, 0.2F }, { 0.5F, 0.8F, 0.1F },
			{ 0.95F, 0.7F, 0.8F }, { 0.3F, 0.3F, 0.3F }, { 0.6F, 0.6F, 0.6F }, { 0.3F, 0.6F, 0.7F },
			{ 0.7F, 0.4F, 0.9F }, { 0.2F, 0.4F, 0.8F }, { 0.5F, 0.4F, 0.3F }, { 0.4F, 0.5F, 0.2F },
			{ 0.8F, 0.3F, 0.3F }, { 0.1F, 0.1F, 0.1F } };

	public boolean playerTooFar(TileEntity tile) {
		Minecraft mc = Minecraft.getMinecraft();
		double d6 = mc.getRenderViewEntity().posX - (double) tile.getPos().getX();
		double d7 = mc.getRenderViewEntity().posY - (double) tile.getPos().getY();
		double d8 = mc.getRenderViewEntity().posZ - (double) tile.getPos().getZ();
		return d6 * d6 + d7 * d7 + d8 * d8 > (double) (this.specialRenderDistance() * this.specialRenderDistance());
	}

	public int specialRenderDistance() {
		return 20;
	}

	public void setWoodTexture(int meta) {
		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		if (meta == 1) {
			manager.bindTexture(PlanksSpruce);
		} else if (meta == 2) {
			manager.bindTexture(PlanksBirch);
		} else if (meta == 3) {
			manager.bindTexture(PlanksJungle);
		} else if (meta == 4) {
			manager.bindTexture(PlanksAcacia);
		} else if (meta == 5) {
			manager.bindTexture(PlanksBigOak);
		} else {
			manager.bindTexture(PlanksOak);
		}

	}

	public static void setMaterialTexture(int meta) {
		TextureManager manager = Minecraft.getMinecraft().getTextureManager();
		if (meta == 1) {
			manager.bindTexture(Stone);
		} else if (meta == 2) {
			manager.bindTexture(Iron);
		} else if (meta == 3) {
			manager.bindTexture(Gold);
		} else if (meta == 4) {
			manager.bindTexture(Diamond);
		} else {
			manager.bindTexture(PlanksOak);
		}

	}
}
