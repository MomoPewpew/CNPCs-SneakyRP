package noppes.npcs.api.wrapper;

import net.minecraft.entity.monster.EntityMob;
import noppes.npcs.api.entity.IMonster;

public class MonsterWrapper extends EntityLivingWrapper implements IMonster {
	public MonsterWrapper(EntityMob entity) {
		super(entity);
	}

	public int getType() {
		return 3;
	}

	public boolean typeOf(int type) {
		return type == 3 ? true : super.typeOf(type);
	}
}
