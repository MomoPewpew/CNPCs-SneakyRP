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
     public static final PropertyInteger ROTATION = PropertyInteger.func_177719_a("rotation", 0, 3);

     public BlockBuilder() {
          super(Material.field_151576_e);
          this.func_149672_a(SoundType.field_185851_d);
     }

     public int func_176201_c(IBlockState state) {
          return (Integer)state.func_177229_b(ROTATION);
     }

     public IBlockState func_176203_a(int meta) {
          return this.func_176223_P().func_177226_a(ROTATION, meta);
     }

     protected BlockStateContainer func_180661_e() {
          return new BlockStateContainer(this, new IProperty[]{ROTATION});
     }

     public EnumBlockRenderType func_149645_b(IBlockState state) {
          return EnumBlockRenderType.MODEL;
     }

     public boolean func_180639_a(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (par1World.field_72995_K) {
               return true;
          } else {
               ItemStack currentItem = player.field_71071_by.func_70448_g();
               if (currentItem.func_77973_b() == CustomItems.wand || currentItem.func_77973_b() == Item.func_150898_a(CustomItems.builder)) {
                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.BuilderBlock, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
               }

               return true;
          }
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int var6 = MathHelper.func_76128_c((double)(entity.field_70177_z / 90.0F) + 0.5D) & 3;
          world.func_180501_a(pos, state.func_177226_a(ROTATION, var6), 2);
          if (entity instanceof EntityPlayer && !world.field_72995_K) {
               NoppesUtilServer.sendOpenGui((EntityPlayer)entity, EnumGuiType.BuilderBlock, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
          }

     }

     public TileEntity func_149915_a(World var1, int var2) {
          return new TileBuilder();
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.SchematicsSet || e == EnumPacketServer.SchematicsTile || e == EnumPacketServer.SchematicsTileSave || e == EnumPacketServer.SchematicsBuild;
     }

     public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
          if (TileBuilder.DrawPos != null && TileBuilder.DrawPos.equals(pos)) {
               TileBuilder.SetDrawPos((BlockPos)null);
          }

     }
}
