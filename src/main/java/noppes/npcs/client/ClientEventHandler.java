package noppes.npcs.client;

import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import noppes.npcs.LogWriter;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.client.renderer.MarkRenderer;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.schematics.SchematicWrapper;
import org.lwjgl.opengl.GL11;

public class ClientEventHandler {
     private int displayList = -1;

     @SubscribeEvent
     public void onRenderTick(RenderWorldLastEvent event) {
          EntityPlayer player = Minecraft.getMinecraft().player;
          if (TileBuilder.DrawPos != null && TileBuilder.DrawPos.func_177951_i(player.func_180425_c()) <= 1000000.0D) {
               TileEntity te = player.world.func_175625_s(TileBuilder.DrawPos);
               if (te != null && te instanceof TileBuilder) {
                    TileBuilder tile = (TileBuilder)te;
                    SchematicWrapper schem = tile.getSchematic();
                    if (schem != null) {
                         GlStateManager.func_179094_E();
                         RenderHelper.func_74519_b();
                         GlStateManager.func_179137_b((double)TileBuilder.DrawPos.getX() - TileEntityRendererDispatcher.field_147554_b, (double)TileBuilder.DrawPos.getY() - TileEntityRendererDispatcher.field_147555_c + 0.01D, (double)TileBuilder.DrawPos.getZ() - TileEntityRendererDispatcher.field_147552_d);
                         GlStateManager.func_179109_b(1.0F, (float)tile.yOffest, 1.0F);
                         if (tile.rotation % 2 == 0) {
                              this.drawSelectionBox(new BlockPos(schem.schema.getWidth(), schem.schema.getHeight(), schem.schema.getLength()));
                         } else {
                              this.drawSelectionBox(new BlockPos(schem.schema.getLength(), schem.schema.getHeight(), schem.schema.getWidth()));
                         }

                         if (TileBuilder.Compiled) {
                              GlStateManager.func_179148_o(this.displayList);
                         } else {
                              BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().func_175602_ab();
                              if (this.displayList >= 0) {
                                   GLAllocation.func_74523_b(this.displayList);
                              }

                              this.displayList = GLAllocation.func_74526_a(1);
                              GL11.glNewList(this.displayList, 4864);

                              try {
                                   for(int i = 0; i < schem.size && i < 25000; ++i) {
                                        int posX = i % schem.schema.getWidth();
                                        int posZ = (i - posX) / schem.schema.getWidth() % schem.schema.getLength();
                                        int posY = ((i - posX) / schem.schema.getWidth() - posZ) / schem.schema.getLength();
                                        IBlockState state = schem.schema.getBlockState(posX, posY, posZ);
                                        if (state.func_185901_i() != EnumBlockRenderType.INVISIBLE) {
                                             BlockPos pos = schem.rotatePos(posX, posY, posZ, tile.rotation);
                                             GlStateManager.func_179094_E();
                                             GlStateManager.func_179123_a();
                                             GlStateManager.enableRescaleNormal();
                                             GlStateManager.func_179109_b((float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
                                             Minecraft.getMinecraft().func_110434_K().bindTexture(TextureMap.field_110575_b);
                                             GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
                                             state = schem.rotationState(state, tile.rotation);

                                             try {
                                                  dispatcher.func_175016_a(state, 1.0F);
                                                  if (GL11.glGetError() != 0) {
                                                       break;
                                                  }
                                             } catch (Exception var24) {
                                             } finally {
                                                  GlStateManager.func_179099_b();
                                                  GlStateManager.func_179101_C();
                                                  GlStateManager.func_179121_F();
                                             }
                                        }
                                   }
                              } catch (Exception var26) {
                                   LogWriter.error("Error preview builder block", var26);
                              } finally {
                                   GL11.glEndList();
                                   if (GL11.glGetError() == 0) {
                                        TileBuilder.Compiled = true;
                                   }

                              }
                         }

                         RenderHelper.disableStandardItemLighting();
                         GlStateManager.func_179109_b(-1.0F, 0.0F, -1.0F);
                         GlStateManager.func_179121_F();
                    }
               }
          }
     }

     @SubscribeEvent
     public void post(Post event) {
          MarkData data = MarkData.get(event.getEntity());
          EntityPlayer player = Minecraft.getMinecraft().player;
          Iterator var4 = data.marks.iterator();

          while(var4.hasNext()) {
               MarkData.Mark m = (MarkData.Mark)var4.next();
               if (m.getType() != 0 && m.availability.isAvailable((EntityPlayer)player)) {
                    MarkRenderer.render(event.getEntity(), event.getX(), event.getY(), event.getZ(), m);
                    break;
               }
          }

     }

     public void drawSelectionBox(BlockPos pos) {
          GlStateManager.func_179090_x();
          GlStateManager.disableLighting();
          GlStateManager.func_179129_p();
          GlStateManager.func_179084_k();
          AxisAlignedBB bb = new AxisAlignedBB(BlockPos.field_177992_a, pos);
          RenderGlobal.func_189697_a(bb, 1.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.func_179098_w();
          GlStateManager.enableLighting();
          GlStateManager.func_179089_o();
          GlStateManager.func_179084_k();
     }
}
