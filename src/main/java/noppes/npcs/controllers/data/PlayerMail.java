package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.controllers.QuestController;

public class PlayerMail implements IInventory, IPlayerMail {
	public String subject = "";
	public String sender = "";
	public NBTTagCompound message = new NBTTagCompound();
	public long time = 0L;
	public boolean beenRead = false;
	public int questId = -1;
	public NonNullList items;
	public long timePast;

	public PlayerMail() {
		this.items = NonNullList.withSize(4, ItemStack.EMPTY);
	}

	public void readNBT(NBTTagCompound compound) {
		this.subject = compound.getString("Subject");
		this.sender = compound.getString("Sender");
		this.time = compound.getLong("Time");
		this.beenRead = compound.getBoolean("BeenRead");
		this.message = compound.getCompoundTag("Message");
		this.timePast = compound.getLong("TimePast");
		if (compound.hasKey("MailQuest")) {
			this.questId = compound.getInteger("MailQuest");
		}

		this.items.clear();
		NBTTagList nbttaglist = compound.getTagList("MailItems", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;
			if (j >= 0 && j < this.items.size()) {
				this.items.set(j, new ItemStack(nbttagcompound1));
			}
		}

	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("Subject", this.subject);
		compound.setString("Sender", this.sender);
		compound.setLong("Time", this.time);
		compound.setBoolean("BeenRead", this.beenRead);
		compound.setTag("Message", this.message);
		compound.setLong("TimePast", System.currentTimeMillis() - this.time);
		compound.setInteger("MailQuest", this.questId);
		if (this.hasQuest()) {
			compound.setString("MailQuestTitle", this.getQuest().title);
		}

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.items.size(); ++i) {
			if (!((ItemStack) this.items.get(i)).isEmpty()) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				((ItemStack) this.items.get(i)).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		compound.setTag("MailItems", nbttaglist);
		return compound;
	}

	public boolean isValid() {
		return !this.subject.isEmpty() && !this.message.isEmpty() && !this.sender.isEmpty();
	}

	public boolean hasQuest() {
		return this.getQuest() != null;
	}

	public Quest getQuest() {
		return QuestController.instance != null ? (Quest) QuestController.instance.quests.get(this.questId) : null;
	}

	public int getSizeInventory() {
		return 4;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public ItemStack getStackInSlot(int i) {
		return (ItemStack) this.items.get(i);
	}

	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.items, index, count);
		if (!itemstack.isEmpty()) {
			this.markDirty();
		}

		return itemstack;
	}

	public ItemStack removeStackFromSlot(int var1) {
		return (ItemStack) this.items.set(var1, ItemStack.EMPTY);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		this.items.set(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		this.markDirty();
	}

	public ITextComponent getDisplayName() {
		return null;
	}

	public boolean hasCustomName() {
		return false;
	}

	public void markDirty() {
	}

	public boolean isUsableByPlayer(EntityPlayer var1) {
		return true;
	}

	public void openInventory(EntityPlayer player) {
	}

	public void closeInventory(EntityPlayer player) {
	}

	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	public PlayerMail copy() {
		PlayerMail mail = new PlayerMail();
		mail.readNBT(this.writeNBT());
		return mail;
	}

	public String getName() {
		return null;
	}

	public int getField(int id) {
		return 0;
	}

	public void setField(int id, int value) {
	}

	public int getFieldCount() {
		return 0;
	}

	public void clear() {
	}

	public boolean isEmpty() {
		for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
			ItemStack item = this.getStackInSlot(slot);
			if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String[] getText() {
		List list = new ArrayList();
		NBTTagList pages = this.message.getTagList("pages", 8);

		for (int i = 0; i < pages.tagCount(); ++i) {
			list.add(pages.getStringTagAt(i));
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	public void setText(String[] pages) {
		NBTTagList list = new NBTTagList();
		if (pages != null && pages.length > 0) {
			String[] var3 = pages;
			int var4 = pages.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				String page = var3[var5];
				list.appendTag(new NBTTagString(page));
			}
		}

		this.message.setTag("pages", list);
	}

	public void setQuest(int id) {
		this.questId = id;
	}

	public IContainer getContainer() {
		return NpcAPI.Instance().getIContainer((IInventory) this);
	}
}
