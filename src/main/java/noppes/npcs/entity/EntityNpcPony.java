package noppes.npcs.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.ModelData;

public class EntityNpcPony extends EntityNPCInterface {
	public boolean isPegasus = false;
	public boolean isUnicorn = false;
	public boolean isFlying = false;
	public ResourceLocation checked = null;

	public EntityNpcPony(World world) {
		super(world);
		this.display.setSkinTexture("customnpcs:textures/entity/ponies/MineLP Derpy Hooves.png");
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
			data.setEntityClass(EntityNpcPony.class);
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
