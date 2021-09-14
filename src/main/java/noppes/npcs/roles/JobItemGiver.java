package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerItemGiverData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobItemGiver extends JobInterface {
	public int cooldownType = 0;
	public int givingMethod = 0;
	public int cooldown = 10;
	public NpcMiscInventory inventory = new NpcMiscInventory(9);
	public int itemGiverId = 0;
	public List lines = new ArrayList();
	private int ticks = 10;
	private List recentlyChecked = new ArrayList();
	private List toCheck;
	public Availability availability = new Availability();

	public JobItemGiver(EntityNPCInterface npc) {
		super(npc);
		this.lines.add("Have these items {player}");
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("igCooldownType", this.cooldownType);
		nbttagcompound.setInteger("igGivingMethod", this.givingMethod);
		nbttagcompound.setInteger("igCooldown", this.cooldown);
		nbttagcompound.setInteger("ItemGiverId", this.itemGiverId);
		nbttagcompound.setTag("igLines", NBTTags.nbtStringList(this.lines));
		nbttagcompound.setTag("igJobInventory", this.inventory.getToNBT());
		nbttagcompound.setTag("igAvailability", this.availability.writeToNBT(new NBTTagCompound()));
		return nbttagcompound;
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		this.itemGiverId = nbttagcompound.getInteger("ItemGiverId");
		this.cooldownType = nbttagcompound.getInteger("igCooldownType");
		this.givingMethod = nbttagcompound.getInteger("igGivingMethod");
		this.cooldown = nbttagcompound.getInteger("igCooldown");
		this.lines = NBTTags.getStringList(nbttagcompound.getTagList("igLines", 10));
		this.inventory.setFromNBT(nbttagcompound.getCompoundTag("igJobInventory"));
		if (this.itemGiverId == 0 && GlobalDataController.instance != null) {
			this.itemGiverId = GlobalDataController.instance.incrementItemGiverId();
		}

		this.availability.readFromNBT(nbttagcompound.getCompoundTag("igAvailability"));
	}

	public NBTTagList newHashMapNBTList(HashMap lines) {
		NBTTagList nbttaglist = new NBTTagList();
		Iterator var4 = lines.keySet().iterator();

		while (var4.hasNext()) {
			String s = (String) var4.next();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Line", s);
			nbttagcompound.setLong("Time", (Long) lines.get(s));
			nbttaglist.appendTag(nbttagcompound);
		}

		return nbttaglist;
	}

	public HashMap getNBTLines(NBTTagList tagList) {
		HashMap map = new HashMap();

		for (int i = 0; i < tagList.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
			String line = nbttagcompound.getString("Line");
			long time = nbttagcompound.getLong("Time");
			map.put(line, time);
		}

		return map;
	}

	private boolean giveItems(EntityPlayer player) {
		PlayerItemGiverData data = PlayerData.get(player).itemgiverData;
		if (!this.canPlayerInteract(data)) {
			return false;
		} else {
			Vector items = new Vector();
			Vector toGive = new Vector();
			Iterator var5 = this.inventory.items.iterator();

			ItemStack is;
			while (var5.hasNext()) {
				is = (ItemStack) var5.next();
				if (!is.isEmpty()) {
					items.add(is.copy());
				}
			}

			if (items.isEmpty()) {
				return false;
			} else {
				if (this.isAllGiver()) {
					toGive = items;
				} else if (this.isRemainingGiver()) {
					var5 = items.iterator();

					while (var5.hasNext()) {
						is = (ItemStack) var5.next();
						if (!this.playerHasItem(player, is.getItem())) {
							toGive.add(is);
						}
					}
				} else if (this.isRandomGiver()) {
					toGive.add(((ItemStack) items.get(this.npc.world.rand.nextInt(items.size()))).copy());
				} else if (this.isGiverWhenNotOwnedAny()) {
					boolean ownsItems = false;
					Iterator var10 = items.iterator();

					while (var10.hasNext()) {
						ItemStack is1 = (ItemStack) var10.next();
						if (this.playerHasItem(player, is1.getItem())) {
							ownsItems = true;
							break;
						}
					}

					if (ownsItems) {
						return false;
					}

					toGive = items;
				} else if (this.isChainedGiver()) {
					int itemIndex = data.getItemIndex(this);
					int i = 0;

					for (Iterator var13 = this.inventory.items.iterator(); var13.hasNext(); ++i) {
						ItemStack item = (ItemStack) var13.next();
						if (i == itemIndex) {
							toGive.add(item);
							break;
						}
					}
				}

				if (toGive.isEmpty()) {
					return false;
				} else if (this.givePlayerItems(player, toGive)) {
					if (!this.lines.isEmpty()) {
						this.npc.say(player,
								new Line((String) this.lines.get(this.npc.getRNG().nextInt(this.lines.size()))));
					}

					if (this.isDaily()) {
						data.setTime(this, (long) this.getDay());
					} else {
						data.setTime(this, System.currentTimeMillis());
					}

					if (this.isChainedGiver()) {
						data.setItemIndex(this, (data.getItemIndex(this) + 1) % this.inventory.items.size());
					}

					return true;
				} else {
					return false;
				}
			}
		}
	}

	private int getDay() {
		return (int) (this.npc.world.getTotalWorldTime() / 24000L);
	}

	private boolean canPlayerInteract(PlayerItemGiverData data) {
		if (this.inventory.items.isEmpty()) {
			return false;
		} else if (this.isOnTimer()) {
			if (!data.hasInteractedBefore(this)) {
				return true;
			} else {
				return data.getTime(this) + (long) (this.cooldown * 1000) < System.currentTimeMillis();
			}
		} else if (this.isGiveOnce()) {
			return !data.hasInteractedBefore(this);
		} else if (this.isDaily()) {
			if (!data.hasInteractedBefore(this)) {
				return true;
			} else {
				return (long) this.getDay() > data.getTime(this);
			}
		} else {
			return false;
		}
	}

	private boolean givePlayerItems(EntityPlayer player, Vector toGive) {
		if (toGive.isEmpty()) {
			return false;
		} else if (this.freeInventorySlots(player) < toGive.size()) {
			return false;
		} else {
			Iterator var3 = toGive.iterator();

			while (var3.hasNext()) {
				ItemStack is = (ItemStack) var3.next();
				this.npc.givePlayerItem(player, is);
			}

			return true;
		}
	}

	private boolean playerHasItem(EntityPlayer player, Item item) {
		Iterator var3 = player.inventory.mainInventory.iterator();

		ItemStack is;
		do {
			if (!var3.hasNext()) {
				var3 = player.inventory.armorInventory.iterator();

				do {
					if (!var3.hasNext()) {
						return false;
					}

					is = (ItemStack) var3.next();
				} while (is.isEmpty() || is.getItem() != item);

				return true;
			}

			is = (ItemStack) var3.next();
		} while (is.isEmpty() || is.getItem() != item);

		return true;
	}

	private int freeInventorySlots(EntityPlayer player) {
		int i = 0;
		Iterator var3 = player.inventory.mainInventory.iterator();

		while (var3.hasNext()) {
			ItemStack is = (ItemStack) var3.next();
			if (NoppesUtilServer.IsItemStackNull(is)) {
				++i;
			}
		}

		return i;
	}

	private boolean isRandomGiver() {
		return this.givingMethod == 0;
	}

	private boolean isAllGiver() {
		return this.givingMethod == 1;
	}

	private boolean isRemainingGiver() {
		return this.givingMethod == 2;
	}

	private boolean isGiverWhenNotOwnedAny() {
		return this.givingMethod == 3;
	}

	private boolean isChainedGiver() {
		return this.givingMethod == 4;
	}

	public boolean isOnTimer() {
		return this.cooldownType == 0;
	}

	private boolean isGiveOnce() {
		return this.cooldownType == 1;
	}

	private boolean isDaily() {
		return this.cooldownType == 2;
	}

	public boolean aiShouldExecute() {
		if (this.npc.isAttacking()) {
			return false;
		} else {
			--this.ticks;
			if (this.ticks > 0) {
				return false;
			} else {
				this.ticks = 10;
				this.toCheck = this.npc.world.getEntitiesWithinAABB(EntityPlayer.class,
						this.npc.getEntityBoundingBox().grow(3.0D, 3.0D, 3.0D));
				this.toCheck.removeAll(this.recentlyChecked);
				List listMax = this.npc.world.getEntitiesWithinAABB(EntityPlayer.class,
						this.npc.getEntityBoundingBox().grow(10.0D, 10.0D, 10.0D));
				this.recentlyChecked.retainAll(listMax);
				this.recentlyChecked.addAll(this.toCheck);
				return this.toCheck.size() > 0;
			}
		}
	}

	public boolean aiContinueExecute() {
		return false;
	}

	public void aiStartExecuting() {
		Iterator var1 = this.toCheck.iterator();

		while (var1.hasNext()) {
			EntityPlayer player = (EntityPlayer) var1.next();
			if (this.npc.canSee(player) && this.availability.isAvailable(player)) {
				this.recentlyChecked.add(player);
				this.interact(player);
			}
		}

	}

	public void killed() {
	}

	private boolean interact(EntityPlayer player) {
		if (!this.giveItems(player)) {
			this.npc.say(player, this.npc.advanced.getInteractLine());
		}

		return true;
	}

	public void delete() {
	}
}
