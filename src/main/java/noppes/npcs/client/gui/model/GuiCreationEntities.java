package noppes.npcs.client.gui.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.NPCRendererHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityNpcAlex;
import noppes.npcs.entity.EntityNpcClassicPlayer;

public class GuiCreationEntities extends GuiCreationScreenInterface implements ICustomScrollListener {
     public HashMap data = new HashMap();
     private List list;
     private GuiCustomScroll scroll;
     private boolean resetToSelected = true;

     public GuiCreationEntities(EntityNPCInterface npc) {
          super(npc);
          Iterator var2 = ForgeRegistries.ENTITIES.getValues().iterator();

          while(var2.hasNext()) {
               EntityEntry ent = (EntityEntry)var2.next();
               String name = ent.getName();
               Class c = ent.getEntityClass();

               try {
                    if (EntityLiving.class.isAssignableFrom(c) && c.getConstructor(World.class) != null && !Modifier.isAbstract(c.getModifiers()) && Minecraft.getMinecraft().func_175598_ae().func_78715_a(c) instanceof RenderLivingBase && !name.toLowerCase().contains("customnpc")) {
                         this.data.put(name, c.asSubclass(EntityLivingBase.class));
                    }
               } catch (SecurityException var7) {
                    var7.printStackTrace();
               } catch (NoSuchMethodException var8) {
               }
          }

          this.data.put("NPC 64x32", EntityNPC64x32.class);
          this.data.put("NPC Alex Arms", EntityNpcAlex.class);
          this.data.put("NPC Classic Player", EntityNpcClassicPlayer.class);
          this.list = new ArrayList(this.data.keySet());
          this.list.add("NPC");
          Collections.sort(this.list, String.CASE_INSENSITIVE_ORDER);
          this.active = 1;
          this.xOffset = 60;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(new GuiNpcButton(10, this.guiLeft, this.guiTop + 46, 120, 20, "Reset To NPC"));
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setUnsortedList(this.list);
          }

          this.scroll.guiLeft = this.guiLeft;
          this.scroll.guiTop = this.guiTop + 68;
          this.scroll.setSize(100, this.ySize - 96);
          String selected = "NPC";
          if (this.entity != null) {
               Iterator var2 = this.data.entrySet().iterator();

               while(var2.hasNext()) {
                    Entry en = (Entry)var2.next();
                    if (((Class)en.getValue()).toString().equals(this.entity.getClass().toString())) {
                         selected = (String)en.getKey();
                    }
               }
          }

          this.scroll.setSelected(selected);
          if (this.resetToSelected) {
               this.scroll.scrollTo(this.scroll.getSelected());
               this.resetToSelected = false;
          }

          this.addScroll(this.scroll);
     }

     protected void func_146284_a(GuiButton btn) {
          super.func_146284_a(btn);
          if (btn.id == 10) {
               this.playerdata.setEntityClass((Class)null);
               this.resetToSelected = true;
               this.func_73866_w_();
          }

     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
          this.playerdata.setEntityClass((Class)this.data.get(scroll.getSelected()));
          Entity entity = this.playerdata.getEntity(this.npc);
          if (entity != null) {
               RenderLivingBase render = (RenderLivingBase)this.field_146297_k.func_175598_ae().func_78715_a(entity.getClass());
               if (!NPCRendererHelper.getTexture(render, entity).equals(TextureMap.field_174945_f.toString())) {
                    this.npc.display.setSkinTexture(NPCRendererHelper.getTexture(render, entity));
               }
          } else {
               this.npc.display.setSkinTexture("customnpcs:textures/entity/humanmale/steve.png");
          }

          this.func_73866_w_();
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
