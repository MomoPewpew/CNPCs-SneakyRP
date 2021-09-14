package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ICustomScrollListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobFollower;

public class GuiNpcFollowerJob extends GuiNPCInterface2 implements ICustomScrollListener {
	private JobFollower job;
	private GuiCustomScroll scroll;

	public GuiNpcFollowerJob(EntityNPCInterface npc) {
		super(npc);
		this.job = (JobFollower) npc.jobInterface;
	}

	public void initGui() {
		super.initGui();
		this.addLabel(new GuiNpcLabel(1, "gui.name", this.guiLeft + 6, this.guiTop + 110));
		this.addTextField(new GuiNpcTextField(1, this, this.fontRenderer, this.guiLeft + 50, this.guiTop + 105, 200, 20,
				this.job.name));
		this.scroll = new GuiCustomScroll(this, 0);
		this.scroll.setSize(143, 208);
		this.scroll.guiLeft = this.guiLeft + 268;
		this.scroll.guiTop = this.guiTop + 4;
		this.addScroll(this.scroll);
		List names = new ArrayList();
		List list = this.npc.world.getEntitiesWithinAABB(EntityNPCInterface.class,
				this.npc.getEntityBoundingBox().grow(40.0D, 40.0D, 40.0D));
		Iterator var3 = list.iterator();

		while (var3.hasNext()) {
			EntityNPCInterface npc = (EntityNPCInterface) var3.next();
			if (npc != this.npc && !names.contains(npc.display.getName())) {
				names.add(npc.display.getName());
			}
		}

		this.scroll.setList(names);
	}

	public void save() {
		this.job.name = this.getTextField(1).getText();
		Client.sendData(EnumPacketServer.JobSave, this.job.writeToNBT(new NBTTagCompound()));
	}

	public void scrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		this.getTextField(1).setText(guiCustomScroll.getSelected());
	}

	public void scrollDoubleClicked(String selection, GuiCustomScroll scroll) {
	}
}
