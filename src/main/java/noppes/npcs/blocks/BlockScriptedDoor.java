package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockScriptedDoor extends BlockNpcDoorInterface implements IPermission {
     public TileEntity func_149915_a(World worldIn, int meta) {
          return new TileScriptedDoor();
     }

     public EnumBlockRenderType func_149645_b(IBlockState state) {
          return EnumBlockRenderType.INVISIBLE;
     }

     public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (world.isRemote) {
               return true;
          } else {
               BlockPos blockpos1 = state.getValue(field_176523_O) == EnumDoorHalf.LOWER ? pos : pos.down();
               IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
               if (iblockstate1.getBlock() != this) {
                    return false;
               } else {
                    ItemStack currentItem = player.inventory.getCurrentItem();
                    if (currentItem != null && (currentItem.getItem() == CustomItems.wand || currentItem.getItem() == CustomItems.scripter || currentItem.getItem() == CustomItems.scriptedDoorTool)) {
                         NoppesUtilServer.sendOpenGui(player, EnumGuiType.ScriptDoor, (EntityNPCInterface)null, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                         return true;
                    } else {
                         TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(blockpos1);
                         if (EventHooks.onScriptBlockInteract(tile, player, side.func_176745_a(), hitX, hitY, hitZ)) {
                              return false;
                         } else {
                              this.func_176512_a(world, blockpos1, ((Boolean)iblockstate1.getValue(BlockDoor.field_176519_b)).equals(false));
                              return true;
                         }
                    }
               }
          }
     }

     public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block neighborBlock, BlockPos pos2) {
          BlockPos blockpos2;
          IBlockState iblockstate2;
          if (state.getValue(field_176523_O) == EnumDoorHalf.UPPER) {
               blockpos2 = pos.down();
               iblockstate2 = worldIn.getBlockState(blockpos2);
               if (iblockstate2.getBlock() != this) {
                    worldIn.func_175698_g(pos);
               } else if (neighborBlock != this) {
                    this.func_189540_a(iblockstate2, worldIn, blockpos2, neighborBlock, blockpos2);
               }
          } else {
               blockpos2 = pos.up();
               iblockstate2 = worldIn.getBlockState(blockpos2);
               if (iblockstate2.getBlock() != this) {
                    worldIn.func_175698_g(pos);
               } else {
                    TileScriptedDoor tile = (TileScriptedDoor)worldIn.getTileEntity(pos);
                    if (!worldIn.isRemote) {
                         EventHooks.onScriptBlockNeighborChanged(tile, pos2);
                    }

                    boolean flag = worldIn.func_175640_z(pos) || worldIn.func_175640_z(blockpos2);
                    if ((flag || neighborBlock.getDefaultState().func_185897_m()) && neighborBlock != this && flag != (Boolean)iblockstate2.getValue(field_176522_N)) {
                         worldIn.func_180501_a(blockpos2, iblockstate2.func_177226_a(field_176522_N, flag), 2);
                         if (flag != (Boolean)state.getValue(field_176519_b)) {
                              this.func_176512_a(worldIn, pos, flag);
                         }
                    }

                    int power = 0;
                    EnumFacing[] var11 = EnumFacing.values();
                    int var12 = var11.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                         EnumFacing enumfacing = var11[var13];
                         int p = worldIn.func_175651_c(pos.func_177972_a(enumfacing), enumfacing);
                         if (p > power) {
                              power = p;
                         }
                    }

                    tile.newPower = power;
               }
          }

     }

     public void func_176512_a(World worldIn, BlockPos pos, boolean open) {
          TileScriptedDoor tile = (TileScriptedDoor)worldIn.getTileEntity(pos);
          if (!EventHooks.onScriptBlockDoorToggle(tile)) {
               super.func_176512_a(worldIn, pos, open);
          }
     }

     public void func_180649_a(World world, BlockPos pos, EntityPlayer playerIn) {
          if (!world.isRemote) {
               IBlockState state = world.getBlockState(pos);
               BlockPos blockpos1 = state.getValue(field_176523_O) == EnumDoorHalf.LOWER ? pos : pos.down();
               IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
               if (iblockstate1.getBlock() == this) {
                    TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(blockpos1);
                    EventHooks.onScriptBlockClicked(tile, playerIn);
               }
          }
     }

     public void func_180663_b(World world, BlockPos pos, IBlockState state) {
          BlockPos blockpos1 = state.getValue(field_176523_O) == EnumDoorHalf.LOWER ? pos : pos.down();
          IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
          if (!world.isRemote && iblockstate1.getBlock() == this) {
               TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(pos);
               EventHooks.onScriptBlockBreak(tile);
          }

          super.func_180663_b(world, pos, state);
     }

     public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
          if (!world.isRemote) {
               TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(pos);
               if (EventHooks.onScriptBlockHarvest(tile, player)) {
                    return false;
               }
          }

          return super.removedByPlayer(state, world, pos, player, willHarvest);
     }

     public void func_180634_a(World world, BlockPos pos, IBlockState state, Entity entityIn) {
          if (!world.isRemote) {
               TileScriptedDoor tile = (TileScriptedDoor)world.getTileEntity(pos);
               EventHooks.onScriptBlockCollide(tile, entityIn);
          }
     }

     public void func_176208_a(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
          BlockPos blockpos1 = state.getValue(field_176523_O) == EnumDoorHalf.LOWER ? pos : pos.down();
          IBlockState iblockstate1 = pos.equals(blockpos1) ? state : world.getBlockState(blockpos1);
          if (player.field_71075_bZ.field_75098_d && iblockstate1.getValue(field_176523_O) == EnumDoorHalf.LOWER && iblockstate1.getBlock() == this) {
               world.func_175698_g(blockpos1);
          }

     }

     public float func_176195_g(IBlockState state, World world, BlockPos pos) {
          return ((TileScriptedDoor)world.getTileEntity(pos)).blockHardness;
     }

     public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
          return ((TileScriptedDoor)world.getTileEntity(pos)).blockResistance;
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.ScriptDoorDataSave;
     }
}
