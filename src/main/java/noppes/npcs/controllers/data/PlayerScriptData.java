package noppes.npcs.controllers.data;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;

public class PlayerScriptData implements IScriptHandler {
	private List scripts = new ArrayList();
	private String scriptLanguage = "ECMAScript";
	private EntityPlayer player;
	private IPlayer playerAPI;
	private long lastPlayerUpdate = 0L;
	public long lastInited = -1L;
	public boolean hadInteract = true;
	private boolean enabled = false;
	private static Map console = new TreeMap();
	private static List errored = new ArrayList();

	public PlayerScriptData(EntityPlayer player) {
		this.player = player;
	}

	public void clear() {
		console = new TreeMap();
		errored = new ArrayList();
		this.scripts = new ArrayList();
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.scripts = NBTTags.GetScript(compound.getTagList("Scripts", 10), this);
		this.scriptLanguage = compound.getString("ScriptLanguage");
		this.enabled = compound.getBoolean("ScriptEnabled");
		console = NBTTags.GetLongStringMap(compound.getTagList("ScriptConsole", 10));
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Scripts", NBTTags.NBTScript(this.scripts));
		compound.setString("ScriptLanguage", this.scriptLanguage);
		compound.setBoolean("ScriptEnabled", this.enabled);
		compound.setTag("ScriptConsole", NBTTags.NBTLongStringMap(console));
		return compound;
	}

	public void runScript(EnumScriptType type, Event event) {
		if (this.isEnabled()) {
			ScriptContainer script;
			if (ScriptController.Instance.lastLoaded > this.lastInited
					|| ScriptController.Instance.lastPlayerUpdate > this.lastPlayerUpdate) {
				this.lastInited = ScriptController.Instance.lastLoaded;
				errored.clear();
				if (this.player != null) {
					this.scripts.clear();
					Iterator var3 = ScriptController.Instance.playerScripts.scripts.iterator();

					while (var3.hasNext()) {
						script = (ScriptContainer) var3.next();
						ScriptContainer s = new ScriptContainer(this);
						s.readFromNBT(script.writeToNBT(new NBTTagCompound()));
						this.scripts.add(s);
					}
				}

				this.lastPlayerUpdate = ScriptController.Instance.lastPlayerUpdate;
				if (type != EnumScriptType.INIT) {
					EventHooks.onPlayerInit(this);
				}
			}

			for (int i = 0; i < this.scripts.size(); ++i) {
				script = (ScriptContainer) this.scripts.get(i);
				if (!errored.contains(i)) {
					script.run(type, event);
					if (script.errored) {
						errored.add(i);
					}

					Iterator var8 = script.console.entrySet().iterator();

					while (var8.hasNext()) {
						Entry entry = (Entry) var8.next();
						if (!console.containsKey(entry.getKey())) {
							console.put(entry.getKey(), " tab " + (i + 1) + ":\n" + (String) entry.getValue());
						}
					}

					script.console.clear();
				}
			}

		}
	}

	public boolean isEnabled() {
		return ScriptController.Instance.playerScripts.enabled && ScriptController.HasStart
				&& (this.player == null || !this.player.world.isRemote);
	}

	public boolean isClient() {
		return !this.player.isServerWorld();
	}

	public boolean getEnabled() {
		return ScriptController.Instance.playerScripts.enabled;
	}

	public void setEnabled(boolean bo) {
		this.enabled = bo;
	}

	public String getLanguage() {
		return ScriptController.Instance.playerScripts.scriptLanguage;
	}

	public void setLanguage(String lang) {
		this.scriptLanguage = lang;
	}

	public List getScripts() {
		return this.scripts;
	}

	public String noticeString() {
		if (this.player == null) {
			return "Global script";
		} else {
			BlockPos pos = this.player.getPosition();
			return MoreObjects.toStringHelper(this.player).add("x", pos.getX()).add("y", pos.getY())
					.add("z", pos.getZ()).toString();
		}
	}

	public IPlayer getPlayer() {
		if (this.playerAPI == null) {
			this.playerAPI = (IPlayer) NpcAPI.Instance().getIEntity(this.player);
		}

		return this.playerAPI;
	}

	public Map getConsoleText() {
		return console;
	}

	public void clearConsole() {
		console.clear();
	}
}
