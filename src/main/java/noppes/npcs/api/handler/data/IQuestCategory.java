package noppes.npcs.api.handler.data;

import java.util.List;

public interface IQuestCategory {
	List quests();

	String getName();

	IQuest create();
}
