package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelNpcSlime extends ModelBase {
     ModelRenderer outerBody;
     ModelRenderer innerBody;
     ModelRenderer slimeRightEye;
     ModelRenderer slimeLeftEye;
     ModelRenderer slimeMouth;

     public ModelNpcSlime(int par1) {
          this.field_78089_u = 64;
          this.field_78090_t = 64;
          this.outerBody = new ModelRenderer(this, 0, 0);
          this.outerBody = new ModelRenderer(this, 0, 0);
          this.outerBody.func_78789_a(-8.0F, 32.0F, -8.0F, 16, 16, 16);
          if (par1 > 0) {
               this.innerBody = new ModelRenderer(this, 0, 32);
               this.innerBody.func_78789_a(-3.0F, 17.0F, -3.0F, 6, 6, 6);
               this.slimeRightEye = new ModelRenderer(this, 0, 0);
               this.slimeRightEye.func_78789_a(-3.25F, 18.0F, -3.5F, 2, 2, 2);
               this.slimeLeftEye = new ModelRenderer(this, 0, 4);
               this.slimeLeftEye.func_78789_a(1.25F, 18.0F, -3.5F, 2, 2, 2);
               this.slimeMouth = new ModelRenderer(this, 0, 8);
               this.slimeMouth.func_78789_a(0.0F, 21.0F, -3.5F, 1, 1, 1);
          }

     }

     public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
          this.func_78087_a(par2, par3, par4, par5, par6, par7, par1Entity);
          if (this.innerBody != null) {
               this.innerBody.func_78785_a(par7);
          } else {
               GlStateManager.func_179094_E();
               GlStateManager.func_179152_a(0.5F, 0.5F, 0.5F);
               this.outerBody.func_78785_a(par7);
               GlStateManager.func_179121_F();
          }

          if (this.slimeRightEye != null) {
               this.slimeRightEye.func_78785_a(par7);
               this.slimeLeftEye.func_78785_a(par7);
               this.slimeMouth.func_78785_a(par7);
          }

     }
}
