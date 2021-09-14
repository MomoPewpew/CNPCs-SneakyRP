package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcButtonYesNo;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.schematics.ISchematic;
import noppes.npcs.schematics.SchematicWrapper;

public class GuiBlockBuilder extends GuiNPCInterface implements IGuiData, ICustomScrollListener, IScrollData, GuiYesNoCallback {
     private int x;
     private int y;
     private int z;
     private TileBuilder tile;
     private GuiCustomScroll scroll;
     private ISchematic selected = null;

     public GuiBlockBuilder(int x, int y, int z) {
          this.x = x;
          this.y = y;
          this.z = z;
          this.setBackground("menubg.png");
          this.xSize = 256;
          this.ySize = 216;
          this.closeOnEsc = true;
          this.tile = (TileBuilder)this.player.world.getTileEntity(new BlockPos(x, y, z));
     }

     public void initPacket() {
          Client.sendData(EnumPacketServer.SchematicsTile, this.x, this.y, this.z);
     }

     public void func_73866_w_() {
          super.func_73866_w_();
          if (this.scroll == null) {
               this.scroll = new GuiCustomScroll(this, 0);
               this.scroll.setSize(125, 208);
          }

          this.scroll.guiLeft = this.guiLeft + 4;
          this.scroll.guiTop = this.guiTop + 4;
          this.addScroll(this.scroll);
          if (this.selected != null) {
               int y = this.guiTop + 4;
               int size = this.selected.getWidth() * this.selected.getHeight() * this.selected.getLength();
               this.addButton(new GuiNpcButtonYesNo(3, this.guiLeft + 200, y, TileBuilder.DrawPos != null && this.tile.getPos().equals(TileBuilder.DrawPos)));
               this.addLabel(new GuiNpcLabel(3, "schematic.preview", this.guiLeft + 130, y + 5));
               String var10004 = I18n.translateToLocal("schematic.width") + ": " + this.selected.getWidth();
               int var10005 = this.guiLeft + 130;
               y += 21;
               this.addLabel(new GuiNpcLabel(0, var10004, var10005, y));
               var10004 = I18n.translateToLocal("schematic.length") + ": " + this.selected.getLength();
               var10005 = this.guiLeft + 130;
               y += 11;
               this.addLabel(new GuiNpcLabel(1, var10004, var10005, y));
               var10004 = I18n.translateToLocal("schematic.height") + ": " + this.selected.getHeight();
               var10005 = this.guiLeft + 130;
               y += 11;
               this.addLabel(new GuiNpcLabel(2, var10004, var10005, y));
               int var3 = this.guiLeft + 200;
               y += 14;
               this.addButton(new GuiNpcButtonYesNo(4, var3, y, this.tile.enabled));
               this.addLabel(new GuiNpcLabel(4, I18n.translateToLocal("gui.enabled"), this.guiLeft + 130, y + 5));
               var3 = this.guiLeft + 200;
               y += 22;
               this.addButton(new GuiNpcButtonYesNo(7, var3, y, this.tile.finished));
               this.addLabel(new GuiNpcLabel(7, I18n.translateToLocal("gui.finished"), this.guiLeft + 130, y + 5));
               var3 = this.guiLeft + 200;
               y += 22;
               this.addButton(new GuiNpcButtonYesNo(8, var3, y, this.tile.started));
               this.addLabel(new GuiNpcLabel(8, I18n.translateToLocal("gui.started"), this.guiLeft + 130, y + 5));
               var10005 = this.guiLeft + 200;
               y += 22;
               this.addTextField(new GuiNpcTextField(9, this, var10005, y, 50, 20, this.tile.yOffest + ""));
               this.addLabel(new GuiNpcLabel(9, I18n.translateToLocal("gui.yoffset"), this.guiLeft + 130, y + 5));
               this.getTextField(9).numbersOnly = true;
               this.getTextField(9).setMinMaxDefault(-10, 10, 0);
               var3 = this.guiLeft + 200;
               y += 22;
               this.addButton(new GuiNpcButton(5, var3, y, 50, 20, new String[]{"0", "90", "180", "270"}, this.tile.rotation));
               this.addLabel(new GuiNpcLabel(5, I18n.translateToLocal("movement.rotation"), this.guiLeft + 130, y + 5));
               var3 = this.guiLeft + 130;
               y += 22;
               this.addButton(new GuiNpcButton(6, var3, y, 120, 20, "availability.options"));
               var3 = this.guiLeft + 130;
               y += 22;
               this.addButton(new GuiNpcButton(10, var3, y, 120, 20, "schematic.instantBuild"));
          }

     }

