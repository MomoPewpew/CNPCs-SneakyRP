package noppes.npcs.roles;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.role.IRoleTrader;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.NBTJsonUtil;

public class RoleTrader extends RoleInterface implements IRoleTrader {
	public String marketName = "";
	public NpcMiscInventory inventoryCurrency = new NpcMiscInventory(36);
	public NpcMiscInventory inventorySold = new NpcMiscInventory(18);
	public boolean ignoreDamage = false;
	public boolean ignoreNBT = false;
	public boolean toSave = false;

	public RoleTrader(EntityNPCInterface npc) {
		super(npc);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("TraderMarket", this.marketName);
		this.writeNBT(nbttagcompound);
		if (this.toSave && !this.npc.isRemote()) {
			save(this, this.marketName);
		}

		this.toSave = false;
		return nbttagcompound;
	}

	public NBTTagCompound writeNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setTag("TraderCurrency", this.inventoryCurrency.getToNBT());
		nbttagcompound.setTag("TraderSold", this.inventorySold.getToNBT());
		nbttagcompound.setBoolean("TraderIgnoreDamage", this.ignoreDamage);
		nbttagcompound.setBoolean("TraderIgnoreNBT", this.ignoreNBT);
		return nbttagcompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.marketName = nbttagcompound.getString("TraderMarket");
		this.readNBT(nbttagcompound);
	}

	public void readNBT(NBTTagCompound nbttagcompound) {
		this.inventoryCurrency.setFromNBT(nbttagcompound.getCompoundTag("TraderCurrency"));
		this.inventorySold.setFromNBT(nbttagcompound.getCompoundTag("TraderSold"));
		this.ignoreDamage = nbttagcompound.getBoolean("TraderIgnoreDamage");
		this.ignoreNBT = nbttagcompound.getBoolean("TraderIgnoreNBT");
	}

	public void interact(EntityPlayer player) {
		this.npc.say(player, this.npc.advanced.getInteractLine());

		try {
			load(this, this.marketName);
		} catch (Exception var3) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, (String) null, var3);
		}

		NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTrader, this.npc);
	}

	public boolean hasCurrency(ItemStack itemstack) {
		if (itemstack == null) {
			return false;
		} else {
			Iterator var2 = this.inventoryCurrency.items.iterator();

			ItemStack item;
			do {
				if (!var2.hasNext()) {
					return false;
				}

				item = (ItemStack) var2.next();
			} while (item.isEmpty()
					|| !NoppesUtilPlayer.compareItems(item, itemstack, this.ignoreDamage, this.ignoreNBT));

			return true;
		}
	}

	public IItemStack getSold(int slot) {
		return NpcAPI.Instance().getIItemStack(this.inventorySold.getStackInSlot(slot));
	}

	public IItemStack getCurrency1(int slot) {
		return NpcAPI.Instance().getIItemStack(this.inventoryCurrency.getStackInSlot(slot));
	}

	public IItemStack getCurrency2(int slot) {
		return NpcAPI.Instance().getIItemStack(this.inventoryCurrency.getStackInSlot(slot + 18));
	}

	public void set(int slot, IItemStack currency, IItemStack currency2, IItemStack sold) {
		if (sold == null) {
			throw new CustomNPCsException("Sold item was null", new Object[0]);
		} else if (slot < 18 && slot >= 0) {
			if (currency == null) {
				currency = currency2;
				currency2 = null;
			}

			if (currency != null) {
				this.inventoryCurrency.items.set(slot, currency.getMCItemStack());
			} else {
				this.inventoryCurrency.items.set(slot, ItemStack.EMPTY);
			}

			if (currency2 != null) {
				this.inventoryCurrency.items.set(slot + 18, currency2.getMCItemStack());
			} else {
				this.inventoryCurrency.items.set(slot + 18, ItemStack.EMPTY);
			}

			this.inventorySold.items.set(slot, sold.getMCItemStack());
		} else {
			throw new CustomNPCsException("Invalid slot: " + slot, new Object[0]);
		}
	}

	public void remove(int slot) {
		if (slot < 18 && slot >= 0) {
			this.inventoryCurrency.items.set(slot, ItemStack.EMPTY);
			this.inventoryCurrency.items.set(slot + 18, ItemStack.EMPTY);
			this.inventorySold.items.set(slot, ItemStack.EMPTY);
		} else {
			throw new CustomNPCsException("Invalid slot: " + slot, new Object[0]);
		}
	}

	public void setMarket(String name) {
		this.marketName = name;
		load(this, name);
	}

	public String getMarket() {
		return this.marketName;
	}

	public static void save(RoleTrader r, String name) {
		if (!name.isEmpty()) {
			File file = getFile(name + "_new");
			File file1 = getFile(name);

			try {
				NBTJsonUtil.SaveFile(file, r.writeNBT(new NBTTagCompound()));
				if (file1.exists()) {
					file1.delete();
				}

				file.renameTo(file1);
			} catch (Exception var5) {
			}

		}
	}

	public static void load(RoleTrader role, String name) {
		if (!role.npc.world.isRemote) {
			File file = getFile(name);
			if (file.exists()) {
				try {
					role.readNBT(NBTJsonUtil.LoadFile(file));
				} catch (Exception var4) {
				}

			}
		}
	}

	private static File getFile(String name) {
		File dir = new File(CustomNpcs.getWorldSaveDirectory(), "markets");
		if (!dir.exists()) {
			dir.mkdir();
		}

		return new File(dir, name.toLowerCase() + ".json");
	}

	public static void setMarket(EntityNPCInterface npc, String marketName) {
		if (!marketName.isEmpty()) {
			if (!getFile(marketName).exists()) {
				save((RoleTrader) npc.roleInterface, marketName);
			}

			load((RoleTrader) npc.roleInterface, marketName);
		}
	}
}
