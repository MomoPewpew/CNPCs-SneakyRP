package noppes.npcs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap.Builder;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.blocks.BlockBorder;
import noppes.npcs.blocks.BlockBuilder;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockCopy;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.blocks.BlockWaypoint;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.blocks.tiles.TileDoor;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.blocks.tiles.TileMailbox2;
import noppes.npcs.blocks.tiles.TileMailbox3;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.client.renderer.blocks.BlockCarpentryBenchRenderer;
import noppes.npcs.client.renderer.blocks.BlockCopyRenderer;
import noppes.npcs.client.renderer.blocks.BlockDoorRenderer;
import noppes.npcs.client.renderer.blocks.BlockMailboxRenderer;
import noppes.npcs.client.renderer.blocks.BlockScriptedRenderer;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.items.ItemMounter;
import noppes.npcs.items.ItemNbtBook;
import noppes.npcs.items.ItemNpcBlock;
import noppes.npcs.items.ItemNpcCloner;
import noppes.npcs.items.ItemNpcMovingPath;
import noppes.npcs.items.ItemNpcScripter;
import noppes.npcs.items.ItemNpcWand;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.items.ItemScriptedDoor;
import noppes.npcs.items.ItemSoulstoneEmpty;
import noppes.npcs.items.ItemSoulstoneFilled;
import noppes.npcs.items.ItemTeleporter;

@ObjectHolder("customnpcs")
public class CustomItems {
     @ObjectHolder("npcwand")
     public static final Item wand = null;
     @ObjectHolder("npcmobcloner")
     public static final Item cloner = null;
     @ObjectHolder("npcscripter")
     public static final Item scripter = null;
     @ObjectHolder("npcmovingpath")
     public static final Item moving = null;
     @ObjectHolder("npcmounter")
     public static final Item mount = null;
     @ObjectHolder("npcteleporter")
     public static final Item teleporter = null;
     @ObjectHolder("npcscripteddoortool")
     public static final Item scriptedDoorTool = null;
     @ObjectHolder("scripted_item")
     public static final ItemScripted scripted_item = null;
     @ObjectHolder("nbt_book")
     public static final ItemNbtBook nbt_book = null;
     @ObjectHolder("npcsoulstoneempty")
     public static final Item soulstoneEmpty = null;
     @ObjectHolder("npcsoulstonefilled")
     public static final Item soulstoneFull = null;
     @ObjectHolder("npcredstoneblock")
     public static final Block redstoneBlock = null;
     @ObjectHolder("npcmailbox")
     public static final Block mailbox = null;
     @ObjectHolder("npcwaypoint")
     public static final Block waypoint = null;
     @ObjectHolder("npcborder")
     public static final Block border = null;
     @ObjectHolder("npcscripted")
     public static final Block scripted = null;
     @ObjectHolder("npcscripteddoor")
     public static final Block scriptedDoor = null;
     @ObjectHolder("npcbuilderblock")
     public static final Block builder = null;
     @ObjectHolder("npccopyblock")
     public static final Block copy = null;
     @ObjectHolder("npccarpentybench")
     public static final Block carpentyBench = null;
     public static CreativeTabNpcs tab = new CreativeTabNpcs("cnpcs");

     public static void load() {
          MinecraftForge.EVENT_BUS.register(new CustomItems());
     }

