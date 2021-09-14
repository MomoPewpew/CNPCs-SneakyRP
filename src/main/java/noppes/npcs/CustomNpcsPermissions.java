package noppes.npcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.LogManager;

public class CustomNpcsPermissions {
	public static final CustomNpcsPermissions.Permission NPC_DELETE = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.delete");
	public static final CustomNpcsPermissions.Permission NPC_CREATE = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.create");
	public static final CustomNpcsPermissions.Permission NPC_GUI = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.gui");
	public static final CustomNpcsPermissions.Permission NPC_FREEZE = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.freeze");
	public static final CustomNpcsPermissions.Permission NPC_RESET = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.reset");
	public static final CustomNpcsPermissions.Permission NPC_AI = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.ai");
	public static final CustomNpcsPermissions.Permission NPC_ADVANCED = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.advanced");
	public static final CustomNpcsPermissions.Permission NPC_DISPLAY = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.display");
	public static final CustomNpcsPermissions.Permission NPC_INVENTORY = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.inventory");
	public static final CustomNpcsPermissions.Permission NPC_STATS = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.stats");
	public static final CustomNpcsPermissions.Permission NPC_CLONE = new CustomNpcsPermissions.Permission(
			"customnpcs.npc.clone");
	public static final CustomNpcsPermissions.Permission GLOBAL_LINKED = new CustomNpcsPermissions.Permission(
			"customnpcs.global.linked");
	public static final CustomNpcsPermissions.Permission GLOBAL_PLAYERDATA = new CustomNpcsPermissions.Permission(
			"customnpcs.global.playerdata");
	public static final CustomNpcsPermissions.Permission GLOBAL_BANK = new CustomNpcsPermissions.Permission(
			"customnpcs.global.bank");
	public static final CustomNpcsPermissions.Permission GLOBAL_DIALOG = new CustomNpcsPermissions.Permission(
			"customnpcs.global.dialog");
	public static final CustomNpcsPermissions.Permission GLOBAL_QUEST = new CustomNpcsPermissions.Permission(
			"customnpcs.global.quest");
	public static final CustomNpcsPermissions.Permission GLOBAL_FACTION = new CustomNpcsPermissions.Permission(
			"customnpcs.global.faction");
	public static final CustomNpcsPermissions.Permission GLOBAL_TRANSPORT = new CustomNpcsPermissions.Permission(
			"customnpcs.global.transport");
	public static final CustomNpcsPermissions.Permission GLOBAL_RECIPE = new CustomNpcsPermissions.Permission(
			"customnpcs.global.recipe");
	public static final CustomNpcsPermissions.Permission GLOBAL_NATURALSPAWN = new CustomNpcsPermissions.Permission(
			"customnpcs.global.naturalspawn");
	public static final CustomNpcsPermissions.Permission SPAWNER_MOB = new CustomNpcsPermissions.Permission(
			"customnpcs.spawner.mob");
	public static final CustomNpcsPermissions.Permission SPAWNER_CREATE = new CustomNpcsPermissions.Permission(
			"customnpcs.spawner.create");
	public static final CustomNpcsPermissions.Permission TOOL_MOUNTER = new CustomNpcsPermissions.Permission(
			"customnpcs.tool.mounter");
	public static final CustomNpcsPermissions.Permission TOOL_PATHER = new CustomNpcsPermissions.Permission(
			"customnpcs.tool.pather");
	public static final CustomNpcsPermissions.Permission TOOL_SCRIPTER = new CustomNpcsPermissions.Permission(
			"customnpcs.tool.scripter");
	public static final CustomNpcsPermissions.Permission TOOL_NBTBOOK = new CustomNpcsPermissions.Permission(
			"customnpcs.tool.nbtbook");
	public static final CustomNpcsPermissions.Permission EDIT_VILLAGER = new CustomNpcsPermissions.Permission(
			"customnpcs.edit.villager");
	public static final CustomNpcsPermissions.Permission EDIT_BLOCKS = new CustomNpcsPermissions.Permission(
			"customnpcs.edit.blocks");
	public static final CustomNpcsPermissions.Permission SOULSTONE_ALL = new CustomNpcsPermissions.Permission(
			"customnpcs.soulstone.all", false);
	public static final CustomNpcsPermissions.Permission SCENES = new CustomNpcsPermissions.Permission(
			"customnpcs.scenes");
	public static CustomNpcsPermissions Instance;

	public CustomNpcsPermissions() {
		Instance = this;
		if (!CustomNpcs.DisablePermissions) {
			LogManager.getLogger(CustomNpcs.class).info("CustomNPC Permissions available:");
			Collections.sort(CustomNpcsPermissions.Permission.permissions, (o1, o2) -> {
				return o1.name.compareToIgnoreCase(o2.name);
			});
			Iterator var1 = CustomNpcsPermissions.Permission.permissions.iterator();

			while (var1.hasNext()) {
				CustomNpcsPermissions.Permission p = (CustomNpcsPermissions.Permission) var1.next();
				PermissionAPI.registerNode(p.name,
						p.defaultValue ? DefaultPermissionLevel.ALL : DefaultPermissionLevel.OP, p.name);
				LogManager.getLogger(CustomNpcs.class).info(p.name);
			}
		}

	}

	public static boolean hasPermission(EntityPlayer player, CustomNpcsPermissions.Permission permission) {
		return CustomNpcs.DisablePermissions ? permission.defaultValue : hasPermissionString(player, permission.name);
	}

	public static boolean hasPermissionString(EntityPlayer player, String permission) {
		return CustomNpcs.DisablePermissions ? true : PermissionAPI.hasPermission(player, permission);
	}

	public static class Permission {
		private static final List permissions = new ArrayList();
		public String name;
		public boolean defaultValue = true;

		public Permission(String name) {
			this.name = name;
			permissions.add(this);
		}

		public Permission(String name, boolean defaultValue) {
			this.name = name;
			permissions.add(this);
			this.defaultValue = defaultValue;
		}
	}
}
