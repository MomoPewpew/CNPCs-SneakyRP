package noppes.npcs.controllers.data;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.api.entity.data.ILine;

public class Line implements ILine {
     protected String text = "";
     protected String sound = "";
     private boolean showText = true;

     public Line() {
     }

     public Line(String text) {
          this.text = text;
     }

     public Line copy() {
          Line line = new Line(this.text);
          line.sound = this.sound;
          line.showText = this.showText;
          return line;
     }

     public static Line formatTarget(Line line, EntityLivingBase entity) {
          if (entity == null) {
               return line;
          } else {
               Line line2 = line.copy();
               if (entity instanceof EntityPlayer) {
                    line2.text = line2.text.replace("@target", ((EntityPlayer)entity).getDisplayNameString());
               } else {
                    line2.text = line2.text.replace("@target", entity.func_70005_c_());
               }

               return line;
          }
     }

     public String getText() {
          return this.text;
     }

     public void setText(String text) {
          if (text == null) {
               text = "";
          }

          this.text = text;
     }

     public String getSound() {
          return this.sound;
     }

     public void setSound(String sound) {
          if (sound == null) {
               sound = "";
          }

          this.sound = sound;
     }

     public boolean getShowText() {
          return this.showText;
     }

     public void setShowText(boolean show) {
          this.showText = show;
     }
}
