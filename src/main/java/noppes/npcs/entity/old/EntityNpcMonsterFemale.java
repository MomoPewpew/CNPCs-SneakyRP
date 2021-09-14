package noppes.npcs.entity.old;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityNpcMonsterFemale extends EntityNPCInterface {
	public EntityNpcMonsterFemale(World world) {
		super(world);
		this.scaleX = this.scaleY = this.scaleZ = 0.9075F;
		this.display.setSkinTexture("customnpcs:textures/entity/monsterfemale/ZombieStephanie.png");
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
			data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92F, 0.92F);
			data.getPartConfig(EnumParts.HEAD).setScale(0.95F, 0.95F);
			data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.92F);
			data.getPartConfig(EnumParts.BODY).setScale(0.92F, 0.92F);
			npc.ais.animationType = 3;
			this.world.spawnEntity(npc);
		}

		super.onUpdate();
	}
}
