package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;

public class GuiNpcQuestReward extends GuiContainerNPCInterface implements ITextfieldListener {
	private Quest quest;
	private ResourceLocation resource;

	public GuiNpcQuestReward(EntityNPCInterface npc, ContainerNpcQuestReward container) {
		super(npc, container);
		this.quest = NoppesUtilServer.getEditingQuest(this.player);
		this.resource = this.getResource("questreward.png");
	}

	public void initGui() {
		super.initGui();
		this.addLabel(new GuiNpcLabel(0, "quest.randomitem", this.guiLeft + 4, this.guiTop + 4));
		this.addButton(new GuiNpcButton(0, this.guiLeft + 4, this.guiTop + 14, 60, 20,
				new String[] { "gui.no", "gui.yes" }, this.quest.randomReward ? 1 : 0));
		this.addButton(new GuiNpcButton(5, this.guiLeft, this.guiTop + this.ySize, 98, 20, "gui.back"));
		this.addLabel(new GuiNpcLabel(1, "quest.exp", this.guiLeft + 4, this.guiTop + 45));
		this.addTextField(new GuiNpcTextField(0, this, this.fontRenderer, this.guiLeft + 4, this.guiTop + 55, 60, 20,
				this.quest.rewardExp + ""));
		this.getTextField(0).numbersOnly = true;
		this.getTextField(0).setMinMaxDefault(0, 99999, 0);
	}

	public void actionPerformed(GuiButton guibutton) {
		int id = guibutton.id;
		if (id == 5) {
			NoppesUtil.openGUI(this.player, GuiNPCManageQuest.Instance);
		}

		if (id == 0) {
			this.quest.randomReward = ((GuiNpcButton) guibutton).getValue() == 1;
		}

	}

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(this.resource);
		int l = (this.width - this.xSize) / 2;
		int i1 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);
		super.drawGuiContainerBackgroundLayer(f, i, j);
	}

	public void save() {
	}

	public void unFocused(GuiNpcTextField textfield) {
		this.quest.rewardExp = textfield.getInteger();
	}
}
