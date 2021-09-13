package noppes.npcs.items;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import noppes.npcs.CustomItems;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class ItemScripted extends Item implements IPermission {
     public static Map Resources = new HashMap();

     public ItemScripted() {
          this.field_77777_bU = 1;
          this.setCreativeTab(CustomItems.tab);
          this.setHasSubtypes(true);
     }

     public Item setUnlocalizedName(String name) {
          super.setUnlocalizedName(name);
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return this;
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.ScriptItemDataGet || e == EnumPacketServer.ScriptItemDataSave;
     }

     public static ItemScriptedWrapper GetWrapper(ItemStack stack) {
          return (ItemScriptedWrapper)NpcAPI.Instance().getIItemStack(stack);
     }

     public boolean showDurabilityBar(ItemStack stack) {
          IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
          return istack instanceof ItemScriptedWrapper ? ((ItemScriptedWrapper)istack).durabilityShow : super.showDurabilityBar(stack);
     }

     public double getDurabilityForDisplay(ItemStack stack) {
          IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
          return istack instanceof ItemScriptedWrapper ? 1.0D - ((ItemScriptedWrapper)istack).durabilityValue : super.getDurabilityForDisplay(stack);
     }

     public int getRGBDurabilityForDisplay(ItemStack stack) {
          IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
          if (!(istack instanceof ItemScriptedWrapper)) {
               return super.getRGBDurabilityForDisplay(stack);
          } else {
               int color = ((ItemScriptedWrapper)istack).durabilityColor;
               return color >= 0 ? color : MathHelper.func_181758_c(Math.max(0.0F, (float)(1.0D - this.getDurabilityForDisplay(stack))) / 3.0F, 1.0F, 1.0F);
          }
     }

     public int getItemStackLimit(ItemStack stack) {
          IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
          return istack instanceof ItemScriptedWrapper ? ((ItemScriptedWrapper)istack).getMaxStackSize() : super.getItemStackLimit(stack);
     }

     public boolean func_77644_a(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
          return true;
     }
}
