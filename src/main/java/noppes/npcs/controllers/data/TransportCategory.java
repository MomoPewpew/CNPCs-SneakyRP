package noppes.npcs.controllers.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TransportCategory {
     public int id = -1;
     public String title = "";
     public HashMap locations = new HashMap();

     public Vector getDefaultLocations() {
          Vector list = new Vector();
          Iterator var2 = this.locations.values().iterator();

          while(var2.hasNext()) {
               TransportLocation loc = (TransportLocation)var2.next();
               if (loc.isDefault()) {
                    list.add(loc);
               }
          }

          return list;
     }

     public void readNBT(NBTTagCompound compound) {
          this.id = compound.func_74762_e("CategoryId");
          this.title = compound.func_74779_i("CategoryTitle");
          NBTTagList locs = compound.func_150295_c("CategoryLocations", 10);
          if (locs != null && locs.func_74745_c() != 0) {
               for(int ii = 0; ii < locs.func_74745_c(); ++ii) {
                    TransportLocation location = new TransportLocation();
                    location.readNBT(locs.func_150305_b(ii));
                    location.category = this;
                    this.locations.put(location.id, location);
               }

          }
     }

     public void writeNBT(NBTTagCompound compound) {
          compound.func_74768_a("CategoryId", this.id);
          compound.func_74778_a("CategoryTitle", this.title);
          NBTTagList locs = new NBTTagList();
          Iterator var3 = this.locations.values().iterator();

          while(var3.hasNext()) {
               TransportLocation location = (TransportLocation)var3.next();
               locs.func_74742_a(location.writeNBT());
          }

          compound.func_74782_a("CategoryLocations", locs);
     }
}
