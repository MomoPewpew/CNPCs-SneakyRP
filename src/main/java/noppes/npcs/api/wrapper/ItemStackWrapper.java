package noppes.npcs.api.wrapper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import noppes.npcs.ItemStackEmptyWrapper;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.INbt;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemScripted;

public class ItemStackWrapper implements IItemStack, ICapabilityProvider, ICapabilitySerializable {
     private Map tempData = new HashMap();
     @CapabilityInject(ItemStackWrapper.class)
     public static Capability ITEMSCRIPTEDDATA_CAPABILITY = null;
     private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS;
     public ItemStack item;
     private NBTTagCompound storedData = new NBTTagCompound();
     public static ItemStackWrapper AIR;
     private final IData tempdata = new IData() {
          public void put(String key, Object value) {
               ItemStackWrapper.this.tempData.put(key, value);
          }

          public Object get(String key) {
               return ItemStackWrapper.this.tempData.get(key);
          }

          public void remove(String key) {
               ItemStackWrapper.this.tempData.remove(key);
          }

          public boolean has(String key) {
               return ItemStackWrapper.this.tempData.containsKey(key);
          }

          public void clear() {
               ItemStackWrapper.this.tempData.clear();
          }

          public String[] getKeys() {
               return (String[])ItemStackWrapper.this.tempData.keySet().toArray(new String[ItemStackWrapper.this.tempData.size()]);
          }
     };
     private final IData storeddata = new IData() {
          public void put(String key, Object value) {
               if (value instanceof Number) {
                    ItemStackWrapper.this.storedData.func_74780_a(key, ((Number)value).doubleValue());
               } else if (value instanceof String) {
                    ItemStackWrapper.this.storedData.func_74778_a(key, (String)value);
               }

          }

          public Object get(String key) {
               if (!ItemStackWrapper.this.storedData.func_74764_b(key)) {
                    return null;
               } else {
                    NBTBase base = ItemStackWrapper.this.storedData.func_74781_a(key);
                    return base instanceof NBTPrimitive ? ((NBTPrimitive)base).func_150286_g() : ((NBTTagString)base).func_150285_a_();
               }
          }

          public void remove(String key) {
               ItemStackWrapper.this.storedData.func_82580_o(key);
          }

          public boolean has(String key) {
               return ItemStackWrapper.this.storedData.func_74764_b(key);
          }

          public void clear() {
               ItemStackWrapper.this.storedData = new NBTTagCompound();
          }

          public String[] getKeys() {
               return (String[])ItemStackWrapper.this.storedData.func_150296_c().toArray(new String[ItemStackWrapper.this.storedData.func_150296_c().size()]);
          }
     };
     private static final ResourceLocation key;

     protected ItemStackWrapper(ItemStack item) {
          this.item = item;
     }

     public IData getTempdata() {
          return this.tempdata;
     }

     public IData getStoreddata() {
          return this.storeddata;
     }

     public int getStackSize() {
          return this.item.func_190916_E();
     }

     public void setStackSize(int size) {
          if (size > this.getMaxStackSize()) {
               throw new CustomNPCsException("Can't set the stacksize bigger than MaxStacksize", new Object[0]);
          } else {
               this.item.func_190920_e(size);
          }
     }

     public void setAttribute(String name, double value) {
          this.setAttribute(name, value, -1);
     }

