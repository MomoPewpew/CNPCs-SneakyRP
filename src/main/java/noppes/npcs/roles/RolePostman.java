package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;

public class RolePostman extends RoleInterface {
     public NpcMiscInventory inventory = new NpcMiscInventory(1);
     private List recentlyChecked = new ArrayList();
     private List toCheck;

     public RolePostman(EntityNPCInterface npc) {
          super(npc);
     }

     public boolean aiShouldExecute() {
          if (this.npc.field_70173_aa % 20 != 0) {
               return false;
          } else {
               this.toCheck = this.npc.world.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b(10.0D, 10.0D, 10.0D));
               this.toCheck.removeAll(this.recentlyChecked);
               List listMax = this.npc.world.func_72872_a(EntityPlayer.class, this.npc.func_174813_aQ().func_72314_b(20.0D, 20.0D, 20.0D));
               this.recentlyChecked.retainAll(listMax);
               this.recentlyChecked.addAll(this.toCheck);
               Iterator var2 = this.toCheck.iterator();

               while(var2.hasNext()) {
                    EntityPlayer player = (EntityPlayer)var2.next();
                    if (PlayerData.get(player).mailData.hasMail()) {
                         player.func_145747_a(new TextComponentTranslation("You've got mail", new Object[0]));
                    }
               }

               return false;
          }
     }

     public boolean aiContinueExecute() {
          return false;
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.func_74782_a("PostInv", this.inventory.getToNBT());
          return nbttagcompound;
     }

     public void readFromNBT(NBTTagCompound nbttagcompound) {
          this.inventory.setFromNBT(nbttagcompound.func_74775_l("PostInv"));
     }

     public void interact(EntityPlayer player) {
          player.openGui(CustomNpcs.instance, EnumGuiType.PlayerMailman.ordinal(), player.world, 1, 1, 0);
     }
}
