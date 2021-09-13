package noppes.npcs.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityDialogNpc extends EntityNPCInterface {
     public EntityDialogNpc(World world) {
          super(world);
     }

     public boolean func_98034_c(EntityPlayer player) {
          return true;
     }

     public boolean func_82150_aj() {
          return true;
     }

     public void func_70071_h_() {
     }

     public boolean func_184645_a(EntityPlayer player, EnumHand hand) {
          return false;
     }
}
