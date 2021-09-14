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
          this.head.func_78790_a(-3.0F, -6.0F, -1.0F, 6, 6, 1, scale);
          this.body = new ModelScaleRenderer(this, 0, 0, EnumParts.BODY);
          this.body.func_78787_b(64, 32);
          this.body.func_78790_a(-5.0F, 0.0F, -1.0F, 10, 16, 1, scale);
          ObfuscationReflectionHelper.setPrivateValue(ModelPlayer.class, this, this.head, 6);
          ObfuscationReflectionHelper.setPrivateValue(ModelPlayer.class, this, this.body, 5);
          this.field_178724_i = this.createScale(this.field_178724_i, EnumParts.ARM_LEFT);
          this.field_178723_h = this.createScale(this.field_178723_h, EnumParts.ARM_RIGHT);
          this.field_178734_a = this.createScale(this.field_178734_a, EnumParts.ARM_LEFT);
          this.field_178732_b = this.createScale(this.field_178732_b, EnumParts.ARM_RIGHT);
          this.field_178722_k = this.createScale(this.field_178722_k, EnumParts.LEG_LEFT);
          this.field_178721_j = this.createScale(this.field_178721_j, EnumParts.LEG_RIGHT);
          this.field_178733_c = this.createScale(this.field_178733_c, EnumParts.LEG_LEFT);
          this.field_178731_d = this.createScale(this.field_178731_d, EnumParts.LEG_RIGHT);
          this.field_78116_c = this.createScale(this.field_78116_c, EnumParts.HEAD);
          this.field_178720_f = this.createScale(this.field_178720_f, EnumParts.HEAD);
          this.field_78115_e = this.createScale(this.field_78115_e, EnumParts.BODY);
          this.field_178730_v = this.createScale(this.field_178730_v, EnumParts.BODY);
     }

     private ModelScaleRenderer createScale(ModelRenderer renderer, EnumParts part) {
          int textureX = (Integer)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, renderer, 2);
          int textureY = (Integer)ObfuscationReflectionHelper.getPrivateValue(ModelRenderer.class, renderer, 3);
          ModelScaleRenderer model = new ModelScaleRenderer(this, textureX, textureY, part);
          model.field_78799_b = renderer.field_78799_b;
          model.field_78801_a = renderer.field_78801_a;
          if (renderer.field_78805_m != null) {
               model.field_78805_m = new ArrayList(renderer.field_78805_m);
          }

          model.field_78804_l = new ArrayList(renderer.field_78804_l);
          func_178685_a(renderer, model);
          List list = (List)this.map.get(part);
          if (list == null) {
               this.map.put(part, list = new ArrayList());
          }

          ((List)list).add(model);
          return model;
     }

     public void func_78087_a(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
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

          if (!this.field_78093_q) {
               this.field_78093_q = player.currentAnimation == 1;
          }

          if (this.field_78117_n && (player.currentAnimation == 7 || player.isPlayerSleeping())) {
               this.field_78117_n = false;
          }

          if (player.currentAnimation == 6) {
               this.field_187076_m = ArmPose.BOW_AND_ARROW;
          }

          this.field_78117_n = player.isSneaking();
          this.field_78115_e.field_78800_c = this.field_78115_e.field_78797_d = this.field_78115_e.field_78798_e = 0.0F;
          this.field_78115_e.field_78795_f = this.field_78115_e.field_78796_g = this.field_78115_e.field_78808_h = 0.0F;
          this.field_178720_f.field_78795_f = this.field_78116_c.field_78795_f = 0.0F;
          this.field_178720_f.field_78808_h = this.field_78116_c.field_78808_h = 0.0F;
          this.field_178720_f.field_78800_c = this.field_78116_c.field_78800_c = 0.0F;
          this.field_178720_f.field_78797_d = this.field_78116_c.field_78797_d = 0.0F;
          this.field_178720_f.field_78798_e = this.field_78116_c.field_78798_e = 0.0F;
          this.field_178722_k.field_78795_f = 0.0F;
          this.field_178722_k.field_78796_g = 0.0F;
          this.field_178722_k.field_78808_h = 0.0F;
          this.field_178721_j.field_78795_f = 0.0F;
          this.field_178721_j.field_78796_g = 0.0F;
          this.field_178721_j.field_78808_h = 0.0F;
          this.field_178724_i.field_78800_c = 0.0F;
          this.field_178724_i.field_78797_d = 2.0F;
          this.field_178724_i.field_78798_e = 0.0F;
          this.field_178723_h.field_78800_c = 0.0F;
          this.field_178723_h.field_78797_d = 2.0F;
          this.field_178723_h.field_78798_e = 0.0F;

          try {
               super.func_78087_a(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
          } catch (Exception var15) {
          }

          if (player.isPlayerSleeping()) {
               if (this.field_78116_c.field_78795_f < 0.0F) {
                    this.field_78116_c.field_78795_f = 0.0F;
                    this.field_178720_f.field_78795_f = 0.0F;
               }
          } else if (player.currentAnimation == 9) {
               this.field_178720_f.field_78795_f = this.field_78116_c.field_78795_f = 0.7F;
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
          } else if (this.field_78117_n) {
               this.field_78115_e.field_78795_f = 0.5F / playerdata.getPartConfig(EnumParts.BODY).scaleY;
          }

          if (player.advanced.job == 9) {
               JobPuppet job = (JobPuppet)player.jobInterface;
               if (job.isActive()) {
                    float pi = 3.1415927F;
                    float partialTicks = Minecraft.getMinecraft().func_184121_ak();
                    if (!job.head.disabled) {
                         this.field_178720_f.field_78795_f = this.field_78116_c.field_78795_f = job.getRotationX(job.head, job.head2, partialTicks) * pi;
                         this.field_178720_f.field_78796_g = this.field_78116_c.field_78796_g = job.getRotationY(job.head, job.head2, partialTicks) * pi;
                         this.field_178720_f.field_78808_h = this.field_78116_c.field_78808_h = job.getRotationZ(job.head, job.head2, partialTicks) * pi;
                    }

                    if (!job.body.disabled) {
                         this.field_78115_e.field_78795_f = job.getRotationX(job.body, job.body2, partialTicks) * pi;
                         this.field_78115_e.field_78796_g = job.getRotationY(job.body, job.body2, partialTicks) * pi;
                         this.field_78115_e.field_78808_h = job.getRotationZ(job.body, job.body2, partialTicks) * pi;
                    }

                    ModelRenderer var10000;
                    if (!job.larm.disabled) {
                         this.field_178724_i.field_78795_f = job.getRotationX(job.larm, job.larm2, partialTicks) * pi;
                         this.field_178724_i.field_78796_g = job.getRotationY(job.larm, job.larm2, partialTicks) * pi;
                         this.field_178724_i.field_78808_h = job.getRotationZ(job.larm, job.larm2, partialTicks) * pi;
                         if (player.display.getHasLivingAnimation()) {
                              var10000 = this.field_178724_i;
                              var10000.field_78808_h -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                              var10000 = this.field_178724_i;
                              var10000.field_78795_f -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
                         }
                    }

                    if (!job.rarm.disabled) {
                         this.field_178723_h.field_78795_f = job.getRotationX(job.rarm, job.rarm2, partialTicks) * pi;
                         this.field_178723_h.field_78796_g = job.getRotationY(job.rarm, job.rarm2, partialTicks) * pi;
                         this.field_178723_h.field_78808_h = job.getRotationZ(job.rarm, job.rarm2, partialTicks) * pi;
                         if (player.display.getHasLivingAnimation()) {
                              var10000 = this.field_178723_h;
                              var10000.field_78808_h += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
                              var10000 = this.field_178723_h;
                              var10000.field_78795_f += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
                         }
                    }

                    if (!job.rleg.disabled) {
                         this.field_178721_j.field_78795_f = job.getRotationX(job.rleg, job.rleg2, partialTicks) * pi;
                         this.field_178721_j.field_78796_g = job.getRotationY(job.rleg, job.rleg2, partialTicks) * pi;
                         this.field_178721_j.field_78808_h = job.getRotationZ(job.rleg, job.rleg2, partialTicks) * pi;
                    }

                    if (!job.lleg.disabled) {
                         this.field_178722_k.field_78795_f = job.getRotationX(job.lleg, job.lleg2, partialTicks) * pi;
                         this.field_178722_k.field_78796_g = job.getRotationY(job.lleg, job.lleg2, partialTicks) * pi;
                         this.field_178722_k.field_78808_h = job.getRotationZ(job.lleg, job.lleg2, partialTicks) * pi;
                    }
               }
          }

          func_178685_a(this.field_178722_k, this.field_178733_c);
          func_178685_a(this.field_178721_j, this.field_178731_d);
          func_178685_a(this.field_178724_i, this.field_178734_a);
          func_178685_a(this.field_178723_h, this.field_178732_b);
          func_178685_a(this.field_78115_e, this.field_178730_v);
          func_178685_a(this.field_78116_c, this.field_178720_f);
     }

     public void func_78088_a(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
          try {
               GlStateManager.func_179094_E();
               if (entityIn.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
               }

               this.field_78116_c.func_78785_a(scale);
               this.field_78115_e.func_78785_a(scale);
               this.field_178723_h.func_78785_a(scale);
               this.field_178724_i.func_78785_a(scale);
               this.field_178721_j.func_78785_a(scale);
               this.field_178722_k.func_78785_a(scale);
               this.field_178720_f.func_78785_a(scale);
               this.field_178733_c.func_78785_a(scale);
               this.field_178731_d.func_78785_a(scale);
               this.field_178734_a.func_78785_a(scale);
               this.field_178732_b.func_78785_a(scale);
               this.field_178730_v.func_78785_a(scale);
               GlStateManager.func_179121_F();
          } catch (Exception var9) {
          }

     }

     protected EnumHandSide func_187072_a(Entity entityIn) {
          if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).field_82175_bq) {
               EntityLivingBase living = (EntityLivingBase)entityIn;
               return living.field_184622_au == EnumHand.MAIN_HAND ? EnumHandSide.RIGHT : EnumHandSide.LEFT;
          } else {
               return super.func_187072_a(entityIn);
          }
     }

     public ModelRenderer func_85181_a(Random random) {
          switch(random.nextInt(5)) {
          case 0:
               return this.field_78116_c;
          case 1:
               return this.field_78115_e;
          case 2:
               return this.field_178724_i;
          case 3:
               return this.field_178723_h;
          case 4:
               return this.field_178722_k;
          case 5:
               return this.field_178721_j;
          default:
               return this.field_78116_c;
          }
     }
}
