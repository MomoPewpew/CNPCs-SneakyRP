package noppes.npcs.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockBorder extends BlockInterface implements IPermission {
     public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

     public BlockBorder() {
          super(Material.ROCK);
          this.setSoundType(SoundType.STONE);
          this.setBlockUnbreakable();
     }

     public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          ItemStack currentItem = player.inventory.getCurrentItem();
          if (!world.isRemote && currentItem != null && currentItem.getItem() == CustomItems.wand) {
               NoppesUtilServer.sendOpenGui(player, EnumGuiType.Border, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
               return true;
          } else {
               return false;
          }
     }

     public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int l = MathHelper.floor((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
          l %= 4;
          world.setBlockState(pos, state.withProperty(ROTATION, l));
          TileBorder tile = (TileBorder)world.getTileEntity(pos);
          TileBorder adjacent = this.getTile(world, pos.west());
          if (adjacent == null) {
               adjacent = this.getTile(world, pos.south());
          }

          if (adjacent == null) {
               adjacent = this.getTile(world, pos.north());
          }

          if (adjacent == null) {
               adjacent = this.getTile(world, pos.east());
          }

          if (adjacent != null) {
               NBTTagCompound compound = new NBTTagCompound();
               adjacent.writeExtraNBT(compound);
               tile.readExtraNBT(compound);
          }

          tile.rotation = l;
          if (entity instanceof EntityPlayer && !world.isRemote) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.Border, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
          }

     }

     private TileBorder getTile(World world, BlockPos pos) {
          TileEntity tile = world.getTileEntity(pos);
          return tile != null && tile instanceof TileBorder ? (TileBorder)tile : null;
     }

     public EnumBlockRenderType getRenderType(IBlockState state) {
          return EnumBlockRenderType.MODEL;
     }

     public boolean isOpaqueCube(IBlockState state) {
          return false;
     }

     public boolean isFullCube(IBlockState state) {
          return false;
     }

     public TileEntity createNewTileEntity(World var1, int var2) {
          return new TileBorder();
     }

     protected BlockStateContainer createBlockState() {
          return new BlockStateContainer(this, new IProperty[]{ROTATION});
     }

     public int getMetaFromState(IBlockState state) {
          return (Integer)state.getValue(ROTATION);
     }

     public IBlockState getStateFromMeta(int meta) {
          return this.getDefaultState().withProperty(ROTATION, meta);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SaveTileEntity;
     }
}
