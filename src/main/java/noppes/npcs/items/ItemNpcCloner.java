package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.IPermission;

public class ItemNpcCloner extends Item implements IPermission {
     public ItemNpcCloner() {
          this.field_77777_bU = 1;
          this.func_77637_a(CustomItems.tab);
     }

     public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (!world.field_72995_K) {
               NoppesUtilServer.sendOpenGui(player, EnumGuiType.MobSpawner, (EntityNPCInterface)null, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
          }

          return EnumActionResult.SUCCESS;
     }

     public Item func_77655_b(String name) {
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return super.func_77655_b(name);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.CloneList || e == EnumPacketServer.SpawnMob || e == EnumPacketServer.MobSpawner || e == EnumPacketServer.ClonePreSave || e == EnumPacketServer.CloneRemove || e == EnumPacketServer.CloneSave;
     }
}