     public void setAttribute(String name, double value, int slot) {
          if (slot >= -1 && slot <= 5) {
               NBTTagCompound compound = this.item.func_77978_p();
               if (compound == null) {
                    this.item.func_77982_d(compound = new NBTTagCompound());
               }

               NBTTagList nbttaglist = compound.func_150295_c("AttributeModifiers", 10);
               NBTTagList newList = new NBTTagList();

               for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    NBTTagCompound c = nbttaglist.func_150305_b(i);
                    if (!c.func_74779_i("AttributeName").equals(name)) {
                         newList.func_74742_a(c);
                    }
               }

               if (value != 0.0D) {
                    NBTTagCompound nbttagcompound = SharedMonsterAttributes.func_111262_a(new AttributeModifier(name, value, 0));
                    nbttagcompound.func_74778_a("AttributeName", name);
                    if (slot >= 0) {
                         nbttagcompound.func_74778_a("Slot", EntityEquipmentSlot.values()[slot].func_188450_d());
                    }

                    newList.func_74742_a(nbttagcompound);
               }

               compound.func_74782_a("AttributeModifiers", newList);
          } else {
               throw new CustomNPCsException("Slot has to be between -1 and 5, given was: " + slot, new Object[0]);
          }
     }

     public double getAttribute(String name) {
          NBTTagCompound compound = this.item.func_77978_p();
          if (compound == null) {
               return 0.0D;
          } else {
               Multimap map = this.item.func_111283_C(EntityEquipmentSlot.MAINHAND);
               Iterator var4 = map.entries().iterator();

               Entry entry;
               do {
                    if (!var4.hasNext()) {
                         return 0.0D;
                    }

                    entry = (Entry)var4.next();
               } while(!((String)entry.getKey()).equals(name));

               AttributeModifier mod = (AttributeModifier)entry.getValue();
               return mod.func_111164_d();
          }
     }

     public boolean hasAttribute(String name) {
          NBTTagCompound compound = this.item.func_77978_p();
          if (compound == null) {
               return false;
          } else {
               NBTTagList nbttaglist = compound.func_150295_c("AttributeModifiers", 10);

               for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    NBTTagCompound c = nbttaglist.func_150305_b(i);
                    if (c.func_74779_i("AttributeName").equals(name)) {
                         return true;
                    }
               }

               return false;
          }
     }

     public int getItemDamage() {
          return this.item.func_77952_i();
     }

     public void setItemDamage(int value) {
          this.item.func_77964_b(value);
     }

     public void addEnchantment(String id, int strenght) {
          Enchantment ench = Enchantment.func_180305_b(id);
          if (ench == null) {
               throw new CustomNPCsException("Unknown enchant id:" + id, new Object[0]);
          } else {
               this.item.func_77966_a(ench, strenght);
          }
     }

     public boolean isEnchanted() {
          return this.item.func_77948_v();
     }

     public boolean hasEnchant(String id) {
          Enchantment ench = Enchantment.func_180305_b(id);
          if (ench == null) {
               throw new CustomNPCsException("Unknown enchant id:" + id, new Object[0]);
          } else if (!this.isEnchanted()) {
               return false;
          } else {
               int enchId = Enchantment.func_185258_b(ench);
               NBTTagList list = this.item.func_77986_q();

               for(int i = 0; i < list.func_74745_c(); ++i) {
                    NBTTagCompound compound = list.func_150305_b(i);
                    if (compound.func_74765_d("id") == enchId) {
                         return true;
                    }
               }

               return false;
          }
     }

     public boolean removeEnchant(String id) {
          Enchantment ench = Enchantment.func_180305_b(id);
          if (ench == null) {
               throw new CustomNPCsException("Unknown enchant id:" + id, new Object[0]);
          } else if (!this.isEnchanted()) {
               return false;
          } else {
               int enchId = Enchantment.func_185258_b(ench);
               NBTTagList list = this.item.func_77986_q();
               NBTTagList newList = new NBTTagList();

               for(int i = 0; i < list.func_74745_c(); ++i) {
                    NBTTagCompound compound = list.func_150305_b(i);
                    if (compound.func_74765_d("id") != enchId) {
                         newList.func_74742_a(compound);
                    }
               }

               if (list.func_74745_c() == newList.func_74745_c()) {
                    return false;
               } else {
                    this.item.func_77978_p().func_74782_a("ench", newList);
                    return true;
               }
          }
     }

     public boolean isBlock() {
          Block block = Block.func_149634_a(this.item.func_77973_b());
          return block != null && block != Blocks.field_150350_a;
     }

     public boolean hasCustomName() {
          return this.item.func_82837_s();
     }

     public void setCustomName(String name) {
          this.item.func_151001_c(name);
     }

     public String getDisplayName() {
          return this.item.func_82833_r();
     }

     public String getItemName() {
          return this.item.func_77973_b().func_77653_i(this.item);
     }

     public String getName() {
          return Item.field_150901_e.func_177774_c(this.item.func_77973_b()) + "";
     }

     public INbt getNbt() {
          NBTTagCompound compound = this.item.func_77978_p();
          if (compound == null) {
               this.item.func_77982_d(compound = new NBTTagCompound());
          }

          return NpcAPI.Instance().getINbt(compound);
     }

     public boolean hasNbt() {
          NBTTagCompound compound = this.item.func_77978_p();
          return compound != null && !compound.func_82582_d();
     }

     public ItemStack getMCItemStack() {
          return this.item;
     }

     public static ItemStack MCItem(IItemStack item) {
          return item == null ? ItemStack.field_190927_a : item.getMCItemStack();
     }

     public void damageItem(int damage, IEntityLiving living) {
          this.item.func_77972_a(damage, living == null ? null : living.getMCEntity());
     }

     public boolean isBook() {
          return false;
     }

     public int getFoodLevel() {
          return this.item.func_77973_b() instanceof ItemFood ? ((ItemFood)this.item.func_77973_b()).func_150905_g(this.item) : 0;
     }

     public IItemStack copy() {
          return createNew(this.item.func_77946_l());
     }

     public int getMaxStackSize() {
          return this.item.func_77976_d();
     }

     public int getMaxItemDamage() {
          return this.item.func_77958_k();
     }

     public INbt getItemNbt() {
          NBTTagCompound compound = new NBTTagCompound();
          this.item.func_77955_b(compound);
          return NpcAPI.Instance().getINbt(compound);
     }

     public double getAttackDamage() {
          HashMultimap map = (HashMultimap)this.item.func_111283_C(EntityEquipmentSlot.MAINHAND);
          Iterator iterator = map.entries().iterator();
          double damage = 0.0D;

          while(iterator.hasNext()) {
               Entry entry = (Entry)iterator.next();
               if (entry.getKey().equals(SharedMonsterAttributes.field_111264_e.func_111108_a())) {
                    AttributeModifier mod = (AttributeModifier)entry.getValue();
                    damage = mod.func_111164_d();
               }
          }

          damage += (double)EnchantmentHelper.func_152377_a(this.item, EnumCreatureAttribute.UNDEFINED);
          return damage;
     }

     public boolean isEmpty() {
          return this.item.func_190926_b();
     }

     public int getType() {
          if (this.item.func_77973_b() instanceof IPlantable) {
               return 5;
          } else {
               return this.item.func_77973_b() instanceof ItemSword ? 4 : 0;
          }
     }

     public boolean isWearable() {
          EntityEquipmentSlot[] var1 = VALID_EQUIPMENT_SLOTS;
          int var2 = var1.length;

          for(int var3 = 0; var3 < var2; ++var3) {
               EntityEquipmentSlot slot = var1[var3];
               if (this.item.func_77973_b().isValidArmor(this.item, slot, EntityNPCInterface.CommandPlayer)) {
                    return true;
               }
          }

          return false;
     }

     public boolean hasCapability(Capability capability, EnumFacing facing) {
          return capability == ITEMSCRIPTEDDATA_CAPABILITY;
     }

     public Object getCapability(Capability capability, EnumFacing facing) {
          return this.hasCapability(capability, facing) ? this : null;
     }

     public static void register(AttachCapabilitiesEvent event) {
          ItemStackWrapper wrapper = createNew((ItemStack)event.getObject());
          event.addCapability(key, wrapper);
     }

     private static ItemStackWrapper createNew(ItemStack item) {
          if (item != null && !item.func_190926_b()) {
               if (item.func_77973_b() instanceof ItemScripted) {
                    return new ItemScriptedWrapper(item);
               } else if (item.func_77973_b() != Items.field_151164_bB && item.func_77973_b() != Items.field_151099_bA && !(item.func_77973_b() instanceof ItemWritableBook) && !(item.func_77973_b() instanceof ItemWrittenBook)) {
                    if (item.func_77973_b() instanceof ItemArmor) {
                         return new ItemArmorWrapper(item);
                    } else {
                         Block block = Block.func_149634_a(item.func_77973_b());
                         return (ItemStackWrapper)(block != Blocks.field_150350_a ? new ItemBlockWrapper(item) : new ItemStackWrapper(item));
                    }
               } else {
                    return new ItemBookWrapper(item);
               }
          } else {
               return AIR;
          }
     }

     public String[] getLore() {
          NBTTagCompound compound = this.item.func_179543_a("display");
          if (compound != null && compound.func_150299_b("Lore") == 9) {
               NBTTagList nbttaglist = compound.func_150295_c("Lore", 8);
               if (nbttaglist.func_82582_d()) {
                    return new String[0];
               } else {
                    List lore = new ArrayList();

                    for(int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                         lore.add(nbttaglist.func_150307_f(i));
                    }

                    return (String[])lore.toArray(new String[lore.size()]);
               }
          } else {
               return new String[0];
          }
     }

     public void setLore(String[] lore) {
          NBTTagCompound compound = this.item.func_190925_c("display");
          if (lore != null && lore.length != 0) {
               NBTTagList nbtlist = new NBTTagList();
               String[] var4 = lore;
               int var5 = lore.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                    String s = var4[var6];
                    nbtlist.func_74742_a(new NBTTagString(s));
               }

               compound.func_74782_a("Lore", nbtlist);
          } else {
               compound.func_82580_o("Lore");
          }
     }

     public NBTBase serializeNBT() {
          return this.getMCNbt();
     }

     public void deserializeNBT(NBTBase nbt) {
          this.setMCNbt((NBTTagCompound)nbt);
     }

     public NBTTagCompound getMCNbt() {
          NBTTagCompound compound = new NBTTagCompound();
          if (!this.storedData.func_82582_d()) {
               compound.func_74782_a("StoredData", this.storedData);
          }

          return compound;
     }

     public void setMCNbt(NBTTagCompound compound) {
          this.storedData = compound.func_74775_l("StoredData");
     }

     public void removeNbt() {
          this.item.func_77982_d((NBTTagCompound)null);
     }

     public boolean compare(IItemStack item, boolean ignoreNBT) {
          if (item == null) {
               item = AIR;
          }

          return NoppesUtilPlayer.compareItems(this.getMCItemStack(), ((IItemStack)item).getMCItemStack(), false, ignoreNBT);
     }

     static {
          VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
          AIR = new ItemStackEmptyWrapper();
          key = new ResourceLocation("customnpcs", "itemscripteddata");
     }
}
