package noppes.npcs;

import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import noppes.npcs.api.event.ItemEvent;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.BankData;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerFactionData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.roles.RoleTransporter;

public class PacketHandlerPlayer {
     @SubscribeEvent
     public void onServerPacket(ServerCustomPacketEvent event) {
          EntityPlayerMP player = ((NetHandlerPlayServer)event.getHandler()).field_147369_b;
          ByteBuf buffer = event.getPacket().payload();
          player.func_184102_h().func_152344_a(() -> {
               EnumPlayerPacket type = null;

               try {
                    type = EnumPlayerPacket.values()[buffer.readInt()];
                    LogWriter.debug("Received: " + type);
                    this.player(buffer, player, type);
               } catch (Exception var8) {
                    LogWriter.error("Error with EnumPlayerPacket." + type, var8);
               } finally {
                    buffer.release();
               }

          });
     }

     private void player(ByteBuf buffer, EntityPlayerMP player, EnumPlayerPacket type) throws Exception {
          if (type == EnumPlayerPacket.MarkData) {
               Entity entity = player.func_184102_h().func_175576_a(Server.readUUID(buffer));
               if (entity == null || !(entity instanceof EntityLivingBase)) {
                    return;
               }

               MarkData var5 = MarkData.get((EntityLivingBase)entity);
          } else if (type == EnumPlayerPacket.KeyPressed) {
               if (!CustomNpcs.EnableScripting || ScriptController.Instance.languages.isEmpty()) {
                    return;
               }

               EventHooks.onPlayerKeyPressed(player, buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
          } else if (type == EnumPlayerPacket.LeftClick) {
               if (!CustomNpcs.EnableScripting || ScriptController.Instance.languages.isEmpty()) {
                    return;
               }

               ItemStack item = player.func_184614_ca();
               PlayerScriptData handler = PlayerData.get(player).scriptData;
               PlayerEvent.AttackEvent ev = new PlayerEvent.AttackEvent(handler.getPlayer(), 0, (Object)null);
               EventHooks.onPlayerAttack(handler, ev);
               if (item.func_77973_b() == CustomItems.scripted_item) {
                    ItemScriptedWrapper isw = ItemScripted.GetWrapper(item);
                    ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent(isw, handler.getPlayer(), 0, (Object)null);
                    EventHooks.onScriptItemAttack(isw, eve);
               }
          } else if (type == EnumPlayerPacket.CloseGui) {
               player.func_71128_l();
          } else {
               EntityNPCInterface npc;
               int slot;
               int bankId;
               if (type == EnumPlayerPacket.CompanionTalentExp) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 6 || player != npc.getOwner()) {
                         return;
                    }

                    slot = buffer.readInt();
                    bankId = buffer.readInt();
                    RoleCompanion role = (RoleCompanion)npc.roleInterface;
                    if (bankId <= 0 || !role.canAddExp(-bankId) || slot < 0 || slot >= EnumCompanionTalent.values().length) {
                         return;
                    }

                    EnumCompanionTalent talent = EnumCompanionTalent.values()[slot];
                    role.addExp(-bankId);
                    role.addTalentExp(talent, bankId);
               } else if (type == EnumPlayerPacket.CompanionOpenInv) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 6 || player != npc.getOwner()) {
                         return;
                    }

