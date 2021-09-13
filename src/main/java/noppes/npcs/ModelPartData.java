package noppes.npcs;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ModelPartData {
     private static Map resources = new HashMap();
     public int color = 16777215;
     public int colorPattern = 16777215;
     public byte type = 0;
     public byte pattern = 0;
     public boolean playerTexture = false;
     public String name;
     private ResourceLocation location;

     public ModelPartData(String name) {
          this.name = name;
     }

     public NBTTagCompound writeToNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74774_a("Type", this.type);
          compound.setInteger("Color", this.color);
          compound.func_74757_a("PlayerTexture", this.playerTexture);
          compound.func_74774_a("Pattern", this.pattern);
          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          if (!compound.hasKey("Type")) {
               this.type = -1;
          } else {
               this.type = compound.func_74771_c("Type");
               this.color = compound.func_74762_e("Color");
               this.playerTexture = compound.getBoolean("PlayerTexture");
               this.pattern = compound.func_74771_c("Pattern");
               this.location = null;
          }
     }

     public ResourceLocation getResource() {
          if (this.location != null) {
               return this.location;
          } else {
               String texture = this.name + "/" + this.type;
               if ((this.location = (ResourceLocation)resources.get(texture)) != null) {
                    return this.location;
               } else {
                    this.location = new ResourceLocation("moreplayermodels:textures/" + texture + ".png");
                    resources.put(texture, this.location);
                    return this.location;
               }
          }
     }

     public void setType(int type) {
          this.type = (byte)type;
          this.location = null;
     }

     public String toString() {
          return "Color: " + this.color + " Type: " + this.type;
     }

     public String getColor() {
          String str;
          for(str = Integer.toHexString(this.color); str.length() < 6; str = "0" + str) {
          }

          return str;
     }
}
