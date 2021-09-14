package noppes.npcs;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import noppes.npcs.constants.EnumParts;

public class ModelDataShared {
	protected ModelPartConfig arm1 = new ModelPartConfig();
	protected ModelPartConfig arm2 = new ModelPartConfig();
	protected ModelPartConfig body = new ModelPartConfig();
	protected ModelPartConfig leg1 = new ModelPartConfig();
	protected ModelPartConfig leg2 = new ModelPartConfig();
	protected ModelPartConfig head = new ModelPartConfig();
	protected ModelPartData legParts = new ModelPartData("legs");
	public ModelEyeData eyes = new ModelEyeData();
	public Class entityClass;
	protected EntityLivingBase entity;
	public NBTTagCompound extra = new NBTTagCompound();
	protected HashMap parts = new HashMap();

	public NBTTagCompound writeToNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		if (this.entityClass != null) {
			compound.setString("EntityClass", this.entityClass.getCanonicalName());
		}

		compound.setTag("ArmsConfig", this.arm1.writeToNBT());
		compound.setTag("BodyConfig", this.body.writeToNBT());
		compound.setTag("LegsConfig", this.leg1.writeToNBT());
		compound.setTag("HeadConfig", this.head.writeToNBT());
		compound.setTag("LegParts", this.legParts.writeToNBT());
		compound.setTag("Eyes", this.eyes.writeToNBT());
		compound.setTag("ExtraData", this.extra);
		NBTTagList list = new NBTTagList();
		Iterator var3 = this.parts.keySet().iterator();

		while (var3.hasNext()) {
			EnumParts e = (EnumParts) var3.next();
			NBTTagCompound item = ((ModelPartData) this.parts.get(e)).writeToNBT();
			item.setString("PartName", e.name);
			list.appendTag(item);
		}

		compound.setTag("Parts", list);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) {
		this.setEntityName(compound.getString("EntityClass"));
		this.arm1.readFromNBT(compound.getCompoundTag("ArmsConfig"));
		this.body.readFromNBT(compound.getCompoundTag("BodyConfig"));
		this.leg1.readFromNBT(compound.getCompoundTag("LegsConfig"));
		this.head.readFromNBT(compound.getCompoundTag("HeadConfig"));
		this.legParts.readFromNBT(compound.getCompoundTag("LegParts"));
		this.eyes.readFromNBT(compound.getCompoundTag("Eyes"));
		this.extra = compound.getCompoundTag("ExtraData");
		HashMap parts = new HashMap();
		NBTTagList list = compound.getTagList("Parts", 10);

		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound item = list.getCompoundTagAt(i);
			String name = item.getString("PartName");
			ModelPartData part = new ModelPartData(name);
			part.readFromNBT(item);
			EnumParts e = EnumParts.FromName(name);
			if (e != null) {
				parts.put(e, part);
			}
		}

		this.parts = parts;
		this.updateTransate();
	}

	private void updateTransate() {
		EnumParts[] var1 = EnumParts.values();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			EnumParts part = var1[var3];
			ModelPartConfig config = this.getPartConfig(part);
			if (config != null) {
				if (part == EnumParts.HEAD) {
					config.setTranslate(0.0F, this.getBodyY(), 0.0F);
				} else {
					ModelPartConfig leg;
					float x;
					float y;
					if (part == EnumParts.ARM_LEFT) {
						leg = this.getPartConfig(EnumParts.BODY);
						x = (1.0F - leg.scaleX) * 0.25F + (1.0F - config.scaleX) * 0.075F;
						y = this.getBodyY() + (1.0F - config.scaleY) * -0.1F;
						config.setTranslate(-x, y, 0.0F);
						if (!config.notShared) {
							ModelPartConfig arm = this.getPartConfig(EnumParts.ARM_RIGHT);
							arm.copyValues(config);
						}
					} else if (part == EnumParts.ARM_RIGHT) {
						leg = this.getPartConfig(EnumParts.BODY);
						x = (1.0F - leg.scaleX) * 0.25F + (1.0F - config.scaleX) * 0.075F;
						y = this.getBodyY() + (1.0F - config.scaleY) * -0.1F;
						config.setTranslate(x, y, 0.0F);
					} else if (part == EnumParts.LEG_LEFT) {
						config.setTranslate(config.scaleX * 0.125F - 0.113F, this.getLegsY(), 0.0F);
						if (!config.notShared) {
							leg = this.getPartConfig(EnumParts.LEG_RIGHT);
							leg.copyValues(config);
						}
					} else if (part == EnumParts.LEG_RIGHT) {
						config.setTranslate((1.0F - config.scaleX) * 0.125F, this.getLegsY(), 0.0F);
					} else if (part == EnumParts.BODY) {
						config.setTranslate(0.0F, this.getBodyY(), 0.0F);
					}
				}
			}
		}

	}

	public void setEntityName(String string) {
		this.entityClass = null;
		this.entity = null;
		Iterator var2 = ForgeRegistries.ENTITIES.getValues().iterator();

		while (var2.hasNext()) {
			EntityEntry ent = (EntityEntry) var2.next();
			Class c = ent.getEntityClass();
			if (c.getCanonicalName().equals(string) && EntityLivingBase.class.isAssignableFrom(c)) {
				this.entityClass = c.asSubclass(EntityLivingBase.class);
				break;
			}
		}

	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
		this.entity = null;
		this.extra = new NBTTagCompound();
	}

	public Class getEntityClass() {
		return this.entityClass;
	}

	public float offsetY() {
		return this.entity == null ? -this.getBodyY() : this.entity.height - 1.8F;
	}

	public void clearEntity() {
		this.entity = null;
	}

	public ModelPartData getPartData(EnumParts type) {
		if (type == EnumParts.LEGS) {
			return this.legParts;
		} else {
			return (ModelPartData) (type == EnumParts.EYES ? this.eyes : (ModelPartData) this.parts.get(type));
		}
	}

	public ModelPartConfig getPartConfig(EnumParts type) {
		if (type == EnumParts.BODY) {
			return this.body;
		} else if (type == EnumParts.ARM_LEFT) {
			return this.arm1;
		} else if (type == EnumParts.ARM_RIGHT) {
			return this.arm2;
		} else if (type == EnumParts.LEG_LEFT) {
			return this.leg1;
		} else {
			return type == EnumParts.LEG_RIGHT ? this.leg2 : this.head;
		}
	}

	public void removePart(EnumParts type) {
		this.parts.remove(type);
	}

	public ModelPartData getOrCreatePart(EnumParts type) {
		if (type == null) {
			return null;
		} else if (type == EnumParts.EYES) {
			return this.eyes;
		} else {
			ModelPartData part = this.getPartData(type);
			if (part == null) {
				this.parts.put(type, part = new ModelPartData(type.name));
			}

			return part;
		}
	}

	public float getBodyY() {
		return (1.0F - this.body.scaleY) * 0.75F + this.getLegsY();
	}

	public float getLegsY() {
		ModelPartConfig legs = this.leg1;
		if (this.leg2.notShared && this.leg2.scaleY > this.leg1.scaleY) {
			legs = this.leg2;
		}

		return (1.0F - legs.scaleY) * 0.75F;
	}
}