     @SubscribeEvent
     public void registerBlocks(Register event) {
          GameRegistry.registerTileEntity(TileRedstoneBlock.class, "TileRedstoneBlock");
          GameRegistry.registerTileEntity(TileBlockAnvil.class, "TileBlockAnvil");
          GameRegistry.registerTileEntity(TileMailbox.class, "TileMailbox");
          GameRegistry.registerTileEntity(TileWaypoint.class, "TileWaypoint");
          GameRegistry.registerTileEntity(TileScripted.class, "TileNPCScripted");
          GameRegistry.registerTileEntity(TileScriptedDoor.class, "TileNPCScriptedDoor");
          GameRegistry.registerTileEntity(TileBuilder.class, "TileNPCBuilder");
          GameRegistry.registerTileEntity(TileCopy.class, "TileNPCCopy");
          GameRegistry.registerTileEntity(TileBorder.class, "TileNPCBorder");
          Block redstoneBlock = (new BlockNpcRedstone()).func_149711_c(50.0F).func_149752_b(2000.0F).func_149663_c("npcredstoneblock").func_149647_a(tab);
          Block mailbox = (new BlockMailbox()).func_149663_c("npcmailbox").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          Block waypoint = (new BlockWaypoint()).func_149663_c("npcwaypoint").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          Block border = (new BlockBorder()).func_149663_c("npcborder").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          Block scripted = (new BlockScripted()).func_149663_c("npcscripted").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          Block scriptedDoor = (new BlockScriptedDoor()).func_149663_c("npcscripteddoor").func_149711_c(5.0F).func_149752_b(10.0F);
          Block builder = (new BlockBuilder()).func_149663_c("npcbuilderblock").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          Block copy = (new BlockCopy()).func_149663_c("npccopyblock").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          Block carpentyBench = (new BlockCarpentryBench()).func_149663_c("npccarpentybench").func_149711_c(5.0F).func_149752_b(10.0F).func_149647_a(tab);
          event.getRegistry().registerAll(new Block[]{redstoneBlock, carpentyBench, mailbox, waypoint, border, scripted, scriptedDoor, builder, copy});
     }

     @SubscribeEvent
     public void registerItems(Register event) {
          Item wand = (new ItemNpcWand()).func_77655_b("npcwand").func_77664_n();
          Item cloner = (new ItemNpcCloner()).func_77655_b("npcmobcloner").func_77664_n();
          Item scripter = (new ItemNpcScripter()).func_77655_b("npcscripter").func_77664_n();
          Item moving = (new ItemNpcMovingPath()).func_77655_b("npcmovingpath").func_77664_n();
          Item mount = (new ItemMounter()).func_77655_b("npcmounter").func_77664_n();
          Item teleporter = (new ItemTeleporter()).func_77655_b("npcteleporter").func_77664_n();
          Item scriptedDoorTool = (new ItemScriptedDoor(scriptedDoor)).func_77655_b("npcscripteddoortool").func_77664_n();
          Item soulstoneEmpty = (new ItemSoulstoneEmpty()).func_77655_b("npcsoulstoneempty").func_77637_a(tab);
          Item soulstoneFull = (new ItemSoulstoneFilled()).func_77655_b("npcsoulstonefilled");
          Item scripted_item = (new ItemScripted()).func_77655_b("scripted_item");
          Item nbt_book = (new ItemNbtBook()).func_77655_b("nbt_book");
          event.getRegistry().registerAll(new Item[]{wand, cloner, scripter, moving, mount, teleporter, scriptedDoorTool, soulstoneEmpty, soulstoneFull, scripted_item, nbt_book});
          event.getRegistry().registerAll(new Item[]{new ItemNpcBlock(redstoneBlock), new ItemNpcBlock(carpentyBench), (new ItemNpcBlock(mailbox)).func_77627_a(true), new ItemNpcBlock(waypoint), new ItemNpcBlock(border), new ItemNpcBlock(scripted), new ItemNpcBlock(scriptedDoor), new ItemNpcBlock(builder), new ItemNpcBlock(copy)});
          tab.item = wand;
          BlockDispenser.field_149943_a.func_82595_a(soulstoneFull, new BehaviorDefaultDispenseItem() {
               public ItemStack func_82487_b(IBlockSource source, ItemStack item) {
                    EnumFacing enumfacing = (EnumFacing)source.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
                    double x = source.func_82615_a() + (double)enumfacing.func_82601_c();
                    double z = source.func_82616_c() + (double)enumfacing.func_82599_e();
                    ItemSoulstoneFilled.Spawn((EntityPlayer)null, item, source.func_82618_k(), new BlockPos(x, source.func_82617_b(), z));
                    item.func_77979_a(1);
                    return item;
               }
          });
     }

     @SubscribeEvent
     public void registerRecipes(Register event) {
          RecipeController.Registry = event.getRegistry();
     }

