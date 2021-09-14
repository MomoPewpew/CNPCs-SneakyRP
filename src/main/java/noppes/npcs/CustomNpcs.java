package noppes.npcs;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockVine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import nikedemos.markovnames.generators.MarkovAncientGreek;
import nikedemos.markovnames.generators.MarkovAztec;
import nikedemos.markovnames.generators.MarkovCustomNPCsClassic;
import nikedemos.markovnames.generators.MarkovGenerator;
import nikedemos.markovnames.generators.MarkovJapanese;
import nikedemos.markovnames.generators.MarkovOldNorse;
import nikedemos.markovnames.generators.MarkovRoman;
import nikedemos.markovnames.generators.MarkovSaami;
import nikedemos.markovnames.generators.MarkovSlavic;
import nikedemos.markovnames.generators.MarkovSpanish;
import nikedemos.markovnames.generators.MarkovWelsh;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.api.wrapper.WrapperEntityData;
import noppes.npcs.api.wrapper.WrapperNpcAPI;
import noppes.npcs.command.CommandNoppes;
import noppes.npcs.config.ConfigLoader;
import noppes.npcs.config.ConfigProp;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.ChunkController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemScripted;

@Mod(
     modid = "customnpcs",
     name = "CustomNpcs",
     version = "1.12",
     acceptedMinecraftVersions = "1.12, 1.12.1, 1.12.2"
)
public class CustomNpcs {
     public static final String MODID = "customnpcs";
     @ConfigProp(
          info = "Whether scripting is enabled or not"
     )
     public static boolean EnableScripting = true;
     @ConfigProp(
          info = "Arguments given to the Nashorn scripting library"
     )
     public static String NashorArguments = "-strict";
     @ConfigProp(
          info = "Disable Chat Bubbles"
     )
     public static boolean EnableChatBubbles = true;
     @ConfigProp(
          info = "Navigation search range for NPCs. Not recommended to increase if you have a slow pc or on a server"
     )
     public static int NpcNavRange = 32;
     @ConfigProp(
          info = "Set to true if you want the dialog command option to be able to use op commands like tp etc"
     )
     public static boolean NpcUseOpCommands = false;
     @ConfigProp
     public static boolean InventoryGuiEnabled = true;
     @ConfigProp
     public static boolean FixUpdateFromPre_1_12 = false;
     @ConfigProp(
          info = "If you are running sponge and you want to disable the permissions set this to true"
     )
     public static boolean DisablePermissions = false;
     @ConfigProp
     public static boolean SceneButtonsEnabled = true;
     @ConfigProp
     public static boolean EnableDefaultEyes = true;
     public static long ticks;
     @SidedProxy(
          clientSide = "noppes.npcs.client.ClientProxy",
          serverSide = "noppes.npcs.CommonProxy"
     )
     public static CommonProxy proxy;
     @ConfigProp(
          info = "Enables CustomNpcs startup update message"
     )
     public static boolean EnableUpdateChecker = true;
     public static CustomNpcs instance;
     public static boolean FreezeNPCs = false;
     @ConfigProp(
          info = "Only ops can create and edit npcs"
     )
     public static boolean OpsOnly = false;
     @ConfigProp(
          info = "Default interact line. Leave empty to not have one"
     )
     public static String DefaultInteractLine = "Hello @p";
     @ConfigProp(
          info = "Number of chunk loading npcs that can be active at the same time"
     )
     public static int ChuckLoaders = 20;
     public static File Dir;
     @ConfigProp(
          info = "Enables leaves decay"
     )
     public static boolean LeavesDecayEnabled = true;
     @ConfigProp(
          info = "Enables Vine Growth"
     )
     public static boolean VineGrowthEnabled = true;
     @ConfigProp(
          info = "Enables Ice Melting"
     )
     public static boolean IceMeltsEnabled = true;
     @ConfigProp(
          info = "Normal players can use soulstone on animals"
     )
     public static boolean SoulStoneAnimals = true;
     @ConfigProp(
          info = "Normal players can use soulstone on all npcs"
     )
     public static boolean SoulStoneNPCs = false;
     @ConfigProp(
          info = "Type 0 = Normal, Type 1 = Solid"
     )
     public static int HeadWearType = 1;
     @ConfigProp(
          info = "When set to Minecraft it will use minecrafts font, when Default it will use OpenSans. Can only use fonts installed on your PC"
     )
     public static String FontType = "Default";
     @ConfigProp(
          info = "Font size for custom fonts (doesn't work with minecrafts font)"
     )
     public static int FontSize = 18;
     @ConfigProp
     public static boolean NpcSpeachTriggersChatEvent = false;
     public static FMLEventChannel Channel;
     public static FMLEventChannel ChannelPlayer;
     public static ConfigLoader Config;
     public static CommandNoppes NoppesCommand = new CommandNoppes();
     public static boolean VerboseDebug = false;
     public static MinecraftServer Server;
     public static final MarkovGenerator[] MARKOV_GENERATOR = new MarkovGenerator[10];

