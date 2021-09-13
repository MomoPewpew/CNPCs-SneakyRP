package noppes.npcs.client.renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.ModelPony;
import noppes.npcs.client.model.ModelPonyArmor;
import noppes.npcs.entity.EntityNpcPony;

public class RenderNPCPony extends RenderNPCInterface {
     private ModelPony modelBipedMain;
     private ModelPonyArmor modelArmorChestplate;
     private ModelPonyArmor modelArmor;

     public RenderNPCPony() {
          super(new ModelPony(0.0F), 0.5F);
          this.modelBipedMain = (ModelPony)this.field_77045_g;
          this.modelArmorChestplate = new ModelPonyArmor(1.0F);
          this.modelArmor = new ModelPonyArmor(0.5F);
     }

     public ResourceLocation getEntityTexture(EntityNpcPony pony) {
          boolean check = pony.textureLocation == null || pony.textureLocation != pony.checked;
          ResourceLocation loc = super.getEntityTexture(pony);
          if (check) {
               try {
                    IResource resource = Minecraft.getMinecraft().func_110442_L().func_110536_a(loc);
                    BufferedImage bufferedimage = ImageIO.read(resource.func_110527_b());
                    pony.isPegasus = false;
                    pony.isUnicorn = false;
                    Color color = new Color(bufferedimage.getRGB(0, 0), true);
                    Color color1 = new Color(249, 177, 49, 255);
                    Color color2 = new Color(136, 202, 240, 255);
                    Color color3 = new Color(209, 159, 228, 255);
                    Color color4 = new Color(254, 249, 252, 255);
                    if (color.equals(color1)) {
                    }

                    if (color.equals(color2)) {
                         pony.isPegasus = true;
                    }

                    if (color.equals(color3)) {
                         pony.isUnicorn = true;
                    }

                    if (color.equals(color4)) {
                         pony.isPegasus = true;
                         pony.isUnicorn = true;
                    }

                    pony.checked = loc;
               } catch (IOException var11) {
               }
          }

          return loc;
     }

     public void doRender(EntityNpcPony pony, double d, double d1, double d2, float f, float f1) {
          ItemStack itemstack = pony.func_184614_ca();
          this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = itemstack == null ? 0 : 1;
          this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = pony.func_70093_af();
          this.modelArmorChestplate.field_78093_q = this.modelArmor.field_78093_q = this.modelBipedMain.field_78093_q = false;
          this.modelArmorChestplate.isSleeping = this.modelArmor.isSleeping = this.modelBipedMain.isSleeping = pony.func_70608_bn();
          this.modelArmorChestplate.isUnicorn = this.modelArmor.isUnicorn = this.modelBipedMain.isUnicorn = pony.isUnicorn;
          this.modelArmorChestplate.isPegasus = this.modelArmor.isPegasus = this.modelBipedMain.isPegasus = pony.isPegasus;
          if (pony.func_70093_af()) {
               d1 -= 0.125D;
          }

          super.doRender(pony, d, d1, d2, f, f1);
          this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
          this.modelArmorChestplate.field_78093_q = this.modelArmor.field_78093_q = this.modelBipedMain.field_78093_q = false;
          this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
          this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
     }
}
