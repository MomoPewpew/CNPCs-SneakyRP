package noppes.npcs.api.gui;

public interface ILabel extends ICustomGuiComponent {
	String getText();

	ILabel setText(String var1);

	int getWidth();

	int getHeight();

	ILabel setSize(int var1, int var2);

	int getColor();

	ILabel setColor(int var1);

	float getScale();

	ILabel setScale(float var1);
}
