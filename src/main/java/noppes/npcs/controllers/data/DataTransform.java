package noppes.npcs.controllers.data;

import java.util.Iterator;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class DataTransform {
     public NBTTagCompound display;
     public NBTTagCompound ai;
     public NBTTagCompound advanced;
     public NBTTagCompound inv;
     public NBTTagCompound stats;
     public NBTTagCompound role;
     public NBTTagCompound job;
     public boolean hasDisplay;
     public boolean hasAi;
     public boolean hasAdvanced;
     public boolean hasInv;
     public boolean hasStats;
     public boolean hasRole;
     public boolean hasJob;
     public boolean isActive;
     private EntityNPCInterface npc;
     public boolean editingModus = false;

     public DataTransform(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setBoolean("TransformIsActive", this.isActive);
          this.writeOptions(compound);
          if (this.hasDisplay) {
               compound.setTag("TransformDisplay", this.display);
          }

          if (this.hasAi) {
               compound.setTag("TransformAI", this.ai);
          }

          if (this.hasAdvanced) {
               compound.setTag("TransformAdvanced", this.advanced);
          }

          if (this.hasInv) {
               compound.setTag("TransformInv", this.inv);
          }

          if (this.hasStats) {
               compound.setTag("TransformStats", this.stats);
          }

          if (this.hasRole) {
               compound.setTag("TransformRole", this.role);
          }

          if (this.hasJob) {
               compound.setTag("TransformJob", this.job);
          }

          return compound;
     }

     public Object writeOptions(NBTTagCompound compound) {
          compound.setBoolean("TransformHasDisplay", this.hasDisplay);
          compound.setBoolean("TransformHasAI", this.hasAi);
          compound.setBoolean("TransformHasAdvanced", this.hasAdvanced);
          compound.setBoolean("TransformHasInv", this.hasInv);
          compound.setBoolean("TransformHasStats", this.hasStats);
          compound.setBoolean("TransformHasRole", this.hasRole);
          compound.setBoolean("TransformHasJob", this.hasJob);
          compound.setBoolean("TransformEditingModus", this.editingModus);
          return compound;
     }

     public void readToNBT(NBTTagCompound compound) {
          this.isActive = compound.getBoolean("TransformIsActive");
          this.readOptions(compound);
          this.display = this.hasDisplay ? compound.getCompoundTag("TransformDisplay") : this.getDisplay();
          this.ai = this.hasAi ? compound.getCompoundTag("TransformAI") : this.npc.ais.writeToNBT(new NBTTagCompound());
          this.advanced = this.hasAdvanced ? compound.getCompoundTag("TransformAdvanced") : this.getAdvanced();
          this.inv = this.hasInv ? compound.getCompoundTag("TransformInv") : this.npc.inventory.writeEntityToNBT(new NBTTagCompound());
          this.stats = this.hasStats ? compound.getCompoundTag("TransformStats") : this.npc.stats.writeToNBT(new NBTTagCompound());
          this.job = this.hasJob ? compound.getCompoundTag("TransformJob") : this.getJob();
          this.role = this.hasRole ? compound.getCompoundTag("TransformRole") : this.getRole();
     }

     public NBTTagCompound getJob() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("NpcJob", this.npc.advanced.job);
          if (this.npc.advanced.job != 0 && this.npc.jobInterface != null) {
               this.npc.jobInterface.writeToNBT(compound);
          }

          return compound;
     }

     public NBTTagCompound getRole() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("Role", this.npc.advanced.role);
          if (this.npc.advanced.role != 0 && this.npc.roleInterface != null) {
               this.npc.roleInterface.writeToNBT(compound);
          }

          return compound;
     }

     public NBTTagCompound getDisplay() {
          NBTTagCompound compound = this.npc.display.writeToNBT(new NBTTagCompound());
          if (this.npc instanceof EntityCustomNpc) {
               compound.setTag("ModelData", ((EntityCustomNpc)this.npc).modelData.writeToNBT());
          }

          return compound;
     }

     public NBTTagCompound getAdvanced() {
          int jopType = this.npc.advanced.job;
          int roleType = this.npc.advanced.role;
          this.npc.advanced.job = 0;
          this.npc.advanced.role = 0;
          NBTTagCompound compound = this.npc.advanced.writeToNBT(new NBTTagCompound());
          compound.func_82580_o("Role");
          compound.func_82580_o("NpcJob");
          this.npc.advanced.job = jopType;
          this.npc.advanced.role = roleType;
          return compound;
     }

     public void readOptions(NBTTagCompound compound) {
          boolean hadDisplay = this.hasDisplay;
          boolean hadAI = this.hasAi;
          boolean hadAdvanced = this.hasAdvanced;
          boolean hadInv = this.hasInv;
          boolean hadStats = this.hasStats;
          boolean hadRole = this.hasRole;
          boolean hadJob = this.hasJob;
          this.hasDisplay = compound.getBoolean("TransformHasDisplay");
          this.hasAi = compound.getBoolean("TransformHasAI");
          this.hasAdvanced = compound.getBoolean("TransformHasAdvanced");
          this.hasInv = compound.getBoolean("TransformHasInv");
          this.hasStats = compound.getBoolean("TransformHasStats");
          this.hasRole = compound.getBoolean("TransformHasRole");
          this.hasJob = compound.getBoolean("TransformHasJob");
          this.editingModus = compound.getBoolean("TransformEditingModus");
          if (this.hasDisplay && !hadDisplay) {
               this.display = this.getDisplay();
          }

          if (this.hasAi && !hadAI) {
               this.ai = this.npc.ais.writeToNBT(new NBTTagCompound());
          }

          if (this.hasStats && !hadStats) {
               this.stats = this.npc.stats.writeToNBT(new NBTTagCompound());
          }

          if (this.hasInv && !hadInv) {
               this.inv = this.npc.inventory.writeEntityToNBT(new NBTTagCompound());
          }

          if (this.hasAdvanced && !hadAdvanced) {
               this.advanced = this.getAdvanced();
          }

          if (this.hasJob && !hadJob) {
               this.job = this.getJob();
          }

          if (this.hasRole && !hadRole) {
               this.role = this.getRole();
          }

     }

     public boolean isValid() {
          return this.hasAdvanced || this.hasAi || this.hasDisplay || this.hasInv || this.hasStats || this.hasJob || this.hasRole;
     }

     public NBTTagCompound processAdvanced(NBTTagCompound compoundAdv, NBTTagCompound compoundRole, NBTTagCompound compoundJob) {
          if (this.hasAdvanced) {
               compoundAdv = this.advanced;
          }

          if (this.hasRole) {
               compoundRole = this.role;
          }

          if (this.hasJob) {
               compoundJob = this.job;
          }

          Set names = compoundRole.getKeySet();
          Iterator var5 = names.iterator();

          String name;
          while(var5.hasNext()) {
               name = (String)var5.next();
               compoundAdv.setTag(name, compoundRole.getTag(name));
          }

          names = compoundJob.getKeySet();
          var5 = names.iterator();

          while(var5.hasNext()) {
               name = (String)var5.next();
               compoundAdv.setTag(name, compoundJob.getTag(name));
          }

          return compoundAdv;
     }

     public void transform(boolean isActive) {
          if (this.isActive != isActive) {
               NBTTagCompound compoundAdv;
               if (this.hasDisplay) {
                    compoundAdv = this.getDisplay();
                    this.npc.display.readToNBT(NBTTags.NBTMerge(compoundAdv, this.display));
                    if (this.npc instanceof EntityCustomNpc) {
                         ((EntityCustomNpc)this.npc).modelData.readFromNBT(NBTTags.NBTMerge(compoundAdv.getCompoundTag("ModelData"), this.display.getCompoundTag("ModelData")));
                    }

                    this.display = compoundAdv;
               }

               if (this.hasStats) {
                    compoundAdv = this.npc.stats.writeToNBT(new NBTTagCompound());
                    this.npc.stats.readToNBT(NBTTags.NBTMerge(compoundAdv, this.stats));
                    this.stats = compoundAdv;
               }

               if (this.hasAdvanced || this.hasJob || this.hasRole) {
                    compoundAdv = this.getAdvanced();
                    NBTTagCompound compoundRole = this.getRole();
                    NBTTagCompound compoundJob = this.getJob();
                    NBTTagCompound compound = this.processAdvanced(compoundAdv, compoundRole, compoundJob);
                    this.npc.advanced.readToNBT(compound);
                    if (this.npc.advanced.role != 0 && this.npc.roleInterface != null) {
                         this.npc.roleInterface.readFromNBT(NBTTags.NBTMerge(compoundRole, compound));
                    }

                    if (this.npc.advanced.job != 0 && this.npc.jobInterface != null) {
                         this.npc.jobInterface.readFromNBT(NBTTags.NBTMerge(compoundJob, compound));
                    }

                    if (this.hasAdvanced) {
                         this.advanced = compoundAdv;
                    }

                    if (this.hasRole) {
                         this.role = compoundRole;
                    }

                    if (this.hasJob) {
                         this.job = compoundJob;
                    }
               }

               if (this.hasAi) {
                    compoundAdv = this.npc.ais.writeToNBT(new NBTTagCompound());
                    this.npc.ais.readToNBT(NBTTags.NBTMerge(compoundAdv, this.ai));
                    this.ai = compoundAdv;
                    this.npc.setCurrentAnimation(0);
               }

               if (this.hasInv) {
                    compoundAdv = this.npc.inventory.writeEntityToNBT(new NBTTagCompound());
                    this.npc.inventory.readEntityFromNBT(NBTTags.NBTMerge(compoundAdv, this.inv));
                    this.inv = compoundAdv;
               }

               this.npc.updateAI = true;
               this.isActive = isActive;
               this.npc.updateClient = true;
          }
     }
}
