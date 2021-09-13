package noppes.npcs.entity.data;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.BossInfo.Color;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class DataDisplay implements INPCDisplay {
     EntityNPCInterface npc;
     private String name;
     private String title = "";
     private int markovGeneratorId = 8;
     private int markovGender = 0;
     public byte skinType = 0;
     private String url = "";
     public GameProfile playerProfile;
     private String texture = "customnpcs:textures/entity/humanmale/steve.png";
     private String cloakTexture = "";
     private String glowTexture = "";
     private int visible = 0;
     private int modelSize = 5;
     private int showName = 0;
     private int skinColor = 16777215;
     private boolean disableLivingAnimation = false;
     private boolean noHitbox = false;
     private byte showBossBar = 0;
     private Color bossColor;

     public DataDisplay(EntityNPCInterface npc) {
          this.bossColor = Color.PINK;
          this.npc = npc;
          this.markovGeneratorId = (new Random()).nextInt(CustomNpcs.MARKOV_GENERATOR.length - 1);
          this.name = this.getRandomName();
     }

     public String getRandomName() {
          return CustomNpcs.MARKOV_GENERATOR[this.markovGeneratorId].fetch(this.markovGender);
     }

     public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
          nbttagcompound.func_74778_a("Name", this.name);
          nbttagcompound.func_74768_a("MarkovGeneratorId", this.markovGeneratorId);
          nbttagcompound.func_74768_a("MarkovGender", this.markovGender);
          nbttagcompound.func_74778_a("Title", this.title);
          nbttagcompound.func_74778_a("SkinUrl", this.url);
          nbttagcompound.func_74778_a("Texture", this.texture);
          nbttagcompound.func_74778_a("CloakTexture", this.cloakTexture);
          nbttagcompound.func_74778_a("GlowTexture", this.glowTexture);
          nbttagcompound.func_74774_a("UsingSkinUrl", this.skinType);
          if (this.playerProfile != null) {
               NBTTagCompound nbttagcompound1 = new NBTTagCompound();
               NBTUtil.func_180708_a(nbttagcompound1, this.playerProfile);
               nbttagcompound.func_74782_a("SkinUsername", nbttagcompound1);
          }

          nbttagcompound.func_74768_a("Size", this.modelSize);
          nbttagcompound.func_74768_a("ShowName", this.showName);
          nbttagcompound.func_74768_a("SkinColor", this.skinColor);
          nbttagcompound.func_74768_a("NpcVisible", this.visible);
          nbttagcompound.func_74757_a("NoLivingAnimation", this.disableLivingAnimation);
          nbttagcompound.func_74757_a("IsStatue", this.noHitbox);
          nbttagcompound.func_74774_a("BossBar", this.showBossBar);
          nbttagcompound.func_74768_a("BossColor", this.bossColor.ordinal());
          return nbttagcompound;
     }

     public void readToNBT(NBTTagCompound nbttagcompound) {
          this.setName(nbttagcompound.func_74779_i("Name"));
          this.setMarkovGeneratorId(nbttagcompound.func_74762_e("MarkovGeneratorId"));
          this.setMarkovGender(nbttagcompound.func_74762_e("MarkovGender"));
          this.title = nbttagcompound.func_74779_i("Title");
          int prevSkinType = this.skinType;
          String prevTexture = this.texture;
          String prevUrl = this.url;
          String prevPlayer = this.getSkinPlayer();
          this.url = nbttagcompound.func_74779_i("SkinUrl");
          this.skinType = nbttagcompound.func_74771_c("UsingSkinUrl");
          this.texture = nbttagcompound.func_74779_i("Texture");
          this.cloakTexture = nbttagcompound.func_74779_i("CloakTexture");
          this.glowTexture = nbttagcompound.func_74779_i("GlowTexture");
          this.playerProfile = null;
          if (this.skinType == 1) {
               if (nbttagcompound.func_150297_b("SkinUsername", 10)) {
                    this.playerProfile = NBTUtil.func_152459_a(nbttagcompound.func_74775_l("SkinUsername"));
               } else if (nbttagcompound.func_150297_b("SkinUsername", 8) && !StringUtils.func_151246_b(nbttagcompound.func_74779_i("SkinUsername"))) {
                    this.playerProfile = new GameProfile((UUID)null, nbttagcompound.func_74779_i("SkinUsername"));
               }

               this.loadProfile();
          }

          this.modelSize = ValueUtil.CorrectInt(nbttagcompound.func_74762_e("Size"), 1, 30);
          this.showName = nbttagcompound.func_74762_e("ShowName");
          if (nbttagcompound.func_74764_b("SkinColor")) {
               this.skinColor = nbttagcompound.func_74762_e("SkinColor");
          }

          this.visible = nbttagcompound.func_74762_e("NpcVisible");
          this.disableLivingAnimation = nbttagcompound.func_74767_n("NoLivingAnimation");
          this.noHitbox = nbttagcompound.func_74767_n("IsStatue");
          this.setBossbar(nbttagcompound.func_74771_c("BossBar"));
          this.setBossColor(nbttagcompound.func_74762_e("BossColor"));
          if (prevSkinType != this.skinType || !this.texture.equals(prevTexture) || !this.url.equals(prevUrl) || !this.getSkinPlayer().equals(prevPlayer)) {
               this.npc.textureLocation = null;
          }

          this.npc.textureGlowLocation = null;
          this.npc.textureCloakLocation = null;
          this.npc.updateHitbox();
     }

     public void loadProfile() {
          if (this.playerProfile != null && !StringUtils.func_151246_b(this.playerProfile.getName()) && this.npc.func_184102_h() != null && (!this.playerProfile.isComplete() || !this.playerProfile.getProperties().containsKey("textures"))) {
               GameProfile gameprofile = this.npc.func_184102_h().func_152358_ax().func_152655_a(this.playerProfile.getName());
               if (gameprofile != null) {
                    Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object)null);
                    if (property == null) {
                         gameprofile = this.npc.func_184102_h().func_147130_as().fillProfileProperties(gameprofile, true);
                    }

                    this.playerProfile = gameprofile;
               }
          }

     }

     public boolean showName() {
          if (this.npc.isKilled()) {
               return false;
          } else {
               return this.showName == 0 || this.showName == 2 && this.npc.isAttacking();
          }
     }

     public String getName() {
          return this.name;
     }

     public void setName(String name) {
          if (!this.name.equals(name)) {
               this.name = name;
               this.npc.bossInfo.func_186739_a(this.npc.func_145748_c_());
               this.npc.updateClient = true;
          }
     }

     public int getShowName() {
          return this.showName;
     }

     public void setShowName(int type) {
          if (type != this.showName) {
               this.showName = ValueUtil.CorrectInt(type, 0, 2);
               this.npc.updateClient = true;
          }
     }

     public int getMarkovGender() {
          return this.markovGender;
     }

     public void setMarkovGender(int gender) {
          if (this.markovGender != gender) {
               this.markovGender = ValueUtil.CorrectInt(gender, 0, 2);
          }
     }

     public int getMarkovGeneratorId() {
          return this.markovGeneratorId;
     }

     public void setMarkovGeneratorId(int id) {
          if (this.markovGeneratorId != id) {
               this.markovGeneratorId = ValueUtil.CorrectInt(id, 0, CustomNpcs.MARKOV_GENERATOR.length - 1);
          }
     }

     public String getTitle() {
          return this.title;
     }

     public void setTitle(String title) {
          if (!this.title.equals(title)) {
               this.title = title;
               this.npc.updateClient = true;
          }
     }

     public String getSkinUrl() {
          return this.url;
     }

     public void setSkinUrl(String url) {
          if (!this.url.equals(url)) {
               this.url = url;
               if (url.isEmpty()) {
                    this.skinType = 0;
               } else {
                    this.skinType = 2;
               }

               this.npc.updateClient = true;
          }
     }

     public String getSkinPlayer() {
          return this.playerProfile == null ? "" : this.playerProfile.getName();
     }

     public void setSkinPlayer(String name) {
          if (name != null && !name.isEmpty()) {
               this.playerProfile = new GameProfile((UUID)null, name);
               this.skinType = 1;
          } else {
               this.playerProfile = null;
               this.skinType = 0;
          }

          this.npc.updateClient = true;
     }

     public String getSkinTexture() {
          return this.texture;
     }

     public void setSkinTexture(String texture) {
          if (texture != null && !this.texture.equals(texture)) {
               this.texture = texture.toLowerCase();
               this.npc.textureLocation = null;
               this.skinType = 0;
               this.npc.updateClient = true;
          }
     }

     public String getOverlayTexture() {
          return this.glowTexture;
     }

     public void setOverlayTexture(String texture) {
          if (!this.glowTexture.equals(texture)) {
               this.glowTexture = texture;
               this.npc.textureGlowLocation = null;
               this.npc.updateClient = true;
          }
     }

     public String getCapeTexture() {
          return this.cloakTexture;
     }

     public void setCapeTexture(String texture) {
          if (!this.cloakTexture.equals(texture)) {
               this.cloakTexture = texture.toLowerCase();
               this.npc.textureCloakLocation = null;
               this.npc.updateClient = true;
          }
     }

     public boolean getHasLivingAnimation() {
          return !this.disableLivingAnimation;
     }

     public void setHasLivingAnimation(boolean enabled) {
          this.disableLivingAnimation = !enabled;
          this.npc.updateClient = true;
     }

     public int getBossbar() {
          return this.showBossBar;
     }

     public void setBossbar(int type) {
          if (type != this.showBossBar) {
               this.showBossBar = (byte)ValueUtil.CorrectInt(type, 0, 2);
               this.npc.bossInfo.func_186758_d(this.showBossBar == 1);
               this.npc.updateClient = true;
          }
     }

     public int getBossColor() {
          return this.bossColor.ordinal();
     }

     public void setBossColor(int color) {
          if (color >= 0 && color < Color.values().length) {
               this.bossColor = Color.values()[color];
               this.npc.bossInfo.func_186745_a(this.bossColor);
          } else {
               throw new CustomNPCsException("Invalid Boss Color: " + color, new Object[0]);
          }
     }

     public int getVisible() {
          return this.visible;
     }

     public void setVisible(int type) {
          if (type != this.visible) {
               this.visible = ValueUtil.CorrectInt(type, 0, 2);
               this.npc.updateClient = true;
          }
     }

     public int getSize() {
          return this.modelSize;
     }

     public void setSize(int size) {
          if (this.modelSize != size) {
               this.modelSize = ValueUtil.CorrectInt(size, 1, 30);
               this.npc.updateClient = true;
          }
     }

     public void setModelScale(int part, float x, float y, float z) {
          ModelData modeldata = ((EntityCustomNpc)this.npc).modelData;
          ModelPartConfig model = null;
          if (part == 0) {
               model = modeldata.getPartConfig(EnumParts.HEAD);
          } else if (part == 1) {
               model = modeldata.getPartConfig(EnumParts.BODY);
          } else if (part == 2) {
               model = modeldata.getPartConfig(EnumParts.ARM_LEFT);
          } else if (part == 3) {
               model = modeldata.getPartConfig(EnumParts.ARM_RIGHT);
          } else if (part == 4) {
               model = modeldata.getPartConfig(EnumParts.LEG_LEFT);
          } else if (part == 5) {
               model = modeldata.getPartConfig(EnumParts.LEG_RIGHT);
          }

          if (model == null) {
               throw new CustomNPCsException("Unknown part: " + part, new Object[0]);
          } else {
               model.setScale(x, y, z);
               this.npc.updateClient = true;
          }
     }

     public float[] getModelScale(int part) {
          ModelData modeldata = ((EntityCustomNpc)this.npc).modelData;
          ModelPartConfig model = null;
          if (part == 0) {
               model = modeldata.getPartConfig(EnumParts.HEAD);
          } else if (part == 1) {
               model = modeldata.getPartConfig(EnumParts.BODY);
          } else if (part == 2) {
               model = modeldata.getPartConfig(EnumParts.ARM_LEFT);
          } else if (part == 3) {
               model = modeldata.getPartConfig(EnumParts.ARM_RIGHT);
          } else if (part == 4) {
               model = modeldata.getPartConfig(EnumParts.LEG_LEFT);
          } else if (part == 5) {
               model = modeldata.getPartConfig(EnumParts.LEG_RIGHT);
          }

          if (model == null) {
               throw new CustomNPCsException("Unknown part: " + part, new Object[0]);
          } else {
               return new float[]{model.scaleX, model.scaleY, model.scaleZ};
          }
     }

     public int getTint() {
          return this.skinColor;
     }

     public void setTint(int color) {
          if (color != this.skinColor) {
               this.skinColor = color;
               this.npc.updateClient = true;
          }
     }

     public void setModel(String id) {
          ModelData modeldata = ((EntityCustomNpc)this.npc).modelData;
          if (id == null) {
               if (modeldata.entityClass == null) {
                    return;
               }

               modeldata.entityClass = null;
               this.npc.updateClient = true;
          } else {
               ResourceLocation resource = new ResourceLocation(id);
               Entity entity = EntityList.func_188429_b(resource, this.npc.field_70170_p);
               if (entity == null) {
                    throw new CustomNPCsException("Failed to create an entity from given id: " + id, new Object[0]);
               }

               modeldata.setEntityName(entity.getClass().getCanonicalName());
               this.npc.updateClient = true;
          }

     }

     public String getModel() {
          ModelData modeldata = ((EntityCustomNpc)this.npc).modelData;
          if (modeldata.entityClass == null) {
               return null;
          } else {
               String name = modeldata.entityClass.getCanonicalName();
               Iterator var3 = ForgeRegistries.ENTITIES.getValues().iterator();

               EntityEntry ent;
               Class c;
               do {
                    if (!var3.hasNext()) {
                         return null;
                    }

                    ent = (EntityEntry)var3.next();
                    c = ent.getEntityClass();
               } while(!c.getCanonicalName().equals(name) || !EntityLivingBase.class.isAssignableFrom(c));

               return ent.getRegistryName().toString();
          }
     }

     public boolean getHasHitbox() {
          return !this.noHitbox;
     }

     public void setHasHitbox(boolean bo) {
          if (this.noHitbox == bo) {
               this.noHitbox = !bo;
               this.npc.updateClient = true;
          }
     }
}
