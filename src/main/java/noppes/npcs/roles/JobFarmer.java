package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.NpcBlockHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.role.IJobFarmer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.data.BlockData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobFarmer extends JobInterface implements MassBlockController.IMassBlock, IJobFarmer {
     public int chestMode = 1;
     private List trackedBlocks = new ArrayList();
     private int ticks = 0;
     private int walkTicks = 0;
     private int blockTicks = 800;
     private boolean waitingForBlocks = false;
     private BlockPos ripe = null;
     private BlockPos chest = null;
     private ItemStack holding;

     public JobFarmer(EntityNPCInterface npc) {
          super(npc);
          this.holding = ItemStack.EMPTY;
          this.overrideMainHand = true;
     }

     public IItemStack getMainhand() {
          String name = this.npc.getJobData();
          ItemStack item = this.stringToItem(name);
          return item.isEmpty() ? (IItemStack)this.npc.inventory.weapons.get(0) : NpcAPI.Instance().getIItemStack(item);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound compound) {
          compound.setInteger("JobChestMode", this.chestMode);
          if (!this.holding.isEmpty()) {
               compound.setTag("JobHolding", this.holding.writeToNBT(new NBTTagCompound()));
          }

          return compound;
     }

     public void readFromNBT(NBTTagCompound compound) {
          this.chestMode = compound.getInteger("JobChestMode");
          this.holding = new ItemStack(compound.getCompoundTag("JobHolding"));
          this.blockTicks = 1100;
     }

     public void setHolding(ItemStack item) {
          this.holding = item;
          this.npc.setJobData(this.itemToString(this.holding));
     }

     public boolean aiShouldExecute() {
          if (!this.holding.isEmpty()) {
               if (this.chestMode == 0) {
                    this.setHolding(ItemStack.EMPTY);
               } else if (this.chestMode == 1) {
                    if (this.chest == null) {
                         this.dropItem(this.holding);
                         this.setHolding(ItemStack.EMPTY);
                    } else {
                         this.chest();
                    }
               } else if (this.chestMode == 2) {
                    this.dropItem(this.holding);
                    this.setHolding(ItemStack.EMPTY);
               }

               return false;
          } else if (this.ripe != null) {
               this.pluck();
               return false;
          } else {
               if (!this.waitingForBlocks && this.blockTicks++ > 1200) {
                    this.blockTicks = 0;
                    this.waitingForBlocks = true;
                    MassBlockController.Queue(this);
               }

               if (this.ticks++ < 100) {
                    return false;
               } else {
                    this.ticks = 0;
                    return true;
               }
          }
     }

     private void dropItem(ItemStack item) {
          EntityItem entityitem = new EntityItem(this.npc.world, this.npc.field_70165_t, this.npc.field_70163_u, this.npc.field_70161_v, item);
          entityitem.func_174869_p();
          this.npc.world.spawnEntity(entityitem);
     }

     private void chest() {
          BlockPos pos = this.chest;
          this.npc.getNavigator().tryMoveToXYZ((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1.0D);
          this.npc.getLookHelper().func_75650_a((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 10.0F, (float)this.npc.getVerticalFaceSpeed());
          if (this.npc.nearPosition(pos) || this.walkTicks++ > 400) {
               if (this.walkTicks < 400) {
                    this.npc.swingArm(EnumHand.MAIN_HAND);
               }

               this.npc.getNavigator().clearPath();
               this.ticks = 100;
               this.walkTicks = 0;
               IBlockState state = this.npc.world.getBlockState(pos);
               if (!(state.getBlock() instanceof BlockChest)) {
                    this.chest = null;
               } else {
                    TileEntityChest tile = (TileEntityChest)this.npc.world.getTileEntity(pos);

                    int i;
                    for(i = 0; !this.holding.isEmpty() && i < tile.getSizeInventory(); ++i) {
                         this.holding = this.mergeStack(tile, i, this.holding);
                    }

                    for(i = 0; !this.holding.isEmpty() && i < tile.getSizeInventory(); ++i) {
                         ItemStack item = tile.getStackInSlot(i);
                         if (item.isEmpty()) {
                              tile.setInventorySlotContents(i, this.holding);
                              this.holding = ItemStack.EMPTY;
                         }
                    }

                    if (!this.holding.isEmpty()) {
                         this.dropItem(this.holding);
                         this.holding = ItemStack.EMPTY;
                    }
               }

               this.setHolding(this.holding);
          }

     }

     private ItemStack mergeStack(IInventory inventory, int slot, ItemStack item) {
          ItemStack item2 = inventory.getStackInSlot(slot);
          if (!NoppesUtilPlayer.compareItems(item, item2, false, false)) {
               return item;
          } else {
               int size = item2.getMaxStackSize() - item2.getCount();
               if (size >= item.getCount()) {
                    item2.setCount(item2.getCount() + item.getCount());
                    return ItemStack.EMPTY;
               } else {
                    item2.setCount(item2.getMaxStackSize());
                    item.setCount(item.getCount() - size);
                    return item.isEmpty() ? ItemStack.EMPTY : item;
               }
          }
     }

     private void pluck() {
          BlockPos pos = this.ripe;
          this.npc.getNavigator().tryMoveToXYZ((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 1.0D);
          this.npc.getLookHelper().func_75650_a((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 10.0F, (float)this.npc.getVerticalFaceSpeed());
          if (this.npc.nearPosition(pos) || this.walkTicks++ > 400) {
               if (this.walkTicks > 400) {
                    pos = NoppesUtilServer.GetClosePos(pos, this.npc.world);
                    this.npc.func_70634_a((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
               }

               this.ripe = null;
               this.npc.getNavigator().clearPath();
               this.ticks = 90;
               this.walkTicks = 0;
               this.npc.swingArm(EnumHand.MAIN_HAND);
               IBlockState state = this.npc.world.getBlockState(pos);
               Block b = state.getBlock();
               if (b instanceof BlockCrops && ((BlockCrops)b).func_185525_y(state)) {
                    BlockCrops crop = (BlockCrops)b;
                    this.npc.world.setBlockState(pos, crop.func_185528_e(0));
                    this.holding = new ItemStack(NpcBlockHelper.getCrop((BlockCrops)b));
               }

               if (b instanceof BlockStem) {
                    state = b.func_176221_a(state, this.npc.world, pos);
                    EnumFacing facing = (EnumFacing)state.getValue(BlockStem.field_176483_b);
                    if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
                         return;
                    }

                    pos = pos.func_177971_a(facing.func_176730_m());
                    b = this.npc.world.getBlockState(pos).getBlock();
                    this.npc.world.setBlockToAir(pos);
                    if (b != Blocks.field_150350_a) {
                         this.holding = new ItemStack(b);
                    }
               }

               this.setHolding(this.holding);
          }

     }

     public boolean aiContinueExecute() {
          return false;
     }

     public void aiUpdateTask() {
          Iterator ite = this.trackedBlocks.iterator();

          while(ite.hasNext() && this.ripe == null) {
               BlockPos pos = (BlockPos)ite.next();
               IBlockState state = this.npc.world.getBlockState(pos);
               Block b = state.getBlock();
               if (b instanceof BlockCrops) {
                    if (((BlockCrops)b).func_185525_y(state)) {
                         this.ripe = pos;
                    }
               } else if (b instanceof BlockStem) {
                    state = b.func_176221_a(state, this.npc.world, pos);
                    EnumFacing facing = (EnumFacing)state.getValue(BlockStem.field_176483_b);
                    if (facing != EnumFacing.UP) {
                         this.ripe = pos;
                    }
               } else {
                    ite.remove();
               }
          }

          this.npc.ais.returnToStart = this.ripe == null;
          if (this.ripe != null) {
               this.npc.getNavigator().clearPath();
               this.npc.getLookHelper().func_75650_a((double)this.ripe.getX(), (double)this.ripe.getY(), (double)this.ripe.getZ(), 10.0F, (float)this.npc.getVerticalFaceSpeed());
          }

     }

     public boolean isPlucking() {
          return this.ripe != null || !this.holding.isEmpty();
     }

     public EntityNPCInterface getNpc() {
          return this.npc;
     }

     public int getRange() {
          return 16;
     }

     public void processed(List list) {
          List trackedBlocks = new ArrayList();
          BlockPos chest = null;
          Iterator var4 = list.iterator();

          while(true) {
               BlockData data;
               label32:
               do {
                    while(var4.hasNext()) {
                         data = (BlockData)var4.next();
                         Block b = data.state.getBlock();
                         if (b instanceof BlockChest) {
                              continue label32;
                         }

                         if ((b instanceof BlockCrops || b instanceof BlockStem) && !trackedBlocks.contains(data.pos)) {
                              trackedBlocks.add(data.pos);
                         }
                    }

                    this.chest = chest;
                    this.trackedBlocks = trackedBlocks;
                    this.waitingForBlocks = false;
                    return;
               } while(chest != null && this.npc.getDistanceSq(chest) <= this.npc.getDistanceSq(data.pos));

               chest = data.pos;
          }
     }

     public int getMutexBits() {
          return this.npc.getNavigator().noPath() ? 0 : AiMutex.LOOK;
     }
}
