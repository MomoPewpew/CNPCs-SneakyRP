package noppes.npcs.client.gui.player.companion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.CustomNpcResourceListener;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleCompanion;
import org.lwjgl.input.Mouse;

public class GuiNpcCompanionTalents extends GuiNPCInterface {
	private RoleCompanion role;
	private Map talents = new HashMap();
	private GuiNpcButton selected;
	private long lastPressedTime = 0L;
	private long startPressedTime = 0L;

	public GuiNpcCompanionTalents(EntityNPCInterface npc) {
		super(npc);
		this.role = (RoleCompanion) npc.roleInterface;
		this.closeOnEsc = true;
		this.setBackground("companion_empty.png");
		this.xSize = 171;
		this.ySize = 166;
	}

	public void initGui() {
		super.initGui();
		this.talents = new HashMap();
		int y = this.guiTop + 12;
		this.addLabel(
				new GuiNpcLabel(0, NoppesStringUtils.translate("quest.exp", ": "), this.guiLeft + 4, this.guiTop + 10));
		GuiNpcCompanionStats.addTopMenu(this.role, this, 2);
		int i = 0;
		Iterator var3 = this.role.talents.keySet().iterator();

		while (var3.hasNext()) {
			EnumCompanionTalent e = (EnumCompanionTalent) var3.next();
			this.addTalent(i++, e);
		}

	}

	private void addTalent(int i, EnumCompanionTalent talent) {
		int y = this.guiTop + 28 + i / 2 * 26;
		int x = this.guiLeft + 4 + i % 2 * 84;
		GuiNpcCompanionTalents.GuiTalent gui = new GuiNpcCompanionTalents.GuiTalent(this.role, talent, x, y);
		gui.setWorldAndResolution(this.mc, this.width, this.height);
		this.talents.put(i, gui);
		if (this.role.getTalentLevel(talent) < 5) {
			this.addButton(new GuiNpcButton(i + 10, x + 26, y, 14, 14, "+"));
			y += 8;
		}

		this.addLabel(new GuiNpcLabel(i, this.role.talents.get(talent) + "/" + this.role.getNextLevel(talent), x + 26,
				y + 8));
	}

	public void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		int id = guibutton.id;
		if (id == 1) {
			CustomNpcs.proxy.openGui(this.npc, EnumGuiType.Companion);
		}

		if (id == 3) {
			NoppesUtilPlayer.sendData(EnumPlayerPacket.CompanionOpenInv);
		}

		if (id >= 10) {
			this.selected = (GuiNpcButton) guibutton;
			this.lastPressedTime = this.startPressedTime = this.mc.world.getWorldTime();
			this.addExperience(1);
		}

	}

	private void addExperience(int exp) {
		EnumCompanionTalent talent = ((GuiNpcCompanionTalents.GuiTalent) this.talents
				.get(this.selected.id - 10)).talent;
		if (this.role.canAddExp(-exp) || this.role.currentExp > 0) {
			if (exp > this.role.currentExp) {
				exp = this.role.currentExp;
			}

			NoppesUtilPlayer.sendData(EnumPlayerPacket.CompanionTalentExp, talent.ordinal(), exp);
			this.role.talents.put(talent, (Integer) this.role.talents.get(talent) + exp);
			this.role.addExp(-exp);
			this.getLabel(this.selected.id - 10).label = this.role.talents.get(talent) + "/"
					+ this.role.getNextLevel(talent);
		}
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		if (this.selected != null && this.mc.world.getWorldTime() - this.startPressedTime > 4L
				&& this.lastPressedTime < this.mc.world.getWorldTime() && this.mc.world.getWorldTime() % 4L == 0L) {
			if (this.selected.mousePressed(this.mc, i, j) && Mouse.isButtonDown(0)) {
				this.lastPressedTime = this.mc.world.getWorldTime();
				if (this.lastPressedTime - this.startPressedTime < 20L) {
					this.addExperience(1);
				} else if (this.lastPressedTime - this.startPressedTime < 40L) {
					this.addExperience(2);
				} else if (this.lastPressedTime - this.startPressedTime < 60L) {
					this.addExperience(4);
				} else if (this.lastPressedTime - this.startPressedTime < 90L) {
					this.addExperience(8);
				} else if (this.lastPressedTime - this.startPressedTime < 140L) {
					this.addExperience(14);
				} else {
					this.addExperience(28);
				}
			} else {
				this.lastPressedTime = 0L;
				this.selected = null;
			}
		}

		this.mc.getTextureManager().bindTexture(Gui.ICONS);
		this.drawTexturedModalRect(this.guiLeft + 4, this.guiTop + 20, 10, 64, 162, 5);
		if (this.role.currentExp > 0) {
			float v = 1.0F * (float) this.role.currentExp / (float) this.role.getMaxExp();
			if (v > 1.0F) {
				v = 1.0F;
			}

			this.drawTexturedModalRect(this.guiLeft + 4, this.guiTop + 20, 10, 69, (int) (v * 162.0F), 5);
		}

		String s = this.role.currentExp + "\\" + this.role.getMaxExp();
		this.mc.fontRenderer.drawString(s, this.guiLeft + this.xSize / 2 - this.mc.fontRenderer.getStringWidth(s) / 2,
				this.guiTop + 10, CustomNpcResourceListener.DefaultTextColor);
		Iterator var5 = this.talents.values().iterator();

		while (var5.hasNext()) {
			GuiNpcCompanionTalents.GuiTalent talent = (GuiNpcCompanionTalents.GuiTalent) var5.next();
			talent.drawScreen(i, j, f);
		}

	}

	public void save() {
	}

	public static class GuiTalent extends GuiScreen {
		private EnumCompanionTalent talent;
		private int x;
		private int y;
		private RoleCompanion role;
		private static final ResourceLocation resource = new ResourceLocation("customnpcs:textures/gui/talent.png");

		public GuiTalent(RoleCompanion role, EnumCompanionTalent talent, int x, int y) {
			this.talent = talent;
			this.x = x;
			this.y = y;
			this.role = role;
		}

		public void drawScreen(int i, int j, float f) {
			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(resource);
			ItemStack item = this.talent.item;
			if (item.getItem() == null) {
				item = new ItemStack(Blocks.DIRT);
			}

			GlStateManager.pushMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			boolean hover = this.x < i && this.x + 24 > i && this.y < j && this.y + 24 > j;
			this.drawTexturedModalRect(this.x, this.y, 0, hover ? 24 : 0, 24, 24);
			this.zLevel = 100.0F;
			this.itemRender.zLevel = 100.0F;
			GlStateManager.enableLighting();
			GlStateManager.enableRescaleNormal();
			RenderHelper.enableGUIStandardItemLighting();
			this.itemRender.renderItemAndEffectIntoGUI(item, this.x + 4, this.y + 4);
			this.itemRender.renderItemOverlays(mc.fontRenderer, item, this.x + 4, this.y + 4);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.translate(0.0F, 0.0F, 200.0F);
			this.drawCenteredString(mc.fontRenderer, this.role.getTalentLevel(this.talent) + "", this.x + 20,
					this.y + 16, 16777215);
			this.itemRender.zLevel = 0.0F;
			this.zLevel = 0.0F;
			GlStateManager.popMatrix();
		}
	}
}
