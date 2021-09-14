package noppes.npcs.client.gui.swing;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.minecraft.client.Minecraft;
import noppes.npcs.client.gui.util.IJTextAreaListener;
import org.lwjgl.opengl.Display;

public class GuiJTextArea extends JDialog implements WindowListener {
	public IJTextAreaListener listener;
	private JTextArea area;

	public GuiJTextArea(String text) {
		this.setDefaultCloseOperation(2);
		this.setSize(Display.getWidth() - 40, Display.getHeight() - 40);
		this.setLocation(Display.getX() + 20, Display.getY() + 20);
		JScrollPane scroll = new JScrollPane(this.area = new JTextArea(text));
		scroll.setVerticalScrollBarPolicy(22);
		this.add(scroll);
		this.addWindowListener(this);
		this.setVisible(true);
	}

	public GuiJTextArea setListener(IJTextAreaListener listener) {
		this.listener = listener;
		return this;
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
		if (this.listener != null) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				this.listener.saveText(this.area.getText());
			});
		}
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
