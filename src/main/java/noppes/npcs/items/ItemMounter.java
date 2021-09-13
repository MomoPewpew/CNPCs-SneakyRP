package noppes.npcs.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomItems;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class ItemMounter extends Item implements IPermission {
     public ItemMounter() {
          this.field_77777_bU = 1;
          this.func_77637_a(CustomItems.tab);
     }

     public Item func_77655_b(String name) {
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return super.func_77655_b(name);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SpawnRider || e == EnumPacketServer.PlayerRider || e == EnumPacketServer.CloneList;
     }
}
