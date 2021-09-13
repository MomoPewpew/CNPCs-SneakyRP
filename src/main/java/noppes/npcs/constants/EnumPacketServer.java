package noppes.npcs.constants;

import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;

public enum EnumPacketServer {
     Delete(CustomNpcsPermissions.NPC_DELETE, true),
     RemoteMainMenu(CustomNpcsPermissions.NPC_GUI),
     NpcMenuClose(CustomNpcsPermissions.NPC_GUI, true),
     RemoteDelete(CustomNpcsPermissions.NPC_DELETE, true),
     RemoteFreeze(CustomNpcsPermissions.NPC_FREEZE),
     RemoteReset(CustomNpcsPermissions.NPC_RESET),
     SpawnMob(CustomNpcsPermissions.SPAWNER_MOB),
     MobSpawner(CustomNpcsPermissions.SPAWNER_CREATE),
     MainmenuAISave(CustomNpcsPermissions.NPC_ADVANCED, true),
     MainmenuAIGet(true),
     MainmenuInvSave(CustomNpcsPermissions.NPC_INVENTORY, true),
     MainmenuInvGet(true),
     MainmenuStatsSave(CustomNpcsPermissions.NPC_STATS, true),
     MainmenuStatsGet(true),
     MainmenuDisplaySave(CustomNpcsPermissions.NPC_DISPLAY, true),
     MainmenuDisplayGet(true),
     ModelDataSave(CustomNpcsPermissions.NPC_DISPLAY, true),
     MainmenuAdvancedSave(CustomNpcsPermissions.NPC_ADVANCED, true),
     MainmenuAdvancedGet(true),
     MainmenuAdvancedMarkData(CustomNpcsPermissions.NPC_ADVANCED, true),
     DialogNpcSet(CustomNpcsPermissions.NPC_ADVANCED),
     DialogNpcRemove(CustomNpcsPermissions.NPC_ADVANCED, true),
     FactionSet(CustomNpcsPermissions.NPC_ADVANCED, true),
     TransportSave(CustomNpcsPermissions.NPC_ADVANCED, true),
     TransformSave(CustomNpcsPermissions.NPC_ADVANCED, true),
     TransformGet(true),
     TransformLoad(CustomNpcsPermissions.NPC_ADVANCED, true),
     TraderMarketSave(CustomNpcsPermissions.NPC_ADVANCED, true),
     JobSave(CustomNpcsPermissions.NPC_ADVANCED, true),
     JobGet(true),
     RoleSave(CustomNpcsPermissions.NPC_ADVANCED, true),
     RoleGet(true),
     JobSpawnerAdd(CustomNpcsPermissions.NPC_ADVANCED, true),
     JobSpawnerRemove(CustomNpcsPermissions.NPC_ADVANCED, true),
     RoleCompanionUpdate(CustomNpcsPermissions.NPC_ADVANCED, true),
     LinkedSet(CustomNpcsPermissions.NPC_ADVANCED, true),
     ClonePreSave(CustomNpcsPermissions.NPC_CLONE),
     CloneSave(CustomNpcsPermissions.NPC_CLONE),
     CloneRemove(CustomNpcsPermissions.NPC_CLONE),
     CloneList,
     LinkedGetAll,
     LinkedRemove(CustomNpcsPermissions.GLOBAL_LINKED),
     LinkedAdd(CustomNpcsPermissions.GLOBAL_LINKED),
     PlayerDataRemove(CustomNpcsPermissions.GLOBAL_PLAYERDATA),
     BankSave(CustomNpcsPermissions.GLOBAL_BANK),
     BanksGet,
     BankGet,
     BankRemove(CustomNpcsPermissions.GLOBAL_BANK),
     DialogCategorySave(CustomNpcsPermissions.GLOBAL_DIALOG),
     DialogCategoryRemove(CustomNpcsPermissions.GLOBAL_DIALOG),
     DialogSave(CustomNpcsPermissions.GLOBAL_DIALOG),
     DialogRemove(CustomNpcsPermissions.GLOBAL_DIALOG),
     TransportCategoryRemove(CustomNpcsPermissions.GLOBAL_TRANSPORT),
     TransportGetLocation(true),
     TransportRemove(CustomNpcsPermissions.GLOBAL_TRANSPORT),
     TransportsGet,
     TransportCategorySave(CustomNpcsPermissions.GLOBAL_TRANSPORT),
     TransportCategoriesGet,
     FactionRemove(CustomNpcsPermissions.GLOBAL_FACTION),
     FactionSave(CustomNpcsPermissions.GLOBAL_FACTION),
     FactionsGet,
     FactionGet,
     QuestCategorySave(CustomNpcsPermissions.GLOBAL_QUEST),
     QuestRemove(CustomNpcsPermissions.GLOBAL_QUEST),
     QuestCategoryRemove(CustomNpcsPermissions.GLOBAL_QUEST),
     QuestRewardSave(CustomNpcsPermissions.GLOBAL_QUEST),
     QuestSave(CustomNpcsPermissions.GLOBAL_QUEST),
     QuestDialogGetTitle(CustomNpcsPermissions.GLOBAL_QUEST),
     RecipeSave(CustomNpcsPermissions.GLOBAL_RECIPE),
     RecipeRemove(CustomNpcsPermissions.GLOBAL_RECIPE),
     NaturalSpawnSave(CustomNpcsPermissions.GLOBAL_NATURALSPAWN),
     NaturalSpawnGet,
     NaturalSpawnRemove(CustomNpcsPermissions.GLOBAL_NATURALSPAWN),
     MerchantUpdate(CustomNpcsPermissions.EDIT_VILLAGER),
     PlayerRider(CustomNpcsPermissions.TOOL_MOUNTER),
     SpawnRider(CustomNpcsPermissions.TOOL_MOUNTER),
     MovingPathSave(CustomNpcsPermissions.TOOL_PATHER, true),
     MovingPathGet(true),
     DoorSave(CustomNpcsPermissions.TOOL_SCRIPTER),
     DoorGet,
     ScriptDataSave(CustomNpcsPermissions.TOOL_SCRIPTER, true),
     ScriptDataGet(true),
     ScriptBlockDataSave(CustomNpcsPermissions.TOOL_SCRIPTER, false),
     ScriptBlockDataGet(false),
     ScriptDoorDataSave(CustomNpcsPermissions.TOOL_SCRIPTER, false),
     ScriptDoorDataGet(false),
     ScriptPlayerSave(CustomNpcsPermissions.TOOL_SCRIPTER, false),
     ScriptPlayerGet(false),
     ScriptItemDataSave(CustomNpcsPermissions.TOOL_SCRIPTER, false),
     ScriptItemDataGet(false),
     ScriptForgeSave(CustomNpcsPermissions.TOOL_SCRIPTER, false),
     ScriptForgeGet(false),
     DialogNpcGet,
     RecipesGet,
     RecipeGet,
     QuestOpenGui,
     PlayerDataGet,
     RemoteNpcsGet(CustomNpcsPermissions.NPC_GUI),
     RemoteTpToNpc,
     SaveTileEntity,
     NaturalSpawnGetAll,
     MailOpenSetup,
     DimensionsGet,
     DimensionTeleport,
     GetTileEntity,
     Gui,
     SchematicsTile,
     SchematicsSet,
     SchematicsBuild,
     SchematicsTileSave,
     SchematicStore,
     SceneStart(CustomNpcsPermissions.SCENES),
     SceneReset(CustomNpcsPermissions.SCENES),
     NbtBookSaveEntity(CustomNpcsPermissions.TOOL_NBTBOOK),
     NbtBookSaveBlock(CustomNpcsPermissions.TOOL_NBTBOOK),
     CustomGuiClose,
     CustomGuiButton,
     CustomGuiSlotChange,
     CustomGuiScrollClick;

