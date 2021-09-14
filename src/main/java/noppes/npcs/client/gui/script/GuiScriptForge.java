package noppes.npcs.client.gui.script;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.ForgeScriptData;

public class GuiScriptForge extends GuiScriptInterface {
	private ForgeScriptData script = new ForgeScriptData();

	public GuiScriptForge() {
		this.handler = this.script;
		Client.sendData(EnumPacketServer.ScriptForgeGet);
	}

	public void setGuiData(NBTTagCompound compound) {
		this.script.readFromNBT(compound);
		super.setGuiData(compound);
	}

	public void save() {
		super.save();
		Client.sendData(EnumPacketServer.ScriptForgeSave, this.script.writeToNBT(new NBTTagCompound()));
	}
}
