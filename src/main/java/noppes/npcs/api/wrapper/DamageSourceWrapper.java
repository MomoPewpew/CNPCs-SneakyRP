package noppes.npcs.api.wrapper;

import net.minecraft.util.DamageSource;
import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;

public class DamageSourceWrapper implements IDamageSource {
     private DamageSource source;

     public DamageSourceWrapper(DamageSource source) {
          this.source = source;
     }

     public String getType() {
          return this.source.getDamageType();
     }

     public boolean isUnblockable() {
          return this.source.isUnblockable();
     }

     public boolean isProjectile() {
          return this.source.isProjectile();
     }

     public DamageSource getMCDamageSource() {
          return this.source;
     }

     public IEntity getTrueSource() {
          return NpcAPI.Instance().getIEntity(this.source.getTrueSource());
     }

     public IEntity getImmediateSource() {
          return NpcAPI.Instance().getIEntity(this.source.getImmediateSource());
     }
}
