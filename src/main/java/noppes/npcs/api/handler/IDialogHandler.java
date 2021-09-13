package noppes.npcs.api.handler;

import java.util.List;
import noppes.npcs.api.handler.data.IDialog;

public interface IDialogHandler {
     List categories();

     IDialog get(int var1);
}
