package noppes.npcs.client.gui.mainmenu;

import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.containers.ContainerNPCInv;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCInv extends GuiContainerNPCInterface2 implements ISliderListener, IGuiData {
     private HashMap chances = new HashMap();
     private ContainerNPCInv container;
     private ResourceLocation slot;

     public GuiNPCInv(EntityNPCInterface npc, ContainerNPCInv container) {
          super(npc, container, 3);
          this.setBackground("npcinv.png");
          this.container = container;
          this.field_147000_g = 200;
          this.slot = this.getResource("slot.png");
          Client.sendData(EnumPacketServer.MainmenuInvGet);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addLabel(new GuiNpcLabel(0, "inv.minExp", this.field_147003_i + 118, this.field_147009_r + 18));
          this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.field_147003_i + 108, this.field_147009_r + 29, 60, 20, this.npc.inventory.getExpMin() + ""));
          this.getTextField(0).numbersOnly = true;
          this.getTextField(0).setMinMaxDefault(0, 32767, 0);
          this.addLabel(new GuiNpcLabel(1, "inv.maxExp", this.field_147003_i + 118, this.field_147009_r + 52));
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.field_147003_i + 108, this.field_147009_r + 63, 60, 20, this.npc.inventory.getExpMax() + ""));
          this.getTextField(1).numbersOnly = true;
          this.getTextField(1).setMinMaxDefault(0, 32767, 0);
          this.addButton(new GuiNpcButton(10, this.field_147003_i + 88, this.field_147009_r + 88, 80, 20, new String[]{"stats.normal", "inv.auto"}, this.npc.inventory.lootMode));
          this.addLabel(new GuiNpcLabel(2, "inv.npcInventory", this.field_147003_i + 191, this.field_147009_r + 5));
          this.addLabel(new GuiNpcLabel(3, "inv.inventory", this.field_147003_i + 8, this.field_147009_r + 101));

          for(int i = 0; i < 9; ++i) {
               int chance = 100;
               if (this.npc.inventory.dropchance.containsKey(i)) {
                    chance = (Integer)this.npc.inventory.dropchance.get(i);
               }

               if (chance <= 0 || chance > 100) {
                    chance = 100;
               }

               this.chances.put(i, chance);
               GuiNpcSlider slider = new GuiNpcSlider(this, i, this.field_147003_i + 211, this.field_147009_r + 14 + i * 21, (float)chance / 100.0F);
               this.addSlider(slider);
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.id == 10) {
               this.npc.inventory.lootMode = ((GuiNpcButton)guibutton).getValue();
          }

     }

     protected void func_146976_a(float f, int i, int j) {
          super.func_146976_a(f, i, j);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.slot);

          for(int id = 4; id <= 6; ++id) {
               Slot slot = this.container.func_75139_a(id);
               if (slot.getHasStack()) {
                    this.drawTexturedModalRect(this.field_147003_i + slot.field_75223_e - 1, this.field_147009_r + slot.field_75221_f - 1, 0, 0, 18, 18);
               }
          }

     }

     public void func_73863_a(int i, int j, float f) {
          int showname = this.npc.display.getShowName();
          this.npc.display.setShowName(1);
          this.drawNpc(50, 84);
          this.npc.display.setShowName(showname);
          super.func_73863_a(i, j, f);
     }

     public void save() {
          this.npc.inventory.dropchance = this.chances;
          this.npc.inventory.setExp(this.getTextField(0).getInteger(), this.getTextField(1).getInteger());
          Client.sendData(EnumPacketServer.MainmenuInvSave, this.npc.inventory.writeEntityToNBT(new NBTTagCompound()));
     }

     public void setGuiData(NBTTagCompound compound) {
          this.npc.inventory.readEntityFromNBT(compound);
          this.func_73866_w_();
     }

     public void mouseDragged(GuiNpcSlider guiNpcSlider) {
          guiNpcSlider.field_146126_j = I18n.translateToLocal("inv.dropChance") + ": " + (int)(guiNpcSlider.sliderValue * 100.0F) + "%";
     }

     public void mousePressed(GuiNpcSlider guiNpcSlider) {
     }

     public void mouseReleased(GuiNpcSlider guiNpcSlider) {
          this.chances.put(guiNpcSlider.id, (int)(guiNpcSlider.sliderValue * 100.0F));
     }
}
