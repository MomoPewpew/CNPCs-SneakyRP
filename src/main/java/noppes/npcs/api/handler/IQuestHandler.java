package noppes.npcs.api.handler;

import java.util.List;
import noppes.npcs.api.handler.data.IQuest;

public interface IQuestHandler {
	List categories();

	IQuest get(int var1);
}
