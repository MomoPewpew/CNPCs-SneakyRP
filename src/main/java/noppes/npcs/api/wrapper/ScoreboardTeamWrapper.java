package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardTeam;

public class ScoreboardTeamWrapper implements IScoreboardTeam {
     private ScorePlayerTeam team;
     private Scoreboard board;

     protected ScoreboardTeamWrapper(ScorePlayerTeam team, Scoreboard board) {
          this.team = team;
          this.board = board;
     }

     public String getName() {
          return this.team.func_96661_b();
     }

     public String getDisplayName() {
          return this.team.func_96669_c();
     }

     public void setDisplayName(String name) {
          if (name.length() > 0 && name.length() <= 32) {
               this.team.func_96664_a(name);
          } else {
               throw new CustomNPCsException("Score team display name must be between 1-32 characters: %s", new Object[]{name});
          }
     }

     public void addPlayer(String player) {
          this.board.func_151392_a(player, this.getName());
     }

     public void removePlayer(String player) {
          this.board.func_96512_b(player, this.team);
     }

     public String[] getPlayers() {
          List list = new ArrayList(this.team.func_96670_d());
          return (String[])list.toArray(new String[list.size()]);
     }

     public void clearPlayers() {
          List list = new ArrayList(this.team.func_96670_d());
          Iterator var2 = list.iterator();

          while(var2.hasNext()) {
               String player = (String)var2.next();
               this.board.func_96512_b(player, this.team);
          }

     }

     public boolean getFriendlyFire() {
          return this.team.func_96665_g();
     }

     public void setFriendlyFire(boolean bo) {
          this.team.func_96660_a(bo);
     }

     public void setColor(String color) {
          TextFormatting enumchatformatting = TextFormatting.func_96300_b(color);
          if (enumchatformatting != null && !enumchatformatting.func_96301_b()) {
               this.team.func_96666_b(enumchatformatting.toString());
               this.team.func_96662_c(TextFormatting.RESET.toString());
          } else {
               throw new CustomNPCsException("Not a proper color name: %s", new Object[]{color});
          }
     }

     public String getColor() {
          String prefix = this.team.func_96668_e();
          if (prefix != null && !prefix.isEmpty()) {
               TextFormatting[] var2 = TextFormatting.values();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                    TextFormatting format = var2[var4];
                    if (prefix.equals(format.toString()) && format != TextFormatting.RESET) {
                         return format.func_96297_d();
                    }
               }

               return null;
          } else {
               return null;
          }
     }

     public void setSeeInvisibleTeamPlayers(boolean bo) {
          this.team.func_98300_b(bo);
     }

     public boolean getSeeInvisibleTeamPlayers() {
          return this.team.func_98297_h();
     }

     public boolean hasPlayer(String player) {
          return this.board.func_96509_i(player) != null;
     }
}
