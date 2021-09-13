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
          return this.source.func_76355_l();
     }

     public boolean isUnblockable() {
          return this.source.func_76363_c();
     }

     public boolean isProjectile() {
          return this.source.func_76352_a();
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
