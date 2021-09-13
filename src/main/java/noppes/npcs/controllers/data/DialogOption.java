package noppes.npcs.controllers.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.handler.data.IDialogOption;
import noppes.npcs.controllers.DialogController;

public class DialogOption implements IDialogOption {
     public int dialogId = -1;
     public String title = "Talk";
     public int optionType = 1;
     public int optionColor = 14737632;
     public String command = "";
     public int slot = -1;

     public void readNBT(NBTTagCompound compound) {
          if (compound != null) {
               this.title = compound.func_74779_i("Title");
               this.dialogId = compound.func_74762_e("Dialog");
               this.optionColor = compound.func_74762_e("DialogColor");
               this.optionType = compound.func_74762_e("OptionType");
               this.command = compound.func_74779_i("DialogCommand");
               if (this.optionColor == 0) {
                    this.optionColor = 14737632;
               }

          }
     }

     public NBTTagCompound writeNBT() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.func_74778_a("Title", this.title);
          compound.func_74768_a("OptionType", this.optionType);
          compound.func_74768_a("Dialog", this.dialogId);
          compound.func_74768_a("DialogColor", this.optionColor);
          compound.func_74778_a("DialogCommand", this.command);
          return compound;
     }

     public boolean hasDialog() {
          if (this.dialogId > 0 && this.optionType == 1) {
               if (!DialogController.instance.hasDialog(this.dialogId)) {
                    this.dialogId = -1;
                    return false;
               } else {
                    return true;
               }
          } else {
               return false;
          }
     }

     public Dialog getDialog() {
          return !this.hasDialog() ? null : (Dialog)DialogController.instance.dialogs.get(this.dialogId);
     }

     public boolean isAvailable(EntityPlayer player) {
          if (this.optionType == 2) {
               return false;
          } else if (this.optionType != 1) {
               return true;
          } else {
               Dialog dialog = this.getDialog();
               return dialog == null ? false : dialog.availability.isAvailable(player);
          }
     }

     public int getSlot() {
          return this.slot;
     }

     public String getName() {
          return this.title;
     }

     public int getType() {
          return this.optionType;
     }
}
