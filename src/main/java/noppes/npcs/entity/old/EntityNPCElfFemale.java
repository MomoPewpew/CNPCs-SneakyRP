package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCElfFemale extends EntityNPCInterface {
	public EntityNPCElfFemale(World world) {
		super(world);
		this.display.setSkinTexture("customnpcs:textures/entity/elffemale/ElfFemale.png");
		this.scaleX = 0.8F;
		this.scaleY = 1.0F;
		this.scaleZ = 0.8F;
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.8F, 1.05F);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 1.05F);
			data.getPartConfig(EnumParts.BODY).setScale(0.8F, 1.05F);
			data.getPartConfig(EnumParts.HEAD).setScale(0.8F, 0.85F);
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
