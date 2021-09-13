package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Iterator;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabFactions;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerFactionData;

public class GuiFaction extends GuiNPCInterface implements IGuiData {
     private int xSize = 200;
     private int ySize = 195;
     private int guiLeft;
     private int guiTop;
     private ArrayList playerFactions = new ArrayList();
     private int page = 0;
     private int pages = 1;
     private GuiButtonNextPage buttonNextPage;
     private GuiButtonNextPage buttonPreviousPage;
     private ResourceLocation indicator;

     public GuiFaction() {
          this.drawDefaultBackground = false;
          this.title = "";
          NoppesUtilPlayer.sendData(EnumPlayerPacket.FactionsGet);
          this.indicator = this.getResource("standardbg.png");
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.guiLeft = (this.width - this.xSize) / 2;
          this.guiTop = (this.height - this.ySize) / 2 + 12;
          TabRegistry.updateTabValues(this.guiLeft, this.guiTop + 8, InventoryTabFactions.class);
          TabRegistry.addTabsToList(this.field_146292_n);
          this.field_146292_n.add(this.buttonNextPage = new GuiButtonNextPage(1, this.guiLeft + this.xSize - 43, this.guiTop + 180, true));
          this.field_146292_n.add(this.buttonPreviousPage = new GuiButtonNextPage(2, this.guiLeft + 20, this.guiTop + 180, false));
          this.updateButtons();
     }

     public void func_73863_a(int i, int j, float f) {
          this.func_146276_q_();
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.renderEngine.bindTexture(this.indicator);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop + 8, 0, 0, this.xSize, this.ySize);
          this.drawTexturedModalRect(this.guiLeft + 4, this.guiTop + 8, 56, 0, 200, this.ySize);
          if (this.playerFactions.isEmpty()) {
               String noFaction = I18n.translateToLocal("faction.nostanding");
               this.field_146289_q.func_78276_b(noFaction, this.guiLeft + (this.xSize - this.field_146289_q.getStringWidth(noFaction)) / 2, this.guiTop + 80, CustomNpcResourceListener.DefaultTextColor);
          } else {
               this.renderScreen();
          }

          super.func_73863_a(i, j, f);
     }

     private void renderScreen() {
          int size = 5;
          if (this.playerFactions.size() % 5 != 0 && this.page == this.pages) {
               size = this.playerFactions.size() % 5;
          }

          for(int id = 0; id < size; ++id) {
               this.func_73730_a(this.guiLeft + 2, this.guiLeft + this.xSize, this.guiTop + 14 + id * 30, -16777216 + CustomNpcResourceListener.DefaultTextColor);
               Faction faction = (Faction)this.playerFactions.get((this.page - 1) * 5 + id);
               String name = faction.name;
               String points = " : " + faction.defaultPoints;
               String standing = I18n.translateToLocal("faction.friendly");
               int color = 65280;
               if (faction.defaultPoints < faction.neutralPoints) {
                    standing = I18n.translateToLocal("faction.unfriendly");
                    color = 16711680;
                    points = points + "/" + faction.neutralPoints;
               } else if (faction.defaultPoints < faction.friendlyPoints) {
                    standing = I18n.translateToLocal("faction.neutral");
                    color = 15924992;
                    points = points + "/" + faction.friendlyPoints;
               } else {
                    points = points + "/-";
               }

               this.field_146289_q.func_78276_b(name, this.guiLeft + (this.xSize - this.field_146289_q.getStringWidth(name)) / 2, this.guiTop + 19 + id * 30, faction.color);
               this.field_146289_q.func_78276_b(standing, this.width / 2 - this.field_146289_q.getStringWidth(standing) - 1, this.guiTop + 33 + id * 30, color);
               this.field_146289_q.func_78276_b(points, this.width / 2, this.guiTop + 33 + id * 30, CustomNpcResourceListener.DefaultTextColor);
          }

          this.func_73730_a(this.guiLeft + 2, this.guiLeft + this.xSize, this.guiTop + 14 + size * 30, -16777216 + CustomNpcResourceListener.DefaultTextColor);
          if (this.pages > 1) {
               String s = this.page + "/" + this.pages;
               this.field_146289_q.func_78276_b(s, this.guiLeft + (this.xSize - this.field_146289_q.getStringWidth(s)) / 2, this.guiTop + 203, CustomNpcResourceListener.DefaultTextColor);
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton instanceof GuiButtonNextPage) {
               int id = guibutton.id;
               if (id == 1) {
                    ++this.page;
               }

               if (id == 2) {
                    --this.page;
               }

               this.updateButtons();
          }
     }

     private void updateButtons() {
          this.buttonNextPage.setVisible(this.page < this.pages);
          this.buttonPreviousPage.setVisible(this.page > 1);
     }

     protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
     }

     public void func_73869_a(char c, int i) {
          if (i == 1 || this.isInventoryKey(i)) {
               this.close();
          }

     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          this.playerFactions = new ArrayList();
          NBTTagList list = compound.getTagList("FactionList", 10);

          for(int i = 0; i < list.tagCount(); ++i) {
               Faction faction = new Faction();
               faction.readNBT(list.getCompoundTagAt(i));
               this.playerFactions.add(faction);
          }

          PlayerFactionData data = new PlayerFactionData();
          data.loadNBTData(compound);
          Iterator var10 = data.factionData.keySet().iterator();

          while(var10.hasNext()) {
               int id = (Integer)var10.next();
               int points = (Integer)data.factionData.get(id);
               Iterator var7 = this.playerFactions.iterator();

               while(var7.hasNext()) {
                    Faction faction = (Faction)var7.next();
                    if (faction.id == id) {
                         faction.defaultPoints = points;
                    }
               }
          }

          this.pages = (this.playerFactions.size() - 1) / 5;
          ++this.pages;
          this.page = 1;
          this.updateButtons();
     }
}
