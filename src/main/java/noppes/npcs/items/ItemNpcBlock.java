package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemNpcBlock extends ItemBlock {
     public ItemNpcBlock(Block block) {
          super(block);
          String name = block.func_149739_a().substring(5);
          this.setRegistryName(name);
          this.func_77655_b(name);
     }
}