     public CustomNpcs() {
          instance = this;
     }

     @EventHandler
     public void load(FMLPreInitializationEvent ev) {
          Channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCs");
          ChannelPlayer = NetworkRegistry.INSTANCE.newEventDrivenChannel("CustomNPCsPlayer");
          Dir = new File(new File(ev.getModConfigurationDirectory(), ".."), "customnpcs");
          Dir.mkdir();
          Config = new ConfigLoader(this.getClass(), ev.getModConfigurationDirectory(), "CustomNpcs");
          Config.loadConfig();
          if (NpcNavRange < 16) {
               NpcNavRange = 16;
          }

          CustomItems.load();
          CapabilityManager.INSTANCE.register(PlayerData.class, new IStorage() {
               public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side) {
                    return null;
               }

               public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt) {
               }
          }, PlayerData.class);
          CapabilityManager.INSTANCE.register(WrapperEntityData.class, new IStorage() {
               public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side) {
                    return null;
               }

               public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt) {
               }
          }, WrapperEntityData.class);
          CapabilityManager.INSTANCE.register(MarkData.class, new IStorage() {
               public NBTBase writeNBT(Capability capability, Object instance, EnumFacing side) {
                    return null;
               }

               public void readNBT(Capability capability, Object instance, EnumFacing side, NBTBase nbt) {
               }
          }, MarkData.class);
          CapabilityManager.INSTANCE.register(ItemStackWrapper.class, new IStorage() {
               public NBTBase writeNBT(Capability capability, ItemStackWrapper instance, EnumFacing side) {
                    return null;
               }

               public void readNBT(Capability capability, ItemStackWrapper instance, EnumFacing side, NBTBase nbt) {
               }

				@Override
				public void readNBT(Capability arg0, Object arg1, EnumFacing arg2, NBTBase arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public NBTBase writeNBT(Capability arg0, Object arg1, EnumFacing arg2) {
					// TODO Auto-generated method stub
					return null;
				}
          }, () -> {
               return null;
          });
          NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
          MinecraftForge.EVENT_BUS.register(new ServerEventsHandler());
          MinecraftForge.EVENT_BUS.register(new ServerTickHandler());
          MinecraftForge.EVENT_BUS.register(new CustomEntities());
          MinecraftForge.EVENT_BUS.register(proxy);
          NpcAPI.Instance().events().register(new AbilityEventHandler());
          ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkController());
          proxy.load();
          ObfuscationReflectionHelper.setPrivateValue(RangedAttribute.class, (RangedAttribute)SharedMonsterAttributes.MAX_HEALTH, Double.MAX_VALUE, 1);
     }

     @EventHandler
     public void load(FMLInitializationEvent ev) {
          PixelmonHelper.load();
          ScriptController controller = new ScriptController();
          if (EnableScripting && controller.languages.size() > 0) {
               MinecraftForge.EVENT_BUS.register(controller);
               MinecraftForge.EVENT_BUS.register((new ScriptPlayerEventHandler()).registerForgeEvents());
               MinecraftForge.EVENT_BUS.register(new ScriptItemEventHandler());
          }

          ForgeModContainer.fullBoundingBoxLadders = true;
          new RecipeController();
          proxy.postload();
          new CustomNpcsPermissions();
          MARKOV_GENERATOR[0] = new MarkovRoman(3);
          MARKOV_GENERATOR[1] = new MarkovJapanese(4);
          MARKOV_GENERATOR[2] = new MarkovSlavic(3);
          MARKOV_GENERATOR[3] = new MarkovWelsh(3);
          MARKOV_GENERATOR[4] = new MarkovSaami(3);
          MARKOV_GENERATOR[5] = new MarkovOldNorse(4);
          MARKOV_GENERATOR[6] = new MarkovAncientGreek(3);
          MARKOV_GENERATOR[7] = new MarkovAztec(3);
          MARKOV_GENERATOR[8] = new MarkovCustomNPCsClassic(3);
          MARKOV_GENERATOR[9] = new MarkovSpanish(3);
          System.out.println("Markov dictionaries loaded!");
     }

     @EventHandler
     public void setAboutToStart(FMLServerAboutToStartEvent event) {
          Server = event.getServer();
          ChunkController.instance.clear();
          FactionController.instance.load();
          new PlayerDataController();
          new TransportController();
          new GlobalDataController();
          new SpawnController();
          new LinkedNpcController();
          new MassBlockController();
          ScriptController.Instance.loadCategories();
          ScriptController.Instance.loadStoredData();
          ScriptController.Instance.loadPlayerScripts();
          ScriptController.Instance.loadForgeScripts();
          ScriptController.HasStart = false;
          WrapperNpcAPI.clearCache();
          Set names = Block.REGISTRY.getKeys();
          Iterator var3 = names.iterator();

          while(var3.hasNext()) {
               ResourceLocation name = (ResourceLocation)var3.next();
               Block block = (Block)Block.REGISTRY.getObject(name);
               if (block instanceof BlockLeaves) {
                    block.setTickRandomly(LeavesDecayEnabled);
               }

               if (block instanceof BlockVine) {
                    block.setTickRandomly(VineGrowthEnabled);
               }

               if (block instanceof BlockIce) {
                    block.setTickRandomly(IceMeltsEnabled);
               }
          }

     }

     @EventHandler
     public void started(FMLServerStartedEvent event) {
          RecipeController.instance.load();
          new BankController();
          DialogController.instance.load();
          QuestController.instance.load();
          ScriptController.HasStart = true;
          ServerCloneController.Instance = new ServerCloneController();
     }

     @EventHandler
     public void stopped(FMLServerStoppedEvent event) {
          ServerCloneController.Instance = null;
          Server = null;
          ItemScripted.Resources.clear();
     }

     @EventHandler
     public void serverstart(FMLServerStartingEvent event) {
          event.registerServerCommand(NoppesCommand);
          EntityNPCInterface.ChatEventPlayer = new FakePlayer(event.getServer().getWorld(0), EntityNPCInterface.ChatEventProfile);
          EntityNPCInterface.CommandPlayer = new FakePlayer(event.getServer().getWorld(0), EntityNPCInterface.CommandProfile);
          EntityNPCInterface.GenericPlayer = new FakePlayer(event.getServer().getWorld(0), EntityNPCInterface.GenericProfile);
     }

     public static File getWorldSaveDirectory() {
          return getWorldSaveDirectory((String)null);
     }

     public static File getWorldSaveDirectory(String s) {
          try {
               File dir = new File(".");
               if (Server != null) {
                    if (!Server.isDedicatedServer()) {
                         dir = new File(Minecraft.getMinecraft().mcDataDir, "saves");
                    }

                    dir = new File(new File(dir, Server.getFolderName()), "customnpcs");
               }

               if (s != null) {
                    dir = new File(dir, s);
               }

               if (!dir.exists()) {
                    dir.mkdirs();
               }

               return dir;
          } catch (Exception var2) {
               LogWriter.error("Error getting worldsave", var2);
               return null;
          }
     }
}
