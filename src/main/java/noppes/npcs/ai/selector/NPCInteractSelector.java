package noppes.npcs.ai.selector;

import com.google.common.base.Predicate;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCInteractSelector implements Predicate {
	private EntityNPCInterface npc;

	public NPCInteractSelector(EntityNPCInterface npc) {
		this.npc = npc;
	}

	public boolean isEntityApplicable(EntityNPCInterface entity) {
		if (entity != this.npc && this.npc.isEntityAlive()) {
			return !entity.isAttacking() && !this.npc.getFaction().isAggressiveToNpc(entity)
					&& this.npc.ais.stopAndInteract;
		} else {
			return false;
		}
	}

	public boolean apply(Object ob) {
		return !(ob instanceof EntityNPCInterface) ? false : this.isEntityApplicable((EntityNPCInterface) ob);
	}
}
