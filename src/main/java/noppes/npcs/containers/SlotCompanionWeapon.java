package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.roles.RoleCompanion;

class SlotCompanionWeapon extends Slot {
     final RoleCompanion role;

     public SlotCompanionWeapon(RoleCompanion role, IInventory iinventory, int id, int x, int y) {
          super(iinventory, id, x, y);
          this.role = role;
     }

     public int func_75219_a() {
          return 1;
     }

     public boolean func_75214_a(ItemStack itemstack) {
          return NoppesUtilServer.IsItemStackNull(itemstack) ? false : this.role.canWearSword(NpcAPI.Instance().getIItemStack(itemstack));
     }
}
