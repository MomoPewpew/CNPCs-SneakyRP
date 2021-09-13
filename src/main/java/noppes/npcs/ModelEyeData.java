package noppes.npcs;

import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.entity.EntityNPCInterface;

public class ModelEyeData extends ModelPartData {
     private Random r = new Random();
     public boolean glint = true;
     public int browThickness = 4;
     public int eyePos = 1;
     public int skinColor = 11830381;
     public int browColor = 5982516;
     public long blinkStart = 0L;

     public ModelEyeData() {
          super("eyes");
          this.color = (new Integer[]{8368696, 16247203, 10526975, 10987431, 10791096, 4210943, 14188339, 11685080, 6724056, 15066419, 8375321, 15892389, 10066329, 5013401, 8339378, 3361970, 6704179, 6717235, 10040115, 16445005, 6085589, 4882687, 55610})[this.r.nextInt(23)];
     }

     public NBTTagCompound writeToNBT() {
          NBTTagCompound compound = super.writeToNBT();
          compound.setBoolean("Glint", this.glint);
          compound.setInteger("SkinColor", this.skinColor);
          compound.setInteger("BrowColor", this.browColor);
          compound.setInteger("PositionY", this.eyePos);
          compound.setInteger("BrowThickness", this.browThickness);
          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          if (!compound.hasNoTags()) {
               super.readFromNBT(compound);
               this.glint = compound.getBoolean("Glint");
               this.skinColor = compound.getInteger("SkinColor");
               this.browColor = compound.getInteger("BrowColor");
               this.eyePos = compound.getInteger("PositionY");
               this.browThickness = compound.getInteger("BrowThickness");
          }
     }

     public boolean isEnabled() {
          return this.type >= 0;
     }

     public void update(EntityNPCInterface npc) {
          if (this.isEnabled() && npc.isEntityAlive() && npc.isServerWorld()) {
               if (this.blinkStart < 0L) {
                    ++this.blinkStart;
               } else if (this.blinkStart == 0L) {
                    if (this.r.nextInt(140) == 1) {
                         this.blinkStart = System.currentTimeMillis();
                         if (npc != null) {
                              Server.sendAssociatedData(npc, EnumPacketClient.EYE_BLINK, npc.getEntityId());
                         }
                    }
               } else if (System.currentTimeMillis() - this.blinkStart > 300L) {
                    this.blinkStart = -20L;
               }

          }
     }
}
