package noppes.npcs.client;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import noppes.npcs.CustomNpcs;
import noppes.npcs.IChatMessages;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;

public class RenderChatMessages implements IChatMessages {
	private Map messages = new TreeMap();
	private int boxLength = 46;
	private float scale = 0.5F;
	private String lastMessage = "";
	private long lastMessageTime = 0L;

	public void addMessage(String message, EntityNPCInterface npc) {
		if (CustomNpcs.EnableChatBubbles) {
			long time = System.currentTimeMillis();
			if (!message.equals(this.lastMessage) || this.lastMessageTime + 5000L <= time) {
				Map messages = new TreeMap(this.messages);
				messages.put(time, new TextBlockClient(message, this.boxLength * 4, true,
						new Object[] { Minecraft.getMinecraft().player, npc }));
				if (messages.size() > 3) {
					messages.remove(messages.keySet().iterator().next());
				}

				this.messages = messages;
				this.lastMessage = message;
				this.lastMessageTime = time;
			}
		}
	}

	public void renderMessages(double par3, double par5, double par7, float textscale, boolean inRange) {
		Map messages = this.getMessages();
		if (!messages.isEmpty()) {
			if (inRange) {
				this.render(par3, par5, par7, textscale, false);
			}

			this.render(par3, par5, par7, textscale, true);
		}
	}

	private void render(double par3, double par5, double par7, float textscale, boolean depth) {
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		float var13 = 1.6F;
		float var14 = 0.016666668F * var13;
		GlStateManager.pushMatrix();
		int size = 0;

		TextBlockClient block;
		for (Iterator var131 = this.messages.values().iterator(); var131.hasNext(); size += block.lines.size()) {
			block = (TextBlockClient) var131.next();
		}

		Minecraft mc = Minecraft.getMinecraft();
		int textYSize = (int) ((float) (size * font.FONT_HEIGHT) * this.scale);
		GlStateManager.translate((float) par3 + 0.0F, (float) par5 + (float) textYSize * textscale * var14,
				(float) par7);
		GlStateManager.scale(textscale, textscale, textscale);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-var14, -var14, var14);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.depthMask(true);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		if (depth) {
			GlStateManager.enableDepth();
		} else {
			GlStateManager.disableDepth();
		}

		int black = depth ? -16777216 : 1426063360;
		int white = depth ? -1140850689 : 1157627903;
		GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE,
				DestFactor.ZERO);
		GlStateManager.disableTexture2D();
		GlStateManager.enableCull();
		this.drawRect(-this.boxLength - 2, -2, this.boxLength + 2, textYSize + 1, white, 0.11D);
		this.drawRect(-this.boxLength - 1, -3, this.boxLength + 1, -2, black, 0.1D);
		this.drawRect(-this.boxLength - 1, textYSize + 2, -1, textYSize + 1, black, 0.1D);
		this.drawRect(3, textYSize + 2, this.boxLength + 1, textYSize + 1, black, 0.1D);
		this.drawRect(-this.boxLength - 3, -1, -this.boxLength - 2, textYSize, black, 0.1D);
		this.drawRect(this.boxLength + 3, -1, this.boxLength + 2, textYSize, black, 0.1D);
		this.drawRect(-this.boxLength - 2, -2, -this.boxLength - 1, -1, black, 0.1D);
		this.drawRect(this.boxLength + 2, -2, this.boxLength + 1, -1, black, 0.1D);
		this.drawRect(-this.boxLength - 2, textYSize + 1, -this.boxLength - 1, textYSize, black, 0.1D);
		this.drawRect(this.boxLength + 2, textYSize + 1, this.boxLength + 1, textYSize, black, 0.1D);
		this.drawRect(0, textYSize + 1, 3, textYSize + 4, white, 0.11D);
		this.drawRect(-1, textYSize + 4, 1, textYSize + 5, white, 0.11D);
		this.drawRect(-1, textYSize + 1, 0, textYSize + 4, black, 0.1D);
		this.drawRect(3, textYSize + 1, 4, textYSize + 3, black, 0.1D);
		this.drawRect(2, textYSize + 3, 3, textYSize + 4, black, 0.1D);
		this.drawRect(1, textYSize + 4, 2, textYSize + 5, black, 0.1D);
		this.drawRect(-2, textYSize + 4, -1, textYSize + 5, black, 0.1D);
		this.drawRect(-2, textYSize + 5, 1, textYSize + 6, black, 0.1D);
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GlStateManager.scale(this.scale, this.scale, this.scale);
		int index = 0;
		Iterator var18 = this.messages.values().iterator();

		while (var18.hasNext()) {
			TextBlockClient block1 = (TextBlockClient) var18.next();

			for (Iterator var20 = block1.lines.iterator(); var20.hasNext(); ++index) {
				ITextComponent chat = (ITextComponent) var20.next();
				String message = chat.getFormattedText();
				font.drawString(message, -font.getStringWidth(message) / 2, index * font.FONT_HEIGHT, black);
			}
		}

		GlStateManager.disableCull();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}

	private void drawRect(int par0, int par1, int par2, int par3, int par4, double par5) {
		int j1;
		if (par0 < par2) {
			j1 = par0;
			par0 = par2;
			par2 = j1;
		}

		if (par1 < par3) {
			j1 = par1;
			par1 = par3;
			par3 = j1;
		}

		float f = (float) (par4 >> 24 & 255) / 255.0F;
		float f1 = (float) (par4 >> 16 & 255) / 255.0F;
		float f2 = (float) (par4 >> 8 & 255) / 255.0F;
		float f3 = (float) (par4 & 255) / 255.0F;
		BufferBuilder tessellator = Tessellator.getInstance().getBuffer();
		GlStateManager.color(f1, f2, f3, f);
		tessellator.begin(7, DefaultVertexFormats.POSITION);
		tessellator.pos((double) par0, (double) par3, par5).endVertex();
		tessellator.pos((double) par2, (double) par3, par5).endVertex();
		tessellator.pos((double) par2, (double) par1, par5).endVertex();
		tessellator.pos((double) par0, (double) par1, par5).endVertex();
		Tessellator.getInstance().draw();
	}

	private Map getMessages() {
		Map messages = new TreeMap();
		long time = System.currentTimeMillis();
		Iterator var4 = this.messages.entrySet().iterator();

		while (var4.hasNext()) {
			Entry entry = (Entry) var4.next();
			if (time <= (Long) entry.getKey() + 10000L) {
				messages.put(entry.getKey(), entry.getValue());
			}
		}

		return this.messages = messages;
	}
}
