package noppes.npcs.api.entity.data;

public interface INPCAdvanced {
     void setLine(int var1, int var2, String var3, String var4);

     String getLine(int var1, int var2);

     int getLineCount(int var1);

     String getSound(int var1);

     void setSound(int var1, String var2);
}
