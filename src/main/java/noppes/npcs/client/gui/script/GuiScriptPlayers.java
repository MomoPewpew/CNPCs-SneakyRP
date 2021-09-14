package noppes.npcs.client.gui.script;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.PlayerScriptData;

public class GuiScriptPlayers extends GuiScriptInterface {
	private PlayerScriptData script = new PlayerScriptData((EntityPlayer) null);

	public GuiScriptPlayers() {
		this.handler = this.script;
		Client.sendData(EnumPacketServer.ScriptPlayerGet);
	}

	public void setGuiData(NBTTagCompound compound) {
		this.script.readFromNBT(compound);
		super.setGuiData(compound);
	}

	public void save() {
		super.save();
		Client.sendData(EnumPacketServer.ScriptPlayerSave, this.script.writeToNBT(new NBTTagCompound()));
	}
}
