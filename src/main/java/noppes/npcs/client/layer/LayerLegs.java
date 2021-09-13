package noppes.npcs.client.layer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.model.part.legs.ModelDigitigradeLegs;
import noppes.npcs.client.model.part.legs.ModelHorseLegs;
import noppes.npcs.client.model.part.legs.ModelMermaidLegs;
import noppes.npcs.client.model.part.legs.ModelNagaLegs;
import noppes.npcs.client.model.part.legs.ModelSpiderLegs;
import noppes.npcs.client.model.part.tails.ModelCanineTail;
import noppes.npcs.client.model.part.tails.ModelDragonTail;
import noppes.npcs.client.model.part.tails.ModelFeatherTail;
import noppes.npcs.client.model.part.tails.ModelRodentTail;
import noppes.npcs.client.model.part.tails.ModelSquirrelTail;
import noppes.npcs.client.model.part.tails.ModelTailFin;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;

public class LayerLegs extends LayerInterface implements LayerPreRender {
     private ModelSpiderLegs spiderLegs;
     private ModelHorseLegs horseLegs;
     private ModelNagaLegs naga;
     private ModelDigitigradeLegs digitigrade;
     private ModelMermaidLegs mermaid;
     private ModelRenderer tail;
     private ModelCanineTail fox;
     private ModelRenderer dragon;
     private ModelRenderer squirrel;
     private ModelRenderer horse;
     private ModelRenderer fin;
     private ModelRenderer rodent;
     private ModelRenderer feathers;
     float rotationPointZ;
     float rotationPointY;

     public LayerLegs(RenderLiving render) {
          super(render);
          this.createParts();
     }

     private void createParts() {
          this.spiderLegs = new ModelSpiderLegs(this.model);
          this.horseLegs = new ModelHorseLegs(this.model);
          this.naga = new ModelNagaLegs(this.model);
          this.mermaid = new ModelMermaidLegs(this.model);
          this.digitigrade = new ModelDigitigradeLegs(this.model);
          this.fox = new ModelCanineTail(this.model);
          this.tail = new ModelRenderer(this.model, 56, 21);
          this.tail.func_78789_a(-1.0F, 0.0F, 0.0F, 2, 9, 2);
          this.tail.func_78793_a(0.0F, 0.0F, 1.0F);
          this.setRotation(this.tail, 0.8714253F, 0.0F, 0.0F);
          this.horse = new ModelRenderer(this.model);
          this.horse.func_78787_b(32, 32);
          this.horse.func_78793_a(0.0F, -1.0F, 1.0F);
          ModelRenderer tailBase = new ModelRenderer(this.model, 0, 26);
          tailBase.func_78787_b(32, 32);
          tailBase.func_78789_a(-1.0F, -1.0F, 0.0F, 2, 2, 3);
          this.setRotation(tailBase, -1.134464F, 0.0F, 0.0F);
          this.horse.func_78792_a(tailBase);
          ModelRenderer tailMiddle = new ModelRenderer(this.model, 0, 13);
          tailMiddle.func_78787_b(32, 32);
          tailMiddle.func_78789_a(-1.5F, -2.0F, 3.0F, 3, 4, 7);
          this.setRotation(tailMiddle, -1.134464F, 0.0F, 0.0F);
          this.horse.func_78792_a(tailMiddle);
          ModelRenderer tailTip = new ModelRenderer(this.model, 0, 0);
          tailTip.func_78787_b(32, 32);
          tailTip.func_78789_a(-1.5F, -4.5F, 9.0F, 3, 4, 7);
          this.setRotation(tailTip, -1.40215F, 0.0F, 0.0F);
          this.horse.func_78792_a(tailTip);
          this.horse.field_78795_f = 0.5F;
          this.dragon = new ModelDragonTail(this.model);
          this.squirrel = new ModelSquirrelTail(this.model);
          this.fin = new ModelTailFin(this.model);
          this.rodent = new ModelRodentTail(this.model);
          this.feathers = new ModelFeatherTail(this.model);
     }

