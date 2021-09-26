package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IPos;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.wrapper.BlockPosWrapper;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBuilder;
import noppes.npcs.roles.JobFarmer;

public class DataAI implements INPCAi {
	private EntityNPCInterface npc;
	public int onAttack = 0;
	public int doorInteract = 2;
	public int findShelter = 2;
	public boolean canSwim = true;
	public boolean reactsToFire = false;
	public boolean avoidsWater = false;
	public boolean avoidsSun = false;
	public boolean returnToStart = true;
	public boolean directLOS = true;
	public boolean canLeap = false;
	public boolean canSprint = false;
	public boolean stopAndInteract = true;
	public boolean attackInvisible = false;
	public int tacticalVariant = 0;
	private int tacticalRadius = 8;
	public int movementType = 0;
	public int animationType = 0;
	private int standingType = 0;
	private int movingType = 0;
	public boolean npcInteracting = true;
	public int orientation = 0;
	public float bodyOffsetX = 5.0F;
	public float bodyOffsetY = 5.0F;
	public float bodyOffsetZ = 5.0F;
	public int walkingRange = 10;
	private int moveSpeed = 5;
	private List movingPath = new ArrayList();
	private BlockPos startPos = null;
	public int movingPos = 0;
	public int movingPattern = 0;
	public boolean movingPause = true;

	public DataAI(EntityNPCInterface npc) {
		this.npc = npc;
	}

	public void readToNBT(NBTTagCompound compound) {
		this.canSwim = compound.getBoolean("CanSwim");
		this.reactsToFire = compound.getBoolean("ReactsToFire");
		this.setAvoidsWater(compound.getBoolean("AvoidsWater"));
		this.avoidsSun = compound.getBoolean("AvoidsSun");
		this.returnToStart = compound.getBoolean("ReturnToStart");
		this.onAttack = compound.getInteger("OnAttack");
		this.doorInteract = compound.getInteger("DoorInteract");
		this.findShelter = compound.getInteger("FindShelter");
		this.directLOS = compound.getBoolean("DirectLOS");
		this.canLeap = compound.getBoolean("CanLeap");
		this.canSprint = compound.getBoolean("CanSprint");
		this.tacticalRadius = compound.getInteger("TacticalRadius");
		this.movingPause = compound.getBoolean("MovingPause");
		this.npcInteracting = compound.getBoolean("npcInteracting");
		this.stopAndInteract = compound.getBoolean("stopAndInteract");
		this.movementType = compound.getInteger("MovementType");
		this.animationType = compound.getInteger("MoveState");
		this.standingType = compound.getInteger("StandingState");
		this.movingType = compound.getInteger("MovingState");
		this.tacticalVariant = compound.getInteger("TacticalVariant");
		this.orientation = compound.getInteger("Orientation");
		this.bodyOffsetY = compound.getFloat("PositionOffsetY");
		this.bodyOffsetZ = compound.getFloat("PositionOffsetZ");
		this.bodyOffsetX = compound.getFloat("PositionOffsetX");
		this.walkingRange = compound.getInteger("WalkingRange");
		this.setWalkingSpeed(compound.getInteger("MoveSpeed"));
		this.setMovingPath(NBTTags.getIntegerArraySet(compound.getTagList("MovingPathNew", 10)));
		this.movingPos = compound.getInteger("MovingPos");
		this.movingPattern = compound.getInteger("MovingPatern");
		this.attackInvisible = compound.getBoolean("AttackInvisible");
		if (compound.hasKey("StartPosNew")) {
			int[] startPos = compound.getIntArray("StartPosNew");
			this.startPos = new BlockPos(startPos[0], startPos[1], startPos[2]);
		}

	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("CanSwim", this.canSwim);
		compound.setBoolean("ReactsToFire", this.reactsToFire);
		compound.setBoolean("AvoidsWater", this.avoidsWater);
		compound.setBoolean("AvoidsSun", this.avoidsSun);
		compound.setBoolean("ReturnToStart", this.returnToStart);
		compound.setInteger("OnAttack", this.onAttack);
		compound.setInteger("DoorInteract", this.doorInteract);
		compound.setInteger("FindShelter", this.findShelter);
		compound.setBoolean("DirectLOS", this.directLOS);
		compound.setBoolean("CanLeap", this.canLeap);
		compound.setBoolean("CanSprint", this.canSprint);
		compound.setInteger("TacticalRadius", this.tacticalRadius);
		compound.setBoolean("MovingPause", this.movingPause);
		compound.setBoolean("npcInteracting", this.npcInteracting);
		compound.setBoolean("stopAndInteract", this.stopAndInteract);
		compound.setInteger("MoveState", this.animationType);
		compound.setInteger("StandingState", this.standingType);
		compound.setInteger("MovingState", this.movingType);
		compound.setInteger("TacticalVariant", this.tacticalVariant);
		compound.setInteger("MovementType", this.movementType);
		compound.setInteger("Orientation", this.orientation);
		compound.setFloat("PositionOffsetX", this.bodyOffsetX);
		compound.setFloat("PositionOffsetY", this.bodyOffsetY);
		compound.setFloat("PositionOffsetZ", this.bodyOffsetZ);
		compound.setInteger("WalkingRange", this.walkingRange);
		compound.setInteger("MoveSpeed", this.moveSpeed);
		compound.setTag("MovingPathNew", NBTTags.nbtIntegerArraySet(this.movingPath));
		compound.setInteger("MovingPos", this.movingPos);
		compound.setInteger("MovingPatern", this.movingPattern);
		this.setAvoidsWater(this.avoidsWater);
		compound.setIntArray("StartPosNew", this.getStartArray());
		compound.setBoolean("AttackInvisible", this.attackInvisible);
		return compound;
	}

