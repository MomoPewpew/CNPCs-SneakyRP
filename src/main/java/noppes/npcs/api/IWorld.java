package noppes.npcs.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;

public interface IWorld {
	/** @deprecated */
	IEntity[] getNearbyEntities(int var1, int var2, int var3, int var4, int var5);

	IEntity[] getNearbyEntities(IPos var1, int var2, int var3);

	/** @deprecated */
	IEntity getClosestEntity(int var1, int var2, int var3, int var4, int var5);

	IEntity getClosestEntity(IPos var1, int var2, int var3);

	IEntity[] getAllEntities(int var1);

	long getTime();

	void setTime(long var1);

	long getTotalTime();

	IBlock getBlock(int var1, int var2, int var3);

	void setBlock(int var1, int var2, int var3, String var4, int var5);

	void removeBlock(int var1, int var2, int var3);

	float getLightValue(int var1, int var2, int var3);

	IPlayer getPlayer(String var1);

	boolean isDay();

	boolean isRaining();

	IDimension getDimension();

	void setRaining(boolean var1);

	void thunderStrike(double var1, double var3, double var5);

	void playSoundAt(IPos var1, String var2, float var3, float var4);

	void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12,
			double var14, int var16);

	void broadcast(String var1);

	IScoreboard getScoreboard();

	IData getTempdata();

	IData getStoreddata();

	IItemStack createItem(String var1, int var2, int var3);

	IItemStack createItemFromNbt(INbt var1);

	void explode(double var1, double var3, double var5, float var7, boolean var8, boolean var9);

	IPlayer[] getAllPlayers();

	String getBiomeName(int var1, int var2);

	void spawnEntity(IEntity var1);

	/** @deprecated */
	@Deprecated
	IEntity spawnClone(double var1, double var3, double var5, int var7, String var8);

	/** @deprecated */
	@Deprecated
	IEntity getClone(int var1, String var2);

	int getRedstonePower(int var1, int var2, int var3);

	WorldServer getMCWorld();

	BlockPos getMCBlockPos(int var1, int var2, int var3);

	IEntity getEntity(String var1);

	IEntity createEntityFromNBT(INbt var1);

	IEntity createEntity(String var1);

	IBlock getSpawnPoint();

	void setSpawnPoint(IBlock var1);

	String getName();
}
