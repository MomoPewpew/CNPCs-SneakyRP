package noppes.npcs.client;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabFactions;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabQuests;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import noppes.npcs.CommonProxy;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.PacketHandlerPlayer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemScripted;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.client.fx.EntityEnderFX;
import noppes.npcs.client.gui.GuiBlockBuilder;
import noppes.npcs.client.gui.GuiBlockCopy;
import noppes.npcs.client.gui.GuiBorderBlock;
import noppes.npcs.client.gui.GuiMerchantAdd;
import noppes.npcs.client.gui.GuiNbtBook;
import noppes.npcs.client.gui.GuiNpcDimension;
import noppes.npcs.client.gui.GuiNpcMobSpawner;
import noppes.npcs.client.gui.GuiNpcMobSpawnerMounter;
import noppes.npcs.client.gui.GuiNpcPather;
import noppes.npcs.client.gui.GuiNpcRedstoneBlock;
import noppes.npcs.client.gui.GuiNpcRemoteEditor;
import noppes.npcs.client.gui.GuiNpcWaypoint;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.global.GuiNPCManageBanks;
import noppes.npcs.client.gui.global.GuiNPCManageDialogs;
import noppes.npcs.client.gui.global.GuiNPCManageFactions;
import noppes.npcs.client.gui.global.GuiNPCManageLinkedNpc;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.global.GuiNPCManageTransporters;
import noppes.npcs.client.gui.global.GuiNpcManageRecipes;
import noppes.npcs.client.gui.global.GuiNpcQuestReward;
import noppes.npcs.client.gui.mainmenu.GuiNPCGlobalMainMenu;
import noppes.npcs.client.gui.mainmenu.GuiNPCInv;
import noppes.npcs.client.gui.mainmenu.GuiNpcAI;
import noppes.npcs.client.gui.mainmenu.GuiNpcAdvanced;
import noppes.npcs.client.gui.mainmenu.GuiNpcDisplay;
import noppes.npcs.client.gui.mainmenu.GuiNpcStats;
import noppes.npcs.client.gui.player.GuiCustomChest;
import noppes.npcs.client.gui.player.GuiMailbox;
import noppes.npcs.client.gui.player.GuiMailmanWrite;
import noppes.npcs.client.gui.player.GuiNPCBankChest;
import noppes.npcs.client.gui.player.GuiNPCTrader;
import noppes.npcs.client.gui.player.GuiNpcCarpentryBench;
import noppes.npcs.client.gui.player.GuiNpcFollower;
import noppes.npcs.client.gui.player.GuiNpcFollowerHire;
import noppes.npcs.client.gui.player.GuiTransportSelection;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionInv;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionStats;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionTalents;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeItem;
import noppes.npcs.client.gui.roles.GuiNpcBankSetup;
import noppes.npcs.client.gui.roles.GuiNpcFollowerSetup;
import noppes.npcs.client.gui.roles.GuiNpcItemGiver;
import noppes.npcs.client.gui.roles.GuiNpcTraderSetup;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;
import noppes.npcs.client.gui.script.GuiScript;
import noppes.npcs.client.gui.script.GuiScriptBlock;
import noppes.npcs.client.gui.script.GuiScriptDoor;
import noppes.npcs.client.gui.script.GuiScriptGlobal;
import noppes.npcs.client.gui.script.GuiScriptItem;
import noppes.npcs.client.model.ModelBipedAlt;
import noppes.npcs.client.model.ModelClassicPlayer;
import noppes.npcs.client.model.ModelNPCGolem;
import noppes.npcs.client.model.ModelNpcCrystal;
import noppes.npcs.client.model.ModelNpcDragon;
import noppes.npcs.client.model.ModelNpcSlime;
import noppes.npcs.client.model.ModelPlayerAlt;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.client.renderer.RenderNPCPony;
import noppes.npcs.client.renderer.RenderNpcCrystal;
import noppes.npcs.client.renderer.RenderNpcDragon;
import noppes.npcs.client.renderer.RenderNpcSlime;
import noppes.npcs.client.renderer.RenderProjectile;
import noppes.npcs.config.TrueTypeFont;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.containers.ContainerCustomChest;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.containers.ContainerNPCInv;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPC64x32;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityNpcAlex;
import noppes.npcs.entity.EntityNpcClassicPlayer;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcPony;
import noppes.npcs.entity.EntityNpcSlime;
import noppes.npcs.entity.EntityProjectile;

