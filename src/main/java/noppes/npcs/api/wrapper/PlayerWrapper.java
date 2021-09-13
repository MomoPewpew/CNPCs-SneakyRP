package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldSettings;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.IPos;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.entity.data.IPixelmonPlayerData;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerDialogData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.util.ValueUtil;

public class PlayerWrapper extends EntityLivingBaseWrapper implements IPlayer {
     private IContainer inventory;
     private Object pixelmonPartyStorage;
     private Object pixelmonPCStorage;
     private final IData storeddata = new IData() {
          public void put(String key, Object value) {
               NBTTagCompound compound = this.getStoredCompound();
               if (value instanceof Number) {
                    compound.func_74780_a(key, ((Number)value).doubleValue());
               } else if (value instanceof String) {
                    compound.func_74778_a(key, (String)value);
               }

          }

          public Object get(String key) {
               NBTTagCompound compound = this.getStoredCompound();
               if (!compound.func_74764_b(key)) {
                    return null;
               } else {
                    NBTBase base = compound.func_74781_a(key);
                    return base instanceof NBTPrimitive ? ((NBTPrimitive)base).func_150286_g() : ((NBTTagString)base).func_150285_a_();
               }
          }

          public void remove(String key) {
               NBTTagCompound compound = this.getStoredCompound();
               compound.func_82580_o(key);
          }

          public boolean has(String key) {
               return this.getStoredCompound().func_74764_b(key);
          }

          public void clear() {
               PlayerData data = PlayerData.get((EntityPlayer)PlayerWrapper.this.entity);
               data.scriptStoreddata = new NBTTagCompound();
          }

          private NBTTagCompound getStoredCompound() {
               PlayerData data = PlayerData.get((EntityPlayer)PlayerWrapper.this.entity);
               return data.scriptStoreddata;
          }

          public String[] getKeys() {
               NBTTagCompound compound = this.getStoredCompound();
               return (String[])compound.func_150296_c().toArray(new String[compound.func_150296_c().size()]);
          }
     };
     private PlayerData data;

     public PlayerWrapper(EntityPlayerMP player) {
          super(player);
     }

     public IData getStoreddata() {
          return this.storeddata;
     }

     public String getName() {
          return ((EntityPlayerMP)this.entity).func_70005_c_();
     }

     public String getDisplayName() {
          return ((EntityPlayerMP)this.entity).getDisplayNameString();
     }

     public int getHunger() {
          return ((EntityPlayerMP)this.entity).func_71024_bL().func_75116_a();
     }

     public void setHunger(int level) {
          ((EntityPlayerMP)this.entity).func_71024_bL().func_75114_a(level);
     }

     public boolean hasFinishedQuest(int id) {
          PlayerQuestData data = this.getData().questData;
          return data.finishedQuests.containsKey(id);
     }

     public boolean hasActiveQuest(int id) {
          PlayerQuestData data = this.getData().questData;
          return data.activeQuests.containsKey(id);
     }

     public IQuest[] getActiveQuests() {
          PlayerQuestData data = this.getData().questData;
          List quests = new ArrayList();
          Iterator var3 = data.activeQuests.keySet().iterator();

          while(var3.hasNext()) {
               int id = (Integer)var3.next();
               IQuest quest = (IQuest)QuestController.instance.quests.get(id);
               if (quest != null) {
                    quests.add(quest);
               }
          }

          return (IQuest[])quests.toArray(new IQuest[quests.size()]);
     }

     public IQuest[] getFinishedQuests() {
          PlayerQuestData data = this.getData().questData;
          List quests = new ArrayList();
          Iterator var3 = data.finishedQuests.keySet().iterator();

          while(var3.hasNext()) {
               int id = (Integer)var3.next();
               IQuest quest = (IQuest)QuestController.instance.quests.get(id);
               if (quest != null) {
                    quests.add(quest);
               }
          }

          return (IQuest[])quests.toArray(new IQuest[quests.size()]);
     }

     public void startQuest(int id) {
          Quest quest = (Quest)QuestController.instance.quests.get(id);
          if (quest != null) {
               QuestData questdata = new QuestData(quest);
               PlayerData data = this.getData();
               data.questData.activeQuests.put(id, questdata);
               Server.sendData((EntityPlayerMP)this.entity, EnumPacketClient.MESSAGE, "quest.newquest", quest.title, 2);
               Server.sendData((EntityPlayerMP)this.entity, EnumPacketClient.CHAT, "quest.newquest", ": ", quest.title);
               data.updateClient = true;
          }
     }

