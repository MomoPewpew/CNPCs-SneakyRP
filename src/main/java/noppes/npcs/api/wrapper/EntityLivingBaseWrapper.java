package noppes.npcs.api.wrapper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLivingBase;
import noppes.npcs.api.entity.data.IMark;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.data.MarkData;

public class EntityLivingBaseWrapper extends EntityWrapper implements IEntityLivingBase {
     public EntityLivingBaseWrapper(EntityLivingBase entity) {
          super(entity);
     }

     public float getHealth() {
          return ((EntityLivingBase)this.entity).func_110143_aJ();
     }

     public void setHealth(float health) {
          ((EntityLivingBase)this.entity).func_70606_j(health);
     }

     public float getMaxHealth() {
          return ((EntityLivingBase)this.entity).func_110138_aP();
     }

     public void setMaxHealth(float health) {
          if (health >= 0.0F) {
               ((EntityLivingBase)this.entity).func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)health);
          }
     }

     public boolean isAttacking() {
          return ((EntityLivingBase)this.entity).func_70643_av() != null;
     }

     public void setAttackTarget(IEntityLivingBase living) {
          if (living == null) {
               ((EntityLivingBase)this.entity).func_70604_c((EntityLivingBase)null);
          } else {
               ((EntityLivingBase)this.entity).func_70604_c(living.getMCEntity());
          }

     }

     public IEntityLivingBase getAttackTarget() {
          return (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLivingBase)this.entity).func_70643_av());
     }

     public IEntityLivingBase getLastAttacked() {
          return (IEntityLivingBase)NpcAPI.Instance().getIEntity(((EntityLivingBase)this.entity).func_110144_aD());
     }

     public int getLastAttackedTime() {
          return ((EntityLivingBase)this.entity).func_142013_aG();
     }

     public boolean canSeeEntity(IEntity entity) {
          return ((EntityLivingBase)this.entity).func_70685_l(entity.getMCEntity());
     }

     public void swingMainhand() {
          ((EntityLivingBase)this.entity).func_184609_a(EnumHand.MAIN_HAND);
     }

     public void swingOffhand() {
          ((EntityLivingBase)this.entity).func_184609_a(EnumHand.OFF_HAND);
     }

     public void addPotionEffect(int effect, int duration, int strength, boolean hideParticles) {
          Potion p = Potion.func_188412_a(effect);
          if (p != null) {
               if (strength < 0) {
                    strength = 0;
               } else if (strength > 255) {
                    strength = 255;
               }

               if (duration < 0) {
                    duration = 0;
               } else if (duration > 1000000) {
                    duration = 1000000;
               }

               if (!p.func_76403_b()) {
                    duration *= 20;
               }

               if (duration == 0) {
                    ((EntityLivingBase)this.entity).func_184589_d(p);
               } else {
                    ((EntityLivingBase)this.entity).func_70690_d(new PotionEffect(p, duration, strength, false, hideParticles));
               }

          }
     }

     public void clearPotionEffects() {
          ((EntityLivingBase)this.entity).func_70674_bp();
     }

     public int getPotionEffect(int effect) {
          PotionEffect pf = ((EntityLivingBase)this.entity).func_70660_b(Potion.func_188412_a(effect));
          return pf == null ? -1 : pf.func_76458_c();
     }

     public IItemStack getMainhandItem() {
          return NpcAPI.Instance().getIItemStack(((EntityLivingBase)this.entity).func_184614_ca());
     }

     public void setMainhandItem(IItemStack item) {
          ((EntityLivingBase)this.entity).func_184611_a(EnumHand.MAIN_HAND, item == null ? ItemStack.field_190927_a : item.getMCItemStack());
     }

     public IItemStack getOffhandItem() {
          return NpcAPI.Instance().getIItemStack(((EntityLivingBase)this.entity).func_184592_cb());
     }

     public void setOffhandItem(IItemStack item) {
          ((EntityLivingBase)this.entity).func_184611_a(EnumHand.OFF_HAND, item == null ? ItemStack.field_190927_a : item.getMCItemStack());
     }

     public IItemStack getArmor(int slot) {
          if (slot >= 0 && slot <= 3) {
               return NpcAPI.Instance().getIItemStack(((EntityLivingBase)this.entity).func_184582_a(this.getSlot(slot)));
          } else {
               throw new CustomNPCsException("Wrong slot id:" + slot, new Object[0]);
          }
     }

     public void setArmor(int slot, IItemStack item) {
          if (slot >= 0 && slot <= 3) {
               ((EntityLivingBase)this.entity).func_184201_a(this.getSlot(slot), item == null ? ItemStack.field_190927_a : item.getMCItemStack());
          } else {
               throw new CustomNPCsException("Wrong slot id:" + slot, new Object[0]);
          }
     }

     private EntityEquipmentSlot getSlot(int slot) {
          if (slot == 3) {
               return EntityEquipmentSlot.HEAD;
          } else if (slot == 2) {
               return EntityEquipmentSlot.CHEST;
          } else if (slot == 1) {
               return EntityEquipmentSlot.LEGS;
          } else {
               return slot == 0 ? EntityEquipmentSlot.FEET : null;
          }
     }

     public float getRotation() {
          return ((EntityLivingBase)this.entity).field_70761_aq;
     }

     public void setRotation(float rotation) {
          ((EntityLivingBase)this.entity).field_70761_aq = rotation;
     }

     public int getType() {
          return 5;
     }

     public boolean typeOf(int type) {
          return type == 5 ? true : super.typeOf(type);
     }

     public boolean isChild() {
          return ((EntityLivingBase)this.entity).func_70631_g_();
     }

     public IMark addMark(int type) {
          MarkData data = MarkData.get((EntityLivingBase)this.entity);
          return data.addMark(type);
     }

     public void removeMark(IMark mark) {
          MarkData data = MarkData.get((EntityLivingBase)this.entity);
          data.marks.remove(mark);
          data.syncClients();
     }

     public IMark[] getMarks() {
          MarkData data = MarkData.get((EntityLivingBase)this.entity);
          return (IMark[])data.marks.toArray(new IMark[data.marks.size()]);
     }

     public float getMoveForward() {
          return ((EntityLivingBase)this.entity).field_191988_bg;
     }

     public void setMoveForward(float move) {
          ((EntityLivingBase)this.entity).field_191988_bg = move;
     }

     public float getMoveStrafing() {
          return ((EntityLivingBase)this.entity).field_70702_br;
     }

     public void setMoveStrafing(float move) {
          ((EntityLivingBase)this.entity).field_70702_br = move;
     }

     public float getMoveVertical() {
          return ((EntityLivingBase)this.entity).field_70701_bs;
     }

     public void setMoveVertical(float move) {
          ((EntityLivingBase)this.entity).field_70701_bs = move;
     }
}
