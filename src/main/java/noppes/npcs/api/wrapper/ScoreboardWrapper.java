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
		this.board = server.getWorld(0).getScoreboard();
	}

	public IScoreboardObjective[] getObjectives() {
		List collection = new ArrayList(this.board.getScoreObjectives());
		IScoreboardObjective[] objectives = new IScoreboardObjective[collection.size()];

		for (int i = 0; i < collection.size(); ++i) {
			objectives[i] = new ScoreboardObjectiveWrapper(this.board, (ScoreObjective) collection.get(i));
		}

		return objectives;
	}

	public String[] getPlayerList() {
		Collection collection = this.board.getObjectiveNames();
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	public IScoreboardObjective getObjective(String name) {
		ScoreObjective obj = this.board.getObjective(name);
		return obj == null ? null : new ScoreboardObjectiveWrapper(this.board, obj);
	}

	public boolean hasObjective(String objective) {
		return this.board.getObjective(objective) != null;
	}

	public void removeObjective(String objective) {
		ScoreObjective obj = this.board.getObjective(objective);
		if (obj != null) {
			this.board.removeObjective(obj);
		}

	}

	public IScoreboardObjective addObjective(String objective, String criteria) {
		IScoreCriteria icriteria = (IScoreCriteria) IScoreCriteria.INSTANCES.get(criteria);
		if (icriteria == null) {
			throw new CustomNPCsException("Unknown score criteria: %s", new Object[] { criteria });
		} else if (objective.length() > 0 && objective.length() <= 16) {
			ScoreObjective obj = this.board.addScoreObjective(objective, icriteria);
			return new ScoreboardObjectiveWrapper(this.board, obj);
		} else {
			throw new CustomNPCsException("Score objective must be between 1-16 characters: %s",
					new Object[] { objective });
		}
	}

	public void setPlayerScore(String player, String objective, int score, String datatag) {
		ScoreObjective objec = this.getObjectiveWithException(objective);
		if (!objec.getCriteria().isReadOnly() && score >= Integer.MIN_VALUE && score <= Integer.MAX_VALUE
				&& this.test(datatag)) {
			Score sco = this.board.getOrCreateScore(player, objec);
			sco.setScorePoints(score);
		}
	}

	private boolean test(String datatag) {
		if (datatag.isEmpty()) {
			return true;
		} else {
			try {
				Entity entity = CommandBase.getEntity(this.server, this.server, datatag);
				NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(datatag);
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				entity.writeToNBT(nbttagcompound1);
				return NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true);
			} catch (Exception var5) {
				return false;
			}
		}
	}

	private ScoreObjective getObjectiveWithException(String objective) {
		ScoreObjective objec = this.board.getObjective(objective);
		if (objec == null) {
			throw new CustomNPCsException("Score objective does not exist: %s", new Object[] { objective });
		} else {
			return objec;
		}
	}

	public int getPlayerScore(String player, String objective, String datatag) {
		ScoreObjective objec = this.getObjectiveWithException(objective);
		return !objec.getCriteria().isReadOnly() && this.test(datatag)
				? this.board.getOrCreateScore(player, objec).getScorePoints()
				: 0;
	}

	public boolean hasPlayerObjective(String player, String objective, String datatag) {
		ScoreObjective objec = this.getObjectiveWithException(objective);
		if (!this.test(datatag)) {
			return false;
		} else {
			return this.board.getObjectivesForEntity(player).get(objec) != null;
		}
	}

	public void deletePlayerScore(String player, String objective, String datatag) {
		ScoreObjective objec = this.getObjectiveWithException(objective);
		if (this.test(datatag)) {
			if (this.board.getObjectivesForEntity(player).remove(objec) != null) {
				this.board.removePlayerFromTeams(player);
			}

		}
	}

	public IScoreboardTeam[] getTeams() {
		List list = new ArrayList(this.board.getTeams());
		IScoreboardTeam[] teams = new IScoreboardTeam[list.size()];

		for (int i = 0; i < list.size(); ++i) {
			teams[i] = new ScoreboardTeamWrapper((ScorePlayerTeam) list.get(i), this.board);
		}

		return teams;
	}

	public boolean hasTeam(String name) {
		return this.board.getTeam(name) != null;
	}

	public IScoreboardTeam addTeam(String name) {
		if (this.hasTeam(name)) {
			throw new CustomNPCsException("Team %s already exists", new Object[] { name });
		} else {
			return new ScoreboardTeamWrapper(this.board.createTeam(name), this.board);
		}
	}

	public IScoreboardTeam getTeam(String name) {
		ScorePlayerTeam team = this.board.getTeam(name);
		return team == null ? null : new ScoreboardTeamWrapper(team, this.board);
	}

	public void removeTeam(String name) {
		ScorePlayerTeam team = this.board.getTeam(name);
		if (team != null) {
			this.board.removeTeam(team);
		}

	}

	public IScoreboardTeam getPlayerTeam(String player) {
		ScorePlayerTeam team = this.board.getPlayersTeam(player);
		return team == null ? null : new ScoreboardTeamWrapper(team, this.board);
	}

	public void removePlayerTeam(String player) {
		this.board.removePlayerFromTeams(player);
	}
}