     @SideOnly(Side.CLIENT)
     @SubscribeEvent
     public void registerModels(ModelRegistryEvent event) {
          ModelLoader.setCustomStateMapper(mailbox, (new Builder()).func_178442_a(new IProperty[]{BlockMailbox.ROTATION, BlockMailbox.TYPE}).func_178441_a());
          ModelLoader.setCustomStateMapper(scriptedDoor, (new Builder()).func_178442_a(new IProperty[]{BlockDoor.field_176522_N}).func_178441_a());
          ModelLoader.setCustomStateMapper(builder, (new Builder()).func_178442_a(new IProperty[]{BlockBuilder.ROTATION}).func_178441_a());
          ModelLoader.setCustomStateMapper(carpentyBench, (new Builder()).func_178442_a(new IProperty[]{BlockCarpentryBench.ROTATION}).func_178441_a());
          ModelLoader.setCustomModelResourceLocation(wand, 0, new ModelResourceLocation("customnpcs:npcwand", "inventory"));
          ModelLoader.setCustomModelResourceLocation(cloner, 0, new ModelResourceLocation("customnpcs:npcmobcloner", "inventory"));
          ModelLoader.setCustomModelResourceLocation(scripter, 0, new ModelResourceLocation("customnpcs:npcscripter", "inventory"));
          ModelLoader.setCustomModelResourceLocation(moving, 0, new ModelResourceLocation("customnpcs:npcmovingpath", "inventory"));
          ModelLoader.setCustomModelResourceLocation(mount, 0, new ModelResourceLocation("customnpcs:npcmounter", "inventory"));
          ModelLoader.setCustomModelResourceLocation(teleporter, 0, new ModelResourceLocation("customnpcs:npcteleporter", "inventory"));
          ModelLoader.setCustomModelResourceLocation(scriptedDoorTool, 0, new ModelResourceLocation("customnpcs:npcscripteddoortool", "inventory"));
          ModelLoader.setCustomModelResourceLocation(soulstoneEmpty, 0, new ModelResourceLocation("customnpcs:npcsoulstoneempty", "inventory"));
          ModelLoader.setCustomModelResourceLocation(soulstoneFull, 0, new ModelResourceLocation("customnpcs:npcsoulstonefilled", "inventory"));
          ModelLoader.setCustomModelResourceLocation(scripted_item, 0, new ModelResourceLocation("customnpcs:scripted_item", "inventory"));
          ModelLoader.setCustomModelResourceLocation(nbt_book, 0, new ModelResourceLocation("customnpcs:nbt_book", "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(redstoneBlock), 0, new ModelResourceLocation(redstoneBlock.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(mailbox), 0, new ModelResourceLocation(mailbox.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(mailbox), 1, new ModelResourceLocation(mailbox.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(mailbox), 2, new ModelResourceLocation(mailbox.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(waypoint), 0, new ModelResourceLocation(waypoint.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(border), 0, new ModelResourceLocation(border.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(scripted), 0, new ModelResourceLocation(scripted.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(scriptedDoor), 0, new ModelResourceLocation(scriptedDoor.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(builder), 0, new ModelResourceLocation(builder.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(copy), 0, new ModelResourceLocation(copy.getRegistryName(), "inventory"));
          ModelLoader.setCustomModelResourceLocation(Item.func_150898_a(carpentyBench), 0, new ModelResourceLocation(carpentyBench.getRegistryName(), "inventory"));
          ClientRegistry.bindTileEntitySpecialRenderer(TileBlockAnvil.class, new BlockCarpentryBenchRenderer());
          ClientRegistry.bindTileEntitySpecialRenderer(TileMailbox.class, new BlockMailboxRenderer(0));
          ClientRegistry.bindTileEntitySpecialRenderer(TileMailbox2.class, new BlockMailboxRenderer(1));
          ClientRegistry.bindTileEntitySpecialRenderer(TileMailbox3.class, new BlockMailboxRenderer(2));
          ClientRegistry.bindTileEntitySpecialRenderer(TileScripted.class, new BlockScriptedRenderer());
          ClientRegistry.bindTileEntitySpecialRenderer(TileDoor.class, new BlockDoorRenderer());
          ClientRegistry.bindTileEntitySpecialRenderer(TileCopy.class, new BlockCopyRenderer());
          ForgeHooksClient.registerTESRItemStack(Item.func_150898_a(carpentyBench), 0, TileBlockAnvil.class);
          ForgeHooksClient.registerTESRItemStack(Item.func_150898_a(mailbox), 0, TileMailbox.class);
          ForgeHooksClient.registerTESRItemStack(Item.func_150898_a(mailbox), 1, TileMailbox2.class);
          ForgeHooksClient.registerTESRItemStack(Item.func_150898_a(mailbox), 2, TileMailbox3.class);
     }
}
