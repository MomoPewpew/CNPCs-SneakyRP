package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCOrcMale extends EntityNPCInterface {
	public EntityNPCOrcMale(World world) {
		super(world);
		this.scaleY = 1.0F;
		this.scaleX = this.scaleZ = 1.2F;
		this.display.setSkinTexture("customnpcs:textures/entity/orcmale/StrandedOrc.png");
	}

	public void onUpdate() {
		this.isDead = true;
		this.setNoAI(true);
		if (!this.world.isRemote) {
			NBTTagCompound compound = new NBTTagCompound();
			this.writeToNBT(compound);
			EntityCustomNpc npc = new EntityCustomNpc(this.world);
			npc.readFromNBT(compound);
			ModelData data = npc.modelData;
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.2F, 1.05F);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.2F, 1.05F);
			data.getPartConfig(EnumParts.BODY).setScale(1.4F, 1.1F, 1.5F);
			data.getPartConfig(EnumParts.HEAD).setScale(1.2F, 1.1F);
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
