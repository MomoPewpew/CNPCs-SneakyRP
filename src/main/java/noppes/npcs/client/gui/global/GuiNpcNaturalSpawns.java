package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.GuiNpcMobSpawnerSelector;
import noppes.npcs.client.gui.SubGuiNpcBiomes;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcNaturalSpawns extends GuiNPCInterface2 implements IGuiData, IScrollData, ITextfieldListener, ICustomScrollListener, ISliderListener {
     private GuiCustomScroll scroll;
     private HashMap data = new HashMap();
     private SpawnData spawn = new SpawnData();

     public GuiNpcNaturalSpawns(EntityNPCInterface npc) {
          super(npc);
          Client.sendData(EnumPacketServer.NaturalSpawnGetAll);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setSize(143, 208);
          }

          this.scroll.guiLeft = this.guiLeft + 214;
          this.scroll.guiTop = this.guiTop + 4;
          this.addScroll(this.scroll);
          this.addButton(new GuiNpcButton(1, this.guiLeft + 358, this.guiTop + 38, 58, 20, "gui.add"));
          this.addButton(new GuiNpcButton(2, this.guiLeft + 358, this.guiTop + 61, 58, 20, "gui.remove"));
          if (this.spawn.id >= 0) {
               this.showSpawn();
          }

     }

     private void showSpawn() {
          this.addLabel(new GuiNpcLabel(1, "gui.title", this.guiLeft + 4, this.guiTop + 8));
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.guiLeft + 60, this.guiTop + 3, 140, 20, this.spawn.name));
          this.addLabel(new GuiNpcLabel(3, "spawning.biomes", this.guiLeft + 4, this.guiTop + 30));
          this.addButton(new GuiNpcButton(3, this.guiLeft + 120, this.guiTop + 25, 50, 20, "selectServer.edit"));
          this.addSlider(new GuiNpcSlider(this, 4, this.guiLeft + 4, this.guiTop + 47, 180, 20, (float)this.spawn.field_76292_a / 100.0F));
          int y = this.guiTop + 70;
          this.addButton(new GuiNpcButton(25, this.guiLeft + 14, y, 20, 20, "X"));
          this.addLabel(new GuiNpcLabel(5, "1:", this.guiLeft + 4, y + 5));
          this.addButton(new GuiNpcButton(5, this.guiLeft + 36, y, 170, 20, this.getTitle(this.spawn.compound1)));
          this.addLabel(new GuiNpcLabel(26, "gui.type", this.guiLeft + 4, this.guiTop + 100));
          this.addButton(new GuiNpcButton(27, this.guiLeft + 70, this.guiTop + 93, 120, 20, new String[]{"spawner.any", "spawner.dark", "spawner.light"}, this.spawn.type));
     }

     private String getTitle(NBTTagCompound compound) {
          return compound != null && compound.func_74764_b("ClonedName") ? compound.func_74779_i("ClonedName") : "gui.selectnpc";
     }

     public void buttonEvent(GuiButton guibutton) {
          int id = guibutton.field_146127_k;
          if (id == 1) {
               this.save();

               String name;
               for(name = I18n.func_74838_a("gui.new"); this.data.containsKey(name); name = name + "_") {
               }

               SpawnData spawn = new SpawnData();
               spawn.name = name;
               Client.sendData(EnumPacketServer.NaturalSpawnSave, spawn.writeNBT(new NBTTagCompound()));
          }

          if (id == 2 && this.data.containsKey(this.scroll.getSelected())) {
               Client.sendData(EnumPacketServer.NaturalSpawnRemove, this.spawn.id);
               this.spawn = new SpawnData();
               this.scroll.clear();
          }

          if (id == 3) {
               this.setSubGui(new SubGuiNpcBiomes(this.spawn));
          }

          if (id == 5) {
               this.setSubGui(new GuiNpcMobSpawnerSelector());
          }

          if (id == 25) {
               this.spawn.compound1 = new NBTTagCompound();
               this.func_73866_w_();
          }

          if (id == 27) {
               this.spawn.type = ((GuiNpcButton)guibutton).getValue();
          }

     }

     public void unFocused(GuiNpcTextField guiNpcTextField) {
          String name = guiNpcTextField.func_146179_b();
          if (!name.isEmpty() && !this.data.containsKey(name)) {
               String old = this.spawn.name;
               this.data.remove(old);
               this.spawn.name = name;
               this.data.put(this.spawn.name, this.spawn.id);
               this.scroll.replace(old, this.spawn.name);
          } else {
               guiNpcTextField.func_146180_a(this.spawn.name);
          }

     }

     public void setData(Vector list, HashMap data) {
          String name = this.scroll.getSelected();
          this.data = data;
          this.scroll.setList(list);
          if (name != null) {
               this.scroll.setSelected(name);
          }

          this.func_73866_w_();
     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
          if (guiCustomScroll.id == 0) {
               this.save();
               String selected = this.scroll.getSelected();
               this.spawn = new SpawnData();
               Client.sendData(EnumPacketServer.NaturalSpawnGet, this.data.get(selected));
          }

     }

     public void save() {
          GuiNpcTextField.unfocus();
          if (this.spawn.id >= 0) {
               Client.sendData(EnumPacketServer.NaturalSpawnSave, this.spawn.writeNBT(new NBTTagCompound()));
          }

     }

     public void setSelected(String selected) {
     }

     public void closeSubGui(SubGuiInterface gui) {
          super.closeSubGui(gui);
          if (gui instanceof GuiNpcMobSpawnerSelector) {
               GuiNpcMobSpawnerSelector selector = (GuiNpcMobSpawnerSelector)gui;
               NBTTagCompound compound = selector.getCompound();
               if (compound != null) {
                    this.spawn.compound1 = compound;
               }

               this.func_73866_w_();
          }

     }

     public void setGuiData(NBTTagCompound compound) {
          this.spawn.readNBT(compound);
          this.setSelected(this.spawn.name);
          this.func_73866_w_();
     }

     public void mouseDragged(GuiNpcSlider guiNpcSlider) {
          guiNpcSlider.field_146126_j = I18n.func_74838_a("spawning.weightedChance") + ": " + (int)(guiNpcSlider.sliderValue * 100.0F);
     }

     public void mousePressed(GuiNpcSlider guiNpcSlider) {
     }

     public void mouseReleased(GuiNpcSlider guiNpcSlider) {
          this.spawn.field_76292_a = (int)(guiNpcSlider.sliderValue * 100.0F);
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