public class ClientProxy extends CommonProxy {
     public static PlayerData playerData = new PlayerData();
     public static KeyBinding QuestLog;
     public static KeyBinding Scene1;
     public static KeyBinding SceneReset;
     public static KeyBinding Scene2;
     public static KeyBinding Scene3;
     public static ClientProxy.FontContainer Font;

     public void load() {
          Font = new ClientProxy.FontContainer(CustomNpcs.FontType, CustomNpcs.FontSize);
          this.createFolders();
          ((IReloadableResourceManager)Minecraft.func_71410_x().func_110442_L()).func_110542_a(new CustomNpcResourceListener());
          CustomNpcs.Channel.register(new PacketHandlerClient());
          CustomNpcs.ChannelPlayer.register(new PacketHandlerPlayer());
          new MusicController();
          MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
          Minecraft mc = Minecraft.func_71410_x();
          QuestLog = new KeyBinding("Quest Log", 38, "key.categories.gameplay");
          if (CustomNpcs.SceneButtonsEnabled) {
               Scene1 = new KeyBinding("Scene1 start/pause", 79, "key.categories.gameplay");
               Scene2 = new KeyBinding("Scene2 start/pause", 80, "key.categories.gameplay");
               Scene3 = new KeyBinding("Scene3 start/pause", 81, "key.categories.gameplay");
               SceneReset = new KeyBinding("Scene reset", 82, "key.categories.gameplay");
               ClientRegistry.registerKeyBinding(Scene1);
               ClientRegistry.registerKeyBinding(Scene2);
               ClientRegistry.registerKeyBinding(Scene3);
               ClientRegistry.registerKeyBinding(SceneReset);
          }

          ClientRegistry.registerKeyBinding(QuestLog);
          mc.field_71474_y.func_74300_a();
          new PresetController(CustomNpcs.Dir);
          if (CustomNpcs.EnableUpdateChecker) {
               VersionChecker checker = new VersionChecker();
               checker.start();
          }

          PixelmonHelper.loadClient();
     }

     public PlayerData getPlayerData(EntityPlayer player) {
          if (player.func_110124_au() == Minecraft.func_71410_x().player.func_110124_au()) {
               if (playerData.player != player) {
                    playerData.player = player;
               }

               return playerData;
          } else {
               return null;
          }
     }