                    NoppesUtilServer.sendOpenGui(player, EnumGuiType.CompanionInv, npc);
               } else if (type == EnumPlayerPacket.FollowerHire) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 2) {
                         return;
                    }

                    NoppesUtilPlayer.hireFollower(player, npc);
               } else if (type == EnumPlayerPacket.FollowerExtend) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 2) {
                         return;
                    }

                    NoppesUtilPlayer.extendFollower(player, npc);
                    Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
               } else if (type == EnumPlayerPacket.FollowerState) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 2) {
                         return;
                    }

                    NoppesUtilPlayer.changeFollowerState(player, npc);
                    Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
               } else if (type == EnumPlayerPacket.RoleGet) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role == 0) {
                         return;
                    }

                    Server.sendData(player, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
               } else if (type == EnumPlayerPacket.Transport) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 4) {
                         return;
                    }

                    ((RoleTransporter)npc.roleInterface).transport(player, Server.readString(buffer));
               } else if (type == EnumPlayerPacket.BankUpgrade) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 3) {
                         return;
                    }

                    NoppesUtilPlayer.bankUpgrade(player, npc);
               } else if (type == EnumPlayerPacket.BankUnlock) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 3) {
                         return;
                    }

                    NoppesUtilPlayer.bankUnlock(player, npc);
               } else if (type == EnumPlayerPacket.BankSlotOpen) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    if (npc == null || npc.advanced.role != 3) {
                         return;
                    }

                    slot = buffer.readInt();
                    bankId = buffer.readInt();
                    BankData data = PlayerDataController.instance.getBankData(player, bankId).getBankOrDefault(bankId);
                    data.openBankGui(player, npc, bankId, slot);
               } else if (type == EnumPlayerPacket.Dialog) {
                    npc = NoppesUtilServer.getEditingNpc(player);
                    LogWriter.debug("Dialog npc: " + npc);
                    if (npc == null) {
                         return;
                    }

                    NoppesUtilPlayer.dialogSelected(buffer.readInt(), buffer.readInt(), player, npc);
               } else if (type == EnumPlayerPacket.CheckQuestCompletion) {
                    PlayerQuestData playerdata = PlayerData.get(player).questData;
                    playerdata.checkQuestCompletion(player, -1);
               } else if (type == EnumPlayerPacket.QuestCompletion) {
                    NoppesUtilPlayer.questCompletion(player, buffer.readInt());
               } else if (type == EnumPlayerPacket.FactionsGet) {
                    PlayerFactionData data = PlayerData.get(player).factionData;
                    Server.sendData(player, EnumPacketClient.GUI_DATA, data.getPlayerGuiData());
               } else if (type == EnumPlayerPacket.MailGet) {
                    PlayerMailData data = PlayerData.get(player).mailData;
                    Server.sendData(player, EnumPacketClient.GUI_DATA, data.saveNBTData(new NBTTagCompound()));
               } else {
                    PlayerMail mail;
                    String username;
                    PlayerMailData data;
                    Iterator it;
                    long time;
                    if (type == EnumPlayerPacket.MailDelete) {
                         time = buffer.readLong();
                         username = Server.readString(buffer);
                         data = PlayerData.get(player).mailData;
                         it = data.playermail.iterator();

                         while(it.hasNext()) {
                              mail = (PlayerMail)it.next();
                              if (mail.time == time && mail.sender.equals(username)) {
                                   it.remove();
                              }
                         }

                         Server.sendData(player, EnumPacketClient.GUI_DATA, data.saveNBTData(new NBTTagCompound()));
                    } else if (type == EnumPlayerPacket.MailSend) {
                         String username = PlayerDataController.instance.hasPlayer(Server.readString(buffer));
                         if (username.isEmpty()) {
                              NoppesUtilServer.sendGuiError(player, 0);
                              return;
                         }

                         PlayerMail mail = new PlayerMail();
                         username = player.getDisplayNameString();
                         if (!username.equals(player.func_70005_c_())) {
                              username = username + "(" + player.func_70005_c_() + ")";
                         }

                         mail.readNBT(Server.readNBT(buffer));
                         mail.sender = username;
                         mail.items = ((ContainerMail)player.openContainer).mail.items;
                         if (mail.subject.isEmpty()) {
                              NoppesUtilServer.sendGuiError(player, 1);
                              return;
                         }

                         NBTTagCompound comp = new NBTTagCompound();
                         comp.func_74778_a("username", username);
                         NoppesUtilServer.sendGuiClose(player, 1, comp);
                         EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
                         if (npc != null && EventHooks.onNPCRole(npc, new RoleEvent.MailmanEvent(player, npc.wrappedNPC, mail))) {
                              return;
                         }

                         PlayerDataController.instance.addPlayerMessage(player.func_184102_h(), username, mail);
                    } else if (type == EnumPlayerPacket.MailboxOpenMail) {
                         time = buffer.readLong();
                         username = Server.readString(buffer);
                         player.func_71128_l();
                         data = PlayerData.get(player).mailData;
                         it = data.playermail.iterator();

                         while(it.hasNext()) {
                              mail = (PlayerMail)it.next();
                              if (mail.time == time && mail.sender.equals(username)) {
                                   ContainerMail.staticmail = mail;
                                   player.openGui(CustomNpcs.instance, EnumGuiType.PlayerMailman.ordinal(), player.world, 0, 0, 0);
                                   break;
                              }
                         }
                    } else if (type == EnumPlayerPacket.MailRead) {
                         time = buffer.readLong();
                         username = Server.readString(buffer);
                         data = PlayerData.get(player).mailData;
                         it = data.playermail.iterator();

                         while(it.hasNext()) {
                              mail = (PlayerMail)it.next();
                              if (!mail.beenRead && mail.time == time && mail.sender.equals(username)) {
                                   if (mail.hasQuest()) {
                                        PlayerQuestController.addActiveQuest(mail.getQuest(), player);
                                   }

                                   mail.beenRead = true;
                              }
                         }
                    }
               }
          }

     }
}
