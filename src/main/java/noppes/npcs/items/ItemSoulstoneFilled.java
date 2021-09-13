package noppes.npcs.items;

import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleFollower;

public class ItemSoulstoneFilled extends Item {
     public ItemSoulstoneFilled() {
          this.func_77625_d(1);
     }

     public Item setUnlocalizedName(String name) {
          super.setUnlocalizedName(name);
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return this;
     }

     @SideOnly(Side.CLIENT)
     public void func_77624_a(ItemStack stack, World world, List list, ITooltipFlag flag) {
          NBTTagCompound compound = stack.func_77978_p();
          if (compound != null && compound.func_150297_b("Entity", 10)) {
               String name = I18n.func_74838_a(compound.func_74779_i("Name"));
               if (compound.func_74764_b("DisplayName")) {
                    name = compound.func_74779_i("DisplayName") + " (" + name + ")";
               }

               list.add(TextFormatting.BLUE + name);
               if (stack.func_77978_p().func_74764_b("ExtraText")) {
                    String text = "";
                    String[] split = compound.func_74779_i("ExtraText").split(",");
                    String[] var9 = split;
                    int var10 = split.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                         String s = var9[var11];
                         text = text + I18n.func_74838_a(s);
                    }

                    list.add(text);
               }

          } else {
               list.add(TextFormatting.RED + "Error");
          }
     }

     public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (world.field_72995_K) {
               return EnumActionResult.SUCCESS;
          } else {
               ItemStack stack = player.func_184586_b(hand);
               if (Spawn(player, stack, world, pos) == null) {
                    return EnumActionResult.FAIL;
               } else {
                    if (!player.field_71075_bZ.field_75098_d) {
                         stack.splitStack(1);
                    }

                    return EnumActionResult.SUCCESS;
               }
          }
     }

     public static Entity Spawn(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
          if (world.field_72995_K) {
               return null;
          } else if (stack.func_77978_p() != null && stack.func_77978_p().func_150297_b("Entity", 10)) {
               NBTTagCompound compound = stack.func_77978_p().func_74775_l("Entity");
               Entity entity = EntityList.func_75615_a(compound, world);
               if (entity == null) {
                    return null;
               } else {
                    entity.func_70107_b((double)pos.func_177958_n() + 0.5D, (double)((float)(pos.func_177956_o() + 1) + 0.2F), (double)pos.func_177952_p() + 0.5D);
                    if (entity instanceof EntityNPCInterface) {
                         EntityNPCInterface npc = (EntityNPCInterface)entity;
                         npc.ais.setStartPos(pos);
                         npc.func_70606_j(npc.func_110138_aP());
                         npc.func_70107_b((double)((float)pos.func_177958_n() + 0.5F), npc.getStartYPos(), (double)((float)pos.func_177952_p() + 0.5F));
                         if (npc.advanced.role == 6 && player != null) {
                              PlayerData data = PlayerData.get(player);
                              if (data.hasCompanion()) {
                                   return null;
                              }

                              ((RoleCompanion)npc.roleInterface).setOwner(player);
                              data.setCompanion(npc);
                         }

                         if (npc.advanced.role == 2 && player != null) {
                              ((RoleFollower)npc.roleInterface).setOwner(player);
                         }
                    }

                    if (!world.func_72838_d(entity)) {
                         player.func_145747_a(new TextComponentTranslation("error.failedToSpawn", new Object[0]));
                         return null;
                    } else {
                         return entity;
                    }
               }
          } else {
               return null;
          }
     }
}
