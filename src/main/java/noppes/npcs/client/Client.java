package noppes.npcs.client;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.CustomNPCsScheduler;

public class Client {
	public static void sendData(EnumPacketServer type, Object... obs) {
		CustomNPCsScheduler.runTack(() -> {
			PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

			try {
				if (!Server.fillBuffer(buffer, type, obs)) {
					return;
				}

				LogWriter.debug("Send: " + type);
				CustomNpcs.Channel.sendToServer(new FMLProxyPacket(buffer, "CustomNPCs"));
			} catch (IOException var4) {
				var4.printStackTrace();
			}

		});
	}
}
