package noppes.npcs.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockWaypoint extends BlockInterface implements IPermission {
     public BlockWaypoint() {
          super(Material.IRON);
          this.setSoundType(SoundType.METAL);
          this.setCreativeTab(CustomItems.tab);
     }

     public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (par1World.isRemote) {
               return false;
          } else {
               ItemStack currentItem = player.inventory.getCurrentItem();
               if (currentItem != null && currentItem.getItem() == CustomItems.wand && CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.EDIT_BLOCKS)) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.Waypoint, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
                    return true;
               } else {
                    return false;
               }
          }
     }

     public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          if (entity instanceof EntityPlayer && !world.isRemote) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.Waypoint, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
          }

     }

     public TileEntity createNewTileEntity(World var1, int var2) {
          return new TileWaypoint();
     }

     public EnumBlockRenderType getRenderType(IBlockState state) {
          return EnumBlockRenderType.MODEL;
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SaveTileEntity;
     }
}
