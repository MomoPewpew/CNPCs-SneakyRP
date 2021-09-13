package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.event.NpcEvent;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataInventory implements IInventory, INPCInventory {
     public Map drops = new HashMap();
     public Map dropchance = new HashMap();
     public Map weapons = new HashMap();
     public Map armor = new HashMap();
     private int minExp = 0;
     private int maxExp = 0;
     public int lootMode = 0;
     private EntityNPCInterface npc;

     public DataInventory(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public NBTTagCompound writeEntityToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.setInteger("MinExp", this.minExp);
          nbttagcompound.setInteger("MaxExp", this.maxExp);
          nbttagcompound.setTag("NpcInv", NBTTags.nbtIItemStackMap(this.drops));
          nbttagcompound.setTag("Armor", NBTTags.nbtIItemStackMap(this.armor));
          nbttagcompound.setTag("Weapons", NBTTags.nbtIItemStackMap(this.weapons));
          nbttagcompound.setTag("DropChance", NBTTags.nbtIntegerIntegerMap(this.dropchance));
          nbttagcompound.setInteger("LootMode", this.lootMode);
          return nbttagcompound;
     }

     public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
          this.minExp = nbttagcompound.getInteger("MinExp");
          this.maxExp = nbttagcompound.getInteger("MaxExp");
          this.drops = NBTTags.getIItemStackMap(nbttagcompound.getTagList("NpcInv", 10));
          this.armor = NBTTags.getIItemStackMap(nbttagcompound.getTagList("Armor", 10));
          this.weapons = NBTTags.getIItemStackMap(nbttagcompound.getTagList("Weapons", 10));
          this.dropchance = NBTTags.getIntegerIntegerMap(nbttagcompound.getTagList("DropChance", 10));
          this.lootMode = nbttagcompound.getInteger("LootMode");
     }

     public IItemStack getArmor(int slot) {
          return (IItemStack)this.armor.get(slot);
     }

     public void setArmor(int slot, IItemStack item) {
          this.armor.put(slot, item);
          this.npc.updateClient = true;
     }

     public IItemStack getRightHand() {
          return (IItemStack)this.weapons.get(0);
     }

     public void setRightHand(IItemStack item) {
          this.weapons.put(0, item);
          this.npc.updateClient = true;
     }

     public IItemStack getProjectile() {
          return (IItemStack)this.weapons.get(1);
     }

     public void setProjectile(IItemStack item) {
          this.weapons.put(1, item);
          this.npc.updateAI = true;
     }

     public IItemStack getLeftHand() {
          return (IItemStack)this.weapons.get(2);
     }

     public void setLeftHand(IItemStack item) {
          this.weapons.put(2, item);
          this.npc.updateClient = true;
     }

     public IItemStack getDropItem(int slot) {
          if (slot >= 0 && slot <= 8) {
               IItemStack item = (IItemStack)this.npc.inventory.drops.get(slot);
               return item == null ? null : NpcAPI.Instance().getIItemStack(item.getMCItemStack());
          } else {
               throw new CustomNPCsException("Bad slot number: " + slot, new Object[0]);
          }
     }

     public void setDropItem(int slot, IItemStack item, int chance) {
          if (slot >= 0 && slot <= 8) {
               chance = ValueUtil.CorrectInt(chance, 1, 100);
               if (item != null && !item.isEmpty()) {
                    this.dropchance.put(slot, chance);
                    this.drops.put(slot, item);
               } else {
                    this.dropchance.remove(slot);
                    this.drops.remove(slot);
               }

          } else {
               throw new CustomNPCsException("Bad slot number: " + slot, new Object[0]);
          }
     }

     public IItemStack[] getItemsRNG() {
          ArrayList list = new ArrayList();
          Iterator var2 = this.drops.keySet().iterator();

          while(var2.hasNext()) {
               int i = (Integer)var2.next();
               IItemStack item = (IItemStack)this.drops.get(i);
               if (item != null && !item.isEmpty()) {
                    int dchance = 100;
                    if (this.dropchance.containsKey(i)) {
                         dchance = (Integer)this.dropchance.get(i);
                    }

                    int chance = this.npc.world.rand.nextInt(100) + dchance;
                    if (chance >= 100) {
                         list.add(item);
                    }
               }
          }

          return (IItemStack[])list.toArray(new IItemStack[list.size()]);
     }

     public void dropStuff(NpcEvent.DiedEvent event, Entity entity, DamageSource damagesource) {
          ArrayList list = new ArrayList();
          int exp;
          int var2;
          if (event.droppedItems != null) {
               IItemStack[] var5 = event.droppedItems;
               exp = var5.length;

               for(var2 = 0; var2 < exp; ++var2) {
                    IItemStack item = var5[var2];
                    EntityItem e = this.getEntityItem(item.getMCItemStack().copy());
                    if (e != null) {
                         list.add(e);
                    }
               }
          }

          int enchant = 0;
          if (damagesource.getTrueSource() instanceof EntityPlayer) {
               enchant = EnchantmentHelper.func_185283_h((EntityLivingBase)damagesource.getTrueSource());
          }

          if (!ForgeHooks.onLivingDrops(this.npc, damagesource, list, enchant, true)) {
               Iterator var12 = list.iterator();

               label55:
               while(true) {
                    while(true) {
                         if (!var12.hasNext()) {
                              break label55;
                         }

                         EntityItem item = (EntityItem)var12.next();
                         if (this.lootMode == 1 && entity instanceof EntityPlayer) {
                              EntityPlayer player = (EntityPlayer)entity;
                              item.setPickupDelay(2);
                              this.npc.world.spawnEntity(item);
                              ItemStack stack = item.getItem();
                              int i = stack.getCount();
                              if (player.inventory.addItemStackToInventory(stack)) {
                                   entity.world.playSound((EntityPlayer)null, player.field_70165_t, player.field_70163_u, player.field_70161_v, SoundEvents.field_187638_cR, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                   player.onItemPickup(item, i);
                                   if (stack.getCount() <= 0) {
                                        item.setDead();
                                   }
                              }
                         } else {
                              this.npc.world.spawnEntity(item);
                         }
                    }
               }
          }

          exp = event.expDropped;

          while(true) {
               while(exp > 0) {
                    var2 = EntityXPOrb.func_70527_a(exp);
                    exp -= var2;
                    if (this.lootMode == 1 && entity instanceof EntityPlayer) {
                         this.npc.world.spawnEntity(new EntityXPOrb(entity.world, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, var2));
                    } else {
                         this.npc.world.spawnEntity(new EntityXPOrb(this.npc.world, this.npc.field_70165_t, this.npc.field_70163_u, this.npc.field_70161_v, var2));
                    }
               }

               return;
          }
     }

     public EntityItem getEntityItem(ItemStack itemstack) {
          if (itemstack != null && !itemstack.isEmpty()) {
               EntityItem entityitem = new EntityItem(this.npc.world, this.npc.field_70165_t, this.npc.field_70163_u - 0.30000001192092896D + (double)this.npc.func_70047_e(), this.npc.field_70161_v, itemstack);
               entityitem.setPickupDelay(40);
               float f2 = this.npc.getRNG().nextFloat() * 0.5F;
               float f4 = this.npc.getRNG().nextFloat() * 3.141593F * 2.0F;
               entityitem.motionX = (double)(-MathHelper.func_76126_a(f4) * f2);
               entityitem.motionZ = (double)(MathHelper.func_76134_b(f4) * f2);
               entityitem.motionY = 0.20000000298023224D;
               return entityitem;
          } else {
               return null;
          }
     }

     public int getSizeInventory() {
          return 15;
     }

     public ItemStack getStackInSlot(int i) {
          if (i < 4) {
               return ItemStackWrapper.MCItem(this.getArmor(i));
          } else {
               return i < 7 ? ItemStackWrapper.MCItem((IItemStack)this.weapons.get(i - 4)) : ItemStackWrapper.MCItem((IItemStack)this.drops.get(i - 7));
          }
     }

     public ItemStack decrStackSize(int par1, int par2) {
          int i = 0;
          Map var3;
          if (par1 >= 7) {
               var3 = this.drops;
               par1 -= 7;
          } else if (par1 >= 4) {
               var3 = this.weapons;
               par1 -= 4;
               i = 1;
          } else {
               var3 = this.armor;
               i = 2;
          }

          ItemStack var4 = null;
          if (var3.get(par1) != null) {
               if (((IItemStack)var3.get(par1)).getMCItemStack().getCount() <= par2) {
                    var4 = ((IItemStack)var3.get(par1)).getMCItemStack();
                    var3.put(par1, (Object)null);
               } else {
                    var4 = ((IItemStack)var3.get(par1)).getMCItemStack().splitStack(par2);
                    if (((IItemStack)var3.get(par1)).getMCItemStack().getCount() == 0) {
                         var3.put(par1, (Object)null);
                    }
               }
          }

          if (i == 1) {
               this.weapons = var3;
          }

          if (i == 2) {
               this.armor = var3;
          }

          return var4 == null ? ItemStack.EMPTY : var4;
     }

     public ItemStack removeStackFromSlot(int par1) {
          int i = 0;
          Map var2;
          if (par1 >= 7) {
               var2 = this.drops;
               par1 -= 7;
          } else if (par1 >= 4) {
               var2 = this.weapons;
               par1 -= 4;
               i = 1;
          } else {
               var2 = this.armor;
               i = 2;
          }

          if (var2.get(par1) != null) {
               ItemStack var3 = ((IItemStack)var2.get(par1)).getMCItemStack();
               var2.put(par1, (Object)null);
               if (i == 1) {
                    this.weapons = var2;
               }

               if (i == 2) {
                    this.armor = var2;
               }

               return var3;
          } else {
               return ItemStack.EMPTY;
          }
     }

     public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
          int i = 0;
          Map var3;
          if (par1 >= 7) {
               var3 = this.drops;
               par1 -= 7;
          } else if (par1 >= 4) {
               var3 = this.weapons;
               par1 -= 4;
               i = 1;
          } else {
               var3 = this.armor;
               i = 2;
          }

          var3.put(par1, NpcAPI.Instance().getIItemStack(par2ItemStack));
          if (i == 1) {
               this.weapons = var3;
          }

          if (i == 2) {
               this.armor = var3;
          }

     }

     public int getInventoryStackLimit() {
          return 64;
     }

     public boolean isUsableByPlayer(EntityPlayer var1) {
          return true;
     }

     public boolean isItemValidForSlot(int i, ItemStack itemstack) {
          return true;
     }

     public String getName() {
          return "NPC Inventory";
     }

     public void markDirty() {
     }

     public boolean hasCustomName() {
          return true;
     }

     public ITextComponent getDisplayName() {
          return null;
     }

     public void openInventory(EntityPlayer player) {
     }

     public void closeInventory(EntityPlayer player) {
     }

     public int getField(int id) {
          return 0;
     }

     public void setField(int id, int value) {
     }

     public int getFieldCount() {
          return 0;
     }

     public void clear() {
     }

     public int getExpMin() {
          return this.npc.inventory.minExp;
     }

     public int getExpMax() {
          return this.npc.inventory.maxExp;
     }

     public int getExpRNG() {
          int exp = this.minExp;
          if (this.maxExp - this.minExp > 0) {
               exp += this.npc.world.rand.nextInt(this.maxExp - this.minExp);
          }

          return exp;
     }

     public void setExp(int min, int max) {
          min = Math.min(min, max);
          this.npc.inventory.minExp = min;
          this.npc.inventory.maxExp = max;
     }

     public boolean isEmpty() {
          for(int slot = 0; slot < this.getSizeInventory(); ++slot) {
               ItemStack item = this.getStackInSlot(slot);
               if (!NoppesUtilServer.IsItemStackNull(item) && !item.isEmpty()) {
                    return false;
               }
          }

          return true;
     }
}
