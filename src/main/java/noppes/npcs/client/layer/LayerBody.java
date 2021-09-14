package noppes.npcs.client.layer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.model.Model2DRenderer;
import noppes.npcs.client.model.ModelPlaneRenderer;
import noppes.npcs.constants.EnumParts;

public class LayerBody extends LayerInterface {
     private Model2DRenderer lWing;
     private Model2DRenderer rWing;
     private Model2DRenderer breasts;
     private ModelRenderer breasts2;
     private ModelRenderer breasts3;
     private ModelPlaneRenderer skirt;
     private Model2DRenderer fin;

     public LayerBody(RenderLiving render) {
          super(render);
          this.createParts();
     }

     private void createParts() {
          this.lWing = new Model2DRenderer(this.model, 56.0F, 16.0F, 8, 16);
          this.lWing.field_78809_i = true;
          this.lWing.func_78793_a(2.0F, 2.5F, 1.0F);
          this.lWing.setRotationOffset(8.0F, 14.0F, 0.0F);
          this.setRotation(this.lWing, 0.7141593F, -0.5235988F, -0.5090659F);
          this.rWing = new Model2DRenderer(this.model, 56.0F, 16.0F, 8, 16);
          this.rWing.func_78793_a(-2.0F, 2.5F, 1.0F);
          this.rWing.setRotationOffset(-8.0F, 14.0F, 0.0F);
          this.setRotation(this.rWing, 0.7141593F, 0.5235988F, 0.5090659F);
          this.breasts = new Model2DRenderer(this.model, 20.0F, 22.0F, 8, 3);
          this.breasts.func_78793_a(-3.6F, 5.2F, -3.0F);
          this.breasts.setScale(0.17F, 0.19F);
          this.breasts.setThickness(1.0F);
          this.breasts2 = new ModelRenderer(this.model);
          Model2DRenderer bottom = new Model2DRenderer(this.model, 20.0F, 22.0F, 8, 4);
          bottom.func_78793_a(-3.6F, 5.0F, -3.1F);
          bottom.setScale(0.225F, 0.2F);
          bottom.setThickness(2.0F);
          bottom.field_78795_f = -0.31415927F;
          this.breasts2.func_78792_a(bottom);
          this.breasts3 = new ModelRenderer(this.model);
          Model2DRenderer right = new Model2DRenderer(this.model, 20.0F, 23.0F, 3, 2);
          right.func_78793_a(-3.8F, 5.3F, -3.6F);
          right.setScale(0.12F, 0.14F);
          right.setThickness(1.75F);
          this.breasts3.func_78792_a(right);
          Model2DRenderer right2 = new Model2DRenderer(this.model, 20.0F, 22.0F, 3, 1);
          right2.func_78793_a(-3.79F, 4.1F, -3.14F);
          right2.setScale(0.06F, 0.07F);
          right2.setThickness(1.75F);
          right2.field_78795_f = 0.34906584F;
          this.breasts3.func_78792_a(right2);
          Model2DRenderer right3 = new Model2DRenderer(this.model, 20.0F, 24.0F, 3, 1);
          right3.func_78793_a(-3.79F, 5.3F, -3.6F);
          right3.setScale(0.06F, 0.07F);
          right3.setThickness(1.75F);
          right3.field_78795_f = -0.34906584F;
          this.breasts3.func_78792_a(right3);
          Model2DRenderer right4 = new Model2DRenderer(this.model, 21.0F, 23.0F, 1, 2);
          right4.func_78793_a(-1.8F, 5.3F, -3.14F);
          right4.setScale(0.12F, 0.14F);
          right4.setThickness(1.75F);
          right4.field_78796_g = 0.34906584F;
          this.breasts3.func_78792_a(right4);
          Model2DRenderer left = new Model2DRenderer(this.model, 25.0F, 23.0F, 3, 2);
          left.func_78793_a(0.8F, 5.3F, -3.6F);
          left.setScale(0.12F, 0.14F);
          left.setThickness(1.75F);
          this.breasts3.func_78792_a(left);
          Model2DRenderer left2 = new Model2DRenderer(this.model, 25.0F, 22.0F, 3, 1);
          left2.func_78793_a(0.81F, 4.1F, -3.18F);
          left2.setScale(0.06F, 0.07F);
          left2.setThickness(1.75F);
          left2.field_78795_f = 0.34906584F;
          this.breasts3.func_78792_a(left2);
          Model2DRenderer left3 = new Model2DRenderer(this.model, 25.0F, 24.0F, 3, 1);
          left3.func_78793_a(0.81F, 5.3F, -3.6F);
          left3.setScale(0.06F, 0.07F);
          left3.setThickness(1.75F);
          left3.field_78795_f = -0.34906584F;
          this.breasts3.func_78792_a(left3);
          Model2DRenderer left4 = new Model2DRenderer(this.model, 24.0F, 23.0F, 1, 2);
          left4.func_78793_a(0.8F, 5.3F, -3.6F);
          left4.setScale(0.12F, 0.14F);
          left4.setThickness(1.75F);
          left4.field_78796_g = -0.34906584F;
          this.breasts3.func_78792_a(left4);
          this.skirt = new ModelPlaneRenderer(this.model, 58, 18);
          this.skirt.addSidePlane(0.0F, 0.0F, 0.0F, 9, 2);
          ModelPlaneRenderer part1 = new ModelPlaneRenderer(this.model, 58, 18);
          part1.addSidePlane(2.0F, 0.0F, 0.0F, 9, 2);
          part1.field_78796_g = -1.5707964F;
          this.skirt.func_78792_a(part1);
          this.skirt.func_78793_a(2.4F, 8.8F, 0.0F);
          this.setRotation(this.skirt, 0.3F, -0.2F, -0.2F);
          this.fin = new Model2DRenderer(this.model, 56.0F, 20.0F, 8, 12);
          this.fin.func_78793_a(-0.5F, 12.0F, 10.0F);
          this.fin.setScale(0.74F);
          this.fin.field_78796_g = 1.5707964F;
     }

