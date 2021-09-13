package noppes.npcs.roles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.BankData;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleBank extends RoleInterface {
     public int bankId = -1;

     public RoleBank(EntityNPCInterface npc) {
          super(npc);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.setInteger("RoleBankID", this.bankId);
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.bankId = nbttagcompound.func_74762_e("RoleBankID");
     }

     public void interact(EntityPlayer player) {
          BankData data = PlayerDataController.instance.getBankData(player, this.bankId).getBankOrDefault(this.bankId);
          data.openBankGui(player, this.npc, this.bankId, 0);
          this.npc.say(player, this.npc.advanced.getInteractLine());
     }

     public Bank getBank() {
          Bank bank = (Bank)BankController.getInstance().banks.get(this.bankId);
          return bank != null ? bank : (Bank)BankController.getInstance().banks.values().iterator().next();
     }
}
