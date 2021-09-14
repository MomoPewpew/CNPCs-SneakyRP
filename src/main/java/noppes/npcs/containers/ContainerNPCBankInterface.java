package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerBankData;

public class ContainerNPCBankInterface extends ContainerNpcInterface {
	public InventoryNPC currencyMatrix;
	private EntityPlayer player;
	public SlotNpcBankCurrency currency;
	public int slot = 0;
	public int bankid;
	private PlayerBankData data;

	public ContainerNPCBankInterface(EntityPlayer player, int slot, int bankid) {
		super(player);
		this.bankid = bankid;
		this.slot = slot;
		this.player = player;
		this.currencyMatrix = new InventoryNPC("currency", 1, this);
		if (!this.isAvailable() || this.canBeUpgraded()) {
			this.currency = new SlotNpcBankCurrency(this, this.currencyMatrix, 0, 80, 29);
			this.addSlotToContainer(this.currency);
		}

		NpcMiscInventory items = new NpcMiscInventory(54);
		if (!player.world.isRemote) {
			this.data = PlayerDataController.instance.getBankData(player, bankid);
			items = (NpcMiscInventory) this.data.getBankOrDefault(bankid).itemSlots.get(slot);
		}

		int xOffset = this.xOffset();

		int l;
		int j1;
		for (l = 0; l < this.getRowNumber(); ++l) {
			for (j1 = 0; j1 < 9; ++j1) {
				int id = j1 + l * 9;
				this.addSlotToContainer(new Slot(items, id, 8 + j1 * 18, 17 + xOffset + l * 18));
			}
		}

		if (this.isUpgraded()) {
			xOffset += 54;
		}

		for (l = 0; l < 3; ++l) {
			for (j1 = 0; j1 < 9; ++j1) {
				this.addSlotToContainer(new Slot(player.inventory, j1 + l * 9 + 9, 8 + j1 * 18, 86 + xOffset + l * 18));
			}
		}

		for (l = 0; l < 9; ++l) {
			this.addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 144 + xOffset));
		}

	}

	public synchronized void setCurrency(ItemStack item) {
		this.currency.item = item;
	}

	public int getRowNumber() {
		return 0;
	}

	public int xOffset() {
		return 0;
	}

	public void onCraftMatrixChanged(IInventory inv) {
	}

	public boolean isAvailable() {
		return false;
	}

	public boolean isUpgraded() {
		return false;
	}

	public boolean canBeUpgraded() {
		return false;
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return ItemStack.EMPTY;
	}

	public void onContainerClosed(EntityPlayer entityplayer) {
		super.onContainerClosed(entityplayer);
		if (!entityplayer.world.isRemote) {
			ItemStack var3 = this.currencyMatrix.getStackInSlot(0);
			this.currencyMatrix.setInventorySlotContents(0, ItemStack.EMPTY);
			if (!NoppesUtilServer.IsItemStackNull(var3)) {
				entityplayer.dropItem(var3, false);
			}
		}

	}
}
