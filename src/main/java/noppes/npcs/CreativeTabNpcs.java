package noppes.npcs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabNpcs extends CreativeTabs {
     public Item item;
     public int meta;

     public CreativeTabNpcs(String label) {
          super(label);
          this.item = Items.field_151054_z;
          this.meta = 0;
     }

     public ItemStack func_78016_d() {
          return new ItemStack(this.item, 1, this.meta);
     }
}
