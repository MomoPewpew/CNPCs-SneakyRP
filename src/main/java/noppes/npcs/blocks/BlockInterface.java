package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockInterface extends BlockContainer {
     protected BlockInterface(Material materialIn) {
          super(materialIn);
     }

     public Block func_149663_c(String name) {
          this.setRegistryName("customnpcs", name);
          return super.func_149663_c(name);
     }
}
