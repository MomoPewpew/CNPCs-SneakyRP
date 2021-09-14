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
          if (TileBuilder.DrawPos != null && TileBuilder.DrawPos.distanceSq(player.getPosition()) <= 1000000.0D) {
               TileEntity te = player.world.getTileEntity(TileBuilder.DrawPos);
               if (te != null && te instanceof TileBuilder) {
                    TileBuilder tile = (TileBuilder)te;
                    SchematicWrapper schem = tile.getSchematic();
                    if (schem != null) {
                         GlStateManager.pushMatrix();
                         RenderHelper.enableStandardItemLighting();
                         GlStateManager.translate((double)TileBuilder.DrawPos.getX() - TileEntityRendererDispatcher.staticPlayerX, (double)TileBuilder.DrawPos.getY() - TileEntityRendererDispatcher.staticPlayerY + 0.01D, (double)TileBuilder.DrawPos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);
                         GlStateManager.translate(1.0F, (float)tile.yOffest, 1.0F);
                         if (tile.rotation % 2 == 0) {
                              this.drawSelectionBox(new BlockPos(schem.schema.getWidth(), schem.schema.getHeight(), schem.schema.getLength()));
                         } else {
                              this.drawSelectionBox(new BlockPos(schem.schema.getLength(), schem.schema.getHeight(), schem.schema.getWidth()));
                         }

                         if (TileBuilder.Compiled) {
                              GlStateManager.callList(this.displayList);
                         } else {
                              BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                              if (this.displayList >= 0) {
                                   GLAllocation.deleteDisplayLists(this.displayList);
                              }

                              this.displayList = GLAllocation.generateDisplayLists(1);
                              GL11.glNewList(this.displayList, 4864);

                              try {
                                   for(int i = 0; i < schem.size && i < 25000; ++i) {
                                        int posX = i % schem.schema.getWidth();
                                        int posZ = (i - posX) / schem.schema.getWidth() % schem.schema.getLength();
                                        int posY = ((i - posX) / schem.schema.getWidth() - posZ) / schem.schema.getLength();
                                        IBlockState state = schem.schema.getBlockState(posX, posY, posZ);
                                        if (state.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                                             BlockPos pos = schem.rotatePos(posX, posY, posZ, tile.rotation);
                                             GlStateManager.pushMatrix();
                                             GlStateManager.pushAttrib();
                                             GlStateManager.enableRescaleNormal();
                                             GlStateManager.translate((float)pos.getX(), (float)pos.getY(), (float)pos.getZ());
                                             Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                                             GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                                             state = schem.rotationState(state, tile.rotation);

                                             try {
                                                  dispatcher.renderBlockBrightness(state, 1.0F);
                                                  if (GL11.glGetError() != 0) {
                                                       break;
                                                  }
                                             } catch (Exception var24) {
                                             } finally {
                                                  GlStateManager.popAttrib();
                                                  GlStateManager.disableRescaleNormal();
                                                  GlStateManager.popMatrix();
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
                         GlStateManager.translate(-1.0F, 0.0F, -1.0F);
                         GlStateManager.popMatrix();
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
          GlStateManager.disableTexture2D();
          GlStateManager.disableLighting();
          GlStateManager.disableCull();
          GlStateManager.disableBlend();
          AxisAlignedBB bb = new AxisAlignedBB(BlockPos.ORIGIN, pos);
          RenderGlobal.drawSelectionBoundingBox(bb, 1.0F, 0.0F, 0.0F, 1.0F);
          GlStateManager.enableTexture2D();
          GlStateManager.enableLighting();
          GlStateManager.enableCull();
          GlStateManager.disableBlend();
     }
}
