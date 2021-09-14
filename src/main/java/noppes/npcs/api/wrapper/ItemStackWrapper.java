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
			return (String[]) ItemStackWrapper.this.tempData.keySet()
					.toArray(new String[ItemStackWrapper.this.tempData.size()]);
		}
	};
	private final IData storeddata = new IData() {
		public void put(String key, Object value) {
			if (value instanceof Number) {
				ItemStackWrapper.this.storedData.setDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof String) {
				ItemStackWrapper.this.storedData.setString(key, (String) value);
			}

		}

		public Object get(String key) {
			if (!ItemStackWrapper.this.storedData.hasKey(key)) {
				return null;
			} else {
				NBTBase base = ItemStackWrapper.this.storedData.getTag(key);
				return base instanceof NBTPrimitive ? ((NBTPrimitive) base).getDouble()
						: ((NBTTagString) base).getString();
			}
		}

		public void remove(String key) {
			ItemStackWrapper.this.storedData.removeTag(key);
		}

		public boolean has(String key) {
			return ItemStackWrapper.this.storedData.hasKey(key);
		}

		public void clear() {
			ItemStackWrapper.this.storedData = new NBTTagCompound();
		}

		public String[] getKeys() {
			return (String[]) ItemStackWrapper.this.storedData.getKeySet()
					.toArray(new String[ItemStackWrapper.this.storedData.getKeySet().size()]);
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
		return this.item.getCount();
	}

	public void setStackSize(int size) {
		if (size > this.getMaxStackSize()) {
			throw new CustomNPCsException("Can't set the stacksize bigger than MaxStacksize", new Object[0]);
		} else {
			this.item.setCount(size);
		}
	}

	public void setAttribute(String name, double value) {
		this.setAttribute(name, value, -1);
	}

	public void setAttribute(String name, double value, int slot) {
		if (slot >= -1 && slot <= 5) {
			NBTTagCompound compound = this.item.getTagCompound();
			if (compound == null) {
				this.item.setTagCompound(compound = new NBTTagCompound());
			}

			NBTTagList nbttaglist = compound.getTagList("AttributeModifiers", 10);
			NBTTagList newList = new NBTTagList();

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound c = nbttaglist.getCompoundTagAt(i);
				if (!c.getString("AttributeName").equals(name)) {
					newList.appendTag(c);
				}
			}

			if (value != 0.0D) {
				NBTTagCompound nbttagcompound = SharedMonsterAttributes
						.writeAttributeModifierToNBT(new AttributeModifier(name, value, 0));
				nbttagcompound.setString("AttributeName", name);
				if (slot >= 0) {
					nbttagcompound.setString("Slot", EntityEquipmentSlot.values()[slot].getName());
				}

				newList.appendTag(nbttagcompound);
			}

			compound.setTag("AttributeModifiers", newList);
		} else {
			throw new CustomNPCsException("Slot has to be between -1 and 5, given was: " + slot, new Object[0]);
		}
	}

	public double getAttribute(String name) {
		NBTTagCompound compound = this.item.getTagCompound();
		if (compound == null) {
			return 0.0D;
		} else {
			Multimap map = this.item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
			Iterator var4 = map.entries().iterator();

			Entry entry;
			do {
				if (!var4.hasNext()) {
					return 0.0D;
				}

				entry = (Entry) var4.next();
			} while (!((String) entry.getKey()).equals(name));

			AttributeModifier mod = (AttributeModifier) entry.getValue();
			return mod.getAmount();
		}
	}

	public boolean hasAttribute(String name) {
		NBTTagCompound compound = this.item.getTagCompound();
		if (compound == null) {
			return false;
		} else {
			NBTTagList nbttaglist = compound.getTagList("AttributeModifiers", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound c = nbttaglist.getCompoundTagAt(i);
				if (c.getString("AttributeName").equals(name)) {
					return true;
				}
			}

			return false;
		}
	}

	public int getItemDamage() {
		return this.item.getItemDamage();
	}

	public void setItemDamage(int value) {
		this.item.setItemDamage(value);
	}

	public void addEnchantment(String id, int strenght) {
		Enchantment ench = Enchantment.getEnchantmentByLocation(id);
		if (ench == null) {
			throw new CustomNPCsException("Unknown enchant id:" + id, new Object[0]);
		} else {
			this.item.addEnchantment(ench, strenght);
		}
	}

	public boolean isEnchanted() {
		return this.item.isItemEnchanted();
	}

	public boolean hasEnchant(String id) {
		Enchantment ench = Enchantment.getEnchantmentByLocation(id);
		if (ench == null) {
			throw new CustomNPCsException("Unknown enchant id:" + id, new Object[0]);
		} else if (!this.isEnchanted()) {
			return false;
		} else {
			int enchId = Enchantment.getEnchantmentID(ench);
			NBTTagList list = this.item.getEnchantmentTagList();

			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound compound = list.getCompoundTagAt(i);
				if (compound.getShort("id") == enchId) {
					return true;
				}
			}

			return false;
		}
	}

	public boolean removeEnchant(String id) {
		Enchantment ench = Enchantment.getEnchantmentByLocation(id);
		if (ench == null) {
			throw new CustomNPCsException("Unknown enchant id:" + id, new Object[0]);
		} else if (!this.isEnchanted()) {
			return false;
		} else {
			int enchId = Enchantment.getEnchantmentID(ench);
			NBTTagList list = this.item.getEnchantmentTagList();
			NBTTagList newList = new NBTTagList();

			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound compound = list.getCompoundTagAt(i);
				if (compound.getShort("id") != enchId) {
					newList.appendTag(compound);
				}
			}

			if (list.tagCount() == newList.tagCount()) {
				return false;
			} else {
				this.item.getTagCompound().setTag("ench", newList);
				return true;
			}
		}
	}

	public boolean isBlock() {
		Block block = Block.getBlockFromItem(this.item.getItem());
		return block != null && block != Blocks.AIR;
	}

	public boolean hasCustomName() {
		return this.item.hasDisplayName();
	}

	public void setCustomName(String name) {
		this.item.setStackDisplayName(name);
	}

	public String getDisplayName() {
		return this.item.getDisplayName();
	}

	public String getItemName() {
		return this.item.getItem().getItemStackDisplayName(this.item);
	}

	public String getName() {
		return Item.REGISTRY.getNameForObject(this.item.getItem()) + "";
	}

	public INbt getNbt() {
		NBTTagCompound compound = this.item.getTagCompound();
		if (compound == null) {
			this.item.setTagCompound(compound = new NBTTagCompound());
		}

		return NpcAPI.Instance().getINbt(compound);
	}

	public boolean hasNbt() {
		NBTTagCompound compound = this.item.getTagCompound();
		return compound != null && !compound.hasNoTags();
	}

	public ItemStack getMCItemStack() {
		return this.item;
	}

	public static ItemStack MCItem(IItemStack item) {
		return item == null ? ItemStack.EMPTY : item.getMCItemStack();
	}

	public void damageItem(int damage, IEntityLiving living) {
		this.item.damageItem(damage, living == null ? null : living.getMCEntity());
	}

	public boolean isBook() {
		return false;
	}

	public int getFoodLevel() {
		return this.item.getItem() instanceof ItemFood ? ((ItemFood) this.item.getItem()).getHealAmount(this.item) : 0;
	}

	public IItemStack copy() {
		return createNew(this.item.copy());
	}

	public int getMaxStackSize() {
		return this.item.getMaxStackSize();
	}

	public int getMaxItemDamage() {
		return this.item.getMaxDamage();
	}

	public INbt getItemNbt() {
		NBTTagCompound compound = new NBTTagCompound();
		this.item.writeToNBT(compound);
		return NpcAPI.Instance().getINbt(compound);
	}

	public double getAttackDamage() {
		HashMultimap map = (HashMultimap) this.item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
		Iterator iterator = map.entries().iterator();
		double damage = 0.0D;

		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			if (entry.getKey().equals(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
				AttributeModifier mod = (AttributeModifier) entry.getValue();
				damage = mod.getAmount();
			}
		}

		damage += (double) EnchantmentHelper.getModifierForCreature(this.item, EnumCreatureAttribute.UNDEFINED);
		return damage;
	}

	public boolean isEmpty() {
		return this.item.isEmpty();
	}

	public int getType() {
		if (this.item.getItem() instanceof IPlantable) {
			return 5;
		} else {
			return this.item.getItem() instanceof ItemSword ? 4 : 0;
		}
	}

	public boolean isWearable() {
		EntityEquipmentSlot[] var1 = VALID_EQUIPMENT_SLOTS;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			EntityEquipmentSlot slot = var1[var3];
			if (this.item.getItem().isValidArmor(this.item, slot, EntityNPCInterface.CommandPlayer)) {
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
		ItemStackWrapper wrapper = createNew((ItemStack) event.getObject());
		event.addCapability(key, wrapper);
	}

	private static ItemStackWrapper createNew(ItemStack item) {
		if (item != null && !item.isEmpty()) {
			if (item.getItem() instanceof ItemScripted) {
				return new ItemScriptedWrapper(item);
			} else if (item.getItem() != Items.WRITTEN_BOOK && item.getItem() != Items.WRITABLE_BOOK
					&& !(item.getItem() instanceof ItemWritableBook) && !(item.getItem() instanceof ItemWrittenBook)) {
				if (item.getItem() instanceof ItemArmor) {
					return new ItemArmorWrapper(item);
				} else {
					Block block = Block.getBlockFromItem(item.getItem());
					return (ItemStackWrapper) (block != Blocks.AIR ? new ItemBlockWrapper(item)
							: new ItemStackWrapper(item));
				}
			} else {
				return new ItemBookWrapper(item);
			}
		} else {
			return AIR;
		}
	}

	public String[] getLore() {
		NBTTagCompound compound = this.item.getSubCompound("display");
		if (compound != null && compound.getTagId("Lore") == 9) {
			NBTTagList nbttaglist = compound.getTagList("Lore", 8);
			if (nbttaglist.isEmpty()) {
				return new String[0];
			} else {
				List lore = new ArrayList();

				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					lore.add(nbttaglist.getStringTagAt(i));
				}

				return (String[]) lore.toArray(new String[lore.size()]);
			}
		} else {
			return new String[0];
		}
	}

	public void setLore(String[] lore) {
		NBTTagCompound compound = this.item.getOrCreateSubCompound("display");
		if (lore != null && lore.length != 0) {
			NBTTagList nbtlist = new NBTTagList();
			String[] var4 = lore;
			int var5 = lore.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				String s = var4[var6];
				nbtlist.appendTag(new NBTTagString(s));
			}

			compound.setTag("Lore", nbtlist);
		} else {
			compound.removeTag("Lore");
		}
	}

	public NBTBase serializeNBT() {
		return this.getMCNbt();
	}

	public void deserializeNBT(NBTBase nbt) {
		this.setMCNbt((NBTTagCompound) nbt);
	}

	public NBTTagCompound getMCNbt() {
		NBTTagCompound compound = new NBTTagCompound();
		if (!this.storedData.isEmpty()) {
			compound.setTag("StoredData", this.storedData);
		}

		return compound;
	}

	public void setMCNbt(NBTTagCompound compound) {
		this.storedData = compound.getCompoundTag("StoredData");
	}

	public void removeNbt() {
		this.item.setTagCompound((NBTTagCompound) null);
	}

	public boolean compare(IItemStack item, boolean ignoreNBT) {
		if (item == null) {
			item = AIR;
		}

		return NoppesUtilPlayer.compareItems(this.getMCItemStack(), ((IItemStack) item).getMCItemStack(), false,
				ignoreNBT);
	}

	static {
		VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
				EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
		AIR = new ItemStackEmptyWrapper();
		key = new ResourceLocation("customnpcs", "itemscripteddata");
	}
}
