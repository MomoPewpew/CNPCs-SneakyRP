package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcTextureCloaks extends GuiNpcSelectionInterface {
     public GuiNpcTextureCloaks(EntityNPCInterface npc, GuiScreen parent) {
          super(npc, parent, npc.display.getCapeTexture().isEmpty() ? "customnpcs:textures/cloak/" : npc.display.getCapeTexture());
          this.title = "Select Cloak";
     }

     public void initGui() {
          super.initGui();
          int index = this.npc.display.getCapeTexture().lastIndexOf("/");
          if (index > 0) {
               String asset = this.npc.display.getCapeTexture().substring(index + 1);
               if (this.npc.display.getCapeTexture().equals(this.assets.getAsset(asset))) {
                    this.slot.selected = asset;
               }
          }

     }

     public void drawScreen(int i, int j, float f) {
          int l = -50;
          int i1 = this.height / 2 + 30;
          this.drawNpc(this.npc, l, i1, 2.0F, 180);
          super.drawScreen(i, j, f);
     }

     public void elementClicked() {
          if (this.dataTextures.contains(this.slot.selected) && this.slot.selected != null) {
               this.npc.display.setCapeTexture(this.assets.getAsset(this.slot.selected));
          }

     }

     public void save() {
     }

     public String[] getExtension() {
          return new String[]{"png"};
     }
}
