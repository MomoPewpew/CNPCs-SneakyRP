package noppes.npcs.api.gui;

public interface IScroll extends ICustomGuiComponent {
     int getWidth();

     int getHeight();

     IScroll setSize(int var1, int var2);

     String[] getList();

     IScroll setList(String[] var1);

     int getDefaultSelection();

     IScroll setDefaultSelection(int var1);

     boolean isMultiSelect();

     IScroll setMultiSelect(boolean var1);
}