     public void sendNotification(String title, String msg, int type) {
          if (type >= 0 && type <= 3) {
               Server.sendData((EntityPlayerMP)this.entity, EnumPacketClient.MESSAGE, title, msg, type);
          } else {
               throw new CustomNPCsException("Wrong type value given " + type, new Object[0]);
          }
     }

     public void finishQuest(int id) {
          Quest quest = (Quest)QuestController.instance.quests.get(id);
          if (quest != null) {
               PlayerData data = this.getData();
               data.questData.finishedQuests.put(id, System.currentTimeMillis());
               data.updateClient = true;
          }
     }

     public void stopQuest(int id) {
          Quest quest = (Quest)QuestController.instance.quests.get(id);
          if (quest != null) {
               PlayerData data = this.getData();
               data.questData.activeQuests.remove(id);
               data.updateClient = true;
          }
     }

     public void removeQuest(int id) {
          Quest quest = (Quest)QuestController.instance.quests.get(id);
          if (quest != null) {
               PlayerData data = this.getData();
               data.questData.activeQuests.remove(id);
               data.questData.finishedQuests.remove(id);
               data.updateClient = true;
          }
     }

     public boolean hasReadDialog(int id) {
          PlayerDialogData data = this.getData().dialogData;
          return data.dialogsRead.contains(id);
     }

     public void showDialog(int id, String name) {
          Dialog dialog = (Dialog)DialogController.instance.dialogs.get(id);
          if (dialog == null) {
               throw new CustomNPCsException("Unknown Dialog id: " + id, new Object[0]);
          } else if (dialog.availability.isAvailable((EntityPlayer)this.entity)) {
               EntityDialogNpc npc = new EntityDialogNpc(this.getWorld().getMCWorld());
               npc.display.setName(name);
               EntityUtil.Copy((EntityLivingBase)this.entity, npc);
               DialogOption option = new DialogOption();
               option.dialogId = id;
               option.title = dialog.title;
               npc.dialogs.put(0, option);
               NoppesUtilServer.openDialog((EntityPlayer)this.entity, npc, dialog);
          }
     }

     public void addFactionPoints(int faction, int points) {
          PlayerData data = this.getData();
          data.factionData.increasePoints((EntityPlayer)this.entity, faction, points);
          data.updateClient = true;
     }

     public int getFactionPoints(int faction) {
          EntityPlayer var10001 = (EntityPlayer)this.entity;
          return this.getData().factionData.getFactionPoints(var10001, faction);
     }

     public float getRotation() {
          return ((EntityPlayerMP)this.entity).field_70177_z;
     }

     public void setRotation(float rotation) {
          ((EntityPlayerMP)this.entity).field_70177_z = rotation;
     }

     public void message(String message) {
          ((EntityPlayerMP)this.entity).func_145747_a(new TextComponentTranslation(NoppesStringUtils.formatText(message, this.entity), new Object[0]));
     }

     public int getGamemode() {
          return ((EntityPlayerMP)this.entity).field_71134_c.func_73081_b().func_77148_a();
     }

     public void setGamemode(int type) {
          ((EntityPlayerMP)this.entity).func_71033_a(WorldSettings.func_77161_a(type));
     }

     public int inventoryItemCount(IItemStack item) {
          int count = 0;

          for(int i = 0; i < ((EntityPlayerMP)this.entity).field_71071_by.func_70302_i_(); ++i) {
               ItemStack is = ((EntityPlayerMP)this.entity).field_71071_by.func_70301_a(i);
               if (is != null && this.isItemEqual(item.getMCItemStack(), is)) {
                    count += is.func_190916_E();
               }
          }

          return count;
     }

     private boolean isItemEqual(ItemStack stack, ItemStack other) {
          if (other.func_190926_b()) {
               return false;
          } else if (stack.func_77973_b() != other.func_77973_b()) {
               return false;
          } else if (stack.func_77952_i() < 0) {
               return true;
          } else {
               return stack.func_77952_i() == other.func_77952_i();
          }
     }

     public int inventoryItemCount(String id, int damage) {
          Item item = (Item)Item.field_150901_e.func_82594_a(new ResourceLocation(id));
          if (item == null) {
               throw new CustomNPCsException("Unknown item id: " + id, new Object[0]);
          } else {
               return this.inventoryItemCount(NpcAPI.Instance().getIItemStack(new ItemStack(item, 1, damage)));
          }
     }

     public IContainer getInventory() {
          if (this.inventory == null) {
               this.inventory = new ContainerWrapper(((EntityPlayerMP)this.entity).field_71071_by);
          }

          return this.inventory;
     }

