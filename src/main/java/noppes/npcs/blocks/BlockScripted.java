package noppes.npcs.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockScripted extends BlockInterface implements IPermission {
     public static final AxisAlignedBB AABB = new AxisAlignedBB(0.0010000000474974513D, 0.0010000000474974513D, 0.0010000000474974513D, 0.9980000257492065D, 0.9980000257492065D, 0.9980000257492065D);
     public static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

     public BlockScripted() {
          super(Material.field_151576_e);
          this.func_149672_a(SoundType.field_185851_d);
     }

     public TileEntity func_149915_a(World worldIn, int meta) {
          return new TileScripted();
     }

     public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess world, BlockPos pos) {
          return AABB;
     }

     public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess world, BlockPos pos) {
          TileScripted tile = (TileScripted)world.func_175625_s(pos);
          return tile != null && tile.isPassible ? AABB_EMPTY : AABB;
     }

     public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (world.isRemote) {
               return true;
          } else {
               ItemStack currentItem = player.inventory.func_70448_g();
               if (currentItem == null || currentItem.func_77973_b() != CustomItems.wand && currentItem.func_77973_b() != CustomItems.scripter) {
                    TileScripted tile = (TileScripted)world.func_175625_s(pos);
                    return !EventHooks.onScriptBlockInteract(tile, player, side.func_176745_a(), hitX, hitY, hitZ);
               } else {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
                    return true;
               }
          }
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          if (entity instanceof EntityPlayer && !world.isRemote) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.ScriptBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
          }

     }

     public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entityIn) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               EventHooks.onScriptBlockCollide(tile, entityIn);
          }
     }

     public void func_176224_k(World world, BlockPos pos) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               EventHooks.onScriptBlockRainFill(tile);
          }
     }

     public void func_180658_a(World world, BlockPos pos, Entity entity, float fallDistance) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               fallDistance = EventHooks.onScriptBlockFallenUpon(tile, entity, fallDistance);
               super.func_180658_a(world, pos, entity, fallDistance);
          }
     }

     public boolean func_149662_c(IBlockState state) {
          return false;
     }

     public boolean func_149686_d(IBlockState state) {
          return false;
     }

     public void func_180649_a(World world, BlockPos pos, EntityPlayer player) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               EventHooks.onScriptBlockClicked(tile, player);
          }
     }

     public void func_180663_b(World world, BlockPos pos, IBlockState state) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               EventHooks.onScriptBlockBreak(tile);
          }

          super.func_180663_b(world, pos, state);
     }

     public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               if (EventHooks.onScriptBlockHarvest(tile, player)) {
                    return false;
               }
          }

          return super.removedByPlayer(state, world, pos, player, willHarvest);
     }

     public Item func_180660_a(IBlockState state, Random rand, int fortune) {
          return null;
     }

     public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               if (EventHooks.onScriptBlockExploded(tile)) {
                    return;
               }
          }

          super.onBlockExploded(world, pos, explosion);
     }

     public void func_189540_a(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos pos2) {
          if (!world.isRemote) {
               TileScripted tile = (TileScripted)world.func_175625_s(pos);
               EventHooks.onScriptBlockNeighborChanged(tile, pos2);
               int power = 0;
               EnumFacing[] var8 = EnumFacing.values();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                    EnumFacing enumfacing = var8[var10];
                    int p = world.func_175651_c(pos.func_177972_a(enumfacing), enumfacing);
                    if (p > power) {
                         power = p;
                    }
               }

               if (tile.prevPower != power && tile.powering <= 0) {
                    tile.newPower = power;
               }

          }
     }

     public boolean func_149744_f(IBlockState state) {
          return true;
     }

     public int func_180656_a(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
          return this.func_176211_b(state, worldIn, pos, side);
     }

     public int func_176211_b(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
          return ((TileScripted)world.func_175625_s(pos)).activePowering;
     }

     public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
          return ((TileScripted)world.func_175625_s(pos)).isLadder;
     }

     public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
          return true;
     }

     public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
          TileScripted tile = (TileScripted)world.func_175625_s(pos);
          return tile == null ? 0 : tile.lightValue;
     }

     public boolean func_176205_b(IBlockAccess world, BlockPos pos) {
          return ((TileScripted)world.func_175625_s(pos)).isPassible;
     }

     public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
          return super.canEntityDestroy(state, world, pos, entity);
     }

     public float getEnchantPowerBonus(World world, BlockPos pos) {
          return super.getEnchantPowerBonus(world, pos);
     }

     public float func_176195_g(IBlockState state, World world, BlockPos pos) {
          return ((TileScripted)world.func_175625_s(pos)).blockHardness;
     }

     public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
          return ((TileScripted)world.func_175625_s(pos)).blockResistance;
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SaveTileEntity || e == EnumPacketServer.ScriptBlockDataSave;
     }
}