     public CustomNpcsPermissions.Permission permission;
     public boolean needsNpc;
     private boolean exempt;

     private EnumPacketServer() {
          this.needsNpc = false;
          this.exempt = false;
     }

     private EnumPacketServer(CustomNpcsPermissions.Permission permission, boolean npc) {
          this(permission);
     }

     private EnumPacketServer(boolean npc) {
          this.needsNpc = false;
          this.exempt = false;
          this.needsNpc = npc;
     }

     private EnumPacketServer(CustomNpcsPermissions.Permission permission) {
          this.needsNpc = false;
          this.exempt = false;
          this.permission = permission;
     }

     public boolean hasPermission() {
          return this.permission != null;
     }

     public void exempt() {
          this.exempt = true;
     }

     public boolean isExempt() {
          return CustomNpcs.OpsOnly || this.exempt;
     }

     static {
          GetTileEntity.exempt();
          ScriptBlockDataGet.exempt();
          ScriptDoorDataGet.exempt();
          FactionsGet.exempt();
          FactionGet.exempt();
          SceneStart.exempt();
          SceneReset.exempt();
          CustomGuiClose.exempt();
          CustomGuiButton.exempt();
          CustomGuiSlotChange.exempt();
          CustomGuiScrollClick.exempt();
     }
}
