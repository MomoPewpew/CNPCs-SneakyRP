package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketServer;

public class GuiNpcWaypoint extends GuiNPCInterface implements IGuiData {
     private TileWaypoint tile;

     public GuiNpcWaypoint(int x, int y, int z) {
          this.tile = (TileWaypoint)this.player.world.getTileEntity(new BlockPos(x, y, z));
          Client.sendData(EnumPacketServer.GetTileEntity, x, y, z);
          this.xSize = 265;
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.tile == null) {
               this.close();
          }

          this.addLabel(new GuiNpcLabel(0, "gui.name", this.guiLeft + 1, this.guiTop + 76, 16777215));
          this.addTextField(new GuiNpcTextField(0, this, this.field_146289_q, this.guiLeft + 60, this.guiTop + 71, 200, 20, this.tile.name));
          this.addLabel(new GuiNpcLabel(1, "gui.range", this.guiLeft + 1, this.guiTop + 97, 16777215));
          this.addTextField(new GuiNpcTextField(1, this, this.field_146289_q, this.guiLeft + 60, this.guiTop + 92, 200, 20, this.tile.range + ""));
          this.getTextField(1).numbersOnly = true;
          this.getTextField(1).setMinMaxDefault(2, 60, 10);
          this.addButton(new GuiNpcButton(0, this.guiLeft + 40, this.guiTop + 190, 120, 20, "Done"));
     }

     protected void func_146284_a(GuiButton guibutton) {
          int id = guibutton.id;
          if (id == 0) {
               this.close();
          }

     }

     public void save() {
          this.tile.name = this.getTextField(0).func_146179_b();
          this.tile.range = this.getTextField(1).getInteger();
          NBTTagCompound compound = new NBTTagCompound();
          this.tile.writeToNBT(compound);
          Client.sendData(EnumPacketServer.SaveTileEntity, compound);
     }

     public void setGuiData(NBTTagCompound compound) {
          this.tile.readFromNBT(compound);
          this.func_73866_w_();
     }
}
