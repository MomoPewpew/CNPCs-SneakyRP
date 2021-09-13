package micdoodle8.mods.galacticraft.api.client.tabs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabRegistry {
     private static ArrayList tabList = new ArrayList();
     private static Class clazzJEIConfig = null;
     public static Class clazzNEIConfig = null;
     private static Minecraft mc;
     private static boolean initWithPotion;

     public static void registerTab(AbstractTab tab) {
          tabList.add(tab);
     }

     public static ArrayList getTabList() {
          return tabList;
     }

     public static void addTabsToInventory(GuiContainer gui) {
     }

     @SideOnly(Side.CLIENT)
     @SubscribeEvent
     public void guiPostInit(Post event) {
          if (event.getGui() instanceof GuiInventory) {
               int guiLeft = (event.getGui().width - 176) / 2;
               int guiTop = (event.getGui().height - 166) / 2;
               guiLeft += getPotionOffset();
               updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
               addTabsToList(event.getButtonList());
          }

     }

     public static void openInventoryGui() {
          mc.player.connection.sendPacket(new CPacketCloseWindow(mc.player.openContainer.windowId));
          GuiInventory inventory = new GuiInventory(mc.player);
          mc.displayGuiScreen(inventory);
     }

     public static void updateTabValues(int cornerX, int cornerY, Class selectedButton) {
          int count = 2;

          for(int i = 0; i < tabList.size(); ++i) {
               AbstractTab t = (AbstractTab)tabList.get(i);
               if (t.shouldAddToList()) {
                    t.id = count;
                    t.x = cornerX + (count - 2) * 28;
                    t.y = cornerY - 28;
                    t.enabled = !t.getClass().equals(selectedButton);
                    t.potionOffsetLast = getPotionOffsetNEI();
                    ++count;
               }
          }

     }

     public static void addTabsToList(List buttonList) {
          Iterator var1 = tabList.iterator();

          while(var1.hasNext()) {
               AbstractTab tab = (AbstractTab)var1.next();
               if (tab.shouldAddToList()) {
                    buttonList.add(tab);
               }
          }

     }

     public static int getPotionOffset() {
          if (!mc.player.getActivePotionEffects().isEmpty()) {
               initWithPotion = true;
               return 60 + getPotionOffsetJEI() + getPotionOffsetNEI();
          } else {
               initWithPotion = false;
               return 0;
          }
     }

     public static int getPotionOffsetJEI() {
          if (clazzJEIConfig != null) {
               try {
                    Object enabled = clazzJEIConfig.getMethod("isOverlayEnabled").invoke((Object)null);
                    if (enabled instanceof Boolean) {
                         if (!(Boolean)enabled) {
                              return 0;
                         }

                         return -60;
                    }
               } catch (Exception var1) {
               }
          }

          return 0;
     }

     public static int getPotionOffsetNEI() {
          if (initWithPotion && clazzNEIConfig != null) {
               try {
                    Object hidden = clazzNEIConfig.getMethod("isHidden").invoke((Object)null);
                    Object enabled = clazzNEIConfig.getMethod("isEnabled").invoke((Object)null);
                    if (hidden instanceof Boolean && enabled instanceof Boolean) {
                         if (!(Boolean)hidden && (Boolean)enabled) {
                              return -60;
                         }

                         return 0;
                    }
               } catch (Exception var2) {
               }
          }

          return 0;
     }

     static {
          try {
               clazzJEIConfig = Class.forName("mezz.jei.config.Config");
          } catch (Exception var2) {
          }

          if (clazzJEIConfig == null) {
               try {
                    clazzNEIConfig = Class.forName("codechicken.nei.NEIClientConfig");
               } catch (Exception var1) {
               }
          }

          mc = FMLClientHandler.instance().getClient();
     }
}
