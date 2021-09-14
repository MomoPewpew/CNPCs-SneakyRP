package noppes.npcs.roles;

import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.data.role.IJobFollower;
import noppes.npcs.entity.EntityNPCInterface;

public class JobFollower extends JobInterface implements IJobFollower {
	public EntityNPCInterface following = null;
	private int ticks = 40;
	private int range = 20;
	public String name = "";

	public JobFollower(EntityNPCInterface npc) {
		super(npc);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setString("FollowingEntityName", this.name);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.name = compound.getString("FollowingEntityName");
	}

	public boolean aiShouldExecute() {
		if (this.npc.isAttacking()) {
			return false;
		} else {
			--this.ticks;
			if (this.ticks > 0) {
				return false;
			} else {
				this.ticks = 10;
				this.following = null;
				List list = this.npc.world.getEntitiesWithinAABB(EntityNPCInterface.class,
						this.npc.getEntityBoundingBox().grow((double) this.getRange(), (double) this.getRange(),
								(double) this.getRange()));
				Iterator var2 = list.iterator();

				while (var2.hasNext()) {
					EntityNPCInterface entity = (EntityNPCInterface) var2.next();
					if (entity != this.npc && !entity.isKilled()
							&& entity.display.getName().equalsIgnoreCase(this.name)) {
						this.following = entity;
						break;
					}
				}

				return false;
			}
		}
	}

	private int getRange() {
		return this.range > CustomNpcs.NpcNavRange ? CustomNpcs.NpcNavRange : this.range;
	}

	public boolean isFollowing() {
		return this.following != null;
	}

	public void reset() {
	}

	public void resetTask() {
		this.following = null;
	}

	public boolean hasOwner() {
		return !this.name.isEmpty();
	}

	public String getFollowing() {
		return this.name;
	}

	public void setFollowing(String name) {
		this.name = name;
	}

	public ICustomNpc getFollowingNpc() {
		return this.following == null ? null : this.following.wrappedNPC;
	}
}
