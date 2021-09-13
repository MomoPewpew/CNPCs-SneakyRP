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

public class GuiNpcMobSpawner extends GuiNPCInterface implements IGuiData {
     private GuiCustomScroll scroll;
     private int posX;
     private int posY;
     private int posZ;
     private List list;
     private static int showingClones = 0;
     private static String search = "";
     private int activeTab = 1;

     public GuiNpcMobSpawner(int i, int j, int k) {
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
          this.addButton(new GuiNpcButton(1, this.guiLeft + 170, this.guiTop + 6, 82, 20, "item.monsterPlacer.name"));
          this.addButton(new GuiNpcButton(2, this.guiLeft + 170, this.guiTop + 100, 82, 20, "spawner.mobspawner"));
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
               this.addButton(new GuiNpcButton(6, this.guiLeft + 170, this.guiTop + 30, 82, 20, "gui.remove"));
               this.getSideButton(20 + this.activeTab).active = true;
               this.showClones();
          }

     }

     private void showEntities() {
          ArrayList list = new ArrayList();
          List classes = new ArrayList();
          Iterator var3 = ForgeRegistries.ENTITIES.getValues().iterator();

          while(var3.hasNext()) {
               EntityEntry ent = (EntityEntry)var3.next();
               Class c = ent.getEntityClass();
               String name = ent.getName();

               try {
                    if (!classes.contains(c) && EntityLiving.class.isAssignableFrom(c) && c.getConstructor(World.class) != null && !Modifier.isAbstract(c.getModifiers())) {
                         list.add(name.toString());
                         classes.add(c);
                    }
               } catch (SecurityException var8) {
                    var8.printStackTrace();
               } catch (NoSuchMethodException var9) {
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
               Entity entity = EntityList.func_188429_b(new ResourceLocation(sel), Minecraft.func_71410_x().field_71441_e);
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
          int id = guibutton.field_146127_k;
          if (id == 0) {
               this.close();
          }

          String sel;
          NBTTagCompound compound;
          if (id == 1) {
               if (showingClones == 2) {
                    sel = this.scroll.getSelected();
                    if (sel == null) {
                         return;
                    }

                    Client.sendData(EnumPacketServer.SpawnMob, true, this.posX, this.posY, this.posZ, sel, this.activeTab);
                    this.close();
               } else {
                    compound = this.getCompound();
                    if (compound == null) {
                         return;
                    }

                    Client.sendData(EnumPacketServer.SpawnMob, false, this.posX, this.posY, this.posZ, compound);
                    this.close();
               }
          }

          if (id == 2) {
               if (showingClones == 2) {
                    sel = this.scroll.getSelected();
                    if (sel == null) {
                         return;
                    }

                    Client.sendData(EnumPacketServer.MobSpawner, true, this.posX, this.posY, this.posZ, sel, this.activeTab);
                    this.close();
               } else {
                    compound = this.getCompound();
                    if (compound == null) {
                         return;
                    }

                    Client.sendData(EnumPacketServer.MobSpawner, false, this.posX, this.posY, this.posZ, compound);
                    this.close();
               }
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

          if (id == 6 && this.scroll.getSelected() != null) {
               if (showingClones == 2) {
                    Client.sendData(EnumPacketServer.CloneRemove, this.activeTab, this.scroll.getSelected());
                    return;
               }

               ClientCloneController.Instance.removeClone(this.scroll.getSelected(), this.activeTab);
               this.scroll.selected = -1;
               this.func_73866_w_();
          }

          if (id > 20) {
               this.activeTab = id - 20;
               this.func_73866_w_();
          }

     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          NBTTagList nbtlist = compound.func_150295_c("List", 8);
          List list = new ArrayList();

          for(int i = 0; i < nbtlist.func_74745_c(); ++i) {
               list.add(nbtlist.func_150307_f(i));
          }

          this.list = list;
          this.scroll.setList(this.getSearchList());
     }
}
