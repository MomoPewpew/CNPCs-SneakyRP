package noppes.npcs.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ai.selector.NPCInteractSelector;
import noppes.npcs.constants.AiMutex;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;

public class EntityAIWander extends EntityAIBase {
     private EntityNPCInterface entity;
     public final NPCInteractSelector selector;
     private double x;
     private double y;
     private double zPosition;
     private EntityNPCInterface nearbyNPC;

     public EntityAIWander(EntityNPCInterface npc) {
          this.entity = npc;
          this.func_75248_a(AiMutex.PASSIVE);
          this.selector = new NPCInteractSelector(npc);
     }

     public boolean func_75250_a() {
          if (this.entity.func_70654_ax() >= 100 || !this.entity.func_70661_as().func_75500_f() || this.entity.isInteracting() || this.entity.func_184218_aH() || this.entity.ais.movingPause && this.entity.func_70681_au().nextInt(80) != 0) {
               return false;
          } else {
               if (this.entity.ais.npcInteracting && this.entity.func_70681_au().nextInt(this.entity.ais.movingPause ? 6 : 16) == 1) {
                    this.nearbyNPC = this.getNearbyNPC();
               }

               if (this.nearbyNPC != null) {
                    this.x = (double)MathHelper.func_76128_c(this.nearbyNPC.field_70165_t);
                    this.y = (double)MathHelper.func_76128_c(this.nearbyNPC.field_70163_u);
                    this.zPosition = (double)MathHelper.func_76128_c(this.nearbyNPC.field_70161_v);
                    this.nearbyNPC.addInteract(this.entity);
               } else {
                    Vec3d vec = this.getVec();
                    if (vec == null) {
                         return false;
                    }

                    this.x = vec.field_72450_a;
                    this.y = vec.field_72448_b;
                    if (this.entity.ais.movementType == 1) {
                         this.y = this.entity.getStartYPos() + (double)this.entity.func_70681_au().nextFloat() * 0.75D * (double)this.entity.ais.walkingRange;
                    }

                    this.zPosition = vec.field_72449_c;
               }

               return true;
          }
     }

     public void func_75246_d() {
          if (this.nearbyNPC != null) {
               this.nearbyNPC.func_70661_as().func_75499_g();
          }

     }

     private EntityNPCInterface getNearbyNPC() {
          List list = this.entity.world.func_175674_a(this.entity, this.entity.func_174813_aQ().func_72314_b((double)this.entity.ais.walkingRange, this.entity.ais.walkingRange > 7 ? 7.0D : (double)this.entity.ais.walkingRange, (double)this.entity.ais.walkingRange), this.selector);
          Iterator ita = list.iterator();

          while(true) {
               EntityNPCInterface npc;
               do {
                    if (!ita.hasNext()) {
                         if (list.isEmpty()) {
                              return null;
                         }

                         return (EntityNPCInterface)list.get(this.entity.func_70681_au().nextInt(list.size()));
                    }

                    npc = (EntityNPCInterface)ita.next();
               } while(npc.ais.stopAndInteract && !npc.isAttacking() && npc.func_70089_S() && !this.entity.faction.isAggressiveToNpc(npc));

               ita.remove();
          }
     }

     private Vec3d getVec() {
          if (this.entity.ais.walkingRange > 0) {
               BlockPos start = new BlockPos((double)this.entity.getStartXPos(), this.entity.getStartYPos(), (double)this.entity.getStartZPos());
               int distance = (int)MathHelper.func_76133_a(this.entity.func_174818_b(start));
               int range = this.entity.ais.walkingRange - distance;
               if (range > CustomNpcs.NpcNavRange) {
                    range = CustomNpcs.NpcNavRange;
               }

               if (range < 3) {
                    range = this.entity.ais.walkingRange;
                    if (range > CustomNpcs.NpcNavRange) {
                         range = CustomNpcs.NpcNavRange;
                    }

                    Vec3d pos2 = new Vec3d((this.entity.field_70165_t + (double)start.func_177958_n()) / 2.0D, (this.entity.field_70163_u + (double)start.func_177956_o()) / 2.0D, (this.entity.field_70161_v + (double)start.func_177952_p()) / 2.0D);
                    return RandomPositionGenerator.func_75464_a(this.entity, distance / 2, distance / 2 > 7 ? 7 : distance / 2, pos2);
               } else {
                    return RandomPositionGenerator.func_75463_a(this.entity, range / 2, range / 2 > 7 ? 7 : range / 2);
               }
          } else {
               return RandomPositionGenerator.func_75463_a(this.entity, CustomNpcs.NpcNavRange, 7);
          }
     }

     public boolean func_75253_b() {
          if (this.nearbyNPC != null && (!this.selector.apply(this.nearbyNPC) || this.entity.isInRange(this.nearbyNPC, (double)this.entity.field_70130_N))) {
               return false;
          } else {
               return !this.entity.func_70661_as().func_75500_f() && this.entity.func_70089_S() && !this.entity.isInteracting();
          }
     }

     public void func_75249_e() {
          this.entity.func_70661_as().func_75492_a(this.x, this.y, this.zPosition, 1.0D);
     }

     public void func_75251_c() {
          if (this.nearbyNPC != null && this.entity.isInRange(this.nearbyNPC, 3.5D)) {
               EntityNPCInterface talk = this.entity;
               if (this.entity.func_70681_au().nextBoolean()) {
                    talk = this.nearbyNPC;
               }

               Line line = talk.advanced.getNPCInteractLine();
               if (line == null) {
                    line = new Line(".........");
               }

               line.setShowText(false);
               talk.saySurrounding(line);
               this.entity.addInteract(this.nearbyNPC);
               this.nearbyNPC.addInteract(this.entity);
          }

          this.nearbyNPC = null;
     }
}
