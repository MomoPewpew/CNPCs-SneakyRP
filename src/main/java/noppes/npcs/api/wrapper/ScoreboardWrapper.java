package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboard;
import noppes.npcs.api.IScoreboardObjective;
import noppes.npcs.api.IScoreboardTeam;

public class ScoreboardWrapper implements IScoreboard {
     private Scoreboard board;
     private MinecraftServer server;

     protected ScoreboardWrapper(MinecraftServer server) {
          this.server = server;
          this.board = server.func_71218_a(0).func_96441_U();
     }

     public IScoreboardObjective[] getObjectives() {
          List collection = new ArrayList(this.board.func_96514_c());
          IScoreboardObjective[] objectives = new IScoreboardObjective[collection.size()];

          for(int i = 0; i < collection.size(); ++i) {
               objectives[i] = new ScoreboardObjectiveWrapper(this.board, (ScoreObjective)collection.get(i));
          }

          return objectives;
     }

     public String[] getPlayerList() {
          Collection collection = this.board.func_96526_d();
          return (String[])collection.toArray(new String[collection.size()]);
     }

     public IScoreboardObjective getObjective(String name) {
          ScoreObjective obj = this.board.func_96518_b(name);
          return obj == null ? null : new ScoreboardObjectiveWrapper(this.board, obj);
     }

     public boolean hasObjective(String objective) {
          return this.board.func_96518_b(objective) != null;
     }

     public void removeObjective(String objective) {
          ScoreObjective obj = this.board.func_96518_b(objective);
          if (obj != null) {
               this.board.func_96519_k(obj);
          }

     }

     public IScoreboardObjective addObjective(String objective, String criteria) {
          IScoreCriteria icriteria = (IScoreCriteria)IScoreCriteria.field_96643_a.get(criteria);
          if (icriteria == null) {
               throw new CustomNPCsException("Unknown score criteria: %s", new Object[]{criteria});
          } else if (objective.length() > 0 && objective.length() <= 16) {
               ScoreObjective obj = this.board.func_96535_a(objective, icriteria);
               return new ScoreboardObjectiveWrapper(this.board, obj);
          } else {
               throw new CustomNPCsException("Score objective must be between 1-16 characters: %s", new Object[]{objective});
          }
     }

     public void setPlayerScore(String player, String objective, int score, String datatag) {
          ScoreObjective objec = this.getObjectiveWithException(objective);
          if (!objec.func_96680_c().func_96637_b() && score >= Integer.MIN_VALUE && score <= Integer.MAX_VALUE && this.test(datatag)) {
               Score sco = this.board.func_96529_a(player, objec);
               sco.func_96647_c(score);
          }
     }

     private boolean test(String datatag) {
          if (datatag.isEmpty()) {
               return true;
          } else {
               try {
                    Entity entity = CommandBase.func_184885_b(this.server, this.server, datatag);
                    NBTTagCompound nbttagcompound = JsonToNBT.func_180713_a(datatag);
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    entity.func_189511_e(nbttagcompound1);
                    return NBTUtil.func_181123_a(nbttagcompound, nbttagcompound1, true);
               } catch (Exception var5) {
                    return false;
               }
          }
     }

     private ScoreObjective getObjectiveWithException(String objective) {
          ScoreObjective objec = this.board.func_96518_b(objective);
          if (objec == null) {
               throw new CustomNPCsException("Score objective does not exist: %s", new Object[]{objective});
          } else {
               return objec;
          }
     }

     public int getPlayerScore(String player, String objective, String datatag) {
          ScoreObjective objec = this.getObjectiveWithException(objective);
          return !objec.func_96680_c().func_96637_b() && this.test(datatag) ? this.board.func_96529_a(player, objec).func_96652_c() : 0;
     }

     public boolean hasPlayerObjective(String player, String objective, String datatag) {
          ScoreObjective objec = this.getObjectiveWithException(objective);
          if (!this.test(datatag)) {
               return false;
          } else {
               return this.board.func_96510_d(player).get(objec) != null;
          }
     }

     public void deletePlayerScore(String player, String objective, String datatag) {
          ScoreObjective objec = this.getObjectiveWithException(objective);
          if (this.test(datatag)) {
               if (this.board.func_96510_d(player).remove(objec) != null) {
                    this.board.func_96524_g(player);
               }

          }
     }

     public IScoreboardTeam[] getTeams() {
          List list = new ArrayList(this.board.func_96525_g());
          IScoreboardTeam[] teams = new IScoreboardTeam[list.size()];

          for(int i = 0; i < list.size(); ++i) {
               teams[i] = new ScoreboardTeamWrapper((ScorePlayerTeam)list.get(i), this.board);
          }

          return teams;
     }

     public boolean hasTeam(String name) {
          return this.board.func_96508_e(name) != null;
     }

     public IScoreboardTeam addTeam(String name) {
          if (this.hasTeam(name)) {
               throw new CustomNPCsException("Team %s already exists", new Object[]{name});
          } else {
               return new ScoreboardTeamWrapper(this.board.func_96527_f(name), this.board);
          }
     }

     public IScoreboardTeam getTeam(String name) {
          ScorePlayerTeam team = this.board.func_96508_e(name);
          return team == null ? null : new ScoreboardTeamWrapper(team, this.board);
     }

     public void removeTeam(String name) {
          ScorePlayerTeam team = this.board.func_96508_e(name);
          if (team != null) {
               this.board.func_96511_d(team);
          }

     }

     public IScoreboardTeam getPlayerTeam(String player) {
          ScorePlayerTeam team = this.board.func_96509_i(player);
          return team == null ? null : new ScoreboardTeamWrapper(team, this.board);
     }

     public void removePlayerTeam(String player) {
          this.board.func_96524_g(player);
     }
}
