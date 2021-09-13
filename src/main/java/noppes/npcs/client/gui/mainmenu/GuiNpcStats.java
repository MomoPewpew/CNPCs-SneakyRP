package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.SubGuiNpcMeleeProperties;
import noppes.npcs.client.gui.SubGuiNpcProjectiles;
import noppes.npcs.client.gui.SubGuiNpcRangeProperties;
import noppes.npcs.client.gui.SubGuiNpcResistanceProperties;
import noppes.npcs.client.gui.SubGuiNpcRespawn;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataStats;

public class GuiNpcStats extends GuiNPCInterface2 implements ITextfieldListener, IGuiData {
     private DataStats stats;

     public GuiNpcStats(EntityNPCInterface npc) {
          super(npc, 2);
          this.stats = npc.stats;
          Client.sendData(EnumPacketServer.MainmenuStatsGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          int y = this.guiTop + 10;
          this.addLabel(new GuiNpcLabel(0, "stats.health", this.guiLeft + 5, y + 5));
          this.addTextField(new GuiNpcTextField(0, this, this.guiLeft + 85, y, 50, 18, this.stats.maxHealth + ""));
          this.getTextField(0).numbersOnly = true;
          this.getTextField(0).setMinMaxDefault(0, Integer.MAX_VALUE, 20);
          this.addLabel(new GuiNpcLabel(1, "stats.aggro", this.guiLeft + 140, y + 5));
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.guiLeft + 220, y, 50, 18, this.stats.aggroRange + ""));
          this.getTextField(1).numbersOnly = true;
          this.getTextField(1).setMinMaxDefault(1, 64, 2);
          this.addLabel(new GuiNpcLabel(34, "stats.creaturetype", this.guiLeft + 275, y + 5));
          this.addButton(new GuiNpcButton(8, this.guiLeft + 355, y, 56, 20, new String[]{"stats.normal", "stats.undead", "stats.arthropod"}, this.stats.creatureType.ordinal()));
          int var10004 = this.guiLeft + 82;
          y += 22;
          this.addButton(new GuiNpcButton(0, var10004, y, 56, 20, "selectServer.edit"));
          this.addLabel(new GuiNpcLabel(2, "stats.respawn", this.guiLeft + 5, y + 5));
          var10004 = this.guiLeft + 82;
          y += 22;
          this.addButton(new GuiNpcButton(2, var10004, y, 56, 20, "selectServer.edit"));
          this.addLabel(new GuiNpcLabel(5, "stats.meleeproperties", this.guiLeft + 5, y + 5));
          var10004 = this.guiLeft + 82;
          y += 22;
          this.addButton(new GuiNpcButton(3, var10004, y, 56, 20, "selectServer.edit"));
          this.addLabel(new GuiNpcLabel(6, "stats.rangedproperties", this.guiLeft + 5, y + 5));
          this.addButton(new GuiNpcButton(9, this.guiLeft + 217, y, 56, 20, "selectServer.edit"));
          this.addLabel(new GuiNpcLabel(7, "stats.projectileproperties", this.guiLeft + 140, y + 5));
          var10004 = this.guiLeft + 82;
          y += 34;
          this.addButton(new GuiNpcButton(15, var10004, y, 56, 20, "selectServer.edit"));
          this.addLabel(new GuiNpcLabel(15, "effect.resistance", this.guiLeft + 5, y + 5));
          var10004 = this.guiLeft + 82;
          y += 34;
          this.addButton(new GuiNpcButton(4, var10004, y, 56, 20, new String[]{"gui.no", "gui.yes"}, this.npc.func_70045_F() ? 1 : 0));
          this.addLabel(new GuiNpcLabel(10, "stats.fireimmune", this.guiLeft + 5, y + 5));
          this.addButton(new GuiNpcButton(5, this.guiLeft + 217, y, 56, 20, new String[]{"gui.no", "gui.yes"}, this.stats.canDrown ? 1 : 0));
          this.addLabel(new GuiNpcLabel(11, "stats.candrown", this.guiLeft + 140, y + 5));
          this.addTextField((new GuiNpcTextField(14, this, this.guiLeft + 355, y, 56, 20, this.stats.healthRegen + "")).setNumbersOnly());
          this.addLabel(new GuiNpcLabel(14, "stats.regenhealth", this.guiLeft + 275, y + 5));
          int var10005 = this.guiLeft + 355;
          y += 22;
          this.addTextField((new GuiNpcTextField(16, this, var10005, y, 56, 20, this.stats.combatRegen + "")).setNumbersOnly());
          this.addLabel(new GuiNpcLabel(16, "stats.combatregen", this.guiLeft + 275, y + 5));
          this.addButton(new GuiNpcButton(6, this.guiLeft + 82, y, 56, 20, new String[]{"gui.no", "gui.yes"}, this.stats.burnInSun ? 1 : 0));
          this.addLabel(new GuiNpcLabel(12, "stats.burninsun", this.guiLeft + 5, y + 5));
          this.addButton(new GuiNpcButton(7, this.guiLeft + 217, y, 56, 20, new String[]{"gui.no", "gui.yes"}, this.stats.noFallDamage ? 1 : 0));
          this.addLabel(new GuiNpcLabel(13, "stats.nofalldamage", this.guiLeft + 140, y + 5));
          var10004 = this.guiLeft + 82;
          y += 22;
          this.addButton(new GuiNpcButtonYesNo(17, var10004, y, 56, 20, this.stats.potionImmune));
          this.addLabel(new GuiNpcLabel(17, "stats.potionImmune", this.guiLeft + 5, y + 5));
          this.addLabel(new GuiNpcLabel(22, "ai.cobwebAffected", this.guiLeft + 140, y + 5));
          this.addButton(new GuiNpcButton(22, this.guiLeft + 217, y, 56, 20, new String[]{"gui.no", "gui.yes"}, this.stats.ignoreCobweb ? 0 : 1));
     }

     public void unFocused(GuiNpcTextField textfield) {
          if (textfield.field_175208_g == 0) {
               this.stats.maxHealth = textfield.getInteger();
               this.npc.func_70691_i((float)this.stats.maxHealth);
          } else if (textfield.field_175208_g == 1) {
               this.stats.aggroRange = textfield.getInteger();
          } else if (textfield.field_175208_g == 14) {
               this.stats.healthRegen = textfield.getInteger();
          } else if (textfield.field_175208_g == 16) {
               this.stats.combatRegen = textfield.getInteger();
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.field_146127_k == 0) {
               this.setSubGui(new SubGuiNpcRespawn(this.stats));
          } else if (button.field_146127_k == 2) {
               this.setSubGui(new SubGuiNpcMeleeProperties(this.stats.melee));
          } else if (button.field_146127_k == 3) {
               this.setSubGui(new SubGuiNpcRangeProperties(this.stats));
          } else if (button.field_146127_k == 4) {
               this.npc.setImmuneToFire(button.getValue() == 1);
          } else if (button.field_146127_k == 5) {
               this.stats.canDrown = button.getValue() == 1;
          } else if (button.field_146127_k == 6) {
               this.stats.burnInSun = button.getValue() == 1;
          } else if (button.field_146127_k == 7) {
               this.stats.noFallDamage = button.getValue() == 1;
          } else if (button.field_146127_k == 8) {
               this.stats.creatureType = EnumCreatureAttribute.values()[button.getValue()];
          } else if (button.field_146127_k == 9) {
               this.setSubGui(new SubGuiNpcProjectiles(this.stats.ranged));
          } else if (button.field_146127_k == 15) {
               this.setSubGui(new SubGuiNpcResistanceProperties(this.stats.resistances));
          } else if (button.field_146127_k == 17) {
               this.stats.potionImmune = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          } else if (button.field_146127_k == 22) {
               this.stats.ignoreCobweb = button.getValue() == 0;
          }

     }

     public void save() {
          Client.sendData(EnumPacketServer.MainmenuStatsSave, this.stats.writeToNBT(new NBTTagCompound()));
     }

     public void setGuiData(NBTTagCompound compound) {
          this.stats.readToNBT(compound);
          this.func_73866_w_();
     }
}
