package noppes.npcs.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityDialogNpc extends EntityNPCInterface {
     public EntityDialogNpc(World world) {
          super(world);
     }

     public boolean isInvisibleToPlayer(EntityPlayer player) {
          return true;
     }

     public boolean isInvisible() {
          return true;
     }

     public void onUpdate() {
     }

     public boolean processInteract(EntityPlayer player, EnumHand hand) {
          return false;
     }
}
