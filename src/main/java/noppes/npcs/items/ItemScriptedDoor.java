package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
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

public class ItemScriptedDoor extends ItemDoor implements IPermission {
     public ItemScriptedDoor(Block block) {
          super(block);
          this.field_77777_bU = 1;
          this.func_77637_a(CustomItems.tab);
     }

     public EnumActionResult func_180614_a(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          EnumActionResult res = super.func_180614_a(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ);
          if (res == EnumActionResult.SUCCESS && !worldIn.field_72995_K) {
               BlockPos newPos = pos.func_177984_a();
               NoppesUtilServer.sendOpenGui(playerIn, EnumGuiType.ScriptDoor, (EntityNPCInterface)null, newPos.func_177958_n(), newPos.func_177956_o(), newPos.func_177952_p());
               return EnumActionResult.SUCCESS;
          } else {
               return res;
          }
     }

     public ItemStack func_77654_b(ItemStack stack, World worldIn, EntityLivingBase playerIn) {
          return stack;
     }

     public Item func_77655_b(String name) {
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return super.func_77655_b(name);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.ScriptDoorDataSave;
     }
}
