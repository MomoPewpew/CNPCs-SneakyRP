package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.WorldServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IRayTrace;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityItem;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.ServerCloneController;

public class EntityWrapper implements IEntity {
     protected Entity entity;
     private Map tempData = new HashMap();
     private IWorld worldWrapper;
     private final IData tempdata = new IData() {
          public void put(String key, Object value) {
               EntityWrapper.this.tempData.put(key, value);
          }

          public Object get(String key) {
               return EntityWrapper.this.tempData.get(key);
          }

          public void remove(String key) {
               EntityWrapper.this.tempData.remove(key);
          }

          public boolean has(String key) {
               return EntityWrapper.this.tempData.containsKey(key);
          }

          public void clear() {
               EntityWrapper.this.tempData.clear();
          }

          public String[] getKeys() {
               return (String[])EntityWrapper.this.tempData.keySet().toArray(new String[EntityWrapper.this.tempData.size()]);
          }
     };
     private final IData storeddata = new IData() {
          public void put(String key, Object value) {
               NBTTagCompound compound = this.getStoredCompound();
               if (value instanceof Number) {
                    compound.func_74780_a(key, ((Number)value).doubleValue());
               } else if (value instanceof String) {
                    compound.setString(key, (String)value);
               }

               this.saveStoredCompound(compound);
          }

          public Object get(String key) {
               NBTTagCompound compound = this.getStoredCompound();
               if (!compound.hasKey(key)) {
                    return null;
               } else {
                    NBTBase base = compound.getTag(key);
                    return base instanceof NBTPrimitive ? ((NBTPrimitive)base).func_150286_g() : ((NBTTagString)base).func_150285_a_();
               }
          }

          public void remove(String key) {
               NBTTagCompound compound = this.getStoredCompound();
               compound.func_82580_o(key);
               this.saveStoredCompound(compound);
          }

          public boolean has(String key) {
               return this.getStoredCompound().hasKey(key);
          }

          public void clear() {
               EntityWrapper.this.entity.getEntityData().func_82580_o("CNPCStoredData");
          }

          private NBTTagCompound getStoredCompound() {
               NBTTagCompound compound = EntityWrapper.this.entity.getEntityData().getCompoundTag("CNPCStoredData");
               if (compound == null) {
                    EntityWrapper.this.entity.getEntityData().setTag("CNPCStoredData", compound = new NBTTagCompound());
               }

               return compound;
          }

          private void saveStoredCompound(NBTTagCompound compound) {
               EntityWrapper.this.entity.getEntityData().setTag("CNPCStoredData", compound);
          }

          public String[] getKeys() {
               NBTTagCompound compound = this.getStoredCompound();
               return (String[])compound.getKeySet().toArray(new String[compound.getKeySet().size()]);
          }
     };

     public EntityWrapper(Entity entity) {
          this.entity = entity;
          this.worldWrapper = NpcAPI.Instance().getIWorld((WorldServer)entity.world);
     }

     public double getX() {
          return this.entity.field_70165_t;
     }

     public void setX(double x) {
          this.entity.field_70165_t = x;
     }

     public double getY() {
          return this.entity.field_70163_u;
     }

     public void setY(double y) {
          this.entity.field_70163_u = y;
     }

     public double getZ() {
          return this.entity.field_70161_v;
     }

     public void setZ(double z) {
          this.entity.field_70161_v = z;
     }

     public int getBlockX() {
          return MathHelper.func_76128_c(this.entity.field_70165_t);
     }

     public int getBlockY() {
          return MathHelper.func_76128_c(this.entity.field_70163_u);
     }

     public int getBlockZ() {
          return MathHelper.func_76128_c(this.entity.field_70161_v);
     }

     public String getEntityName() {
          String s = EntityList.func_75621_b(this.entity);
          if (s == null) {
               s = "generic";
          }

          return I18n.func_74838_a("entity." + s + ".name");
     }

     public String getName() {
          return this.entity.func_70005_c_();
     }

     public void setName(String name) {
          this.entity.func_96094_a(name);
     }

     public boolean hasCustomName() {
          return this.entity.func_145818_k_();
     }

     public void setPosition(double x, double y, double z) {
          this.entity.func_70107_b(x, y, z);
     }

     public IWorld getWorld() {
          if (this.entity.world != this.worldWrapper.getMCWorld()) {
               this.worldWrapper = NpcAPI.Instance().getIWorld((WorldServer)this.entity.world);
          }

          return this.worldWrapper;
     }

     public boolean isAlive() {
          return this.entity.func_70089_S();
     }

     public IData getTempdata() {
          return this.tempdata;
     }

     public IData getStoreddata() {
          return this.storeddata;
     }

     public long getAge() {
          return (long)this.entity.field_70173_aa;
     }

