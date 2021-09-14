package noppes.npcs.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class BlockBuilder extends BlockInterface implements IPermission {
     public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

     public BlockBuilder() {
          super(Material.ROCK);
          this.setSoundType(SoundType.STONE);
     }

     public int getMetaFromState(IBlockState state) {
          return (Integer)state.getValue(ROTATION);
     }

     public IBlockState getStateFromMeta(int meta) {
          return this.getDefaultState().withProperty(ROTATION, meta);
     }

     protected BlockStateContainer createBlockState() {
          return new BlockStateContainer(this, new IProperty[]{ROTATION});
     }

     public EnumBlockRenderType getRenderType(IBlockState state) {
          return EnumBlockRenderType.MODEL;
     }

     public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (par1World.isRemote) {
               return true;
          } else {
               ItemStack currentItem = player.inventory.getCurrentItem();
               if (currentItem.getItem() == CustomItems.wand || currentItem.getItem() == Item.getItemFromBlock(CustomItems.builder)) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.BuilderBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
               }

               return true;
          }
     }

     public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int var6 = MathHelper.floor((double)(entity.rotationYaw / 90.0F) + 0.5D) & 3;
          world.setBlockState(pos, state.withProperty(ROTATION, var6), 2);
          if (entity instanceof EntityPlayer && !world.isRemote) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.BuilderBlock, (EntityNPCInterface)null, pos.getX(), pos.getY(), pos.getZ());
          }

     }

     public TileEntity createNewTileEntity(World var1, int var2) {
          return new TileBuilder();
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SchematicsSet || e == EnumPacketServer.SchematicsTile || e == EnumPacketServer.SchematicsTileSave || e == EnumPacketServer.SchematicsBuild;
     }

     public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
          if (TileBuilder.DrawPos != null && TileBuilder.DrawPos.equals(pos)) {
               TileBuilder.SetDrawPos((BlockPos)null);
          }

     }
}
