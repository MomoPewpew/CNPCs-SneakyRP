package noppes.npcs;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.Lines;
import noppes.npcs.entity.EntityNPCInterface;

public class VersionCompatibility {
     public static int ModRev = 18;

     public static void CheckNpcCompatibility(EntityNPCInterface npc, NBTTagCompound compound) {
          if (npc.npcVersion != ModRev) {
               if (npc.npcVersion < 12) {
                    CompatabilityFix(compound, npc.advanced.writeToNBT(new NBTTagCompound()));
                    CompatabilityFix(compound, npc.ais.writeToNBT(new NBTTagCompound()));
                    CompatabilityFix(compound, npc.stats.writeToNBT(new NBTTagCompound()));
                    CompatabilityFix(compound, npc.display.writeToNBT(new NBTTagCompound()));
                    CompatabilityFix(compound, npc.inventory.writeEntityToNBT(new NBTTagCompound()));
               }

               if (npc.npcVersion < 5) {
                    String texture = compound.func_74779_i("Texture");
                    texture = texture.replace("/mob/customnpcs/", "customnpcs:textures/entity/");
                    texture = texture.replace("/mob/", "customnpcs:textures/entity/");
                    compound.func_74778_a("Texture", texture);
               }

               int i;
               int i;
               if (npc.npcVersion < 6 && compound.func_74781_a("NpcInteractLines") instanceof NBTTagList) {
                    List interactLines = NBTTags.getStringList(compound.func_150295_c("NpcInteractLines", 10));
                    Lines lines = new Lines();

                    for(i = 0; i < interactLines.size(); ++i) {
                         Line line = new Line();
                         line.setText((String)interactLines.toArray()[i]);
                         lines.lines.put(i, line);
                    }

                    compound.func_74782_a("NpcInteractLines", lines.writeToNBT());
                    List worldLines = NBTTags.getStringList(compound.func_150295_c("NpcLines", 10));
                    lines = new Lines();

                    for(i = 0; i < worldLines.size(); ++i) {
                         Line line = new Line();
                         line.setText((String)worldLines.toArray()[i]);
                         lines.lines.put(i, line);
                    }

                    compound.func_74782_a("NpcLines", lines.writeToNBT());
                    List attackLines = NBTTags.getStringList(compound.func_150295_c("NpcAttackLines", 10));
                    lines = new Lines();

                    for(int i = 0; i < attackLines.size(); ++i) {
                         Line line = new Line();
                         line.setText((String)attackLines.toArray()[i]);
                         lines.lines.put(i, line);
                    }

                    compound.func_74782_a("NpcAttackLines", lines.writeToNBT());
                    List killedLines = NBTTags.getStringList(compound.func_150295_c("NpcKilledLines", 10));
                    lines = new Lines();

                    for(int i = 0; i < killedLines.size(); ++i) {
                         Line line = new Line();
                         line.setText((String)killedLines.toArray()[i]);
                         lines.lines.put(i, line);
                    }

                    compound.func_74782_a("NpcKilledLines", lines.writeToNBT());
               }

               NBTTagList list;
               if (npc.npcVersion == 12) {
                    list = compound.func_150295_c("StartPos", 3);
                    if (list.func_74745_c() == 3) {
                         int z = ((NBTTagInt)list.func_74744_a(2)).func_150287_d();
                         i = ((NBTTagInt)list.func_74744_a(1)).func_150287_d();
                         i = ((NBTTagInt)list.func_74744_a(0)).func_150287_d();
                         compound.func_74783_a("StartPosNew", new int[]{i, i, z});
                    }
               }

               if (npc.npcVersion == 13) {
                    boolean bo = compound.func_74767_n("HealthRegen");
                    compound.func_74768_a("HealthRegen", bo ? 1 : 0);
                    NBTTagCompound comp = compound.func_74775_l("TransformStats");
                    bo = comp.func_74767_n("HealthRegen");
                    comp.func_74768_a("HealthRegen", bo ? 1 : 0);
                    compound.func_74782_a("TransformStats", comp);
               }

               if (npc.npcVersion == 15) {
                    list = compound.func_150295_c("ScriptsContainers", 10);
                    if (list.func_74745_c() > 0) {
                         ScriptContainer script = new ScriptContainer(npc.script);

                         for(i = 0; i < list.func_74745_c(); ++i) {
                              NBTTagCompound scriptOld = list.func_150305_b(i);
                              EnumScriptType type = EnumScriptType.values()[scriptOld.func_74762_e("Type")];
                              script.script = script.script + "\nfunction " + type.function + "(event) {\n" + scriptOld.func_74779_i("Script") + "\n}";
                              Iterator var23 = NBTTags.getStringList(compound.func_150295_c("ScriptList", 10)).iterator();

                              while(var23.hasNext()) {
                                   String s = (String)var23.next();
                                   if (!script.scripts.contains(s)) {
                                        script.scripts.add(s);
                                   }
                              }
                         }
                    }

                    if (compound.func_74767_n("CanDespawn")) {
                         compound.func_74768_a("SpawnCycle", 4);
                    }

                    if (compound.func_74762_e("RangeAndMelee") <= 0) {
                         compound.func_74768_a("DistanceToMelee", 0);
                    }
               }

               if (npc.npcVersion == 16) {
                    compound.func_74778_a("HitSound", "random.bowhit");
                    compound.func_74778_a("GroundSound", "random.break");
               }

               if (npc.npcVersion == 17) {
                    if (compound.func_74779_i("NpcHurtSound").equals("minecraft:game.player.hurt")) {
                         compound.func_74778_a("NpcHurtSound", "minecraft:entity.player.hurt");
                    }

                    if (compound.func_74779_i("NpcDeathSound").equals("minecraft:game.player.hurt")) {
                         compound.func_74778_a("NpcDeathSound", "minecraft:entity.player.hurt");
                    }

                    if (compound.func_74779_i("FiringSound").equals("random.bow")) {
                         compound.func_74778_a("FiringSound", "minecraft:entity.arrow.shoot");
                    }

                    if (compound.func_74779_i("HitSound").equals("random.bowhit")) {
                         compound.func_74778_a("HitSound", "minecraft:entity.arrow.hit");
                    }

                    if (compound.func_74779_i("GroundSound").equals("random.break")) {
                         compound.func_74778_a("GroundSound", "minecraft:block.stone.break");
                    }
               }

               npc.npcVersion = ModRev;
          }
     }

     public static void CheckAvailabilityCompatibility(ICompatibilty compatibilty, NBTTagCompound compound) {
          if (compatibilty.getVersion() != ModRev) {
               CompatabilityFix(compound, compatibilty.writeToNBT(new NBTTagCompound()));
               compatibilty.setVersion(ModRev);
          }
     }

     private static void CompatabilityFix(NBTTagCompound compound, NBTTagCompound check) {
          Collection tags = check.func_150296_c();
          Iterator var3 = tags.iterator();

          while(var3.hasNext()) {
               String name = (String)var3.next();
               NBTBase nbt = check.func_74781_a(name);
               if (!compound.func_74764_b(name)) {
                    compound.func_74782_a(name, nbt);
               } else if (nbt instanceof NBTTagCompound && compound.func_74781_a(name) instanceof NBTTagCompound) {
                    CompatabilityFix(compound.func_74775_l(name), (NBTTagCompound)nbt);
               }
          }

     }
}
