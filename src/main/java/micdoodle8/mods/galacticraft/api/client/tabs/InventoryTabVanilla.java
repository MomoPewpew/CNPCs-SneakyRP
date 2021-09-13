package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class InventoryTabVanilla extends AbstractTab {
     public InventoryTabVanilla() {
          super(0, 0, 0, new ItemStack(Blocks.field_150462_ai));
     }

     public void onTabClicked() {
          TabRegistry.openInventoryGui();
     }

     public boolean shouldAddToList() {
          return true;
     }
}
