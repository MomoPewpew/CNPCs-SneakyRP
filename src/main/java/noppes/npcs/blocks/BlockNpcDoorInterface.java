package noppes.npcs.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileDoor;

public abstract class BlockNpcDoorInterface extends BlockDoor implements ITileEntityProvider {
	public BlockNpcDoorInterface() {
		super(Material.WOOD);
		this.hasTileEntity = true;
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileDoor();
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(CustomItems.scriptedDoorTool, 1, this.damageDropped(state));
	}

	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState iblockstate1;
		if (state.getValue(HALF) == EnumDoorHalf.LOWER) {
			iblockstate1 = worldIn.getBlockState(pos.up());
			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(HINGE, iblockstate1.getValue(HINGE)).withProperty(POWERED,
						iblockstate1.getValue(POWERED));
			}
		} else {
			iblockstate1 = worldIn.getBlockState(pos.down());
			if (iblockstate1.getBlock() == this) {
				state = state.withProperty(FACING, iblockstate1.getValue(FACING)).withProperty(OPEN,
						iblockstate1.getValue(OPEN));
			}
		}

		return state;
	}

	public Block setTranslationKey(String name) {
		this.setRegistryName(name);
		return super.setTranslationKey(name);
	}
}
