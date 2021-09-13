package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;

public class ContainerNPCCompanion extends ContainerNpcInterface {
     public InventoryNPC currencyMatrix;
     public RoleCompanion role;

     public ContainerNPCCompanion(EntityNPCInterface npc, EntityPlayer player) {
          super(player);
          this.role = (RoleCompanion)npc.roleInterface;

          int size;
          int i;
          for(size = 0; size < 3; ++size) {
               for(i = 0; i < 9; ++i) {
                    this.addSlotToContainer(new Slot(player.inventory, i + size * 9 + 9, 6 + i * 18, 87 + size * 18));
               }
          }

          for(size = 0; size < 9; ++size) {
               this.addSlotToContainer(new Slot(player.inventory, size, 6 + size * 18, 145));
          }

          if (this.role.talents.containsKey(EnumCompanionTalent.INVENTORY)) {
               size = (this.role.getTalentLevel(EnumCompanionTalent.INVENTORY) + 1) * 2;

               for(i = 0; i < size; ++i) {
                    this.addSlotToContainer(new Slot(this.role.inventory, i, 114 + i % 3 * 18, 8 + i / 3 * 18));
               }
          }

          if (this.role.getTalentLevel(EnumCompanionTalent.ARMOR) > 0) {
               this.addSlotToContainer(new SlotCompanionArmor(this.role, npc.inventory, 0, 6, 8, EntityEquipmentSlot.HEAD));
               this.addSlotToContainer(new SlotCompanionArmor(this.role, npc.inventory, 1, 6, 26, EntityEquipmentSlot.CHEST));
               this.addSlotToContainer(new SlotCompanionArmor(this.role, npc.inventory, 2, 6, 44, EntityEquipmentSlot.LEGS));
               this.addSlotToContainer(new SlotCompanionArmor(this.role, npc.inventory, 3, 6, 62, EntityEquipmentSlot.FEET));
          }

          if (this.role.getTalentLevel(EnumCompanionTalent.SWORD) > 0) {
               this.addSlotToContainer(new SlotCompanionWeapon(this.role, npc.inventory, 4, 79, 17));
          }

     }

     public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i) {
          return ItemStack.EMPTY;
     }

     public void onContainerClosed(EntityPlayer entityplayer) {
          super.onContainerClosed(entityplayer);
          this.role.setStats();
     }
}
