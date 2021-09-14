package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCDwarfMale extends EntityNPCInterface {
	public EntityNPCDwarfMale(World world) {
		super(world);
		this.scaleX = this.scaleZ = 0.85F;
		this.scaleY = 0.6875F;
		this.display.setSkinTexture("customnpcs:textures/entity/dwarfmale/Simon.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1F, 0.7F, 0.9F);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9F, 0.7F);
			data.getPartConfig(EnumParts.BODY).setScale(1.2F, 0.7F, 1.5F);
			data.getPartConfig(EnumParts.HEAD).setScale(0.85F, 0.85F);
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
