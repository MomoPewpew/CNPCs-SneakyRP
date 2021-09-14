package noppes.npcs;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.util.CustomNPCsScheduler;

public class Server {
	public static void sendData(EntityPlayerMP player, EnumPacketClient enu, Object... obs) {
		sendDataDelayed(player, enu, 0, obs);
	}

	public static void sendDataDelayed(EntityPlayerMP player, EnumPacketClient type, int delay, Object... obs) {
		CustomNPCsScheduler.runTack(() -> {
			PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

			try {
				if (!fillBuffer(buffer, type, obs)) {
					return;
				}

				LogWriter.debug("Send: " + type);
				CustomNpcs.Channel.sendTo(new FMLProxyPacket(buffer, "CustomNPCs"), player);
			} catch (IOException var5) {
				LogWriter.error(type + " Errored", var5);
			}

		}, delay);
	}

	public static boolean sendDataChecked(EntityPlayerMP player, EnumPacketClient type, Object... obs) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());

		try {
			if (!fillBuffer(buffer, type, obs)) {
				return false;
			}

			LogWriter.debug("SendDataChecked: " + type);
			CustomNpcs.Channel.sendTo(new FMLProxyPacket(buffer, "CustomNPCs"), player);
		} catch (IOException var5) {
			LogWriter.error(type + " Errored", var5);
		}

		return true;
	}

	public static void sendAssociatedData(Entity entity, EnumPacketClient type, Object... obs) {
		List list = entity.world.getEntitiesWithinAABB(EntityPlayerMP.class,
				entity.getEntityBoundingBox().grow(160.0D, 160.0D, 160.0D));
		if (!list.isEmpty()) {
			CustomNPCsScheduler.runTack(() -> {
				ByteBuf buffer = Unpooled.buffer();

				try {
					if (!fillBuffer(buffer, type, obs)) {
						return;
					}

					LogWriter.debug("SendAssociatedData: " + type);
					Iterator var4 = list.iterator();

					while (var4.hasNext()) {
						EntityPlayerMP player = (EntityPlayerMP) var4.next();
						CustomNpcs.Channel.sendTo(new FMLProxyPacket(new PacketBuffer(buffer.copy()), "CustomNPCs"),
								player);
					}
				} catch (IOException var9) {
					LogWriter.error(type + " Errored", var9);
				} finally {
					buffer.release();
				}

			});
		}
	}

	public static void sendRangedData(Entity entity, int range, EnumPacketClient type, Object... obs) {
		List list = entity.world.getEntitiesWithinAABB(EntityPlayerMP.class,
				entity.getEntityBoundingBox().grow((double) range, (double) range, (double) range));
		if (!list.isEmpty()) {
			CustomNPCsScheduler.runTack(() -> {
				ByteBuf buffer = Unpooled.buffer();

				try {
					if (!fillBuffer(buffer, type, obs)) {
						return;
					}

					LogWriter.debug("sendRangedData: " + type);
					Iterator var4 = list.iterator();

					while (var4.hasNext()) {
						EntityPlayerMP player = (EntityPlayerMP) var4.next();
						CustomNpcs.Channel.sendTo(new FMLProxyPacket(new PacketBuffer(buffer.copy()), "CustomNPCs"),
								player);
					}
				} catch (IOException var9) {
					LogWriter.error(type + " Errored", var9);
				} finally {
					buffer.release();
				}

			});
		}
	}

	public static void sendRangedData(World world, BlockPos pos, int range, EnumPacketClient type, Object... obs) {
		List list = world.getEntitiesWithinAABB(EntityPlayerMP.class,
				(new AxisAlignedBB(pos)).grow((double) range, (double) range, (double) range));
		if (!list.isEmpty()) {
			CustomNPCsScheduler.runTack(() -> {
				ByteBuf buffer = Unpooled.buffer();

				try {
					if (!fillBuffer(buffer, type, obs)) {
						return;
					}

					LogWriter.debug("sendRangedData: " + type);
					Iterator var4 = list.iterator();

					while (var4.hasNext()) {
						EntityPlayerMP player = (EntityPlayerMP) var4.next();
						CustomNpcs.Channel.sendTo(new FMLProxyPacket(new PacketBuffer(buffer.copy()), "CustomNPCs"),
								player);
					}
				} catch (IOException var9) {
					LogWriter.error(type + " Errored", var9);
				} finally {
					buffer.release();
				}

			});
		}
	}

	public static void sendToAll(MinecraftServer server, EnumPacketClient type, Object... obs) {
		List list = new ArrayList(server.getPlayerList().getPlayers());
		CustomNPCsScheduler.runTack(() -> {
			ByteBuf buffer = Unpooled.buffer();

			try {
				if (!fillBuffer(buffer, type, obs)) {
					return;
				}

				LogWriter.debug("SendToAll: " + type);
				Iterator var4 = list.iterator();

				while (var4.hasNext()) {
					EntityPlayerMP player = (EntityPlayerMP) var4.next();
					CustomNpcs.Channel.sendTo(new FMLProxyPacket(new PacketBuffer(buffer.copy()), "CustomNPCs"),
							player);
				}
			} catch (IOException var9) {
				LogWriter.error(type + " Errored", var9);
			} finally {
				buffer.release();
			}

		});
	}

	public static boolean fillBuffer(ByteBuf buffer, Enum enu, Object... obs) throws IOException {
		buffer.writeInt(enu.ordinal());
		Object[] var3 = obs;
		int var4 = obs.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			Object ob = var3[var5];
			if (ob != null) {
				Iterator var8;
				String s;
				if (ob instanceof Map) {
					Map map = (Map) ob;
					buffer.writeInt(map.size());
					var8 = map.keySet().iterator();

					while (var8.hasNext()) {
						s = (String) var8.next();
						int value = (Integer) map.get(s);
						buffer.writeInt(value);
						writeString(buffer, s);
					}
				} else if (ob instanceof MerchantRecipeList) {
					((MerchantRecipeList) ob).writeToBuf(new PacketBuffer(buffer));
				} else if (ob instanceof List) {
					List list = (List) ob;
					buffer.writeInt(list.size());
					var8 = list.iterator();

					while (var8.hasNext()) {
						s = (String) var8.next();
						writeString(buffer, s);
					}
				} else if (ob instanceof UUID) {
					writeString(buffer, ob.toString());
				} else if (ob instanceof Enum) {
					buffer.writeInt(((Enum) ob).ordinal());
				} else if (ob instanceof Integer) {
					buffer.writeInt((Integer) ob);
				} else if (ob instanceof Boolean) {
					buffer.writeBoolean((Boolean) ob);
				} else if (ob instanceof String) {
					writeString(buffer, (String) ob);
				} else if (ob instanceof Float) {
					buffer.writeFloat((Float) ob);
				} else if (ob instanceof Long) {
					buffer.writeLong((Long) ob);
				} else if (ob instanceof Double) {
					buffer.writeDouble((Double) ob);
				} else if (ob instanceof NBTTagCompound) {
					writeNBT(buffer, (NBTTagCompound) ob);
				}
			}
		}

		if (buffer.array().length >= 65534) {
			LogWriter.error("Packet " + enu + " was too big to be send");
			return false;
		} else {
			return true;
		}
	}

	public static UUID readUUID(ByteBuf buffer) {
		return UUID.fromString(readString(buffer));
	}

	public static void writeNBT(ByteBuf buffer, NBTTagCompound compound) throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));

		try {
			CompressedStreamTools.write(compound, dataoutputstream);
		} finally {
			dataoutputstream.close();
		}

		byte[] bytes = bytearrayoutputstream.toByteArray();
		buffer.writeInt(bytes.length);
		buffer.writeBytes(bytes);
	}

	public static NBTTagCompound readNBT(ByteBuf buffer) throws IOException {
		byte[] bytes = new byte[buffer.readInt()];
		buffer.readBytes(bytes);
		DataInputStream datainputstream = new DataInputStream(
				new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes))));

		NBTTagCompound var3;
		try {
			var3 = CompressedStreamTools.read(datainputstream, NBTSizeTracker.INFINITE);
		} finally {
			datainputstream.close();
		}

		return var3;
	}

	public static void writeString(ByteBuf buffer, String s) {
		byte[] bytes = s.getBytes(Charsets.UTF_8);
		buffer.writeInt(bytes.length);
		buffer.writeBytes(bytes);
	}

	public static String readString(ByteBuf buffer) {
		try {
			byte[] bytes = new byte[buffer.readInt()];
			buffer.readBytes(bytes);
			return new String(bytes, Charsets.UTF_8);
		} catch (IndexOutOfBoundsException var2) {
			return null;
		}
	}
}
