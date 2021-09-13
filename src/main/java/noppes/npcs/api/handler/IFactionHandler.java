package noppes.npcs.api.handler;

import java.util.List;
import noppes.npcs.api.handler.data.IFaction;

public interface IFactionHandler {
     List list();

     IFaction delete(int var1);

     IFaction create(String var1, int var2);

     IFaction get(int var1);
}
