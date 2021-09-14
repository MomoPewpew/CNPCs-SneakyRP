package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNPCTextures extends GuiNpcSelectionInterface {
     public GuiNPCTextures(EntityNPCInterface npc, GuiScreen parent) {
          super(npc, parent, npc.display.getSkinTexture());
          this.title = "Select Texture";
          this.parent = parent;
     }

     public void initGui() {
          super.initGui();
          int index = this.npc.display.getSkinTexture().lastIndexOf("/");
          if (index > 0) {
               String asset = this.npc.display.getSkinTexture().substring(index + 1);
               if (this.npc.display.getSkinTexture().equals(this.assets.getAsset(asset))) {
                    this.slot.selected = asset;
               }
          }

     }

     public void drawScreen(int i, int j, float f) {
          int l = -50;
          int i1 = this.height / 2 + 30;
          this.drawNpc(this.npc, l, i1, 2.0F, 0);
          super.drawScreen(i, j, f);
     }

     public void elementClicked() {
          if (this.dataTextures.contains(this.slot.selected) && this.slot.selected != null) {
               this.npc.display.setSkinTexture(this.assets.getAsset(this.slot.selected));
               this.npc.textureLocation = null;
          }

     }

     public void save() {
     }

     public String[] getExtension() {
          return new String[]{"png"};
     }
}
