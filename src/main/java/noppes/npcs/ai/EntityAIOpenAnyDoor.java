package noppes.npcs.ai;

import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIOpenAnyDoor extends EntityAIBase {
     private EntityNPCInterface npc;
     private BlockPos position;
     private Block door;
     private IProperty property;
     private boolean hasStoppedDoorInteraction;
     private float entityX;
     private float entityZ;
     private int closeDoorTemporisation;

     public EntityAIOpenAnyDoor(EntityNPCInterface npc) {
          this.npc = npc;
     }

     public boolean func_75250_a() {
          if (!this.npc.field_70123_F) {
               return false;
          } else {
               Path pathentity = this.npc.func_70661_as().func_75505_d();
               if (pathentity != null && !pathentity.func_75879_b()) {
                    for(int i = 0; i < Math.min(pathentity.func_75873_e() + 2, pathentity.func_75874_d()); ++i) {
                         PathPoint pathpoint = pathentity.func_75877_a(i);
                         this.position = new BlockPos(pathpoint.field_75839_a, pathpoint.field_75837_b + 1, pathpoint.field_75838_c);
                         if (this.npc.getDistanceSq((double)this.position.getX(), this.npc.field_70163_u, (double)this.position.getZ()) <= 2.25D) {
                              this.door = this.getDoor(this.position);
                              if (this.door != null) {
                                   return true;
                              }
                         }
                    }

                    this.position = (new BlockPos(this.npc)).up();
                    this.door = this.getDoor(this.position);
                    return this.door != null;
               } else {
                    return false;
               }
          }
     }

     public boolean func_75253_b() {
          return this.closeDoorTemporisation > 0 && !this.hasStoppedDoorInteraction;
     }

     public void func_75249_e() {
          this.hasStoppedDoorInteraction = false;
          this.entityX = (float)((double)((float)this.position.getX() + 0.5F) - this.npc.field_70165_t);
          this.entityZ = (float)((double)((float)this.position.getZ() + 0.5F) - this.npc.field_70161_v);
          this.closeDoorTemporisation = 20;
          this.setDoorState(this.door, this.position, true);
     }

     public void func_75251_c() {
          this.setDoorState(this.door, this.position, false);
     }

     public void func_75246_d() {
          --this.closeDoorTemporisation;
          float f = (float)((double)((float)this.position.getX() + 0.5F) - this.npc.field_70165_t);
          float f1 = (float)((double)((float)this.position.getZ() + 0.5F) - this.npc.field_70161_v);
          float f2 = this.entityX * f + this.entityZ * f1;
          if (f2 < 0.0F) {
               this.hasStoppedDoorInteraction = true;
          }

     }

     public Block getDoor(BlockPos pos) {
          IBlockState state = this.npc.world.getBlockState(pos);
          Block block = state.getBlock();
          if (!state.func_185913_b() && block != Blocks.field_150454_av) {
               if (block instanceof BlockDoor) {
                    return block;
               } else {
                    Set set = state.func_177228_b().keySet();
                    Iterator var5 = set.iterator();

                    IProperty prop;
                    do {
                         if (!var5.hasNext()) {
                              return null;
                         }

                         prop = (IProperty)var5.next();
                    } while(!(prop instanceof PropertyBool) || !prop.func_177701_a().equals("open"));

                    this.property = prop;
                    return block;
               }
          } else {
               return null;
          }
     }

     public void setDoorState(Block block, BlockPos position, boolean open) {
          if (block instanceof BlockDoor) {
               ((BlockDoor)block).func_176512_a(this.npc.world, position, open);
          } else {
               IBlockState state = this.npc.world.getBlockState(position);
               if (state.getBlock() != block) {
                    return;
               }

               this.npc.world.setBlockState(position, state.func_177226_a(this.property, open));
               this.npc.world.func_180498_a((EntityPlayer)null, open ? 1003 : 1006, position, 0);
          }

     }
}