     protected void func_146284_a(GuiButton guibutton) {
          if (guibutton.id == 3) {
               GuiNpcButtonYesNo button = (GuiNpcButtonYesNo)guibutton;
               if (button.getBoolean()) {
                    TileBuilder.SetDrawPos(new BlockPos(this.x, this.y, this.z));
                    this.tile.setDrawSchematic(new SchematicWrapper(this.selected));
               } else {
                    TileBuilder.SetDrawPos((BlockPos)null);
                    this.tile.setDrawSchematic((SchematicWrapper)null);
               }
          }

          if (guibutton.id == 4) {
               this.tile.enabled = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.id == 5) {
               this.tile.rotation = ((GuiNpcButton)guibutton).getValue();
               TileBuilder.Compiled = false;
          }

          if (guibutton.id == 6) {
               this.setSubGui(new SubGuiNpcAvailability(this.tile.availability));
          }

          if (guibutton.id == 7) {
               this.tile.finished = ((GuiNpcButtonYesNo)guibutton).getBoolean();
               Client.sendData(EnumPacketServer.SchematicsSet, this.x, this.y, this.z, this.scroll.getSelected());
          }

          if (guibutton.id == 8) {
               this.tile.started = ((GuiNpcButtonYesNo)guibutton).getBoolean();
          }

          if (guibutton.id == 10) {
               this.save();
               GuiYesNo guiyesno = new GuiYesNo(this, "", I18n.translateToLocal("schematic.instantBuildText"), 0);
               this.displayGuiScreen(guiyesno);
          }

     }

     public void save() {
          if (this.getTextField(9) != null) {
               this.tile.yOffest = this.getTextField(9).getInteger();
          }

          Client.sendData(EnumPacketServer.SchematicsTileSave, this.x, this.y, this.z, this.tile.writePartNBT(new NBTTagCompound()));
     }

     public void setGuiData(final NBTTagCompound compound) {
          if (compound.hasKey("Width")) {
               final List states = new ArrayList();
               NBTTagList list = compound.getTagList("Data", 10);

               for(int i = 0; i < list.tagCount(); ++i) {
                    states.add(NBTUtil.func_190008_d(list.getCompoundTagAt(i)));
               }

               this.selected = new ISchematic() {
                    public short getWidth() {
                         return compound.getShort("Width");
                    }

                    public int getTileEntitySize() {
                         return 0;
                    }

                    public NBTTagCompound getTileEntity(int i) {
                         return null;
                    }

                    public String getName() {
                         return compound.getString("SchematicName");
                    }

                    public short getLength() {
                         return compound.getShort("Length");
                    }

                    public short getHeight() {
                         return compound.getShort("Height");
                    }

                    public IBlockState getBlockState(int i) {
                         return (IBlockState)states.get(i);
                    }

                    public IBlockState getBlockState(int x, int y, int z) {
                         return this.getBlockState((y * this.getLength() + z) * this.getWidth() + x);
                    }

                    public NBTTagCompound getNBT() {
                         return null;
                    }
               };
               if (TileBuilder.DrawPos != null && TileBuilder.DrawPos.equals(this.tile.getPos())) {
                    SchematicWrapper wrapper = new SchematicWrapper(this.selected);
                    wrapper.rotation = this.tile.rotation;
                    this.tile.setDrawSchematic(wrapper);
               }

               this.scroll.setSelected(this.selected.getName());
               this.scroll.scrollTo(this.selected.getName());
          } else {
               this.tile.readPartNBT(compound);
          }

          this.func_73866_w_();
     }

     public void func_73878_a(boolean flag, int i) {
          if (flag) {
               Client.sendData(EnumPacketServer.SchematicsBuild, this.x, this.y, this.z);
               this.close();
               this.selected = null;
          } else {
               NoppesUtil.openGUI(this.player, this);
          }

     }

     public void scrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
          if (scroll.hasSelected()) {
               if (this.selected != null) {
                    this.getButton(3).setDisplay(0);
               }

               TileBuilder.SetDrawPos((BlockPos)null);
               this.tile.setDrawSchematic((SchematicWrapper)null);
               Client.sendData(EnumPacketServer.SchematicsSet, this.x, this.y, this.z, scroll.getSelected());
          }
     }

     public void setData(Vector list, HashMap data) {
          this.scroll.setList(list);
          if (this.selected != null) {
               this.scroll.setSelected(this.selected.getName());
          }

          this.func_73866_w_();
     }

     public void setSelected(String selected) {
     }

     public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
     }
}