     public void postload() {
          MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
          if (CustomNpcs.InventoryGuiEnabled) {
               MinecraftForge.EVENT_BUS.register(new TabRegistry());
               if (TabRegistry.getTabList().isEmpty()) {
                    TabRegistry.registerTab(new InventoryTabVanilla());
               }

               TabRegistry.registerTab(new InventoryTabFactions());
               TabRegistry.registerTab(new InventoryTabQuests());
          }

          RenderingRegistry.registerEntityRenderingHandler(EntityNpcPony.class, new RenderNPCPony());
          RenderingRegistry.registerEntityRenderingHandler(EntityNpcCrystal.class, new RenderNpcCrystal(new ModelNpcCrystal(0.5F)));
          RenderingRegistry.registerEntityRenderingHandler(EntityNpcDragon.class, new RenderNpcDragon(new ModelNpcDragon(0.0F), 0.5F));
          RenderingRegistry.registerEntityRenderingHandler(EntityNpcSlime.class, new RenderNpcSlime(new ModelNpcSlime(16), new ModelNpcSlime(0), 0.25F));
          RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, new RenderProjectile());
          RenderingRegistry.registerEntityRenderingHandler(EntityCustomNpc.class, new RenderCustomNpc(new ModelPlayerAlt(0.0F, false)));
          RenderingRegistry.registerEntityRenderingHandler(EntityNPC64x32.class, new RenderCustomNpc(new ModelBipedAlt(0.0F)));
          RenderingRegistry.registerEntityRenderingHandler(EntityNPCGolem.class, new RenderNPCInterface(new ModelNPCGolem(0.0F), 0.0F));
          RenderingRegistry.registerEntityRenderingHandler(EntityNpcAlex.class, new RenderCustomNpc(new ModelPlayerAlt(0.0F, true)));
          RenderingRegistry.registerEntityRenderingHandler(EntityNpcClassicPlayer.class, new RenderCustomNpc(new ModelClassicPlayer(0.0F)));
          Minecraft.func_71410_x().getItemColors().func_186730_a((stack, tintIndex) -> {
               return 9127187;
          }, new Item[]{CustomItems.mount, CustomItems.cloner, CustomItems.moving, CustomItems.scripter, CustomItems.wand, CustomItems.teleporter});
          Minecraft.func_71410_x().getItemColors().func_186730_a((stack, tintIndex) -> {
               IItemStack item = NpcAPI.Instance().getIItemStack(stack);
               return stack.func_77973_b() == CustomItems.scripted_item ? ((IItemScripted)item).getColor() : -1;
          }, new Item[]{CustomItems.scripted_item});
     }

     private void createFolders() {
          File file = new File(CustomNpcs.Dir, "assets/customnpcs");
          if (!file.exists()) {
               file.mkdirs();
          }

          File check = new File(file, "sounds");
          if (!check.exists()) {
               check.mkdir();
          }

          File json = new File(file, "sounds.json");
          if (!json.exists()) {
               try {
                    json.createNewFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(json));
                    writer.write("{\n\n}");
                    writer.close();
               } catch (IOException var5) {
               }
          }

          check = new File(file, "textures");
          if (!check.exists()) {
               check.mkdir();
          }

     }

     public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
          if (ID > EnumGuiType.values().length) {
               return null;
          } else {
               EnumGuiType gui = EnumGuiType.values()[ID];
               EntityNPCInterface npc = NoppesUtil.getLastNpc();
               Container container = this.getContainer(gui, player, x, y, z, npc);
               return this.getGui(npc, gui, container, x, y, z);
          }
     }

     private GuiScreen getGui(EntityNPCInterface npc, EnumGuiType gui, Container container, int x, int y, int z) {
          if (gui == EnumGuiType.CustomChest) {
               return new GuiCustomChest((ContainerCustomChest)container);
          } else {
               if (gui == EnumGuiType.MainMenuDisplay) {
                    if (npc != null) {
                         return new GuiNpcDisplay(npc);
                    }

                    Minecraft.func_71410_x().player.func_145747_a(new TextComponentString("Unable to find npc"));
               } else {
                    if (gui == EnumGuiType.MainMenuStats) {
                         return new GuiNpcStats(npc);
                    }

                    if (gui == EnumGuiType.MainMenuInv) {
                         return new GuiNPCInv(npc, (ContainerNPCInv)container);
                    }

                    if (gui == EnumGuiType.MainMenuAdvanced) {
                         return new GuiNpcAdvanced(npc);
                    }

                    if (gui == EnumGuiType.QuestReward) {
                         return new GuiNpcQuestReward(npc, (ContainerNpcQuestReward)container);
                    }

                    if (gui == EnumGuiType.QuestItem) {
                         return new GuiNpcQuestTypeItem(npc, (ContainerNpcQuestTypeItem)container);
                    }

                    if (gui == EnumGuiType.MovingPath) {
                         return new GuiNpcPather(npc);
                    }

                    if (gui == EnumGuiType.ManageFactions) {
                         return new GuiNPCManageFactions(npc);
                    }

                    if (gui == EnumGuiType.ManageLinked) {
                         return new GuiNPCManageLinkedNpc(npc);
                    }

                    if (gui == EnumGuiType.BuilderBlock) {
                         return new GuiBlockBuilder(x, y, z);
                    }

                    if (gui == EnumGuiType.ManageTransport) {
                         return new GuiNPCManageTransporters(npc);
                    }

                    if (gui == EnumGuiType.ManageRecipes) {
                         return new GuiNpcManageRecipes(npc, (ContainerManageRecipes)container);
                    }

                    if (gui == EnumGuiType.ManageDialogs) {
                         return new GuiNPCManageDialogs(npc);
                    }

                    if (gui == EnumGuiType.ManageQuests) {
                         return new GuiNPCManageQuest(npc);
                    }

                    if (gui == EnumGuiType.ManageBanks) {
                         return new GuiNPCManageBanks(npc, (ContainerManageBanks)container);
                    }

                    if (gui == EnumGuiType.MainMenuGlobal) {
                         return new GuiNPCGlobalMainMenu(npc);
                    }

                    if (gui == EnumGuiType.MainMenuAI) {
                         return new GuiNpcAI(npc);
                    }

                    if (gui == EnumGuiType.PlayerAnvil) {
                         return new GuiNpcCarpentryBench((ContainerCarpentryBench)container);
                    }

                    if (gui == EnumGuiType.PlayerFollowerHire) {
                         return new GuiNpcFollowerHire(npc, (ContainerNPCFollowerHire)container);
                    }

                    if (gui == EnumGuiType.PlayerFollower) {
                         return new GuiNpcFollower(npc, (ContainerNPCFollower)container);
                    }

                    if (gui == EnumGuiType.PlayerTrader) {
                         return new GuiNPCTrader(npc, (ContainerNPCTrader)container);
                    }

                    if (gui == EnumGuiType.PlayerBankSmall || gui == EnumGuiType.PlayerBankUnlock || gui == EnumGuiType.PlayerBankUprade || gui == EnumGuiType.PlayerBankLarge) {
                         return new GuiNPCBankChest(npc, (ContainerNPCBankInterface)container);
                    }

                    if (gui == EnumGuiType.PlayerTransporter) {
                         return new GuiTransportSelection(npc);
                    }

                    if (gui == EnumGuiType.Script) {
                         return new GuiScript(npc);
                    }

                    if (gui == EnumGuiType.ScriptBlock) {
                         return new GuiScriptBlock(x, y, z);
                    }

                    if (gui == EnumGuiType.ScriptItem) {
                         return new GuiScriptItem(Minecraft.func_71410_x().player);
                    }

                    if (gui == EnumGuiType.ScriptDoor) {
                         return new GuiScriptDoor(x, y, z);
                    }

                    if (gui == EnumGuiType.ScriptPlayers) {
                         return new GuiScriptGlobal();
                    }

                    if (gui == EnumGuiType.SetupFollower) {
                         return new GuiNpcFollowerSetup(npc, (ContainerNPCFollowerSetup)container);
                    }

                    if (gui == EnumGuiType.SetupItemGiver) {
                         return new GuiNpcItemGiver(npc, (ContainerNpcItemGiver)container);
                    }

                    if (gui == EnumGuiType.SetupTrader) {
                         return new GuiNpcTraderSetup(npc, (ContainerNPCTraderSetup)container);
                    }

                    if (gui == EnumGuiType.SetupTransporter) {
                         return new GuiNpcTransporter(npc);
                    }

                    if (gui == EnumGuiType.SetupBank) {
                         return new GuiNpcBankSetup(npc);
                    }

                    if (gui == EnumGuiType.NpcRemote && Minecraft.func_71410_x().field_71462_r == null) {
                         return new GuiNpcRemoteEditor();
                    }

                    if (gui == EnumGuiType.PlayerMailman) {
                         return new GuiMailmanWrite((ContainerMail)container, x == 1, y == 1);
                    }

                    if (gui == EnumGuiType.PlayerMailbox) {
                         return new GuiMailbox();
                    }

                    if (gui == EnumGuiType.MerchantAdd) {
                         return new GuiMerchantAdd();
                    }

                    if (gui == EnumGuiType.NpcDimensions) {
                         return new GuiNpcDimension();
                    }

                    if (gui == EnumGuiType.Border) {
                         return new GuiBorderBlock(x, y, z);
                    }

                    if (gui == EnumGuiType.RedstoneBlock) {
                         return new GuiNpcRedstoneBlock(x, y, z);
                    }

                    if (gui == EnumGuiType.MobSpawner) {
                         return new GuiNpcMobSpawner(x, y, z);
                    }

                    if (gui == EnumGuiType.CopyBlock) {
                         return new GuiBlockCopy(x, y, z);
                    }

                    if (gui == EnumGuiType.MobSpawnerMounter) {
                         return new GuiNpcMobSpawnerMounter(x, y, z);
                    }

                    if (gui == EnumGuiType.Waypoint) {
                         return new GuiNpcWaypoint(x, y, z);
                    }

                    if (gui == EnumGuiType.Companion) {
                         return new GuiNpcCompanionStats(npc);
                    }

                    if (gui == EnumGuiType.CompanionTalent) {
                         return new GuiNpcCompanionTalents(npc);
                    }

                    if (gui == EnumGuiType.CompanionInv) {
                         return new GuiNpcCompanionInv(npc, (ContainerNPCCompanion)container);
                    }

                    if (gui == EnumGuiType.NbtBook) {
                         return new GuiNbtBook(x, y, z);
                    }

                    if (gui == EnumGuiType.CustomGui) {
                         return new GuiCustom((ContainerCustomGui)container);
                    }
               }

               return null;
          }
     }

     public void openGui(int i, int j, int k, EnumGuiType gui, EntityPlayer player) {
          Minecraft minecraft = Minecraft.func_71410_x();
          if (minecraft.player == player) {
               GuiScreen guiscreen = this.getGui((EntityNPCInterface)null, gui, (Container)null, i, j, k);
               if (guiscreen != null) {
                    minecraft.displayGuiScreen(guiscreen);
               }

          }
     }

     public void openGui(EntityNPCInterface npc, EnumGuiType gui) {
          this.openGui(npc, gui, 0, 0, 0);
     }

     public void openGui(EntityNPCInterface npc, EnumGuiType gui, int x, int y, int z) {
          Minecraft minecraft = Minecraft.func_71410_x();
          Container container = this.getContainer(gui, minecraft.player, x, y, z, npc);
          GuiScreen guiscreen = this.getGui(npc, gui, container, x, y, z);
          if (guiscreen != null) {
               minecraft.displayGuiScreen(guiscreen);
          }

     }

     public void openGui(EntityPlayer player, Object guiscreen) {
          Minecraft minecraft = Minecraft.func_71410_x();
          if (player.world.field_72995_K && guiscreen instanceof GuiScreen) {
               if (guiscreen != null) {
                    minecraft.displayGuiScreen((GuiScreen)guiscreen);
               }

          }
     }

     public void spawnParticle(EntityLivingBase player, String string, Object... ob) {
          if (string.equals("Block")) {
               BlockPos pos = (BlockPos)ob[0];
               int id = (Integer)ob[1];
               Block block = Block.func_149729_e(id & 4095);
               Minecraft.func_71410_x().field_71452_i.func_180533_a(pos, block.func_176203_a(id >> 12 & 255));
          } else if (string.equals("ModelData")) {
               ModelData data = (ModelData)ob[0];
               ModelPartData particles = (ModelPartData)ob[1];
               EntityCustomNpc npc = (EntityCustomNpc)player;
               Minecraft minecraft = Minecraft.func_71410_x();
               double height = npc.func_70033_W() + (double)data.getBodyY();
               Random rand = npc.func_70681_au();

               for(int i = 0; i < 2; ++i) {
                    EntityEnderFX fx = new EntityEnderFX(npc, (rand.nextDouble() - 0.5D) * (double)player.field_70130_N, rand.nextDouble() * (double)player.field_70131_O - height - 0.25D, (rand.nextDouble() - 0.5D) * (double)player.field_70130_N, (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D, particles);
                    minecraft.field_71452_i.func_78873_a(fx);
               }
          }

     }

     public boolean hasClient() {
          return true;
     }

     public EntityPlayer getPlayer() {
          return Minecraft.func_71410_x().player;
     }

     public static void bindTexture(ResourceLocation location) {
          try {
               if (location == null) {
                    return;
               }

               TextureManager manager = Minecraft.func_71410_x().func_110434_K();
               ITextureObject ob = manager.func_110581_b(location);
               if (ob == null) {
                    ob = new SimpleTexture(location);
                    manager.func_110579_a(location, (ITextureObject)ob);
               }

               GlStateManager.func_179144_i(((ITextureObject)ob).func_110552_b());
          } catch (NullPointerException var3) {
          } catch (ReportedException var4) {
          }

     }

     public void spawnParticle(EnumParticleTypes particle, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
          Minecraft mc = Minecraft.func_71410_x();
          double xx = mc.func_175606_aa().field_70165_t - x;
          double yy = mc.func_175606_aa().field_70163_u - y;
          double zz = mc.func_175606_aa().field_70161_v - z;
          if (xx * xx + yy * yy + zz * zz <= 256.0D) {
               Particle fx = mc.field_71452_i.func_178927_a(particle.func_179348_c(), x, y, z, motionX, motionY, motionZ, new int[0]);
               if (fx != null) {
                    if (particle == EnumParticleTypes.FLAME) {
                         ObfuscationReflectionHelper.setPrivateValue(ParticleFlame.class, (ParticleFlame)fx, scale, 0);
                    } else if (particle == EnumParticleTypes.SMOKE_NORMAL) {
                         ObfuscationReflectionHelper.setPrivateValue(ParticleSmokeNormal.class, (ParticleSmokeNormal)fx, scale, 0);
                    }

               }
          }
     }

     public static class FontContainer {
          private TrueTypeFont textFont = null;
          public boolean useCustomFont = true;

          private FontContainer() {
          }

          public FontContainer(String fontType, int fontSize) {
               this.textFont = new TrueTypeFont(new Font(fontType, 0, fontSize), 1.0F);
               this.useCustomFont = !fontType.equalsIgnoreCase("minecraft");

               try {
                    if (!this.useCustomFont || fontType.isEmpty() || fontType.equalsIgnoreCase("default")) {
                         this.textFont = new TrueTypeFont(new ResourceLocation("customnpcs", "opensans.ttf"), fontSize, 1.0F);
                    }
               } catch (Exception var4) {
                    LogWriter.info("Failed loading font so using Arial");
               }

          }

          public int height(String text) {
               return this.useCustomFont ? this.textFont.height(text) : Minecraft.func_71410_x().fontRenderer.field_78288_b;
          }

          public int width(String text) {
               return this.useCustomFont ? this.textFont.width(text) : Minecraft.func_71410_x().fontRenderer.func_78256_a(text);
          }

          public ClientProxy.FontContainer copy() {
               ClientProxy.FontContainer font = new ClientProxy.FontContainer();
               font.textFont = this.textFont;
               font.useCustomFont = this.useCustomFont;
               return font;
          }

          public void drawString(String text, int x, int y, int color) {
               if (this.useCustomFont) {
                    this.textFont.draw(text, (float)x, (float)y, color);
               } else {
                    Minecraft.func_71410_x().fontRenderer.func_175063_a(text, (float)x, (float)y, color);
               }

          }

          public String getName() {
               return !this.useCustomFont ? "Minecraft" : this.textFont.getFontName();
          }

          public void clear() {
               if (this.textFont != null) {
                    this.textFont.dispose();
               }

          }
     }
}
