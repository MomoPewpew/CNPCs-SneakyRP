package noppes.npcs.controllers.data;

import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerTransportData {
     public HashSet transports = new HashSet();

     public void loadNBTData(NBTTagCompound compound) {
          HashSet dialogsRead = new HashSet();
          if (compound != null) {
               NBTTagList list = compound.func_150295_c("TransportData", 10);
               if (list != null) {
                    for(int i = 0; i < list.func_74745_c(); ++i) {
                         NBTTagCompound nbttagcompound = list.func_150305_b(i);
                         dialogsRead.add(nbttagcompound.func_74762_e("Transport"));
                    }

                    this.transports = dialogsRead;
               }
          }
     }

     public void saveNBTData(NBTTagCompound compound) {
          NBTTagList list = new NBTTagList();
          Iterator var3 = this.transports.iterator();

          while(var3.hasNext()) {
               int dia = (Integer)var3.next();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.func_74768_a("Transport", dia);
               list.func_74742_a(nbttagcompound);
          }

          compound.func_74782_a("TransportData", list);
     }
}
