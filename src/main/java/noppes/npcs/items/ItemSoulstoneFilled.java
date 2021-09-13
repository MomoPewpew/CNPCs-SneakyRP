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
          NBTTagCompound compound = stack.getTagCompound();
          if (compound != null && compound.hasKey("Entity", 10)) {
               String name = I18n.translateToLocal(compound.getString("Name"));
               if (compound.hasKey("DisplayName")) {
                    name = compound.getString("DisplayName") + " (" + name + ")";
               }

               list.add(TextFormatting.BLUE + name);
               if (stack.getTagCompound().hasKey("ExtraText")) {
                    String text = "";
                    String[] split = compound.getString("ExtraText").split(",");
                    String[] var9 = split;
                    int var10 = split.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                         String s = var9[var11];
                         text = text + I18n.translateToLocal(s);
                    }

                    list.add(text);
               }

          } else {
               list.add(TextFormatting.RED + "Error");
          }
     }

     public EnumActionResult func_180614_a(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
          if (world.isRemote) {
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
          if (world.isRemote) {
               return null;
          } else if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("Entity", 10)) {
               NBTTagCompound compound = stack.getTagCompound().getCompoundTag("Entity");
               Entity entity = EntityList.createEntityFromNBT(compound, world);
               if (entity == null) {
                    return null;
               } else {
                    entity.func_70107_b((double)pos.getX() + 0.5D, (double)((float)(pos.getY() + 1) + 0.2F), (double)pos.getZ() + 0.5D);
                    if (entity instanceof EntityNPCInterface) {
                         EntityNPCInterface npc = (EntityNPCInterface)entity;
                         npc.ais.setStartPos(pos);
                         npc.func_70606_j(npc.getMaxHealth());
                         npc.func_70107_b((double)((float)pos.getX() + 0.5F), npc.getStartYPos(), (double)((float)pos.getZ() + 0.5F));
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

                    if (!world.spawnEntity(entity)) {
                         player.sendMessage(new TextComponentTranslation("error.failedToSpawn", new Object[0]));
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
