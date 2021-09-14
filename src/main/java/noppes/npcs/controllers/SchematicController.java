package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.schematics.Blueprint;
import noppes.npcs.schematics.BlueprintUtil;
import noppes.npcs.schematics.ISchematic;
import noppes.npcs.schematics.Schematic;
import noppes.npcs.schematics.SchematicWrapper;

public class SchematicController {
     public static SchematicController Instance = new SchematicController();
     private SchematicWrapper building = null;
     private ICommandSender buildStarter = null;
     private int buildingPercentage = 0;
     public List included = Arrays.asList("archery_range.schematic", "bakery.schematic", "barn.schematic", "building_site.schematic", "chapel.schematic", "church.schematic", "gate.schematic", "glassworks.schematic", "guard_Tower.schematic", "guild_house.schematic", "house.schematic", "house_small.schematic", "inn.schematic", "library.schematic", "lighthouse.schematic", "mill.schematic", "observatory.schematic", "ship.schematic", "shop.schematic", "stall.schematic", "stall2.schematic", "stall3.schematic", "tier_house1.schematic", "tier_house2.schematic", "tier_house3.schematic", "tower.schematic", "wall.schematic", "wall_corner.schematic");

     public List list() {
          List list = new ArrayList();
          list.addAll(this.included);
          File[] var2 = this.getDir().listFiles();
          int var3 = var2.length;

          for(int var4 = 0; var4 < var3; ++var4) {
               File file = var2[var4];
               String name = file.getName();
               if (name.toLowerCase().endsWith(".schematic") || name.toLowerCase().endsWith(".blueprint")) {
                    list.add(name);
               }
          }

          Collections.sort(list);
          return list;
     }

     public File getDir() {
          File dir = new File(CustomNpcs.getWorldSaveDirectory(), "schematics");
          if (!dir.exists()) {
               dir.mkdir();
          }

          return dir;
     }

     public void info(ICommandSender sender) {
          if (this.building == null) {
               this.sendMessage(sender, "Nothing is being build");
          } else {
               this.sendMessage(sender, "Already building: " + this.building.schema.getName() + " - " + this.building.getPercentage() + "%");
               if (this.buildStarter != null) {
                    this.sendMessage(sender, "Build started by: " + this.buildStarter.getName());
               }
          }

     }

     private void sendMessage(ICommandSender sender, String message) {
          if (sender != null) {
               sender.sendMessage(new TextComponentString(message));
          }
     }

     public void stop(ICommandSender sender) {
          if (this.building != null && this.building.isBuilding) {
               this.sendMessage(sender, "Stopped building: " + this.building.schema.getName());
               this.building = null;
          } else {
               this.sendMessage(sender, "Not building");
          }

     }

     public void build(SchematicWrapper schem, ICommandSender sender) {
          if (this.building != null && this.building.isBuilding) {
               this.info(sender);
          } else {
               this.buildingPercentage = 0;
               this.building = schem;
               this.building.isBuilding = true;
               this.buildStarter = sender;
          }
     }

     public void updateBuilding() {
          if (this.building != null) {
               this.building.build();
               if (this.buildStarter != null && this.building.getPercentage() - this.buildingPercentage >= 10) {
                    this.sendMessage(this.buildStarter, "Building at " + this.building.getPercentage() + "%");
                    this.buildingPercentage = this.building.getPercentage();
               }

               if (!this.building.isBuilding) {
                    if (this.buildStarter != null) {
                         this.sendMessage(this.buildStarter, "Building finished");
                    }

                    this.building = null;
               }

          }
     }

     public SchematicWrapper load(String name) {
          InputStream stream = null;
          if (this.included.contains(name)) {
               stream = MinecraftServer.class.getResourceAsStream("/assets/customnpcs/schematics/" + name);
          }

          if (stream == null) {
               File file = new File(this.getDir(), name);
               if (!file.exists()) {
                    return null;
               }

               try {
                    stream = new FileInputStream(file);
               } catch (FileNotFoundException var6) {
                    return null;
               }
          }

          try {
               NBTTagCompound compound = CompressedStreamTools.readCompressed((InputStream)stream);
               ((InputStream)stream).close();
               if (name.toLowerCase().endsWith(".blueprint")) {
                    Blueprint bp = BlueprintUtil.readBlueprintFromNBT(compound);
                    bp.setName(name);
                    return new SchematicWrapper(bp);
               } else {
                    Schematic schema = new Schematic(name);
                    schema.load(compound);
                    return new SchematicWrapper(schema);
               }
          } catch (IOException var5) {
               LogWriter.except(var5);
               return null;
          }
     }

     public void save(ICommandSender sender, String name, int type, BlockPos pos, short height, short width, short length) {
          name = name.replace(" ", "_");
          if (!this.included.contains(name)) {
               World world = sender.getEntityWorld();
               File file = null;
               ISchematic schema = null;
               if (type == 0) {
                    file = new File(this.getDir(), name + ".schematic");
                    schema = Schematic.Create(world, name, pos, height, width, length);
               } else if (type == 1) {
                    file = new File(this.getDir(), name + ".blueprint");
                    schema = BlueprintUtil.createBlueprint(world, pos, width, length, height);
               }

               NoppesUtilServer.NotifyOPs("Schematic " + name + " succesfully created");

               try {
                    CompressedStreamTools.writeCompressed(((ISchematic)schema).getNBT(), new FileOutputStream(file));
               } catch (Exception var12) {
                    var12.printStackTrace();
               }

          }
     }
}
