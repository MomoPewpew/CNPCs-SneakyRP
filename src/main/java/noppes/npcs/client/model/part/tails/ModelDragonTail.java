package noppes.npcs.client.model.part.tails;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import noppes.npcs.client.model.ModelPlaneRenderer;

public class ModelDragonTail extends ModelRenderer {
     public ModelDragonTail(ModelBiped base) {
          super(base);
          int x = 52;
          int y = 16;
          ModelRenderer dragon = new ModelRenderer(base, x, y);
          dragon.func_78793_a(0.0F, 0.0F, 3.0F);
          this.func_78792_a(dragon);
          ModelRenderer DragonTail2 = new ModelRenderer(base, x, y);
          DragonTail2.func_78793_a(0.0F, 2.0F, 2.0F);
          ModelRenderer DragonTail3 = new ModelRenderer(base, x, y);
          DragonTail3.func_78793_a(0.0F, 4.5F, 4.0F);
          ModelRenderer DragonTail4 = new ModelRenderer(base, x, y);
          DragonTail4.func_78793_a(0.0F, 7.0F, 5.75F);
          ModelRenderer DragonTail5 = new ModelRenderer(base, x, y);
          DragonTail5.func_78793_a(0.0F, 9.0F, 8.0F);
          ModelPlaneRenderer planeLeft = new ModelPlaneRenderer(base, x, y);
          planeLeft.addSidePlane(-1.5F, -1.5F, -1.5F, 3, 3);
          ModelPlaneRenderer planeRight = new ModelPlaneRenderer(base, x, y);
          planeRight.addSidePlane(-1.5F, -1.5F, -1.5F, 3, 3);
          this.setRotation(planeRight, 3.1415927F, 3.1415927F, 0.0F);
          ModelPlaneRenderer planeTop = new ModelPlaneRenderer(base, x, y);
          planeTop.addTopPlane(-1.5F, -1.5F, -1.5F, 3, 3);
          this.setRotation(planeTop, 0.0F, -1.5707964F, 0.0F);
          ModelPlaneRenderer planeBottom = new ModelPlaneRenderer(base, x, y);
          planeBottom.addTopPlane(-1.5F, -1.5F, -1.5F, 3, 3);
          this.setRotation(planeBottom, 0.0F, -1.5707964F, 3.1415927F);
          ModelPlaneRenderer planeBack = new ModelPlaneRenderer(base, x, y);
          planeBack.addBackPlane(-1.5F, -1.5F, -1.5F, 3, 3);
          this.setRotation(planeBack, 0.0F, 0.0F, 1.5707964F);
          ModelPlaneRenderer planeFront = new ModelPlaneRenderer(base, x, y);
          planeFront.addBackPlane(-1.5F, -1.5F, -1.5F, 3, 3);
          this.setRotation(planeFront, 0.0F, 3.1415927F, -1.5707964F);
          dragon.func_78792_a(planeLeft);
          dragon.func_78792_a(planeRight);
          dragon.func_78792_a(planeTop);
          dragon.func_78792_a(planeBottom);
          dragon.func_78792_a(planeFront);
          dragon.func_78792_a(planeBack);
          DragonTail2.func_78792_a(planeLeft);
          DragonTail2.func_78792_a(planeRight);
          DragonTail2.func_78792_a(planeTop);
          DragonTail2.func_78792_a(planeBottom);
          DragonTail2.func_78792_a(planeFront);
          DragonTail2.func_78792_a(planeBack);
          DragonTail3.func_78792_a(planeLeft);
          DragonTail3.func_78792_a(planeRight);
          DragonTail3.func_78792_a(planeTop);
          DragonTail3.func_78792_a(planeBottom);
          DragonTail3.func_78792_a(planeFront);
          DragonTail3.func_78792_a(planeBack);
          DragonTail4.func_78792_a(planeLeft);
          DragonTail4.func_78792_a(planeRight);
          DragonTail4.func_78792_a(planeTop);
          DragonTail4.func_78792_a(planeBottom);
          DragonTail4.func_78792_a(planeFront);
          DragonTail4.func_78792_a(planeBack);
          dragon.func_78792_a(DragonTail2);
          dragon.func_78792_a(DragonTail3);
          dragon.func_78792_a(DragonTail4);
     }

     public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.field_78795_f = x;
          model.field_78796_g = y;
          model.field_78808_h = z;
     }
}
