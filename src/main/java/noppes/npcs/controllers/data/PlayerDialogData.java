package noppes.npcs.controllers.data;

import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerDialogData {
     public HashSet dialogsRead = new HashSet();

     public void loadNBTData(NBTTagCompound compound) {
          HashSet dialogsRead = new HashSet();
          if (compound != null) {
               NBTTagList list = compound.func_150295_c("DialogData", 10);
               if (list != null) {
                    for(int i = 0; i < list.func_74745_c(); ++i) {
                         NBTTagCompound nbttagcompound = list.func_150305_b(i);
                         dialogsRead.add(nbttagcompound.func_74762_e("Dialog"));
                    }

                    this.dialogsRead = dialogsRead;
               }
          }
     }

     public void saveNBTData(NBTTagCompound compound) {
          NBTTagList list = new NBTTagList();
          Iterator var3 = this.dialogsRead.iterator();

          while(var3.hasNext()) {
               int dia = (Integer)var3.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74768_a("Dialog", dia);
               list.func_74742_a(nbttagcompound);
          }

          compound.func_74782_a("DialogData", list);
     }
}