     public boolean removeItem(IItemStack item, int amount) {
          int count = this.inventoryItemCount(item);
          if (amount > count) {
               return false;
          } else {
               if (count == amount) {
                    this.removeAllItems(item);
               } else {
                    for(int i = 0; i < ((EntityPlayerMP)this.entity).field_71071_by.func_70302_i_(); ++i) {
                         ItemStack is = ((EntityPlayerMP)this.entity).field_71071_by.func_70301_a(i);
                         if (is != null && this.isItemEqual(item.getMCItemStack(), is)) {
                              if (amount < is.func_190916_E()) {
                                   is.func_77979_a(amount);
                                   break;
                              }

                              ((EntityPlayerMP)this.entity).field_71071_by.func_70299_a(i, ItemStack.field_190927_a);
                              amount -= is.func_190916_E();
                         }
                    }
               }

               this.updatePlayerInventory();
               return true;
          }
     }

     public boolean removeItem(String id, int damage, int amount) {
          Item item = (Item)Item.field_150901_e.func_82594_a(new ResourceLocation(id));
          if (item == null) {
               throw new CustomNPCsException("Unknown item id: " + id, new Object[0]);
          } else {
               return this.removeItem(NpcAPI.Instance().getIItemStack(new ItemStack(item, 1, damage)), amount);
          }
     }

