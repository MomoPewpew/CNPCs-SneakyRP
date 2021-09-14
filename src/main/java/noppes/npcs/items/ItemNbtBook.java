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
          this.maxStackSize = 1;
          this.setCreativeTab(CustomItems.tab);
     }

     public Item setTranslationKey(String name) {
          super.setTranslationKey(name);
          this.setRegistryName(new ResourceLocation("customnpcs", name));
          return this;
     }

     public void blockEvent(RightClickBlock event) {
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI, EnumGuiType.NbtBook, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
          IBlockState state = event.getWorld().getBlockState(event.getPos());
          NBTTagCompound data = new NBTTagCompound();
          TileEntity tile = event.getWorld().getTileEntity(event.getPos());
          if (tile != null) {
               tile.writeToNBT(data);
          }

          NBTTagCompound compound = new NBTTagCompound();
          compound.setTag("Data", data);
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI_DATA, compound);
     }

     public void entityEvent(EntityInteract event) {
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI, EnumGuiType.NbtBook, 0, 0, 0);
          NBTTagCompound data = new NBTTagCompound();
          event.getTarget().writeToNBTAtomically(data);
          NBTTagCompound compound = new NBTTagCompound();
          compound.setInteger("EntityId", event.getTarget().getEntityId());
          compound.setTag("Data", data);
          Server.sendData((EntityPlayerMP)event.getEntityPlayer(), EnumPacketClient.GUI_DATA, compound);
     }

     public boolean isAllowed(EnumPacketServer e) {
          return e == EnumPacketServer.NbtBookSaveEntity || e == EnumPacketServer.NbtBookSaveBlock;
     }
}
