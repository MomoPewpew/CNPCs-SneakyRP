package noppes.npcs.api.wrapper;

import noppes.npcs.api.IPos;
import noppes.npcs.api.IRayTrace;
import noppes.npcs.api.block.IBlock;

public class RayTraceWrapper implements IRayTrace {
     private final IBlock block;
     private final int sideHit;
     private final IPos pos;

     public RayTraceWrapper(IBlock block, int sideHit) {
          this.block = block;
          this.sideHit = sideHit;
          this.pos = block.getPos();
     }

     public IPos getPos() {
          return this.block.getPos();
     }

     public IBlock getBlock() {
          return this.block;
     }

     public int getSideHit() {
          return this.sideHit;
     }
}
