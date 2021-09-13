package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.world.biome.Biome;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.data.SpawnData;

public class SubGuiNpcBiomes extends SubGuiInterface {
     private SpawnData data;
     private GuiCustomScroll scroll1;
     private GuiCustomScroll scroll2;

     public SubGuiNpcBiomes(SpawnData data) {
          this.data = data;
          this.setBackground("menubg.png");
          this.xSize = 346;
          this.ySize = 216;
          this.closeOnEsc = true;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.scroll1 == null) {
               this.scroll1 = new GuiCustomScroll(this, 0);
               this.scroll1.setSize(140, 180);
          }

          this.scroll1.guiLeft = this.guiLeft + 4;
          this.scroll1.guiTop = this.guiTop + 14;
          this.addScroll(this.scroll1);
          this.addLabel(new GuiNpcLabel(1, "spawning.availableBiomes", this.guiLeft + 4, this.guiTop + 4));
          if (this.scroll2 == null) {
               this.scroll2 = new GuiCustomScroll(this, 1);
               this.scroll2.setSize(140, 180);
          }

          this.scroll2.guiLeft = this.guiLeft + 200;
          this.scroll2.guiTop = this.guiTop + 14;
          this.addScroll(this.scroll2);
          this.addLabel(new GuiNpcLabel(2, "spawning.spawningBiomes", this.guiLeft + 200, this.guiTop + 4));
          List biomes = new ArrayList();
          Iterator ita = Biome.field_185377_q.iterator();

          while(ita.hasNext()) {
               Biome base = (Biome)ita.next();
               if (base != null && base.func_185359_l() != null && !this.data.biomes.contains(base.func_185359_l())) {
                    biomes.add(base.func_185359_l());
               }
          }

          this.scroll1.setList(biomes);
          this.scroll2.setList(this.data.biomes);
          this.addButton(new GuiNpcButton(1, this.guiLeft + 145, this.guiTop + 40, 55, 20, ">"));
          this.addButton(new GuiNpcButton(2, this.guiLeft + 145, this.guiTop + 62, 55, 20, "<"));
          this.addButton(new GuiNpcButton(3, this.guiLeft + 145, this.guiTop + 90, 55, 20, ">>"));
          this.addButton(new GuiNpcButton(4, this.guiLeft + 145, this.guiTop + 112, 55, 20, "<<"));
          this.addButton(new GuiNpcButton(66, this.guiLeft + 260, this.guiTop + 194, 60, 20, "gui.done"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.field_146127_k == 1 && this.scroll1.hasSelected()) {
               this.data.biomes.add(this.scroll1.getSelected());
               this.scroll1.selected = -1;
               this.scroll1.selected = -1;
               this.func_73866_w_();
          }

          if (button.field_146127_k == 2 && this.scroll2.hasSelected()) {
               this.data.biomes.remove(this.scroll2.getSelected());
               this.scroll2.selected = -1;
               this.func_73866_w_();
          }

          if (button.field_146127_k == 3) {
               this.data.biomes.clear();
               Iterator ita = Biome.field_185377_q.iterator();

               while(ita.hasNext()) {
                    Biome base = (Biome)ita.next();
                    if (base != null) {
                         this.data.biomes.add(base.func_185359_l());
                    }
               }

               this.scroll1.selected = -1;
               this.scroll1.selected = -1;
               this.func_73866_w_();
          }

          if (button.field_146127_k == 4) {
               this.data.biomes.clear();
               this.scroll1.selected = -1;
               this.scroll1.selected = -1;
               this.func_73866_w_();
          }

          if (button.field_146127_k == 66) {
               this.close();
          }

     }
}
