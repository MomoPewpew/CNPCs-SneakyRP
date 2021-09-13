package noppes.npcs.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockCopy extends BlockInterface implements IPermission {
     public BlockCopy() {
          super(Material.field_151576_e);
          this.func_149672_a(SoundType.field_185851_d);
     }

     public boolean func_180639_a(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (par1World.isRemote) {
               return true;
          } else {
               ItemStack currentItem = player.inventory.func_70448_g();
               if (currentItem != null && currentItem.func_77973_b() == CustomItems.wand) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.CopyBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
               }

               return true;
          }
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          if (entity instanceof EntityPlayer && !world.isRemote) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.CopyBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
          }

     }

     public boolean func_149662_c(IBlockState state) {
          return false;
     }

     public boolean func_149686_d(IBlockState state) {
          return false;
     }

     public TileEntity func_149915_a(World var1, int var2) {
          return new TileCopy();
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.GetTileEntity || e == EnumPacketServer.SchematicStore || e == EnumPacketServer.SchematicsTile || e == EnumPacketServer.SchematicsTileSave || e == EnumPacketServer.SaveTileEntity;
     }
}
