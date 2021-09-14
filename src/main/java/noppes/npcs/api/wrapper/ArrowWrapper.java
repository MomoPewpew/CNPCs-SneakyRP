package noppes.npcs.api.wrapper;

import net.minecraft.entity.projectile.EntityArrow;
import noppes.npcs.api.entity.IArrow;

public class ArrowWrapper extends EntityWrapper implements IArrow {
	public ArrowWrapper(EntityArrow entity) {
		super(entity);
	}

	public int getType() {
		return 4;
	}

	public boolean typeOf(int type) {
		return type == 4 ? true : super.typeOf(type);
	}
}
