package noppes.npcs.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomItems;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class ItemMounter extends Item implements IPermission {
	public ItemMounter() {
		this.maxStackSize = 1;
		this.setCreativeTab(CustomItems.tab);
	}

	public Item setUnlocalizedName(String name) {
		this.setRegistryName(new ResourceLocation("customnpcs", name));
		return super.setUnlocalizedName(name);
	}

	public boolean isAllowed(EnumPacketServer e) {
		return e == EnumPacketServer.SpawnRider || e == EnumPacketServer.PlayerRider || e == EnumPacketServer.CloneList;
	}
}
