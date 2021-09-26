package noppes.npcs.client.renderer;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.io.File;
import java.security.MessageDigest;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.ImageDownloadAlt;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;

import noppes.npcs.LogWriter;

public class RenderNPCInterface<T extends EntityNPCInterface> extends RenderLiving<T> {
	public static int LastTextureTick;

	public RenderNPCInterface(ModelBase model, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), model, f);
	}

	public void renderName(T npc, double d, double d1, double d2) {
		if (npc != null && this.canRenderName(npc) && this.renderManager.renderViewEntity != null) {
			double d0 = npc.getDistanceSq(this.renderManager.renderViewEntity);
			if (d0 <= 512.0D) {
				float scale;
				if (npc.messages != null) {
					scale = npc.baseHeight / 5.0F * (float) npc.display.getSize();
					float offset = npc.height * (1.2F
							+ (!npc.display.showName() ? 0.0F : (npc.display.getTitle().isEmpty() ? 0.15F : 0.25F)));
					npc.messages.renderMessages(d, d1 + (double) offset, d2, 0.666667F * scale,
							npc.isInRange(this.renderManager.renderViewEntity, 4.0D));
				}

				scale = npc.baseHeight / 5.0F * (float) npc.display.getSize();
				if (npc.display.showName()) {
					this.renderLivingLabel(npc, (float) d, (float) d1 + npc.height - 0.06F * scale, (float) d2, 64,
							npc.getName(), npc.display.getTitle());
				}

			}
		}
	}

	public void doRenderShadowAndFire(Entity par1Entity, double par2, double par4, double par6, float par8,
			float par9) {
		EntityNPCInterface npc = (EntityNPCInterface) par1Entity;
		this.shadowSize = npc.width;
		if (!npc.isKilled()) {
			super.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
		}

	}

	protected void renderLivingLabel(EntityNPCInterface npc, float d, float d1, float d2, int i, String name,
			String title) {
		FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
		float f1 = npc.baseHeight / 5.0F * (float) npc.display.getSize();
		float f2 = 0.01666667F * f1;
		GlStateManager.pushMatrix();
		GlStateManager.translate(d, d1, d2);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		float height = f1 / 6.5F * 2.0F;
		int color = npc.getFaction().color;
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.translate(0.0F, height, 0.0F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE,
				DestFactor.ZERO);
		if (!title.isEmpty()) {
			title = "<" + title + ">";
			float f3 = 0.01666667F * f1 * 0.6F;
			GlStateManager.translate(0.0F, -f1 / 6.5F * 0.4F, 0.0F);
			GlStateManager.scale(-f3, -f3, f3);
			fontrenderer.drawString(title, -fontrenderer.getStringWidth(title) / 2, 0, color);
			GlStateManager.scale(1.0F / -f3, 1.0F / -f3, 1.0F / f3);
			GlStateManager.translate(0.0F, f1 / 6.5F * 0.85F, 0.0F);
		}

		GlStateManager.scale(-f2, -f2, f2);
		if (npc.isInRange(this.renderManager.renderViewEntity, 4.0D)) {
			GlStateManager.disableDepth();
			fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, 0, color + 1426063360);
			GlStateManager.enableDepth();
		}

		GlStateManager.depthMask(true);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		fontrenderer.drawString(name, -fontrenderer.getStringWidth(name) / 2, 0, color);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	protected void renderColor(EntityNPCInterface npc) {
		if (npc.hurtTime <= 0 && npc.deathTime <= 0) {
			float red = (float) (npc.display.getTint() >> 16 & 255) / 255.0F;
			float green = (float) (npc.display.getTint() >> 8 & 255) / 255.0F;
			float blue = (float) (npc.display.getTint() & 255) / 255.0F;
			GlStateManager.color(red, green, blue, 1.0F);
		}

	}

	private void renderLiving(EntityNPCInterface npc, double d, double d1, double d2, float xoffset, float yoffset,
			float zoffset) {
	}

	protected void applyRotations(T npc, float f, float f1, float f2) {
		if (npc.isEntityAlive() && npc.isPlayerSleeping()) {
			GlStateManager.rotate((float) npc.ais.orientation, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(this.getDeathMaxRotation(npc), 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
		} else if (npc.isEntityAlive() && npc.currentAnimation == 7) {
			GlStateManager.rotate(270.0F - f1, 0.0F, 1.0F, 0.0F);
			float scale = (float) ((EntityCustomNpc) npc).display.getSize() / 5.0F;
			GlStateManager.translate(-scale + ((EntityCustomNpc) npc).modelData.getLegsY() * scale, 0.14F, 0.0F);
			GlStateManager.rotate(270.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.applyRotations(npc, f, f1, f2);
		}

	}

	protected void preRenderCallback(EntityNPCInterface npc, float f) {
		this.renderColor(npc);
		int size = npc.display.getSize();
		GlStateManager.scale(npc.scaleX / 5.0F * (float) size, npc.scaleY / 5.0F * (float) size,
				npc.scaleZ / 5.0F * (float) size);
	}

	public void doRender(T npc, double d, double d1, double d2, float f, float f1) {
		// if(this instanceof RenderCustomNpc) {
		// 	RenderCustomNpc aaaa = ((RenderCustomNpc)this);
		// 	if(!aaaa.isRendering) {
		// 		aaaa.isRendering = true;
		// 		aaaa.doRender((EntityCustomNpc)npc, d, d1, d2, f, f1);
		// 		aaaa.isRendering = false;
		// 		return;
		// 	}
		// }
		if (!npc.isKilled() || !npc.stats.hideKilledBody || npc.deathTime <= 20) {
			if ((npc.display.getBossbar() == 1 || npc.display.getBossbar() == 2 && npc.isAttacking()) && !npc.isKilled()
					&& npc.deathTime <= 20 && npc.canSee(Minecraft.getMinecraft().player)) {
			}

			if (npc.ais.getStandingType() == 3 && !npc.isWalking() && !npc.isInteracting()) {
				npc.prevRenderYawOffset = npc.renderYawOffset = (float) npc.ais.orientation;
			}

			super.doRender(npc, d, d1, d2, f, f1);
		}
	}

	protected void renderModel(T npc, float par2, float par3, float par4, float par5, float par6, float par7) {
		// Float o = null;
		// if(this instanceof RenderCustomNpc) {
		// 	RenderCustomNpc aaaa = ((RenderCustomNpc)this);
		// 	if(!aaaa.isRenderingModel) {
		// 		aaaa.isRenderingModel = true;
		// 		aaaa.renderModel((EntityCustomNpc)npc, par2, par3, par4, par5, par6, par7);
		// 		aaaa.isRenderingModel = false;
		// 		return;
		// 	}
		// }
		super.renderModel(npc, par2, par3, par4, par5, par6, par7);
		if (!npc.display.getOverlayTexture().isEmpty()) {
			GlStateManager.depthFunc(515);
			if (npc.textureGlowLocation == null) {
				npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
			}

			this.bindTexture(npc.textureGlowLocation);
			float f1 = 1.0F;
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(1, 1);
			GlStateManager.disableLighting();
			if (npc.isInvisible()) {
				GlStateManager.depthMask(false);
			} else {
				GlStateManager.depthMask(true);
			}

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.001F, 1.001F, 1.001F);
			this.mainModel.render(npc, par2, par3, par4, par5, par6, par7);
			GlStateManager.popMatrix();
			GlStateManager.enableLighting();
			GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
			GlStateManager.depthFunc(515);
			GlStateManager.disableBlend();
		}

	}

	protected float handleRotationFloat(T npc, float par2) {
		return !npc.isKilled() && npc.display.getHasLivingAnimation() ? super.handleRotationFloat(npc, par2) : 0.0F;
	}

	protected void renderLivingAt(T npc, double d, double d1, double d2) {
		this.shadowSize = (float) npc.display.getSize() / 10.0F;
		float xOffset = 0.0F;
		float yOffset = npc.currentAnimation == 0 ? npc.ais.bodyOffsetY / 10.0F - 0.5F : 0.0F;
		float zOffset = 0.0F;
		if (npc.isEntityAlive()) {
			if (npc.isPlayerSleeping()) {
				xOffset = (float) (-Math.cos(Math.toRadians((double) (180 - npc.ais.orientation))));
				zOffset = (float) (-Math.sin(Math.toRadians((double) npc.ais.orientation)));
				yOffset += 0.14F;
			} else if (npc.currentAnimation == 1 || npc.isRiding()) {
				yOffset -= 0.5F - ((EntityCustomNpc) npc).modelData.getLegsY() * 0.8F;
			}
		}

		xOffset = xOffset / 5.0F * (float) npc.display.getSize();
		yOffset = yOffset / 5.0F * (float) npc.display.getSize();
		zOffset = zOffset / 5.0F * (float) npc.display.getSize();
		super.renderLivingAt(npc, d + (double) xOffset, d1 + (double) yOffset, d2 + (double) zOffset);
	}

	private void loadSkin(File file, ResourceLocation resource, String par1Str) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		if (texturemanager.getTexture(resource) == null) {
			ITextureObject object = new ImageDownloadAlt(file, par1Str, DefaultPlayerSkin.getDefaultSkinLegacy(),
					new ImageBufferDownloadAlt());
			texturemanager.loadTexture(resource, object);
		}
	}

	// @Override
	protected ResourceLocation getEntityTexture(T npc) {
		if (((EntityNPCInterface) npc).textureLocation == null) {
			if (((EntityNPCInterface) npc).display.skinType == 0) {
				((EntityNPCInterface) npc).textureLocation = new ResourceLocation(((EntityNPCInterface) npc).display.getSkinTexture());
			} else {
				if (LastTextureTick < 5) {
					return DefaultPlayerSkin.getDefaultSkinLegacy();
				}

				if (((EntityNPCInterface) npc).display.skinType == 1 && ((EntityNPCInterface) npc).display.playerProfile != null) {
					Minecraft minecraft = Minecraft.getMinecraft();
					Map map = minecraft.getSkinManager().loadSkinFromCache(((EntityNPCInterface) npc).display.playerProfile);
					if (map.containsKey(Type.SKIN)) {
						((EntityNPCInterface) npc).textureLocation = minecraft.getSkinManager()
								.loadSkin((MinecraftProfileTexture) map.get(Type.SKIN), Type.SKIN);
					}
				} else if (((EntityNPCInterface) npc).display.skinType == 2) {
					try {
						MessageDigest digest = MessageDigest.getInstance("MD5");
						byte[] hash = digest.digest(((EntityNPCInterface) npc).display.getSkinUrl().getBytes("UTF-8"));
						StringBuilder sb = new StringBuilder(2 * hash.length);
						byte[] var5 = hash;
						int var6 = hash.length;

						for (int var7 = 0; var7 < var6; ++var7) {
							byte b = var5[var7];
							sb.append(String.format("%02x", b & 255));
						}

						((EntityNPCInterface) npc).textureLocation = new ResourceLocation("skins/" + sb.toString());
						this.loadSkin((File) null, ((EntityNPCInterface) npc).textureLocation, ((EntityNPCInterface) npc).display.getSkinUrl());
					} catch (Exception var9) {
					}
				}
			}
		}

		return ((EntityNPCInterface) npc).textureLocation == null ? DefaultPlayerSkin.getDefaultSkinLegacy() : ((EntityNPCInterface) npc).textureLocation;
	}
}
