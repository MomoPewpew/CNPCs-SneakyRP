package noppes.npcs.api.item;

public interface IItemScripted extends IItemStack {
     boolean hasTexture(int var1);

     String getTexture(int var1);

     void setTexture(int var1, String var2);

     void setMaxStackSize(int var1);

     double getDurabilityValue();

     void setDurabilityValue(float var1);

     boolean getDurabilityShow();

     void setDurabilityShow(boolean var1);

     int getDurabilityColor();

     void setDurabilityColor(int var1);

     int getColor();

     void setColor(int var1);
}
