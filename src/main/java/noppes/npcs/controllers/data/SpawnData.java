package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom.Item;
import noppes.npcs.NBTTags;

public class SpawnData extends Item {
     public List biomes = new ArrayList();
     public int id = -1;
     public String name = "";
     public NBTTagCompound compound1 = new NBTTagCompound();
     public boolean liquid = false;
     public int type = 0;

     public SpawnData() {
          super(10);
     }

     public void readNBT(NBTTagCompound compound) {
          this.id = compound.func_74762_e("SpawnId");
          this.name = compound.func_74779_i("SpawnName");
          this.field_76292_a = compound.func_74762_e("SpawnWeight");
          if (this.field_76292_a == 0) {
               this.field_76292_a = 1;
          }

          this.biomes = NBTTags.getStringList(compound.func_150295_c("SpawnBiomes", 10));
          this.compound1 = compound.func_74775_l("SpawnCompound1");
          this.type = compound.func_74762_e("SpawnType");
     }

     public NBTTagCompound writeNBT(NBTTagCompound compound) {
          compound.func_74768_a("SpawnId", this.id);
          compound.func_74778_a("SpawnName", this.name);
          compound.func_74768_a("SpawnWeight", this.field_76292_a);
          compound.func_74782_a("SpawnBiomes", NBTTags.nbtStringList(this.biomes));
          compound.func_74782_a("SpawnCompound1", this.compound1);
          compound.func_74768_a("SpawnType", this.type);
          return compound;
     }
}
