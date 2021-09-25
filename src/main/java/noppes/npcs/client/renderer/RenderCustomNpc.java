package noppes.npcs.client.renderer;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.layer.LayerArms;
import noppes.npcs.client.layer.LayerBody;
import noppes.npcs.client.layer.LayerEyes;
import noppes.npcs.client.layer.LayerHead;
import noppes.npcs.client.layer.LayerHeadwear;
import noppes.npcs.client.layer.LayerLegs;
import noppes.npcs.client.layer.LayerNpcCloak;
import noppes.npcs.client.layer.LayerPreRender;
import noppes.npcs.client.model.ModelBipedAlt;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

import noppes.npcs.LogWriter;

public class RenderCustomNpc extends RenderNPCInterface<EntityCustomNpc> {
	private float partialTicks;
	private EntityLivingBase entity;
	private RenderLivingBase renderEntity;
	public ModelBiped npcmodel;
	// public boolean isRendering;
	// public boolean isRenderingModel;

	public RenderCustomNpc(ModelBiped model) {
		super(model, 0.5F);
		// this.isRendering = false;
		// this.isRenderingModel = false;
		this.npcmodel = (ModelBiped) this.mainModel;
		this.layerRenderers.add(new LayerEyes(this));
		this.layerRenderers.add(new LayerHeadwear(this));
		this.layerRenderers.add(new LayerHead(this));
		this.layerRenderers.add(new LayerArms(this));
		this.layerRenderers.add(new LayerLegs(this));
		this.layerRenderers.add(new LayerBody(this));
		this.layerRenderers.add(new LayerNpcCloak(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerCustomHead(this.npcmodel.bipedHead));
		LayerBipedArmor armor = new LayerBipedArmor(this);
		this.addLayer(armor);
		ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(0.5F), 1);
		ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(1.0F), 2);
	}

	// public void doRender(EntityNPCInterface npc, double d, double d1, double d2, float f, float f1) {
	// 	this.doRender((EntityCustomNpc)npc, d, d1, d2, f, f1);
	// }
	public void doRender(EntityCustomNpc npc, double d, double d1, double d2, float f, float partialTicks) {
		this.partialTicks = partialTicks;
		this.entity = npc.modelData.getEntity(npc);
		if (this.entity != null) {
			Render render = this.renderManager.getEntityRenderObject(this.entity);
			if (render instanceof RenderLivingBase) {
				this.renderEntity = (RenderLivingBase) render;
			} else {
				this.renderEntity = null;
				this.entity = null;
			}
		} else {
			this.renderEntity = null;
			List list = this.layerRenderers;
			Iterator var11 = list.iterator();

			while (var11.hasNext()) {
				LayerRenderer layer = (LayerRenderer) var11.next();
				if (layer instanceof LayerPreRender) {
					((LayerPreRender) layer).preRender(npc);
				}
			}
		}

		this.npcmodel.rightArmPose = this.getPose(npc, npc.getHeldItemMainhand());
		this.npcmodel.leftArmPose = this.getPose(npc, npc.getHeldItemOffhand());
		super.doRender(npc, d, d1, d2, f, partialTicks);
	}

	public ArmPose getPose(EntityCustomNpc npc, ItemStack item) {
		if (NoppesUtilServer.IsItemStackNull(item)) {
			return ArmPose.EMPTY;
		} else {
			if (npc.getItemInUseCount() > 0) {
				EnumAction enumaction = item.getItemUseAction();
				if (enumaction == EnumAction.BLOCK) {
					return ArmPose.BLOCK;
				}

				if (enumaction == EnumAction.BOW) {
					return ArmPose.BOW_AND_ARROW;
				}
			}

			return ArmPose.ITEM;
		}
	}

	// protected void renderModel(EntityNPCInterface npc, float par2, float par3, float par4, float par5, float par6, float par7) {
	// 	this.renderModel((EntityCustomNpc)npc, par2, par3, par4, par5, par6, par7);
	// }
	protected void renderModel(EntityCustomNpc npc, float par2, float par3, float par4, float par5, float par6, float par7) {
		if (this.renderEntity != null) {
			boolean flag = !npc.isInvisible();
			boolean flag1 = !flag && !npc.isInvisibleToPlayer(Minecraft.getMinecraft().player);
			if (!flag && !flag1) {
				return;
			}

			if (flag1) {
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(770, 771);
				GlStateManager.alphaFunc(516, 0.003921569F);
			}

			ModelBase model = this.renderEntity.getMainModel();
			if (PixelmonHelper.isPixelmon(this.entity)) {
				ModelBase pixModel = (ModelBase) PixelmonHelper.getModel(this.entity);
				if (pixModel != null) {
					model = pixModel;
					PixelmonHelper.setupModel(this.entity, pixModel);
				}
			}

			model.swingProgress = this.mainModel.swingProgress;
			model.isRiding = this.entity.isRiding() && this.entity.getRidingEntity() != null
					&& this.entity.getRidingEntity().shouldRiderSit();
			model.setLivingAnimations(this.entity, par2, par3, this.partialTicks);
			model.setRotationAngles(par2, par3, par4, par5, par6, par7, this.entity);
			model.isChild = this.entity.isChild();
			NPCRendererHelper.renderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model,
					this.getEntityTexture(npc));
			if (!npc.display.getOverlayTexture().isEmpty()) {
				GlStateManager.depthFunc(515);
				if (npc.textureGlowLocation == null) {
					npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
				}

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
				NPCRendererHelper.renderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model,
						npc.textureGlowLocation);
				GlStateManager.popMatrix();
				GlStateManager.enableLighting();
				GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
				GlStateManager.depthFunc(515);
				GlStateManager.disableBlend();
			}

			if (flag1) {
				GlStateManager.disableBlend();
				GlStateManager.alphaFunc(516, 0.1F);
				GlStateManager.popMatrix();
				GlStateManager.depthMask(true);
			}
		} else {
			super.renderModel(npc, par2, par3, par4, par5, par6, par7);
		}

	}

	protected void renderLayers(EntityCustomNpc entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
		if (this.entity != null && this.renderEntity != null) {
			NPCRendererHelper.drawLayers(this.entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
					headPitch, scaleIn, this.renderEntity);
		} else {
			super.renderLayers(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw,
					headPitch, scaleIn);
		}

	}

	protected void preRenderCallback(EntityCustomNpc npc, float f) {
		if (this.renderEntity != null) {
			this.renderColor(npc);
			int size = npc.display.getSize();
			if (this.entity instanceof EntityNPCInterface) {
				((EntityNPCInterface) this.entity).display.setSize(5);
			}

			NPCRendererHelper.preRenderCallback(this.entity, f, this.renderEntity);
			npc.display.setSize(size);
			GlStateManager.scale(0.2F * (float) npc.display.getSize(), 0.2F * (float) npc.display.getSize(),
					0.2F * (float) npc.display.getSize());
		} else {
			super.preRenderCallback((EntityNPCInterface) npc, f);
		}

	}

	protected float handleRotationFloat(EntityCustomNpc par1EntityLivingBase, float par2) {
		return this.renderEntity != null ? NPCRendererHelper.handleRotationFloat(this.entity, par2, this.renderEntity)
				: super.handleRotationFloat(par1EntityLivingBase, par2);
	}
}
