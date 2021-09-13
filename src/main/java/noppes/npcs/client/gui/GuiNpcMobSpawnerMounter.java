package noppes.npcs.client.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noppes.npcs.client.Client;
import noppes.npcs.client.controllers.ClientCloneController;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNpcMobSpawnerMounter extends GuiNPCInterface implements IGuiData {
     private GuiCustomScroll scroll;
     private int posX;
     private int posY;
     private int posZ;
     private List list;
     private static int showingClones = 0;
     private static String search = "";
     private int activeTab = 1;

     public GuiNpcMobSpawnerMounter(int i, int j, int k) {
          this.xSize = 256;
          this.posX = i;
          this.posY = j;
          this.posZ = k;
          this.closeOnEsc = true;
          this.setBackground("menubg.png");
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.guiTop += 10;
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setSize(165, 188);
          } else {
               this.scroll.clear();
          }

          this.scroll.guiLeft = this.guiLeft + 4;
          this.scroll.guiTop = this.guiTop + 26;
          this.addScroll(this.scroll);
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.guiLeft + 4, this.guiTop + 4, 165, 20, search));
          GuiMenuTopButton button;
          this.addTopButton(button = new GuiMenuTopButton(3, this.guiLeft + 4, this.guiTop - 17, "spawner.clones"));
          button.active = showingClones == 0;
          this.addTopButton(button = new GuiMenuTopButton(4, button, "spawner.entities"));
          button.active = showingClones == 1;
          this.addTopButton(button = new GuiMenuTopButton(5, button, "gui.server"));
          button.active = showingClones == 2;
          this.addButton(new GuiNpcButton(1, this.guiLeft + 170, this.guiTop + 6, 82, 20, "spawner.mount"));
          this.addButton(new GuiNpcButton(2, this.guiLeft + 170, this.guiTop + 50, 82, 20, "spawner.mountplayer"));
          if (showingClones != 0 && showingClones != 2) {
               this.showEntities();
          } else {
               this.addSideButton(new GuiMenuSideButton(21, this.guiLeft - 69, this.guiTop + 2, 70, 22, "Tab 1"));
               this.addSideButton(new GuiMenuSideButton(22, this.guiLeft - 69, this.guiTop + 23, 70, 22, "Tab 2"));
               this.addSideButton(new GuiMenuSideButton(23, this.guiLeft - 69, this.guiTop + 44, 70, 22, "Tab 3"));
               this.addSideButton(new GuiMenuSideButton(24, this.guiLeft - 69, this.guiTop + 65, 70, 22, "Tab 4"));
               this.addSideButton(new GuiMenuSideButton(25, this.guiLeft - 69, this.guiTop + 86, 70, 22, "Tab 5"));
               this.addSideButton(new GuiMenuSideButton(26, this.guiLeft - 69, this.guiTop + 107, 70, 22, "Tab 6"));
               this.addSideButton(new GuiMenuSideButton(27, this.guiLeft - 69, this.guiTop + 128, 70, 22, "Tab 7"));
               this.addSideButton(new GuiMenuSideButton(28, this.guiLeft - 69, this.guiTop + 149, 70, 22, "Tab 8"));
               this.addSideButton(new GuiMenuSideButton(29, this.guiLeft - 69, this.guiTop + 170, 70, 22, "Tab 9"));
               this.getSideButton(20 + this.activeTab).active = true;
               this.showClones();
          }

     }

     private void showEntities() {
          ArrayList list = new ArrayList();
          Iterator var2 = ForgeRegistries.ENTITIES.getValues().iterator();

          while(var2.hasNext()) {
               EntityEntry ent = (EntityEntry)var2.next();
               Class c = ent.getEntityClass();
               String name = ent.getName();

               try {
                    if (EntityLiving.class.isAssignableFrom(c) && c.getConstructor(World.class) != null && !Modifier.isAbstract(c.getModifiers())) {
                         list.add(name.toString());
                    }
               } catch (SecurityException var7) {
                    var7.printStackTrace();
               } catch (NoSuchMethodException var8) {
               }
          }

          this.list = list;
          this.scroll.setList(this.getSearchList());
     }

     private void showClones() {
          if (showingClones == 2) {
               Client.sendData(EnumPacketServer.CloneList, this.activeTab);
          } else {
               new ArrayList();
               this.list = ClientCloneController.Instance.getClones(this.activeTab);
               this.scroll.setList(this.getSearchList());
          }
     }

     public void func_73869_a(char c, int i) {
          super.func_73869_a(c, i);
          if (!search.equals(this.getTextField(1).func_146179_b())) {
               search = this.getTextField(1).func_146179_b().toLowerCase();
               this.scroll.setList(this.getSearchList());
          }
     }

     private List getSearchList() {
          if (search.isEmpty()) {
               return new ArrayList(this.list);
          } else {
               List list = new ArrayList();
               Iterator var2 = this.list.iterator();

               while(var2.hasNext()) {
                    String name = (String)var2.next();
                    if (name.toLowerCase().contains(search)) {
                         list.add(name);
                    }
               }

               return list;
          }
     }

     private NBTTagCompound getCompound() {
          String sel = this.scroll.getSelected();
          if (sel == null) {
               return null;
          } else if (showingClones == 0) {
               return ClientCloneController.Instance.getCloneData(this.player, sel, this.activeTab);
          } else {
               Entity entity = EntityList.func_188429_b(new ResourceLocation(sel), Minecraft.getMinecraft().field_71441_e);
               if (entity == null) {
                    return null;
               } else {
                    NBTTagCompound compound = new NBTTagCompound();
                    entity.func_184198_c(compound);
                    return compound;
               }
          }
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 0) {
               this.close();
          }

          if (id == 1) {
               NBTTagCompound compound = this.getCompound();
               if (compound != null) {
                    compound.setTag("Pos", this.newDoubleNBTList((double)this.posX + 0.5D, (double)(this.posY + 1), (double)this.posZ + 0.5D));
                    Client.sendData(EnumPacketServer.SpawnRider, compound);
                    this.close();
               }
          }

          if (id == 2) {
               Client.sendData(EnumPacketServer.PlayerRider);
               this.close();
          }

          if (id == 3) {
               showingClones = 0;
               this.func_73866_w_();
          }

          if (id == 4) {
               showingClones = 1;
               this.func_73866_w_();
          }

          if (id == 5) {
               showingClones = 2;
               this.func_73866_w_();
          }

          if (id > 20) {
               this.activeTab = id - 20;
               this.func_73866_w_();
          }

     }

     protected NBTTagList newDoubleNBTList(double... par1ArrayOfDouble) {
          NBTTagList nbttaglist = new NBTTagList();
          double[] adouble = par1ArrayOfDouble;
          int i = par1ArrayOfDouble.length;

          for(int j = 0; j < i; ++j) {
               double d1 = adouble[j];
               nbttaglist.appendTag(new NBTTagDouble(d1));
          }

          return nbttaglist;
     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          NBTTagList nbtlist = compound.getTagList("List", 8);
          List list = new ArrayList();

          for(int i = 0; i < nbtlist.tagCount(); ++i) {
               list.add(nbtlist.func_150307_f(i));
          }

          this.list = list;
          this.scroll.setList(this.getSearchList());
     }
}
