package noppes.npcs.client.gui.player.companion;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class GuiNpcCompanionInv extends GuiContainerNPCInterface {
     private final ResourceLocation resource = new ResourceLocation("customnpcs", "textures/gui/companioninv.png");
     private final ResourceLocation slot = new ResourceLocation("customnpcs", "textures/gui/slot.png");
     private EntityNPCInterface npc;
     private RoleCompanion role;

     public GuiNpcCompanionInv(EntityNPCInterface npc, ContainerNPCCompanion container) {
          super(npc, container);
          this.npc = npc;
          this.role = (RoleCompanion)npc.roleInterface;
          this.closeOnEsc = true;
          this.xSize = 171;
          this.ySize = 166;
     }

     public void initGui() {
          super.initGui();
          GuiNpcCompanionStats.addTopMenu(this.role, this, 3);
     }

     public void actionPerformed(GuiButton guibutton) {
          super.actionPerformed(guibutton);
          int id = guibutton.id;
          if (id == 1) {
               CustomNpcs.proxy.openGui(this.npc, EnumGuiType.Companion);
          }

          if (id == 2) {
               CustomNpcs.proxy.openGui(this.npc, EnumGuiType.CompanionTalent);
          }

     }

     protected void drawGuiContainerForegroundLayer(int par1, int par2) {
     }

     protected void drawGuiContainerBackgroundLayer(float f, int xMouse, int yMouse) {
          this.drawWorldBackground(0);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
          this.mc.renderEngine.bindTexture(this.resource);
          this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
          this.mc.renderEngine.bindTexture(this.slot);
          int size;
          if (this.role.getTalentLevel(EnumCompanionTalent.ARMOR) > 0) {
               for(size = 0; size < 4; ++size) {
                    this.drawTexturedModalRect(this.guiLeft + 5, this.guiTop + 7 + size * 18, 0, 0, 18, 18);
               }
          }

          if (this.role.getTalentLevel(EnumCompanionTalent.SWORD) > 0) {
               this.drawTexturedModalRect(this.guiLeft + 78, this.guiTop + 16, 0, this.npc.inventory.weapons.get(0) == null ? 18 : 0, 18, 18);
          }

          if (this.role.getTalentLevel(EnumCompanionTalent.RANGED) > 0) {
          }

          if (this.role.talents.containsKey(EnumCompanionTalent.INVENTORY)) {
               size = (this.role.getTalentLevel(EnumCompanionTalent.INVENTORY) + 1) * 2;

               for(int i = 0; i < size; ++i) {
                    this.drawTexturedModalRect(this.guiLeft + 113 + i % 3 * 18, this.guiTop + 7 + i / 3 * 18, 0, 0, 18, 18);
               }
          }

     }

     public void drawScreen(int i, int j, float f) {
          super.drawScreen(i, j, f);
          super.drawNpc(52, 70);
     }

     public void save() {
     }
}
