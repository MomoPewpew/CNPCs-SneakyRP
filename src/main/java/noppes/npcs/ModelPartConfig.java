package noppes.npcs;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.util.ValueUtil;

public class ModelPartConfig {
     public float scaleX = 1.0F;
     public float scaleY = 1.0F;
     public float scaleZ = 1.0F;
     public float transX = 0.0F;
     public float transY = 0.0F;
     public float transZ = 0.0F;
     public boolean notShared = false;

     public NBTTagCompound writeToNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setFloat("ScaleX", this.scaleX);
          compound.setFloat("ScaleY", this.scaleY);
          compound.setFloat("ScaleZ", this.scaleZ);
          compound.setFloat("TransX", this.transX);
          compound.setFloat("TransY", this.transY);
          compound.setFloat("TransZ", this.transZ);
          compound.setBoolean("NotShared", this.notShared);
          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.scaleX = this.checkValue(compound.getFloat("ScaleX"), 0.5F, 1.5F);
          this.scaleY = this.checkValue(compound.getFloat("ScaleY"), 0.5F, 1.5F);
          this.scaleZ = this.checkValue(compound.getFloat("ScaleZ"), 0.5F, 1.5F);
          this.transX = this.checkValue(compound.getFloat("TransX"), -1.0F, 1.0F);
          this.transY = this.checkValue(compound.getFloat("TransY"), -1.0F, 1.0F);
          this.transZ = this.checkValue(compound.getFloat("TransZ"), -1.0F, 1.0F);
          this.notShared = compound.getBoolean("NotShared");
     }

     public String toString() {
          return "ScaleX: " + this.scaleX + " - ScaleY: " + this.scaleY + " - ScaleZ: " + this.scaleZ;
     }

     public float getScaleX() {
          return this.scaleX;
     }

     public float getScaleY() {
          return this.scaleY;
     }

     public float getScaleZ() {
          return this.scaleZ;
     }

     public void setScale(float x, float y, float z) {
          this.scaleX = ValueUtil.correctFloat(x, 0.5F, 1.5F);
          this.scaleY = ValueUtil.correctFloat(y, 0.5F, 1.5F);
          this.scaleZ = ValueUtil.correctFloat(z, 0.5F, 1.5F);
     }

     public void setScale(float x, float y) {
          this.scaleZ = this.scaleX = x;
          this.scaleY = y;
     }

     public float checkValue(float given, float min, float max) {
          if (given < min) {
               return min;
          } else {
               return given > max ? max : given;
          }
     }

     public void setTranslate(float transX, float transY, float transZ) {
          this.transX = transX;
          this.transY = transY;
          this.transZ = transZ;
     }

     public void copyValues(ModelPartConfig config) {
          this.scaleX = config.scaleX;
          this.scaleY = config.scaleY;
          this.scaleZ = config.scaleZ;
          this.transX = config.transX;
          this.transY = config.transY;
          this.transZ = config.transZ;
     }
}
