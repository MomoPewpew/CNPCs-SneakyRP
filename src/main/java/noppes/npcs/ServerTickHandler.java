package noppes.npcs;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import noppes.npcs.client.AnalyticsTracking;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.SyncController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.data.DataScenes;

public class ServerTickHandler {
	public int ticks = 0;

	@SubscribeEvent
	public void onServerTick(PlayerTickEvent event) {
		if (event.side == Side.SERVER && event.phase == Phase.START) {
			EntityPlayer player = event.player;
			PlayerData data = PlayerData.get(player);
			if (data.updateClient) {
				Server.sendData((EntityPlayerMP) player, EnumPacketClient.SYNC_END, 8, data.getSyncNBT());
				data.updateClient = false;
			}

		}
	}

	@SubscribeEvent
	public void onServerTick(WorldTickEvent event) {
		if (event.side == Side.SERVER && event.phase == Phase.START) {
			NPCSpawning.findChunksForSpawning((WorldServer) event.world);
		}

	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.side == Side.SERVER && event.phase == Phase.START && this.ticks++ >= 20) {
			SchematicController.Instance.updateBuilding();
			MassBlockController.Update();
			this.ticks = 0;
			Iterator var2 = DataScenes.StartedScenes.values().iterator();

			while (var2.hasNext()) {
				DataScenes.SceneState state = (DataScenes.SceneState) var2.next();
				if (!state.paused) {
					++state.ticks;
				}
			}

			var2 = DataScenes.ScenesToRun.iterator();

			while (var2.hasNext()) {
				DataScenes.SceneContainer entry = (DataScenes.SceneContainer) var2.next();
				entry.update();
			}

			DataScenes.ScenesToRun = new ArrayList();
		}

	}

	@SubscribeEvent
	public void playerLogin(PlayerLoggedInEvent event) {
		MinecraftServer server = event.player.getServer();
		if (server.isSnooperEnabled()) {
			String serverName = null;
			if (server.isDedicatedServer()) {
				serverName = "server";
			} else {
				serverName = ((IntegratedServer) server).getPublic() ? "lan" : "local";
			}

			AnalyticsTracking.sendData(event.player, "join", serverName);
		}

		SyncController.syncPlayer((EntityPlayerMP) event.player);
	}
}
