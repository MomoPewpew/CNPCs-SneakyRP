package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerCustomChest;

public class GuiCustomChest extends GuiContainer {
     private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
     private IInventory upperChestInventory;
     private final int inventoryRows;
     public String title = null;

     public GuiCustomChest(ContainerCustomChest container) {
          super(container);
          this.inventoryRows = container.rows;
     }

     public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
          this.func_146276_q_();
          super.func_73863_a(mouseX, mouseY, partialTicks);
          if (this.title != null && !this.title.isEmpty()) {
               this.field_146289_q.func_78276_b(this.title, (this.field_146294_l - this.field_146289_q.func_78256_a(this.title)) / 2, (this.field_146295_m - this.field_147000_g) / 2 + 5, CustomNpcResourceListener.DefaultTextColor);
          }

          this.func_191948_b(mouseX, mouseY);
     }

     protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {
          GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
          this.field_146297_k.func_110434_K().func_110577_a(CHEST_GUI_TEXTURE);
          int i = (this.field_146294_l - this.field_146999_f) / 2;
          int j = (this.field_146295_m - this.field_147000_g) / 2;
          this.func_73729_b(i, j, 0, 0, this.field_146999_f, this.inventoryRows * 18 + 17);
          this.func_73729_b(i, j + this.inventoryRows * 18 + 17, 0, 126, this.field_146999_f, 96);
     }

     public void func_146281_b() {
          super.func_146281_b();
          NoppesUtilPlayer.sendData(EnumPlayerPacket.CloseGui);
     }
}
