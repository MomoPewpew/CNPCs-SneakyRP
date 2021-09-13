package noppes.npcs.controllers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import noppes.npcs.LogWriter;
import org.apache.logging.log4j.LogManager;

public class PixelmonHelper {
     public static boolean Enabled = Loader.isModLoaded("pixelmon");
     public static EventBus EVENT_BUS;
     public static Object storageManager;
     private static Method getPartyStorage;
     private static Method getPcStorage;
     private static Method getPokemonData;
     private static Method getPixelmonModel = null;
     private static Class modelSetupClass;
     private static Method modelSetupMethod;
     private static Class pixelmonClass;

     public static void load() {
          if (Enabled) {
               try {
                    Class c = Class.forName("com.pixelmonmod.pixelmon.Pixelmon");
                    storageManager = c.getDeclaredField("storageManager").get((Object)null);
                    EVENT_BUS = (EventBus)c.getDeclaredField("EVENT_BUS").get((Object)null);
                    c = Class.forName("com.pixelmonmod.pixelmon.api.storage.IStorageManager");
                    getPartyStorage = c.getMethod("getParty", EntityPlayerMP.class);
                    getPcStorage = c.getMethod("getPCForPlayer", EntityPlayerMP.class);
                    pixelmonClass = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base");
                    getPokemonData = pixelmonClass.getMethod("getPokemonData");
               } catch (Exception var1) {
                    LogWriter.except(var1);
                    Enabled = false;
               }

          }
     }

     public static void loadClient() {
          if (Enabled) {
               try {
                    Class c = Class.forName("com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client");
                    getPixelmonModel = c.getMethod("getModel");
                    modelSetupClass = Class.forName("com.pixelmonmod.pixelmon.client.models.PixelmonModelSmd");
                    modelSetupMethod = modelSetupClass.getMethod("setupForRender", c);
               } catch (Exception var1) {
                    LogWriter.except(var1);
                    Enabled = false;
               }

          }
     }

     public static List getPixelmonList() {
          List list = new ArrayList();
          if (!Enabled) {
               return list;
          } else {
               try {
                    Class c = Class.forName("com.pixelmonmod.pixelmon.enums.EnumPokemonModel");
                    Object[] array = c.getEnumConstants();
                    Object[] var3 = array;
                    int var4 = array.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                         Object ob = var3[var5];
                         list.add(ob.toString());
                    }
               } catch (Exception var7) {
                    LogManager.getLogger().error("getPixelmonList", var7);
               }

               return list;
          }
     }

     public static boolean isPixelmon(Entity entity) {
          if (!Enabled) {
               return false;
          } else {
               String s = EntityList.getEntityString(entity);
               return s == null ? false : s.contains("Pixelmon");
          }
     }

     public static String getName(EntityLivingBase entity) {
          if (Enabled && isPixelmon(entity)) {
               try {
                    Method m = entity.getClass().getMethod("getName");
                    return m.invoke(entity).toString();
               } catch (Exception var2) {
                    LogManager.getLogger().error("getName", var2);
                    return "";
               }
          } else {
               return "";
          }
     }

     public static Object getModel(EntityLivingBase entity) {
          try {
               return getPixelmonModel.invoke(entity);
          } catch (Exception var2) {
               LogManager.getLogger().error("getModel", var2);
               return null;
          }
     }

     public static void setupModel(EntityLivingBase entity, Object model) {
          try {
               if (modelSetupClass.isAssignableFrom(model.getClass())) {
                    modelSetupMethod.invoke(model, entity);
               }
          } catch (Exception var3) {
               LogManager.getLogger().error("setupModel", var3);
          }

     }

     public static Object getPokemonData(Entity entity) {
          try {
               return getPokemonData.invoke(entity);
          } catch (Exception var2) {
               var2.printStackTrace();
               return null;
          }
     }

     public static Object getParty(EntityPlayerMP player) {
          try {
               return getPartyStorage.invoke(storageManager, player);
          } catch (Exception var2) {
               var2.printStackTrace();
               return null;
          }
     }

     public static Object getPc(EntityPlayerMP player) {
          try {
               return getPcStorage.invoke(storageManager, player);
          } catch (Exception var2) {
               var2.printStackTrace();
               return null;
          }
     }

     public static Class getPixelmonClass() {
          return pixelmonClass;
     }
}
