package noppes.npcs.api.wrapper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.item.IItemBlock;

public class ItemBlockWrapper extends ItemStackWrapper implements IItemBlock {
     protected String blockName;

     protected ItemBlockWrapper(ItemStack item) {
          super(item);
          Block b = Block.func_149634_a(item.func_77973_b());
          this.blockName = Block.field_149771_c.func_177774_c(b) + "";
     }

     public int getType() {
          return 2;
     }

     public String getBlockName() {
          return this.blockName;
     }
}
