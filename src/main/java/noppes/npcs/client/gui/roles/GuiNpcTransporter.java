package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import java.util.Vector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcTransporter extends GuiNPCInterface2 implements IScrollData, IGuiData {
     private GuiCustomScroll scroll;
     public TransportLocation location = new TransportLocation();
     private HashMap data = new HashMap();

     public GuiNpcTransporter(EntityNPCInterface npc) {
          super(npc);
     }

     public void initPacket() {
          Client.sendData(EnumPacketServer.TransportCategoriesGet);
          Client.sendData(EnumPacketServer.TransportGetLocation);
     }

     public void initGui() {
          super.initGui();
          Vector list = new Vector();
          list.addAll(this.data.keySet());
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setSize(143, 208);
          }

          this.scroll.guiLeft = this.guiLeft + 214;
          this.scroll.guiTop = this.guiTop + 4;
          this.addScroll(this.scroll);
          this.addLabel(new GuiNpcLabel(0, "gui.name", this.guiLeft + 4, this.height + 8));
          this.addTextField(new GuiNpcTextField(0, this, this.fontRenderer, this.guiLeft + 60, this.guiTop + 3, 140, 20, this.location.name));
          this.addButton(new GuiNpcButton(0, this.guiLeft + 4, this.guiTop + 31, new String[]{"transporter.discovered", "transporter.start", "transporter.interaction"}, this.location.type));
     }

     protected void actionPerformed(GuiButton guibutton) {
          GuiNpcButton button = (GuiNpcButton)guibutton;
          if (button.id == 0) {
               this.location.type = button.getValue();
          }

     }

     public void save() {
          if (this.scroll.hasSelected()) {
               String name = this.getTextField(0).getText();
               if (!name.isEmpty()) {
                    this.location.name = name;
               }

               this.location.pos = new BlockPos(this.player);
               this.location.dimension = this.player.dimension;
               int cat = (Integer)this.data.get(this.scroll.getSelected());
               Client.sendData(EnumPacketServer.TransportSave, cat, this.location.writeNBT());
          }
     }

     public void setData(Vector list, HashMap data) {
          this.data = data;
          this.scroll.setList(list);
     }

     public void setSelected(String selected) {
          this.scroll.setSelected(selected);
     }

     public void setGuiData(NBTTagCompound compound) {
          TransportLocation loc = new TransportLocation();
          loc.readNBT(compound);
          this.location = loc;
          this.initGui();
     }
}
