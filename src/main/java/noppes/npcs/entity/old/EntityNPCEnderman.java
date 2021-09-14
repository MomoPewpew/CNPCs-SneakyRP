package noppes.npcs.entity.old;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.entity.EntityCustomNpc;

public class EntityNPCEnderman extends EntityNpcEnderchibi {
	public EntityNPCEnderman(World world) {
		super(world);
		this.display.setSkinTexture("customnpcs:textures/entity/enderman/enderman.png");
		this.display.setOverlayTexture("customnpcs:textures/overlays/ender_eyes.png");
		this.width = 0.6F;
		this.height = 2.9F;
	}

	public void updateHitbox() {
		if (this.currentAnimation == 2) {
			this.width = this.height = 0.2F;
		} else if (this.currentAnimation == 1) {
			this.width = 0.6F;
			this.height = 2.3F;
		} else {
			this.width = 0.6F;
			this.height = 2.9F;
		}

		this.width = this.width / 5.0F * (float) this.display.getSize();
		this.height = this.height / 5.0F * (float) this.display.getSize();
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
			data.setEntityClass(EntityEnderman.class);
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
