package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.entity.data.DataAI;

public class SubGuiNpcMovement extends SubGuiInterface implements ITextfieldListener {
     private DataAI ai;

     public SubGuiNpcMovement(DataAI ai) {
          this.ai = ai;
          this.setBackground("menubg.png");
          this.xSize = 256;
          this.ySize = 216;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int y = this.guiTop + 4;
          this.addLabel(new GuiNpcLabel(0, "movement.type", this.guiLeft + 4, y + 5));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 80, y, 100, 20, new String[]{"ai.standing", "ai.wandering", "ai.movingpath"}, this.ai.getMovingType()));
          int var10004 = this.guiLeft + 80;
          y += 22;
          this.addButton(new GuiNpcButton(15, var10004, y, 100, 20, new String[]{"movement.ground", "movement.flying", "movement.swimming"}, this.ai.movementType));
          this.addLabel(new GuiNpcLabel(15, "movement.navigation", this.guiLeft + 4, y + 5));
          int var10005;
          if (this.ai.getMovingType() == 1) {
               var10005 = this.guiLeft + 100;
               y += 22;
               this.addTextField(new GuiNpcTextField(4, this, var10005, y, 40, 20, this.ai.walkingRange + ""));
               this.getTextField(4).numbersOnly = true;
               this.getTextField(4).setMinMaxDefault(0, 1000, 10);
               this.addLabel(new GuiNpcLabel(4, "gui.range", this.guiLeft + 4, y + 5));
               var10004 = this.guiLeft + 100;
               y += 22;
               this.addButton(new GuiNpcButton(5, var10004, y, 50, 20, new String[]{"gui.no", "gui.yes"}, this.ai.npcInteracting ? 1 : 0));
               this.addLabel(new GuiNpcLabel(5, "movement.wanderinteract", this.guiLeft + 4, y + 5));
               var10004 = this.guiLeft + 80;
               y += 22;
               this.addButton(new GuiNpcButton(9, var10004, y, 80, 20, new String[]{"gui.no", "gui.yes"}, this.ai.movingPause ? 1 : 0));
               this.addLabel(new GuiNpcLabel(9, "movement.pauses", this.guiLeft + 4, y + 5));
          } else if (this.ai.getMovingType() == 0) {
               var10005 = this.guiLeft + 99;
               y += 22;
               this.addTextField(new GuiNpcTextField(7, this, var10005, y, 24, 20, (int)this.ai.bodyOffsetX + ""));
               this.addLabel(new GuiNpcLabel(17, "spawner.posoffset", this.guiLeft + 4, y + 5));
               this.addLabel(new GuiNpcLabel(7, "X:", this.guiLeft + 115, y + 5));
               this.getTextField(7).numbersOnly = true;
               this.getTextField(7).setMinMaxDefault(0, 10, 5);
               this.addLabel(new GuiNpcLabel(8, "Y:", this.guiLeft + 125, y + 5));
               this.addTextField(new GuiNpcTextField(8, this, this.guiLeft + 135, y, 24, 20, (int)this.ai.bodyOffsetY + ""));
               this.getTextField(8).numbersOnly = true;
               this.getTextField(8).setMinMaxDefault(0, 10, 5);
               this.addLabel(new GuiNpcLabel(9, "Z:", this.guiLeft + 161, y + 5));
               this.addTextField(new GuiNpcTextField(9, this, this.guiLeft + 171, y, 24, 20, (int)this.ai.bodyOffsetZ + ""));
               this.getTextField(9).numbersOnly = true;
               this.getTextField(9).setMinMaxDefault(0, 10, 5);
               var10004 = this.guiLeft + 80;
               y += 22;
               this.addButton(new GuiNpcButton(3, var10004, y, 100, 20, new String[]{"stats.normal", "movement.sitting", "movement.lying", "movement.hug", "movement.sneaking", "movement.dancing", "movement.aiming", "movement.crawling"}, this.ai.animationType));
               this.addLabel(new GuiNpcLabel(3, "movement.animation", this.guiLeft + 4, y + 5));
               if (this.ai.animationType != 2) {
                    var10004 = this.guiLeft + 80;
                    y += 22;
                    this.addButton(new GuiNpcButton(4, var10004, y, 80, 20, new String[]{"movement.body", "movement.manual", "movement.stalking", "movement.head"}, this.ai.getStandingType()));
                    this.addLabel(new GuiNpcLabel(1, "movement.rotation", this.guiLeft + 4, y + 5));
               } else {
                    var10005 = this.guiLeft + 99;
                    y += 22;
                    this.addTextField(new GuiNpcTextField(5, this, var10005, y, 40, 20, this.ai.orientation + ""));
                    this.getTextField(5).numbersOnly = true;
                    this.getTextField(5).setMinMaxDefault(0, 359, 0);
                    this.addLabel(new GuiNpcLabel(6, "movement.rotation", this.guiLeft + 4, y + 5));
                    this.addLabel(new GuiNpcLabel(5, "(0-359)", this.guiLeft + 142, y + 5));
               }

               if (this.ai.getStandingType() == 1 || this.ai.getStandingType() == 3) {
                    this.addTextField(new GuiNpcTextField(5, this, this.guiLeft + 165, y, 40, 20, this.ai.orientation + ""));
                    this.getTextField(5).numbersOnly = true;
                    this.getTextField(5).setMinMaxDefault(0, 359, 0);
                    this.addLabel(new GuiNpcLabel(5, "(0-359)", this.guiLeft + 207, y + 5));
               }
          }

          if (this.ai.getMovingType() != 0) {
               var10004 = this.guiLeft + 80;
               y += 22;
               this.addButton(new GuiNpcButton(12, var10004, y, 100, 20, new String[]{"stats.normal", "movement.sneaking", "movement.aiming", "movement.dancing", "movement.crawling", "movement.hug"}, EntityAIAnimation.getWalkingAnimationGuiIndex(this.ai.animationType)));
               this.addLabel(new GuiNpcLabel(12, "movement.animation", this.guiLeft + 4, y + 5));
          }

          if (this.ai.getMovingType() == 2) {
               var10004 = this.guiLeft + 80;
               y += 22;
               this.addButton(new GuiNpcButton(8, var10004, y, 80, 20, new String[]{"ai.looping", "ai.backtracking"}, this.ai.movingPattern));
               this.addLabel(new GuiNpcLabel(8, "movement.name", this.guiLeft + 4, y + 5));
               var10004 = this.guiLeft + 80;
               y += 22;
               this.addButton(new GuiNpcButton(9, var10004, y, 80, 20, new String[]{"gui.no", "gui.yes"}, this.ai.movingPause ? 1 : 0));
               this.addLabel(new GuiNpcLabel(9, "movement.pauses", this.guiLeft + 4, y + 5));
          }

          var10004 = this.guiLeft + 100;
          y += 22;
          this.addButton(new GuiNpcButton(13, var10004, y, 50, 20, new String[]{"gui.no", "gui.yes"}, this.ai.stopAndInteract ? 1 : 0));
          this.addLabel(new GuiNpcLabel(13, "movement.stopinteract", this.guiLeft + 4, y + 5));
          var10005 = this.guiLeft + 80;
          y += 22;
          this.addTextField(new GuiNpcTextField(14, this, var10005, y, 50, 18, this.ai.getWalkingSpeed() + ""));
          this.getTextField(14).numbersOnly = true;
          this.getTextField(14).setMinMaxDefault(0, 10, 4);
          this.addLabel(new GuiNpcLabel(14, "stats.movespeed", this.guiLeft + 5, y + 5));
          this.addButton(new GuiNpcButton(66, this.guiLeft + 190, this.guiTop + 190, 60, 20, "gui.done"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.field_146127_k == 0) {
               this.ai.setMovingType(button.getValue());
               if (this.ai.getMovingType() != 0) {
                    this.ai.animationType = 0;
                    this.ai.setStandingType(0);
                    this.ai.bodyOffsetX = this.ai.bodyOffsetY = this.ai.bodyOffsetZ = 5.0F;
               }

               this.func_73866_w_();
          } else if (button.field_146127_k == 3) {
               this.ai.animationType = button.getValue();
               this.func_73866_w_();
          } else if (button.field_146127_k == 4) {
               this.ai.setStandingType(button.getValue());
               this.func_73866_w_();
          } else if (button.field_146127_k == 5) {
               this.ai.npcInteracting = button.getValue() == 1;
          } else if (button.field_146127_k == 8) {
               this.ai.movingPattern = button.getValue();
          } else if (button.field_146127_k == 9) {
               this.ai.movingPause = button.getValue() == 1;
          } else if (button.field_146127_k == 12) {
               if (button.getValue() == 0) {
                    this.ai.animationType = 0;
               }

               if (button.getValue() == 1) {
                    this.ai.animationType = 4;
               }

               if (button.getValue() == 2) {
                    this.ai.animationType = 6;
               }

               if (button.getValue() == 3) {
                    this.ai.animationType = 5;
               }

               if (button.getValue() == 4) {
                    this.ai.animationType = 7;
               }

               if (button.getValue() == 5) {
                    this.ai.animationType = 3;
               }
          } else if (button.field_146127_k == 13) {
               this.ai.stopAndInteract = button.getValue() == 1;
          } else if (button.field_146127_k == 15) {
               this.ai.movementType = button.getValue();
          } else if (button.field_146127_k == 66) {
               this.close();
          }

     }

     public void unFocused(GuiNpcTextField textfield) {
          if (textfield.field_175208_g == 7) {
               this.ai.bodyOffsetX = (float)textfield.getInteger();
          } else if (textfield.field_175208_g == 8) {
               this.ai.bodyOffsetY = (float)textfield.getInteger();
          } else if (textfield.field_175208_g == 9) {
               this.ai.bodyOffsetZ = (float)textfield.getInteger();
          } else if (textfield.field_175208_g == 5) {
               this.ai.orientation = textfield.getInteger();
          } else if (textfield.field_175208_g == 4) {
               this.ai.walkingRange = textfield.getInteger();
          } else if (textfield.field_175208_g == 14) {
               this.ai.setWalkingSpeed(textfield.getInteger());
          }

     }
}
