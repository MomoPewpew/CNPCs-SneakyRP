package noppes.npcs.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.util.CustomNPCsScheduler;
import noppes.npcs.util.IPermission;

public class ItemNpcWand extends Item implements IPermission {
     public ItemNpcWand() {
          this.field_77777_bU = 1;
          this.setCreativeTab(CustomItems.tab);
     }

     public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
          ItemStack itemstack = player.func_184586_b(hand);
          if (!world.field_72995_K) {
               return new ActionResult(EnumActionResult.SUCCESS, itemstack);
          } else {
               CustomNpcs.proxy.openGui(0, 0, 0, EnumGuiType.NpcRemote, player);
               return new ActionResult(EnumActionResult.SUCCESS, itemstack);
          }
     }

     public int func_77626_a(ItemStack par1ItemStack) {
          return 72000;
     }

     public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos bpos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (world.field_72995_K) {
               return EnumActionResult.SUCCESS;
          } else {
               if (CustomNpcs.OpsOnly && !player.func_184102_h().func_184103_al().func_152596_g(player.func_146103_bH())) {
                    player.func_145747_a(new TextComponentTranslation("availability.permission", new Object[0]));
               } else if (CustomNpcsPermissions.hasPermission(player, CustomNpcsPermissions.NPC_CREATE)) {
                    EntityCustomNpc npc = new EntityCustomNpc(world);
                    npc.ais.setStartPos(bpos.func_177984_a());
                    npc.func_70012_b((double)((float)bpos.func_177958_n() + 0.5F), npc.getStartYPos(), (double)((float)bpos.func_177952_p() + 0.5F), player.field_70177_z, player.field_70125_A);
                    world.func_72838_d(npc);
                    npc.func_70606_j(npc.func_110138_aP());
                    CustomNPCsScheduler.runTack(() -> {
                         NoppesUtilServer.sendOpenGui(player, EnumGuiType.MainMenuDisplay, npc);
                    }, 100);
               } else {
                    player.func_145747_a(new TextComponentTranslation("availability.permission", new Object[0]));
               }

               return EnumActionResult.SUCCESS;
          }
     }

     public ItemStack func_77654_b(ItemStack stack, World worldIn, EntityLivingBase playerIn) {
          return stack;
     }

     public Item setUnlocalizedName(String name) {
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return super.setUnlocalizedName(name);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return true;
     }
}
