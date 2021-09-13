package noppes.npcs.containers;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;

public class ContainerMail extends ContainerNpcInterface {
     public static PlayerMail staticmail = new PlayerMail();
     public PlayerMail mail = new PlayerMail();
     private boolean canEdit;
     private boolean canSend;

     public ContainerMail(EntityPlayer player, boolean canEdit, boolean canSend) {
          super(player);
          this.mail = staticmail;
          staticmail = new PlayerMail();
          this.canEdit = canEdit;
          this.canSend = canSend;
          player.inventory.func_174889_b(player);

          int k;
          for(k = 0; k < 4; ++k) {
               this.func_75146_a(new SlotValid(this.mail, k, 179 + k * 24, 138, canEdit));
          }

          int j;
          for(j = 0; j < 3; ++j) {
               for(k = 0; k < 9; ++k) {
                    this.func_75146_a(new Slot(player.inventory, k + j * 9 + 9, 28 + k * 18, 175 + j * 18));
               }
          }

          for(j = 0; j < 9; ++j) {
               this.func_75146_a(new Slot(player.inventory, j, 28 + j * 18, 230));
          }

     }

     public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par2) {
          ItemStack itemstack = ItemStack.field_190927_a;
          Slot slot = (Slot)this.field_75151_b.get(par2);
          if (slot != null && slot.func_75216_d()) {
               ItemStack itemstack1 = slot.func_75211_c();
               itemstack = itemstack1.func_77946_l();
               if (par2 < 4) {
                    if (!this.func_75135_a(itemstack1, 4, this.field_75151_b.size(), true)) {
                         return ItemStack.field_190927_a;
                    }
               } else if (!this.canEdit || !this.func_75135_a(itemstack1, 0, 4, false)) {
                    return null;
               }

               if (itemstack1.func_190916_E() == 0) {
                    slot.func_75215_d(ItemStack.field_190927_a);
               } else {
                    slot.func_75218_e();
               }
          }

          return itemstack;
     }

     public void func_75134_a(EntityPlayer player) {
          super.func_75134_a(player);
          if (!this.canEdit && !player.world.field_72995_K) {
               PlayerMailData data = PlayerData.get(player).mailData;
               Iterator it = data.playermail.iterator();

               while(it.hasNext()) {
                    PlayerMail mail = (PlayerMail)it.next();
                    if (mail.time == this.mail.time && mail.sender.equals(this.mail.sender)) {
                         mail.readNBT(this.mail.writeNBT());
                         break;
                    }
               }
          }

     }
}
