package noppes.npcs.api;

import java.io.File;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.handler.ICloneHandler;
import noppes.npcs.api.handler.IDialogHandler;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.item.IItemStack;

public abstract class NpcAPI {
	private static NpcAPI instance = null;

	public abstract ICustomNpc createNPC(World var1);

	public abstract ICustomNpc spawnNPC(World var1, int var2, int var3, int var4);

	public abstract IEntity getIEntity(Entity var1);

	public abstract IBlock getIBlock(World var1, BlockPos var2);

	public abstract IContainer getIContainer(IInventory var1);

	public abstract IContainer getIContainer(Container var1);

	public abstract IItemStack getIItemStack(ItemStack var1);

	public abstract IWorld getIWorld(WorldServer var1);

	public abstract IWorld getIWorld(int var1);

	public abstract IWorld[] getIWorlds();

	public abstract INbt getINbt(NBTTagCompound var1);

	public abstract IPos getIPos(double var1, double var3, double var5);

	public abstract IFactionHandler getFactions();

	public abstract IRecipeHandler getRecipes();

	public abstract IQuestHandler getQuests();

	public abstract IDialogHandler getDialogs();

	public abstract ICloneHandler getClones();

	public abstract IDamageSource getIDamageSource(DamageSource var1);

	public abstract INbt stringToNbt(String var1);

	public abstract IPlayerMail createMail(String var1, String var2);

	public abstract ICustomGui createCustomGui(int var1, int var2, int var3, boolean var4);

	public abstract INbt getRawPlayerData(String var1);

	public abstract EventBus events();

	public abstract void registerCommand(CommandNoppesBase var1);

	public abstract File getGlobalDir();

	public abstract File getWorldDir();

	public static boolean IsAvailable() {
		return Loader.isModLoaded("customnpcs");
	}

	public static NpcAPI Instance() {
		if (instance != null) {
			return instance;
		} else if (!IsAvailable()) {
			return null;
		} else {
			try {
				Class c = Class.forName("noppes.npcs.api.wrapper.WrapperNpcAPI");
				instance = (NpcAPI) c.getMethod("Instance").invoke((Object) null);
			} catch (Exception var1) {
				var1.printStackTrace();
			}

			return instance;
		}
	}

	public abstract void registerPermissionNode(String var1, int var2);

	public abstract boolean hasPermissionNode(String var1);

	public abstract String executeCommand(IWorld var1, String var2);

	public abstract String getRandomName(int var1, int var2);
}
