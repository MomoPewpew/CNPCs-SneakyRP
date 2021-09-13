package noppes.npcs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumParts;

public class EntityCustomNpc extends EntityNPCFlying {
     public ModelData modelData = new ModelData();

     public EntityCustomNpc(World world) {
          super(world);
          if (!CustomNpcs.EnableDefaultEyes) {
               this.modelData.eyes.type = -1;
          }

     }

     public void func_70037_a(NBTTagCompound compound) {
          if (compound.hasKey("NpcModelData")) {
               this.modelData.readFromNBT(compound.getCompoundTag("NpcModelData"));
          }

          super.func_70037_a(compound);
     }

     public void func_70014_b(NBTTagCompound compound) {
          super.func_70014_b(compound);
          compound.setTag("NpcModelData", this.modelData.writeToNBT());
     }

     public boolean func_70039_c(NBTTagCompound compound) {
          boolean bo = super.func_184198_c(compound);
          if (bo) {
               String s = this.func_70022_Q();
               if (s.equals("minecraft:customnpcs.customnpc")) {
                    compound.setString("id", "customnpcs:customnpc");
               }
          }

          return bo;
     }

     public void func_70071_h_() {
          super.func_70071_h_();
          if (this.isRemote()) {
               ModelPartData particles = this.modelData.getPartData(EnumParts.PARTICLES);
               if (particles != null && !this.isKilled()) {
                    CustomNpcs.proxy.spawnParticle(this, "ModelData", this.modelData, particles);
               }

               EntityLivingBase entity = this.modelData.getEntity(this);
               if (entity != null) {
                    try {
                         entity.func_70071_h_();
                    } catch (Exception var4) {
                    }

                    EntityUtil.Copy(this, entity);
               }
          }

          this.modelData.eyes.update(this);
     }

     public boolean func_184205_a(Entity par1Entity, boolean force) {
          boolean b = super.func_184205_a(par1Entity, force);
          this.updateHitbox();
          return b;
     }

     public void updateHitbox() {
          Entity entity = this.modelData.getEntity(this);
          if (this.modelData != null && entity != null) {
               if (entity instanceof EntityNPCInterface) {
                    ((EntityNPCInterface)entity).updateHitbox();
               }

               this.field_70130_N = entity.field_70130_N / 5.0F * (float)this.display.getSize();
               this.height = entity.height / 5.0F * (float)this.display.getSize();
               if (this.field_70130_N < 0.1F) {
                    this.field_70130_N = 0.1F;
               }

               if (this.height < 0.1F) {
                    this.height = 0.1F;
               }

               if (!this.display.getHasHitbox() || this.isKilled() && this.stats.hideKilledBody) {
                    this.field_70130_N = 1.0E-5F;
               }

               double var10000 = (double)(this.field_70130_N / 2.0F);
               World var10001 = this.world;
               if (var10000 > World.MAX_ENTITY_RADIUS) {
                    World var2 = this.world;
                    World.MAX_ENTITY_RADIUS = (double)(this.field_70130_N / 2.0F);
               }

               this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
          } else {
               this.baseHeight = 1.9F - this.modelData.getBodyY() + (this.modelData.getPartConfig(EnumParts.HEAD).scaleY - 1.0F) / 2.0F;
               super.updateHitbox();
          }

     }
}
