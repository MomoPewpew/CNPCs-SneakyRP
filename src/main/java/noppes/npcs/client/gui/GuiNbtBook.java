package noppes.npcs.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNbtBook extends GuiNPCInterface implements IGuiData {
     private int x;
     private int y;
     private int z;
     private TileEntity tile;
     private IBlockState state;
     private ItemStack blockStack;
     private int entityId;
     private Entity entity;
     private NBTTagCompound originalCompound;
     private NBTTagCompound compound;
     private String faultyText = null;
     private String errorMessage = null;

     public GuiNbtBook(int x, int y, int z) {
          this.x = x;
          this.y = y;
          this.z = z;
          this.setBackground("menubg.png");
          this.xSize = 256;
          this.ySize = 216;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.state != null) {
               this.addLabel(new GuiNpcLabel(11, "x: " + this.x + ", y: " + this.y + ", z: " + this.z, this.guiLeft + 60, this.guiTop + 6));
               this.addLabel(new GuiNpcLabel(12, "id: " + Block.REGISTRY.getNameForObject(this.state.getBlock()), this.guiLeft + 60, this.guiTop + 16));
          }

          if (this.entity != null) {
               this.addLabel(new GuiNpcLabel(12, "id: " + EntityRegistry.getEntry(this.entity.getClass()).getRegistryName(), this.guiLeft + 60, this.guiTop + 6));
          }

          this.addButton(new GuiNpcButton(0, this.guiLeft + 38, this.guiTop + 144, 180, 20, "nbt.edit"));
          this.getButton(0).enabled = this.compound != null && !this.compound.hasNoTags();
          this.addLabel(new GuiNpcLabel(0, "", this.guiLeft + 4, this.guiTop + 167));
          this.addLabel(new GuiNpcLabel(1, "", this.guiLeft + 4, this.guiTop + 177));
          this.addButton(new GuiNpcButton(66, this.guiLeft + 128, this.guiTop + 190, 120, 20, "gui.close"));
          this.addButton(new GuiNpcButton(67, this.guiLeft + 4, this.guiTop + 190, 120, 20, "gui.save"));
          if (this.errorMessage != null) {
               this.getButton(67).enabled = false;
               int i = this.errorMessage.indexOf(" at: ");
               if (i > 0) {
                    this.getLabel(0).label = this.errorMessage.substring(0, i);
                    this.getLabel(1).label = this.errorMessage.substring(i);
               } else {
                    this.getLabel(0).label = this.errorMessage;
               }
          }

          if (this.getButton(67).enabled && this.originalCompound != null) {
               this.getButton(67).enabled = !this.originalCompound.equals(this.compound);
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 0) {
               if (this.faultyText != null) {
                    this.setSubGui((new SubGuiNpcTextArea(this.compound.toString(), this.faultyText)).enableHighlighting());
               } else {
                    this.setSubGui((new SubGuiNpcTextArea(this.compound.toString())).enableHighlighting());
               }
          }

          if (id == 67) {
               this.getLabel(0).label = "Saved";
               if (this.compound.equals(this.originalCompound)) {
                    return;
               }

               if (this.tile == null) {
                    Client.sendData(EnumPacketServer.NbtBookSaveEntity, this.entityId, this.compound);
                    return;
               }

               Client.sendData(EnumPacketServer.NbtBookSaveBlock, this.x, this.y, this.z, this.compound);
               this.originalCompound = this.compound.copy();
               this.getButton(67).enabled = false;
          }

          if (id == 66) {
               this.close();
          }

     }

     public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
          super.func_73863_a(mouseX, mouseY, partialTicks);
          if (!this.hasSubGui()) {
               if (this.state != null) {
                    GlStateManager.func_179094_E();
                    GlStateManager.translate((float)(this.guiLeft + 4), (float)(this.guiTop + 4), 0.0F);
                    GlStateManager.func_179152_a(3.0F, 3.0F, 3.0F);
                    RenderHelper.enableGUIStandardItemLighting();
                    this.field_146296_j.renderItemAndEffectIntoGUI(this.blockStack, 0, 0);
                    this.field_146296_j.func_175030_a(this.field_146289_q, this.blockStack, 0, 0);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.func_179121_F();
               }

               if (this.entity instanceof EntityLivingBase) {
                    this.drawNpc((EntityLivingBase)this.entity, 20, 80, 1.0F, 0);
               }

          }
     }

     public void closeSubGui(SubGuiInterface gui) {
          super.closeSubGui(gui);
          if (gui instanceof SubGuiNpcTextArea) {
               try {
                    this.compound = JsonToNBT.getTagFromJson(((SubGuiNpcTextArea)gui).text);
                    this.errorMessage = this.faultyText = null;
               } catch (NBTException var3) {
                    this.errorMessage = var3.getLocalizedMessage();
                    this.faultyText = ((SubGuiNpcTextArea)gui).text;
               }

               this.func_73866_w_();
          }

     }

     public void save() {
     }

     public void setGuiData(NBTTagCompound compound) {
          if (compound.hasKey("EntityId")) {
               this.entityId = compound.getInteger("EntityId");
               this.entity = this.player.world.getEntityByID(this.entityId);
          } else {
               this.tile = this.player.world.getTileEntity(new BlockPos(this.x, this.y, this.z));
               this.state = this.player.world.getBlockState(new BlockPos(this.x, this.y, this.z));
               this.blockStack = this.state.getBlock().func_185473_a(this.player.world, new BlockPos(this.x, this.y, this.z), this.state);
          }

          this.originalCompound = compound.getCompoundTag("Data");
          this.compound = this.originalCompound.copy();
          this.func_73866_w_();
     }
}
