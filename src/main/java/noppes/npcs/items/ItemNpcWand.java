package noppes.npcs.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.util.CustomNPCsScheduler;
import noppes.npcs.util.IPermission;

public class ItemNpcWand extends Item implements IPermission {
	public ItemNpcWand() {
		this.maxStackSize = 1;
		this.setCreativeTab(CustomItems.tab);
	}

	public ActionResult onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (!world.isRemote) {
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		} else {
			CustomNpcs.proxy.openGui(0, 0, 0, EnumGuiType.NpcRemote, player);
			return new ActionResult(EnumActionResult.SUCCESS, itemstack);
		}
	}

	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos bpos, EnumHand hand, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		} else {
			if (CustomNpcs.OpsOnly && !player.getServer().getPlayerList().canSendCommands(player.getGameProfile())) {
				player.sendMessage(new TextComponentTranslation("availability.permission", new Object[0]));
			} else if (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.NPC_CREATE)) {
				EntityCustomNpc npc = new EntityCustomNpc(world);
				npc.ais.setStartPos(bpos.up());
				npc.setLocationAndAngles((double) ((float) bpos.getX() + 0.5F), npc.getStartYPos(),
						(double) ((float) bpos.getZ() + 0.5F), player.rotationYaw, player.rotationPitch);
				world.spawnEntity(npc);
				npc.setHealth(npc.getMaxHealth());
				CustomNPCsScheduler.runTack(() -> {
					NoppesUtilServer.sendOpenGui(player, EnumGuiType.MainMenuDisplay, npc);
				}, 100);
			} else {
				player.sendMessage(new TextComponentTranslation("availability.permission", new Object[0]));
			}

			return EnumActionResult.SUCCESS;
		}
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase playerIn) {
		return stack;
	}

	public Item setTranslationKey(String name) {
		this.setRegistryName(new ResourceLocation("customnpcs", name));
		return super.setTranslationKey(name);
	}

	public boolean isAllowed(EnumPacketServer e) {
		return true;
	}
}
