package noppes.npcs.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.client.model.animation.AniBow;
import noppes.npcs.client.model.animation.AniCrawling;
import noppes.npcs.client.model.animation.AniDancing;
import noppes.npcs.client.model.animation.AniHug;
import noppes.npcs.client.model.animation.AniNo;
import noppes.npcs.client.model.animation.AniPoint;
import noppes.npcs.client.model.animation.AniWaving;
import noppes.npcs.client.model.animation.AniYes;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.roles.JobPuppet;

public class ModelPlayerAlt extends ModelPlayer {
     private ModelRenderer body;
     private ModelRenderer head;
     private Map map = new HashMap();

     public ModelPlayerAlt(float scale, boolean arms) {
          super(scale, arms);
          this.head = new ModelScaleRenderer(this, 24, 0, EnumParts.HEAD);
          this.head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, scale);
          this.body = new ModelScaleRenderer(this, 0, 0, EnumParts.BODY);
          this.body.setTextureSize(64, 32);
          this.body.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, scale);
          ObfuscationReflectionHelper.setPrivateValue(ModelPlayer.class, this, this.head, 6);
          ObfuscationReflectionHelper.setPrivateValue(ModelPlayer.class, this, this.body, 5);
          this.bipedLeftArm = this.createScale(this.bipedLeftArm, EnumParts.ARM_LEFT);
          this.bipedRightArm = this.createScale(this.bipedRightArm, EnumParts.ARM_RIGHT);
          this.bipedLeftArmwear = this.createScale(this.bipedLeftArmwear, EnumParts.ARM_LEFT);
          this.bipedRightArmwear = this.createScale(this.bipedRightArmwear, EnumParts.ARM_RIGHT);
          this.bipedLeftLeg = this.createScale(this.bipedLeftLeg, EnumParts.LEG_LEFT);
          this.bipedRightLeg = this.createScale(this.bipedRightLeg, EnumParts.LEG_RIGHT);
          this.bipedLeftLegwear = this.createScale(this.bipedLeftLegwear, EnumParts.LEG_LEFT);
          this.bipedRightLegwear = this.createScale(this.bipedRightLegwear, EnumParts.LEG_RIGHT);
          this.bipedHead = this.createScale(this.bipedHead, EnumParts.HEAD);
          this.bipedHeadwear = this.createScale(this.bipedHeadwear, EnumParts.HEAD);
          this.bipedBody = this.createScale(this.bipedBody, EnumParts.BODY);
          this.bipedBodyWear = this.createScale(this.bipedBodyWear, EnumParts.BODY);
     }

     private ModelScaleRenderer createScale(ModelRenderer renderer, EnumParts part) {
          int textureX = (Integer)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, renderer, 2);
          int textureY = (Integer)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, renderer, 3);
          ModelScaleRenderer model = new ModelScaleRenderer(this, textureX, textureY, part);
          model.textureHeight = renderer.textureHeight;
          model.textureWidth = renderer.textureWidth;
          if (renderer.childModels != null) {
               model.childModels = new ArrayList(renderer.childModels);
          }

          model.cubeList = new ArrayList(renderer.cubeList);
          copyModelAngles(renderer, model);
          List list = (List)this.map.get(part);
          if (list == null) {
               this.map.put(part, list = new ArrayList());
          }

          ((List)list).add(model);
          return model;
     }

     public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
          EntityCustomNpc player = (EntityCustomNpc)entity;
          ModelData playerdata = player.modelData;
          Iterator var10 = this.map.keySet().iterator();

          while(var10.hasNext()) {
               EnumParts part = (EnumParts)var10.next();
               ModelPartConfig config = playerdata.getPartConfig(part);

               ModelScaleRenderer model;
               for(Iterator var13 = ((List)this.map.get(part)).iterator(); var13.hasNext(); model.config = config) {
                    model = (ModelScaleRenderer)var13.next();
               }
          }

          if (!this.isRiding) {
               this.isRiding = player.currentAnimation == 1;
          }

          if (this.isSneak && (player.currentAnimation == 7 || player.isPlayerSleeping())) {
               this.isSneak = false;
          }

          if (player.currentAnimation == 6) {
               this.rightArmPose = ArmPose.BOW_AND_ARROW;
          }

          this.isSneak = player.isSneaking();
          this.bipedBody.rotationPointX = this.bipedBody.rotationPointY = this.bipedBody.rotationPointZ = 0.0F;
          this.bipedBody.rotateAngleX = this.bipedBody.rotateAngleY = this.bipedBody.rotateAngleZ = 0.0F;
          this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX = 0.0F;
          this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ = 0.0F;
          this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX = 0.0F;
          this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY = 0.0F;
          this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ = 0.0F;
          this.bipedLeftLeg.rotateAngleX = 0.0F;
          this.bipedLeftLeg.rotateAngleY = 0.0F;
          this.bipedLeftLeg.rotateAngleZ = 0.0F;
          this.bipedRightLeg.rotateAngleX = 0.0F;
          this.bipedRightLeg.rotateAngleY = 0.0F;
          this.bipedRightLeg.rotateAngleZ = 0.0F;
          this.bipedLeftArm.rotationPointX = 0.0F;
          this.bipedLeftArm.rotationPointY = 2.0F;
          this.bipedLeftArm.rotationPointZ = 0.0F;
          this.bipedRightArm.rotationPointX = 0.0F;
          this.bipedRightArm.rotationPointY = 2.0F;
          this.bipedRightArm.rotationPointZ = 0.0F;

          try {
               super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
          } catch (Exception var15) {
          }

          if (player.isPlayerSleeping()) {
               if (this.bipedHead.rotateAngleX < 0.0F) {
                    this.bipedHead.rotateAngleX = 0.0F;
                    this.bipedHeadwear.rotateAngleX = 0.0F;
               }
          } else if (player.currentAnimation == 9) {
               this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX = 0.7F;
          } else if (player.currentAnimation == 3) {
               AniHug.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 7) {
               AniCrawling.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 10) {
               AniWaving.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 5) {
               AniDancing.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 11) {
               AniBow.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 13) {
               AniYes.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 12) {
               AniNo.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (player.currentAnimation == 8) {
               AniPoint.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity, this);
          } else if (this.isSneak) {
               this.bipedBody.rotateAngleX = 0.5F / playerdata.getPartConfig(EnumParts.BODY).scaleY;
          }

          if (player.advanced.job == 9) {
               JobPuppet job = (JobPuppet)player.jobInterface;
               if (job.isActive()) {
                    float pi = 3.1415927F;
                    float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
                    if (!job.head.disabled) {
                         this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX = job.getRotationX(job.head, job.head2, partialTicks) * pi;
                         this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY = job.getRotationY(job.head, job.head2, partialTicks) * pi;
                         this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ = job.getRotationZ(job.head, job.head2, partialTicks) * pi;
                    }

                    if (!job.body.disabled) {
                         this.bipedBody.rotateAngleX = job.getRotationX(job.body, job.body2, partialTicks) * pi;
                         this.bipedBody.rotateAngleY = job.getRotationY(job.body, job.body2, partialTicks) * pi;
                         this.bipedBody.rotateAngleZ = job.getRotationZ(job.body, job.body2, partialTicks) * pi;
                    }

                    ModelRenderer var10000;
                    if (!job.larm.disabled) {
                         this.bipedLeftArm.rotateAngleX = job.getRotationX(job.larm, job.larm2, partialTicks) * pi;
                         this.bipedLeftArm.rotateAngleY = job.getRotationY(job.larm, job.larm2, partialTicks) * pi;
                         this.bipedLeftArm.rotateAngleZ = job.getRotationZ(job.larm, job.larm2, partialTicks) * pi;
                         if (player.display.getHasLivingAnimation()) {
                              var10000 = this.bipedLeftArm;
                              var10000.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                              var10000 = this.bipedLeftArm;
                              var10000.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
                         }
                    }

                    if (!job.rarm.disabled) {
                         this.bipedRightArm.rotateAngleX = job.getRotationX(job.rarm, job.rarm2, partialTicks) * pi;
                         this.bipedRightArm.rotateAngleY = job.getRotationY(job.rarm, job.rarm2, partialTicks) * pi;
                         this.bipedRightArm.rotateAngleZ = job.getRotationZ(job.rarm, job.rarm2, partialTicks) * pi;
                         if (player.display.getHasLivingAnimation()) {
                              var10000 = this.bipedRightArm;
                              var10000.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                              var10000 = this.bipedRightArm;
                              var10000.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
                         }
                    }

                    if (!job.rleg.disabled) {
                         this.bipedRightLeg.rotateAngleX = job.getRotationX(job.rleg, job.rleg2, partialTicks) * pi;
                         this.bipedRightLeg.rotateAngleY = job.getRotationY(job.rleg, job.rleg2, partialTicks) * pi;
                         this.bipedRightLeg.rotateAngleZ = job.getRotationZ(job.rleg, job.rleg2, partialTicks) * pi;
                    }

                    if (!job.lleg.disabled) {
                         this.bipedLeftLeg.rotateAngleX = job.getRotationX(job.lleg, job.lleg2, partialTicks) * pi;
                         this.bipedLeftLeg.rotateAngleY = job.getRotationY(job.lleg, job.lleg2, partialTicks) * pi;
                         this.bipedLeftLeg.rotateAngleZ = job.getRotationZ(job.lleg, job.lleg2, partialTicks) * pi;
                    }
               }
          }

          copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
          copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
          copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
          copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
          copyModelAngles(this.bipedBody, this.bipedBodyWear);
          copyModelAngles(this.bipedHead, this.bipedHeadwear);
     }

     public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
          try {
               GlStateManager.pushMatrix();
               if (entityIn.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
               }

               this.bipedHead.render(scale);
               this.bipedBody.render(scale);
               this.bipedRightArm.render(scale);
               this.bipedLeftArm.render(scale);
               this.bipedRightLeg.render(scale);
               this.bipedLeftLeg.render(scale);
               this.bipedHeadwear.render(scale);
               this.bipedLeftLegwear.render(scale);
               this.bipedRightLegwear.render(scale);
               this.bipedLeftArmwear.render(scale);
               this.bipedRightArmwear.render(scale);
               this.bipedBodyWear.render(scale);
               GlStateManager.popMatrix();
          } catch (Exception var9) {
          }

     }

     protected EnumHandSide getMainHand(Entity entityIn) {
          if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isSwingInProgress) {
               EntityLivingBase living = (EntityLivingBase)entityIn;
               return living.swingingHand == EnumHand.MAIN_HAND ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
          } else {
               return super.getMainHand(entityIn);
          }
     }

     public ModelRenderer getRandomModelBox(Random random) {
          switch(random.nextInt(5)) {
          case 0:
               return this.bipedHead;
          case 1:
               return this.bipedBody;
          case 2:
               return this.bipedLeftArm;
          case 3:
               return this.bipedRightArm;
          case 4:
               return this.bipedLeftLeg;
          case 5:
               return this.bipedRightLeg;
          default:
               return this.bipedHead;
          }
     }
}
