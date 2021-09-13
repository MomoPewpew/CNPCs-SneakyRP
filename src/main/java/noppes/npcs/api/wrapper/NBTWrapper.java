package noppes.npcs.api.wrapper;

import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.INbt;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.util.NBTJsonUtil;

public class NBTWrapper implements INbt {
     private NBTTagCompound compound;

     public NBTWrapper(NBTTagCompound compound) {
          this.compound = compound;
     }

     public void remove(String key) {
          this.compound.func_82580_o(key);
     }

     public boolean has(String key) {
          return this.compound.func_74764_b(key);
     }

     public boolean getBoolean(String key) {
          return this.compound.func_74767_n(key);
     }

     public void setBoolean(String key, boolean value) {
          this.compound.func_74757_a(key, value);
     }

     public short getShort(String key) {
          return this.compound.func_74765_d(key);
     }

     public void setShort(String key, short value) {
          this.compound.func_74777_a(key, value);
     }

     public int getInteger(String key) {
          return this.compound.func_74762_e(key);
     }

     public void setInteger(String key, int value) {
          this.compound.func_74768_a(key, value);
     }

     public byte getByte(String key) {
          return this.compound.func_74771_c(key);
     }

     public void setByte(String key, byte value) {
          this.compound.func_74774_a(key, value);
     }

     public long getLong(String key) {
          return this.compound.func_74763_f(key);
     }

     public void setLong(String key, long value) {
          this.compound.func_74772_a(key, value);
     }

     public double getDouble(String key) {
          return this.compound.func_74769_h(key);
     }

     public void setDouble(String key, double value) {
          this.compound.func_74780_a(key, value);
     }

     public float getFloat(String key) {
          return this.compound.func_74760_g(key);
     }

     public void setFloat(String key, float value) {
          this.compound.func_74776_a(key, value);
     }

     public String getString(String key) {
          return this.compound.func_74779_i(key);
     }

     public void setString(String key, String value) {
          this.compound.func_74778_a(key, value);
     }

     public byte[] getByteArray(String key) {
          return this.compound.func_74770_j(key);
     }

     public void setByteArray(String key, byte[] value) {
          this.compound.func_74773_a(key, value);
     }

     public int[] getIntegerArray(String key) {
          return this.compound.func_74759_k(key);
     }

     public void setIntegerArray(String key, int[] value) {
          this.compound.func_74783_a(key, value);
     }

     public Object[] getList(String key, int type) {
          NBTTagList list = this.compound.func_150295_c(key, type);
          Object[] nbts = new Object[list.func_74745_c()];

          for(int i = 0; i < list.func_74745_c(); ++i) {
               if (list.func_150303_d() == 10) {
                    nbts[i] = NpcAPI.Instance().getINbt(list.func_150305_b(i));
               } else if (list.func_150303_d() == 8) {
                    nbts[i] = list.func_150307_f(i);
               } else if (list.func_150303_d() == 6) {
                    nbts[i] = list.func_150309_d(i);
               } else if (list.func_150303_d() == 5) {
                    nbts[i] = list.func_150308_e(i);
               } else if (list.func_150303_d() == 3) {
                    nbts[i] = list.func_186858_c(i);
               } else if (list.func_150303_d() == 11) {
                    nbts[i] = list.func_150306_c(i);
               }
          }

          return nbts;
     }

     public int getListType(String key) {
          NBTBase b = this.compound.func_74781_a(key);
          if (b == null) {
               return 0;
          } else if (b.func_74732_a() != 9) {
               throw new CustomNPCsException("NBT tag " + key + " isn't a list", new Object[0]);
          } else {
               return ((NBTTagList)b).func_150303_d();
          }
     }

     public void setList(String key, Object[] value) {
          NBTTagList list = new NBTTagList();
          Object[] var4 = value;
          int var5 = value.length;

          for(int var6 = 0; var6 < var5; ++var6) {
               Object nbt = var4[var6];
               if (nbt instanceof INbt) {
                    list.func_74742_a(((INbt)nbt).getMCNBT());
               } else if (nbt instanceof String) {
                    list.func_74742_a(new NBTTagString((String)nbt));
               } else if (nbt instanceof Double) {
                    list.func_74742_a(new NBTTagDouble((Double)nbt));
               } else if (nbt instanceof Float) {
                    list.func_74742_a(new NBTTagFloat((Float)nbt));
               } else if (nbt instanceof Integer) {
                    list.func_74742_a(new NBTTagInt((Integer)nbt));
               } else if (nbt instanceof int[]) {
                    list.func_74742_a(new NBTTagIntArray((int[])((int[])nbt)));
               }
          }

          this.compound.func_74782_a(key, list);
     }

     public INbt getCompound(String key) {
          return NpcAPI.Instance().getINbt(this.compound.func_74775_l(key));
     }

     public void setCompound(String key, INbt value) {
          if (value == null) {
               throw new CustomNPCsException("Value cant be null", new Object[0]);
          } else {
               this.compound.func_74782_a(key, value.getMCNBT());
          }
     }

     public String[] getKeys() {
          return (String[])this.compound.func_150296_c().toArray(new String[this.compound.func_150296_c().size()]);
     }

     public int getType(String key) {
          return this.compound.func_150299_b(key);
     }

     public NBTTagCompound getMCNBT() {
          return this.compound;
     }

     public String toJsonString() {
          return NBTJsonUtil.Convert(this.compound);
     }

     public boolean isEqual(INbt nbt) {
          return nbt == null ? false : this.compound.equals(nbt.getMCNBT());
     }

     public void clear() {
          Iterator var1 = this.compound.func_150296_c().iterator();

          while(var1.hasNext()) {
               String name = (String)var1.next();
               this.compound.func_82580_o(name);
          }

     }

     public void merge(INbt nbt) {
          this.compound.func_179237_a(nbt.getMCNBT());
     }
}