     public void damage(float amount) {
          this.entity.func_70097_a(DamageSource.field_76377_j, amount);
     }

     public void despawn() {
          this.entity.field_70128_L = true;
     }

     public void spawn() {
          if (this.worldWrapper.getMCWorld().func_175733_a(this.entity.func_110124_au()) != null) {
               throw new CustomNPCsException("Entity is already spawned", new Object[0]);
          } else {
               this.entity.field_70128_L = false;
               this.worldWrapper.getMCWorld().func_72838_d(this.entity);
          }
     }

     public void kill() {
          this.entity.func_70106_y();
     }

     public boolean inWater() {
          return this.entity.func_70055_a(Material.field_151586_h);
     }

     public boolean inLava() {
          return this.entity.func_70055_a(Material.field_151587_i);
     }

     public boolean inFire() {
          return this.entity.func_70055_a(Material.field_151581_o);
     }

     public boolean isBurning() {
          return this.entity.func_70027_ad();
     }

     public void setBurning(int ticks) {
          this.entity.func_70015_d(ticks);
     }

     public void extinguish() {
          this.entity.func_70066_B();
     }

     public String getTypeName() {
          return EntityList.func_75621_b(this.entity);
     }

     public IEntityItem dropItem(IItemStack item) {
          return (IEntityItem)NpcAPI.Instance().getIEntity(this.entity.func_70099_a(item.getMCItemStack(), 0.0F));
     }

     public IEntity[] getRiders() {
          List list = this.entity.func_184188_bt();
          IEntity[] riders = new IEntity[list.size()];

          for(int i = 0; i < list.size(); ++i) {
               riders[i] = NpcAPI.Instance().getIEntity((Entity)list.get(i));
          }

          return riders;
     }

     public IRayTrace rayTraceBlock(double distance, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox) {
          Vec3d vec3d = this.entity.func_174824_e(1.0F);
          Vec3d vec3d1 = this.entity.func_70676_i(1.0F);
          Vec3d vec3d2 = vec3d.func_72441_c(vec3d1.field_72450_a * distance, vec3d1.field_72448_b * distance, vec3d1.field_72449_c * distance);
          RayTraceResult result = this.entity.world.func_147447_a(vec3d, vec3d2, stopOnLiquid, ignoreBlockWithoutBoundingBox, true);
          return result == null ? null : new RayTraceWrapper(NpcAPI.Instance().getIBlock(this.entity.world, result.func_178782_a()), result.field_178784_b.func_176745_a());
     }

     public IEntity[] rayTraceEntities(double distance, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox) {
          Vec3d vec3d = this.entity.func_174824_e(1.0F);
          Vec3d vec3d1 = this.entity.func_70676_i(1.0F);
          Vec3d vec3d2 = vec3d.func_72441_c(vec3d1.field_72450_a * distance, vec3d1.field_72448_b * distance, vec3d1.field_72449_c * distance);
          RayTraceResult result = this.entity.world.func_147447_a(vec3d, vec3d2, stopOnLiquid, ignoreBlockWithoutBoundingBox, false);
          if (result != null) {
               vec3d2 = new Vec3d(result.field_72307_f.field_72450_a, result.field_72307_f.field_72448_b, result.field_72307_f.field_72449_c);
          }

          return this.findEntityOnPath(distance, vec3d, vec3d2);
     }

     private IEntity[] findEntityOnPath(double distance, Vec3d vec3d, Vec3d vec3d1) {
          List list = this.entity.world.func_72839_b(this.entity, this.entity.getEntityBoundingBox().func_186662_g(distance));
          List result = new ArrayList();
          Iterator var7 = list.iterator();

          while(var7.hasNext()) {
               Entity entity1 = (Entity)var7.next();
               if (entity1.func_70067_L() && entity1 != this.entity) {
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().func_186662_g((double)entity1.func_70111_Y());
                    RayTraceResult raytraceresult1 = axisalignedbb.func_72327_a(vec3d, vec3d1);
                    if (raytraceresult1 != null) {
                         result.add(NpcAPI.Instance().getIEntity(entity1));
                    }
               }
          }

          result.sort((o1, o2) -> {
               double d1 = this.entity.func_70068_e(o1.getMCEntity());
               double d2 = this.entity.func_70068_e(o2.getMCEntity());
               if (d1 == d2) {
                    return 0;
               } else {
                    return d1 > d2 ? 1 : -1;
               }
          });
          return (IEntity[])result.toArray(new IEntity[result.size()]);
     }

     public IEntity[] getAllRiders() {
          List list = new ArrayList(this.entity.func_184182_bu());
          IEntity[] riders = new IEntity[list.size()];

          for(int i = 0; i < list.size(); ++i) {
               riders[i] = NpcAPI.Instance().getIEntity((Entity)list.get(i));
          }

          return riders;
     }

