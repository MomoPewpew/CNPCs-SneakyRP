package noppes.npcs.controllers;

import java.util.List;
import java.util.Map;
import net.minecraftforge.fml.common.eventhandler.Event;
import noppes.npcs.constants.EnumScriptType;

public interface IScriptHandler {
	void runScript(EnumScriptType var1, Event var2);

	boolean isClient();

	boolean getEnabled();

	void setEnabled(boolean var1);

	String getLanguage();

	void setLanguage(String var1);

	List getScripts();

	String noticeString();

	Map getConsoleText();

	void clearConsole();
}
