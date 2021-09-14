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
		this.setMaxStackSize(64);
	}

	public Item setTranslationKey(String name) {
		super.setTranslationKey(name);
		this.setRegistryName(new ResourceLocation("customnpcs", name));
		return this;
	}

	public boolean store(EntityLivingBase entity, ItemStack stack, EntityPlayer player) {
		if (this.hasPermission(entity, player) && !(entity instanceof EntityPlayer)) {
			ItemStack stone = new ItemStack(CustomItems.soulstoneFull);
			NBTTagCompound compound = new NBTTagCompound();
			if (!entity.writeToNBTAtomically(compound)) {
				return false;
			} else {
				ServerCloneController.Instance.cleanTags(compound);
				stone.setTagInfo("Entity", compound);
				String name = EntityList.getEntityString(entity);
				if (name == null) {
					name = "generic";
				}

				stone.setTagInfo("Name", new NBTTagString("entity." + name + ".name"));
				if (entity instanceof EntityNPCInterface) {
					EntityNPCInterface npc = (EntityNPCInterface) entity;
					stone.setTagInfo("DisplayName", new NBTTagString(entity.getName()));
					if (npc.advanced.role == 6) {
						RoleCompanion role = (RoleCompanion) npc.roleInterface;
						stone.setTagInfo("ExtraText", new NBTTagString("companion.stage,: ," + role.stage.name));
					}
				} else if (entity instanceof EntityLiving && ((EntityLiving) entity).hasCustomName()) {
					stone.setTagInfo("DisplayName", new NBTTagString(((EntityLiving) entity).getCustomNameTag()));
				}

				NoppesUtilServer.GivePlayerItem(player, player, stone);
				if (!player.capabilities.isCreativeMode) {
					stack.splitStack(1);
					if (stack.getCount() <= 0) {
						player.inventory.deleteStack(stack);
					}
				}

				entity.isDead = true;
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
			EntityNPCInterface npc = (EntityNPCInterface) entity;
			if (npc.advanced.role == 6) {
				RoleCompanion role = (RoleCompanion) npc.roleInterface;
				if (role.getOwner() == player) {
					return true;
				}
			}

			if (npc.advanced.role == 2) {
				RoleFollower role = (RoleFollower) npc.roleInterface;
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
