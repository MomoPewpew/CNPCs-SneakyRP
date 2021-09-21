package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

public class VersionChecker extends Thread {
	public void run() {
		String name = '\u00A7'+ "2CustomNpcs" + '\u00A7' + "f";
		String link = '\u00A7'+"9"+'\u00A7' + "nClick here";
		String text = name + " installed. For more info " + link;

		EntityPlayerSP player;
		try {
			player = Minecraft.getMinecraft().player;
		} catch (NoSuchMethodError var7) {
			return;
		}

		while ((player = Minecraft.getMinecraft().player) == null) {
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException var6) {
				var6.printStackTrace();
			}
		}

		TextComponentTranslation message = new TextComponentTranslation(text, new Object[0]);
		message.getStyle()
				.setClickEvent(new ClickEvent(Action.OPEN_URL, "http://www.kodevelopment.nl/minecraft/customnpcs/"));
		player.sendMessage(message);
	}
}
