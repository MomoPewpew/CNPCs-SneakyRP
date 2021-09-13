package noppes.npcs.client.gui;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NaturalOrderComparator;

public abstract class GuiNpcSelectionInterface extends GuiNPCInterface {
     public GuiNPCStringSlot slot;
     public GuiScreen parent;
     private String up = "..<" + I18n.func_74838_a("gui.up") + ">..";
     private String root = "";
     public AssetsBrowser assets;
     private HashSet dataFolder = new HashSet();
     protected HashSet dataTextures = new HashSet();

     public GuiNpcSelectionInterface(EntityNPCInterface npc, GuiScreen parent, String sound) {
          super(npc);
          this.root = AssetsBrowser.getRoot(sound);
          this.assets = new AssetsBrowser(this.root, this.getExtension());
          this.drawDefaultBackground = false;
          this.title = "";
          this.parent = parent;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.dataFolder.clear();
          String ss = "Current Folder: /assets" + this.root;
          this.addLabel(new GuiNpcLabel(0, ss, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(ss) / 2, 20, 16777215));
          Vector list = new Vector();
          if (!this.assets.isRoot) {
               list.add(this.up);
          }

          Iterator var3 = this.assets.folders.iterator();

          String texture;
          while(var3.hasNext()) {
               texture = (String)var3.next();
               list.add("/" + texture);
               this.dataFolder.add("/" + texture);
          }

          var3 = this.assets.files.iterator();

          while(var3.hasNext()) {
               texture = (String)var3.next();
               list.add(texture);
               this.dataTextures.add(texture);
          }

          Collections.sort(list, new NaturalOrderComparator());
          this.slot = new GuiNPCStringSlot(list, this, false, 18);
          this.slot.func_148134_d(4, 5);
          this.addButton(new GuiNpcButton(2, this.field_146294_l / 2 - 100, this.field_146295_m - 44, 98, 20, "gui.back"));
          this.addButton(new GuiNpcButton(3, this.field_146294_l / 2 + 2, this.field_146295_m - 44, 98, 20, "gui.up"));
          this.getButton(3).field_146124_l = !this.assets.isRoot;
     }

     public void func_73863_a(int i, int j, float f) {
          this.slot.func_148128_a(i, j, f);
          super.func_73863_a(i, j, f);
     }

     public void elementClicked() {
          if (this.slot.selected != null && this.dataTextures.contains(this.slot.selected)) {
               if (this.parent instanceof GuiNPCInterface) {
                    ((GuiNPCInterface)this.parent).elementClicked();
               } else if (this.parent instanceof GuiNPCInterface2) {
                    ((GuiNPCInterface2)this.parent).elementClicked();
               }
          }

     }

     public void func_146274_d() throws IOException {
          this.slot.func_178039_p();
          super.func_146274_d();
     }

     public void doubleClicked() {
          if (this.slot.selected.equals(this.up)) {
               this.root = this.root.substring(0, this.root.lastIndexOf("/"));
               this.assets = new AssetsBrowser(this.root, this.getExtension());
               this.func_73866_w_();
          } else if (this.dataFolder.contains(this.slot.selected)) {
               this.root = this.root + this.slot.selected;
               this.assets = new AssetsBrowser(this.root, this.getExtension());
               this.func_73866_w_();
          } else {
               this.close();
               NoppesUtil.openGUI(this.player, this.parent);
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.field_146127_k;
          if (id == 2) {
               this.close();
               NoppesUtil.openGUI(this.player, this.parent);
          }

          if (id == 3) {
               this.root = this.root.substring(0, this.root.lastIndexOf("/"));
               this.assets = new AssetsBrowser(this.root, this.getExtension());
               this.func_73866_w_();
          }

     }

     public void save() {
     }

     public String getSelected() {
          return this.assets.getAsset(this.slot.selected);
     }

     public abstract String[] getExtension();
}
