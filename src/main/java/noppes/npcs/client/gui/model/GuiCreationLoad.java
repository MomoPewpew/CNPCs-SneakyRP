package noppes.npcs.client.gui.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.controllers.Preset;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiCreationLoad extends GuiCreationScreenInterface implements ICustomScrollListener {
     private List list = new ArrayList();
     private GuiCustomScroll scroll;

     public GuiCreationLoad(EntityNPCInterface npc) {
          super(npc);
          this.active = 5;
          this.xOffset = 60;
          PresetController.instance.load();
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
          }

          this.list.clear();
          Iterator var1 = PresetController.instance.presets.values().iterator();

          while(var1.hasNext()) {
               Preset preset = (Preset)var1.next();
               this.list.add(preset.name);
          }

          this.scroll.setList(this.list);
          this.scroll.guiLeft = this.guiLeft;
          this.scroll.guiTop = this.guiTop + 45;
          this.scroll.setSize(100, this.ySize - 96);
          this.addScroll(this.scroll);
          this.addButton(new GuiNpcButton(10, this.guiLeft, this.guiTop + this.ySize - 46, 120, 20, "gui.remove"));
     }

     protected void func_146284_a(GuiButton btn) {
          super.func_146284_a(btn);
          if (btn.field_146127_k == 10 && this.scroll.hasSelected()) {
               PresetController.instance.removePreset(this.scroll.getSelected());
               this.func_73866_w_();
          }

     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
          Preset preset = PresetController.instance.getPreset(scroll.getSelected());
          this.playerdata.readFromNBT(preset.data.writeToNBT());
          this.func_73866_w_();
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
