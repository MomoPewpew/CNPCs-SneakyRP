package noppes.npcs.api.handler.data;

import java.util.List;

public interface IDialogCategory {
     List dialogs();

     String getName();

     IDialog create();
}
