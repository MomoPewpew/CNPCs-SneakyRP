package noppes.npcs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelNpcCrystal extends ModelBase {
     private ModelRenderer field_41057_g;
     private ModelRenderer field_41058_h = new ModelRenderer(this, "glass");
     private ModelRenderer field_41059_i;
     float ticks;

     public ModelNpcCrystal(float par1) {
          this.field_41058_h.func_78784_a(0, 0).func_78789_a(-4.0F, -4.0F, -4.0F, 8, 8, 8);
          this.field_41057_g = new ModelRenderer(this, "cube");
          this.field_41057_g.func_78784_a(32, 0).func_78789_a(-4.0F, -4.0F, -4.0F, 8, 8, 8);
          this.field_41059_i = new ModelRenderer(this, "base");
          this.field_41059_i.func_78784_a(0, 16).func_78789_a(-6.0F, 16.0F, -6.0F, 12, 4, 12);
     }

     public void func_78086_a(EntityLivingBase par1EntityLiving, float f6, float f5, float par9) {
          this.ticks = par9;
     }

     public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
          GlStateManager.func_179094_E();
          GlStateManager.func_179152_a(2.0F, 2.0F, 2.0F);
          GlStateManager.func_179109_b(0.0F, -0.5F, 0.0F);
          this.field_41059_i.func_78785_a(par7);
          float f = (float)par1Entity.field_70173_aa + this.ticks;
          float f1 = MathHelper.func_76126_a(f * 0.2F) / 2.0F + 0.5F;
          f1 += f1 * f1;
          par3 = f * 3.0F;
          par4 = f1 * 0.2F;
          GlStateManager.func_179114_b(par3, 0.0F, 1.0F, 0.0F);
          GlStateManager.func_179109_b(0.0F, 0.1F + par4, 0.0F);
          GlStateManager.func_179114_b(60.0F, 0.7071F, 0.0F, 0.7071F);
          this.field_41058_h.func_78785_a(par7);
          float sca = 0.875F;
          GlStateManager.func_179152_a(sca, sca, sca);
          GlStateManager.func_179114_b(60.0F, 0.7071F, 0.0F, 0.7071F);
          GlStateManager.func_179114_b(par3, 0.0F, 1.0F, 0.0F);
          this.field_41058_h.func_78785_a(par7);
          GlStateManager.func_179152_a(sca, sca, sca);
          GlStateManager.func_179114_b(60.0F, 0.7071F, 0.0F, 0.7071F);
          GlStateManager.func_179114_b(par3, 0.0F, 1.0F, 0.0F);
          this.field_41057_g.func_78785_a(par7);
          GlStateManager.func_179121_F();
     }
}
