package noppes.npcs.api.wrapper;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import noppes.npcs.api.entity.IVillager;

public class VillagerWrapper extends EntityLivingWrapper implements IVillager {
	public VillagerWrapper(EntityVillager entity) {
		super(entity);
	}

	public int getProfession() {
		return ((EntityVillager) this.entity).getProfession();
	}

	public String getCareer() {
		return ((EntityVillager) this.entity).getProfessionForge().getCareer(((EntityVillager) this.entity).getProfession())
				.getName();
	}

	public int getType() {
		return 9;
	}

	public boolean typeOf(int type) {
		return type == 9 ? true : super.typeOf(type);
	}

	@Override
	public EntityMob getMCEntity() {
		// TODO Auto-generated method stub
		return null;
	}
}
