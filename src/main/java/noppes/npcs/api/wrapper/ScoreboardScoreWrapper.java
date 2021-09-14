package noppes.npcs.api.wrapper;

import net.minecraft.scoreboard.Score;
import noppes.npcs.api.IScoreboardScore;

public class ScoreboardScoreWrapper implements IScoreboardScore {
	private Score score;

	public ScoreboardScoreWrapper(Score score) {
		this.score = score;
	}

	public int getValue() {
		return this.score.getScorePoints();
	}

	public void setValue(int val) {
		this.score.setScorePoints(val);
	}

	public String getPlayerName() {
		return this.score.getPlayerName();
	}
}
