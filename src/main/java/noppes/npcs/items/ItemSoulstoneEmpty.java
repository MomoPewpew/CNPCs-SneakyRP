package noppes.npcs.items;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class ItemSoulstoneEmpty extends Item {
     public ItemSoulstoneEmpty() {
          this.func_77625_d(64);
     }

     public Item setUnlocalizedName(String name) {
          super.setUnlocalizedName(name);
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return this;
     }

     public boolean store(EntityLivingBase entity, ItemStack stack, EntityPlayer player) {
          if (this.hasPermission(entity, player) && !(entity instanceof EntityPlayer)) {
               ItemStack stone = new ItemStack(CustomItems.soulstoneFull);
               NBTTagCompound compound = new NBTTagCompound();
               if (!entity.func_184198_c(compound)) {
                    return false;
               } else {
                    ServerCloneController.Instance.cleanTags(compound);
                    stone.func_77983_a("Entity", compound);
                    String name = EntityList.func_75621_b(entity);
                    if (name == null) {
                         name = "generic";
                    }

                    stone.func_77983_a("Name", new NBTTagString("entity." + name + ".name"));
                    if (entity instanceof EntityNPCInterface) {
                         EntityNPCInterface npc = (EntityNPCInterface)entity;
                         stone.func_77983_a("DisplayName", new NBTTagString(entity.func_70005_c_()));
                         if (npc.advanced.role == 6) {
                              RoleCompanion role = (RoleCompanion)npc.roleInterface;
                              stone.func_77983_a("ExtraText", new NBTTagString("companion.stage,: ," + role.stage.name));
                         }
                    } else if (entity instanceof EntityLiving && ((EntityLiving)entity).func_145818_k_()) {
                         stone.func_77983_a("DisplayName", new NBTTagString(((EntityLiving)entity).func_95999_t()));
                    }

                    NoppesUtilServer.GivePlayerItem(player, player, stone);
                    if (!player.field_71075_bZ.field_75098_d) {
                         stack.splitStack(1);
                         if (stack.getCount() <= 0) {
                              player.inventory.func_184437_d(stack);
                         }
                    }

                    entity.field_70128_L = true;
                    return true;
               }
          } else {
               return false;
          }
     }

     public boolean hasPermission(EntityLivingBase entity, EntityPlayer player) {
          if (NoppesUtilServer.isOp(player)) {
               return true;
          } else if (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.SOULSTONE_ALL)) {
               return true;
          } else if (entity instanceof EntityNPCInterface) {
               EntityNPCInterface npc = (EntityNPCInterface)entity;
               if (npc.advanced.role == 6) {
                    RoleCompanion role = (RoleCompanion)npc.roleInterface;
                    if (role.getOwner() == player) {
                         return true;
                    }
               }

               if (npc.advanced.role == 2) {
                    RoleFollower role = (RoleFollower)npc.roleInterface;
                    if (role.getOwner() == player) {
                         return !role.refuseSoulStone;
                    }
               }

               return CustomNpcs.SoulStoneNPCs;
          } else {
               return entity instanceof EntityAnimal ? CustomNpcs.SoulStoneAnimals : false;
          }
     }
}
