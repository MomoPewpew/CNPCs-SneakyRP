package noppes.npcs.api.wrapper;

import net.minecraft.scoreboard.Score;
import noppes.npcs.api.IScoreboardScore;

public class ScoreboardScoreWrapper implements IScoreboardScore {
     private Score score;

     public ScoreboardScoreWrapper(Score score) {
          this.score = score;
     }

     public int getValue() {
          return this.score.func_96652_c();
     }

     public void setValue(int val) {
          this.score.func_96647_c(val);
     }

     public String getPlayerName() {
          return this.score.func_96653_e();
     }
}
