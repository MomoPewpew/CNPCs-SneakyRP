package noppes.npcs;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.items.ItemScripted;

public class ScriptItemEventHandler {
     @SubscribeEvent
     public void invoke(EntityJoinWorldEvent event) {
          if (!event.getWorld().field_72995_K && event.getEntity() instanceof EntityItem) {
               EntityItem entity = (EntityItem)event.getEntity();
               ItemStack stack = entity.func_92059_d();
               if (!stack.func_190926_b() && stack.func_77973_b() == CustomItems.scripted_item && EventHooks.onScriptItemSpawn(ItemScripted.GetWrapper(stack), entity)) {
                    event.setCanceled(true);
               }

          }
     }

     @SubscribeEvent
     public void invoke(ItemTossEvent event) {
          if (!event.getPlayer().field_70170_p.field_72995_K) {
               EntityItem entity = event.getEntityItem();
               ItemStack stack = entity.func_92059_d();
               if (!stack.func_190926_b() && stack.func_77973_b() == CustomItems.scripted_item && EventHooks.onScriptItemTossed(ItemScripted.GetWrapper(stack), event.getPlayer(), entity)) {
                    event.setCanceled(true);
               }

          }
     }

     @SubscribeEvent
     public void invoke(EntityItemPickupEvent event) {
          if (!event.getEntityPlayer().field_70170_p.field_72995_K) {
               EntityItem entity = event.getItem();
               ItemStack stack = entity.func_92059_d();
               if (!stack.func_190926_b() && stack.func_77973_b() == CustomItems.scripted_item) {
                    EventHooks.onScriptItemPickedUp(ItemScripted.GetWrapper(stack), event.getEntityPlayer(), entity);
               }

          }
     }
}
