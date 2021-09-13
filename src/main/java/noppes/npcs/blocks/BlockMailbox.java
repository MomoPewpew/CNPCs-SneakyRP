package noppes.npcs.blocks;

import java.util.ArrayList;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.Server;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;

public class BlockMailbox extends BlockInterface {
     public static final PropertyInteger ROTATION = PropertyInteger.func_177719_a("rotation", 0, 3);
     public static final PropertyInteger TYPE = PropertyInteger.func_177719_a("type", 0, 2);

     public BlockMailbox() {
          super(Material.field_151573_f);
          this.func_149672_a(SoundType.field_185852_e);
     }

     public void func_149666_a(CreativeTabs par2CreativeTabs, NonNullList par3List) {
          par3List.add(new ItemStack(this, 1, 0));
          par3List.add(new ItemStack(this, 1, 1));
          par3List.add(new ItemStack(this, 1, 2));
     }

     public boolean func_180639_a(World par1World, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (!par1World.isRemote) {
               Server.sendData((EntityPlayerMP)player, EnumPacketClient.GUI, EnumGuiType.PlayerMailbox, pos.getX(), pos.getY(), pos.getZ());
          }

          return true;
     }

     public ArrayList getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
          ArrayList ret = new ArrayList();
          int damage = (Integer)state.getValue(TYPE);
          ret.add(new ItemStack(this, 1, damage));
          return ret;
     }

     public int func_180651_a(IBlockState state) {
          return (Integer)state.getValue(TYPE);
     }

     public int func_176201_c(IBlockState state) {
          return (Integer)state.getValue(ROTATION) | (Integer)state.getValue(TYPE) << 2;
     }

     public IBlockState func_176203_a(int meta) {
          return this.getDefaultState().func_177226_a(TYPE, (Integer.valueOf(meta) >> 2) % 3).func_177226_a(ROTATION, (meta | 4) % 4);
     }

     protected BlockStateContainer func_180661_e() {
          return new BlockStateContainer(this, new IProperty[]{TYPE, ROTATION});
     }

     public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
          int l = MathHelper.floor((double)(entity.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
          world.func_180501_a(pos, state.func_177226_a(TYPE, stack.getItemDamage()).func_177226_a(ROTATION, l % 4), 2);
     }

     public boolean func_149662_c(IBlockState state) {
          return false;
     }

     public boolean func_149686_d(IBlockState state) {
          return false;
     }

     public TileEntity func_149915_a(World var1, int var2) {
          return new TileMailbox();
     }
}
