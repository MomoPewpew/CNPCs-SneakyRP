package noppes.npcs.api.gui;

public interface ITexturedButton extends IButton {
	String getTexture();

	ITexturedButton setTexture(String var1);

	int getTextureX();

	int getTextureY();

	ITexturedButton setTextureOffset(int var1, int var2);
}
