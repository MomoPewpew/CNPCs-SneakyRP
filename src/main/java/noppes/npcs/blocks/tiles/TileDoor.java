package noppes.npcs.blocks.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;

public class TileDoor extends TileNpcEntity implements ITickable {
     public int ticksExisted = 0;
     public Block blockModel;
     public boolean needsClientUpdate;
     public TileEntity renderTile;
     public boolean renderTileErrored;
     public ITickable renderTileUpdate;

     public TileDoor() {
          this.blockModel = CustomItems.scriptedDoor;
          this.needsClientUpdate = false;
          this.renderTileErrored = true;
          this.renderTileUpdate = null;
     }

     public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
          return oldState.getBlock() != newState.getBlock();
     }

     public void readFromNBT(NBTTagCompound compound) {
          super.readFromNBT(compound);
          this.setDoorNBT(compound);
     }

     public void setDoorNBT(NBTTagCompound compound) {
          this.blockModel = (Block)Block.REGISTRY.getObject(new ResourceLocation(compound.getString("ScriptDoorBlockModel")));
          if (this.blockModel == null || !(this.blockModel instanceof BlockDoor)) {
               this.blockModel = CustomItems.scriptedDoor;
          }

          this.renderTileUpdate = null;
          this.renderTile = null;
          this.renderTileErrored = false;
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          this.getDoorNBT(compound);
          return super.writeToNBT(compound);
     }

     public NBTTagCompound getDoorNBT(NBTTagCompound compound) {
          compound.setString("ScriptDoorBlockModel", Block.REGISTRY.getNameForObject(this.blockModel) + "");
          return compound;
     }

     public void setItemModel(Block block) {
          if (block == null || !(block instanceof BlockDoor)) {
               block = CustomItems.scriptedDoor;
          }

          if (this.blockModel != block) {
               this.blockModel = block;
               this.needsClientUpdate = true;
          }
     }

     public void func_73660_a() {
          if (this.renderTileUpdate != null) {
               try {
                    this.renderTileUpdate.func_73660_a();
               } catch (Exception var2) {
                    this.renderTileUpdate = null;
               }
          }

          ++this.ticksExisted;
          if (this.ticksExisted >= 10) {
               this.ticksExisted = 0;
               if (this.needsClientUpdate) {
                    this.markDirty();
                    IBlockState state = this.field_145850_b.getBlockState(this.field_174879_c);
                    this.field_145850_b.notifyBlockUpdate(this.field_174879_c, state, state, 3);
                    this.needsClientUpdate = false;
               }
          }

     }

     public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
          this.handleUpdateTag(pkt.func_148857_g());
     }

     public void handleUpdateTag(NBTTagCompound compound) {
          this.setDoorNBT(compound);
     }

     public SPacketUpdateTileEntity func_189518_D_() {
          return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
     }

     public NBTTagCompound func_189517_E_() {
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("x", this.field_174879_c.getX());
          compound.setInteger("y", this.field_174879_c.getY());
          compound.setInteger("z", this.field_174879_c.getZ());
          this.getDoorNBT(compound);
          return compound;
     }
}