     public void render(float par2, float par3, float par4, float par5, float par6, float par7) {
          this.model.field_78115_e.func_78794_c(0.0625F);
          this.renderSkirt(par7);
          this.renderWings(par7);
          this.renderFin(par7);
          this.renderBreasts(par7);
     }

     private void renderWings(float par7) {
          ModelPartData data = this.playerdata.getPartData(EnumParts.WINGS);
          if (data != null) {
               this.preRender(data);
               this.rWing.func_78785_a(par7);
               this.lWing.func_78785_a(par7);
          }
     }

     private void renderSkirt(float par7) {
          ModelPartData data = this.playerdata.getPartData(EnumParts.SKIRT);
          if (data != null) {
               this.preRender(data);
               GlStateManager.func_179094_E();
               GlStateManager.func_179152_a(1.7F, 1.04F, 1.6F);

               for(int i = 0; i < 10; ++i) {
                    GlStateManager.func_179114_b(36.0F, 0.0F, 1.0F, 0.0F);
                    this.skirt.func_78785_a(par7);
               }

               GlStateManager.func_179121_F();
          }
     }

     private void renderFin(float par7) {
          ModelPartData data = this.playerdata.getPartData(EnumParts.FIN);
          if (data != null) {
               this.preRender(data);
               this.fin.func_78785_a(par7);
          }
     }

     private void renderBreasts(float par7) {
          ModelPartData data = this.playerdata.getPartData(EnumParts.BREASTS);
          if (data != null) {
               data.playerTexture = true;
               this.preRender(data);
               if (data.type == 0) {
                    this.breasts.func_78785_a(par7);
               }

               if (data.type == 1) {
                    this.breasts2.func_78785_a(par7);
               }

               if (data.type == 2) {
                    this.breasts3.func_78785_a(par7);
               }

          }
     }

     public void rotate(float par1, float par2, float par3, float par4, float par5, float par6) {
          this.rWing.field_78795_f = 0.7141593F;
          this.rWing.field_78808_h = 0.5090659F;
          this.lWing.field_78795_f = 0.7141593F;
          this.lWing.field_78808_h = -0.5090659F;
          float motion = Math.abs(MathHelper.sin(par1 * 0.033F + 3.1415927F) * 0.4F) * par2;
          Model2DRenderer var10000;
          if (this.npc.field_70122_E && (double)motion <= 0.01D) {
               var10000 = this.lWing;
               var10000.field_78808_h += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
               var10000 = this.rWing;
               var10000.field_78808_h -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
               var10000 = this.lWing;
               var10000.field_78795_f += MathHelper.sin(par3 * 0.067F) * 0.05F;
               var10000 = this.rWing;
               var10000.field_78795_f += MathHelper.sin(par3 * 0.067F) * 0.05F;
          } else {
               float speed = 0.55F + 0.5F * motion;
               float y = MathHelper.sin(par3 * 0.55F);
               var10000 = this.rWing;
               var10000.field_78808_h += y * 0.5F * speed;
               var10000 = this.rWing;
               var10000.field_78795_f += y * 0.5F * speed;
               var10000 = this.lWing;
               var10000.field_78808_h -= y * 0.5F * speed;
               var10000 = this.lWing;
               var10000.field_78795_f += y * 0.5F * speed;
          }

          this.setRotation(this.skirt, 0.3F, -0.2F, -0.2F);
          ModelPlaneRenderer var10 = this.skirt;
          var10.field_78795_f += this.model.field_178724_i.field_78795_f * 0.04F;
          var10 = this.skirt;
          var10.field_78808_h += this.model.field_178724_i.field_78795_f * 0.06F;
          var10 = this.skirt;
          var10.field_78808_h -= MathHelper.cos(par3 * 0.09F) * 0.04F - 0.05F;
     }
}
