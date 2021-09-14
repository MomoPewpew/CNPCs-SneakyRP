package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCOrcFemale extends EntityNPCInterface {
	public EntityNPCOrcFemale(World world) {
		super(world);
		this.scaleX = this.scaleY = this.scaleZ = 0.9375F;
		this.display.setSkinTexture("customnpcs:textures/entity/orcfemale/StrandedFemaleOrc.png");
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
			data.getOrCreatePart(EnumParts.BREASTS).type = 2;
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1F, 1.0F);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.1F, 1.0F);
			data.getPartConfig(EnumParts.BODY).setScale(1.1F, 1.0F, 1.25F);
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
