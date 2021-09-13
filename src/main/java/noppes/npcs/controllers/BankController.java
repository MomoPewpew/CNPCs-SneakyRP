package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.data.Bank;

public class BankController {
     public HashMap banks;
     private String filePath = "";
     private static BankController instance;

     public BankController() {
          instance = this;
          this.banks = new HashMap();
          this.loadBanks();
          if (this.banks.isEmpty()) {
               Bank bank = new Bank();
               bank.id = 0;
               bank.name = "Default Bank";

               for(int i = 0; i < 6; ++i) {
                    bank.slotTypes.put(i, 0);
               }

               this.banks.put(bank.id, bank);
          }

     }

     public static BankController getInstance() {
          if (newInstance()) {
               instance = new BankController();
          }

          return instance;
     }

     private static boolean newInstance() {
          if (instance == null) {
               return true;
          } else {
               File file = CustomNpcs.getWorldSaveDirectory();
               if (file == null) {
                    return false;
               } else {
                    return !instance.filePath.equals(file.getAbsolutePath());
               }
          }
     }

     private void loadBanks() {
          File saveDir = CustomNpcs.getWorldSaveDirectory();
          if (saveDir != null) {
               this.filePath = saveDir.getAbsolutePath();

               try {
                    File file = new File(saveDir, "bank.dat");
                    if (file.exists()) {
                         this.loadBanks(file);
                    }
               } catch (Exception var5) {
                    try {
                         File file = new File(saveDir, "bank.dat_old");
                         if (file.exists()) {
                              this.loadBanks(file);
                         }
                    } catch (Exception var4) {
                    }
               }

          }
     }

     private void loadBanks(File file) throws IOException {
          this.loadBanks(CompressedStreamTools.func_74796_a(new FileInputStream(file)));
     }

     public void loadBanks(NBTTagCompound nbttagcompound1) throws IOException {
          HashMap banks = new HashMap();
          NBTTagList list = nbttagcompound1.func_150295_c("Data", 10);
          if (list != null) {
               for(int i = 0; i < list.func_74745_c(); ++i) {
                    NBTTagCompound nbttagcompound = list.func_150305_b(i);
                    Bank bank = new Bank();
                    bank.readEntityFromNBT(nbttagcompound);
                    banks.put(bank.id, bank);
               }
          }

          this.banks = banks;
     }

     public NBTTagCompound getNBT() {
          NBTTagList list = new NBTTagList();
          Iterator var2 = this.banks.values().iterator();

          while(var2.hasNext()) {
               Bank bank = (Bank)var2.next();
               NBTTagCompound nbtfactions = new NBTTagCompound();
               bank.writeEntityToNBT(nbtfactions);
               list.func_74742_a(nbtfactions);
          }

          NBTTagCompound nbttagcompound = new NBTTagCompound();
          nbttagcompound.func_74782_a("Data", list);
          return nbttagcompound;
     }

     public Bank getBank(int bankId) {
          Bank bank = (Bank)this.banks.get(bankId);
          return bank != null ? bank : (Bank)this.banks.values().iterator().next();
     }

     public void saveBanks() {
          try {
               File saveDir = CustomNpcs.getWorldSaveDirectory();
               File file = new File(saveDir, "bank.dat_new");
               File file1 = new File(saveDir, "bank.dat_old");
               File file2 = new File(saveDir, "bank.dat");
               CompressedStreamTools.func_74799_a(this.getNBT(), new FileOutputStream(file));
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
          } catch (Exception var5) {
               var5.printStackTrace();
          }

     }

     public void saveBank(Bank bank) {
          if (bank.id < 0) {
               bank.id = this.getUnusedId();
          }

          this.banks.put(bank.id, bank);
          this.saveBanks();
     }

     public int getUnusedId() {
          int id;
          for(id = 0; this.banks.containsKey(id); ++id) {
          }

          return id;
     }

     public void removeBank(int bank) {
          if (bank >= 0 && this.banks.size() > 1) {
               this.banks.remove(bank);
               this.saveBanks();
          }
     }
}
