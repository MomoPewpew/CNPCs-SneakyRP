package noppes.npcs.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import noppes.npcs.CustomItems;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.util.IPermission;

public class ItemNbtBook extends Item implements IPermission {
     public ItemNbtBook() {
          this.field_77777_bU = 1;
          this.setCreativeTab(CustomItems.tab);
     }

     public Item setUnlocalizedName(String name) {
          super.setUnlocalizedName(name);
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return this;
     }

     public void blockEvent(RightClickBlock event) {
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI, EnumGuiType.NbtBook, event.getPos().func_177958_n(), event.getPos().func_177956_o(), event.getPos().func_177952_p());
          IBlockState state = event.getWorld().func_180495_p(event.getPos());
          NBTTagCompound data = new NBTTagCompound();
          TileEntity tile = event.getWorld().func_175625_s(event.getPos());
          if (tile != null) {
               tile.func_189515_b(data);
          }

          NBTTagCompound compound = new NBTTagCompound();
          compound.setTag("Data", data);
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI_DATA, compound);
     }

     public void entityEvent(EntityInteract event) {
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI, EnumGuiType.NbtBook, 0, 0, 0);
          NBTTagCompound data = new NBTTagCompound();
          event.getTarget().func_184198_c(data);
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("EntityId", event.getTarget().func_145782_y());
          compound.setTag("Data", data);
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI_DATA, compound);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.NbtBookSaveEntity || e == EnumPacketServer.NbtBookSaveBlock;
     }
}
