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
     public static final PropertyInteger ROTATION = PropertyInteger.func_177719_a("rotation", 0, 3);

     public BlockBorder() {
          super(Material.field_151576_e);
          this.func_149672_a(SoundType.field_185851_d);
          this.func_149722_s();
     }

     public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          ItemStack currentItem = player.inventory.func_70448_g();
          if (!world.field_72995_K && currentItem != null && currentItem.func_77973_b() == CustomItems.wand) {
               NoppesUtilServer.sendOpenGui(player, EnumGuiType.Border, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
               return true;
          } else {
               return false;
          }
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int l = MathHelper.func_76128_c((double)(entity.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
          l %= 4;
          world.func_175656_a(pos, state.func_177226_a(ROTATION, l));
          TileBorder tile = (TileBorder)world.func_175625_s(pos);
          TileBorder adjacent = this.getTile(world, pos.func_177976_e());
          if (adjacent == null) {
               adjacent = this.getTile(world, pos.func_177968_d());
          }

          if (adjacent == null) {
               adjacent = this.getTile(world, pos.func_177978_c());
          }

          if (adjacent == null) {
               adjacent = this.getTile(world, pos.func_177974_f());
          }

          if (adjacent != null) {
               NBTTagCompound compound = new NBTTagCompound();
               adjacent.writeExtraNBT(compound);
               tile.readExtraNBT(compound);
          }

          tile.rotation = l;
          if (entity instanceof EntityPlayer && !world.field_72995_K) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.Border, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
          }

     }

     private TileBorder getTile(World world, BlockPos pos) {
          TileEntity tile = world.func_175625_s(pos);
          return tile != null && tile instanceof TileBorder ? (TileBorder)tile : null;
     }

     public EnumBlockRenderType func_149645_b(IBlockState state) {
          return EnumBlockRenderType.MODEL;
     }

     public boolean func_149662_c(IBlockState state) {
          return false;
     }

     public boolean func_149686_d(IBlockState state) {
          return false;
     }

     public TileEntity func_149915_a(World var1, int var2) {
          return new TileBorder();
     }

     protected BlockStateContainer func_180661_e() {
          return new BlockStateContainer(this, new IProperty[]{ROTATION});
     }

     public int func_176201_c(IBlockState state) {
          return (Integer)state.func_177229_b(ROTATION);
     }

     public IBlockState func_176203_a(int meta) {
          return this.func_176223_P().func_177226_a(ROTATION, meta);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SaveTileEntity;
     }
}
