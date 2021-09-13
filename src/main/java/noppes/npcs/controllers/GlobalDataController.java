package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;

public class GlobalDataController {
     public static GlobalDataController instance;
     private int itemGiverId = 0;

     public GlobalDataController() {
          instance = this;
          this.load();
     }

     private void load() {
          File saveDir = CustomNpcs.getWorldSaveDirectory();

          try {
               File file = new File(saveDir, "global.dat");
               if (file.exists()) {
                    this.loadData(file);
               }
          } catch (Exception var5) {
               try {
                    File file = new File(saveDir, "global.dat_old");
                    if (file.exists()) {
                         this.loadData(file);
                    }
               } catch (Exception var4) {
                    var4.printStackTrace();
               }
          }

     }

     private void loadData(File file) throws Exception {
          NBTTagCompound nbttagcompound1 = CompressedStreamTools.func_74796_a(new FileInputStream(file));
          this.itemGiverId = nbttagcompound1.func_74762_e("itemGiverId");
     }

     public void saveData() {
          try {
               File saveDir = CustomNpcs.getWorldSaveDirectory();
               NBTTagCompound nbttagcompound = new NBTTagCompound();
               nbttagcompound.setInteger("itemGiverId", this.itemGiverId);
               File file = new File(saveDir, "global.dat_new");
               File file1 = new File(saveDir, "global.dat_old");
               File file2 = new File(saveDir, "global.dat");
               CompressedStreamTools.func_74799_a(nbttagcompound, new FileOutputStream(file));
               if (file1.exists()) {
                    file1.delete();
               }

               file2.renameTo(file1);
               if (file2.exists()) {
                    file2.delete();
               }

               file.renameTo(file2);
               if (file.exists()) {
                    file.delete();
               }
          } catch (Exception var6) {
               var6.printStackTrace();
          }

     }

     public int incrementItemGiverId() {
          ++this.itemGiverId;
          this.saveData();
          return this.itemGiverId;
     }
}
