package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiBorderBlock extends GuiNPCInterface implements IGuiData {
     private TileBorder tile;

     public GuiBorderBlock(int x, int y, int z) {
          this.tile = (TileBorder)this.player.world.func_175625_s(new BlockPos(x, y, z));
          Client.sendData(EnumPacketServer.GetTileEntity, x, y, z);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          this.addButton(new GuiNpcButton(4, this.guiLeft + 40, this.guiTop + 40, 120, 20, "Availability Options"));
          this.addLabel(new GuiNpcLabel(0, "Height", this.guiLeft + 1, this.guiTop + 76, 16777215));
          this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.guiLeft + 60, this.guiTop + 71, 40, 20, this.tile.height + ""));
          this.getTextField(0).numbersOnly = true;
          this.getTextField(0).setMinMaxDefault(0, 500, 6);
          this.addLabel(new GuiNpcLabel(1, "Message", this.guiLeft + 1, this.guiTop + 100, 16777215));
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.guiLeft + 60, this.guiTop + 95, 200, 20, this.tile.message));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 40, this.guiTop + 190, 120, 20, "Done"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 0) {
               this.close();
          }

          if (id == 4) {
               this.save();
               this.setSubGui(new SubGuiNpcAvailability(this.tile.availability));
          }

     }

     public void save() {
          if (this.tile != null) {
               this.tile.height = this.getTextField(0).getInteger();
               this.tile.message = this.getTextField(1).func_146179_b();
               NBTTagCompound compound = new NBTTagCompound();
               this.tile.func_189515_b(compound);
               Client.sendData(EnumPacketServer.SaveTileEntity, compound);
          }
     }

     public void setGuiData(NBTTagCompound compound) {
          this.tile.readFromNBT(compound);
          this.func_73866_w_();
     }
}