     public boolean giveItem(IItemStack item) {
          ItemStack mcItem = item.getMCItemStack();
          if (mcItem.func_190926_b()) {
               return false;
          } else {
               boolean bo = ((EntityPlayerMP)this.entity).field_71071_by.func_70441_a(mcItem.func_77946_l());
               if (bo) {
                    NoppesUtilServer.playSound((EntityLivingBase)this.entity, SoundEvents.field_187638_cR, 0.2F, ((((EntityPlayerMP)this.entity).func_70681_au().nextFloat() - ((EntityPlayerMP)this.entity).func_70681_au().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    this.updatePlayerInventory();
               }

               return bo;
          }
     }

     public boolean giveItem(String id, int damage, int amount) {
          Item item = (Item)Item.field_150901_e.func_82594_a(new ResourceLocation(id));
          if (item == null) {
               return false;
          } else {
               ItemStack mcStack = new ItemStack(item);
               IItemStack itemStack = NpcAPI.Instance().getIItemStack(mcStack);
               itemStack.setStackSize(amount);
               itemStack.setItemDamage(damage);
               return this.giveItem(itemStack);
          }
     }

     public void updatePlayerInventory() {
          ((EntityPlayerMP)this.entity).field_71069_bz.func_75142_b();
          PlayerQuestData playerdata = this.getData().questData;
          playerdata.checkQuestCompletion((EntityPlayer)this.entity, 0);
     }

     public IBlock getSpawnPoint() {
          BlockPos pos = ((EntityPlayerMP)this.entity).func_180470_cg();
          return pos == null ? this.getWorld().getSpawnPoint() : NpcAPI.Instance().getIBlock(((EntityPlayerMP)this.entity).field_70170_p, pos);
     }

     public void setSpawnPoint(IBlock block) {
          ((EntityPlayerMP)this.entity).func_180473_a(new BlockPos(block.getX(), block.getY(), block.getZ()), true);
     }

     public void setSpawnpoint(int x, int y, int z) {
          x = ValueUtil.CorrectInt(x, -30000000, 30000000);
          z = ValueUtil.CorrectInt(z, -30000000, 30000000);
          y = ValueUtil.CorrectInt(y, 0, 256);
          ((EntityPlayerMP)this.entity).func_180473_a(new BlockPos(x, y, z), true);
     }

     public void resetSpawnpoint() {
          ((EntityPlayerMP)this.entity).func_180473_a((BlockPos)null, false);
     }

     public void removeAllItems(IItemStack item) {
          for(int i = 0; i < ((EntityPlayerMP)this.entity).field_71071_by.func_70302_i_(); ++i) {
               ItemStack is = ((EntityPlayerMP)this.entity).field_71071_by.func_70301_a(i);
               if (is != null && is.func_77969_a(item.getMCItemStack())) {
                    ((EntityPlayerMP)this.entity).field_71071_by.func_70299_a(i, ItemStack.field_190927_a);
               }
          }

     }

     public boolean hasAchievement(String achievement) {
          StatBase statbase = StatList.func_151177_a(achievement);
          return false;
     }

     public int getExpLevel() {
          return ((EntityPlayerMP)this.entity).field_71068_ca;
     }

     public void setExpLevel(int level) {
          ((EntityPlayerMP)this.entity).field_71068_ca = level;
          ((EntityPlayerMP)this.entity).func_82242_a(0);
     }

     public void setPosition(double x, double y, double z) {
          NoppesUtilPlayer.teleportPlayer((EntityPlayerMP)this.entity, x, y, z, ((EntityPlayerMP)this.entity).field_71093_bK);
     }

     public void setPos(IPos pos) {
          NoppesUtilPlayer.teleportPlayer((EntityPlayerMP)this.entity, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), ((EntityPlayerMP)this.entity).field_71093_bK);
     }

     public int getType() {
          return 1;
     }

     public boolean typeOf(int type) {
          return type == 1 ? true : super.typeOf(type);
     }

     public boolean hasPermission(String permission) {
          return CustomNpcsPermissions.hasPermissionString((EntityPlayer)this.entity, permission);
     }

     public IPixelmonPlayerData getPixelmonData() {
          if (!PixelmonHelper.Enabled) {
               throw new CustomNPCsException("Pixelmon isnt installed", new Object[0]);
          } else {
               return new IPixelmonPlayerData() {
                    public Object getParty() {
                         if (PlayerWrapper.this.pixelmonPartyStorage == null) {
                              PlayerWrapper.this.pixelmonPartyStorage = PixelmonHelper.getParty((EntityPlayerMP)PlayerWrapper.this.entity);
                         }

                         return PlayerWrapper.this.pixelmonPartyStorage;
                    }

                    public Object getPC() {
                         if (PlayerWrapper.this.pixelmonPCStorage == null) {
                              PlayerWrapper.this.pixelmonPCStorage = PixelmonHelper.getPc((EntityPlayerMP)PlayerWrapper.this.entity);
                         }

                         return PlayerWrapper.this.pixelmonPCStorage;
                    }
               };
          }
     }

     private PlayerData getData() {
          if (this.data == null) {
               this.data = PlayerData.get((EntityPlayer)this.entity);
          }

          return this.data;
     }

     public ITimers getTimers() {
          return this.getData().timers;
     }

     public void removeDialog(int id) {
          PlayerData data = this.getData();
          data.dialogData.dialogsRead.remove(id);
          data.updateClient = true;
     }

     public void addDialog(int id) {
          PlayerData data = this.getData();
          data.dialogData.dialogsRead.add(id);
          data.updateClient = true;
     }

     public void closeGui() {
          ((EntityPlayerMP)this.entity).func_71128_l();
          Server.sendData((EntityPlayerMP)this.entity, EnumPacketClient.GUI_CLOSE, -1, new NBTTagCompound());
     }

     public int factionStatus(int factionId) {
          Faction faction = FactionController.instance.getFaction(factionId);
          if (faction == null) {
               throw new CustomNPCsException("Unknown faction: " + factionId, new Object[0]);
          } else {
               return faction.playerStatus(this);
          }
     }

     public void kick(String message) {
          ((EntityPlayerMP)this.entity).field_71135_a.func_194028_b(new TextComponentTranslation(message, new Object[0]));
     }

     public boolean canQuestBeAccepted(int questId) {
          return PlayerQuestController.canQuestBeAccepted((EntityPlayer)this.entity, questId);
     }

     public void showCustomGui(ICustomGui gui) {
          CustomGuiController.openGui(this, (CustomGuiWrapper)gui);
     }

     public ICustomGui getCustomGui() {
          return ((EntityPlayerMP)this.entity).field_71070_bA instanceof ContainerCustomGui ? ((ContainerCustomGui)((EntityPlayerMP)this.entity).field_71070_bA).customGui : null;
     }

     public void clearData() {
          PlayerData data = this.getData();
          data.setNBT(new NBTTagCompound());
          data.save(true);
     }

     public IContainer showChestGui(int rows) {
          ScriptContainer current = ScriptContainer.Current;
          ((EntityPlayerMP)this.entity).func_71053_j();
          ((EntityPlayerMP)this.entity).openGui(CustomNpcs.instance, EnumGuiType.CustomChest.ordinal(), ((EntityPlayerMP)this.entity).field_70170_p, rows, 0, 0);
          ContainerCustomChestWrapper container = (ContainerCustomChestWrapper)NpcAPI.Instance().getIContainer(((EntityPlayerMP)this.entity).field_71070_bA);
          container.script = current;
          return container;
     }

     public IContainer getOpenContainer() {
          return NpcAPI.Instance().getIContainer(((EntityPlayerMP)this.entity).field_71070_bA);
     }

     public void playSound(String sound, float volume, float pitch) {
          BlockPos pos = ((EntityPlayerMP)this.entity).func_180425_c();
          Server.sendData((EntityPlayerMP)this.entity, EnumPacketClient.PLAY_SOUND, sound, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), volume, pitch);
     }

     public void sendMail(IPlayerMail mail) {
          PlayerData data = this.getData();
          data.mailData.playermail.add(((PlayerMail)mail).copy());
          data.save(false);
     }
}
