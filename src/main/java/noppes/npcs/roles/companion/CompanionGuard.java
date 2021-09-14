package noppes.npcs.roles.companion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.entity.EntityNPCInterface;

public class CompanionGuard extends CompanionJobInterface {
	public boolean isStanding = false;

	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("CompanionGuardStanding", this.isStanding);
		return compound;
	}

	public void setNBT(NBTTagCompound compound) {
		this.isStanding = compound.getBoolean("CompanionGuardStanding");
	}

	public boolean isEntityApplicable(Entity entity) {
		if (!(entity instanceof EntityPlayer) && !(entity instanceof EntityNPCInterface)) {
			if (entity instanceof EntityCreeper) {
				return false;
			} else {
				return entity instanceof IMob;
			}
		} else {
			return false;
		}
	}

	public boolean isSelfSufficient() {
		return this.isStanding;
	}
}
