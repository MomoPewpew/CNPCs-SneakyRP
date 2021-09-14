package noppes.npcs.controllers.data;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.FactionController;

public class PlayerFactionData {
	public HashMap factionData = new HashMap();

	public void loadNBTData(NBTTagCompound compound) {
		HashMap factionData = new HashMap();
		if (compound != null) {
			NBTTagList list = compound.getTagList("FactionData", 10);
			if (list != null) {
				for (int i = 0; i < list.tagCount(); ++i) {
					NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
					factionData.put(nbttagcompound.getInteger("Faction"), nbttagcompound.getInteger("Points"));
				}

				this.factionData = factionData;
			}
		}
	}

	public void saveNBTData(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		Iterator var3 = this.factionData.keySet().iterator();

		while (var3.hasNext()) {
			int faction = (Integer) var3.next();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Faction", faction);
			nbttagcompound.setInteger("Points", (Integer) this.factionData.get(faction));
			list.appendTag(nbttagcompound);
		}

		compound.setTag("FactionData", list);
	}

	public int getFactionPoints(EntityPlayer player, int factionId) {
		Faction faction = FactionController.instance.getFaction(factionId);
		if (faction == null) {
			return 0;
		} else {
			if (!this.factionData.containsKey(factionId)) {
				if (player.world.isRemote) {
					return faction.defaultPoints;
				}

				PlayerScriptData handler = PlayerData.get(player).scriptData;
				PlayerWrapper wrapper = (PlayerWrapper) NpcAPI.Instance().getIEntity(player);
				PlayerEvent.FactionUpdateEvent event = new PlayerEvent.FactionUpdateEvent(wrapper, faction,
						faction.defaultPoints, true);
				EventHooks.OnPlayerFactionChange(handler, event);
				this.factionData.put(factionId, event.points);
			}

			return (Integer) this.factionData.get(factionId);
		}
	}

	public void increasePoints(EntityPlayer player, int factionId, int points) {
		Faction faction = FactionController.instance.getFaction(factionId);
		if (faction != null && player != null && !player.world.isRemote) {
			PlayerScriptData handler = PlayerData.get(player).scriptData;
			PlayerWrapper wrapper = (PlayerWrapper) NpcAPI.Instance().getIEntity(player);
			PlayerEvent.FactionUpdateEvent event;
			if (!this.factionData.containsKey(factionId)) {
				event = new PlayerEvent.FactionUpdateEvent(wrapper, faction, faction.defaultPoints, true);
				EventHooks.OnPlayerFactionChange(handler, event);
				this.factionData.put(factionId, event.points);
			}

			event = new PlayerEvent.FactionUpdateEvent(wrapper, faction, points, false);
			EventHooks.OnPlayerFactionChange(handler, event);
			this.factionData.put(factionId, (Integer) this.factionData.get(factionId) + points);
		}
	}

	public NBTTagCompound getPlayerGuiData() {
		NBTTagCompound compound = new NBTTagCompound();
		this.saveNBTData(compound);
		NBTTagList list = new NBTTagList();
		Iterator var3 = this.factionData.keySet().iterator();

		while (var3.hasNext()) {
			int id = (Integer) var3.next();
			Faction faction = FactionController.instance.getFaction(id);
			if (faction != null && !faction.hideFaction) {
				NBTTagCompound com = new NBTTagCompound();
				faction.writeNBT(com);
				list.appendTag(com);
			}
		}

		compound.setTag("FactionList", list);
		return compound;
	}
}