     public void addRider(IEntity entity) {
          if (entity != null) {
               entity.getMCEntity().func_184205_a(this.entity, true);
          }

     }

     public void clearRiders() {
          this.entity.func_184226_ay();
     }

     public IEntity getMount() {
          return NpcAPI.Instance().getIEntity(this.entity.func_184187_bx());
     }

     public void setMount(IEntity entity) {
          if (entity == null) {
               this.entity.func_184210_p();
          } else {
               this.entity.func_184205_a(entity.getMCEntity(), true);
          }

     }

     public void setRotation(float rotation) {
          this.entity.field_70177_z = rotation;
     }

     public float getRotation() {
          return this.entity.field_70177_z;
     }

     public void setPitch(float rotation) {
          this.entity.field_70125_A = rotation;
     }

     public float getPitch() {
          return this.entity.field_70125_A;
     }

     public void knockback(int power, float direction) {
          float v = direction * 3.1415927F / 180.0F;
          this.entity.func_70024_g((double)(-MathHelper.func_76126_a(v) * (float)power), 0.1D + (double)((float)power * 0.04F), (double)(MathHelper.func_76134_b(v) * (float)power));
          Entity var10000 = this.entity;
          var10000.motionX *= 0.6D;
          var10000 = this.entity;
          var10000.motionZ *= 0.6D;
          this.entity.field_70133_I = true;
     }

     public boolean isSneaking() {
          return this.entity.func_70093_af();
     }

     public boolean isSprinting() {
          return this.entity.func_70051_ag();
     }

     public Entity getMCEntity() {
          return this.entity;
     }

     public int getType() {
          return 0;
     }

     public boolean typeOf(int type) {
          return type == this.getType();
     }

     public String getUUID() {
          return this.entity.func_110124_au().toString();
     }

     public String generateNewUUID() {
          UUID id = UUID.randomUUID();
          this.entity.func_184221_a(id);
          return id.toString();
     }

     public INbt getNbt() {
          return NpcAPI.Instance().getINbt(this.entity.getEntityData());
     }

     public void storeAsClone(int tab, String name) {
          NBTTagCompound compound = new NBTTagCompound();
          if (!this.entity.func_184198_c(compound)) {
               throw new CustomNPCsException("Cannot store dead entities", new Object[0]);
          } else {
               ServerCloneController.Instance.addClone(compound, name, tab);
          }
     }

     public INbt getEntityNbt() {
          NBTTagCompound compound = new NBTTagCompound();
          this.entity.func_189511_e(compound);
          ResourceLocation resourcelocation = EntityList.func_191301_a(this.entity);
          if (this.getType() == 1) {
               resourcelocation = new ResourceLocation("player");
          }

          if (resourcelocation != null) {
               compound.setString("id", resourcelocation.toString());
          }

          return NpcAPI.Instance().getINbt(compound);
     }

     public void setEntityNbt(INbt nbt) {
          this.entity.func_70020_e(nbt.getMCNBT());
     }

     public void playAnimation(int type) {
          this.worldWrapper.getMCWorld().func_73039_n().func_151248_b(this.entity, new SPacketAnimation(this.entity, type));
     }

     public float getHeight() {
          return this.entity.height;
     }

     public float getEyeHeight() {
          return this.entity.func_70047_e();
     }

     public float getWidth() {
          return this.entity.field_70130_N;
     }

     public IPos getPos() {
          return new BlockPosWrapper(this.entity.func_180425_c());
     }

     public void setPos(IPos pos) {
          this.entity.func_70107_b((double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F));
     }

     public String[] getTags() {
          return (String[])this.entity.func_184216_O().toArray(new String[this.entity.func_184216_O().size()]);
     }

     public void addTag(String tag) {
          this.entity.func_184211_a(tag);
     }

     public boolean hasTag(String tag) {
          return this.entity.func_184216_O().contains(tag);
     }

     public void removeTag(String tag) {
          this.entity.func_184197_b(tag);
     }

     public double getMotionX() {
          return this.entity.motionX;
     }

     public double getMotionY() {
          return this.entity.motionY;
     }

     public double getMotionZ() {
          return this.entity.motionZ;
     }

     public void setMotionX(double motion) {
          if (this.entity.motionX != motion) {
               this.entity.motionX = motion;
               this.entity.field_70133_I = true;
          }
     }

     public void setMotionY(double motion) {
          if (this.entity.motionY != motion) {
               this.entity.motionY = motion;
               this.entity.field_70133_I = true;
          }
     }

     public void setMotionZ(double motion) {
          if (this.entity.motionZ != motion) {
               this.entity.motionZ = motion;
               this.entity.field_70133_I = true;
          }
     }
}
