package noppes.npcs.schematics;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class BlueprintUtil {
     public static Blueprint createBlueprint(World world, BlockPos pos, short sizeX, short sizeY, short sizeZ) {
          return createBlueprint(world, pos, sizeX, sizeY, sizeZ, (String)null);
     }

     public static Blueprint createBlueprint(World world, BlockPos pos, short sizeX, short sizeY, short sizeZ, String name, String... architects) {
          List pallete = new ArrayList();
          short[][][] structure = new short[sizeY][sizeZ][sizeX];
          List tileEntities = new ArrayList();
          List requiredMods = new ArrayList();

          for(short y = 0; y < sizeY; ++y) {
               for(short z = 0; z < sizeZ; ++z) {
                    for(short x = 0; x < sizeX; ++x) {
                         IBlockState state = world.getBlockState(pos.add(x, y, z));
                         String modName;
                         if (!requiredMods.contains(modName = state.getBlock().getRegistryName().getResourceDomain())) {
                              requiredMods.add(modName);
                         }

                         TileEntity te = world.getTileEntity(pos.add(x, y, z));
                         if (te != null) {
                              NBTTagCompound teTag = te.serializeNBT();
                              teTag.setShort("x", x);
                              teTag.setShort("y", y);
                              teTag.setShort("z", z);
                              tileEntities.add(teTag);
                         }

                         if (!pallete.contains(state)) {
                              pallete.add(state);
                         }

                         structure[y][z][x] = (short)pallete.indexOf(state);
                    }
               }
          }

          IBlockState[] states = new IBlockState[pallete.size()];
          states = (IBlockState[])pallete.toArray(states);
          NBTTagCompound[] tes = new NBTTagCompound[tileEntities.size()];
          tes = (NBTTagCompound[])tileEntities.toArray(tes);
          Blueprint schem = new Blueprint(sizeX, sizeY, sizeZ, (short)((byte)pallete.size()), states, structure, tes, requiredMods);
          if (name != null) {
               schem.setName(name);
          }

          if (architects != null) {
               schem.setArchitects(architects);
          }

          return schem;
     }

     public static NBTTagCompound writeBlueprintToNBT(Blueprint schem) {
          NBTTagCompound tag = new NBTTagCompound();
          tag.setByte("version", (byte)1);
          tag.setShort("size_x", schem.getSizeX());
          tag.setShort("size_y", schem.getSizeY());
          tag.setShort("size_z", schem.getSizeZ());
          IBlockState[] palette = schem.getPallete();
          NBTTagList paletteTag = new NBTTagList();

          for(short i = 0; i < schem.getPalleteSize(); ++i) {
               NBTTagCompound state = new NBTTagCompound();
               NBTUtil.writeBlockState(state, palette[i]);
               paletteTag.appendTag(state);
          }

          tag.setTag("palette", paletteTag);
          int[] blockInt = convertBlocksToSaveData(schem.getStructure(), schem.getSizeX(), schem.getSizeY(), schem.getSizeZ());
          tag.setIntArray("blocks", blockInt);
          NBTTagList finishedTes = new NBTTagList();
          NBTTagCompound[] tes = schem.getTileEntities();

          for(int i = 0; i < tes.length; ++i) {
               finishedTes.appendTag(tes[i]);
          }

          tag.setTag("tile_entities", finishedTes);
          List requiredMods = schem.getRequiredMods();
          NBTTagList modsList = new NBTTagList();

          for(int i = 0; i < requiredMods.size(); ++i) {
               modsList.appendTag(new NBTTagString((String)requiredMods.get(i)));
          }

          tag.setTag("required_mods", modsList);
          String name = schem.getName();
          String[] architects = schem.getArchitects();
          if (name != null) {
               tag.setString("name", name);
          }

          if (architects != null) {
               NBTTagList architectsTag = new NBTTagList();
               String[] var12 = architects;
               int var13 = architects.length;

               for(int var14 = 0; var14 < var13; ++var14) {
                    String architect = var12[var14];
                    architectsTag.appendTag(new NBTTagString(architect));
               }

               tag.setTag("architects", architectsTag);
          }

          return tag;
     }

     public static Blueprint readBlueprintFromNBT(NBTTagCompound tag) {
          byte version = tag.getByte("version");
          if (version != 1) {
               return null;
          } else {
               short sizeX = tag.getShort("size_x");
               short sizeY = tag.getShort("size_y");
               short sizeZ = tag.getShort("size_z");
               List requiredMods = new ArrayList();
               NBTTagList modsList = (NBTTagList)tag.getTag("required_mods");
               short modListSize = (short)modsList.tagCount();

               for(int i = 0; i < modListSize; ++i) {
                    requiredMods.add(((NBTTagString)modsList.get(i)).getString());
                    if (!Loader.isModLoaded((String)requiredMods.get(i))) {
                         Logger.getGlobal().log(Level.WARNING, "Couldn't load Blueprint, the following mod is missing: " + (String)requiredMods.get(i));
                         return null;
                    }
               }

               NBTTagList paletteTag = (NBTTagList)tag.getTag("palette");
               short paletteSize = (short)paletteTag.tagCount();
               IBlockState[] palette = new IBlockState[paletteSize];

               for(short i = 0; i < palette.length; ++i) {
                    palette[i] = NBTUtil.readBlockState(paletteTag.getCompoundTagAt(i));
               }

               short[][][] blocks = convertSaveDataToBlocks(tag.getIntArray("blocks"), sizeX, sizeY, sizeZ);
               NBTTagList teTag = (NBTTagList)tag.getTag("tile_entities");
               NBTTagCompound[] tileEntities = new NBTTagCompound[teTag.tagCount()];

               for(short i = 0; i < tileEntities.length; ++i) {
                    tileEntities[i] = teTag.getCompoundTagAt(i);
               }

               Blueprint schem = new Blueprint(sizeX, sizeY, sizeZ, paletteSize, palette, blocks, tileEntities, requiredMods);
               if (tag.hasKey("name")) {
                    schem.setName(tag.getString("name"));
               }

               if (tag.hasKey("architects")) {
                    NBTTagList architectsTag = (NBTTagList)tag.getTag("architects");
                    String[] architects = new String[architectsTag.tagCount()];

                    for(int i = 0; i < architectsTag.tagCount(); ++i) {
                         architects[i] = architectsTag.getStringTagAt(i);
                    }

                    schem.setArchitects(architects);
               }

               return schem;
          }
     }

     public static void writeToFile(OutputStream os, Blueprint schem) {
          try {
               CompressedStreamTools.writeCompressed(writeBlueprintToNBT(schem), os);
          } catch (IOException var3) {
               var3.printStackTrace();
          }

     }

     public static Blueprint readFromFile(InputStream is) {
          try {
               NBTTagCompound tag = CompressedStreamTools.readCompressed(is);
               return readBlueprintFromNBT(tag);
          } catch (IOException var3) {
               var3.printStackTrace();
               return null;
          }
     }

     private static int[] convertBlocksToSaveData(short[][][] multDimArray, short sizeX, short sizeY, short sizeZ) {
          short[] oneDimArray = new short[sizeX * sizeY * sizeZ];
          int j = 0;

          short z;
          for(short y = 0; y < sizeY; ++y) {
               for(z = 0; z < sizeZ; ++z) {
                    for(short x = 0; x < sizeX; ++x) {
                         oneDimArray[j++] = multDimArray[y][z][x];
                    }
               }
          }

          int[] ints = new int[(int)Math.ceil((double)((float)oneDimArray.length / 2.0F))];
          int currentInt = false;

          int currentInt;
          for(int i = 1; i < oneDimArray.length; i += 2) {
               z = oneDimArray[i - 1];
               currentInt = z << 16 | oneDimArray[i];
               ints[(int)Math.ceil((double)((float)i / 2.0F)) - 1] = currentInt;
               currentInt = false;
          }

          if (oneDimArray.length % 2 == 1) {
               currentInt = oneDimArray[oneDimArray.length - 1] << 16;
               ints[ints.length - 1] = currentInt;
          }

          return ints;
     }

     public static short[][][] convertSaveDataToBlocks(int[] ints, short sizeX, short sizeY, short sizeZ) {
          short[] oneDimArray = new short[ints.length * 2];

          for(int i = 0; i < ints.length; ++i) {
               oneDimArray[i * 2] = (short)(ints[i] >> 16);
               oneDimArray[i * 2 + 1] = (short)ints[i];
          }

          short[][][] multDimArray = new short[sizeY][sizeZ][sizeX];
          int i = 0;

          for(short y = 0; y < sizeY; ++y) {
               for(short z = 0; z < sizeZ; ++z) {
                    for(short x = 0; x < sizeX; ++x) {
                         multDimArray[y][z][x] = oneDimArray[i++];
                    }
               }
          }

          return multDimArray;
     }
}
