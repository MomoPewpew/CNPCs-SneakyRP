package noppes.npcs.client.model.part.horns;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelAntennasBack extends ModelRenderer {
     public ModelAntennasBack(ModelBiped base) {
          super(base);
          ModelRenderer rightantenna1 = new ModelRenderer(base, 60, 27);
          rightantenna1.addBox(-1.0F, 0.0F, 0.0F, 1, 4, 1);
          rightantenna1.setRotationPoint(3.0F, -10.9F, 0.0F);
          this.setRotation(rightantenna1, -0.7504916F, 0.0698132F, 0.0698132F);
          this.addChild(rightantenna1);
          ModelRenderer leftantenna1 = new ModelRenderer(base, 56, 27);
          leftantenna1.mirror = true;
          leftantenna1.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
          leftantenna1.setRotationPoint(-3.0F, -10.9F, 0.0F);
          this.setRotation(leftantenna1, -0.7504916F, -0.0698132F, -0.0698132F);
          this.addChild(leftantenna1);
          ModelRenderer rightantenna2 = new ModelRenderer(base, 60, 27);
          rightantenna2.addBox(-1.0F, 0.0F, 0.0F, 1, 4, 1);
          rightantenna2.setRotationPoint(4.6F, -12.2F, 3.4F);
          this.setRotation(rightantenna2, -1.22173F, 0.4363323F, 0.0698132F);
          this.addChild(rightantenna2);
          ModelRenderer leftantenna2 = new ModelRenderer(base, 56, 27);
          leftantenna2.mirror = true;
          leftantenna2.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
          leftantenna2.setRotationPoint(-4.6F, -12.2F, 3.4F);
          this.setRotation(leftantenna2, -1.22173F, -0.4363323F, -0.0698132F);
          this.addChild(leftantenna2);
     }

     private void setRotation(ModelRenderer model, float x, float y, float z) {
          model.rotateAngleX = x;
          model.rotateAngleY = y;
          model.rotateAngleZ = z;
     }
}