     public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
          this.renderLegs(par7);
          this.renderTails(par7);
     }

     private void renderTails(float par7) {
          ModelPartData data = this.playerdata.getPartData(EnumParts.TAIL);
          if (data != null) {
               GlStateManager.func_179094_E();
               ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.LEG_LEFT);
               GlStateManager.func_179109_b(config.transX * par7, config.transY + this.rotationPointY * par7, config.transZ * par7 + this.rotationPointZ * par7);
               GlStateManager.func_179109_b(0.0F, 0.0F, (config.scaleZ - 1.0F) * 5.0F * par7);
               GlStateManager.func_179152_a(config.scaleX, config.scaleY, config.scaleZ);
               this.preRender(data);
               if (data.type == 0) {
                    if (data.pattern == 1) {
                         this.tail.field_78800_c = -0.5F;
                         ModelRenderer var10000 = this.tail;
                         var10000.field_78796_g = (float)((double)var10000.field_78796_g - 0.2D);
                         this.tail.func_78785_a(par7);
                         ++this.tail.field_78800_c;
                         var10000 = this.tail;
                         var10000.field_78796_g = (float)((double)var10000.field_78796_g + 0.4D);
                         this.tail.func_78785_a(par7);
                         this.tail.field_78800_c = 0.0F;
                    } else {
                         this.tail.func_78785_a(par7);
                    }
               } else if (data.type == 1) {
                    this.dragon.func_78785_a(par7);
               } else if (data.type == 2) {
                    this.horse.func_78785_a(par7);
               } else if (data.type == 3) {
                    this.squirrel.func_78785_a(par7);
               } else if (data.type == 4) {
                    this.fin.func_78785_a(par7);
               } else if (data.type == 5) {
                    this.rodent.func_78785_a(par7);
               } else if (data.type == 6) {
                    this.feathers.func_78785_a(par7);
               } else if (data.type == 7) {
                    this.fox.func_78785_a(par7);
               }

               GlStateManager.func_179121_F();
          }
     }

     private void renderLegs(float par7) {
          ModelPartData data = this.playerdata.getPartData(EnumParts.LEGS);
          if (data.type > 0) {
               GlStateManager.func_179094_E();
               ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.LEG_LEFT);
               this.preRender(data);
               if (data.type == 1) {
                    GlStateManager.func_179109_b(0.0F, config.transY * 2.0F, config.transZ * par7 + 0.04F);
                    GlStateManager.func_179152_a(config.scaleX, config.scaleY, config.scaleZ);
                    this.naga.func_78785_a(par7);
               } else if (data.type == 2) {
                    GlStateManager.func_179137_b(0.0D, (double)(config.transY * 1.76F) - 0.1D * (double)config.scaleY, (double)(config.transZ * par7));
                    GlStateManager.func_179152_a(1.06F, 1.06F, 1.06F);
                    GlStateManager.func_179152_a(config.scaleX, config.scaleY, config.scaleZ);
                    this.spiderLegs.func_78785_a(par7);
               } else if (data.type == 3) {
                    if (config.scaleY >= 1.0F) {
                         GlStateManager.func_179109_b(0.0F, config.transY * 1.76F, config.transZ * par7);
                    } else {
                         GlStateManager.func_179109_b(0.0F, config.transY * 1.86F, config.transZ * par7);
                    }

                    GlStateManager.func_179152_a(0.79F, 0.9F - config.scaleY / 10.0F, 0.79F);
                    GlStateManager.func_179152_a(config.scaleX, config.scaleY, config.scaleZ);
                    this.horseLegs.func_78785_a(par7);
               } else if (data.type == 4) {
                    GlStateManager.func_179109_b(0.0F, config.transY * 1.86F, config.transZ * par7);
                    GlStateManager.func_179152_a(config.scaleX, config.scaleY, config.scaleZ);
                    this.mermaid.func_78785_a(par7);
               } else if (data.type == 5) {
                    GlStateManager.func_179109_b(0.0F, config.transY * 1.86F, config.transZ * par7);
                    GlStateManager.func_179152_a(config.scaleX, config.scaleY, config.scaleZ);
                    this.digitigrade.func_78785_a(par7);
               }

               GlStateManager.func_179121_F();
          }
     }

     public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
          this.rotateLegs(par1, par2, par3, par4, par5, par6);
          this.rotateTail(par1, par2, par3, par4, par5, par6);
     }

     public void rotateLegs(float par1, float par2, float par3, float par4, float par5, float par6) {
          ModelPartData part = this.playerdata.getPartData(EnumParts.LEGS);
          if (part.type == 2) {
               this.spiderLegs.setRotationAngles(this.playerdata, par1, par2, par3, par4, par5, par6, this.npc);
          } else if (part.type == 3) {
               this.horseLegs.setRotationAngles(this.playerdata, par1, par2, par3, par4, par5, par6, this.npc);
          } else if (part.type == 1) {
               this.naga.isRiding = this.model.field_78093_q;
               this.naga.isSleeping = this.npc.func_70608_bn();
               this.naga.isCrawling = this.npc.currentAnimation == 7;
               this.naga.isSneaking = this.model.field_78117_n;
               this.naga.setRotationAngles(par1, par2, par3, par4, par5, par6, this.npc);
          } else if (part.type == 4) {
               this.mermaid.setRotationAngles(par1, par2, par3, par4, par5, par6, this.npc);
          } else if (part.type == 5) {
               this.digitigrade.setRotationAngles(par1, par2, par3, par4, par5, par6, this.npc);
          }

     }

     public void rotateTail(float par1, float par2, float par3, float par4, float par5, float par6) {
          ModelPartData part = this.playerdata.getPartData(EnumParts.LEGS);
          ModelPartData partTail = this.playerdata.getPartData(EnumParts.TAIL);
          ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.LEG_LEFT);
          float rotateAngleY = MathHelper.func_76134_b(par1 * 0.6662F) * 0.2F * par2;
          float rotateAngleX = MathHelper.func_76126_a(par3 * 0.067F) * 0.05F;
          this.rotationPointZ = 0.0F;
          this.rotationPointY = 11.0F;
          if (part.type == 2) {
               this.rotationPointY = 12.0F + (config.scaleY - 1.0F) * 3.0F;
               this.rotationPointZ = 15.0F + (config.scaleZ - 1.0F) * 10.0F;
               if (this.npc.func_70608_bn() || this.npc.currentAnimation == 7) {
                    this.rotationPointY = 12.0F + 16.0F * config.scaleZ;
                    this.rotationPointZ = 1.0F * config.scaleY;
                    rotateAngleX = -0.7853982F;
               }
          } else if (part.type == 3) {
               this.rotationPointY = 10.0F;
               this.rotationPointZ = 16.0F + (config.scaleZ - 1.0F) * 12.0F;
          } else {
               this.rotationPointZ = (1.0F - config.scaleZ) * 1.0F;
          }

          if (partTail != null) {
               if (partTail.type == 2) {
                    rotateAngleX = (float)((double)rotateAngleX + 0.5D);
               }

               if (partTail.type == 0) {
                    rotateAngleX += 0.87F;
               }

               if (partTail.type == 7) {
                    this.fox.setRotationAngles(par1, par2, par3, par4, par5, par6, this.npc);
               }
          }

          this.rotationPointZ += this.model.field_178721_j.field_78798_e + 0.5F;
          this.fox.field_78795_f = this.tail.field_78795_f = this.feathers.field_78795_f = this.dragon.field_78795_f = this.squirrel.field_78795_f = this.horse.field_78795_f = this.fin.field_78795_f = this.rodent.field_78795_f = rotateAngleX;
          this.fox.field_78796_g = this.tail.field_78796_g = this.feathers.field_78796_g = this.dragon.field_78796_g = this.squirrel.field_78796_g = this.horse.field_78796_g = this.fin.field_78796_g = this.rodent.field_78796_g = rotateAngleY;
     }

     public void preRender(EntityCustomNpc player) {
          this.npc = player;
          this.playerdata = player.modelData;
          ModelPartData data = this.playerdata.getPartData(EnumParts.LEGS);
          this.model.field_178722_k.field_78807_k = this.model.field_178721_j.field_78807_k = data == null || data.type != 0;
     }
}
