package noppes.npcs.api.wrapper;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IScoreboardObjective;
import noppes.npcs.api.IScoreboardScore;

public class ScoreboardObjectiveWrapper implements IScoreboardObjective {
     private ScoreObjective objective;
     private Scoreboard board;

     protected ScoreboardObjectiveWrapper(Scoreboard board, ScoreObjective objective) {
          this.objective = objective;
          this.board = board;
     }

     public String getName() {
          return this.objective.func_96679_b();
     }

     public String getDisplayName() {
          return this.objective.func_96678_d();
     }

     public void setDisplayName(String name) {
          if (name.length() > 0 && name.length() <= 32) {
               this.objective.func_96681_a(name);
          } else {
               throw new CustomNPCsException("Score objective display name must be between 1-32 characters: %s", new Object[]{name});
          }
     }

     public String getCriteria() {
          return this.objective.func_96680_c().func_96636_a();
     }

     public boolean isReadyOnly() {
          return this.objective.func_96680_c().func_96637_b();
     }

     public IScoreboardScore[] getScores() {
          Collection list = this.board.func_96534_i(this.objective);
          IScoreboardScore[] scores = new IScoreboardScore[list.size()];
          int i = 0;

          for(Iterator var4 = list.iterator(); var4.hasNext(); ++i) {
               Score score = (Score)var4.next();
               scores[i] = new ScoreboardScoreWrapper(score);
          }

          return scores;
     }

     public IScoreboardScore getScore(String player) {
          return !this.hasScore(player) ? null : new ScoreboardScoreWrapper(this.board.func_96529_a(player, this.objective));
     }

     public IScoreboardScore createScore(String player) {
          return new ScoreboardScoreWrapper(this.board.func_96529_a(player, this.objective));
     }

     public void removeScore(String player) {
          this.board.func_178822_d(player, this.objective);
     }

     public boolean hasScore(String player) {
          return this.board.func_178819_b(player, this.objective);
     }
}
