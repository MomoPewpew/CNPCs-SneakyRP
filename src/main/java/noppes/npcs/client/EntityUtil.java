package noppes.npcs.client;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityUtil {
     public static void Copy(EntityLivingBase copied, EntityLivingBase entity) {
          entity.world = copied.world;
          entity.field_70725_aQ = copied.field_70725_aQ;
          entity.field_70140_Q = copied.field_70140_Q;
          entity.field_70141_P = copied.field_70140_Q;
          entity.field_70122_E = copied.field_70122_E;
          entity.field_82151_R = copied.field_82151_R;
          entity.field_191988_bg = copied.field_191988_bg;
          entity.field_70702_br = copied.field_70702_br;
          entity.func_70107_b(copied.field_70165_t, copied.field_70163_u, copied.field_70161_v);
          entity.func_174826_a(copied.getEntityBoundingBox());
          entity.field_70169_q = copied.field_70169_q;
          entity.field_70167_r = copied.field_70167_r;
          entity.field_70166_s = copied.field_70166_s;
          entity.motionX = copied.motionX;
          entity.motionY = copied.motionY;
          entity.motionZ = copied.motionZ;
          entity.field_70177_z = copied.field_70177_z;
          entity.field_70126_B = copied.field_70126_B;
          entity.field_70125_A = copied.field_70125_A;
          entity.field_70127_C = copied.field_70127_C;
          entity.field_70759_as = copied.field_70759_as;
          entity.field_70758_at = copied.field_70758_at;
          entity.field_70761_aq = copied.field_70761_aq;
          entity.field_70760_ar = copied.field_70760_ar;
          entity.field_70726_aT = copied.field_70726_aT;
          entity.field_70727_aS = copied.field_70727_aS;
          entity.field_70142_S = copied.field_70142_S;
          entity.field_70137_T = copied.field_70137_T;
          entity.field_70136_U = copied.field_70136_U;
          entity.field_70721_aZ = copied.field_70721_aZ;
          entity.field_184618_aE = copied.field_184618_aE;
          entity.field_184619_aG = copied.field_184619_aG;
          entity.field_70733_aJ = copied.field_70733_aJ;
          entity.field_70732_aI = copied.field_70732_aI;
          entity.field_82175_bq = copied.field_82175_bq;
          entity.field_110158_av = copied.field_110158_av;
          entity.func_70606_j(Math.min(copied.func_110143_aJ(), entity.getMaxHealth()));
          entity.field_70128_L = copied.field_70128_L;
          entity.field_70725_aQ = copied.field_70725_aQ;
          entity.field_70173_aa = copied.field_70173_aa;
          entity.getEntityData().func_179237_a(copied.getEntityData());
          if (entity instanceof EntityPlayer && copied instanceof EntityPlayer) {
               EntityPlayer ePlayer = (EntityPlayer)entity;
               EntityPlayer cPlayer = (EntityPlayer)copied;
               ePlayer.field_71109_bG = cPlayer.field_71109_bG;
               ePlayer.field_71107_bF = cPlayer.field_71107_bF;
               ePlayer.field_71091_bM = cPlayer.field_71091_bM;
               ePlayer.field_71096_bN = cPlayer.field_71096_bN;
               ePlayer.field_71097_bO = cPlayer.field_71097_bO;
               ePlayer.field_71094_bP = cPlayer.field_71094_bP;
               ePlayer.field_71095_bQ = cPlayer.field_71095_bQ;
               ePlayer.field_71085_bR = cPlayer.field_71085_bR;
          }

          if (entity instanceof EntityDragon) {
               entity.field_70177_z += 180.0F;
          }

          if (entity instanceof EntityChicken) {
               ((EntityChicken)entity).field_70883_f = copied.field_70122_E ? 0.0F : 1.0F;
          }

          EntityEquipmentSlot[] var6 = EntityEquipmentSlot.values();
          int var9 = var6.length;

          for(int var4 = 0; var4 < var9; ++var4) {
               EntityEquipmentSlot slot = var6[var4];
               entity.setItemStackToSlot(slot, copied.func_184582_a(slot));
          }

          if (copied instanceof EntityNPCInterface && entity instanceof EntityNPCInterface) {
               EntityNPCInterface npc = (EntityNPCInterface)copied;
               EntityNPCInterface target = (EntityNPCInterface)entity;
               target.textureLocation = npc.textureLocation;
               target.textureGlowLocation = npc.textureGlowLocation;
               target.textureCloakLocation = npc.textureCloakLocation;
               target.display = npc.display;
               target.inventory = npc.inventory;
               target.currentAnimation = npc.currentAnimation;
               target.setDataWatcher(npc.func_184212_Q());
          }

          if (entity instanceof EntityCustomNpc && copied instanceof EntityCustomNpc) {
               EntityCustomNpc npc = (EntityCustomNpc)copied;
               EntityCustomNpc target = (EntityCustomNpc)entity;
               target.modelData = npc.modelData.copy();
               target.modelData.setEntityClass((Class)null);
          }

     }

     public static void setRecentlyHit(EntityLivingBase entity) {
          entity.field_70718_bc = 100;
     }
}
