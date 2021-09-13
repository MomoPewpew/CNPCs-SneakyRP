package noppes.npcs.client.gui.script;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.client.Client;
import noppes.npcs.constants.EnumPacketServer;

public class GuiScriptDoor extends GuiScriptInterface {
     private TileScriptedDoor script;

     public GuiScriptDoor(int x, int y, int z) {
          this.handler = this.script = (TileScriptedDoor)this.player.world.func_175625_s(new BlockPos(x, y, z));
          Client.sendData(EnumPacketServer.ScriptDoorDataGet, x, y, z);
     }

     public void setGuiData(NBTTagCompound compound) {
          this.script.setNBT(compound);
          super.setGuiData(compound);
     }

     public void save() {
          super.save();
          BlockPos pos = this.script.func_174877_v();
          Client.sendData(EnumPacketServer.ScriptDoorDataSave, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), this.script.getNBT(new NBTTagCompound()));
     }
}
