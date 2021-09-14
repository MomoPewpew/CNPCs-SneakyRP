package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNPCFurryMale extends EntityNPCInterface {
	public EntityNPCFurryMale(World world) {
		super(world);
		this.display.setSkinTexture("customnpcs:textures/entity/furrymale/WolfGrey.png");
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
			ModelPartData ears = data.getOrCreatePart(EnumParts.EARS);
			ears.type = 0;
			ears.color = 6182997;
			ModelPartData snout = data.getOrCreatePart(EnumParts.SNOUT);
			snout.type = 2;
			snout.color = 6182997;
			ModelPartData tail = data.getOrCreatePart(EnumParts.TAIL);
			tail.type = 0;
			tail.color = 6182997;
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
