package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class ContainerNPCTraderSetup extends Container {
	public RoleTrader role;

	public ContainerNPCTraderSetup(EntityNPCInterface npc, EntityPlayer player) {
		this.role = (RoleTrader) npc.roleInterface;

		int j1;
		int l1;
		for (j1 = 0; j1 < 18; ++j1) {
			byte x = 7;
			l1 = x + j1 % 3 * 94;
			byte y = 15;
			int y1 = y + j1 / 3 * 22;
			this.addSlotToContainer(new Slot(this.role.inventoryCurrency, j1 + 18, l1, y1));
			this.addSlotToContainer(new Slot(this.role.inventoryCurrency, j1, l1 + 18, y1));
			this.addSlotToContainer(new Slot(this.role.inventorySold, j1, l1 + 43, y1));
		}

		for (j1 = 0; j1 < 3; ++j1) {
			for (l1 = 0; l1 < 9; ++l1) {
				this.addSlotToContainer(new Slot(player.inventory, l1 + j1 * 9 + 9, 48 + l1 * 18, 147 + j1 * 18));
			}
		}

		for (j1 = 0; j1 < 9; ++j1) {
			this.addSlotToContainer(new Slot(player.inventory, j1, 48 + j1 * 18, 205));
		}

	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
		return ItemStack.EMPTY;
	}

	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