	public List getMovingPath() {
		if (this.movingPath.isEmpty() && this.startPos != null) {
			this.movingPath.add(this.getStartArray());
		}

		return this.movingPath;
	}

	public void setMovingPath(List list) {
		this.movingPath = list;
		if (!this.movingPath.isEmpty()) {
			int[] startPos = (int[]) this.movingPath.get(0);
			this.startPos = new BlockPos(startPos[0], startPos[1], startPos[2]);
		}

	}

	public BlockPos startPos() {
		if (this.startPos == null) {
			this.startPos = new BlockPos(this.npc);
		}

		return this.startPos;
	}

	public int[] getStartArray() {
		BlockPos pos = this.startPos();
		return new int[] { pos.getX(), pos.getY(), pos.getZ() };
	}

	public int[] getCurrentMovingPath() {
		List list = this.getMovingPath();
		int size = list.size();
		if (size == 1) {
			return (int[]) list.get(0);
		} else {
			int pos = this.movingPos;
			if (this.movingPattern == 0 && pos >= size) {
				pos = this.movingPos = 0;
			}

			if (this.movingPattern == 1) {
				int size2 = size * 2 - 1;
				if (pos >= size2) {
					pos = this.movingPos = 0;
				} else if (pos >= size) {
					pos = size2 - pos;
				}
			}

			return (int[]) list.get(pos);
		}
	}

	public void clearMovingPath() {
		this.movingPath.clear();
		this.movingPos = 0;
	}

	public void setMovingPathPos(int m_pos, int[] pos) {
		if (m_pos < 0) {
			m_pos = 0;
		}

		this.movingPath.set(m_pos, pos);
	}

	public int[] getMovingPathPos(int m_pos) {
		return (int[]) this.movingPath.get(m_pos);
	}

	public void appendMovingPath(int[] pos) {
		this.movingPath.add(pos);
	}

	public int getMovingPos() {
		return this.movingPos;
	}

	public void setMovingPos(int pos) {
		this.movingPos = pos;
	}

	public int getMovingPathSize() {
		return this.movingPath.size();
	}

	public void incrementMovingPath() {
		List list = this.getMovingPath();
		if (list.size() == 1) {
			this.movingPos = 0;
		} else {
			++this.movingPos;
			if (this.movingPattern == 0) {
				this.movingPos %= list.size();
			} else if (this.movingPattern == 1) {
				int size = list.size() * 2 - 1;
				this.movingPos %= size;
			}

		}
	}

	public void decreaseMovingPath() {
		List list = this.getMovingPath();
		if (list.size() == 1) {
			this.movingPos = 0;
		} else {
			--this.movingPos;
			if (this.movingPos < 0) {
				if (this.movingPattern == 0) {
					this.movingPos = list.size() - 1;
				} else if (this.movingPattern == 1) {
					this.movingPos = list.size() * 2 - 2;
				}
			}

		}
	}

	public double getDistanceSqToPathPoint() {
		int[] pos = this.getCurrentMovingPath();
		return this.npc.getDistanceSq((double) pos[0] + 0.5D, (double) pos[1], (double) pos[2] + 0.5D);
	}

	public IPos getStartPos() {
		return new BlockPosWrapper(this.startPos());
	}

	public void setStartPos(BlockPos pos) {
		this.startPos = pos;
	}

	public void setStartPos(IPos pos) {
		this.startPos = pos.getMCBlockPos();
	}

	public void setStartPos(double x, double y, double z) {
		this.startPos = new BlockPos(x, y, z);
	}

	public void setReturnsHome(boolean bo) {
		this.returnToStart = bo;
	}

	public boolean getReturnsHome() {
		return this.returnToStart;
	}

	public boolean shouldReturnHome() {
		if (this.npc.advanced.job == 10 && ((JobBuilder) this.npc.jobInterface).isBuilding()) {
			return false;
		} else {
			return this.npc.advanced.job == 11 && ((JobFarmer) this.npc.jobInterface).isPlucking() ? false
					: this.returnToStart;
		}
	}

