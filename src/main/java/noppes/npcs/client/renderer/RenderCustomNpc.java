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

public class RenderCustomNpc extends RenderNPCInterface {
     private float partialTicks;
     private EntityLivingBase entity;
     private RenderLivingBase renderEntity;
     public ModelBiped npcmodel;

     public RenderCustomNpc(ModelBiped model) {
          super(model, 0.5F);
          this.npcmodel = (ModelBiped)this.field_77045_g;
          this.field_177097_h.add(new LayerEyes(this));
          this.field_177097_h.add(new LayerHeadwear(this));
          this.field_177097_h.add(new LayerHead(this));
          this.field_177097_h.add(new LayerArms(this));
          this.field_177097_h.add(new LayerLegs(this));
          this.field_177097_h.add(new LayerBody(this));
          this.field_177097_h.add(new LayerNpcCloak(this));
          this.func_177094_a(new LayerHeldItem(this));
          this.func_177094_a(new LayerCustomHead(this.npcmodel.field_78116_c));
          LayerBipedArmor armor = new LayerBipedArmor(this);
          this.func_177094_a(armor);
          ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(0.5F), 1);
          ObfuscationReflectionHelper.setPrivateValue(LayerArmorBase.class, armor, new ModelBipedAlt(1.0F), 2);
     }

     public void doRender(EntityCustomNpc npc, double d, double d1, double d2, float f, float partialTicks) {
          this.partialTicks = partialTicks;
          this.entity = npc.modelData.getEntity(npc);
          if (this.entity != null) {
               Render render = this.field_76990_c.func_78713_a(this.entity);
               if (render instanceof RenderLivingBase) {
                    this.renderEntity = (RenderLivingBase)render;
               } else {
                    this.renderEntity = null;
                    this.entity = null;
               }
          } else {
               this.renderEntity = null;
               List list = this.field_177097_h;
               Iterator var11 = list.iterator();

               while(var11.hasNext()) {
                    LayerRenderer layer = (LayerRenderer)var11.next();
                    if (layer instanceof LayerPreRender) {
                         ((LayerPreRender)layer).preRender(npc);
                    }
               }
          }

          this.npcmodel.field_187076_m = this.getPose(npc, npc.func_184614_ca());
          this.npcmodel.field_187075_l = this.getPose(npc, npc.func_184592_cb());
          super.doRender(npc, d, d1, d2, f, partialTicks);
     }

     public ArmPose getPose(EntityCustomNpc npc, ItemStack item) {
          if (NoppesUtilServer.IsItemStackNull(item)) {
               return ArmPose.EMPTY;
          } else {
               if (npc.func_184605_cv() > 0) {
                    EnumAction enumaction = item.func_77975_n();
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

     protected void renderModel(EntityCustomNpc npc, float par2, float par3, float par4, float par5, float par6, float par7) {
          if (this.renderEntity != null) {
               boolean flag = !npc.func_82150_aj();
               boolean flag1 = !flag && !npc.func_98034_c(Minecraft.func_71410_x().player);
               if (!flag && !flag1) {
                    return;
               }

               if (flag1) {
                    GlStateManager.func_179094_E();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                    GlStateManager.func_179132_a(false);
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179112_b(770, 771);
                    GlStateManager.func_179092_a(516, 0.003921569F);
               }

               ModelBase model = this.renderEntity.field_77045_g;
               if (PixelmonHelper.isPixelmon(this.entity)) {
                    ModelBase pixModel = (ModelBase)PixelmonHelper.getModel(this.entity);
                    if (pixModel != null) {
                         model = pixModel;
                         PixelmonHelper.setupModel(this.entity, pixModel);
                    }
               }

               model.field_78095_p = this.field_77045_g.field_78095_p;
               model.field_78093_q = this.entity.func_184218_aH() && this.entity.func_184187_bx() != null && this.entity.func_184187_bx().shouldRiderSit();
               model.func_78086_a(this.entity, par2, par3, this.partialTicks);
               model.func_78087_a(par2, par3, par4, par5, par6, par7, this.entity);
               model.field_78091_s = this.entity.func_70631_g_();
               NPCRendererHelper.renderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model, this.getEntityTexture(npc));
               if (!npc.display.getOverlayTexture().isEmpty()) {
                    GlStateManager.func_179143_c(515);
                    if (npc.textureGlowLocation == null) {
                         npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
                    }

                    float f1 = 1.0F;
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179112_b(1, 1);
                    GlStateManager.disableLighting();
                    if (npc.func_82150_aj()) {
                         GlStateManager.func_179132_a(false);
                    } else {
                         GlStateManager.func_179132_a(true);
                    }

                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179152_a(1.001F, 1.001F, 1.001F);
                    NPCRendererHelper.renderModel(this.entity, par2, par3, par4, par5, par6, par7, this.renderEntity, model, npc.textureGlowLocation);
                    GlStateManager.func_179121_F();
                    GlStateManager.enableLighting();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, f1);
                    GlStateManager.func_179143_c(515);
                    GlStateManager.func_179084_k();
               }

               if (flag1) {
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179092_a(516, 0.1F);
                    GlStateManager.func_179121_F();
                    GlStateManager.func_179132_a(true);
               }
          } else {
               super.renderModel(npc, par2, par3, par4, par5, par6, par7);
          }

     }

     protected void renderLayers(EntityCustomNpc entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
          if (this.entity != null && this.renderEntity != null) {
               NPCRendererHelper.drawLayers(this.entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn, this.renderEntity);
          } else {
               super.func_177093_a(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
          }

     }

     protected void preRenderCallback(EntityCustomNpc npc, float f) {
          if (this.renderEntity != null) {
               this.renderColor(npc);
               int size = npc.display.getSize();
               if (this.entity instanceof EntityNPCInterface) {
                    ((EntityNPCInterface)this.entity).display.setSize(5);
               }

               NPCRendererHelper.preRenderCallback(this.entity, f, this.renderEntity);
               npc.display.setSize(size);
               GlStateManager.func_179152_a(0.2F * (float)npc.display.getSize(), 0.2F * (float)npc.display.getSize(), 0.2F * (float)npc.display.getSize());
          } else {
               super.preRenderCallback(npc, f);
          }

     }

     protected float handleRotationFloat(EntityCustomNpc par1EntityLivingBase, float par2) {
          return this.renderEntity != null ? NPCRendererHelper.handleRotationFloat(this.entity, par2, this.renderEntity) : super.handleRotationFloat(par1EntityLivingBase, par2);
     }
}
