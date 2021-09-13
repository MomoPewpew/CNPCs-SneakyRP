package noppes.npcs.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.LogWriter;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.PlayerBankData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.util.NBTJsonUtil;

public class PlayerDataController {
     public static PlayerDataController instance;
     public Map nameUUIDs;

     public PlayerDataController() {
          instance = this;
          File dir = CustomNpcs.getWorldSaveDirectory("playerdata");
          Map map = new HashMap();
          File[] var3 = dir.listFiles();
          int var4 = var3.length;

          for(int var5 = 0; var5 < var4; ++var5) {
               File file = var3[var5];
               if (!file.isDirectory() && file.getName().endsWith(".json")) {
                    try {
                         NBTTagCompound compound = NBTJsonUtil.LoadFile(file);
                         if (compound.func_74764_b("PlayerName")) {
                              map.put(compound.func_74779_i("PlayerName"), file.getName().substring(0, file.getName().length() - 5));
                         }
                    } catch (Exception var8) {
                         LogWriter.error("Error loading: " + file.getAbsolutePath(), var8);
                    }
               }
          }

          this.nameUUIDs = map;
     }

     public PlayerBankData getBankData(EntityPlayer player, int bankId) {
          Bank bank = BankController.getInstance().getBank(bankId);
          PlayerBankData data = PlayerData.get(player).bankData;
          if (!data.hasBank(bank.id)) {
               data.loadNew(bank.id);
          }

          return data;
     }

     public String hasPlayer(String username) {
          Iterator var2 = this.nameUUIDs.keySet().iterator();

          String name;
          do {
               if (!var2.hasNext()) {
                    return "";
               }

               name = (String)var2.next();
          } while(!name.equalsIgnoreCase(username));

          return name;
     }

     public PlayerData getDataFromUsername(MinecraftServer server, String username) {
          EntityPlayer player = server.func_184103_al().func_152612_a(username);
          PlayerData data = null;
          if (player == null) {
               Iterator var5 = this.nameUUIDs.keySet().iterator();

               while(var5.hasNext()) {
                    String name = (String)var5.next();
                    if (name.equalsIgnoreCase(username)) {
                         data = new PlayerData();
                         data.setNBT(PlayerData.loadPlayerData((String)this.nameUUIDs.get(name)));
                         break;
                    }
               }
          } else {
               data = PlayerData.get(player);
          }

          return data;
     }

     public void addPlayerMessage(MinecraftServer server, String username, PlayerMail mail) {
          mail.time = System.currentTimeMillis();
          PlayerData data = this.getDataFromUsername(server, username);
          data.mailData.playermail.add(mail.copy());
          data.save(false);
     }

     public List getPlayersData(ICommandSender sender, String username) throws CommandException {
          ArrayList list = new ArrayList();
          List players = EntitySelector.func_179656_b(sender, username, EntityPlayerMP.class);
          if (players.isEmpty()) {
               PlayerData data = this.getDataFromUsername(sender.func_184102_h(), username);
               if (data != null) {
                    list.add(data);
               }
          } else {
               Iterator var7 = players.iterator();

               while(var7.hasNext()) {
                    EntityPlayer player = (EntityPlayer)var7.next();
                    list.add(PlayerData.get(player));
               }
          }

          return list;
     }
}