	public int getAnimation() {
		return this.animationType;
	}

	public int getCurrentAnimation() {
		return this.npc.currentAnimation;
	}

	public void setAnimation(int type) {
		this.animationType = type;
	}

	public int getRetaliateType() {
		return this.onAttack;
	}

	public void setRetaliateType(int type) {
		if (type >= 0 && type <= 3) {
			this.onAttack = type;
			this.npc.updateAI = true;
		} else {
			throw new CustomNPCsException("Unknown retaliation type: " + type, new Object[0]);
		}
	}

	public int getMovingType() {
		return this.movingType;
	}

	public void setMovingType(int type) {
		if (type >= 0 && type <= 2) {
			this.movingType = type;
			this.npc.updateAI = true;
		} else {
			throw new CustomNPCsException("Unknown moving type: " + type, new Object[0]);
		}
	}

	public int getStandingType() {
		return this.standingType;
	}

	public void setStandingType(int type) {
		if (type >= 0 && type <= 3) {
			this.standingType = type;
			this.npc.updateAI = true;
		} else {
			throw new CustomNPCsException("Unknown standing type: " + type, new Object[0]);
		}
	}

	public boolean getAttackInvisible() {
		return this.attackInvisible;
	}

	public void setAttackInvisible(boolean attack) {
		this.attackInvisible = attack;
	}

	public int getWanderingRange() {
		return this.walkingRange;
	}

	public void setWanderingRange(int range) {
		if (range >= 1 && range <= 50) {
			this.walkingRange = range;
		} else {
			throw new CustomNPCsException("Bad wandering range: " + range, new Object[0]);
		}
	}

	public boolean getInteractWithNPCs() {
		return this.npcInteracting;
	}

	public void setInteractWithNPCs(boolean interact) {
		this.npcInteracting = interact;
	}

	public boolean getStopOnInteract() {
		return this.stopAndInteract;
	}

	public void setStopOnInteract(boolean stopOnInteract) {
		this.stopAndInteract = stopOnInteract;
	}

	public int getWalkingSpeed() {
		return this.moveSpeed;
	}

	public void setWalkingSpeed(int speed) {
		if (speed >= 0 && speed <= 10) {
			this.moveSpeed = speed;
			this.npc.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)
					.setBaseValue((double) this.npc.getSpeed());
			this.npc.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED)
					.setBaseValue((double) (this.npc.getSpeed() * 2.0F));
		} else {
			throw new CustomNPCsException("Wrong speed: " + speed, new Object[0]);
		}
	}

	public int getMovingPathType() {
		return this.movingPattern;
	}

	public boolean getMovingPathPauses() {
		return this.movingPause;
	}

	public void setMovingPathType(int type, boolean pauses) {
		if (type < 0 && type > 1) {
			throw new CustomNPCsException("Moving path type: " + type, new Object[0]);
		} else {
			this.movingPattern = type;
			this.movingPause = pauses;
		}
	}

	public int getDoorInteract() {
		return this.doorInteract;
	}

	public void setDoorInteract(int type) {
		this.doorInteract = type;
		this.npc.updateAI = true;
	}

	public boolean getCanSwim() {
		return this.canSwim;
	}

	public void setCanSwim(boolean canSwim) {
		this.canSwim = canSwim;
	}

	public int getSheltersFrom() {
		return this.findShelter;
	}

	public void setSheltersFrom(int type) {
		this.findShelter = type;
		this.npc.updateAI = true;
	}

	public boolean getAttackLOS() {
		return this.directLOS;
	}

	public void setAttackLOS(boolean enabled) {
		this.directLOS = enabled;
		this.npc.updateAI = true;
	}

	public boolean getAvoidsWater() {
		return this.avoidsWater;
	}

	public void setAvoidsWater(boolean enabled) {
		if (this.npc.getNavigator() instanceof PathNavigateGround) {
			this.npc.setPathPriority(PathNodeType.WATER, enabled ? PathNodeType.WATER.getPriority() : 0.0F);
		}

		this.avoidsWater = enabled;
	}

	public boolean getLeapAtTarget() {
		return this.canLeap;
	}

	public void setLeapAtTarget(boolean leap) {
		this.canLeap = leap;
		this.npc.updateAI = true;
	}

	public int getTacticalType() {
		return this.tacticalVariant;
	}

	public void setTacticalType(int type) {
		this.tacticalVariant = type;
		this.npc.updateAI = true;
	}

	public int getTacticalRange() {
		return this.tacticalRadius;
	}

	public void setTacticalRange(int range) {
		this.tacticalRadius = range;
	}

	public int getNavigationType() {
		return this.movementType;
	}

	public void setNavigationType(int type) {
		this.movementType = type;
	}
}
